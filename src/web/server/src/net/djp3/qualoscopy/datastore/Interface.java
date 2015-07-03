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

import edu.uci.ics.luci.utility.webserver.handlers.login.Datastore;

public class Interface {
	
	final static Integer ITERATIONS = 200;


	private static transient volatile Logger log = null;


	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Interface.class);
		}
		return log;
	}
	
	private static volatile Interface instance = null;
	
	public static Interface getInstance(){
		if(instance == null){
			getLog().info("Creating a default Datastore Interface");
			instance = new Interface(null);
		}
		return(instance);
	}
	
	static void createInterface(Datastore db){
		instance = new Interface(db);
	}
	
	/**
	 * This is supported just for testing
	 * @param db
	 * @param seed
	 */
	static void createInterface(Datastore db,long seed){
		instance = new Interface(db,seed);
	}
	
	Random r = new Random(System.currentTimeMillis());
	
	private Datastore db;
	
	private Interface(Datastore db){
		this(db,System.currentTimeMillis());
	}
	
	void setRandom(Random r){
		this.r = r;
	}
	
	/**
	 * This interface is provided just for testing purposes
	 * @param db
	 * @param seed
	 */
	private Interface(Datastore db,long seed){
		this.db = db;
		this.setRandom(new Random(seed));
	}
	
	
	public String createSessionID(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
	
	public synchronized String createSalt(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
}