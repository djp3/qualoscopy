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
import java.util.regex.Pattern;

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.event.Event;
import edu.uci.ics.luci.utility.webserver.event.result.api.APIEventResult;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

/*
 * /update/patient:
	request:
		{
			"version", <version>
			"user_id" <user_id>
			"shsid", <hash(session_id+salt_x),
			"shsk", <hash(session_key+salt_x),
			"mr_id", <MRID>,
			"last": <Last Name>,
			"first": <First Name>,
			"dob": <Month/Day/Year>, 01/11/1980
			"gender": <M/F/O>,
			"next_procedure":<Month/Day/Year> 01/11/2016
		}
	return:
		{
			"version", <version>,
			"salt", <salt>,
			"error", <true/false>,
			"errors", [<errors>]
		}

*/
public class QAPIEvent_UpdatePatient extends QAPIEvent_CheckSession implements Cloneable{ 
	
	public static final String genderSyntax = "[MFO]";
	public static final Pattern patternGender = Pattern.compile(genderSyntax);
	
	public static final String dobSyntax = "[1]?[0-9][/][123]?[0-9][/][12][09][0-9][0-9]";
	public static final Pattern patternDOB = Pattern.compile(dobSyntax);
	
	public static final String nextProcedureSyntax = "[1]?[0-9][/][123]?[0-9][/][12][09][0-9][0-9]";
	public static final Pattern patternNextProcedure = Pattern.compile(nextProcedureSyntax);
	
	public static final String ERROR_NULL_PATIENT_ID = "\"patient_id\" was null";
	public static final String ERROR_PATIENT_ID_PATTERN_FAIL = "\"patient_id\" did not parse as a long";
	
	public static final String ERROR_NULL_MR_ID = "\"mr_id\" was null";
	public static final String ERROR_NULL_LAST = "\"last\" was null";
	public static final String ERROR_NULL_FIRST = "\"first\" was null";
	
	public static final String ERROR_NULL_GENDER = "\"gender\" was null";
	public static final String ERROR_GENDER_PATTERN_FAIL = "\"gender\" did not conform to the expected syntax:"+genderSyntax;
	
	public static final String ERROR_NULL_DOB = "\"dob\" was null";
	public static final String ERROR_DOB_PATTERN_FAIL = "\"dob\" did not conform to the expected syntax:"+dobSyntax;
	
	public static final String ERROR_NULL_NEXT_PROCEDURE = "\"next_procedure\" was null";
	public static final String ERROR_NEXT_PROCEDURE_PATTERN_FAIL = "\"next_procedure\" did not conform to the expected syntax:"+nextProcedureSyntax;


	
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_UpdatePatient.class);
		}
		return log;
	}

	
	public QAPIEvent_UpdatePatient(String version, DatastoreInterface db) {
		super(version,db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_UpdatePatient incoming = null;
		if(_incoming instanceof QAPIEvent_UpdatePatient){
			incoming = (QAPIEvent_UpdatePatient) _incoming;
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
		Set<String> _patient_id = r.getParameters().get("patient_id");
		Long patient_id = null;
		if(_patient_id == null){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_PATIENT_ID);
			response.put("errors", errors);
		}
		else{
			try{
				patient_id = Long.valueOf(_patient_id.iterator().next());
			}
			catch(NumberFormatException e){
				error ="true";
				response.put("error", error);
				errors.add("Problem handling "+r.getCommand()+":"+ERROR_PATIENT_ID_PATTERN_FAIL);
				response.put("errors", errors);
			}
		}
		
		Set<String> _mr_id = r.getParameters().get("mr_id");
		String mr_id = null;
		if((_mr_id == null) || ((mr_id = (_mr_id.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_MR_ID);
			response.put("errors", errors);
		}
		
		Set<String> _last = r.getParameters().get("last");
		String last = null;
		if((_last == null) || ((last = (_last.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_LAST);
			response.put("errors", errors);
		}
		
		Set<String> _first = r.getParameters().get("first");
		String first = null;
		if((_first == null) || ((first = (_first.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_FIRST);
			response.put("errors", errors);
		}
		
		Set<String> _gender = r.getParameters().get("gender");
		String gender = null;
		if((_gender == null) || ((gender = (_gender.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_GENDER);
			response.put("errors", errors);
		}
		else{
			if(!patternGender.matcher(gender).matches()){
				error ="true";
				response.put("error", error);
				errors.add("Problem handling "+r.getCommand()+":"+ERROR_GENDER_PATTERN_FAIL);
				response.put("errors", errors);
			}
		}
		
		Set<String> _dob = r.getParameters().get("dob");
		String dob = null;
		if((_dob == null) || ((dob = (_dob.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_DOB);
			response.put("errors", errors);
		}
		else{
			if(!patternDOB.matcher(dob).matches()){
				error ="true";
				response.put("error", error);
				errors.add("Problem handling "+r.getCommand()+":"+ERROR_DOB_PATTERN_FAIL);
				response.put("errors", errors);
			}
		}
		
		Set<String> _next_procedure = r.getParameters().get("next_procedure");
		String next_procedure = null;
		if((_next_procedure == null) || ((next_procedure = (_next_procedure.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_NEXT_PROCEDURE);
			response.put("errors", errors);
		}
		else{
			if(!patternNextProcedure.matcher(next_procedure).matches()){
				error ="true";
				response.put("error", error);
				errors.add("Problem handling "+r.getCommand()+":"+ERROR_NEXT_PROCEDURE_PATTERN_FAIL);
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
				String user_id = r.getParameters().get("user_id").iterator().next();
				
				error = getDB().updatePatient(user_id,patient_id,mr_id,first,last,gender,dob,next_procedure);
				if(ac_id != null){
					response.put("ac_id", ac_id);
				}
				else{
					error = "true";
					response.put("error",error);
					errors.add("Unable to add new patient with user_id:"+user_id+", mr_id:"+mr_id);
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

