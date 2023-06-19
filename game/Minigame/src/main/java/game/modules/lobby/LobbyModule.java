package game.modules.lobby;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dichvuthe.response.RechargeResponse;
import com.vinplay.dichvuthe.service.RechargeService;
import com.vinplay.dichvuthe.service.impl.RechargeServiceImpl;
import com.vinplay.dichvuthe.utils.DvtConst;
import com.vinplay.livecasino.api.core.obj.BalanceResponse;
import com.vinplay.livecasino.api.core.obj.LaunchGameResponse;
import com.vinplay.livecasino.api.core.obj.TCGBaseResponse;
import com.vinplay.livecasino.api.core.obj.TCGamingConfigObj;
import com.vinplay.livecasino.api.dao.impl.LiveCasinoDaoImpl;
import com.vinplay.livecasino.api.response.LiveCasinoUserResponse;
import com.vinplay.livecasino.api.wsclient.TCGamingAPICommon;
import com.vinplay.livecasino.api.wsclient.impl.TCGamingAPICommonImpl;
import com.vinplay.safebox.core.impl.SafeBoxImpl;
import com.vinplay.safebox.response.SafeBoxResponse;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.PartnerConfig;


import java.util.concurrent.TimeUnit;

import com.vinplay.vbee.common.enums.Platform;
import com.vinplay.vbee.common.enums.ProviderType;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import game.modules.lobby.cmd.rev.*;
import game.modules.lobby.cmd.send.*;
import okhttp3.*;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONException;
import bitzero.server.extensions.data.BaseMsg;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import java.util.TimeZone;

import org.json.JSONObject;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.usercore.service.OtpService;
import com.vinplay.usercore.service.SecurityService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.VippointService;
import com.vinplay.usercore.service.impl.OtpServiceImpl;
import com.vinplay.usercore.service.impl.SecurityServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.service.impl.VippointServiceImpl;
import com.vinplay.usercore.utils.LuckyUtils;
import com.vinplay.usercore.utils.VippointUtils;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import com.vinplay.usercore.entities.TransferMoneyResponse;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.core.IBZEventListener;
import bitzero.server.core.IBZEventParam;
import bitzero.server.entities.User;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;
import game.modules.lobby.cmd.rev.ActiveMobileCmd;
import game.modules.lobby.cmd.rev.ChuyenKhoanCmd;
import game.modules.lobby.cmd.send.ChuyenKhoanMsg;
import game.modules.lobby.cmd.send.ResultChuyenKhoanMsg;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.vinplay.dal.service.impl.AgentServiceImpl;
import game.modules.lobby.cmd.send.OTPMsg;
import game.modules.lobby.cmd.rev.CheckUserCmd;
import com.vinplay.dal.dao.LogMoneyUserDao;
import com.vinplay.dal.dao.impl.LogMoneyUserDaoImpl;

import com.vinplay.vbee.common.response.LogUserMoneyResponse;
import com.vinplay.vbee.common.response.AgentResponse;
import game.modules.lobby.cmd.rev.OtpCmd;
import game.modules.lobby.cmd.rev.DoiPassCmd;
import game.modules.lobby.cmd.rev.DoiVippointCmd;
import game.modules.lobby.cmd.rev.GameConfigCmd;
import game.modules.lobby.cmd.rev.KetSatCmd;
import game.modules.lobby.cmd.rev.LoginOtpCmd;
import game.modules.lobby.cmd.rev.SendOTPCmd;
import game.modules.lobby.cmd.rev.UpdateEmailCmd;
import game.modules.lobby.cmd.rev.UpdateMobileCmd;
import game.modules.lobby.cmd.rev.UpdateNewMobileCmd;
import game.modules.lobby.cmd.rev.UpdateUserInfoCmd;
import game.modules.lobby.cmd.send.ActiveEmailMsg;
import game.modules.lobby.cmd.send.ActiveMobileMsg;
import game.modules.lobby.cmd.send.CheckUserMsg;
import game.modules.lobby.cmd.send.DoiPassMsg;
import game.modules.lobby.cmd.send.DoiVippointMsg;
import game.modules.lobby.cmd.send.EventVPUnluckyMsg;
import game.modules.lobby.cmd.send.GetEventVPInfoMsg;
import game.modules.lobby.cmd.send.GetInfoMsg;
import game.modules.lobby.cmd.send.GetMoneyUseMsg;
import game.modules.lobby.cmd.send.HasNewMailMsg;
import game.modules.lobby.cmd.send.HuVangMsg;
import game.modules.lobby.cmd.send.KetSatMsg;
import game.modules.lobby.cmd.send.ResultKetSatMsg;
import game.modules.lobby.cmd.send.SendOTPMsg;
import game.modules.lobby.cmd.send.UpdateEmailMsg;
import game.modules.lobby.cmd.send.UpdateMobileMsg;
import game.modules.lobby.cmd.send.UpdateNewMobileMsg;
import game.modules.lobby.cmd.send.UpdateUserInfoMsg;
import game.modules.minigame.utils.MiniGameUtils;
import game.utils.ConfigGame;
import game.utils.HuVangConfig;
import game.utils.ServerUtil;
import game.entities.QuotaResponse;

public class LobbyModule extends BaseClientRequestHandler {
    private static final String CURRENT_COMMAND = "cmd";
    private static final String CURRENT_OBJECT_COMMAND = "obj_cmd";
    private static final String FORCE_CHECK_OTP = "force_check_otp";
    private static final String UPDATE_NEW_MOBILE = "update_new_mobile";
    private GameLoopTask gameLoopTask = new GameLoopTask();
    private UserService userService = new UserServiceImpl();
    private OtpService otpService = new OtpServiceImpl();
    private SecurityService securityService = new SecurityServiceImpl();
    private VippointService vpService = new VippointServiceImpl();
    private Set<User> usersSubJackpot = new HashSet<User>();
    private long countUpdateJackpot = 0L;
    private Date eventTimeStart = null;
    private Date eventTimeEnd = null;
    private EventStartTask eventStartTask = new EventStartTask();
    private EventEndTask eventEndTask = new EventEndTask();
    private EventLuckyStartTask eventLuckyStartTask = new EventLuckyStartTask();
    private EventLuckyEndTask eventLuckyEndTask = new EventLuckyEndTask();
    private long timeLucky;
    private EventUnluckyTask eventUnluckyTask = new EventUnluckyTask();
    private EventluckyTask eventLuckyTask = new EventluckyTask();
    private EventX2EndTask eventX2EndTask = new EventX2EndTask();
    private final Runnable slotDailyTask = new SlotDailyTask();
    private static final org.apache.log4j.Logger logger = Logger.getLogger("vbee");

    private RechargeService rechargeService = new RechargeServiceImpl();

    private TCGamingConfigObj configObj = new TCGamingConfigObj();
    private TCGamingAPICommon api = null;

    private SafeBoxImpl safeBox = new SafeBoxImpl();

    public void init() {
        super.init();
        this.getParentExtension().addEventListener(BZEventType.USER_DISCONNECT, (IBZEventListener) this);
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.gameLoopTask, 10, 1, TimeUnit.SECONDS);
        try {
            this.initVP();
            MongoDBConnectionFactory.init();
        } catch (Exception e) {
            Debug.trace(("init vippoint event error " + e));
        }
        try {
            LuckyUtils.initSlotMap();
        } catch (Exception e) {
            Debug.trace(("init slot free error " + e));
        }

        try {
            PartnerConfig.ReadConfig();
        } catch (Exception e) {
            Debug.trace((Object) ("init partnerconfig event error " + e));
        }

        configObj.setApiUrl("http://www.connect6play.com/doBusiness.do");
        configObj.setMerchantCode("go88vndk");
        configObj.setDesKey("ZU2JmW3M");
        configObj.setSha256Key("SLzDawl0l6At7m6f");

        api = new TCGamingAPICommonImpl(configObj);

