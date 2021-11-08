package cn.com.heyue.utils;

/**
 * @author ：yinbeng
 * @date ：Created in 2020-11-12 13:46
 * @description：xxx
 */
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.lang3.ArrayUtils;
public class EncryptionMachine {
    //初始向量
    //private static String ivInfo = "0000000000000000";
    private static String ivInfo = "4156B52B00000000";
    private static char[] CHARARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static byte[] funStringToBcd(char[] data) {
        int len = data.length;

        if (len % 2 != 0 || len == 0) {
            throw new RuntimeException("数据长度错误");
        }
        byte[] outData = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            outData[i] = (byte) (((Character.digit(data[j], 16) & 0x0F) << 4) | (Character.digit(data[j + 1], 16) & 0x0F));
            j++;
            j++;
        }
        return outData;
    }

    private static String funByteToHexString(byte[] data) {
        int len = data.length;
        char[] outChar = new char[len << 1];
        for (int i = 0, j = 0; j < len; j++) {
            outChar[i++] = CHARARRAY[(0xF0 & data[j]) >>> 4];
            outChar[i++] = CHARARRAY[data[j] & 0x0F];
        }
        String outString = new String(outChar);
        return outString;
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static byte[] funHexStrToByte(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baKeyword;
    }

    private static byte[] ecbEncrypt(byte[] data, byte[] key) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // DES的ECB模式
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data);
            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException("ECB-DES算法，加密数据出错!");
        }
    }

    /**
     * 93.      * 解密函数(ECB)  94.      *   95.      * @param data  96.      *            解密数据  97.      * @param key  98.      *            密钥  99.      * @return 返回解密后的数据  100.
     */
    private static byte[] ecbDecrypt(byte[] data, byte[] key) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // byte rawKeyData[] = /* 用某种方法获取原始密匙数据 */;
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // DES的ECB模式
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(data);
            return decryptedData;
        } catch (Exception e) {
            throw new RuntimeException("ECB-DES算法，解密出错。");
        }
    }

    /**
     * 132.      * 加密函数(CBC)  133.      *   134.      * @param data  135.      *            加密数据  136.      * @param key  137.      *            密钥  138.      * @return 返回加密后的数据  139.
     */
    private static byte[] cbcEncrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // DES的CBC模式,采用NoPadding模式，data长度必须是8的倍数
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            // 用密钥初始化Cipher对象
            IvParameterSpec param = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);
            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data);
            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException("CBC-DES算法，加密数据出错!");
        }
    }

    @SuppressWarnings("unused")
    private static byte[] cbcDecrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // DES的CBC模式,采用NoPadding模式，data长度必须是8的倍数
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            // 用密钥初始化Cipher对象
            IvParameterSpec param = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, param);
            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(data);
            return decryptedData;
        } catch (Exception e) {
            throw new RuntimeException("CBC-DES算法，解密出错。");
        }
    }

    /**
     * 将工作密钥解为明文
     */
    public static String analogDecryptWorkingKey(String plaintextZmk, String ciphertextWorkingKey) {
        byte[] inPlaintextZmk = funStringToBcd(plaintextZmk.toCharArray());
        byte[] inCiphertextWorkingKey = funStringToBcd(ciphertextWorkingKey.toCharArray());
        byte[] leftPlaintextZmk = ArrayUtils.subarray(inPlaintextZmk, 0, 8);
        byte[] rightPlaintextZmk = ArrayUtils.subarray(inPlaintextZmk, 8, 16);
        byte[] leftCiphertextWorkingKey = ArrayUtils.subarray(inCiphertextWorkingKey, 0, 8);
        byte[] rightCiphertextWorkingKey = ArrayUtils.subarray(inCiphertextWorkingKey, 8, 16);
        // 一、WorkingKey左部分解密 (反3DES)
        /* 1、ECB-DES解密  left key */
        byte[] ecbDecryptReturn = ecbDecrypt(leftCiphertextWorkingKey, leftPlaintextZmk);
        System.out.println(funByteToHexString(ecbDecryptReturn));
        /* 2、 ECB-DES加密  right key */
        byte[] ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, rightPlaintextZmk);
        /* 3、 ECB-DES解密  left key */
        byte[] leftPlaintextWorkingKey = ecbDecrypt(ecbEncryptReturn, leftPlaintextZmk);
        // 一、WorkingKey右部分解密 (反3DES)
        /* 1、ECB-DES解密  left key */
        ecbDecryptReturn = ecbDecrypt(rightCiphertextWorkingKey, leftPlaintextZmk);
        /* 2、 ECB-DES加密  right key */
        ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, rightPlaintextZmk);
        /* 3、 ECB-DES解密  left key */
        byte[] rightPlaintextWorkingKey = ecbDecrypt(ecbEncryptReturn, leftPlaintextZmk);
        byte[] plaintextWorkingKey = ArrayUtils.addAll(leftPlaintextWorkingKey, rightPlaintextWorkingKey);
        return funByteToHexString(plaintextWorkingKey);
    }

    /**
     * 244.      * 计算密钥校验值  245.      *@param plaintextKey  246.      *      明文KEY  247.      *@return  248.      *      返回校验值  249.
     **/
    public static String getCheckValueOfKey(String plaintextKey) {
        byte[] desData = funStringToBcd("0000000000000000".toCharArray());
        byte[] leftPlaintextKey = funStringToBcd(plaintextKey.substring(0, 16).toCharArray());
        byte[] rightPlaintextKey = funStringToBcd(plaintextKey.substring(16, 32).toCharArray());
        /* 1、ECB-DES加密  left key */
        byte[] ecbEncryptReturn = ecbEncrypt(desData, leftPlaintextKey);
        /* 2、 ECB-DES解密  right key */
        byte[] ecbDecryptReturn = ecbDecrypt(ecbEncryptReturn, rightPlaintextKey);
        /* 3、 ECB-DES加密  left key */
        ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, leftPlaintextKey);
        return funByteToHexString(ArrayUtils.subarray(ecbEncryptReturn, 0, 3));
    }

    /**
     * 267.      * 3DES算法计算MAC  268.      *@param macKey  269.      *      计算mac的Key(明文)  270.      *@param macData  271.      *      计算mac的数据域  272.      *@return  273.      *      返回mac数据  274.
     **/
    public static String analogMacBy3Des(String macKey, String macData,String iv) {
        //byte[] inMacKey = funStringToBcd(macKey.toCharArray());
        byte[] inMacKey = funHexStrToByte(macKey);
        //byte[] inMacData = macData.getBytes();
        byte[] inMacData = funHexStrToByte(macData);
        if (inMacData.length % 8 != 0) {
            int iFillLen = 8 - inMacData.length % 8;
            byte[] bFillData = new byte[iFillLen];
            for (int i = 0; i < iFillLen; i++) {
                bFillData[i] = 0x00;
            }
            inMacData = ArrayUtils.addAll(inMacData, bFillData);
        }
        //初始变量
        //byte[] ivData = funStringToBcd(ivInfo.toCharArray());
        byte[] ivData = funHexStrToByte(iv);
        //left key
        byte[] lKey = ArrayUtils.subarray(inMacKey, 0, 8);
        //right key
        byte[] rKey = ArrayUtils.subarray(inMacKey, 8, 16);
        /* 1、CBC-DES加密  left key */
        byte[] cbcEncryptReturn = cbcEncrypt(inMacData, lKey, ivData);
        System.out.println("cbc加密后："+funByteToHexString(cbcEncryptReturn));
        byte[] ecbDecryptData = ArrayUtils.subarray(cbcEncryptReturn, cbcEncryptReturn.length - 8, cbcEncryptReturn.length);
        System.out.println("截取后8位："+funByteToHexString(ecbDecryptData));
        /* 2、 ECB-DES解密  right key */
        byte[] ecbDecryptReturn = ecbDecrypt(ecbDecryptData, rKey);
        System.out.println("ecb解密后："+funByteToHexString(ecbDecryptReturn));
        /* 3、 ECB-DES加密  left key */
        byte[] ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, lKey);
        System.out.println("ecb加密后："+funByteToHexString(ecbEncryptReturn));
        return funByteToHexString(ecbEncryptReturn);
    }

    /**
     * 311.      * PIN加密  312.      *@param pinKey  313.      *      计算pin的Key  314.      *@param cardNo  315.      *      卡号/主账号  316.      *@param pinData  317.      *      明文PIN  318.      *@return  319.      *      返回pin密文  320.
     **/
    public static String analogPinEncrypt(String pinKey, String cardNo, String pinData) {
        byte[] inPinKey = funStringToBcd(pinKey.toCharArray());
        //left key
        byte[] lKey = ArrayUtils.subarray(inPinKey, 0, 8);
        //right key
        byte[] rKey = ArrayUtils.subarray(inPinKey, 8, 16);
        /* xorPin 处理 */
        byte[] xorPin = funStringToBcd(String.format("%02d%-14s", pinData.length(), pinData).replace(' ', 'F').toCharArray());
        /* xorPan 处理*/
        byte[] xorPan = funStringToBcd(String.format("0000%12s", cardNo.substring(cardNo.length() - 13, cardNo.length() - 1)).toCharArray());
        /* xorData 处理*/
        byte[] xorData = new byte[8];
        for (int i = 0; i < 8; i++) {
            xorData[i] = (byte) (xorPin[i] ^ xorPan[i]);
        }
        /* 1、ECB-DES加密  left key */
        byte[] ecbEncryptReturn = ecbEncrypt(xorData, lKey);
        //      System.out.println(funByteToHexString(xorData) + " " + funByteToHexString(lKey));
        /* 2、 ECB-DES解密  right key */
        byte[] ecbDecryptReturn = ecbDecrypt(ecbEncryptReturn, rKey);
        //      System.out.println(funByteToHexString(ecbEncryptReturn) + " " + funByteToHexString(rKey));
        /* 3、 ECB-DES加密  left key */
        ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, lKey);
        //      System.out.println(funByteToHexString(ecbDecryptReturn) + " " + funByteToHexString(lKey));
        return funByteToHexString(ecbEncryptReturn);
    }

    public static void main(String[] args){
        //mac 测试
        String macBuf = analogMacBy3Des("A1808C707646F102026ED0F86BFB5EC4", "841301001C48A30CBBE7E3FFD70112AF605A5DFADD0A470E5462A7FF82800000","4156B52B00000000");
        System.out.println(macBuf);

        //pin 测试
        /*String pinBuf = analogPinEncrypt("92BC5DA70DC46E9E8A380E2F85105EF8", "512316**********", "369");
        System.out.println(pinBuf);

        //workKey解密
        String workkeyBuf = analogDecryptWorkingKey("33333333333234543422222222222222", "CDEECF47CF0EEBE879D7270EAF5036D9");
        System.out.println(workkeyBuf);
        //       计算校验值
        String chKeyBuf = getCheckValueOfKey("92BC5DA70DC46E9E8A380E2F85105EF8");
        System.out.println(chKeyBuf);*/
    }
}