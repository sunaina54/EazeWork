package hr.eazework.parsers;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class MainParser implements SecurityMaster {

	private static Key SECRET_KEY = null;
	private static byte[] SALT_DATA = null;

	private static Key getSecretKey(String username) throws SecurityAPIExcption

	{
		SecretKey secretKey;
		Key encyptionKey;
		try {
			SALT_DATA = getSaltData(username);
			if (SALT_DATA == null) {
				throw new SecurityAPIExcption("salt data is null");
			}
			PBEKeySpec pbeKeySpec = new PBEKeySpec(username.toCharArray(),
					SALT_DATA, PBE_ITERATION_COUNT, KEY_SIZE);
			SecretKeyFactory factory;

			factory = SecretKeyFactory.getInstance(RANDOM_ALGORITHM, PROVIDER);

			secretKey = factory.generateSecret(pbeKeySpec);
			encyptionKey = new SecretKeySpec(secretKey.getEncoded(), "AES");
		} catch (InvalidKeySpecException e) {
			throw new SecurityAPIExcption("Invalid KeySpec Exception");

		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption("No Such Algorithm Exception");
		} catch (NoSuchProviderException e) {
			throw new SecurityAPIExcption("No Such Provider Exception");
		}
		return encyptionKey;

	}

	private static byte[] getSaltData(String username)
			throws SecurityAPIExcption {
		String encTarget = username;
		MessageDigest mdEnc = null;
		try {
			mdEnc = MessageDigest.getInstance(ALGORITHM_FOR_SALT_DATA);
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption(
					"No Such Algorithm Exception in salt data genration");
		}

		mdEnc.update(encTarget.getBytes(), 0, encTarget.length());

		byte[] dataInBase64 = Base64.encode(mdEnc.digest(), Base64.NO_WRAP);
		SALT_DATA = new byte[8];
		for (int i = 0, j = 7; i < 8; i++, j--) {
			SALT_DATA[j] = dataInBase64[i];
		}
		String st = new String(dataInBase64);
		return SALT_DATA;
	}

	public static String getDigestedData(String dataToBeDigested)
			throws SecurityAPIExcption {
		String digestedStr = null;
		MessageDigest mdEnc = null;
		if (dataToBeDigested == null || dataToBeDigested.equalsIgnoreCase("")) {
			throw new SecurityAPIExcption("dataToBeDigested is null");
		}
		try {
			mdEnc = MessageDigest.getInstance(ALGORITHM_FOR_SALT_DATA);
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption(
					"No Such Algorithm Exception in salt data genration");
		}

		mdEnc.update(dataToBeDigested.getBytes(), 0, dataToBeDigested.length());

		byte[] dataInBase64 = Base64.encode(mdEnc.digest(), Base64.NO_WRAP);

		digestedStr = new String(dataInBase64);
		return digestedStr;
	}

	public static String getEncryptData(String username, String dataToEncrypt)
			throws SecurityAPIExcption {
		String encriptedText = "";
		try {
			if (username == null || username.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("key is null");
			}
			if (dataToEncrypt == null || dataToEncrypt.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("data is null");
			}
			username = new StringBuilder(username).reverse().toString();
			SECRET_KEY = getSecretKey(username);
			if (SECRET_KEY == null) {
				throw new SecurityAPIExcption("SECRET KEY is null");
			}
			Cipher cipher;
			cipher = Cipher.getInstance(ALGORITHM_FOR_ENCRYPTION);
			AlgorithmParameters.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, IV_PARAMETER);

			byte[] encVal;

			encVal = cipher.doFinal(dataToEncrypt.getBytes("UTF-8"));

			byte[] dataInBase64 = Base64.encode(encVal, Base64.NO_WRAP);
			encriptedText = new String(dataInBase64);

		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption("No Such Algorithm Exception");
		} catch (InvalidKeyException e) {
			throw new SecurityAPIExcption("Invalid Key Exception");
		} catch (InvalidAlgorithmParameterException e) {
			throw new SecurityAPIExcption(
					"Invalid Algorithm Parameter Exception");
		} catch (IllegalBlockSizeException e) {
			throw new SecurityAPIExcption("Illegal Block Size Exception");
		} catch (BadPaddingException e) {
			throw new SecurityAPIExcption("Bad Padding Exception");
		} catch (NoSuchPaddingException e) {
			throw new SecurityAPIExcption("No Such Padding Exception");

		} catch (UnsupportedEncodingException e) {
			throw new SecurityAPIExcption("Unsupported Encoding Exception");
		}
		return encriptedText;
	}

	public static String getDecryptData(String username, String dataToDecrypt)
			throws SecurityAPIExcption {
		String decryptText = "";
		try {
			if (username == null || username.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("key is null");
			}
			if (dataToDecrypt == null || dataToDecrypt.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("data is null");
			}
			username = new StringBuilder(username).reverse().toString();
			SECRET_KEY = getSecretKey(username);

			byte[] textbyte = Base64.decode(dataToDecrypt, Base64.NO_WRAP);
			if (textbyte != null) {

				Cipher cipher = Cipher.getInstance(ALGORITHM_FOR_ENCRYPTION);
				AlgorithmParameters.getInstance("AES");
				if (SECRET_KEY == null) {
					throw new SecurityAPIExcption("SECRET KEY is null");
				}
				cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, IV_PARAMETER);
				byte[] results = new byte[textbyte.length];
				int outputBytes = cipher.doFinal(textbyte, 0, textbyte.length,
						results, 0);
				decryptText = (new String(results)).trim();
			}
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption("No Such Algorithm Exception");
		} catch (InvalidKeyException e) {
			throw new SecurityAPIExcption("Invalid Key Exception");
		} catch (InvalidAlgorithmParameterException e) {
			throw new SecurityAPIExcption(
					"Invalid Algorithm Parameter Exception");
		} catch (IllegalBlockSizeException e) {
			throw new SecurityAPIExcption("Illegal Block Size Exception");
		} catch (BadPaddingException e) {
			throw new SecurityAPIExcption("Bad Padding Exception");
		} catch (NoSuchPaddingException e) {
			throw new SecurityAPIExcption("No Such Padding Exception");

		} catch (ShortBufferException e) {
			throw new SecurityAPIExcption("Short Buffer Exception");
		}
		return decryptText;
	}

	public static String getEncryptUSERData(String username,
			String dataToEncrypt) throws SecurityAPIExcption {
		String encriptedText = "";
		try {
			if (username == null || username.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("key is null");
			}
			if (dataToEncrypt == null || dataToEncrypt.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("data is null");
			}
			username = new StringBuilder(username).reverse().toString();
			SECRET_KEY = getSecretKey(username);
			if (SECRET_KEY == null) {
				throw new SecurityAPIExcption("SECRET KEY is null");
			}
			Cipher cipher;
			cipher = Cipher.getInstance(ALGORITHM_FOR_ENCRYPTION);
			AlgorithmParameters.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, IV_PARAMETER);

			byte[] encVal;

			encVal = cipher.doFinal(dataToEncrypt.getBytes("UTF-8"));

			byte[] dataInBase64 = Base64.encode(encVal, Base64.NO_WRAP);
			encriptedText = new String(dataInBase64);

		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption("No Such Algorithm Exception");
		} catch (InvalidKeyException e) {
			throw new SecurityAPIExcption("Invalid Key Exception");
		} catch (InvalidAlgorithmParameterException e) {
			throw new SecurityAPIExcption(
					"Invalid Algorithm Parameter Exception");
		} catch (IllegalBlockSizeException e) {
			throw new SecurityAPIExcption("Illegal Block Size Exception");
		} catch (BadPaddingException e) {
			throw new SecurityAPIExcption("Bad Padding Exception");
		} catch (NoSuchPaddingException e) {
			throw new SecurityAPIExcption("No Such Padding Exception");

		} catch (UnsupportedEncodingException e) {
			throw new SecurityAPIExcption("Unsupported Encoding Exception");
		}
		return encriptedText;
	}

	public static String getDecryptUSERData(String username,
			String dataToDecrypt) throws SecurityAPIExcption {
		String decryptText = "";
		try {
			if (username == null || username.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("key is null");
			}
			if (dataToDecrypt == null || dataToDecrypt.equalsIgnoreCase("")) {
				throw new SecurityAPIExcption("data is null");
			}
			username = new StringBuilder(username).reverse().toString();
			SECRET_KEY = getSecretKey(username);

			byte[] textbyte = Base64.decode(dataToDecrypt, Base64.NO_WRAP);
			if (textbyte != null) {

				Cipher cipher = Cipher.getInstance(ALGORITHM_FOR_ENCRYPTION);
				AlgorithmParameters.getInstance("AES");
				if (SECRET_KEY == null) {
					throw new SecurityAPIExcption("SECRET KEY is null");
				}
				cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, IV_PARAMETER);
				byte[] results = new byte[textbyte.length];
				int outputBytes = cipher.doFinal(textbyte, 0, textbyte.length,
						results, 0);
				decryptText = (new String(results)).trim();
			}
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityAPIExcption("No Such Algorithm Exception");
		} catch (InvalidKeyException e) {
			throw new SecurityAPIExcption("Invalid Key Exception");
		} catch (InvalidAlgorithmParameterException e) {
			throw new SecurityAPIExcption(
					"Invalid Algorithm Parameter Exception");
		} catch (IllegalBlockSizeException e) {
			throw new SecurityAPIExcption("Illegal Block Size Exception");
		} catch (BadPaddingException e) {
			throw new SecurityAPIExcption("Bad Padding Exception");
		} catch (NoSuchPaddingException e) {
			throw new SecurityAPIExcption("No Such Padding Exception");

		} catch (ShortBufferException e) {
			throw new SecurityAPIExcption("Short Buffer Exception");
		}
		return decryptText;
	}

}