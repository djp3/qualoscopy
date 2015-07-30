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


package net.djp3.qualoscopy.datastore;

import static org.junit.Assert.*;

import java.util.Map;

import net.djp3.qualoscopy.datastore.DatastoreInterface.InitialCredentials;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.datastructure.Pair;

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
		
		DatastoreInterface db = null;
		try{
			db = new DatastoreInterface(null);
		}
		catch(Exception e){
			fail("This shouldn't throw an exception");
		}
		
		assertTrue(db.createSessionID() != db.createSessionID());
		assertTrue(db.createSessionKey() != db.createSessionKey());
		assertTrue(db.createSalt() != db.createSalt());
		
		String source = "foo.foo.bar.bar";
		Pair<String, String> ss = db.createAndStoreInitialSessionIDAndSalt(source);
		assertTrue(db.getInitialCredentials().size()==1);
		
		for(InitialCredentials x :db.getInitialCredentials()){
			assertTrue(x.getSalt().equals(ss.getSecond()));
			assertTrue(x.getSessionID().equals(ss.getFirst()));
		}
		
		db.removeInitialCredentials(db.getInitialCredentials().get(0));
		assertTrue(db.getInitialCredentials().size()==0);
		
		String user_id = "Tom";
		String salt  = db.createAndStoreSalt(user_id);
		
		assertTrue(db.getHashedPassword(user_id) != null);
		
		String shsid = SHA256.makeSomethingUp();
		String sessionKey = db.createAndStoreSession(user_id, shsid, source);
		assertTrue(sessionKey != null);
		
		assertTrue(!db.checkSession(null, shsid, sessionKey, source));
		assertTrue(!db.checkSession(user_id, null, sessionKey, source));
		assertTrue(!db.checkSession(user_id, shsid, null, source));
		assertTrue(!db.checkSession(user_id, shsid, sessionKey, null));
		assertTrue(!db.checkSession(user_id, SHA256.sha256(shsid,1), SHA256.sha256(sessionKey+salt,1), source));
		assertTrue(!db.checkSession(user_id, SHA256.sha256(shsid+salt,1), SHA256.sha256(sessionKey,1), source));
		assertTrue(db.checkSession(user_id, SHA256.sha256(shsid+salt,1), SHA256.sha256(sessionKey+salt,1), source));
		
		assertTrue(db.getPatients(user_id) != null);
		String patient_id = SHA256.makeSomethingUp();
		assertTrue(db.getPatientProcedures(user_id,patient_id) != null);
		
		/******************/
		/* Wipe a session */
		try{
			db.wipeSessions(user_id);
		}
		catch (Exception e){
			fail("This shouldn't fail");
		}
		/* Make sure it works */
		sessionKey = db.createAndStoreSession(user_id, shsid, source);
		assertTrue(sessionKey != null);
		salt  = db.createAndStoreSalt(user_id);
		assertTrue(salt != null);
		assertTrue(db.checkSession(user_id, SHA256.sha256(shsid+salt,1), SHA256.sha256(sessionKey+salt,1), source));
		
		/* Same thing but wiping */
		sessionKey = db.createAndStoreSession(user_id, shsid, source);
		assertTrue(sessionKey != null);
		salt  = db.createAndStoreSalt(user_id);
		assertTrue(salt != null);
		db.wipeSessions(user_id);
		assertTrue(!db.checkSession(user_id, SHA256.sha256(shsid+salt,1), SHA256.sha256(sessionKey+salt,1), source));
		
		/* Same thing but nothing to wipe */
		db.wipeSessions(user_id);
		assertTrue(!db.checkSession(user_id, SHA256.sha256(shsid+salt,1), SHA256.sha256(sessionKey+salt,1), source));
		/******************/
		
		
		patient_id = db.addPatient(user_id);
		Map<String, Patient> patients = db.getPatients(user_id);
		assertTrue(patients.keySet().contains(patient_id));
		
		assertTrue(db.addPatient(null) == null);
		
		
		
	}

}
