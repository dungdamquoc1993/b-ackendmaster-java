/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.usercore.service;

import java.util.Map;

import com.vinplay.usercore.entities.AgentBank;

public interface AgentBankService {
	public String create(AgentBank agentBank);
	
	public AgentBank getById(long id);
	
	public AgentBank getByBankNumber(String bankNumber);
	
	public AgentBank getByBankCode(String agentCode, String bankCode);
	
	public String update(AgentBank agentBank);
	
	public String Delete(long id);
	
	public Map<String, Object> search(String keyword, String agentCode, int pageIndex, int limit);
}

