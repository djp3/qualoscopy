package net.djp3.qualoscopy.datastore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.CalendarCache;

public class Polyp {
	
	private static Random random = new Random();
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Polyp.class);
		}
		return log;
	}
	
	public static Polyp generateFakePolyp() {
		Long _polypID = -1L;
		while(_polypID < 0){
			_polypID = random.nextLong();
		}
		String polyp_id = "Polyp_"+_polypID;
		
		String time_removed = random.nextInt(24)+":"+random.nextInt(60);
		
		Polyp polyp = new Polyp();
		polyp.setPolypID(polyp_id);
		polyp.setTimeRemovedString(time_removed);
		
		return polyp;
	}
	
	public final SimpleDateFormat sdf;
	public final SimpleDateFormat shortSdf;
	
	String polypID = null;
	Long time_removed = null;
	
	public Polyp(){
		super();
		sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		sdf.setTimeZone(CalendarCache.TZ_GMT);
		
		shortSdf = new SimpleDateFormat("HH:mm");
		shortSdf.setTimeZone(CalendarCache.TZ_GMT);
	}
	
	
	public String getPolypID() {
		return polypID;
	}
	public void setPolypID(String polypID) {
		this.polypID = polypID;
	}

	public Long getTimeRemoved() {
		return time_removed;
	}
	
	public String getTimeRemovedString() {
		Long local = this.getTimeRemoved();
		if(local != null){
			Date date = new Date(local);
			String formatted = shortSdf.format(date);
			return formatted;
		}
		return null;
	}

	public String setTimeRemoved(Long time_removed) {
		this.time_removed = time_removed;
		return null;
	}
	
	public String setTimeRemovedString(String time_removed_string) {
		try{
			Date parse = shortSdf.parse(time_removed_string);
			long local = parse.getTime();
			this.time_removed = local;
			return null; //Return error message if it doesn't match a pattern
		}
		catch(ParseException e){
			return "Internal Error: setTimeRemovedString";
		}
	}



	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		ret.put("polyp_id",""+this.getPolypID());
		
		ret.put("time_removed",""+this.getTimeRemovedString());
		
		return ret;
	}
	
	public void clearData() {
		if (this.setTimeRemoved((Long) null) != null) {
			throw new IllegalStateException("Shouldn't get an error");
		}
	}
	
	

}
