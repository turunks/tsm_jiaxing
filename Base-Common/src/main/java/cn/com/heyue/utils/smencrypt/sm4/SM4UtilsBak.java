package cn.com.heyue.utils.smencrypt.sm4;

/**
 * Created by $(USER) on $(DATE)
 */

import cn.com.heyue.utils.smencrypt.Util;
import org.apache.commons.codec.binary.Base64;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4UtilsBak {
//	private String secretKey = "";
//    private String iv = "";
//    private boolean hexString = false;

    public String secretKey = "";
    public String iv = "";
    public boolean hexString = false;

    public SM4UtilsBak() {
    }


    public String encryptData_ECB(String plainText,Boolean isPadding) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = isPadding;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] textBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                textBytes = Util.hexStringToBytes(plainText);
            } else {
                keyBytes = Util.hexStringToBytes(secretKey);
                textBytes = plainText.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            //byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("UTF-8"));
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, textBytes);
            return Util.byteToHex(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decryptData_ECB(String cipherText,Boolean isPadding) {
        try {
            /*byte[] encrypted = Util.hexToByte(cipherText);
            cipherText=Base64.encodeBase64String(encrypted);;
            //cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }*/

            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = isPadding;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] textBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                textBytes = Util.hexStringToBytes(cipherText);
            } else {
                keyBytes = Util.hexStringToBytes(secretKey);
                textBytes = cipherText.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            //byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.decodeBase64(textBytes));
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, textBytes);
            //byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
            //return new String(decrypted, "UTF-8");
            return Util.byteToString(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encryptData_CBC(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            byte[] textBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
                textBytes = Util.hexStringToBytes(plainText);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
                textBytes = plainText.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            //byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("UTF-8"));
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, textBytes);
            return Util.byteToHex(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decryptData_CBC(String cipherText) {
        try {
            byte[] encrypted = Util.hexToByte(cipherText);
            cipherText=Base64.encodeBase64String(encrypted);;
            //cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            //byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64.decodeBase64(cipherText));
            /*String text = new String(decrypted, "UTF-8");
            return text.substring(0,text.length()-1);*/
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String plainText = "I Love You Every Day";
        String s = Util.byteToHex(plainText.getBytes());
        System.out.println("原文" + s);
        SM4UtilsBak sm4 = new SM4UtilsBak();
        //sm4.secretKey = "JeF8U9wHFOMfs2Y8";
        sm4.secretKey = "64EC7C763AB7BF64E2D75FF83A319918";
        sm4.hexString = true;

        System.out.println("ECB模式加密");
        String cipherText = sm4.encryptData_ECB(plainText,true);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        String plainText2 = sm4.decryptData_ECB(cipherText,true);
        System.out.println("明文: " + plainText2);
        System.out.println("");

        System.out.println("CBC模式加密");
        sm4.iv = "31313131313131313131313131313131";
        String cipherText2 = sm4.encryptData_CBC(plainText);
        System.out.println("加密密文: " + cipherText2);
        System.out.println("");

        String plainText3 = sm4.decryptData_CBC(cipherText2);
        System.out.println("解密明文: " + plainText3);

    }
}
