package net.djp3.qualoscopy.datastore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.minidev.json.JSONObject;

public class Patient {
	String medicalRecordID;
	String firstName;
	String lastName;
	String gender;
	Long dateOfBirth;
	Long nextProcedure;
	
	public Patient(String medicalRecordID, String firstName, String lastName, String gender, Long dateOfBirth, Long nextProcedure) {
		super();
		this.setMedicalRecordID(medicalRecordID);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setGender(gender);
		this.setDateOfBirth(dateOfBirth);
		this.setNextProcedure(nextProcedure);
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

	public Long getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Long getNextProcedure() {
		return nextProcedure;
	}

	public void setNextProcedure(Long nextProcedure) {
		this.nextProcedure = nextProcedure;
	}

	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("mrid",this.getMedicalRecordID());
		
		ret.put("last",this.getLastName());
		
		Date date = new Date(this.getDateOfBirth());
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
		ret.put("dob",formatted);
		
		ret.put("gender", this.getGender());
		
		date = new Date(this.getNextProcedure());
        formatted = format.format(date);
		ret.put("next_procedure",formatted);
		return ret;
	}
	
	

	
}