        long currentTime = System.currentTimeMillis() / 1000L;
        long endToday = DateTimeUtils.getEndTimeToDayAsLong() / 1000L;
        int n = (int) (endToday - currentTime);
        BitZeroServer.getInstance().getTaskScheduler().schedule(this.slotDailyTask, n + 5, TimeUnit.SECONDS);
        logger.debug(("LobbyModule Init"));
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        switch (dataCmd.getId()) {
            case 20050: {
                this.getInfo(user);
                break;
            }
            case 20051: {
                this.getMoneyUse(user, dataCmd);
                break;
            }
            case 20000: {
                this.doiPass(user, dataCmd);
                break;
            }
            case 20001: {
                this.doiVippoint(user, dataCmd);
                break;
            }
            case 20002: {
                this.updateUserInfo(user, dataCmd);
                break;
            }
            case 20003: {
                this.updateEmail(user, dataCmd);
                break;
            }
            case 20004: {
                this.updateMobile(user, dataCmd);
                Debug.trace(dataCmd.readString());
                break;
            }
            case 20005: {
                this.activeEmail(user, dataCmd);
                break;
            }
            case 20006: {
                this.activeMobile(user, dataCmd);
                break;
            }
            case 20007: {
                this.updateNewMobile(user, dataCmd);
                break;
            }
            case 20008: {
                this.loginOtp(user, dataCmd);
                break;
            }
            case 20009: {
                this.ketSat(user, dataCmd);
                break;
            }
            case 20010: {
                this.gameConfig(user, dataCmd);
                break;
            }
            case 20012: {
                this.napTheDienThoai(user, dataCmd);
                break;
            }
            case 20013: {
                this.activeMobileValidate(user, dataCmd);
                break;
            }
//            case 20011: {
//                this.napXu(user, dataCmd);
//                break;
//            }
//            case 20017: {
//                this.giftCode(user, dataCmd);
//                break;
//            }
            case 20018: {
                this.checkUser(user, dataCmd);
                break;
            }

            case 20220: {
                this.sendOtp(user, dataCmd);
                break;
            }
            case 20102: {
                this.subscribeJackPot(user);
                break;
            }
            case 20103: {
                this.unSubscribeJackPot(user);
                break;
            }
            case 20104: {
                this.updateHuVang(user);
                break;
            }
//            case 20037: {
//                this.checkIAP(user, dataCmd);
//                break;
//            }
//            case 20038: {
//                this.resultIAP(user, dataCmd);
//                break;
//            }
            case 20039: {
                this.getEventInfo(user, dataCmd);
                break;
            }
            case 20014: {
                this.chuyenKhoan(user, dataCmd);
                break;
            }
            case 20040: {
                this.requestApiOTP(user, dataCmd);
                break;
            }
            case 20041: {
                this.confirmApiOTP(user, dataCmd);
                break;
            }
            case 20042: {
                this.playVQMM(user);
                break;
            }
            case 20043: {
                this.getVQVip(user);
                break;
            }
            case 20044: {
                this.playVQVip(user);
            }

            case 20202: {
                this.depositMomoManual(user, dataCmd);
                break;
            }
            case 20203: {
                this.depositBankManual(user, dataCmd);
                break;
            }
            case 20204: {
                this.getBankAvailable(user);
                break;
            }
            case 20205: {
                this.depositBank(user, dataCmd);
                break;
            }
            // getBalance casino
            case 20300: {
                this.getBalanceLiveCasino(user, dataCmd);
                break;
            }
            // rút tiền casino
            case 20301: {
                this.cashOutAllLiveCasino(user, dataCmd);
                break;
            }
            // và nộp tiền
            case 20302: {
                this.depositLiveCasino(user, dataCmd);
                break;
            }
            // laays link game
            case 20303: {
                this.launchCasino(user, dataCmd);
                break;
            }
            //nop tien ket sat
            case 20310: {
                depositSafeBox(user, dataCmd);
                break;
            }
            //lay tien ket sat
            case 20311: {
                getAmountSafeBox(user);
                break;
            }
            //rut tien ket sat
            case 20312: {
                withDrawSafeBox(user, dataCmd);
                break;
            }
        }
    }

    private void depositSafeBox(User user, DataCmd dataCmd) {
        SafeBoxDepositCmd cmd = new SafeBoxDepositCmd(dataCmd);
        SafeBoxDepositMsg msg = new SafeBoxDepositMsg();
        SafeBoxResponse safeBoxResponse = safeBox.depositSafeBox(user.getName(), cmd.amount);
        if (safeBoxResponse.status == 0) {
            msg.Error = 0;
        } else
            msg.Error = 1;
        msg.message = safeBoxResponse.message;
        this.send((BaseMsg) msg, user);
    }

    private void getAmountSafeBox(User user) {
        SafeBoxMsg msg = new SafeBoxMsg();
        SafeBoxResponse safeBoxResponse = safeBox.getSafeBox(user.getName());
        if (safeBoxResponse.status == 0) {
            msg.Error = 0;
        } else
            msg.Error = 1;
        msg.amount = (long) safeBoxResponse.amount;
        this.send((BaseMsg) msg, user);
    }

    private void withDrawSafeBox(User user, DataCmd dataCmd) {
        SafeBoxWithDrawCmd cmd = new SafeBoxWithDrawCmd(dataCmd);
        SafeBoxWithDrawMsg msg = new SafeBoxWithDrawMsg();
        SafeBoxResponse safeBoxResponse = safeBox.withDraw(user.getName(), cmd.amount, cmd.otp);
        if (safeBoxResponse.status == 0) {
            msg.Error = 0;
        } else
            msg.Error = 1;
        msg.message = safeBoxResponse.message;
        this.send((BaseMsg) msg, user);
    }

    private void launchCasino(User user, DataCmd dataCmd) {
        LaunchGameLiveCasinoCmd cmd = new LaunchGameLiveCasinoCmd(dataCmd);
        LaunchGameCasinoMsg msg = new LaunchGameCasinoMsg();
        String nickname = regisUser(user);
        if (TextUtils.isEmpty(nickname)) {
            msg.Error = (byte) 1;
        } else {
            LaunchGameResponse responseDataLaunchGame = api.launchGame(nickname, 112, cmd.platform, "1", cmd.gameCode);
            if (responseDataLaunchGame.getStatus() == 0) {
                msg.Error = (byte) responseDataLaunchGame.getStatus();
                msg.gameUrl = responseDataLaunchGame.getGame_url();
            } else {
                msg.Error = (byte) 1;
                msg.gameUrl = "";
            }
        }
        this.send((BaseMsg) msg, user);
    }

    private void depositLiveCasino(User user, DataCmd dataCmd) {
        DepositMomoManualCmd cmd = new DepositMomoManualCmd(dataCmd);
        double amount = cmd.Amount;
        DepositLiveCasinoMsg msg = new DepositLiveCasinoMsg();
        msg.amount = String.valueOf(amount);

        String nickname = regisUser(user);
        if (TextUtils.isEmpty(nickname)) {
            msg.Error = (byte) 1;
        } else {
            String id = VinPlayUtils.genTransactionId(user.getId());
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            UserCacheModel userCache = (UserCacheModel) client.getMap("users").get(user.getName());
            Debug.trace((Object) ("depositBalance: " + userCache.getVin()));
            if (amount > userCache.getVin()) {
                msg.Error = (byte) 1;
            } else {
                TCGBaseResponse responseDataFund = api.fundTransferIn(nickname, 112, amount, id);
                Debug.trace((Object) ("depositBalance: " + responseDataFund.getStatus()));
                if (responseDataFund.getStatus() == 0) {
                    msg.Error = (byte) responseDataFund.getStatus();

                    UserServiceImpl service = new UserServiceImpl();
                    BaseResponseModel response1 = service.updateMoneyFromAdmin(user.getName(), -(long) (amount * 1000), "vin", "RechargeByCasino", "Deposit live casino", "Deposit live casino", 0, false);

//                    String description = "Kết quả: Nộp thành công " + (amount * 1000) + "vào live casino";
//                    String description1 = "Nộp " + " " + (amount * 1000) + "vào live casino";
//                    long currentMoney = userCache.getVinTotal();
//                    LogMoneyUserMessage messageLog2 = new LogMoneyUserMessage(user.getId(), nickname, "RechargeByCard", description1, currentMoney, 0L, "vin", description, 0L, false, false);
//                    try {
//                        RMQApi.publishMessageLogMoney(messageLog2);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } else {
                    msg.Error = (byte) 1;
                }
            }
        }
        this.send((BaseMsg) msg, user);
    }

    private String regisUser(User user) {
        String nickname = "go88" + user.getName();
        if (nickname.length() > 14) {
            nickname = nickname.substring(nickname.length() - 14);
        }
        String currency = "VND";
        String password = randomString(10);

        LiveCasinoDaoImpl liveCasinoDao = new LiveCasinoDaoImpl();
        LiveCasinoUserResponse liveCasinoUserResponse = liveCasinoDao.getUserCasino(nickname);
        if (liveCasinoUserResponse == null) {
            liveCasinoDao.insertUserCasino(nickname, password);
            TCGBaseResponse responseRegister = api.registerMember(nickname, password, currency);
            if (responseRegister.getStatus() != 0) {
                return "";
            }
        }
        return nickname;
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private void cashOutAllLiveCasino(User user, DataCmd dataCmd) {
        CashOutAllLiveCasinoMsg msg = new CashOutAllLiveCasinoMsg();
        String nickname = regisUser(user);
        if (TextUtils.isEmpty(nickname)) {
            msg.Error = (byte) 1;
        } else {
            BalanceResponse res = api.getBalanceMember(nickname, 112);
            if (res != null && res.getStatus() == 0) {
                msg.amount = res.getBalance();
                String id1 = VinPlayUtils.genTransactionId(user.getId());
                TCGBaseResponse responseDataFundOut = api.fundTransferOutAll(nickname, 112, id1);
                if (responseDataFundOut.getStatus() == 0) {
                    BalanceResponse res2 = api.getBalanceMember(nickname, 112);
                    if (res2 != null && res2.getStatus() == 0) {
                        msg.Error = (byte) res2.getStatus();
                        msg.balance = res2.getBalance();
                        UserServiceImpl service = new UserServiceImpl();
                        BaseResponseModel response1 = service.updateMoneyFromAdmin(user.getName(), Long.parseLong(msg.amount) * 1000, "vin", "RechargeByCasino", "Cashout live casino", "Cashout live casino", 0, false);

                    } else {
                        msg.Error = (byte) 1;
                    }
                } else {
                    msg.Error = (byte) 1;
                }
            } else {
                msg.Error = (byte) 1;
            }
        }
        Debug.trace((Object) ("cashOutAllBalance " + msg.balance + " " + msg.amount));
        this.send((BaseMsg) msg, user);
    }

    private void getBalanceLiveCasino(User user, DataCmd dataCmd) {
        String nickname = regisUser(user);
        GetBalanceLiveCasinoMsg msg = new GetBalanceLiveCasinoMsg();
        if (TextUtils.isEmpty(nickname)) {
            msg.Error = (byte) 1;
        } else {
            BalanceResponse res = api.getBalanceMember(nickname, 112);
            if (res != null && res.getStatus() == 0) {
                msg.Error = (byte) res.getStatus();
                if (msg.Error == DvtConst.RECHARGE_STATUS_SUCCESS) {
                    msg.balance = res.getBalance();
                }
            } else {
                msg.Error = (byte) 1;
                msg.balance = "";
            }
        }
        this.send((BaseMsg) msg, user);
    }

    private void depositMomoManual(User user, DataCmd dataCmd) {
        try {
//            DepositMomoManualCmd cmd = new DepositMomoManualCmd(dataCmd);
            String nickname = user.getName();
            Debug.trace((Object) ("depositMomoManual "));
            RechargeResponse res = this.rechargeService.rechargeByMomoManual(nickname);
            DepositMomoManualMsg msg = new DepositMomoManualMsg();
            if (res != null) {
                msg.Error = (byte) res.getCode();
                Debug.trace((Object) ("depositMomoManual " + res.getMessage()));
                if (msg.Error == DvtConst.RECHARGE_STATUS_SUCCESS) {
                    msg.data = res.getMessage();
                }
            } else {
                msg.Error = (byte) 1;
            }
            this.send((BaseMsg) msg, user);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void depositBankManual(User user, DataCmd dataCmd) {
        try {
//            DepositMomoManualCmd cmd = new DepositMomoManualCmd(dataCmd);
            String nickname = user.getName();
            Debug.trace((Object) ("depositBankManual "));
            RechargeResponse res = this.rechargeService.rechargeByBankManualHaDongPho(nickname);
            DepositBankManualMsg msg = new DepositBankManualMsg();
            if (res != null) {
                msg.Error = (byte) res.getCode();
                Debug.trace((Object) ("depositBankManual " + res.getMessage()));
                if (msg.Error == DvtConst.RECHARGE_STATUS_SUCCESS) {
                    msg.data = res.getMessage();
                }
            } else {
                msg.Error = (byte) 1;
            }
            this.send((BaseMsg) msg, user);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void getBankAvailable(User user) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://api.3456789.pro/api?c=GetBankAvailable&apiKey=aa399aba-c9d5-4eb7-8002-140ca4b38b8d")
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            GetBankAvailableManualMsg msg = new GetBankAvailableManualMsg();
            if (response.code() == 200 && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body());
                if (jsonObject.has("stt") && jsonObject.getInt("stt") != 1) {
                    msg.Error = (byte) 0;
                    Debug.trace((Object) ("getBankAvailable " + jsonObject.toString()));
                    if (msg.Error == DvtConst.RECHARGE_STATUS_SUCCESS) {
                        msg.data = jsonObject.getJSONArray("data").toString();
                    }
                } else {
                    msg.Error = (byte) 1;
                }

            } else {
                msg.Error = (byte) 1;
            }

            this.send((BaseMsg) msg, user);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void depositBank(User user, DataCmd dataCmd) {
        try {
            DepositBankCmd cmd = new DepositBankCmd(dataCmd);
            String nickname = user.getName();
            Debug.trace((Object) ("depositBankManual "));
            RechargeResponse res = this.rechargeService.rechargeByBankManual(nickname, cmd.amount, cmd.code);
            DepositBankManualMsg msg = new DepositBankManualMsg();
            if (res != null) {
                msg.Error = (byte) res.getCode();
                Debug.trace((Object) ("depositBankManual " + res.getMessage()));
                if (msg.Error == DvtConst.RECHARGE_STATUS_SUCCESS) {
                    msg.data = res.getMessage();
                }
            } else {
                msg.Error = (byte) 1;
            }
            this.send((BaseMsg) msg, user);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void napTheDienThoai(User user, DataCmd dataCmd) {

        GachTheDienThoaiCmd cmd = new GachTheDienThoaiCmd(dataCmd);
        byte result = 1;
        RechargeResponse res = null;
        try {
            Debug.trace((Object) ("Recharge error: " + PartnerConfig.CongGachThe + ":" + cmd.provider + ":" + cmd.serial + ":" + cmd.pin + ":" + cmd.menhgia));
            Platform platform = Platform.find((String) ((String) user.getProperty((Object) "pf")));
            cmd.menhgia = cmd.menhgia.replace(".", "");
            cmd.menhgia = cmd.menhgia.replace(",", "");
            //res = this.rechargeService.rechargeByCard(user.getName(), ProviderType.getProviderById((int)cmd.provider), cmd.serial, cmd.pin, platform.getName());
            logger.debug((Object) ("napTheDienThoai:" + PartnerConfig.CongGachThe.trim()));

            if (PartnerConfig.CongGachThe.trim().equals("GACHTHE")) {
                Debug.trace((Object) ("GACHTHEnapTheDienThoai "));
                res = this.rechargeService.rechargeByGachThe(user.getName(), ProviderType.getProviderById((int) cmd.provider), cmd.serial, cmd.pin, cmd.menhgia, platform.getName(), user.getId());
            } else if (PartnerConfig.CongGachThe.trim().equals("HADONGPHO")) {
                Debug.trace((Object) ("GACHTHEnapTheDienThoai "));
                res = this.rechargeService.rechargeByGachTheHaDongPho(user.getName(), ProviderType.getProviderById((int) cmd.provider), cmd.serial, cmd.pin, cmd.menhgia, platform.getName(), user.getId());
            } else {
                res = new RechargeResponse(-1, 0, 0, 0);
            }
        } catch (Exception e) {
            logger.debug("napTheDienThoai error:" + e.getMessage());
            Debug.trace((Object) ("Recharge error: " + e.getMessage()));
        }
        Debug.trace("rechargeByGachThe  3333");
        NapTheDienThoaiMsg msg = new NapTheDienThoaiMsg();
        if (res != null) {
            msg.Error = (byte) res.getCode();
            msg.currentMoney = res.getCurrentMoney();
            msg.numFail = res.getFail();
            msg.timeFail = res.getTime();
            msg.message = res.getMessage();
        } else {
            msg.Error = result;
        }
        RechargeServiceImpl.logger.debug("rechargeByGachThe  44444" + msg.Error + "  " + msg.message);
        Debug.trace((Object) ("Recharge Gachthe error: " + msg.Error));
        this.send((BaseMsg) msg, user);
    }

    private void playVQMM(User user) {
//        LuckyServiceImpl service = new LuckyServiceImpl();
//        LuckyResponse response = service.getResultLuckyRotation(user.getName(), user.getIpAddress());
//        VQMMMsg msg = new VQMMMsg();
//        switch (response.getErrorCode()) {
//            case "0": {
//                msg.Error = 0;
//                break;
//            }
//            case "3001": {
//                msg.Error = 2;
//                break;
//            }
//            case "3002": {
//                msg.Error = 3;
//                break;
//            }
//            default: {
//                msg.Error = 1;
//            }
//        }
//        msg.prizeVin = response.getResultVin();
//        msg.prizeXu = response.getResultXu();
//        msg.prizeSlot = response.getResultSlot();
//        msg.remainCount = (short)response.getRotateCount();
//        msg.currentMoneyVin = response.getCurrentMoneyVin();
//        msg.currentMoneyXu = response.getCurrentMoneyXu();
//        this.send(msg, user);
    }

    private void getVQVip(User user) {
//        LuckyServiceImpl service = new LuckyServiceImpl();
//        LuckyVipResponse response = service.rotateLuckyVip(user.getName(), true);
//        GetVQVipMsg msg = new GetVQVipMsg();
//        msg.remainCount = (short)response.getRotateCount();
//        this.send(msg, user);
    }

    private void playVQVip(User user) {
//        LuckyServiceImpl service = new LuckyServiceImpl();
//        LuckyVipResponse response = service.rotateLuckyVip(user.getName(), false);
//        VQVipMsg msg = new VQVipMsg();
//        switch (response.getErrorCode()) {
//            case "0": {
//                msg.Error = 0;
//                break;
//            }
//            case "1201": {
//                msg.Error = 2;
//                break;
//            }
//            case "1202": {
//                msg.Error = 3;
//                break;
//            }
//            case "1203": {
//                msg.Error = 4;
//                break;
//            }
//            case "1204": {
//                msg.Error = 5;
//                break;
//            }
//            default: {
//                msg.Error = 1;
//            }
//        }
//        msg.prizeVin = response.getResultVin();
//        msg.prizeMulti = (short)response.getResultMulti();
//        msg.remainCount = (short)response.getRotateCount();
//        msg.currentMoneyVin = response.getCurrentMoney();
//        this.send(msg, user);
    }

    private void requestApiOTP(User user, DataCmd dataCmd) {
    }

    private void confirmApiOTP(User user, DataCmd dataCmd) {
    }

//    private void checkIAP(User user, DataCmd dataCmd) {
//        CheckIAPCmd cmd = new CheckIAPCmd(dataCmd);
//        CheckIAPMsg msg = new CheckIAPMsg();
//        msg.Error = this.rechargeService.checkRechargeIAP(user.getName(), (int)cmd.productId);
//        this.send(msg, user);
//    }

//    private void resultIAP(User user, DataCmd dataCmd) {
//        ResultIAPCmd cmd = new ResultIAPCmd(dataCmd);
//        ResultIAPMsg msg = new ResultIAPMsg();
//        Debug.trace(("nickname: " + user.getName()));
//        Debug.trace(("signedData: " + cmd.signedData));
//        Debug.trace(("signature: " + cmd.signature));
//        RechargeIAPResponse rcRes = this.rechargeService.rechargeIAP(user.getName(), cmd.signedData, cmd.signature);
//        msg.Error = (byte)rcRes.getCode();
//        msg.productId = (byte)rcRes.getProductId();
//        msg.currentMoney = rcRes.getCurrentMoney();
//        this.send(msg, user);
//    }

    private void getInfo(User user) {
        GetInfoMsg msg = new GetInfoMsg();
        UserCacheModel userCache = this.userService.getUser(user.getName());
        try {
            if (userCache != null) {
                msg.Error = 0;
                msg.isVerifyPhone = userCache.isVerifyMobile();
                msg.username = userCache.getUsername();
                msg.cmt = userCache.getIdentification() != null ? userCache.getIdentification() : "";
                msg.mobile = userCache.getMobile() != null ? userCache.getMobile() : "";
                msg.email = userCache.getEmail() != null ? userCache.getEmail() : "";
                msg.mobileSecure = userCache.isHasMobileSecurity() ? (byte) 1 : 0;
                msg.emailSecure = userCache.isHasEmailSecurity() ? (byte) 1 : 0;
                msg.appSecure = userCache.isHasAppSecurity() ? (byte) 1 : 0;
                msg.loginSecure = userCache.isHasLoginSecurity() ? (byte) 1 : 0;
                msg.moneyLoginotp = userCache.getLoginOtp();
                msg.usertype = userCache.getUsertype();
//                ObjectMapper mapper = new ObjectMapper();
                msg.configGame = "";
                msg.referralCode = userCache.getReferralCode() == null ? "" : userCache.getReferralCode();
                msg.birthday = userCache.getBirthday() == null ? "" : userCache.getBirthday();
                msg.address = userCache.getAddress() == null ? "" : userCache.getAddress();
                try {
                    msg.gender = userCache.isGender();
                } catch (Exception e) {
                    msg.gender = false;
                }
                msg.safe = userCache.getSafe();
                msg.moneyUse = userCache.getVin();
            } else {
                msg.Error = 1;
            }
        } catch (Exception e) {
            Debug.trace("LobbyModule get info error: " + e);
            msg.Error = 1;
        }
        this.send(msg, user);
    }

    private void getMoneyUse(User user, DataCmd dataCmd) {
        GetMoneyUseMsg msg = new GetMoneyUseMsg();
        msg.moneyUse = this.userService.getMoneyUserCache(user.getName(), "vin");
        this.send(msg, user);
    }

    private void doiPass(User user, DataCmd dataCmd) {
        DoiPassCmd cmd = new DoiPassCmd(dataCmd);
        byte res = this.securityService.changePassword(user.getName(), cmd.oldPass, cmd.newPass, true);
        DoiPassMsg msg = new DoiPassMsg();
        if (res == 0) {
            user.setProperty(CURRENT_COMMAND, dataCmd.getId());
            user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
            msg.Error = 4;
        } else {
            msg.Error = res;
        }

        this.send(msg, user);
    }

    private void doiVippoint(User user, DataCmd dataCmd) {
        DoiVippointCmd cmd = new DoiVippointCmd(dataCmd);
        byte res = this.vpService.checkCashoutVP(user.getName());
        if (res == 0) {
            user.setProperty(CURRENT_COMMAND, dataCmd.getId());
            user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
            // send otp
//            OtpServiceImpl otpService = new OtpServiceImpl();
//            try {
//                int ret = otpService.sendVoiceOtp(user.getName(), "");
//                if(ret != 0){
//                    Debug.trace("Cannot send OTP message!");
//                }
//            }
//            catch (Exception e) {
//                Debug.trace(("LobbyModule error: " + e));
//            }
        }
        DoiVippointMsg msg = new DoiVippointMsg();
        msg.Error = res;
        this.send(msg, user);
    }

    private void updateUserInfo(User user, DataCmd dataCmd) {
        UpdateUserInfoCmd cmd = new UpdateUserInfoCmd(dataCmd);
        if (cmd.cmt == null)
            cmd.cmt = "";

        if (cmd.email == null)
            cmd.email = "";

        if (cmd.birthday == null || cmd.birthday.isEmpty())
            cmd.birthday = "";

        if (cmd.address == null)
            cmd.address = "";

        if (cmd.referralCode == null)
            cmd.referralCode = "";

        byte res = this.securityService.updateUserInfo(user.getName(), cmd.cmt, cmd.email, cmd.birthday, cmd.gender, cmd.address, cmd.referralCode);
        UpdateUserInfoMsg msg = new UpdateUserInfoMsg();
        msg.Error = res;
        this.send(msg, user);
    }

    private void updateEmail(User user, DataCmd dataCmd) {
        UpdateEmailCmd cmd = new UpdateEmailCmd(dataCmd);
        byte res = this.securityService.updateEmail(user.getName(), cmd.email);
        UpdateEmailMsg msg = new UpdateEmailMsg();
        msg.Error = res;
        this.send(msg, user);
    }

    private void updateMobile(User user, DataCmd dataCmd) {
        UpdateMobileCmd cmd = new UpdateMobileCmd(dataCmd);
        byte res = this.securityService.updateMobile(user.getName(), cmd.mobile);
        UpdateMobileMsg msg = new UpdateMobileMsg();
        msg.Error = res;
        this.send(msg, user);
    }

    private void activeEmail(User user, DataCmd dataCmd) {
        byte res = this.securityService.activeEmail(user.getName());
        ActiveEmailMsg msg = new ActiveEmailMsg();
        msg.Error = res;
        this.send(msg, user);
    }

    private void activeMobile(User user, DataCmd dataCmd) {
        ActiveMobileCmd cmd = new ActiveMobileCmd(dataCmd);
//        byte res = this.securityService.activeMobile(user.getName(), true);
//        if (res == 0) {
//            user.setProperty(CURRENT_COMMAND, dataCmd.getId());
//            user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
//        }
        ActiveMobileMsg msg = new ActiveMobileMsg();
        try {
            int ret = otpService.sendOtpEsms(user.getName(), cmd.mobile);
            logger.debug("activeMobile " + ret);
            if (ret != 0) {
                Debug.trace("Cannot send OTP message!");
            }
            msg.Error = (byte) ret;
        } catch (Exception e) {
            Debug.trace(("LobbyModule error: " + e));
        }

        this.send(msg, user);
    }

    private void activeMobileValidate(User user, DataCmd dataCmd) {
        ActiveMobileValidateCmd cmd = new ActiveMobileValidateCmd(dataCmd);
        ActiveMobileValidateMsg msg = new ActiveMobileValidateMsg();
        try {
            int res = otpService.checkOtp(cmd.otp, user.getName(), "0", cmd.mobile);
            Debug.trace("activeMobile  " + res);
            msg.Error = (byte) res;
            if (res == 0) {
                userService.updateVerifyMobile(user.getName(), cmd.mobile, true);
                this.securityService.activeMobile(user.getName(), true);

                HazelcastInstance instance = HazelcastClientFactory.getInstance();
                IMap userMap = instance.getMap("users");
                UserCacheModel userCache = null;
                if (userMap.containsKey(user.getName())) {
                    try {
                        userMap.lock(user.getName());
                        userCache = (UserCacheModel) userMap.get(user.getName());

                        userCache.setMobile(cmd.mobile);
                        userCache.setVerifyMobile(true);
                        userMap.put(user.getName(), userCache);
                    } catch (Exception e) {
                        logger.debug(e);
                    } finally {
                        userMap.unlock(user.getName());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        this.send(msg, user);
    }

    private void updateNewMobile(User user, DataCmd dataCmd) {
        UpdateNewMobileCmd cmd = new UpdateNewMobileCmd(dataCmd);


        byte res = this.securityService.updateNewMobile(user.getName(), cmd.mobile, true);
        if (res == 0) {
            user.setProperty(CURRENT_COMMAND, dataCmd.getId());
            user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
            user.setProperty(UPDATE_NEW_MOBILE, cmd.mobile);
        }
        UpdateNewMobileMsg msg = new UpdateNewMobileMsg();

//        OtpServiceImpl otpService = new OtpServiceImpl();
//        try {
//            int ret = otpService.sendVoiceOtp(user.getName(), cmd.mobile);
//            if(ret != 0){
//                Debug.trace("Cannot send OTP message!");
//            }
//        }
//        catch (Exception e) {
//            Debug.trace(("LobbyModule error: " + e));
//        }
        msg.Error = res;
        this.send(msg, user);
    }

    private void loginOtp(User user, DataCmd dataCmd) {
        LoginOtpCmd cmd = new LoginOtpCmd(dataCmd);
        user.setProperty(CURRENT_COMMAND, dataCmd.getId());
        user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
        try {
            int ret = otpService.sendVoiceOtp(user.getName(), "", true);
            if (ret != 0) {
                Debug.trace("Cannot send OTP message!");
            }
        } catch (Exception e) {
            Debug.trace(("LobbyModule error: " + e));
        }
    }

    private void ketSat(User user, DataCmd dataCmd) {
        KetSatCmd cmd = new KetSatCmd(dataCmd);
        byte res = 1;
        Debug.trace("ketSat type: " + cmd.type + " moneyExchange: " + cmd.moneyExchange);
        if (cmd.type == 0) {
            KetSatMsg msg = new KetSatMsg();
            MoneyResponse moneyres = this.securityService.takeMoneyInSafe(user.getName(), cmd.moneyExchange, cmd.otp, false);
            if (moneyres.getErrorCode().equals("0")) {
                res = 0;
                user.setProperty(CURRENT_COMMAND, dataCmd.getId());
                user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
            } else if (moneyres.getErrorCode().equals("1002")) {
                res = 2;
            }
            msg.moneyUse = moneyres.getMoneyUse();
            msg.safe = moneyres.getSafeMoney();
            msg.Error = res;
            msg.type = cmd.type;
            this.send(msg, user);
        } else if (cmd.type == 1) {
            ResultKetSatMsg ksMsg = new ResultKetSatMsg();
            MoneyResponse moneyres = this.securityService.sendMoneyToSafe(user.getName(), cmd.moneyExchange, false);
            if (moneyres.getErrorCode().equals("0")) {
                res = 0;
            } else if (moneyres.getErrorCode().equals("1002")) {
                res = 2;
            }
            ksMsg.moneyUse = moneyres.getMoneyUse();
            ksMsg.safe = moneyres.getSafeMoney();
            ksMsg.currentMoney = moneyres.getCurrentMoney();
            ksMsg.Error = res;
            ksMsg.type = cmd.type;
            this.send(ksMsg, user);
        }
        getInfo(user);
    }

    private synchronized void chuyenKhoan(User user, DataCmd dataCmd) {
        ChuyenKhoanCmd cmd = new ChuyenKhoanCmd(dataCmd);
        TransferMoneyResponse res;
        if (user.getName().toLowerCase().equals(cmd.receiver.toLowerCase())) {
            res = new TransferMoneyResponse((byte) 22, 0, 0);
        } else {
            res = this.userService.transferMoney(user.getName(), cmd.receiver, cmd.moneyExchange, cmd.description, false);
            if (res.getCode() == 0) {
                user.setProperty((Object) CURRENT_COMMAND, (Object) dataCmd.getId());
                user.setProperty((Object) CURRENT_OBJECT_COMMAND, (Object) cmd);
            }
        }

        ChuyenKhoanMsg msg = new ChuyenKhoanMsg();
        msg.Error = res.getCode();
        msg.moneyUse = res.getMoneyUse();
        this.send((BaseMsg) msg, user);
    }

    private QuotaResponse CheckQuota(String nick_name, boolean seven_days) {
        QuotaResponse response = new QuotaResponse();
        try {
            UserModel userModel = userService.getUserByNickName(nick_name);
            if (userModel != null) {
                if (userModel.getDaily() == 1 || userModel.getDaily() == 2) {
                    response.setCode(0);
                    return response;
                }
                // nap the                   
                LogMoneyUserDao logService = new LogMoneyUserDaoImpl();
                long total_giftcode_money = 0;
                long total_user_receive = 0;
                long total_agency_receive = 0;
                long total_user_transfer = 0;
                long total_agency_transfer = 0;

                AgentServiceImpl service = new AgentServiceImpl();
                List<AgentResponse> agents = service.listAgent();
                ArrayList<String> agentNames = new ArrayList<String>();
                if (agents != null && agents.size() > 0) {
                    for (AgentResponse agent : agents) {
                        agentNames.add(agent.nickName);
                    }
                }
                List<LogUserMoneyResponse> resultGiftCode = logService.searchAllLogMoneyUser(nick_name, "GIFTCODE", seven_days);
                if (resultGiftCode != null && resultGiftCode.size() > 0) {
                    total_giftcode_money = resultGiftCode.stream().map((trans) -> trans.moneyExchange).reduce(total_giftcode_money, (accumulator, _item) -> accumulator + _item);
                }
                List<LogUserMoneyResponse> resulReceive = logService.searchAllLogMoneyUser(nick_name, "RECEIVE", seven_days);
                if (resulReceive != null && resulReceive.size() > 0) {
                    for (LogUserMoneyResponse trans : resulReceive) {
                        boolean matchAgent = false;
                        for (String s : agentNames) {
                            if (trans.description.contains(s)) {
                                matchAgent = true;
                            }
                        }
                        if (matchAgent) {
                            total_agency_receive += trans.moneyExchange;
                        } else {
                            total_user_receive += trans.moneyExchange;
                        }
                    }
                }
                List<LogUserMoneyResponse> resulTransfer = logService.searchAllLogMoneyUser(nick_name, "TRANSFER", seven_days);
                if (resulTransfer != null && resulTransfer.size() > 0) {
                    for (LogUserMoneyResponse trans : resulTransfer) {
                        boolean matchAgent = false;
                        for (String s : agentNames) {
                            if (trans.description.contains(s)) {
                                matchAgent = true;
                            }
                        }
                        if (matchAgent) {
                            total_agency_transfer += trans.moneyExchange;
                        } else {
                            total_user_transfer += trans.moneyExchange;
                        }
                    }
                }
                long total_recharge_card_money = 0;//total_agency_receive - userModel.getRechargeMoney();                
                List<LogUserMoneyResponse> resultCard = logService.searchAllLogMoneyUser(nick_name, "CARD", seven_days);
                if (resultCard != null && resultCard.size() > 0) {
                    total_recharge_card_money = resultCard.stream().map((trans) -> trans.moneyExchange).reduce(total_recharge_card_money, (accumulator, _item) -> accumulator + _item);
                }
                // formula
                long quota = 0;
                //quota += (total_recharge_card_money + total_agency_receive + total_user_receive + total_giftcode_money);
                quota += total_recharge_card_money; // nap the x3
                quota += total_agency_receive; // nap dai ly x3;
                quota += total_user_receive * 4; // nguoi choi khac chuyen x6;
                quota += total_giftcode_money * 4; // tien gift code * 6;                 
                // tru di so tien chuyen di
                quota += total_agency_transfer;
                quota += total_user_transfer;
                Debug.trace("Quota:" + quota);
                long total_bet_money = 0 - logService.getTotalBetWin(nick_name, "BET", null);
                long total_win_money = logService.getTotalBetWin(nick_name, "WIN", null);
                // get total bet taixiu
                //long total_bet_tx_money = 0 - logService.getTotalBetWin(nick_name, "BET","TaiXiu");
                //long total_win_tx_money = logService.getTotalBetWin(nick_name, "WIN","TaiXiu");
                //long taixiu_bet_win = total_bet_tx_money - total_win_tx_money;
                //if (taixiu_bet_win < 0)
                //    taixiu_bet_win = 0 - taixiu_bet_win;
                // Ban Ca                
                long total_bet_banca_money = 0 - logService.getTotalBetWin(nick_name, "BET", "HamCaMap");
                long total_bet_slot_money = 0 - logService.getTotalBetWin(nick_name, "BET", "SLOT");
                long finalQuota = total_bet_money - (total_bet_banca_money * 9) / 10 - (total_bet_slot_money * 9) / 10;
                long manual_quota = userModel.getManual_quota();
                finalQuota += manual_quota;
                //long total_win_banca_money = logService.getTotalBetWin(nick_name, "WIN","HamCaMap");
                //Debug.trace("Final quota :" + ((0 - total_bet_money) - quota));
                Debug.trace("Final quota :" + finalQuota);
                // check quote
                if (finalQuota > quota) {
                    response.setCode(0);
                } else {
                    response.setCode(1);
                }
                response.setTotal_agency_receive(total_agency_receive);
                response.setTotal_agency_transfer(total_agency_transfer);
                response.setTotal_bet_money(total_bet_money);
                response.setTotal_giftcode_money(total_giftcode_money);
                response.setTotal_recharge_card_money(total_recharge_card_money);
                response.setTotal_user_receive(total_user_receive);
                response.setTotal_user_transfer(total_user_transfer);
                response.setTotal_win_money(total_win_money);
            }
        } catch (Exception ex) {
            response.setCode(-1);
            Debug.trace((Object) ("Check quote error : " + ex.getMessage()));
        }
        return response;
    }

    private void gameConfig(User user, DataCmd dataCmd) {
        GameConfigCmd cmd = new GameConfigCmd(dataCmd);
        user.setProperty(CURRENT_COMMAND, dataCmd.getId());
        user.setProperty(CURRENT_OBJECT_COMMAND, cmd);
    }


    private void checkUser(User user, DataCmd dataCmd) {
        CheckUserCmd cmd = new CheckUserCmd(dataCmd);
        byte type1 = this.userService.checkUser(user.getName());
        byte type2 = this.userService.checkUser(cmd.nickname);
        CheckUserMsg msg = new CheckUserMsg();
        if (type2 == -1) {
            msg.Error = 0;
        } else {
            msg.Error = 1;
            msg.fee = this.userService.calFeeTransfer((int) type1, (int) type2);
        }
        msg.type = type2;
        this.send(msg, user);
    }

    private void sendOtp(User user, DataCmd dataCmd) {
        SendOtpTypeCmd cmd = new SendOtpTypeCmd(dataCmd);
        UserDaoImpl userDao = new UserDaoImpl();
        UserModel model = null;
        try {
            model = userDao.getUserByNickName(user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SendOTPMsg msg = new SendOTPMsg();
        msg.Error = 1;
        if (model == null) {
            this.send(msg, user);
            return;
        }
        if (cmd.type == 0) {
            try {
                int ret = otpService.sendOtpEsms(user.getName(), model.getMobile());
                if (ret != 0) {
                    Debug.trace("Cannot send OTP message!");
                }
                msg.Error = (byte) ret;
            } catch (Exception e) {
                Debug.trace(("LobbyModule error: " + e));
            }
        } else {
            try {
                int ret = otpService.sendOtpTele(user.getName());
                if (ret != 0) {
                    Debug.trace("Cannot send OTP message!");
                }
                msg.Error = (byte) ret;
            } catch (Exception e) {
                Debug.trace(("LobbyModule error: " + e));
            }
        }

        this.send(msg, user);
    }


    private byte parseErrorCodeGiftCode(String errorCode) {
        if (errorCode.equals("0")) {
            return 2;
        }
        if (errorCode.equals("10002")) {
            return 1;
        }
        if (errorCode.equals("10003")) {
            return 3;
        }
        if (errorCode.equals("10004")) {
            return 4;
        }
        return 0;
    }

    public void handleServerEvent(IBZEvent ibzevent) throws BZException {
        if (ibzevent.getType() == BZEventType.USER_DISCONNECT) {
            User user = (User) ibzevent.getParameter((IBZEventParam) BZEventParam.USER);
            this.unSubscribeJackPot(user);
        }
    }

    private void subscribeJackPot(User user) {
        this.usersSubJackpot.add(user);
    }

    private void unSubscribeJackPot(User user) {
        this.usersSubJackpot.remove(user);
    }

    public void updateJackpot() {
//        try {
//            CacheServiceImpl cacheService = new CacheServiceImpl();
//            int miniPoker100 = cacheService.getValueInt(Games.MINI_POKER.getName() + "_vin_100");
//            int miniPoker1000 = cacheService.getValueInt(Games.MINI_POKER.getName() + "_vin_1000");
//            int miniPoker10000 = cacheService.getValueInt(Games.MINI_POKER.getName() + "_vin_10000");
//            int pokeGo100 = cacheService.getValueInt(Games.POKE_GO.getName() + "_vin_100");
//            int pokeGo1000 = cacheService.getValueInt(Games.POKE_GO.getName() + "_vin_1000");
//            int pokeGo10000 = cacheService.getValueInt(Games.POKE_GO.getName() + "_vin_10000");
//            int khoBau100 = cacheService.getValueInt(Games.KHO_BAU.getName() + "_vin_100");
//            int khoBau1000 = cacheService.getValueInt(Games.KHO_BAU.getName() + "_vin_1000");
//            int khoBau10000 = cacheService.getValueInt(Games.KHO_BAU.getName() + "_vin_10000");
//            int ndv100 = cacheService.getValueInt(Games.NU_DIEP_VIEN.getName() + "_vin_100");
//            int ndv1000 = cacheService.getValueInt(Games.NU_DIEP_VIEN.getName() + "_vin_1000");
//            int ndv10000 = cacheService.getValueInt(Games.NU_DIEP_VIEN.getName() + "_vin_10000");
//            int avengers100 = cacheService.getValueInt(Games.AVENGERS.getName() + "_vin_100");
//            int avengers1000 = cacheService.getValueInt(Games.AVENGERS.getName() + "_vin_1000");
//            int avengers10000 = cacheService.getValueInt(Games.AVENGERS.getName() + "_vin_10000");
//            int vqv100 = cacheService.getValueInt(Games.VUONG_QUOC_VIN.getName() + "_vin_100");
//            int vqv1000 = cacheService.getValueInt(Games.VUONG_QUOC_VIN.getName() + "_vin_1000");
//            int vqv10000 = cacheService.getValueInt(Games.VUONG_QUOC_VIN.getName() + "_vin_10000");
//            int fish100 = cacheService.getValueInt(Games.HAM_CA_MAP.getName() + "_vin_100");
//            int fish1000 = cacheService.getValueInt(Games.HAM_CA_MAP.getName() + "_vin_1000");
//
//            //spartan game
//
//            int spartan100 = cacheService.getValueInt(Games.SPARTAN.getName() + "_vin_100");
//            int spartan1000 = cacheService.getValueInt(Games.SPARTAN.getName() + "_vin_1000");
//            int spartan5000 = cacheService.getValueInt(Games.SPARTAN.getName() + "_vin_5000");
//            int spartan10000 = cacheService.getValueInt(Games.SPARTAN.getName() + "_vin_10000");
//
//            UpdateJackpotMsg msg = new UpdateJackpotMsg();
//            msg.potMiniPoker100 = miniPoker100;
//            msg.potMiniPoker1000 = miniPoker1000;
//            msg.potMiniPoker10000 = miniPoker10000;
//            msg.potPokeGo100 = pokeGo100;
//            msg.potPokeGo1000 = pokeGo1000;
//            msg.potPokeGo10000 = pokeGo10000;
//            msg.potKhoBau100 = khoBau100;
//            msg.potKhoBau1000 = khoBau1000;
//            msg.potKhoBau10000 = khoBau10000;
//            msg.potNDV100 = ndv100;
//            msg.potNDV1000 = ndv1000;
//            msg.potNDV10000 = ndv10000;
//            msg.potAvengers100 = avengers100;
//            msg.potAvengers1000 = avengers1000;
//            msg.potAvengers10000 = avengers10000;
//            msg.vqv100 = vqv100;
//            msg.vqv1000 = vqv1000;
//            msg.vqv10000 = vqv10000;
//            msg.fish100 = fish100;
//            msg.fish1000 = fish1000;
//
//            msg.sparta100 = spartan100;
//            msg.sparta1000 = spartan1000;
//            msg.sparta5000 = spartan1000;
//            msg.sparta10000 = spartan10000;
//            for (User user : this.usersSubJackpot) {
//                if (user == null) continue;
//                this.send(msg, user);
//            }
//        }
//        catch (Exception e) {
//            Debug.trace(("Update jackpot exception: 1 " + e.getMessage()));
//        }
    }

    public void updateHuVang(User user) {
        HuVangMsg msg = new HuVangMsg();
        msg.huBaCay = HuVangConfig.instance().getThoiGianHuVang(Games.BA_CAY.getName());
        msg.huBaiCao = HuVangConfig.instance().getThoiGianHuVang(Games.BAI_CAO.getName());
        msg.huBinh = HuVangConfig.instance().getThoiGianHuVang(Games.BINH.getName());
        msg.huSam = HuVangConfig.instance().getThoiGianHuVang(Games.SAM.getName());
        msg.huTLMN = HuVangConfig.instance().getThoiGianHuVang(Games.TLMN.getName());
        this.send(msg, user);
    }

    private void gameLoop() {
        ++this.countUpdateJackpot;
        if (this.countUpdateJackpot >= (long) ConfigGame.getIntValue("update_jackpot_time")) {
            this.updateJackpot();
            this.countUpdateJackpot = 0L;
        }
    }

    private void getEventInfo(User user, DataCmd dataCmd) {
        GetEventVPInfoMsg msg = new GetEventVPInfoMsg();
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
        if (sLucky != null && sLucky.equals("1")) {
            Date now = new Date();
            try {
                String currentDate = VinPlayUtils.getDateTimeStr((Date) now).substring(0, 11);
                String etLuckyToday = currentDate + VippointUtils.END_LUCKY_TIME;
                Date eventLuckyTimeEnd = VinPlayUtils.getDateTime((String) etLuckyToday);
                if (now.getTime() < eventLuckyTimeEnd.getTime()) {
                    msg.status = 1;
                    msg.time = (eventLuckyTimeEnd.getTime() - now.getTime()) / 1000L;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            msg.status = 0;
            msg.time = 0L;
        }
        this.send(msg, user);
    }

    private void initVP() throws JSONException, SQLException, ParseException {
        VippointUtils.init();
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        this.eventTimeStart = VinPlayUtils.getDateTime((String) VippointUtils.START);
        this.eventTimeEnd = VinPlayUtils.getDateTime((String) VippointUtils.END);
        Date eventX2End = VippointUtils.END_X2_TIME;
        Date eventLuckyTimeStart = VinPlayUtils.getDateTime((String) (VippointUtils.START.substring(0, 11) + VippointUtils.START_LUCKY_TIME));
        Date eventLuckyTimeEnd = VinPlayUtils.getDateTime((String) (VippointUtils.START.substring(0, 11) + VippointUtils.END_LUCKY_TIME));
        this.timeLucky = eventLuckyTimeEnd.getTime() - eventLuckyTimeStart.getTime();
        Debug.trace(("time lucky: " + this.timeLucky));
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap<String, String> map = instance.getMap("cacheConfig");
        if (now.getTime() < this.eventTimeStart.getTime()) {
            Debug.trace(("event chua dien ra: " + new Date()));
            map.put("VIPPOINT_EVENT_STATUS", "0");
            map.put("VIPPOINT_EVENT_X2_STATUS", "0");
            map.put("VIPPOINT_EVENT_LUCKY", "0");
            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventStartTask, this.calculateRemainTime(this.eventTimeStart), TimeUnit.SECONDS);
            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventEndTask, this.calculateRemainTime(this.eventTimeEnd), TimeUnit.SECONDS);
            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventX2EndTask, this.calculateRemainTime(eventX2End), TimeUnit.SECONDS);
            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyStartTask, this.calculateRemainTime(eventLuckyTimeStart), TimeUnit.SECONDS);
            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyEndTask, this.calculateRemainTime(eventLuckyTimeEnd), TimeUnit.SECONDS);
        } else if (now.getTime() < this.eventTimeEnd.getTime()) {
            Debug.trace("event dang dien ra");
            map.put("VIPPOINT_EVENT_STATUS", "1");
            if (now.getTime() < eventX2End.getTime()) {
                Debug.trace("event x2 dang dien ra");
                map.put("VIPPOINT_EVENT_X2_STATUS", "1");
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventX2EndTask, this.calculateRemainTime(eventX2End), TimeUnit.SECONDS);
            } else {
                Debug.trace("event x2 da ket thuc");
                map.put("VIPPOINT_EVENT_X2_STATUS", "0");
            }
            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventEndTask, this.calculateRemainTime(this.eventTimeEnd), TimeUnit.SECONDS);
            String currentDate = VinPlayUtils.getDateTimeStr((Date) now).substring(0, 11);
            String stLuckyToday = currentDate + VippointUtils.START_LUCKY_TIME;
            String etLuckyToday = currentDate + VippointUtils.END_LUCKY_TIME;
            eventLuckyTimeStart = VinPlayUtils.getDateTime((String) stLuckyToday);
            eventLuckyTimeEnd = VinPlayUtils.getDateTime((String) etLuckyToday);
            if (now.getTime() < eventLuckyTimeStart.getTime()) {
                Debug.trace("event lucky chua dien ra");
                map.put("VIPPOINT_EVENT_LUCKY", "0");
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyStartTask, this.calculateRemainTime(eventLuckyTimeStart), TimeUnit.SECONDS);
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyEndTask, this.calculateRemainTime(eventLuckyTimeEnd), TimeUnit.SECONDS);
            } else if (now.getTime() < eventLuckyTimeEnd.getTime()) {
                Debug.trace("event lucky dang dien ra");
                List unluckyTime = VippointUtils.randomUnluckyTime((Date) this.eventTimeStart, (Date) this.eventTimeEnd);
                Debug.trace("unluckyTime: ");
                for (Object dt : unluckyTime) {
                    if (now.getTime() < ((Date) dt).getTime()) {
                        BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventUnluckyTask, this.calculateRemainTime((Date) dt) / 60, TimeUnit.MINUTES);
                        Debug.trace(("OK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                        continue;
                    }
                    Debug.trace(("NOK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                }
                List<Date> luckyTime = VippointUtils.randomLuckyTime((Date) this.eventTimeStart, (Date) this.eventTimeEnd);
                Debug.trace("luckyTime: ");
                for (Date dt : luckyTime) {
                    if (now.getTime() < dt.getTime()) {
                        BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyTask, this.calculateRemainTime(dt) / 60, TimeUnit.MINUTES);
                        Debug.trace(("OK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                        continue;
                    }
                    Debug.trace(("NOK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                }
                String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
                if (sLucky != null && sLucky.equals("0")) {
                    this.sendMsgToAllUser((byte) 1, eventLuckyTimeEnd.getTime() - now.getTime());
                }
                map.put("VIPPOINT_EVENT_LUCKY", "1");
                cal.setTime(eventLuckyTimeStart);
                cal.add(5, 1);
                eventLuckyTimeStart = cal.getTime();
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyStartTask, this.calculateRemainTime(eventLuckyTimeStart), TimeUnit.SECONDS);
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyEndTask, this.calculateRemainTime(eventLuckyTimeEnd), TimeUnit.SECONDS);
            } else {
                Debug.trace("event lucky da het");
                String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
                if (sLucky != null && sLucky.equals("1")) {
                    this.sendMsgToAllUser((byte) 0, 0L);
                }
                map.put("VIPPOINT_EVENT_LUCKY", "0");
                cal.setTime(eventLuckyTimeStart);
                cal.add(5, 1);
                eventLuckyTimeStart = cal.getTime();
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyStartTask, this.calculateRemainTime(eventLuckyTimeStart), TimeUnit.SECONDS);
                cal.setTime(eventLuckyTimeEnd);
                cal.add(5, 1);
                eventLuckyTimeEnd = cal.getTime();
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) this.eventLuckyEndTask, this.calculateRemainTime(eventLuckyTimeEnd), TimeUnit.SECONDS);
            }
        } else {
            Debug.trace("event da ket thuc");
            map.put("VIPPOINT_EVENT_STATUS", "0");
            map.put("VIPPOINT_EVENT_X2_STATUS", "0");
            String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
            if (sLucky != null && sLucky.equals("1")) {
                this.sendMsgToAllUser((byte) 0, 0L);
            }
            map.put("VIPPOINT_EVENT_LUCKY", "0");
        }
    }

    private int calculateRemainTime(Date runTime) {
        int time = 0;
        Date now = new Date();
        if (runTime.getTime() > now.getTime()) {
            time = (int) ((runTime.getTime() - now.getTime()) / 1000L);
        }
        return time;
    }

    private void sendMsgToAllUser(byte status, long time) {
        GetEventVPInfoMsg msg = new GetEventVPInfoMsg();
        msg.status = status;
        msg.time = time / 1000L;
        ServerUtil.sendMsgToAllUsers(msg);
    }

    private final class GameLoopTask
            implements Runnable {
        private GameLoopTask() {
        }

        @Override
        public void run() {
            LobbyModule.this.gameLoop();
        }
    }

    private final class EventStartTask
            implements Runnable {
        private EventStartTask() {
        }

        @Override
        public void run() {
            Debug.trace(("vippoint event start: " + new Date()));
            HazelcastInstance instance = HazelcastClientFactory.getInstance();
            IMap map = instance.getMap("cacheConfig");
            map.put("VIPPOINT_EVENT_STATUS", "1");
            map.put("VIPPOINT_EVENT_X2_STATUS", "1");
        }
    }

    private final class EventEndTask
            implements Runnable {
        private EventEndTask() {
        }

        @Override
        public void run() {
            Debug.trace(("vippoint event end: " + new Date()));
            HazelcastInstance instance = HazelcastClientFactory.getInstance();
            IMap<String, String> map = instance.getMap("cacheConfig");
            map.put("VIPPOINT_EVENT_STATUS", "0");
            map.put("VIPPOINT_EVENT_X2_STATUS", "0");
            String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
            if (sLucky != null && sLucky.equals("1")) {
                LobbyModule.this.sendMsgToAllUser((byte) 0, 0L);
            }
            map.put("VIPPOINT_EVENT_LUCKY", "0");
        }
    }

    private final class EventX2EndTask
            implements Runnable {
        private EventX2EndTask() {
        }

        @Override
        public void run() {
            Debug.trace(("event x2 end: " + new Date()));
            HazelcastInstance instance = HazelcastClientFactory.getInstance();
            IMap map = instance.getMap("cacheConfig");
            map.put("VIPPOINT_EVENT_X2_STATUS", "0");
        }
    }

    private final class EventLuckyStartTask
            implements Runnable {
        private EventLuckyStartTask() {
        }

        @Override
        public void run() {
            Date now = new Date();
            if (now.getTime() < LobbyModule.this.eventTimeEnd.getTime()) {
                Debug.trace(("event lucky start: " + now));
                try {
                    VippointUtils.init();
                    HazelcastInstance instance = HazelcastClientFactory.getInstance();
                    IMap map = instance.getMap("cacheConfig");
                    String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
                    if (sLucky != null && sLucky.equals("0")) {
                        Debug.trace(("send message to all user: " + now));
                        LobbyModule.this.sendMsgToAllUser((byte) 1, LobbyModule.this.timeLucky);
                    }
                    map.put("VIPPOINT_EVENT_LUCKY", "1");
                    List unluckyTime = VippointUtils.randomUnluckyTime((Date) LobbyModule.this.eventTimeStart, (Date) LobbyModule.this.eventTimeEnd);
                    Debug.trace("unluckyTime: ");
                    for (Object dt : unluckyTime) {
                        if (now.getTime() < ((Date) dt).getTime()) {
                            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) LobbyModule.this.eventUnluckyTask, LobbyModule.this.calculateRemainTime((Date) dt) / 60, TimeUnit.MINUTES);
                            Debug.trace(("OK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                            continue;
                        }
                        Debug.trace(("NOK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                    }
                    List<Date> luckyTime = VippointUtils.randomLuckyTime((Date) LobbyModule.this.eventTimeStart, (Date) LobbyModule.this.eventTimeEnd);
                    Debug.trace("luckyTime: ");
                    for (Date dt : luckyTime) {
                        if (now.getTime() < dt.getTime()) {
                            BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) LobbyModule.this.eventLuckyTask, LobbyModule.this.calculateRemainTime(dt) / 60, TimeUnit.MINUTES);
                            Debug.trace(("OK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                            continue;
                        }
                        Debug.trace(("NOK: " + VinPlayUtils.getDateTimeStr((Date) dt)));
                    }
                    BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) LobbyModule.this.eventLuckyStartTask, 24, TimeUnit.HOURS);
                } catch (Exception e) {
                    Debug.trace(("event lucky start error: " + e));
                }
            }
        }
    }

    private final class EventLuckyEndTask
            implements Runnable {
        private EventLuckyEndTask() {
        }

        @Override
        public void run() {
            Date now = new Date();
            if (now.getTime() < LobbyModule.this.eventTimeEnd.getTime()) {
                Debug.trace(("event lucky end: " + now));
                HazelcastInstance instance = HazelcastClientFactory.getInstance();
                IMap map = instance.getMap("cacheConfig");
                String sLucky = (String) map.get("VIPPOINT_EVENT_LUCKY");
                if (sLucky != null && sLucky.equals("1")) {
                    Debug.trace(("send message to all user: " + now));
                    LobbyModule.this.sendMsgToAllUser((byte) 0, 0L);
                }
                map.put("VIPPOINT_EVENT_LUCKY", "0");
                BitZeroServer.getInstance().getTaskScheduler().schedule((Runnable) LobbyModule.this.eventLuckyEndTask, 24, TimeUnit.HOURS);
            }
        }
    }

    private final class EventUnluckyTask
            implements Runnable {
        private EventUnluckyTask() {
        }

        @Override
        public void run() {
            Debug.trace(("sub vippoint: " + new Date()));
            EventVPUnluckyMsg msg = new EventVPUnluckyMsg();
            ServerUtil.sendMsgToAllUsers(msg);
            List<String> users = LobbyModule.this.vpService.subVippointEvent();
            for (String nickname : users) {
                HasNewMailMsg mailMsg = new HasNewMailMsg();
                MiniGameUtils.sendMessageToUser(mailMsg, nickname);
            }
        }
    }

    private final class EventluckyTask
            implements Runnable {
        private EventluckyTask() {
        }

        @Override
        public void run() {
            Debug.trace(("add vippoint: " + new Date()));
            List<String> users = LobbyModule.this.vpService.addVippointEvent();
            for (String nickname : users) {
                HasNewMailMsg mailMsg = new HasNewMailMsg();
                MiniGameUtils.sendMessageToUser(mailMsg, nickname);
            }
        }
    }

    public String revertMobile(String mobile) {
        if (mobile.substring(0, 2).equals("84")) {
            return "0" + mobile.substring(2);
        }
        return mobile;
    }


    private final class SlotDailyTask
            implements Runnable {
        private SlotDailyTask() {
        }

        @Override
        public void run() {
            try {
                LuckyUtils.initSlotMap();
                // DvtUtils.initDVT((boolean)false);
            } catch (Exception e) {
                e.printStackTrace();
                Debug.trace(("init slot free errot: " + e));
            }
            BitZeroServer.getInstance().getTaskScheduler().schedule(LobbyModule.this.slotDailyTask, 24, TimeUnit.HOURS);
        }
    }

}

