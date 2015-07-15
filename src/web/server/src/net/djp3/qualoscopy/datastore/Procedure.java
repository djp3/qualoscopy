package net.djp3.qualoscopy.datastore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.minidev.json.JSONObject;

public class Procedure {
	
	String ac_id;
	Long dateOfService;
	String faculty;
	boolean completed;
	
	public Procedure(String ac_id, Long dateOfService, String faculty, boolean completed) {
		super();
		this.setAc_id(ac_id);
		this.setDateOfService(dateOfService);
		this.setFaculty(faculty);
		this.setCompleted(completed);
	}
	public String getAc_id() {
		return ac_id;
	}
	public void setAc_id(String ac_id) {
		this.ac_id = ac_id;
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
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("ac_id",this.getAc_id());
		
		ret.put("faculty",this.getFaculty());
		
		ret.put("completed",this.isCompleted()?"true":"false");
		
		Date date = new Date(this.getDateOfService());
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
		ret.put("dos",formatted);
		
		return ret;
	}
	
	

}
