package net.djp3.qualoscopy.api;

import static org.junit.Assert.fail;

import java.util.HashMap;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent_Error;
import edu.uci.ics.luci.utility.webserver.input.channel.socket.HTTPInputOverSocket;

public class QAPIEvent_Test {
	
WebServer ws = null;
	
	private static int testPort = 9020;
	public static synchronized int testPortPlusPlus(){
		int x = testPort;
		testPort++;
		return(x);
	}

	public static WebServer startAWebServerSocket(Globals globals,int port,boolean secure) {
		WebServer ws = null;
		try {
			HTTPInputOverSocket inputChannel = new HTTPInputOverSocket(port,secure);
			HashMap<String, APIEvent> requestHandlerRegistry = new HashMap<String,APIEvent>();

			
			// Null is a default Handler
			requestHandlerRegistry.put(null,new APIEvent_Error(Globals.getGlobals().getSystemVersion()));
				
			AccessControl accessControl = new AccessControl();
			accessControl.reset();
			ws = new WebServer(inputChannel, requestHandlerRegistry, accessControl,null);
			
			ws.start();
			
			globals.addQuittable(ws);
			
		} catch (RuntimeException e) {
			fail("Couldn't start webserver"+e);
		}
		return ws;
	}

}
