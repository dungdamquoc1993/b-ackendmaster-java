/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  bitzero.server.BitZeroServer
 *  bitzero.server.entities.User
 *  bitzero.server.extensions.data.BaseMsg
 *  bitzero.server.util.TaskScheduler
 *  bitzero.util.common.business.Debug
 *  com.vinplay.cardlib.models.Card
 *  com.vinplay.cardlib.models.Deck
 *  com.vinplay.cardlib.models.Rank
 *  com.vinplay.dal.service.BroadcastMessageService
 *  com.vinplay.dal.service.CaoThapService
 *  com.vinplay.dal.service.impl.BroadcastMessageServiceImpl
 *  com.vinplay.dal.service.impl.CaoThapServiceImpl
 *  com.vinplay.usercore.service.UserService
 *  com.vinplay.usercore.service.impl.UserServiceImpl
 *  com.vinplay.vbee.common.enums.Games
 *  com.vinplay.vbee.common.response.MoneyResponse
 *  com.vinplay.vbee.common.statics.TransType
 */
package game.modules.minigame.room;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.server.util.TaskScheduler;
import bitzero.util.common.business.Debug;
import com.vinplay.cardlib.models.Card;
import com.vinplay.cardlib.models.Deck;
import com.vinplay.cardlib.models.Rank;
import com.vinplay.dal.service.BroadcastMessageService;
import com.vinplay.dal.service.CaoThapService;
import com.vinplay.dal.service.impl.BroadcastMessageServiceImpl;
import com.vinplay.dal.service.impl.CacheServiceImpl;
import com.vinplay.dal.service.impl.CaoThapServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;
import game.modules.description.CaoThapDescription.CaoThapDescriptionUtils;
import game.modules.minigame.cmd.send.caothap.ResultCaoThapMsg;
import game.modules.minigame.cmd.send.caothap.StartPlayCaoThapMsg;
import game.modules.minigame.cmd.send.caothap.StopPlayCaoThapMsg;
import game.modules.minigame.cmd.send.caothap.UpdatePotCaoThapMsg;
import game.modules.minigame.cmd.send.caothap.UpdateTimeCaoThapMsg;
import game.modules.minigame.entities.CaoThapInfo;
import game.modules.minigame.entities.MinigameConstant;
import game.modules.minigame.room.MGRoom;
import game.modules.minigame.utils.CaoThapUtils;
import game.utils.GameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MGRoomCaoThap
        extends MGRoom {
    public long pot;
    public long fund;
    private float tax = MinigameConstant.MINIGAME_TAX_VIN;
    private byte moneyType;
    private String moneyTypeStr;
    private int baseBetValue = 0;
    private UserService userService = new UserServiceImpl();
    private final Runnable gameLoopTask = new GameLoopTask();
    private final Runnable timeLoopTask = new TimeLoopTask();
    private Map<String, CaoThapInfo> usersCaoThap = new HashMap<>();
    private CaoThapService ctService = new CaoThapServiceImpl();
    private BroadcastMessageService broadcastMsgService = new BroadcastMessageServiceImpl();
    private String gameID = Games.CAO_THAP.getId()+"";

    public MGRoomCaoThap(String roomName, byte moneyType, long pot, long fund, int baseBetValue) {
        super(roomName);
        this.moneyType = moneyType;
        if (moneyType == 1) {
            this.moneyTypeStr = "vin";
            this.tax = MinigameConstant.MINIGAME_TAX_VIN;
        } else if (moneyType == 0) {
            this.moneyTypeStr = "xu";
            this.tax = MinigameConstant.MINIGAME_TAX_XU;
        }
        this.pot = pot;
        this.fund = fund;
        this.baseBetValue = baseBetValue;
        this.usersCaoThap = new HashMap<String, CaoThapInfo>();

        CacheServiceImpl cacheService = new CacheServiceImpl();
        cacheService.setValue(roomName, (int)pot);
        cacheService.setValue(roomName + "_x2", 0);

        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.gameLoopTask, 10, 1, TimeUnit.SECONDS);
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.timeLoopTask, 10, 5, TimeUnit.SECONDS);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void startPlay(User user, int betValue, long referenceId) {
        Debug.trace((Object)("CAO THAP startPlay: " + user.getName()));
        Map<String, CaoThapInfo> map;
        StartPlayCaoThapMsg msg = new StartPlayCaoThapMsg();
        Map<String, CaoThapInfo> map2 = map = this.usersCaoThap;
        synchronized (map2) {
            if (!this.usersCaoThap.containsKey(user.getName())) {
                long moneyUse = this.userService.getMoneyUserCache(user.getName(), this.moneyTypeStr);
                if (moneyUse >= (long) betValue) {
                    Deck deck = new Deck();
                    deck.shuffle();
                    Card card = deck.deal();
                    byte numA = 0;
                    if (card.getRank() == Rank.Ace) {
                        numA = 1;
                    }
                    long moneyToSystem = Math.round((float) betValue * this.tax / 100.0f);
                    long moneyToFund = (long) betValue - moneyToSystem;
                    MoneyResponse mnres = new MoneyResponse(false, "1001");
                    if (!isBot(user.getName())) {
                        mnres = this.userService.updateMoney(user.getName(), -betValue, this.moneyTypeStr, "CaoThap",
                                this.gameID,
                                CaoThapDescriptionUtils.getCaoThapBetDesciption(this.gameID,referenceId),
                                moneyToSystem, Long.valueOf(referenceId), TransType.START_TRANS);

                    } else {
                        mnres.setSuccess(true);
                    }
                    if (mnres != null && mnres.isSuccess()) {
                        Debug.trace((Object)("CAO THAP startPlay 1: " + user.getName()), " ,fund=", this.fund, moneyToFund, " ,pot= ", this.pot );
                        this.fund += moneyToFund;
                        this.saveFund();
                        this.savePot();
                        List<Double> ratioLst = CaoThapUtils.getRatio(deck, card);
                        msg.money1 = Math.round((double) betValue * ratioLst.get(1));
                        msg.money2 = betValue;
                        msg.money3 = Math.round((double) betValue * ratioLst.get(0));
                        msg.card = (byte) card.getCode();
                        msg.currentMoney = mnres.getCurrentMoney();
                        msg.referenceId = referenceId;
                        ArrayList<Card> carryCards = new ArrayList<Card>();
                        carryCards.add(card);
//                        CaoThapInfo info = new CaoThapInfo(user, referenceId, deck, card, 1, 120, betValue, numA, carryCards, msg.money1, msg.money3, user.getUniqueId());
                        CaoThapInfo info = new CaoThapInfo(user, referenceId, deck, card, (short) 1, (short) 120, betValue, numA, carryCards, msg.money1, msg.money3, user.getId());
                        this.usersCaoThap.put(user.getName(), info);
                        try {
                            if (!this.isBot(user.getName())) {
                                this.ctService.logCaoThap(referenceId, user.getName(), (long) betValue, (short) 0, (long) (-betValue), card.toString(), this.pot, this.fund, (int) this.moneyType, (short) 0, 1);

                            }
                        } catch (Exception e) {
                            Debug.trace((Object[]) new Object[]{"CAO THAP: log cao thap error ", e.getMessage()});
                        }
                    } else {
                        msg.Error = 100;
                    }
                } else {
                    msg.Error = 3;
                }
            } else {
                msg.Error = 1;
            }
        }
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void play(User user, byte choose) {
        Debug.trace((Object)("CAO THAP play: " + user.getName()), " ,fund=", this.fund, " ,pot=", this.pot);
        Map<String, CaoThapInfo> map;
        ResultCaoThapMsg msg = new ResultCaoThapMsg();
        Map<String, CaoThapInfo> map2 = map = this.usersCaoThap;
        synchronized (map2) {
            if (this.usersCaoThap.containsKey(user.getName())) {
                long currentMoney = this.userService.getCurrentMoneyUserCache(user.getName(), this.moneyTypeStr);
                CaoThapInfo info = this.usersCaoThap.get(user.getName());
                info.setTime((short) 120);
                info.setStep((short) (info.getStep() + 1));
                if (!(info.getMoneyUp() == 0L && choose == 1 || info.getMoneyDown() == 0L && choose == 0)) {
                    long moneyWin;
                    long fundStep;
                    byte numA;
                    Card card;
                    Deck deck;
                    short result = 0;
                    int i = 0;
                    boolean noHu = false;
                    if (this.moneyType == 1 && info.getNumA() == 2) {
                        // // lock no hũ 11/01 kane
                        //noHu = (this.baseBetValue < 100000 || this.userService.getTotalRechargeMoney(user.getName()) >= Math.round((double) this.pot * 0.1)) && CaoThapUtils.isDoWithRatio(1000.0);
                    }
                    do {
                        fundStep = this.fund;
                        numA = info.getNumA();
                        if (info.getMoneyUp() == 0L || info.getMoneyDown() == 0L) {
                            if (noHu) {
                                deck = info.getDeck();
                                card = deck.deal();
                            } else {
                                card = CaoThapUtils.randomNoA(info.getDeck());
                                deck = info.getDeck();
                                deck.popCard(card);
                            }
                            ++i;
                        } else if (i == 0) {
                            if (noHu) {
                                deck = info.getDeck();
                                card = deck.deal();
                            } else {
                                card = CaoThapUtils.randomNoA(info.getDeck());
                                deck = info.getDeck();
                                deck.popCard(card);
                            }
                        } else {
                            card = CaoThapUtils.randomThua(info.getDeck(), info.getCard(), choose);
                            deck = info.getDeck();
                            deck.popCard(card);
                        }
                        moneyWin = 0L;
                        if (card.getRank().getRank() == info.getCard().getRank().getRank()) {
                            result = 6;
                            moneyWin = Math.round(info.getMoney() * 9L / 10L);
                        } else if (card.getRank().getRank() > info.getCard().getRank().getRank()) {
                            if (choose == 1) {
                                result = 4;
                                moneyWin = info.getMoneyUp();
                            } else {
                                result = 5;
                            }
                        } else if (card.getRank().getRank() < info.getCard().getRank().getRank()) {
                            if (choose == 1) {
                                result = 5;
                            } else {
                                result = 4;
                                moneyWin = info.getMoneyDown();
                            }
                        }
                        if (result == 4) {
                            fundStep = info.getStep() > 2 ? (fundStep = fundStep - (moneyWin - info.getMoney())) : (fundStep = fundStep - moneyWin);
                        } else if (result == 6) {
                            if (info.getStep() > 2) {
                                ++i;
                            } else {
                                fundStep -= info.getMoney();
                            }
                        } else if (result == 5) {
                            ++i;
                        }
                        if (result == 5 || card.getRank() != Rank.Ace || (numA = (byte) (numA + 1)) != 3) continue;
                        result = 7;
                    } while (fundStep <= 0L && ++i <= 1);
                    boolean next = false;
                    long moneyToUser = 0L;
                    if (result == 4) {
                        moneyToUser = moneyWin;
                        next = true;
                        this.fund = info.getStep() > 2 ? (this.fund = this.fund - (moneyWin - info.getMoney())) : (this.fund = this.fund - moneyWin);
                        this.saveFund();
                    } else if (result == 6) {
                        moneyToUser = moneyWin;
                        this.pot += info.getMoney() - moneyWin;
                        Debug.trace((Object)("CAO THAP result : "), result, user.getName(), this.pot);
                        this.savePot();
                        next = true;
                        if (info.getStep() <= 2) {
                            this.fund -= info.getMoney();
                            this.saveFund();
                        }
                    } else if (result == 5) {
                        moneyToUser = 0L;
                        if (info.getStep() > 2) {
                            this.fund += info.getMoney();
                            this.saveFund();
                        }
                        currentMoney = this.userService.getCurrentMoneyUserCache(user.getName(), this.moneyTypeStr);
                        if (!isBot(user.getName())) {
                            this.userService.updateMoney(user.getName(), moneyToUser, this.moneyTypeStr, "CaoThap", "", "", 0L, Long.valueOf(info.getReferenceId()), TransType.END_TRANS);

                        }
                    } else if (result == 7) {
                        if (info.getMoney() > moneyWin) {
                            this.pot += info.getMoney() - moneyWin;
                        } else {
                            this.fund -= moneyWin - info.getMoney();
                            this.saveFund();
                        }
                        moneyToUser = Math.round(this.pot / 2L);
                        this.pot -= moneyToUser;
                        Debug.trace((Object)("CAO THAP result : "), result, user.getName(), this.pot);
                        this.savePot();
                        MoneyResponse mnres = this.userService.updateMoney(user.getName(), moneyToUser += moneyWin, this.moneyTypeStr,
                                "CaoThap",
                                this.gameID, CaoThapDescriptionUtils.getCaoThapJackPotDesciption(this.gameID,info.getReferenceId(),info.getStep())
                                , 0L, Long.valueOf(info.getReferenceId()), TransType.END_TRANS);
                        if (mnres != null && mnres.isSuccess()) {
                            if (this.moneyType == 1) {
                                GameUtils.sendSMSToUser(user.getName(), "Chuc mung " + user.getName() + " da no hu game Cao Thap phong " + this.baseBetValue + ". So tien no hu: " + moneyToUser + " Vin");
                                if (moneyToUser >= (long) BroadcastMessageServiceImpl.MIN_MONEY) {
                                    this.broadcastMsgService.putMessage(Games.CAO_THAP.getId(), user.getName(), moneyToUser);
                                }
                            }
                            currentMoney = mnres.getCurrentMoney();
                            try {
                                List<Card> carryCardsNoHu = info.getCarryCards();
                                carryCardsNoHu.add(card);
                                if (!isBot(user.getName())) {
                                    this.ctService.logCaoThapWin(info.getReferenceId(), user.getName(), (long) this.baseBetValue, (short) 7, moneyToUser, CaoThapUtils.getCardStr(carryCardsNoHu), (int) this.moneyType);

                                }
                            } catch (Exception e) {
                                Debug.trace((Object[]) new Object[]{"CAO THAP: log cao thap error ", e.getMessage()});
                            }
                        }
                    }
                    try {
                        if(!isBot(user.getName())){
                            this.ctService.logCaoThap(info.getReferenceId(), user.getName(), info.getMoney(), result, moneyToUser, card.toString(), this.pot, this.fund, (int) this.moneyType, (short) choose, (int) info.getStep());

                        }
                        } catch (Exception e) {
                        Debug.trace((Object[]) new Object[]{"CAO THAP: log cao thap error ", e.getMessage()});
                    }
                    List<Double> ratioLst = CaoThapUtils.getRatio(deck, card);
                    msg.money1 = Math.round((double) moneyToUser * ratioLst.get(1));
                    msg.money2 = moneyToUser;
                    msg.money3 = Math.round((double) moneyToUser * ratioLst.get(0));
                    msg.card = (byte) card.getCode();
                    this.sendMessageToUser((BaseMsg) msg, user);
                    if (next) {
                        info.setDeck(deck);
                        info.setCard(card);
                        info.setNumA(numA);
                        info.setMoney(moneyToUser);
                        List<Card> carryCards = info.getCarryCards();
                        carryCards.add(card);
                        info.setCarryCards(carryCards);
                        info.setMoneyUp(msg.money1);
                        info.setMoneyDown(msg.money3);
                        this.usersCaoThap.put(user.getName(), info);
                    } else {
                        StopPlayCaoThapMsg msgStop = new StopPlayCaoThapMsg();
                        msgStop.result = (byte) result;
                        msgStop.currentMoney = currentMoney;
                        msgStop.moneyExchange = moneyToUser;
                        this.usersCaoThap.remove(user.getName());
                        this.sendMessageToUser((BaseMsg) msgStop, user);
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stopPlay(User user) {
        Map<String, CaoThapInfo> map;
        StopPlayCaoThapMsg msg = new StopPlayCaoThapMsg();
        Map<String, CaoThapInfo> map2 = map = this.usersCaoThap;
        synchronized (map2) {
            if (this.usersCaoThap.containsKey(user.getName())) {
                CaoThapInfo info = this.usersCaoThap.get(user.getName());
                if (info.getStep() != 1) {
                    MoneyResponse mnres = this.userService.updateMoney(user.getName(), info.getMoney(), this.moneyTypeStr, "CaoThap",
                            this.gameID,
                            CaoThapDescriptionUtils.getCaoThapWinDesciption(this.gameID, info.getReferenceId(),info.getStep())
                            , 0L, Long.valueOf(info.getReferenceId()), TransType.END_TRANS);
                    if (mnres != null && mnres.isSuccess()) {
                        try {
                            this.ctService.logCaoThapWin(info.getReferenceId(), user.getName(), (long) this.baseBetValue, (short) 4, info.getMoney(), CaoThapUtils.getCardStr(info.getCarryCards()), (int) this.moneyType);
                        } catch (Exception e) {
                            Debug.trace((Object[]) new Object[]{"CAO THAP: log cao thap error ", e.getMessage()});
                        }
                        msg.result = (byte) 4;
                        msg.currentMoney = mnres.getCurrentMoney();
                        msg.moneyExchange = info.getMoney();
                        this.usersCaoThap.remove(user.getName());
                        if (this.moneyType == 1 && info.getMoney() >= (long) BroadcastMessageServiceImpl.MIN_MONEY) {
                            this.broadcastMsgService.putMessage(Games.CAO_THAP.getId(), user.getName(), info.getMoney());
                        }
                    } else {
                        msg.Error = 100;
                    }
                } else {
                    msg.Error = 2;
                }
                this.sendMessageToUser((BaseMsg) msg, user);
            }
        }
    }

    public void updatePotToUser(User user) {
        UpdatePotCaoThapMsg msg = new UpdatePotCaoThapMsg();
        msg.value = this.pot;
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    private void saveFund() {
        try {
            Debug.trace((Object)("CAO THAP fund: "), this.fund);
            this.ctService.updateFundCaoThap(this.name, this.fund);
        } catch (IOException | InterruptedException | TimeoutException e) {
            Debug.trace((Object[]) new Object[]{"CAO THAP: update fund cao thap error ", e.getMessage()});
        }
    }

    private void savePot() {
        Debug.trace((Object)("CAO THAP pot: "), this.pot);
        UpdatePotCaoThapMsg msg = new UpdatePotCaoThapMsg();
        msg.value = this.pot;
        this.sendMessageToRoom(msg);
        try {
            this.ctService.updatePotCaoThap(this.name, this.pot);
        } catch (IOException | InterruptedException | TimeoutException e) {
            Debug.trace((Object[]) new Object[]{"CAO THAP: update pot cao thap error ", e.getMessage()});
        }
    }

    public Map<String, CaoThapInfo> getUsers() {
        return this.usersCaoThap;
    }

    public void gameLoop() {
        try {
            for (Map.Entry<String, CaoThapInfo> entry : this.usersCaoThap.entrySet()) {
                CaoThapInfo info = entry.getValue();
                short i = Short.parseShort(String.valueOf(info.getTime() - 1));
                info.setTime(i);
                if (i == 0) {
                    if (info.getStep() == 1) {
                        byte choose = -1;
                        if (info.getMoneyUp() == 0L) {
                            choose = 0;
                        } else if (info.getMoneyDown() == 0L) {
                            choose = 1;
                        } else {
                            Random rd = new Random();
                            choose = (byte) rd.nextInt(2);
                        }
                        this.play(info.getUser(), choose);
                        continue;
                    }
                    this.stopPlay(info.getUser());
                    continue;
                }
                this.usersCaoThap.put(entry.getKey(), info);
            }
        } catch (Exception e) {
            Debug.trace((Object[]) new Object[]{"Exception: " + e.getMessage(), e});
        }
    }

    public void timeLoop() {
        try {
            for (Map.Entry<String, CaoThapInfo> entry : this.usersCaoThap.entrySet()) {
                UpdateTimeCaoThapMsg msg = new UpdateTimeCaoThapMsg();
                msg.time = entry.getValue().getTime();
                this.sendMessageToUser((BaseMsg) msg, entry.getValue().getUser());
            }
        } catch (Exception e) {
            Debug.trace((Object[]) new Object[]{"Exception: " + e.getMessage(), e});
        }
    }

    @Override
    public boolean joinRoom(User user) {
        boolean result = super.joinRoom(user);
        if (result) {
            user.setProperty((Object) "MGROOM_CAO_THAP_INFO", (Object) this);
        }
        return result;
    }

    @Override
    public boolean quitRoom(User user) {
        return super.quitRoom(user);
    }

    public boolean isBot(String username) {
        UserCacheModel model = this.userService.getUser(username);
        return model.isBot();
    }

    public class ResultCaoThap {
        public static final short LOI_HE_THONG = 100;
        public static final short USER_PLAYING = 1;
        public static final short STEP_ONE = 2;
        public static final short NOT_ENOUGH_MONEY = 3;
        public static final short THANG = 4;
        public static final short THUA = 5;
        public static final short HOA = 6;
        public static final short NO_HU = 7;
    }

    private final class GameLoopTask
            implements Runnable {
        private GameLoopTask() {
        }

        @Override
        public void run() {
            try {
                MGRoomCaoThap.this.gameLoop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final class TimeLoopTask
            implements Runnable {
        private TimeLoopTask() {
        }

        @Override
        public void run() {
            try {
                MGRoomCaoThap.this.timeLoop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

