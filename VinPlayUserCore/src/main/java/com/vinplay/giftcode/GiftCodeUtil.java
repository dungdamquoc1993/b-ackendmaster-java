package com.vinplay.giftcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.usercore.service.UserBonusService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserBankServiceImpl;
import com.vinplay.usercore.service.impl.UserBonusServiceImpl;
import com.vinplay.vbee.common.models.UserBonusModel;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import bitzero.util.common.business.Debug;

public class GiftCodeUtil {
	public static void main(String[] args) {
		String string_date = "2021-06-03 23:59:59";

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
		    Date d = f.parse(string_date);
		    long milliseconds = d.getTime();
		    System.out.println(milliseconds);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
	}
    public static int isUsedGiftCode(GiftCodeModel giftCodeModel, String userName,String ip,UserService userService) {
        if (giftCodeModel == null) return GiftCodeErrorCode.WRONG_GIFT_CODE;
        try {
            Date date = VinPlayUtils.getDateTime(VinPlayUtils.getCurrentDateTime());
            if (date.getTime() > giftCodeModel.exprired.getTime() || date.getTime() < giftCodeModel.from.getTime()) {
                return GiftCodeErrorCode.GIFTCODE_EXPIRED; // khong nam trong khoang thoi gian
            }
            if (giftCodeModel.time_used >= giftCodeModel.max_use) {
                return GiftCodeErrorCode.GIFTCODE_OUT_OF_USE; // gift code het so lan su dung
            }

            if (isUserUsedGiftCode(giftCodeModel.id, userName)) {
                return GiftCodeErrorCode.USER_USED;
            }
            int giftcodeType =giftCodeModel.type;
            switch (giftCodeModel.type) {
                case GiftCodeType.ONE_FOR_THIS_USER: {
                    if (!giftCodeModel.user_name.equalsIgnoreCase(userName)) {
                        return GiftCodeErrorCode.NOT_USE_FOR_USERNAME; // gift code khong danh cho ban
                    }
                    
        			break;
                }
                case GiftCodeType.ONE_FOR_MANY_USER: {
                    if (!userService.isXacThucSDT(userName)) {
                        return GiftCodeErrorCode.NOT_VERIFY_PHONE;
                    }
                    if(isUserUsedGiftCode(giftCodeModel.id,userName)){
                        return GiftCodeErrorCode.USER_USED;
                    }
                    // ban da su dung gift code nay roi
                    break;
                }
                case GiftCodeType.ONE_FOR_ONE_USER: {
                    break;
                }
                case GiftCodeType.ONE_FOR_ONE_USER_IN_EVENT: {
                    if(userUsedGiftCodeInEvent(giftCodeModel.event,userName)){
                        return GiftCodeErrorCode.USER_USED_IN_EVENT;
                    }
                    break;
                }
            }
         // validation
			int eventId = giftCodeModel.event;
			BaseResponse<String> isValid = validation(userName, ip, giftCodeModel.money, giftCodeModel.giftcode,
					userService, eventId, giftcodeType);
			if (!isValid.isSuccess()) {
				return Integer.valueOf(isValid.getErrorCode());
			}
			// insert
			insertUserUsedGiftCode(giftCodeModel.id, userName, giftCodeModel.event);
			updateNumberUsedGiftCode(giftCodeModel.id, giftCodeModel.time_used);
		} catch (Exception e) {
			Debug.trace(e);
			return GiftCodeErrorCode.WRONG_GIFT_CODE;
		}
        return GiftCodeErrorCode.SUCCESS;
    }
    private static BaseResponse<String> validation(String nickName,String ip, int money ,String giftCode ,UserService userService, int eventId , int giftcodeType) {
		String clientIp = "";
		if (ip != null && !"".equals(ip)) {
			String [] arrayIp = ip.split(",");
			clientIp = arrayIp[0].trim();
		}
		// sessionInfo, code);
		UserBonusService userBonusService = new UserBonusServiceImpl();
		double amount = money;
		UserBonusModel model = new UserBonusModel(nickName, eventId, amount, null, clientIp, "Khuyến mãi GIFTCODE EVENT"+eventId +" "+giftCode);
		if (userBonusService.isReceivedBonus(nickName, eventId) && giftcodeType == 0) {
			return new BaseResponse<String>(Constant.ERROR_SYSTEM, "Quý khách đã được nhận giftcode đợt này rồi");
		}
		if(userBonusService.isSameIP(clientIp,eventId)) {
			return new BaseResponse<String>(Constant.ERROR_SAMEIP, "Quý khách vui lòng nhận giftcode đợt sau !");
		}
		UserBankService userBankService = new UserBankServiceImpl();
		//check added bank
		if (!userBankService.isAddBank(nickName)) {
			return new BaseResponse<String>(Constant.ERROR_BANK_ADD, "Quý khách vui lòng thêm tài khoản ngân hàng để nhận giftcode ");
		}
		try {
			if (!userService.isXacThucSDT(nickName)) {
				return new BaseResponse<String>(Constant.ERROR_VERIFYPHONE, "Quý khách vui lòng xác thực SĐT để nhận giftcode");
			}
		} catch (Exception e2) {
			return new BaseResponse<String>(Constant.ERROR_PARAM, e2.getMessage());
		}
		try {
			userBonusService.insertBonus(model);
			return new BaseResponse<String>("0","Chúc mừng quý khách đã nhận được giftcode ",null);
		} catch (Exception e) {
			return new BaseResponse<String>(Constant.ERROR_PARAM, e.getMessage());
		}
	}
    
