/**
 *	@fileoverview Layout布局自动调整
 *	@author guoweic
 *	@version 
 */

// window的resize事件执行方法集合
var layoutResizeMap = new HashMap();

// layout的初始化执行方法集合
var layoutInitMap = new HashMap();

/**
 * 给指定对象增加onresize方法
 * @private
 */
function addResizeEvent(obj, func, bUndoResizeFunc) {
	if (!obj)
		return;
	if (bUndoResizeFunc) {  // 如果不执行窗口大小变化后的布局调整方法
		layoutInitMap.put(obj.id, func);
	} else {
		if (IS_IE) {  // IE浏览器
			EventUtil.addEventHandler(obj, "resize", function(){func.call(obj)});
		} 
		else if (!IS_IE) {  // firefox浏览器
			layoutResizeMap.put(obj.id, func);
		}
		layoutInitMap.put(obj.id, func);
	}
};

function addAndCallResizeEvent(obj,func){
	addResizeEvent(obj,func);
	func.call(obj);
}


// 开发人员自定义的window.onresize方法集合
var windowResizeArray = new Array;

/**
 * 开发人员增加自定义的window.onresize方法
 * @param func
 * @return
 * @private
 */
function addWindowResize(func) {
	windowResizeArray.add(func);
};

//给window增加onresize方法
window.onresize = windowResizeFunc;

/**
 * 窗口大小变化后执行方法
 * @return
 * @private
 */
function windowResizeFunc() {
	var e = EventUtil.getEvent();
	if (!IS_IE) {  // firefox浏览器
		var keySet = layoutResizeMap.keySet();
		for (var i = 0; i < keySet.length; i++) {
			var id = keySet[i];
			var obj = $ge(id);
			var func = layoutResizeMap.get(id);
			func.call(obj, e);
		}
	}
	// 执行开发人员自定义的window.onresize方法
	for (var i = 0; i < windowResizeArray.length; i++) {
		windowResizeArray[i].call(e);
	}
	// 删除事件对象（用于清除依赖关系）
	clearEvent(e);
};

/**
 * 布局初始化方法
 * @return
 * @private
 */
function layoutInitFunc() {
	var keySet = layoutInitMap.keySet();
	for (var i = 0; i < keySet.length; i++) {
		var id = keySet[i];
		var obj = $ge(id);
		var func = layoutInitMap.get(id);
		func.call(obj);
	}
};

/**
 * IE6中调整容器内部DIV高度
 * @param oContainer
 * @param height
 * @return
 * @private
 */
function resizeChildDiv(oContainer, height) {
	var arrChild = oContainer.childNodes;
	for (var i = 0; i < arrChild.length; i++) {
		if (arrChild[i].nodeName == "DIV") {
			if (arrChild[i].style.height == "100%") {
				// 设置是高度100%的div标志
				arrChild[i].isMaxSizeDiv = true;
			}
			if (arrChild[i].isMaxSizeDiv) {
				arrChild[i].style.height = height;
			}
		}
	}
};

/**
 * border的onresize方法
 * @param e
 * @return
 * @private
 */
function borderResize(e) {
	var oBorder = this;
	var id = oBorder.id;
	
	var oBorderCenter = $ge(id + "_center");
	var newWidth = oBorder.offsetWidth;
	var newHeight = oBorder.offsetHeight;
	if (newWidth != oBorder.oldWidth) {
		var oBorderLeft = $ge(id + "_left");
		var oBorderRight = $ge(id + "_right");
		var width = oBorder.offsetWidth - (oBorderLeft ? oBorderLeft.offsetWidth : 0) - (oBorderRight ? oBorderRight.offsetWidth : 0);
		if (!IS_IE || IS_IE8)  // 处理firefox和IE8宽度显示不正确问题
			width -= 0.5;
		if (width < 0)
			width = 0;
		oBorderCenter.style.width = width + "px";
	}
	if (newHeight != oBorder.oldHeight) {
		var oBorderMiddle = $ge(id + "_middle");
		var oBorderInner = $ge(id + "_inner");
		var oBorderTop = $ge(id + "_top");
		var oBorderBottom = $ge(id + "_bottom");
		var height = oBorder.offsetHeight - (oBorderTop ? oBorderTop.offsetHeight : 0) - (oBorderBottom ? oBorderBottom.offsetHeight : 0);
		if (height < 0)
			height = 0;
		oBorderMiddle.style.height = height + "px";
		if (oBorderInner)
			oBorderInner.style.height = height + "px";
	}
	oBorder.oldWidth = oBorder.offsetWidth;
	oBorder.oldHeight = oBorder.offsetHeight;
};

