package com.vinplay.usercore.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.usercore.dao.AgentBankDao;
import com.vinplay.usercore.dao.impl.AgentBankDaoImpl;
import com.vinplay.usercore.entities.AgentBank;
import com.vinplay.usercore.service.AgentBankService;

public class AgentBankServiceImpl implements AgentBankService {
	private static final Logger logger = Logger.getLogger("user_core");
	private AgentBankDao dao = new AgentBankDaoImpl();
	private AgentDAO agentDao = new AgentDAOImpl();

	private String ValidateConfig(AgentBank agentBank) {
		try {
			if (StringUtils.isBlank(agentBank.getAgent_code()))
				return "Agent code can not empty";

			if (StringUtils.isBlank(agentBank.getBank_acount()))
				return "Bank account can not empty";

			if (StringUtils.isBlank(agentBank.getBank_code()))
				return "Bank code can not empty";

			if (StringUtils.isBlank(agentBank.getBank_number()))
				return "Bank number can not empty";

			if (agentDao.DetailUserAgentByCode(agentBank.getAgent_code()) == null) 
				return "Agent code is invalid";

			if (agentBank.getId() == 0) { // Create
				if (dao.getByBankNumber(agentBank.getBank_number()) != null)
					return "Bank number is exist";

				if (dao.getByBankCode(agentBank.getAgent_code(), agentBank.getBank_code()) != null)
					return "Bank code is exist";
			} else {// update
				AgentBank oldAgentBank = dao.getByBankNumber(agentBank.getBank_number());
				if (oldAgentBank != null && oldAgentBank.getId() != agentBank.getId())
					return "Bank number is exist";

				oldAgentBank = dao.getByBankCode(agentBank.getAgent_code(), agentBank.getBank_code());
				if (oldAgentBank != null && oldAgentBank.getId() != agentBank.getId())
					return "Bank code is exist";
			}

			return "success";
		} catch (Exception e) {
			logger.error("Error ValidateConfig: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String create(AgentBank agentBank) {
		try {
			String result = "";
			result = ValidateConfig(agentBank);
			if (!"success".equals(result))
				return result;

			return dao.create(agentBank) == true ? "success" : "Insert not success";
		} catch (Exception e) {
			logger.error("Error create bank of agent: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public AgentBank getById(long id) {
		try {
			return dao.getById(id);
		} catch (Exception e) {
			logger.error("GetById exception: " + e.getMessage());
			return null;
		}
	}

	@Override
	public AgentBank getByBankNumber(String bankNumber) {
		try {
			return dao.getByBankNumber(bankNumber);
		} catch (Exception e) {
			logger.error("GetByBankNumber exception: " + e.getMessage());
			return null;
		}
	}

	@Override
	public AgentBank getByBankCode(String agentCode, String bankCode) {
		try {
			return dao.getByBankCode(agentCode, bankCode);
		} catch (Exception e) {
			logger.error("GetByBankNumber exception: " + e.getMessage());
			return null;
		}
	}

	@Override
	public String update(AgentBank agentBank) {
		try {
			if (agentBank.getId() < 1)
				return "Id is invalid";
			String result = "";
			result = ValidateConfig(agentBank);
			if (!"success".equals(result))
				return result;

			return dao.update(agentBank) == true ? "success" : "Update not success";
		} catch (Exception e) {
			logger.error("Error update bank of agent: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String Delete(long id) {
		try {
			if (id < 1)
				return "Id is invalid";

			return dao.delete(id) == true ? "success" : "Delete not success";
		} catch (Exception e) {
			logger.error("Error delete bank of agent: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public Map<String, Object> search(String keyword, String agentCode, int pageIndex, int limit) {
		try {
			return dao.search(keyword, agentCode, pageIndex, limit);
		} catch (Exception e) {
			logger.error("Error delete bank of agent: " + e.getMessage());
			return new HashMap<>();
		}
	}
}
