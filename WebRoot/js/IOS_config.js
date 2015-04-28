//绑定按钮的安卓提交事件
$("#IOSConfigSubmit").click(function(event) {
	if (IOSSubmitJudge()) {
		IOSConfigSubmit();
	} else {
		//alert("配置填写错误！");
	}

});

$(document).ready(function(){
	//自动显示服务器上保存的图片名
	getCompanyImages(encodeURI("/pack/getCompanyInfo"));	
});

//提交安卓客户端的数据
function IOSConfigSubmit() {
	//遍历
	// alert($("#versionName").val());
	// alert($("#versionName").attr("name"));

	var values = $(":input").serializeArray();
	// for(var index in values){
	// 	alert(values[index].name+" : "+values[index].value);
	// }

	$.ajax({
			type: "get",

			data: values,
			url: "/pack/configSaveServlet?platform=IOS",
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
			url: '/pack/deleteImagesServlet?platform=IOS',

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



//绑定校验事件
$("input[name=name]").focusout(function(event) {
	if ($("input[name=name]").val() == "" || $("input[name=name]").val() == null) {
		$("#IOSCondigTips").text("应用名不能为空！");
	} else {
		$("#IOSCondigTips").text("")
	}
});



$("input[name=version]").focusout(function(event) {
	if ($("input[name=version]").val() == "" || $("input[name=version]").val() == null ) {
		$("#IOSCondigTips").text("版本号不能为空且必须为数字！");
	} else {
		$("#IOSCondigTips").text("")
	}
});


$("input[name=serverIp]").focusout(function(event) {
	if ($("input[name=serverIp]").val() == "" || $("input[name=serverIp]").val() == null) {

		$("#IOSCondigTips").text("默认IP不能为空！");
	} else {
		$("#IOSCondigTips").text("")
	}
});

$("input[name=serverPort]").focusout(function(event) {
	if ($("input[name=serverPort]").val() == "" || $("input[name=serverPort]").val() == null) {

		$("#IOSCondigTips").text("默认端口不能为空");
	} else {
		$("#IOSCondigTips").text("")
	}
});


//提交时判断
function IOSSubmitJudge() {
	var values = $(":input").serializeArray();
	var return_val=true;
	for (var index in values) {
		if (values[index].value == "" || values[index].value == null) {
			console.log($("input[name="+values[index].name+"]").parent().next("div")[0]);
			$("input[name="+values[index].name+"]").parent().next("div").text("输入不能为空！");
			return_val=false;
		}
	}
	if ($("input[name=version]").val() == "" || $("input[name=version]").val() == null ) {
		$("input[name=version]").parent().next("div").text("输入必须为数字且不能为空！");
		return_val=false;
	}
	return return_val;

}

//true选中
$("#IOSDefaultsetFalse").click(function(event) {
	$("input[name=serverIp]").attr('readonly', false);
	$("input[name=serverPort]").attr('readonly', false);
	$("input[name=serverIp]").val("");
	$("input[name=serverPort]").val("");
});

//false选中
$("#IOSDefaultsetTrue").click(function(event) {
	$("input[name=serverIp]").attr('readonly', true);
	$("input[name=serverPort]").attr('readonly', true);
	$("input[name=serverIp]").val("192.168.1.1");
	$("input[name=serverPort]").val("80");
});




// 初始化Web Uploader
var uploader = WebUploader.create({

	// 选完文件后，是否自动上传。
	auto: true,

	// swf文件路径
	swf: 'js/Uploader.swf',

	// 文件接收服务端。
	server: '/pack/uploaderServlet?platform=IOS',

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
uploader.on('uploadSuccess', function(file,response ) {
	$('#' + file.id).addClass('upload-state-done');

	console.log("上传成功服务器返回结果："+response+"文件状态："+file.status);
	// if($.trim(response[0])=="ERROR1"){

	// 	alert("logo名称错误！");
	// }
	
});

//接受服务器回应
uploader.on( 'uploadAccept', function( file, response ) {

	console.log("服务器返回结果："+response._raw+"文件状态："+file.status);
    if ( $.trim(response._raw)=="ERROR1" ) {
        // 通过return false来告诉组件，此文件上传有错。
        alert("上传的logo图片不符合要求！");
    }
});

// 文件上传失败，显示上传出错。
uploader.on('uploadError', function(file,reason) {
	var $li = $('#' + file.id),
		$error = $li.find('div.error');

	// 避免重复创建
	if (!$error.length) {
		$error = $('<div class="error"></div>').appendTo($li);
	}

	console.log("上传错误服务器返回结果："+reason);

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


