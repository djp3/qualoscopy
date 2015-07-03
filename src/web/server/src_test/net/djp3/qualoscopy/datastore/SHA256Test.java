package net.djp3.qualoscopy.datastore;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class SHA256Test {
	
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
		String x = System.currentTimeMillis() + "";

		try {
			assertEquals(SHA256.sha256(x, 0), SHA256.sha256(x, 0));
			fail("This should throw an exception");
		} catch (IllegalArgumentException e) {
			assertTrue(e.toString().contains(SHA256.ITERATIONS_MUST_BE_1));

		} catch (NoSuchAlgorithmException e) {
			fail("This shouldn't throw this exception ");
		}

		try {
			for (int i = 1; i < 100; i++) {
				assertEquals(SHA256.sha256(x, i), SHA256.sha256(x, i));
			}
		} catch (NoSuchAlgorithmException e) {
			fail("This shouldn't throw an exception ");
		}

	}

	@Test
	public void test2() {
		try {
			String test = "Luke Raus";
			System.out.println(test + ":" + SHA256.sha256(test, 1));

			test = "Hello World 123456";
			System.out.println(test + ":" + SHA256.sha256(test, 1));

			test = "éñƔ";
			System.out.println(test + ":" + SHA256.sha256(test, 1));
		} catch (NoSuchAlgorithmException e) {
			fail("This shouldn't throw an exception ");
		}

	}

}
