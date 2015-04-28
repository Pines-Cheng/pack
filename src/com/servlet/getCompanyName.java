package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.util.MyJSON;
import com.util.MyLog;
import com.util.PropertiesOperate;

/**
 * 获取所有公司的名字
 * 
 * @author spider
 * 
 */
public class getCompanyName extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 公司配置文件名
	public static String companyPropertiesPath;
	public static String companysFilePath;
	public static String tomcatCompanysFilePath;
	public static String androidTempPackPath;
	public static String androidProjectPath;
	public static String packSaveDir;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		PrintWriter out = response.getWriter();

		MyJSON js = new MyJSON();

		getCompanyNames(js.JSONObject);

		out.println(js.getJsonpString());
		out.flush();
		out.close();
	}

	/**
	 * @throws IOException
	 * @category 初始化servlet
	 */
	public void init(ServletConfig config) {

		// 初始化日志文件
		String logPath = config.getServletContext().getInitParameter("logPath");
		try {
			MyLog.LogInit(logPath);
			MyLog.logger.info("hello");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 获取公司名的配置文件??只可以在init里面做吗？？？
		companyPropertiesPath = config.getServletContext().getInitParameter(
				"companyName");
		MyLog.logger.info(companyPropertiesPath);

		// 获取公司文件信息的文件夹路径
		companysFilePath = config.getServletContext().getInitParameter(
				"companysFilePath");
		MyLog.logger.info(companysFilePath);

		// 获取tomcat下pack文件夹路径
		tomcatCompanysFilePath = config.getServletContext().getInitParameter(
				"tomcatCompanysFilePath");
		MyLog.logger.info(tomcatCompanysFilePath);

		androidProjectPath = config.getServletContext().getInitParameter(
				"androidProjectPath");
		MyLog.logger.info(androidProjectPath);

		androidTempPackPath = config.getServletContext().getInitParameter(
				"androidTempPackPath");
		MyLog.logger.info(androidTempPackPath);

		packSaveDir = config.getServletContext()
				.getInitParameter("packSaveDir");
		MyLog.logger.info(packSaveDir);

	}

	public boolean getCompanyNames(JSONObject js) {
		// 1.创建Set对象companyNames，通过属性表初始化
		Set<String> companyNames = new HashSet<String>();
		try {

			PropertiesOperate.createProperties(companyPropertiesPath);
			companyNames = PropertiesOperate
					.getPropertiesKeys(companyPropertiesPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 遍历
		Iterator<String> it = companyNames.iterator();
		while (it.hasNext()) {
			String companyName = it.next();
			System.out.println(companyName);
		}

		// 2.转为jsonString
		js.put("companyNames", companyNames);
		return true;

	}
}
