package com.vinplay.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import java.util.Base64;

/**
 * 加密解密
 * 
 * @author Donny
 *
 */
public class EncodeUtil {

	/**
	 * 数据库密钥
	 */
	public static String pwdInDb = "3.1415926";
	
	/**
	 * 本地密钥
	 */
	private static final String pwdInProgram = "finishjxf2012diven";
	

	
	/**
	 * 自定义解密
	 * @param pwd 加密密文
	 * @return 密码原文
	 * @throws Exception
	 */
	public static String customDecode(String pwd)throws Exception{

		int length = pwd.length();
		StringBuilder result = new StringBuilder();
		if (length%2>0) {
			result.append(pwd.charAt(length/2));
			result.append(pwd.substring(1, length/2));
			result.append(pwd.charAt(length-1));
			result.append(pwd.substring(length/2+1,length-1));
			result.append(pwd.charAt(0));
		}else {
			result.append(pwd.charAt(length/2-1));
			result.append(pwd.substring(1, length/2-1));
			result.append(pwd.charAt(0));
			result.append(pwd.charAt(length-1));
			result.append(pwd.substring(length/2+1,length-1));
			result.append(pwd.charAt(length/2));
		}
		
		return aesDecrypt(aesDecrypt(result.toString(), pwdInDb), pwdInProgram);
	}
	
	
	/**
	 * 自定义加密
	 * @param pwd 密码原文
	 * @return 加密后密文
	 * @throws Exception
	 */
	public static String customEncode(String pwd)throws Exception{
		String encoded = aesEncrypt(aesEncrypt(pwd, pwdInProgram), pwdInDb);
		
		int length = encoded.length();
		StringBuilder result = new StringBuilder();
		if (length%2>0) {
			result.append(encoded.charAt(length-1));
			result.append(encoded.substring(1, length/2));
			result.append(encoded.charAt(0));
			result.append(encoded.substring(length/2+1,length-1));
			result.append(encoded.charAt(length/2));
		}else {
			result.append(encoded.charAt(length/2-1));
			result.append(encoded.substring(1, length/2-1));
			result.append(encoded.charAt(0));
			result.append(encoded.charAt(length-1));
			result.append(encoded.substring(length/2+1,length-1));
			result.append(encoded.charAt(length/2));
		}
		return result.toString();
	}
	
	
	/** 
     * 将byte[]转为各种进制的字符串 
     * @param bytes byte[] 
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制 
     * @return 转换后的字符串 
     */  
    public static String binary(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }  
      
    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    public static String base64Encode(byte[] bytes){  
        return Base64.getEncoder().encodeToString(bytes);  
    }  
      
    /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return StringUtils.isEmpty(base64Code) ? null : Base64.getDecoder().decode(base64Code);  
    }  
      
    /** 
     * 获取byte[]的md5值 
     * @param bytes byte[] 
     * @return md5 
     * @throws Exception 
     */  
    public static byte[] md5(byte[] bytes) throws Exception {  
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(bytes);  
          
        return md.digest();  
    }  
      
    /** 
     * 获取字符串md5值 
     * @param msg  
     * @return md5 
     * @throws Exception 
     */  
    public static byte[] md5(String msg) throws Exception {  
        return StringUtils.isEmpty(msg) ? null : md5(msg.getBytes());  
    }  
      
    /** 
     * 结合base64实现md5加密 
     * @param msg 待加密字符串 
     * @return 获取md5后转为base64 
     * @throws Exception 
     */  
    public static String md5Encrypt(String msg) throws Exception{  
        return StringUtils.isEmpty(msg) ? null : base64Encode(md5(msg));  
    }  
      
    /** 
     * AES加密 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128, new SecureRandom(encryptKey.getBytes()));  
  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
          
        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
      
    /** 
     * AES加密为base 64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    public static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
      
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128, new SecureRandom(decryptKey.getBytes()));  
          
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
          
        return new String(decryptBytes);  
    }  
      
    /** 
     * 将base 64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }
}
