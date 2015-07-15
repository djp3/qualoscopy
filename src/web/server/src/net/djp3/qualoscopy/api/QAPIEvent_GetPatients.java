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
/*
/get_patients:
        {
                "version", <version>
                "user_id" <user_id>
                "shsid", <hash(session_id+salt_x),
                "shsk", <hash(session_key+salt_x)
        }
        {
                "version", <version>,
                "patients",[
                                        {
                                                "MR": <MR>,
                                                "Last": <Last Name>,
                                                "First": <First Name>,
                                                "DOB": <Month/Day/Year>, 01/11/1980
                                                "Gender": <M/F/O>,
                                                "NextProcedure:<Month/Day/Year> 01/11/2016
                                        },...
                                        ],
                "salt", <salt>
                "error", <true/false>,
                "errors", [<errors>]
        }
*/

package net.djp3.qualoscopy.api;

import java.security.InvalidParameterException;
import java.util.Set;

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.djp3.qualoscopy.datastore.Patient;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

public class QAPIEvent_GetPatients extends QAPIEvent_CheckSession implements Cloneable{ 
	
	public static final String ERROR_NULL_USER_ID = "\"user_id\" was null";
	public static final String ERROR_NULL_SHSID = "\"shsi\" (Salted Hashed Session ID) was null";
	public static final String ERROR_NULL_SHSK = "\"shsk\" (Salted Hashed Session Key) was null";
	public static final String ERROR_NULL_SOURCE = "Internal Error - source was null- not a parameter";
	
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_GetPatients.class);
		}
		return log;
	}

	
	public QAPIEvent_GetPatients(String version, DatastoreInterface db) {
		super(version,db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_GetPatients incoming = null;
		if(_incoming instanceof QAPIEvent_GetPatients){
			incoming = (QAPIEvent_GetPatients) _incoming;
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
				String user_id = (String) response.get("user_id");
				Set<Patient> data = getDB().getPatients(user_id);
				JSONArray patients = new JSONArray();
				for(Patient p:data){
					patients.add(p.toJSON());
				}
				response.put("patients", patients);
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

