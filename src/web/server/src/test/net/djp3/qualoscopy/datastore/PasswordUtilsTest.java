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

import java.util.Arrays;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.datastore.PasswordUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class PasswordUtilsTest {

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
		//this is to force a UTF-8 check
		Globals.setGlobals(new GlobalsQualoscopy("TEST VERSION",true));
		String p = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789`~!@#$%^&*()-_[{]};:'\"\\|,<.>/?åéøüñ";
		
		byte[] first = PasswordUtils.hashPassword(p);
		byte[] second = PasswordUtils.hashPassword(p);
		assertTrue(Arrays.equals(first,second));
		
		assertTrue(Arrays.equals(first,PasswordUtils.hexStringToByteArray(PasswordUtils.bytesToHexString(first)))); 
		
		
		assertTrue(PasswordUtils.checkPassword((String)null,(byte[])null));
		assertTrue(PasswordUtils.checkPassword((byte[])null,(byte[])null));
		assertTrue(PasswordUtils.checkPassword(p, PasswordUtils.hashPassword(p)));
		assertTrue(PasswordUtils.checkPassword(PasswordUtils.hashPassword(p), PasswordUtils.hashPassword(p)));
	}

}
