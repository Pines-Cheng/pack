/**
 * 
 */
package com.util;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * @author spider
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "太行 阿胶 挨打 拗口 执拗 扒皮";
		System.out.println(PinyinHelper.convertToPinyinString(str, ",",
				PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
		System.out.println(PinyinHelper.convertToPinyinString(str, ",",
				PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
		System.out.println(PinyinHelper.convertToPinyinString(str, ",",
				PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
		System.out.println(PinyinHelper.getShortPinyin(str)); // nhsj
	}

}
