/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  bitzero.server.BitZeroServer
 *  bitzero.server.entities.User
 *  bitzero.server.extensions.data.BaseMsg
 *  bitzero.server.util.TaskScheduler
 *  bitzero.util.common.business.Debug
 *  com.vinplay.dal.service.BroadcastMessageService
 *  com.vinplay.dal.service.CacheService
 *  com.vinplay.dal.service.MiniGameService
 *  com.vinplay.dal.service.PokeGoService
 *  com.vinplay.dal.service.impl.BroadcastMessageServiceImpl
 *  com.vinplay.dal.service.impl.CacheServiceImpl
 *  com.vinplay.dal.service.impl.MiniGameServiceImpl
 *  com.vinplay.dal.service.impl.PokeGoServiceImpl
 *  com.vinplay.usercore.service.UserService
 *  com.vinplay.usercore.service.impl.UserServiceImpl
 *  com.vinplay.vbee.common.enums.Games
 *  com.vinplay.vbee.common.exceptions.KeyNotFoundException
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.models.cache.UserCacheModel
 *  com.vinplay.vbee.common.response.MoneyResponse
 *  com.vinplay.vbee.common.statics.TransType
 *  com.vinplay.vbee.common.utils.CommonUtils
 *  com.vinplay.vbee.common.utils.DateTimeUtils
 */
