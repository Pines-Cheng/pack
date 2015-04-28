package com.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pack.define.packConfigDefine;
import com.util.MyJSON;
import com.util.MyLog;
import com.util.XML;

/**
 * android端的svn的更新和获取版本信息
 * 
 * @author spider
 * 
 */
public class svnServlet extends HttpServlet {

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

		PrintWriter out = response.getWriter();

		// 创建json对象
		MyJSON js = new MyJSON();

		String command = request.getParameter("command");
		if (command != null) {
			// 获取svn版本信息
			if (command.equals("getSvnInfo")) {

				String[] shellString = new String[] {
						"sh",
						request.getRealPath("/") + File.separator
								+ "svn_info.sh" };
				MyLog.logger.info(shellString[1]);

				try {
					// 执行脚本成功
					if (shellOperate(js.JSONObject, shellString, command)) {
						js.JSONObject.put("status", "SUCCESS");
						js.JSONObject.put("message", "");

						// 通过XML文件获取versionName
						js.JSONObject
								.put("versionCode",
										XML.getVersionCode(getCompanyName.androidProjectPath
												+ File.separator
												+ "AndroidManifest.xml"));
					}
				} catch (Exception e) {

					// e.printStackTrace();

					js.JSONObject.put("status", "ERROR");
					js.JSONObject.put("message", "执行脚本异常");
				}

				// 更新svn版本
			} else if (command.equals("update")) {

				String[] shellString = new String[] { "sh",
						packConfigDefine.UPDATE_ANDROID_SVN_SHELL_SCRIPT_PATH };
				try {
					// 执行脚本
					shellOperate(js.JSONObject, shellString, command);

				} catch (Exception e) {

					// e.printStackTrace();
					js.JSONObject.put("status", "ERROR");
					js.JSONObject.put("message", "执行脚本异常");
				}
			}
		}

		// 转为jsonString并发送
		out.println(js.getJsonpString());

		out.flush();
		out.close();
	}

	/**
	 * @param js
	 * @category 执行shell脚本
	 * @param shellString
	 *            shell语句组成的字符串
	 * @param out
	 *            返回的流
	 * @param command
	 *            执行的命令
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private boolean shellOperate(JSONObject js, String[] shellString,
			String command) throws IOException, Exception {
		Process process = null;

		process = Runtime.getRuntime().exec(shellString);

		// 会阻塞！！！
		int exitValue = process.waitFor();
		if (0 != exitValue) {
			js.put("message", "call shell failed. error code is :" + exitValue);
			return false;
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line = "";

		if (command.equals("getSvnInfo")) {
			while ((line = input.readLine()) != null) {
				// 追加一行
				MyLog.logger.info(line);

				// 通过命令行获取svn版本号

				String[] result = line.split(":");
				MyLog.logger.info("获取svn版本号的脚本执行结果：" + line + "result[0]:"
						+ result[0]);

				if (result[0].equals("Last Changed Rev")) {
					js.put("status", "SUCCESS");
					js.put("versionName", "2.5." + result[1].replace(" ", ""));
					input.close();
					return true;

				}

			}
			js.put("status", "ERROR");
			js.put("message", "脚本执行结果找不到Last Changed Rev");
			return false;

		}
		js.put("status", "SUCCESS");
		return true;

	}
}
