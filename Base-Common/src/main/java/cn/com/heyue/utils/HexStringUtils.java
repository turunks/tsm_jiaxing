package cn.com.heyue.utils;


/**
 * 16进制字符串工具类
 *
 * @author 付祖远
 * @version V1.0
 */
public class HexStringUtils {
    /**
     * 整数转16进制字符串，不足指定长度补0
     *
     * @param i      要转的整数
     * @param length 字符串长度
     * @return 16进制字符串
     */
    public static String intToHexString(int i, int length) {
        String result = Integer.toHexString(i);
        if (length - result.length() > 0) {
            return String.format("%0" + (length - result.length()) + "d", 0) + result;
        } else {
            return result;
        }
    }

    /**
     * 二进制字符串转16进制字符串
     *
     * @param str
     * @return
     */
    public static String binToHexString(String str) {
        return Long.toHexString(Long.parseLong(str, 2));
    }

    /**
     * 字符串转16进制字符串
     *
     * @param str 要转的字符串
     * @return
     */
    public static String strToHexString(String str) {
        byte[] b = str2HexByte(str);
        return bytesToHexString(b);
    }

    /**
     * 字节转16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 字符串每两位一组转化为数组
     *
     * @param data
     * @return
     */
    public static byte[] str2HexByte(String data) {
        if (1 == data.length() % 2)
            return null;
        else {
            byte[] li = new byte[data.length() / 2];
            for (int i = 0; i < data.length(); i += 2) {
                int cp1 = data.codePointAt(i);
                int cp2 = data.codePointAt(i + 1);
                li[i / 2] = (byte) (asc2Hex(cp1) << 4 | asc2Hex(cp2));
            }
            return li;
        }
    }

    /**
     * 字符asc码数值转为byte数值
     *
     * @param data
     * @return
     */
    public static int asc2Hex(int data) {
        int li;
        if (data >= 0X30 && data <= 0X39) {
            //0-9
            li = data - 0X30;
        } else if (data >= 0X41 && data <= 0X5A) {
            //A-F
            li = data - 0X37;
        } else if (data >= 0X61 && data <= 0X7A) {
            //a-f
            li = data - 0X57;
        } else {
            li = data;
        }
        return li;
    }
    
    /**
	 * 16进制字符串转换为字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s) {
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
		try {
			s = new String(baKeyword, "iso8859-1");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString(((int) ch & 0x000000ff) | 0xffffff00).substring(6).toUpperCase());
        }
        return hex.toString();
    }

    public static void main(String[] args) {
        System.out.println(HexStringUtils.intToHexString(1234567890, 20));
//        System.out.println(HexStringUtils.intToHexString((int) DateUtils.getNowTimeStamp(), 8));
        System.out.println(Integer.parseInt("3104970710990000004",16));
//        System.out.println(HexStringUtils.strToHexString("117E00"));
//        System.out.println(new String(HexStringUtils.str2HexByte("117E00")));
        System.out.println(HexStringUtils.bytesToHexString(HexStringUtils.str2HexByte("61A4E53BB5349173DA6E5DDC4C08C851")));

    }
}
