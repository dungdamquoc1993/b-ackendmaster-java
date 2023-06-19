
package com.vinplay.interfaces.sa;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.spec.IvParameterSpec;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.vinplay.dto.sa.SABetDetails;
import com.vinplay.dto.sa.SADepositResp;
import com.vinplay.dto.sa.SALoginReq;
import com.vinplay.dto.sa.SALoginResp;
import com.vinplay.dto.sa.SALogs;
import com.vinplay.dto.sa.SAUserInfoResp;
import com.vinplay.dto.sa.SAWithdrawalResp;
import com.vinplay.logic.CommonMethod;
import com.vinplay.usercore.utils.GameThirdPartyInit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.vinplay.utils.Constants;

public class SAUtils {

	private static String SecretKey = GameThirdPartyInit.SA_SECRET_KEY;
	private static String strencryptKey = GameThirdPartyInit.SA_ENCRYPT_KEY;
	private static byte[] encryptKey = strencryptKey.getBytes();
	private static String MD5Key = GameThirdPartyInit.SA_MD5_KEY;
	private static final Logger logger = Logger.getLogger(SAUtils.class);
	
	private static final String[] Suit = {"", "♠", "♥", "♣", "♦" };//0 1 2 3 4
	private static final Map<String, String> rank = new HashMap<>();
	static {
		rank.put("1", "A");
		rank.put("2", "2");
		rank.put("3", "3");
		rank.put("4", "4");
		rank.put("5", "5");
		rank.put("6", "6");
		rank.put("7", "7");
		rank.put("8", "8");
		rank.put("9", "9");
		rank.put("10", "10");
		rank.put("11", "J");
		rank.put("12", "Q");
		rank.put("13", "K");
	}
	private static Map<String, String> mapBac =new HashMap<>();
	
	private static void put(String key, String value) {
		if (value != null && !value.equals("") && key != null && !"".equals(key)) {
			if (mapBac.containsKey(key)) {
				String valueOld = mapBac.get(key);
				mapBac.put(key, value + valueOld);
			} else {
				mapBac.put(key, value);
			}
		}
	}

