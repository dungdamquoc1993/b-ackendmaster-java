package game.modules.XocDia.model;

import bitzero.util.common.business.Debug;
import com.vinplay.dal.service.MiniGameService;
import com.vinplay.dal.service.impl.MiniGameServiceImpl;

public class XocDiaFundModel {

    private long fund = 0;
    public MiniGameService mgService = new MiniGameServiceImpl();

    public static final String fundName = "FundXocDiaFull";
    public static XocDiaFundModel _instance = null;

    public static XocDiaFundModel getInstance(){
        if(_instance == null){
            synchronized (XocDiaFundModel.class){
                if(_instance == null){
                    _instance = new XocDiaFundModel();
                }
            }
        }
        return _instance;
    }

    private XocDiaFundModel(){
        try {
            this.fund =  mgService.getFund(fundName);
        }catch (Exception e){
            Debug.trace(e);
        }
    }

    public synchronized void addMoneyToFund(long money){
        this.fund += money;
        this.saveFund();
    }

    public void saveFund(){
        try {
            this.mgService.saveFund(fundName, this.fund);
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    public long getFund(){
        return this.fund;
    }
}
