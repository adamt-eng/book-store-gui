package main_app.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class SecurityService
{
	private static final String AES_CIPHER = "AES/ECB/PKCS5Padding";
	private static final String SHA_256 = "SHA-256";

	private static SecretKey generateSecretKey()
	{
		try
		{
			byte[] keyBytes = "NCrtUUNQk0G4DI48nZP1+fZvBWmPm9mYLr4P+gCa2mo=".getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance(SHA_256);
			keyBytes = sha.digest(keyBytes);
			return new SecretKeySpec(keyBytes, 0, 16, "AES");
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static String encrypt(String plainText)
	{
		try
		{
			SecretKey secretKey = generateSecretKey();
			Cipher cipher = Cipher.getInstance(AES_CIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encryptedBytes);
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static String decrypt(String encryptedText)
	{
		try
		{
			SecretKey secretKey = generateSecretKey();
			Cipher cipher = Cipher.getInstance(AES_CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
			return new String(decryptedBytes, "UTF-8");
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static String hash(String input)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance(SHA_256);
			byte[] hashedBytes = digest.digest(input.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) sb.append(String.format("%02x", b));
			return sb.toString();
		}
		catch (Exception e)
		{
			return "";
		}
	}
}
