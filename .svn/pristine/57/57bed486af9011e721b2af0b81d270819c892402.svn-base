/**
 * 
 */
package com.aliyun;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.util.MyJSON;
import com.util.MyLog;

/**
 * @author spider
 * 
 */
public class OSSClientOperate {

	private String accessKeyId;
	private String accessKeySecret;
	private String endpoint;

	private OSSClient client;
	private List<Bucket> buckets;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		OSSClientOperate oss = new OSSClientOperate(
				OSSClientConfig.ACCESS_KEY_ID,
				OSSClientConfig.ACCESS_KEY_SECRET, OSSClientConfig.END_POINT);

		// oss.printListDirObjects("sctest", "xserver/");
		// oss.deleteDir("sctest", "xserver/zhongrong");
		// System.out.println("删除后！");
		// oss.printListDirObjects("sctest", "xserver/");

		// oss.copyDir("zkyh", "xserver/zhongrong", "sctest",
		// "xserver/zhongrong");
		// 列出prefix中下的文件和文件夹
		// oss.printFilesAndDir("sctest", "/", "xserver/");

		// 列出子目录
		// oss.getSubDir("sctest", "/", "");

		// oss.printFilesAndDir("sctest", "/", "spider/");

		// try {
		// oss.putObject("sctest", "spider/log.html", "WebRoot/home.html");
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println(oss.isObjectExsit("sctest",
		// "test/jinshanwangpan/netdisk.ipa"));

		// try {
		// oss.putObject("sctest", "test/test.zip", "E:/test.zip");
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try {
			oss.multipartUpload("sctest", "test/test.zip", "F:/test.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 构造函数，初始化参数
	 * 
	 * @param accessKeyId
	 *            账号
	 * @param accessKeySecret
	 *            密码
	 * @param endpoint
	 *            地址
	 */
	public OSSClientOperate(String accessKeyId, String accessKeySecret,
			String endpoint) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.endpoint = endpoint;
		this.client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		this.buckets = client.listBuckets();
	}

	/**
	 * @category 打印列出所有的object
	 * 
	 * @param bucketName
	 */
	public void printListObjects(String bucketName) {

		// 获取指定bucket下的所有Object信息
		ObjectListing listing = client.listObjects(bucketName);

		// 遍历所有Object
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			System.out.println(objectSummary.getKey());
		}

	}

	/**
	 * 注意：上传会直接覆盖已有的文件
	 * 
	 * @category 上传文件
	 * 
	 * @param bucketName
	 * @param key
	 *            上传的路径
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public boolean putObject(String bucketName, String key, String filePath)
			throws FileNotFoundException {

		// 获取指定文件的输入流
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("文件不存在！");
			return false;
		}
		InputStream content = new FileInputStream(file);

		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();

		// 必须设置ContentLength
		meta.setContentLength(file.length());

		// 上传Object.
		PutObjectResult result = client.putObject(bucketName, key, content,
				meta);

		// 打印ETag
		System.out.println(result.getETag());

		return true;
	}

	/**
	 * @category 通过bucket和key获取object写入文件
	 * @param filePath
	 * @param bucketName
	 * @param key
	 * @throws IOException
	 */
	public void getObject(String filePath, String bucketName, String key)
			throws IOException {

		// 获取Object，返回结果为OSSObject对象
		OSSObject object = client.getObject(bucketName, key);
		// 获取Object的输入流
		InputStream objectContent = object.getObjectContent();
		// 处理Object
		AliyunTools.writeFile(filePath, objectContent);

		// 关闭流
		objectContent.close();
	}

	/**
	 * @category 创建模拟文件夹
	 * @param bucketName
	 * @param dir
	 *            目录名+"/"
	 * @throws IOException
	 */
	public void createSimulateDir(String bucketName, String dir)
			throws IOException {

		ObjectMetadata objectMeta = new ObjectMetadata();
		/*
		 * 这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,dataStream仍然可以有数据
		 */
		byte[] buffer = new byte[0];
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		objectMeta.setContentLength(0);
		try {
			client.putObject(bucketName, dir, in, objectMeta);
		} finally {
			in.close();
		}
	}

	/**
	 * @category 打印出prefix目录下的所有object
	 * @param bucketName
	 * @param prefix
	 */
	public void printListDirObjects(String bucketName, String prefix) {
		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				bucketName);

		// 递归列出fun目录下的所有文件
		listObjectsRequest.setPrefix(prefix);

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有Object
		System.out.println("Objects:");
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			System.out.println(objectSummary.getKey());
		}

		// 遍历所有CommonPrefix
		System.out.println("\nCommonPrefixs:");
		for (String commonPrefix : listing.getCommonPrefixes()) {
			System.out.println(commonPrefix);
		}
	}

	/**
	 * @category 分别列出prefix下的文件和文件夹,无法列出子文件夹下的文件
	 * @param bucketName
	 * @param delimiter
	 * @param prefix
	 */
	public Set<String> printFilesAndDir(String bucketName, String delimiter,
			String prefix) {

		Set<String> objects = new HashSet<String>();

		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				bucketName);

		// "/" 为文件夹的分隔符
		listObjectsRequest.setDelimiter(delimiter);

