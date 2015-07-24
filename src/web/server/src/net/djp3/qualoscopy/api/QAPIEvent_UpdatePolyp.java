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
import net.djp3.qualoscopy.datastore.Polyp;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

public class QAPIEvent_UpdatePolyp extends QAPIEvent_CheckSession implements Cloneable{ 
	
	public static final String ERROR_NULL_PROCEDURE_ID = "\"procedure_id\" was null";
	public static final String ERROR_NULL_POLYP_ID = "\"polyp_id\" was null";

	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_UpdatePolyp.class);
		}
		return log;
	}
	
	/**
	 * Added for hashCode and equals autogeneration
	 */
	private final long serialVersionUID = -1516866163389217495L;

	
	public QAPIEvent_UpdatePolyp(String version, DatastoreInterface db) {
		super(version,db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_UpdatePolyp incoming = null;
		if(_incoming instanceof QAPIEvent_UpdatePolyp){
			incoming = (QAPIEvent_UpdatePolyp) _incoming;
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
		Set<String> _procedure_id = r.getParameters().get("procedure_id");
		String procedure_id = null;
		if((_procedure_id == null) || ((procedure_id = (_procedure_id.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_PROCEDURE_ID);
			response.put("errors", errors);
		}
		
		Set<String> _polyp_id = r.getParameters().get("polyp_id");
		String polyp_id = null;
		if((_polyp_id == null) || ((polyp_id = (_polyp_id.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_POLYP_ID);
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
				
				Polyp polyp = new Polyp();
				polyp.setPolypID(polyp_id);
				
				//Get optional parameters
				Set<String> _time_removed= r.getParameters().get("time_removed");
				String time_removed= null;
				if((_time_removed!= null) && ((time_removed= (_time_removed.iterator().next())) != null)){
					String errorMessage = polyp.setTimeRemovedString(time_removed);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				
				String userID = r.getParameters().get("user_id").iterator().next();
				String errorMessage = getDB().updatePolyp(userID,procedure_id,polyp);
				if(errorMessage != null){
					error = "true";
					response.put("error",error);
					errors.add(errorMessage);
					response.put("errors", errors);
				}
				else{
					Polyp localpolyp = getDB().getPolyp(userID,procedure_id,polyp.getPolypID());
					response.put("procedure_id",procedure_id);
					response.put("polyp_id",localpolyp.getPolypID());
					response.put("polyp", localpolyp.toJSON());
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (int) (serialVersionUID ^ (serialVersionUID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof QAPIEvent_UpdatePolyp)) {
			return false;
		}
		QAPIEvent_UpdatePolyp other = (QAPIEvent_UpdatePolyp) obj;
		if (serialVersionUID != other.serialVersionUID) {
			return false;
		}
		return true;
	}
	
	
	
}

