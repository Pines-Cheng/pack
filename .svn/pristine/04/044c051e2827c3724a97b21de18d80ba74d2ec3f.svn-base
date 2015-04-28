//绑定按钮的安卓提交事件
$("#androidConfigSubmit").click(function(event) {
	if (androidSubmitJudge()) {
		androidConfigSubmit();
	} else {
		//alert("配置填写错误！");
	}

});


//提交安卓客户端的数据
function androidConfigSubmit() {

	//如果validation验证失败
	if (!$('#androidForm').validationEngine('validate')) {
		return;
	}

	//遍历

	var values = $(":input").serialize();
	// alert(values);
	var androidTextArea = $("#androidTextArea").val();
	// alert(textArea);
	
	// for(var index in values){
	// 	alert(values[index].name+" : "+values[index].value);
	// }

	$.ajax({
			type: "post",

			data: values+"&androidTextArea="+androidTextArea,
			url: "/pack/configSaveServlet?platform=android",
			success: function() {
				//alert("保存配置文件成功");

			},
			error: function() {
				//alert("保存配置文件失败");

			}
		})
		.done(function() {
			console.log("success");
			alert("保存配置成功！");
			//fancybox关闭弹出窗口
			parent.$.fancybox.close();
			//自动显示服务器上保存的图片名
			setTimeout(function() {
				getCompanyImages(encodeURI("/pack/getCompanyInfo"));
			}, 200);
		})
		.fail(function() {
			console.log("error");
			alert("保存配置文件失败");
		})
		.always(function() {
			console.log("complete");

		});

}

//删除图片
$("#deleteImages").click(function(event) {

	//salert("删除图片！");

	$.ajax({
			url: '/pack/deleteImagesServlet?platform=android',

			data: {},

			success: //自动显示服务器上保存的图片名
				alert("删除文件成功")
		})
		.done(function() {
			console.log("success");


		})
		.fail(function() {
			console.log("error");
			alert("删除logo失败！");
		})
		.always(function() {
			console.log("complete");
		});

	//自动显示服务器上保存的图片名
	setTimeout(function() {
		getCompanyImages(encodeURI("/pack/getCompanyInfo"));
	}, 200);



});


$("input[name=appName]").focusout(function(event) {
	if ($("input[name=appName]").val() == "" || $("input[name=appName]").val() == null) {
		$("#androidCondigTips").text("应用名不能为空！");
	} else {
		$("#androidCondigTips").text("")
	}
});



$("input[name=versionCode]").focusout(function(event) {
	if ($("input[name=versionCode]").val() == "" || $("input[name=versionCode]").val() == null || isNaN($("input[name=versionCode]").val())) {
		$("#androidCondigTips").text("版本号不能为空且必须为数字！");
	} else {
		$("#androidCondigTips").text("")
	}
});

$("input[name=spaceName]").focusout(function(event) {
	if ($("input[name=spaceName]").val() == "" || $("input[name=spaceName]").val() == null) {

		$("#androidCondigTips").text("下载目录不能为空！");
	} else {
		$("#androidCondigTips").text("")
	}
});

$("input[name=versionName]").focusout(function(event) {
	if ($("input[name=versionName]").val() == "" || $("input[name=versionName]").val() == null) {

		$("#androidCondigTips").text("版本名不能为空！");
	} else {
		$("#androidCondigTips").text("")
	}
});

$("input[name=publishApkName]").focusout(function(event) {
	if ($("input[name=publishApkName]").val() == "" || $("input[name=publishApkName]").val() == null) {

		$("#androidCondigTips").text("APK包名不能为空！");
	} else {
		$("#androidCondigTips").text("")
	}
});

$("input[name=serverip]").focusout(function(event) {
	if ($("input[name=serverip]").val() == "" || $("input[name=serverip]").val() == null) {

		$("#androidCondigTips").text("默认IP不能为空！");
	} else {
		$("#androidCondigTips").text("")
	}
});

