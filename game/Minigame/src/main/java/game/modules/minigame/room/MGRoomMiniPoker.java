package game.modules.minigame.room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.cardlib.models.Card;
import com.vinplay.cardlib.models.GroupType;
import com.vinplay.cardlib.utils.CardLibUtils;
import com.vinplay.dal.service.BroadcastMessageService;
import com.vinplay.dal.service.CacheService;
import com.vinplay.dal.service.MiniGameService;
import com.vinplay.dal.service.MiniPokerService;
import com.vinplay.dal.service.SlotMachineService;
import com.vinplay.dal.service.impl.BroadcastMessageServiceImpl;
import com.vinplay.dal.service.impl.CacheServiceImpl;
import com.vinplay.dal.service.impl.MiniGameServiceImpl;
import com.vinplay.dal.service.impl.MiniPokerServiceImpl;
import com.vinplay.dal.service.impl.SlotMachineServiceImpl;
import com.vinplay.operator_maxium.MaxiumWinConfig;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;
import com.vinplay.vbee.common.utils.DateTimeUtils;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import game.GameConfig.GameConfig;
import game.modules.Minipoker.DealCardMinipoker;
import game.modules.description.SlotDescription.SlotDescriptionUtils;
import game.modules.minigame.cmd.send.minipoker.ForceStopAuatoPlayMiniPokerMsg;
import game.modules.minigame.cmd.send.minipoker.ResultMiniPokerMsg;
import game.modules.minigame.cmd.send.minipoker.UpdatePotMiniPokerMsg;
import game.modules.minigame.config.MiniPokerRate;
import game.modules.minigame.config.MinipokerResultData;
import game.modules.minigame.entities.AutoUserMiniPoker;
import game.modules.minigame.entities.MinigameConstant;
import game.modules.minigame.utils.GenerationMiniPoker;
import game.utils.ConfigGame;

