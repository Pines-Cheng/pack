//安卓打包按钮绑定等待和打包的事件
$("#androidPackStart").click(function(event) {
	//alert("开始打包！");
	//每次打包前，都将打包结果初始化为ERROR
	$("body", parent.document).attr('packResult', "ERROR");
	console.log("点击打包，初始化为ERROR！");
	spinDisplay();
	androidPack();


});

//安卓客户端打包
function androidPack() {
	//遍历
	// var values = $(":input").serializeArray();
	$("body", parent.document).attr('packResult', "ERROR");

	$.ajax({
			
			//异步不可为false，不然按钮没反应
			// async:false,
			// data: values,
			dataType: "json",
			url: "/pack/androidPackServlet?command=pack&platform=android",

			success: function(result) {
				if (result.status == "ERROR") {
					spinHidden();

					//将json数据保存到data中
					$("body", parent.document).attr('packResult', "ERROR");
					alert(result.message);
					//设置打包结果
				} else {

					//将json数据保存到data中
					$("body", parent.document).attr('androidProjectDestPath', result.androidProjectDestPath);
					$("body", parent.document).attr('downloadFileURL', result.downloadFileURL);
					console.log( result.androidProjectDestPath+"//"+result.downloadFileURL);
					console.log("保存打包结果信息成功！");

					//绑定框架的路径
					$("#pack_iframe").attr("src", result.shellOperateLogURL);

					//绑定shell脚本执行的路径
					$("#androidPackShellOperateLog").attr('href', result.shellOperateLogURL);

					//获取下载的URL，绑定下载按钮
					$("#pack_download").click(function(event) {
						//alert("下载！");
						window.open("/pack/downloadServlet?filePath=" + result.downloadFileURL);
					});

					//显示和日志查看页面
					$("#androidPackShellOperateLog").css('display', "");
					//显示下载按钮
					$("#pack_download").css('display', "");

					spinHidden();

					//设置打包结果
					$("body", parent.document).attr('packResult', "SUCCESS");

					alert("打包成功！");

				}

			},
			error: function() {
				//alert("保存配置文件失败");

			}
		})
		.done(function() {
			console.log("androidPack() success");

		})
		.fail(function() {
			console.log("error");
			spinHidden();
			alert("打包失败");
			//设置打包结果
			$("body", parent.document).attr('packResult', "ERROR");
		})
		.always(function() {
			console.log("androidPack() complete");

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
			spinHidden();



			//设置打包结果
			$("body", parent.document).attr('packResult', "SUCCESS");

			//显示下载按钮
			$("#pack_download").css('display', "");
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