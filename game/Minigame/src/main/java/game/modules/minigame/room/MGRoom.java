package game.modules.minigame.room;

import java.util.ArrayList;
import java.util.List;

import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.ExtensionUtility;

public abstract class MGRoom {
    public static final String MGROOM_TAI_XIU_INFO = "MGROOM_TAI_XIU_INFO";
    public static final String MGROOM_MINI_POKER_INFO = "MGROOM_MINI_POKER_INFO";
    public static final String MGROOM_BAU_CUA_INFO = "MGROOM_BAU_CUA_INFO";
    public static final String MGROOM_CAO_THAP_INFO = "MGROOM_CAO_THAP_INFO";
    public static final String MGROOM_POKEGO_INFO = "MGROOM_POKEGO_INFO";
    protected String name;
    protected String gameName;
    protected List<User> users = new ArrayList<User>();
    
    public MGRoom(String name) {
        this.name = name;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean joinRoom(User user) {
        List<User> list2 = this.users;
        synchronized (list2) {
            if (!this.users.contains(user)) {
                this.users.add(user);
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
	public boolean quitRoom(User user) {
		List<User> list = this.users;
		synchronized (list) {
			if (this.users.contains(user)) {
				this.users.remove(user);
				return true;
			}
		}
		return false;
	}

    public void sendMessageToRoom(BaseMsg msg) {
        List<User> usersCopy = new ArrayList<User>(this.users);
        for (User user : usersCopy) {
            if (user == null) continue;
            ExtensionUtility.getExtension().send(msg, user);
        }
    }

//    public void sendMessageToUser(BaseMsg msg, String username) {
//        List<User> users = new ArrayList<>();
//        users.add(ExtensionUtility.getExtension().getApi().getUserByName(username));
//        if (users != null) {
//            ExtensionUtility.getExtension().sendUsers(msg, users);
//        }
//    }

    public void sendMessageToUser(BaseMsg msg, String username) {
        List<User> users = ExtensionUtility.getExtension().getApi().getUserByName(username);
        if (users != null) {
            ExtensionUtility.getExtension().sendUsers(msg, users);
        }
    }

    public void sendMessageToUser(BaseMsg msg, User user) {
        if (user != null) {
            ExtensionUtility.getExtension().send(msg, user);
        }
    }
}

