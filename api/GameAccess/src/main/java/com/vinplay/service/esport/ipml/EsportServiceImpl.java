/**
 * Archie
 */
package com.vinplay.service.esport.ipml;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.vinplay.connection.ConnectionPoolUtil;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.esport.EsportBalanceDto;
import com.vinplay.dto.esport.EsportLoginBase64;
import com.vinplay.dto.esport.EsportRespose;
import com.vinplay.dto.esport.EsportTransferDto;
import com.vinplay.dto.esport.EsportUserDto;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.GamesCommonService;
import com.vinplay.service.esport.EsportService;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.BaseResponse;
import com.vinplay.utils.MD5Utils;
import com.vinplay.utils.ThirdpartyConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


/**
 * @author Archie
 *
 */
public class EsportServiceImpl implements EsportService {

	private static final Logger LOGGER = Logger.getLogger(EsportServiceImpl.class);

	private static final ObjectMapper MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	private static final String[] TOKEN_HEADER = { "Authorization", GameThirdPartyInit.ESPORT_PRIVATE_TOKEN };
	
	private ResultFormat getBalanceEsport(String esportId) throws Exception {
		
		//make url
		String fullUrl = String.format("%s%s%s", GameThirdPartyInit.ESPORT_API_URL, ThirdpartyConstant.ESPORT.API_GET_BALANCE,
				esportId);
		//call api
		String data = ConnectionPoolUtil.runGet(fullUrl, TOKEN_HEADER);
		if (data != null && !"".equals(data)) {
			LOGGER.info("Espor getBalance response = " + data);
			EsportRespose<EsportBalanceDto> result = MAPPER.readValue(data,
					new TypeReference<EsportRespose<EsportBalanceDto>>() {
					});
			
			if (result.getResults() != null && !result.getResults().isEmpty()) {
				LOGGER.info("Esport getBalance data json parse = " + result.toString());
				EsportBalanceDto balanUser = result.getResults().get(0);
				List<Object> lstResult = new ArrayList<>();
				lstResult.add(balanUser);
				return new ResultFormat(0, "SUCCESS", lstResult);
			}else {
				LOGGER.error("Esport getBalance data json parse = null or empty ,fullUrl =" + fullUrl);
				return new ResultFormat("Esport đang bảo trì ");
			}
			
		} else {
			LOGGER.error("EsportBalance full Url = " + fullUrl);
			return new ResultFormat("Esport đang bảo trì ");
		}
	}

	private static EsportTransferDto esportTranfer(String sportId, Double amount, int direction, String referNo) throws Exception{
		LOGGER.info("EsportTransferDto request , sportId=" + sportId + " ,amount =" + amount + ",direction=" + direction);
		// parameter
		String fullUrl = null;
		if (direction == 1) {// deposit
			fullUrl = String.format("%s%s", GameThirdPartyInit.ESPORT_API_URL, ThirdpartyConstant.ESPORT.API_DEPOSIT);
		} else {// withdraw
			fullUrl = String.format("%s%s", GameThirdPartyInit.ESPORT_API_URL, ThirdpartyConstant.ESPORT.API_WITHDRAW);
		}
		JSONObject params = new JSONObject();
		params.put("member", sportId);
		params.put("operator_id", GameThirdPartyInit.ESPORT_PARTNER_ID);
		params.put("amount", amount);
		params.put("reference_no", referNo);
		// call API
		String data = ConnectionPoolUtil.postData(fullUrl, params.toString(), TOKEN_HEADER);
		
		if (data != null && !"".equals(data)) {
			EsportTransferDto result = MAPPER.readValue(data, EsportTransferDto.class);
			LOGGER.info("Esport transfer response ="+result.toString());
			return result;
		}
		return null;
	}

