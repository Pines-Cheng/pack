/**
 * 
 */
package com.pack.thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

import com.aliyun.OSSClientOperate;
import com.aliyun.UploadFileInfo;
import com.util.MyJSON;
import com.util.MyLog;

/**
 * @author spider
 * 
 */
public class UploadFilesToOSSThread implements Runnable {

	// sprivate static UploadFilesToOSSThread uniqueUploadFilesToOSS;

	// 获取文件上传的队列
	private HttpServletRequest request;
	private OSSClientOperate oss;

	// 创建对象
	public UploadFilesToOSSThread(HttpServletRequest request,
			OSSClientOperate oss) {
		this.request = request;
		this.oss = oss;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 如果队列里面有文件，则取出来，上传
		Queue<UploadFileInfo> uploadQueue = (Queue<UploadFileInfo>) request
				.getSession().getAttribute("uploadQueue");
		String channel = (String) request.getSession().getAttribute("channel");
		MyLog.logger.warn("channel");

		while (true) {

			MyJSON js = new MyJSON();

			if (uploadQueue.size() == 0) {

				try {
					// 睡眠1秒
					Thread.sleep(1000);
					MyLog.logger.warn("睡眠了1秒！");

					CometEngine engine = CometContext.getInstance().getEngine();
					js.JSONObject.put("status", "wait");
					engine.sendToAll(channel, js.getJsonString());

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			} else {

				MyLog.logger.warn("进入线程中：队列有文件了！");

				js.JSONObject.put("status", "upload");

				// 使用poll方法，将被移出
				UploadFileInfo temp = uploadQueue.poll();

				// 集合方式遍历，元素不会被移除
				int i = 1;
				ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (UploadFileInfo x : uploadQueue) {

					Map<String, String> uploadFileMap = new HashMap<String, String>();
					uploadFileMap
							.put("filePath",
									x.getFilePath().split(File.separator)[x
											.getFilePath()
											.split(File.separator).length - 1]);
					uploadFileMap.put("percent", "0");
					list.add(uploadFileMap);
				}

				try {

					MyLog.logger.warn("进入线程中：上传文件！" + temp.getFilePath());

					oss.multipartUpload(temp.getBucketName(), temp.getKey(),
							temp.getFilePath(), js, channel, list);
					MyLog.logger.warn("进入线程中：上传完成！" + temp.getFilePath());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// 如果队列里面没有文件，每隔1秒轮询一次

	}
}
