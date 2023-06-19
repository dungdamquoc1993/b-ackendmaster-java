package game.Jetty.model;

import java.util.List;

public class UserOnlineResponse {
    private List<UserOnline> userOnlineList;
    private int totalRecord;

    public UserOnlineResponse(List<UserOnline> userOnlineList, int totalRecord) {
        this.userOnlineList = userOnlineList;
        this.totalRecord = totalRecord;
    }

    public UserOnlineResponse() {
    }

    public List<UserOnline> getUserOnlineList() {
        return userOnlineList;
    }

    public void setUserOnlineList(List<UserOnline> userOnlineList) {
        this.userOnlineList = userOnlineList;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
}
