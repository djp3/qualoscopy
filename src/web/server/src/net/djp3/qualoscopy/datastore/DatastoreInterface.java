/*
	Copyright 2015
		Donald J. Patterson
*/
/*
	This file is part of the Qualoscopy Web Service, i.e. "Qualoscopy"

    Qualoscopy is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Utilities is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Utilities.  If not, see <http://www.gnu.org/licenses/>.
*/


package net.djp3.qualoscopy.datastore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.event.api.login.Datastore;

public class DatastoreInterface {
	
	private static final Integer ITERATIONS = 200;
	private static Random r = new Random(System.currentTimeMillis()-45060);

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(DatastoreInterface.class);
		}
		return log;
	}

	private Datastore db = null;
	/*This is the temporary Datastore substitutions */
	private List<InitialCredentials> initialCredentials;
	private List<Session> sessions;
	private Map<String,Set<String>> unusedSalts;
	private Set<Patient> patients;
	private Map<String, Set<Procedure>> procedures;
	
	void setRandom(Random r){
		DatastoreInterface.r = r;
	}
	//TODO: Make sure you clean up old salts
	
	public DatastoreInterface(Datastore db){
		this(db,System.currentTimeMillis());
	}
	
	/**
	 * This interface is provided just for testing purposes
	 * @param db
	 * @param seed
	 */
	private DatastoreInterface(Datastore db,long seed){
		this.db = db;
		// TODO: Replace below with equivalent in Datastore
		initialCredentials = Collections.synchronizedList(new ArrayList<InitialCredentials>());
		sessions  = Collections.synchronizedList(new ArrayList<Session>());
		unusedSalts  = Collections.synchronizedMap(new HashMap<String,Set<String>>());
		this.setRandom(new Random(seed));
	}
	
	
	protected String createSessionID(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
	
	protected String createSessionKey(){
		return SHA256.sha256(r.nextLong()+"",ITERATIONS);
	}
	
	protected synchronized String createSalt(){
		return SHA256.sha256(r.nextLong()+"",ITERATIONS);
	}
	
	public class InitialCredentials{
		String sessionID;
		String salt;
		String source;
		
		public InitialCredentials(String sessionID, String salt, String source) {
			super();
			this.sessionID = sessionID;
			this.salt = salt;
			this.source = source;
		}
		
		public String getSessionID() {
			return sessionID;
		}
		public void setSessionID(String sessionID) {
			this.sessionID = sessionID;
		}
		public String getSalt() {
			return salt;
		}
		public void setSalt(String salt) {
			this.salt = salt;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
	}
	
	public class Session{
		
		String user_id;
		String session_id;
		String session_key;
		String source;
		
		public Session(String user_id, String session_id, String session_key, String source) {
			super();
			this.setUser_id(user_id);
			this.setSession_ID(session_id);
			this.setSession_key(session_key);
			this.setSource(source);
		}
		
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getSession_ID() {
			return session_id;
		}
		public void setSession_ID(String session_id) {
			this.session_id = session_id;
		}
		public String getSession_key() {
			return session_key;
		}
		public void setSession_key(String session_key) {
			this.session_key = session_key;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		
	}
	
	public synchronized Pair<String,String> createAndStoreInitialSessionIDAndSalt(String source){
		//TODO: Store in database somewhere
		String sessionID = createSessionID();
		String salt = createSalt();
		initialCredentials.add(new InitialCredentials(sessionID,salt,source));
		Pair<String, String> pair = new Pair<String,String>(sessionID,salt);
		return pair;
	}
	
	public List<InitialCredentials> getInitialCredentials() {
		return initialCredentials;
	}

	public boolean removeInitialCredentials(InitialCredentials passing) {
		return(getInitialCredentials().remove(passing));
	}
	
	public synchronized String createAndStoreSalt(String userID){
		//TODO: Store salt in database somewhere
		String salt = createSalt();
		synchronized(unusedSalts){
			Set<String> salts = unusedSalts.get(userID);
			if(salts == null){
				salts = new HashSet<String>();
			}
			salts.add(salt);
			unusedSalts.put(userID,salts);
		}
		return salt;
	}

	public String getHashedPassword(String user_id) {
		//TODO: Make this use a database 
		if(user_id.equals("Luke")){
			return SHA256.sha256("Raus",1);
		}
		else{
			return SHA256.makeSomethingUp();
		}
		
	}

	public String createAndStoreSession(String user_id, String shsid,String source) {
		String sessionKey = createSessionKey();
		sessions.add(new Session(user_id, shsid,sessionKey,source));
		return sessionKey;
	}

	/**
	 * 
	 * @param user_id
	 * @param shsid
	 * @param shsk
	 * @param source
	 * @return
	 */
	public boolean checkSession(String user_id, String shsid, String shsk, String source) {
		if(user_id == null){
			return false;
		}
		
		if(shsid == null){
			return false;
		}
		
		if(shsk == null){
			return false;
		}
		
		if(source == null){
			return false;
		}
		
		synchronized(sessions){
			for(Session session: sessions){
				if(user_id.equals(session.getUser_id())){
					synchronized(unusedSalts){
						Set<String> saltSet = unusedSalts.get(session.getUser_id());
						String removeMe = null;
						for(String salt :saltSet){
							if(removeMe == null){
								if(shsid.equals(SHA256.sha256(session.getSession_ID()+salt,1))){
									if(shsk.equals(SHA256.sha256(session.getSession_key()+salt, 1))){
										removeMe = salt;
									}
								}
							}
						}
						if(removeMe != null){
							saltSet.remove(removeMe);
							unusedSalts.put(user_id, saltSet);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public Set<Patient> getPatients(String user_id) {
		if(patients == null){
			patients = generateFakePatients();
		}
		return patients;
	}
	

	public Set<Procedure> getPatientProcedures(String user_id, String mr_id) {
		if(procedures == null){
			procedures = new HashMap<String,Set<Procedure>>();
		}
		if(procedures.get(user_id) == null){
			procedures.put(user_id,generateFakeProcedures());
		}
		return procedures.get(user_id);
	}
	


	private Set<Patient> generateFakePatients() {
		final int numPatients = 25;
		Set<Patient> patients= new HashSet<Patient>(numPatients);
		while(patients.size() < numPatients){
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
			
				date = sdf.parse("2015-07-01 00:00:00.000");
				long nextProcedure = date.getTime() + Math.abs(r.nextInt(90*24*60*60))*1000; //90 days from 7/1/25
				patients.add(new Patient(medicalRecordID, firstName, lastName, gender, dateOfBirth, nextProcedure));
			} catch (ParseException e) {
				getLog().error("Problem making fake patients:"+e);
			}
		}
		return patients;
	}

	private Set<Procedure> generateFakeProcedures() {

		final int numProcedures = r.nextInt(4);
		Set<Procedure> procedures= new HashSet<Procedure>(numProcedures);
		while(procedures.size() < numProcedures){
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
				procedures.add(new Procedure(ac_id, dateOfService,faculty, completed));
			} catch (ParseException e) {
				getLog().error("Problem making fake patients:"+e);
			}
		}
		return procedures;
	}

	public void wipeSessions(String user_id) {
		if(user_id == null){
			getLog().error("Can't wipe session for null user_id");
		}
		else{
			synchronized(sessions){
				Set<Session> removeUs = new HashSet<Session>();
				for(Session session: sessions){
					if(session == null){
						getLog().error("Why do I have a null session in the data set?");
						removeUs.add(session);
					}
					else{
						if(session.getUser_id() == null){
							getLog().error("Why do I have a session with a null user_id?");
							removeUs.add(session);
						}
						else{
							if(user_id.equals(session.getUser_id())){
								removeUs.add(session);
							}
						}
					}
				}
				getLog().debug("Wiped "+removeUs.size()+" sessions");
				sessions.removeAll(removeUs);
			}
			synchronized(unusedSalts){
				Set<String> removed = unusedSalts.remove(user_id);
				if(removed == null){
					removed = new HashSet<String>();
				}
				getLog().debug("Wiped "+removed.size()+" unused salts");
			}
		}
	}

}
