//绑定阿里云上传事件
$("#uploadToAliyunTest").click(function(event) {
	spinDisplay();
	// alert("你点击了一下！");
	var select_file = $("#home_select_history option:selected").text();
	var selectCompanyName = $("body").attr("selectCompanyName");

	if (select_file == null || select_file == undefined || selectCompanyName == undefined) {
		console.log("获取的值无效！");
	} else {



		var IOSDownloadFilePath = "/var/local/pack/IOS/" + selectCompanyName + "/" + select_file;
		var url = "/pack/OSSOperateServlet?command=uploadToTest&filePath=" + IOSDownloadFilePath;
		var operateFunction = function(result) {
			if (result.status == "ERROR") {
				alert(result.message);
			} else if (result.status == "SUCCESS") {
				alert("文件添加到上传队列成功！");
			}
			console.log("uploadToAliyunTest success!");
		};

		generalAjax(url, operateFunction);
	}



});


// 通用的AJAX函数，穿进去一个URL和一个函数指针
function generalAjax(url, operateFunction) {
	$.ajax({

		dataType: "json",
		url: url
			// 同步的，不做完无法进行其他操作，锁住浏览器
			// async:false

	}).done(function(result) {
		spinHidden();
		operateFunction(result);

	}).fail(function() {
		spinHidden();
		console.log("error");
	}).always(function() {
		spinHidden();
		console.log("complete");
	});
}



//为修改安卓端配置文件添加事件，fancybox的配置及调用
$(document).ready(function() {



	//无边框！！！
	$(".fancybox_config")
		.attr('rel', 'gallery')
		.fancybox({
			padding: 0,
			arrows: false,
			nextClick: false,
			closeClick: false,
			mouseWheel: false,
			helpers: {
				overlay: {
					closeClick: false
				}
			},

			afterClose: function() {
				setTimeout(function() {
					getCompanyImages(encodeURI("/pack/getCompanyInfo"))
				}, 1000);
				return;
			}
		});

	$(".fancybox_aliyunManage")
		.attr('rel', 'gallery')
		.fancybox({
			// padding: 0,
			// arrows: false,
			// nextClick: false,
			// closeClick: false,
			// mouseWheel: false,
			// width: '800',
			// height: '600',
			// autoSize: false

			maxWidth: 800,
			maxHeight: 600,
			fitToView: false,
			width: '70%',
			height: '70%',
			autoSize: false,
			closeClick: false,
			openEffect: 'none',
			closeEffect: 'none',

			helpers: {
				overlay: {
					closeClick: false
				}
			}
		});


});



//打包的fancybox,退出时自动删除临时文件
$(document).ready(function() {
	//无边框！！！
	$(".fancybox_pack_android")
		.attr('rel', 'gallery')
		.fancybox({
			padding: 0,
			arrows: false,
			nextClick: false,
			closeClick: false,
			mouseWheel: false,
			helpers: {
				overlay: {
					closeClick: false
				}
			},
			afterClose: function() {
				//alert("执行ajax删除！");
				setTimeout(function() {
					getCompanyImages(encodeURI("/pack/getCompanyInfo"));
				}, 1000);

				deleteAndroidPackTempFile();

				console.log($("body").attr("packResult"));


			}


		});
	//无边框！！！
	$(".fancybox_pack_IOS")
		.attr('rel', 'gallery')
		.fancybox({
			padding: 0,
			arrows: false,
			nextClick: false,
			closeClick: false,
			mouseWheel: false,
			helpers: {
				overlay: {
					closeClick: false
				}
			},
			afterClose: function() {
				//alert("执行ajax删除！");
				setTimeout(function() {
					getCompanyImages(encodeURI("/pack/getCompanyInfo"));
				}, 1000);

				console.log("删除IOS临时文件！");
				deleteIOSPackTempFile();

				console.log($("body").attr("IOSPackResult"));


			}


		});

	//无边框！！！
	$(".fancybox_pack_PC")
		.attr('rel', 'gallery')
		.fancybox({
			padding: 0,
			arrows: false,
			nextClick: false,
			closeClick: false,
			mouseWheel: false,
			helpers: {
				overlay: {
					closeClick: false
				}
			},
			afterClose: function() {
				//alert("执行ajax删除！");
				setTimeout(function() {
					getCompanyImages(encodeURI("/pack/getCompanyInfo"));
				}, 1000);

				

				console.log($("body").attr("PCPackResult"));


			}


		});

});

function deleteAndroidPackTempFile() {
	if ($("body").attr("packResult") == "SUCCESS") {
		if (confirm("是否保存本次打包生成的安装包？") == false) {
			var result = $("body").attr('androidProjectDestPath');
			if (result != null || result != undefined) {
				console.log("发送ajax!,删除临时文件！");
				$.ajax({
						dataType: "json",
						url: "/pack/androidPackServlet?command=delete&deleteFilePath=" + result

					})
					.done(function(result) {
						if (result.status == "SUCCESS") {
							console.log("delete temp pack files success!");
						} else {
							alert(result.message);
						}
					})
					.fail(function() {
						console.log("error");
					})
					.always(function() {
						console.log("complete");
					});
			} else {
				console.log("删除临时文件时，返回的result数据不存在！");
			}
		} else {
			console.log("点击了确认，保存！");
			var result = $("body").attr('androidProjectDestPath');
			var downloadFileURL = $("body").attr('downloadFileURL');

			if (result != null) {
				console.log("发送ajax!,删除临时文件！");
				$.ajax({

						dataType: "json",
						url: "/pack/androidPackServlet?command=save&androidProjectDestPath=" + result + "&downloadFileURL=" + downloadFileURL

					})
					.done(function() {
						console.log("success");
					})
					.fail(function() {
						console.log("error");
					})
					.always(function() {
						console.log("complete");
					});

				//更新打包历史
				changePackHistoryPlatform();

			} else {
				console.log("删除临时文件时，返回的result数据不存在！");
			}
		}
	}

	//将打包结果和打包返回信息清空
	$("body").attr('androidProjectDestPath', "");
	$("body").attr('packResult', "ERROR");
}

