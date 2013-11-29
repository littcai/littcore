package com.litt.core.uid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author caiyuan
 *
 */
public class RandomGUID {
	public String valueBeforeMD5 = "";

	public String valueAfterMD5 = "";

	private static Random myRand;

	private static SecureRandom mySecureRand;

	private static String sid;

	/*
	 * Static block to take care of one time secureRandom seed.
	 * It takes a few seconds to initialize SecureRandom.  You might
	 * want to consider removing this static block or replacing
	 * it with a "time since first loaded" seed to reduce this time.
	 * This block will run only once per JVM instance.
	 */

	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			sid = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Default constructor.  With no specification of security option,
	 * this constructor defaults to lower security, high performance.
	 */
	public RandomGUID()
	{
		getRandomGUID(false);
	}

	/*
	 * Constructor with security option.  Setting secure true
	 * enables each random number generated to be cryptographically
	 * strong.  Secure false defaults to the standard Random function seeded
	 * with a single cryptographically strong random number.
	 */
	public RandomGUID(boolean secure)
	{
		getRandomGUID(secure);
	}

	/*
	 * Method to generate the random GUID
	 */
	private void getRandomGUID(boolean secure)
	{
		try {
			MessageDigest md5 = null;
			StringBuffer sbValueBeforeMD5 = new StringBuffer();

			md5 = MessageDigest.getInstance("MD5");
			
			long time = System.currentTimeMillis();
			long rand = 0;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}

			// This StringBuffer can be a long as you need; the MD5
			// hash will always return 128 bits. You can change
			// the seed to include anything you want here.
			// You could even stream a file through the MD5 making
			// the odds of guessing it at least as great as that
			// of guessing the contents of the file!
			sbValueBeforeMD5.append(sid);
			sbValueBeforeMD5.append(':');
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(':');
			sbValueBeforeMD5.append(Long.toString(rand));

			valueBeforeMD5 = sbValueBeforeMD5.toString();
			md5.update(valueBeforeMD5.getBytes());

			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & 0xFF;
				if (b < 0x10)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}

			valueAfterMD5 = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Convert to the standard format for GUID
	 * (Useful for SQL Server UniqueIdentifiers, etc.)
	 * Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
	 */
	public String toString() {
		String raw = valueAfterMD5.toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		sb.append('-');
		sb.append(raw.substring(8, 12));
		sb.append('-');
		sb.append(raw.substring(12, 16));
		sb.append('-');
		sb.append(raw.substring(16, 20));
		sb.append('-');
		sb.append(raw.substring(20));

		return sb.toString();
	}
	
	/**
	 * 静态方法获得GUID
	 * @return
	 */
	public static String getGuid()
	{
		RandomGUID guid = new RandomGUID();
		return guid.toString();
	}

	/*
	 * Demonstraton and self test of class
	 */
	public static void main(String args[])throws Exception {
		
			RandomGUID myGUID = new RandomGUID();
			System.out.println("Seeding String=" + myGUID.valueBeforeMD5);
			System.out.println("rawGUID=" + myGUID.valueAfterMD5);
			System.out.println("RandomGUID=" + myGUID.toString());
		
	}

}
