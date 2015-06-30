package net.djp3.qualoscopy.events.handlers;

import static org.junit.Assert.*;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.events.QEvent;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventHandlerTest {

	static final String qInstance = "Test_Instance";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Globals.setGlobals(null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParameters() {
		QEventHandler q = new QEventHandler();
		
		QEvent event = new QEvent(null);
		
		JSONObject json = q.checkParameters(0,event);
		assertTrue(json != null);
	
		assertEquals("true",(String)json.get("error"));
		assertEquals(QEventHandler.ERROR_QINSTANCE_NULL,((JSONArray)json.get("errors")).get(0));
		
		event = new QEvent(qInstance);
		json = q.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertEquals(QEventHandler.ERROR_GLOBALS_NULL,((JSONArray)json.get("errors")).get(0));
		
		GlobalsQualoscopy g = new GlobalsQualoscopy("TEST_VERSION");
		Globals.setGlobals(g);
		event = new QEvent(qInstance);
		json = q.checkParameters(0,event);
		assertTrue(json == null);
	}
	
	@Test
	public void testOnEvent() {
		GlobalsQualoscopy g = new GlobalsQualoscopy("TEST_VERSION");
		Globals.setGlobals(g);
		
		QEventHandler q = new QEventHandler();
		
		JSONObject json = q.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertEquals(QEventHandler.ERROR_PARAMETERS_NOT_CHECKED,((JSONArray)json.get("errors")).get(0));
		
		QEvent event = new QEvent(qInstance);
		json = q.checkParameters(0,event);
		assertTrue(json == null);
		
		json = q.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
	}
}
