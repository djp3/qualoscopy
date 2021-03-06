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

import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.QEventVoid;
import net.minidev.json.JSONObject;


public class QEventHandlerVoid extends QEventHandler {
	
	private boolean parametersChecked = false;

	@Override
	protected boolean getParametersChecked() {
		return super.getParametersChecked() || parametersChecked;
	}

	private void setParametersChecked(boolean parametersChecked) {
		this.parametersChecked = parametersChecked;
	}
	
	
	protected boolean typeMatches(QEvent q){
		return (q instanceof QEventVoid);
	}

	@Override
	public JSONObject checkParameters(long eventTime, QEvent _event) {
		// Check parent
		JSONObject ret = super.checkParameters(eventTime, _event);
		if (ret != null) {
			return ret;
		}

		ret = new JSONObject();

		if (! (typeMatches(_event)) ) {
			return checkParametersTypeError(_event, ret);
		}

		this.setParametersChecked(true);

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

		/* Not sure why I'm doing this exactly.  It prevents an event from being executed twice
		 * without being rechecked */
		this.setParametersChecked(false);
		
		return ret;
	}



}
