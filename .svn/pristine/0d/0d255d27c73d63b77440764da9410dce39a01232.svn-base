package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @category 文件操作的静态方法
 */
public class FileOperate {

	public static void main(String[] args) throws IOException {
		MyLog.LogInit("log.html");
		// File file1 = new File("e:/log.txt");
		// File file2 = new File("e:/copy.txt");
		// copyDir(file1, file2);
		// copyDir("e:/test", "e:/new");

		FileOperate.deletePreviousFiles("e:/config2.cfg", 1000);

	}

	File file;
	private String configFilePath;

	/**
	 * @category 构造函数
	 */
	public FileOperate(String congfigFilePath) {
		this.configFilePath = congfigFilePath;

		file = new File(configFilePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @category 递归删除目录下的所有文件
	 */
	public static void deleteFiles(String filePath) {
		// MyLog.logger.info("要删除的根文件/目录" + filePath);
		File file = new File(filePath);
		if (file.exists()) {
			// MyLog.logger.info("exists");
			if (file.isDirectory()) {
				File[] fileList = file.listFiles();
				// 目录为空，直接删除
				if (fileList == null || fileList.length == 0) {
					if (file.delete()) {
						// MyLog.logger.info("删除目录" + file);
					} else {
						MyLog.logger.error(file + "删除失败！");
					}
				} else {
					// 检测目录下文件
					for (int i = 0; i < fileList.length; i++) {

						if (fileList[i].isFile()) {
							// 是文件，就删除
							if (fileList[i].delete()) {
								// MyLog.logger.info("删除文件/目录"
								// + fileList[i].getName());
							} else {
								MyLog.logger.error(fileList[i].getName()
										+ "删除失败！");
							}
						}
						// 是目录，递归删除
						if (fileList[i].isDirectory()) {
							FileOperate.deleteFiles(fileList[i]
									.getAbsolutePath());
							// 删除完目录下文件之后，删除目录自己
							if (fileList[i].delete()) {
								MyLog.logger.info("删除目录" + file);
							} else {
								MyLog.logger.error(file + "删除失败！");
							}
						}
					}
					// 删除完目录下文件之后，删除目录自己
					if (file.delete()) {
						MyLog.logger.info("删除目录" + file);
					} else {
						MyLog.logger.error(file + "删除失败！");
					}
				}
			} else {
				file.delete();
			}
		} else {
			MyLog.logger.error("要删除的根文件/目录" + filePath + "不存在！");
		}
	}

	/**
	 * @throws IOException
	 * @category 复制src目录下的文件到dir目录下
	 */
	public static int copyDir(String sourceFilePath, String destinationPath)
			throws IOException {
		int numberOfFilesCopied = 0;
		File sourceFileOrDirectory = new File(sourceFilePath);
		File destinationFileOrDirectory = new File(destinationPath);

		return copyDir(sourceFileOrDirectory, destinationFileOrDirectory);
	}

	/**
	 * @throws IOException
	 * @category 复制src目录下的文件到dir目录下
	 */
	public static int copyDir(File sourceFileOrDirectory,
			File destinationFileOrDirectory) throws IOException {
		int numberOfFilesCopied = 0;
		if (sourceFileOrDirectory.isDirectory()) {
			// 创建目标目录
			destinationFileOrDirectory.mkdirs();
			String list[] = sourceFileOrDirectory.list();

			for (int i = 0; i < list.length; i++) {
				String dest1 = destinationFileOrDirectory.getPath()
						+ File.separator + list[i];
				String src1 = sourceFileOrDirectory.getPath() + File.separator
						+ list[i];
				numberOfFilesCopied += copyDir(src1, dest1);
			}
		} else {
			InputStream fin = new FileInputStream(sourceFileOrDirectory);
			fin = new BufferedInputStream(fin);
			try {
				OutputStream fout = new FileOutputStream(
						destinationFileOrDirectory);
				fout = new BufferedOutputStream(fout);
				try {
					int c;
					while ((c = fin.read()) >= 0) {
						fout.write(c);
					}
				} finally {
					fout.close();
				}
			} finally {
				fin.close();
			}
			numberOfFilesCopied++;
		}
		return numberOfFilesCopied;
	}

	/**
	 * @throws IOException
	 * @category 复制文件
	 */
	public static void copyFile(String srcFilePath, String dirFilePath) {
		try {
			File f1 = new File(srcFilePath);
			File f2 = new File(dirFilePath);
			if (f1.exists()) {
				if (!f2.exists()) {
					f2.createNewFile();
				}
				InputStream in = new FileInputStream(f1);
				OutputStream out = new FileOutputStream(f2, true);
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
				System.out.println("File copied.");
			} else {
				System.out.println(srcFilePath + "文件不存在！");
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}

	/**
	 * @category 创建文件，如果文件存在，则清空文件
	 */
	public void clearFile() throws IOException {
		file = new File(configFilePath);
		if (!file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			file.createNewFile();
		}
	}

	/**
	 * @throws IOException
	 * @category ӡ��������Ϣ
	 */
	public void printf() throws IOException {
		String readline;
		file = new File(configFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(configFilePath), "UTF-8"));
		while ((readline = br.readLine()) != null) {
			MyLog.logger.info(readline);
		}
		br.close();
	}

	public File getFile() {
		return file;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	/**
	 * @throws IOException
	 * @category 如果文件不存在，就创建新的文件
	 */
	public static boolean ifFileNotExistThenCreate(String filePath)
			throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
			MyLog.logger.info(filePath + "文件被创建");

		}
		return true;

	}

	/**
	 * @throws IOException
	 * @category 如果目录不存在，就创建新的目录
	 */
	public static boolean ifDirNotExistThenCreate(String filePath)
			throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
			MyLog.logger.info(filePath + "目录被创建");
		}
		return true;
	}

	/**
	 * @param filePath
	 * @throws IOException
	 */
	public static void clearFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
			file.createNewFile();
		} else {
			file.createNewFile();
		}
	}

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean isFileExist(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			Set test = PropertiesOperate.getPropertiesKeys(filePath);
			if (test.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * @category 向Properties文件中追加一行属性
	 * 
	 * @param name
	 * @param parameter
	 * @param iOSConfigFilePath
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean appendLine(String name, String parameter,
			String iOSConfigFilePath) throws UnsupportedEncodingException,
			IOException {

		File file = new File(iOSConfigFilePath);
		if (!file.exists()) {
			return false;
		}

		String appendString = new String("\n" + name + "=" + parameter + "\n");
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(appendString.getBytes("utf-8"));
		fos.close();
		return true;

	}

	/**
	 * @category 如果文件存在，则清空！
	 * @param filePath
	 * @throws IOException
	 */
	public static void ifFileExistsThenClear(String filePath)
			throws IOException {

		File file = new File(filePath);

		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}

	}

	/**
	 * 删除文件创建时间和当前时间差在num内的文件
	 * 
	 * @param filesPath
	 * @param num
	 *            一天以前：1000000 一小时前：10000 一分前：100
	 * @return
	 */
	public static boolean deletePreviousFiles(String filesPath, long rangeTime) {

		File file = new File(filesPath);
		if (!file.exists()) {
			System.out.println("deletePreviousFiles:文件不存在！");
			return false;
		}
		long tempTime = file.lastModified();
		long fileTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date(tempTime)));

		long curentTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()));
		System.out.println(fileTime);
		System.out.println(curentTime);
		if (curentTime < fileTime) {
			return false;
		} else {
			if ((curentTime - fileTime) >= rangeTime) {
				FileOperate.deleteFiles(filesPath);
				System.out.println(filesPath + "删除成功！");
			}
		}
		return true;

	}
}
