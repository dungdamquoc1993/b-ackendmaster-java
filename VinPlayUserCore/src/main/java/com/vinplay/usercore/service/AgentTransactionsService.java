/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.usercore.service;

import java.util.Map;
import com.vinplay.payment.entities.AgentTransaction;

public interface AgentTransactionsService {
	public String create(AgentTransaction transaction);
	
	public AgentTransaction getById(String id);
	
	public String updateStatus(String id, int status, long fee, String description, String userApprove);
	
	public String updateStatus(String id, int status, String description, String userApprove);
	
	public String delete(String id, String description, String userApprove);
	
	public Map<String, Object> search(String keyword, int status, String timeStart, String timeEnd, int page);
	
	public Map<String, Object> searchWithAgentCode(String agentCode, int status, String timeStart, String timeEnd, int page);
}