function borderResizeEdit(e) {
	var borderWidth = 1;
	var marginWidth = 1;
	var paddingWidth = 3;
	
	var otherWidth = marginWidth * 2 + paddingWidth * 2 + borderWidth * 2;
	
	var edit = "_raw";
	var oBorder = this;
	var id = oBorder.id;
	var oBorderRaw = $ge(id + edit);	
	
	var oBorderCenter = $ge(id + "_center");
	var oBorderCenterRaw = $ge(id + "_center" + edit);
	
	var newWidth = oBorder.offsetWidth;
	var newHeight = oBorder.offsetHeight;
	if (newWidth != oBorder.oldWidth) {
		var oBorderLeft = $ge(id + "_left");		
		var oBorderLeftRaw = $ge(id + "_left" + edit);
		
		var oBorderRight = $ge(id + "_right");
		var oBorderRightRaw = $ge(id + "_right" + edit);
		
		var oBorderWidth = oBorderRaw.offsetWidth - otherWidth;	
		if(oBorderWidth < 0) oBorderWidth = 0;
		
		var centerWidthRaw = oBorderWidth - (oBorderLeftRaw ? oBorderLeftRaw.offsetWidth : (oBorderLeft?oBorderLeft.offsetWidth:0)) - (oBorderRightRaw ? oBorderRightRaw.offsetWidth : (oBorderRight?oBorderRight.offsetWidth:0));	
		if (!IS_IE || IS_IE8)  // 处理firefox和IE8宽度显示不正确问题
			centerWidthRaw -= 0.5;
		if (centerWidthRaw < 0)
			centerWidthRaw = 0;
		if(oBorderCenterRaw){
			oBorderCenterRaw.style.width = centerWidthRaw + "px";
			var centerWidth = centerWidthRaw - otherWidth;
			if(centerWidth < 0) centerWidth = 0;
				oBorderCenter.style.width = centerWidth + "px";
		}else{
			oBorderCenter.style.width = centerWidthRaw + "px";
		}
		
		
		oBorder.style.width = oBorderWidth + "px";		
		
		if(oBorderLeftRaw){
			var leftWidth = oBorderLeftRaw.offsetWidth - otherWidth;
			if(leftWidth < 0) leftWidth = 0;
			oBorderLeft.style.width = leftWidth + "px";
		}		
		if(oBorderRightRaw){
			var rightWidth = oBorderRightRaw.offsetWidth -otherWidth;
			if(rightWidth < 0) rightWidth = 0;
			oBorderRight.style.width = rightWidth + "px";
		}	
	}
	if (newHeight != oBorder.oldHeight) {
		var oBorderMiddle = $ge(id + "_middle");
		var oBorderInner = $ge(id + "_inner");
		var oBorderTop = $ge(id + "_top");
		var oBorderBottom = $ge(id + "_bottom");
		var height = oBorder.offsetHeight - (oBorderTop ? oBorderTop.offsetHeight : 0) - (oBorderBottom ? oBorderBottom.offsetHeight : 0);
		if (height < 0)
			height = 0;
		oBorderMiddle.style.height = height + "px";
		if (oBorderInner)
			oBorderInner.style.height = height + "px";
	}
	oBorder.oldWidth = oBorder.offsetWidth;
	oBorder.oldHeight = oBorder.offsetHeight;
};

/**
 * flowh的onresize方法
 * @param e
 * @return
 * @private
 */
