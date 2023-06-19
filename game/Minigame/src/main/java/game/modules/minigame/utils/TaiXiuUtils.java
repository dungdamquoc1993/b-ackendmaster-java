package game.modules.minigame.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dal.entities.taixiu.ResultTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiuDetail;

public class TaiXiuUtils {
    private static Logger loggerTaiXiu = Logger.getLogger("csvBetTaiXiu");
    private static final String FORMAT_LOG_BET_TAI_XIU = "%d,\t%s,\t%d,\t%d,\t%d,\t%s,\t%s";

    public static String buildLichSuPhien(List<ResultTaiXiu> input, int number) {
        int end = input.size();
        int start = end - number > 0 ? end - number : 0;
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; ++i) {
            ResultTaiXiu entry = input.get(i);
            builder.append(entry.dice1);
            builder.append(",");
            builder.append(entry.dice2);
            builder.append(",");
            builder.append(entry.dice3);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String logLichSuPhien(List<ResultTaiXiu> input, int number) {
        int end = input.size();
        int start = end - number > 0 ? end - number : 0;
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; ++i) {
            ResultTaiXiu entry = input.get(i);
            builder.append(entry.result);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static void logBetTaiXiu(TransactionTaiXiuDetail tran) {
        String moneyType = tran.moneyType == 1 ? "vin" : "xu";
        TaiXiuUtils.logBetTaiXiu(tran.referenceId, tran.username, tran.betValue, tran.betSide, tran.inputTime, moneyType);
    }

    public static void logBetTaiXiu(long referenceId, String nickname, long betValue, int betSide, int inputTime, String moneyType) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String str = String.format(FORMAT_LOG_BET_TAI_XIU, referenceId, nickname, betValue, betSide, inputTime, moneyType, df.format(new Date()));
        loggerTaiXiu.debug((Object)str);
    }
}

