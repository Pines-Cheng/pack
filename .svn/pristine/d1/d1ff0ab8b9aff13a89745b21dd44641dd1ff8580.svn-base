package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellOperate {

	public static void shellOperate(String[] shellString) throws IOException,
			Exception {
		Process process = null;
		// List<String> processList = new ArrayList<String>();
		// ִ�нű�
		// out.println("inshell operate!");
		process = Runtime.getRuntime().exec(shellString);
		// 会阻塞！！！
		// int exitValue = process.waitFor();
		// if (0 != exitValue) {
		// out.println("call shell failed. error code is :" + exitValue);
		// }

		BufferedReader input = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line = "";

		while ((line = input.readLine()) != null) {
			// 追加一行

			MyLog.logger.info(line);
			// out.println(line);
			// out.flush();
			// processList.add(line + "\r\n");
			// out.println(line + "<br>");
		}
		input.close();

	}
}
