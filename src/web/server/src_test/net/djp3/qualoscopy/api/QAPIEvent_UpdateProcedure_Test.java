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

public class QAPIEvent_UpdateProcedure_Test {

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
		
			QAPIEvent_UpdateProcedure a = new QAPIEvent_UpdateProcedure(version,null);
			QAPIEvent_UpdateProcedure b = (QAPIEvent_UpdateProcedure) a.clone();
			
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
		ws.updateAPIRegistry("/update/procedure", new QAPIEvent_UpdateProcedure(version,db));
		
		String session_id = null;
		String session_key = null;
		String salt = null;
		String patient_id = null;
		String procedure_id = null;
		
		String ac_id = null;
		String date_time_of_service = null;
		String faculty_id = null;
		String location = null;
		String fellow = null;
		String pre_drug = null;
		String prep_liters = null;
		String split_prep = null;
		String bisacodyl = null;
		String last_colon = null;
		String primary_indication = null;
		String other_indication = null;
		String scope = null;
		String endocuff = null;
		String cap_assisted = null;
		String underwater = null;
		String sedation_level = null;
		String versed = null;
		String fentanyl = null;
		String demerol = null;
		String benadryl = null;
		String extent = null;
		String prep_quality_left = null;
		String prep_quality_mid = null;
		String prep_quality_right = null;
		String time_insertion = null;
		String time_begin_withdrawal = null;
		String time_scope_withdrawn = null;
		
		
		
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
			
			assertTrue(response.size()==5);// salt, procedure,error,errors,version
			JSONArray procedures = ((JSONArray)response.get("procedures"));
			assertTrue(procedures.size() >0);
			for(int i = 0; i < procedures.size(); i++){
				JSONObject procedure = (JSONObject) procedures.get(i);
				int size = 30;
				assertTrue(procedure.size()==size);
				size--;
				assertTrue(procedure.get("procedure_id")!= null);
				size--;
				assertTrue(procedure.get("date_time_of_service")!= null);
				size--;
				assertTrue(procedure.get("completed")!= null);
				size--;
				assertTrue(procedure.get("ac_id")!= null);
				size--;
				assertTrue(procedure.get("faculty_id")!= null);
				size--;
				assertTrue(procedure.get("location") != null);
				size--;
				assertTrue(procedure.get("fellow") != null);
				size--;
				assertTrue(procedure.get("pre_drug") != null);
				size--;
				assertTrue(procedure.get("prep_liters") != null);
				size--;
				assertTrue(procedure.get("split_prep") != null);
				size--;
				assertTrue(procedure.get("bisacodyl") != null);
				size--;
				assertTrue(procedure.get("last_colon") != null);
				size--;
				assertTrue(procedure.get("primary_indication") != null);
				size--;
				assertTrue(procedure.get("other_indication") != null);
				size--;
				assertTrue(procedure.get("scope") != null);
				size--;
				assertTrue(procedure.get("endocuff") != null);
				size--;
				assertTrue(procedure.get("cap_assisted") != null);
				size--;
				assertTrue(procedure.get("underwater") != null);
				size--;
				assertTrue(procedure.get("sedation_level") != null);
				size--;
				assertTrue(procedure.get("versed") != null);
				size--;
				assertTrue(procedure.get("fentanyl") != null);
				size--;
				assertTrue(procedure.get("demerol") != null);
				size--;
				assertTrue(procedure.get("benadryl") != null);
				size--;
				assertTrue(procedure.get("extent") != null);
				size--;
				assertTrue(procedure.get("prep_quality_left") != null);
				size--;
				assertTrue(procedure.get("prep_quality_mid") != null);
				size--;
				assertTrue(procedure.get("prep_quality_right") != null);
				size--;
				assertTrue(procedure.get("time_insertion") != null);
				size--;
				assertTrue(procedure.get("time_begin_withdrawal") != null);
				size--;
				assertTrue(procedure.get("time_scope_withdrawn") != null);
				assertTrue(size == 0);

			}
			
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
		
