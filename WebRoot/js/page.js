/*
 * @本文件是一个封装的函数库。
 */

// 在页面加载完成之后，先执行初始化页面的事件
$(document).ready(function() {
	init_page();
});

// 页面初始化函数
function init_page() {
	// window.alert("in");
	// window.alert($(window).height()); 631
	// window.alert($(document).height()); 665

	var height = $(window).height();
	var width = $(window).width();

	// window.alert(width-400);

	// 左面板
	$(".layout-left").height(height);
	// 中间的面板
	$(".layout_mid").height(height);
	// 主面板
	$(".layout_main").height(height).width(width - 520);
	// 公司名的浮动面板
	$("#company_panel").height(height - 400);
	// 取消整个页面的滚动条
	$("body").css('overflow', 'hidden');
	// 将添加公司的块隐藏起来
	$("#edit_company").css('display', 'none');

	// 绑定缩放的事件，无论页面怎么缩放，都不会影响整体的效果！！
	// 错误，会执行AJAX好多次！！
	$(window).resize(function(event) {
		/* Act on the event */
		// init_page();
	});

	// 公司名未选中，收起并显示公司名未选择
	check_company_select();

	// 公司的设置显示及隐藏
	$("#company_name_select").on('click', function(event) {
		$("#company_operate").slideToggle(100);
		// alert(event);
	});

	// 点击添加按钮，当前公司添加块出现的事件
	$("#add_button").click(function(event) {
		/* Act on the event */
		$("#edit_company").slideDown(100);
		$("#edit_company").attr('command', 'add');
	});

	$("#edit_button").click(function(event) {
		/* Act on the event */
		$("#edit_company").slideDown(100);
		// 保存命令
		$("#edit_company").attr('command', 'edit');
		// 将选中公司的名字填入输入框
		$("#edit_input").val($("body").attr("selectCompanyName"));
	});

	// 单击勾或者叉按钮，添加块隐藏
	$("#confirm_button").click(function(event) {

		// 1.input表单提交
		// $("#companyEditInput").submit();

		// 2.取body上的companyEditInput值验证
		if (!$('#companyEditInput').validationEngine('validate')) {
			return;
		}

		if ($("#edit_input").val() == null || $("#edit_input").val() == "") {
			$("#companyNameInputTips").html("不能为空！");
		} else {

			var url = "/pack/companyNameOperateServlet?command=" + $("#edit_company").attr('command') + "&companyName=" + $("#edit_input").val();
			$.ajax({
				url: url,
				dataType: "json",
				success: function(result) {
					// alert($.trim(result) == "ERROR");

					if ($.trim(result.status) == "ERROR") {
						$("#companyNameInputTips").html("公司名已存在！");
					} else {
						$("#edit_company").slideUp(100);
						// 初始化时自动从服务器加载公司名列表
						getCompanyList();
						// 清空值
						$("#edit_input").val("");
						$("#companyNameInputTips").val("");
					}
				}
			}).done(function(data) {
				console.log("success");

			}).fail(function() {
				console.log("error");
			}).always(function() {
				console.log("complete");
			});

		}

	});

	$("#cancel_button").click(function(event) {
		/* Act on the event */
		$("#edit_company").slideUp(100);
	});

	// 公司名操作按钮显示
	$("#delete_button,#edit_button").slideUp(100);

	// 初始化时自动从服务器加载公司名列表
	getCompanyList();
	console.log("reload company list");

}

// 默认选中一家公司
// $("#海尔").attr("checked","true");
// $("#中石油").attr("checked","true");

