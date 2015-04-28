package com.util;

/**
 * @author spider
 * @category utf-8转ISO-88591-1
 * 
 */
public class EncodingConvert {

	public static String native2Ascii(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char aChar = s.charAt(i);
			if ((aChar < 0x0020) || (aChar > 0x007e)) {
				sb.append('\\');
				sb.append('u');
				sb.append(toHex((aChar >> 12) & 0xF));
				sb.append(toHex((aChar >> 8) & 0xF));
				sb.append(toHex((aChar >> 4) & 0xF));
				sb.append(toHex(aChar & 0xF));
			} else {
				sb.append(aChar);
			}
		}
		return sb.toString();
	}

	/**
	 * @param nibble
	 * @return 字符
	 */
	private static char toHex(int nibble) {
		final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		return hexDigit[nibble & 0xF];
	}
}
