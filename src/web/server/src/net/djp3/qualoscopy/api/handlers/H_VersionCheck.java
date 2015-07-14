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

import java.util.Set;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.api.methods.QAPIM_VersionCheck;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.WebEventHandlerInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.server.ServerCallHandler;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.server.ServerCallHandlerInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.server.ServerCallHandler_Version;
import edu.uci.ics.luci.utility.webserver.disruptor.events.WebEventInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.events.server.ServerCall;
import edu.uci.ics.luci.utility.webserver.disruptor.results.ServerCallResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;

/**
 * This handler returns the current version, but requires an input parameter of the correct API version.
 * It's kind of pointless as an information gathering tool, but it helps to test connectivity and parameter
 * setting.
 * @author djp3
 *
 */
public class H_VersionCheck extends ServerCallHandler_Version implements ServerCallHandlerInterface, WebEventHandlerInterface{ 
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(H_VersionCheck.class);
		}
		return log;
	}
	
	public static final String ERROR_NULL_VERSION = "Version was null";

	private String requestedVersion = null;
	
	public H_VersionCheck() {
		super();
	}
	
	public H_VersionCheck(String apiVersion) {
		super(apiVersion);
	}
	
	@Override
	public boolean isInitialized(){
		return (super.isInitialized());
	}
	
	@Override
	public ServerCallHandler copy() {
		return new H_VersionCheck(getAPIVersion());
	}
	
	@Override
	public Class<? extends ServerCall> getMatchingMethod() {
		return QAPIM_VersionCheck.class;
	}
	
	public String getRequestedVersion(){
		return requestedVersion;
	}
	
	protected String setRequestedVersion(String requestedVersion){
		this.requestedVersion = requestedVersion;
		return getRequestedVersion();
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

		QAPIM_VersionCheck e = null;
		
		if(!(_e instanceof QAPIM_VersionCheck)){
			getLog().error(ERROR_UNABLE_TO_HANDLE_WEB_EVENT+","+this.getClass().getCanonicalName()+" can't handle event of type "+_e.getClass().getCanonicalName());
			return null;
		}
		else{
			e = (QAPIM_VersionCheck) _e;
			return(onEvent(e));
		}
	}
	
}

