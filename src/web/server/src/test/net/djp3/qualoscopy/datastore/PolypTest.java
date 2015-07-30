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

public class PolypTest {

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
			new Polyp();
		} catch(Exception e){
			fail("Should not throw an exception "+e);
		}
	}
	
	@Test
	public void testFake() {
		
		Polyp polyp = Polyp.generateFakePolyp();
		for(Field f: polyp.getClass().getFields()){
			try {
				assertTrue(f.get(polyp) != null);
			} catch (IllegalArgumentException e) {
				fail("This exception: "+e);
			} catch (IllegalAccessException e) {
				fail("This exception: "+e);
			}
			
		}
	}
	
	@Test
	public void testPolypID() {
		
		Polyp polyp = Polyp.generateFakePolyp();
		assertTrue(polyp.getPolypID() != null);
	}
	
	
	
	@Test
	public void testToJSON() {
		
		Polyp polyp = Polyp.generateFakePolyp();
		JSONObject j = polyp.toJSON();
		assertTrue(j.get("polyp_id").equals(polyp.getPolypID()));
		assertTrue(j.get("time_removed").equals(polyp.getTimeRemovedString()));
	}
	
	@Test
	public void testGenerate() {
		
		Polyp polyp = Polyp.generateFakePolyp();
		for(Field f: polyp.getClass().getDeclaredFields()){
			if(!f.getName().equals("polypID")){
				if(!Modifier.isFinal(f.getModifiers())){
					if(!Modifier.isPrivate(f.getModifiers())){
						try {
							assertTrue(f.get(polyp) != null);
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
		
		Polyp polyp = Polyp.generateFakePolyp();
		polyp.clearData();
		for(Field f: polyp.getClass().getDeclaredFields()){
			if(!f.getName().equals("polypID")){
				if(!Modifier.isFinal(f.getModifiers())){
					if(!Modifier.isPrivate(f.getModifiers())){
						try {
							assertTrue(f.get(polyp) == null);
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
