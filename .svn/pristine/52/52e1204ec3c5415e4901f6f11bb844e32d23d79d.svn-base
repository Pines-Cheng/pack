package com.pack.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pack.define.packConfigDefine;
import com.servlet.getCompanyName;
import com.util.FileOperate;
import com.util.Md5;
import com.util.MyJSON;
import com.util.MyLog;
import com.util.PropertiesOperate;
import com.util.ShellOperate;
import com.util.Zip;

/**
 * android端的打包、删除、重建
 * 
 * @author spider
 * 
 */
public class androidPackServlet extends HttpServlet {

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
		response.setCharacterEncoding("utf-8");

		String selectedCompanyName = request.getSession()
				.getAttribute("selectedCompanyName").toString();

		PrintWriter out = response.getWriter();

		MyJSON js = new MyJSON();

		String command = request.getParameter("command");

		// 打包
		if (command.equals("pack")) {

			// 每次打包前，都先删除一天前的打包临时文件
			deleteAndroidPreviousTempFiles();
			// out.println("pack");
			try {
				MyLog.logger.info("进入pack");
				pack(request, response, js.JSONObject, selectedCompanyName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 保存打包的包
		if (command.equals("save")) {
			String downloadFileURL = request.getParameter("downloadFileURL");
			save(request, response, js.JSONObject, downloadFileURL,
					selectedCompanyName);
		}

		// 删除文件
		if (command.equals("delete")) {
			delete(request, js.JSONObject);

		}

		out.print(js.getJsonString());
		out.flush();
		out.close();
	}

	/**
	 * @param js
	 * @param downloadFileURL
	 * @category 保存
	 * */
	private boolean save(HttpServletRequest request,
			HttpServletResponse response, JSONObject js,
			String downloadFileURL, String selectedCompanyName)
			throws IOException {

		String androidProjectDestPath = request
				.getParameter("androidProjectDestPath");

		// 1.从downloadFileURL解析出zipApkName
		String[] temp = downloadFileURL.split(File.separator);
		String ApkName = temp[temp.length - 1];

		// 以公司名建立历史版本保存文件夹
		String packSaveCompanyPath = new String(getCompanyName.packSaveDir
				+ File.separator + "android" + File.separator
				+ selectedCompanyName);
		FileOperate.ifDirNotExistThenCreate(packSaveCompanyPath);

		// 2.重新生成带时间的zipApkName
		String date = getCurrentTime();
		String zipSaveFileString = new String(packSaveCompanyPath
				+ File.separator + date + "_" + ApkName);

		// 复制
		FileOperate.copyDir(downloadFileURL, zipSaveFileString);

		// 复制完后删除临时文件
		FileOperate.deleteFiles(androidProjectDestPath);
		js.put("status", "SUCCESS");
		js.put("message", "保存历史包成功！");
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param request
	 * @param js
	 */
	private boolean delete(HttpServletRequest request, JSONObject js) {
		String deleteFilePath = request.getParameter("deleteFilePath");
		MyLog.logger.info(deleteFilePath);
		FileOperate.deleteFiles(deleteFilePath);

		js.put("status", "SUCCESS");
		js.put("message", "删除成功！");
		return true;
	}

	/**
	 * @return
	 * @throws Exception
	 * @category 到一个临时文件夹打包
	 * @throws ServletException
	 *             if an error occurs
	 */
	private boolean pack(HttpServletRequest request,
			HttpServletResponse response, JSONObject js,
			String selectedCompanyName) throws Exception {
		// 获取需要的路径
		String date = getCurrentTime();

		// 临时随机文件夹
		String androidProjectDestPath = new String(
				getCompanyName.androidTempPackPath + File.separator + "android"
						+ File.separator + selectedCompanyName + File.separator
						+ date);

		// 获取配置文件的路径
		String configPath = new String(getCompanyName.companysFilePath
				+ File.separator + "android" + File.separator
				+ selectedCompanyName + "/config");

		String imagesPath = new String(getCompanyName.companysFilePath
				+ File.separator + "android" + File.separator
				+ selectedCompanyName + "/Images");

		// 获取android.peroperties的路径
		String androidPropertiesSrcPath = new String(configPath
				+ "/android.properties");
		// 获取config.peroperties的路径
		String configPropertiesSrcPath = new String(configPath
				+ "/config.properties");
		// 获取pic.properties的路径
		String picPropertiesSrcPath = new String(configPath + "/pic.properties");

		String configPropertiesDestPath = new String(androidProjectDestPath
				+ "/assets/config.properties");

		String androidPropertiesDestPath = new String(androidProjectDestPath
				+ "/android.properties");
		String picPropertiesDestPath = new String(androidProjectDestPath
				+ "/pic.properties");

		// 校验文件,文件有误，返回false
		if (!verifyRequestConfigFiles(androidPropertiesSrcPath,
				configPropertiesSrcPath, picPropertiesSrcPath, imagesPath, js)) {

			return false;

		}

		MyLog.logger.info("配置文件齐全！");

		// 1.初始化打包的项目文件夹
		if (!initPackFiles(request, js, androidProjectDestPath,
				androidPropertiesSrcPath, configPropertiesSrcPath,
				picPropertiesSrcPath, configPropertiesDestPath,
				androidPropertiesDestPath, picPropertiesDestPath, imagesPath)) {
			return false;
		}

		String[] shellString = new String[] { "sh",
				androidProjectDestPath + File.separator + "androidPack.sh",
				androidProjectDestPath };

		// 脚本执行日志的URL
		MyLog.logger.info("脚本执行日志的URL");
		String shellOperateLogURL = new String("/pack/temp/android"
				+ File.separator + selectedCompanyName + File.separator
				+ androidProjectDestPath.split("/")[7] + File.separator
				+ "shellOperateLog");
		// apk包名
		String downloadApkName = getDownloadApkName(androidPropertiesDestPath);

		String downloadAPKPath = getDownladApkName(androidProjectDestPath,
				downloadApkName);
		String downloadZipFilePath = downloadAPKPath.replace(".apk", "")
				+ ".zip";

		MyLog.logger.info("sh " + androidProjectDestPath + File.separator
				+ "androidPack.sh" + androidProjectDestPath);

		// 2.执行shell脚本
		ShellOperate.shellOperate(shellString);

		// 安卓端的update文件
		String jsonFilePathString = androidProjectDestPath + File.separator
				+ "update_android.json";

		File downloadFile = new File(downloadAPKPath);
		if (downloadFile.exists()) {

			// 3.将更新内容写入update.json文件
			getAndroidUpdateJson(configPropertiesDestPath,
					androidPropertiesDestPath, downloadApkName,
					downloadAPKPath, jsonFilePathString);
		}

		// 4.将打包的包和update.json文件一同打包成压缩文件
		getAndroidFownloadFile(androidProjectDestPath, downloadApkName,
				downloadAPKPath, downloadZipFilePath, jsonFilePathString);

		js.put("shellOperateLogURL", shellOperateLogURL);
		js.put("downloadFileURL", downloadZipFilePath);
		js.put("androidProjectDestPath", androidProjectDestPath);
		js.put("status", "SUCCESS");
		return true;

	}

	/**
	 * @param androidPropertiesSrcPath
	 * @param configPropertiesSrcPath
	 * @param picPropertiesSrcPath
	 * @param imagesPath
	 * @param js
	 * @return
	 * @throws IOException
	 */
	private boolean verifyRequestConfigFiles(String androidPropertiesSrcPath,
			String configPropertiesSrcPath, String picPropertiesSrcPath,
			String imagesPath, JSONObject js) throws IOException {
		if (!(FileOperate.isFileExist(configPropertiesSrcPath)
				&& FileOperate.isFileExist(androidPropertiesSrcPath) && FileOperate
					.isFileExist(picPropertiesSrcPath))) {

			js.put("status", "ERROR");
			js.put("message", "配置文件缺失！");
			return false;
		}

		for (String imageName : packConfigDefine.ANDROID_IMAGES) {
			String imagPath = imagesPath + File.separator + imageName;
			if (!(new File(imagPath).exists())) {
				js.put("status", "ERROR");
				js.put("message", "配置图片：" + imageName + "不存在！");
				return false;
			}
		}

		return true;
	}

	/**
	 * 准备打包好需要的所有文件，包括项目文件和配置文件
	 * 
	 * @param request
	 * 
	 * @param js
	 * @param androidProjectDestPath
	 * @param androidPropertiesSrcPath
	 * @param configPropertiesSrcPath
	 * @param picPropertiesSrcPath
	 * @param configPropertiesDestPath
	 * @param androidPropertiesDestPath
	 * @param picPropertiesDestPath
	 * @param imagesPath
	 */
	private boolean initPackFiles(HttpServletRequest request, JSONObject js,
			String androidProjectDestPath, String androidPropertiesSrcPath,
			String configPropertiesSrcPath, String picPropertiesSrcPath,
			String configPropertiesDestPath, String androidPropertiesDestPath,
			String picPropertiesDestPath, String imagesPath) {
		// 1.复制打包必须的脚本文件和项目文件
		MyLog.logger.info("复制文件");
		try {

			FileOperate.copyDir(getCompanyName.androidProjectPath,
					androidProjectDestPath);
			FileOperate.copyDir(configPropertiesSrcPath,
					configPropertiesDestPath);
			FileOperate.copyDir(androidPropertiesSrcPath,
					androidPropertiesDestPath);
			FileOperate.copyDir(picPropertiesSrcPath, picPropertiesDestPath);

			// 2.复制平板的图片到 工程/res/drawable-hdpi-2048x1536/imageName
			FileOperate.copyFile(imagesPath + File.separator
					+ packConfigDefine.ANDROID_IMAGES[5],
					androidProjectDestPath + File.separator
							+ "res/drawable-hdpi-2048x1536" + File.separator
							+ packConfigDefine.ANDROID_IMAGES[0]);

			FileOperate.copyFile(imagesPath + File.separator
					+ packConfigDefine.ANDROID_IMAGES[6],
					androidProjectDestPath + File.separator
							+ "res/drawable-hdpi-2048x1536" + File.separator
							+ packConfigDefine.ANDROID_IMAGES[1]);

			// 3.复制脚本
			MyLog.logger.info("复制脚本");
			String shellStringSrcPath = new String(request.getRealPath("/")
					+ File.separator + "androidPack.sh");

			String shellStringDestPath = new String(androidProjectDestPath
					+ File.separator + "androidPack.sh");

			FileOperate.copyDir(shellStringSrcPath, shellStringDestPath);
		} catch (Exception e) {
			js.put("status", "ERROR");
			js.put("message", "复制打包文件失败！");
			return false;
		}
		return true;
	}

	/**
	 * 将打好包和配置文件压缩成一下zip文件
	 * 
	 * @param androidProjectDestPath
	 * @param downloadApkName
	 * @param downloadAPKPath
	 * @param downloadZipFilePath
	 * @param jsonFilePathString
	 * @throws IOException
	 */
	private void getAndroidFownloadFile(String androidProjectDestPath,
			String downloadApkName, String downloadAPKPath,
			String downloadZipFilePath, String jsonFilePathString)
			throws IOException {
		// 1. 将两个文件复制到一个文件夹中
		String tempZipDirString = androidProjectDestPath + File.separator
				+ "pack";
		FileOperate.ifDirNotExistThenCreate(tempZipDirString);
		String destJsonFileString = tempZipDirString + File.separator
				+ "update_android.json";
		String destApkFileString = tempZipDirString + File.separator
				+ downloadApkName;
		FileOperate.copyFile(jsonFilePathString, destJsonFileString);
		FileOperate.copyFile(downloadAPKPath, destApkFileString);
		// 2.压缩
		Zip.zipIt(downloadZipFilePath, tempZipDirString);
	}

	/**
	 * 写入android打包的update.json文件
	 * 
	 * @param configPropertiesDestPath
	 * @param androidPropertiesDestPath
	 * @param downloadApkName
	 * @param downloadAPKPath
	 * @param jsonFilePathString
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void getAndroidUpdateJson(String configPropertiesDestPath,
			String androidPropertiesDestPath, String downloadApkName,
			String downloadAPKPath, String jsonFilePathString)
			throws IOException, FileNotFoundException,
			UnsupportedEncodingException {
		// 生成json更新文件，压缩
		// 将json升级文件配置好
		JSONObject updateJson = new JSONObject();

		updateJson.put("name", downloadApkName);
		updateJson.put("md5", Md5.getMD5(downloadAPKPath));
		updateJson.put("vcode", PropertiesOperate.getProperties("versionCode",
				androidPropertiesDestPath));
		updateJson.put("vname", PropertiesOperate.getProperties("versionName",
				androidPropertiesDestPath));
		updateJson.put(
				"downUrl",
				"http://"
						+ PropertiesOperate.getProperties("serverip",
								configPropertiesDestPath)
						+ ":"
						+ PropertiesOperate.getProperties("serverport",
								configPropertiesDestPath) + "/res/");
		updateJson.put("content", PropertiesOperate.getProperties(
				"androidTextArea", androidPropertiesDestPath));

		// 将json字符串写入文件
		FileOutputStream fos = new FileOutputStream(
				new File(jsonFilePathString));
		fos.write(updateJson.toString().getBytes("utf-8"));
		fos.close();
	}

	/**
	 * 获取下载的应用名
	 * 
	 * @param androidProjectDestPath
	 * @param downloadApkName
	 * @return
	 */
	private String getDownladApkName(String androidProjectDestPath,
			String downloadApkName) {
		String downloadFileURL = new String(androidProjectDestPath
				+ File.separator + "bin" + File.separator + downloadApkName);
		return downloadFileURL;
	}

	private String getDownloadApkName(String androidPropertiesDestPath)
			throws IOException {
		String downloadApkName = new String(PropertiesOperate.getProperties(
				"publishApkName", androidPropertiesDestPath)
				+ "_V"
				+ PropertiesOperate.getProperties("versionName",
						androidPropertiesDestPath) + ".apk");
		return downloadApkName;
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

	private boolean deleteAndroidPreviousTempFiles() {
		String androidTempDirString = getCompanyName.androidTempPackPath
				+ File.separator + "android";

		// 1.获取文件夹下的每个公司信息
		File androidTempDir = new File(androidTempDirString);

		File[] androidTempDirFils = androidTempDir.listFiles();
		// 如果为空
		if (androidTempDirFils == null || androidTempDirFils.length == 0) {
			return false;
		}
		// 获取临时文件下的所有公司文件
		for (File companyFile : androidTempDirFils) {
			String[] tempFilesString = companyFile.list();
			// 如果为空
			if (tempFilesString == null || tempFilesString.length == 0) {
				return false;
			}
			for (String tempFileString : tempFilesString) {
				// 删除公司名目录下一天前的临时文件
				FileOperate.deletePreviousFiles(tempFileString, 1000000);
			}
		}
		return true;

	}
}