		// 列出fun目录下的所有文件和文件夹
		listObjectsRequest.setPrefix(prefix);

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有Object
		System.out.println("Objects:");
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			System.out.println(objectSummary.getKey());
			objects.add(objectSummary.getKey());
		}

		// 遍历所有CommonPrefix
		System.out.println("\nCommonPrefixs:");
		for (String commonPrefix : listing.getCommonPrefixes()) {
			System.out.println(commonPrefix);
			objects.add(commonPrefix);
		}
		return objects;
	}

	/**
	 * @category copy一个object
	 * @param srcBucketName
	 * @param srcKey
	 * @param destBucketName
	 * @param destKey
	 * 
	 *            使用该方法copy的object必须小于1G，否则会报错
	 */
	public void copyObject(String srcBucketName, String srcKey,
			String destBucketName, String destKey) {

		// 拷贝Object
		CopyObjectResult result = client.copyObject(srcBucketName, srcKey,
				destBucketName, destKey);

		// 打印结果
		System.out.println("ETag: " + result.getETag() + " LastModified: "
				+ result.getLastModified());
	}

	/**
	 * @category 复制Dir
	 * @param srcBucketName
	 * @param srcPrefix
	 * @param destBucketName
	 * @param destPrefix
	 */
	public void copyDir(String srcBucketName, String srcPrefix,
			String destBucketName, String destPrefix) {

		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				srcBucketName);

		// 递归列出src目录下的所有文件
		listObjectsRequest.setPrefix(srcPrefix);

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有Object
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			// 1.递遍历获取源key
			String srcKey = objectSummary.getKey();
			System.out.println("srcKey:" + srcKey);
			// 2.通过替换srcprefix获取目标key
			String destKey = srcKey.replaceFirst(srcPrefix, destPrefix);
			System.out.println("destKey:" + destKey);
			// 3.copyObject
			copyObject(srcBucketName, srcKey, destBucketName, destKey);
		}

	}

	/**
	 * @category 删除bucketName下的prefix的文件夹下所有内容
	 * @param bucketName
	 * @param prefix
	 */
	public void deleteDir(String bucketName, String prefix) {
		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				bucketName);

		// 递归列出fun目录下的所有文件
		listObjectsRequest.setPrefix(prefix);

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有Object

		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			client.deleteObject(bucketName, objectSummary.getKey());
			System.out.println("delete Object:" + objectSummary.getKey());
		}
		// 最后删除文件夹自己
		client.deleteObject(bucketName, prefix);
	}

	/**
	 * @category 获取子文件夹的名字
	 * @param bucketName
	 * @param delimiter
	 * @param prefix
	 * @return
	 */
	public Set<String> getSubDir(String bucketName, String delimiter,
			String prefix) {

		Set<String> subDirs = new HashSet<String>();

		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				bucketName);

		// "/" 为文件夹的分隔符
		listObjectsRequest.setDelimiter(delimiter);

		// 列出fun目录下的所有文件和文件夹
		listObjectsRequest.setPrefix(prefix);

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有CommonPrefix
		System.out.println("\nCommonPrefixs:");
		for (String commonPrefix : listing.getCommonPrefixes()) {
			// System.out.println(commonPrefix);
			// 分割
			String[] dirs = commonPrefix.split("/");

			// 取最后一个
			String companyPinyin = dirs[dirs.length - 1];
			System.out.println(companyPinyin);
			// 添加进Set
			subDirs.add(companyPinyin);

		}
		return subDirs;

	}

	/**
	 * 根据key判断是文件夹或者目录是否存在（object是否存在！）
	 * 
	 * @param bucketName
	 * @param key
	 * @return 存在，返回true，不存在，返回false
	 */
	public boolean isObjectExsit(String bucketName, String key) {

		Set<String> objects = printFilesAndDir(bucketName, "/", key);

		Iterator<String> i = objects.iterator();
		while (i.hasNext()) {
			// 如果相等
			if (i.next().equals(key)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 文件分块上传
	 * 
	 * @param bucketName
	 * @param key
	 * @throws IOException
	 */
	public boolean multipartUpload(String bucketName, String key,
			String filePath) throws IOException {

		int uploadPercent = 0;

		if (!(new File(filePath).exists())) {
			System.out.println("文件不存在！");
			return false;
		}

		// 开始Multipart Upload
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(
				bucketName, key);
		InitiateMultipartUploadResult initiateMultipartUploadResult = client
				.initiateMultipartUpload(initiateMultipartUploadRequest);

		// 打印UploadId
		System.out.println("UploadId: "
				+ initiateMultipartUploadResult.getUploadId());

		// 设置每块为 4K
		final int partSize = 1024 * 128;
		File partFile = new File(filePath);
		// 计算分块数目
		int partCount = (int) (partFile.length() / partSize);
		if (partFile.length() % partSize != 0) {
			partCount++;
		}
		// 新建一个List保存每个分块上传后的ETag和PartNumber
		List<PartETag> partETags = new ArrayList<PartETag>();

		for (int i = 0; i < partCount; i++) {

			if ((i * 100 / partCount) > uploadPercent) {
				uploadPercent = i * 100 / partCount;
				System.out.println(uploadPercent);
			}
			// 获取文件流
			FileInputStream fis = new FileInputStream(partFile);
			// 跳到每个分块的开头
			long skipBytes = partSize * i;
			fis.skip(skipBytes);
			// 计算每个分块的大小
			long size = partSize < partFile.length() - skipBytes ? partSize
					: partFile.length() - skipBytes;
			// 创建UploadPartRequest，上传分块
			UploadPartRequest uploadPartRequest = new UploadPartRequest();
			uploadPartRequest.setBucketName(bucketName);
			uploadPartRequest.setKey(key);
			uploadPartRequest.setUploadId(initiateMultipartUploadResult
					.getUploadId());
			uploadPartRequest.setInputStream(fis);
			uploadPartRequest.setPartSize(size);
			uploadPartRequest.setPartNumber(i + 1);
			UploadPartResult uploadPartResult = client
					.uploadPart(uploadPartRequest);
			// 将返回的PartETag保存到List中。
			partETags.add(uploadPartResult.getPartETag());
			// 关闭文件
			fis.close();
		}

		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
				bucketName, key, initiateMultipartUploadResult.getUploadId(),
				partETags);

		// 完成分块上传
		CompleteMultipartUploadResult completeMultipartUploadResult = client
				.completeMultipartUpload(completeMultipartUploadRequest);

		// 打印Object的ETag
		System.out.println(completeMultipartUploadResult.getETag());

		return true;

	}

	/**
	 * 文件分块上传
	 * 
	 * @param bucketName
	 * @param key
	 * @param request
	 * @throws IOException
	 */
	public boolean multipartUpload(String bucketName, String key,
			String filePath, MyJSON js, String channel,
			ArrayList<Map<String, String>> list) throws IOException {

		int uploadPercent = 0;

		if (!(new File(filePath).exists())) {
			System.out.println("文件不存在！");
			return false;
		}

		// 开始Multipart Upload
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(
				bucketName, key);
		InitiateMultipartUploadResult initiateMultipartUploadResult = client
				.initiateMultipartUpload(initiateMultipartUploadRequest);

		// 打印UploadId
		// System.out.println("UploadId: "
		// + initiateMultipartUploadResult.getUploadId());

		// 设置每块为 4K
		final int partSize = 1024 * 128;
		File partFile = new File(filePath);
		// 计算分块数目
		int partCount = (int) (partFile.length() / partSize);
		if (partFile.length() % partSize != 0) {
			partCount++;
		}
		// 新建一个List保存每个分块上传后的ETag和PartNumber
		List<PartETag> partETags = new ArrayList<PartETag>();

		// 已完成的百分比
		for (int i = 0; i < partCount; i++) {
			if ((i * 100 / partCount) > uploadPercent) {
				uploadPercent = i * 100 / partCount;
				System.out.println(uploadPercent);

				Map<String, String> uploadFileMap = new HashMap<String, String>();
				uploadFileMap.put("filePath",
						filePath.split(File.separator)[filePath
								.split(File.separator).length - 1]);
				uploadFileMap.put("percent", String.valueOf(uploadPercent));

				list.add(uploadFileMap);
				js.JSONObject.put("message", list);

				CometEngine engine = CometContext.getInstance().getEngine();
				MyLog.logger.warn("发送json前：" + js.getJsonString());
				MyLog.logger.warn("  channel:" + channel);

				engine.sendToAll(channel, js.getJsonString());
				MyLog.logger.warn("发送json后：" + js.getJsonString());

				list.remove(list.size() - 1);

			}
			// 获取文件流
			FileInputStream fis = new FileInputStream(partFile);
			// 跳到每个分块的开头
			long skipBytes = partSize * i;
			fis.skip(skipBytes);
			// 计算每个分块的大小
			long size = partSize < partFile.length() - skipBytes ? partSize
					: partFile.length() - skipBytes;
			// 创建UploadPartRequest，上传分块
			UploadPartRequest uploadPartRequest = new UploadPartRequest();
			uploadPartRequest.setBucketName(bucketName);
			uploadPartRequest.setKey(key);
			uploadPartRequest.setUploadId(initiateMultipartUploadResult
					.getUploadId());
			uploadPartRequest.setInputStream(fis);
			uploadPartRequest.setPartSize(size);
			uploadPartRequest.setPartNumber(i + 1);
			UploadPartResult uploadPartResult = client
					.uploadPart(uploadPartRequest);
			// 将返回的PartETag保存到List中。
			partETags.add(uploadPartResult.getPartETag());
			// 关闭文件
			fis.close();
		}

		MyLog.logger.warn("完成上传0");
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
				bucketName, key, initiateMultipartUploadResult.getUploadId(),
				partETags);

		MyLog.logger.warn("完成上传1");
		// 完成分块上传
		CompleteMultipartUploadResult completeMultipartUploadResult = client
				.completeMultipartUpload(completeMultipartUploadRequest);

		MyLog.logger.warn("完成上传2");
		// 打印Object的ETag
		System.out.println(completeMultipartUploadResult.getETag());

		return true;

	}
}