// 检查公司是否选中，并显示出来
function check_company_select() {

	var company_list = document.getElementsByName("company_name");
	var result = false;
	for (var i = 0; i < company_list.length; i++) {

		// 如果被选中
		if (company_list[i].checked) {
			// 改变按钮颜色
			$(company_list[i]).next().children().css('background-color',
				'#801414');

			result = true;
			var element = $(company_list[i]).next("div");
			// var test = document.getElementById("company_name_select");
			// alert(test);
			var test = $("#company_name_select").html();
			// alert(test);
			// 设置值，并放下
			$("#company_name_select").html(company_list[i].value);
			// 显示设置
			$("#company_operate").slideDown(100);
			// 公司名操作按钮显示
			$("#delete_button,#edit_button,#add_button").slideDown(100);

			// **************发送ajax请求图片，并在页面上显示。************************
			var url = encodeURI("/pack/getCompanyInfo?selectedCompany=" + company_list[i].value);
			getCompanyImages(url);

			// 将选中的文件名保存在页面body中
			$("body").attr('selectCompanyName', company_list[i].value);

			// 获取选中公司打包的历史信息
			changePackHistoryPlatform();

		} else {
			// 恢复按钮颜色
			$(company_list[i]).next().children().css('background-color',
				'#337AB7');
		}

		// changePackHistoryPlatform();
	}
	if (result == false) {
		$("#company_name_select").html("未选择！");
		$("#company_operate").slideUp(100);

	}
}

// Ajax请求,刷新公司名的列表
function getCompanyList() {
	var url = "/pack/getCompanyName";

	$("#company_button").empty();

	$.ajax({
		type: "get",
		async: false,
		url: url,
		dataType: "jsonp",
		jsonp: "callback", // 传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		jsonpCallback: "callback", // 自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
		success: function(result) {

			for (var i in result.companyNames) {
				// alert(i+":"+result.keys[i]);//循环输出a:1,b:2,etc.
				var companyName = result.companyNames[i];
				$("#company_button")
					.append(' <input type="radio" style="display:inline" name="company_name" value="' + companyName + '" id="' + companyName + '"></input><label  class="" style="" for="' + companyName + '"><div>' + companyName + '</div></label><br> ');
			}
			// 重新绑定点击事件
			$("#company_button>input").click(function(event) {

				// 修改当前公司，并显示公司名
				check_company_select();
			});
		},
		error: function() {
			alert('服务器连接失败，请联系管理员！');
		}
	});

}

// 选中公司后，发送选中的公司名，Ajax请求公司文件夹图片路径
// 同时作用于主页和配置文件修改页
function getCompanyImages(url) {
	$.ajax({
		type: "get",
		async: false,
		url: url,
		dataType: "jsonp",
		jsonp: "callback", // 传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		jsonpCallback: "callback", // 自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据

		// 成功之后，返回图片的名称及绝对路径
		success: function(result) {

			// 更新安卓部分的公司信息（主页面和配置修改页面）
			updateAndroidCompanyInfo(result);
			updateIOSCompanyInfo(result);
			updatePCCompanyInfo(result);

			// 阿里云的消息处理
			aliyunUploadMessageOperate(result.channel);

			$(".fancybox").fancybox({
				openEffect: 'none',
				closeEffect: 'none'
			});

		},
		error: function() {
			alert('服务器连接失败，请联系管理员！');

		}
	});

};

function aliyunUploadMessageOperate(channel) {
	console.log("channel:" + channel);

	JS.Engine.on(channel, function(recevied) {

		var js = JSON.parse(recevied);

		console.log("第二个：" + recevied);

		if (js.status == "upload") {

			$("#aliyunUploadMessage").empty();
			for (var num in js.message) {
				console.log(num);

				var filePath = js.message[num].filePath;
				var percent = js.message[num].percent;

				$("#aliyunUploadMessage")
					.append("<div>" + filePath + "：</div><div class=\"progress\" ><div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"" + percent + " \"aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: " + percent + "%;\">" + percent + "%</div></div>");

			}

		} else {
			$("#aliyunUploadMessage").empty();
		}
	});
	JS.Engine.start('conn');

}

