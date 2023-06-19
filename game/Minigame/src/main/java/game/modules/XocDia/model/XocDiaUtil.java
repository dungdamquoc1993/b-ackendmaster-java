package game.modules.XocDia.model;

import bitzero.server.entities.Room;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import com.vinplay.game.XocDia.XocDiaHistoryItem;
import com.vinplay.game.XocDia.XocDiaSoiCauUtil;
import com.vinplay.game.XocDia.history.GamePlayXocDiaModel;
import com.vinplay.game.XocDia.history.XocDiaGamePlayHistoryDetail;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.statics.TransType;
import game.modules.XocDia.XocDiaConstant;
import game.modules.XocDia.XocDiaRoomManager;
import game.modules.XocDia.bot.BotXocDia;
import game.modules.XocDia.model.bet.BetItem;
import game.modules.XocDia.model.bet.XocDiaBetDoorItem;
import game.modules.description.XocDiaDescription.XocDiaDescriptionUtils;
import game.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XocDiaUtil {

    public static XocDiaUtil _instance = null;

    public static XocDiaUtil getInstance() {
        if (_instance == null) {
            synchronized (XocDiaUtil.class) {
                if (_instance == null) {
                    _instance = new XocDiaUtil();
                }
            }
        }
        return _instance;
    }

    private XocDiaUtil() {
    }

    private XocDiaBetModel xocDiaBetModel = XocDiaBetModel.getInstance();
    private XocDiaFundModel xocDiaFundModel = XocDiaFundModel.getInstance();
    public BotXocDia botXocDia = new BotXocDia();

    private GamePlayXocDiaModel mHistoryGamePlay = null;

    private List<XocDiaGamePlayHistoryDetail> listAllPlayer = new ArrayList<>();

    public static byte[] diceResult = new byte[4];

    UserService userService = new UserServiceImpl();

    public void onTimer() {
       try {
           long curTime = GameUtil.getTimeStampInSeconds();
           long secondTime = curTime - this.xocDiaBetModel.startTime;
           switch (this.xocDiaBetModel.status) {
               case XocDiaBetModel.BET_PHASE: {
                   if (secondTime > XocDiaFullConfig.BET_PHASE) {
                       this.xocDiaBetModel.updateStatus(XocDiaBetModel.DICE_PHASE);
                   }
                   if (this.xocDiaBetModel.isStart == 1) {
                       botXocDia.setupBetFun();
                   }
                   if (secondTime >= this.botXocDia.timeStartBetFun) {
                       botXocDia.betFun();
                   }
                   this.xocDiaBetModel.isStart = 0;
                   break;
               }
               case XocDiaBetModel.DICE_PHASE:{
                   if (!this.xocDiaBetModel.flagRunDice) {
                       this.xocDiaBetModel.flagRunDice = true;
                       this.showResult();
                       this.xocDiaBetModel.updateStatus(XocDiaBetModel.PAY_PHASE);
                       mHistoryGamePlay = new GamePlayXocDiaModel(this.xocDiaBetModel.referenceIdXocDia, this.xocDiaBetModel.totalBet);
                       mHistoryGamePlay.setDiceResult(diceResult);
                   }
                   break;
               }
               case XocDiaBetModel.PAY_PHASE:{
                   if (!this.xocDiaBetModel.flagRunPayMoney && curTime - this.xocDiaBetModel.startTime > XocDiaFullConfig.DICE_PHASE - 5) {
                       this.xocDiaBetModel.flagRunPayMoney = true;
                       //Debug.debug("Tai Pay Bet phase");
                       payMoney();
                       mHistoryGamePlay.setDiceResult(diceResult);
                       //todo ghi vao database GamePlayXocDiaModel
                       XocDiaSoiCauUtil.addListSoiCau(this.xocDiaBetModel.referenceIdXocDia,diceResult);

                   }
                   if (this.xocDiaBetModel.flagRunPayMoney && curTime - this.xocDiaBetModel.startTime > XocDiaFullConfig.DICE_PHASE) {
                       //reset and come to new session
                       this.xocDiaBetModel.reset();
                       this.listAllPlayer.clear();
                   }
                   break;
               }
           }
       }catch (Exception e){
           Debug.trace(e);
       }
    }

    private void showResult() {
        long[] userBet = new long[this.xocDiaBetModel.totalBet.length];
        for(int i =0;i<userBet.length;i++){
            userBet[i] = this.xocDiaBetModel.totalBet[i] - this.xocDiaBetModel.listBotBet[i];
        }
        diceResult = new DiceResult().getResult(userBet);

        //todo send cho tat ca user ket qua
//        Room room = XocDiaRoomManager.getRoomToJoin();
//        if (room != null) {
//            ResultSessionXocDiaFullMsg msg = new ResultSessionXocDiaFullMsg(diceResult);
//            ExtensionUtility.instance().send(msg,room.getSessionList());
//        }

    }

    private void payMoney() {
        byte listTrang = 0, listDen = 0;
        for (int i = 0; i < diceResult.length; i++) {
            if (diceResult[i] == 0) {
                listTrang++;
            } else {
                listDen++;
            }
        }
        if (listTrang % 2 == 0) {// chan
            XocDiaBetDoorItem xocDiaBetDoorItem = this.xocDiaBetModel.mDoor[XocDiaConstant.BET_CHAN];
            payMoneyHandler(xocDiaBetDoorItem.listBet, xocDiaBetDoorItem.historyBet, (byte) 1, XocDiaConstant.BET_CHAN);
        } else { // le
            XocDiaBetDoorItem xocDiaBetDoorItem = this.xocDiaBetModel.mDoor[XocDiaConstant.BET_LE];
            payMoneyHandler(xocDiaBetDoorItem.listBet, xocDiaBetDoorItem.historyBet, (byte) 1, XocDiaConstant.BET_LE);
        }


        if (listTrang == 0) {// 4 den
            XocDiaBetDoorItem xocDiaBetDoorItem = this.xocDiaBetModel.mDoor[XocDiaConstant.BET_0_4];
            payMoneyHandler(xocDiaBetDoorItem.listBet, xocDiaBetDoorItem.historyBet, (byte) 15, XocDiaConstant.BET_0_4);
        }

        if (listTrang == 4) {// 4 trang
            XocDiaBetDoorItem xocDiaBetDoorItem = this.xocDiaBetModel.mDoor[XocDiaConstant.BET_4_0];
            payMoneyHandler(xocDiaBetDoorItem.listBet, xocDiaBetDoorItem.historyBet, (byte) 15, XocDiaConstant.BET_4_0);
        }

        if (listTrang == 1) {         // 3 den 1 trang
            XocDiaBetDoorItem xocDiaBetDoorItem = this.xocDiaBetModel.mDoor[XocDiaConstant.BET_1_3];
            payMoneyHandler(xocDiaBetDoorItem.listBet, xocDiaBetDoorItem.historyBet, (byte) 3, XocDiaConstant.BET_1_3);
        }

        if (listTrang == 3) {         // 3 trang 1 den
            XocDiaBetDoorItem xocDiaBetDoorItem = this.xocDiaBetModel.mDoor[XocDiaConstant.BET_3_1];
            payMoneyHandler(xocDiaBetDoorItem.listBet, xocDiaBetDoorItem.historyBet, (byte) 3, XocDiaConstant.BET_3_1);
        }

        //todo add vao lich su soi cau
        //   xocDiaHistoryModel.add(new XocDiaHistoryItem(this.xocDiaBetModel.gamePlayId, diceResult));
        notifyChangeMoney();
    }

    private void payMoneyHandler(Map<Long, BetItem> listBet, List<XocDiaGamePlayHistoryDetail> listHistoryDetail, byte multiplyWin, byte door) {
        listBet.forEach((k, v) -> {  // lamda
            long winMoney = v.money * multiplyWin * (100 - 2) / 100 + v.money;
            long fee = v.money * multiplyWin * 2 / 100 + v.money;

            XocDiaGamePlayHistoryDetail historyItem = new XocDiaGamePlayHistoryDetail(v.uId, this.xocDiaBetModel.referenceIdXocDia,
                    v.userName, door, v.money, v.time);
            historyItem.setPay(winMoney);
            listAllPlayer.add(historyItem);

            if (v.isBot)
                return;

            listHistoryDetail.add(historyItem);

            userService.updateMoney( v.userName, winMoney, "vin", Games.XOC_DIA.getName(),
                    Games.XOC_DIA.getId()+"", XocDiaDescriptionUtils.getXocDiaWinDescription(
                            Games.XOC_DIA.getId()+"",this.xocDiaBetModel.referenceIdXocDia)
                  , fee, this.xocDiaBetModel.referenceIdXocDia, TransType.END_TRANS);

            this.xocDiaFundModel.addMoneyToFund(-winMoney);
        });
    }

    private void notifyChangeMoney() {
        Stream<XocDiaGamePlayHistoryDetail> mHistoryStream = this.listAllPlayer.stream();

        //rdd win real user
        List<XocDiaGamePlayHistoryDetail> transform = mHistoryStream
                .collect(Collectors.groupingBy(foo -> foo.getUsername()))
                .entrySet().stream()
                .map(e -> e.getValue().stream()
                        .reduce((obj1, obj2) -> {
                            obj1.setPay(obj1.getPay() + obj2.getPay());
                            return obj1;
                        }))
                .map(f -> f.get())
                .collect(Collectors.toList());

        transform.forEach((v) -> {
                long moneyUser = userService.getCurrentMoneyUserCache(v.getUsername(),"vin");
                long moneyWin = v.getPay();
                //todo send cho user thang
//                        this.sendMessageToUser();
                }
        );

        //todo gui goi tin ket thuc van nhe
//        Room room = XocDiaRoomManager.getRoomToJoin();
       // ExtensionUtility.instance().send(new EndEndGameXocDiaFullMsg(transform), room.getSessionList());
    }

    public static void sendMessageToUserXocDia(BaseMsg msg, String username) {
        List<User> users = ExtensionUtility.getExtension().getApi().getUserByName(username);
        if (users != null) {
            ExtensionUtility.getExtension().sendUsers(msg, users);
        }
    }

    public static void sendMessageToAllUserXocDia(BaseMsg msg){
        Room room = XocDiaRoomManager.getRoomToJoin();
        ExtensionUtility.instance().send(msg, room.getSessionList());
    }
}
