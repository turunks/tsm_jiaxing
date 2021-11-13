package cn.com.heyue.smencrypt.sm4;

import cn.com.heyue.smencrypt.Util;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

/**
 * @author ：yinbeng
 * @date ：Created in 2020-10-15 15:58
 * @description：xxx
 */
public class SM4Util {
    //加解密的字节快大小
    public static final int BLOCK_SIZE = 16;

    public static byte[] padding(byte[] input, int mode) {
        if (input == null) {
            return null;
        }

        byte[] ret = (byte[]) null;
        if (mode == 1) {
            //填充:hex必须是32的整数倍填充 ,填充的是80  00 00 00
            int p = 16 - input.length % 16;
            String inputHex = Hex.toHexString(input)+ "80";
            StringBuffer stringBuffer =new StringBuffer(inputHex);
            for (int i = 0; i <p-1 ; i++) {
                stringBuffer.append("00");
            }
            ret= Hex.decode(stringBuffer.toString());
        } else {
            /*int p = input[input.length - 1];
            ret = new byte[input.length - p];
            System.arraycopy(input, 0, ret, 0, input.length - p);*/
            String inputHex =Hex.toHexString(input);
            int i = inputHex.lastIndexOf("80");
            String substring = inputHex.substring(0, i);
            //ret= Util.hexToByte(substring);
            ret= Hex.decode(substring);
        }
        return ret;
    }

    public static String padding(String data) {
        byte[] input = Hex.decode(data);
        //填充:hex必须是32的整数倍填充 ,填充的是80  00 00 00
        int p = 16 - input.length % 16;
        String inputHex = Hex.toHexString(input)+ "80";
        StringBuffer stringBuffer =new StringBuffer(inputHex);
        for (int i = 0; i <p-1 ; i++) {
            stringBuffer.append("00");
        }
        return stringBuffer.toString();
    }

    /**
     * SM4ECB加密算法
     * @param in            待加密内容
     * @param keyBytes      密钥
     * @return
     */
    public static byte[] encryptByEcb0(byte[] in, byte[] keyBytes,Boolean isPadding) {
        SM4Engine sm4Engine = new SM4Engine();
        sm4Engine.init(true, new KeyParameter(keyBytes));

        if(isPadding){
            in = padding(in, 1);
        }
        int inLen = in.length;
        byte[] out = new byte[inLen];

        int times = inLen / BLOCK_SIZE;

        for (int i = 0; i < times; i++) {
            sm4Engine.processBlock(in, i * BLOCK_SIZE, out, i * BLOCK_SIZE);
        }

        return out;
    }

    /**
     * SM4ECB加密算法
     * @param in            待加密内容
     * @param keyBytes      密钥
     * @return
     */
    public static String encryptByEcb(byte[] in, byte[] keyBytes,Boolean isPadding) {
        byte[] out = encryptByEcb0(in, keyBytes,isPadding);
        String cipher = Hex.toHexString(out);
        return cipher;
    }

    /**
     * SM4的ECB加密算法
     * @param content   待加密内容
     * @param key       密钥
     * @return
     */
    public static String encryptByEcb(String content, String key,Boolean isPadding) {
        byte[] in = Hex.decode(content);
        byte[] keyBytes = Hex.decode(key);

        String cipher = encryptByEcb(in, keyBytes,isPadding);
        return cipher.toUpperCase();
    }

    /**
     * SM4的ECB解密算法
     * @param in        密文内容
     * @param keyBytes  密钥
     * @return
     */
    public static byte[] decryptByEcb0(byte[] in, byte[] keyBytes) {
        SM4Engine sm4Engine = new SM4Engine();
        sm4Engine.init(false, new KeyParameter(keyBytes));

        int inLen = in.length;
        byte[] out = new byte[inLen];

        int times = inLen / BLOCK_SIZE;

        for (int i = 0; i < times; i++) {
            sm4Engine.processBlock(in, i * BLOCK_SIZE, out, i * BLOCK_SIZE);
        }

        return out;
    }

    /**
     * SM4的ECB解密算法
     * @param in        密文内容
     * @param keyBytes  密钥
     * @return
     */
    public static String decryptByEcb(byte[] in, byte[] keyBytes,Boolean isPadding) {
        byte[] out = decryptByEcb0(in, keyBytes);
        if(isPadding){
            out = padding(out, 0);
        }
        String plain = Hex.toHexString(out);
        //String plain = new String(out);

        return plain;
    }

