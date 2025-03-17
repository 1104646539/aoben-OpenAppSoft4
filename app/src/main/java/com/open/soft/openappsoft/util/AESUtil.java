package com.open.soft.openappsoft.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // 使用CBC模式和PKCS5填充
    private static final int KEY_SIZE = 256; // 密钥长度
    private static final int ITERATION_COUNT = 1000; // PBKDF2迭代次数

    /**
     * 使用密码和盐生成密钥和IV
     */
    private static SecretKey generateKey(String password, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, KEY_SIZE);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static byte[] generateIV(String password, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, 128); // IV长度为128位
        return factory.generateSecret(spec).getEncoded();
    }

    /**
     * AES加密
     */
    public static String encrypt(String plainText, String password, String salt) {
        try {
            SecretKey secretKey = generateKey(password, salt);
            byte[] iv = generateIV(password, salt);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     */
    public static String decrypt(String encryptedText, String password, String salt) {
        try {
            SecretKey secretKey = generateKey(password, salt);
            byte[] iv = generateIV(password, salt);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.decode(encryptedText,Base64.DEFAULT));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    public static void main(String[] args) {
        String password = "myPassword123"; // 密码
        String salt = "randomSalt"; // 盐值
        String plainText = "Hello, this is a secret message!";

        // 加密
        String encryptedText = encrypt(plainText, password, salt);
        System.out.println("Encrypted (Java): " + encryptedText);

        // 解密
        String decryptedText = decrypt(encryptedText, password, salt);
        System.out.println("Decrypted (Java): " + decryptedText);
    }
}