package org.jeecg.utils;

import org.springframework.util.StringUtils;

public class HexConvertUtil {

	/**
	 * ascii编码
	 *
	 * @param str
	 * @return
	 */
	public static String convertStringToHex(String str) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();
	}

	/**
	 * ascii编码，可设空客
	 *
	 * @param str
	 * @param isSpace
	 * @return
	 */
	public static String convertStringToHex(String str, Boolean isSpace) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
			if (isSpace)
				hex.append(" ");
		}

		return hex.toString();
	}

	public static String convertHexToString(String hex) {
		hex = hex.replaceAll(" ", "");
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();

		for (int i = 0; i < hex.length() - 1; i += 2) {

			String s = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(s, 16);
			sb.append((char) decimal);
			sb2.append(decimal);
		}

		return sb.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (StringUtils.isEmpty(hexString)) {
			return null;
		}
		hexString = hexString.replace(" ", "");
		// toUpperCase将字符串中的所有字符转换为大写
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		// toCharArray将此字符串转换为一个新的字符数组。
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 返回匹配字符
	 *
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将字节数组转换为short类型，即统计字符串长度
	 *
	 * @param b
	 * @return
	 */
	public static short bytes2Short2(byte[] b) {
		short i = (short) (((b[1] & 0xff) << 8) | b[0] & 0xff);
		return i;
	}

	/**
	 * 将字节数组转换为16进制字符串
	 *
	 * @param bytes
	 * @return
	 */
	public static String BinaryToHexString(byte[] bytes) {
		String hexStr = "0123456789ABCDEF";
		String result = "";
		String hex = "";
		for (byte b : bytes) {
			hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
			hex += String.valueOf(hexStr.charAt(b & 0x0F));
			result += hex + " ";
		}
		return result;
	}


	public static void main(String[] args) {

//		System.out.println("======ASCII码转换为16进制======");
//		String str = "*00007VERSION\\n1$";
//		System.out.println("字符串: " + str);
//		String hex = convertStringToHex(str);
//		System.out.println("====转换为16进制=====" + hex);
//
//		System.out.println("======16进制转换为ASCII======");
//		System.out.println("Hex : " + hex);
//		System.out.println("ASCII : " + convertHexToString(hex));
//
//		byte[] bytes = hexStringToBytes(hex);

		byte[] b = hexStringToBytes("02 04 00 00 00 02 71 f8");
		System.out.println(b);

		String a = HexConvertUtil.BinaryToHexString(b);
		System.out.println(a);
	}
}