public class MGRoomMiniPoker
        extends MGRoom {
    private float tax = MinigameConstant.MINIGAME_TAX_VIN;
    public long pot;
    public long fund;
    private short moneyType;
    private String moneyTypeStr;
    private long betValue = 0L;
    private long initPotValue = 500000L;
    private UserService userService = new UserServiceImpl();
    private MiniPokerService mpService = new MiniPokerServiceImpl();
    private MiniGameService mgService = new MiniGameServiceImpl();
    private SlotMachineService slotMachineService = new SlotMachineServiceImpl();
    private BroadcastMessageService broadcastMsgService = new BroadcastMessageServiceImpl();
    private GenerationMiniPoker gen = new GenerationMiniPoker();
    private Map<String, AutoUserMiniPoker> usersAuto = new HashMap<String, AutoUserMiniPoker>();
    private final Runnable gameLoopTask = new GameLoopTask();
    private long lastTimeUpdatePotToRoom = 0L;
    private long lastTimeUpdateFundToRoom = 0L;
    private ThreadPoolExecutor executor;
    private int countHu = -1;
    private int countNoHuX2 = 0;
    protected CacheService sv = new CacheServiceImpl();
    private String gameID = Games.MINI_POKER.getId() + "";

    public long fundJackPot;
    private String fundJackPotName;

    public MGRoomMiniPoker(String roomName, short moneyType, long pot, long fund, long baseBetValue, long initPotValue, String fundJackPotName, long fundJackPot) {
        super(roomName);
        this.gameName = Games.MINI_POKER.getName();
        this.moneyType = moneyType;
        if (moneyType == 1) {
            this.moneyTypeStr = "vin";
            this.tax = MinigameConstant.MINIGAME_TAX_VIN;
        } else if (moneyType == 0) {
            this.moneyTypeStr = "xu";
            this.tax = MinigameConstant.MINIGAME_TAX_XU;
        }
        this.executor = moneyType == 1 ? (ThreadPoolExecutor) Executors.newFixedThreadPool(ConfigGame.getIntValue("mini_poker_thread_pool_per_room_vin")) : (ThreadPoolExecutor) Executors.newFixedThreadPool(ConfigGame.getIntValue("mini_poker_thread_pool_per_room_xu"));
        this.pot = pot;
        CacheServiceImpl cacheService = new CacheServiceImpl();
        cacheService.setValue(this.name, (int) pot);
        this.fund = fund;
        this.betValue = baseBetValue;
        this.initPotValue = initPotValue;
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.gameLoopTask, 10, 1, TimeUnit.SECONDS);
        try {
            this.countHu = this.sv.getValueInt(this.name + "_count_hu");
            this.countNoHuX2 = this.sv.getValueInt(this.name + "_count_no_hu_x2");
            this.calculatHuX2();
        } catch (KeyNotFoundException keyNotFoundException) {
            // empty catch block
        }
        try {
            this.mgService.savePot(this.name, pot, this.isMultiJackpot());
        } catch (IOException | InterruptedException | TimeoutException exception) {
            // empty catch block
        }
        this.fundJackPotName = fundJackPotName;
        this.fundJackPot = fundJackPot;
    }

    public boolean isMultiJackpot() {
        return GameConfig.getInstance().slotMultiJackpotConfig.isMultiJackpot(this.gameName);
    }

    public short play(User user) {
        return this.play(user, this.betValue);
    }

    public short play(User user, long betValue) {
        ResultMiniPokerMsg msg = this.play(user.getName(), betValue);
        this.sendMessageToUser((BaseMsg) msg, user);
        return msg.result;
    }

    public synchronized void addMoneyToPot(long money) {
        this.pot += money;

//        Debug.trace("addMoneyToPot  " + money, "pot  " + this.pot);
        this.savePot();
    }

    public void botEatJackpot(String keyBot, long nextTime, String userName) {
        String currentTimeStr = DateTimeUtils.getCurrentTime();
        int result = 1;
        long totalPrizes = this.pot;
        if (this.isMultiJackpot()) {
            totalPrizes = this.pot * GameConfig.getInstance().slot3x3GameConfig.MULTI_JACKPOT;
        }
        try {
            this.slotMachineService.addTop(this.gameName, userName, (int)this.betValue, totalPrizes, currentTimeStr, result);
        } catch (Exception e) {

        }
        this.resetPot();
        try {
            this.mgService.saveFund(keyBot, nextTime);
        } catch (Exception e) {

        }
    }

    public void resetPot() {
        this.pot = this.initPotValue;
        this.savePot();
    }
    public MinipokerResultData playMiniPoker(List<Card> cards) {
        MinipokerResultData minipokerResultData = new MinipokerResultData();
        GroupType groupType = CardLibUtils.calculateTypePoker(cards);
        short result = 0;
        long prize = 0;
        switch (groupType) {
            case HighCard: {
                result = 11;
                break;
            }
            case OnePair: {
                if (CardLibUtils.pairEqualOrGreatJack(cards)) {
                    result = 9;
                    prize = (int) ((float) betValue * MiniPokerRate.PAIR_BIGGER_THAN_JACK);
                    break;
                }
                result = 10;
                prize = 0;
                break;
            }
            case TwoPair: {
                result = 8;
                prize = this.betValue * MiniPokerRate.TWO_PAIR;
                break;
            }
            case ThreeOfKind: {
                result = 7;
                prize = this.betValue * MiniPokerRate.THREE_OF_A_KIND;
                break;
            }
            case Straight: {
                result = 6;
                prize = this.betValue * MiniPokerRate.STRAIGHT;
                break;
            }
            case Flush: {
                result = 5;
                prize = this.betValue * MiniPokerRate.FLUSH;
                break;
            }
            case FullHouse: {
                result = 4;
                prize = this.betValue * MiniPokerRate.FULL_HOUSE;
                break;
            }
            case FourOfKind: {
                result = 3;
                prize = this.betValue * MiniPokerRate.FOUR_OF_A_KIND;
                break;
            }
            case StraightFlush: {
                if (CardLibUtils.isStraightFlushJack(cards)) {
                    result = 1;
                    minipokerResultData.isJackPot = true;
                    break;
                }
                result = 2;
                prize = this.betValue * MiniPokerRate.STRAIGHT_FLUSH;
                break;
            }
        }
        minipokerResultData.result = result;
        minipokerResultData.cards = cards;
        minipokerResultData.moneyWin = prize;

        return minipokerResultData;
    }

    public MinipokerResultData playMiniPoker(boolean isJackpot) {
        MinipokerResultData minipokerResultData = new MinipokerResultData();
//        byte[] listCard = DealCardMinipoker.rollFlush();
//        List<Card> cards = DealCardMinipoker.byteArrayToList(listCard);
        List<Card> cards = this.gen.randomCards();
        if (isJackpot) {
            cards = DealCardMinipoker.dealCardJackpotMinipoker();
        }
        GroupType groupType = CardLibUtils.calculateTypePoker(cards);
        short result = 0;
        long prize = 0;
        switch (groupType) {
            case HighCard: {
                result = 11;
                break;
            }
            case OnePair: {
                if (CardLibUtils.pairEqualOrGreatJack(cards)) {
                    result = 9;
                    prize = (int) ((float) betValue * MiniPokerRate.PAIR_BIGGER_THAN_JACK);
                    break;
                }
                result = 10;
                prize = 0;
                break;
            }
            case TwoPair: {
                result = 8;
                prize = this.betValue * MiniPokerRate.TWO_PAIR;
                break;
            }
            case ThreeOfKind: {
                result = 7;
                prize = this.betValue * MiniPokerRate.THREE_OF_A_KIND;
                break;
            }
            case Straight: {
                result = 6;
                prize = this.betValue * MiniPokerRate.STRAIGHT;
                break;
            }
            case Flush: {
                result = 5;
                prize = this.betValue * MiniPokerRate.FLUSH;
                break;
            }
            case FullHouse: {
                result = 4;
                prize = this.betValue * MiniPokerRate.FULL_HOUSE;
                break;
            }
            case FourOfKind: {
                result = 3;
                prize = this.betValue * MiniPokerRate.FOUR_OF_A_KIND;
                break;
            }
            case StraightFlush: {
                if (CardLibUtils.isStraightFlushJack(cards)) {
                    result = 1;
                    minipokerResultData.isJackPot = true;
                    break;
                }
                result = 2;
                prize = this.betValue * MiniPokerRate.STRAIGHT_FLUSH;
                break;
            }
        }
        minipokerResultData.result = result;
        minipokerResultData.cards = cards;
        minipokerResultData.moneyWin = prize;

        return minipokerResultData;
    }

    public boolean isUserBigWin(String userName) {
        return this.userService.isUserBigWin(userName);
    }

    public boolean isUserJackpot(String userName) {
        return this.slotMachineService.isSetJackpotForUser(this.gameName, userName, (int) this.betValue);
    }

    public synchronized ResultMiniPokerMsg play(String username, long betValue) {
        long lastPot = this.pot;
        long lastFund = this.fund;
        ResultMiniPokerMsg resultMiniPokerMsg = new ResultMiniPokerMsg();
        StringBuilder builder = new StringBuilder();
        short result = 0;
        long prize = 0L;
        int soLanNoHu = ConfigGame.getIntValue("mini_poker_so_lan_no_hu");
        Random rd;
        long currentMoney = this.userService.getMoneyUserCache(username, this.moneyTypeStr);
        UserCacheModel u = this.userService.getUser(username);
        long referenceId = System.currentTimeMillis();

        long moneyToPot = betValue * GameConfig.getInstance().minipokerGameConfig.rateToJackPot / 100;
        long fee = betValue * GameConfig.getInstance().minipokerGameConfig.fee / 100;
        long moneyToFundJackpot = betValue * GameConfig.getInstance().minipokerGameConfig.rateToFundJackPot / 100;
        long moneyToFund = betValue - moneyToPot - fee - moneyToFundJackpot;

        if (betValue > 0L) {
            if (currentMoney >= betValue) {
                MoneyResponse moneyRes = this.userService.updateMoney(username, -betValue, this.moneyTypeStr, Games.MINI_POKER.getName(),
                        this.gameID, SlotDescriptionUtils.getBetDescription(this.gameID)
                        , fee, Long.valueOf(referenceId), TransType.START_TRANS);
                if (moneyRes != null && moneyRes.isSuccess()) {

                    boolean isCheckUserBigWin = this.isUserBigWin(username);
                    long tienThuongX2 = 0L;
                    if(!isCheckUserBigWin){
                        this.pot += moneyToPot;
                        this.fund += moneyToFund;
                        this.fundJackPot += moneyToFundJackpot;
                    }
//                    boolean isUserJackpot = true;
                       boolean isUserJackpot = this.isUserJackpot(username);
                    MinipokerResultData minipokerResultData = this.playMiniPoker(isUserJackpot);
                    long userValue = this.userService.getUserValue(username);

                    long maxiumWin = MaxiumWinConfig.getMaxiumMoneyWin(userValue) - moneyRes.getCurrentMoney();
//                    maxiumWin = (long) 999999999;
                    Debug.trace(" get user value maxiumWin  ", userValue, maxiumWin);
                    long moneyWinJackpot = this.pot;

                    if (minipokerResultData.isJackPot) {
                        if (this.isMultiJackpot()) {
                            moneyWinJackpot = this.pot * GameConfig.getInstance().minipokerGameConfig.MULTI_JACKPOT;
                            long moneyMinusFund = this.pot * GameConfig.getInstance().minipokerGameConfig.MULTI_JACKPOT - (this.pot - this.initPotValue);
                            if ( (moneyWinJackpot > maxiumWin) ||(this.fundJackPot < moneyMinusFund && !isUserJackpot)) {
                                minipokerResultData = DealCardMinipoker.playLose();
                            } else {
                                minipokerResultData.result = 12;
                                minipokerResultData.moneyWin = this.pot * GameConfig.getInstance().minipokerGameConfig.MULTI_JACKPOT;
                                if(!isCheckUserBigWin){
                                    this.fundJackPot -= minipokerResultData.moneyWin;
                                }
                            }
                        } else {
                            if ( (moneyWinJackpot > maxiumWin) || (this.fundJackPot < this.initPotValue && !isUserJackpot)) {
                                minipokerResultData = DealCardMinipoker.playLose();
                            } else {
                                minipokerResultData.moneyWin = this.pot;
                                if(!isCheckUserBigWin){
                                    this.fundJackPot -= minipokerResultData.moneyWin;
                                }
                            }
                        }
                    } else {

                        if ((maxiumWin < minipokerResultData.moneyWin)|| (this.fund < minipokerResultData.moneyWin && !isUserJackpot)) {
                            minipokerResultData = DealCardMinipoker.playLose();
                        }
                    }

                    if(isCheckUserBigWin){
                        minipokerResultData = playMiniPoker(DealCardMinipoker.dealCardForUserBigWin());
                    }

                    result = minipokerResultData.result;
                    prize = minipokerResultData.moneyWin;
                    if (!minipokerResultData.isJackPot) {
                        if(!isCheckUserBigWin){
                            this.fund -= prize;
                        }
                    } else {

                        String currentTimeStr = DateTimeUtils.getCurrentTime();
                        try {
                            this.slotMachineService.addTop(this.gameName, username, (int)this.betValue, prize, currentTimeStr, result);
                        }catch (Exception e){

                        }

                        this.resetPot();
//                        GameUtils.sendSMSToUser(username, "Chuc mung " + username + " da no hu game MiniPoker phong " + betValue + ". So tien no hu: " + this.pot + " Vin");
                                          }
                    if (minipokerResultData.cards.size() == 5) {
                        resultMiniPokerMsg.card1 = (byte) minipokerResultData.cards.get(0).getCode();
                        resultMiniPokerMsg.card2 = (byte) minipokerResultData.cards.get(1).getCode();
                        resultMiniPokerMsg.card3 = (byte) minipokerResultData.cards.get(2).getCode();
                        resultMiniPokerMsg.card4 = (byte) minipokerResultData.cards.get(3).getCode();
                        resultMiniPokerMsg.card5 = (byte) minipokerResultData.cards.get(4).getCode();
                    }

                    long moneyAdded = prize;
                    if (result == 12) {
                        tienThuongX2 = this.pot * (GameConfig.getInstance().minipokerGameConfig.MULTI_JACKPOT - 1);
                        moneyAdded -= tienThuongX2;
                        this.userService.updateMoney(username, tienThuongX2, this.moneyTypeStr, this.gameName,
                                this.gameID, SlotDescriptionUtils.getMultiJackpotDescription(this.gameID)
                                , 0L, null, TransType.NO_VIPPOINT);
                    }
                    moneyRes = this.userService.updateMoney(username, moneyAdded, this.moneyTypeStr, Games.MINI_POKER.getName(),
                            this.gameID,
                            SlotDescriptionUtils.getPayDescription(this.gameID, betValue, moneyAdded, result),
                            0, Long.valueOf(referenceId), TransType.END_TRANS);
                    long moneyExchange = prize - betValue;
                    if (moneyRes != null && moneyRes.isSuccess()) {
                        currentMoney = moneyRes.getCurrentMoney();
                        if (this.moneyType == 1 && moneyExchange >= (long) BroadcastMessageServiceImpl.MIN_MONEY) {
                            this.broadcastMsgService.putMessage(Games.MINI_POKER.getId(), username, moneyExchange);
                        }
                    }
                    builder.append(minipokerResultData.cards.get(0).toString());
                    for (int i = 1; i < minipokerResultData.cards.size(); ++i) {
                        builder.append(",");
                        builder.append(minipokerResultData.cards.get(i).toString());
                    }
                    try {
                        if (!isBot(username)) {
                            this.mpService.logMiniPoker(username, betValue, result, prize, builder.toString(), lastPot, lastFund, (int) this.moneyType);

                        }
                    } catch (IOException | InterruptedException | TimeoutException e) {
                        Debug.trace("Log mini poker error ", e.getMessage());
                    }

                    this.saveFund();
                    this.savePot();
                }
            } else {
                result = 102;
            }
        } else {
            result = 101;
        }


        resultMiniPokerMsg.result = result;
        resultMiniPokerMsg.prize = prize;
        resultMiniPokerMsg.currentMoney = currentMoney;
//        Debug.trace("result  = " + result, "  prize = " + prize, "    currentMoney = " + currentMoney,
//                "     this.pot = " + this.pot, "   this.fund = " + this.fund, "   this.fundJackPot = " + this.fundJackPot);
        return resultMiniPokerMsg;
    }

