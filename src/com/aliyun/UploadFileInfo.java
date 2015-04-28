/**
 * 
 */
package com.aliyun;

/**
 * @author spider
 * 
 */
public class UploadFileInfo {

	private String bucketName;
	private String key;
	private String filePath;

	public UploadFileInfo(String bucketName, String key, String filePath) {
		this.bucketName = bucketName;
		this.key = key;
		this.filePath = filePath;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
