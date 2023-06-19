package game.Jetty.handle;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import com.google.gson.Gson;
import com.vinplay.vbee.common.statics.Consts;

import game.Jetty.JettyErrorCode;
import game.Jetty.JettyResponse;
import game.Jetty.JettyUtils;
import game.Jetty.model.UserOnline;
import game.Jetty.model.UserOnlineResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListUserOnlineHandler extends AbstractHandler {
	
	  private String getIpAddress(HttpServletRequest request) {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			String clientIp = null;
			if (ipAddress != null && !"".equals(ipAddress)) {
				String[] arrayIp = ipAddress.split(",");
				if (arrayIp.length > 0) {
					clientIp = arrayIp[0].trim();
				}
			}
			return clientIp;
		}

	  
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
		// get ipAddress
		String ip = getIpAddress(request);
		if (!Consts.IP_SERVER.contains(ip)) {
			return;
		}
    	int page = 1, maxItem = 10;
        int count = BitZeroServer.getInstance().getUserManager().getUserCount();
        String nickname = request.getParameter("nickname");
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e){
        }
        int start = (page-1)*maxItem;
        int end = page*maxItem;

        if (end>count) end = count;
        List<User> user1 = BitZeroServer.getInstance().getUserManager().getAllUsers().subList(start,end);
        Gson gson = new Gson();
        List<UserOnline> users = new ArrayList<>();
        for (User u:user1){
            boolean isMobile = false;
            boolean isWeb = false;
            if(u.getSession()!=null){
                isMobile= u.getSession().isMobile();
                isWeb = u.getSession().isWebsocket();
            }
            UserOnline userOnline = new UserOnline(u.getId(), u.getName(), u.getIpAddress(), ((u.getJoinedRoom()==null)? null: u.getJoinedRoom().getName()), isMobile,
                    isWeb,u.getPrivilegeId(), u.getLastLoginTime(), u.getPlayerId(), u.getBadWordsWarnings(),
                    u.getFloodWarnings(), u.isBeingKicked(), u.isConnected(), u.isJoining());
            if (nickname==null || nickname.isEmpty()){
                users.add(userOnline);
            } else {
                if (userOnline.getName()==nickname){
                    users.add(userOnline);
                }
            }
        }
        UserOnlineResponse response = new UserOnlineResponse(users, count);
        JettyUtils.send(request, httpServletResponse, new JettyResponse(JettyErrorCode.SUCCESS, gson.toJson(response)));
        return;
    }
}
