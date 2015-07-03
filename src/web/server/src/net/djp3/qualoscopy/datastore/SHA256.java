package net.djp3.qualoscopy.datastore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

	public static final String ITERATIONS_MUST_BE_1 = "Iterations must be >= 1";

	static String sha256(String input,int iterations) throws NoSuchAlgorithmException {
		if(iterations < 1){
			throw new IllegalArgumentException(ITERATIONS_MUST_BE_1);
		}
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
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

}
