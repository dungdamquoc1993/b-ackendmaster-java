package com.vinplay.api.processors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hazelcast.core.IMap;
import com.vinplay.api.entities.GetUserInfoResponse;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;

public class GetUserInfoProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("api");

    public String execute(Param<HttpServletRequest> param) {   
        try
        {
            HttpServletRequest request = (HttpServletRequest)param.get();                    
            String userName = request.getParameter("un");      
            String token = request.getParameter("t");      
            GetUserInfoResponse res = new GetUserInfoResponse(500, "error");
            UserServiceImpl userService = new UserServiceImpl();
            if (userName != null && !"".equals(userName))
            {
                try {
                    UserModel userModel = userService.getUserByUserName(userName);
                    if (userModel != null)
                    { 
                        IMap userMap = HazelcastClientFactory.getInstance().getMap("users");
                        if (!userMap.containsKey(userModel.getNickname()))
                        {
                            return res.toJson();
                        }
                        UserCacheModel userCache = (UserCacheModel) userMap.get( userModel.getNickname());    
                        if (userCache != null)
                        {
                            if (userCache.getAccessToken().equals(token)) {                                
                                // thong tin chung
                                res.setNickname(userModel.getNickname());
                                res.setCurrent_balance(userModel.getVin());
                                res.setEmail(userModel.getEmail());
                                res.setIdentification(userModel.getIdentification());
                                res.setMobile(userModel.getMobile());
                                res.setVip_point(userModel.getVippoint());
                                res.setCode(200);
                                res.setMessage("success");
                            }
                            else
                            {
                                res.setCode(400);
                                res.setMessage("bad request");
                            }
                        }
                        else
                        {
                            res.setCode(400);
                            res.setMessage("bad request");
                        }
                    }
                    else
                    {
                        res.setCode(404);
                        res.setMessage("nickname not found");
                    }
                } catch (SQLException ex) {
                    res.setCode(500);
                    res.setMessage("error");
                }
            }
            else
            {
                res.setCode(400);
                res.setMessage("bad request");
            }
            return res.toJson();
        }
        catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            return ex.getMessage() + "\n" + sStackTrace;
        }
    }
}

