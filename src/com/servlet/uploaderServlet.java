package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;
import com.pack.define.packConfigDefine;
import com.util.FileOperate;
import com.util.MyJSON;
import com.util.MyLog;

/**
 * 上传文件的servlet，三端通用
 * 
 * @author spider
 * 
 */
public class uploaderServlet extends HttpServlet {

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

		PrintWriter out = response.getWriter();

		// 初始化路径
		String selectedCompanyName = request.getSession()
				.getAttribute("selectedCompanyName").toString();
		MyLog.logger.info("selectedCompanyName为：" + selectedCompanyName);

		String platform = request.getParameter("platform");
		// platform参数正确
		if ((platform != null)) {
			if (platform.equals("android") || platform.equals("IOS")
					|| platform.equals("PC")) {

				String saveImagesDirString = new String(
						getCompanyName.companysFilePath + File.separator
								+ platform + File.separator
								+ selectedCompanyName + "/Images");

				MyJSON js = new MyJSON();

				try {
					// 保存上传的文件。
					fileUploaderOperate(request, saveImagesDirString, platform,
							js.JSONObject);

					out.println(js.getJsonString());
					MyLog.logger.info("上传文件成功！");

				} catch (Exception e) {

					// out.println("");
					e.printStackTrace();
					// out.println("error");
					MyLog.logger.info("文件上传失败！");
				}

				// 图片上传之后，更新到库里一次

			}
		}
		out.flush();
		out.close();

	}

	/**
	 * @param platform
	 * @param jSONObject
	 * @throws Exception
	 * @category 接收文件，并保存，三端通用
	 */
	public void fileUploaderOperate(HttpServletRequest request,
			String fileSaveDir, String platform, JSONObject jSONObject)
			throws Exception {

		ServletFileUpload upload = initUploader(request);

		// 从request得到上传的文件列表,并获得其迭代器
		List<FileItem> fileItems;

		fileItems = upload.parseRequest(request);

		// Process the uploaded file items
		Iterator<FileItem> i = fileItems.iterator();

		// 处理文件：写入或者其他操作
		String fileName;
		while (i.hasNext()) {
			FileItem fi = (FileItem) i.next();
			if (fi.isFormField()) {// 当是表单域时，进行的处理

				// Get the uploaded file parameters
				String fieldName = fi.getFieldName();// 得到元素的name属性的值
				String fieldValue = fi.getString("utf-8");// 得到元素的value的值
				fileName = fi.getName();// 这里是表单域，所以得到的是null

			} else {// 当是文件域时，将文件保存到硬盘中

				String contentType = fi.getContentType();
				boolean isInMemory = fi.isInMemory();
				long sizeInBytes = fi.getSize();
				String fieldName = fi.getFieldName();// 得到元素的name属
				fileName = fi.getName();// 取得上传的文件名

				// 校验文件名
				if (!fileUploderVerify(fileName, platform)) {
					jSONObject.put("status", "ERROR");
					jSONObject.put("message", "图片名：" + fileName + "不符合要求！");
					continue;
				}

				MyLog.logger.info(contentType + "  " + isInMemory + "  "
						+ sizeInBytes + "  " + fieldName);

				MyLog.logger.info("获取到需要上传的文件名：" + fileName);

				FileOperate.ifDirNotExistThenCreate(fileSaveDir);

				String uploaderFile = new String(fileSaveDir + File.separator
						+ fileName);

				FileOperate.ifFileExistsThenClear(uploaderFile);

				// 写入的上传文件数据
				fi.write(new File(uploaderFile));

				jSONObject.put("status", "SUCCESS");
				jSONObject.put("message", fileName + "上传成功！");

				MyLog.logger.info("成功上传文件：" + fileSaveDir + File.separator
						+ fileName);

			}
		}
	}

	/**
	 * @param fileName
	 * @return 文件名符合，返回true
	 */
	private boolean fileUploderVerify(String fileName, String platform) {

		String[] fileImages = null;

		if (platform.equals("android")) {
			fileImages = packConfigDefine.ANDROID_IMAGES;
		}
		if (platform.equals("IOS")) {
			fileImages = packConfigDefine.IOS_IMAGES;
		}

		if (platform.equals("PC")) {
			fileImages = packConfigDefine.PC_IMAGES;
		}

		// 有一个符合，返回true
		for (String imageName : fileImages) {
			if (fileName.equals(imageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param fileName
	 * @return
	 */
	private boolean PCFileUploderVerify(String fileName) {
		// 有一个符合，返回true
		for (String imageName : packConfigDefine.PC_IMAGES) {
			if (fileName.equals(imageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @category 校验IOS上传的文件名
	 * @param fileName
	 * @return
	 */
	private boolean IOSFileUploderVerify(String fileName) {

		// 有一个符合，返回true
		for (String imageName : packConfigDefine.IOS_IMAGES) {
			if (fileName.equals(imageName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param fileName
	 * @return
	 * @category 安卓端上传logo校验
	 */
	private boolean androidFileUploderVerify(String fileName) {

		// 有一个符合，返回true
		for (String imageName : packConfigDefine.ANDROID_IMAGES) {
			if (fileName.equals(imageName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @throws Exception
	 * @category 初始化文件上传
	 */
	private ServletFileUpload initUploader(HttpServletRequest request) {

		// 判断request中是否包含有multipart内容
		System.out.println(ServletFileUpload.isMultipartContent(request));

		// 如果有，生成DiskFileItemFactory工厂将进行相关的设置（不知道的情况下也可以不设置）
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(4096);// 设置缓冲区大小，这里是4kb
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(System.getProperty("user.home")));// 设置临时目录
		// factory.setHeaderEncoding("UTF-8");// 设置字符集 防止中文文件名乱码

		// 生成上传ServletFileUpload类，并将DiskFileFactory工厂传给它，并对ServletFileUpload进行配置
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(3 * 1024 * 1024);// 设置上传文件最大3M
		upload.setSizeMax(6 * 1024 * 1024);// 设置请求总文件大小6M
		return upload;
	}

}
