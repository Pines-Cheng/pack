package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.FileOperate;
import com.util.MyJSON;
import com.util.MyLog;

/**
 * 删除配置图片的servlet
 * 
 * @author spider
 * 
 */
public class deleteImagesServlet extends HttpServlet {

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

		PrintWriter out = response.getWriter();

		// 创建json对象，用于返回
		MyJSON js = new MyJSON();

		String platform = request.getParameter("platform");

		// platform参数正确
		if ((platform != null)) {
			if (platform.equals("android") || platform.equals("IOS")
					|| platform.equals("PC")) {

				// 初始化路径
				String savaDir = new String(getCompanyName.companysFilePath
						+ File.separator + platform + File.separator
						+ selectedCompanyName + "/Images");

				FileOperate.deleteFiles(savaDir);
				js.JSONObject.put("status", "SUCCESS");
				js.JSONObject.put("message", "删除公司图片！");

			}
		} else {
			js.JSONObject.put("status", "ERROR");
			js.JSONObject.put("message", "添加的公司名为空！");
		}

		MyLog.logger.info(js.getJsonpString());
		out.println(js.getJsonpString());
		out.flush();
		out.close();

	}

}
