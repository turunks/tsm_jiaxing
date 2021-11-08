package cn.com.heyue.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类
 *
 * @author 付祖远
 * @version V1.0
 */
public class StringUtils {
    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 首字母转小写
     *
     * @param s 要转换的串
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s 要转换的串
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 加密字符串
     *
     * @param str        原字符串
     * @param prefixLen  前多少位
     * @param suffixLen  后多少位
     * @param replaceStr 替换的串
     * @return
     */
    public static String encryString(String str, Integer prefixLen, Integer suffixLen, String replaceStr) {
        StringBuffer buf = new StringBuffer();
        char[] array = str.toCharArray();
        if (prefixLen == null) {
            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1) {
                    buf.append(replaceStr);
                }else{
                    buf.append(array[i]);
                }
            }
        }else{
            for (int i = 0; i < array.length; i++) {
                if (i <= prefixLen - 1) {
                    buf.append(array[i]);
                } else if (i >= array.length - suffixLen) {
                    buf.append(array[i]);
                } else {
                    buf.append(replaceStr);
                }
            }
        }
        return buf.toString();
    }

    public static byte[] toByteArray(String hexString) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(hexString))
            return null;
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1)
                return byteArray;
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }

    /**
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Long amount) {
        int flag = 0;
        String amString = amount.toString();
        if(amString.charAt(0)=='-'){
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if(amString.length()==1){
            result.append("0.0").append(amString);
        }else if(amString.length() == 2){
            result.append("0.").append(amString);
        }else{
            String intString = amString.substring(0,amString.length()-2);
            for(int i=1; i<=intString.length();i++){
                if( (i-1)%3 == 0 && i !=1){
                    result.append(",");
                }
                result.append(intString.substring(intString.length()-i,intString.length()-i+1));
            }
            result.reverse().append(".").append(amString.substring(amString.length()-2));
        }
        if(flag == 1){
            return "-"+result.toString();
        }else{
            return result.toString();
        }
    }

    public static void main(String[] args) {
//        System.out.println(StringUtils.encryString("张三李", null, 0, "*"));
//        System.out.println(StringUtils.encryString("4201071979050215", 2, 2, "*"));
//        org.json.JSONObject jsonObject = new org.json.JSONObject(new LinkedHashMap());
//        jsonObject.put("mobile","121");
//        jsonObject.put("IFck","1212");
////        System.out.println(jsonObject);
//
//        System.out.println(jsonObject);
    }
}