// 在主页和配置页更新配置信息
function updateAndroidCompanyInfo(result) {
	// 同时在主页和配置页添加logo的缩略图！！
	$("#android_logo,#android_fileList").empty();
	$("#android_config_file").empty();

	if ($.isEmptyObject(result.android.companyImagesInfo)) {
		$("#android_logo,#android_fileList").append('<h4 >您还没有上传logo！</h4>');
	} else {
		for (var i in result.android.companyImagesInfo) {
			// alert(i + ":" + result.companyImagesInfo[i]);
			$("#android_logo,#android_fileList")
				.append('<div id="images"> <div ><a class="fancybox " rel="gallery1" href="' + result.android.companyImagesInfo[i] + '" title= "' + i + '"> <img src= "' + result.android.companyImagesInfo[i] + '" alt="" /> </a></div> <div>' + i + '</div></div>');
			// console.log(result.android.companyImagesInfo[i]);
		}
	}
	if ($.isEmptyObject(result.android.companyConfigInfo)) {
		$("#android_config_file ").append('<h4 >您还没有添加配置文件！</h4>');
	} else {
		for (var i in result.android.companyConfigInfo) {
			// alert(i + ":" + result.companyImagesInfo[i]);
			$("#android_config_file").append(i + '&nbsp;=&nbsp;' + result.android.companyConfigInfo[i] + '&nbsp;&nbsp;|&nbsp;&nbsp;');

			// var companyName = result.keys[i];
			// $("#company_button").append();
		}

		// 将信息填入表中
		// console.log("服务器获取的信息：" + result.android.companyConfigInfo.appName);
		$("input[name=appName]").attr("value",
			result.android.companyConfigInfo.appName);
		$("input[name=spaceName]").attr("value",
			result.android.companyConfigInfo.spaceName);
		$("input[name=publishApkName]").attr("value",
			result.android.companyConfigInfo.publishApkName);
		$("input[name=versionCode]").attr("value",
			result.android.companyConfigInfo.versionCode);
		$("input[name=versionName]").attr("value",
			result.android.companyConfigInfo.versionName);
		$("input[name=serverip]").attr("value",
			result.android.companyConfigInfo.serverip);
		$("input[name=serverport]").attr("value",
			result.android.companyConfigInfo.serverport);
		$("#androidTextArea")
			.val(result.android.companyConfigInfo.androidTextArea);
	}
}

// 在主页和配置页更新配置信息
function updateIOSCompanyInfo(result) {
	// 同时在主页和配置页添加logo的缩略图！！
	$("#IOS_logo,#IOS_fileList").empty();
	$("#IOS_config_file").empty();

	if ($.isEmptyObject(result.IOS.companyImagesInfo)) {
		$("#IOS_logo,#IOS_fileList").append('<h4 >您还没有上传logo！</h4>');
	} else {
		for (var i in result.IOS.companyImagesInfo) {
			// alert(i + ":" + result.companyImagesInfo[i]);
			$("#IOS_logo,#IOS_fileList")
				.append('<div id="images"> <div ><a class="fancybox " rel="gallery1" href="' + result.IOS.companyImagesInfo[i] + '" title= "' + i + '"> <img src= "' + result.IOS.companyImagesInfo[i] + '" alt="" /> </a></div> <div>' + i + '</div></div>');
			console.log(result.IOS.companyImagesInfo[i]);
		}
	}
	if ($.isEmptyObject(result.IOS.companyConfigInfo)) {
		$("#IOS_config_file ").append('<h4 >您还没有添加配置文件！</h4>');
	} else {
		for (var i in result.IOS.companyConfigInfo) {
			// alert(i + ":" + result.companyImagesInfo[i]);
			$("#IOS_config_file").append(i + '&nbsp;=&nbsp;' + result.IOS.companyConfigInfo[i] + '&nbsp;&nbsp;|&nbsp;&nbsp; ');

			// var companyName = result.keys[i];
			// $("#company_button").append();
		}

		// 将信息填入表中
		// console.log("服务器获取的信息：" + result.IOS.companyConfigInfo.name);
		$("input[name=name]").attr("value", result.IOS.companyConfigInfo.name);
		$("input[name=version]").attr("value",
			result.IOS.companyConfigInfo.version);
		$("input[name=serverIp]").attr("value",
			result.IOS.companyConfigInfo.serverIp);
		$("input[name=serverPort]").attr("value",
			result.IOS.companyConfigInfo.serverPort);
	}
}



