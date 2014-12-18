package com.snlabs.aarogyatelangana.account.dao.impl;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snlabs.aarogyatelangana.account.beans.LoginUser;
import com.snlabs.aarogyatelangana.account.beans.NewUser;
import com.snlabs.aarogyatelangana.account.beans.UserDetails;
import com.snlabs.aarogyatelangana.account.controller.LoginController;
import com.snlabs.aarogyatelangana.account.dao.AccountDao;
import com.snlabs.aarogyatelangana.account.utils.UserDetailsRowMapper;

/**
 * @author nbattula
 * 
 */
public class AccountDaoImpl implements AccountDao {

	static Logger LOGGER = LoggerFactory.getLogger(AccountDaoImpl.class);
	
	private DataSource dataSource;

	public AccountDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public UserDetails getAccountDetails(LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("DTL.F_LOGIN_ID, ");
		sb.append("DTL.F_DISPLAY_NAME, ");
		sb.append("DTL.F_MOBILE_NUMBER, ");
		sb.append("ADDS.F_DISTRICT, ");
		sb.append("ADDS.F_STATE, ");
		sb.append("RLS.F_ROLE ");
		sb.append("FROM ");
		sb.append("DEMO.T_USER_DETAILS DTL, ");
		sb.append("DEMO.T_USER_ADDRESS ADDS, ");
		sb.append("DEMO.T_USER_ROLES RLS ");
		sb.append("WHERE ");
		sb.append("DTL.F_LOGIN_ID = ADDS.F_LOGIN_ID ");
		sb.append("AND DTL.F_LOGIN_ID = RLS.F_LOGIN_ID ");
		sb.append("AND DTL.F_IS_ENABLED = 'true' ");
		sb.append("AND DTL.F_LOGIN_ID = ? ");
		sb.append("AND DTL.F_PASSWORD = ? ");

		String query = sb.toString();

		Object[] args = new Object[] { user.getUserName(), user.getPassword() };

		try {

			@SuppressWarnings("unchecked")
			UserDetails userDetails = (UserDetails) jdbcTemplate
					.queryForObject(query, args, new UserDetailsRowMapper());
			return userDetails;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean createAccount(NewUser user, UserDetails userDetails) {

		String queryDtl = "INSERT INTO DEMO.T_USER_DETAILS (F_LOGIN_ID, F_PASSWORD, F_IS_ENABLED, F_DISPLAY_NAME, F_MOBILE_NUMBER, F_CREATED_BY, F_CREATION_DATE) VALUES (?, ?, ?, ?, ?, ?, SYSDATE())";
		Object[] argsDtl = new Object[] { user.getLoginId(), user.getPassword(), new String("true"),
				user.getDisplayName(), user.getMobileNumber(), userDetails.getLoginId() };
		
		String queryAddress = "INSERT INTO DEMO.T_USER_ADDRESS(F_LOGIN_ID,F_DISTRICT,F_STATE)VALUES(?,?,?)";
		Object[] argsAddress = new Object[] { user.getLoginId(), user.getDistrict(), user.getState()};
		
		String queryRole = "INSERT INTO DEMO.T_USER_ROLES(F_LOGIN_ID,F_ROLE)VALUES(?,?)";
		Object[] argsRole = new Object[] { user.getLoginId(), user.getUserRole()};
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		try {
			int outDtl = jdbcTemplate.update(queryDtl, argsDtl);
			int outAddress = jdbcTemplate.update(queryAddress, argsAddress);
			int outRole = jdbcTemplate.update(queryRole, argsRole);

			if (outDtl != 0 && outAddress != 0 && outRole != 0) {
				LOGGER.info("Account saved with id=" + user.getLoginId());
				return true;
			} else {
				LOGGER.info("Account save failed with id = "+ user.getLoginId());
				return false;
			}
		} catch (Exception e) {
			LOGGER.info("Account save failed with  exception id = "+ user.getLoginId());
			return false;
		}

	}

	@Override
	public boolean updateAccount(NewUser user) {
		// TODO Auto-generated method stub
		String query = "UPDATE DEMO.T_USER_DETAILS SET F_PASSWORD = ? WHERE F_LOGIN_ID = ?";

		Object[] args = new Object[] { user.getPassword(), user.getLoginId() };

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		int out = jdbcTemplate.update(query, args);

		if (out != 0) {
			LOGGER.info("Existing account saved with id="
					+ user.getLoginId());
			return true;
		} else {
			LOGGER.info("Existing account save failed with id="
					+ user.getLoginId());
			return false;
		}
	}

}
