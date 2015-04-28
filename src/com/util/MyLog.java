package com.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Logger;

public class MyLog {

	public static Logger logger;
	public static HTMLLayout layout;
	// 设置日志输出端
	public static FileAppender fileAppender = null;
	public static ConsoleAppender consoleAppender = null;

	public static void main(String[] args) throws IOException {
		MyLog.LogInit("log.txt");
		MyLog.logger.info("hello");

	}

	public static void LogInit(String filepath) throws IOException {
		File file = new File(filepath);

		if (file.exists()) {
			System.out.println("文件存在！");
		} else {
			file.createNewFile();
		}
		// 单例模式
		if (logger == null && layout == null) {
			init(filepath);
		}
	}

	private static void init(String filepath) {
		logger = Logger.getLogger(MyLog.class);
		// 设置日志输出格式
		// layout = new SimpleLayout();
		layout = new HTMLLayout();

		try {
			fileAppender = new FileAppender(layout, filepath, false);
			consoleAppender = new ConsoleAppender(layout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 添加appender
		logger.addAppender(fileAppender);
		logger.addAppender(consoleAppender);
		// 使用默认的配置信息，不需要写log4j.properties
		// BasicConfigurator.configure();
		// 设置日志输出级别为info，这将覆盖配置文件中设置的级别
		// logger.setLevel(Level.INFO);
	}

}
