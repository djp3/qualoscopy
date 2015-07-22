package net.djp3.qualoscopy.datastore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minidev.json.JSONObject;

public class Procedure {
	
	public static final String acIDSyntax = "..*";
	public static final Pattern patternAcID = Pattern.compile(acIDSyntax);
	public static final String ERROR_PATTERN_FAIL_AC_ID = "Ac ID failed to match syntax: "+acIDSyntax;
	public static final String ERROR_PATTERN_FAIL_AC_ID_NULL = "Ac ID failed to match syntax: it was null";
	
	public static final String dosSyntax = "^[01]?[0-9][/][123]?[0-9][/][12][09][0-9][0-9] [0-2]?[0-9]:[0-5][0-9]$";
	public static final Pattern patternDOS = Pattern.compile(dosSyntax);
	public static final String ERROR_PATTERN_FAIL_DOS = "\"date_time_of_service\" did not conform to the expected syntax:"+dosSyntax;

	
	private static Random r = new Random();
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Procedure.class);
		}
		return log;
	}
	
	public static Procedure generateFakeProcedure() {
		Long _procedure_id = -1L;
		while(_procedure_id < 0){
			_procedure_id = r.nextLong();
		}
		String procedure_id = ""+_procedure_id;
		
		String ac_id = String.format("AC_%06d",r.nextInt(999999));
		String faculty;
		switch(r.nextInt(10)){
			case 0: faculty = "Dr. Wang";break;
			case 1: faculty = "Dr. Patterson";break;
			case 2: faculty = "Dr. Raus";break;
			case 3: faculty = "Dr. Park";break;
			case 4: faculty = "Dr. Karnes";break;
			case 5: faculty = "Dr. Anton-Culver";break;
			case 6: faculty = "Dr. Shapiro";break;
			case 7: faculty = "Dr. Smith";break;
			case 8: faculty = "Dr. Lupton";break;
			case 9: faculty = "Dr. Brock";break;
			default: faculty = "Dr. St. Claire";break;
		}
		boolean completed = r.nextBoolean();
		try {
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date;
			date = sdf.parse("2014-07-01 12:00");
			long dateTimeOfService = date.getTime() + Math.abs(r.nextInt(365*24*60*60))*1000; //365 days from 7/1/14
			Procedure procedure = new Procedure();
			procedure.setProcedureID(procedure_id);
			procedure.setAcID(ac_id);
			procedure.setDateTimeOfService(dateTimeOfService);
			procedure.setFacultyID(faculty);
			procedure.setCompleted(completed);
			return procedure;
		} catch (ParseException e) {
			getLog().error("Problem making fake patients:"+e);
		}
		return null;
	}
	
	public final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY HH:MM");
	
	String procedureID = null;
	String acID = null;
	Long dateTimeOfService = null;
	String facultyID = null;
	Boolean completed = null;
	
	
	public String getProcedureID() {
		return procedureID;
	}
	public void setProcedureID(String procedureID) {
		this.procedureID = procedureID;
	}
	public String getAcID() {
		return acID;
	}
	public String setAcID(String ac_id) {
		if(ac_id != null){
			if(!patternAcID.matcher(ac_id).matches()){
				return ERROR_PATTERN_FAIL_AC_ID;
			}
			else{
				this.acID = ac_id;
				return null;
			}
		}else{
			return ERROR_PATTERN_FAIL_AC_ID_NULL;
		}
	}
	
	public Long getDateTimeOfService() {
		return dateTimeOfService;
	}
	
	public String getDateTimeOfServiceString() {
		Date date = new Date(this.getDateTimeOfService());
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:MM");
		format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
		String formatted = format.format(date);
		return formatted;
	}
	
	public void setDateTimeOfService(Long dateTimeOfService) {
		this.dateTimeOfService = dateTimeOfService;
	}
	
	public String setDateTimeOfService(String dateTimeOfService) {
		if(!patternDOS.matcher(dateTimeOfService).matches()){
			return ERROR_PATTERN_FAIL_DOS;
		}
		else{
			try{
				long localdos = sdf.parse(dateTimeOfService).getTime();
				this.dateTimeOfService = localdos;
				return null;
			}
			catch(ParseException e){
				return ERROR_PATTERN_FAIL_DOS;
			}
		}
	}
	
	public String getFacultyID() {
		return facultyID;
	}
	
	public String setFacultyID(String facultyID) {
		this.facultyID = facultyID;
		return null; //Return error message if it doesn't match a pattern
	}
	public Boolean isCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("ac_id",this.getAcID());
		
		ret.put("faculty_id",this.getFacultyID());
		
		if(this.isCompleted() != null){
			ret.put("completed",this.isCompleted()?"true":"false");
		}
		else{
			ret.put("completed","null");
		}
		
		if(this.getDateTimeOfService() != null){
			ret.put("date_time_of_service",getDateTimeOfServiceString());
		}
		else{
			ret.put("date_time_of_service","null");
		}
		
		return ret;
	}
	
	public void clearData() {
		this.setAcID(null);
		this.setCompleted(null);
		this.setDateTimeOfService((Long)null);
		this.setFacultyID(null);
	}
	
	

}
