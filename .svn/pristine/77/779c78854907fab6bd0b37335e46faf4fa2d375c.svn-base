/**
 * 
 */
package com.servlet;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

import com.alibaba.fastjson.JSONObject;

/**
 * @author spider
 * 
 */
@WebListener()
public class TestComet4j implements ServletContextListener {

	private static final String CHANNEL = "hello";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		// 单例模式，永远只能初始化一个
		CometContext cc = CometContext.getInstance();
		cc.registChannel(CHANNEL);// 注册一个应用的channel

		Thread helloAppModule = new Thread(new HelloAppModule(),
				"Sender App Module");
		helloAppModule.setDaemon(true);
		helloAppModule.start();

	}

	class HelloAppModule implements Runnable {
		public void run() {

			JSONObject js = new JSONObject();

			int i = 0;

			while (true) {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				CometEngine engine = CometContext.getInstance().getEngine();

				Date now = new Date();

				js.put("memory", Runtime.getRuntime().freeMemory() / 1024);
				js.put("time", i++);

				// 使用sendToAll方法向所有客户端在"hello"通道上发送了一条消息
				engine.sendToAll(CHANNEL, js.toJSONString());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		// TODO Auto-generated method stub

	}

}
