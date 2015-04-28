package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pack.define.packConfigDefine;
import com.util.FileOperate;
import com.util.MyJSON;
import com.util.PropertiesOperate;

/**
 * 保存配置的servlet，三端通用
 * 
 * @author spider
 * 
 */
public class configSaveServlet extends HttpServlet {

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

		request.setCharacterEncoding("UTF-8");

		response.setContentType("text/html;charset=utf-8");

		String selectedCompanyName = request.getSession()
				.getAttribute("selectedCompanyName").toString();

		PrintWriter out = response.getWriter();

		MyJSON js = new MyJSON();

		String platform = request.getParameter("platform");
		if ((platform != null)) {
			if (platform.equals("android")) {
				androidConfigSave(request, out, js.JSONObject,
						selectedCompanyName);
			}
			if (platform.equals("IOS")) {
				IOSConfigSave(request, out, js.JSONObject, selectedCompanyName);
			}
			if (platform.equals("PC")) {
				PCConfigSave(request, out, js.JSONObject, selectedCompanyName);
			}
		}

		out.println(js.getJsonString());
		out.flush();
		out.close();

	}

	/**
	 * @param request
	 * @param out
	 * @param jSONObject
	 * @return
	 * @throws IOException
	 */
	private boolean androidConfigSave(HttpServletRequest request,
			PrintWriter out, JSONObject jSONObject, String selectedCompanyName)
			throws IOException {

		// 获取配置文件的路径
		String configPath = new String(getCompanyName.companysFilePath
				+ File.separator + "android" + File.separator
				+ selectedCompanyName + "/config");
		// 获取android.peroperties的路径
		String androidPropertiesPath = new String(configPath + File.separator
				+ "android.properties");
		// 获取config.peroperties的路径
		String configPropertiesPath = new String(configPath + File.separator
				+ "config.properties");

		// 将两个配置文件都清空
		FileOperate.clearFile(androidPropertiesPath);
		FileOperate.clearFile(configPropertiesPath);

		// 获取request参数
		try {
			Enumeration<String> enm = request.getParameterNames();
			while (enm.hasMoreElements()) {
				String name = enm.nextElement();
				if (name.equals("callback") || name.equals("_")) {
					continue;
				}
				if (name.equals("defaultset") || name.equals("serverip")
						|| name.equals("serverport")) {
					// 追加到config.properties
					PropertiesOperate.appendProperties(name,
							request.getParameter(name), configPropertiesPath,
							null);
				} else {
					// 追加到android.properties
					PropertiesOperate.appendProperties(name,
							request.getParameter(name), androidPropertiesPath,
							null);
				}
			}
		} catch (Exception e) {
			jSONObject.put("status", "ERROR");
			jSONObject.put("message", "写入配置文件错误！");
			return false;
		}
		jSONObject.put("status", "SUCCESS");
		jSONObject.put("message", "保存配置成功！");
		return true;
	}

	/**
	 * @param request
	 * @param out
	 * @param jSONObject
	 * @return
	 * @throws IOException
	 */
	private boolean IOSConfigSave(HttpServletRequest request, PrintWriter out,
			JSONObject jSONObject, String selectedCompanyName)
			throws IOException {
		String localDir = new String(getCompanyName.tomcatCompanysFilePath
				+ File.separator + "IOS" + File.separator + selectedCompanyName
				+ "/Images");

		// 获取配置文件的路径
		String configPath = new String(getCompanyName.companysFilePath
				+ File.separator + "IOS" + File.separator + selectedCompanyName
				+ "/config");
		// 获取Iconfig.cfg的路径
		String IOSConfigFilePath = new String(configPath + File.separator
				+ "config.cfg");

		// 将配置文件都清空
		FileOperate.clearFile(IOSConfigFilePath);

		// 获取request参数
		try {

			// 额外的配置信息，包括图片目录和其他信息

			PropertiesOperate.appendProperties("identifier",
					"com.sucun.SuCunCloud", IOSConfigFilePath, null);

			Enumeration<String> enm = request.getParameterNames();
			while (enm.hasMoreElements()) {
				String name = enm.nextElement();
				if (name.equals("callback") || name.equals("_")
						|| name.equals("platform")) {
					continue;
				} else if (name.equals("defaultset") || name.equals("serverIp")
						|| name.equals("serverPort") || name.equals("name")
						|| name.equals("version")) {

					if (name.equals("name")) {
						// 追加到一行config.cfg，此处未采用properties自带的方法，因为自带的方法为ISO-88591-1编码，会导致显示及打包时的乱码，故采用特殊处理，以utf-8的格式单独写入。
						FileOperate.appendLine(name,
								request.getParameter(name), IOSConfigFilePath);
					} else {
						// 直接通过properties属性写入到config.cfg
						PropertiesOperate.appendProperties(name,
								request.getParameter(name), IOSConfigFilePath,
								null);
					}
				}
			}

		} catch (Exception e) {
			jSONObject.put("status", "ERROR");
			jSONObject.put("message", "写入配置文件错误！");
			return false;
		}
		jSONObject.put("status", "SUCCESS");
		jSONObject.put("message", "保存配置成功！");
		return true;
	}

	/**
	 * @param request
	 * @param out
	 * @param jSONObject
	 * @throws IOException
	 */
	private boolean PCConfigSave(HttpServletRequest request, PrintWriter out,
			JSONObject jSONObject, String selectedCompanyName)
			throws IOException {

		// 获取配置文件的路径
		String pcConfigPath = new String(getCompanyName.companysFilePath
				+ File.separator + "PC" + File.separator + selectedCompanyName
				+ "/config");
		// 获取选中的公司的config1文件路径
		String companyCustomPropertiesFilePath = pcConfigPath + File.separator
				+ "custom.properties";
		String companySettingPropertiesFilePath = pcConfigPath + File.separator
				+ "setting.properties";

		// 将配置文件都清空
		FileOperate.clearFile(companyCustomPropertiesFilePath);
		FileOperate.clearFile(companySettingPropertiesFilePath);

		// 获取request参数
		try {

			// 额外的配置信息，包括图片目录和其他信息

			Enumeration<String> enm = request.getParameterNames();
			while (enm.hasMoreElements()) {
				String name = enm.nextElement();
				if (verifyConfigInfo(name, packConfigDefine.PC_CUSTOM_CONFIG)) {

					// 直接通过properties属性写入到config.cfg
					PropertiesOperate.appendProperties(name,
							request.getParameter(name),
							companyCustomPropertiesFilePath, null);
				} else if (verifyConfigInfo(name,
						packConfigDefine.PC_SETTING_CONFIG)) {

					// 直接通过properties属性写入到config.cfg
					PropertiesOperate.appendProperties(name,
							request.getParameter(name),
							companySettingPropertiesFilePath, null);
				}
			}

		} catch (Exception e) {
			jSONObject.put("status", "ERROR");
			jSONObject.put("message", "写入配置文件错误！");
			return false;
		}
		jSONObject.put("status", "SUCCESS");
		jSONObject.put("message", "保存配置成功！");
		return true;

	}

	/**
	 * @param name
	 * @param pcCustomConfig
	 * @return
	 */
	private boolean verifyConfigInfo(String name, String[] pcCustomConfig) {

		for (String temp : pcCustomConfig) {
			if (name.equals(temp)) {
				return true;
			}
		}
		return false;
	}

}
