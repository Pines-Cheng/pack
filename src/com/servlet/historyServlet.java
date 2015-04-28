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
 * 历史打包版本的下载、删除，三端通用
 * 
 * @author spider
 * 
 */
public class historyServlet extends HttpServlet {

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

		String command = request.getParameter("command");
		String platform = request.getParameter("platform");

		if (platform != null) {

			// 获取历史版本信息
			if (command == null) {

				PrintWriter out = response.getWriter();

				MyJSON js = new MyJSON();

				// 返回请求平台的历史信息
				js.JSONObject.put(
						platform,
						getPackHistoryFiles(request, selectedCompanyName,
								platform));

				MyLog.logger.info(js.getJsonpString());
				out.println(js.getJsonpString());

				out.flush();
				out.close();

			} else {

				// 获取返回的接送字符串
				MyJSON js = new MyJSON();

				String fileName = request.getParameter("fileName");
				if (fileName.isEmpty()) {
					js.JSONObject.put("status", "ERROR");
					js.JSONObject.put("message", "参数fileName为空");

				}

				// 获取要下载或删除的文件的绝对路径
				String controlFilePath = new String(getCompanyName.packSaveDir
						+ File.separator + platform + File.separator
						+ selectedCompanyName + File.separator + fileName);

				// 下载
				if (command.equals("download")) {

					String url = new String("downloadServlet?filePath="
							+ controlFilePath);

					MyLog.logger.info("下载URL：" + url);

					// 转发到下载的servlet
					request.getRequestDispatcher(url)
							.forward(request, response);

				}

				// 删除
				if (command.equals("delete")) {

					PrintWriter out = response.getWriter();

					FileOperate.deleteFiles(controlFilePath);

					js.JSONObject.put("status", "SUCCESS");
					js.JSONObject
							.put("message", "删除" + controlFilePath + "成功！");

					out.print(js.getJsonString());
					out.flush();
					out.close();

				}
			}
		}

	}

	/**
	 * 获取打包的历史文件。三端通用
	 * 
	 * @param out
	 * @param SelectedCompanyName
	 * @param tomcatHistoryCompanyDir
	 * @throws IOException
	 */
	private String[] getPackHistoryFiles(HttpServletRequest request,
			String SelectedCompanyName, String platform) throws IOException {

		// 库里的文件
		String packSaveCompanyDir = new String(getCompanyName.packSaveDir
				+ File.separator + platform + File.separator
				+ SelectedCompanyName);

		FileOperate.ifDirNotExistThenCreate(packSaveCompanyDir);
		File packSaveCompanyFile = new File(packSaveCompanyDir);

		// 列出历史目录下文件名
		String[] filesName = packSaveCompanyFile.list();
		// 返回json
		return filesName;

	}

}
