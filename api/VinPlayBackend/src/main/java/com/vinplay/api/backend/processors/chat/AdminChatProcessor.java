package com.vinplay.api.backend.processors.chat;

import com.vinplay.dal.service.impl.ChatLobbyServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import javax.servlet.http.HttpServletRequest;

public class AdminChatProcessor implements BaseProcessor<HttpServletRequest, String> {
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String nickname = request.getParameter("nn");
		long time = Long.parseLong(request.getParameter("t"));
		ChatLobbyServiceImpl chatService = new ChatLobbyServiceImpl();
		chatService.banChatUser(nickname, time);
		return "1";
	}
}
