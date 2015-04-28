package com.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.util.FileOperate;
import com.util.MyJSON;
import com.util.MyLog;
import com.util.PropertiesOperate;

/**
 * 删除、修改、添加公司名
 * 
 * @author spider
 * 
 */
public class companyNameOperateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");

		response.setContentType("text/html;charset=utf-8");

		String selectedCompanyName = request.getSession()
				.getAttribute("selectedCompanyName").toString();

		String command = request.getParameter("command");
		String platform = request.getParameter("platform");

		PrintWriter out = response.getWriter();

		// 创建json对象，用于返回
		MyJSON js = new MyJSON();

		if (command.equals("delete")) {

			deleteCompany(js.JSONObject, selectedCompanyName);

		}
		if (command.equals("edit")) {
			editCompany(request, out, js.JSONObject, selectedCompanyName);

		}
		if (command.equals("add")) {
			addCompany(request, js.JSONObject);
		}

		MyLog.logger.info(js.getJsonString());
		out.println(js.getJsonString());
		out.flush();
		out.close();
	}

	/**
	 * @param request
	 * @param command
	 * @param out
	 * @param js
	 * @throws IOException
	 */
	private boolean addCompany(HttpServletRequest request, JSONObject js)
			throws IOException {

		// 获取公司名
		String companyName = request.getParameter("companyName");

		if (companyName.isEmpty()) {
			js.put("status", "ERROR");
			js.put("message", "添加的公司名为空！");
			return false;
		}

		// 如果存在，就返回错误 执行else
		if (verifyCompanyName(companyName)) {
			PropertiesOperate.appendProperties(companyName, PinyinHelper
					.convertToPinyinString(companyName, "",
							PinyinFormat.WITHOUT_TONE),
					getCompanyName.companyPropertiesPath, null);

		} else {
			js.put("status", "ERROR");
			js.put("message", "添加的公司名:" + companyName + "已存在！");
			return false;

		}

		js.put("status", "SUCCESS");
		js.put("message", "添加公司成功！");
		return true;
	}

	/**
	 * @param request
	 * @param out
	 * @param jSONObject
	 * @throws IOException
	 */
	private boolean editCompany(HttpServletRequest request, PrintWriter out,
			JSONObject js, String selectedCompanyName) throws IOException {
		String companyName = request.getParameter("companyName");

		if (companyName.isEmpty()) {
			js.put("status", "ERROR");
			js.put("message", "修改的公司名为空！");
			return false;
		}

		// 如果公司名不存在与配置文件中，返回true，添加进properties
		if (verifyCompanyName(companyName)) {

			// 添加到配置文件中
			PropertiesOperate.appendProperties(companyName, PinyinHelper
					.convertToPinyinString(companyName, "",
							PinyinFormat.WITHOUT_TONE),
					getCompanyName.companyPropertiesPath, null);

			// copy原来的到新的文件夹中
			FileOperate
					.copyDir(getCompanyName.companysFilePath + File.separator
							+ "android" + File.separator + selectedCompanyName,
							getCompanyName.companysFilePath + File.separator
									+ "android" + File.separator + companyName);
			FileOperate.copyDir(getCompanyName.companysFilePath
					+ File.separator + "IOS" + File.separator
					+ selectedCompanyName, getCompanyName.companysFilePath
					+ File.separator + "IOS" + File.separator + companyName);
			FileOperate.copyDir(getCompanyName.companysFilePath
					+ File.separator + "PC" + File.separator
					+ selectedCompanyName, getCompanyName.companysFilePath
					+ File.separator + "PC" + File.separator + companyName);

			// 删除配置文件中原来的公司
			PropertiesOperate.removeProperties(selectedCompanyName,
					getCompanyName.companyPropertiesPath);

			// 删除原来的配置文件夹
			FileOperate.deleteFiles(getCompanyName.companysFilePath
					+ File.separator + "android" + File.separator
					+ selectedCompanyName);
			FileOperate.deleteFiles(getCompanyName.companysFilePath
					+ File.separator + "IOS" + File.separator
					+ selectedCompanyName);
			FileOperate.deleteFiles(getCompanyName.companysFilePath
					+ File.separator + "PC" + File.separator
					+ selectedCompanyName);
		} else {
			js.put("status", "ERROR");
			js.put("message", "修改的公司名:" + companyName + "已存在！");
			return false;
		}

		js.put("status", "SUCCESS");
		js.put("message", "修改公司名成功！");
		return true;
	}

	/**
	 * @param jSONObject
	 */
	private void deleteCompany(JSONObject jSONObject, String selectedCompanyName) {
		try {
			// 删除properties文件中的公司名
			PropertiesOperate.removeProperties(selectedCompanyName,
					getCompanyName.companyPropertiesPath);
			// 删除/usr/local/pack目录下的公司配置信息
			FileOperate.deleteFiles(getCompanyName.companysFilePath
					+ File.separator + "android" + File.separator
					+ selectedCompanyName);
			FileOperate.deleteFiles(getCompanyName.companysFilePath
					+ File.separator + "IOS" + File.separator
					+ selectedCompanyName);
			FileOperate.deleteFiles(getCompanyName.companysFilePath
					+ File.separator + "PC" + File.separator
					+ selectedCompanyName);

			jSONObject.put("status", "SUCCESS");
			jSONObject.put("message", "");
		} catch (Exception e) {

			e.printStackTrace();
			jSONObject.put("status", "ERROR");
			jSONObject.put("message", "删除文件出现错误！");
		}
	}

	/**
	 * @param companyName
	 * @return false 存在 ，true 不存在
	 * @throws IOException
	 */
	private boolean verifyCompanyName(String companyName) throws IOException {
		// 1.校验所有的公司名
		Properties prop = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(
				getCompanyName.companyPropertiesPath));
		prop.load(in);
		if (prop.containsKey(companyName)
				|| prop.containsValue(PinyinHelper.convertToPinyinString(
						companyName, "", PinyinFormat.WITHOUT_TONE))) {
			return false;
		} else {

			return true;
		}
	}
}
