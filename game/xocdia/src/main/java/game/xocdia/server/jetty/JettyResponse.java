package game.xocdia.server.jetty;

public class JettyResponse {
    public byte status;
    public String message;

    public JettyResponse(byte status, String message){
        this.status = status;
        this.message = message;
    }
}
