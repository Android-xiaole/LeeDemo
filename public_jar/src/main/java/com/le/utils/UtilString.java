/*
 * Copyright (c) 2014, KJFrameForAndroid 张涛 (kymjs123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.le.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public class UtilString {
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static Pattern phone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	private final static Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
	
	/**
	 *  判断  是数字,英文字母和中文
	 * @param s
	 * @return
	 * @return: boolean
	 */
	public static boolean isValidTagAndAlias(String s) {
		Matcher m = p.matcher(s);
		return m.matches();
	}
	/**
	 * 判断给定字符串是否空白串
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 */
	public static boolean isEmpty(CharSequence input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 */
	public static boolean isEmail(CharSequence email) {
		if (isEmpty(email))
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断是不是一个合法的手机号码
	 */
	public static boolean isPhone(CharSequence phoneNum) {
		if (isEmpty(phoneNum))
			return false;
		return phone.matcher(phoneNum).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * String转long
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * String转double
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static double toDouble(String obj) {
		try {
			return Double.parseDouble(obj);
		} catch (Exception e) {
		}
		return 0D;
	}

	/**
	 * 字符串转布尔
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断一个字符串是不是数字
	 */
	public static boolean isNumber(CharSequence str) {
		try {
			Integer.parseInt(str.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 是否为空或空内容
	 */
	public static boolean isNull(String str){
		if (str == null){
			return true;
		}
		str = str.trim();
		if (str.equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否在Unicode编码里
	 * @param chineseStr
	 * @return
	 * @return: boolean
	 */
	public static boolean isChineseCharacter(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			// 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
			if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD') || ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	/**
	 *  判断是否含有"�"这个特殊字符
	 * @param str
	 * @return
	 * @return: boolean
	 */
	public static boolean isSpecialCharacter(String str) {
		// 是"�"这个特殊字符的乱码情况
		if (str.contains("ï¿½")) {
			return true;
		}
		return false;
	}
	/**
	 * 得到最后一个what向后的内容不含what
	 */
	public static String getLast(String str, String what){
		if (str==null) {
			return null;
		}
		int lastIndexOf = str.lastIndexOf(what);
		int start = lastIndexOf+what.length();
		if (start<str.length()) {
			return str.substring(start, str.length());
		}
		return null;
	}
	/**
	 * 得到第一个start 到第一个end 的内容</p>
	 * start为空时为从最前面
	 */
	public static String getFirst(String str, String start, String end){
		if (str == null||end ==null) {
			return null;
		}
		int indexStart = 0;
		if (start!=null) {
			indexStart = str.indexOf(start)+start.length();
		}
		int indexEnd = str.indexOf(end);
		if (indexStart<indexEnd) {
			return str.substring(indexStart, indexEnd);
		}
		return null;
	}
	
	
	
	
	
}
