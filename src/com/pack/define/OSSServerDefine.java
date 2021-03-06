/**
 * 
 */
package com.pack.define;

/**
 * @author spider
 * 
 */
public class OSSServerDefine {

	public final static String OSS_IPA_Name = "netdisk.ipa";
	public final static String OSS_PLIST_NAME = "netdisk.plist";

	public final static String OSS_PLIST_TEMPLATE_PATH = "res/file/netdisk.plist";

	public final static String OSS_ZKYH_XSERVER_IPA_URL = "https://oss-cn-hangzhou.aliyuncs.com/zkyh/xserver/#{companyPinyin}/netdisk.ipa";
	public final static String OSS_ZKYH_TEST_IPA_URL = "https://oss-cn-hangzhou.aliyuncs.com/zkyh/test/#{companyPinyin}/netdisk.ipa";

	public final static String XPATH_URL = "/plist/dict/array/dict/array/dict/string[2]";
	public final static String XPATH_COMPANY_NAME = "/plist/dict/array/dict/dict/string[4]";

	public final static String OSS_TEST_DIR_NAME = "test/";
	public final static String OSS_XSERVER_DIR_NAME = "xserver/";

	// 实际操作的bucket
	public final static String OPERATE_BUCKET_NAME = "sctest";

	// 阿里云上传的临时文件
	public final static String OSS_OPERATE_TEMP_FILE = "/tmp/aliyun";

}
