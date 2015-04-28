/**
 * 
 */
package com.pack.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.pack.define.packConfigDefine;
import com.servlet.getCompanyName;
import com.util.FileOperate;
import com.util.MyJSON;
import com.util.MyLog;
import com.util.Zip;

/**
 * @author spider
 * 
 */
public class PCPackServlet extends HttpServlet {

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

		String selectedCompanyName = request.getSession()
				.getAttribute("selectedCompanyName").toString();

		PrintWriter out = response.getWriter();

		String command = request.getParameter("command");

		// 创建返回的json对象
		MyJSON js = new MyJSON();

		// 打PC包
		if (command.equals("pack")) {
			pack(js.JSONObject, selectedCompanyName);
		}

		out.println(js.getJsonpString());
		out.flush();
		out.close();
	}

	/**
	 * @param jSONObject
	 * @param selectedCompanyName
	 * @throws IOException
	 */
	private void pack(JSONObject js, String selectedCompanyName)
			throws IOException {
		// TODO Auto-generated method stub
		// 1.获取配置文件夹绝对路径，初始化目录
		String PCConfigDirString = new String(getCompanyName.companysFilePath
				+ File.separator + "PC" + File.separator + selectedCompanyName);

		String imagesPath = PCConfigDirString + File.separator + "Images";
		String configPath = PCConfigDirString + File.separator + "config";

		String PackZipFileString = new String(
				getCompanyName.tomcatCompanysFilePath + File.separator + "zip"
						+ File.separator + "PC" + File.separator
						+ selectedCompanyName + ".zip");

		// 将打包时间作为临时文件夹的名字
		String date = getCurrentTime();

		// URL根据自己部署的主机地址补齐
		String url = packConfigDefine.PC_PACK_HOST
				+ "/PCPack?selectedCompanyName=" + selectedCompanyName
				+ "&date=" + date;

		if (!FileOperate
				.ifDirNotExistThenCreate(getCompanyName.tomcatCompanysFilePath
						+ File.separator + "zip" + File.separator + "PC")) {
			js.put("status", "ERROR");
			js.put("message", "创建zip目录失败！");
			return;
		}

		MyLog.logger.info(PackZipFileString);
		MyLog.logger.info(PCConfigDirString);

		// 2.判断配置文件是否齐全,如果为false，返回
		if (!verifyRequiredConfigFiles(configPath, imagesPath, js)) {
			return;
		}

		// 3.压缩
		Zip.zipIt(PackZipFileString, PCConfigDirString);
		File zipFile = new File(PackZipFileString);

		// 4.发送
		CloseableHttpClient httpclient = HttpClients.createDefault();
		FileEntity entity = new FileEntity(zipFile, ContentType.create(
				"text/plain", "UTF-8"));
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);

		// 5.获取response
		CloseableHttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httppost);
		} catch (Exception e) {
			js.put("status", "ERROR");
			js.put("message", "IOS打包服务器未启动！");
			return;
		}

		// 6.对response处理
		responseOperate(js, selectedCompanyName, date, httppost, httpResponse);
	}

	/**
	 * 处理response
	 * 
	 * @param js
	 * @param selectedCompanyName
	 * @param date
	 *            时间，创建文件名
	 * @param httppost
	 * @param httpResponse
	 * @throws IOException
	 * @throws ParseException
	 */
	private void responseOperate(JSONObject js, String selectedCompanyName,
			String date, HttpPost httppost, CloseableHttpResponse httpResponse)
			throws IOException, ParseException {

		String shellOperateLogPath = new String(packConfigDefine.PC_PACK_HOST
				+ "/packTemp/" + date + "/shellOperateLog");
		// 打包临时文件夹的路径
		String IOSpackTempFileString = new String(
				packConfigDefine.PC_PACK_TEMP_FILE + File.separator + date);

		StatusLine statusLine = httpResponse.getStatusLine();
		// 请求成功
		if (statusLine.getStatusCode() == 200) {
			HttpEntity responseEntity = httpResponse.getEntity();

			// 直接打印出来
			if (responseEntity != null) {
				long len = responseEntity.getContentLength();
				if (len != -1 && len < 1048) {
					MyLog.logger.info(EntityUtils.toString(responseEntity));
				} else {
					// 将获取的包保存在历史里面
					String apkTomcatSaveDir = new String(
							getCompanyName.packSaveDir + File.separator + "PC"
									+ File.separator + selectedCompanyName);
					String exeTomcatSavePathString = new String(
							apkTomcatSaveDir + File.separator + date + "_"
									+ selectedCompanyName + ".exe");
					try {
						FileOperate.ifDirNotExistThenCreate(apkTomcatSaveDir);
						FileOperate
								.ifFileNotExistThenCreate(exeTomcatSavePathString);
					} catch (Exception e1) {
						js.put("status", "ERROR");
						js.put("message", "创建目录失败！");
						return;
					}

					try {

						// 将接收的文件写入

						InputStream is = responseEntity.getContent();
						FileOutputStream fos = new FileOutputStream(new File(
								exeTomcatSavePathString));

						byte[] buffer = new byte[1024];
						int readNum;
						while ((readNum = is.read(buffer)) != -1) {
							fos.write(buffer, 0, readNum);
						}
						fos.close();
						httppost.abort();

					} catch (Exception e) {
						js.put("status", "ERROR");
						js.put("message", "读取返回的应用文件失败！");
						return;

					}
					// 打包成功后返回数据
					js.put("status", "SUCCESS");
					js.put("message", "打包成功！");
					js.put("IOSDownloadFilePath", exeTomcatSavePathString);
					js.put("shellOperateLogPath", shellOperateLogPath);
					js.put("IOSPackTempFileString", IOSpackTempFileString);

				}

			} else {
				js.put("status", "ERROR");
				js.put("message", "实体为空！");
				return;
			}
		} else {
			js.put("status", "ERROR");
			js.put("message", "请求失败！");
			return;
		}
	}

	/**
	 * @param IOSConfigFilesString
	 * @param imagesPath
	 * @param js
	 * @return
	 */
	private boolean verifyRequiredConfigFiles(String PCConfigDirString,
			String imagesPath, JSONObject js) {

		// 获取选中的公司的config1文件路径
		String companyCustomPropertiesFilePath = PCConfigDirString
				+ File.separator + "custom.properties";
		String companySettingPropertiesFilePath = PCConfigDirString
				+ File.separator + "setting.properties";

		if (!(new File(companyCustomPropertiesFilePath)).exists()) {
			js.put("status", "ERROR");
			js.put("message", "配置文件：" + companyCustomPropertiesFilePath
					+ "不存在！");
			return false;
		}

		if (!(new File(companySettingPropertiesFilePath)).exists()) {
			js.put("status", "ERROR");
			js.put("message", "配置文件：" + companySettingPropertiesFilePath
					+ "不存在！");
			return false;
		}

		for (String imageName : packConfigDefine.PC_IMAGES) {
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
	 * @return
	 */
	private String getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = sdf.format(d);
		return currentTime;
	}

}
