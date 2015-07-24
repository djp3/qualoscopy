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

import java.util.ArrayList;
import java.util.Collections;
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
	private static final String ERROR_FAIL_USER_ID_NULL = "Internal error: tried to update a procedure with a null user id";
	private static final String ERROR_FAIL_PATIENT_ID_NULL = "Internal error: tried to update a procedure with a null patient id";
	private static final String ERROR_FAIL_PROCEDURE_NULL = "Internal error: tried to update a procedure with a null procedure";
	private static final String ERROR_FAIL_PATIENT_ID_UNKNOWN = "Internal error: patient id does not exist in the database";
	private static final String ERROR_FAIL_PATIENT_ID_HAS_NO_PROCEDURES = "Internal error: patient id has no procedures, add one first";
	private static final String ERROR_FAIL_PROCEDURE_DOESNT_EXIST = "Internal error: procedure id does not exist, add one first";
	
	private static final String ERROR_FAIL_PROCEDURE_ID_NULL = "Internal error: tried to update a polyp with a null procedure id";
	private static final String ERROR_FAIL_POLYP_NULL = "Internal error: tried to update a polyp with a null polyp";
	private static final String ERROR_FAIL_PROCEDURE_ID_UNKNOWN = "Internal error: procedure id does not exist in the database";
	private static final String ERROR_FAIL_PROCEDURE_ID_HAS_NO_POLYPS = "Internal error: procedure id has no polyps, add one first";
	private static final String ERROR_FAIL_POLYP_DOESNT_EXIST = "Internal error: polyp with that id does not exist, add one first";
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
	private Map<String,Patient> patients; // <patient_id,Patient>
	private Map<String, Map<String,Procedure>> procedures; //<patient_id,<procedure_id, Procedure>>
	private Map<String, Map<String,Polyp>> polyps; //<procedure_id,<polyp_id, Polyp>>
	
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
		else if(user_id.equals("test_user")){
			return SHA256.sha256("test_password",1);
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

	public Map<String,Patient> getPatients(String user_id) {
		if(patients == null){
			patients = generateFakePatients();
		}
		return patients;
	}
	

	public Map<String, Procedure> getPatientProcedures(String userID, String patientID) {
		if(procedures == null){
			procedures = Collections.synchronizedMap(new HashMap<String,Map<String,Procedure>>());
		}
		if(procedures.get(patientID) == null){
			procedures.put(patientID,generateFakeProcedures());
		}
		return procedures.get(patientID);
	}
	


	private Map<String,Patient> generateFakePatients() {
		final int numPatients = 25;
		Map<String,Patient> patients= new HashMap<String,Patient>(numPatients);
		while(patients.size() < numPatients){
			Patient patient = Patient.generateFakePatient();
			if(patient != null){
				patients.put(patient.getPatientID(),patient);
			}
			else{
				getLog().error("Null patient created ??!?!!?");
			}
		}
		return patients;
	}

	

	private Map<String,Procedure> generateFakeProcedures() {

		final int numProcedures = r.nextInt(4)+4;
		Map<String,Procedure> _procedures= new HashMap<String,Procedure>(numProcedures);
		Map<String, Procedure> procedures = Collections.synchronizedMap(_procedures);
		while(procedures.size() < numProcedures){
			Procedure procedure = Procedure.generateFakeProcedure();
			if(procedure != null){
				procedures.put(procedure.getProcedureID(),procedure);
			}
			else{
				getLog().error("Null procedure created ??!?!!?");
			}
		}
		return procedures;
	}
	
	
	private Map<String,Polyp> generateFakePolyps() {

		final int numPolyps = r.nextInt(4)+1;
		Map<String,Polyp> _polyps= new HashMap<String,Polyp>(numPolyps);
		Map<String, Polyp> polyps = Collections.synchronizedMap(_polyps);
		while(polyps.size() < numPolyps){
			Polyp polyp = Polyp.generateFakePolyp();
			if(polyp != null){
				polyps.put(polyp.getPolypID(),polyp);
			}
			else{
				getLog().error("Null polyp created ??!?!!?");
			}
		}
		return polyps;
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

	public String addPatient(String user_id) {
		if(user_id == null){
			return null;
		}
		else{
			Patient patient = Patient.generateFakePatient();
			patient.clearData();
			patients.put(patient.getPatientID(), patient);
			return patient.getPatientID();
		}
	}
	
	public String updatePatient(String userID, String patientID, String mrID, String first, String last, String gender, Long dob) {
		
		if(!patients.containsKey(patientID)){
			return "Patient with patient ID:"+patientID+" does not exist";
		}
		else{
			Patient newPatient = new Patient(patientID,mrID,first,last,gender,dob);
			patients.put(patientID, newPatient);
		}
		return null;
	}

	public String addProcedure(String userID, String patientID) {
		if(userID != null){
			if(patientID != null){
				Procedure procedure = Procedure.generateFakeProcedure();
				
				procedure.clearData();
				
				Map<String, Procedure> p = procedures.get(patientID);
				if(p == null){
					p = new HashMap<String,Procedure>();
				}
				p.put(procedure.getProcedureID(), procedure);
				procedures.put(patientID, p);
				
				return procedure.getProcedureID();
			}
		}
		return null;
	}



	public String updateProcedure(String userID, String patientID, Procedure procedure) {
		if(userID != null){
			if(patientID != null){
				if(procedure != null){
					Map<String, Procedure> plist = procedures.get(patientID);
					if(plist == null){
						return ERROR_FAIL_PATIENT_ID_UNKNOWN;
					}
					else{
						if (plist.size() == 0){
							return ERROR_FAIL_PATIENT_ID_HAS_NO_PROCEDURES;
						}
						else{
							Procedure p = plist.get(procedure.getProcedureID());
							if(p == null){
								return ERROR_FAIL_PROCEDURE_DOESNT_EXIST;
							}
							else{
								if(procedure.getAcID() != null){
									p.setAcID(procedure.getAcID());
								}
								if(procedure.getDateTimeOfService() != null){
									if(p.setDateTimeOfService(procedure.getDateTimeOfService()) != null){
										return procedure.ERROR_PATTERN_FAIL_DOS;
									}
								}
								if(procedure.getFacultyID() != null){
									p.setFacultyID(procedure.getFacultyID());
								}
								if(procedure.getFacultyID() != null){
									p.setFacultyID(procedure.getFacultyID());
								}

								if(procedure.getLocation() != null){
									p.setLocation(procedure.getLocation());
								}
								if(procedure.getFellow() != null){
									p.setFellow(procedure.getFellow());
								}
								if(procedure.getPreDrug() != null){
									p.setPreDrug(procedure.getPreDrug());
								}
								if(procedure.getPrepLiters() != null){
									p.setPrepLiters(procedure.getPrepLiters());
								}
								if(procedure.getSplitPrep() != null){
									p.setSplitPrep(procedure.getSplitPrep());
								}
								if(procedure.getBisacodyl() != null){
									p.setBisacodyl(procedure.getBisacodyl());
								}
								if(procedure.getLastColon() != null){
									p.setLastColon(procedure.getLastColon());
								}
								if(procedure.getPrimaryIndication() != null){
									p.setPrimaryIndication(procedure.getPrimaryIndication());
								}
								if(procedure.getOtherIndication() != null){
									p.setOtherIndication(procedure.getOtherIndication());
								}
								if(procedure.getScope() != null){
									p.setScope(procedure.getScope());
								}
								if(procedure.getEndocuff() != null){
									p.setEndocuff(procedure.getEndocuff());
								}
								if(procedure.getCapAssisted() != null){
									p.setCapAssisted(procedure.getCapAssisted());
								}
								if(procedure.getUnderwater() != null){
									p.setUnderwater(procedure.getUnderwater());
								}
								if(procedure.getSedationLevel() != null){
									p.setSedationLevel(procedure.getSedationLevel());
								}
								if(procedure.getVersed() != null){
									p.setVersed(procedure.getVersed());
								}
								if(procedure.getFentanyl() != null){
									p.setFentanyl(procedure.getFentanyl());
								}
								if(procedure.getDemerol() != null){
									p.setDemerol(procedure.getDemerol());
								}
								if(procedure.getBenadryl() != null){
									p.setBenadryl(procedure.getBenadryl());
								}
								if(procedure.getExtent() != null){
									p.setExtent(procedure.getExtent());
								}
								if(procedure.getPrepQualityLeft() != null){
									p.setPrepQualityLeft(procedure.getPrepQualityLeft());
								}
								if(procedure.getPrepQualityMid() != null){
									p.setPrepQualityMid(procedure.getPrepQualityMid());
								}
								if(procedure.getPrepQualityRight() != null){
									p.setPrepQualityRight(procedure.getPrepQualityRight());
								}
								if(procedure.getTimeInsertion() != null){
									p.setTimeInsertion(procedure.getTimeInsertion());
								}
								if(procedure.getTimeBeginWithdrawal() != null){
									p.setTimeBeginWithdrawal(procedure.getTimeBeginWithdrawal());
								}
								if(procedure.getTimeScopeWithdrawn() != null){
									p.setTimeScopeWithdrawn(procedure.getTimeScopeWithdrawn());
								}


								
								plist.put(p.getProcedureID(), p);
								procedures.put(patientID, plist);
								return null; //No error
							}
						}
					}
				}
				else{
					return ERROR_FAIL_PROCEDURE_NULL;
				}
			}
			else{
				return ERROR_FAIL_PATIENT_ID_NULL;
			}
		}
		else{
			return ERROR_FAIL_USER_ID_NULL;
		}
	}
	
	public Map<String, Polyp> getProcedurePolyps(String userID, String procedureID) {
		if(polyps == null){
			polyps = Collections.synchronizedMap(new HashMap<String,Map<String,Polyp>>());
		}
		if(polyps.get(procedureID) == null){
			polyps.put(procedureID,generateFakePolyps());
		}
		return polyps.get(procedureID);
	}

	public String addPolyp(String userID, String procedureID) {
		if(userID != null){
			if(procedureID != null){
				Polyp polyp = Polyp.generateFakePolyp();
				
				polyp.clearData();
				
				Map<String, Polyp> p = polyps.get(procedureID);
				if(p == null){
					p = new HashMap<String,Polyp>();
				}
				p.put(polyp.getPolypID(), polyp);
				polyps.put(procedureID, p);
				
				return polyp.getPolypID();
			}
		}
		return null;
	}

	public String updatePolyp(String userID, String procedure_id, Polyp polyp) {
		if(userID != null){
			if(procedure_id != null){
				if(polyp != null){
					Map<String, Polyp> plist = polyps.get(procedure_id);
					if(plist == null){
						return ERROR_FAIL_PROCEDURE_ID_UNKNOWN;
					}
					else{
						if (plist.size() == 0){
							return ERROR_FAIL_PROCEDURE_ID_HAS_NO_POLYPS;
						}
						else{
							Polyp p = plist.get(polyp.getPolypID());
							if(p == null){
								return ERROR_FAIL_POLYP_DOESNT_EXIST;
							}
							else{
								if(polyp.getTimeRemoved() != null){
									p.setTimeRemoved(polyp.getTimeRemoved());
								}

								
								plist.put(p.getPolypID(), p);
								polyps.put(procedure_id, plist);
								return null; //No error
							}
						}
					}
				}
				else{
					return ERROR_FAIL_POLYP_NULL;
				}
			}
			else{
				return ERROR_FAIL_PROCEDURE_ID_NULL;
			}
		}
		else{
			return ERROR_FAIL_USER_ID_NULL;
		}
	}

}
