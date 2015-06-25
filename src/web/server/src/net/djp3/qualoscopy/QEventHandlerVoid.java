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
package net.djp3.qualoscopy;

import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.handlers.QEventHandler;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class QEventHandlerVoid extends QEventHandler {

private boolean parametersChecked = false;

private boolean getParametersChecked() {
	return parametersChecked;
}

private void setParametersChecked(boolean parametersChecked) {
	this.parametersChecked = parametersChecked;
}

@Override
public JSONObject checkParameters(long eventTime, QEvent _event) {
	//Check parent 
	JSONObject ret = super.checkParameters(eventTime, _event);
	if(ret != null){
		return ret;
	}
	
	this.setParametersChecked(true);
	return null;
}

@Override
public JSONObject onEvent() {
	JSONObject ret = super.onEvent();
	if(ret.get("error").equals("true")){
		return ret;
	}
	
	if(!this.getParametersChecked()){
		ret = new JSONObject();
		ret.put("error","true");
		JSONArray errors = new JSONArray();
		errors.add("Parameters were not checked before calling onEvent");
		ret.put("errors", errors);
		return ret;
	}
	
	this.setParametersChecked(false);
	
	return ret;
}

}

