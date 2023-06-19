package game.Jetty.handle;

import bitzero.server.BitZeroServer;
import game.Jetty.JettyErrorCode;
import game.Jetty.JettyResponse;
import game.Jetty.JettyUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CountUserOnlineHandler extends AbstractHandler {
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        int ccu = BitZeroServer.getInstance().getUserManager().getUserCount();
        JettyUtils.send(request, httpServletResponse, new JettyResponse(JettyErrorCode.SUCCESS, ccu + ""));
        return;
    }
}
