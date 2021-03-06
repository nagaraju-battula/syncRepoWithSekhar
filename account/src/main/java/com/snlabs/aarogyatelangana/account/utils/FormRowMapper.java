package com.snlabs.aarogyatelangana.account.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.snlabs.aarogyatelangana.account.beans.Form;
import com.snlabs.aarogyatelangana.account.beans.User;

public class FormRowMapper implements RowMapper{
	Form form  = null;
	List<User> detailsList = new ArrayList<User>();
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		do{    
			    form= new Form();
				form.setPatientName(rs.getString("F_PATIENT_NAME"));
				form.setFormID(rs.getInt("F_FORM_ID"));
				form.setAge(rs.getInt("F_AGE"));
				form.setGuardianName(rs.getString("F_GUARDIAN_NAME"));
				form.setPatientAddress(rs.getString("F_PATIENT_ADDRESS"));
				form.setReferralAddress(rs.getString("F_REFERRAL_ADDRESS"));
				form.setMenstrualPeriod(rs.getInt("F_MENSTRUAL_PERIOD"));
				form.setMedicalDisease(rs.getString("F_MEDICAL_DISEASE"));
				form.setParentalDiagnosis(rs.getString("F_PARENTAL_DIAGNOSIS"));
				form.setGynecologistDetails(rs.getString("F_GYNA_DETAILS"));
				detailsList.add(form);
		}while(rs.next());
		return detailsList;
	}
}
