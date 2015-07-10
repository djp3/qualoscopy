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


package net.djp3.qualoscopy.webhandlers;

import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.input.request.Request;

/**
 * @author djp3
 *
 */
public class HandlerCheckSource extends HandlerCheckVersion{
	
	public static final String ERROR_NULL_SOURCE = "Internal error: source address was null";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerCheckSource.class);
		}
		return log;
	}
	
	/** The originator's source, for example the ip address.  If this changes then
	 * a session id is invalidated 
	 */
	private String source = null;
	private DatastoreInterface di = null;;

	public HandlerCheckSource(QEventWrapperQueuer eventPublisher,DatastoreInterface di) {
		super(eventPublisher);
		this.setDatastoreInterface(di);
	}
	
	@Override
	public HandlerAbstract copy() {
		return new HandlerCheckSource(getEventPublisher(), getDatastoreInterface());
	}

	public String getSource(){
		return source;
	}
	
	private String setSource(String source){
		this.source = source;
		return getSource();
	}
	

	private DatastoreInterface getDatastoreInterface() {
		return di;
	}

	private DatastoreInterface setDatastoreInterface(DatastoreInterface di) {
		this.di = di;
		return di;
	}

	@Override
	protected JSONArray getParameters(Request r){
		JSONArray errors = new JSONArray();
		
		String _source = r.getSource();
		if(setSource(_source) == null){
			errors.add("Problem handling "+r.getCommand()+":"+ERROR_NULL_SOURCE);
		} 
		
		return errors;
	}

	
	@Override
	protected JSONObject constructReply(Request r){
		JSONObject ret = super.constructReply(r);
		if(ret.get("error").equals("false")){
			JSONArray errors = getParameters(r);
			if(errors.size() != 0){
				ret.put("error", "true");
				ret.put("errors", errors);
			}
		}
		return ret;
	}

}

