package com.vinplay.usercore.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.vinplay.payment.dao.AgentTransactionsDao;
import com.vinplay.payment.dao.impl.AgentTransactionsDaoImpl;
import com.vinplay.payment.entities.AgentTransaction;
import com.vinplay.usercore.service.AgentTransactionsService;

public class AgentTransactionsServiceImpl implements AgentTransactionsService {
	private static final Logger logger = Logger.getLogger(AgentTransactionsServiceImpl.class);
	private AgentTransactionsDao dao = new AgentTransactionsDaoImpl();

	private String ValidateConfig(AgentTransaction transaction) {
		try {
			if (StringUtils.isBlank(transaction.AgentCode))
				return "Agent code can not empty";

			if (StringUtils.isBlank(transaction.AgentId))
				return "Bank account can not empty";

			if (StringUtils.isBlank(transaction.Nickname))
				return "Bank code can not empty";

			if (StringUtils.isBlank(transaction.Username))
				return "Bank number can not empty";
			
			if (transaction.Money < 0)
				return "Money is invalid";

			if (!StringUtils.isBlank(transaction.Id)) {
				AgentTransaction transCheck = dao.getById(transaction.Id);
				if (transCheck == null)
					return "Id is invalid";
			}

			return "success";
		} catch (Exception e) {
			logger.error("Error ValidateConfig: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String create(AgentTransaction transaction) {
		try {
			String result = "";
			result = ValidateConfig(transaction);
			if (!"success".equals(result))
				return result;

			return dao.create(transaction) > 0 ? "success" : "Create transactions failure";
		} catch (Exception e) {
			logger.error("Error create transaction of agent: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public AgentTransaction getById(String id) {
		try {
			return dao.getById(id);
		} catch (Exception e) {
			logger.error("GetById exception: " + e.getMessage());
			return null;
		}
	}

	@Override
	public String updateStatus(String id, int status, long fee, String description, String userApprove) {
		try {
			AgentTransaction transaction = dao.getById(id);
			if(transaction == null)
				return "Transaction is not exist";
			
			if(status < transaction.Status)
				return "Status of transaction is invalid";
			
			return dao.updateStatus(id, status, fee, description ,userApprove) == true ? "success" : "Update status of transaction failure";
		} catch (Exception e) {
			logger.error("Error update bank of agent: " + e.getMessage());
			return e.getMessage();
		}
	}
	
	@Override
	public String updateStatus(String id, int status, String description, String userApprove) {
		try {
			AgentTransaction transaction = dao.getById(id);
			if(transaction == null)
				return "Transaction is not exist";
			
			if(status < transaction.Status)
				return "Status of transaction is invalid";
			
			return dao.updateStatus(id, status, description, userApprove) == true ? "success" : "Update status of transaction failure";
		} catch (Exception e) {
			logger.error("Error update bank of agent: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String delete(String id, String description, String userApprove) {
		try {
			AgentTransaction transaction = dao.getById(id);
			if(transaction == null)
				return "Transaction is not exist";

			return dao.delete(id, description, userApprove) == true ? "success" : "Delete transaction failure";
		} catch (Exception e) {
			logger.error("Error delete bank of agent: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public Map<String, Object> search(String keyword, int status, String timeStart, String timeEnd, int page) {
		try {
			return dao.search(keyword, status, timeStart, timeEnd, page);
		} catch (Exception e) {
			logger.error("Error delete bank of agent: " + e.getMessage());
			return new HashMap<>();
		}
	}
	
	@Override
	public Map<String, Object> searchWithAgentCode(String agentCode, int status, String timeStart, String timeEnd, int page) {
		try {
			return dao.searchWithAgentCode(agentCode, status, timeStart, timeEnd, page);
		} catch (Exception e) {
			logger.error("Error delete bank of agent: " + e.getMessage());
			return new HashMap<>();
		}
	}
}
