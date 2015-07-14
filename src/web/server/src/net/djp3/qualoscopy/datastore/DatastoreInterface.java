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

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.event.api.login.Datastore;

public class DatastoreInterface {
	
	private static final Integer ITERATIONS = 200;
	private static Random r = new Random(System.currentTimeMillis());

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(DatastoreInterface.class);
		}
		return log;
	}

	private Datastore db = null;
	
	void setRandom(Random r){
		DatastoreInterface.r = r;
	}
	
	public DatastoreInterface(Datastore db){
		this(db,System.currentTimeMillis());
	}
	
	/**
	 * This interface is provided just for testing purposes
	 * @param db
	 * @param seed
	 */
	private DatastoreInterface(Datastore db,long seed){
		this.db = db;
		this.setRandom(new Random(seed));
	}
	
	
	protected String createSessionID(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
	
	protected synchronized String createSalt(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
	
	public synchronized Pair<String,String> createAndStoreInitialSessionIDAndSalt(String source){
		//TODO: Store in database somewhere
		String sessionID = createSessionID();
		String salt = createSalt();
		return new Pair<String,String>(sessionID,salt);
	}
	
	public synchronized String createAndStoreSalt(String userID){
		//TODO: Store salt in database somewhere
		return createSalt();
	}
}
