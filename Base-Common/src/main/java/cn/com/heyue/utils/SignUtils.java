package cn.com.heyue.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 签名工具类
 *
 * @author 付祖远
 * @version V1.0
 */
public class SignUtils<T> {
    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 得到md5签名串
     *
     * @param obj
     * @param secretKey
     * @return
     * @throws Exception
     */
    private String getMd5Info(T obj, String secretKey, String signMethod, String fieldNames) throws Exception {
        List<Field> fieldList = ReflectionUtils.getDeclaredField(obj);
        Map<String, Object> map = new TreeMap<String, Object>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            field.setAccessible(true);
            String fieldName = field.getName();
            String filedType = field.getType().toString();
            // TODO 不签名data对象
            if (org.apache.commons.lang3.StringUtils.equals(fieldName, "data")) {
				continue;
            }
            if (StringUtils.endsWith(filedType, "String") || StringUtils.endsWith(filedType, "Integer")
                    || StringUtils.endsWith(filedType, "Long") || StringUtils.endsWith(filedType, "Float")) {
                if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
                    map.put(fieldName, ReflectionUtils.getFieldValue(obj, fieldName));
                }
            } else if (StringUtils.endsWith(filedType, "Date")) {
                if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
                    map.put(fieldName, DateUtils.format((Date) ReflectionUtils.getFieldValue(obj, fieldName),
                            "yyyy-MM-dd HH:mm:ss"));
                }
            } else {
                // TODO 不签名data对象
//                if(ReflectionUtils.getFieldValue(obj, fieldName)!=null){
//                    map.put(fieldName, new ObjectMapper().writeValueAsString(ReflectionUtils.getFieldValue(obj, fieldName)));
//                }
            }
        }
        StringBuffer buf = new StringBuffer();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = (map.get(key) != null && StringUtils.isNotBlank(map.get(key).toString()))
                    ? map.get(key).toString()
                    : null;
            if (value != null && !StringUtils.equals(key, fieldNames)) {
//                buf.append(cn.com.heyue.utils.StringUtils.toUpperCaseFirstOne(key)).append("=").append(map.get(key).toString()).append("&");
                buf.append(key).append("=").append(map.get(key).toString()).append("&");
            }
        }
        String signSrc = null;
        if (StringUtils.isBlank(signMethod)) {
            signSrc = buf.substring(0, buf.length() - 1) + secretKey;
        } else if (StringUtils.equals(signMethod, "ramCity")) {
            signSrc = buf.append("key=" + secretKey).toString();
        }

        String hmac = Md5Encrypt.md5(signSrc, "utf-8").toUpperCase();
        log.info("签名原始串：{}，签名加密串：{}", signSrc, hmac);
        return hmac;
    }

    /**
     * md5签名，对象中的List，HashMap等集合对象不签名
     *
     * @param obj       加密的对象
     * @param secretKey 加密串
     * @throws Exception
     */
    public void md5Sign(T obj, String secretKey, String fieldName, String signMethod) throws Exception {
        String hmac = this.getMd5Info(obj, secretKey, signMethod, fieldName);
        ReflectionUtils.setFieldValue(obj, fieldName, hmac);
    }

    /**
     * md5验签，对象中的List，HashMap等集合对象不签名
     *
     * @param obj       对象
     * @param secretKey 密钥
     * @throws Exception
     */
    public boolean md5Verify(T obj, String secretKey, String signMethod, String fieldName) throws Exception {
        String hmacSrc = ReflectionUtils.getFieldValue(obj, fieldName).toString();
        // 生成签名串
        String hmac = this.getMd5Info(obj, secretKey, signMethod, fieldName);
        //log.info("【生成的签名串】{}", hmac);
        return hmacSrc.equalsIgnoreCase(hmac);
    }

    /**
     * 签名，对象中的List，HashMap等集合对象不签名
     *
     * @param obj       加密的对象
     * @param secretKey 加密串
     * @throws Exception
     */
    public void sign(String signType, T obj, String secretKey, String fieldName) throws Exception {
        String sign = this.getSignInfo(signType, obj, secretKey, fieldName);
        ReflectionUtils.setFieldValue(obj, fieldName, sign);
    }

    /**
     * 得到签名串
     *
     * @param obj
     * @param secretKey
     * @return
     * @throws Exception
     */
    private String getSignInfo(String signType, T obj, String secretKey, String fieldNames) throws Exception {
        List<Field> fieldList = ReflectionUtils.getDeclaredField(obj);
        List<String> fieldNameList = new ArrayList<String>();
        Map<String, Object> map = new TreeMap<String, Object>();
        for (Field field : fieldList) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String filedType = field.getType().toString();
            if (org.apache.commons.lang3.StringUtils.endsWith(filedType, "String") || org.apache.commons.lang3.StringUtils.endsWith(filedType, "Integer")
                    || org.apache.commons.lang3.StringUtils.endsWith(filedType, "Long") || org.apache.commons.lang3.StringUtils.endsWith(filedType, "Float")) {
                if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
                    fieldNameList.add(fieldName);
                    map.put(fieldName, ReflectionUtils.getFieldValue(obj, fieldName));
                }
            } else if (org.apache.commons.lang3.StringUtils.endsWith(filedType, "Date")) {
                if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
                    fieldNameList.add(fieldName);
                    map.put(fieldName, DateUtils.format((Date) ReflectionUtils.getFieldValue(obj, fieldName),
                            "yyyy-MM-dd HH:mm:ss"));
                }
            } else {
                if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
                    fieldNameList.add(fieldName);
                    map.put(fieldName, ReflectionUtils.getFieldValue(obj, fieldName));
                }
            }
        }
        StringBuilder buf = new StringBuilder();
        Collections.sort(fieldNameList);
        for (String fieldName : fieldNameList) {
            String value = (map.get(fieldName) != null && org.apache.commons.lang3.StringUtils.isNotBlank(map.get(fieldName).toString()))
                    ? map.get(fieldName).toString()
                    : null;
            if (value != null && !org.apache.commons.lang3.StringUtils.equals(fieldName, fieldNames)) {
                buf.append(fieldName).append("=").append(map.get(fieldName).toString()).append("&");
            }
        }
        String signSrc = null;
        String sign = null;
        if ("RSA".equals(signType)) {//RSA公钥解密
            String signStr = Objects.requireNonNull(ReflectionUtils.getFieldValue(obj, fieldNames)).toString();
            signSrc = buf.substring(0, buf.length() - 1);
            log.info("验签原始串：{}", signSrc);
            log.info("验签公钥：{}", secretKey);
            boolean flag = RSAUtils.verify(signSrc.getBytes(), secretKey, signStr);//RSA公钥解密
            sign = flag + "";
            //sign= RSAUtils.sign(signSrc.getBytes("utf-8"), secretKey).replaceAll(System.getProperty("line.separator"), "");
        } else if ("RSAENCODE".equals(signType)) {//RSA加密
            signSrc = buf.substring(0, buf.length() - 1);
            sign = RSAUtils.sign(signSrc.getBytes(StandardCharsets.UTF_8), secretKey).replaceAll(System.getProperty("line.separator"), "");
        } else if ("RSA2ENCODE".equals(signType)) {//RSA2加密
            signSrc = buf.substring(0, buf.length() - 1);
            sign = RSAUtils.signWithRsa2(signSrc.getBytes(StandardCharsets.UTF_8), secretKey).replaceAll(System.getProperty("line.separator"), "");
        } else {
            // MD5
            signSrc = buf.substring(0, buf.length() - 1) + secretKey;
            sign = DigestUtils.md5Hex(signSrc).toUpperCase();
        }
        log.info("签名原始串：{}，签名加密串：{}", signSrc, sign);
        return sign;
    }


    public static void main(String[] args) throws Exception {
        String secretKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK+mfcHYdxzVGiSxW4Oai2ngxp5vZBuPoLVD3XvOuZJovHyYcfYO5XxGjZFOLKDxmVr0BBo9r5tjEP6JQfEZsniulSP6nhIcHibHT55bKTrWUV1y86jTzKUaD+1ut/7kFwqj7XrBxWjzu+w207yMQmc3/Xw2HeCr2QVVOLu5Wx2LAgMBAAECgYBkqW9shddNSLsgyBuAAjNfTBfHeGLQgLFC8E+fwksLIsbSSLMWplbRr8U1vq1I1uxh+XHYb/2ReWal2JdXyrhCCaroRK7ydkCTzlp8Ld41JXGquifE2oesEcXV+P/3hTEwCSPU8g7/+zkFANeaeYsKzM5VLL/RR/J+c8FFT2bJIQJBAOBfNwQ5cB9TTKNvJgT7Ip4QRTj1A5hROfjKYpv7xcnepbdUkP4dn/AYnhlXufto9fWIxRbKFESbtarsCWkNh1kCQQDIaRaAspZ5Qukyiwv4s3qwFv9A46WOt49TzppGxfrudLjiC/76D+T48YMF2wgNQrVTkf9drKq4N3JUkXpEVVODAkABVO9ZqhGdTfxFeBJssyTodUZD6UhlLg4/CMx/CoaJBTZMmUbnCzv4y9ycX+XttRjIpVAuK3LlWVtBsXb41ai5AkEAsfY/eUEcxclY8vzuQIaAJ+YD/EcqOLVfnPgVn0snLKCRnIkyfjOnDDjKNGTvnXbjNbaroS822ibCa75TaGKsqQJBAKwSSc1Y9Qj2HAq2loI/rLnAQ2tO4hzXtisBK8afxoBWsHgAJf3Qcf7zG/GFZA4m8tHhMv38wOCToycwgFA8+dw=";
        String pubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvpn3B2Hcc1RoksVuDmotp4Maeb2Qbj6C1Q917zrmSaLx8mHH2DuV8Ro2RTiyg8Zla9AQaPa+bYxD+iUHxGbJ4rpUj+p4SHB4mx0+eWyk61lFdcvOo08ylGg/tbrf+5BcKo+16wcVo87vsNtO8jEJnN/18Nh3gq9kFVTi7uVsdiwIDAQAB";
        String s = "channelCode=C002&merchantId=3178032938579106&productNo=18672302063&requestId=C156410104965934898&timestamp=1564101050&userId=123456&version=1.0";
        String sign = RSAUtils.signWithRsa2(s.getBytes(StandardCharsets.UTF_8), secretKey);
        System.out.println(sign);

        boolean flag = RSAUtils.verifyWithRsa2(s.getBytes(StandardCharsets.UTF_8), pubkey, sign);
        if (flag) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }
}