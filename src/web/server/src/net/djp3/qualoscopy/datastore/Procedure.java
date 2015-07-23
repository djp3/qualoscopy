package net.djp3.qualoscopy.datastore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.CalendarCache;

public class Procedure {
	
	public static final String acIDSyntax = "..*";
	public static final Pattern patternAcID = Pattern.compile(acIDSyntax);
	public static final String ERROR_PATTERN_FAIL_AC_ID = "Ac ID failed to match syntax: "+acIDSyntax;
	public static final String ERROR_PATTERN_FAIL_AC_ID_NULL = "Ac ID failed to match syntax: it was null";
	
	public static final String dosSyntax = "^[01]?[0-9][/][123]?[0-9][/][12][09][0-9][0-9] [0-2]?[0-9]:[0-5][0-9]$";
	public static final Pattern patternDOS = Pattern.compile(dosSyntax);
	public static final String ERROR_PATTERN_FAIL_DOS = "\"date_time_of_service\" did not conform to the expected syntax:"+dosSyntax;

	
	private static Random random = new Random();
	
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
			_procedure_id = random.nextLong();
		}
		String procedure_id = ""+_procedure_id;
		
		String ac_id = String.format("AC_%06d",random.nextInt(999999));
		String faculty;
		switch(random.nextInt(10)){
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
		
		String location = "UCI-"+random.nextInt(9999);
		String fellow = "Dr. Patterson-"+random.nextInt(99);
		String pre_drug = "Novacaine-"+random.nextInt(99);
		String prep_liters = ""+random.nextInt(10);
		String split_prep = random.nextBoolean()?"Y":"N";
		String bisacodyl = random.nextBoolean()?"Y":"N";
		String last_colon = ""+random.nextInt(10);
		String primary_indication = "foo-"+random.nextInt(99);
		String other_indication = "foo-"+random.nextInt(99);
		String scope = "foo-"+random.nextInt(99);
		String endocuff = random.nextBoolean()?"Y":"N";
		String cap_assisted = random.nextBoolean()?"Y":"N";
		String underwater = random.nextBoolean()?"Y":"N";
		String sedation_level = random.nextBoolean()?"unsedated":"moderate"; //or "ga"
		String versed = ""+random.nextInt(10);
		String fentanyl = ""+random.nextInt(10);
		String demerol = ""+random.nextInt(10);
		String benadryl = ""+random.nextInt(10);
		String extent = ""+random.nextInt(10);
		String prep_quality_left = ""+random.nextInt(4);
		String prep_quality_mid = ""+random.nextInt(4);
		String prep_quality_right = ""+random.nextInt(4);
		String time_insertion = random.nextInt(24)+":"+random.nextInt(60);
		String time_begin_withdrawal = random.nextInt(24)+":"+random.nextInt(60);
		String time_scope_withdrawn = random.nextInt(24)+":"+random.nextInt(60);
		try {
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date;
			date = sdf.parse("2014-07-01 12:00");
			long dateTimeOfService = date.getTime() + Math.abs(random.nextInt(365*24*60*60))*1000; //365 days from 7/1/14
			Procedure procedure = new Procedure();
			procedure.setProcedureID(procedure_id);
			procedure.setAcID(ac_id);
			procedure.setDateTimeOfService(dateTimeOfService);
			procedure.setFacultyID(faculty);
			procedure.setLocation(location);
			procedure.setFellow(fellow);
			procedure.setPreDrug(pre_drug);
			procedure.setPrepLiters(prep_liters);
			procedure.setSplitPrep(split_prep);
			procedure.setBisacodyl(bisacodyl);
			procedure.setLastColon(last_colon);
			procedure.setPrimaryIndication(primary_indication);
			procedure.setOtherIndication(other_indication);
			procedure.setScope(scope);
			procedure.setEndocuff(endocuff);
			procedure.setCapAssisted(cap_assisted);
			procedure.setUnderwater(underwater);
			procedure.setSedationLevel(sedation_level);
			procedure.setVersed(versed);
			procedure.setFentanyl(fentanyl);
			procedure.setDemerol(demerol);
			procedure.setBenadryl(benadryl);
			procedure.setExtent(extent);
			procedure.setPrepQualityLeft(prep_quality_left);
			procedure.setPrepQualityMid(prep_quality_mid);
			procedure.setPrepQualityRight(prep_quality_right);
			procedure.setTimeInsertionString(time_insertion);
			procedure.setTimeBeginWithdrawalString(time_begin_withdrawal);
			procedure.setTimeScopeWithdrawnString(time_scope_withdrawn);


			
			
			return procedure;
		} catch (ParseException e) {
			getLog().error("Problem making fake patients:"+e);
		}
		return null;
	}
	
	public final SimpleDateFormat sdf;
	public final SimpleDateFormat shortSdf;
	
	String procedureID = null;
	String acID = null;
	Long dateTimeOfService = null;
	String facultyID = null;
	String location = null;
	String fellow = null;
	String pre_drug = null;
	String prep_liters = null;
	String split_prep = null;
	String bisacodyl = null;
	String last_colon = null;
	String primary_indication = null;
	String other_indication = null;
	String scope = null;
	String endocuff = null;
	String cap_assisted = null;
	String underwater = null;
	String sedation_level = null;
	String versed = null;
	String fentanyl = null;
	String demerol = null;
	String benadryl = null;
	String extent = null;
	String prep_quality_left = null;
	String prep_quality_mid = null;
	String prep_quality_right = null;
	Long time_insertion = null;
	Long time_begin_withdrawal = null;
	Long time_scope_withdrawn = null;
	
	public Procedure(){
		super();
		sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		sdf.setTimeZone(CalendarCache.TZ_GMT);
		
		shortSdf = new SimpleDateFormat("HH:mm");
		shortSdf.setTimeZone(CalendarCache.TZ_GMT);
	}
	
	
	public String getProcedureID() {
		return procedureID;
	}
	public void setProcedureID(String procedureID) {
		this.procedureID = procedureID;
	}
	public String getAcID() {
		return acID;
	}
	
	public void setAcIDNoChecks(String ac_id) {
		this.acID = ac_id;
	}
	
	public String setAcID(String ac_id) {
		if(ac_id != null){
			if(!patternAcID.matcher(ac_id).matches()){
				return ERROR_PATTERN_FAIL_AC_ID;
			}
			else{
				setAcIDNoChecks(ac_id);
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
		Long dtos = this.getDateTimeOfService();
		Date date = new Date(dtos);
		String formatted = sdf.format(date);
		return formatted;
	}
	
	public String setDateTimeOfService(Long dateTimeOfService) {
		this.dateTimeOfService = dateTimeOfService;
		return null; //Return error message if it doesn't match a pattern
	}
	
	public String setDateTimeOfService(String dateTimeOfService) {
		if(!patternDOS.matcher(dateTimeOfService).matches()){
			return ERROR_PATTERN_FAIL_DOS;
		}
		else{
			try{
				Date parse = sdf.parse(dateTimeOfService);
				long localdos = parse.getTime();
				this.dateTimeOfService = localdos;
				return null; //Return error message if it doesn't match a pattern
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
	
	
	
	

	public String getLocation() {
		return location;
	}

	public String setLocation(String location) {
		this.location = location;
		return null;
	}

	public String getFellow() {
		return fellow;
	}

	public String setFellow(String fellow) {
		this.fellow = fellow;
		return null;
	}

	public String getPreDrug() {
		return pre_drug;
	}

	public String setPreDrug(String pre_drug) {
		this.pre_drug = pre_drug;
		return null;
	}

	public String getPrepLiters() {
		return prep_liters;
	}

	public String setPrepLiters(String prep_liters) {
		this.prep_liters = prep_liters;
		return null;
	}

	public String getSplitPrep() {
		return split_prep;
	}

	public String setSplitPrep(String split_prep) {
		this.split_prep = split_prep;
		return null;
	}

	public String getBisacodyl() {
		return bisacodyl;
	}

	public String setBisacodyl(String bisacodyl) {
		this.bisacodyl = bisacodyl;
		return null;
	}

	public String getLastColon() {
		return last_colon;
	}

	public String setLastColon(String last_colon) {
		this.last_colon = last_colon;
		return null;
	}

	public String getPrimaryIndication() {
		return primary_indication;
	}

	public String setPrimaryIndication(String primary_indication) {
		this.primary_indication = primary_indication;
		return null;
	}

	public String getOtherIndication() {
		return other_indication;
	}

	public String setOtherIndication(String other_indication) {
		this.other_indication = other_indication;
		return null;
	}

	public String getScope() {
		return scope;
	}

	public String setScope(String scope) {
		this.scope = scope;
		return null;
	}

	public String getEndocuff() {
		return endocuff;
	}

	public String setEndocuff(String endocuff) {
		this.endocuff = endocuff;
		return null;
	}

	public String getCapAssisted() {
		return cap_assisted;
	}

	public String setCapAssisted(String cap_assisted) {
		this.cap_assisted = cap_assisted;
		return null;
	}

	public String getUnderwater() {
		return underwater;
	}

	public String setUnderwater(String underwater) {
		this.underwater = underwater;
		return null;
	}

	public String getSedationLevel() {
		return sedation_level;
	}

	public String setSedationLevel(String sedation_level) {
		this.sedation_level = sedation_level;
		return null;
	}

	public String getVersed() {
		return versed;
	}

	public String setVersed(String versed) {
		this.versed = versed;
		return null;
	}

	public String getFentanyl() {
		return fentanyl;
	}

	public String setFentanyl(String fentanyl) {
		this.fentanyl = fentanyl;
		return null;
	}

	public String getDemerol() {
		return demerol;
	}

	public String setDemerol(String demerol) {
		this.demerol = demerol;
		return null;
	}

	public String getBenadryl() {
		return benadryl;
	}

	public String setBenadryl(String benadryl) {
		this.benadryl = benadryl;
		return null;
	}

	public String getExtent() {
		return extent;
	}

	public String setExtent(String extent) {
		this.extent = extent;
		return null;
	}

	public String getPrepQualityLeft() {
		return prep_quality_left;
	}

	public String setPrepQualityLeft(String prep_quality_left) {
		this.prep_quality_left = prep_quality_left;
		return null;
	}

	public String getPrepQualityMid() {
		return prep_quality_mid;
	}

	public String setPrepQualityMid(String prep_quality_mid) {
		this.prep_quality_mid = prep_quality_mid;
		return null;
	}

	public String getPrepQualityRight() {
		return prep_quality_right;
	}

	public String setPrepQualityRight(String prep_quality_right) {
		this.prep_quality_right = prep_quality_right;
		return null;
	}

	public Long getTimeInsertion() {
		return time_insertion;
	}
	
	public String getTimeInsertionString() {
		Long local = this.getTimeInsertion();
		if(local != null){
			Date date = new Date(local);
			String formatted = shortSdf.format(date);
			return formatted;
		}
		return null;
	}

	public String setTimeInsertion(Long time_insertion) {
		this.time_insertion = time_insertion;
		return null;
	}
	
	public String setTimeInsertionString(String time_insertion) {
		try{
			Date parse = shortSdf.parse(time_insertion);
			long local = parse.getTime();
			this.time_insertion = local;
			return null; //Return error message if it doesn't match a pattern
		}
		catch(ParseException e){
			return "Internal Error";
		}
	}

	public Long getTimeBeginWithdrawal() {
		return time_begin_withdrawal;
	}
	
	public String getTimeBeginWithdrawalString() {
		Long local = this.getTimeBeginWithdrawal();
		if(local != null){
			Date date = new Date(local);
			String formatted = shortSdf.format(date);
			return formatted;
		}
		return null;
	}
	

	public String setTimeBeginWithdrawal(Long time_begin_withdrawal) {
		this.time_begin_withdrawal = time_begin_withdrawal;
		return null;
	}

	public String setTimeBeginWithdrawalString(String time_begin_withdrawal) {
		try{
			Date parse = shortSdf.parse(time_begin_withdrawal);
			long local = parse.getTime();
			this.time_begin_withdrawal = local;
			return null; //Return error message if it doesn't match a pattern
		}
		catch(ParseException e){
			return "Internal Error";
		}
	}
	

	public Long getTimeScopeWithdrawn() {
		return time_scope_withdrawn;
	}
	
	public String getTimeScopeWithdrawnString() {
		Long local = this.getTimeScopeWithdrawn();
		if(local != null){
			Date date = new Date(local);
			String formatted = shortSdf.format(date);
			return formatted;
		}
		return null;
	}

	public String setTimeScopeWithdrawn(Long time_scope_withdrawn) {
		this.time_scope_withdrawn = time_scope_withdrawn;
		return null;
	}
	
	public String setTimeScopeWithdrawnString(String time_scope_withdrawn) {
		try{
			Date parse = sdf.parse("01/01/1970 "+time_scope_withdrawn);
			long local = parse.getTime();
			this.time_scope_withdrawn = local;
			return null; //Return error message if it doesn't match a pattern
		}
		catch(ParseException e){
			return "Internal Error";
		}
	}


	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("procedure_id",""+this.getProcedureID());
		
		ret.put("ac_id",""+this.getAcID());
		
		ret.put("faculty_id",""+this.getFacultyID());
		
		//TODO: Figure out what makes a procedure completed
		ret.put("completed","false");
		ret.put("location",this.getLocation());
		ret.put("fellow",this.getFellow());
		ret.put("pre_drug",this.getPreDrug());
		ret.put("prep_liters",this.getPrepLiters());
		ret.put("split_prep",this.getSplitPrep());
		ret.put("bisacodyl",this.getBisacodyl());
		ret.put("last_colon",this.getLastColon());
		ret.put("primary_indication",this.getPrimaryIndication());
		ret.put("other_indication",this.getOtherIndication());
		ret.put("scope",this.getScope());
		ret.put("endocuff",this.getEndocuff());
		ret.put("cap_assisted",this.getCapAssisted());
		ret.put("underwater",this.getUnderwater());
		ret.put("sedation_level",this.getSedationLevel());
		ret.put("versed",this.getVersed());
		ret.put("fentanyl",this.getFentanyl());
		ret.put("demerol",this.getDemerol());
		ret.put("benadryl",this.getBenadryl());
		ret.put("extent",this.getExtent());
		ret.put("prep_quality_left",this.getPrepQualityLeft());
		ret.put("prep_quality_mid",this.getPrepQualityMid());
		ret.put("prep_quality_right",this.getPrepQualityRight());
		ret.put("time_insertion",this.getTimeInsertionString());
		ret.put("time_begin_withdrawal",this.getTimeBeginWithdrawalString());
		ret.put("time_scope_withdrawn",this.getTimeScopeWithdrawnString());

		
		if(this.getDateTimeOfService() != null){
			ret.put("date_time_of_service",""+getDateTimeOfServiceString());
		}
		else{
			ret.put("date_time_of_service","null");
		}
		
		return ret;
	}
	
	public void clearData() {
		this.setAcIDNoChecks(null);
		if (this.setDateTimeOfService((Long) null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setFacultyID(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setLocation(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setFellow(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setPreDrug(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setPrepLiters(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setSplitPrep(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setBisacodyl(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setLastColon(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setPrimaryIndication(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setOtherIndication(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setScope(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setEndocuff(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setCapAssisted(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setUnderwater(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setSedationLevel(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setVersed(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setFentanyl(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setDemerol(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setBenadryl(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setExtent(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setPrepQualityLeft(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setPrepQualityMid(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setPrepQualityRight(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setTimeInsertion(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setTimeBeginWithdrawal(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
		if (this.setTimeScopeWithdrawn(null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}

	}
	
	

}
