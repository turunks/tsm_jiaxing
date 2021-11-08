package cn.com.heyue.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

/**
 * @author ：dengjie
 * @date ：2021/4/22
 * @description ：
 */
public class EncodesUtils {


    private final static String KEY_ALGORITHM = "AES";
    private final static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private final static String DEFAULT_ENCODING = "UTF-8";

    public static String encryptToString(String key, String offset, String content) throws Exception {
        return byteArrayToHex(encryptProcess(key, offset, content, DEFAULT_ENCODING));
    }


    private static byte[] encryptProcess(String key, String offset, String content, String encoding) throws Exception {
        byte[] data = {};
        Key secretKey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
        byte[] byteContent = content.getBytes(encoding);
        IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        data = cipher.doFinal(byteContent);
        return data;
    }

    public static String byteArrayToHex(byte[] bytes) {
        String retorno = "";
        if (bytes == null || bytes.length == 0) {
            return retorno;
        }
        for (int i = 0; i < bytes.length; i++) {
            byte valor = bytes[i];
            int d1 = valor & 0xF;
            d1 += (d1 < 10) ? 48 : 55;
            int d2 = (valor & 0xF0) >> 4;
            d2 += (d2 < 10) ? 48 : 55;
            retorno = retorno + (char) d2 + (char) d1;
        }
        return retorno;
    }

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * 对输入字符串进行md5散列.
     */
    public static byte[] md5(byte[] input) {
        return digest(input, MD5, null, 1);
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(input);

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            return null;
        }
    }

    /**
     * 获取签名串（key1=&key2=value2...）
     *
     * @param params   参数集
     * @param signName 不参与签名的参数
     * @return
     */
    public static String getSignData(final Map<String, String> params, String signName) throws Exception {
        final StringBuilder sb = new StringBuilder();
        // 按照key做排序
        final List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            final String key = (String) keys.get(i);
            if (signName.equals(key)) {
                continue;
            }
            final String value = (String) params.get(key);
            if (StringUtils.isNotBlank(value)) {
                sb.append((i == 0 ? "" : "&") + key + "=" + new String(value.getBytes(), "UTF-8"));
            } else   //  如果value为null，则写成：key=&key1=value1
            {
                sb.append((i == 0 ? "" : "&") + key + "=");
            }
        }
        return sb.toString();
    }

    public static String decryptToString(String key, String offset, String content) throws Exception {
        return new String(decryptProcess(key, offset, content), DEFAULT_ENCODING);
    }

    public static byte[] hexToByteArray(String hexa) throws Exception {
        if (hexa == null) {
            throw new Exception();
        }
        if ((hexa.length() % 2) != 0) {
            throw new Exception();
        }
        int tamArray = hexa.length() / 2;
        byte[] retorno = new byte[tamArray];
        for (int i = 0; i < tamArray; i++) {
            retorno[i] = hexToByte(hexa.substring(i * 2, i * 2 + 2));
        }
        return retorno;
    }

    private static byte[] decryptProcess(String key, String offset, String content) throws Exception {
        byte[] data = {};
        Key secretKey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        data = cipher.doFinal(hexToByteArray(content));
        return data;
    }

    public static byte hexToByte(String hexa) throws Exception {
        if (hexa == null) {
            throw new Exception();
        }
        if (hexa.length() != 2) {
            throw new Exception();
        }
        byte[] b = hexa.getBytes();
        byte valor = (byte) (hexDigitValue((char) b[0]) * 16 + hexDigitValue((char) b[1]));
        return valor;
    }

    private static int hexDigitValue(char c) throws Exception {
        int retorno = 0;
        if (c >= '0' && c <= '9') {
            retorno = (int) (((byte) c) - 48);
        } else if (c >= 'A' && c <= 'F') {
            retorno = (int) (((byte) c) - 55);
        } else if (c >= 'a' && c <= 'f') {
            retorno = (int) (((byte) c) - 87);
        } else {
            throw new Exception();
        }
        return retorno;
    }

}

 