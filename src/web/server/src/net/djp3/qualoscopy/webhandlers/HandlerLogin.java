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

import net.djp3.qualoscopy.events.QEventCheckVersion;
import net.djp3.qualoscopy.events.QEventLogin;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.handlers.login.Datastore;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;
import edu.uci.ics.luci.utility.webserver.output.response.Response;

/**
 * @author djp3
 *
 */
public class HandlerLogin extends HandlerCheckSession{
	
	public static final String ERROR_NULL_USER_ID = "user_id was null";
	public static final String ERROR_NULL_SHPASSWORD = "salted_hashed_password was null";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerLogin.class);
		}
		return log;
	}
	
	String shp;
	

	@Override
	public HandlerAbstract copy() {
		return new HandlerLogin(getEventPublisher(),getDatastore());
	}
	

	public String getSaltedHashedPassword(){
		return shp;
	}


	/**
	 * 
	 * @param version, the correct API version
	 * @param eventPublisher, the queue to put events on
	 */
	public HandlerLogin(QEventWrapperQueuer eventPublisher,Datastore db) {
		super(eventPublisher,db);
	}
	
	
	protected JSONArray getLoginParameters(String restFunction,Map<String,Set<String>> parameters){
		JSONArray errors = new JSONArray();
		
		Set<String> _shp = parameters.get("salted_hashed_password");
		if((_shp == null) || ((shp = (_shp.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+":"+ERROR_NULL_SHPASSWORD);
		} 
		
		return errors;
	}
	
	
	/**
	 * @param parameters a map of key and value that was passed through the REST request
	 * @return a pair where the first element is the content type and the bytes are the output bytes to send back
	 */
	@Override
	public Response handle(Request request, Output o) {
		getLog().info("Handling request");
		
		JSONObject ret = new JSONObject();
		
		Response response = o.makeOutputChannelResponse();
		
		JSONArray errors = new JSONArray();
		
		errors.addAll(getVersionParameters(request.getCommand(),request.getParameters()));
		errors.addAll(getSessionParameters(request.getCommand(),request.getParameters(),request.getSource()));
		errors.addAll(getLoginParameters(request.getCommand(),request.getParameters()));
		
		if(errors.size() != 0){
			ret.put("error", "true");
			ret.put("errors", errors);
		}
		else{
			QEventLogin q = new QEventLogin(this);
			ret = this.queueEvent(QEventType.LOGIN, q);
		}
		
		response.setStatus(Response.Status.OK);
		response.setDataType(Response.DataType.JSON);
		response.setBody(wrapCallback(request.getParameters(),ret.toString()));
		
		return response;
	}


}

