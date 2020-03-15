package com.indorse.java.assignment.friendbook.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.tomcat.util.buf.HexUtils;

public class CommonUtil {
  public static boolean isEmpty(String input) {
    if (input == null || input.trim().equals("")) {
      return true;
    }

    return false;
  }

  public static String generateSaltPasswordHash(String salt, String password)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    byte[] saltBytes = HexUtils.fromHexString(salt);

    KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 128);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = factory.generateSecret(spec).getEncoded();
    return HexUtils.toHexString(hash);
  }

  public static String generateNewSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return HexUtils.toHexString(salt);
  }
}
