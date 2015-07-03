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


package net.djp3.qualoscopy.events;

import net.djp3.qualoscopy.QEventHandlerResultListener;
import net.minidev.json.JSONObject;

public class ResultChecker implements QEventHandlerResultListener{
	
	private Object semaphore = new Object();
	private Boolean resultOK = null;
	private String expect;
	private JSONObject results;
	
	public Object getSemaphore() {
		return semaphore;
	}
	
	public Boolean getResultOK() {
		return resultOK;
	}

	public JSONObject getResults() {
		return results;
	}

	public ResultChecker(boolean expectError){
		if(expectError){
			this.expect = "true";
		}
		else{
			this.expect = "false";
		}
	}

	@Override
	public void onFinish(JSONObject result) {
		synchronized(semaphore){
			results = result;
			if(result == null){
				resultOK = false;
			}
			else{
				if(result.get("error") == null){
					resultOK = false;
				}
				else{
					if(result.get("error").equals(this.expect)){
						resultOK = true;
					}
					else{
						resultOK = false;
					}
				}
			}
			semaphore.notifyAll();
		}
	}
	
}
