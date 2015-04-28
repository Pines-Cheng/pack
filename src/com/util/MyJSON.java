package com.util;

import java.io.PrintWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MyJSON {

	public JSONObject JSONObject;

	public static void main(String[] args) {

	}

	public MyJSON() {
		this.JSONObject = new JSONObject();
	}

	/**
	 * @category 返回jsonp字符串，配合AJAX进行跨域的请求
	 * @return 跨域的字符串
	 */
	public String getJsonpString() {
		String jsonString = JSON.toJSONString(this.JSONObject);
		jsonString = "callback(" + jsonString + ");";
		return jsonString;
	}

	/**
	 * @category 返回json字符串，
	 * @return 普通的json字符串
	 */
	public String getJsonString() {
		String jsonString = JSON.toJSONString(this.JSONObject);
		return jsonString;
	}

	/**
	 * @param status
	 * @param message
	 * @param out
	 * @param js
	 */
	public void putAndReturn(String status, String message, PrintWriter out,
			MyJSON js) {
		js.JSONObject.put("status", status);
		js.JSONObject.put("message", message);
		out.print(js.getJsonString());
		out.flush();
		out.close();

	}

}
