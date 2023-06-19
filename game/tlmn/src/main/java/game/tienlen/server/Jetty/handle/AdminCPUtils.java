package game.tienlen.server.Jetty.handle;

import game.tienlen.server.BotFundController;
import game.tienlen.server.Jetty.model.FundData;

public class AdminCPUtils {
    public static FundData getFundTLMN() {
        FundData fundData = new FundData();
        fundData.listFund = BotFundController.getInstance().listFund;
        return fundData;
    }

    public static void setFundTLMN(FundData fundData) {
        BotFundController.getInstance().listFund = fundData.listFund;
    }
}
