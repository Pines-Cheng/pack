

$(document).ready(function() {
			getOSSInfo();
		});

function getOSSInfo() {
	spinDisplay();

	var operateFunction = function(result) {
		if (result.xserverCompanys == undefined
				|| result.testCompanys == undefined) {
			alert("从阿里云获取信息错误！");
		} else {

			for (var i in result.testCompanys) {
				$("#aliyunTestTable")
						.append("<tr><td id="
								+ "test_"
								+ result.testCompanys[i]
								+ ">"
								+ result.testCompanys[i]
								+ "</td><td><button type=\"button\" class=\"btn btn-default btn-sm\"  >上传到xserver</button></td><td><button type=\"button\" class=\"btn btn-default btn-sm\"  >删除</button></td></tr>");
				// 绑定上传和删除时间
				bindTestButton("test_" + result.testCompanys[i]);
				console.log("test_" + result.testCompanys[i]);
			}

			for (var i in result.xserverCompanys) {
				$("#aliyunXserverTable")
						.append("<tr><td id="
								+ "xserver_"
								+ result.xserverCompanys[i]
								+ ">"
								+ result.xserverCompanys[i]
								+ "</td><td></td><td><button type=\"button\" class=\"btn btn-default btn-sm\"  >删除</button></td></tr>");
				// 绑定上传和删除时间
				bindXserverButton("xserver_" + result.xserverCompanys[i]);
				console.log("xserver_" + result.xserverCompanys[i]);
			}

		}
	}

	var url = "/pack/OSSOperateServlet?command=getInfo";
	generalAjax(url, operateFunction);
}

// 传进去一个URL和一个函数指针
function generalAjax(url, operateFunction) {
	spinDisplay();
	$.ajax({

		dataType : "json",
		url : url
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

//从test下的两个按钮
function bindTestButton(id) {

	// 绑定第一个button 上传xserver，
	$("#" + id).next().children().click(function() {

		var url = "/pack/OSSOperateServlet?command=uploadToXserver&companyPinyin="
				+ getCompanyPinyinFromId(id);

		var operateFunction = function(result) {
			//返回状态错误
			if (result.status == null || result.status == undefined) {
				alert("从服务器获取信息失败！");
			} else if (result.status == "ERROR") {
				alert(result.message);
			} else {
				//执行成功！
				//清空，重新加载
				emptyAndReloadTable();
			}
		};

		// 执行AJAX
		generalAjax(url, operateFunction);

	});

	// 获取第二个button，删除test
	$("#" + id).next().next().children().click(function() {
		var url = "/pack/OSSOperateServlet?command=deleteTest&companyPinyin="
				+ getCompanyPinyinFromId(id);

		var operateFunction = function(result) {
			//返回状态错误
			if (result.status == null || result.status == undefined) {
				alert("从服务器获取信息失败！");
			} else if (result.status == "ERROR") {
				alert(result.message);
			} else {
				//执行成功！
				emptyAndReloadTable();
			}
		};

		// 执行AJAX
		generalAjax(url, operateFunction);
	});
}

//从test_companyName 中获取companyName
function getCompanyPinyinFromId(id){
	var strArray = id.split("_");
	return strArray[1];
}

//删除xserver上的文件夹
function bindXserverButton(id) {
	$("#" + id).next().next().children().first().click(function() {
				var url = "/pack/OSSOperateServlet?command=deleteXserver&companyPinyin="
				+ getCompanyPinyinFromId(id);

		var operateFunction = function(result) {
			//返回状态错误
			if (result.status == null || result.status == undefined) {
				alert("从服务器获取信息失败！");
			} else if (result.status == "ERROR") {
				alert(result.message);
			} else {
				//执行成功！
				emptyAndReloadTable();
			}
		};

		// 执行AJAX
		generalAjax(url, operateFunction);
			});
}

//清空表格，冲洗加载
function emptyAndReloadTable(){
	$("#aliyunTestTable").empty();
	$("#aliyunXserverTable").empty();
	getOSSInfo();
}
