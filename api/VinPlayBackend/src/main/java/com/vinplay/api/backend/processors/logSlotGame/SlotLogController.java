package com.vinplay.api.backend.processors.logSlotGame;

//import com.vinplay.api.backend.processors.logSlotGame.logSlotGame.LogSlotTamHung;
import com.vinplay.api.backend.processors.logSlotGame.logSlotModel.LogSlotDAO;

public class SlotLogController {

    private static final Object lock = new Object();

    private static SlotLogController instance;

    private SlotLogController(){
//        this.logSlotTamHung = new LogSlotTamHung();
        this.logSlotDAO = new LogSlotDAO();
    }

//    public LogSlotTamHung logSlotTamHung;
    public LogSlotDAO logSlotDAO;
    public static SlotLogController getInstance(){
        if(instance == null){
            synchronized (lock){
                if(instance == null){
                    instance = new SlotLogController();
                }
            }
        }
        return instance;
    }
}
