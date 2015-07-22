package net.djp3.qualoscopy.datastore;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
		
	
		Procedure procedure = Procedure.generateFakeProcedure();
		assertTrue(procedure.getDateTimeOfService() != null);
		assertTrue(procedure.setDateTimeOfService("12/31/2015 24:62") == Procedure.ERROR_PATTERN_FAIL_DOS);
		assertTrue(procedure.setDateTimeOfService("12/31/2015 12:12") == null);
	}
	
	@Test
	public void testFacultyID() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		assertTrue(procedure.getFacultyID() != null);
	}
	
	@Test
	public void testCompleted() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		assertTrue(procedure.isCompleted() != null);
	}
	
	@Test
	public void testToJSON() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		JSONObject j = procedure.toJSON();
		assertTrue(j.get("ac_id").equals(procedure.getAcID()));
		assertTrue(j.get("faculty_id").equals(procedure.getFacultyID()));
		assertTrue(j.get("completed").equals(procedure.isCompleted()?"true":"false"));
		assertTrue(j.get("date_time_of_service").equals(procedure.getDateTimeOfServiceString()));
		
	}
	
	@Test
	public void testClear() {
		
		Procedure procedure = Procedure.generateFakeProcedure();
		procedure.clearData();
		for(Field f: procedure.getClass().getFields()){
			if(!f.getName().equals("procedureID")){
				if(!Modifier.isFinal(f.getModifiers())){
					try {
						assertTrue(f.get(procedure) == null);
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
