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

import net.djp3.qualoscopy.EventHandlerResultChecker;
import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.QEventCheckVersion;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;
import edu.uci.ics.luci.utility.webserver.output.response.Response;

/**
 * This handler returns the current version, but requires an input parameter of the correct API version.
 * It's kind of pointless as an information gathering tool, but it helps to test connectivity and parameter
 * setting.
 * @author djp3
 *
 */
public class HandlerCheckVersion extends HandlerPublisher{
	
	public static final String ERROR_NULL_VERSION = "Version was null";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerCheckVersion.class);
		}
		return log;
	}

	String aPIVersion = null;
	String requestedVersion = null;

	/**
	 * 
	 * @param eventPublisher, the queue to put events on
	 * @param aPIVersion, the correct API version
	 */
	public HandlerCheckVersion(QEventWrapperQueuer eventPublisher, String aPIVersion) {
		super(eventPublisher);
		this.setAPIVersion(aPIVersion);
	}
	
	@Override
	public HandlerAbstract copy() {
		return new HandlerCheckVersion(getEventPublisher(),getAPIVersion());
	}
	
	public String getAPIVersion(){
		return aPIVersion;
	}

	public String getRequestedVersion(){
		return requestedVersion;
	}
	
	protected String setRequestedVersion(String requestedVersion){
		this.requestedVersion = requestedVersion;
		return getRequestedVersion();
	}

	@Override
	protected JSONArray getParameters(Request r){
		JSONArray errors = new JSONArray();
		
		Set<String> _version = r.getParameters().get("version");
		if((_version == null) || ((setRequestedVersion(_version.iterator().next()))==null)){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_VERSION);
		} 
		
		return errors;
	}
	
	@Override
	protected JSONObject constructReply(Request r){
		JSONObject ret = super.constructReply(r);
		if(ret.get("error").equals("false")){
			JSONArray errors = getParameters(r);
			if(errors.size() != 0){
				ret.put("error", "true");
				ret.put("errors", errors);
			}
			else{
				QEventCheckVersion q = new QEventCheckVersion(getAPIVersion(),getRequestedVersion());
				ret = this.processRequestEvent(QEventType.CHECK_VERSION, q);
			}
		}
		return ret;
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
		
		if(errors.size() != 0){
			ret.put("error", "true");
			ret.put("errors", errors);
		}
		else{
			QEventCheckVersion q = new QEventCheckVersion(getAPIVersion(),getRequestedVersion());
			ret = this.queueEvent(QEventType.CHECK_VERSION, q);
		}
		
		response.setStatus(Response.Status.OK);
		response.setDataType(Response.DataType.JSON);
		response.setBody(wrapCallback(request.getParameters(),ret.toString()));
		
		return response;
	}



}

