//package com.vinplay.api.backend.processors.logSlotGame;
//
//import com.vinplay.vbee.common.enums.Games;
//
//public class LogSlotUtils {
//
//    public static String getGameNameWithID(int mt) {
//        switch (mt) {
//            case 0:
//                return Games.TAMHUNG.getName(); //Chim dien
//            case 1:
//                return Games.AUDITION.getName(); //Tay du
//            case 2:
//                return Games.MAYBACH.getName(); // The thao
//            case 3:
//                return Games.SPARTAN.getName(); // Than tai
//            case 4:
//                return Games.BENLEY.getName(); // Bitcoin
//            case 5:
//                return "candy"; // slot pokemon
//            case 6:
//                return Games.CHIEM_TINH.getName();
//            case 7:
//                return Games.MINI_POKER.getName();
//            case 8:
//                return Games.ROLL_ROYE.getName();
//        }
//        return "";
//    }
//
//    public static String getTableDataBase(int mt) {
//        return "log_" + getGameNameWithID(mt);
//    }
//}
