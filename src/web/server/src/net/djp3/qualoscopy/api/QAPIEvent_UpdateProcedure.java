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
import java.util.Map.Entry;
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

public class QAPIEvent_UpdateProcedure extends QAPIEvent_CheckSession implements Cloneable{ 
	
	public static final String ERROR_NULL_PATIENT_ID = "\"patient_id\" was null";
	public static final String ERROR_NULL_PROCEDURE_ID = "\"procedure_id\" was null";

	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QAPIEvent_UpdateProcedure.class);
		}
		return log;
	}
	
	/**
	 * Added for hashCode and equals autogeneration
	 */
	private final long serialVersionUID = -1516866063389217495L;

	
	public QAPIEvent_UpdateProcedure(String version, DatastoreInterface db) {
		super(version,db);
	}
	
	@Override
	public void set(Event _incoming) {
		QAPIEvent_UpdateProcedure incoming = null;
		if(_incoming instanceof QAPIEvent_UpdateProcedure){
			incoming = (QAPIEvent_UpdateProcedure) _incoming;
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
		String patient_id = null;
		if((_patient_id == null) || ((patient_id = (_patient_id.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_PATIENT_ID);
			response.put("errors", errors);
		}
		
		Set<String> _procedure_id = r.getParameters().get("procedure_id");
		String procedure_id = null;
		if((_procedure_id == null) || ((procedure_id = (_procedure_id.iterator().next())) == null)){
			error ="true";
			response.put("error", error);
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_PROCEDURE_ID);
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
				
				Procedure procedure = new Procedure();
				procedure.setProcedureID(procedure_id);
				
				//Get optional parameters
				Set<String> _acID = r.getParameters().get("ac_id");
				String acID = null;
				if((_acID != null) && ((acID = (_acID.iterator().next())) != null)){
					String errorMessage = procedure.setAcID(acID);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}
				
				Set<String> _dos = r.getParameters().get("date_time_of_service");
				String dos = null;
				if((_dos != null) && ((dos = (_dos.iterator().next())) != null)){
					String errorMessage = procedure.setDateTimeOfService(dos);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}
				
				Set<String> _facultyID = r.getParameters().get("faculty_id");
				String facultyID = null;
				if((_facultyID != null) && ((facultyID = (_facultyID.iterator().next())) != null)){
					String errorMessage = procedure.setFacultyID(facultyID);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}
				

				Set<String> _location = r.getParameters().get("location");
				String location = null;
				if((_location != null) && ((location = (_location.iterator().next())) != null)){
					String errorMessage = procedure.setLocation(location);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _fellow = r.getParameters().get("fellow");
				String fellow = null;
				if((_fellow != null) && ((fellow = (_fellow.iterator().next())) != null)){
					String errorMessage = procedure.setFellow(fellow);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _pre_drug = r.getParameters().get("pre_drug");
				String pre_drug = null;
				if((_pre_drug!= null) && ((pre_drug= (_pre_drug.iterator().next())) != null)){
					String errorMessage = procedure.setPreDrug(pre_drug);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _prep_liters= r.getParameters().get("prep_liters");
				String prep_liters= null;
				if((_prep_liters!= null) && ((prep_liters= (_prep_liters.iterator().next())) != null)){
					String errorMessage = procedure.setPrepLiters(prep_liters);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _split_prep= r.getParameters().get("split_prep");
				String split_prep= null;
				if((_split_prep!= null) && ((split_prep= (_split_prep.iterator().next())) != null)){
					String errorMessage = procedure.setSplitPrep(split_prep);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _bisacodyl= r.getParameters().get("bisacodyl");
				String bisacodyl= null;
				if((_bisacodyl!= null) && ((bisacodyl= (_bisacodyl.iterator().next())) != null)){
					String errorMessage = procedure.setBisacodyl(bisacodyl);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _last_colon= r.getParameters().get("last_colon");
				String last_colon= null;
				if((_last_colon!= null) && ((last_colon= (_last_colon.iterator().next())) != null)){
					String errorMessage = procedure.setLastColon(last_colon);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _primary_indication= r.getParameters().get("primary_indication");
				String primary_indication= null;
				if((_primary_indication!= null) && ((primary_indication= (_primary_indication.iterator().next())) != null)){
					String errorMessage = procedure.setPrimaryIndication(primary_indication);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _other_indication= r.getParameters().get("other_indication");
				String other_indication= null;
				if((_other_indication!= null) && ((other_indication= (_other_indication.iterator().next())) != null)){
					String errorMessage = procedure.setOtherIndication(other_indication);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _scope= r.getParameters().get("scope");
				String scope= null;
				if((_scope!= null) && ((scope= (_scope.iterator().next())) != null)){
					String errorMessage = procedure.setScope(scope);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _endocuff= r.getParameters().get("endocuff");
				String endocuff= null;
				if((_endocuff!= null) && ((endocuff= (_endocuff.iterator().next())) != null)){
					String errorMessage = procedure.setEndocuff(endocuff);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _cap_assisted= r.getParameters().get("cap_assisted");
				String cap_assisted= null;
				if((_cap_assisted!= null) && ((cap_assisted= (_cap_assisted.iterator().next())) != null)){
					String errorMessage = procedure.setCapAssisted(cap_assisted);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _underwater= r.getParameters().get("underwater");
				String underwater= null;
				if((_underwater!= null) && ((underwater= (_underwater.iterator().next())) != null)){
					String errorMessage = procedure.setUnderwater(underwater);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _sedation_level= r.getParameters().get("sedation_level");
				String sedation_level= null;
				if((_sedation_level!= null) && ((sedation_level= (_sedation_level.iterator().next())) != null)){
					String errorMessage = procedure.setSedationLevel(sedation_level);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _versed= r.getParameters().get("versed");
				String versed= null;
				if((_versed!= null) && ((versed= (_versed.iterator().next())) != null)){
					String errorMessage = procedure.setVersed(versed);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _fentanyl= r.getParameters().get("fentanyl");
				String fentanyl= null;
				if((_fentanyl!= null) && ((fentanyl= (_fentanyl.iterator().next())) != null)){
					String errorMessage = procedure.setFentanyl(fentanyl);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _demerol= r.getParameters().get("demerol");
				String demerol= null;
				if((_demerol!= null) && ((demerol= (_demerol.iterator().next())) != null)){
					String errorMessage = procedure.setDemerol(demerol);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _benadryl= r.getParameters().get("benadryl");
				String benadryl= null;
				if((_benadryl!= null) && ((benadryl= (_benadryl.iterator().next())) != null)){
					String errorMessage = procedure.setBenadryl(benadryl);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _extent= r.getParameters().get("extent");
				String extent= null;
				if((_extent!= null) && ((extent= (_extent.iterator().next())) != null)){
					String errorMessage = procedure.setExtent(extent);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _prep_quality_left= r.getParameters().get("prep_quality_left");
				String prep_quality_left= null;
				if((_prep_quality_left!= null) && ((prep_quality_left= (_prep_quality_left.iterator().next())) != null)){
					String errorMessage = procedure.setPrepQualityLeft(prep_quality_left);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _prep_quality_mid= r.getParameters().get("prep_quality_mid");
				String prep_quality_mid= null;
				if((_prep_quality_mid!= null) && ((prep_quality_mid= (_prep_quality_mid.iterator().next())) != null)){
					String errorMessage = procedure.setPrepQualityMid(prep_quality_mid);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _prep_quality_right= r.getParameters().get("prep_quality_right");
				String prep_quality_right= null;
				if((_prep_quality_right!= null) && ((prep_quality_right= (_prep_quality_right.iterator().next())) != null)){
					String errorMessage = procedure.setPrepQualityRight(prep_quality_right);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _time_insertion= r.getParameters().get("time_insertion");
				String time_insertion= null;
				if((_time_insertion!= null) && ((time_insertion= (_time_insertion.iterator().next())) != null)){
					String errorMessage = procedure.setTimeInsertionString(time_insertion);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _time_begin_withdrawal= r.getParameters().get("time_begin_withdrawal");
				String time_begin_withdrawal= null;
				if((_time_begin_withdrawal!= null) && ((time_begin_withdrawal= (_time_begin_withdrawal.iterator().next())) != null)){
					String errorMessage = procedure.setTimeBeginWithdrawalString(time_begin_withdrawal);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}

				Set<String> _time_scope_withdrawn= r.getParameters().get("time_scope_withdrawn");
				String time_scope_withdrawn= null;
				if((_time_scope_withdrawn!= null) && ((time_scope_withdrawn= (_time_scope_withdrawn.iterator().next())) != null)){
					String errorMessage = procedure.setTimeScopeWithdrawnString(time_scope_withdrawn);
					if(errorMessage != null){
						error = "true";
						response.put("error",error);
						errors.add(errorMessage);
						response.put("errors", errors);
					}
				}


				
				
				String userID = r.getParameters().get("user_id").iterator().next();
				String errorMessage = getDB().updateProcedure(userID,patient_id,procedure);
				if(errorMessage != null){
					error = "true";
					response.put("error",error);
					errors.add(errorMessage);
					response.put("errors", errors);
				}
				else{
					Procedure localprocedure = getDB().getProcedure(userID,patient_id,procedure.getProcedureID());
					response.put("patient_id", patient_id);
					response.put("procedure_id", localprocedure.getProcedureID());
					response.put("procedure", localprocedure.toJSON());
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
		if (!(obj instanceof QAPIEvent_UpdateProcedure)) {
			return false;
		}
		QAPIEvent_UpdateProcedure other = (QAPIEvent_UpdateProcedure) obj;
		if (serialVersionUID != other.serialVersionUID) {
			return false;
		}
		return true;
	}
	
	
	
}

