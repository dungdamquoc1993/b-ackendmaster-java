package com.vinplay.payment.dao;

import com.vinplay.payment.entities.AgentTransaction;
import java.util.Map;

public interface AgentTransactionsDao {
	
	public long create(AgentTransaction model);
	
	public Boolean updateStatus(String id, int status, long fee, String description, String userApprove);
	
	public Boolean updateStatus(String id, int status, String description, String userApprove);
	
	public Boolean delete(String id, String description, String userApprove);
	
	public AgentTransaction getById(String Id);
	
	public Map<String, Object> search(String keyword, int status, String timeStart, String timeEnd, int page);
	
	public Map<String, Object> searchWithAgentCode(String agentCode, int status, String timeStart, String timeEnd, int page);
}