		//System.out.println(responseString);

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
		
		
		

		/**** Now test update procedures with missing fields****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/procedure")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									//.setParameter("patient_id",patient_id)
									.setParameter("procedure_id",procedure_id);
			responseString = WebUtil.fetchWebPage(uriBuilder, null,null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
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
		
		
		/**** Now test update procedures with missing fields****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/procedure")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("patient_id",patient_id);
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
		
		
		/**** Now test update procedures with bad credentials ****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/procedure")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk","bad"+SHA256.sha256(session_key+salt,1))
									.setParameter("patient_id",patient_id)
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
		
		System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(((String)response.get("salt")) == null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		

		/**** Now test update procedures with good credentials and minimal info****/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/procedure")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("patient_id",patient_id)
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
		
		

		/**** Now get patient and make sure all the fields match null ****/
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
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(response.size()==5);// salt, procedure,error,errors,version
			JSONArray procedures = ((JSONArray)response.get("procedures"));
			assertTrue(procedures.size() >0);
			for(int i = 0; i < procedures.size(); i++){
				JSONObject procedure = (JSONObject) procedures.get(i);
				//System.out.println(procedure.toJSONString());
				if(procedure.get("procedure_id").equals(procedure_id)){
					assertTrue(procedure.get("date_time_of_service").equals("null"));
					assertTrue(procedure.get("completed").equals("false"));
					assertTrue(procedure.get("ac_id").equals("null"));
					assertTrue(procedure.get("faculty_id").equals("null"));
				}
			}
			
			salt = ((String)response.get("salt"));
			assertTrue(salt != null);
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		


		/**** Now test update procedures with lots of info****/
		ac_id = "ac_id-"+System.currentTimeMillis();
		date_time_of_service = "07/22/2015 20:00";
		faculty_id = "faculty_id-"+random.nextInt(999999);
		location = "UCI-"+random.nextInt(9999);
		fellow = "Dr. Patterson-"+random.nextInt(99);
		pre_drug = "Novacaine-"+random.nextInt(99);
		prep_liters = ""+random.nextInt(10);
		split_prep = random.nextBoolean()?"Y":"N";
		bisacodyl = random.nextBoolean()?"Y":"N";
		last_colon = ""+random.nextInt(10);
		primary_indication = "foo-"+random.nextInt(99);
		other_indication = "foo-"+random.nextInt(99);
		scope = "foo-"+random.nextInt(99);
		endocuff = random.nextBoolean()?"Y":"N";
		cap_assisted = random.nextBoolean()?"Y":"N";
		underwater = random.nextBoolean()?"Y":"N";
		sedation_level = random.nextBoolean()?"unsedated":"moderate"; //or "ga"
		versed = ""+random.nextInt(10);
		fentanyl = ""+random.nextInt(10);
		demerol = ""+random.nextInt(10);
		benadryl = ""+random.nextInt(10);
		extent = ""+random.nextInt(10);
		prep_quality_left = ""+random.nextInt(4);
		prep_quality_mid = ""+random.nextInt(4);
		prep_quality_right = ""+random.nextInt(4);
		time_insertion = String.format("%02d:%02d",random.nextInt(24),random.nextInt(60));
		time_begin_withdrawal = String.format("%02d:%02d",random.nextInt(24),random.nextInt(60));
		time_scope_withdrawn = String.format("%02d:%02d",random.nextInt(24),random.nextInt(60));

		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
									.setScheme("http")
									.setHost("localhost")
									.setPort(ws.getInputChannel().getPort())
									.setPath("/update/procedure")
									.setParameter("version", version)
									.setParameter("user_id","test_user")
									.setParameter("shsid",SHA256.sha256(session_id+salt,1))
									.setParameter("shsk",SHA256.sha256(session_key+salt,1))
									.setParameter("patient_id",patient_id)
									.setParameter("procedure_id",procedure_id)
									.setParameter("ac_id",ac_id)
									.setParameter("date_time_of_service",date_time_of_service)
									.setParameter("faculty_id",faculty_id)
									.setParameter("date_time_of_service",date_time_of_service)
									.setParameter("location",location)
									.setParameter("fellow",fellow)
									.setParameter("pre_drug",pre_drug)
									.setParameter("prep_liters",prep_liters)
									.setParameter("split_prep",split_prep)
									.setParameter("bisacodyl",bisacodyl)
									.setParameter("last_colon",last_colon)
									.setParameter("primary_indication",primary_indication)
									.setParameter("other_indication",other_indication)
									.setParameter("scope",scope)
									.setParameter("endocuff",endocuff)
									.setParameter("cap_assisted",cap_assisted)
									.setParameter("underwater",underwater)
									.setParameter("sedation_level",sedation_level)
									.setParameter("versed",versed)
									.setParameter("fentanyl",fentanyl)
									.setParameter("demerol",demerol)
									.setParameter("benadryl",benadryl)
									.setParameter("extent",extent)
									.setParameter("prep_quality_left",prep_quality_left)
									.setParameter("prep_quality_mid",prep_quality_mid)
									.setParameter("prep_quality_right",prep_quality_right)
									.setParameter("time_insertion",time_insertion)
									.setParameter("time_begin_withdrawal",time_begin_withdrawal)
									.setParameter("time_scope_withdrawn",time_scope_withdrawn);


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
		
		

