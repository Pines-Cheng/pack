/**
 * 
 */
package com.pack.define;

/**
 * @author spider
 * 
 */
public class packConfigDefine {

	// IOS打包服务器的地址
	public final static String IOS_PACK_HOST = "http://192.168.1.139:8080/iospack";

	// PC端打包服务器的地址
	public final static String PC_PACK_HOST = "";

	// IOS打包的临时文件夹
	public final static String IOS_PACK_TEMP_FILE = "/Users/jiangjack/Desktop/apache-tomcat-6.0.43/webapps/iospack/packTemp";

	// PC打包的临时文件夹
	public final static String PC_PACK_TEMP_FILE = "";

	// android端的打包logo名称
	public static final String[] ANDROID_IMAGES = { "app_logo_bg.png",
			"app_logo_bar.png", "app_logo.png", "app_logo_bar_bg.png",
			"app_logo_s.png", "app_logo_bg_pad.png", "app_logo_bar_pad.png" };

	// IOS端的打包logo名称
	public static final String[] IOS_IMAGES = { "29pt.png", "40pt.png",
			"58pt.png", "72pt.png", "76pt.png", "80pt.png", "120pt.png",
			"152pt.png", "180pt.png", "mycloud_banner.png",
			"mycloud_welcome.png" };

	// PC端的打包logo名称
	public static final String[] PC_IMAGES = { "banner.png", "logo.ico",
			"logo_png.png", "main_menu.bmp", "open.bmp", "title.png" };

	// 安卓svn更新代码的脚本
	public static final String UPDATE_ANDROID_SVN_SHELL_SCRIPT_PATH = "/usr/local/Android/svn/update_android_code.sh";

	// PC端的custom配置文件项
	public static final String[] PC_CUSTOM_CONFIG = { "PRODUCT_NAME",
			"SHORTCUT_NAME", "PRODUCT_PUBLISHER", "DEFAULT_SERVER",
			"DEFAULT_PORT", "SPARE_HOST", "SPARE_PORT", "SKIN_COLOR" };
	// PC端的setting配置文件项
	public static final String[] PC_SETTING_CONFIG = { "SavePassword",
			"AutoRun", "AutoLogin", "AutoOpen", "AutoUpdate", "IsUseSpare",
			"Bridge", "OneKeyGetLog", "About", "LocalOpen", "VisitWeb",
			"ShareManagement", "LockManagement", "GotoRecycle", "Link",
			"MenuLock", "MailTo", "BridgeVisit", "MustUpdate", "Upload",
			"Guide", "FirstLogin", "Download", "UseChAccount", "FixedDrive",
			"UseDokanDrive", "UseCbfsDrive", "CheckNetWork", "FixedHost",
			"UseOutLook", "Tag", "HistoryEnable", "HistoryDownload", "Restore",
			"compare", "proxyStyle", "ProxyIP", "ProxyPort", "ProxyStyle",
			"ShellEnable", "MenuEnable", "OverlayEnable", "Lock", "Admin",
			"Write", "read", "List" };
}
