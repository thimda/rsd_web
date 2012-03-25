	<%@ page contentType="text/html;charset=UTF-8" %>
	/** 
	 * webbx 主页面的js逻辑 
	 */
	function isOtherUserLogin() {
		//2分钟检测
		window.intervalID = setInterval("judgeIsOtherUserLogin();",  2 * 60 * 1000);
	}
	
	
	//标识是否直接关闭页签
	var closeTag = 0;
	
	//重新加载页面，除了当前激活的TabItem外，都要关闭
	function refreshOldPage() {
		var tabComp = pageUI.getTab("pagetab");
		var url = window.globalPath + "/core/uimeta.um?pageId=quickdesk";
		$ge("showFrame").src = url;
		var currentIndex = tabComp.getSelectedIndex();
		if(currentIndex == 0)
			return;
		var tabItem = tabComp.tabItems[currentIndex];
		var iframe = tabItem.getObjHtml().firstChild;
		var frameSrc = iframe.src;
		if(!frameSrc){
			iframe = tabItem.getObjHtml().firstChild.firstChild;
			frameSrc = iframe.src;
		}
		var frameTitle = tabItem.title;
		var funcode = tabItem.name;
		var tag = 1;
		var menuCard = iframe.contentWindow.pageUI.getMenubar("menu_card");
 		if(menuCard != null){
 			var menuItem =  menuCard.getMenu("card_save");
 			var sumitMenuItem =  menuCard.getMenu("submit");
	 		if(!menuItem.disabled || !sumitMenuItem.disabled){
				if (!this.confirmDialog) {
					tag = 0;
					var message = "未保存数据,确认关闭页签？";
					this.confirmDialog = new ConfirmDialogComp("confirmDialog","确定对话框", "", "", "确定要关闭此页签吗?", "");
					this.confirmDialog.changeMsg(message);
					this.confirmDialog.onOk = function() {
							closeTag = 1;
							tabComp.removeItemTab(currentIndex);
							var items = tabComp.getTabItems(), count = items.length;
							for ( var i = 0; i < count; i++) {
									// 如果已经打开过相同funcode的单据
									if (items[i].id == "desktop")
										continue;
									if(i == currentIndex)
										continue;
									tabComp.removeItemTab(i);
							}
							if(currentIndex < count) 
								tabComp.removeItemTab(count - 1);
						 	//增加新的激活项的Item
							var item1 = tabComp.createItem(funcode, frameTitle, true);
							tabComp.activeTab(tabComp.getItemIndex(item1));
							item1.getObjHtml().appendChild(getNodeFrame(funcode, frameSrc));
							this.hide();
							closeTag = 0;
							//this.hide();
				};
				this.confirmDialog.onCancel = function() {
						this.confirmDialog = null;
						closeTag = 0;
				}
			}
			this.confirmDialog.show();
			closeTag = 0;
	 	}
	 			
	}
}			
	
	
	//删除通过直接加载过来得到的节点
	function delShortCut(pk_shortcut){
		var shortcutDiv = $ge(pk_shortcut);
		var parentDiv = shortcutDiv.parentNode;
		parentDiv.removeChild(shortcutDiv);	
		var textshortcutDiv =  $ge("text_" + pk_shortcut);
		parentDiv.removeChild(textshortcutDiv);	
	}
	
	//处理tab页签关闭
	TabItem.prototype.close = function() {
		if(closeTag == 1){
			return this.parentTab.closeItem(this);
		}
		else{
			var tag = 1;
			var oThis = this;
			var iframe = oThis.getObjHtml().firstChild.firstChild;
			if(!iframe)
				iframe =  oThis.getObjHtml().firstChild;
	 		var menuCard = iframe.contentWindow.pageUI.getMenubar("menu_card");
	 		if(menuCard != null){
	 			var menuItem =  menuCard.getMenu("card_save");
	 			var sumitMenuItem =  menuCard.getMenu("submit");
		 		if(!menuItem.disabled || !sumitMenuItem.disabled){
					if (!this.confirmDialog) {
						tag = 0;
						var message = "存在未保存数据,确认关闭页签？";
						this.confirmDialog = new ConfirmDialogComp("confirmDialog","确定对话框", "", "", "确定要关闭此页签吗?", "");
						this.confirmDialog.changeMsg(message);
						this.confirmDialog.show();
						this.confirmDialog.onOk = function() {
						var closedTab = oThis.parentTab.getItemIndex(oThis);
						if(closedTab != -1)
							oThis.parentTab.removeItemTab(closedTab);
						this.hide();
					};
					oThis.confirmDialog.onCancel = function() {
							oThis.confirmDialog = null;
					}
				}
				if(tag == 0)
					return false;
				return true;	
		 	}
		 			
		}
		return true;
	}
};
	


		/**
	 * 每隔一段时间连接server，取得用户状态
	 */
	function judgeIsOtherUserLogin() {
		if ("production" != window.debugMode)
			return;
		var proxy = new ServerProxy(null,null,true);
		 //将参数传递到后台
		 proxy.addParam("listener_class", "nc.uap.lfw.ncadapter.main.JudgeOthersLogin");
		 var sbr = new SubmitRule();  
		 proxy.setSubmitRule(sbr); 
		 proxy.setReturnFunc(returnFucForJuage);
		 proxy.execute();
		 function returnFucForJuage(result, returnArgs, success){
	   	 	var result = getSessionAttribute("flag");
   			if(result == 'goout'){
   				clearInterval(window.intervalID);//清除周期链接server
   				showMessageDialog("由于当前用户在其他客户端强制登录，本客户端将退出工作台", gotoLoginPageFuc);
   			}else if(result == 'stop'){
   				clearInterval(window.intervalID);
   				showMessageDialog("由于当前用户被强制终止，本客户端将退出工作台", gotoLoginPageFuc);
   			}else if(result == 'session_out'){
   				clearInterval(window.intervalID);
   				showMessageDialog("由于恢复服务器环境时，已经有相同用户登录，本客户端将退出工作台", gotoLoginPageFuc);
   			}
	   	}
	}
	
	//重新打开登陆页面
	function gotoLoginPageFuc(){
		window.location.href = window.corePath + "/main.jsp?rand=" + (Math.random()*10000).toString().substring(0, 4);
	}

	/**
	 * 关闭整个浏览器时调用该方法
	 */
	function closeBrowser() {
		// 注销用户
		var cmd = new CommonCommand('userLogOut');
		cmd.setAsync(false);
		cmd.exec();
	}

	/**
	 * @private
	 */
	function gotoLoginPageFuc() {
		window.location.href = window.corePath + "/login/login.jsp?rand=" + (Math.random() * 10000).toString().substring(0, 4);
	}

	function showBillMsg() {
		var table = document.getElementById('msgtable');
		table.rows[0].cells[0].innerHTML = window.status;
	}

	function cleanBillMsg() {
		var table = document.getElementById('msgtable');
		table.rows[0].cells[0].innerHTML = '';
	}

	/**
	 * 用户注销
	 */
	function logout() {
		ConfirmDialogComp.showDialog("${ml:transp('lfw','lfw_main005')}",
				logoutFunc, null, null, null);
	}

	/**
	 * 用户注销逻辑
	 * @private
	 */
	function logoutFunc() {
		var cmd = new CommonCommand('userLogOut');
		cmd.setAsync(true);
		cmd.setReturnFunc(returnFucForLogOut);
		cmd.exec();

		function returnFucForLogOut(result, returnArgs, success) {
			if (success == true && result[0] == "success") {
				// 用户已经注销的标示,之所以设置此标示是因为下句函数调用中window.location.href的重新设置会调用到onbeforeunload,由于此时session
				// 已经validate了,所以在lfwCoreController中会抛出"客户端传入了不正确的pageId:"
				window.$logoutflag = true;
				gotoLoginPageFuc();
			} else
				showMessageDialog("${ml:transp('lfw','lfw_main006')}");
		}
	}

	function changeImage() {
	}

	function doHelp() {
	}

	/**
	 * 根据功能节点号打开单据
	 * @param funCode 功能节点号
	 */
	function openBill(funCode) {
		if (funCode == null || funCode == "")
			return;

		// 输入不允许有中文
		if (funCode.lengthb() != funCode.length || !isDigital(funCode)) {
			showWarningDialog("${ml:transp('lfw','lfw_main007')}");
			return;
		}

		if (funCode.length >= 2) {
			var ds = getDataset("ds_" + funCode.substring(0, 2));
			if (ds != null) {
				for ( var i = ds.getRowCount() - 1; i >= 0; i--) {
					if (ds.getRow(i).getCellValue(0) == funCode) {
						var dsRow = ds.getRow(i);
						ds.setRowSelected(ds.getRowIndex(dsRow));
						openNode(funCode, dsRow.getCellValue(ds
								.nameToIndex("linkURL")), null, {
							title :dsRow.getCellValue(ds.nameToIndex("label"))
						});
						break;
					}
				}
			}
		}
	}

	/**
	 * openNode
	 * @param name 打开tabitem的funcode（功能注册编码）
	 * @param src iframe中的
	 * @param funcode 打开页面的pageID
	 * @param attrArr 自定义属性,例如{title:'',funProperty:''}
	 */
	function openNode(funcode, src, attrArr) {
		//cleanBillMsg();
		if (src == null)
			return;
		src = window.corePath + "/" + src;
		var title = null;
		if (attrArr != null && attrArr.title != null)
			title = attrArr.title;
		else
			title = funcode;//默认显示的是功能注册编码

		//默认以tab多页签打开
		var openmode = getConfigAttribute("openNodeMode");
		if (openmode == null || openmode == '') {
			openmode = "0";
		}
		// 增加节点点击频率
		addFunNodeFrequency(funcode);

		switch (openmode) {
		case "0":
			// 最多打开几个页签后提示(默认为5个)
			var openTabMax = null;
			if (attrArr != null && attrArr.openTabMax != null)
				openTabMax = attrArr.openTabMax;
			else
				openTabMax = 5;

			var tab = getComponent("pageTab"), items = tab.getTabItems(), count = items.length;
			for ( var i = 0; i < count; i++) {
				// 如果已经打开过相同funcode的单据
				if (items[i].name == funcode) {
					tab.activeTab(tab.getItemIndex(items[i]));
					return;

				}
			}

			// 如果已经打开单据数超过设置的打开最大限度则进行提示
			if (count > openTabMax) {
				continueOpen.tab = tab;
				continueOpen.funcode = funcode;
				continueOpen.title = title;
				continueOpen.src = src;
				ConfirmDialogComp.showDialog(
						"${ml:transp('lfw','lfw_quickdesk001')}" + (count - 1)
								+ "${ml:transp('lfw','lfw_quickdesk002')}",
						continueOpen, null, null, null);
			} else {
				var item1 = tab.createItem(funcode, title, true);
				tab.activeTab(tab.getItemIndex(item1));
				// 显示"加载图片"
				showLoadingDiv(item1.getObjHtml());
				if (div != null)
					showBillMsg();
				item1.getObjHtml().appendChild(getNodeFrame(funcode, src));
			}
			break;
		case "1":
			var tab = getComponent("pageTab");
			var items = tab.getTabItems();
			var count = items.length;
			if (count == 2) {
				var iframe = items[1].getObjHtml().firstChild.firstChild;
				iframe.src = src;
				items[1].changeTitle(title);
				tab.activeTab(1);
			} else {
				var item1 = tab.createItem(funcode, title, true);
				tab.activeTab(1);
				var div = getNodeFrame(funcode, src);
				item1.getObjHtml().appendChild(div);
				showBillMsg();
			}
			break;
		case "2":
			window
					.open(
							src,
							title,
							"toolbar=no,menubar=no,scrollbar=yes,resizable=yes,location=no,status=no,fullscreen=no");
			showBillMsg();
			break;
		default:
			break;
		}
	};

	function addFunNodeFrequency(funcode) {
		var cmd = new CommonCommand('addFunNodeFrequency');
		cmd.setAsync(true);
		cmd.setParameter("funcode=" + funcode);
		cmd.setReturnFunc(returnFuc);
		cmd.exec();

		function returnFuc(result, returnArgs, success) {
		}
	};

	/**
	 * 显示单据加载完毕前的loading图片
	 */
	function showLoadingDiv(parentHtml) {
		if (parentHtml == null)
			return;

		if (window.$loadingDiv == null) {
			window.$loadingDiv = $ce("div");
			window.$loadingDiv.style.position = "absolute";
			window.$loadingDiv.style.display = "none";
			window.$loadingDiv.style.left = "0";
			window.$loadingDiv.style.top = "0";
			window.$loadingDiv.style.width = "100%";
			window.$loadingDiv.style.height = "100%";
			window.$loadingDiv.style.background = "#ffffff url('/lfw/frame/themes/webclassic/images/common/loading.gif') no-repeat center center";
		}
		parentHtml.appendChild(window.$loadingDiv);
		window.$loadingDiv.style.display = "block";
	};

	/**
	 * @private
	 */
	function getNodeFrame(funcode, src) {
		var div = $ce("DIV");
		div.id = "tab_item_div_" + funcode;
		div.style.top = "0px";
		div.style.left = "0px";
		div.style.height = "100%";
		div.style.width = "100%";

		var frame = document.createElement("iframe");
		frame.style.top = "0px";
		frame.style.left = "0px";
		frame.style.height = "100%";
		frame.style.width = "100%";
		frame.style.border = "0";
		if (IS_FF)
			frame.style.background = "#FFFFFF";
		frame.frameBorder = "0";
		frame.src = src;
		div.appendChild(frame);
		return div;
	};

	/**
	 * @private
	 */
	function continueOpen(tab, funcode, title, src) {
		var tab = null;
		var funcode = null;
		var title = null;
		var src = null;
		if (continueOpen.tab != null)
			tab = continueOpen.tab;
		if (continueOpen.funcode != null)
			funcode = continueOpen.funcode;
		if (continueOpen.title != null)
			title = continueOpen.title;
		if (continueOpen.src != null)
			src = continueOpen.src;

		var item1 = tab.createItem(funcode, title, true);
		tab.activeTab(tab.getItemIndex(item1));
		var div = getNodeFrame(funcode, src);
		item1.getObjHtml().appendChild(div);
		delete (continueOpen.tab);
		delete (continueOpen.funcode);
		delete (continueOpen.title);
		delete (continueOpen.src);
	};

	function toClose() {
		window.opener = null;
		window.open("", "_self");
		window.close();
	}

	//function quickLogin()
	//{
	//	var url = getCorePath() + "/mainpage/quicklogin.jsp?pageId=login.pagemeta&rand=" + (Math.random()*10000).toString().substring(0, 4);
	//	showDialog(url , "快速切换", "400", "350", "switch_dialog", true,true);		
	//}

	/**
	 *打开日志
	 */
	//function openLog()
	//{
	//	var url = getCorePath() + "/mainpage/log.jsp?pageId=lfwmainmenu.log.pagemeta&rand=" + (Math.random()*10000).toString().substring(0, 4);
	//	showDialog(url , "日志信息", "700", "500", "log_dialog", true,true);
	//}
	
	/**
	 * 修改配置
	 */
	function modifyConfig() {
		var url = getCorePath() + "/uimeta.um?pageId=userprefs&rand="
				+ (Math.random() * 10000).toString().substring(0, 4);
		showDialog(url, "${ml:transp('lfw','lfw_main010')}", "340", "300",
				"modifyconfig", false, false);
	};

	/**
	 * 根据key得到在cookie中相应的value
	 */
	function getCookieById(name) {
		var allCookie = document.cookie;
		var pos = allCookie.indexOf("INVOCATION_KEY=");
		var value = null;
		if (pos != -1) {
			var start = pos + 15;
			var end = allCookie.indexOf(";", start);
			if (end == -1)
				end = allCookie.length;
			value = allCookie.substring(start, end);
		}
		var v = value.split("$");
		if (name == "accountCode")
			return v[0];
		else if (name == "usercode")
			return v[2];
		else if (name == "userType")
			return v[9];
		else if (name == "datasource")
			return v[7];
		else if (name == "accountName")
			return v[8];
	};

	function openBillNode(row) {
		if (openBillNode.mode == null)
			openBillNode.mode = getConfigAttribute("openNodeMode");
		openNode(row.getCellValue(0), row.getCellValue(2), openBillNode.mode, {
			title :row.getCellValue(1),
			openTabMax :5
		});
	};

	window.isFunNodeListHide = false;
	function showHideList() {
		if (isFunNodeListHide == null || isFunNodeListHide == false) {
			//$ge('funnodeListTr').style.display = "none";
			//TODO guoweic 隐藏功能，暂时只能这样写
			//$ge('$d_grid_layout_leftArea_4').style.height = "0px";
			$ge('$d_grid_layout_leftArea_4').style.display = "none";
			$ge('$d_grid_layout_leftArea_2').style.height = $ge('$d_grid_layout_leftArea_2').offsetHeight + 200 + "px";

			$ge('funnodeShowImg').src = window.nodeImagePath + "/normal/a.png";
			$ge('funnodeShowText').replaceChild(document.createTextNode("显示"), $ge('funnodeShowText').firstChild);
			isFunNodeListHide = true;
		} else {
			//$ge('funnodeListTr').style.display = "block";
			//TODO guoweic 隐藏功能，暂时只能这样写
			$ge('$d_grid_layout_leftArea_2').style.height = $ge('$d_grid_layout_leftArea_2').offsetHeight - 200 + "px";
			//$ge('$d_grid_layout_leftArea_4').style.height = "200px";
			$ge('$d_grid_layout_leftArea_4').style.display = "block";

			$ge('funnodeShowImg').src = window.nodeImagePath + "/normal/b.png";
			$ge('funnodeShowText').replaceChild(document.createTextNode("隐藏"), $ge('funnodeShowText').firstChild);
			isFunNodeListHide = false;
		}
	}

	function changeHideImage(img, state) {
		if (isFunNodeListHide == null || isFunNodeListHide == false) {
			if (state == 0)
				img.src = window.nodeImagePath + "/normal/b.png";
			else
				img.src = window.nodeImagePath + "/normal/b-over.png";
		} else {
			if (state == 0)
				img.src = window.nodeImagePath + "/normal/a.png";
			else
				img.src = window.nodeImagePath + "/normal/a-over.png";
		}
	}
	
	
	
	function onImgContextMenu(id, funcode, funname, url) {
		// 打开菜单
		var img = document.getElementById(id);
		img.oncontextmenu = function(e) {
			//img.setAttribute("funCode", funcode);
			//img.setAttribute("funName", funname);
			if(!e)
				e = window.event || e;
			e.triggerId = funcode;
			// 打开菜单
			window.$c_navwidget_shortcut_menu.show(e);
			 var proxy = new ServerProxy(null,null,true);
			 //将参数传递到后台
			 proxy.addParam("listener_class", "nc.uap.lfw.ncadapter.main.MainDelShortCutListener");
			 //传递要删除的快捷方式的funcode
			 proxy.addParam("funcode",funcode);
			 var sbr = new SubmitRule();  
			 proxy.setSubmitRule(sbr); 
			 proxy.execute();
		};
	}
	
	/**
	 * 重新显示快捷方式
	 */
	function reloadShortCut() {
		var ds = pageUI.getWidget("navwidget").getDataset("shortcutds");
		var rows = ds.getAllRows();
		var container = $ge("shortCutDiv");
		// 清空
		container.innerHTML = "";
		for (var i = 0, n = rows.length; i < n; i++) {
			var funCode = rows[i].getCellValue(1);
			var name = rows[i].getCellValue(6);
			var funUrl = rows[i].getCellValue(7);
			
			var shortCutDiv = $ce("div");
			shortCutDiv.align = "center";
			shortCutDiv.style.height = "70px";
			shortCutDiv.style.width = "100%";
			shortCutDiv.style[ATTRFLOAT] = "left";

			var img = $ce("img");
			img.align = "absmiddle";
			img.style.height = "50px";
			img.style.width = "50px";
			img.style.cursor = "pointer";
			
			//根据位置修改图片
			var currentIndex = i%5;
			//TODO 修改图片
			img.src = "	html/nodes/main_webclassic/themes/webclassic/images/funnodes/node" + eval(currentIndex) + ".png";

			img.setAttribute("funCode", funCode);
			img.setAttribute("funName", name);
			img.setAttribute("rowIndex", i);
			img.setAttribute("funUrl", funUrl);

			// 双击事件
			img.ondblclick = function(e) {
				var funCode = this.getAttribute("funCode");
				var funName = this.getAttribute("funName");
				var funUrl = this.getAttribute("funUrl");
				// 打开页面
				openNewPage(funCode, funName, funUrl);
			};

			// 菜单事件
			img.oncontextmenu = function(e) {
				// Dataset选中对应行
				var rowIndex = this.getAttribute("rowIndex");
				var widget = pageUI.getWidget("navwidget")
				var ds = widget.getDataset("shortcutds");
				ds.setRowSelected(rowIndex);
				
				// 打开菜单
				window.$c_navwidget_shortcut_menu.show(e);
			};

			var textDiv = $ce("div");
			textDiv.align = "center";
			textDiv.style.height = "15px";
			textDiv.style.width = "100%";
			textDiv.style.top = "5px";
			textDiv.style.position = "relative";
			textDiv.innerHTML = name;

			shortCutDiv.appendChild(img);
			shortCutDiv.appendChild(textDiv);
			container.appendChild(shortCutDiv);
		}
	}

	/**
	 * 通过快捷方式打开页面
	 */
	function openNewPage(funcode, funname, url) {
		var ds = pageUI.getWidget("navwidget").getDataset("shortcutds");
		var rows = ds.getAllRows();
		//var url = "";
		// 获取对应URL（url是否应提前保存到数据库中？）
		//for (var i = 0, n = rows.length; i < n; i++) {
		//	var row = rows[i];
		//	if (row.getCellValue(ds.nameToIndex("fun_code")) == funcode) {
		//		url = row.getCellValue(ds.nameToIndex("ext1"));
		//		break;
		//	}
		//}
	 	var title = funname;
	 	var src = window.corePath + "/" + url;
	 	var openTabMax = 5;
	 	var widget = pageUI.getWidget("mainwidget");
		var tab = pageUI.getTab("pagetab");
		var items = tab.getTabItems();
		var count = items.length;
		for (var i = 0; i < count; i++) {
			// 如果已经打开过相同funcode的单据
			if (items[i].name == funcode) {
				tab.activeTab(tab.getItemIndex(items[i]));
				return;
			}
		}

		// 如果已经打开单据数超过设置的打开最大限度则进行提示
		if (count > openTabMax) 
		{
			continueOpen.tab = tab;
			continueOpen.funcode = funcode;
			continueOpen.title = title;
			continueOpen.src = src;
			ConfirmDialogComp.showDialog("已经打开了 " + (count - 1)
							+ "个布局,继续打开将影响客户端速度,确定打开吗?",
					continueOpen, null, null, null);
		}
		else {
			var item1 = tab.createItem(funcode, title, true);
			tab.activeTab(tab.getItemIndex(item1));
			item1.getObjHtml().appendChild(getNodeFrame(funcode, src));
		}			
					
		function continueOpen()
		{
			var	tab = continueOpen.tab;
			var	funcode = continueOpen.funcode;
			var	title = continueOpen.title;
			var	src = continueOpen.src;
			var item1 = tab.createItem(funcode, title, true);
			tab.activeTab(tab.getItemIndex(item1));
			var div = getNodeFrame(funcode, src);
			item1.getObjHtml().appendChild(div);
			delete(continueOpen.tab);
			delete(continueOpen.funcode);
			delete(continueOpen.title);
			delete(continueOpen.src);
		};
		
		function getNodeFrame(funcode,src)
		{
			var div = $ce("DIV");
		    div.id = "tab_item_div_" + funcode;
		    div.style.top = "0px";
		    div.style.left = "0px";                        
		    div.style.height = "100%";
		    div.style.width = "100%";
		    
		    var frame=document.createElement("iframe");
			frame.style.top = "0px";
		    frame.style.left = "0px";                        
		    frame.style.height = "100%";
		    frame.style.width = "100%";
		    frame.style.border = "0";
		    if(IS_FF)
		    	frame.style.background = "#FFFFFF";
		    frame.frameBorder = "0";
			frame.src=src;
			div.appendChild(frame);
			return div;
		};
	}
