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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.QualoscopyWebServer;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventVoid;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;
import net.djp3.qualoscopy.events.ResultChecker;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.WebUtil;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerError;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerShutdown;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;
import edu.uci.ics.luci.utility.webserver.input.channel.socket.HTTPInputOverSocket;

public class HandlerInitiateSesstionTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		while(Globals.getGlobals() != null){
			Thread.sleep(100);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Globals.setGlobals(null);
	}

	final static String TEST_VERSION = "-999";
	final static String worldName = "earth_" + System.currentTimeMillis();
	final static String worldPassword = "earthPassword_"
			+ System.currentTimeMillis();
	final static String playerName = "Player_Name" + System.currentTimeMillis();
	final static String playerPassword = "Player_Password"
			+ System.currentTimeMillis();

	private WebServer ws = null;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		while(!ws.isTerminated()){
			Thread.sleep(100);
		}
	}

	private static int testPort = 9020;

	public static synchronized int testPortPlusPlus() {
		int x = testPort;
		testPort++;
		return (x);
	}

	public static WebServer startAWebServerSocket(Globals globals, int port, boolean secure) {
		try {
			HTTPInputOverSocket inputChannel = new HTTPInputOverSocket(port, secure);
			HashMap<String, HandlerAbstract> requestHandlerRegistry = new HashMap<String, HandlerAbstract>();

			// Null is a default Handler
			requestHandlerRegistry.put(null, new HandlerError(Globals
					.getGlobals().getSystemVersion()));

			RequestDispatcher requestDispatcher = new RequestDispatcher(
					requestHandlerRegistry);
			AccessControl accessControl = new AccessControl();
			accessControl.reset();
			WebServer ws = new WebServer(inputChannel, requestDispatcher,
					accessControl);
			ws.start();
			globals.addQuittable(ws);
			return ws;
		} catch (RuntimeException e) {
			fail("Couldn't start webserver" + e);
		}
		return null;
	}

	private void startAWebServer(int port, QEventWrapperQueuer eventPublisher) {
		try {
			boolean secure = false;
			ws = startAWebServerSocket(Globals.getGlobals(), port, secure);
			ws.getRequestDispatcher() .updateRequestHandlerRegistry( null, new HandlerVersion(Globals.getGlobals() .getSystemVersion()));
			ws.getRequestDispatcher() .updateRequestHandlerRegistry( "", new HandlerVersion(Globals.getGlobals() .getSystemVersion()));
			ws.getRequestDispatcher() .updateRequestHandlerRegistry( "/", new HandlerVersion(Globals.getGlobals() .getSystemVersion()));
			ws.getRequestDispatcher() .updateRequestHandlerRegistry( "/version", new HandlerVersionChecked(eventPublisher,Globals.getGlobals().getSystemVersion()));
			ws.getRequestDispatcher() .updateRequestHandlerRegistry( "/initiate_session", new HandlerInitiateSession(eventPublisher,null));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/shutdown", new HandlerShutdown(Globals.getGlobals()));

		} catch (RuntimeException e) {
			fail("Couldn't start webserver" + e);
		}
	}

	public QEventWrapperQueuer startATestSystem() {
		List<QEventWrapper> events = new ArrayList<QEventWrapper>();

		String logFileName = "test/test_" + this.getClass().getCanonicalName();

		GlobalsQualoscopy globals = new GlobalsQualoscopy(TEST_VERSION, true);
		GlobalsQualoscopy.setGlobals(globals);

		QEventWrapperQueuer eventPublisher = QualoscopyWebServer.createEventQueue(logFileName);
		globals.addQuittable(eventPublisher);

		QEventVoid dummyEvent1 = new QEventVoid();
		ResultChecker resultChecker1 = new ResultChecker(false);
		QEventWrapper event = new QEventWrapper(QEventType.VOID, dummyEvent1, resultChecker1);
		events.add(event);
		
		QEventVoid dummyEvent2 = new QEventVoid();
		ResultChecker resultChecker2 = new ResultChecker(false);
		event = new QEventWrapper(QEventType.VOID, dummyEvent2, resultChecker2);
		events.add(event);

		eventPublisher.onData(events);
		synchronized (resultChecker1.getSemaphore()) {
			while (resultChecker1.getResultOK() == null) {
				try {
					resultChecker1.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		synchronized (resultChecker2.getSemaphore()) {
			while (resultChecker2.getResultOK() == null) {
				try {
					resultChecker2.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		
		try {
			assertTrue(resultChecker1.getResultOK());
		} catch (AssertionError e) {
			System.err.println(resultChecker1.getResults().toJSONString());
			throw e;
		}
		
		try {
			assertTrue(resultChecker2.getResultOK());
		} catch (AssertionError e) {
			System.err.println(resultChecker2.getResults().toJSONString());
			throw e;
		}

		return eventPublisher;
	}

	@Test
	public void testWebServer() {

		QEventWrapperQueuer publisher = startATestSystem();

		int port = testPortPlusPlus();
		startAWebServer(port, publisher);

		/* Check the version */
		String responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http") .setHost("localhost") .setPort(ws.getInputChannel().getPort()).setPath("/");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		// System.out.println(responseString);

		JSONObject response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false", response.get("error"));
			assertEquals(TEST_VERSION, response.get("version"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		/* Shut. it. down. */
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/shutdown");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false", response.get("error"));
			assertEquals("true", response.get("shutdown"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}

	}
	
	@Test
	public void testHandler() {

		QEventWrapperQueuer publisher = startATestSystem();

		int port = testPortPlusPlus();
		startAWebServer(port, publisher);

		/* Try calling the function without the version */
		String responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel()
					.getPort())
					.setPath("/initiate_session");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		// System.out.println(responseString);

		JSONObject response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true", response.get("error"));
			JSONArray errors = (JSONArray) response.get("errors");
			String error = (String) errors.get(0);
			assertTrue(error.contains(HandlerVersionChecked.ERROR_NULL_VERSION));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		
		/* Try calling the function with the version */
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel()
					.getPort())
					.setPath("/initiate_session")
					.setParameter("version", Globals.getGlobals().getSystemVersion());

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		 System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false", response.get("error"));
			String session_id = (String) response.get("session_id");
			String session_salt = (String) response.get("session_salt");
			System.out.println("session_id: "+session_id);
			System.out.println("session_salt: "+session_salt);
			
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		
		/* Shut. it. down. */
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/shutdown");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false", response.get("error"));
			assertEquals("true", response.get("shutdown"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}

	}

}
