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


package net.djp3.qualoscopy.events.handlers;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.events.QEvent;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class QEventHandler {

	public static final String ERROR_TYPE_MISMATCH = "Internal error, event type mismatch";
	public static final String ERROR_PARAMETERS_NOT_CHECKED = "Parameters were not checked before calling onEvent";
	public static final String ERROR_GLOBALS_NULL = "Global variables have not been initialized";
	private boolean parametersChecked = false;

	protected boolean getParametersChecked() {
		return parametersChecked;
	}

	private void setParametersChecked(boolean parametersChecked) {
		this.parametersChecked = parametersChecked;
	}
	

	protected JSONObject checkParametersTypeError(QEvent _event, JSONObject ret) {
		ret.put("error", "true");
		JSONArray errors = new JSONArray();
		errors.add(ERROR_TYPE_MISMATCH+"\n"
				+ this.getClass().getCanonicalName() + " was called with "
				+ _event.getClass().getCanonicalName());
		ret.put("errors", errors);
		return ret;
	}
	
	protected boolean typeMatches(QEvent q){
		return (q instanceof QEvent);
	}
	

	/**
	 * 
	 * @param eventTime
	 * @param event
	 * @return JSONObject with description of error or null if no errors
	 */
	public JSONObject checkParameters(long eventTime, QEvent event) {
		JSONObject ret = new JSONObject();

		// Make sure globals is not null
		GlobalsQualoscopy g = GlobalsQualoscopy.getGlobalsQualoscopy();
		if (g == null) {
			ret.put("error", "true");
			JSONArray errors = new JSONArray();
			errors.add(ERROR_GLOBALS_NULL);
			ret.put("errors", errors);
			return ret;
		}

		this.setParametersChecked(true);

		return null;
	}

	protected JSONObject onEventParameterCheckError() {
		JSONObject ret = new JSONObject();
		ret.put("error", "true");
		JSONArray errors = new JSONArray();
		errors.add(ERROR_PARAMETERS_NOT_CHECKED);
		ret.put("errors", errors);
		return ret;
	}

	public JSONObject onEvent() {
		if (this.getParametersChecked()) {
			this.setParametersChecked(false);
			JSONObject ret = new JSONObject();
			ret.put("error", "false");
			return ret;
		} else {
			return onEventParameterCheckError();
		}
	}


}
