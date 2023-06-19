/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinplay.dichvuthe.client;

import bitzero.util.common.business.Debug;
import com.vinplay.dichvuthe.service.impl.RechargeServiceImpl;
import com.vinplay.usercore.utils.PartnerConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * @author HA
 */
public class GachTheClient {

    public GachTheClient() {

    }

    public void installMyPolicy() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sc = SSLContext.getInstance("SSL");
        X509HostnameVerifier verifier = new X509HostnameVerifier() {

            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier) verifier);
    }

    public void installAllTrustManager() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String urlHostname, SSLSession _session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject doCharge(int idCard, String cardType, String pin, String seri, String transId, long amount) throws Exception {
        this.installAllTrustManager();
        this.validateCharge(cardType, pin, seri);
        String sign = DigestUtils.md5Hex(pin + seri + idCard + amount + PartnerConfig.GachTheEmail + PartnerConfig.GachTheClientId + PartnerConfig.GachTheSecretKey);
        String urlRequest = "https://shippay.top/api/paymentCard?sign=" + sign + "&carrier=" + idCard + "&amount=" + amount + "&pin=" + pin + "&serial=" + seri + "&email=" + PartnerConfig.GachTheEmail + "&url_callback=" + PartnerConfig.GachTheCallBackUrl + "&lang=vi&custom_trans=" + transId;
        Debug.trace("rechargeByGachThe url: " + urlRequest);
        URL url = new URL(urlRequest);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setConnectTimeout(90000);
        request.setUseCaches(false);
        request.setDoOutput(true);
        request.setDoInput(true);
        HttpURLConnection.setFollowRedirects(true);
        request.setInstanceFollowRedirects(true);
        request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        request.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String result = "";
        String line = "";
        while ((line = rd.readLine()) != null) {
            result = result.concat(line);
        }
        JSONObject json = (JSONObject) new JSONParser().parse(result);
        Debug.trace("rechargeByGachThe ress: " + json);
        return json;
    }

    public JSONObject doChargeHaDongPho(int idCard, String cardType, String pin, String seri, String transId, long amount) throws Exception {
        this.installAllTrustManager();
        this.validateCharge(cardType, pin, seri);
        String sign = DigestUtils.md5Hex("12225012515147" + seri + pin + cardType);
        String urlRequest = "http://hadongpho.com/api/chargews?merchant_id=12225012515147&cardseri="+ seri +"&cardcode="+pin+"&cardamount="+amount+"&cardtype="+cardType+"&refcode="+transId+"&callback_url="+PartnerConfig.GachTheCallBackUrl+"&signature=" + sign;
        Debug.trace("rechargeByGachThe url: " + urlRequest);
        URL url = new URL(urlRequest);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setConnectTimeout(20000);
        request.setUseCaches(false);
        request.setDoOutput(true);
        request.setDoInput(true);
        HttpURLConnection.setFollowRedirects(true);
        request.setInstanceFollowRedirects(true);
        request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        request.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String result = "";
        String line = "";
        while ((line = rd.readLine()) != null) {
            result = result.concat(line);
        }
        JSONObject json = (JSONObject) new JSONParser().parse(result);
        Debug.trace("rechargeByGachThe ress: " + json);
        return json;
    }

    private String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateCharge(String cardType, String pin, String seri) throws GachTheException {
        if (cardType == null || cardType.isEmpty()) {
            throw new GachTheException("Ch\u01b0a c\u00f3 lo\u1ea1i th\u1ebb", 1);
        }
        if (!Arrays.asList("VTT", "VNP", "VMS", "FPT").contains(cardType)) {
            throw new GachTheException("Lo\u1ea1i th\u1ebb kh\u00f4ng h\u1ee3p l\u1ec7", 2);
        }
        if (pin == null || pin.isEmpty()) {
            throw new GachTheException("Ch\u01b0a c\u00f3 m\u00e3 pin", 3);
        }
        if (seri == null || seri.isEmpty()) {
            throw new GachTheException("Ch\u01b0a c\u00f3 serial", 4);
        }
        return true;
    }
}


