package com.vinplay.service;

public interface GameDaoService {
	Integer getMaxFieldValue(String fieldName, String collectionName);

	Integer countUserRemain(String nickName, String collectionName);

	String findGameUserIdByNickName(String idName, String nickName, String collectionName);

	String findNickNameByGameUserId(String idKey, String idValue, String collectionName);

	String getLastUpdateTime(String gameName);

	boolean updateLastTime(String gameName, String lastTime);
}
