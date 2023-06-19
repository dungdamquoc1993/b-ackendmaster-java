package com.vinplay.api.backend.processors;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.SendMailResponse;

public class SendMailProcessor implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest)param.get();
        SendMailResponse response = new SendMailResponse(true, "0", "");
        String nickName = request.getParameter("nn");
        String title = request.getParameter("tm");
        String content = request.getParameter("cm");
        if (!title.isEmpty() && !content.isEmpty()) {
            MailBoxServiceImpl service = new MailBoxServiceImpl();
            UserServiceImpl user = new UserServiceImpl();
            boolean check = false;
            if (nickName.trim().equals("*")) {
                //Set<String> nicknames = (Set) HazelcastClientFactory.getInstance().getMap("users").keySet();
				List<String> nicknames = HazelcastClientFactory.getInstance().getMap("users").entrySet().stream()
						.filter(x -> {
							if (x.getValue() instanceof UserCacheModel) {
								UserCacheModel da = (UserCacheModel) x.getValue();
								if (da.isBot() || da.isBanLogin()) {
									return false;
								}
								return true;
							} else {
								return false;
							}
						}).map(map -> map.getKey().toString()).collect(Collectors.toList());
                check = service.sendMailBoxFromByNickName(nicknames, title, content);
                if (check) {
                    response.setErrorCode("0");
                    response.setSuccess(true);
                } else {
                    response.setErrorCode("10001");
                }
            } else {
                String[] parts;
                for (String name : parts = nickName.split(",")) {
                    try {
                        if (user.checkNickname(name)) continue;
                        response.setErrorCode("10002");
                        response.setNickName(name);
                        response.setSuccess(false);
                        return response.toJson();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                for (String name : parts) {
                    check = service.sendMailBoxFromByNickNameAdmin(name, title, content);
                }
                if (check) {
                    response.setErrorCode("0");
                    response.setSuccess(true);
                } else {
                    response.setErrorCode("10001");
                }
            }
            return response.toJson();
        }
        return "MISSING PARAMETTER";
    }
}

