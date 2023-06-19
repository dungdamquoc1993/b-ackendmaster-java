package com.vinplay.dichvuthe.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;
import org.apache.log4j.Logger;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dichvuthe.client.VinplayClient;
import com.vinplay.dichvuthe.service.AlertService;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import org.json.JSONException;
import org.json.JSONObject;

public class AlertServiceImpl implements AlertService {
    private static final Logger logger = Logger.getLogger((String) "api");

    public boolean sendSMS2List(List<String> receives, String content, boolean call) {
        return this.sendSMS(receives, content, call);
    }

    @Override
    public boolean SendSMSEsms(String phone, String messge) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.otp.vin?action=send&key=f7ff930b78d6ccc1ffd5&to=" + phone + "&code=" + messge)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.has("Code") && jsonObject.getInt("Code") == 0) {
                    return true;
                } else return false;
            } else return false;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean SendVoiceOTPESMS(String phone, String otp) {
        return true;
    }

    @Override
    public boolean SendSMSRutCuoc(String phone, String messge) {
        return true;
    }

    @Override
    public boolean SendSMSAirpay(String phone, String messge) {
        return true;
    }

    @Override
    public boolean SendSmsBrandName(String phone, String message) {
        return false;
    }

    @Override
    public boolean sendSMS2One(String mobile, String content, boolean call) {
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change. Enabled
     * aggressive block sorting Enabled unnecessary exception pruning Enabled
     * aggressive exception aggregation
     */
    private boolean sendSMS(List<String> receives, String content, boolean call) {
        return false;
    }

    @Override
    public boolean alert2List(List<String> receives, String content, boolean call) {
        return this.alert(receives, content, call);
    }

    @Override
    public boolean alert2One(String mobile, String content, boolean call) {
        ArrayList<String> receives = new ArrayList<String>();
        receives.add(mobile);
        return this.alert(receives, content, call);
    }

    private boolean alert(List<String> receives, String content, boolean call) {
        try {
            VinplayClient.aleft(receives, content, call);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug((Object) e);
            return false;
        }
    }

    private String revertMobile(String mobile) {
        if (mobile.substring(0, 1).equals("0")) {
            return "84" + mobile.substring(1);
        }
        return mobile;
    }

    private String revertMobileBegin84(String mobile) {
        if (mobile.substring(0, 1).equals("0")) {
            return "84" + mobile.substring(1);
        }
        return mobile;
    }

    private String revertMobileBegin0(String mobile) {
        if (mobile.substring(0, 2).equals("84")) {
            return "0" + mobile.substring(2);
        }
        return mobile;
    }

    @Override
    public boolean sendEmail(String subject, String content, List<String> receives) {
        try {
            VinplayClient.sendEmail(subject, content, receives);
        } catch (Exception e) {
            logger.debug((Object) e);
        }
        return true;
    }

    @Override
    public boolean sendSMS2User(String username, String content) {
        String mobile;
        UserCacheModel model;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey((Object) username) && (model = (UserCacheModel) userMap.get((Object) username)) != null
                && model.isHasMobileSecurity() && (mobile = model.getMobile()) != null && !mobile.isEmpty()) {
            try {
                return this.sendSMS2One(mobile, content, false);
            } catch (Exception exception) {
                // empty catch block
            }
        }
        return false;
    }
}
