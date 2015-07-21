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
import java.util.Map;
import java.util.Set;

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.djp3.qualoscopy.datastore.Procedure;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;


public class QAPIEvent_GetPatientProcedures extends QAPIEvent_CheckSession implements Cloneable{ 
	
	public static final String ERROR_NULL_PATIENT_ID = "\"patient_id\" was null";
	public static final String ERROR_PATIENT_ID_PATTERN_FAIL = "\"patient_id\" did not conform to the expected syntax of a long";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_GetPatientProcedures.class);
		}
		return log;
	}

	
	public QAPIEvent_GetPatientProcedures(String version, DatastoreInterface db) {
		super(version,db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_GetPatientProcedures incoming = null;
		if(_incoming instanceof QAPIEvent_GetPatientProcedures){
			incoming = (QAPIEvent_GetPatientProcedures) _incoming;
			super.set(incoming);
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
		Set<String> _patient_id = r.getParameters().get("patient_id");
		String patient_id = null;
		if((_patient_id == null) || ((patient_id = (_patient_id.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_PATIENT_ID);
			response.put("errors", errors);
		}
		
		if(error.equals("false")){
			/* Clean up from session checking */
			String valid = (String) response.get("valid");
			if(valid.equals("false")){
				response.remove("valid");
				error = "true";
				response.put("error",error);
				errors.add("Session did not validate");
				response.put("errors", errors);
			}
			else{
				response.remove("valid");
				String user_id = r.getParameters().get("user_id").iterator().next();
				Map<String, Procedure> data = getDB().getPatientProcedures(user_id,patient_id);
				JSONArray procedures = new JSONArray();
				for(Procedure p:data.values()){
					procedures.add(p.toJSON());
				}
				response.put("procedures", procedures);
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

