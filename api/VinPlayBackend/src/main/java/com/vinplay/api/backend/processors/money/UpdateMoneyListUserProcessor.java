//package com.vinplay.api.backend.processors.money;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.core.IMap;
//import com.vinplay.api.backend.models.UpdateMoneyModel;
//import com.vinplay.api.backend.response.UpdateMoneyResponse;
//import com.vinplay.usercore.service.impl.OtpServiceImpl;
//import com.vinplay.usercore.service.impl.UserServiceImpl;
//import com.vinplay.usercore.utils.GameCommon;
//import com.vinplay.vbee.common.cp.BaseProcessor;
//import com.vinplay.vbee.common.cp.Param;
//import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
//import com.vinplay.vbee.common.models.UserModel;
//import com.vinplay.vbee.common.response.BaseResponseModel;
//
//public class UpdateMoneyListUserProcessor
//implements BaseProcessor<HttpServletRequest, String> {
//    private static final Logger logger = Logger.getLogger((String)"backend");
//
//    public String execute(Param<HttpServletRequest> param) {
//        HttpServletRequest request = (HttpServletRequest)param.get();
//        UpdateMoneyResponse response = new UpdateMoneyResponse(false, "1001");
//        try {
//            String actionName = request.getParameter("ac");
//            String data = request.getParameter("data");
//            String moneyType = request.getParameter("mt");
//            String reason = request.getParameter("rs");
//            String otp = request.getParameter("otp");
//            String type = request.getParameter("type");
//            logger.debug(("Request UpdateMoneyListUserProcessor: data: " + data + ", moneyType: " + moneyType + ", reason: " + reason + ", otp: " + otp + ", otpType: " + type));
//            if (data != null && reason != null && !reason.isEmpty() && moneyType != null && (moneyType.equals("vin") || moneyType.equals("xu")) && otp != null && type != null && (type.equals("1") || type.equals("0"))) {
//                String admin;
//                String[] arr;
//                OtpServiceImpl otpService = new OtpServiceImpl();
//                int code = 3;
//                String[] array = arr = GameCommon.getValueStr("SUPER_ADMIN").split(",");
//                int length = array.length;
//                for (int j = 0; j < length && (code = otpService.checkOtp(otp, admin = array[j], type, (String)null)) != 0; ++j) {
//                }
//                if (code == 0) {
//                    if (actionName == null) {
//                        actionName = "Admin";
//                    }
//                    HazelcastInstance client = HazelcastClientFactory.getInstance();
//                    IMap userMap = client.getMap("users");
//                    UserServiceImpl service = new UserServiceImpl();
//                    ArrayList<UpdateMoneyModel> listUpdate = new ArrayList<UpdateMoneyModel>();
//                    ArrayList<UpdateMoneyModel> listResponse = new ArrayList<UpdateMoneyModel>();
//                    JSONArray jArr = new JSONArray(data);
//                    if (jArr != null) {
//                        for (int i = 0; i < jArr.length(); ++i) {
//                            JSONObject jObj = jArr.getJSONObject(i);
//                            Iterator keys = jObj.keys();
//                            while (keys.hasNext()) {
//                                UpdateMoneyModel model;
//                                String key = (String)keys.next();
//                                long money = jObj.getLong(key);
//                                UserModel userModel = service.getNicknameExactly(key, userMap);
//                                if (userModel == null) {
//                                    model = new UpdateMoneyModel(key, money, false, "2001");
//                                    listResponse.add(model);
//                                    continue;
//                                }
//                                if (userModel.isBot()) {
//                                    model = new UpdateMoneyModel(key, money, true, "1001");
//                                    listResponse.add(model);
//                                    continue;
//                                }
//                                model = new UpdateMoneyModel(userModel.getNickname(), money, false, "1001");
//                                listUpdate.add(model);
//                            }
//                        }
//                    }
//                    for (UpdateMoneyModel md : listUpdate) {
//                        BaseResponseModel mnres = service.updateMoneyFromAdmin(md.nickname, md.money, moneyType, actionName, "Admin", reason);
//                        md.errorCode = mnres.getErrorCode();
//                        listResponse.add(md);
//                        // notify
//                       /* try {
//                            HttpClient httpClient = HttpClientBuilder.create().build();
//                            String url = "";
//                            if ("XXENG".equals(PartnerConfig.Client))
//                                url = PartnerConfig.HostBot;
//                            else if ("R68".equals(PartnerConfig.Client))
//                                url = PartnerConfig.HostBot;
//                            else
//                                url = PartnerConfig.HostBot;
//                            HttpPost httpPost = new HttpPost(url + "/rpadmin/ozawasecret1243/3");
//                            httpPost.addHeader("Content-Type", "application/json");
//
//                            JSONObject obj = new JSONObject();                 
//                            obj.put("sender_nick_name", "admin");
//                            String serverName = "xxeng";
//                            UserService userService = new UserServiceImpl();
//                            UserModel receiverModel = userService.getUserByNickName(md.nickname);
//                            if (receiverModel != null && receiverModel.getClient() != null && !receiverModel.getClient().equals("")) {
//                                if (receiverModel.getClient().equals("M")) {
//                                    serverName = "manVip";
//                                }
//                                else if (receiverModel.getClient().equals("R")) {
//                                    serverName = "r99";
//                                }
//                                else if (receiverModel.getClient().equals("V")) {
//                                    serverName = "Vip52";
//                                }
//                            }
//                            obj.put("serverName", serverName);
//                            obj.put("receiver_nick_name", md.nickname);
//                            obj.put("money", md.money);
//                            obj.put("description", reason);
//                            obj.put("created_time", System.currentTimeMillis());                        
//                            StringEntity requestEntity = new StringEntity(obj.toString(), "UTF-8");
//                            httpPost.setEntity(requestEntity);
//
//                            // add request header
//                            HttpResponse httpResponse = httpClient.execute(httpPost);
//
//                            BufferedReader rd = new BufferedReader(
//                                    new InputStreamReader(httpResponse.getEntity().getContent()));
//
//                            StringBuffer result = new StringBuffer();
//                            String line = "";
//                            while ((line = rd.readLine()) != null) {
//                                result.append(line);
//                            }            
//                            Debug.trace(("LobbyModule bot tele response: " + result));
//                        } catch (Exception ex) {
//                            Debug.trace(("LobbyModule error: " + ex));
//                        }*/
//                    }
//                    response.setListResponse(listResponse);
//                    response.setSuccess(true);
//                    response.setErrorCode("0");
//                } else if (code == 3) {
//                    response.setErrorCode("1008");
//                } else if (code == 4) {
//                    response.setErrorCode("1021");
//                }
//            }
//        }
//        catch (Exception e) {
//            logger.debug(e);
//        }
//        logger.debug(("Response UpdateMoneyUser: " + response.toJson()));
//        return response.toJson();
//    }
//}
//
