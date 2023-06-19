package game.modules.minigame.room;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dal.config.CacheConfig;
import com.vinplay.dal.entities.report.ReportMoneySystemModel;
import com.vinplay.dal.entities.taixiu.ResultTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiuDetail;
import com.vinplay.dal.service.BroadcastMessageService;
import com.vinplay.dal.service.CacheService;
import com.vinplay.dal.service.MiniGameService;
import com.vinplay.dal.service.TaiXiuService;
import com.vinplay.dal.service.impl.BroadcastMessageServiceImpl;
import com.vinplay.dal.service.impl.CacheServiceImpl;
import com.vinplay.dal.service.impl.MiniGameServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;

import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import game.modules.TaiXiu.TaiXiuUtil;
import game.modules.description.TaiXiuDescription.TaiXiuDescriptionUtils;
import game.modules.minigame.cmd.rev.BetTaiXiuCmd;
import game.modules.minigame.cmd.send.BetTaiXiuMsg;
import game.modules.minigame.cmd.send.TaiXiuInfoMsg;
import game.modules.minigame.cmd.send.TaiXiuJackpotMsg;
import game.modules.minigame.cmd.send.TaiXiuRefundMsg;
import game.modules.minigame.cmd.send.UpdatePrizeTaiXiuMsg;
import game.modules.minigame.cmd.send.UpdateResultDicesMsg;
import game.modules.minigame.cmd.send.UpdateTaiXiuPerSecondMsg;
import game.modules.minigame.entities.BalanceMoneyTX;
import game.modules.minigame.entities.MinigameConstant;
import game.modules.minigame.entities.PotTaiXiu;
import game.modules.minigame.utils.TaiXiuUtils;
import game.utils.ConfigGame;


import game.modules.minigame.room.MGRoom;
import org.bson.Document;

//import game.modules.minigame.utils.TaiXiuUtils;
//import game.utils.ConfigGame;
public class MGRoomTaiXiu extends MGRoom {
    public long referenceId;
    private short moneyType;
    private String moneyTypeStr;
    private PotTaiXiu potTai;
    private PotTaiXiu potXiu;
    private long startTime = 0L;
    private short result = (short) -1;
    private boolean bettingRound = false;
    private boolean enableBetting = false;
    private ResultTaiXiu resultTX;
    private TaiXiuService api = new TaiXiuServiceImpl();
    private UserService userService = new UserServiceImpl();
    private CacheService cacheService = new CacheServiceImpl();
    private BroadcastMessageService broadcastMsgService = new BroadcastMessageServiceImpl();
    private float tax = MinigameConstant.MINIGAME_TAX_TX;

    private float taxJp = MinigameConstant.MINIGAME_TAX_TX_JACKPOT;
    private BalanceMoneyTX balance = new BalanceMoneyTX();
    private long blackListBetTai = 0L;
    private long blackListBetXiu = 0L;
    private long whiteListBetTai = 0L;
    private long whiteListBetXiu = 0L;

    private boolean flagJpTai = false;
    private boolean flagJpXiu = false;

    private static boolean isJpTai = false;
    private static boolean isJpXiu = false;
    private long fundTaiXiu = 0;
    private static volatile long fundJpTai = 0;
    private static volatile long fundJpXiu = 0;
    private static long fundJpFakeTai = 0;
    private static long fundJpFakeXiu = 0;
    private static long nextJpTai = 5, nextJpXiu = 5;

    private long minMoneyJp = 10000000;
    private static AtomicInteger winCount = new AtomicInteger(0);

    private static AtomicInteger countJpTai = new AtomicInteger(0);

    private static AtomicInteger countJpXiu = new AtomicInteger(0);

    private MiniGameService mgService = new MiniGameServiceImpl();
    public long realPotTai = 0;
    public long realPotXiu = 0;
    public short realNumBetTai = 0;
    public short realNumBetXiu = 0;

    public MGRoomTaiXiu(String name, long referenceId, short moneyType, long fundTaiXiu, long fundJpTais, long fundJpXius, long jpFkTais, long jpFkXius) {
        super(name);
        this.moneyType = moneyType;
        this.moneyTypeStr = "xu";
        if (moneyType == 1) {
            this.moneyTypeStr = "vin";
            this.tax = MinigameConstant.MINIGAME_TAX_TX;
            this.taxJp = MinigameConstant.MINIGAME_TAX_TX_JACKPOT;
            ReportMoneySystemModel model = this.api.getReportTX(ConfigGame.getIntValue("interval_reset_balance", 10));
            if (model != null) {
                this.balance = new BalanceMoneyTX(model.moneyWin, model.moneyLost, model.fee, model.dateReset);
                Debug.trace(("TAI XIU VIN, win=" + model.moneyWin + ", loss=" + model.moneyLost + ", fee= " + model.fee + ", date reset= " + model.dateReset));
            }
        } else {
            this.tax = MinigameConstant.MINIGAME_TAX_TX;
        }
        this.potTai = new PotTaiXiu();
        this.potXiu = new PotTaiXiu();
        this.referenceId = referenceId;

        this.fundTaiXiu = fundTaiXiu;
        fundJpTai = fundJpTais;
        fundJpXiu = fundJpXius;
        fundJpFakeTai = jpFkTais;
        fundJpFakeXiu = jpFkXius;
        Debug.trace("TAI XIU VIN: moneyTypeStr = " + this.moneyTypeStr + " ,fundJpTai= " + fundJpTai + ",fundJpXiu=" + fundJpXiu);
    }

    public void startNewGame(long newReferenceId) {
        this.referenceId = newReferenceId;
        this.bettingRound = true;
        this.enableBetting = true;
        this.blackListBetTai = 0L;
        this.blackListBetXiu = 0L;
        this.whiteListBetTai = 0L;
        this.whiteListBetXiu = 0L;
        this.realPotTai = 0;
        this.realPotXiu = 0;
        this.realNumBetTai = 0;
        this.realNumBetXiu = 0;
        this.potTai.renew();
        this.potXiu.renew();
        this.startTime = System.currentTimeMillis();
        Debug.trace("START NEW ROUND " + this.referenceId);
        clearUserBetToDb();
    }

    public void finish() {
        this.resultTX = null;
        this.startTime = System.currentTimeMillis();
        this.bettingRound = false;
        try {
            this.cacheService.removeKey("allow_betting_" + this.referenceId);
            this.cacheService.removeKey("force_result_" + this.referenceId);
        } catch (Exception e) {

        }

    }

    public boolean isFlagJpTai() {
        return flagJpTai;
    }

    public void setFlagJpTai(boolean flagJpTai) {
        this.flagJpTai = flagJpTai;
    }

    public boolean isFlagJpXiu() {
        return flagJpXiu;
    }

    public void setFlagJpXiu(boolean flagJpXiu) {
        this.flagJpXiu = flagJpXiu;
    }