function deleteIOSPackTempFile() {
	//打包成功
	if ($("body").attr("IOSPackResult") == "SUCCESS") {
		//不保存打包文件
		if (confirm("是否保存本次打包生成的安装包？") == false) {
			//文件下载路径
			var IOSDownloadFilePath = $("body").attr('IOSDownloadFilePath');
			//临时打包路径
			var IOSPackTempFileString = $("body").attr('IOSPackTempFileString');

			if (IOSDownloadFilePath != null || IOSDownloadFilePath != undefined) {
				console.log("发送ajax!,删除临时文件和历史打包！");
				$.ajax({

						url: "/pack/IOSPackServlet?command=delete&deleteFilePath=" + IOSDownloadFilePath + "&IOSPackTempFileString=" + IOSPackTempFileString,
						async: false,
						dataType: "jsonp",
						jsonp: "callback", //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
						jsonpCallback: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
					})
					.done(function(result) {
						if (result.redirect) {
							// data.redirect contains the string URL to redirect to

							$.ajax({

								url: result.redirect,
								async: false,
								dataType: "jsonp",
								jsonp: "callback",
								jsonpCallback: "callback"
							});

						}
					})
					.fail(function() {
						console.log("error");
					})
					.always(function() {
						console.log("complete");
					});
			} else {
				console.log("删除临时文件时，返回的result数据不存在！");
			}

			//保存打包文件
		} else {
			console.log("点击了确认，保存！");
			//文件下载路径
			var IOSDownloadFilePath = $("body").attr('IOSDownloadFilePath');
			//临时打包路径
			var IOSPackTempFileString = $("body").attr('IOSPackTempFileString');

			if (IOSDownloadFilePath != null || IOSDownloadFilePath != undefined) {
				console.log("发送ajax!,删除临时文件！");
				$.ajax({

						url: "/pack/IOSPackServlet?command=delete&IOSPackTempFileString=" + IOSPackTempFileString,
						async: false,
						dataType: "jsonp",
						jsonp: "callback", //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
						jsonpCallback: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
					})
					.done(function(result) {
						if (result.redirect) {
							// data.redirect contains the string URL to redirect to
							$.ajax({

								url: result.redirect,
								async: false,
								dataType: "jsonp",
								jsonp: "callback",
								jsonpCallback: "callback"
							});
						}
					})
					.fail(function() {
						console.log("error");
					})
					.always(function() {
						console.log("complete");
					});
			} else {
				console.log("删除临时文件时，返回的result数据不存在！");
			}
		}
	}

	//将打包结果和打包返回信息清空
	$("body").attr('IOSDownloadFilePath', "");
	$("body").attr('IOSPackTempFileString', "");
	$("body").attr('IOSPackResult', "ERROR");
}


//图片预览的框架
$(document).ready(function() {
	$(".fancybox").fancybox({
		openEffect: 'none',
		closeEffect: 'none'
	});
});


//每次点击公司名，都会出发公司名选择事件，为公司选择绑定多个事件
$("#company_button>input").click(function(event) {
	/* Act on the event */
	//修改当前公司，并显示公司名
	check_company_select();

	//alert("haha");
	//改变按钮样式
});

//修改安卓配置文件的事件
$("#android_config_button").click(function(event) {
	//alert("进入");

	//自动显示服务器上保存的图片名
	getCompanyImages(encodeURI("/pack/getCompanyInfo"));


});

//删除一个公司名
$("#delete_button").click(function(event) {
	var url = "/pack/companyNameOperateServlet?command=delete&selectedCompanyName=" + $("body").attr("selectCompanyName");
	companyDelete(url);
	window.location.reload();
});



//历史版本下载
$("#home_pack_download").click(function(event) {
	var select_platform = $("#home_select_platform option:selected").text();
	var select_file = $("#home_select_history option:selected").text();
	//如果显示存在历史打包文件
	if (select_file != "历史打包文件不存在!") {

		var url = "/pack/historyServlet?command=download&fileName=" + select_file + "&platform=" + select_platform

		//下载
		window.open(url);


	}
});
//历史版本删除
$("#home_pack_delete").click(function(event) {
	var select_platform = $("#home_select_platform option:selected").text();
	var select_file = $("#home_select_history option:selected").text();
	//如果显示存在历史打包文件
	if (select_file != "历史打包文件不存在!") {
		$.ajax({
				url: "/pack/historyServlet?command=delete&fileName=" + select_file + "&platform=" + select_platform

			})
			.done(function() {
				console.log("home_pack_delete success");
				changePackHistoryPlatform();
			})
			.fail(function() {
				console.log("error");
			})
			.always(function() {
				console.log("complete");
			});
	}
});

//启动validation Engine验证
$(document).ready(function() {
	// binds form submission and fields to the validation engine

	$('#companyEditInput').validationEngine("attach", {
		addPromptClass: 'formError-noArrow formError-small'

	});

});