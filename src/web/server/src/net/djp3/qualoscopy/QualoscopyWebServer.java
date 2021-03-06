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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.djp3.qualoscopy.api.QAPIEvent_AddPatient;
import net.djp3.qualoscopy.api.QAPIEvent_AddProcedure;
import net.djp3.qualoscopy.api.QAPIEvent_CheckSession;
import net.djp3.qualoscopy.api.QAPIEvent_GetPatientProcedures;
import net.djp3.qualoscopy.api.QAPIEvent_GetPatients;
import net.djp3.qualoscopy.api.QAPIEvent_InitiateSession;
import net.djp3.qualoscopy.api.QAPIEvent_KillSession;
import net.djp3.qualoscopy.api.QAPIEvent_Login;
import net.djp3.qualoscopy.api.QAPIEvent_UpdatePatient;
import net.djp3.qualoscopy.api.QAPIEvent_VersionCheck;
import net.djp3.qualoscopy.datastore.DatastoreInterface;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent_Shutdown;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent_Version;
import edu.uci.ics.luci.utility.webserver.input.channel.socket.HTTPInputOverSocket;

public class QualoscopyWebServer {
	private static int port = 9021;
	
	public final static String VERSION="0.1";

	private static transient volatile Logger log = null;

	
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(QualoscopyWebServer.class);
		}
		return log;
	}
	
	
	
	public static void main(String[] args) throws ConfigurationException {

		/* Try and turn off log messages about the log messaging system */
		System.setProperty("Log4jDefaultStatusLevel","error");
		
		/* Get configurations */
		Configuration config = new PropertiesConfiguration( "Qualoscopy.properties");

		/* Set up the global variable */
		GlobalsQualoscopy globalsQualoscopy = new GlobalsQualoscopy(VERSION,false);
		globalsQualoscopy.setTesting(false);
		Globals.setGlobals(globalsQualoscopy);
		
		/* Set up a log file */
		String logFileName = config.getString("event.logfile");
		File logFile = new File(logFileName);
		if(logFile.exists()){
			logFile.delete();
		}
		try {
			logFile.createNewFile();
		} catch (IOException e1) {
			getLog().error(e1);
		}
		
		WebServer ws = null;
		HashMap<String, APIEvent> requestHandlerRegistry;

		try {
			boolean secure = true;
			
			HTTPInputOverSocket inputChannel = new HTTPInputOverSocket(port,secure);

					
			requestHandlerRegistry = new HashMap<String,APIEvent>();
			DatastoreInterface db = new DatastoreInterface(null);
			// Null is a default Handler
			requestHandlerRegistry.put(null, new APIEvent_Version(VERSION));
			requestHandlerRegistry.put("", new APIEvent_Version(VERSION));
			requestHandlerRegistry.put("/", new APIEvent_Version(VERSION));
			requestHandlerRegistry.put("/version", new QAPIEvent_VersionCheck(VERSION));
			requestHandlerRegistry.put("/session/initiate", new QAPIEvent_InitiateSession(VERSION,db));
			requestHandlerRegistry.put("/session/check", new QAPIEvent_CheckSession(VERSION,db));
			requestHandlerRegistry.put("/session/kill", new QAPIEvent_KillSession(VERSION,db));
			requestHandlerRegistry.put("/get/patients", new QAPIEvent_GetPatients(VERSION,db));
			requestHandlerRegistry.put("/get/patient/procedures", new QAPIEvent_GetPatientProcedures(VERSION,db));
			requestHandlerRegistry.put("/add/patient", new QAPIEvent_AddPatient(VERSION,db));
			requestHandlerRegistry.put("/update/patient", new QAPIEvent_UpdatePatient(VERSION,db));
			requestHandlerRegistry.put("/add/procedure", new QAPIEvent_AddProcedure(VERSION,db));
			requestHandlerRegistry.put("/login", new QAPIEvent_Login(VERSION,db));
			requestHandlerRegistry.put("/shutdown", new APIEvent_Shutdown(Globals.getGlobals()));
						
			AccessControl accessControl = new AccessControl();
			accessControl.reset();
			ws = new WebServer(inputChannel, requestHandlerRegistry, accessControl,logFile);
			
			Globals.getGlobals().addQuittable(ws);
			
			ws.start();

		} catch (RuntimeException e) {
			e.printStackTrace();
			Globals.getGlobals().setQuitting(true);
			return;
		}

		synchronized(ws){
			while(!Globals.getGlobals().isQuitting()){
				try {
					ws.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		getLog().info("Qualoscopy shutdown");
	}
}


