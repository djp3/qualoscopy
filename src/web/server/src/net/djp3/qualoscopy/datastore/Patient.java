package net.djp3.qualoscopy.datastore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.CalendarCache;

public class Patient {
	private static final Random r = new Random(System.currentTimeMillis()-234134);
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Patient.class);
		}
		return log;
	}
	
	private static CalendarCache cc = new CalendarCache();
	
	public static Patient generateFakePatient() {
		Long patientID = r.nextLong();
		String medicalRecordID = String.format("MR_%05d",Math.abs(r.nextInt(99999)));
		String firstName;
		switch(Math.abs(r.nextInt(10))){
			case 0: firstName = "Tao";break;
			case 1: firstName = "Don";break;
			case 2: firstName = "Luke";break;
			case 3: firstName = "Hannah";break;
			case 4: firstName = "William";break;
			case 5: firstName = "Hoda";break;
			case 6: firstName = "Al";break;
			case 7: firstName = "Julie";break;
			case 8: firstName = "Julia";break;
			case 9: firstName = "John";break;
			default: firstName = "Marie";break;
		}
		String lastName;
		switch(Math.abs(r.nextInt(10))){
			case 0: lastName = "Wang";break;
			case 1: lastName = "Patterson";break;
			case 2: lastName = "Raus";break;
			case 3: lastName = "Park";break;
			case 4: lastName = "Karnes";break;
			case 5: lastName = "Anton-Culver";break;
			case 6: lastName = "Shapiro";break;
			case 7: lastName = "Smith";break;
			case 8: lastName = "Lupton";break;
			case 9: lastName = "Brock";break;
			default: lastName = "St. Claire";break;
		}
		String gender = (r.nextBoolean() ?"M":"F");
		try {
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
			Date date;
			date = sdf.parse("1960-01-01 00:00:00.000");
			long dateOfBirth = date.getTime() + Math.abs(r.nextInt(40*365*24*60*60))*1000; //40 years from 1960
			
			String tz = "America/Los_Angeles";
			
			cc.getCalendar(tz).setTimeInMillis(dateOfBirth);
			String dob = cc.getCalendar(tz).get(Calendar.DAY_OF_MONTH)+"/";
			dob = dob +cc.getCalendar(tz).get(Calendar.MONTH)+"/";
			dob = dob +cc.getCalendar(tz).get(Calendar.YEAR);
		
			date = sdf.parse("2015-07-01 00:00:00.000");
			long nextProcedure = date.getTime() + Math.abs(r.nextInt(90*24*60*60))*1000; //90 days from 7/1/25
			cc.getCalendar(tz).setTimeInMillis(nextProcedure);
			String np = cc.getCalendar(tz).get(Calendar.DAY_OF_MONTH)+"/";
			np = np +cc.getCalendar(tz).get(Calendar.MONTH)+"/";
			np = np +cc.getCalendar(tz).get(Calendar.YEAR);
			
			return new Patient(patientID, medicalRecordID, firstName, lastName, gender, dob, np);
		} catch (ParseException e) {
			getLog().error("Problem making fake patients:"+e);
		}
		return null;
	}
	
	
	Long patientID;
	String medicalRecordID;
	String firstName;
	String lastName;
	String gender;
	String dateOfBirth;
	String nextProcedure;
	
	/**
	 * 
	 * @param patientID, patient ID, internal unique
	 * @param medicalRecordID, medical Record ID
	 * @param firstName, first Name
	 * @param lastName, last Name
	 * @param gender, gender
	 * @param dateOfBirth, dateOfBirth
	 * @param nextProcedure, nextProcedure
	 */
	public Patient(Long patientID, String medicalRecordID, String firstName, String lastName, String gender, String dateOfBirth, String nextProcedure) {
		super();
		this.setPatientID(patientID);
		this.setMedicalRecordID(medicalRecordID);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setGender(gender);
		this.setDateOfBirth(dateOfBirth);
		this.setNextProcedure(nextProcedure);
	}
	
	public Long getPatientID() {
		return patientID;
	}

	public void setPatientID(Long patientID) {
		this.patientID = patientID;
	}

	public String getMedicalRecordID() {
		return medicalRecordID;
	}

	public void setMedicalRecordID(String medicalRecordID) {
		this.medicalRecordID = medicalRecordID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNextProcedure() {
		return nextProcedure;
	}

	public void setNextProcedure(String nextProcedure) {
		this.nextProcedure = nextProcedure;
	}

	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("mr_id",this.getMedicalRecordID());
		
		ret.put("first",this.getFirstName());
		
		ret.put("last",this.getLastName());
		
		ret.put("dob",this.getDateOfBirth());
		
		ret.put("gender", this.getGender());
		
		ret.put("next_procedure",this.getNextProcedure());
		return ret;
	}

	public void clearData() {
		this.setMedicalRecordID(null);
		this.setDateOfBirth(null);
		this.setFirstName(null);
		this.setGender(null);
		this.setLastName(null);
		this.setNextProcedure(null);
	}
	
	

	
}