// 在主页和配置页更新配置信息
function updatePCCompanyInfo(result) {
	// 同时在主页和配置页添加logo的缩略图！！
	$("#PC_logo,#PC_fileList").empty();
	$("#PC_config_file").empty();

	if ($.isEmptyObject(result.PC.companyImagesInfo)) {
		$("#PC_logo,#PC_fileList").append('<h4 >您还没有上传logo！</h4>');
	} else {
		for (var i in result.PC.companyImagesInfo) {
			// alert(i + ":" + result.companyImagesInfo[i]);
			$("#PC_logo,#PC_fileList")
				.append('<div id="images"> <div ><a class="fancybox " rel="gallery1" href="' + result.PC.companyImagesInfo[i] + '" title= "' + i + '"> <img src= "' + result.PC.companyImagesInfo[i] + '" alt="" /> </a></div> <div>' + i + '</div></div>');
			console.log(result.PC.companyImagesInfo[i]);
		}
	}
	if ($.isEmptyObject(result.PC.companyConfigInfo)) {
		$("#PC_config_file ").append('<h4 >您还没有添加配置文件！</h4>');
	} else {
		for (var i in result.PC.companyConfigInfo) {
			// alert(i + ":" + result.companyImagesInfo[i]);
			$("#PC_config_file").append(i + '&nbsp;=&nbsp;' + result.PC.companyConfigInfo[i] + '&nbsp;&nbsp;|&nbsp;&nbsp; ');

			// var companyName = result.keys[i];
			// $("#company_button").append();
		}

		// 将信息填入表中
		// console.log("服务器获取的信息：" + result.PC.companyConfigInfo.name);

		var json = result.PC.companyConfigInfo;
		$("input[name=PRODUCT_NAME]").attr("value", json.PRODUCT_NAME);
		$("input[name=SHORTCUT_NAME]").attr("value", json.SHORTCUT_NAME);
		$("input[name=PRODUCT_PUBLISHER]").attr("value", json.PRODUCT_PUBLISHER);
		$("input[name=DEFAULT_SERVER]").attr("value", json.DEFAULT_SERVER);
		$("input[name=DEFAULT_PORT]").attr("value", json.DEFAULT_PORT);
		$("input[name=SPARE_HOST]").attr("value", json.SPARE_HOST);
		$("input[name=SPARE_PORT]").attr("value", json.SPARE_PORT);


		$("input[name=ProxyIP]").attr("value", json.ProxyIP);
		$("input[name=ProxyPort]").attr("value", json.ProxyPort);

		$("select[name=SKIN_COLOR]").children("[value=" + json.SKIN_COLOR + "]").attr("selected", true);
		$("select[name=ProxyStyle]").children("[value=" + json.ProxyStyle + "]").attr("selected", true);


	}
}

$("#search_input").change(function(event) {

	search();
});

$("#search_button").click(function(event) {
	// alert("搜索");
	search();
});

// 搜索
function search() {

	if ($("#search_input").val() != null && $("#search_input").val() != "") {
		// 添加完之后，清空companyNames
		$("#company_button").empty();
		// 刷新
		getCompanyList();
		// 选中
		$("#" + $("body").attr("selectCompanyName")).trigger('click');
		check_company_select();

		// 遍历input,取得value
		var searchText = $("#search_input").val();
		// alert(searchText);
		$("input[type=radio]").each(function(index, value) {

			// alert(index + ":" + $(value).val());
			if ($(value).val().search(searchText) == -1) {
				$(value).css('display', 'none');
				$(value).next().css('display', 'none');
			}
		});

	} else {
		// 添加完之后，清空companyNames
		$("#company_button").empty();
		// 刷新
		getCompanyList();
		// 选中
		$("#" + $("body").attr("selectCompanyName")).trigger('click');
		check_company_select();
	}

}

