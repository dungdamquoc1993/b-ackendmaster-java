/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.usercore.dao.impl.GameTourDaoImpl
 *  com.vinplay.vbee.common.gencode.CodeGenerator
 */
package com.vinplay.api.backend.utils;

import java.util.Random;

public class CommonUtils {
	public static String GenerateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }
	
	public static String GenerateRandomNumberString(int charLength) {
		char[] chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < charLength; i++) {
			sb.append(chars[rnd.nextInt(chars.length)]);
		}

		return sb.toString();
    }
	
	public static void main(String[] args) {
		System.out.println(GenerateRandomNumberString(5));
		System.out.println(GenerateRandomNumber(5));
	}
}

