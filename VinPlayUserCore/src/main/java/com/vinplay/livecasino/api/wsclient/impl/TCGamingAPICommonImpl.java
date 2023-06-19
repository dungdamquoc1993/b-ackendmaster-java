package com.vinplay.livecasino.api.wsclient.impl;

import bitzero.util.common.business.Debug;
import com.vinplay.livecasino.api.core.common.DESEncrypt;
import com.vinplay.livecasino.api.core.common.HashUtil;
import com.vinplay.livecasino.api.core.common.HttpUtils;
import com.vinplay.livecasino.api.core.obj.*;
import com.vinplay.livecasino.api.wsclient.exception.ProcessException;
import com.vinplay.livecasino.api.wsclient.exception.RemoteException;
import com.vinplay.livecasino.api.wsclient.exception.TransportException;
import com.vinplay.livecasino.api.core.common.JsonUtil;
import com.vinplay.livecasino.api.wsclient.TCGamingAPICommon;
import org.apache.log4j.Logger;

import java.net.URLEncoder;


/**
 * 天成 COMMON API 共用接口 实现类别
 *
 * @author PHOENIX WU
 */

/**
 * <CODE> TCGamingAPICommonImpl </CODE>
 * 天成共用接口实作类别
 *
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class TCGamingAPICommonImpl implements TCGamingAPICommon {

    private static final Logger logger = Logger.getLogger(TCGamingAPICommonImpl.class);

    // 用以判断错误
    private static final String UNKNOWN_TRANS_STATUS = "UNKNOWN";

    // 连接接口的设定物件
    private TCGamingConfigObj configObj;

    /**
     * 在需告类别时，需要配置此物件
     *
     * @param configObj 接口连接配置物件
     */
    public TCGamingAPICommonImpl(TCGamingConfigObj configObj) {
        this.configObj = configObj;
    }

    /**
     *
     */
    @Override
    public TCGBaseResponse registerMember(String username, String password, String currency) throws RemoteException, ProcessException, TransportException {

        CreateRegisterPlayerApiRequest request = new CreateRegisterPlayerApiRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setCurrency(currency);

        String json = JsonUtil.toJson(request);
        Debug.trace( "TCGBaseResponse " + json);
        String result = doRequest(configObj, json);
        Debug.trace("TCGBaseResponse result" + result);
        TCGBaseResponse response = JsonUtil.fromJson(result, TCGBaseResponse.class);

//		if(!response.isSuccess()){
//			throw new ProcessException("Failed on TCG , "+ response.getErrorMessage(),null);
//		}

        return response;
    }

    @Override
    public BalanceResponse getBalanceMember(String username, int productType) throws RemoteException, ProcessException, TransportException {
        GetBalanceMemberApiRequest request = new GetBalanceMemberApiRequest();
        request.setUsername(username);
        request.setProduct_type(productType);

        String json = JsonUtil.toJson(request);
        Debug.trace("TCGBaseResponse getBalanceMember" + json);
        String result = doRequest(configObj, json);
        Debug.trace("TCGBaseResponse getBalanceMember result" + result);
        BalanceResponse response = JsonUtil.fromJson(result, BalanceResponse.class);
        return response;
    }

    @Override
    public TCGBaseResponse fundTransferIn(String username, int productType, double amount, String transactionId) throws RemoteException, ProcessException, TransportException {
        FundTransactionInApiRequest request = new FundTransactionInApiRequest();
        request.setUsername(username);
        request.setProduct_type(productType);
        request.setAmount(amount);
        request.setReference_no(transactionId);
        String json = JsonUtil.toJson(request);
        Debug.trace("TCGBaseResponse fundTransferIn" + json);
        String result = doRequest(configObj, json);
        Debug.trace("TCGBaseResponse fundTransferIn result" + result);
        TCGBaseResponse response = JsonUtil.fromJson(result, TCGBaseResponse.class);
        return response;
    }

    @Override
    public TCGBaseResponse fundTransferOutAll(String username, int productType, String transactionId) throws RemoteException, ProcessException, TransportException {
        FundTransactionOutAllApiRequest request = new FundTransactionOutAllApiRequest();
        request.setUsername(username);
        request.setProduct_type(productType);
        request.setReference_no(transactionId);
        String json = JsonUtil.toJson(request);
        Debug.trace("TCGBaseResponse fundTransferOutAll" + json);
        String result = doRequest(configObj, json);
        Debug.trace("TCGBaseResponse fundTransferOutAll result" + result);
        TCGBaseResponse response = JsonUtil.fromJson(result, TCGBaseResponse.class);
        return response;
    }

    @Override
    public LaunchGameResponse launchGame(String username, int productType, String platform, String game_mode, String game_code) throws RemoteException, ProcessException, TransportException {
        LaunchGameApiRequest request = new LaunchGameApiRequest();
        request.setUsername(username);
        request.setProduct_type(productType);
        request.setPlatform(platform);
        request.setGame_mode(game_mode);
        request.setGame_code(game_code);
        String json = JsonUtil.toJson(request);
        Debug.trace("TCGBaseResponse launchGame" + json);
        String result = doRequest(configObj, json);
        Debug.trace("TCGBaseResponse launchGame result" + result);
        LaunchGameResponse response = JsonUtil.fromJson(result, LaunchGameResponse.class);
        return response;
    }


    /**
     * 呼叫天成接口
     *
     * @param json 参数
     * @return 结果字串
     */
    protected String doRequest(TCGamingConfigObj configObj, String json) {

        System.out.println("json :\n " + json);

        try {
            // 参数加密
            DESEncrypt des = new DESEncrypt(configObj.getDesKey());
            String encryptedParams = des.encrypt(json);

            //签名档加密
            String sign = HashUtil.sha256(encryptedParams + configObj.getSha256Key());

            //组连接字串
            String data = "merchant_code=" + URLEncoder.encode(configObj.getMerchantCode(), "UTF-8")
                    + "&params=" + URLEncoder.encode(encryptedParams, "UTF-8")
                    + "&sign=" + URLEncoder.encode(sign, "UTF-8");


            System.out.println("data :\n " + data);

            //传送
            return HttpUtils.newPost(configObj.getApiUrl(), data).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