    public long getFundTaiXiu() {
        return fundTaiXiu;
    }

    public void setFundTaiXiu(long fundTaiXiu) {
        this.fundTaiXiu = fundTaiXiu;
    }

    public long getFundJpTai() {
        return fundJpTai;
    }

    public void setFundJpTai(long fundJpTais) {
        fundJpTai = fundJpTais;
    }

    public long getFundJpXiu() {
        return fundJpXiu;
    }

    public void setFundJpXiu(long fundJpXius) {
        fundJpXiu = fundJpXius;
    }

    public void disableBetting() {
        this.enableBetting = false;
        this.cacheService.setValue("allow_betting_" + this.referenceId, 0);
    }

    public void updateResultDices(short[] dices, short result) {
        this.result = result;
        UpdateResultDicesMsg msg = new UpdateResultDicesMsg();
        msg.result = result;
        msg.dice1 = dices[0];
        msg.dice2 = dices[1];
        msg.dice3 = dices[2];
        this.resultTX = new ResultTaiXiu();
        this.resultTX.referenceId = this.referenceId;
        this.resultTX.dice1 = msg.dice1;
        this.resultTX.dice2 = msg.dice2;
        this.resultTX.dice3 = msg.dice3;
        this.resultTX.result = msg.result;
        this.resultTX.moneyType = this.moneyType;
        this.sendMessageToRoom(msg);
    }

    public short getRemainTime() {
        long currentTime = System.currentTimeMillis();
        int remainTime = (int) ((currentTime - this.startTime) / 1000L);
        if (remainTime < 0) {
            remainTime = 0;
        } else if (remainTime > 60) {
            remainTime = 60;
        }
        if (this.bettingRound) {
            return (short) (60 - remainTime);
        }
        return (short) (30 - remainTime);
    }

    public void betTaiXiu(User user, BetTaiXiuCmd cmd) {
        //TODO
        boolean isUBot = isUserBot(user.getName());
        if (isUBot) {
            BetTaiXiuMsg msg = this.betTaiXiu(user.getName(), 0, cmd.betValue, cmd.inputTime, cmd.moneyType, cmd.betSide, true);
            this.sendMessageToUser(msg, user);
        } else {
            BetTaiXiuMsg msg = this.betTaiXiu(user.getName(), cmd.userId, cmd.betValue, cmd.inputTime, cmd.moneyType, cmd.betSide, false);
            this.sendMessageToUser(msg, user);
        }
    }

