package game.xocdia.server.jetty.handle;

import bitzero.util.common.business.CommonHandle;
import game.modules.bot.BotFundController;
import game.xocdia.server.jetty.model.FundData;

public class AdminCPUtils {
    public static FundData getFundXD() {
        CommonHandle.writeInfoLog("Xocdia Test getFundXD ");
        FundData fundData = new FundData();
        fundData.listFund = BotFundController.getInstance().listFund;
        return fundData;
    }

    public static void setFundXD(FundData fundData) {
        CommonHandle.writeInfoLog("Xocdia Test setFundXD ");
        BotFundController.getInstance().listFund = fundData.listFund;
    }
}
