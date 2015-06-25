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

private boolean parametersChecked = false;


private boolean getParametersChecked() {
	return parametersChecked;
}

private void setParametersChecked(boolean parametersChecked) {
	this.parametersChecked = parametersChecked;
}

/** 
 * 
 * @param eventTime
 * @param event
 * @return JSONObject with description of error or null if no errors
 */
public JSONObject checkParameters(long eventTime, QEvent event){
	
	JSONObject ret = new JSONObject();
	
	//Make sure the world name is not null
	if(event.getWorldName() == null){
		ret.put("error","true");
		JSONArray errors = new JSONArray();
		errors.add("World can't have a null name");
		ret.put("errors", errors);
		return ret;
	}
	
	//Make sure the world password is not null
	//* We know this won't be null */
	/*
	if(event.getWorldHashedPassword() == null){
		ret.put("error","true");
		JSONArray errors = new JSONArray();
		errors.add("World can't have a null password");
		ret.put("errors", errors);
		return ret;
	}*/
	
	//Make sure globals is not null
	GlobalsQualoscopy g = GlobalsQualoscopy.getGlobalsTerraTower();
	if(g == null){
		ret.put("error","true");
		JSONArray errors = new JSONArray();
		errors.add("Global variables have not been initiazed");
		ret.put("errors", errors);
		return ret;
	}
	
	this.setParametersChecked(true);
	
	return null;
}


public JSONObject onEvent(){
	if(this.getParametersChecked()){
		this.setParametersChecked(false);
		JSONObject ret = new JSONObject();
		ret.put("error","false");
		return ret;
	}
	else{
		JSONObject ret = new JSONObject();
		ret.put("error","true");
		JSONArray errors = new JSONArray();
		errors.add("Parameters were not checked before calling onEvent");
		ret.put("errors", errors);
		return ret;
	}
}

}