    public void insertUserBetToDb(String nickname, long betValue, int inputTime, int betSide) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("user_bet_tai_xiu");
        Document doc = new Document();
        doc.append("referentId", this.referenceId);
        doc.append("nick_name", nickname);
        doc.append("inputTime", inputTime);
        doc.append("betSide", betSide);
        doc.append("betValue", betValue);
        col.insertOne(doc);
    }

    public void clearUserBetToDb() {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("user_bet_tai_xiu");
        col.drop();
    }

    private boolean isUserBot(String nickName) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap userMap = client.getMap(CacheConfig.CACHE_USER_WIN);
        return userMap.containsKey(nickName) && (Boolean) userMap.get(nickName);
    }

    public BetTaiXiuMsg betTaiXiu(String nickname, int userId, long betValue, short inputTime, short moneyType, short betSide, boolean isBot) {
        boolean isLivestream = isUserBot(nickname);

        if (!isBot) {
            if (betSide == 1) {
                this.realPotTai += betValue;
                if (!this.potTai.hasBet(nickname)) {
                    this.realNumBetTai++;
                }
            } else {
                this.realPotXiu += betValue;
                if (!this.potXiu.hasBet(nickname)) {
                    this.realNumBetXiu++;
                }
            }
        }
        inputTime = this.getRemainTime();
        long currentMoney = this.userService.getMoneyUserCache(nickname, this.moneyTypeStr);
        int result = 2;
        if (this.enableBetting) {
            if (betValue >= 100L) {
                if (betValue > currentMoney) {
                    result = 3;
                } else {
                    TransactionTaiXiuDetail transTX = new TransactionTaiXiuDetail(this.referenceId, userId, nickname, betValue, betSide, inputTime, moneyType);
                    if (betSide == 1 && this.potXiu.getTotalBetByUsername(nickname) > 0L || betSide == 0 && this.potTai.getTotalBetByUsername(nickname) > 0L) {
                        result = 5;
                    } else {
                        //String betSideStr = betSide == 0 ? "X\u1ec9u" : "T\u00e0i";

                        MoneyResponse res = new MoneyResponse(false, "1001");
                        if (!isBot || isLivestream) {
                            res = this.userService.updateMoney(nickname, -betValue, this.moneyTypeStr, "TaiXiu",
                                    Games.TAI_XIU.getId() + "",
                                    TaiXiuDescriptionUtils.getTaiXiuBetDescription(Games.TAI_XIU.getId() + "",
                                            this.referenceId, inputTime + "", betSide),
                                    0L, Long.valueOf(this.referenceId), TransType.START_TRANS);

                        } else {
                            res.setSuccess(true);
                        }
                        if (res.isSuccess()) {
                            if (!this.enableBetting) {
                                result = 1;
                                if (!isBot || isLivestream) {
                                    this.userService.updateMoney(nickname, betValue, this.moneyTypeStr, "TaiXiuHoanTien",
                                            Games.TAI_XIU.getId() + "",
                                            TaiXiuDescriptionUtils.getTaiXiuTraCuocDescription(Games.TAI_XIU.getId() + "", this.referenceId),
                                            0L, Long.valueOf(this.referenceId), TransType.END_TRANS);

                                }
                            } else {
                                if (isLivestream) {
                                    isBot = true;
                                } else {
                                    isBot = this.isBot(nickname);
                                }
                                //isBot = this.isBot(nickname) || isLivestream;
                                if (moneyType == 1 && !isBot) {
                                    SplittableRandom rd = new SplittableRandom();
                                    int n;
                                    this.balance.addBet(betValue);
                                    if (betValue >= (long) ConfigGame.getIntValue("tx_min_money_black_list", 100000) && ConfigGame.inBlackList(nickname) && (n = rd.nextInt(100)) <= ConfigGame.getIntValue("tx_black_list_percent", 50)) {
                                        Debug.trace(("Black list " + nickname + " money= " + betValue + ", bet side= " + betSide));
                                        if (betSide == 1) {
                                            this.blackListBetTai += betValue;
                                        } else {
                                            this.blackListBetXiu += betValue;
                                        }
                                    }
                                    if (betValue >= (long) ConfigGame.getIntValue("tx_min_money_white_list", 100000) && ConfigGame.inWhiteList(nickname) && (n = rd.nextInt(100)) <= ConfigGame.getIntValue("tx_white_list_percent", 50)) {
                                        Debug.trace(("White list " + nickname + " money= " + betValue + ", bet side= " + betSide));
                                        if (betSide == 1) {
                                            this.whiteListBetTai += betValue;
                                        } else {
                                            this.whiteListBetXiu += betValue;
                                        }
                                    }
                                }
                                if (betSide == 1) {
                                    this.potTai.bet(transTX, isBot);
                                } else {
                                    this.potXiu.bet(transTX, isBot);
                                }
                                currentMoney = res.getCurrentMoney();

                                transTX.genTransactionCode();
                                if (!isBot) {
                                    TaiXiuUtils.logBetTaiXiu(transTX);
                                }
                                result = 0;
                            }
                            if (!isBot) {
                                try {
                                    insertUserBetToDb(nickname, betValue, inputTime, betSide);
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            result = 1;
                        }
                    }
                }
            } else {
                result = 4;
            }
        }
        BetTaiXiuMsg msg = new BetTaiXiuMsg();
        msg.Error = (byte) result;
        msg.currentMoney = currentMoney;
        return msg;
    }


    public void initJackpot() {
        SplittableRandom rand = new SplittableRandom();
        nextJpTai = rand.nextLong(7 - 4 + 1) + 4;
        nextJpXiu = rand.nextLong(7 - 4 + 1) + 4;
        if (isJpTai) {
            fundJpTai = 0;
            isJpTai = false;
        }
        if (isJpXiu) {
            fundJpXiu = 0;
            isJpXiu = false;
        }
    }

    public void updateTaiXiuPerSecond() {
        UpdateTaiXiuPerSecondMsg msg = new UpdateTaiXiuPerSecondMsg();
        msg.remainTime = this.getRemainTime();
        msg.bettingState = this.bettingRound;
        msg.potTai = this.getPotTai();
        msg.potXiu = this.getPotXiu();
        msg.numBetTai = this.potTai.getNumBet();
        msg.numBetXiu = this.potXiu.getNumBet();
        msg.fundJpTai = fundJpFakeTai;
        msg.fundJpXiu = fundJpFakeXiu;
        msg.referenceId = this.referenceId;
        msg.realNumBetTai = this.realNumBetTai;
        msg.realNumBetXiu = this.realNumBetXiu;
        msg.realPotXiu = this.realPotXiu;
        msg.realPotTai = this.realPotTai;
//        List<String> items = new ArrayList<>();
//        for (TransactionTaiXiuDetail i : this.potTai.getRealContributors()) {
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            try {
//                String json = ow.writeValueAsString(i);
//                items.add(json.toString());
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (TransactionTaiXiuDetail i : this.potXiu.getRealContributors()) {
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            try {
//                String json = ow.writeValueAsString(i);
//                items.add(json.toString());
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        msg.list = items;
        this.sendMessageToRoom(msg);
    }

    public short[] getResult(long id) {
        PotTaiXiu potX = this.potXiu;
        PotTaiXiu potT = this.potTai;
        long tongTienHopLe = (potT == null || potX == null) ? 0 : (Math.min(potT.getTotalValue(), potX.getTotalValue()));
        long sumMoneyCurrent = 0;
        long moneyUserBet = 0;
        PotTaiXiu pot = potT.getTotalValue() > potX.getTotalValue() ? potT : potX;
        long userOppositeBet = potT.getTotalValue() > potX.getTotalValue() ? this.getUserBetXiu() : this.getUserBetTai();

        for (TransactionTaiXiuDetail tran : pot.contributors) {  // check tra lai tien
            long tienDuocTinh = tran.betValue;
            if (sumMoneyCurrent + tran.betValue > tongTienHopLe) {
                tienDuocTinh = tongTienHopLe - sumMoneyCurrent;
            }
            sumMoneyCurrent += tienDuocTinh;
            if (tran.userId > 0) {
                moneyUserBet += tienDuocTinh;
            }
        }

        long moneyBetXiu = 0;
        long moneyBetTai = 0;
        if (potT.getTotalValue() > potX.getTotalValue()) {
            moneyBetXiu = userOppositeBet;
            moneyBetTai = moneyUserBet;
        } else {
            moneyBetXiu = moneyUserBet;
            moneyBetTai = userOppositeBet;
        }

        short[] result = {};
        final int Xtimes = 3;
        short[] dataCache = this.api.suaKetQuaTaiXiu();//1,2,3 get từ cache ra
        if (dataCache != null) {
            result = dataCache;
        } else {
            result = TaiXiuUtil.genarateRandomResult();
            int totalDiceTemp = result[0] + result[1] + result[2];
            while (totalDiceTemp == 3 || totalDiceTemp == 18) {
                result = TaiXiuUtil.genarateRandomResult();
                totalDiceTemp = result[0] + result[1] + result[2];
            }
            Debug.trace("TAIXIUDEBUG Result :" + result[0] + " " + result[1] + " " + result[2]);
            if (winCount.get() > Xtimes) {
                winCount.set(0);
            }
            if (moneyBetTai + moneyBetXiu > 0) {
                Debug.trace("MGRoomTaiXiu debug moneyBetTai" + moneyBetTai + " moneyBetXiu= " + moneyBetXiu);
                if (winCount.get() < Xtimes) {
                    if (TaiXiuUtil.isXiu(result)) {
                        Debug.trace("MGRoomTaiXiu debug TaiXiuUtil.isXiu(result)" + moneyBetTai + " moneyBetXiu= " + moneyBetXiu + "  fundTaiXiu = " + this.fundTaiXiu);
                        if (moneyBetXiu > moneyBetTai) {
                            winCount.getAndIncrement();
                            long moneyMinusFund = moneyBetXiu - moneyBetTai;
                            if (this.fundTaiXiu - moneyMinusFund < 0) {
                                result = TaiXiuUtil.genarateResult(true);
                                Debug.trace("MGRoomTaiXiu debug this.fundTaiXiu - moneyMinusFund < 0 " + moneyBetTai + " moneyBetXiu= " + moneyBetXiu + "  fundTaiXiu = " + this.fundTaiXiu);
                            }
                        }
                    } else {
                        Debug.trace("MGRoomTaiXiu debug false TaiXiuUtil.isXiu(result)" + moneyBetTai + " moneyBetXiu= " + moneyBetXiu + "  fundTaiXiu = " + this.fundTaiXiu);
                        if (moneyBetTai > moneyBetXiu) {
                            winCount.getAndIncrement();
                            long moneyMinusFund = moneyBetTai - moneyBetXiu;
                            if (this.fundTaiXiu - moneyMinusFund < 0) {
                                result = TaiXiuUtil.genarateResult(false);
                                Debug.trace("MGRoomTaiXiu debug this.fundTaiXiu - moneyMinusFund < 0" + moneyBetTai + " moneyBetXiu= " + moneyBetXiu + "  fundTaiXiu = " + this.fundTaiXiu);
                            }
                        }
                    }

                } else {
                    result = moneyBetTai > moneyBetXiu ? TaiXiuUtil.genarateResult(false) : TaiXiuUtil.genarateResult(true);
                    winCount.set(0);
                    Debug.trace("MGRoomTaiXiu debug winCount.get() > Xtimes" + moneyBetTai + " moneyBetXiu= " + moneyBetXiu + "  fundTaiXiu = " + this.fundTaiXiu);
                }
            }
        }
        //check jp
        int totalDice = result[0] + result[1] + result[2];

        if (totalDice == 18) {
            isJpXiu = false;
            isJpTai = true;
            sendNotifyJp(id, fundJpFakeTai);
//            if (countJpTai.incrementAndGet() >= nextJpTai) {
//                isJpTai = true;
//            } else {
//                isJpTai = false;
//            }
        }
        if (totalDice == 3) {
            isJpTai = false;
            isJpXiu = true;
            sendNotifyJp(id, fundJpFakeXiu);
//            if (countJpXiu.incrementAndGet() >= nextJpXiu) {
//                isJpXiu = true;
//            } else {
//                isJpXiu = false;
//            }
        }


//        if (flagJpTai || flagJpXiu) {
//            if (flagJpTai) {
//                result = new short[]{6, 6, 6};
//                isJpTai = true;
//                isJpXiu = false;
//                // SEND NOTIFY
//                sendNotifyJp(id, fundJpFakeTai);
//            } else {
//                result = new short[]{1, 1, 1};
//                isJpXiu = true;
//                isJpTai = false;
//                // SEND NOTIFY
//                sendNotifyJp(id, fundJpFakeXiu);
//            }
//            flagJpTai = flagJpXiu = false;
//        } else {
//            //bot eat jp
//            if ((TaiXiuUtil.isXiu(result) && potX.getRealUserBet() == 0)) {
//                if (fundJpFakeXiu > 50000000) {
//                    result = new short[]{1, 1, 1};
//                    isJpXiu = true;
//                    sendNotifyJp(id, fundJpFakeXiu);
//                }
//            } else if ((!TaiXiuUtil.isXiu(result) && potT.getRealUserBet() == 0)) {
//                if (fundJpFakeTai > 50000000) {
//                    result = new short[]{6, 6, 6};
//                    isJpTai = true;
//                    sendNotifyJp(id, fundJpFakeTai);
//                }
//            } else {
//                if (isJpTai && fundJpTai > minMoneyJp && fundJpFakeTai > fundJpTai
//                        && (potT.getRealUserBet() + potX.getRealUserBet() > 5000000)) {
//                    isJpTai = true;
//                    // SEND NOTIFY
//                    sendNotifyJp(id, fundJpFakeTai);
////					initJackpot();
//                } else {
//                    isJpTai = false;
//                }
//                if (isJpXiu && fundJpXiu > minMoneyJp && fundJpFakeXiu > fundJpXiu
//                        && (potT.getRealUserBet() + potX.getRealUserBet() > 5000000)) {
//                    isJpXiu = true;
//                    // SEND NOTIFY
//                    sendNotifyJp(id, fundJpFakeXiu);
////					initJackpot();
//                } else {
//                    isJpXiu = false;
//                }
//            }
//
//        }

        if (!isJpTai && totalDice == 18) {
            while (true) {
                result = TaiXiuUtil.genarateResult(false);
                if (result[0] + result[1] + result[2] != totalDice) {
                    break;
                }
            }
        }

        if (!isJpXiu && totalDice == 3) {
            while (true) {
                result = TaiXiuUtil.genarateResult(true);
                if (result[0] + result[1] + result[2] != totalDice) {
                    break;
                }
            }
        }
        if (TaiXiuUtil.isXiu(result)) {
            long changeMoney = (long) (moneyBetTai - (moneyBetXiu * (100 - (this.tax + this.taxJp)) / 100));
            this.fundTaiXiu += changeMoney / 2;
            fundJpXiu += changeMoney / 2;
            if (potT.getTotalValue() - potX.getTotalValue() > 0) {
                fundJpFakeXiu += (long) ((potT.getTotalValue() - potX.getTotalValue()) * taxJp / 100);
            }
        } else {
            long changeMoney = (long) (moneyBetXiu - (moneyBetTai * (100 - (this.tax + this.taxJp)) / 100));
            this.fundTaiXiu += changeMoney / 2;
            fundJpTai += changeMoney / 2;

            if (potX.getTotalValue() - potT.getTotalValue() > 0) {
                fundJpFakeTai += (long) ((potX.getTotalValue() - potT.getTotalValue()) * taxJp / 100);
            }
        }
        this.saveFund(totalDice);

        Debug.trace("TAIXIUDEBUG Result End:" + result[0] + " " + result[1] + " " + result[2]);

        this.updateResultDices(result, (short) (TaiXiuUtil.isXiu(result) ? 0 : 1));
        return result;
    }

    private void sendNotifyJp(long id, long amount) {
        TaiXiuJackpotMsg msg = new TaiXiuJackpotMsg();
        msg.amount = amount;
        msg.id = id;
        this.sendMessageToRoom(msg);
    }

    public void saveFund(int result) {
        String keyBot = this.moneyType == 1 ? "TaiXiu_Fund_vin" : "TaiXiu_Fund_xu";
        try {
            this.mgService.saveFund(keyBot, this.fundTaiXiu);
        } catch (Exception e) {
        }
        // save fun jp
        try {
            if (isJpXiu) {
                this.mgService.saveFund("TaiXiu_Fund_JPXiu", 0);
                fundJpFakeXiu = getRanFakeInit();
                saveJPFakeXiu(fundJpFakeXiu);
            } else if (isJpTai) {
                this.mgService.saveFund("TaiXiu_Fund_JPTai", 0);
                fundJpFakeTai = getRanFakeInit();
                saveJPFakeTai(fundJpFakeTai);
            } else {
                this.mgService.saveFund("TaiXiu_Fund_JPTai", fundJpTai);
                saveJPFakeTai(fundJpFakeTai);
                this.mgService.saveFund("TaiXiu_Fund_JPXiu", fundJpXiu);
                saveJPFakeXiu(fundJpFakeXiu);
            }
        } catch (Exception e) {
        }
    }

    private long getRanFakeInit() {
        SplittableRandom rdom = new SplittableRandom();
        return rdom.nextLong(4000000l) + 1000000l;
    }

    private void saveJPFakeTai(long amount) {
        cacheService.setValueJp(CacheConfigName.TAIXIU_FUND_JP_FAKETAI, amount);
    }

    private void saveJPFakeXiu(long amount) {
        cacheService.setValueJp(CacheConfigName.TAIXIU_FUND_JP_FAKEXIU, amount);
    }

    public void calculateMoneyReturn() {
        Debug.trace("calculateMoneyReturn  ");
        PotTaiXiu potX = this.potXiu;
        PotTaiXiu potT = this.potTai;
        long tongTienHopLe = (potT == null || potX == null) ? 0 : (potT.getTotalValue() > potX.getTotalValue() ? potX.getTotalValue() : potT.getTotalValue());
        long tongTienTaiDaTinh = 0L;
        long tongTienXiuDaTinh = 0L;

        if (potT != null && potT.contributors != null) {
            for (TransactionTaiXiuDetail tran : potT.contributors) {
                try {
                    long tienDuocTinh = tran.betValue;
                    if (tongTienTaiDaTinh + tran.betValue > tongTienHopLe) {
                        tienDuocTinh = tongTienHopLe - tongTienTaiDaTinh;
                    }
                    tongTienTaiDaTinh += tienDuocTinh;
                    long refund = tran.betValue - tienDuocTinh;
                    if (tran.userId != 0 && refund > 0) {
                        TaiXiuRefundMsg taiXiuRefundMsg = new TaiXiuRefundMsg(refund);
                        Debug.trace("Tra lai tien " + tran.username + "    " + refund);
                        this.sendMessageToUser(taiXiuRefundMsg, tran.username);
                    }
                } catch (Exception e) {
                    Debug.trace("Error calculate prize user1 " + tran.username + " error: " + e.getMessage());
                }
            }
        }

        if (potX != null && potX.contributors != null) {
            for (TransactionTaiXiuDetail tran : potX.contributors) {
                try {
                    long tienDuocTinh = tran.betValue;
                    if (tongTienXiuDaTinh + tran.betValue > tongTienHopLe) {
                        tienDuocTinh = tongTienHopLe - tongTienXiuDaTinh;
                    }
                    tongTienXiuDaTinh += tienDuocTinh;
                    long refund = tran.betValue - tienDuocTinh;
                    if (tran.userId != 0 && refund > 0) {
                        TaiXiuRefundMsg taiXiuRefundMsg = new TaiXiuRefundMsg(refund);
                        Debug.trace("Tra lai tien " + tran.username + "    " + refund);
                        this.sendMessageToUser(taiXiuRefundMsg, tran.username);
                    }

                } catch (Exception e) {
                    Debug.trace(("Error calculate prize user2 " + tran.username + " error: " + e.getMessage()));
                }
            }
        }
    }

    public void calculatePrize(long referenceId) {
        PotTaiXiu potX = this.potXiu;
        PotTaiXiu potT = this.potTai;
        Map<String, TransactionTaiXiu> sumTXTMap = new HashMap<String, TransactionTaiXiu>();
        Map<String, TransactionTaiXiu> sumTai = new HashMap<String, TransactionTaiXiu>();
        Map<String, TransactionTaiXiu> sumXiu = new HashMap<String, TransactionTaiXiu>();
        long tongTienHopLe = (potT == null || potX == null) ? 0 : (potT.getTotalValue() > potX.getTotalValue() ? potX.getTotalValue() : potT.getTotalValue());
        long tongTienTaiDaTinh = 0L;
        long tongTienXiuDaTinh = 0L;// 1 - tai , 0 - xiu
        long totalForCalculateJp = 0;
        if (isJpTai || isJpXiu) {
            totalForCalculateJp = this.result == 0 ? potX.getTotalValue() - potX.getTotalBotBet()
                    : potT.getTotalValue() - potT.getTotalBotBet();
        }

        long totalCashIn = 0L;
        long totalCashOut = 0L;
        ResultTaiXiu rs = new ResultTaiXiu();
        try {
            if (this.resultTX != null) {
                rs = this.resultTX;
                Debug.trace(("resultTX " + this.resultTX));
            } else {
                Debug.trace((" error: " + this.resultTX));
            }
        } catch (Exception ex) {
            Debug.trace((" error resultTX: " + ex.getMessage()));
        }
        switch (this.result) {
            case 0: {
                if (potX != null && potX.contributors != null) {
                    for (TransactionTaiXiuDetail tran : potX.contributors) {
                        try {
                            long tienDuocTinh = tran.betValue;
                            if (tongTienXiuDaTinh + tran.betValue > tongTienHopLe) {
                                tienDuocTinh = tongTienHopLe - tongTienXiuDaTinh;
                            }
                            tongTienXiuDaTinh += tienDuocTinh;
                            tran.prize = (long) ((float) tienDuocTinh * (100.0f - this.tax) / 100.0f) + tienDuocTinh;
                            rs.totalPrize += tran.prize;
                            tran.refund = tran.betValue - tienDuocTinh;
                            rs.totalRefundXiu += tran.refund;
                            this.updateSumTran(sumTXTMap, tran);
                            this.updateSumTran(sumXiu, tran);
                            this.saveTransactionDetailTX(tran);
                        } catch (Exception e) {
                            Debug.trace((Object) ("Error calculate prize user9 " + tran.username + " error: " + e.getMessage()));
                        }
                    }
                }
                if (potT == null || potT.contributors == null) break;
                for (TransactionTaiXiuDetail tran : potT.contributors) {
                    try {
                        long tienDuocTinh = tran.betValue;
                        if (tongTienTaiDaTinh + tran.betValue > tongTienHopLe) {
                            tienDuocTinh = tongTienHopLe - tongTienTaiDaTinh;
                        }
                        tongTienTaiDaTinh += tienDuocTinh;
                        tran.refund = tran.betValue - tienDuocTinh;
                        rs.totalRefundTai += tran.refund;
                        this.updateSumTran(sumTXTMap, tran);
                        this.updateSumTran(sumTai, tran);
                        this.saveTransactionDetailTX(tran);
                    } catch (Exception e) {
                        Debug.trace((Object) ("Error calculate prize user8 " + tran.username + " error: " + e.getMessage()));
                    }
                }
                break;
            }
            case 1: {
                if (potT != null && potT.contributors != null) {
                    for (TransactionTaiXiuDetail tran : potT.contributors) {
                        try {
                            long tienDuocTinh = tran.betValue;
                            if (tongTienTaiDaTinh + tran.betValue > tongTienHopLe) {
                                tienDuocTinh = tongTienHopLe - tongTienTaiDaTinh;
                            }
                            tongTienTaiDaTinh += tienDuocTinh;
                            tran.prize = (long) ((float) tienDuocTinh * (100.0f - this.tax) / 100.0f) + tienDuocTinh;
                            rs.totalPrize += tran.prize;
                            tran.refund = tran.betValue - tienDuocTinh;
                            rs.totalRefundTai += tran.refund;
                            this.updateSumTran(sumTXTMap, tran);
                            this.updateSumTran(sumTai, tran);
                            this.saveTransactionDetailTX(tran);
                        } catch (Exception e) {
                            Debug.trace((Object) ("Error calculate prize user7 " + tran.username + " error: " + e.getMessage()));
                        }
                    }
                }
                if (potX == null || potX.contributors == null) break;
                for (TransactionTaiXiuDetail tran : potX.contributors) {
                    try {
                        long tienDuocTinh = tran.betValue;
                        if (tongTienXiuDaTinh + tran.betValue > tongTienHopLe) {
                            tienDuocTinh = tongTienHopLe - tongTienXiuDaTinh;
                        }
                        tongTienXiuDaTinh += tienDuocTinh;
                        tran.refund = tran.betValue - tienDuocTinh;
                        rs.totalRefundXiu += tran.refund;
                        this.updateSumTran(sumTXTMap, tran);
                        this.updateSumTran(sumXiu, tran);
                        this.saveTransactionDetailTX(tran);
                    } catch (Exception e) {
                        Debug.trace((Object) ("Error calculate prize user6 " + tran.username + " error: " + e.getMessage()));
                    }
                }
                break;
            }
            default: {
                Debug.trace("error TX, room=" + this.moneyTypeStr + ", reference= " + referenceId + ", result= " + this.result);
            }
        }
        if (this.moneyType == 1) {
            Debug.trace(("TX phien= " + referenceId + ", tinh toan xong ket qua"));
        }
        rs.totalTai = potT.getTotalValue() - potT.getTotalBotBet();
        rs.numBetTai = potT.getNumBet() - potT.getNumBotBet();
        rs.totalXiu = potX.getTotalValue() - potX.getTotalBotBet();
        rs.numBetXiu = potX.getNumBet() - potX.getNumBotBet();
        if (isJpTai) {
            rs.totalJp = fundJpTai;
        } else if (isJpXiu) {
            rs.totalJp = fundJpXiu;
        } else {
            rs.totalJp = 0;
        }
        UpdateMoneyTXTask taskTai = new UpdateMoneyTXTask(sumTai);
        taskTai.start();
        if (this.moneyType == 1) {
            Debug.trace(("TX phien= " + referenceId + ", cap nhat xong ben tai"));
        }
        UpdateMoneyTXTask taskXiu = new UpdateMoneyTXTask(sumXiu);
        taskXiu.start();
        if (this.moneyType == 1) {
            Debug.trace(("TX phien= " + referenceId + ", cap nhat xong ben xiu"));
        }
        ArrayList<TransactionTaiXiu> trans = new ArrayList<TransactionTaiXiu>(sumTXTMap.values());
        try {
            this.api.saveResultTaiXiu(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.api.saveTransactionTaiXiu(trans);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (this.moneyType == 1) {
////            CalculateEndTXTask task = new CalculateEndTXTask(trans);
////            task.start();
//
//            try {
//                HazelcastInstance client = HazelcastClientFactory.getInstance();
//                IMap bankMap = client.getMap("txBank");
//                String key = "txBank:" + this.moneyType;
//                long bank = 0L;
//                //bankMap.lock(key);
//                if (bankMap.containsKey(key)) {
//                    bank = (long) bankMap.get(key);
//                }
//                logger.error("calculatePrize key:" + key + " bank:" + bank);
//
//                bank += totalCashIn - totalCashOut;
//                bankMap.put(key, bank);
//                logger.error("calculatePrize 2 key:" + key + " bank:" + bank);
//
//                bankMap.put("referenceId", referenceId);
//                bankMap.put("totalCashIn", totalCashIn);
//                bankMap.put("totalCashOut", totalCashOut);
//
//                //bankMap.unlock(key);
//            } catch (Exception ex) {
//                logger.error("calculatePrize ex:" + ex.getMessage());
//
//                Debug.trace((" error resultTX: " + ex.getMessage()));
//            }
//        }
    }

    private void updateSumTran(Map<String, TransactionTaiXiu> map, TransactionTaiXiuDetail tranDetail) {
        if (map.containsKey(tranDetail.username)) {
            TransactionTaiXiu txt = map.get(tranDetail.username);
            if (txt.betSide == tranDetail.betSide) {
                txt.betValue += tranDetail.betValue;
                txt.totalPrize += tranDetail.prize;
                txt.totalRefund += tranDetail.refund;
                txt.totalJp += tranDetail.jpAmount;
                map.put(tranDetail.username, txt);
            }
        } else {
            TransactionTaiXiu tran = new TransactionTaiXiu();
            tran.referenceId = tranDetail.referenceId;
            tran.userId = tranDetail.userId;
            tran.username = tranDetail.username;
            tran.moneyType = tranDetail.moneyType;
            tran.betSide = tranDetail.betSide;
            tran.betValue = tranDetail.betValue;
            tran.totalPrize = tranDetail.prize;
            tran.totalRefund = tranDetail.refund;
            tran.totalJp = tranDetail.jpAmount;
            map.put(tranDetail.username, tran);
        }
    }

    public short calculateBalanceTX(int type) {
        long tienDuocTinh;
        long totalPrizeBotTai = 0L;
        long totalPrizeBotXiu = 0L;
        long totalPrizeUserTai = 0L;
        long totalPrizeUserXiu = 0L;
        long tongTienHopLe = this.potTai.getTotalValue() > this.potXiu.getTotalValue() ? this.potXiu.getTotalValue() : this.potTai.getTotalBotBet();
        long tongTienXiuDaTinh = 0L;
        long tongTienTaiDaTinh = 0L;
        for (TransactionTaiXiuDetail tran : this.potXiu.contributors) {
            tienDuocTinh = tran.betValue;
            if (tongTienXiuDaTinh + tran.betValue > tongTienHopLe) {
                tienDuocTinh = tongTienHopLe - tongTienXiuDaTinh;
            }
            tongTienXiuDaTinh += tienDuocTinh;
            if (this.isBot(tran.username)) {
                totalPrizeBotXiu += tienDuocTinh;
                continue;
            }
            totalPrizeUserXiu += tienDuocTinh;
        }
        for (TransactionTaiXiuDetail tran : this.potTai.contributors) {
            tienDuocTinh = tran.betValue;
            if (tongTienTaiDaTinh + tran.betValue > tongTienHopLe) {
                tienDuocTinh = tongTienHopLe - tongTienTaiDaTinh;
            }
            tongTienTaiDaTinh += tienDuocTinh;
            if (this.isBot(tran.username)) {
                totalPrizeBotTai += tienDuocTinh;
                continue;
            }
            totalPrizeUserTai += tienDuocTinh;
        }
        Debug.trace(("Bot tai: " + totalPrizeBotTai + ", bot xiu: " + totalPrizeBotXiu));
        Debug.trace(("User tai: " + totalPrizeUserTai + ", user xiu: " + totalPrizeUserXiu));
        short result = -1;
        switch (type) {
            case 1: {
                result = this.tinhCuaThang(totalPrizeBotTai, totalPrizeBotXiu);
                Debug.trace(("He thong am, force= " + result));
                break;
            }
            case -2: {
                result = this.tinhCuaThang(-this.blackListBetTai, -this.blackListBetXiu);
                Debug.trace(("Black list: " + result));
                break;
            }
            case -3: {
                result = this.tinhCuaThang(this.whiteListBetTai, this.whiteListBetXiu);
                Debug.trace(("White list: " + result));
                break;
            }
            case -1: {
                long totalUserWinSystem = Math.abs(totalPrizeUserTai - totalPrizeUserXiu);
                if (totalUserWinSystem <= this.balance.getMaxWinUser()) {
                    result = this.tinhCuaThang(totalPrizeUserTai, totalPrizeUserXiu);
                    Debug.trace(("Nguoi choi am, force = " + result));
                    break;
                }
                Debug.trace(("Nguoi choi am nhung so tien an qua lo'n= " + totalUserWinSystem));
                result = this.checkHeThongAm(totalPrizeUserTai, totalPrizeUserXiu, totalPrizeBotTai, totalPrizeBotXiu);
                break;
            }
            default: {
                result = this.checkHeThongAm(totalPrizeUserTai, totalPrizeUserXiu, totalPrizeBotTai, totalPrizeBotXiu);
            }
        }
        return result;
    }

    private short tinhCuaThang(long tai, long xiu) {
        if (tai > xiu) {
            return 1;
        }
        if (tai < xiu) {
            return 0;
        }
        return -1;
    }

    private short checkHeThongAm(long totalPrizeUserTai, long totalPrizeUserXiu, long totalPrizeBotTai, long totalPrizeBotXiu) {
        short result = -1;
        long fee = balance.getFee();
        long revenueUser = balance.getRevenueUser();
        long maxUserWin = Math.abs(totalPrizeUserTai - totalPrizeUserXiu);
        if ((float) (revenueUser + (long) ((float) maxUserWin * (100.0F - tax) / 100.0F)) >= (float) -fee * ConfigGame.getFloatValue("tx_min_fee", 1.0F)) {
            result = tinhCuaThang(totalPrizeBotTai, totalPrizeBotXiu);
            Debug.trace("Chong he thong am, force= " + result + ", max user win= " + maxUserWin);
        }
        return result;
    }

    public int calculateForceBalance() {
        boolean hasBlackList = this.blackListBetTai + this.blackListBetXiu > 0L;
        boolean hasWhiteList = this.whiteListBetTai + this.whiteListBetXiu > 0L;
        return this.balance.isForceBalance(hasBlackList, hasWhiteList);
    }

    private void saveTransactionDetailTX(TransactionTaiXiuDetail tran) {
        try {
            this.api.saveTransactionTaiXiuDetail(tran);
        } catch (Exception e) {
            Debug.trace(("Update transaction detail tai xiu error: " + e.getMessage()));
        }
    }

    public void updateTaiXiuInfo(User user) {
        TaiXiuInfoMsg msg = new TaiXiuInfoMsg();
        msg.gameId = (short) 2;
        msg.moneyType = this.moneyType;
        msg.referenceId = this.referenceId;
        msg.remainTime = this.getRemainTime();
        msg.bettingState = this.bettingRound;
        msg.potTai = this.getPotTai();
        msg.potXiu = this.getPotXiu();
        msg.myBetTai = this.getTotalBettingTaiByUsername(user.getName());
        msg.myBetXiu = this.getTotalBettingXiuByUsername(user.getName());
        msg.jpTai = fundJpFakeTai;
        msg.jpXiu = fundJpFakeXiu;
        if (this.resultTX != null) {
            msg.dice1 = (short) this.resultTX.dice1;
            msg.dice2 = (short) this.resultTX.dice2;
            msg.dice3 = (short) this.resultTX.dice3;
        }
        this.sendMessageToUser((BaseMsg) msg, user);
    }

    public boolean isBetting() {
        return this.bettingRound;
    }

    public long getPotTai() {
        return this.potTai.getTotalValue();
    }

    public long getBotBetTai() {
        return this.potTai.getTotalBotBet();
    }

    public long getUserBetTai() {
        return this.potTai.getTotalValue() - this.potTai.getTotalBotBet();
    }

    public long getPotXiu() {
        return this.potXiu.getTotalValue();
    }

    public long getBotBetXiu() {
        return this.potXiu.getTotalBotBet();
    }

    public long getUserBetXiu() {
        return this.potXiu.getTotalValue() - this.potXiu.getTotalBotBet();
    }

    public long getTotalBettingTaiByUsername(String usernname) {
        return this.potTai.getTotalBetByUsername(usernname);
    }

    public long getTotalBettingXiuByUsername(String username) {
        return this.potXiu.getTotalBetByUsername(username);
    }

    public BalanceMoneyTX getBalanceTX() {
        return this.balance;
    }

    public boolean isBot(String username) {
        UserCacheModel model = this.userService.getUser(username);
        return model.isBot();
    }

    public static short getMoneyType(int roomId) {
        return roomId == 0 ? (short) 0 : 1;
    }

    public static String getKeyRoom(short moneyType) {
        return "" + moneyType + "_" + 2;
    }

    @Override
    public boolean joinRoom(User user) {
        boolean result = super.joinRoom(user);
        if (result) {
            user.setProperty("MGROOM_TAI_XIU_INFO", this);
        }
        return result;
    }

    @Override
    public boolean quitRoom(User user) {
        boolean result = super.quitRoom(user);
        if (result) {
            user.removeProperty("MGROOM_TAI_XIU_INFO");
        }
        return result;
    }

    private final class UpdateMoneyTXTask extends Thread {

        private Map<String, TransactionTaiXiu> trans = new HashMap<String, TransactionTaiXiu>();

        private UpdateMoneyTXTask(Map<String, TransactionTaiXiu> trans) {
            this.trans = trans;
        }

        @Override
        public void run() {
            boolean isJp = false;
            for (Map.Entry<String, TransactionTaiXiu> entry : this.trans.entrySet()) {
                try {
                    String username = entry.getKey();
                    TransactionTaiXiu txt = entry.getValue();
                    long currentMoney = MGRoomTaiXiu.this.userService.getCurrentMoneyUserCache(username, MGRoomTaiXiu.this.moneyTypeStr);
                    if (txt.totalPrize == 0L && txt.totalRefund == 0L) {
                        MGRoomTaiXiu.this.userService.updateMoney(username, 0L, MGRoomTaiXiu.this.moneyTypeStr, "TaiXiu",
                                "", "", 0L, Long.valueOf(MGRoomTaiXiu.this.referenceId), TransType.END_TRANS);
                    } else {
                        MoneyResponse res;
                        if (txt.totalPrize > 0L) {

                            // jackpot
                            if (txt.totalJp > 0L) {
                                isJp = true;
                                if (!MGRoomTaiXiu.this.isBot(username)) {
                                    res = MGRoomTaiXiu.this.userService.updateMoney(username, txt.totalJp, MGRoomTaiXiu.this.moneyTypeStr,
                                            "TaiXiu", Games.TAI_XIU.getId() + "",
                                            TaiXiuDescriptionUtils.getTaiXiuWinDescription(Games.TAI_XIU.getId() + "", MGRoomTaiXiu.this.referenceId, (byte) 3)
                                            , 0L, Long.valueOf(MGRoomTaiXiu.this.referenceId), TransType.IN_TRANS);

                                    if (res.isSuccess()) {
                                        if (MGRoomTaiXiu.this.moneyType == 1 && !MGRoomTaiXiu.this.isBot(username)) {
                                            MGRoomTaiXiu.this.balance.addWin(txt.totalJp);
                                        }
                                        currentMoney = res.getCurrentMoney();
                                        if (MGRoomTaiXiu.this.moneyType == 1) {
                                            MGRoomTaiXiu.this.broadcastMsgService.putMessage(Games.TAI_XIU.getId(), username, txt.totalJp);
                                        }

//                                        if (MGRoomTaiXiu.this.moneyType == 1 && !MGRoomTaiXiu.this.isBot(username)) {
//                                            MGRoomTaiXiu.this.balance.addWin(txt.totalJp);
//                                        }
//                                        currentMoney = res.getCurrentMoney();
                                    }
                                }
                            }

                            TransType transType = TransType.END_TRANS;
                            if (txt.totalRefund > 0L) {
                                transType = TransType.IN_TRANS;
                            }
                            long fee = (long) (MGRoomTaiXiu.this.tax * (float) txt.totalPrize / (200.0f - MGRoomTaiXiu.this.tax));
                            MoneyResponse res2 = new MoneyResponse(false, "1001");
                            if (!MGRoomTaiXiu.this.isBot(username)) {
                                res2 = MGRoomTaiXiu.this.userService.updateMoney(username, txt.totalPrize, MGRoomTaiXiu.this.moneyTypeStr,
                                        "TaiXiu", Games.TAI_XIU.getId() + "",
                                        TaiXiuDescriptionUtils.getTaiXiuWinDescription(Games.TAI_XIU.getId() + "", MGRoomTaiXiu.this.referenceId, (byte) 1)
                                        , fee, Long.valueOf(MGRoomTaiXiu.this.referenceId), transType);

                            } else {
                                res2.setSuccess(true);
                            }
                            if (res2.isSuccess()) {
                                if (MGRoomTaiXiu.this.moneyType == 1 && !MGRoomTaiXiu.this.isBot(username)) {
                                    MGRoomTaiXiu.this.balance.addWin(txt.totalPrize);
                                    MGRoomTaiXiu.this.balance.addFee(fee);
                                }
                                currentMoney = res2.getCurrentMoney();
                                long totalExchange = (long) ((float) txt.totalPrize * (100.0f - MGRoomTaiXiu.this.tax) / (200.0f - MGRoomTaiXiu.this.tax));
                                if (MGRoomTaiXiu.this.moneyType == 1 && totalExchange >= (long) BroadcastMessageServiceImpl.MIN_MONEY) {
                                    MGRoomTaiXiu.this.broadcastMsgService.putMessage(Games.TAI_XIU.getId(), username, totalExchange);
                                }
                            }
                        }
                        if (txt.totalRefund > 0L) {
                            if (!MGRoomTaiXiu.this.isBot(username)) {
                                res = MGRoomTaiXiu.this.userService.updateMoney(username, txt.totalRefund, MGRoomTaiXiu.this.moneyTypeStr,
                                        "TaiXiu", Games.TAI_XIU.getId() + "",
                                        TaiXiuDescriptionUtils.getTaiXiuRefundDescription(Games.TAI_XIU.getId() + "", MGRoomTaiXiu.this.referenceId)
                                        , 0L, Long.valueOf(MGRoomTaiXiu.this.referenceId), TransType.END_TRANS);
                                if (res.isSuccess()) {
                                    if (MGRoomTaiXiu.this.moneyType == 1 && !MGRoomTaiXiu.this.isBot(username)) {
                                        MGRoomTaiXiu.this.balance.addWin(txt.totalRefund);
                                    }
                                    currentMoney = res.getCurrentMoney();
                                }
                            }
                        }
                        
                        /*if (txt.totalRefund > 0L && (res = MGRoomTaiXiu.this.userService.updateMoney(username, txt.totalRefund, MGRoomTaiXiu.this.moneyTypeStr, "TaiXiu", "Ho\u00e0n tr\u1ea3 t\u00e0i x\u1ec9u", "Phi\u00ean " + MGRoomTaiXiu.this.referenceId, 0L, Long.valueOf(MGRoomTaiXiu.this.referenceId), TransType.END_TRANS)).isSuccess()) {
                            if (MGRoomTaiXiu.this.moneyType == 1 && !MGRoomTaiXiu.this.isBot(username)) {
                                MGRoomTaiXiu.this.balance.addWin(txt.totalRefund);
                            }

                        }*/
                    }
                    UpdatePrizeTaiXiuMsg msg = new UpdatePrizeTaiXiuMsg();
                    msg.moneyType = MGRoomTaiXiu.this.moneyType;
                    msg.totalMoney = txt.totalPrize + txt.totalRefund + txt.totalJp;
                    msg.currentMoney = currentMoney;
                    MGRoomTaiXiu.this.sendMessageToUser((BaseMsg) msg, username);
                } catch (Exception e) {
                    Debug.trace(("Update tai xiu money phien " + MGRoomTaiXiu.this.referenceId + " error: " + e.getMessage()));
                }
            }
            //init
            if (isJp) {
                initJackpot();
            }
        }
    }

//    private final class CalculateEndTXTask
//            extends Thread {
//        private List<TransactionTaiXiu> trans;
//
//        private CalculateEndTXTask(List<TransactionTaiXiu> trans) {
//            this.trans = trans;
//        }
//
//        @Override
//        public void run() {
//            try {
//                for (TransactionTaiXiu tran : this.trans) {
//                    if (tran.betValue - tran.totalRefund < 20000L) continue;
//                    int soLuotThem = RutLocUtils.getLuotRutLoc(tran.betValue - tran.totalRefund);
//                    MGRoomTaiXiu.this.sendMessageToUser((BaseMsg) msg, tran.username);
//                }
//            } catch (Exception e) {
//                Debug.trace(("Error save tai xiu: " + e.getMessage()));
//            }
//        }
//    }

}

