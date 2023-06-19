package com.vinplay.usercore.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.vinplay.usercore.entities.AgentBank;

public interface AgentBankDao {
	public List<AgentBank> findAll();
	
	public AgentBank getById(long id);
	
	public AgentBank getByBankNumber(String bankNumber);
	
	public AgentBank getByBankCode(String agentCode, String bankCode);

	public boolean create(AgentBank agentBank);
	
	public boolean update(AgentBank agentBank);
	
	public boolean delete(long id);
	
	public Map<String, Object> search(String keyword, String agentCode, int pageIndex, int limit) throws SQLException;
}
