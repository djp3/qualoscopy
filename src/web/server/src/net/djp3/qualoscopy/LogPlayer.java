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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogPlayer implements Runnable{
	

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(LogPlayer.class);
		}
		return log;
	}
	
	private QEventWrapperQueuer q;
	private TreeMap<Long, QEventWrapper> events;
	private boolean realTime;
	private long numberEventsFired;
	private ArrayList<EventHandlerResultChecker> results;

	public long getNumberEventsFired() {
		return numberEventsFired;
	}

	public void setNumberEventsFired(long numberEventsFired) {
		this.numberEventsFired = numberEventsFired;
	}
	
	public long getNumberEventsRemaining() {
		return events.size();
	}

	public ArrayList<EventHandlerResultChecker> getResults() {
		return results;
	}

	LogPlayer(String logFileName, QEventWrapperQueuer q,boolean realTime) throws IOException {
		this.q = q;
		this.realTime = realTime;
		
		results = new ArrayList<EventHandlerResultChecker>();
		
		setNumberEventsFired(0);

		BufferedReader reader = null;
		try {
			Path newFile = Paths.get(logFileName);
			reader = Files.newBufferedReader(newFile, Charset.defaultCharset());

			events = new TreeMap<Long, QEventWrapper>();

			String lineFromFile = "";
			while ((lineFromFile = reader.readLine()) != null) {
				JSONObject logEntry = (JSONObject) JSONValue .parse(lineFromFile);
				QEventWrapper ew = QEventWrapper.fromJSON((JSONObject) logEntry);
				events.put(ew.getTimestamp(),ew);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	/*
	public static class MyResultListener implements QEventHandlerResultListener{
		
		private Boolean resultOK = null;
		private JSONArray errors = null;

		public Boolean getResultOK() {
			return resultOK;
		}

		private void setResultOK(Boolean resultOK) {
			this.resultOK = resultOK;
		}
		
		public JSONArray getErrors() {
			return errors;
		}

		public void setErrors(JSONArray errors) {
			this.errors = errors;
		}
		

		@Override
		public void onFinish(JSONObject result) {
			setResultOK(false);
			
			if(result.get("error").equals("true")){
				setResultOK(false);
				setErrors((JSONArray) result.get("errors"));
			}
			if(result.get("error").equals("false")){
				setResultOK(true);
			}
			
		}

	}*/

	@Override
	public void run() {
		long replayStart = System.currentTimeMillis();
		
		if(events.isEmpty()){
			getLog().info("No entries in log to play back");
			return;
		}
		
		Entry<Long, QEventWrapper> first  = events.pollFirstEntry();
		long logStart = first.getKey();
		numberEventsFired++;
		q.onData(first.getValue());
		getLog().info("Starting log play back");
		
		while(!events.isEmpty()){
			Entry<Long, QEventWrapper> current = events.pollFirstEntry();
			
			if(realTime){
				long realElapsed = System.currentTimeMillis() - replayStart;
				long simElapsed = current.getKey() - logStart;
				while(simElapsed > realElapsed){
					try {
						Thread.sleep(simElapsed - realElapsed);
					} catch (InterruptedException e) {
					}
					realElapsed = System.currentTimeMillis() - replayStart;
				}
			}
			
			EventHandlerResultChecker result = new EventHandlerResultChecker();
			QEventWrapper eventWrapper = current.getValue();
			eventWrapper.addResultListener(result);
			results.add(result);
			
			numberEventsFired++;
			q.onData(current.getValue());
		}
		
		
	}

}