	private static String convertWithStream(Map<String, ?> map) {
		  return map.keySet()
					.stream()
					.map(key -> key + "=" + map.get(key))
					.collect(Collectors.joining(", ", "", ""));
	}
	//de quy
	private static  String getChild(NodeList nodeList ,Element rootElement,String parentNode,String gametype) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) nodeList.item(i);
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					NodeList nodeListscheck = node.getChildNodes();
					if(nodeListscheck.getLength() >1) {
						getChild(nodeListscheck, subElement,node.getNodeName(),gametype);
					}else {
						if ("bac".equals(gametype)) {
							String value = nodeListscheck.item(0).getTextContent();
							String strSuitRank ="";
							if("Suit".equals(node.getNodeName())) {
								strSuitRank = Suit[Integer.valueOf(value)];
							}else {
								strSuitRank = rank.get(value);
							}
							put(parentNode.replaceAll("[0123456789]","").replaceAll("Card", ""), strSuitRank);
						}else  {
							String value = nodeListscheck.item(0).getTextContent();
							put(node.getNodeName(), value);
						}
						
					}
					
				}
				
			}
		}
		return null;
	}
	
	public static String callAPI(String QS, String strdate) {
		String q = null;
		try {
			q = desEncrypt(QS);
		} catch (Exception e) {
			logger.error("ex", e);
		}
		String s = getMd5(QS + MD5Key + strdate + SecretKey);
		String resp = postData(GameThirdPartyInit.SA_GENERIC_API_URL, q, s);
		logger.info("SA callAPI , param :" +QS +"  ressponse: "+resp);
		return resp;
	}

	public static String callAPIReport(String QS, String strdate) throws Exception{
		String q = null;
		try {
			q = desEncrypt(QS);
		} catch (Exception e) {
			logger.error("ex", e);
		}
		String s = getMd5(QS + MD5Key + strdate + SecretKey);
		String resp = postData(GameThirdPartyInit.SA_GET_BET_API_URL, q, s);
		logger.info("SA callAPIReport , param :" +QS +"  ressponse: "+resp);
		return resp;
	}

	public static SALoginResp login(SALoginReq request) {
		Date currentTime = new Date();
		SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMddHHmmss");
		String strdate = format0.format(currentTime);
		String qs = "method=LoginRequest&Key=" + SecretKey + "&Time=" + strdate + "&Username=" + request.getUserName()
				+ "&CurrencyType=VND";
		String resp = callAPI(qs, strdate);
		if (resp == null || "".equals(resp)) {
			logger.error("SADepositResp login request: " + qs + ", response is null");
			return null;
		}
		Document doc = convertStringToXMLDocument(resp);

		Element rootElement = doc.getDocumentElement();
		if (rootElement.getNodeType() == Node.ELEMENT_NODE) {
			NodeList nList = doc.getElementsByTagName("ErrorMsgId");
			if (nList.getLength() > 0) {
				String ErrorMsgId = rootElement.getElementsByTagName("ErrorMsgId").item(0).getTextContent();
				if (ErrorMsgId.equals("0")) {
					String token = rootElement.getElementsByTagName("Token").item(0).getTextContent();
					String username = rootElement.getElementsByTagName("DisplayName").item(0).getTextContent();
					String ErrorMsg = rootElement.getElementsByTagName("ErrorMsg").item(0).getTextContent();
					SALoginResp respDto = new SALoginResp();
					respDto.setDisplayName(username);
					respDto.setErrorMsg(ErrorMsg);
					respDto.setErrorMsgId(ErrorMsgId);
					respDto.setToken(token);
					return respDto;
				} 
			}
		}
		logger.error("SALoginResp request : " + qs);
		logger.error("SALoginResp response : " + resp);
		return null;
	}

	public static SAUserInfoResp getSAInfo(String saID) {
		Date currentTime = new Date();
		SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMddHHmmss");
		String strdate = format0.format(currentTime);
		String qs = "method=GetUserStatusDV&Key=" + SecretKey + "&Time=" + strdate + "&Username=" + saID;
		String resp = callAPI(qs, strdate);
		if (resp == null || "".equals(resp)) {
			logger.error("SAUserInfoResp request : " + qs);
			return new SAUserInfoResp();
		}
		Document doc = convertStringToXMLDocument(resp);

		Element rootElement = doc.getDocumentElement();
		if (rootElement.getNodeType() == Node.ELEMENT_NODE) {
			NodeList nList = doc.getElementsByTagName("ErrorMsgId");
			if (nList.getLength() > 0) {
				String ErrorMsgId = rootElement.getElementsByTagName("ErrorMsgId").item(0).getTextContent();
				SAUserInfoResp respDto = new SAUserInfoResp();
				String ErrorMsg = rootElement.getElementsByTagName("ErrorMsg").item(0).getTextContent();
				if ("0".equals(ErrorMsgId)) {
					String Balance = rootElement.getElementsByTagName("Balance").item(0).getTextContent();
					respDto.setBalance(Double.parseDouble(Balance)*Constants.SA_RATE);
				}
				respDto.setErrorMsg(ErrorMsg);
				respDto.setErrorMsgId(ErrorMsgId);
				return respDto;
			} 
		}
		logger.error("SAUserInfoResp request : " + qs);
		logger.error("SAUserInfoResp response : " + resp);
		return null;
	}

	public static SADepositResp deposit(Double amount_trans, String userName, String saID) {
		Date currentTime = new Date();
		SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMddHHmmss");
		String strdate = format0.format(currentTime);

		String orderid = "IN" + strdate + saID;

		String qs = "method=CreditBalanceDV&Key=" + SecretKey + "&Time=" + strdate + "&Username=" + saID + "&OrderId="
				+ orderid + "&CreditAmount=" + amount_trans;
		String resp = callAPI(qs, strdate);
		if(resp==null ||"".equals(resp)) {
			logger.error("SADepositResp deposit request: " + qs + ", response is null");
			return null;
		}
		Document doc = convertStringToXMLDocument(resp);

		Element rootElement = doc.getDocumentElement();
		if (rootElement.getNodeType() == Node.ELEMENT_NODE) {
			NodeList nList = doc.getElementsByTagName("ErrorMsgId");
			if (nList.getLength() > 0) {
				String ErrorMsgId = rootElement.getElementsByTagName("ErrorMsgId").item(0).getTextContent();
				if (ErrorMsgId.equals("0")) {
					String Username = rootElement.getElementsByTagName("Username").item(0).getTextContent();
					String Balance = rootElement.getElementsByTagName("Balance").item(0).getTextContent();
					String ErrorMsg = rootElement.getElementsByTagName("ErrorMsg").item(0).getTextContent();
					String CreditAmount = rootElement.getElementsByTagName("CreditAmount").item(0).getTextContent();

					SADepositResp respDto = new SADepositResp();
					respDto.setBalance(Double.parseDouble(Balance));
					respDto.setErrorMsg(ErrorMsg);
					respDto.setUsername(Username);
					respDto.setErrorMsgId(ErrorMsgId);
					respDto.setCreditamount(Double.parseDouble(CreditAmount));
					return respDto;
				}
			}
		}
		logger.error("SADepositResp deposit request: " + qs);
		logger.error("SADepositResp deposit response: " + resp);
		return null;
	}

	public static SAWithdrawalResp withdrawal(Double amount_trans, String userName, String saID) {

		Date currentTime = new Date();
		SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMddHHmmss");
		String strdate = format0.format(currentTime);

		String orderid = "OUT" + strdate + saID;

		String qs = "method=DebitBalanceDV&Key=" + SecretKey + "&Time=" + strdate + "&Username=" + saID + "&OrderId="
				+ orderid + "&DebitAmount=" + amount_trans;
		String resp = callAPI(qs, strdate);
		if(resp==null ||"".equals(resp)) {
			logger.error("SADepositResp withdrawal request: " + qs + ", response is null");
			return null;
		}
		Document doc = convertStringToXMLDocument(resp);

		Element rootElement = doc.getDocumentElement();
		if (rootElement.getNodeType() == Node.ELEMENT_NODE) {
			NodeList nList = doc.getElementsByTagName("ErrorMsgId");
			if (nList.getLength() > 0) {
				String ErrorMsgId = rootElement.getElementsByTagName("ErrorMsgId").item(0).getTextContent();
				if (ErrorMsgId.equals("0")) {

					String Username = rootElement.getElementsByTagName("Username").item(0).getTextContent();
					String Balance = rootElement.getElementsByTagName("Balance").item(0).getTextContent();
					String ErrorMsg = rootElement.getElementsByTagName("ErrorMsg").item(0).getTextContent();
					String DebitAmount = rootElement.getElementsByTagName("DebitAmount").item(0).getTextContent();

					SAWithdrawalResp respDto = new SAWithdrawalResp();
					respDto.setBalance(Double.parseDouble(Balance) * Constants.SA_RATE);
					respDto.setErrorMsg(ErrorMsg);
					respDto.setUsername(Username);
					respDto.setErrorMsgId(ErrorMsgId);
					respDto.setDebitamount(Double.parseDouble(DebitAmount) * Constants.SA_RATE);
					return respDto;
				} else if (ErrorMsgId.equals("121")) {
					SAWithdrawalResp respDto = new SAWithdrawalResp();
					respDto.setErrorMsgId(ErrorMsgId);
					respDto.setErrorMsg("SAGaming Balance Not Enough!!!");
					return respDto;
				} 
			} 
		}
		logger.error("SAWithdrawalResp Withdraw : " + qs);
		logger.error("SAWithdrawalResp Withdraw : " + resp);
		return null;
	}
	public static SALogs getBetLogs(String startTime, String endTime) {
		String strdate = CommonMethod.GetCurDate("yyyyMMddHHmmss");
		String qs = "method=GetAllBetDetailsForTimeIntervalDV&Key=" + SecretKey + "&Time=" + strdate + "&FromTime=" + startTime + "&ToTime=" + endTime;
		String resp = "";
		SALogs respLog = new SALogs();
		try {
			resp = callAPIReport(qs, strdate);
			if (resp == null || "".equals(resp)) {
				throw new Exception("SADepositResp getBetLogs request: " + qs + ", response is null");
			}
			Document doc = convertStringToXMLDocument(resp);

			Element rootElement = doc.getDocumentElement();

			if (rootElement.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nList = doc.getElementsByTagName("ErrorMsgId");
				if (nList.getLength() > 0) {
					String ErrorMsgId = rootElement.getElementsByTagName("ErrorMsgId").item(0).getTextContent();

					if (ErrorMsgId.equals("0")) {
						List<SABetDetails> betDetails = new ArrayList<>();

						String ErrorMsg = rootElement.getElementsByTagName("ErrorMsg").item(0).getTextContent();
						respLog.setErrorMsgId(ErrorMsgId);
						respLog.setErrorMsg(ErrorMsg);
						NodeList nListBetLog = rootElement.getElementsByTagName("BetDetail");
						for (int temp = 0; temp < nListBetLog.getLength(); temp++) {
							Node node = nListBetLog.item(temp);
							if (node.getNodeType() == Node.ELEMENT_NODE && node.hasChildNodes()) {
								String betTime = rootElement.getElementsByTagName("BetTime").item(temp)
										.getTextContent();
								String payoutTime = rootElement.getElementsByTagName("PayoutTime").item(temp)
										.getTextContent();
								String userName = rootElement.getElementsByTagName("Username").item(temp)
										.getTextContent();
								String hostID = rootElement.getElementsByTagName("HostID").item(temp).getTextContent();
								String gameID = rootElement.getElementsByTagName("GameID").item(temp).getTextContent();
								String round = rootElement.getElementsByTagName("Round").item(temp).getTextContent();
								String set = rootElement.getElementsByTagName("Set").item(temp).getTextContent();
								String betID = rootElement.getElementsByTagName("BetID").item(temp).getTextContent();
								String betAmount = rootElement.getElementsByTagName("BetAmount").item(temp)
										.getTextContent();
								String rolling = rootElement.getElementsByTagName("Rolling").item(temp)
										.getTextContent();
								String resultAmount = rootElement.getElementsByTagName("ResultAmount").item(temp)
										.getTextContent();
								// String gameResult =
								// rootElement.getElementsByTagName("GameResult").item(temp).getChildNodes().toString();
								String balance = rootElement.getElementsByTagName("Balance").item(temp)
										.getTextContent();
								String gameType = rootElement.getElementsByTagName("GameType").item(temp)
										.getTextContent();
								String betType = rootElement.getElementsByTagName("BetType").item(temp)
										.getTextContent();
								String betSource = rootElement.getElementsByTagName("BetSource").item(temp)
										.getTextContent();
								Node nodeState = rootElement.getElementsByTagName("State").item(temp);
								String state = nodeState != null ? nodeState.getTextContent() : "";

								String transactionID = rootElement.getElementsByTagName("TransactionID").item(temp)
										.getTextContent();

								mapBac.clear();
								String gameResult = "";
								try {
									Node gameResultNode = rootElement.getElementsByTagName("GameResult").item(temp);
									if (gameResultNode.hasChildNodes()) {
										NodeList resu = gameResultNode.getChildNodes().item(1).getChildNodes();
										getChild(resu, rootElement, "", gameType);
										gameResult = convertWithStream(mapBac);
									}
								} catch (Exception e) {
									gameResult = "";
									logger.error(e);
								}
								SABetDetails betDetail = new SABetDetails(betTime, payoutTime, userName, hostID, gameID,
										round, set, betID, betAmount, rolling, resultAmount, balance, gameType, betType,
										betSource, transactionID, state, gameResult, Constants.SA_RATE);
								betDetails.add(betDetail);
							}
						}
						respLog.setBetDetailList(betDetails);
						return respLog;
					} else {
						String ErrorMsg = rootElement.getElementsByTagName("ErrorMsg").item(0).getTextContent();
						logger.error("ErrorMsgId= " + ErrorMsgId + " ,ErrorMsgId = " + ErrorMsg);
						respLog.setErrorMsgId(ErrorMsgId);
						respLog.setErrorMsg(ErrorMsg);
						return respLog;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			respLog.setErrorMsgId("99");
			respLog.setErrorMsg(e.getMessage());
		}
		logger.error("SALogs request: " + qs + ", response=" + resp);
		return respLog;
	}

	private static Document convertStringToXMLDocument(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (Exception e) {
			logger.error("ex", e);
		}
		return null;
	}

	public static String desEncrypt(String QS) throws Exception {
		try {
			KeySpec keySpec = new DESKeySpec(encryptKey);
			SecretKey myDesKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
			IvParameterSpec iv = new IvParameterSpec(encryptKey);

			Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, iv);
			byte[] text = QS.getBytes();

			byte[] textEncrypted = desCipher.doFinal(text);
			String t = Base64.getEncoder().encodeToString(textEncrypted).toString();

			desCipher.init(Cipher.DECRYPT_MODE, myDesKey, iv);

			byte[] textDecrypted = desCipher.doFinal(textEncrypted);

			logger.info("Query String : " + new String(textDecrypted));
			return t;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException
				| IllegalBlockSizeException | InvalidAlgorithmParameterException | BadPaddingException e) {
			logger.error("ex", e);
		}
		return null;
	}

	public static String getMd5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());

			BigInteger no = new BigInteger(1, messageDigest);

			StringBuilder hashtext = new StringBuilder(no.toString(16));
			while (hashtext.length() < 32) {
				hashtext.insert(0, "0");
			}
			return hashtext.toString();
		}catch (NoSuchAlgorithmException e) {
			logger.error("ex", e);
			throw new RuntimeException(e);
		}
	}

	public static String postData(String url, String q, String s) {
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		clientParams.setHttpElementCharset("UTF-8");

		HttpState httpState = new HttpState();
		httpClient.setParams(clientParams);
		httpClient.getParams().setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);

		httpClient.setState(httpState);
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		PostMethod postMethod = new PostMethod(url);
		BufferedReader br =null;
		try {
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			postMethod.addParameter("q", q);
			postMethod.addParameter("s", s);
			int statusCode = httpClient.executeMethod(postMethod);
			logger.info("statusCode" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + postMethod.getStatusLine());
			}
			InputStream response = postMethod.getResponseBodyAsStream();
			br= new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception ex) {
			logger.error("Error：" + ex.getMessage() + "," + url);
		} finally{
			if(postMethod!=null)
				postMethod.releaseConnection();
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("ex", e);
				}
			}
		}
		return null;
	}

}