package game.modules.minigame.room;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import com.vinplay.dal.service.*;
import com.vinplay.dal.service.impl.*;
import com.vinplay.operator_maxium.MaxiumWinConfig;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;
import com.vinplay.vbee.common.utils.CommonUtils;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import game.GameConfig.GameConfig;
import game.modules.Slot3x3.Slot3x3TableInfo;
import game.modules.Slot3x3.Slot3x3Util;
import game.modules.description.SlotDescription.SlotDescriptionUtils;
import game.modules.minigame.CandyModule;
import game.modules.minigame.cmd.send.galaxy.BigWinGalaxyMsg;
import game.modules.minigame.cmd.send.pokego.ForceStopAutoPlayPokeGoMsg;
import game.modules.minigame.cmd.send.pokego.ResultPokeGoMsg;
import game.modules.minigame.cmd.send.pokego.UpdatePotPokeGoMsg;
import game.modules.minigame.entities.pokego.AutoUserPokeGo;
import game.modules.minigame.entities.pokego.Award;
import game.modules.minigame.entities.pokego.AwardsOnLine;
import game.modules.minigame.entities.pokego.Item;
import game.modules.minigame.entities.pokego.Line;
import game.modules.minigame.entities.pokego.Lines;
import game.modules.minigame.utils.PokeGoUtils;
import game.utils.ConfigGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MGRoomCandy
        extends MGRoom {
    public long pot;
    public long fund;
    private long initPotValue;
    private int betValue;
    private short moneyType;
    private String moneyTypeStr;
    private UserService userService = new UserServiceImpl();
    private MiniGameService mgService = new MiniGameServiceImpl();
    private PokeGoService pgService = new PokeGoServiceImpl();
    private SlotMachineService slotMachineService = new SlotMachineServiceImpl();
    private BroadcastMessageService broadcastMsgService = new BroadcastMessageServiceImpl();
    private final Runnable gameLoopTask = new GameLoopTask();
    private Map<String, AutoUserPokeGo> usersAuto = new HashMap<String, AutoUserPokeGo>();
    private Lines lines = new Lines();
    private long lastTimeUpdatePotToRoom = 0L;
    private long lastTimeUpdateFundToRoom = 0L;
    private ThreadPoolExecutor executor;
    private int countHu = -1;
    private int countNoHuX2 = 0;
    public String gameName = Games.CANDY.getName();
    public String gameID = Games.CANDY.getId() + "";
    protected CacheService sv = new CacheServiceImpl();
    protected CandyModule module;

    public long fundJackPot;
    private String fundJackPotName;

    public MGRoomCandy(CandyModule module, String name, short moneyType, long pot, long fund, int betValue, long initPotValue, String fundJackPotName, long fundJackPot) {
        super(name);
        this.module = module;
        this.moneyType = moneyType;
        this.moneyTypeStr = this.moneyType == 1 ? "vin" : "xu";
        this.executor = moneyType == 1 ? (ThreadPoolExecutor) Executors.newFixedThreadPool(ConfigGame.getIntValue(this.gameName + "_thread_pool_per_room_vin")) :
                (ThreadPoolExecutor) Executors.newFixedThreadPool(ConfigGame.getIntValue(this.gameName + "_thread_pool_per_room_xu"));
        this.pot = pot;
        CacheServiceImpl cacheService = new CacheServiceImpl();
        cacheService.setValue(name, (int) pot);
        this.fund = fund;
        this.betValue = betValue;
        this.initPotValue = initPotValue;
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.gameLoopTask, 10, 1, TimeUnit.SECONDS);
        try {
            this.countHu = this.sv.getValueInt(name + "_count_hu");
            this.countNoHuX2 = this.sv.getValueInt(name + "_count_no_hu_x2");
            this.calculatHuX2();
        } catch (KeyNotFoundException keyNotFoundException) {
            // empty catch block
        }
        try {
            this.mgService.savePot(name, pot, this.isMultiJackpot());
        } catch (IOException | InterruptedException | TimeoutException exception) {
            // empty catch block
        }

        this.fundJackPotName = fundJackPotName;
        this.fundJackPot = fundJackPot;
    }

    public boolean isMultiJackpot(){
        return GameConfig.getInstance().slotMultiJackpotConfig.isMultiJackpot(this.gameName);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void autoPlay(User user, String lines) {
        Map<String, AutoUserPokeGo> map;
        Map<String, AutoUserPokeGo> map2 = map = this.usersAuto;
        synchronized (map2) {
            if (this.usersAuto.containsKey(user.getName())) {
                AutoUserPokeGo entry = this.usersAuto.get(user.getName());
                this.forceStopAutoPlay(entry.getUser());
            }
            this.usersAuto.put(user.getName(), new AutoUserPokeGo(user, lines));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stopAutoPlay(User user) {
        Map<String, AutoUserPokeGo> map;
        Map<String, AutoUserPokeGo> map2 = map = this.usersAuto;
        synchronized (map2) {
            AutoUserPokeGo entry;
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
        Map<String, AutoUserPokeGo> map;
        Map<String, AutoUserPokeGo> map2 = map = this.usersAuto;
        synchronized (map2) {
            this.usersAuto.remove(user.getName());
            ForceStopAutoPlayPokeGoMsg msg = new ForceStopAutoPlayPokeGoMsg();
            this.sendMessageToUser((BaseMsg) msg, user);
        }
    }

    public synchronized void addMoneyToPot(long money) {
        this.pot += money;

//        Debug.trace("addMoneyToPot  " + money, "pot  " + this.pot);
        this.savePot();
    }

    public void resetPot() {
        this.pot = this.initPotValue;
        this.savePot();
    }

    public void botEatJackpot(String keyBot, long nextTime, String username) {
        String currentTimeStr = DateTimeUtils.getCurrentTime();
        int result = 3;
        long totalPrizes = this.pot;
        if (this.isMultiJackpot()) {
            result = 4;
            totalPrizes = this.pot * GameConfig.getInstance().slot3x3GameConfig.MULTI_JACKPOT;
        }
        try {
            this.pgService.addTop(username, this.betValue, totalPrizes, this.moneyType, currentTimeStr, result);
        } catch (Exception e) {

        }
        this.broadcastMsgService.putMessage(Games.CANDY.getId(), username, totalPrizes);
        this.broadcastBigWin(username, (byte) result, totalPrizes);
        this.resetPot();
        try {
            this.mgService.saveFund(keyBot, nextTime);
        } catch (Exception e) {

        }

    }

    public boolean isUserBigWin(String userName) {
        return this.userService.isUserBigWin(userName);
    }

    public boolean isUserJackpot(String userName){
        return this.slotMachineService.isSetJackpotForUser(this.gameName,userName,this.betValue);
    }

    public synchronized ResultPokeGoMsg play(String username, String linesStr) {
        long startTime = System.currentTimeMillis();
        String currentTimeStr = DateTimeUtils.getCurrentTime();
        long refernceId = CandyModule.getNewRefenceId();
        short result = 0;
        int soLanNoHu = ConfigGame.getIntValue(this.gameName + "_so_lan_no_hu");
        Random rd;
        String[] lineArr = linesStr.split(",");
        long currentMoney = this.userService.getMoneyUserCache(username, this.moneyTypeStr);
        UserCacheModel u = this.userService.getUser(username);
        long totalBetValue = lineArr.length * this.betValue;
        ResultPokeGoMsg msg = new ResultPokeGoMsg();

        long fee = totalBetValue * GameConfig.getInstance().slot3x3GameConfig.FEE / 100;
        long moneyToPot = totalBetValue * GameConfig.getInstance().slot3x3GameConfig.MONEY_TO_JACKPOT / 100;
        long moneyToFundJackpot = totalBetValue * (GameConfig.getInstance().slot3x3GameConfig.MONEY_TO_FUND_JACKPOT + GameConfig.getInstance().slot3x3GameConfig.MONEY_TO_JACKPOT) / 100;
        long moneyToFund = totalBetValue - fee - moneyToFundJackpot;
        if (lineArr.length > 0 && !linesStr.isEmpty()) {
            if (totalBetValue > 0L) {
                if (totalBetValue <= currentMoney) {
                    MoneyResponse moneyRes = this.userService.updateMoney(username, -totalBetValue, this.moneyTypeStr, Games.CANDY.getName(),
                            this.gameID,
                            SlotDescriptionUtils.getBetDescription(this.gameID), fee, Long.valueOf(refernceId), TransType.START_TRANS);
                    if (moneyRes != null && moneyRes.isSuccess()) {
                        boolean isUserBigWin = this.isUserBigWin(username);
                        if(!isUserBigWin){
                            this.fund += moneyToFund;
                            this.fundJackPot += moneyToFundJackpot;
                            this.pot += moneyToPot;
                        }
                        long tienThuongX2 = 0;
                        int[] rowIndex = new int[lineArr.length];
                        for (int i = 0; i < lineArr.length; i++) {
                            rowIndex[i] = Integer.parseInt(lineArr[i]);
                        }
                        long totalPrizes = 0;
                        //Debug.trace(" get user value 1");
                        long userValue = this.userService.getUserValue(username);
                        long maxiumWin = MaxiumWinConfig.getMaxiumMoneyWin(userValue) - moneyRes.getCurrentMoney();

//                        Debug.trace(" get user value maxiumWin  ", userValue, maxiumWin);
                       // Debug.trace(" get user value 3");
                        Slot3x3TableInfo slot3x3TableInfo = Slot3x3Util.getSlot3x1TableInfo(
                                rowIndex, this.betValue, this.fund, this.fundJackPot,
                                this.isMultiJackpot(), this.pot, this.initPotValue, maxiumWin
                        );
                        if(isUserBigWin){
                            slot3x3TableInfo = Slot3x3Util.getSlot3x1TableInfoBigWin(rowIndex,this.betValue,
                                    this.isMultiJackpot(), this.pot, this.initPotValue);
                        }

                        if(this.isUserJackpot(username)){
                            slot3x3TableInfo = new Slot3x3TableInfo((byte) 0,this.betValue,
                                    Slot3x3Util.getMoneyJackPot(this.pot,this.isMultiJackpot(), this.initPotValue));
                            slot3x3TableInfo.calculateRowIndex(rowIndex);
                        }

//                        slot3x3TableInfo = new Slot3x3TableInfo((byte) 0,this.betValue,
//                                Slot3x3Util.getMoneyJackPot(this.pot,this.isMultiJackpot(), this.initPotValue));
//                        slot3x3TableInfo.calculateRowIndex(rowIndex);


                        totalPrizes += slot3x3TableInfo.money * this.betValue / 10;
                        if(!isUserBigWin){
                            this.fund -= totalPrizes;
                        }
                        if (slot3x3TableInfo.isJackPot) {
//                            long moneyMinusToFundJackpot = this.pot * Slot3x3Util.MULTI_JACKPOT - (this.pot - this.initPotValue);
                            if (this.isMultiJackpot()) {
                                totalPrizes += this.pot * GameConfig.getInstance().slot3x3GameConfig.MULTI_JACKPOT;
                                if(!isUserBigWin){
                                    this.fundJackPot -= this.pot * GameConfig.getInstance().slot3x3GameConfig.MULTI_JACKPOT;
                                }
                            } else {
                                totalPrizes += this.pot;
                                if(!isUserBigWin){
                                    this.fundJackPot -= this.pot;
                                }
                            }
                        }
                        if (slot3x3TableInfo.isJackPot) {
                            if (this.isMultiJackpot()) {
                                tienThuongX2 = this.pot * (GameConfig.getInstance().minipokerGameConfig.MULTI_JACKPOT - 1);
                                result = 4;
                                this.noHuX2();
//                                GameUtils.sendSMSToUser(username, "Chuc mung " + username + " da no hu game Kim Cuong phong " + this.betValue + ". So tien no hu: " + totalPrizes + " Vin");
                            } else {
                                result = 3;
                            }
                            this.resetPot();
                        } else {
                            result = totalPrizes >= (long) (this.betValue * 100) ? (short) 2 : 1;
                        }
                        long moneyExchange = totalPrizes - tienThuongX2;
                        if (tienThuongX2 > 0L && !u.isBot()) {
                            moneyRes = this.userService.updateMoney(username, tienThuongX2, this.moneyTypeStr, this.gameName,
                                    this.gameID, SlotDescriptionUtils.getMultiJackpotDescription(this.gameID),
                                    0L, null, TransType.NO_VIPPOINT);
                            currentMoney = moneyRes.getCurrentMoney();
                        }
                        if (moneyExchange != 0 && !u.isBot()) {
                            moneyRes = this.userService.updateMoney(username, moneyExchange, this.moneyTypeStr, this.gameName,
                                    this.gameID,
                                    SlotDescriptionUtils.getPayDescription(this.gameID,totalBetValue, totalPrizes, result),
                                    0, Long.valueOf(refernceId), TransType.END_TRANS);
                        }
                        long moneyWin = totalPrizes - (long) this.betValue;
                        if (moneyRes != null && moneyRes.isSuccess()) {
                            currentMoney = moneyRes.getCurrentMoney();
                            if (this.moneyType == 1 && moneyWin >= (long) BroadcastMessageServiceImpl.MIN_MONEY) {
                                this.broadcastMsgService.putMessage(Games.CANDY.getId(), username, moneyWin);
                            }
                        }
                        String linesWin = slot3x3TableInfo.lineWinToString();
                        String prizesOnLine = slot3x3TableInfo.moneyWinToString();
                        msg.matrix = slot3x3TableInfo.matrixToString();
                        msg.linesWin = linesWin;
                        msg.prize = totalPrizes;
                        try {
                            if (!u.isBot()) {
                                this.pgService.logPokeGo(refernceId, username, this.betValue, linesStr, linesWin, prizesOnLine, result, totalPrizes, this.moneyType, currentTimeStr);

                            }
                            if (result == 3 || result == 4) {
                                this.pgService.addTop(username, this.betValue, totalPrizes, this.moneyType, currentTimeStr, result);
                            }

                            if (result == 3 || result == 2 || result == 4) {
                                this.broadcastBigWin(username, (byte) result, totalPrizes);
                            }
                        } catch (InterruptedException awardList) {
                        } catch (TimeoutException awardList) {
                        } catch (IOException awardList) {
                            // empty catch block
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
        } else {
            result = 101;
        }
        msg.result = (byte) result;
        msg.currentMoney = currentMoney;
        long endTime = System.currentTimeMillis();
        long handleTime = endTime - startTime;
        String ratioTime = CommonUtils.getRatioTime((long) handleTime);
        PokeGoUtils.log(refernceId, username, this.betValue, msg.matrix, result, this.moneyType, handleTime, ratioTime, currentTimeStr);
//        Debug.trace("totalWin = " + msg.prize, "  pot = " + this.pot, " fund =" + this.fund,
//                "fund Jackpot = " + this.fundJackPot);
        return msg;
    }

    private void broadcastBigWin(String username, byte result, long totalPrizes) {
        BigWinGalaxyMsg bigWinMsg = new BigWinGalaxyMsg();
        bigWinMsg.username = username;
        bigWinMsg.type = (byte) result;
        bigWinMsg.betValue = (short) this.betValue;
        bigWinMsg.totalPrizes = totalPrizes;
        bigWinMsg.timestamp = DateTimeUtils.getCurrentTime();
        this.module.sendMsgToAllUsers(bigWinMsg);
    }

//    public synchronized ResultPokeGoMsg play(String username, String linesStr) {
//        long startTime = System.currentTimeMillis();
//        String currentTimeStr = DateTimeUtils.getCurrentTime();
//        long refernceId = CandyModule.getNewRefenceId();
//        short result = 0;
//        int soLanNoHu = ConfigGame.getIntValue(this.gameName+"_so_lan_no_hu");
//        Random rd;
//        String[] lineArr = linesStr.split(",");
//        long currentMoney = this.userService.getMoneyUserCache(username, this.moneyTypeStr);
//        UserCacheModel u = this.userService.getUser(username);
//        long totalBetValue = lineArr.length * this.betValue;
//        ResultPokeGoMsg msg = new ResultPokeGoMsg();
//        if (lineArr.length > 0 && !linesStr.isEmpty()) {
//            if (totalBetValue > 0L) {
//                if (totalBetValue <= currentMoney) {
//                    MoneyResponse moneyRes = this.userService.updateMoney(username, -totalBetValue, this.moneyTypeStr, Games.CANDY.getName(), "Quay "+this.gameName, "\u0110\u1eb7t c\u01b0\u1ee3c Quay "+this.gameName, 0L, Long.valueOf(refernceId), TransType.START_TRANS);
//                    if (moneyRes != null && moneyRes.isSuccess()) {
//                        long fee = totalBetValue * 2L / 100L;
//                        long moneyToPot = totalBetValue * 1L / 100L;
//                        long moneyToFund = totalBetValue - fee - moneyToPot;
//                        if(!u.isBot()){
//                            this.fund += moneyToFund;
//                        }
//
//                        this.pot += moneyToPot;
//                        boolean enoughPair = false;
//                        ArrayList<AwardsOnLine> awardsOnLines = new ArrayList<AwardsOnLine>();
//                        long totalPrizes = 0L;
//                        block4 :
//                        while (!enoughPair) {
//                            result = 0;
//                            awardsOnLines.clear();
//                            totalPrizes = 0L;
//                            String linesWin = "";
//                            String prizesOnLine = "";
//                            boolean forceNoHu = false;
//                            if (lineArr.length >= 5) {
//                                if ((soLanNoHu > 0) && (fund > pot * 2L)) {
//                                    rd = new Random();
//                                    if (rd.nextInt(soLanNoHu) == 0)
//                                        forceNoHu = true;
//                                }
//                            }
//                            Item[][] matrix = forceNoHu ? PokeGoUtils.generateMatrixNoHu(lineArr) : PokeGoUtils.generateMatrix();
////                            Item[][] matrix = PokeGoUtils.generateMatrixNoHu(lineArr);
//                            for (int i = 0; i < lineArr.length; ++i) {
//                                String entry = lineArr[i];
//                                ArrayList<Award> awardList = new ArrayList<Award>();
//                                Line line = PokeGoUtils.getLine(this.lines, matrix, Integer.parseInt(entry));
//                                PokeGoUtils.calculateLine(line, awardList);
//                                for (Award award : awardList) {
//                                    long money = 0L;
//                                    if (award != Award.TRIPLE_POKER_BALL) {
//                                        money = (long)(award.getRatio() * (float)this.betValue);
//                                    } else {
//                                        for (AwardsOnLine e : awardsOnLines) {
//                                            if (e.getAward() != Award.TRIPLE_POKER_BALL) continue;
//                                            continue block4;
//                                        }
//                                        result = 3;
//                                        money = this.huX2 ? this.pot * 2L : this.pot;
//                                    }
//                                    AwardsOnLine aol = new AwardsOnLine(award, money, line.getName());
//                                    awardsOnLines.add(aol);
//                                }
//                            }
//                            StringBuilder builderLinesWin = new StringBuilder();
//                            StringBuilder builderPrizesOnLine = new StringBuilder();
//                            for (AwardsOnLine entry2 : awardsOnLines) {
//                                if (entry2.getAward() == Award.TRIPLE_POKER_BALL) {
//                                    // Chan nguoi choi no hu
//                                     if (!u.isBot()) continue block4;
//                                     if(!forceNoHu) continue block4;
//                                    totalPrizes += this.pot;
//                                } else {
//                                    totalPrizes += entry2.getMoney();
//                                }
//                                builderLinesWin.append(",");
//                                builderLinesWin.append(entry2.getLineId());
//                                builderPrizesOnLine.append(",");
//                                builderPrizesOnLine.append(entry2.getMoney());
//                            }
//                            if (builderLinesWin.length() > 0) {
//                                builderLinesWin.deleteCharAt(0);
//                            }
//                            if (builderPrizesOnLine.length() > 0) {
//                                builderPrizesOnLine.deleteCharAt(0);
//                            }
//                            if(!u.isBot()){
//                                if (result == 3 ? this.fund - 2 * pot < 0L : this.fund - totalPrizes < 0L) continue;
//                            }
//                            enoughPair = true;
//                            if (totalPrizes > 0L) {
//                                if (result == 3) {
//                                    if (this.huX2) {
//                                        totalPrizes += this.pot;
//                                        result = 4;
//                                    }
//                                    this.noHuX2();
//                                    if (this.moneyType == 1) {
//                                        //GameUtils.sendSMSToUser(username, "Chuc mung " + username + " da no hu game Kim Cuong phong " + this.betValue + ". So tien no hu: " + totalPrizes + " Vin");
//                                    }
//                                    this.pot = this.initPotValue;
//                                    this.fund -= this.initPotValue;
//                                } else {
//                                    if(!u.isBot())
//                                        this.fund -= totalPrizes;
//                                    result = totalPrizes >= (long)(this.betValue * 100) ? (short)2 : 1;
//                                }
//                            }
//                            moneyRes = this.userService.updateMoney(username, totalPrizes, this.moneyTypeStr, Games.CANDY.getName(), "Quay "+this.gameName, this.buildDescription(totalBetValue, totalPrizes, result), fee, Long.valueOf(refernceId), TransType.END_TRANS);
//                            long moneyExchange = totalPrizes - (long)this.betValue;
//                            if (moneyRes != null && moneyRes.isSuccess()) {
//                                currentMoney = moneyRes.getCurrentMoney();
//                                if (this.moneyType == 1 && moneyExchange >= (long)BroadcastMessageServiceImpl.MIN_MONEY) {
//                                    this.broadcastMsgService.putMessage(Games.CANDY.getId(), username, moneyExchange);
//                                }
//                            }
//                            linesWin = builderLinesWin.toString();
//                            prizesOnLine = builderPrizesOnLine.toString();
//                            msg.matrix = PokeGoUtils.matrixToString(matrix);
//                            msg.linesWin = linesWin;
//                            msg.prize = totalPrizes;
//                            try {
//                                if(!u.isBot()){
//                                    this.pgService.logPokeGo(refernceId, username, (long)this.betValue, linesStr, linesWin, prizesOnLine, result, totalPrizes, this.moneyType, currentTimeStr);
//
//                                }
//                                if (result == 3 || result == 4) {
//                                    this.pgService.addTop(username, this.betValue, totalPrizes, (int)this.moneyType, currentTimeStr, (int)result);
//                                }
//                            }
//                            catch (InterruptedException awardList) {
//                            }
//                            catch (TimeoutException awardList) {
//                            }
//                            catch (IOException awardList) {
//                                // empty catch block
//                            }
//                            this.saveFund();
//                            this.savePot();
//                        }
//                    }
//                } else {
//                    result = 102;
//                }
//            } else {
//                result = 101;
//            }
//        } else {
//            result = 101;
//        }
//        msg.result = (byte)result;
//        msg.currentMoney = currentMoney;
//        long endTime = System.currentTimeMillis();
//        long handleTime = endTime - startTime;
//        String ratioTime = CommonUtils.getRatioTime((long)handleTime);
//        PokeGoUtils.log(refernceId, username, this.betValue, msg.matrix, result, this.moneyType, handleTime, ratioTime, currentTimeStr);
//        return msg;
//    }

    public short play(User user, String linesStr) {
        String username = user.getName();
        ResultPokeGoMsg msg = this.play(username, linesStr);
        this.sendMessageToUser((BaseMsg) msg, user);
        return msg.result;
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
            UpdatePotPokeGoMsg msg = new UpdatePotPokeGoMsg();
            msg.value = this.pot;
            msg.x2 = this.isMultiJackpot() ? (byte) 1 : 0;
            this.sendMessageToRoom(msg);
            this.lastTimeUpdatePotToRoom = currentTime;
            try {
                this.mgService.savePot(this.name, this.pot, this.isMultiJackpot());
            } catch (IOException | InterruptedException | TimeoutException e) {
                Debug.trace((Object[]) new Object[]{"MINI POKER: update pot poker error ", e.getMessage()});
            }
        }
    }

    public void updatePotToUser(User user) {
        UpdatePotPokeGoMsg msg = new UpdatePotPokeGoMsg();
        msg.value = this.pot;
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    private boolean checkDieuKienNoHu(String username) {
        try {
            UserModel u = this.userService.getUserByUserName(username);
            return u.isBot();
        } catch (Exception u) {
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void gameLoop() {
        Map<String, AutoUserPokeGo> map;
        ArrayList<AutoUserPokeGo> usersPlay = new ArrayList<AutoUserPokeGo>();
        Map<String, AutoUserPokeGo> map2 = map = this.usersAuto;
        synchronized (map2) {
            for (AutoUserPokeGo user : this.usersAuto.values()) {
                boolean play = user.incCount();
                if (!play) continue;
                usersPlay.add(user);
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
            PlayListPokeGoTask task = new PlayListPokeGoTask(tmp);
            this.executor.execute(task);
        }
        usersPlay.clear();
    }

    public void playListPokeGo(List<AutoUserPokeGo> users) {
        for (AutoUserPokeGo user : users) {
            short result = this.play(user.getUser(), user.getLines());
            if (result == 3 || result == 4 || result == 101 || result == 102 || result == 100) {
                this.forceStopAutoPlay(user.getUser());
                continue;
            }
            if (result == 0) {
                user.setMaxCount(4);
                continue;
            }
            user.setMaxCount(8);
        }
        users.clear();
    }

    @Override
    public boolean joinRoom(User user) {
        boolean result = super.joinRoom(user);
        if (result) {
            user.setProperty((Object) "MGROOM_" + this.gameName + "_INFO", (Object) this);
        }
        return result;
    }

    public void startHuX2() {

    }

    public void stopHuX2() {

    }

    public void noHuX2() {

    }

    private void calculatHuX2() {

    }

    private String buildDescription(long totalBet, long totalPrizes, short result) {
        if (totalBet == 0L) {
            return this.resultToString(result) + ": " + totalPrizes;
        }
        return "Quay: " + totalBet + ", " + this.resultToString(result) + ": " + totalPrizes;
    }

    private String resultToString(short result) {
        switch (result) {
            case 3: {
                return "N\u1ed5 h\u0169";
            }
            case 4: {
                return "N\u1ed5 h\u0169 X2";
            }
            case 1: {
                return "Th\u1eafng";
            }
            case 2: {
                return "Th\u1eafng l\u1edbn";
            }
        }
        return "Tr\u01b0\u1ee3t";
    }

    private final class GameLoopTask
            implements Runnable {
        private GameLoopTask() {
        }

        @Override
        public void run() {
            try {
                MGRoomCandy.this.gameLoop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ResultPokeGo {
        public static final short LOI_HE_THONG = 100;
        public static final short DAT_CUOC_KHONG_HOP_LE = 101;
        public static final short KHONG_DU_TIEN = 102;
        public static final short TRUOT = 0;
        public static final short THANG = 1;
        public static final short THANG_LON = 2;
        public static final short NO_HU = 3;
        public static final short NO_HU_X2 = 4;
    }

    private final class PlayListPokeGoTask
            extends Thread {
        private List<AutoUserPokeGo> users;

        private PlayListPokeGoTask(List<AutoUserPokeGo> users) {
            this.users = users;
            this.setName("AutoPlayPokeGo");
        }

        @Override
        public void run() {
            MGRoomCandy.this.playListPokeGo(this.users);
        }
    }

}

