package com.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * @author spider
 * @category 属性文件的操作
 * 
 */
public class PropertiesOperate {

	public static void main(String[] args) {
		try {
			PropertiesOperate.appendProperties("test1", "spider",
					"test.properties", "你阿妈");
			PropertiesOperate.appendProperties("test2", "spider",
					"test.properties", "尼玛");
			PropertiesOperate.ListAllProperties("test.properties");
			PropertiesOperate.removeProperties("test2", "test.properties");
			PropertiesOperate.ListAllProperties("test.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String result = PropertiesOperate.getProperties("test1",
					"test.properties");
			// MyLog.logger.info(result);
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PropertiesOperate.getAllProperties("test.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @category 创建配置文件
	 * */
	public static void createProperties(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath() + "配置文件存在！");
		} else {
			file.createNewFile();
			System.out.println(file.getAbsolutePath() + "创建成功！");
		}
	}

	/**
	 * @category 判断配置文件是否存在
	 * */
	public static boolean isExists(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @category 打印所有的配置信息
	 * */
	public static void ListAllProperties(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			// 初始化
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			prop.load(in);
			// 遍历
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				// MyLog.logger.info(key + ":" + prop.getProperty(key));
				System.out.println(key + ":" + prop.getProperty(key));
			}
			in.close();
		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}
	}

	/**
	 * @category 列出所有的配置信息，返回一个map
	 * */
	public static HashMap<String, String> getAllProperties(String filePath)
			throws IOException {

		File file = new File(filePath);
		HashMap<String, String> companyConfigInfo = new HashMap<String, String>();

		if (file.exists()) {
			// 初始化
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			prop.load(in);
			// 遍历
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				// 名字里面含有中文，转换一下编码。
				if (key.equals("name")) {
					String str = new String(prop.getProperty(key).getBytes(
							"ISO-8859-1"), "utf-8");
					companyConfigInfo.put(key, str);
				} else {
					System.out.println(key + ":" + prop.getProperty(key));
					companyConfigInfo.put(key, prop.getProperty(key));
				}
			}
			in.close();
		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}
		return companyConfigInfo;
	}

	/**
	 * @category 列出所有的配置信息，返回一个map
	 * */
	public static HashMap<String, String> getAllProperties(String filePath1,
			String filePath2) throws IOException {

		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		HashMap<String, String> companyConfigInfo = new HashMap<String, String>();

		if (file1.exists()) {
			// 初始化
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath1));
			prop.load(in);
			// 遍历
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				// MyLog.logger.info(key + ":" + prop.getProperty(key));
				System.out.println(key + ":" + prop.getProperty(key));
				companyConfigInfo.put(key, prop.getProperty(key));
			}
			in.close();
		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}
		if (file2.exists()) {
			// 初始化
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath2));
			prop.load(in);
			// 遍历
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				// MyLog.logger.info(key + ":" + prop.getProperty(key));
				System.out.println(key + ":" + prop.getProperty(key));
				companyConfigInfo.put(key, prop.getProperty(key));
			}
			in.close();
		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}
		return companyConfigInfo;
	}

	/**
	 * @category 通过关键字和配置文件路径获取配置
	 * */
	public static String getProperties(String key, String filePath)
			throws IOException {
		String result = "";
		File file = new File(filePath);
		if (file.exists()) {
			Properties prop = new Properties();
			// ��ȡ�����ļ�a.properties
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			// ���������б�
			prop.load(in);
			result = prop.getProperty(key);
			in.close();
		} else {

			System.out.println("配置文件不存在！");
		}
		return result;

	}

	/**
	 * @category 添加属性
	 * */
	public static void appendProperties(String key, String value,
			String filePath, String comments) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			Properties prop = new Properties();
			// true
			FileOutputStream oFile = new FileOutputStream(filePath, true);
			prop.setProperty(key, value);
			prop.store(oFile, comments);
			oFile.close();
		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}
	}

	/**
	 * @category 获取所有的key值
	 * */
	public static Set<String> getPropertiesKeys(String filePath)
			throws IOException {
		// 1.获取set集合的值
		File file = new File(filePath);
		Set<String> keys = null;

		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			prop.load(in);
			keys = prop.stringPropertyNames();

		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}
		return keys;
	}

	/**
	 * @category 移出属性文件的一个属性
	 * @param key
	 * @param filePath
	 * @throws IOException
	 */
	public static void removeProperties(String key, String filePath)
			throws IOException {
		// 1.获取set集合的值
		File file = new File(filePath);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));

			prop.load(in);
			System.out.println(prop);
			prop.remove(key);
			in.close();
			FileOutputStream oFile = new FileOutputStream(filePath);
			prop.store(oFile, null);
			oFile.close();

		} else {
			// MyLog.logger.info("配置文件不存在！");
			System.out.println("配置文件不存在！");
		}

	}
}
