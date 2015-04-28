//安卓打包按钮绑定等待和打包的事件
$("#IOSPackStart").click(function(event) {
	//alert("开始打包！");
	//每次打包前，都将打包结果初始化为ERROR
	$("body", parent.document).attr('IOSPackResult', "ERROR");
	console.log("点击打包，初始化为ERROR！");
	spinDisplay();
	IOSPack();
	// alert("IOS打包暂时还有点小问题，请稍等！");

});

//绑定阿里云上传事件
$("#uploadToAliyunTest").click(function(event) {
	// alert("你点击了一下！");
	spinDisplay();
	var IOSDownloadFilePath = $("body", parent.document).attr('IOSDownloadFilePath');
	if(IOSDownloadFilePath==null||IOSDownloadFilePath==undefined){
		console.log("IOSDownloadFilePath is null!");
		// return;
	}
	var url = "/pack/OSSOperateServlet?command=uploadToTest&filePath=" +IOSDownloadFilePath;
		var operateFunction = function(result) {
			if(result.status=="ERROR"){
				alert(result.message);
			}
			console.log("uploadToAliyunTest success!");
		};

	generalAjax(url, operateFunction);

});


// 穿进去一个URL和一个函数指针
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


//安卓客户端打包
function IOSPack() {
	//遍历
	// var values = $(":input").serializeArray();
	$("body", parent.document).attr('IOSPackResult', "ERROR");
	$.ajax({
			type: "get",
			//async:false,
			// data: values,
			url: "/pack/IOSPackServlet?command=pack&platform=IOS",
			dataType: "jsonp",
			jsonp: "callback", //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback: "callback", //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
			success: function(result) {
				if (result.status == "ERROR") {
					spinHidden();
					alert(result.message);
					//设置打包结果
					$("body", parent.document).attr('IOSPackResult', "ERROR");
				} else if (!$.isEmptyObject(result)) {

					spinHidden();

					//将json数据保存到body中
					$("body", parent.document).attr('IOSPackTempFileString', result.IOSPackTempFileString);
					$("body", parent.document).attr('IOSDownloadFilePath', result.IOSDownloadFilePath);
					//alert("保存配置文件成功");

					//绑定框架的路径
					$("#pack_iframe").attr("src", result.shellOperateLogPath);

					//绑定shell脚本执行的路径
					$("#IOSPackShellOperateLog").attr('href', result.shellOperateLogPath);

					//获取下载的URL，绑定下载按钮
					$("#IOSPackDownload").click(function(event) {
						//alert("下载！");
						window.open("/pack/downloadServlet?filePath=" + result.IOSDownloadFilePath);
					});

					//显示和日志查看页面
					$("#IOSPackShellOperateLog").css('display', "");
					$("#IOSPackDownload").css('display', "");

					//设置打包结果
					$("body", parent.document).attr('IOSPackResult', "SUCCESS");
					alert("打包成功！");



				}



			},
			error: function() {
				//alert("保存配置文件失败");

			}
		})
		.done(function() {
			console.log("IOSPack() success");

		})
		.fail(function() {
			console.log("error");
			alert("打包失败");
			//设置打包结果
			$("body", parent.document).attr('IOSPackResult', "ERROR");
			spinHidden();
		})
		.always(function() {
			console.log("IOSPack() complete");

		});

}

//再次获取日志文件，并显示
function reloadLog(result) {
	$.ajax({
			//async:false,
			url: result,
		})
		.done(function() {
			console.log("reloadLog(result) success");
			$("#pack_iframe")[0].contentWindow.location.reload();

		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("reloadLog(result) complete");
		});
}

//获取日志文件，并显示
function getShellOperateLog(result) {
	$.ajax({
			//async:false,
			url: result,
		})
		.done(function() {
			console.log("getShellOperateLog(result) success");

			//$("#pack_iframe")[0].contentWindow.location.reload();
			//alert("打包完成！");
			spinHidden();

			$("#pack_iframe")[0].contentWindow.location.reload();



			//设置打包结果
			$("body", parent.document).attr('IOSPackResult', "SUCCESS");

			//显示下载按钮
			$("#IOSPackDownload").css('display', "");
			alert("打包完成！");

			console.log("打包成功，结果设置为SUCCESS！");

		})
		.fail(function() {
			console.log("getShellOperateLog(result) error");
		})
		.always(function() {
			console.log("getShellOperateLog(result) complete");
		});
}