package utils;

import exceptions.DecryptException;
import exceptions.EncryptException;
import org.h2.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Utils {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ENCRYPTION_PASS = "73cc3c7e0f9f15ae834d798dfb166105";

    public static Boolean isNotNullOrEmpty(String string){
        return !StringUtils.isNullOrEmpty(string);
    }
    public static Boolean isNullOrEmpty(String string){
        return StringUtils.isNullOrEmpty(string);
    }
    public static String toMD5(String value) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(value.getBytes(),0,value.length());
        return new BigInteger(1,m.digest()).toString(16);
    }

    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(final String strToEncrypt) throws EncryptException {
        try {
            setKey(ENCRYPTION_PASS);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            throw new EncryptException();
        }
    }

    public static String decrypt(final String strToDecrypt) throws DecryptException {
        try {
            setKey(ENCRYPTION_PASS);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            throw new DecryptException();
        }
    }
}