		/**** Now get patient and make sure all the fields match what we set them to ****/
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
		
		//System.out.println(responseString);

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertTrue(((String)response.get("version")).equals(version));
			
			assertTrue(response.size()==5);// salt, procedure,error,errors,version
			JSONArray procedures = ((JSONArray)response.get("procedures"));
			assertTrue(procedures.size() >0);
			for(int i = 0; i < procedures.size(); i++){
				JSONObject procedure = (JSONObject) procedures.get(i);
				if(procedure.get("procedure_id").equals(procedure_id)){
					//System.out.println(procedure.toJSONString());
					assertTrue(procedure.get("date_time_of_service").equals(date_time_of_service));
					assertTrue(procedure.get("ac_id").equals(ac_id));
					assertTrue(procedure.get("faculty_id").equals(faculty_id));
					assertTrue(procedure.get("location").equals(location));
					assertTrue(procedure.get("fellow").equals(fellow));
					assertTrue(procedure.get("pre_drug").equals(pre_drug));
					assertTrue(procedure.get("prep_liters").equals(prep_liters));
					assertTrue(procedure.get("split_prep").equals(split_prep));
					assertTrue(procedure.get("bisacodyl").equals(bisacodyl));
					assertTrue(procedure.get("last_colon").equals(last_colon));
					assertTrue(procedure.get("primary_indication").equals(primary_indication));
					assertTrue(procedure.get("other_indication").equals(other_indication));
					assertTrue(procedure.get("scope").equals(scope));
					assertTrue(procedure.get("endocuff").equals(endocuff));
					assertTrue(procedure.get("cap_assisted").equals(cap_assisted));
					assertTrue(procedure.get("underwater").equals(underwater));
					assertTrue(procedure.get("sedation_level").equals(sedation_level));
					assertTrue(procedure.get("versed").equals(versed));
					assertTrue(procedure.get("fentanyl").equals(fentanyl));
					assertTrue(procedure.get("demerol").equals(demerol));
					assertTrue(procedure.get("benadryl").equals(benadryl));
					assertTrue(procedure.get("extent").equals(extent));
					assertTrue(procedure.get("prep_quality_left").equals(prep_quality_left));
					assertTrue(procedure.get("prep_quality_mid").equals(prep_quality_mid));
					assertTrue(procedure.get("prep_quality_right").equals(prep_quality_right));
					assertEquals(procedure.get("time_insertion"),time_insertion);
					assertEquals(procedure.get("time_begin_withdrawal"),time_begin_withdrawal);
					assertTrue(procedure.get("time_scope_withdrawn").equals(time_scope_withdrawn));
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
