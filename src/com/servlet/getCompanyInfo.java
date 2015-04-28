package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comet4j.core.CometContext;

import com.aliyun.UploadFileInfo;
import com.util.FileOperate;
import com.util.MyJSON;
import com.util.MyLog;
import com.util.PropertiesOperate;

/**
 * 获取公司的所有配置信息的servlet
 * 
 * @author spider
 * 
 */
public class getCompanyInfo extends HttpServlet implements
		ServletContextListener {

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
		response.setCharacterEncoding("utf-8");

		// 保存选择的公司名
		initSession(request);

		String selectedCompanyName = request.getSession()
				.getAttribute("selectedCompanyName").toString();

		PrintWriter out = response.getWriter();

		// 创建json对象
		MyJSON js = new MyJSON();

		// 添加选中公司各个端的配置信息
		js.JSONObject.put("android",
				getAndroidCompanyInfo(request, out, selectedCompanyName));
		js.JSONObject.put("IOS",
				getIOSCompanyInfo(request, out, selectedCompanyName));
		js.JSONObject.put("PC",
				getPCCompanyInfo(request, out, selectedCompanyName));

		// 返回监听的channel
		js.JSONObject.put("channel",
				request.getSession().getAttribute("channel"));

		// 转为jsonString并发送

		MyLog.logger.info(js.getJsonpString());
		out.println(js.getJsonpString());
		out.flush();
		out.close();
	}

	/**
	 * @category 获取安卓端选中公司的配置信息，以json返回
	 * @param request
	 * @param out
	 * @return
	 * @throws IOException
	 * 
	 */
	private HashMap<String, HashMap> getAndroidCompanyInfo(
			HttpServletRequest request, PrintWriter out,
			String selectedCompanyName) throws IOException {

		// 获取配置文件夹路径
		String tomcatSelectCompamyFileString = new String(
				getCompanyName.tomcatCompanysFilePath + File.separator
						+ "android" + File.separator + selectedCompanyName);
		String saveSelectCompamyFileString = new String(
				getCompanyName.companysFilePath + File.separator + "android"
						+ File.separator + selectedCompanyName);
		String savePicFileString = new String(getCompanyName.companysFilePath
				+ File.separator + "android" + File.separator
				+ selectedCompanyName + File.separator + "Images");

		// 获取配置文件信息
		String savePicPropertiesFileString = new String(
				saveSelectCompamyFileString + File.separator
						+ "config/pic.properties");
		String configPropertiesPath = new String(tomcatSelectCompamyFileString
				+ File.separator + "config/config.properties");
		String androidPropertiesPath = new String(tomcatSelectCompamyFileString
				+ File.separator + "config/android.properties");

		// 1.创建返回的map
		HashMap<String, String> companyConfigInfo = new HashMap<String, String>();
		HashMap<String, HashMap> CompanyInfo = new HashMap<String, HashMap>();

		// 2.检查公司的保存目录——文件目录树，不存在则创建
		initAndroidCompanyFileTree(selectedCompanyName);

		// 3. 将图片保存到save 的pic.properties中
		saveAndroidImagesProperties(savePicFileString,
				savePicPropertiesFileString);

		// 4.创建目录树，验证选中公司的目录信息，不存在就创建并copy过来，存在就直接copy到pack目录下
		InitTomcatCompany(request, "android", selectedCompanyName);

		// 5.初始化android端图片的HashMap
		HashMap<String, String> companyImagesInfo = getImagesHashMap(
				savePicFileString, "android", selectedCompanyName);

		// 6.获取获取配置文件信息,添加到HashMap
		companyConfigInfo = PropertiesOperate.getAllProperties(
				configPropertiesPath, androidPropertiesPath);

		// 7.添加到androidCompanyInfo的HashMap
		CompanyInfo.put("companyImagesInfo", companyImagesInfo);
		CompanyInfo.put("companyConfigInfo", companyConfigInfo);

		return CompanyInfo;

	}

	private HashMap<String, HashMap> getIOSCompanyInfo(
			HttpServletRequest request, PrintWriter out,
			String selectedCompanyName) throws IOException {

		// 获取配置文件夹路径
		String tomcatSelectCompamyFileString = new String(
				getCompanyName.tomcatCompanysFilePath + File.separator + "IOS"
						+ File.separator + selectedCompanyName);
		String savePicFileString = new String(getCompanyName.companysFilePath
				+ File.separator + "IOS" + File.separator + selectedCompanyName
				+ File.separator + "Images");
		// 获取配置文件信息
		String saveConfigPropertiesFileString = new String(
				tomcatSelectCompamyFileString + File.separator
						+ "config/config.cfg");

		// 1.创建返回的map
		HashMap<String, String> companyConfigInfo = new HashMap<String, String>();
		HashMap<String, HashMap> CompanyInfo = new HashMap<String, HashMap>();

		// 2.创建空目录和空配置文件
		initIOSCompanyFileTree(selectedCompanyName);

		// 3.同步保存目录和tomcat目录
		InitTomcatCompany(request, "IOS", selectedCompanyName);

		// 4.初始化IOS端图片的HashMap
		HashMap<String, String> companyImagesInfo = getImagesHashMap(
				savePicFileString, "IOS", selectedCompanyName);

		// 5.获取获取配置文件信息,添加到HashMap
		companyConfigInfo = PropertiesOperate
				.getAllProperties(saveConfigPropertiesFileString);

		// 6.添加到androidCompanyInfo的HashMap
		CompanyInfo.put("companyImagesInfo", companyImagesInfo);
		CompanyInfo.put("companyConfigInfo", companyConfigInfo);

		return CompanyInfo;

	}

	/**
	 * 待完善
	 * 
	 * @param request
	 * @param out
	 * @return
	 * @throws IOException
	 */
	private HashMap<String, HashMap> getPCCompanyInfo(
			HttpServletRequest request, PrintWriter out,
			String selectedCompanyName) throws IOException {

		String companyFilePath = getCompanyName.companysFilePath
				+ File.separator + "PC" + File.separator + selectedCompanyName;

		String companyconfigFilePath = companyFilePath + File.separator
				+ "config";

		// 获取选中的公司的config1文件路径
		String companyCustomPropertiesFilePath = companyconfigFilePath
				+ File.separator + "custom.properties";
		String companySettingPropertiesFilePath = companyconfigFilePath
				+ File.separator + "setting.properties";

		String savePicFileString = companyFilePath + File.separator + "Images";

		// 1.创建返回的map
		HashMap<String, String> companyConfigInfo = new HashMap<String, String>();
		HashMap<String, HashMap> CompanyInfo = new HashMap<String, HashMap>();

		// 2.检查公司的保存目录——文件目录树，不存在则创建
		initPCCompanyFileTree(selectedCompanyName);

		// 4.创建目录树，验证选中公司的目录信息，不存在就创建并copy过来，存在就直接copy到pack目录下
		InitTomcatCompany(request, "PC", selectedCompanyName);

		// 4.初始化IOS端图片的HashMap
		HashMap<String, String> companyImagesInfo = getImagesHashMap(
				savePicFileString, "PC", selectedCompanyName);

		// 6.获取获取配置文件信息,添加到HashMap
		companyConfigInfo = PropertiesOperate.getAllProperties(
				companyCustomPropertiesFilePath,
				companySettingPropertiesFilePath);

		// 6.添加到androidCompanyInfo的HashMap
		CompanyInfo.put("companyImagesInfo", companyImagesInfo);
		CompanyInfo.put("companyConfigInfo", companyConfigInfo);

		return CompanyInfo;

	}

	/**
	 * 获取图片的map，key=图片名，value图片路径,返回字符串
	 * 
	 * @category 获取图片的map，key=图片名，value图片路径,返回字符串
	 * @param savePicFileString
	 * @param savePicPropertiesFileString
	 * @param platform
	 * @param tomcatCompanyImageFile
	 * @return
	 * @throws IOException
	 */
	private HashMap<String, String> getImagesHashMap(String savePicFileString,
			String platform, String selectedCompanyName) throws IOException {

		// 获取tomcat图片目录
		File tomcatCompanyImageFile = new File(savePicFileString);

		HashMap<String, String> companyImagesInfo = new HashMap<String, String>();

		String[] imagesStrings = tomcatCompanyImageFile.list();

		// 判断图片文件夹是否为空，不为空就遍历，且返回图片的链接json字符串
		if (imagesStrings == null) {
			MyLog.logger.info("image文件夹为空！");

		} else {
			// 使用图片名和路径初始化map对象
			MyLog.logger.info("image文件夹不为空！");

			for (int i = 0; i < imagesStrings.length; i++) {
				MyLog.logger.info(imagesStrings[i] + ":" + "/pack/pack"
						+ File.separator + platform + File.separator
						+ selectedCompanyName + imagesStrings[i]);
				companyImagesInfo.put(imagesStrings[i], "/pack/pack"
						+ File.separator + platform + File.separator
						+ selectedCompanyName + "/Images/" + imagesStrings[i]);
			}
		}
		return companyImagesInfo;
	}

	/**
	 * 对于安卓端，需要在pic.properties配置文件中写入图片的配置信息，每次在获取配置文件信息的时候，
	 * 将根据images文件夹下的图片信息更新pic.proprerties文件
	 * 
	 * @category 对于安卓端，需要在pic.properties配置文件中写入图片的配置信息，每次在获取配置文件信息的时候，
	 *           将根据images文件夹下的图片信息更新pic.proprerties文件
	 * @param savePicFileString
	 * @param savePicPropertiesFileString
	 * @param tomcatCompanyImageFile
	 * @return
	 * @throws IOException
	 *             @
	 */
	private HashMap<String, String> saveAndroidImagesProperties(
			String savePicFileString, String savePicPropertiesFileString)
			throws IOException {

		// 获取tomcat图片目录
		File tomcatCompanyImageFile = new File(savePicFileString);

		HashMap<String, String> companyImagesInfo = new HashMap<String, String>();

		String[] imagesStrings = tomcatCompanyImageFile.list();

		// 将图片路径写入配置文件pic.properties
		// 如果图片存在，删除删除重建
		File picPropertiesFile = new File(savePicPropertiesFileString);
		if (picPropertiesFile.exists()) {
			picPropertiesFile.delete();
		}
		FileOperate.ifFileNotExistThenCreate(savePicPropertiesFileString);

		if (imagesStrings == null) {
			MyLog.logger.info("image文件夹为空！");

		} else {
			// 使用图片名和路径初始化map对象
			MyLog.logger.info("image文件夹不为空！");

			for (int i = 0; i < imagesStrings.length; i++) {

				// 获取pic.properties的路径，将图片名和绝对路径添加到pic.properties中
				String[] pictureName = imagesStrings[i].split("\\.");
				PropertiesOperate.appendProperties(pictureName[0],
						savePicFileString + File.separator + imagesStrings[i],
						savePicPropertiesFileString, null);
			}
		}
		return companyImagesInfo;
	}

	/**
	 * @throws IOException
	 * @category 如果公司的目录树不存在，就创建
	 */
	private void initAndroidCompanyFileTree(String selectedCompanyName)
			throws IOException {
		// 获取选中的pack文件夹路径
		// 判断公司文件夹下是否存在，若不存在就创建
		FileOperate.ifDirNotExistThenCreate(getCompanyName.companysFilePath);
		// 获取选中的公司的配置文件夹路径
		String companyFilePath = getCompanyName.companysFilePath
				+ File.separator + "android" + File.separator
				+ selectedCompanyName;
		// 判断公司文件夹下是否存在，若不存在就创建
		FileOperate.ifDirNotExistThenCreate(companyFilePath);
		// 获取选中的公司的images文件夹路径
		String companyImagesFilePath = companyFilePath + File.separator
				+ "Images";
		FileOperate.ifDirNotExistThenCreate(companyImagesFilePath);
		// 获取选中的公司的config文件夹路径
		String companyconfigFilePath = companyFilePath + File.separator
				+ "config";
		FileOperate.ifDirNotExistThenCreate(companyconfigFilePath);
		// 获取选中的公司的config1文件路径
		String companyconfig1FilePath = companyconfigFilePath + File.separator
				+ "config.properties";
		FileOperate.ifFileNotExistThenCreate(companyconfig1FilePath);
		// 获取选中的公司的config2文件路径
		String companyconfig2FilePath = companyconfigFilePath + File.separator
				+ "pic.properties";
		FileOperate.ifFileNotExistThenCreate(companyconfig2FilePath);
		// 获取选中的公司的config2文件路径
		String companyconfig3FilePath = companyconfigFilePath + File.separator
				+ "android.properties";
		FileOperate.ifFileNotExistThenCreate(companyconfig3FilePath);
		// 获取选中的公司的log文件夹路径
		String companylogFilePath = companyFilePath + File.separator + "log";
		FileOperate.ifDirNotExistThenCreate(companylogFilePath);
	}

	/**
	 * @throws IOException
	 * @category 如果公司的目录树不存在,就创建
	 */
	private void initPCCompanyFileTree(String selectedCompanyName)
			throws IOException {
		// 获取选中的pack文件夹路径
		// 判断公司文件夹下是否存在，若不存在就创建
		FileOperate.ifDirNotExistThenCreate(getCompanyName.companysFilePath);
		// 获取选中的公司的配置文件夹路径
		String companyFilePath = getCompanyName.companysFilePath
				+ File.separator + "PC" + File.separator + selectedCompanyName;
		// 判断公司文件夹下是否存在，若不存在就创建
		FileOperate.ifDirNotExistThenCreate(companyFilePath);
		// 获取选中的公司的images文件夹路径
		String companyImagesFilePath = companyFilePath + File.separator
				+ "Images";
		FileOperate.ifDirNotExistThenCreate(companyImagesFilePath);
		// 获取选中的公司的config文件夹路径
		String companyconfigFilePath = companyFilePath + File.separator
				+ "config";
		FileOperate.ifDirNotExistThenCreate(companyconfigFilePath);

		// 获取选中的公司的config1文件路径
		String companyCustomPropertiesFilePath = companyconfigFilePath
				+ File.separator + "custom.properties";
		String companySettingPropertiesFilePath = companyconfigFilePath
				+ File.separator + "setting.properties";

		FileOperate.ifFileNotExistThenCreate(companyCustomPropertiesFilePath);
		FileOperate.ifFileNotExistThenCreate(companySettingPropertiesFilePath);

		// 获取选中的公司的log文件夹路径
		String companylogFilePath = companyFilePath + File.separator + "log";
		FileOperate.ifDirNotExistThenCreate(companylogFilePath);
	}

	/**
	 * @throws IOException
	 * @category 如果公司的目录树不存在,就创建
	 */
	private void initIOSCompanyFileTree(String selectedCompanyName)
			throws IOException {
		// 获取选中的pack文件夹路径
		// 判断公司文件夹下是否存在，若不存在就创建
		FileOperate.ifDirNotExistThenCreate(getCompanyName.companysFilePath);
		// 获取选中的公司的配置文件夹路径

		String companyFilePath = getCompanyName.companysFilePath
				+ File.separator + "IOS" + File.separator + selectedCompanyName;
		// 判断公司文件夹下是否存在，若不存在就创建
		FileOperate.ifDirNotExistThenCreate(companyFilePath);

		// 获取选中的公司的images文件夹路径
		String companyImagesFilePath = companyFilePath + File.separator
				+ "Images";
		FileOperate.ifDirNotExistThenCreate(companyImagesFilePath);

		// 获取选中的公司的config文件夹路径
		String companyconfigFilePath = companyFilePath + File.separator
				+ "config";
		FileOperate.ifDirNotExistThenCreate(companyconfigFilePath);

		// 获取选中的公司的config1文件路径
		String companyconfig1FilePath = companyconfigFilePath + File.separator
				+ "config.cfg";
		FileOperate.ifFileNotExistThenCreate(companyconfig1FilePath);

		// 获取选中的公司的log文件夹路径
		String companylogFilePath = companyFilePath + File.separator + "log";
		FileOperate.ifDirNotExistThenCreate(companylogFilePath);
	}

	/**
	 * @param request
	 * @param platform
	 * @category 安卓端选中的公司，在tomcat下初始化公司文件夹,三端通用
	 */
	private void InitTomcatCompany(HttpServletRequest request, String platform,
			String selectedCompanyName) throws IOException {

		// 如果tomcat的pack目录下存在选中的公司文件名，则删除，然后再复制过来
		String selectCompanyNameString = new String(
				getCompanyName.tomcatCompanysFilePath + File.separator
						+ platform + File.separator + selectedCompanyName);
		String savedCompanyNameString = new String(
				getCompanyName.companysFilePath + File.separator + platform
						+ File.separator + selectedCompanyName);
		FileOperate.ifDirNotExistThenCreate(savedCompanyNameString);
		FileOperate.ifDirNotExistThenCreate(selectCompanyNameString);

		File selectCompanyNameFile = new File(selectCompanyNameString);
		if (selectCompanyNameFile.exists()) {
			FileOperate.deleteFiles(selectCompanyNameString);
			selectCompanyNameFile.mkdirs();
			FileOperate
					.copyDir(savedCompanyNameString, selectCompanyNameString);
			MyLog.logger.info("复制到" + selectCompanyNameString);
		} else {
			selectCompanyNameFile.mkdir();
			FileOperate
					.copyDir(savedCompanyNameString, selectCompanyNameString);
			MyLog.logger.info("复制到" + getCompanyName.tomcatCompanysFilePath
					+ File.separator + selectedCompanyName);
		}

	}

	/**
	 * @param request
	 * @category 初始化选中的公司
	 */
	private void initSession(HttpServletRequest request) {
		// 打印出请求的公司名
		String selectedCompanyName = request.getParameter("selectedCompany");
		if (selectedCompanyName == null) {
			selectedCompanyName = request.getSession()
					.getAttribute("selectedCompanyName").toString();
			MyLog.logger.info("从session里面获取的selectedCompanyName为："
					+ selectedCompanyName);
		} else {
			request.getSession().setAttribute("selectedCompanyName",
					selectedCompanyName);
		}
		MyLog.logger.info("initcompany:你选择了：" + selectedCompanyName + "公司！");

		// 初始化上传队列和channel
		if (request.getSession().getAttribute("uploadQueue") == null) {
			Queue<UploadFileInfo> uploadQueue = new LinkedList<UploadFileInfo>();
			request.getSession().setAttribute("uploadQueue", uploadQueue);
		}

		// 初始化channel
		if (request.getSession().getAttribute("channel") == null) {

			String channel = getCurrentTime();

			// 注册一个应用的channel
			CometContext cc = CometContext.getInstance();
			cc.registChannel(channel);
			MyLog.logger.warn("注册了：" + channel);

			request.getSession().setAttribute("channel", channel);
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = sdf.format(d);
		return currentTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

}
