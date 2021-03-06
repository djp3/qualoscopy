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

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

public class QAPIEvent_AddProcedure extends QAPIEvent_CheckSession implements Cloneable{ 
	
	public static final String ERROR_NULL_PATIENT_ID = "\"patient_id\" was null";
	public static final String ERROR_PATIENT_ID_PATTERN_FAIL = "\"patient_id\" did not conform to the expected syntax of a long";

	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_AddProcedure.class);
		}
		return log;
	}

	
	public QAPIEvent_AddProcedure(String version, DatastoreInterface db) {
		super(version,db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_AddProcedure incoming = null;
		if(_incoming instanceof QAPIEvent_AddProcedure){
			incoming = (QAPIEvent_AddProcedure) _incoming;
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
		
		/* Get additional parameters */
		Set<String> _patientID = r.getParameters().get("patient_id");
		Long patientID = null;
		if(_patientID == null){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_PATIENT_ID);
			response.put("errors", errors);
		}
		else{
			try{
				patientID = Long.valueOf(_patientID.iterator().next());
			}
			catch(NumberFormatException e){
				error ="true";
				response.put("error", error);
				errors.add("Problem handling "+r.getCommand()+":"+ERROR_PATIENT_ID_PATTERN_FAIL);
				response.put("errors", errors);
			}
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
				String userID = r.getParameters().get("user_id").iterator().next();
				Long procedureID = getDB().addProcedure(userID,patientID);
				if(procedureID != null){
					response.put("procedure_id", procedureID);
				}
				else{
					error = "true";
					response.put("error",error);
					errors.add("Unable to add new procedure with user_id:"+userID+", patient_id:"+patientID);
					response.put("errors", errors);
				}
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

