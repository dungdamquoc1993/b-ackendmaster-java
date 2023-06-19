package com.vinplay.usercore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vinplay.payment.entities.Bank;
import com.vinplay.usercore.dao.BankDao;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.DateTimeUtils;

public class BankDaoImpl implements BankDao {
	
	@Override
	public List<Bank> findAll() {
		List<Bank> lstBank = new ArrayList<>();
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
			String sql = "SELECT * FROM banks WHERE 1=1 ";
			PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				Bank bank = new Bank(rs);
				bank.setAddby("");
				if (bank.getStatus() == 1) {// active
					lstBank.add(bank);
				}
			}
			rs.close();
			stm.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return lstBank;
	}
	
	@Override
	public boolean addBank(Bank bank) {
		String sql = " INSERT INTO banks (bank_name,status,create_date,code,logo,add_by)  VALUES  (?, ?, ?, ?, ? ,?) ";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
				PreparedStatement stm = conn.prepareStatement(sql);) {
			stm.setString(1, bank.getBank_name());
			stm.setInt(2, bank.getStatus());
			stm.setString(3, DateTimeUtils.getCurrentDateTime());
			stm.setString(4, bank.getCode());
			stm.setString(5, bank.getLogo());
			stm.setString(6, bank.getAddby());
			stm.executeUpdate();
			//add to cached
			GameCommon.LIST_BANK_NAME.add(bank);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public boolean editBank(Bank bank) {
		String sql = " UPDATE banks SET bank_name = ?,  status = ?,  code = ?,  logo = ?,  update_date = ? , add_by=?  WHERE id = ? ";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
				PreparedStatement stm = conn.prepareStatement(sql);) {
			stm.setString(1, bank.getBank_name());
			stm.setInt(2, bank.getStatus());
			stm.setString(3, bank.getCode());
			stm.setString(4, bank.getLogo());
			stm.setString(5, DateTimeUtils.getCurrentDateTime());
			stm.setString(6, bank.getAddby());
			stm.setLong(7, bank.getId());
			stm.executeUpdate();
			//update cached
			for (Bank bankCa : GameCommon.LIST_BANK_NAME) {
				if(bankCa.getId()==bank.getId()) {
					GameCommon.LIST_BANK_NAME.remove(bankCa);
					GameCommon.LIST_BANK_NAME.add(bank);
					break;
				}
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean deleteBank(long id) {
		String sql = " delete from  banks  WHERE id = ? ";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
				PreparedStatement stm = conn.prepareStatement(sql);) {
			stm.setLong(1, id);
			stm.executeUpdate();
			// update cached
			for (Bank bank : GameCommon.LIST_BANK_NAME) {
				if (bank.getId() == id) {
					GameCommon.LIST_BANK_NAME.remove(bank);
					break;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Bank> search(String bankName, String bankCode, int page, int totalrecord) throws SQLException {
		List<Bank> lstBank = new ArrayList<>();
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
			String sql = "SELECT * FROM banks WHERE 1=1 ";
			int num_start = (page - 1) * totalrecord;
			int index = 1;
			String limit = " LIMIT " + num_start + ", " + totalrecord + "";

			if (bankName != null && !"".equals(bankName)) {
				sql += " and bank_name like ?";
			}
			if (bankCode != null && !"".equals(bankCode)) {
				sql += " and code like ?";
			}

			sql = sql + " order by id DESC" + limit;

			PreparedStatement stm = conn.prepareStatement(sql);

			if (bankName != null && !"".equals(bankName)) {
				stm.setString(index, '%' + bankName + '%');
				++index;
			}
			if (bankCode != null && !"".equals(bankCode)) {
				stm.setString(index, '%' + bankCode + '%');
				++index;
			}

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				Bank bank = new Bank(rs);
				lstBank.add(bank);
			}
			rs.close();
			stm.close();
		}
		return lstBank;
	}

}