function flowhResize(e) {
	var oFlow = this;
	var id = oFlow.id;
	
	var newWidth = oFlow.offsetWidth;
	if (newWidth != oFlow.oldWidth) {
		var arrKnownWidthItem = new Array();
		var arrUnknownWidthItem = new Array();
		
		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(id) != -1) {  // 是flowh的子项
				//if (oFlowItem.style.width && oFlowItem.style.width != "") {  // 设置了宽度
				if (oFlowItem.getAttribute("haswidth") == "1") {  // 设置了宽度
					arrKnownWidthItem.push(oFlowItem);
				} else {
					arrUnknownWidthItem.push(oFlowItem);
				}
			}
		}
		
		var totalWidth = oFlow.offsetWidth;
		for (var i = 0; i < arrKnownWidthItem.length; i++) {
			totalWidth -= arrKnownWidthItem[i].offsetWidth;
		}
		var width = totalWidth / (arrUnknownWidthItem.length == 0 ? 1 : arrUnknownWidthItem.length);
		// width取整数
		width = parseInt(width);
		
		for (var i = 0, n = arrUnknownWidthItem.length; i < n; i++) {
			if (i == n-1) {
				width = (totalWidth - width * (arrUnknownWidthItem.length - 1));
				if (!IS_IE || IS_IE8)  // 处理firefox和IE8宽度显示不正确问题
					width -= 1;
			}
			if (width < 0)
				width = 0;
			arrUnknownWidthItem[i].style.width = width + "px";
			if (arrUnknownWidthItem[i].offsetHeight == 0 ){
              arrUnknownWidthItem[i].style.height = oFlow.offsetHeight+"px";
            }
		}
	}
	oFlow.oldWidth = oFlow.offsetWidth;
};


function flowhResizeEdit(e) {
	var borderWidth = 1;
	var marginWidth = 1;
	var paddingWidth = 3;
	
	var otherWidth = marginWidth * 2 + paddingWidth * 2 + borderWidth * 2;
	
	var oFlow = this;
	var id = oFlow.id;
	
	var newWidth = oFlow.offsetWidth;
	if (newWidth != oFlow.oldWidth) {
		var arrKnownWidthItem = new Array();
		var arrUnknownWidthItem = new Array();
		
		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(id) != -1) {  // 是flowh的子项
				//if (oFlowItem.style.width && oFlowItem.style.width != "") {  // 设置了宽度
				if (oFlowItem.getAttribute("haswidth") == "1") {  // 设置了宽度
					arrKnownWidthItem.push(oFlowItem);
				} else {
					arrUnknownWidthItem.push(oFlowItem);
				}
			}
		}
		
		var totalWidth = oFlow.offsetWidth;
		for (var i = 0; i < arrKnownWidthItem.length; i++) {
			totalWidth -= (arrKnownWidthItem[i].offsetWidth + otherWidth);
		}
		var width = totalWidth / (arrUnknownWidthItem.length == 0 ? 1 : arrUnknownWidthItem.length);
		// width取整数
		width = parseInt(width);
		
		for (var i = 0, n = arrUnknownWidthItem.length; i < n; i++) {
			if (i == n-1) {
				width = (totalWidth - width * (arrUnknownWidthItem.length - 1) - otherWidth);
				if (!IS_IE || IS_IE8)  // 处理firefox和IE8宽度显示不正确问题
					width -= 1;
			}
			if (width < 0)
				width = 0;
			arrUnknownWidthItem[i].style.width = width + "px";
		}
	}
	oFlow.oldWidth = oFlow.offsetWidth;
};

/**
 * flowv的onresize方法
 * @param e
 * @return
 * @private
 */
function flowvResize(e) {
	var oFlow = this;
	var id = oFlow.id;
	
	var newHeight = oFlow.offsetHeight;
	if (newHeight != oFlow.oldHeight) {
		var arrKnownHeightItem = new Array();
		var arrUnknownHeightItem = new Array();
		
		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(id) != -1) {  // 是flowv的子项
				if (oFlowItem.getAttribute("hasheight") == "1") {  // 设置了高度
					arrKnownHeightItem.push(oFlowItem);
				} else {
					arrUnknownHeightItem.push(oFlowItem);
				}
			}
		}
		
		var totalHeight = oFlow.offsetHeight;
		for (var i = 0; i < arrKnownHeightItem.length; i++) {
			totalHeight -= arrKnownHeightItem[i].offsetHeight;
		}
		var height = totalHeight / (arrUnknownHeightItem.length == 0 ? 1 : arrUnknownHeightItem.length);
		// height取整数
		height = parseInt(height);
		
		for (var i = 0, n = arrUnknownHeightItem.length; i < n; i++) {
			if (i == n-1) {
				height = (totalHeight - height * (arrUnknownHeightItem.length - 1));
			}
			if (height < 0)
				height = 0;
			arrUnknownHeightItem[i].style.height = height + "px";
			if (IS_IE6) {  // 如果是IE6，调整内部div高度
				resizeChildDiv(arrUnknownHeightItem[i], height + "px");
			}
		}
	}
	oFlow.oldHeight = oFlow.offsetHeight;
};

