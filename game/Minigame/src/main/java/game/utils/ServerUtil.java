/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  bitzero.server.api.IBZApi
 *  bitzero.server.entities.User
 *  bitzero.server.entities.managers.IUserManager
 *  bitzero.server.extensions.data.BaseMsg
 *  bitzero.util.ExtensionUtility
 */
package game.utils;

import bitzero.server.api.IBZApi;
import bitzero.server.entities.User;
import bitzero.server.entities.managers.IUserManager;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.ExtensionUtility;
import java.util.List;
import java.util.ArrayList;

public class ServerUtil {
    public static void sendMsgToUser(BaseMsg msg, String username) {
//        List<User> user = new ArrayList<User>();
//        List<User> user = ExtensionUtility.getExtension().getApi().getUserByName(username);

        List<User> user = ExtensionUtility.getExtension().getApi().getUserByName(username);

        if (user != null) {
            ExtensionUtility.getExtension().sendUsers(msg, user);
        }
    }

    public static void sendMsgToUser(BaseMsg msg, User user) {
        if (user != null) {
            ExtensionUtility.getExtension().send(msg, user);
        }
    }

    public static void sendMsgToAllUsers(BaseMsg msg) {
        List<User> users = ExtensionUtility.globalUserManager.getAllUsers();
        for (User user : users) {
            ServerUtil.sendMsgToUser(msg, user);
        }
    }
}