	@Override
	public ResultFormat transfer(ObjectInputStream objInStream) throws Exception {
		// get params 
		String loginname = (String) objInStream.readObject();
		Integer direction = Integer.parseInt(objInStream.readObject().toString());
		Double amount = Double.parseDouble(objInStream.readObject().toString());
		String ip = (String) objInStream.readObject();
		// validation
		if (!CommonMethod.ValidateRequest(loginname)) {
			LOGGER.info("EsportService loginname : " + loginname + "Trong 20s chỉ thực hiện request 1 lần ");
			return new ResultFormat(1, "Xin chờ 20 giây để thực hiện giao dịch tiếp theo ");
		}
		
		if (amount > 100000000 || amount < 0) {
			LOGGER.error("EsportService loginname = " + loginname + " , Giao dịch thất bại ");
			return new ResultFormat(1, "Giao dịch thất bại ");
		}
		
		if (direction != 0 && direction != 1) {
			LOGGER.error("EsportService direction = " + direction + " , Giao dịch thất bại ");
			return new ResultFormat(1, "Giao dịch thất bại , không xác định phương thức transfer");
		}
		// check exist loginname
		String esportId = mappingUserEsport(loginname);
		if (esportId == null || "".equals(esportId)) {
			LOGGER.error("It's unable to mapping Esport User , loginname=" + loginname);
			return new ResultFormat(1, "Không thể mapping user ,loginname =" + loginname);
		}

		LOGGER.info("EsportService request transfer loginname = " + loginname + ", esportId=  " + esportId
				+ ", direction= " + direction + ",amount=" + amount);

		String wid = CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
		EsportTransferDto resDto = new EsportTransferDto();
		
		//check balance and maintenance
		ResultFormat balanceObj = getBalanceEsport(esportId);
		Double currentBalance = 0.0;
		if (balanceObj.getRes() == 0) {
			EsportBalanceDto balanUser = (EsportBalanceDto)balanceObj.getList().get(0);
			currentBalance = balanUser.getBalance() * GameThirdPartyInit.ESPORT_RATE;
		}else {
			return new ResultFormat(2, "Hệ thống đang bảo trì , vui lòng quay lại sau !");
		}
		try {
			if (direction == 1) { // deposit
				BaseResponse<Boolean> res = updateBalance(loginname, amount, direction, ip, wid, 0);
				if (res.getData()) {
					long t1 =System.currentTimeMillis();
					resDto = esportTranfer(esportId, amount / GameThirdPartyInit.ESPORT_RATE, direction, wid);
					long t2 =System.currentTimeMillis();
					System.out.println("::::::::::::::::::::::::::::::::::TIMEXESPORT"+(t2-t1));
					if (resDto != null) {
						LOGGER.info("EsportService deposit success ,loginname = " + loginname + ", esportId= " + esportId);
						List<Object> list = new ArrayList<Object>();
						list.add(resDto);
						return new ResultFormat(0, "SUCCESS", list);
					} else {
						LOGGER.error("ESportSerice deposit fail , loginname= " + loginname + ", esportId= " + esportId);
						return new ResultFormat(1, "Liên hệ CSKH !");
					}
				} else {
					LOGGER.error("ESportSerice deposit fail , loginname= " + loginname + ", error = " +res.getMessage());
					return new ResultFormat(1, res.getMessage());
				}
			} else if (direction == 0) { // withdraw
				//check enough
				if(currentBalance < amount) {
					return new ResultFormat(1, "Số dư tài khoản Esport không đủ ", null);
				}
				long t1 =System.currentTimeMillis();
				resDto = esportTranfer(esportId, amount / GameThirdPartyInit.ESPORT_RATE, direction, wid);
				long t2 =System.currentTimeMillis();
				System.out.println("::::::::::::::::::::::::::::::::::TimeEsportAPI="+(t2-t1));
				if (resDto != null) {
					BaseResponse<Boolean> res = updateBalance(loginname, amount, direction, ip, wid, 0);
					if (res.getData()) {
						LOGGER.info("ESportService withdraw success ,loginname=" + loginname + ", esportId= " + esportId);
						List<Object> list = new ArrayList<Object>();
						list.add(resDto);
						return new ResultFormat(0, "SUCCESS", list);
					} else {
						LOGGER.error("ESportService withdraw fail , loginname=" + loginname + ", esportId=" + esportId +" ,Error message= "+res.getMessage());
						return new ResultFormat(1, res.getMessage());
					}

				} else {
					LOGGER.error("ESportService playerName : " + esportId + " Can not connect ESport");
					return new ResultFormat(1, "Không thể connect tới ESport , chuyển quỹ thất bại !");
				}
			} else {
				LOGGER.error("ESportService playerName : " + esportId + " msg : Transaction type is incorrect!");
				return new ResultFormat(1, "Transaction type is incorrect!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("ESportService playerName :" + esportId, ex);
			return new ResultFormat(1, ex.getMessage());
		}
	}

	@Override
	public Optional<EsportLoginBase64> esportDecode(String token) {
		LOGGER.info("esportLoginDecode token: " + token);
		byte[] actualByte = Base64.getDecoder().decode(token.getBytes());
		String tokenDecode = new String(actualByte);
		Gson gson = new Gson();
		EsportLoginBase64 esportLoginBase64 = gson.fromJson(tokenDecode, EsportLoginBase64.class);
		LOGGER.info("esportLoginDecode decode success: " + tokenDecode);
		return Optional.ofNullable(esportLoginBase64);
	}

	private  String esportEncode(String loginName, String esportUserName) {
		LOGGER.info("esportLoginEncode encode loginName: " + loginName);
		String token = null;
		EsportLoginBase64 esports = new EsportLoginBase64();
		esports.setLoginName(loginName);
		esports.setTimeStamp(System.currentTimeMillis());
		String md5EsportUserName = MD5Utils.generateKey(esportUserName);
		String md5OurSecretKey = MD5Utils.generateKey(GameThirdPartyInit.ESPORT_SECRET_TOKEN);
		esports.setEsport_userName(md5EsportUserName);
		esports.setSecret_key(md5OurSecretKey);
		Gson gson = new Gson();
		String tokenJson = gson.toJson(esports);
		byte[] actualByte = Base64.getEncoder().encode(tokenJson.getBytes());
		token = new String(actualByte);
		LOGGER.info("esportLoginEncode token is: " + token);
		return token;
	}

	public static void main(String[] args) {
		//esportEncode("archie", "test001");
	}

	@Override
	public ResultFormat login(String loginname) throws Exception {
		ResultFormat rf = new ResultFormat();
		if (loginname == null || "".equals(loginname)) {
			rf.setRes(1);
			rf.setMsg("loginname is null or empty");
			return rf;
		}
		String esportId = mappingUserEsport(loginname);
		if (esportId == null) {
			LOGGER.error("[ESPORT]: Không tồn tại user này, loginname = " + loginname);
			rf.setRes(1);
			rf.setMsg("Không tồn tại user này");
			return rf;
		}
		// create token
		String token = esportEncode(loginname, esportId);
		if (token == null) {
			LOGGER.error("[EsportService] Esport token is null or empty , loginname = " + loginname);
			rf.setRes(1);
			rf.setMsg("Liên hệ bộ phận CSKH !");
			return rf;
		}
		List<Object> lst = new ArrayList<Object>();
		EsportUserDto eUser = new EsportUserDto(esportId, loginname, token);
		lst.add(eUser);
		return new ResultFormat(0, "SUCCESS", lst);
	}

	@Override
	public ResultFormat getBalance(String loginname ,boolean isCreate) throws Exception {
		ResultFormat rf = new ResultFormat();
		List<Object> listObj = new ArrayList<Object>();
		if(isCreate) {
			//auto mapping user if not exist
			String esportId = mappingUserEsport(loginname);
			return getBalanceEsport(esportId);
		}else {
			BaseResponse<String> resp = GamesCommonService.checkPlayerExist(loginname, "ESPORT");
			if (resp != null) {
				if (0 == resp.getCode()) {
					// call api
					return getBalanceEsport(resp.getData());
				} else {
					rf.setRes(0);
					rf.setList(listObj);
					rf.setMsg(resp.getMessage());
				}
			} else {
				rf.setRes(1);
				rf.setList(listObj);
				rf.setMsg("Thất bại! Liên hệ CSKH");
			}
		}
		
		return rf;
	}

	private String mappingUserEsport(String loginName) {
		return null;
	}

	private BaseResponse<Boolean> updateBalance(String playerName, Double amount, int direction, String ip, String wid,
			int ispendding) {
		return null;
	}

}