/**
 * 将滚动条移至锚点
 * @param id FlowvLayout的id
 * @param anchor 锚点名称
 * @param widgetId 所属Widget的ID
 * @return
 * @private
 */
function scrollToAnchor(id, anchor, widgetId) {
	// 真实ID
	if (id == null || id == "")
		return;
	var divId = "$d_" + (widgetId == null || widgetId == "" ? "" : (widgetId + "_")) + id;
	// FlowvLayout的Div对象
	var oFlow = $ge(divId);
	if (oFlow && anchor != null && anchor != "") {
		// 判断是否出现纵向滚动条
		if (isDivVScroll(oFlow)) {
			var scrollTop = 0;
			for (var i = 0; i < oFlow.childNodes.length; i++) {
				var oFlowItem = oFlow.childNodes[i];
				if (oFlowItem.id && oFlowItem.id.indexOf(divId) != -1) {  // 是flowv的子项
					if (oFlowItem.getAttribute("anchor") == anchor) {  // 是指定锚点的子项
						// 设置滚动条位置
						oFlow.scrollTop = scrollTop;
					} else {
						// 计算滚动条位置
						scrollTop += oFlowItem.offsetHeight;
					}
				}
			}
		}
	}
};

/**
 * 获取FlowvLayout指定的Panel的Div对象(必须是指定了锚点的Panel)
 * @param id FlowvLayout的id
 * @param anchor 锚点名称
 * @param widgetId 所属Widget的ID
 * @return
 * @private
 */
function getFlowvPanel(id, anchor, widgetId) {
	// 真实ID
	if (id == null || id == "")
		return null;
	var divId = "$d_" + (widgetId == null || widgetId == "" ? "" : (widgetId + "_")) + id;
	// FlowvLayout的Div对象
	var oFlow = $ge(divId);
	if (oFlow && anchor != null && anchor != "") {
		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(divId) != -1) {  // 是flowv的子项
				if (oFlowItem.getAttribute("anchor") == anchor) {  // 是指定锚点的子项
					return oFlowItem;
				}
			}
		}
	}
};

/**
 * grid_layout的onresize方法
 * @param e
 * @return
 * @private
 */
