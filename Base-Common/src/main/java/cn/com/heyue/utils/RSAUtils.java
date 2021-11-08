package cn.com.heyue.utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//import static com.chinaums.ysmktaln.mktaln4sp.Service.byteArr2HexString;
//import static com.chinaums.ysmktaln.mktaln4sp.Service.hexString2ByteArr;


public class RSAUtils {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    public static final String KEY_ALGORTHM = "RSA";//
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";//公钥
    public static final String PRIVATE_KEY = "RSAPrivateKey";//私钥

    private static final Logger logger = Logger.getLogger("RSAUtils");

    private static final int ZERO = 0;

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        logger.info(getPublicKey(keyMap));
        logger.info(getPrivateKey(keyMap));
        return keyMap;
    }

    /**
     * 取得公钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得私钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 用私钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        //解密密钥
        byte[] keyBytes = decryptBASE64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密 * @param data 	加密数据
     *
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 用公钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        //对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 用公钥解密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       //加密数据
     * @param privateKey //私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造PKCS8EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));

    }

    /**
     * 用私钥对信息生成数字签名 rsa2
     *
     * @param data       //加密数据
     * @param privateKey //私钥
     * @return
     * @throws Exception
     */
    public static String signWithRsa2(byte[] data, String privateKey) throws Exception {
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        signature.initSign(privateKey2);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名 rsa2
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verifyWithRsa2(byte[] data, String publicKey, String sign) throws Exception {
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造PKCS8EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));

    }


//    /**
//     * 用私钥对信息生成数字签名
//     *
//     * @param data       //加密数据
//     * @param privateKey //私钥
//     * @return
//     * @throws Exception
//     */
//    public static String umSign(String data, String privateKey) throws Exception {
//        //解密私钥
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(hexString2ByteArr(privateKey));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey priKey = keyFactory.generatePrivate(keySpec);
//        Signature si = Signature.getInstance("SHA1WithRSA");
//        si.initSign(priKey);
//        si.update(data.getBytes("UTF-8"));
//        byte[] dataSign = si.sign();
//        return byteArr2HexString(dataSign);
//    }

