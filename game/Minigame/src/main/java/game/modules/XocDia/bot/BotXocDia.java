package game.modules.XocDia.bot;

import bitzero.server.entities.Room;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import game.GameConfig.BotBetConfig;
import game.modules.XocDia.XocDiaRoomManager;
import game.modules.XocDia.model.XocDiaBetModel;
import game.modules.XocDia.model.XocDiaUtil;
import game.modules.minigame.entities.BotMinigame;
import game.utils.GameUtil;

import java.util.*;

public class BotXocDia {
    private List<BotUser> listBot = new ArrayList<>();
    public ArrayList<ArrayList<Long>> listBet = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> indexBet = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> distributionBet = new ArrayList<>();
    public int timeStartBetFun = 5;

    public static int BOT_IN_ROOM = 20;
    public static int MIX = 10;
    public Map<String, BotUser> listBotInRoomCoDinh = new HashMap<>();
    public Map<String, BotUser> listBotInRoomInOut = new HashMap<>();


    public BotXocDia() {
        try {
            List<String> bots = BotMinigame.getBots(1000, "vin");
            for (int i = 0; i < bots.size(); i++) {
                BotUser botUser = new BotUser(i, bots.get(i));
                listBot.add(botUser);
            }
            int mix = GameUtil.randomMax(MIX);
            this.listBotInRoomCoDinh.clear();
            while (this.listBotInRoomCoDinh.size() < BOT_IN_ROOM + mix) {
                int value = GameUtil.randomMax(this.listBot.size());
                this.listBotInRoomCoDinh.put(this.listBot.get(value).id + "", this.listBot.get(value));
                this.listBot.get(value).randomMoneyInGame(GameUtil.randomBetween(1000000, 5000000));
                this.listBot.get(value).numberPlayerCount = GameUtil.randomBetween(5, 9);
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    public void setupBetFun() {
        if (this.listBot.size() < 1) {
            return;
        }
        this.listBet = new ArrayList<>();
        this.indexBet = new ArrayList<>();
        this.distributionBet = new ArrayList<>();
        XocDiaBetModel.getInstance().listBotBet = new long[6];
        this.timeStartBetFun = GameUtil.randomBetween(5, 7);
        this.listBet = BotBetConfig.getListBetBotXocDia();

        Room room = XocDiaRoomManager.getRoomToJoin();
        List<BotUser> listBotLeaveRoom = new ArrayList<>();
        int numberBotCoDinhOut = 0;
        for (Map.Entry<String, BotUser> entry : this.listBotInRoomCoDinh.entrySet()){
            BotUser botUser = entry.getValue();
            botUser.currentPlayerCount++;
            if(botUser.currentPlayerCount>botUser.numberPlayerCount || botUser.money<100000){
                listBotLeaveRoom.add(botUser);
                numberBotCoDinhOut++;
                //todo send to usser
//                //bot leave room;
//                ExtensionUtility.instance().send(new NewPlayerJoinRoomXocDiaMsg(botUser.id, botUser.display_name,
//                        botUser.money, botUser.avatar, botUser.level), room.getSessionList());
                this.listBotInRoomCoDinh.remove(botUser.id + "");
            }
        }
        for(BotUser botUser1 : listBotLeaveRoom){
            numberBotCoDinhOut++;
            //bot leave room;
            //todo send to user
           // ExtensionUtility.instance().send(new LeaveRoomXocDiaMsg(botUser1.id), room.getSessionList());
            this.listBotInRoomCoDinh.remove(botUser1.id + "");
        }
        while (numberBotCoDinhOut > 0){
            int index = GameUtil.randomMax(this.listBot.size());
            BotUser botUser = this.listBot.get(index);
            if(!this.listBotInRoomCoDinh.containsKey(botUser.id+"")){
                numberBotCoDinhOut--;
                this.listBotInRoomCoDinh.put(botUser.id+"", botUser);
                botUser.randomMoneyInGame(GameUtil.randomBetween(1000000,5000000));
                botUser.numberPlayerCount = GameUtil.randomBetween(5,9);
                botUser.currentPlayerCount = 0;
                //todo send to usser
//                ExtensionUtility.instance().send(new NewPlayerJoinRoomXocDiaMsg(botUser.id, botUser.display_name,
//                        botUser.money,botUser.avatar,botUser.level), room.getSessionList());
            }
        }

        ArrayList<Integer> indexBet = new ArrayList<>();
        for (int i = 0; i < this.listBot.size(); i++) {
            indexBet.add(i);
        }
        Collections.shuffle(indexBet);
        this.sendAllBotInOutLeaveRoom();
        this.listBotInRoomInOut = new HashMap<>();
        for (int i = 0; i < this.listBet.size(); i++) {
            ArrayList<Integer> betIndex = new ArrayList<>();
            for (int j = 0; j < this.listBet.get(i).size(); j++) {
                int index = GameUtil.randomMax(indexBet.size());
                if (!this.listBotInRoomCoDinh.containsKey(this.listBot.get(indexBet.get(index)).id+"")) {
                    this.listBotInRoomInOut.put(this.listBot.get(indexBet.get(index)).id+"", this.listBot.get(indexBet.get(index)));
                    this.listBot.get(indexBet.get(index)).randomMoneyInGame(GameUtil.randomBetween(1000000,5000000));
                }
                betIndex.add(indexBet.get(index));
                indexBet.remove(index);
            }
            this.indexBet.add(betIndex);
        }
        this.sendAllBotInOutJoinRoom();

        for (int i = 0; i < this.listBet.size(); i++) {
            int[] valuebet = new int[GameUtil.randomBetween(29, 32)];
            ArrayList distributionBetAll = new ArrayList();
            for (int j = 0; j < this.listBet.get(i).size(); j++) {
                valuebet[GameUtil.randomMax(valuebet.length)]++;
            }
            for (int j = 0; j < valuebet.length; j++) {
                distributionBetAll.add(valuebet[j]);
            }
            this.distributionBet.add(distributionBetAll);
        }
    }

    public void betFun() {
        for (int i = 0; i < this.listBet.size(); i++) {
            if (this.listBet.get(i).size() > 0 && this.indexBet.get(i).size() > 0) {
                if (this.distributionBet.get(i).size() == 0) {
                    for (int j = 0; j < this.listBet.get(i).size(); j++) {
                        BotUser botUser = this.listBot.get(this.indexBet.get(i).get(j));
                        botUser.money -= this.listBet.get(i).get(j);
                        XocDiaBetModel.getInstance().bet(botUser.id, botUser.display_name, (byte) i, this.listBet.get(i).get(j), true);
                    }
                } else {
                    int valueBet = this.distributionBet.get(i).get(0);
                    this.distributionBet.get(i).remove(this.distributionBet.get(i).get(0));
                    for (int j = 0; j < valueBet; j++) {
                        BotUser botUser = this.listBot.get(this.indexBet.get(i).get(0));
                        this.indexBet.get(i).remove(this.indexBet.get(i).get(0));
                        long money = this.listBet.get(i).get(0);
                        this.listBet.get(i).remove(this.listBet.get(i).get(0));
                        botUser.money -= money;
                        XocDiaBetModel.getInstance().bet(botUser.id, botUser.display_name, (byte) i, money, true);
                    }
                }
            }
        }
    }

    public void sendAllBotInOutJoinRoom() {
        Room room = XocDiaRoomManager.getRoomToJoin();
        for (Map.Entry<String, BotUser> entry : this.listBotInRoomInOut.entrySet()) {
            BotUser botUser = entry.getValue();
            //todo send to usser
//            ExtensionUtility.instance().send(new NewPlayerJoinRoomXocDiaMsg(botUser.id, botUser.display_name,
//                    botUser.money, botUser.avatar, botUser.level), room.getSessionList());
        }
    }

    public void sendAllBotInOutLeaveRoom() {
        Room room = XocDiaRoomManager.getRoomToJoin();
        for (Map.Entry<String, BotUser> entry : this.listBotInRoomInOut.entrySet()) {
            BotUser botUser = entry.getValue();
            //todo send to usser
            //          ExtensionUtility.instance().send(new LeaveRoomXocDiaMsg(botUser.id), room.getSessionList());
        }
    }

}
