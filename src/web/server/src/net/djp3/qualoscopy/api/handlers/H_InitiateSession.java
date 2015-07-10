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


package net.djp3.qualoscopy.api.handlers;

import net.djp3.qualoscopy.api.methods.QAPIM_InitiateSession;
import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.WebEventHandlerInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.server.ServerCallHandler;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.server.ServerCallHandlerInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.events.WebEventInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.events.server.ServerCall;
import edu.uci.ics.luci.utility.webserver.disruptor.results.ServerCallResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;

public class H_InitiateSession extends H_VersionCheck implements ServerCallHandlerInterface, WebEventHandlerInterface{ 
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(H_InitiateSession.class);
		}
		return log;
	}

	private static DatastoreInterface db = null;
	
	public static DatastoreInterface getDB() {
		return db;
	}

	public static void setDB(DatastoreInterface db) {
		H_InitiateSession.db = db;
	}

	public H_InitiateSession() {
		super();
	}
	
	public H_InitiateSession(DatastoreInterface db) {
		super();
		setDB(db);
	}
	
	@Override
	public boolean isInitialized(){
		return ((super.isInitialized()) && (db != null));
	}
	
	@Override
	public ServerCallHandler copy() {
		return new H_InitiateSession(getDB());
	}
	
	@Override
	public Class<? extends ServerCall> getMatchingMethod() {
		return QAPIM_InitiateSession.class;
	}

	@Override
	protected JSONObject buildResponse(Request r) {
		JSONObject response = super.buildResponse(r);
		String error = (String) response.get("error");
		if(error == null){
			error ="true";
			response.put("error", error);
		}
		
		JSONArray errors = (JSONArray) response.get("errors");
		if(errors == null){
			errors = new JSONArray();
			response.put("errors", errors);
		}
		
		if(error.equals("false")){
			String source = r.getSource();
		
			Pair<String, String> pair = getDB().createAndStoreInitialSessionIDAndSalt(source);
			response.put("session_id",pair.getFirst());
			response.put("session_salt",pair.getSecond());
		}
		return response;
	}
	
	@Override
	public ServerCallResult onEvent(ServerCall e) {
		Request request = null;
		Output output = null;
		
		ServerCallResult response = null;
		
		request = e.getRequest();
		output = e.getOutput();
		response = output.makeOutputChannelResponse();
		
		JSONObject ret = buildResponse(request);
		
		response.setStatus(ServerCallResult.Status.OK);
		response.setDataType(ServerCallResult.DataType.JSON);
		response.setResponseBody(wrapCallback(request.getParameters(),ret.toString()));
			
		return response;
	}
	
	@Override
	public ServerCallResult onEvent(WebEventInterface _e) {

		QAPIM_InitiateSession e = null;
		
		if(!(_e instanceof QAPIM_InitiateSession)){
			getLog().error(ERROR_UNABLE_TO_HANDLE_WEB_EVENT+","+this.getClass().getCanonicalName()+" can't handle event of type "+_e.getClass().getCanonicalName());
			return null;
		}
		else{
			e = (QAPIM_InitiateSession) _e;
			return(onEvent(e));
		}
	}
	
}

