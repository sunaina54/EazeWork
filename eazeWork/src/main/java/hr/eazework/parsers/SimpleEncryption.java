package hr.eazework.parsers;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * 
 * @author Shrangar
 */
public class SimpleEncryption {

	private SimpleEncryption() {

	}

	private static String ENCRYPTION_KEY = null;

	private static final String ALGO = "AES";

	public static String encrypt(String data) throws Exception {
		String encryptedValue = null;
		if (ENCRYPTION_KEY != null && data != null) {
			Key key = generateKey(ENCRYPTION_KEY);
			Cipher c = Cipher.getInstance(ALGO);
			c.getAlgorithm();
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(data.getBytes());
			encryptedValue = Base64.encodeToString(encVal, Base64.NO_WRAP);
		}
		return encryptedValue;
	}

	public static byte[] decrypt(byte[] encryptedData) throws Exception {
		byte[] decValue = null;
		if (ENCRYPTION_KEY != null && encryptedData != null) {
			Key key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ALGO);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.decode(encryptedData,
					Base64.NO_PADDING);
			decValue = c.doFinal(decordedValue);
		}
		return decValue;
	}

	private static Key generateKey(String keyValue) throws Exception {
		Key key = new SecretKeySpec(keyValue.getBytes(), ALGO);
		return key;
	}

	public static void setENCRYPTION_KEY(String eNCRYPTION_KEY) {
		ENCRYPTION_KEY = eNCRYPTION_KEY;
	}
}