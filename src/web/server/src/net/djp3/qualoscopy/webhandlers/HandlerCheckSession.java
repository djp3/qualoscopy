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


package net.djp3.qualoscopy.webhandlers;

import java.util.Map;
import java.util.Set;

import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.minidev.json.JSONArray;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.handlers.login.Datastore;

/**
 * @author djp3
 *
 */
public abstract class HandlerCheckSession extends HandlerCheckVersion{
	
	public static final String ERROR_NULL_USER_ID = "user_id was null";
	public static final String ERROR_NULL_SESSION_ID = "session_id was null";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerCheckSession.class);
		}
		return log;
	}

	private Datastore db;
	
	
	private String userID;
	private String sessionID;
	
	
	protected Datastore getDatastore()
	{
		return db;
	}
	
	public String getUserID(){
		return this.userID;
	}
	
	public String getSessionID(){
		return this.sessionID;
	}
	
	public String getSource()
	{
		return this.source;
	}
	
	/**
	 * 
	 * @param version, the correct API version
	 * @param eventPublisher, the queue to put events on
	 */
	public HandlerCheckSession(QEventWrapperQueuer eventPublisher,Datastore db) {
		super(eventPublisher,Globals.getGlobals().getSystemVersion());
		this.db = db;
	}
	
	
	protected JSONArray getSessionParameters(String restFunction,Map<String,Set<String>> parameters,String source){
		JSONArray errors = new JSONArray();
		
		Set<String> _userID = parameters.get("user_id");
		if((_userID == null) || ((userID = (_userID.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+":"+ERROR_NULL_USER_ID);
		} 
		
		
		Set<String> _sessionID = parameters.get("session_id");
		if((_sessionID == null) || ((sessionID = (_sessionID.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+":"+ERROR_NULL_SESSION_ID);
		} 
		
		if((this.source = source) == null){
			errors.add("Problem handling "+restFunction+":"+ERROR_NULL_SOURCE);
		}
		
		return errors;
	}

}

