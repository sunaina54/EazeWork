package hr.eazework.parsers;

import javax.crypto.spec.IvParameterSpec;

public interface SecurityMaster {
	// Algorithm for Encryption
	final static String ALGORITHM_FOR_ENCRYPTION = "AES/CBC/PKCS7Padding";
	// Secret key use

	// initialize vector
	final static byte[] IV = new byte[128 / 8];

	final static IvParameterSpec IV_PARAMETER = new IvParameterSpec(IV);
	// Provider for Key generation
	final static String PROVIDER = "BC";
	// Algorithm for Key generation
	final static String RANDOM_ALGORITHM = "PBKDF2WithHmacSHA1";

	final static int PBE_ITERATION_COUNT = 129;
	final static int KEY_SIZE = 256;
	final static String ALGORITHM_FOR_SALT_DATA = "SHA-1";
}