$("input[name=serverport]").focusout(function(event) {
	if ($("input[name=serverport]").val() == "" || $("input[name=serverport]").val() == null) {

		$("#androidCondigTips").text("默认端口不能为空");
	} else {
		$("#androidCondigTips").text("")
	}
});

//提交时判断
function androidSubmitJudge() {
	var values = $(":input").serializeArray();
	var return_val = true;
	for (var index in values) {
		if (values[index].value == "" || values[index].value == null) {
			console.log($("input[name=" + values[index].name + "]").parent().next("div")[0]);
			$("input[name=" + values[index].name + "]").parent().next("div").text("输入不能为空！");
			return_val = false;
		}
	}
	if ($("input[name=versionCode]").val() == "" || $("input[name=versionCode]").val() == null || isNaN($("input[name=versionCode]").val())) {
		$("input[name=versionCode]").parent().next("div").text("输入必须为数字且不能为空！");
		return_val = false;
	}
	return return_val;

}

//true选中
$("#androidDefaultsetTrue").click(function(event) {
	$("input[name=serverip]").attr('readonly', false);
	$("input[name=serverport]").attr('readonly', false);
	$("input[name=serverip]").val("");
	$("input[name=serverport]").val("");
});

//false选中
$("#androidDefaultsetFalse").click(function(event) {
	$("input[name=serverip]").attr('readonly', true);
	$("input[name=serverport]").attr('readonly', true);
	$("input[name=serverip]").val("192.168.1.1");
	$("input[name=serverport]").val("80");
});

//显示当前版本号
function get_current_android_svn_version() {
	$.ajax({
			url: '/pack/svnServlet?command=getSvnInfo',
			async: false,
			dataType: "jsonp",
			jsonp: "callback", //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据

		})
		.done(function(result) {
			if (!$.isEmptyObject(result)) {
				if ($.trim(result.status) == "ERROR") {
					alert("ERROR:" + result.message);
				} else if ($.trim(result.status) == "SUCCESS") {
					//版本名与版本号不为空
					if ((!$.isEmptyObject(result.versionCode)) && (!$.isEmptyObject(result.versionName))) {
						console.log(" get_current_android_svn_version success");
						console.log(result);
						//alert($("#current_android_svn_version").text());

						//更改显示的版本号
						$("#current_android_svn_version").text("");
						$("#current_android_svn_version").text(result.versionName);
						console.log("current_android_svn_version" + result.versionName);

						//保存svn属性
						$("body", parent.document).attr('versionCode', $.trim(result.versionCode));
						$("body", parent.document).attr('versionName', $.trim(result.versionName));

					} else {

						alert("ERROR:版本号或版本名为空！")
					}

				}

			} else {

				alert("ERROR:获取svn错误，请检查服务器！");

			}

		})

	.fail(function() {
			console.log("get_current_android_svn_version  error");
		})
		.always(function() {
			console.log("get_current_android_svn_version complete");
		});
}

//true选中
$("#androidUseSvnVersionCodeFalse").click(function(event) {
	$("input[name=versionCode]").attr('readonly', false);
	$("input[name=versionName]").attr('readonly', false);
	$("input[name=versionCode]").val("");
	$("input[name=versionName]").val("");
});

//false选中
$("#androidUseSvnVersionCodeTrue").click(function(event) {
	$("input[name=versionCode]").attr('readonly', true);
	$("input[name=versionName]").attr('readonly', true);
	$("input[name=versionCode]").val($("body", parent.document).attr('versionCode'));

	$("input[name=versionName]").val($("body", parent.document).attr('versionName'));
});

