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
import java.util.Set;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent_Version;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

/**
 * This handler returns the current version, but requires an input parameter of the correct API version.
 * It's kind of pointless as an information gathering tool, but it helps to test connectivity and parameter
 * setting.
 * @author djp3
 *
 */
public class QAPIEvent_VersionCheck extends APIEvent_Version implements Cloneable{ 
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_VersionCheck.class);
		}
		return log;
	}
	
	public static final String ERROR_NULL_VERSION = "Version was null";

	private String requestedVersion = null;
	
	
	
	public QAPIEvent_VersionCheck(String apiVersion) {
		super(apiVersion);
	}
	
	public String getRequestedVersion(){
		return requestedVersion;
	}

	protected String setRequestedVersion(String requestedVersion){
		this.requestedVersion = requestedVersion;
		return getRequestedVersion();
	}

	@Override
	public void set(Event _incoming) {
		QAPIEvent_VersionCheck incoming = null;
		if(_incoming instanceof QAPIEvent_VersionCheck){
			incoming = (QAPIEvent_VersionCheck) _incoming;
			super.set(incoming);
			this.setRequestedVersion(incoming.getRequestedVersion());
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
	
	
	protected JSONObject buildResponse(Request r) {
		JSONObject response = buildResponseSkeleton();
		JSONArray errors = (JSONArray) response.get("errors");
		if(errors == null){
			errors = new JSONArray();
			response.put("errors", errors);
		}
		
		// Make sure globals is not null
		GlobalsQualoscopy g = GlobalsQualoscopy.getGlobalsQualoscopy();
		if (g == null) {
			response.put("error", "true");
			errors.add(ERROR_GLOBALS_NULL);
			response.put("errors", errors);
		}

		//Get the version parameter
		Set<String> _version = r.getParameters().get("version");
		if((_version == null) || ((setRequestedVersion(_version.iterator().next()))==null)){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_VERSION);
			response.put("error", "true");
			response.put("errors", errors);
		}
		else{
			// Check the version parameter
			if(!getRequestedVersion().equals(getAPIVersion())){
				errors.add("Incorrect API version request"+", providing:"+getAPIVersion()+", requested:"+getRequestedVersion());
				response.put("error", "true");
				response.put("errors", errors);
			}
			else{
				response.put("version", getAPIVersion());
			}
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

