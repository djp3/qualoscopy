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


package net.djp3.qualoscopy.api;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.djp3.qualoscopy.datastore.DatastoreInterface.InitialCredentials;
import net.djp3.qualoscopy.datastore.SHA256;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

/**
 * @author djp3
 *
 */
public class QAPIEvent_Login extends QAPIEvent_VersionCheck implements Cloneable{ 
	
	public static final String ERROR_NULL_USER_ID = "\"user_id\" was null";
	public static final String ERROR_NULL_SESSION_ID = "\"session_id\" (Session ID) was null";
	public static final String ERROR_NULL_SHP = "\"shp\" (Salted Hashed Password) was null";
	public static final String ERROR_NULL_SOURCE = "Internal Error - source was null- not a parameter";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_Login.class);
		}
		return log;
	}
	
	private DatastoreInterface db = null;
	
	public DatastoreInterface getDB() {
		return db;
	}

	public void setDB(DatastoreInterface db) {
		this.db = db;
	}


	/**
	 * 
	 * @param version, the correct API version
	 * @param eventPublisher, the queue to put events on
	 */
	public QAPIEvent_Login(String version,DatastoreInterface db) {
		super(version);
		setDB(db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_Login incoming = null;
		if(_incoming instanceof QAPIEvent_Login){
			incoming = (QAPIEvent_Login) _incoming;
			super.set(incoming);
			this.setDB(incoming.getDB());
		}
		else{
			getLog().error(ERROR_SET_ENCOUNTERED_TYPE_MISMATCH+", incoming:"+_incoming.getClass().getName()+", this:"+this.getClass().getName());
			throw new InvalidParameterException(ERROR_SET_ENCOUNTERED_TYPE_MISMATCH+", incoming:"+_incoming.getClass().getName()+", this:"+this.getClass().getName());
		}
	}
	
	@Override
	public Object clone(){
		return(super.clone());
	}
	
	
	
	@Override
	protected JSONObject buildResponse(Request r) {
		JSONObject response = super.buildResponse(r);
		JSONArray errors = (JSONArray) response.get("errors");
		if(errors == null){
			errors = new JSONArray();
			response.put("errors", errors);
		}
		
		String error = (String) response.get("error");
		if(error == null){
			error ="true";
			response.put("error", error);
			errors.add("Response building failed: "+this.getClass().getName());
			response.put("errors", errors);
		}
		
		//Get parameters 
		Set<String> _user_id = r.getParameters().get("user_id");
		String user_id = null;
		if((_user_id == null) || ((user_id = (_user_id.iterator().next())) == null)){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_USER_ID);
			response.put("error", "true");
			response.put("errors", errors);
		}
		
		Set<String> _session_id = r.getParameters().get("session_id");
		String session_id  = null;
		if((_session_id == null) || ((session_id = (_session_id.iterator().next())) == null)){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_SESSION_ID);
			response.put("error", "true");
			response.put("errors", errors);
		}
		
		Set<String> _shp = r.getParameters().get("shp");
		String shp  = null;
		if((_shp == null) || ((shp = (_shp.iterator().next())) == null)){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_SHP);
			response.put("error", "true");
			response.put("errors", errors);
		}
		
		String source = r.getSource();
		if(source == null){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_SOURCE);
			response.put("error", "true");
			response.put("errors", errors);
		}
		
		if(error.equals("false")){
			
			List<InitialCredentials> credentials = getDB().getInitialCredentials();
			boolean credentialsPassed = false;
			InitialCredentials passing = null;
			for(InitialCredentials c : credentials){
				if(source.equals(c.getSource())){
					if(session_id.equals(c.getSessionID())){
						String hashedPassword = getDB().getHashedPassword(user_id);
						if(hashedPassword != null){
							String attempt = SHA256.sha256(hashedPassword+c.getSalt(),1);
							if(shp.equals(attempt)){
								credentialsPassed = true;
								passing = c;
							}
						}
					}
				}
			}
			
			if(credentialsPassed){
				if(getDB().removeInitialCredentials(passing)){
					String sessionKey = getDB().createAndStoreSession(user_id,session_id,source);
					String salt = getDB().createAndStoreSalt(user_id);
					response.put("session_key", sessionKey);
					response.put("salt", salt);
				}
				else{
					/* Rejected because something fishy happened */
					buildRejection(response, errors);
				}
			}
			else{
				buildRejection(response, errors);
			}
		}
		return response;
	}


	public void buildRejection(JSONObject response, JSONArray errors) {
		response.put("error", "true");
		errors.add("Credentials are not accepted");
		response.put("errors",errors);
	}
	
	
	@Override
	public APIEventResult onEvent() {
		
		APIEventResult response = null;
		
		response = getOutput().makeOutputChannelResponse();
		
		JSONObject ret = buildResponse(getRequest());
		
		response.setStatus(APIEventResult.Status.OK);
		response.setDataType(APIEventResult.DataType.JSON);
		response.setResponseBody(wrapCallback(getRequest().getParameters(),ret.toString()));
		
		return response;
	}


}

