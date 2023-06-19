/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  bitzero.server.entities.User
 *  bitzero.server.extensions.data.BaseMsg
 *  bitzero.util.common.business.Debug
 *  com.vinplay.dal.service.BauCuaService
 *  com.vinplay.dal.service.BroadcastMessageService
 *  com.vinplay.dal.service.MiniGameService
 *  com.vinplay.dal.service.impl.BauCuaServiceImpl
 *  com.vinplay.dal.service.impl.BroadcastMessageServiceImpl
 *  com.vinplay.dal.service.impl.MiniGameServiceImpl
 *  com.vinplay.usercore.service.UserService
 *  com.vinplay.usercore.service.impl.UserServiceImpl
 *  com.vinplay.vbee.common.enums.Games
 *  com.vinplay.vbee.common.models.minigame.baucua.ResultBauCua
 *  com.vinplay.vbee.common.models.minigame.baucua.TransactionBauCua
 *  com.vinplay.vbee.common.models.minigame.baucua.TransactionBauCuaDetail
 *  com.vinplay.vbee.common.response.MoneyResponse
 *  com.vinplay.vbee.common.statics.TransType
 *  com.vinplay.vbee.common.utils.CommonUtils
 */
package game.modules.minigame.room;

import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import com.vinplay.dal.service.BauCuaService;
import com.vinplay.dal.service.BroadcastMessageService;
import com.vinplay.dal.service.MiniGameService;
import com.vinplay.dal.service.impl.BauCuaServiceImpl;
import com.vinplay.dal.service.impl.BroadcastMessageServiceImpl;
import com.vinplay.dal.service.impl.MiniGameServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.models.minigame.baucua.ResultBauCua;
import com.vinplay.vbee.common.models.minigame.baucua.TransactionBauCua;
import com.vinplay.vbee.common.models.minigame.baucua.TransactionBauCuaDetail;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;
import com.vinplay.vbee.common.utils.CommonUtils;
import game.modules.minigame.cmd.send.baucua.BauCuaInfoMsg;
import game.modules.minigame.cmd.send.baucua.BauCuaLichSuPhienMsg;
import game.modules.minigame.cmd.send.baucua.ResultBetBauCuaMsg;
import game.modules.minigame.cmd.send.baucua.UpdateBauCuaPerSecondMsg;
import game.modules.minigame.cmd.send.baucua.UpdateBauCuaPrizeMsg;
import game.modules.minigame.cmd.send.baucua.UpdateBauCuaResultMsg;
import game.modules.minigame.entities.BotBauCua;
import game.modules.minigame.entities.BotMinigame;
import game.modules.minigame.entities.MinigameConstant;
import game.modules.minigame.entities.Pot;
import game.modules.minigame.entities.PotBauCua;
import game.modules.minigame.room.MGRoom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class MGRoomBauCua
        extends MGRoom {
    public byte id;
    public long fund;
    private List<PotBauCua> pots = new ArrayList<PotBauCua>();
    private byte moneyType;
    private String moneyTypeStr;
    private long referenceId;
    private float tax = MinigameConstant.MINIGAME_TAX_VIN;
    private int minBetValue;
    private UserService userService = new UserServiceImpl();
    private BauCuaService bcService = new BauCuaServiceImpl();
    private MiniGameService mgService = new MiniGameServiceImpl();
    private Map<String, TransactionBauCua> transactionsMap = new HashMap<String, TransactionBauCua>();

    private TransactionBauCua transactionBauCuaUser = new TransactionBauCua();
    private byte[] dices;
    private byte xPot;
    private byte xValue;
    private ResultBauCua resultBC = new ResultBauCua();
    private List<ResultBauCua> lichSuPhien = new ArrayList<ResultBauCua>();
    private List<BotBauCua> botBC = new ArrayList<BotBauCua>();
    private BroadcastMessageService broadcastMsgService = new BroadcastMessageServiceImpl();
    public static final String GAME_NAME = "BauCua";
    private static final byte BETTING_SUCCESS = 1;
    private static final byte BETTING_FAIL = 100;
    private static final byte INVALID_BETTING_STATE = 101;
    private static final byte NOT_ENOUGH_MONEY = 102;

    public MGRoomBauCua(String name, int minBetValue, byte moneyType, byte id, long fund) {
        super(name);
        this.id = id;
        this.fund = fund;
        this.moneyType = moneyType;
        if (moneyType == 1) {
            this.moneyTypeStr = "vin";
        } else if (moneyType == 0) {
            this.moneyTypeStr = "xu";
        }
        this.minBetValue = minBetValue;
        if (this.moneyType == 0) {
            this.tax = MinigameConstant.MINIGAME_TAX_XU;
        }
        for (int i = 0; i < 6; ++i) {
            this.pots.add(new PotBauCua(i));
        }
        this.lichSuPhien = this.bcService.getLichSuPhien(30, id);
        Debug.trace((Object) ("Lich su phien " + this.buildLichSuPhien()));
    }

    public void updateBauCuaInfoToUser(User user, byte remainTime, boolean bettingState) {
        BauCuaInfoMsg msg = new BauCuaInfoMsg();
        msg.referenceId = this.referenceId;
        msg.remainTime = remainTime;
        msg.potData = this.buildPotData();
        msg.betData = this.buildBetData(user.getName());
        msg.lichSuPhien = this.buildLichSuPhien();
        msg.bettingState = bettingState;
        msg.dice1 = this.resultBC.dices[0];
        msg.dice2 = this.resultBC.dices[1];
        msg.dice3 = this.resultBC.dices[2];
        msg.xPot = this.resultBC.xPot;
        msg.xValue = this.resultBC.xValue;
        msg.room = this.id;
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    public void startNewGame(long newReferenceId) {
        transactionBauCuaUser.betValues = new long[6];
        this.referenceId = newReferenceId;
        for (PotBauCua entry : this.pots) {
            entry.renew();
        }
        this.transactionsMap.clear();
        this.resultBC = new ResultBauCua(this.referenceId, this.id, this.minBetValue);
        this.botBC = BotMinigame.getBotBauCua(this.id);
        Debug.trace((Object) ("BOT BAU CUA ROOM " + this.id + ", size= " + this.botBC.size()));
    }

    public void botBet(int time, boolean bettingState) {
        for (BotBauCua b : this.botBC) {
            if (b.getTimeBetting() != time) continue;
            this.bet(b.getNickname(), b.getBetStr(), bettingState);
        }
    }

    public ResultBetBauCuaMsg bet(String username, String betStr, boolean bettingState) {
        long currentMoney;
        int result;
        ResultBetBauCuaMsg msg;
        block15:
        {
            long totalBetValue = 0L;
            result = 100;
            msg = new ResultBetBauCuaMsg();
            currentMoney = this.userService.getMoneyUserCache(username, this.moneyTypeStr);
            if (bettingState) {
                String[] arr = betStr.split(",");
                if (arr.length == 6) {
                    long[] betValues = new long[6];
                    try {
                        int i;
                        for (i = 0; i < 6; ++i) {
                            betValues[i] = Long.parseLong(arr[i]);
                            if (betValues[i] < 0L) {
                                throw new NumberFormatException();
                            }
                            totalBetValue += betValues[i];
                        }
                        if (totalBetValue <= 0L) break block15;
                        if (totalBetValue <= currentMoney) {
                            long fee = (long) ((float) totalBetValue * this.tax / 100.0f);
                            MoneyResponse response = new MoneyResponse(false, "1001");
                            if (!this.isBot(username)) {
                                response = this.userService.updateMoney(username, -totalBetValue, this.moneyTypeStr, GAME_NAME, "B\u1ea7u cua: \u0110\u1eb7t c\u01b0\u1ee3c", "Phi\u00ean " + this.referenceId, fee, Long.valueOf(this.referenceId), TransType.START_TRANS);

                            } else {
                                response.setSuccess(true);
                            }
                            if (!response.isSuccess()) break block15;
                            if (!bettingState) {
                                result = 1;
                                if (!isBot(username)) {
                                    this.userService.updateMoney(username, totalBetValue, this.moneyTypeStr, GAME_NAME, "B\u1ea7u cua: Tr\u1ea3 c\u01b0\u1ee3c", "Ho\u00e0n tr\u1ea3 \u0111\u1eb7t c\u01b0\u1ee3c phi\u00ean " + this.referenceId, 0L, Long.valueOf(this.referenceId), TransType.END_TRANS);
                                    break block15;
                                }

                            }

                            this.fund += totalBetValue - fee;
                            if (!this.transactionsMap.containsKey(username)) {
                                TransactionBauCua newTransaction = new TransactionBauCua();
                                newTransaction.username = username;
                                newTransaction.moneyType = this.moneyType;
                                newTransaction.referenceId = this.referenceId;
                                newTransaction.room = this.minBetValue;
                                newTransaction.betValues = betValues;
                                this.transactionsMap.put(username, newTransaction);
                            } else {
                                TransactionBauCua transaction = this.transactionsMap.get(username);
                                for (i = 0; i < 6; ++i) {
                                    long[] arrl = transaction.betValues;
                                    int n = i;
                                    arrl[n] = arrl[n] + betValues[i];
                                }
                                this.transactionsMap.put(username, transaction);
                            }
                            if (!isBot(username)) {
                                for (i = 0; i < 6; ++i) {
                                    transactionBauCuaUser.betValues[i] += betValues[i];
                                }
                                Debug.trace("BAU CUA BET USER " + transactionBauCuaUser.betValues[0] + " " + transactionBauCuaUser.betValues[1] + " " + transactionBauCuaUser.betValues[2] + " " + transactionBauCuaUser.betValues[3] + " " + transactionBauCuaUser.betValues[4] + " " + transactionBauCuaUser.betValues[5] + ", bettingState = " + bettingState);
                            }


                            TransactionBauCuaDetail tranDetail = new TransactionBauCuaDetail();
                            tranDetail.username = username;
                            tranDetail.referenceId = this.referenceId;
                            tranDetail.moneyType = this.moneyType;
                            tranDetail.room = this.minBetValue;
                            tranDetail.betValues = betValues;
                            currentMoney = response.getCurrentMoney();
                            for (i = 0; i < 6; ++i) {
                                if (betValues[i] <= 0L) continue;
                                this.pots.get(i).bet(tranDetail.username, tranDetail.betValues[i]);
                            }
                            try {
                                this.mgService.saveFund(this.name, this.fund);
                                if (!isBot(username)) {
                                    this.bcService.saveTransactionBauCuaDetail(tranDetail);
                                }

                            } catch (IOException | InterruptedException | TimeoutException arrl) {
                                // empty catch block
                            }
                            result = 1;
                            if (!isBot(username))
                                Debug.trace("BAU CUA BET 4 ");
                            break block15;
                        }
                        result = 102;
                    } catch (NumberFormatException e) {
                        Debug.trace((Object) ("Bet value: " + betStr + " incorrect: " + e.getMessage()));
                    }
                }
            } else {
                result = 101;
            }
        }

        msg.result = (byte) result;
        msg.currentMoney = currentMoney;
        return msg;
    }

    public void bet(User user, String betStr, boolean bettingState) {
        ResultBetBauCuaMsg msg = this.bet(user.getName(), betStr, bettingState);
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    public void finishGame() {
    }

    public void updateBauCuaPerSecond(byte remainTime, boolean bettingState) {
        UpdateBauCuaPerSecondMsg msg = new UpdateBauCuaPerSecondMsg();
        msg.potData = this.buildPotData();
        msg.remainTime = remainTime;
        msg.bettingState = bettingState;
        this.sendMessageToRoom(msg);
    }

    public long calculatePrizes() {
        int[] tiLe = this.calculateTiLe(this.dices);
        long totalVinPay = 0L;
        long[] totalBetValuesInRoom = new long[6];
        long[] totalPrizesInRoom = new long[6];
        for (TransactionBauCua tran : this.transactionsMap.values()) {
            long totalPrize = 0L;
            long totalBetValues = 0L;
            for (int i = 0; i < 6; ++i) {
                int n;
                long[] arrl;
                if (tiLe[i] > 0) {
                    totalPrize += tran.betValues[i] * (long) tiLe[i] + tran.betValues[i];
                    tran.prizes[i] = tran.betValues[i] * (long) tiLe[i] + tran.betValues[i];
                    arrl = totalPrizesInRoom;
                    n = i;
                    arrl[n] = arrl[n] + (tran.betValues[i] * (long) tiLe[i] + tran.betValues[i]);
                }
                totalBetValues += tran.betValues[i];
                arrl = totalBetValuesInRoom;
                n = i;
                arrl[n] = arrl[n] + tran.betValues[i];
            }
            if (totalPrize > 0L) {
                MoneyResponse response;
                if (this.moneyType == 1) {
                    totalVinPay += totalPrize;
                }
                if (!isBot(tran.username) && (response = this.userService.updateMoney(tran.username, totalPrize, this.moneyTypeStr, GAME_NAME, "B\u1ea7u cua: Tr\u1eadn th\u1eafng", "Phi\u00ean " + this.referenceId, 0L, Long.valueOf(this.referenceId), TransType.END_TRANS)) != null && response.isSuccess()) {
                    UpdateBauCuaPrizeMsg msg = new UpdateBauCuaPrizeMsg();
                    msg.prize = totalPrize;
                    msg.currentMoney = response.getCurrentMoney();
                    msg.room = this.id;
                    this.sendMessageToUser((BaseMsg) msg, tran.username);
                    if (this.moneyType == 1 && totalPrize >= (long) BroadcastMessageServiceImpl.MIN_MONEY) {
                        this.broadcastMsgService.putMessage(Games.BAU_CUA.getId(), tran.username, totalPrize);
                    }
                }
            }
            try {
                tran.totalExchange = totalPrize - totalBetValues;
                tran.dices = CommonUtils.arrayByteToString((byte[]) this.resultBC.dices);
                if (!isBot(tran.username)) {
                    this.bcService.saveTransactionBauCua(tran);
                }

            } catch (IOException | InterruptedException | TimeoutException response) {
                // empty catch block
            }
            this.fund -= totalPrize;
            try {
                this.mgService.saveFund(this.name, this.fund);
            } catch (IOException | InterruptedException | TimeoutException response) {
            }
        }
        this.resultBC.totalBetValues = totalBetValuesInRoom;
        this.resultBC.totalPrizes = totalPrizesInRoom;
        try {
            this.bcService.saveResultBauCua(this.resultBC);
            if (this.moneyType == 1) {
                ArrayList<TransactionBauCua> list = new ArrayList<TransactionBauCua>(this.transactionsMap.values());
                Debug.trace((Object) ("TRANSACTION BAU CUA: " + list.size()));
                this.bcService.calculteToiChonCa(this.dices, list);
            }
        } catch (IOException | InterruptedException | TimeoutException list) {
            // empty catch block
        }
        return totalVinPay;
    }

    public long tryCalculatePrizes(int[] tiLe) {
        long totalValues = 0L;
        for (TransactionBauCua tran : this.transactionsMap.values()) {
            for (int i = 0; i < 6; ++i) {
                totalValues += tran.betValues[i] * (long) tiLe[i] + tran.betValues[i];
            }
        }
        return totalValues;
    }

    public String buildPotData() {
        StringBuilder builder = new StringBuilder();
        for (PotBauCua pot : this.pots) {
            builder.append(pot.getTotalValue());
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public String buildBetData(String username) {
        StringBuilder builder = new StringBuilder();
        for (PotBauCua pot : this.pots) {
            builder.append(pot.getTotalBetByUsername(username));
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    @Override
    public boolean joinRoom(User user) {
        boolean result = super.joinRoom(user);
        if (result) {
            user.setProperty((Object) "MGROOM_BAU_CUA_INFO", (Object) this);
        }
        return result;
    }

    public float getTax() {
        return this.tax;
    }

    public byte getMoneyType() {
        return this.moneyType;
    }

    private byte[] generateDices() {
        byte[] dices;
//        block1 : {
//            int num = 0;
//            do {
        long tongthang = 0, tongthua = 0;
        while (true) {
            tongthang = 0;
            tongthua = 0;
            Random rd = new Random();
            dices = new byte[]{(byte) rd.nextInt(6), (byte) rd.nextInt(6), (byte) rd.nextInt(6)};
            for (int i = 0; i < 6; i++) {
                boolean check = false;
                for (int j = 0; j < 3; j++) {
                    if (i == dices[j]) {
                        check = true;
                        tongthang += transactionBauCuaUser.betValues[i];
                    }
                }
                if (!check) tongthua += transactionBauCuaUser.betValues[i];
            }
            Debug.trace("BAU CUA BET GEN RESULT tongthang: " + tongthang + " tongthua: " + tongthua + " KETQUA: " + dices[0] + " " + dices[1] + " " + dices[2]);
            if (tongthang <= tongthua) {
                break;
            }
        }

        this.xPot = this.randomXPot();
        this.xValue = this.randomXValue();
//        int[] tiLe = this.calculateTiLe(dices);
//        long totalPrizes = this.tryCalculatePrizes(tiLe);

//                Debug.trace("MGRoomBauCua fund: " + fund + "  totalPrizes: " + totalPrizes + " dices: " + dices[0] +" " + dices[1] +" " +dices[2]);
//
//                if (this.fund - totalPrizes > 0L) break block1;
//            } while (++num <= 3);
        return dices;
//        }
//        return dices;
    }

    private byte[] traGiaiBeNhat() {
        byte[] dices = new byte[3];
        ArrayList<Pot> potsTmp = new ArrayList<Pot>();
        ArrayList<Integer> indexPot = new ArrayList<Integer>();
        for (int j = 0; j < this.pots.size(); ++j) {
            Pot p = this.pots.get(j);
            boolean added = false;
            for (int i = 0; i < potsTmp.size(); ++i) {
                Random rd;
                int n;
                Pot pt = (Pot) potsTmp.get(i);
                if (p.getTotalValue() < pt.getTotalValue()) {
                    potsTmp.add(i, p);
                    indexPot.add(i, j);
                    added = true;
                    break;
                }
                if (p.getTotalValue() != pt.getTotalValue() || (n = (rd = new Random()).nextInt(2)) != 0) continue;
                potsTmp.add(i, p);
                indexPot.add(i, j);
                added = true;
                break;
            }
            if (added) continue;
            potsTmp.add(p);
            indexPot.add(j);
        }
        for (int i = 0; i < 3; ++i) {
            dices[i] = ((Integer) indexPot.get(i)).byteValue();
        }
        this.xPot = 1;
        this.xValue = 1;
        return dices;
    }

    private int[] calculateTiLe(byte[] dices) {
        int[] tiLe = new int[6];
        for (int i = 0; i < 6; ++i) {
            tiLe[i] = 0;
            for (int j = 0; j < dices.length; ++j) {
                if (i != dices[j]) continue;
                int[] arrn = tiLe;
                int n = i;
                arrn[n] = arrn[n] + 1;
            }
            if (tiLe[i] <= 0 || i != this.xPot) continue;
            int[] arrn = tiLe;
            int n = i;
            arrn[n] = arrn[n] * this.xValue;
        }
        return tiLe;
    }

    public void generateResult() {
        this.dices = this.generateDices();
        this.resultBC.referenceId = this.referenceId;
        this.resultBC.dices = this.dices;
        this.resultBC.xPot = this.xPot;
        this.resultBC.xValue = this.xValue;
        Debug.trace((Object) ("BAU CUA " + this.id + " DICES: " + this.dices[0] + "," + this.dices[1] + "," + this.dices[2] + ", xPot= " + this.xPot + ", xValue= " + this.xValue));
        UpdateBauCuaResultMsg msg = new UpdateBauCuaResultMsg();
        msg.dice1 = this.dices[0];
        msg.dice2 = this.dices[1];
        msg.dice3 = this.dices[2];
        msg.xPot = this.xPot;
        msg.xValue = this.xValue;
        this.sendMessageToRoom(msg);
        this.lichSuPhien.add(this.resultBC);
        if (this.lichSuPhien.size() > 30) {
            this.lichSuPhien.remove(0);
        }
    }

    private byte randomXPot() {
        Random rd = new Random();
        return (byte) rd.nextInt(6);
    }

    private byte randomXValue() {
        Random rd = new Random();
        int n = rd.nextInt(20);
        if (n == 0) {
            return 3;
        }
        if (1 <= n && n <= 7) {
            return 2;
        }
        return 1;
    }

    public boolean isBot(String username) {
        UserCacheModel model = this.userService.getUser(username);
        return model.isBot();
    }

    public void getLichSuPhien(User user) {
        BauCuaLichSuPhienMsg msg = new BauCuaLichSuPhienMsg();
        msg.data = this.buildLichSuPhien();
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    private String buildLichSuPhien() {
        StringBuilder builder = new StringBuilder();
        for (ResultBauCua entry : this.lichSuPhien) {
            builder.append(entry.dices[0]);
            builder.append(",");
            builder.append(entry.dices[1]);
            builder.append(",");
            builder.append(entry.dices[2]);
            builder.append(",");
            builder.append(entry.xPot);
            builder.append(",");
            builder.append(entry.xValue);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}

