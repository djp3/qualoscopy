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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SHA256 {

	public static final String ITERATIONS_MUST_BE_1 = "Iterations must be >= 1";
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(SHA256.class);
		}
		return log;
	}
	private static Random r = new Random(System.currentTimeMillis());
	
	public static String makeSomethingUp(){
		
		long x = r.nextLong();
		return x+"";
	}

	public static String sha256(String input,int iterations) {
		if(iterations < 1){
			throw new IllegalArgumentException(ITERATIONS_MUST_BE_1);
		}
		MessageDigest mDigest = null;
		
		try{
			mDigest = MessageDigest.getInstance("SHA-256");
			byte[] result = mDigest.digest(input.getBytes());
			for(int i = 1; i < iterations; i++){
				result= mDigest.digest(result);
			}
		
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
		}
		catch(NoSuchAlgorithmException e){
			getLog().fatal("Unable to hash"+e);
			return makeSomethingUp();
		}
	}

}
