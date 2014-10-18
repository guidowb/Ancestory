package org.guidowb.ancestory.security;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Password
{
	public static final int SALT_SIZE = 24;
	public static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
	public static final int HASH_SIZE = 24;
	public static final int HASH_ITERATIONS = 1000;

	public static String hashPassword(String password) {
		byte[] salt = createSalt();
		byte[] hash = createHash(password, salt, HASH_ITERATIONS);
		return Integer.toString(HASH_ITERATIONS) + ":" + toHex(salt) + ":" + toHex(hash);
	}

    public static boolean isPassword(String newPassword, String oldPassword) {
    	String[] oldParts = oldPassword.split(":");
    	int oldIterations = Integer.parseInt(oldParts[0]);
    	byte[] oldSalt = fromHex(oldParts[1]);
    	byte[] oldHash = fromHex(oldParts[2]);
    	byte[] newHash;
		newHash = createHash(newPassword, oldSalt, oldIterations);
    	return timeConstantCompare(oldHash, newHash);
    }

    private static byte[] createSalt() {
    	SecureRandom random = new SecureRandom();
    	byte[] salt = new byte[SALT_SIZE];
    	random.nextBytes(salt);
    	return salt;
    }

    private static byte[] createHash(String password, byte[] salt, int iterations) {
	    try {
	        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, HASH_SIZE * 8);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_ALGORITHM);
	        return skf.generateSecret(spec).getEncoded();
		} catch (Exception e) {
			// Exceptions thrown above include NoSuchAlgorithmException and InvalidKeySpecException.
			// Both of those indicate an application code or configuration issue that we do not
			// expect our callers to deal with, so there's no point in polluting the code with
			// throws clauses.
			throw new RuntimeException(e);
		}
    }

    private static boolean timeConstantCompare(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex)
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }

    private static String toHex(byte[] bytes)
    {
        String hex = "";
        for (int i = 0; i < bytes.length; i += 2) {
        	Integer bite = (bytes[i] << 8) | bytes[i+1];
        	hex += String.format("%02x", bite);
        }
        return hex;
    }
}
