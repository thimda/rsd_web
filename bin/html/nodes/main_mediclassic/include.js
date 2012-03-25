/**
 * 增加单据页签
 * @param url 链接地址
 * @param title 标题
 * @param nodeId 唯一ID
 * @param refresh 重新打开时是否刷新页面
 * @return
 */
function openNode(url, title, nodeId, refresh) {
	var src = "";
	if (url.charAt(0)=="/" || url.indexOf("http")==0) 
		src = url;
	 else  
		src = window.corePath + "/" + url;
	var openTabMax = 5;
	var tab = pageUI.getTab("pagetab");
	var items = tab.getTabItems();
	var count = items.length;
	for (var i = 0; i < count; i++) {
		// 如果已经打开过相同nodeId的单据
		if (items[i].id == nodeId) {
			tab.activeTab(tab.getItemIndex(items[i]));
			// 刷新页面
			if (refresh) {
				items[i].getObjHtml().childNodes[0].childNodes[0].src = "";
				items[i].getObjHtml().removeChild(items[i].getObjHtml().childNodes[0]);
				items[i].getObjHtml().appendChild(getNodeFrame(nodeId, src));
			}
			return;
		}
	}

	// 如果已经打开单据数超过设置的打开最大限度则进行提示
	if (count > openTabMax) {
		continueOpen.tab = tab;
		continueOpen.funcode = nodeId;
		continueOpen.title = title;
		continueOpen.src = src;
		ConfirmDialogComp.showDialog("已经打开了 " + (count - 1)
						+ "个布局,继续打开将影响客户端速度,确定打开吗?",
				continueOpen, null, null, null);
	} else {
		var item1 = tab.createItem(nodeId, title, true);
		tab.activeTab(tab.getItemIndex(item1));
		item1.getObjHtml().appendChild(getNodeFrame(nodeId, src));
	}
};

function continueOpen() {
	var tab = continueOpen.tab;
	var funcode = continueOpen.funcode;
	var title = continueOpen.title;
	var src = continueOpen.src;
	var item1 = tab.createItem(funcode, title, true);
	tab.activeTab(tab.getItemIndex(item1));
	var div = getNodeFrame(funcode, src);
	item1.getObjHtml().appendChild(div);
	delete (continueOpen.tab);
	delete (continueOpen.funcode);
	delete (continueOpen.title);
	delete (continueOpen.src);
};

/**
 * 增加Tab子项关闭事件
 * @return
 */
function addTabListener() {
	var tab = pageUI.getTab("pagetab");
	var listener = new TabListener();
	listener.id = tab.id + "_close_listener";
	listener.closeItem = function(tabItemEvent) {
		var item = tabItemEvent.item;
		item.getObjHtml().childNodes[0].childNodes[0].src = "";
	};
	tab.addListener(listener);
};

function getNodeFrame(nodeId, src) {
	var div = $ce("DIV");
	div.id = "tab_item_div_" + nodeId;
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
	if (!IS_IE)
		frame.style.background = "#FFFFFF";
	frame.frameBorder = "0";
	frame.src = src;
	div.appendChild(frame);
	return div;
};


