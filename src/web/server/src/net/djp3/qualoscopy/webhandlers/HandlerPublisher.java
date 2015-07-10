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

import net.djp3.qualoscopy.EventHandlerResultChecker;
import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;
import edu.uci.ics.luci.utility.webserver.output.response.Response;

/**
 * @author djp3
 *
 */
public class HandlerPublisher extends HandlerAbstract{
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerPublisher.class);
		}
		return log;
	}
	
	private QEventWrapperQueuer eventPublisher;
	
	protected QEventWrapperQueuer getEventPublisher() {
		return eventPublisher;
	}
	

	protected JSONObject processRequestEvent(QEventType type, QEvent q){
		EventHandlerResultChecker resultChecker = new EventHandlerResultChecker();
		QEventWrapper event = new QEventWrapper(type,q,resultChecker);
		getEventPublisher().onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResults() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		return(resultChecker.getResults());
	}

	/**
	 * 
	 * @param eventPublisher, the queue to put events on
	 */
	public HandlerPublisher(QEventWrapperQueuer eventPublisher) {
		super();
		this.eventPublisher = eventPublisher;
	}
	
	@Override
	public HandlerAbstract copy() {
		return new HandlerPublisher(getEventPublisher());
	}

	/**
	 *	This function collects any parameters required from the incoming request and stores
	 * them locally. 
	 * @param r
	 * @return An array of strings which describe an errors encountered.  If there are
	 * errors then processing stops.
	 */
	protected JSONArray getParameters(Request r){
		JSONArray errors = new JSONArray();
		
		return errors;
	}

	
	
	/**
	 * This function evaluates the incoming request for any syntax errors and if there
	 * are any present returns the appropriate JSONObject.  Fast check errors should be done
	 * immediately.
	 * If this is an event that requires longer or more complex processing then it should be
	 * queued for event processing which is able to * handle longer running validations.
	 * @param r, the incoming Request
	 * @return error = false or error = true and errors is an Array of Strings describing
	 *  the errors
	 */
	protected JSONObject constructReply(Request r){
		JSONObject ret = new JSONObject();
		ret.put("error", "false");
		return ret;
	}
	
	
	@Override
	public Response handle(Request request, Output o) {
		getLog().info("Handling request");
		
		Response response = o.makeOutputChannelResponse();
		
		JSONObject reply = constructReply(request);
		
		response.setStatus(Response.Status.OK);
		response.setDataType(Response.DataType.JSON);
		response.setBody(wrapCallback(request.getParameters(),reply.toString()));
		
		return response;
	}



}

