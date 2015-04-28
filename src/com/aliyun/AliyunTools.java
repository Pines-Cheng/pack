/**
 * 
 */
package com.aliyun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author spider
 * 
 */
public class AliyunTools {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @category 从输入流写入文件中
	 * @param filePath
	 * @param objectContent
	 * @throws IOException
	 */
	public static void writeFile(String filePath, InputStream objectContent)
			throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream fos = new FileOutputStream(file);
		writeFile(fos, objectContent);

	}

	/**
	 * @category 从输入流写入文件中
	 * @param fos
	 * @param objectContent
	 * @throws IOException
	 */
	public static void writeFile(OutputStream fos, InputStream objectContent)
			throws IOException {
		byte[] buf = new byte[1024];
		int readNum;

		while ((readNum = objectContent.read(buf)) != -1) {
			fos.write(buf, 0, readNum);
		}
		fos.close();
	}

}
