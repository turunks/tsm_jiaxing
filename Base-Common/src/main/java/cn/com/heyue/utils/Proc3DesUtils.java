package cn.com.heyue.utils;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;

/**
 * PROC-3DES-MAC加密
 *
 * @author 付祖远
 * @version V1.0
 */
public class Proc3DesUtils {
    public static void main(String[] args) {
        //测试
        //包含汉字的字符串
        //String str= "182013022708453600030000000300379401|00000202|ksds|lk|20130227|20130309|499130293990007|优惠券";

        //纯字母数字
        String str2 = "841301001C48A30CBBE7E3FFD70112AF605A5DFADD0A470E5462A7FF82800000";
        String result = Get_PBOC_DES(str2.getBytes(), "00000000000000000000000000000000", "4156B52B00000000");
        System.out.println("PBOCDES加密后：" + result);
    }

    /*
     *  PBOCDES加密
     *  @param shuju:加密的数据的byte[]
     *  @param key:密钥 16位十六进制
     *  @param IV:初始向量，默认为0000000000000000
     */
    public static String Get_PBOC_DES(byte[] shuju, String key, String IV) {
        String returntype = "";
        try {
            //----------------------------------------
            byte[] keyss = new byte[8];
            byte[] IVS = new byte[8];
            keyss = ConvertUtils.hexStringToByte(key);
            IVS = ConvertUtils.hexStringToByte(IV);
            //----------------------------------------
            byte[] keys = keyss;
            //数据内容字节数组
            String slshuju = ConvertUtils.bytesToHexString(shuju);
            int TLen = 0;
            int DBz = 0;
            if (slshuju.length() % 16 != 0 || slshuju.length() % 16 == 0) {
                TLen = (((int) (slshuju.length() / 16)) + 1) * 16;
                DBz = (slshuju.length() / 16) + 1;
                slshuju = slshuju + "8";
                TLen = TLen - slshuju.length();
                for (int i = 0; i < TLen; i++) {
                    slshuju = slshuju + "0";
                }
            }
            byte[] Zshuju = new byte[slshuju.length() / 2];
            Zshuju = ConvertUtils.hexStringToByte(slshuju);

            byte[] D1 = new byte[8];
            byte[] D2 = new byte[8];
            byte[] I2 = new byte[8];
            byte[] I3 = new byte[8];
            byte[] bytTemp = new byte[8];
            byte[] bytTempX = new byte[8];
            //初始向量
            byte[] I0 = IVS;
            if (DBz >= 1) {
                for (int i = 0; i < 8; i++) {
                    D1[i] = Zshuju[i];
                }
                for (int i = 0; i < 8; i++) {
                    bytTemp[i] = (byte) (I0[i] ^ D1[i]);
                }
                I2 = bytTemp;
                bytTempX = DesECBencrypt.encryptDES(I2, keys);
            }
            if (DBz >= 2) {
                for (int j = 2; j <= DBz; j++) {
                    for (int i = (j - 1) * 8; i < j * 8; i++) {
                        D2[i - (j - 1) * 8] = Zshuju[i];
                    }
                    for (int i = 0; i < 8; i++) {
                        bytTemp[i] = (byte) (bytTempX[i] ^ D2[i]);
                    }
                    I3 = bytTemp;
                    bytTempX = DesECBencrypt.encryptDES(I3, keys);
                }
            }
            returntype = ConvertUtils.bytesToHexString(bytTempX);

        } catch (Exception e) {
            returntype = "";
        }
        return returntype;
    }


    /**
     * 3des解密
     * @param senkey 密钥
     * @param sendata 待解密数据
     * @return
     * @throws Exception
     */
    public static String decryptMode(String senkey, String sendata) throws Exception{
        byte[] key = new BASE64Decoder().decodeBuffer(senkey);
        byte[] data = new BASE64Decoder().decodeBuffer(sendata);

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
        cipher.init(2, deskey);
        byte[] bOut = cipher.doFinal(data);
        return new String(bOut, "UTF-8");
    }

}
