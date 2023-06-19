package com.vinplay.run;

import java.util.Objects;
import java.util.TimerTask;
import org.apache.log4j.Logger;

import com.vinplay.dao.ag.AgDao;
import com.vinplay.dao.impl.ag.AgDaoImpl;
import com.vinplay.dto.ag.BaseAGRequest;
import com.vinplay.dto.ag.BaseAGResponseDto;
import com.vinplay.interfaces.ag.MemberAGService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.impl.GameDaoServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;


/**
 * Auto create user AG
 */
public class TaskAutoCreateUserAG extends TimerTask {
    private static final Logger LOGGER = Logger.getLogger(TaskAutoCreateUserAG.class);

    private static final int NUM_ACCOUNT_REG_DEV = 20;
    private static final int NUM_ACCOUNT_REG_PRO = 1000;
    private static final int TIME_OUT = 5 * 60 * 1000; // 5 minutes time out

    private static final String METHOD_CREATE_USER_AG = "lg";

    private int tempMaxUserId = 0;
    private int numAccountReg = 1000;
    
    private GameDaoService gameDaoService = new GameDaoServiceImpl();
    private AgDao agDao = new AgDaoImpl();
	private static final String COLLECTION_AGUSER = "aguser";
    

    /*public static void main(String[] args) throws Exception {
        String s = CommonMethod.getTimeSpace(CommonMethod.GetCurDate("yyyy-MM-dd"), "yyyy-MM-dd",
                -1, "dd");
        LOGGER.info("[TaskAutoCreateUserAG]  Time space: " + s);
    }*/

    public TaskAutoCreateUserAG() {
        if ("dev".equals(GameThirdPartyInit.enviroment)) {
            numAccountReg = NUM_ACCOUNT_REG_DEV;
        } else if ("pro".equals(GameThirdPartyInit.enviroment)) {
            numAccountReg = NUM_ACCOUNT_REG_PRO;
        }
    }
    
    /**
     * Create user each 4AM Time out 5m
     */
    @Override
    public void run() {
        boolean isNotMaintain = !InitData.isAGDown();
        String hour = CommonMethod.GetCurDate("HH");
        if (GameThirdPartyInit.CREATE_USER_TIME.equals(hour) && isNotMaintain) {
        	int maxAgId = gameDaoService.getMaxFieldValue("agcountid",COLLECTION_AGUSER); 
        	int noAgUserMapping = gameDaoService.countUserRemain(null,COLLECTION_AGUSER);
        	
            LOGGER.info("[TaskAutoCreateUserAG]  max agid: " + maxAgId);
            LOGGER.info("[TaskAutoCreateUserAG]  number of remain agid: " + noAgUserMapping);

            if (noAgUserMapping < numAccountReg) {
                int count = 1;
                int increaseId = 1;
                int agCountId = 0;
                String agId = "0";
                if (maxAgId < tempMaxUserId) {
                    maxAgId = tempMaxUserId;
                    LOGGER.info("[TaskAutoCreateUserAG] max agid: " + maxAgId);
                    LOGGER.info("[TaskAutoCreateUserAG] temp_maxUserId: " + tempMaxUserId);
                }
                // catch exception exit loop in case AG platfom maintance!
                long startTime = System.currentTimeMillis(); // fetch starting time
                while (count <= (numAccountReg - noAgUserMapping) && (System.currentTimeMillis() - startTime) < TIME_OUT) {
                    String agPassword = (maxAgId + increaseId) +""+ CommonMethod.randomString(12);
                    agId = GameThirdPartyInit.AG_PREFIX + Integer.toString(maxAgId + increaseId);
                    BaseAGResponseDto respone = createUser(agId, agPassword);

                    if (Objects.isNull(respone) || !"0".equals(respone.getInfo())) {
                        String responseMessage = respone != null ? respone.getMsg() : "";
                        LOGGER.error("[TaskAutoCreateUserAG] create failed: "
                                + (maxAgId + increaseId) + " : " + responseMessage);
                    } else {
                        // Insert account information in to VN Lottery DB
                        try {
                        	agCountId =maxAgId + increaseId;
                        	boolean isInsert = agDao.createUserNoMapping(agId, agPassword, agCountId);
                            if (isInsert) {
                                count++;
                                LOGGER.info("[TaskAutoCreateUserAG] Insert AG id : " + agId
                                        + " to AGUSER tbl ok!");
                            } else {
                                LOGGER.error("[TaskAutoCreateUserAG] Insert AG id : " + agId
                                        + " to AGUSER tbl error!");
                            }

                        } catch (Exception ex) {
                            LOGGER.error("[TaskAutoCreateUserAG] p_AG_GeneralAGUser", ex);
                            return;
                        }
                    }
                    increaseId++;
                }
                LOGGER.info("[TaskAutoCreateUserAG] exec in loop while : "
                        + (System.currentTimeMillis() - startTime) / 1000 + "minutes");
                tempMaxUserId = agCountId;
                LOGGER.info("[TaskAutoCreateUserAG] temp_maxUserId: " + tempMaxUserId);
                LOGGER.info("[TaskAutoCreateUserAG] number registed: " + (count - 1));
            }
        }

    }

    private BaseAGResponseDto createUser(String agId, String agPassword) {
        // call reg new member from AG service
        BaseAGRequest request = new BaseAGRequest();
        request.setCagent(GameThirdPartyInit.AG_CAGENT);
        request.setLoginname(agId);
        request.setPassword(agPassword);
        request.setMethod(METHOD_CREATE_USER_AG);
        request.setActype(GameThirdPartyInit.AG_AC_TYPE);
        request.setOddtype(GameThirdPartyInit.AG_ODD_TYPE);
        request.setCur(GameThirdPartyInit.AG_CURRENCY);

        MemberAGService service = new MemberAGService();
        return service.createUser(request);
    }

}
