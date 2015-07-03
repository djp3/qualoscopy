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

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperFactory;
import net.djp3.qualoscopy.events.QEventWrapperHandler;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.djp3.qualoscopy.webhandlers.HandlerInitiateSession;
import net.djp3.qualoscopy.webhandlers.HandlerVersionChecked;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerShutdown;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;
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
	
	private static QEventWrapperQueuer eventPublisher;
	
	/**
	 * Create Event Disruptor
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static QEventWrapperQueuer createEventQueue(String logFile) {
		// Executor that will be used to construct new threads for consumers
	    Executor executor = Executors.newCachedThreadPool();
	
	    // The factory for the event
	    QEventWrapperFactory factory = new QEventWrapperFactory();
	
	    // Specify the size of the ring buffer, must be power of 2.
	    int bufferSize = 1024;
	
	    // Construct the Disruptor
	    Disruptor<QEventWrapper> disruptor = new Disruptor<QEventWrapper>(factory, bufferSize, executor);
	
	    // Connect the handler
	    disruptor.handleEventsWith(new QEventWrapperHandler());
	        
	    // Start the Disruptor, starts all threads running
	    disruptor.start();
	
	    // Get the ring buffer from the Disruptor to be used for publishing.
	    RingBuffer<QEventWrapper> ringBuffer = disruptor.getRingBuffer();
	
	    QEventWrapperQueuer localEventPublisher = new QEventWrapperQueuer(disruptor,ringBuffer,logFile);
	    
	    return(localEventPublisher);
	}

	
	
	public static void main(String[] args) throws ConfigurationException {

		/* Try and turn off log messages about the log messaging system */
		System.setProperty("Log4jDefaultStatusLevel","error");
		
		/* Get configurations */
		Configuration config = new PropertiesConfiguration( "Qualoscopy.properties");

		/* Set up the global variable */
		GlobalsQualoscopy globalsQualoscopy = new GlobalsQualoscopy(VERSION,false);
		Globals.setGlobals(globalsQualoscopy);
		
		/* Set up an event queue with logging */
		String logFileName = config.getString("event.logfile");
		eventPublisher = createEventQueue(logFileName);
		Globals.getGlobals().addQuittable(eventPublisher);
		
		WebServer ws = null;
		HashMap<String, HandlerAbstract> requestHandlerRegistry;

		try {
			boolean secure = true;
			
			HTTPInputOverSocket inputChannel = new HTTPInputOverSocket(port,secure);
					
			// Null is a default Handler
			requestHandlerRegistry = new HashMap<String, HandlerAbstract>();
			requestHandlerRegistry.put(null, new HandlerVersion(VERSION));
			requestHandlerRegistry.put("", new HandlerVersion(VERSION));
			requestHandlerRegistry.put("/", new HandlerVersion(VERSION));
			requestHandlerRegistry.put("/version", new HandlerVersionChecked(eventPublisher,VERSION));
			requestHandlerRegistry.put("/initiate_session", new HandlerInitiateSession(eventPublisher,null));
			requestHandlerRegistry.put("/shutdown", new HandlerShutdown(Globals.getGlobals()));
						
			RequestDispatcher requestDispatcher = new RequestDispatcher(requestHandlerRegistry);
			AccessControl accessControl = new AccessControl();
			accessControl.reset();
			ws = new WebServer(inputChannel, requestDispatcher, accessControl);
			ws.start();
			Globals.getGlobals().addQuittable(ws);

		} catch (RuntimeException e) {
			e.printStackTrace();
			Globals.getGlobals().setQuitting(true);
			return;
		}

		while(!Globals.getGlobals().isQuitting()){
			try {
				Thread.sleep(GlobalsQualoscopy.ONE_SECOND);
			} catch (InterruptedException e) {
			}
		}
		getLog().info("Qualoscopy shutdown");
	}
}


