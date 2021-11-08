/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Sougugo License, Version 1.0 (the "License");
 */
package cn.com.heyue.utils;

import java.util.List;
import java.util.Random;

/**
 * @description:关于字符串操作的工具类
 * @author:Tony 2010/10/30
 * @version:1.0
 * @modify:
 */
public class PswdUtil {

	// 72个字符集
	private static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*', '~', '|' };

	/**
	 * 
	 * @param passLength
	 *            随机密码长度
	 * @param count
	 *            随机密码个数
	 * @return 随机密码数组
	 */
	public static String[] getRandomPswd(int type, int passLength, int count) {
		try {
			String[] passwords = new String[count];// 新建一个长度为指定需要密码个数的字符串数组
			Random random = new Random();
			if (type == 1) {
				for (int i = 0; i < count; i++) {// 外循环 从0开始为密码数组赋值
					// 也就是开始一个一个的生成密码
					StringBuilder password = new StringBuilder("");// 保存生成密码的变量
					for (int m = 1; m <= passLength; m++) {// 内循环 从1开始到密码长度
						// 正式开始生成密码
						password.append(chars[random.nextInt(10)]);// 为密码变量随机增加上面字符中的一个
					}
					passwords[i] = password.toString();// 将生成出来的密码赋值给密码数组
				}
			} else if (type == 2) {
				for (int i = 0; i < count; i++) {// 外循环 从0开始为密码数组赋值
					// 也就是开始一个一个的生成密码
					StringBuilder password = new StringBuilder("");// 保存生成密码的变量
					for (int m = 1; m <= passLength; m++) {// 内循环 从1开始到密码长度
						// 正式开始生成密码
						password.append(chars[random.nextInt(53) + 10]);// 为密码变量随机增加上面字符中的一个
					}
					passwords[i] = password.toString();// 将生成出来的密码赋值给密码数组
				}
			} else if (type == 3) {
				for (int i = 0; i < count; i++) {// 外循环 从0开始为密码数组赋值
					// 也就是开始一个一个的生成密码
					StringBuilder password = new StringBuilder("");// 保存生成密码的变量
					for (int m = 1; m <= passLength; m++) {// 内循环 从1开始到密码长度
						// 正式开始生成密码
						password.append(chars[random.nextInt(72)]);// 为密码变量随机增加上面字符中的一个
					}
					passwords[i] = password.toString();// 将生成出来的密码赋值给密码数组
				}
			} else {
				System.out.println("输入错误!");
			}
			return passwords;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查字符串是否在list中存在
	 * 
	 * @param checkStr
	 *            要检查的字符串
	 * @param list
	 *            字符串列表
	 * @return true：存在 false:不存在
	 */
	public static boolean checkStringByListExists(String checkStr, List<String> list) {
		try {
			for (String obj : list) {
				if (checkStr.indexOf(obj) != -1) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 得到字符串在list中存在的string
	 * 
	 * @param checkStr
	 *            要检查的字符串
	 * @param list
	 *            字符串列表
	 * @return 没查到值返回null
	 */
	public static String getStringByListExists(String checkStr, List<String> list) {
		try {
			for (String obj : list) {
				if (checkStr.indexOf(obj) != -1) {
					return obj;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPswd(int length) {
		try {
			Random random = new Random();
			String sRand = "";
			for (int i = 0; i < length; i++) {
				int r = random.nextInt(62);
				String rand;
				if (r < 10) {
					// 数字
					rand = String.valueOf(r);
				} else if (r > 9 && r < 36) {
					// 大写字母
					char temp = (char) (r + 55);
					rand = String.valueOf(temp);
				} else {
					// 小写字母
					char temp = (char) (r + 61);
					rand = String.valueOf(temp);
				}
				sRand += rand;
			}
			return sRand;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(PswdUtil.getRandomPswd(2, 12, 1)[0]);
//		System.out.println(PswdUtil.getPswd(6));
	}
}