$("#android_svn_update").click(function(event) {

	$("#current_android_svn_version").empty();
	$("#current_android_svn_version").append('正在从SVN获取最新代码...');

	$.ajax({
			url: '/pack/svnServlet?command=update'

		})
		.done(function(data) {
			console.log("android_svn_update " + data);

			//alert("svn update成功！");


			var last = $("body", parent.document).attr('versionName');
			get_current_android_svn_version();
			var current = $("body", parent.document).attr('versionName');


			if (last == current) {

				$("#current_android_svn_version").append('当前版本已是最新！');
			} else {

				$("#current_android_svn_version").append('更新成功！');
			}



		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});

});


//启动validation Engine验证
$(document).ready(function() {
	// binds form submission and fields to the validation engine
	$('#androidForm').validationEngine('attach');

	$('#androidForm').validationEngine({
		addPromptClass: 'formError-noArrow formError-small'

	});

});


// 初始化Web Uploader
var uploader = WebUploader.create({

	// 选完文件后，是否自动上传。
	auto: true,

	// swf文件路径
	swf: 'js/Uploader.swf',

	// 文件接收服务端。
	server: '/pack/uploaderServlet?platform=android',

	// 选择文件的按钮。可选。
	// 内部根据当前运行是创建，可能是input元素，也可能是flash.
	pick: '#filePicker',

	//只允许选择图片文件。
	accept: {
		title: 'Images',
		extensions: 'png',
		mimeTypes: 'image/*'
	}
});

// 当有文件添加进来的时候
uploader.on('fileQueued', function(file) {
	var $li = $('<div id="' + file.id + '" class="file-item thumbnail">' + '<img>' + '<div class="info">' + file.name + '</div>' + '</div>'),
		$img = $li.find('img');

	var $list = $("#fileList");
	// $list为容器jQuery实例
	$list.append($li);

	// 创建缩略图
	// 如果为非图片文件，可以不用调用此方法。
	// thumbnailWidth x thumbnailHeight 为 100 x 100
	uploader.makeThumb(file, function(error, src) {
		if (error) {
			$img.replaceWith('<span>不能预览</span>');
			return;
		}

		$img.attr('src', src);
	}, 100, 100);
});

// 文件上传过程中创建进度条实时显示。
uploader.on('uploadProgress', function(file, percentage) {
	var $li = $('#' + file.id),
		$percent = $li.find('.progress span');

	// 避免重复创建
	if (!$percent.length) {
		$percent = $('<p class="progress"><span></span></p>').appendTo($li)
			.find('span');
	}

	$percent.css('width', percentage * 100 + '%');
});

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
uploader.on('uploadSuccess', function(file, response) {
	$('#' + file.id).addClass('upload-state-done');

	console.log("上传成功服务器返回结果：" + response + "文件状态：" + file.status);
	// if($.trim(response[0])=="ERROR1"){

	// 	alert("logo名称错误！");
	// }

});

//接受服务器回应
uploader.on('uploadAccept', function(file, response) {

	console.log("服务器返回结果：" + response._raw + "文件状态：" + file.status);
	if ($.trim(response._raw) == "ERROR1") {
		// 通过return false来告诉组件，此文件上传有错。
		alert("上传的logo图片不符合要求！");
	}
});

// 文件上传失败，显示上传出错。
uploader.on('uploadError', function(file, reason) {
	var $li = $('#' + file.id),
		$error = $li.find('div.error');

	// 避免重复创建
	if (!$error.length) {
		$error = $('<div class="error"></div>').appendTo($li);
	}

	console.log("上传错误服务器返回结果：" + reason);

	$error.text('上传失败');
});

// 完成上传完了，成功或者失败，先删除进度条。
uploader.on('uploadComplete', function(file) {

	$('#' + file.id).find('.progress').remove();

	//console.log("上传完成服务器返回结果："+response);

	//自动显示服务器上保存的图片名
	setTimeout(function() {

		getCompanyImages(encodeURI("/pack/getCompanyInfo"));

	}, 1000);
	//自动显示服务器上保存的图片名
	//getCompanyImages(encodeURI("/pack/getCompanyInfo"));
});