//    public synchronized ResultMiniPokerMsg play(String username, long betValue) {
//        long lastPot = this.pot;
//        long lastFund = this.fund;
//        ResultMiniPokerMsg resultMiniPokerMsg = new ResultMiniPokerMsg();
//        StringBuilder builder = new StringBuilder();
//        short result = 0;
//        long prize = 0L;
//        int soLanNoHu = ConfigGame.getIntValue("mini_poker_so_lan_no_hu");
//        Random rd;
//        long currentMoney = this.userService.getMoneyUserCache(username, this.moneyTypeStr);
//        UserCacheModel u = this.userService.getUser(username);
//        long referenceId = System.currentTimeMillis();
//        if (this.fund >= 0L) {
//            if (betValue > 0L) {
//                if (currentMoney >= betValue) {
//                    MoneyResponse moneyRes = this.userService.updateMoney(username, -betValue, this.moneyTypeStr, Games.MINI_POKER.getName(), "Quay MiniPoker", "\u0110\u1eb7t c\u01b0\u1ee3c MiniPoker", 0L, Long.valueOf(referenceId), TransType.START_TRANS);
//                    if (moneyRes != null && moneyRes.isSuccess()) {
//                        boolean enoughToPair = false;
//                        long moneyToPot = betValue * 1L / 100L;
//                        long fee = (long)((float)betValue * this.tax / 100.0f);
//                        long moneyToFund = betValue - moneyToPot - fee;
//                        long tienThuongX2 = 0L;
//                        this.pot += moneyToPot;
//                        if(!u.isBot()){
//
//                            this.fund += moneyToFund;
//                        }
//                        block13 : while (!enoughToPair) {
//                            GroupType groupType;
//                            prize = 0L;
//                            tienThuongX2 = 0L;
//                            long moneyExchange = 0L;
//                            boolean forceNoHu = false;
//                            if ((soLanNoHu > 0) && (fund > initPotValue * 2L)) {
//                                rd = new Random();
//                                if (rd.nextInt(soLanNoHu) == 0)
//                                    forceNoHu = true;
//                            }
//                            Debug.trace(forceNoHu + ":" + fund);
//                            // // lock no hũ 11/01 kane
//                            //List<Card> cards = this.gen.randomCards2(forceNoHu);
//                            MinipokerResultData minipokerResultData = this.playMiniPoker();
//                            List<Card> cards = this.gen.randomCards2(false);
//                            if (cards.size() != 5) {
//                                cards = this.gen.randomCards();
//                            }
//                            if ((groupType = CardLibUtils.calculateTypePoker(cards)) == null) continue;
//                            switch (groupType) {
//                                case HighCard: {
//                                    result = 11;
//                                    break;
//                                }
//                                case OnePair: {
//                                    if (CardLibUtils.pairEqualOrGreatJack(cards)) {
//                                        result = 9;
//                                        prize = (int)((float)betValue * MiniPokerRate.PAIR_BIGGER_THAN_JACK);
//                                        break;
//                                    }
//                                    result = 10;
//                                    prize = 0L;
//                                    break;
//                                }
//                                case TwoPair: {
//                                    result = 8;
//                                    prize = betValue * MiniPokerRate.TWO_PAIR;
//                                    break;
//                                }
//                                case ThreeOfKind: {
//                                    result = 7;
//                                    prize = betValue * MiniPokerRate.THREE_OF_A_KIND;
//                                    break;
//                                }
//                                case Straight: {
//                                    result = 6;
//                                    prize = betValue * MiniPokerRate.STRAIGHT;
//                                    break;
//                                }
//                                case Flush: {
//                                    result = 5;
//                                    prize = betValue * MiniPokerRate.FLUSH;
//                                    break;
//                                }
//                                case FullHouse: {
//                                    result = 4;
//                                    prize = betValue * MiniPokerRate.FULL_HOUSE;
//                                    break;
//                                }
//                                case FourOfKind: {
//                                    result = 3;
//                                    prize = betValue * MiniPokerRate.FOUR_OF_A_KIND;
//                                    break;
//                                }
//                                case StraightFlush: {
//                                    if (!u.isBot()) continue block13;
//                                    if (CardLibUtils.isStraightFlushJack(cards)) {
//                                        if (!u.isBot()) continue block13;
//                                        result = 1;
//                                        if (this.huX2) {
//                                            tienThuongX2 = this.pot;
//                                            prize = this.pot * 2L;
//                                            break;
//                                        }
//                                        prize = this.pot;
//                                        break;
//                                    }
//                                    result = 2;
//                                    prize = betValue * MiniPokerRate.STRAIGHT_FLUSH;
//                                }
//                            }
//                            long fundExchange = prize > 0L ? prize : 0L;
//                            long l = fundExchange;
//                            if (result == 1 ? this.fund - this.initPotValue < 0L : this.fund - fundExchange < 0L) continue;
//                            enoughToPair = true;
//                            if (cards.size() == 5) {
//                                resultMiniPokerMsg.card1 = (byte)cards.get(0).getCode();
//                                resultMiniPokerMsg.card2 = (byte)cards.get(1).getCode();
//                                resultMiniPokerMsg.card3 = (byte)cards.get(2).getCode();
//                                resultMiniPokerMsg.card4 = (byte)cards.get(3).getCode();
//                                resultMiniPokerMsg.card5 = (byte)cards.get(4).getCode();
//                            }
//                            if (prize > 0L) {
//                                if (result == 1) {
//                                    if (this.huX2) {
//                                        result = 12;
//                                    }
//                                    this.noHuX2();
//                                    if (this.moneyType == 1) {
//                                        GameUtils.sendSMSToUser(username, "Chuc mung " + username + " da no hu game MiniPoker phong " + betValue + ". So tien no hu: " + this.pot + " Vin");
//                                    }
//                                    long oldPotValue = this.pot;
//                                    this.pot = this.initPotValue;
//                                    this.fund -= this.initPotValue;
//                                    // get usercache
//                                    HazelcastInstance client = HazelcastClientFactory.getInstance();
//                                    IMap<String, UserModel> userMap = client.getMap("users");
//                                    UserModel model = null;
//                                    String displayName = username;
//                                    if (userMap.containsKey((Object)username)) {
//                                        model = (UserModel)userMap.get((Object)displayName);
//                                        if (model.getClient() != null && model.getClient() != "")
//                                        {
//                                            displayName = "[" + model.getClient() + "] " + username;
//                                        }
//                                        else
//                                        {
//                                            displayName = "[X] " + username;
//                                        }
//                                    }
//                                    else
//                                    {
//                                        UserDaoImpl dao = new UserDaoImpl();
//                                        try {
//                                            model = dao.getUserByNickName(username);
//                                            if (model.getClient() != null && model.getClient() != "")
//                                            {
//                                                displayName = "[" + model.getClient() + "] " + username;
//                                            }
//                                            else
//                                            {
//                                                displayName = "[X] " + username;
//                                            }
//                                        } catch (SQLException ex) {
//
//                                        }
//                                    }
//
//                                } else {
//                                    this.fund -= fundExchange;
//                                }
//                            }
//                            long moneyAdded = prize;
//                            String des = "Quay MiniPoker";
//                            if (result == 12) {
//                                moneyAdded -= tienThuongX2;
//                                this.userService.updateMoney(username, tienThuongX2, this.moneyTypeStr, this.gameName, des, "Th\u1eafng X2", 0L, null, TransType.NO_VIPPOINT);
//                            }
//                            moneyRes = this.userService.updateMoney(username, moneyAdded, this.moneyTypeStr, Games.MINI_POKER.getName(), des, this.buildDescription(betValue, moneyAdded, result), fee, Long.valueOf(referenceId), TransType.END_TRANS);
//                            moneyExchange = prize - betValue;
//                            if (moneyRes != null && moneyRes.isSuccess()) {
//                                currentMoney = moneyRes.getCurrentMoney();
//                                if (this.moneyType == 1 && moneyExchange >= (long)BroadcastMessageServiceImpl.MIN_MONEY) {
//                                    this.broadcastMsgService.putMessage(Games.MINI_POKER.getId(), username, moneyExchange);
//                                }
//                            }
//                            builder.append(cards.get(0).toString());
//                            for (int i = 1; i < cards.size(); ++i) {
//                                builder.append(",");
//                                builder.append(cards.get(i).toString());
//                            }
//                            try {
//                                if(!isBot(username)){
//                                    this.mpService.logMiniPoker(username, betValue, result, prize, builder.toString(), lastPot, lastFund, (int)this.moneyType);
//
//                                }
//                                }
//                            catch (IOException | InterruptedException | TimeoutException e) {
//                                Debug.trace((Object[])new Object[]{"Log mini poker error ", e.getMessage()});
//                            }
//                        }
//                        this.saveFund();
//                        this.savePot();
//                    }
//                } else {
//                    result = 102;
//                }
//            } else {
//                result = 101;
//            }
//        } else {
//            result = 100;
//        }
//        resultMiniPokerMsg.result = result;
//        resultMiniPokerMsg.prize = prize;
//        resultMiniPokerMsg.currentMoney = currentMoney;
//        return resultMiniPokerMsg;
//    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void autoPlay(User user) {
        Map<String, AutoUserMiniPoker> map;
        Map<String, AutoUserMiniPoker> map2 = map = this.usersAuto;
        synchronized (map2) {
            if (this.usersAuto.containsKey(user.getName())) {
                AutoUserMiniPoker entry = this.usersAuto.get(user.getName());
                this.forceStopAutoPlay(entry.getUser());
            }
            this.usersAuto.put(user.getName(), new AutoUserMiniPoker(user, 0));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stopAutoPlay(User user) {
        Map<String, AutoUserMiniPoker> map;
        Map<String, AutoUserMiniPoker> map2 = map = this.usersAuto;
        synchronized (map2) {
            AutoUserMiniPoker entry;
//            if (this.usersAuto.containsKey(user.getName()) && (entry = this.usersAuto.get(user.getName())).getUser().getUniqueId() == user.getUniqueId()) {
            if (this.usersAuto.containsKey(user.getName()) && (entry = this.usersAuto.get(user.getName())).getUser().getId() == user.getId()) {
                this.usersAuto.remove(user.getName());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forceStopAutoPlay(User user) {
        Map<String, AutoUserMiniPoker> map;
        Map<String, AutoUserMiniPoker> map2 = map = this.usersAuto;
        synchronized (map2) {
            this.usersAuto.remove(user.getName());
            ForceStopAuatoPlayMiniPokerMsg msg = new ForceStopAuatoPlayMiniPokerMsg();
            this.sendMessageToUser((BaseMsg) msg, user);
        }
    }

    public boolean isBot(String username) {
        UserCacheModel model = this.userService.getUser(username);
        return model.isBot();
    }

    private boolean checkDienKienNoHu(String username) {
        try {
            UserModel u = this.userService.getUserByUserName(username);
            return u.isBot();
        } catch (Exception u) {
            return false;
        }
    }

    private void saveFund() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastTimeUpdateFundToRoom >= 60000L) {
            try {
                this.mgService.saveFund(this.name, this.fund);
                this.mgService.saveFund(this.fundJackPotName, this.fundJackPot);
            } catch (IOException | InterruptedException | TimeoutException e) {
                Debug.trace((Object[]) new Object[]{"MINI POKER: update fund poker error ", e.getMessage()});
            }
            this.lastTimeUpdateFundToRoom = currentTime;
        }
    }

    private void savePot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastTimeUpdatePotToRoom >= 3000L) {
            UpdatePotMiniPokerMsg msg = new UpdatePotMiniPokerMsg();
            msg.value = this.pot;
            msg.x2 = this.isMultiJackpot() ? (byte) 1 : 0;
            this.sendMessageToRoom(msg);
            this.lastTimeUpdatePotToRoom = currentTime;
            try {
                this.mgService.savePot(this.name, this.pot, false);
            } catch (IOException | InterruptedException | TimeoutException e) {
                Debug.trace((Object[]) new Object[]{"MINI POKER: update pot poker error ", e.getMessage()});
            }
        }
    }

    public void updatePotToUser(User user) {
        UpdatePotMiniPokerMsg msg = new UpdatePotMiniPokerMsg();
        msg.value = this.pot;
        msg.x2 = this.isMultiJackpot() ? (byte) 1 : 0;
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void gameLoop() {
        Map<String, AutoUserMiniPoker> map;
        ArrayList<User> usersPlay = new ArrayList<User>();
        Map<String, AutoUserMiniPoker> map2 = map = this.usersAuto;
        synchronized (map2) {
            for (AutoUserMiniPoker user : this.usersAuto.values()) {
                boolean play = user.incCount();
                if (!play) continue;
                usersPlay.add(user.getUser());
            }
        }
        int numThreads = usersPlay.size() / 100 + 1;
        for (int i = 1; i <= numThreads; ++i) {
            int fromIndex = (i - 1) * 100;
            int toIndex = i * 100;
            if (toIndex > usersPlay.size()) {
                toIndex = usersPlay.size();
            }
            ArrayList tmp = new ArrayList(usersPlay.subList(fromIndex, toIndex));
            PlayListMiniPokerTask task = new PlayListMiniPokerTask(tmp);
            this.executor.execute(task);
        }
        usersPlay.clear();
    }

    public void playListMiniPoker(List<User> users) {
        for (User user : users) {
            short result = this.play(user, this.betValue);
            if (result != 1 && result != 2 && result != 12 && result != 102 && result != 100) continue;
            this.forceStopAutoPlay(user);
        }
        users.clear();
    }

    private String buildDescription(long totalBet, long totalPrizes, short result) {
        if (totalBet == 0L) {
            return this.resultToString(result) + ": " + totalPrizes;
        }
        return "Quay: " + totalBet + ", " + this.resultToString(result) + ": " + totalPrizes;
    }

    private String resultToString(short result) {
        switch (result) {
            case 1: {
                return "N\u1ed5 h\u0169";
            }
            case 12: {
                return "N\u1ed5 h\u0169 X2";
            }
            case 2: {
                return "Th\u00f9ng ph\u00e1 s\u1ea3nh";
            }
            case 3: {
                return "T\u1ee9 qu\u00fd";
            }
            case 4: {
                return "C\u00f9 l\u0169";
            }
            case 5: {
                return "Th\u1eafng";
            }
            case 6: {
                return "S\u1ea3nh";
            }
            case 7: {
                return "S\u1ea3nh ch\u00faa";
            }
            case 8: {
                return "Hai \u0111\u00f4i";
            }
            case 9: {
                return "L\u00e1 b\u00e0i cao";
            }
        }
        return "Tr\u01b0\u1ee3t";
    }

    @Override
    public boolean joinRoom(User user) {
        boolean result = super.joinRoom(user);
        if (result) {
            user.setProperty((Object) "MGROOM_MINI_POKER_INFO", (Object) this);
        }
        return result;
    }

    @Override
    public boolean quitRoom(User user) {
        return super.quitRoom(user);
    }

    public void startHuX2() {
        /*
        Debug.trace((Object)(this.gameName + " start hu X2"));
        this.countHu = 1;
        this.sv.setValue(this.name + "_count_hu", this.countHu);
        if (this.moneyType == 1 && this.betValue == 100L) {
            this.huX2 = true;
        }*/
    }

    public void stopHuX2() {
//        Debug.trace((Object) (this.gameName + " stop hu x2"));
//        this.countHu = -1;
//        this.countNoHuX2 = 0;
//        this.huX2 = false;
//        this.sv.setValue(this.name + "_count_hu", this.countHu);
//        this.sv.setValue(this.name + "_count_no_hu_x2", this.countNoHuX2);
    }

    public void noHuX2() {
        /*
        if (this.countHu > -1) {
            ++this.countHu;
            this.sv.setValue(this.name + "_count_hu", this.countHu);
            if (this.huX2) {
                ++this.countNoHuX2;
                this.sv.setValue(this.name + "_count_no_hu_x2", this.countNoHuX2);
                Debug.trace((Object)(this.gameName + " No hu X2: " + this.countHu + " , huX2= " + this.countNoHuX2));
                if (this.betValue == 100L && this.countNoHuX2 >= 10) {
                    MiniPokerModule.stopX2();
                    this.stopHuX2();
                }
                if (this.betValue == 1000L && this.countNoHuX2 >= 1) {
                    MiniPokerModule.stopX2();
                    this.stopHuX2();
                }
            }
            this.calculatHuX2();
        }*/
    }

    private void calculatHuX2() {
        /*
        if (this.countHu > -1 && this.moneyType == 1) {
            if (this.betValue == 100L) {
                this.huX2 = this.countHu % 4 == 1 && this.countNoHuX2 < 10;
            } else if (this.betValue == 1000L) {
                this.huX2 = this.countHu == 3 && this.countNoHuX2 < 1;
            }
            Debug.trace((Object)("Count hu X2 " + this.name + ": " + this.countNoHuX2));
        }
        Debug.trace((Object)("Count hu " + this.name + ": " + this.countHu + ", x2= " + this.huX2));

         */
    }

    public class ResultPoker {
        public static final short LOI_HE_THONG = 100;
        public static final short DAT_CUOC_KHONG_HOP_LE = 101;
        public static final short KHONG_DU_TIEN = 102;
        public static final short TRUOT = 0;
        public static final short NO_HU = 1;
        public static final short THUNG_PHA_SANH_NHO = 2;
        public static final short TU_QUY = 3;
        public static final short CU_LU = 4;
        public static final short THUNG = 5;
        public static final short SANH = 6;
        public static final short SAM_CO = 7;
        public static final short HAI_DOI = 8;
        public static final short MOT_DOI_TO = 9;
        public static final short MOT_DOI_NHO = 10;
        public static final short BAI_CAO = 11;
        public static final short NO_HU_X2 = 12;
    }

    private final class GameLoopTask
            implements Runnable {
        private GameLoopTask() {
        }

        @Override
        public void run() {
            try {
                MGRoomMiniPoker.this.gameLoop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final class PlayListMiniPokerTask
            extends Thread {
        private List<User> users;

        private PlayListMiniPokerTask(List<User> users) {
            this.users = users;
            this.setName("AutoPlayMiniPoker");
        }

        @Override
        public void run() {
            MGRoomMiniPoker.this.playListMiniPoker(this.users);
        }
    }

}

