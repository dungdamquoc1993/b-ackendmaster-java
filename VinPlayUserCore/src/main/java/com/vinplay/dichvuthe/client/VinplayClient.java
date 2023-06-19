package com.vinplay.dichvuthe.client;

import java.util.List;


public class VinplayClient {

    public static Object cashOutByBank(String id, String bank, String account, String name, int amount, String sign) throws Exception {
        return new Object();
    }

    public static Object reCheckCashOutByBank(String id, String sign) throws Exception {
        return new Object();
    }

    public static String sendAleftSMS(List<String> receives, String content, boolean call) throws Exception {
//        AlertObj obj = new AlertObj(receives, content, call);
//        ObjectMapper mapper = new ObjectMapper();
//        String res = HttpClient.post(GameCommon.getValueStr("DVT_URL") + "/api/alert", mapper.writeValueAsString((Object)obj));
        return "";
    }

    public static String aleft(List<String> receives, String content, boolean call) throws Exception {
        return "";
    }

    public static String sendEmail(String subject, String content, List<String> receives) throws Exception {
        return "";
    }
}

