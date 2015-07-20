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

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

public class QAPIEvent_InitiateSession extends QAPIEvent_VersionCheck implements Cloneable{ 
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_InitiateSession.class);
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

	
	public QAPIEvent_InitiateSession(String version, DatastoreInterface db) {
		super(version);
		setDB(db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_InitiateSession incoming = null;
		if(_incoming instanceof QAPIEvent_InitiateSession){
			incoming = (QAPIEvent_InitiateSession) _incoming;
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
		
		if(error.equals("false")){
			String source = r.getSource();
		
			Pair<String, String> pair = getDB().createAndStoreInitialSessionIDAndSalt(source);
			response.put("session_id",pair.getFirst());
			response.put("salt",pair.getSecond());
		}
		return response;
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

