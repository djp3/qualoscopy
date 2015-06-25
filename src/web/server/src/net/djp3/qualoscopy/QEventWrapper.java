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


import java.util.ArrayList;
import java.util.List;

import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.handlers.QEventHandler;
import net.minidev.json.JSONObject;

/**
* Setting the eventType and the event is required
* @author djp3
*
*/
public class QEventWrapper {

/* The basic data encapsulated by the wrapper */
private long timestamp;		//Official time of the event
private QEventType eventType;
private QEvent event;
private transient QEventHandler handler;
private transient List<QEventHandlerResultListener> resultListeners;

/* Getters and Setters */
public long getTimestamp() {
	return timestamp;
}

public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
}

public QEventType getEventType() {
	return eventType;
}

public void setEventType(QEventType eventType) {
	this.eventType = eventType;
}

public QEvent getEvent(){
	return event;
}

public void setEvent(QEvent event){
	this.event = event;
	resetEventHandler();
}

public QEventHandler getHandler(){
	return handler;
}

private void setHandler(QEventHandler handler){
	this.handler = handler;
}

/** This is only helpful for testing
 * 
 */
public void resetEvent(){
	switch(this.getEventType()){
	case BUILD_TOWER: this.setEvent(new QEvent((String)null, (String)null));
		break;
	case BURN_BOMB_FUSE: this.setEvent(new QEvent((String)null,(String)null));
		break;
	case VOID: this.setEvent(new QEventVoid());
		break;
	default: this.setEvent(null);
		break;
	
	}
}
public void resetEventHandler(){
	switch(this.getEventType()){
	case BUILD_TOWER: this.setHandler(new QEventHandler());
		break;
	case BURN_BOMB_FUSE: this.setHandler(new QEventHandler());
		break;
	case VOID: this.setHandler(new QEventHandlerVoid());
		break;
	default: this.setHandler(null);
		break;
	
	}
}

public void checkConsistency(){
	boolean problem = false;
	switch(this.getEventType()){
	case BUILD_TOWER: problem = (!(this.getEvent() instanceof QEvent));
						   problem |= (!(this.getHandler() instanceof QEventHandler));
		break;
	case BURN_BOMB_FUSE: problem = (!(this.getEvent() instanceof QEvent));
						   problem |= (!(this.getHandler() instanceof QEventHandler));
		break;
	case VOID: problem = (!(this.getEvent() instanceof QEventVoid));
						   problem |= (!(this.getHandler() instanceof QEventHandlerVoid));
		break;
	default:
		problem = true;
		break;
	}
	if(problem){
		throw new IllegalArgumentException("EventType:"+this.getEventType()+" is inconsistent with Event:"+this.getEvent().getClass().getCanonicalName());
	}
}

public List<QEventHandlerResultListener>getResultListeners(){
	return this.resultListeners;
}

public void addResultListener(QEventHandlerResultListener rl){
	if(rl != null){
		resultListeners.add(rl);
	}
}

public void setResultListeners(List<QEventHandlerResultListener> rl){
	this.resultListeners = rl;
}


public QEventWrapper(QEventType eventType,QEvent event,QEventHandlerResultListener resultListener){
	this(System.currentTimeMillis(),eventType,event,resultListener);
}


QEventWrapper(long eventTime,QEventType eventType,QEvent event,QEventHandlerResultListener resultListener){
	this.setTimestamp(eventTime);
	
	if(eventType == null){
		throw new IllegalArgumentException("eventType can't be null");
	}
	this.setEventType(eventType);
	
	//Event can be null because it has to be initialized for disruptor
	//before an event exists
	this.setEvent(event);
	
	this.setResultListeners(new ArrayList<QEventHandlerResultListener>());
	this.addResultListener(resultListener);
}


QEventWrapper(QEventType eventType,QEvent event,List<QEventHandlerResultListener> resultListeners){
	this(System.currentTimeMillis(),eventType,event,resultListeners);
}


QEventWrapper(long eventTime,QEventType eventType,QEvent event,List<QEventHandlerResultListener> resultListeners){
	
	this.setTimestamp(eventTime);
	if(eventType == null){
		throw new IllegalArgumentException("eventType can't be null");
	}
	this.setEventType(eventType);
	
	//Event can be null because it has to be initialized for disruptor
	//before an event exists
	this.setEvent(event);
	
	if(resultListeners == null){
		this.setResultListeners(new ArrayList<QEventHandlerResultListener>());
	}
	else{
		this.setResultListeners(resultListeners);
	}
}

void set(QEventWrapper ttEventWrapper){
	this.setTimestamp(ttEventWrapper.getTimestamp());
	this.setEventType(ttEventWrapper.getEventType());
	this.setEvent(ttEventWrapper.getEvent());
	this.setHandler(ttEventWrapper.getHandler());
	this.getResultListeners().clear();
	this.getResultListeners().addAll(ttEventWrapper.getResultListeners());
	checkConsistency();
}

@Override
public String toString(){
	String localEventType = "";
	String localEvent = "";
	if(this.getEventType() == null){
		localEventType = "null";
	}
	if(this.getEvent() == null){
		localEvent = "null";
	}
	return(localEventType+":"+localEvent);
}

public JSONObject toJSON(){
	JSONObject ret = new JSONObject();
	ret.put("timestamp",""+getTimestamp());
	ret.put("eventType",getEventType().toString());
	ret.put("event", getEvent().toJSON());
	return ret;
}

static public QEventWrapper fromJSON(JSONObject in){
	long eventTime = Long.parseLong((String) in.get("timestamp"));
	QEventType eventType = QEventType.fromString((String) in.get("eventType"));
	QEvent event;
	switch(eventType){
		case BUILD_TOWER: event = QEvent.fromJSON((JSONObject)in.get("event"));
			break;
		case BURN_BOMB_FUSE: event = QEvent.fromJSON((JSONObject)in.get("event"));
			break;
		case VOID: event = QEventVoid.fromJSON((JSONObject)in.get("event"));
			break;
		default:event = null;
			break;
	}
	QEventWrapper ret = new QEventWrapper(eventTime,eventType,event,(QEventHandlerResultListener)null);
	return ret;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((event == null) ? 0 : event.hashCode());
	result = prime * result
			+ ((eventType == null) ? 0 : eventType.hashCode());
	result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj) {
		return true;
	}
	if (obj == null) {
		return false;
	}
	if (!(obj instanceof QEventWrapper)) {
		return false;
	}
	QEventWrapper other = (QEventWrapper) obj;
	if (event == null) {
		if (other.event != null) {
			return false;
		}
	} else if (!event.equals(other.event)) {
		return false;
	}
	if (eventType != other.eventType) {
		return false;
	}
	if (timestamp != other.timestamp) {
		return false;
	}
	return true;
}




}

