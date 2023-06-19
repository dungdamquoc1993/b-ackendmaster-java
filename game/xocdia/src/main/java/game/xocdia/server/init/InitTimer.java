package game.xocdia.server.init;

import bitzero.util.common.business.Debug;
import game.xocdia.server.jetty.JettyUtils;

public class InitTimer  implements Runnable {
    @Override
    public void run() {
        this.init();
    }

    private void init() {
        try {
            JettyUtils.jettyInit();
        } catch (Exception e) {
            Debug.trace(e);
        }
    }
}
