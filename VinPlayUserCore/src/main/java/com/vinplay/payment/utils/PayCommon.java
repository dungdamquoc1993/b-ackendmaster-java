package com.vinplay.payment.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class PayCommon {
	/**
	 * Define enumeration status
	 * 
	 * @author vuanhtuan
	 *
	 */
	public enum PAYSTATUS {
		/*
		 * Pending process order
		 */
		PENDING(0, "pending", "Dang cho xu ly"),
		/*
		 * Received and processing order
		 */
		RECEIVED(1, "received", "Da nhan va dang xu ly"),
		/*
		 * Order has processed success and wait add point to user
		 */
		SUCCESS(2, "success", "Da xu ly thanh cong"),
		/*
		 * Order has processed fail
		 */
		FAILED(3, "failed", "Da xu ly that bai"),
		/*
		 * Order has processed complete and added point to user
		 */
		COMPLETED(4, "completed", "Giao dich hoan tat"),
		/*
		 * Order is reviewing
		 */
		REVIEW(5, "review", "Dang xem xet"),
		
		/*
		 * Request too many times
		 */
		SPAM(11, "spam", "Yeu cau bi gui qua nhieu lan"),
		
		REQUEST(12, "request", "Yeu cau rut tien");

		private Integer id;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		private String key;
		private String alias;

		/**
		 * constructor
		 * 
		 * @param id
		 * @param key
		 * @param alias
		 */
		private PAYSTATUS(Integer id, String key, String alias) {
			this.id = id;
			this.key = key;
			this.alias = alias;
		}

		public static PAYSTATUS getById(Integer id) {
			for (PAYSTATUS payStatus : PAYSTATUS.values()) {
				if (payStatus.getId().equals(id)) {
					return payStatus;
				}
			}

			return null;
		}

		public static PAYSTATUS getByKey(String key) {
			for (PAYSTATUS payStatus : PAYSTATUS.values()) {
				if (payStatus.getKey().equals(key)) {
					return payStatus;
				}
			}

			return null;
		}
	}
	
	/**
	 * Hash string using MD5
	 * @param input
	 * @return String hased
	 */
	public static String getMd5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * Hash string using HmacSHA1
	 * @param value
	 * @param key
	 * @return String hased
	 */
	private String getHMACSHA1(String value, String key) throws Exception {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(value.getBytes());
            byte[] hexBytes = new Hex().encode(rawHmac);
            return new String(hexBytes, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
        return "";
    }
	
	/**
	 * Hash string using HmacSHA256
	 * @param key
	 * @param data
	 * @return String hased
	 * @throws Exception
	 */
	public static String getHMACSHA256(String key, String data) throws Exception {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("ASCII"), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("ASCII")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