    public static boolean userUsedGiftCodeInEvent(int event, String userName) throws SQLException{
        boolean value = false;
        String sql = "SELECT * FROM gift_code_useds WHERE event=? AND username=?";
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            int param = 1;
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(param++, event);
            stm.setString(param++, userName);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                value = true;
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
        return value;
    }

    // co the dong bo bang cach khac
    public static synchronized void updateNumberUsedGiftCode(int giftcodeID, int currentTimeUse) throws SQLException {
        int timeInsert = currentTimeUse + 1;
        String sql = "UPDATE vinplay.gift_codes SET time_used=? WHERE id=?";

        try( Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setInt(param++, timeInsert);
            stm.setInt(param++, giftcodeID);
            stm.executeUpdate();
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    public static void insertUserUsedGiftCode(int giftcodeID, String userName, int event) throws SQLException {
        String sql = "INSERT INTO vinplay.gift_code_useds (giftcode_id,username,event)  VALUES (?,?,?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setInt(param++, giftcodeID);
            stm.setString(param++, userName);
            stm.setInt(param++, event);
            stm.executeUpdate();
            //stm.setTimestamp();
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    public static boolean isUserUsedGiftCode(int giftCodeID, String userName) throws SQLException{
        try( Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            String sql = "SELECT * FROM gift_code_useds WHERE giftcode_id=? AND username=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setInt(param++, giftCodeID);
            stm.setString(param++, userName);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                conn.close();
                return true;
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
        return false;
    }

    public static GiftCodeModel getGiftCode(String giftCode) {
        GiftCodeModel giftCodeModel = null;
        String sql = "SELECT * FROM vinplay.gift_codes WHERE giftcode=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, giftCode);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                giftCodeModel = parseGiftCodeModel(rs);
            }
            rs.close();
            stm.close();
        } catch (Exception e) {
            Debug.trace(e);
        }
        return giftCodeModel;
    }

    public static boolean giftCodeIsExits(String giftCode) {
        GiftCodeModel giftCodeModel = GiftCodeUtil.getGiftCode(giftCode);
        return giftCodeModel != null;
    }

    public static void insertGiftCode(GiftCodeModel giftCodeModel) throws SQLException {
    	String sql = "INSERT INTO gift_codes (giftcode,`type`,money,time_used,max_use,`from`,exprired,created_by,event,user_name)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setString(param++, giftCodeModel.giftcode);
            stm.setInt(param++, giftCodeModel.type);
            stm.setLong(param++, giftCodeModel.money);
            stm.setInt(param++, giftCodeModel.time_used);
            stm.setInt(param++, giftCodeModel.max_use);
            stm.setTimestamp(param++, giftCodeModel.from);
            stm.setTimestamp(param++, giftCodeModel.exprired);
            stm.setString(param++, giftCodeModel.created_by);
            stm.setInt(param++, giftCodeModel.event);
            stm.setString(param++, giftCodeModel.user_name);

            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            Debug.trace(e);
            throw e;
        }
    }

    public static GiftCodeModel parseGiftCodeModel(ResultSet rs) throws SQLException {
        GiftCodeModel giftCodeModel = new GiftCodeModel();

        giftCodeModel.id = rs.getInt("id");
        giftCodeModel.giftcode = rs.getString("giftcode");
        giftCodeModel.type = rs.getInt("type");
        giftCodeModel.money = rs.getInt("money");
        giftCodeModel.time_used = rs.getInt("time_used");
        giftCodeModel.max_use = rs.getInt("max_use");
        giftCodeModel.from = rs.getTimestamp("from");
        giftCodeModel.exprired = rs.getTimestamp("exprired");
        giftCodeModel.created_at = rs.getTimestamp("created_at");
        giftCodeModel.created_by = rs.getString("created_by");
        giftCodeModel.event = rs.getInt("event");
        giftCodeModel.user_name = rs.getString("user_name");

        return giftCodeModel;
    }
}
