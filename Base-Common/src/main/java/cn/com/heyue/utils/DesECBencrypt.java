package cn.com.heyue.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * DES加密和解密
 *
 * @author 付祖远
 * @version V1.0
 */
public class DesECBencrypt {
    /**
     * 加密数据
     * @param encryptbyte  注意：这里的数据长度只能为8的倍数
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptDES(byte[] encryptbyte, byte[] encryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getKey(encryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(encryptbyte);
        return encryptedData;
    }

    /**
     * 自定义一个key
     * @param keyRule
     */
    public static byte[] getKey(byte[] keyRule) {
        Key key = null;
        byte[] keyByte = keyRule;
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        key = new SecretKeySpec(byteTemp, "DES");
        return key.getEncoded();
    }

    /***
     * 解密数据
     * @param decryptString
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString, byte[] decryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getKey(decryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(ConvertUtils.hexStringToByte(decryptString));

        return new String(decryptedData);
    }

    public static void main(String[] args) {
        String str="";
        System.out.println("-----------------");
        try {
            //str = decrypt("A104E4BA4E121B4E", "1111111111111111");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }
}

