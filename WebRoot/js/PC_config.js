// 绑定按钮的安卓提交事件
$("#PCConfigSubmit").click(function(event) {
			if (PCSubmitJudge()) {
				PCConfigSubmit();
			} else {
				// alert("配置填写错误！");
			}

		});

$("#settingPanelHead").click(function(event) {

			// 折叠表单
			$("#settingPanel").slideToggle("normal");
		});

function initPCConfig() {
	
	console.log("进入initPConfig!");
	
	// 折叠表单
	$("#settingPanel").slideUp("fast");
	
	console.log("执行完slideUp!");

	var disabledSelectArray = new Array("OverlayEnable", "Lock", "Admin",
			"Write", "read", "List");

	// 如果shell为false，下面的几个都为disable
	$("select[name=ShellEnable]").change(function() {
		
		console.log("进入$(\"select[name=ShellEnable]\").change()事件！");
		
		// 当shell开关改变时，一关都为关，为开时，才能够选择
		if ($("select[name=ShellEnable]").children(":selected").val() == "true") {

			console.log("选中的值为："+$("select[name=ShellEnable]").children(":selected").val() );
			
			for (var i = 0; i < disabledSelectArray.length; i++) {
				$("select[name=" + disabledSelectArray[i] + "]").removeAttr(
						"disabled");
			}
		} else {
			
			console.log("选中的值为："+$("select[name=ShellEnable]").children(":selected").val() );

			// 全部设为true
			for (var i = 0; i < disabledSelectArray.length; i++) {
				$("select[name=" + disabledSelectArray[i] + "]")
						.children("[value=true]").attr(":selected", "false");
				$("select[name=" + disabledSelectArray[i] + "]").attr(
						"disabled", "true");
			}
		}

	});

	// 如果shell为false，下面的几个都为disable
	$("select[name=OverlayEnable]").change(function() {
		
		console.log("进入$(\"select[name=OverlayEnable]\").change()事件！");
		
		if ($("select[name=ShellEnable").children(":selected").val() == "true") {
			// 当shell开关改变时，一关都为关，为开时，才能够选择
			if ($("select[name=OverlayEnable]").children(":selected").val() == "true") {

				for (var i = 1; i < disabledSelectArray.length; i++) {
					$("select[name=" + disabledSelectArray[i] + "]").removeAttr(
							"disabled");
				}
			} else {
				for (var i = 1; i < disabledSelectArray.length; i++) {
					$("select[name=" + disabledSelectArray[i] + "]")
							.children("[value=true]")
							.attr("selected", "false");
					$("select[name=" + disabledSelectArray[i] + "]").attr(
							"disabled", "true");
				}
			}
		}

	});
	
		getCompanyImages(encodeURI("/pack/getCompanyInfo"));

}

$(document).ready(function() {
			// 自动显示服务器上保存的图片名
			initPCConfig();
			console.log("执行完initPCConfig()");

		});

// 提交安卓客户端的数据
function PCConfigSubmit() {
	// 遍历
	// alert($("#versionName").val());
	// alert($("#versionName").attr("name"));

	var values = $("#customForm,#settingForm").serializeArray();
	// for(var index in values){
	// alert(values[index].name+" : "+values[index].value);
	// }

	$.ajax({
				type : "get",

				data : values,
				url : "/pack/configSaveServlet?platform=PC",
				success : function() {
					// alert("保存配置文件成功");

				},
				error : function() {
					// alert("保存配置文件失败");

				}
			}).done(function() {
				console.log("success");
				alert("保存配置成功！");
				// fancybox关闭弹出窗口
				parent.$.fancybox.close();
				// 自动显示服务器上保存的图片名
				setTimeout(function() {
							getCompanyImages(encodeURI("/pack/getCompanyInfo"));
						}, 200);
			}).fail(function() {
				console.log("error");
				alert("保存配置文件失败");
			}).always(function() {
				console.log("complete");

			});

}

// 删除图片
$("#deleteImages").click(function(event) {

			// salert("删除图片！");

			$.ajax({
						url : '/pack/deleteImagesServlet?platform=IOS',

						success : // 自动显示服务器上保存的图片名
						alert("删除文件成功")
					}).done(function() {
						console.log("success");

					}).fail(function() {
						console.log("error");
						alert("删除logo失败！");
					}).always(function() {
						console.log("complete");
					});

			// 自动显示服务器上保存的图片名
			setTimeout(function() {
						getCompanyImages(encodeURI("/pack/getCompanyInfo"));
					}, 200);

		});

// 提交时判断
function PCSubmitJudge() {
	var values = $(":input").serializeArray();
	var return_val = true;
	for (var index in values) {
		if (values[index].value == "" || values[index].value == null) {
			console.log($("input[name=" + values[index].name + "]").parent()
					.next("div")[0]);
			$("input[name=" + values[index].name + "]").parent().next("div")
					.text("输入不能为空！");
			return_val = false;
		}
	}

	return return_val;

}

// 初始化Web Uploader
var uploader = WebUploader.create({

			// 选完文件后，是否自动上传。
			auto : true,

			// swf文件路径
			swf : 'js/Uploader.swf',

			// 文件接收服务端。
			server : '/pack/uploaderServlet?platform=PC',

			// 选择文件的按钮。可选。
			// 内部根据当前运行是创建，可能是input元素，也可能是flash.
			pick : '#filePicker',

			// 只允许选择图片文件。
			accept : {
				title : 'Images',
				extensions : 'png,ico,bmp',
				mimeTypes : 'image/*'
			}
		});

// 当有文件添加进来的时候
uploader.on('fileQueued', function(file) {
	var $li = $('<div id="' + file.id + '" class="file-item thumbnail">'
			+ '<img>' + '<div class="info">' + file.name + '</div>' + '</div>'), $img = $li
			.find('img');

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
			var $li = $('#' + file.id), $percent = $li.find('.progress span');

			// 避免重复创建
			if (!$percent.length) {
				$percent = $('<p class="progress"><span></span></p>')
						.appendTo($li).find('span');
			}

			$percent.css('width', percentage * 100 + '%');
		});

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
uploader.on('uploadSuccess', function(file, response) {
	$('#' + file.id).addClass('upload-state-done');

	console.log("上传成功服务器返回结果：" + response + "文件状态：" + file.status);
		// if($.trim(response[0])=="ERROR1"){

		// alert("logo名称错误！");
		// }

	});

// 接受服务器回应
uploader.on('uploadAccept', function(file, response) {

			console.log("服务器返回结果：" + response._raw + "文件状态：" + file.status);
			if ($.trim(response._raw) == "ERROR1") {
				// 通过return false来告诉组件，此文件上传有错。
				alert("上传的logo图片不符合要求！");
			}
		});

// 文件上传失败，显示上传出错。
uploader.on('uploadError', function(file, reason) {
			var $li = $('#' + file.id), $error = $li.find('div.error');

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

	// console.log("上传完成服务器返回结果："+response);

	// 自动显示服务器上保存的图片名
	setTimeout(function() {

				getCompanyImages(encodeURI("/pack/getCompanyInfo"));

			}, 1000);
		// 自动显示服务器上保存的图片名
		// getCompanyImages(encodeURI("/pack/getCompanyInfo"));
	});