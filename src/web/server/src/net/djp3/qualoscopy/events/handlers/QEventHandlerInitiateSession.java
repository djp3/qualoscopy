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

import net.djp3.qualoscopy.api.HandlerCheckSession;
import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.QEventCheckSession;
import net.djp3.qualoscopy.events.QEventCheckVersion;
import net.djp3.qualoscopy.events.QEventInitiateSession;
import net.djp3.qualoscopy.webhandlers.HandlerCheckVersion;
import net.minidev.json.JSONObject;

public class QEventHandlerInitiateSession extends QEventHandlerCheckVersion {


	private boolean parametersChecked = false;
	protected String source = null;

	@Override
	protected boolean getParametersChecked() {
		return super.getParametersChecked() || parametersChecked;
	}

	private void setParametersChecked(boolean parametersChecked) {
		this.parametersChecked = parametersChecked;
	}
	
	
	protected boolean typeMatches(QEvent q){
		return (q instanceof QEventInitiateSession);
	}
	
	
	@Override
	public JSONObject checkParameters(long eventTime, QEvent _event) {
		// Check parent
		JSONObject ret = super.checkParameters(eventTime, _event);
		if (ret != null) {
			return ret;
		}
		
		QEventInitiateSession event = null;
		if (typeMatches(_event)) {
			event = ((QEventInitiateSession) _event);
			source = event.getSource();
			
			/* TODO: Make sure that the sessionID matches the user and the source */
			/* This will require looking it up in the database */
			this.setParametersChecked(true);
			return null;
		} else {
			return checkParametersTypeError(_event, ret);
		}

		return null;
	}

	@Override
	public JSONObject onEvent() {

		JSONObject ret = super.onEvent();
		if (ret.get("error").equals("true")) {
			return ret;
		}

		if (!this.getParametersChecked()) {
			return onEventParameterCheckError();
		}

		try{
			ret.put("session_id",DatastoreInterface.getInstance().createAndStoreInitialSessionID(null));
			ret.put("session_salt",DatastoreInterface.getInstance().createAndStoreSalt(null));
			
			return ret;
		}
		finally{
			/* Not sure why I'm doing this exactly.  It prevents an event from being executed twice
			 * without being rechecked */
			this.setParametersChecked(false);
		}
	}

}
