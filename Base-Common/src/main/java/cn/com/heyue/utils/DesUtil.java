package cn.com.heyue.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 
 * @ClassName: DesUtil
 * @Description:TODO Des算法工具类
 * @author: yiwenhao
 * @date: 2018年11月7日 上午8:35:01
 * 
 * @Copyright: 2018 www.xiongdi.cn. All rights reserved.
 *             注意：本内容仅限于深圳市雄帝科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class DesUtil {

	public static String padStr = "00000000";

	/**
	 * 
	 * @Title: XD_Str3Des_Mac @Description: TODO 3DES 计算MAC @param: @param
	 *         data @param: @param key_16， 32位密钥，格式（0-9,A-F） @param: @param
	 *         ramdom_4， 8位随机数，格式（0-9，A-F） @param: @return @return: String
	 *         成功：返回8位MAC , 失败：返回"" @throws
	 */
	public static String XD_Str3Des_Mac(String data, String key_16, String ramdom_4) {
		String mac = null;
		byte[] dataBuff = null;
		byte[] keyBuff = null;
		byte[] ramBuff = null;

		// 1、随机数获取4字节秘钥初始值，后补4字节0x00。
		int ramdomLen = ramdom_4.length();
		if (ramdomLen != 8) {
			return mac;
		}
		ramdom_4 += "00000000";
		ramdomLen += 8;
		ramBuff = new byte[ramdomLen / 2];
		HexStringToMulBytes(ramdom_4, ramBuff, 0);

		// 2、秘钥长度必须是16字节。
		int keyLen = key_16.length();
		if (keyLen != 32) {
			return mac;
		}
		keyBuff = new byte[keyLen / 2];
		HexStringToMulBytes(key_16, keyBuff, 0);

		// 3、数据域按照8字节分块，（1）最后数据是8字节： 补8000000000000000(8字节)；
		// （2）最后数据不足8字节：补80 + n个00，凑齐8字节。
		int dataLen = data.length();
		int leftLen = 0;

		if ((leftLen = dataLen % 8) == 0) {
			data += padStr;
			dataLen += 8;
		} else {
			data += padStr.substring(0, 8 - leftLen);
			dataLen += (8 - leftLen);
		}

		dataBuff = new byte[dataLen + 1];
		try {
			dataBuff = data.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return mac;
		}

		// 4、3DES加密算MAC，第一块 ：用 随机数异或得数据tData，与维护秘钥的lkey进行Des加密，得秘钥tKey。
		// 第二块：用tKey 异或得数据tData，与维护秘钥的lkey进行Des加密，得秘钥tKey。
		// 第三块：用tKey
		// 异或得数据tData，与维护秘钥的lkey进行Des加密，得秘钥tKey。...以此例推,去最后获取tKey进行3DES计算。
		int blockCount = dataLen / 8;
		byte[] tBlock = new byte[8];
		byte[] tData = new byte[8];
		byte[] tKey = new byte[8];

		for (int i = 0; i < blockCount; i++) {
			System.arraycopy(dataBuff, (i * 8), tBlock, 0, 8);
			// 异或
			if (i == 0) {// 第一块
				tData = xor8(tBlock, ramBuff);
			} else {
				tData = xor8(tBlock, tKey);
			}

			// Des加密
			tKey = DESencode(keyBuff, 0, tData, 0, 8);
		}

		// 5、3DES加密、解密、加密
		tKey = DESdecode(keyBuff, 8, tKey, 0, 8);
		tKey = DESencode(keyBuff, 0, tKey, 0, 8);

		mac = byteToString(tKey, 0, 8).substring(0, 8).toUpperCase();

		return mac;
	}

	/**
	 * 
	 * @Title: XD_Str3Des_Encode @Description: TODO 3DES加密算法 @param: @param
	 *         data @param: @param key_16，
	 *         32位密钥，格式（0-9,A-F） @param: @return @return: String 成功：返回加密后数据 ,
	 *         失败：返回"" @throws
	 */
	public static String XD_Str3Des_Encode(String data, String key_16) {
		String secretData = null;
		byte[] dataBuff = null;
		byte[] secretBuff = null;
		byte[] keyBuff = null;

		// 1、秘钥长度必须是16字节。
		int keyLen = key_16.length();
		if (keyLen != 32) {
			return secretData;
		}
		keyBuff = new byte[keyLen / 2];
		HexStringToMulBytes(key_16, keyBuff, 0);

		// 加密字段中增加数据长度，方便解密
		int dataLen = getWordCount(data);// data.length();
		byte[] dataLenTmp = new byte[4];
		integerToLsbByte(dataLenTmp, 0, dataLen);
		String strDataLen = byteToString(dataLenTmp, 0, 4).toUpperCase();
		data = strDataLen + data;

		// 2、数据域按照8字节分块，（1）最后数据是8字节： 补8000000000000000(8字节)；
		// （2）最后数据不足8字节：补80 + n个00，凑齐8字节。
		dataLen += strDataLen.length();
		int leftLen = 0;
		if ((leftLen = dataLen % 8) != 0) {
			data += padStr.substring(0, 8 - leftLen);
			dataLen += (8 - leftLen);
		}

		dataBuff = new byte[dataLen];
		secretBuff = new byte[dataLen];
		try {
			dataBuff = data.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return secretData;
		}

		// 3、3DES运算
		int blockCount = dataLen / 8;
		byte[] tKey = new byte[8];
		for (int i = 0; i < blockCount; i++) {
			// Des加密
			tKey = DESencode(keyBuff, 0, dataBuff, i * 8, 8);
			tKey = DESdecode(keyBuff, 8, tKey, 0, 8);
			tKey = DESencode(keyBuff, 0, tKey, 0, 8);

			System.arraycopy(tKey, 0, secretBuff, i * 8, 8);

		}
		secretData = byteToString(secretBuff, 0, dataLen).toUpperCase();
		return secretData;
	}

	/**
	 * 
	 * @Title: XD_Str3Des_Decode @Description: TODO 3DES解密算法 @param: @param data
	 *         必须为偶数位，格式（0-9，A-F)，传加密后的数据 @param: @param key_16，
	 *         32位密钥，格式（0-9,A-F） @param: @return @return: String 成功：返回加密后数据 ,
	 *         失败：返回"" @throws
	 */
	public static String XD_Str3Des_Decode(String data, String key_16) {
		String secretData = null;
		byte[] dataBuff = null;
		byte[] secretBuff = null;
		byte[] keyBuff = null;

		// 1、秘钥长度必须是16字节。
		int keyLen = key_16.length();
		if (keyLen != 32) {
			return secretData;
		}

		keyBuff = new byte[keyLen / 2];
		HexStringToMulBytes(key_16, keyBuff, 0);

		// 2、数据域按照8字节分块，（1）最后数据是8字节： 补8000000000000000(8字节)；
		// （2）最后数据不足8字节：补80 + n个00，凑齐8字节。
		int dataLen = data.length();
		if ((dataLen % 8) != 0) {
			return secretData;
		}

		dataBuff = new byte[dataLen];
		secretBuff = new byte[dataLen];
		HexStringToMulBytes(data, dataBuff, 0);

		// 3、3DES运算
		int blockCount = dataLen / 8;
		byte[] tKey = new byte[8];
		for (int i = 0; i < blockCount; i++) {
			// Des解密
			tKey = DESdecode(keyBuff, 0, dataBuff, i * 8, 8);
			tKey = DESencode(keyBuff, 8, tKey, 0, 8);
			tKey = DESdecode(keyBuff, 0, tKey, 0, 8);

			System.arraycopy(tKey, 0, secretBuff, i * 8, 8);

		}

		String strDataLen = "";
		try {
			strDataLen = new String(secretBuff, 0, 8, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return strDataLen;
		}

		byte[] dataLenTmp = new byte[4];
		HexStringToMulBytes(strDataLen, dataLenTmp, 0);

		int tmpDataLen = lsbByteToInteger(dataLenTmp, 0);

		dataLenTmp = new byte[tmpDataLen];
		memcopy(secretBuff, 8, dataLenTmp, 0, tmpDataLen);

		try {
			secretData = new String(dataLenTmp, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secretData;
	}

	private static byte[] xor8(byte[] block, byte[] b) {
		for (int i = 0; i < 8; i++) {
			block[i] ^= b[i];
		}

		return block;
	}

	private static byte[] DESencode(byte[] keyData, int koff, byte[] data, int off, int len) {
		try {
			SecretKey key = getSecretKey(keyData, koff);
			return DESencode(key, data, off, len);
		} catch (Exception e) {
			return null;
		}
	}

	private static byte[] DESdecode(byte[] keyData, int koff, byte[] data, int off, int len) {
		try {
			SecretKey key = getSecretKey(keyData, koff);
			return DESdecode(key, data, off, len);
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] memcopy(byte[] SrcArr, int SrcOffset, byte[] DesArr, int DesOffset, int len) {
		for (int i = 0; i < len; i++) {
			DesArr[DesOffset + i] = SrcArr[SrcOffset + i];
		}
		return DesArr;
	}

	private static int lsbByteToInteger(byte[] data, int offset) {
		return (((int) data[offset + 3] & 0xFF) << 24) + (((int) data[offset + 2] & 0xFF) << 16)
				+ (((int) data[offset + 1] & 0xFF) << 8) + ((int) data[offset + 0] & 0xFF);
	}

	private static byte[] DESencode(Key key, byte[] data, int off, int len) throws Exception {
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, key);

		// 正式执行加密操作
		byte[] encryptedData = cipher.doFinal(data, off, len);

		return encryptedData;
	}

	private static byte[] DESdecode(Key key, byte[] data, int off, int len) throws Exception {
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, key);

		// 正式执行解密操作
		byte[] decryptedData = cipher.doFinal(data, off, len);

		return decryptedData;
	}

	private static SecretKey getSecretKey(byte[] keyData, int off) throws Exception {

		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(keyData, off);

		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);

		return key;
	}

	private static void HexStringToMulBytes(String str, byte[] Arr, int offset) {
		int len = str.length() & 0xFFFFFFFE;

		byte[] pByte = str.getBytes();

		for (int i = 0; i < len; i += 2) {
			Arr[offset++] = (byte) ((ascByteToInt(pByte[i]) << 4) + ascByteToInt(pByte[i + 1]));
		}
	}

	private static int ascByteToInt(byte b) {
		int dat = b & 0x0F;
		if (b > '9') {
			dat += 9;
		}
		return dat;
	}

	private static String byteToString(byte[] src, int offset, int len) {
		String str = "";

		while (len-- != 0) {
			String tmp = Integer.toHexString(src[offset++] & 0xFF);
			if (tmp.length() == 1)
				tmp = "0" + tmp;
			str += tmp;
		}

		return str;
	}

	private static void integerToLsbByte(byte[] dst, int offset, int data) {
		dst[offset++] = (byte) (data);
		dst[offset++] = (byte) (data >> 8);
		dst[offset++] = (byte) (data >> 16);
		dst[offset++] = (byte) (data >> 24);
	}

	private static int getWordCount(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;

		}
		return length;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String random = "8A9375BD";
		// String privateKey = "E0CFF97CE779AECA3D749A15DC24D2AB";
		// String data =
		// "04D6850034000000000000000100000000000000000000000000000000000000000000000000000000000000000000000000000000";
		// System.out.println(XD_Str3Des_Mac(data, privateKey, random));

		String privateKey = "00000000000000000000000000000000";
		// String data = "{\"password\":\"0000000000000000\",\"joinUpCode\":
		// \"100001\",\"userId\": \"1\"}";
		// String data =
		// "{\"joinUpCode\":\"100001\",\"distributorId\":\"1001\",\"organizationCode\":\"11111111\",\"idCard\":\"430124199612115173\",\"userName\":\"银果\",\"mobile\":\"18711045908\",\"qrCodeType\":\"01\"}";
		// System.out.println(XD_Str3Des_Encode(data, privateKey));

		String data = "959215E45AE55DEBD752619674DDBAB7C9189090389FE723E2F060B20C66658BF4F92876D4C689AAA24244C94F60C132626CBCB76EEE6A2529C489F9E0137AD01B4BC75A46E9D636D87071D4343D902FC99FB76486D19EDB865933202D397FAE7E43C31D6898C94FF032F9FD419997C6F95669C272FF2A7814A2198F5AD940265398F1A75E7A6C2D230721A08057426357B25B572BEE55B2DC92AD994C3CEAF6";
		System.out.println(XD_Str3Des_Encode(data, privateKey));
		// System.out.println(XD_Str3Des_Decode(data, privateKey));

	}

}
