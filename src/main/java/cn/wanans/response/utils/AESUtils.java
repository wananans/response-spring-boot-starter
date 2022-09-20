package cn.wanans.response.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author w
 * @since 2022-09-13
 */
public class AESUtils {


    /**
     * 可选加密模式
     *
     * <p>AES/CBC/NoPadding （128）</p>
     * <p>AES/CBC/PKCS5Padding （128）</p>
     * <p>AES/ECB/NoPadding （128）</p>
     * <p>AES/ECB/PKCS5Padding （128）</p>
     * <p>AES/GCM/NoPadding （128）</p>
     * <p>DES/CBC/NoPadding （56）</p>
     * <p>DES/CBC/PKCS5Padding （56）</p>
     * <p>DES/ECB/NoPadding （56）</p>
     * <p>DES/ECB/PKCS5Padding （56）</p>
     * <p>DESede/CBC/NoPadding （168）</p>
     * <p>DESede/CBC/PKCS5Padding （168）</p>
     * <p>DESede/ECB/NoPadding （168）</p>
     * <p>DESede/ECB/PKCS5Padding （168）</p>
     * <p>RSA/ECB/PKCS1Padding （ RSA/ECB/PKCS1Padding ）</p>
     * <p>RSA/ECB/OAEPWithSHA-1AndMGF1Padding （ RSA/ECB/OAEPWithSHA-1AndMGF1Padding ）</p>
     * <p>RSA/ECB/OAEPWithSHA-256AndMGF1Padding （ RSA/ECB/OAEPWithSHA-256AndMGF1Padding ）</p>
     */
    private static final String ENCRYPT_MODEL = "AES/ECB/PKCS5Padding";


    // 获取 cipher
    private static Cipher getCipher(byte[] key, int model) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(ENCRYPT_MODEL);
        cipher.init(model, secretKeySpec);
        return cipher;
    }

    // AES加密
    public static String encrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    // AES解密
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(Base64.getDecoder().decode(data));
    }


    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = getCipher(key.getBytes(StandardCharsets.UTF_8), Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }


    public static byte[] decrypt(String data, String key) throws Exception {
        Cipher cipher = getCipher(key.getBytes(StandardCharsets.UTF_8), Cipher.DECRYPT_MODE);
        return cipher.doFinal(Base64.getDecoder().decode(data));
    }
}
