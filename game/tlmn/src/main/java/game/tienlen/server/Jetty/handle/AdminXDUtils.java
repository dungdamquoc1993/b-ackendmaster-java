package game.tienlen.server.Jetty.handle;

import bitzero.util.common.business.CommonHandle;
import game.modules.bot.BotFundController;
import game.tienlen.server.Jetty.model.FundData;
import game.xocdia.conf.XocDiaConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminXDUtils {
    private static String keyFund = "fundXocDia";
    private static int[] listBetLevel = {1000, 2000,5000,10000,20000,50000,100000,200000,500000};

    public static FundData getFundXD() {
        CommonHandle.writeInfoLog("Xocdia Test getFundXD ");
        FundData fundData = new FundData();
        fundData.listFund = BotFundController.getInstance().listFund;
        return fundData;
    }

    public static void setFundXD(FundData fundData) throws IOException {
        CommonHandle.writeInfoLog("Xocdia Test setFundXD " + fundData.listFund);
//        BotFundController.getInstance().listFund = fundData.listFund;
//        CommonHandle.writeInfoLog("Xocdia Test setFundXD 2 ");

        for (int i = 0; i < fundData.listFund.length; i++){
            CommonHandle.writeInfoLog("Xocdia Test setFundXD 3: " + i + " ,fund= " + fundData.listFund[i]);

            int betLevel = listBetLevel[0];
//            if(XocDiaConfig.listBetLevel != null){
//                betLevel = getKeyByValue(XocDiaConfig.instance().listBetLevel, Integer.valueOf(i));
//            } else {
//                CommonHandle.writeInfoLog("Xocdia Test XocDiaConfig.listBetLevel is null ");
//            }

//            CommonHandle.writeInfoLog("Xocdia Test setFundXD 4 ");

            if(betLevel > 0){
                String keyFundBetLevel = keyFund + "_" + betLevel;

                try {
                    BotFundController.getInstance().saveFund(keyFundBetLevel, fundData.listFund[i], betLevel);
                    CommonHandle.writeInfoLog("Xocdia saveFund " + keyFundBetLevel + " ,fund=" + fundData.listFund[i]);
                }catch (Exception e){
                    CommonHandle.writeInfoLog(e.getClass() + ": " +  e.getMessage() + ": " + e.getCause());
                    CommonHandle.writeErrLogDebug(e);
                }
            }
        }
    }

    public static String getKeyByValue(Map<String, Integer> map, Integer value) {
        try{
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                CommonHandle.writeInfoLog("Test get key by value: " + value + " ,getValue: " + entry.getValue() + " ,getKey: " + entry.getKey());

                if (entry.getValue() == value) {
                    return entry.getKey();
                }
            }

        } catch (Exception e){
            CommonHandle.writeInfoLog(e.getClass() + ": " +  e.getMessage() + ": " + e.getCause());
            CommonHandle.writeErrLogDebug(e);
        }

        return null;
    }
}