//    /**
//     * 校验数字签名
//     *
//     * @param data      加密数据
//     * @param publicKey 公钥
//     * @param sign      数字签名
//     * @return
//     * @throws Exception
//     */
//    public static boolean umVerify(String data, String publicKey, String sign) throws Exception {
//        //解密公钥
//        Signature verf = Signature.getInstance("SHA1WithRSA");
//        KeyFactory keyFac = KeyFactory.getInstance("RSA");
//        PublicKey puk = keyFac.generatePublic(new X509EncodedKeySpec(hexString2ByteArr(publicKey)));
//        verf.initVerify(puk);
//        verf.update(data.getBytes("UTF-8"));
//        return verf.verify(hexString2ByteArr(sign));
//    }

    public static PublicKey getPublicKey(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        //构造PKCS8EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicKey;
    }

    public static boolean verify(byte[] data, PublicKey publicKey, String sign) throws Exception {
        //解密公钥
        byte[] keyBytes = publicKey.getEncoded();
        //构造PKCS8EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(HexStringUtils.str2HexByte(sign));

    }

    public static boolean verifyCmpay(byte[] data, String publicKey, String sign) throws Exception {
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造PKCS8EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(hexToByte(sign.getBytes()));

    }

    private static byte[] hexToByte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] b2 = new byte[b.length / 2];

            for (int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }

            b = (byte[]) null;
            return b2;
        }
    }

    public static void main(String[] args) throws Exception {
//        Map<String, Object> map = initKey();
//        System.out.println(getPublicKey(map));
//        System.out.println(getPrivateKey(map));
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCS5H2SrRUF203RIePeNdQD2V7A5/AZ4VeGFgFZ9mQMyH+7KPMy2E2H/c" +
//                "39ZfvnsN+XapnpFvE/Rg2NfKwxCkKnhLwDgsg16LN1GtDWMfCRaDL+MAEgl0T8vFqT9aSUdEzo074p17DY7DF4QKfwJ4aGg6pJQKFc" +
//                "O6bEVAAhUo2VfQIDAQAB";
//
//        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJLkfZKtFQXbTdEh49411APZXsDn" +
//                "8BnhV4YWAVn2ZAzIf7so8zLYTYf9zf1l++ew35dqmekW8T9GDY18rDEKQqeEvAOCyDXos3Ua0NYx" +
//                "8JFoMv4wASCXRPy8WpP1pJR0TOjTvinXsNjsMXhAp/AnhoaDqklAoVw7psRUACFSjZV9AgMBAAEC" +
//                "gYBzgkeYopBIhbZAgOTd5Pgueqz/BqQQxdl8cCtp4c21G6kNtNrhHaSCDrv/ykV3YOfB+qrC1y6n" +
//                "8d9QZjxFZ+sW982zEtaZWeLnFGxUie0aFPzyKvxyFsNzeI2cd6+oItC7zyxM+Bya75D2oXKijdKu" +
//                "aKSMk4TDJgtHqCjIVXZLpQJBAOiEczCFFRiUKo6SvL3CPqtZKh1yx45iSooE/21chnyceTK/ahf7" +
//                "YeBXUXhFzuM59JDEFnIJ0u1Qwn609LAL1McCQQChukdzUAcfpo5sDl3vOkeo5C9TZMycyP46p4nx" +
//                "9FVO8uxDjQwY5b0qqTxODCR+TvGEU/B4f6Ei1Bf7J1sy6zebAkEAtAtTkr+KPVUFuw6evaU6l73c" +
//                "YZ8uLO+pXkROcSVTMgyLwKL9iQJroKacfDA56jNHUA22f6lhvcdqQ2jli6gtowJAcM1r7qsf/NyA" +
//                "NDAlJqoAt3VI6SLCcIzkfebDmYZxRhq73jXv/SGouqvFyy4++fZ4EHM3o+RpcNB8VYpS2wYjBwJA" +
//                "Rtxv8fxPs9ZuHH3nwyuqBeHv36VRXxDbS9QZQ3bcPvLQX7s8m3pAKalA6WfnEZ+Rz8DZpB0ooVnM" +
//                "I1ULOhoWcQ==";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code",1);
//        jsonObject.put("msg","测试");
//        byte[] b = encryptByPublicKey(jsonObject.toJSONString().getBytes(),publicKey);
//        System.out.println(new String(b));
//        byte[] b1 = decryptByPrivateKey(b,privateKey);
//        System.out.println(new String(b1));
        ;
//        System.out.println(DigestUtils.sha1Hex("ssssssrrrrrrr"));
//        System.out.println(getFormattedText(RSAUtils.encryptSHA("ssssssrrrrrrr".getBytes())));
//        String s = "{\"agentCode\":\"88888888\",\"agentUserId\":\"00000000000000000066\",\"mobileNO\":\"13400000001\",\"realName\":\"刘三\",\"idCard\":\"420207188907813432\"}";

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3h3Xt00Pp1UrY2L1SdL/s6gl4qsM6NxoMKyDcapfgtqVvCBomUGavxtsK1a+ZYiOuvn9qDWz9IP8Bm5ejLM1iypcOKM60bd7mvObHIKr25ZM+0C7feKx8+OtOVzd/wv1fDKftyGSc5I5d6crhiRnO72XDf8OJEMtSezRrinkzpQIDAQAB";

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMMdz48/wu5OUEvXMJf6WH2Ry6uAcchcWdX+0Mrvz0Nom6HBm9DWW7ll1yf5Qts1QqQHpvm0I/SxnGWmgJUozIoe116H9gZfAEPywxz5dxGzR6Hf+3KvgSgG7duibMdYMaYeMf8Xkai0VOsZwHrFDABE7LiZPlKR62VDdzu25wj5AgMBAAECgYBKcdxYrp5EaHLwjNlIk0ciGfeYpvhC1yGbqY6mb1soQAhpbkJyKudyVG4EHXGpy6dyiEzoJxg063NdwWp7/sYTHk/N13UzGTudIKuNacnJk0WKu4owQticC71ZIqUjSZgN0CiEKQ6YfoGOFTzeMqzVYQjImPzGdLK74y3YYlmigQJBAObzhzYlWjOypx3klmrPTu2KXPg3ATTEB8kN/isY8bYuikVdd2yUd0AvaC7PPwEEjGmsSrEeXw1tsVfZ8VkBaikCQQDYR0+8VzGLdgIFQc/6+IY5fQlEt/Hc7qsi7JT4o+f+BGJlAT7+OeDMThavKdWq1UvZDyCKdtYRfxQ1jj7D4yJRAkBrG6InkGcm9sHecTb5Ti+ypqq7Svc6O3fI3L51ylm/PhJOXSyXpLsxf0r3+pGjrTJZh9gUEJvQpIDM13zA5JERAkBI2zTsED9baIRjuvjR5Xhp00oVARYTw76YxDOm0qgq9NUki1fqEhs9F60ikqgspS+oziS7IC8as8FeDS3tlQ0RAkA5OdDvhQRQPI75ULyHazTEm4Rak8TKmKl64pmnwcw4GS9fKWs7jRAuem1OtwA8HAqjaDeXC8Cd6fDfq7z5bZnE";

        String s = RSAUtils.signWithRsa2("这是一个测试".getBytes(StandardCharsets.UTF_8), privateKey).replaceAll(System.getProperty("line.separator"), "");
        System.out.println(s);
    }

    /**
     * 用私钥解密RSA2 * @param data 	加密数据
     *
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static byte[] verifyWithRsa2ByPrivateKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //return cipher.doFinal(data);
        return encryptAndDecrypt(data, cipher, 256);
    }

    /**
     * 加密解密分段处理公共方法
     */
    private static byte[] encryptAndDecrypt(byte[] data, Cipher cipher, int maxSize) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = ZERO;
        byte[] cache;
        int i = ZERO;
        // 对数据分段加密
        while (inputLen - offSet > ZERO) {
            if (inputLen - offSet > maxSize) {
                cache = cipher.doFinal(data, offSet, maxSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, ZERO, cache.length);
            i++;
            offSet = i * maxSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
}
