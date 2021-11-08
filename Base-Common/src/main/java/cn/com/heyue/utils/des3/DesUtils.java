package cn.com.heyue.utils.des3;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * simple introduction
 *
 * <p>
 * detailed comment
 * @author zjj 2016-4-12
 * @see
 * @since 1.0
 */
public class DesUtils
{

    static String DES = "DES/ECB/NoPadding";
    static String TriDes = "DESede/ECB/NoPadding";

    /**
     * 把16进制字符串转换成字节数组
     * @param hex
     * @return
     */
    public static byte[] hexStringToBytes(String hex)
    {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++)
        {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 把字节数组转化为16进制字符串
     * @param bytes byte[]
     * @return String
     */
    public static String bytesToHexString(byte[] bytes)
    {
        String ret = "";
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 将字符转化为字节
     * @param c
     * @return
     * @author zjj 2016-4-12
     * @since 1.0
     */
    private static byte toByte(char c)
    {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**单DES加密算法
     * <p>如果参数是字符串，请先用{@link DesUtils.hexStringToByte}转换为字节数组
     * @param key 密钥，字节数组,长度为8
     * @param data 待加密的源数据，字节数组
     * @return
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] des_encrypt(byte key[], byte data[]) {
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance(DES);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**单DES解密算法
     * <p>如果参数是字符串，请先用{@link DesUtils.hexStringToByte}转换为字节数组
     * @param key 密钥，字节数组，长度为8
     * @param data 待加密的源数据，字节数组
     * @return
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] des_decrypt(byte key[], byte data[]) {
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(DES);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3DES加密算法
     * @param keyStr String类型的密钥,在方法中会自动转化为字节数组
     * @param data byte[]待加密的源数据
     * @return byte[] 加密后的字节数组
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] trides_encrypt(String keyStr, byte data[]) {

        return trides_encrypt(hexStringToBytes(keyStr),data);

    }
    /**
     * 3DES加密算法
     * @param keyStr String类型的密钥,在方法中会自动转化为字节数组
     * @param dataStr String类型的密钥,在方法中会自动转化为字节数组
     * @return byte[] 加密后的字节数组
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] trides_encrypt(String keyStr, String dataStr) {

        return trides_encrypt(hexStringToBytes(keyStr),hexStringToBytes(dataStr));

    }
    /**
     * 3DES加密算法
     * <p>如果参数是字符串，请先用{@link DesUtils.hexStringToByte}转换为字节数组
     * @param key byte[]密钥,字节数组,长度固定为24
     * @param data byte[]待加密的源数据
     * @return byte[] 加密后的字节数组
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] trides_encrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            int len = data.length;
            if(data.length % 8 != 0){
                len = data.length - data.length % 8 + 8;
            }
            byte [] needData = null;
            if(len != 0)
                needData = new byte[len];

            for(int i = 0 ; i< len ; i++){
                needData[i] = 0x00;
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(needData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3DES解密算法
     * @param keyStr String类型的密钥,在方法中会自动转化为字节数组
     * @param data byte[]待加密的源数据
     * @return byte[] 加密后的字节数组
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] trides_decrypt(String keyStr, byte data[]) {

        return trides_encrypt(hexStringToBytes(keyStr),data);

    }
    /**
     * 3DES解密算法
     * @param keyStr String类型的密钥,在方法中会自动转化为字节数组
     * @param dataStr String类型的密钥,在方法中会自动转化为字节数组
     * @return byte[] 加密后的字节数组
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] trides_decrypt(String keyStr, String dataStr) {

        return trides_encrypt(hexStringToBytes(keyStr),hexStringToBytes(dataStr));

    }
    /**
     * 3DES解密算法
     * <p>如果参数是字符串，请先用{@link DesUtils.hexStringToByte}转换为字节数组
     * @param key byte[]密钥,字节数组,长度固定为24
     * @param data byte[]待加密的源数据
     * @return byte[] 加密后的字节数组
     * @author zjj 2016-4-12
     * @since 1.0
     */
    public static byte[] trides_decrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            int len = data.length;
            if(data.length % 8 != 0){
                len = data.length - data.length % 8 + 8;
            }
            byte [] needData = null;
            if(len != 0)
                needData = new byte[len];

            for(int i = 0 ; i< len ; i++){
                needData[i] = 0x00;
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(needData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