// 删除公司文件名
function companyDelete(url) {
	$.ajax({

		async: false,
		url: url,
		dataType: "jsonp",
		jsonp: "callback", // 传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		jsonpCallback: "callback", // 自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据

		success: function(result) {
			if (!$.isEmptyObject(result)) {
				if ($.trim(result.status) == "ERROR") {
					alert("ERROR:" + result.message);
				}
				if ($.trim(result.status) == "SUCCESS") {
					// 添加完之后，清空companyNames
					$("#company_button").empty();

					// 刷新
					getCompanyList();

					// 选中
					$("#" + $("body").attr("selectCompanyName"))
						.trigger('click');
					check_company_select();
				}
			} else {
				alert("返回结果为空！");
			}

		}

	}).done(function() {
		console.log("success");

	}).fail(function() {
		console.log("error");

	}).always(function() {
		console.log("complete");
	});
}

// 改变打包历史平台时
function changePackHistoryPlatform() {
	var test = $("#home_select_platform option:selected").text();
	console.log(test);
	// 每次选中之后都发送请求
	if (test == "android") {

		getPackHistory();
	} else if (test == "IOS") {
		// console.log("显示按钮！");

		getPackHistory();
	} else if (test == "PC") {

		getPackHistory();
	}

	$("#home_select_platform").change(function() {
		var test = $("#home_select_platform option:selected").text();
		console.log(test);
		// 每次选中之后都发送请求
		if (test == "android") {

			$("#uploadToAliyunTest").css("display", "none");

			getPackHistory();
		} else if (test == "IOS") {

			$("#uploadToAliyunTest").css("display", "inline");
			getPackHistory();
		} else if (test == "PC") {

			$("#uploadToAliyunTest").css("display", "none");
			getPackHistory();
		}
	});
}

// 获取安卓历史版本
function getPackHistory() {
	var select_platform = $("#home_select_platform option:selected").text();
	$.ajax({
		url: "/pack/historyServlet?platform=" + select_platform,
		dataType: 'jsonp',

		jsonp: "callback", // 传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		jsonpCallback: "callback", // 自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据

		success: function(result) {
			// 清空显示
			$("#home_select_history").empty();

			if (select_platform == "android") {
				if (!$.isEmptyObject(result.android)) {
					// 不为空，遍历，显示历史打包

					for (var i in result.android) {
						var historyPackCompanyFile = result.android[i];

						$("#home_select_history").append('<option>' + historyPackCompanyFile + '</option>');
					}

				} else {
					// 为空，则显示无

					$("#home_select_history")
						.append('<option>历史打包文件不存在!</option>');

				}
			}

			if (select_platform == "IOS") {
				if (!$.isEmptyObject(result.IOS)) {
					// 不为空，遍历，显示历史打包

					for (var i in result.IOS) {
						var historyPackCompanyFile = result.IOS[i];

						$("#home_select_history").append('<option>' + historyPackCompanyFile + '</option>');
					}

				} else {
					// 为空，则显示无

					$("#home_select_history")
						.append('<option>历史打包文件不存在!</option>');

				}
			}

			if (select_platform == "PC") {
				if (!$.isEmptyObject(result.PC)) {
					// 不为空，遍历，显示历史打包

					for (var i in result.PC) {
						var historyPackCompanyFile = result.PC[i];

						$("#home_select_history").append('<option>' + historyPackCompanyFile + '</option>');
					}

				} else {
					// 为空，则显示无

					$("#home_select_history")
						.append('<option>历史打包文件不存在!</option>');

				}
			}

		}
	}).done(function() {
		console.log("success");
	}).fail(function() {
		console.log("error");
	}).always(function() {
		console.log("complete");
	});
}