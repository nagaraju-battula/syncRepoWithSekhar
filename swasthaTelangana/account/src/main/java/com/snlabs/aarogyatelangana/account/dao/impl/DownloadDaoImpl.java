package com.snlabs.aarogyatelangana.account.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snlabs.aarogyatelangana.account.beans.Form;
import com.snlabs.aarogyatelangana.account.beans.Patient;
import com.snlabs.aarogyatelangana.account.beans.User;
import com.snlabs.aarogyatelangana.account.dao.DownloadDao;
import com.snlabs.aarogyatelangana.account.utils.CompleteDetailsRowMapper;
import com.snlabs.aarogyatelangana.account.utils.FormRowMapper;
import com.snlabs.aarogyatelangana.account.utils.PatientRowMapper;

public class DownloadDaoImpl implements DownloadDao{

	private DataSource dataSource;
	
	@Override
	public File downloadExcelForm(HttpServletRequest request,HttpSession session) {
		String fileId = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM demo.t_form WHERE ");
		if(session.getAttribute("formId") != null){
			fileId = String.valueOf(session.getAttribute("formId"));
			queryBuilder.append("F_FORM_ID = ").append(fileId);
		}else{
			fileId = String.valueOf(session.getAttribute("formId"));
			queryBuilder.append("F_FORM_ID = ").append(fileId);
		}
		String filePath = "/resources/F_"+fileId+".xls";
		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("");
		System.out.println(" application path: "+appPath);
		String fullPath = appPath + filePath;
		File downloadFile = new File(fullPath);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);			
		try{
			List<User> detailsList = jdbcTemplate.queryForObject(queryBuilder.toString(), new FormRowMapper());	
			System.out.println(" details Form Size "+detailsList.size());
			return prepareExcelSheet(detailsList,fullPath,downloadFile);
		}catch(Exception e){
			e.printStackTrace();
		}	
		return null;
	}

	private File prepareExcelSheet(List<User> detailsList, String fullPath, File downloadFile) {
		try{
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Details");
		int rownum=0;	
		Form form = null;
		Patient pe = null;
		//setWorkBookStyles(workBook);			
		prepareHeader(sheet.createRow(rownum),workBook);
		Row row = sheet.createRow(++rownum);
		for(User user:detailsList){
			int colnum =0;
			Cell cell = null;				
			if(user instanceof Patient){					
				pe = (Patient) user;					
				cell = row.createCell(colnum);
				cell.setCellValue((String)pe.getPatientName());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)pe.getPatientAddress());
				cell = row.createCell(++colnum);
				cell.setCellValue((Integer)pe.getPrice());
				cell = row.createCell(++colnum);
				cell.setCellValue((Integer)pe.getDiscount());
				cell = row.createCell(++colnum);
				cell.setCellValue((Integer)pe.getNetAmount());
			}
			if(user instanceof Form){
				form = (Form) user;
				cell = row.createCell(colnum);
				cell.setCellValue((String)form.getPatientName());
				cell = row.createCell(++colnum);
				cell.setCellValue((Integer)form.getAge());
				cell = row.createCell(++colnum);
				cell.setCellValue((Integer)form.getNoOfChildren());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)form.getGuardianName());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)form.getPatientAddress());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)form.getReferralAddress());
				cell = row.createCell(++colnum);
				cell.setCellValue((Integer)form.getMenstrualPeriod());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)form.getMedicalDisease());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)form.getParentalDiagnosis());
				cell = row.createCell(++colnum);
				cell.setCellValue((String)form.getGynecologistDetails());
			}				
		}			
		FileOutputStream fos = new FileOutputStream(new File(fullPath));
		workBook.write(fos);
		fos.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	return downloadFile;
	}

	@Override
	public File downloadExcelPatient(HttpServletRequest request,HttpSession session) {
		String fileId = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM demo.t_patient WHERE ");
		if(session.getAttribute("patientId") != null){
			fileId = (String)session.getAttribute("patientId");
			queryBuilder.append("F_PATIENT_ID = ").append(fileId);
		}else{
			fileId = (String)session.getAttribute("patientName");
			queryBuilder.append("F_PATIENT_NAME = '").append(fileId).append("'");
		}
		String filePath = "/resources/P_"+fileId+".xls";
		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("");
		String fullPath = appPath + filePath;
		File downloadFile = new File(fullPath);
		System.out.println("FULL  path: "+fullPath);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);			
		try{
			List<User> detailsList = jdbcTemplate.queryForObject(queryBuilder.toString(), new PatientRowMapper());
			System.out.println("Size "+detailsList.size());	
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("Details");
			int rownum=0;	
			Form form = null;
			Patient pe = null;
			//setWorkBookStyles(workBook);			
			prepareHeader(sheet.createRow(rownum),workBook);
			Row row = sheet.createRow(++rownum);
			for(User user:detailsList){
				int colnum =0;
				Cell cell = null;				
				if(user instanceof Patient){					
					pe = (Patient) user;					
					cell = row.createCell(colnum);
					cell.setCellValue((String)pe.getPatientName());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)pe.getPatientAddress());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)pe.getPrice());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)pe.getDiscount());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)pe.getNetAmount());
				}
				if(user instanceof Form){
					form = (Form) user;
					cell = row.createCell(colnum);
					cell.setCellValue((String)form.getPatientName());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)form.getAge());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)form.getNoOfChildren());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getGuardianName());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getPatientAddress());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getReferralAddress());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)form.getMenstrualPeriod());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getMedicalDisease());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getParentalDiagnosis());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getGynecologistDetails());
				}				
			}			
			FileOutputStream fos = new FileOutputStream(new File(fullPath));
			workBook.write(fos);
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return downloadFile;
	}

	@Override
	public File downloadDetails(HttpServletRequest request,HttpSession session) {
		String filePath = "/resources/completeDetailsExcelReport.xls";
		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("");
		System.out.println(" application path: "+appPath);
		String fullPath = appPath + filePath;
		File downloadFile = new File(fullPath);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String detailsQuery = "SELECT * FROM demo.t_patient patient,"+
										  "t_form formf"+
										  " WHERE patient.f_form_id = formf.f_form_id";			
		try{
			List<User> detailsList = jdbcTemplate.queryForObject(detailsQuery, new CompleteDetailsRowMapper());
			System.out.println("Size "+detailsList.size());	
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("CompleteDetails");
			int rownum=0;	
			Form form = null;
			Patient pe = null;
			//setWorkBookStyles(workBook);			
			prepareHeader(sheet.createRow(rownum),workBook);
			Row row = sheet.createRow(++rownum);
			for(User user:detailsList){
				int colnum =0;
				Cell cell = null;				
				if(user instanceof Patient){					
					pe = (Patient) user;					
					cell = row.createCell(colnum);
					cell.setCellValue((String)pe.getPatientName());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)pe.getPatientAddress());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)pe.getPrice());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)pe.getDiscount());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)pe.getNetAmount());
				}
				if(user instanceof Form){
					form = (Form) user;
					cell = row.createCell(colnum);
					cell.setCellValue((String)form.getPatientName());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)form.getAge());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)form.getNoOfChildren());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getGuardianName());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getPatientAddress());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getReferralAddress());
					cell = row.createCell(++colnum);
					cell.setCellValue((Integer)form.getMenstrualPeriod());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getMedicalDisease());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getParentalDiagnosis());
					cell = row.createCell(++colnum);
					cell.setCellValue((String)form.getGynecologistDetails());
				}				
			}			
			FileOutputStream fos = new FileOutputStream(new File(fullPath));
			workBook.write(fos);
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return downloadFile;
	}

	private void setWorkBookStyles(HSSFWorkbook wb) {
		try{
			HSSFFont defaultFont= wb.createFont();
		    defaultFont.setFontHeightInPoints((short)10);
		    defaultFont.setFontName("Arial");
		    //defaultFont.setColor(IndexedColors.BLACK.getIndex());
		    defaultFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    defaultFont.setItalic(false);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void prepareHeader(Row row, HSSFWorkbook workBook) {
		int colNum = 0;		
		CellStyle style=workBook.createCellStyle();
		try{
			row.createCell(colNum).setCellValue("PATIENT NAME");
			row.createCell(++colNum).setCellValue("PATIENT ADDRESS");
			row.createCell(++colNum).setCellValue("PRICE");
			row.createCell(++colNum).setCellValue("DISCOUNT");
			row.createCell(++colNum).setCellValue("NET AMOUNT");
			row.createCell(++colNum).setCellValue("PATIENT NAME");
			row.createCell(++colNum).setCellValue("AGE");
			row.createCell(++colNum).setCellValue("NO OF CHILDREN");
			row.createCell(++colNum).setCellValue("GURDIAN NAME");
			row.createCell(++colNum).setCellValue("PATIENT ADDRESS");
			row.createCell(++colNum).setCellValue("REFERRAL ADDRESS");
			row.createCell(++colNum).setCellValue("MENUSTRAL PERIOD");
			row.createCell(++colNum).setCellValue("MEDICAL DISEASE");
			row.createCell(++colNum).setCellValue("PARENTAL DIAGNOLYSIS");
			row.createCell(++colNum).setCellValue("GYNECOLOGIST ADDRESS");			
		    style.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
		    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		    style.setAlignment(CellStyle.ALIGN_CENTER);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
