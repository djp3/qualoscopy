package net.djp3.qualoscopy.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Random;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.datastore.DatastoreInterface;
import net.djp3.qualoscopy.datastore.SHA256;
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
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.WebUtil;
import edu.uci.ics.luci.utility.webserver.event.EventVoid;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent;
import edu.uci.ics.luci.utility.webserver.event.api.APIEvent_Version;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.socket.Output_Socket_HTTP;

public class QAPIEvent_UpdatePolyp_Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Globals.setGlobals(new GlobalsQualoscopy("0.1",true));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Globals.getGlobals().setQuitting(true);
		Globals.setGlobals(null);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	Random random = new Random(System.currentTimeMillis());
	private WebServer ws = null;

	HashMap<String,APIEvent> requestHandlerRegistry;

	@Test
	public void test() {
		
		try{
			String version = System.currentTimeMillis()+"";
		
			QAPIEvent_UpdatePolyp a = new QAPIEvent_UpdatePolyp(version,null);
			QAPIEvent_UpdatePolyp b = (QAPIEvent_UpdatePolyp) a.clone();
			
			QAPIEvent_CheckSession c = new QAPIEvent_CheckSession(version,null);
			
			assertTrue(!a.equals(null));
			
			assertTrue(!a.equals("Hello world"));
			
			assertTrue(!a.equals(c));
			
			assertTrue(a.equals(a));
			assertTrue(a.hashCode() == a.hashCode());
			
			assertTrue(a.equals(b));
			assertTrue(a.hashCode() == b.hashCode());
			
			assertTrue(b.equals(a));
			assertTrue(b.hashCode() == a.hashCode());
			
			/*setting */
			b.setRequest(new Request());
			b.setOutput(new Output_Socket_HTTP(null));
			//b.setAPIVersion(version);
			assertTrue(!a.equals(b));
			a.set(b);
			assertTrue(a.equals(b));
			
			try{
				a.set(new EventVoid());
				fail("This should throw an exception");
			}
			catch(InvalidParameterException e){
				//okay
			}
			
		
		}catch(Exception e){
			fail("Exception make me fail"+e);
		}
	}
	
	
	@Test
	public void testWebServerSocket() {
		String version = System.currentTimeMillis()+"";
		
		int port = QAPIEvent_Test.testPortPlusPlus();
		boolean secure = false;
		DatastoreInterface db = new DatastoreInterface(null);
		ws = QAPIEvent_Test.startAWebServerSocket(Globals.getGlobals(),port,secure);
		ws.updateAPIRegistry("/", new APIEvent_Version(version));
		ws.updateAPIRegistry("/version", new QAPIEvent_VersionCheck(version));
		ws.updateAPIRegistry("/initiate_session", new QAPIEvent_InitiateSession(version,db));
		ws.updateAPIRegistry("/login", new QAPIEvent_Login(version,db));
		ws.updateAPIRegistry("/get/patients", new QAPIEvent_GetPatients(version,db));
		ws.updateAPIRegistry("/get/patient/procedures", new QAPIEvent_GetPatientProcedures(version,db));
		ws.updateAPIRegistry("/add/patient", new QAPIEvent_AddPatient(version,db));
		ws.updateAPIRegistry("/add/procedure", new QAPIEvent_AddProcedure(version,db));
		ws.updateAPIRegistry("/add/polyp", new QAPIEvent_AddPolyp(version,db));
		ws.updateAPIRegistry("/get/procedure/polyps", new QAPIEvent_GetProcedurePolyps(version,db));
		ws.updateAPIRegistry("/update/polyp", new QAPIEvent_UpdatePolyp(version,db));

		
		String session_id = null;
		String session_key = null;
		String salt = null;
		String patient_id = null;
		String procedure_id = null;
		String polyp_id = null;
		
		String time_removed = null;

		
		
		
		/**** Make sure the version responds ****/
		String responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/");
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		//System.out.println(responseString);

		JSONObject response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		/**** Make sure the version check responds ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/version")
									.setParameter("version", version);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		/**** Make sure the initiate session responds ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/initiate_session")
									.setParameter("version", version);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			session_id = ((String)response.get("session_id"));
			assertTrue(session_id != null);
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		/**** Make sure the login responds ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/login")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("session_id", session_id)
									.setParameter("shp",SHA256.sha256(SHA256.sha256("test_password", 1)+salt,1));
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			session_key = ((String)response.get("session_key"));
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		/**** Now test get_patients ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/get/patients")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1));
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
			JSONArray patients = (JSONArray) response.get("patients"); 
			assertTrue(patients.size() > 0);
			JSONObject patient = (JSONObject) patients.get(0);
			assertTrue(patient.size() == 6);
			for(int i=0;i < patients.size(); i++){
				patient = (JSONObject) patients.get(i);
				assertTrue(patient.get("patient_id") != null);
				assertTrue(patient.get("mr_id") != null);
				assertTrue(patient.get("first") != null);
				assertTrue(patient.get("last") != null);
				assertTrue(patient.get("gender") != null);
				assertTrue(patient.get("dob") != null);
			}
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		
		
		
		/**** Now test add_patients ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/add/patient")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1));
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			patient_id = ((String)response.get("patient_id"));
			assertTrue(patient_id != null);
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		

		/**** Now test get patient procedures with good credentials****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/get/patient/procedures")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("patient_id",patient_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		/**** Now test add procedures with good credentials****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/add/procedure")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("patient_id",patient_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(response.size()==5);// salt, procedure_id,error,errors,version
			procedure_id = (String) response.get("procedure_id");
			assertTrue(procedure_id != null);
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		


		/**** Now test get polyp with good credentials****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/get/procedure/polyps")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		/**** Now test add polyp with bad credentials****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/add/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid","bad"+SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(((String)response.get("salt"))==null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		


		/**** Now test add polyp without procedure id ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/add/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1));
									//.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		

		/**** Now test add polyp with good credentials****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/add/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			polyp_id = (String) response.get("polyp_id");
			assertTrue(polyp_id != null);
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		

		/**** Now make sure new polyp showed up ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/get/procedure/polyps")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			JSONArray polyps = (JSONArray) response.get("polyps");
			assertTrue(polyps != null);
			boolean showedUp = false;
			for(int i = 0;i < polyps.size();i++){
				JSONObject polyp = (JSONObject) polyps.get(i);
				if(polyp.get("polyp_id").equals(polyp_id)){
					showedUp = true;
					assertTrue(polyp.get("time_removed").equals("null"));
				}
			}
			assertTrue(showedUp);
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		

		/**** Now test update polyp without procedure id****/
		time_removed = String.format("%02d:%02d",random.nextInt(24),random.nextInt(60));

		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									//.setParameter("procedure_id",procedure_id)
									.setParameter("polyp_id",polyp_id)
									.setParameter("time_removed",time_removed);


			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		/**** Now test update polyp without polyp id****/

		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id)
									//.setParameter("polyp_id",polyp_id)
									.setParameter("time_removed",time_removed);


			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		/**** Now test update polyp with bad credentials ****/
		time_removed = String.format("%02d:%02d",random.nextInt(24),random.nextInt(60));

		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid","bad"+SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id)
									.setParameter("polyp_id",polyp_id)
									.setParameter("time_removed",time_removed);


			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(((String)response.get("salt"))==null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		

		/**** Now test update polyp with lots of info****/
		time_removed = String.format("%02d:%02d",random.nextInt(24),random.nextInt(60));

		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/polyp")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id)
									.setParameter("polyp_id",polyp_id)
									.setParameter("time_removed",time_removed);


			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		/**** Now get polyp and make sure all the fields match what we set them to ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/get/procedure/polyps")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(response.size()==5);// salt, procedure,error,errors,version
			JSONArray polyps = ((JSONArray)response.get("polyps"));
			assertTrue(polyps.size() >0);
			for(int i = 0; i < polyps.size(); i++){
				JSONObject polyp = (JSONObject) polyps.get(i);
				if(polyp.get("polyp_id").equals(polyp_id)){
					//System.out.println(procedure.toJSONString());
					assertTrue(polyp.get("time_removed").equals(time_removed));
				}
			}
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		
		//Globals.getGlobals().setQuitting(true);

	}

}