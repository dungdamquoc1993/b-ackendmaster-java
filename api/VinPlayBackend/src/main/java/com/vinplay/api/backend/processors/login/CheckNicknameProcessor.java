package com.vinplay.api.backend.processors.login;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserModel;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CheckNicknameProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest)param.get();
        String nickname = request.getParameter("nn");
        String code = "-2";
        if (nickname != null) {
            try {
                UserServiceImpl service = new UserServiceImpl();
                UserModel model = service.getUserByNickName(nickname);
                if(model == null)
                	return "-1";
                
                if(model.getDaily() == 0)
                	return "0";
                
                AgentDAO dao = new AgentDAOImpl();
                UserAgentModel agent = dao.DetailUserAgentByNickName(nickname);
                return String.valueOf(agent.getLevel());
            }
            catch (Exception e) {
                logger.debug((Object)e);
                return code = "-2";
            }
        }
        
        return code;
    }
}