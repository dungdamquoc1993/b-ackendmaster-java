package com.vinplay.usercore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.vinplay.payment.entities.Bank;
import com.vinplay.usercore.dao.AgentBankDao;
import com.vinplay.usercore.entities.AgentBank;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.pools.ConnectionPool;

public class AgentBankDaoImpl implements AgentBankDao {
	
	@Override
	public List<AgentBank> findAll() {
		List<AgentBank> agentBanks = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = "SELECT * FROM agentbanks ";
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				try{
					AgentBank agentBank = new AgentBank(rs);
					agentBanks.add(agentBank);
				}catch (Exception e) { }
			}
			
			return agentBanks;
		}catch (Exception e) {
			return new ArrayList<>();
		}finally {
			try {
				rs.close();
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
		
	}
	
	@Override
	public AgentBank getById(long id) {
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = "SELECT * FROM agentbanks where id=?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id);
			rs = stm.executeQuery();
			while (rs.next()) {
				try{
					AgentBank agentBank = new AgentBank(rs);
					return agentBank;
				}catch (Exception e) { return null; }
			}
			
			return null;
		}catch (Exception e) {
			return null;
		}
		finally {
			try {
				rs.close();
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
	}
	
	@Override
	public AgentBank getByBankNumber(String bankNumber) {
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = "SELECT * FROM agentbanks where and bank_number=? ";
			stm = conn.prepareStatement(sql);
			stm.setString(1, bankNumber);
			rs = stm.executeQuery();
			while (rs.next()) {
				try{
					AgentBank agentBank = new AgentBank(rs);
					return agentBank;
				}catch (Exception e) { return null; }
			}
			
			return null;
		}catch (Exception e) {
			return null;
		}
		finally {
			try {
				rs.close();
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
	}
	
	@Override
	public AgentBank getByBankCode(String agentCode, String bankCode) {
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = "SELECT * FROM agentbanks where agent_code=? and bank_code=? ";
			stm = conn.prepareStatement(sql);
			stm.setString(1, agentCode);
			stm.setString(2, bankCode);
			rs = stm.executeQuery();
			while (rs.next()) {
				try{
					AgentBank agentBank = new AgentBank(rs);
					return agentBank;
				}catch (Exception e) { return null; }
			}
			
			return null;
		}catch (Exception e) {
			return null;
		}
		finally {
			try {
				rs.close();
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
	}
	
	@Override
	public boolean create(AgentBank agentBank) {
		Bank bank = GameCommon.LIST_BANK_NAME.stream()
				  .filter(x -> agentBank.getBank_code().equals(x.getCode()))
				  .findAny()
				  .orElse(null);
		if(bank == null)
			return false;
		
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = " INSERT INTO agentbanks (agent_code,bank_acount,bank_code,bank_code,bank_number,bank_branch) VALUES (?,?,?,?,?,?) ";	
			stm = conn.prepareStatement(sql);
			stm.setString(1, agentBank.getAgent_code());
			stm.setString(2, agentBank.getBank_acount());
			stm.setString(3, agentBank.getBank_code());
			stm.setString(4, bank == null ? agentBank.getBank_code() : bank.getBank_name());
			stm.setString(5, agentBank.getBank_number());
			stm.setString(6, agentBank.getBank_branch());
			stm.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
		finally {
			try {
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
	}

	@Override
	public boolean update(AgentBank agentBank) {
		Bank bank = GameCommon.LIST_BANK_NAME.stream()
				  .filter(x -> agentBank.getBank_code().equals(x.getCode()))
				  .findAny()
				  .orElse(null);
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = " UPDATE agentbanks SET agent_code=?, bank_acount=?, bank_code=?, bank_name=?, bank_number=?, bank_branch=? WHERE id = ? ";
			stm = conn.prepareStatement(sql);
			stm.setString(1, agentBank.getAgent_code());
			stm.setString(2, agentBank.getBank_acount());
			stm.setString(3, agentBank.getBank_code());
			stm.setString(4, bank == null ? agentBank.getBank_code() : bank.getBank_name());
			stm.setString(5, agentBank.getBank_number());
			stm.setString(6, agentBank.getBank_branch());
			stm.setLong(7, agentBank.getId());
			stm.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
		finally {
			try {
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
	}
	
	@Override
	public boolean delete(long id) {
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
			String sql = " delete from  agentbanks  WHERE id = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id);
			stm.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
		finally {
			try {
				stm.close();
				conn.close();
			}catch (Exception e) { }
		}
	}

	@Override
	public Map<String, Object> search(String keyword, String agentCode, int pageIndex, int limit) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin")) {
			pageIndex = pageIndex < 1 ? 0 : pageIndex - 1;
			String paginateCondition = (pageIndex == -1 || limit == -1) ? "" : (" limit ?,?");
			String keywordCondition = "";
			if(!StringUtils.isBlank(keyword)) {
				keywordCondition = " and ("
						+ "(bank_acount like ?) or "
						+ "(bank_code like ?) or "
						+ "(bank_name like ?) or "
						+ "(bank_number like ?) or "
						+ "(bank_branch like ?) "
						+ ")";
			}
			
			String agentCodeCondition = "";
			if(!StringUtils.isBlank(agentCode))
				agentCodeCondition = " and (agent_code = ?)";
			
			String sql = "select * from agentbanks where (1=1)"
					+ keywordCondition
					+ agentCodeCondition
					+ " ORDER BY id" + paginateCondition;
			String sqlCount = "select count(id) total from agentbanks where (1=1)"
		  			+ keywordCondition
		  			+ agentCodeCondition;
			PreparedStatement stmt = conn.prepareStatement(sql);
			PreparedStatement stmtCount = conn.prepareStatement(sqlCount);
			int index = 1;
			if(!StringUtils.isBlank(keyword)) {
				for(int i = index; i < 6; i++) {
					stmt.setString(index, "%" + keyword + "%");
					stmtCount.setString(index, "%" + keyword + "%");
					index ++;
				}
			}
			
			if(!StringUtils.isBlank(agentCode)) {
				stmt.setString(index, agentCode);
				stmtCount.setString(index, agentCode);
				index++;
			}
			
			if (-1 != pageIndex && -1 != limit) {
				stmt.setInt(index++, pageIndex * limit);
				stmt.setInt(index++, limit);
			}
			
			List<AgentBank> agentBanks = new ArrayList<>();
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				try{
					AgentBank agentBank = new AgentBank(rs);
					agentBanks.add(agentBank);
				}catch (Exception e) { }
			}
			
			map.put("agentBanks", agentBanks);
			ResultSet rsCount = stmtCount.executeQuery();
			while (rsCount.next()) {
				map.put("total", rsCount.getObject("total") == null ? 0 : rsCount.getInt("total"));
			}
			
			rs.close();
			rsCount.close();
			stmt.close();
			stmtCount.close();
			if (conn != null) {
				conn.close();
			}

			return map;
		} catch (SQLException e) {
			map.put("agentBanks", new ArrayList<>());
			map.put("total", 0);
			return map;
		}
	}
}