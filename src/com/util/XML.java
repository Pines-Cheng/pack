package com.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XML {

	public static void main(String[] args) throws Exception {

		// String filePath = new String("AndroidManifest.xml");
		// System.out.println("返回的是" + getVersionCode(filePath));

		// /dict/array/dict/array/dict/string

		readXpathText("WebRoot/res/file/netdisk.plist",
				"/plist/dict/array/dict/array/dict/string[2]");

		replaceXpathText("WebRoot/res/file/netdisk.plist",
				"/plist/dict/array/dict/array/dict/string[2]", "haha");

		readXpathText("WebRoot/res/file/netdisk.plist",
				"/plist/dict/array/dict/array/dict/string[2]");

		readXpathText("WebRoot/res/file/netdisk.plist",
				"/plist/dict/array/dict/dict/string[4]");

		replaceXpathText("WebRoot/res/file/netdisk.plist",
				"/plist/dict/array/dict/dict/string[4]", "haha");

		readXpathText("WebRoot/res/file/netdisk.plist",
				"/plist/dict/array/dict/dict/string[4]");

		// System.out.println(System.getProperty("user.dir"));
	}

	/**
	 * @category 通过XML文件获取svn版本号等信息 AndroidMain.xml
	 * @param filePath
	 * @return
	 * @throws DocumentException
	 * @throws Exception
	 */
	public static String getVersionCode(String filePath)
			throws DocumentException, Exception {
		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(new File(filePath));

		// 获取根元素
		Element root = document.getRootElement();
		System.out.println("Root: " + root.getName());

		// 输出其属性
		System.out.println("first World Attr: " + root.attribute(1).getName()
				+ "=" + root.attributeValue("versionCode"));
		return root.attributeValue("versionCode");
	}

	/**
	 * 获取XPath的node，将text写进去
	 * 
	 * @param filePath
	 *            文件路径
	 * @param nodePath
	 *            XPath
	 * @param text
	 *            替换的文本
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void replaceXpathText(String filePath, String nodePath,
			String text) throws DocumentException, IOException {

		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(filePath));

		Node node = document.selectSingleNode(nodePath);

		// List<?> list = document.selectNodes(nodePath);
		// Iterator<?> i = list.iterator();
		// while (i.hasNext()) {
		// Node node = (Node) i.next();
		// System.out.println(node.getText());
		// }

		node.setText(text);
		// System.out.println("set");

		// 写回去
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8"); // 指定XML编码
		XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
		writer.write(document);
		writer.close();
	}

	/**
	 * @param filePath
	 * @param nodePath
	 * @return
	 * @throws DocumentException
	 */
	public static String readXpathText(String filePath, String nodePath)
			throws DocumentException {

		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(filePath));

		Node node = document.selectSingleNode(nodePath);
		System.out.println(node.getText());

		return node.getText();

	}
}
