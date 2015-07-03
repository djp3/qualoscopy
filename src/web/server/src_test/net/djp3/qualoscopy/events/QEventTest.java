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


package net.djp3.qualoscopy.events;

import static org.junit.Assert.*;

import org.junit.Test;

public class QEventTest {

	@Test
	public void test() {
		String s = "thing";
		QEvent thing1 = new QEvent();
		QEvent thing2 = new QEvent();
		
		assertTrue(!thing1.equals(null));
		assertTrue(!thing1.equals(s));
		assertTrue(thing1.equals(thing1));
		
		assertEquals(thing1.hashCode(),thing1.hashCode());
		assertEquals(thing1.hashCode(),thing2.hashCode());
		
		assertEquals(thing1,QEvent.fromJSON(thing1.toJSON()));
		assertEquals(thing1,QEvent.fromJSON(thing2.toJSON()));
		
	}

}
