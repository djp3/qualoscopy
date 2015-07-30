package net.djp3.qualoscopy.datastore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProcedureTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void test() {
		try{
			new Procedure();
		} catch(Exception e){
			fail("Should not throw an exception "+e);
		}
	}
	
	@Test
	public void testFake() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		for(Field f: procedure.getClass().getFields()){
			try {
				assertTrue(f.get(procedure) != null);
			} catch (IllegalArgumentException e) {
				fail("This exception: "+e);
			} catch (IllegalAccessException e) {
				fail("This exception: "+e);
			}
			
		}
	}
	
	@Test
	public void testProcedureID() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		assertTrue(procedure.getProcedureID() != null);
	}
	
	
	@Test
	public void testAcID() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		assertTrue(procedure.getAcID() != null);
		assertTrue(procedure.setAcID("") == Procedure.ERROR_PATTERN_FAIL_AC_ID);
		assertTrue(procedure.setAcID(SHA256.makeSomethingUp()) == null);
	}
	
	
	@Test
	public void testDateOfService() {
		
		String time = "07/22/2015 20:00";
		
		Procedure procedure = Procedure.generateFakeProcedure();
		
		/*This is to catch an error in SimpleDateFormat patterning which I made once. once. */
		try {
			assertTrue(procedure.sdf.format(procedure.sdf.parse(time)).equals(time));
		} catch (ParseException e) {
			fail("This shouldn't throw an exception");
		}
		
		assertTrue(procedure.getDateTimeOfService() != null);
		assertTrue(procedure.setDateTimeOfService("12/31/2015 24:62") == Procedure.ERROR_PATTERN_FAIL_DOS);
		assertTrue(procedure.setDateTimeOfService(time) == null);
		assertTrue(procedure.getDateTimeOfServiceString().equals(time));
	}
	
	@Test
	public void testFacultyID() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		assertTrue(procedure.getFacultyID() != null);
	}
	
	
	@Test
	public void testToJSON() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		JSONObject j = procedure.toJSON();
		assertTrue(j.get("procedure_id").equals(procedure.getProcedureID()));
		assertTrue(j.get("ac_id").equals(procedure.getAcID()));
		assertTrue(j.get("faculty_id").equals(procedure.getFacultyID()));
		//TODO: Figure out completed
		assertTrue(j.get("completed").equals("false"));
		assertTrue(j.get("date_time_of_service").equals(procedure.getDateTimeOfServiceString()));
		
	}
	
	@Test
	public void testGenerate() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		for(Field f: procedure.getClass().getDeclaredFields()){
			if(!f.getName().equals("procedureID")){
				if(!Modifier.isFinal(f.getModifiers())){
					if(!Modifier.isPrivate(f.getModifiers())){
						try {
							assertTrue(f.get(procedure) != null);
						}catch(AssertionError e){
							System.err.println(f.getName());
							throw e;
						} catch (IllegalArgumentException e) {
							fail("This exception: "+e);
						} catch (IllegalAccessException e) {
							fail("This exception: "+e);
						}
					}
				}
			}
		}
	}
	
	@Test
	public void testClear() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		procedure.clearData();
		for(Field f: procedure.getClass().getDeclaredFields()){
			if(!f.getName().equals("procedureID")){
				if(!Modifier.isFinal(f.getModifiers())){
					if(!Modifier.isPrivate(f.getModifiers())){
						try {
							assertTrue(f.get(procedure) == null);
						}catch(AssertionError e){
							System.err.println(f.getName());
							throw e;
						} catch (IllegalArgumentException e) {
							fail("This exception: "+e);
						} catch (IllegalAccessException e) {
							fail("This exception: "+e);
						}
					}
				}
			}
		}
	}
	


}