    /**
     * SM4的ECB解密算法
     * @param cipher    密文内容
     * @param key       密钥
     * @return
     */
    public static String decryptByEcb(String cipher, String key,Boolean isPadding) {
        byte[] in = Hex.decode(cipher);
        byte[] keyBytes = Hex.decode(key);

        String plain = decryptByEcb(in, keyBytes,isPadding);
        return plain;
    }

    public static byte[] doCBCMac(byte[] key, byte[] iv, byte[] data) {
        SM4Engine engine = new SM4Engine();
        BlockCipherPadding padding = new ISO7816d4Padding();//补0x80 00...
        org.bouncycastle.crypto.Mac mac = new CBCBlockCipherMac(engine, engine.getBlockSize() * 8, padding);
        CipherParameters cipherParameters = new KeyParameter(key);
        mac.init(new ParametersWithIV(cipherParameters, iv));
        mac.update(data, 0, data.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
        //  return doMac(mac, key, iv, data);
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        /*String d = "{\"dealDateTime\":\"20201013155823\",\"validDate\":\"20201231\"}";
        String tk = "00000000000000000000000000000000";//约定密钥
        String rk = "00000000000000000000000000000000";//过程密钥

        System.out.println("业务数据:"+d);
        System.out.println("编码填充:"+Hex.toHexString(d.getBytes(Charset.forName("UTF-8"))).toUpperCase());
        System.out.println("约定密钥:"+tk);
        System.out.println("过程密钥:"+rk);

        String encData = SM4Util.encryptByEcb(Hex.toHexString(d.getBytes()),rk,true);
        System.out.println("业务数据加密："+encData.toUpperCase());

        String encKeyData = SM4Util.encryptByEcb(rk,tk,false);
        System.out.println("过程密钥加密："+encKeyData.toUpperCase());

        String mac = Hex.toHexString(Arrays.copyOf(SM4.doCBCMac(Util.hexStringToBytes(rk),new byte[16],Util.hexStringToBytes(encData)),4));
        System.out.println("计算摘要mac:"+mac);

        String rkData = SM4Util.decryptByEcb(encKeyData,tk,false);
        System.out.println("过程密钥解密，明文："+rkData);

        String data = SM4Util.decryptByEcb(encData,rkData,true);
        System.out.println("业务数据解密，明文："+new String(Hex.decode(data)));*/


        String d = "{\"balance\":10000,\"cardDealNo\":2,\"cardNo\":\"3104895199900000002\",\"dealDateTime\":\"20201116162157\",\"issueInst\":\"05212482\",\"mac1\":\"0C9E4561\",\"random\":\"94F830A2\",\"skey\":0}";
        String tk = "C1EB0430933D26AFD148D21E0387708D";//约定密钥
        String rk = "2b0e13d6beec4b138b60784b6ad259ec";//过程密钥

        System.out.println("业务数据:"+d);
        System.out.println("编码填充:"+Hex.toHexString(d.getBytes(Charset.forName("UTF-8"))).toUpperCase());
        System.out.println("约定密钥:"+tk);
        System.out.println("过程密钥:"+rk);

        String encData = SM4Util.encryptByEcb(Hex.toHexString(d.getBytes()),rk,true);
        System.out.println("业务数据加密："+encData.toUpperCase());

        String encKeyData = SM4Util.encryptByEcb(rk,tk,false);
        System.out.println("过程密钥加密："+encKeyData.toUpperCase());

        String s = encData + encKeyData;

        String mac = Hex.toHexString(Arrays.copyOf(SM4.doCBCMac(Util.hexStringToBytes(rk),new byte[16],Util.hexStringToBytes(s)),4));
        System.out.println("计算摘要mac:"+mac);

        String rkData = SM4Util.decryptByEcb(encKeyData,tk,false);
        System.out.println("过程密钥解密，明文："+rkData);

        String data = SM4Util.decryptByEcb(encData,rkData,true);
        System.out.println("业务数据解密，明文："+new String(Hex.decode(data)));
    }
}
