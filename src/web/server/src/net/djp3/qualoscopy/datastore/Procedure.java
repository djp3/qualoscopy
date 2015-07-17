package net.djp3.qualoscopy.datastore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minidev.json.JSONObject;

public class Procedure {
	
	private static Random r = new Random();
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Procedure.class);
		}
		return log;
	}
	
	public static Procedure generateFakeProcedure() {
		Long procedure_id = r.nextLong();
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
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
			Date date;
			date = sdf.parse("2014-07-01 00:00:00.000");
			long dateOfService = date.getTime() + Math.abs(r.nextInt(365*24*60*60))*1000; //365 days from 7/1/14
			return new Procedure(procedure_id, ac_id, dateOfService,faculty, completed);
		} catch (ParseException e) {
			getLog().error("Problem making fake patients:"+e);
		}
		return null;
	}
	
	Long procedureID;
	String acID;
	Long dateOfService;
	String faculty;
	Boolean completed;
	
	public Procedure(Long procedureID, String acID, Long dateOfService, String faculty, Boolean completed) {
		super();
		this.setProcedureID(procedureID);
		this.setAcID(acID);
		this.setDateOfService(dateOfService);
		this.setFaculty(faculty);
		this.setCompleted(completed);
	}
	public Long getProcedureID() {
		return procedureID;
	}
	public void setProcedureID(Long procedureID) {
		this.procedureID = procedureID;
	}
	public String getAcID() {
		return acID;
	}
	public void setAcID(String ac_id) {
		this.acID = ac_id;
	}
	public Long getDateOfService() {
		return dateOfService;
	}
	public void setDateOfService(Long dateOfService) {
		this.dateOfService = dateOfService;
	}
	public String getFaculty() {
		return faculty;
	}
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("ac_id",this.getAcID());
		
		ret.put("faculty",this.getFaculty());
		
		ret.put("completed",this.isCompleted()?"true":"false");
		
		Date date = new Date(this.getDateOfService());
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
		ret.put("dos",formatted);
		
		return ret;
	}
	public void clearData() {
		this.setAcID(null);
		this.setCompleted(null);
		this.setDateOfService(null);
		this.setFaculty(null);
	}
	
	

}
