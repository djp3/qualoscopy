package net.djp3.qualoscopy.datastore;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class InterfaceTest {
	
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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		/* Check to make sure that Interface makes a default instance */
		assertTrue(Interface.getInstance() != null);
		String uniqueSessionID_1 = Interface.getInstance().createSessionID();
		String uniqueSessionSalt_1 = Interface.getInstance().createSalt();
		
		Interface.createInterface(null);
		String uniqueSessionID_2 = Interface.getInstance().createSessionID();
		String uniqueSessionSalt_2 = Interface.getInstance().createSalt();
		
		/* Check to make sure that Interface's are consistent */
		long seed = System.currentTimeMillis();
		Interface.createInterface(null, seed);
		Interface interface1 = Interface.getInstance();
		
		Interface.createInterface(null, seed);
		Interface interface2 = Interface.getInstance();
		
		assertTrue(interface1 != interface2);
		
		String sessionID_1 = interface1.createSessionID();
		String sessionID_2 = interface2.createSessionID();
		String sessionSalt_1 = interface1.createSalt();
		String sessionSalt_2 = interface2.createSalt();
		
		assertTrue(sessionID_1.equals(sessionID_2));
		assertTrue(!sessionID_1.equals(uniqueSessionID_1));
		assertTrue(!sessionID_1.equals(uniqueSessionID_2));
		assertTrue(!sessionID_2.equals(uniqueSessionID_1));
		assertTrue(!sessionID_2.equals(uniqueSessionID_2));
		assertTrue(!uniqueSessionID_1.equals(uniqueSessionID_2));
		
		assertTrue(sessionSalt_1.equals(sessionSalt_2));
		assertTrue(!sessionSalt_1.equals(uniqueSessionSalt_1));
		assertTrue(!sessionSalt_1.equals(uniqueSessionSalt_2));
		assertTrue(!sessionSalt_2.equals(uniqueSessionSalt_1));
		assertTrue(!sessionSalt_2.equals(uniqueSessionSalt_2));
		assertTrue(!uniqueSessionSalt_1.equals(uniqueSessionSalt_2));
		
	}

}