function gridLayoutResize(e) {
	// 根DIV
	var oGridLayout = this;
	var id = oGridLayout.id;
	// 列数
	var colCount = oGridLayout.getAttribute("colcount");
	// 行数
	//var rowCount = oGridLayout.getAttribute("rowcount");
	var newHeight = oGridLayout.offsetHeight;
	var newWidth = oGridLayout.offsetWidth;
	if (newHeight != oGridLayout.oldHeight) {  // 高度改变
		// 已知行高度DIV集合
		var arrKnownHeightItem = new Array();
		// 未知行高度DIV集合
		var arrUnknownHeightItem = new Array();
		
		for (var i = 0; i < oGridLayout.childNodes.length; i++) {
			var oGridLayoutItem = oGridLayout.childNodes[i];
			if (oGridLayoutItem.id && oGridLayoutItem.id.indexOf(id) != -1 
					&& oGridLayoutItem.getAttribute("hasheight") && oGridLayoutItem.getAttribute("hasheight") != "") {  // 是行DIV
				if (oGridLayoutItem.getAttribute("hasheight") == "1") {  // 设置了高度
					arrKnownHeightItem.push(oGridLayoutItem);
				} else {
					arrUnknownHeightItem.push(oGridLayoutItem);
				}
			}
		}
		
		var totalHeight = oGridLayout.offsetHeight;
		for (var i = 0; i < arrKnownHeightItem.length; i++) {
			totalHeight -= arrKnownHeightItem[i].offsetHeight;
		}
		var height = totalHeight / arrUnknownHeightItem.length;
		// height取整数
		height = parseInt(height);
		
		for (var i = 0, n = arrUnknownHeightItem.length; i < n; i++) {
			if (i == n-1) {
				height = (totalHeight - height * (arrUnknownHeightItem.length - 1));
			}
			if (height < 0)
				height = 0;
			arrUnknownHeightItem[i].style.height = height + "px";
		}
	}
	if (newWidth != oGridLayout.oldWidth) {  // 宽度改变
		// 单元格DIV集合
		//var arrCellItem = new Array();
		// 已知列宽度DIV集合
		var arrKnownWidthItem = new Array();
		// 未知列宽度DIV集合
		var arrUnknownWidthItem = new Array();
		// 已知宽度DIV列号
		var arrKnownItemNum = new Array();
		// 未知宽度DIV列号
		var arrUnknownItemNum = new Array();
		
		for (var j = 0; j < oGridLayout.childNodes.length; j++) {
			var oGridLayoutRowItem = oGridLayout.childNodes[j];
			if (oGridLayoutRowItem.id && oGridLayoutRowItem.id.indexOf(id) != -1 
					&& oGridLayoutRowItem.getAttribute("hasheight") && oGridLayoutRowItem.getAttribute("hasheight") != "") {  // 是行DIV
				for (var i = 0; i < oGridLayoutRowItem.childNodes.length; i++) {
					var oGridLayoutItem = oGridLayoutRowItem.childNodes[i];
					if (oGridLayoutItem.id && oGridLayoutItem.id.indexOf(id) != -1 
							&& oGridLayoutItem.getAttribute("haswidth") && oGridLayoutItem.getAttribute("haswidth") != "") {  // 是单元格DIV
						//arrCellItem.push(oGridLayoutItem);
						if (oGridLayoutItem.getAttribute("haswidth") == "1") {  // 设置了宽度
							arrKnownWidthItem.push(oGridLayoutItem);
							if (arrKnownItemNum.indexOf(i) == -1) 
								arrKnownItemNum.push(i);
						} else {
							arrUnknownWidthItem.push(oGridLayoutItem);
							if (arrUnknownItemNum.indexOf(i) == -1) 
								arrUnknownItemNum.push(i);
						}
					}
				}
			}
		}
		
		var totalWidth = oGridLayout.offsetWidth;
		for (var i = 0; i < arrKnownItemNum.length; i++) {
			totalWidth -= arrKnownWidthItem[i].offsetWidth;
		}
		var width = totalWidth / (arrUnknownItemNum.length == 0 ? 1 : arrUnknownItemNum.length);
		// width取整数
		width = parseInt(width);
		
		// 给最后一个无宽度DIV的指定的宽度
		var lastWidth = (totalWidth - width * (arrUnknownItemNum.length - 1));
		lastWidth = lastWidth < 0 ? 0 : lastWidth;
		var specialWidth = (lastWidth - 0.5) < 0 ? 0 : (lastWidth - 0.5); 
		for (var i = 0, n = arrUnknownWidthItem.length; i < n; i++) {
			if ((i % arrUnknownItemNum.length) == 1) {  // 是每行最后一个无宽度DIV
				if (!IS_IE || IS_IE8)  // 处理firefox和IE8宽度显示不正确问题
					arrUnknownWidthItem[i].style.width = specialWidth + "px";
				else
					arrUnknownWidthItem[i].style.width = lastWidth + "px";
			} else {
				arrUnknownWidthItem[i].style.width = (width < 0 ? 0 : width) + "px";
			}
		}
	}
	oGridLayout.oldHeight = oGridLayout.offsetHeight;
	oGridLayout.oldWidth = oGridLayout.offsetWidth;
};

/**
 * cardLayout的onresize方法
 * @param e
 * @return
 * @private
 */
function cardResize(e) {
	var oCard = this;
	var childDivArray = oCard.childNodes;
	for (var i = 0, n = childDivArray.length; i < n; i++) {
		var childDiv = childDivArray[i];
		if (childDiv.nodeName.toLowerCase() == "div") {
			childDiv.style.width = oCard.offsetWidth + "px";
			childDiv.style.height = oCard.offsetHeight + "px";
		}
	}
};

