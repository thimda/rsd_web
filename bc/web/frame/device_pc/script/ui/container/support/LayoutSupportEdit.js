/**
 * @fileoverview Layout布局自动调整
 * @author guoweic
 * @version
 */

// window的resize事件执行方法集合
var layoutResizeMap = new HashMap();

// layout的初始化执行方法集合
var layoutInitMap = new HashMap();

/**
 * 给指定对象增加onresize方法
 * 
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

function addResizeEventTable(obj, func, bUndoResizeFunc) {
	if (!obj)
		return;
	if (bUndoResizeFunc) { // 如果不执行窗口大小变化后的布局调整方法
		layoutInitMap.put(obj.id, func);
	} else {
		layoutResizeMap.put(obj.id, func);
		layoutInitMap.put(obj.id, func);
	}
};

// 开发人员自定义的window.onresize方法集合
var windowResizeArray = new Array;

/**
 * 开发人员增加自定义的window.onresize方法
 * 
 * @param func
 * @return
 * @private
 */
function addWindowResize(func) {
	windowResizeArray.add(func);
};

// 给window增加onresize方法
window.onresize = windowResizeFunc;

/**
 * 窗口大小变化后执行方法
 * 
 * @return
 * @private
 */
function windowResizeFunc() {
	var e = EventUtil.getEvent();
	var keySet = layoutResizeMap.keySet();
	for (var i = 0; i < keySet.length; i++) {
		var id = keySet[i];
		var obj = $ge(id);
		var func = layoutResizeMap.get(id);
		func.call(obj, e);
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
 * 
 * @return
 * @private
 */
function layoutInitFunc() {
	var keySet = layoutInitMap.keySet();
	for (var i = 0; i < keySet.length; i++) {
		var id = keySet[i];
		var obj = $ge(id);
		if (obj == null)
			continue;
		var func = layoutInitMap.get(id);
		func.call(obj);
	}
};

/**
 * IE6中调整容器内部DIV高度
 * 
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
// 调整容器内部div的高度和宽度
function resizeChildrenDiv(oContainer, otherHeight, otherWidth, unit) {
	var arrChild = oContainer.childNodes;
	for (var i = 0; i < arrChild.length; i++) {
		if (arrChild[i].nodeName == "DIV") {
			resizeWidth(oContainer, arrChild[i], otherWidth);
			resizeHeight(oContainer, arrChild[i], otherHeight);
			if (IS_IE6) {
				resizeChildDiv(arrChild[i], arrChild[i].newHeight + unit)
			}
		}
	}
}

// 仅调整容器内部div的高度和宽度
function resizeChildrenDivWidth(oContainer, otherWidth, unit) {
	var arrChild = oContainer.childNodes;
	for (var i = 0; i < arrChild.length; i++) {
		if (arrChild[i].nodeName == "DIV") {
			resizeWidth(oContainer, arrChild[i], otherWidth);
		}
	}
}

var borderWidth = 0; // 边宽
var marginWidth = 0; // 外边距宽
var paddingWidth = 0; // 内边距宽度
var otherWidth = marginWidth * 2 + paddingWidth * 2 + borderWidth * 2; // 内外层div
// 宽的差
var otherHeight = otherWidth + 2; // 内外层div 高的差
var edit = "_raw"; // 外层div的后缀
var unit = "px"

// 重新计算高度，以raw的高度为准，计算edit的新高度
// 并添加属性newHeight属性，为下面的元素使用
function resizeHeight(raw, edit, otherHeight) {
	var newHeight = 0;
	if (raw.newHeight) {
		var newHeight = raw.newHeight - otherHeight;
	} else {
		newHeight = raw.offsetHeight - otherHeight;
	}

	if (newHeight < 0) {
		newHeight = 0;
	}
	
	edit.newHeight = newHeight;
	edit.style.height = edit.newHeight + unit;

}
// 根据raw的宽度和高度，指定edit的高度和宽度
function resizeWidth(raw, edit, otherWidth) {
	var newWidth = 0;
	if (raw.newWidth) {
		newWidth = raw.newWidth - otherWidth;
	} else {
		newWidth = raw.offsetWidth - otherWidth;
	}
	if (newWidth < 0) {
		newWidth = 0;
	}
	edit.newWidth = newWidth;
	edit.style.width = edit.newWidth + unit;
	
}
// 计算可编辑的div的高度和宽度，借助于他上层div的高度和宽度
function editableDivResize(e) {
	var oThisDiv = this;
	initEditDivWidthHeight(oThisDiv);
}

// 根据其父节点的高度和宽度 计算可编辑div的高度和宽度 renxh
function initEditDivWidthHeight(oThisDiv) {
	var oThisDivRaw = $ge(oThisDiv.id + edit);
	if (oThisDivRaw) {
		oThisDivRaw.newWidth = oThisDivRaw.offsetWidth;
		oThisDivRaw.newHeight = oThisDivRaw.offsetHeight;
		if(oThisDivRaw.newWidth > 0){
			resizeWidth(oThisDivRaw, oThisDiv, otherWidth);
		}
		if(oThisDivRaw.newHeight > 0){
			resizeHeight(oThisDivRaw, oThisDiv, otherHeight);
		}
		
	} else {
		oThisDivRaw = oThisDiv.parentNode;
		if (oThisDivRaw) {
			if (oThisDivRaw.className == 'editable'
					|| oThisDivRaw.className == 'edit_selected'
					|| oThisDivRaw.className == 'edit_gridLayout'
					|| oThisDivRaw.className == 'edit_gridLayout_selected') {
				resizeWidth(oThisDivRaw, oThisDiv, otherWidth);
				resizeHeight(oThisDivRaw, oThisDiv, otherHeight);
			} 
			else {
				oThisDivRaw.newWidth = oThisDivRaw.offsetWidth;
				oThisDivRaw.newHeight = oThisDivRaw.offsetHeight;
				resizeWidth(oThisDivRaw, oThisDiv, otherWidth);
				resizeHeight(oThisDivRaw, oThisDiv, otherHeight);
			}
		} 
		else {
			oThisDiv.newWidth = oThisDiv.offsetWidth;
			oThisDiv.newHeight = oThisDiv.offsetHeight;
		}
	}
	// resizeChildrenDiv(oThisDiv, otherHeight, otherWidth, unit)
}

// 根据其父节点的高度和宽度 计算可编辑div的高度和宽度 renxh
function initEditDivWidth(oThisDiv) {
	var oThisDivRaw = $ge(oThisDiv.id + edit);
	if (oThisDivRaw) {
		oThisDivRaw.newWidth = oThisDivRaw.offsetWidth;
		if(oThisDivRaw.newWidth > 0){
			resizeWidth(oThisDivRaw, oThisDiv, otherWidth);
		}
		
	} else {
		oThisDivRaw = oThisDiv.parentNode;
		if (oThisDivRaw) {
			if (oThisDivRaw.className == 'editable'
					|| oThisDivRaw.className == 'edit_selected'
					|| oThisDivRaw.className == 'edit_gridLayout'
					|| oThisDivRaw.className == 'edit_gridLayout_selected') {
				resizeWidth(oThisDivRaw, oThisDiv, otherWidth);
			} 
			else {
				oThisDivRaw.newWidth = oThisDivRaw.offsetWidth;
				resizeWidth(oThisDivRaw, oThisDiv, otherWidth);
			}
		} 
		else {
			oThisDiv.newWidth = oThisDiv.offsetWidth;
		}
	}
}

function tableResize(e) {
	var table = this;
	var parent = table.parentNode;
	if (parent) {
		table.style.height = (parent.offsetHeight - otherHeight) + "px";
		table.style.width = (parent.offsetWidth - otherWidth) + "px";
		table.newWidth = (parent.offsetWidth - otherWidth);
		table.newHeight = parent.offsetHeight - otherHeight;
		var arrKnownHeightItem = new Array();
		var arrUnknownHeightItem = new Array();
		var allKnowHeight = 0;// 记录行已知高度的总和
		var trs = table.childNodes[0].childNodes;
		var avgHeight = (table.newHeight) / trs.length;
		for (var i = 0; i < trs.length; i++) {
			trs[i].style.height = (avgHeight - otherHeight) + 'px';
			trs[i].style.width = table.newWidth + 'px';
			trs[i].newHeight = (avgHeight - otherHeight);
			trs[i].newWidth = table.newWidth;
			cellWidthResize(trs[i]);
		}
	}

}

function tableResize2(e) {
	var table = this;
	var parent = table.parentNode;
	if (parent) {
		var haswidth = table.getAttribute("haswidth");
		var hasheight = table.getAttribute("hasheight");
		if (haswidth == '0') { // 没有指定宽度，需要计算
			var newWidth = parent.offsetWidth - otherWidth;
			table.setAttribute('width', newWidth);
			table.newWidth = newWidth;
		} else {
			table.newWidth = table.getAttribute("width");
		}
		if (hasheight == '0') {
			var newHeight = parent.offsetHeight - otherHeight;
			table.setAttribute('height', newHeight);
			table.newHeight = newHeight;
		} else {
			table.newHeight = table.getAttribute("height");;
		}

		var unknowHeight = new Array();
		var knowHeight = new Array();

		var trs = table.childNodes[0].childNodes;
		var allHeight = 0;
		for (var i = 0; i < trs.length; i++) {
			cellWidthResize2(trs[i], table);
			if (trs[i].getAttribute("hasheight") == '1') {
				var height = trs[i].getAttribute('height');
				allHeight += parseInt(height);
				knowHeight.push(trs[i]);
			} else {
				unknowHeight.push(trs[i]);
			}
		}
		var average = (table.newHeight - allHeight) / unknowHeight.length;
		var newHeight = average - otherHeight;
		for (var i = 0; i < unknowHeight.length; i++) {
			var cells = unknowHeight[i].childNodes;
			for (var j = 0; j < cells.length; j++) {
				cells[j].setAttribute('height', average - otherHeight);
			}
		}
	}

}
/**
 * 计算列的宽度
 * 
 * @param {}
 *            tr
 * @param {}
 *            table
 */
function cellWidthResize2(tr, table) {
	var allHeight = table.newHeight;
	var allWidth = table.newWidth;

	var cells = tr.childNodes;
	var cellKnowWidth = new Array();
	var cellUnknowWidth = new Array();
	var usedWidth = 0;
	var maxHeight = -1;
	for (var i = 0; i < cells.length; i++) { // 对宽度进行分类
		var cell = cells[i];
		var haswidth = cell.getAttribute("haswidth");
		if (haswidth == '1') {
			cellKnowWidth.push(cell);
			usedWidth += parseInt(cell.getAttribute("width"));
		} else {
			cellUnknowWidth.push(cell);
		}

		var hasheight = cell.getAttribute("hasheight");
		if (hasheight == '1') {
			var height = parseInt(cell.getAttribute("height"));
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
	}

	if (maxHeight > -1) {
		tr.setAttribute('hasheight', '1');
		tr.setAttribute('height', maxHeight);
	} else {
		tr.setAttribute('hasheight', '0');
	}
	var average = (allWidth - usedWidth) / cellUnknowWidth.length;
	if (average >= 0) {
		for (var i = 0; i < cellUnknowWidth.length; i++) {
			cellUnknowWidth[i].setAttribute('width', average);
		}
	}

}

function cellWidthResize(tr) {
	var cells = tr.childNodes;
	var avgWidth = tr.newWidth / cells.length;
	for (var i = 0; i < cells.length; i++) {
		cells[i].style.height = tr.newHeight + "px";
		cells[i].style.width = avgWidth + 'px';
	}
}

/**
 * border的onresize方法
 * 
 * @param e
 * @return
 * @private
 */
function borderResize(e) {

	// 先规定好外边的高度和宽度
	var oBorder = this;
	var id = oBorder.id;
	initEditDivWidthHeight(oBorder);

	// 处理三层层oBorder内部div的高度和宽度
	var oBorderMiddle = $ge(id + "_middle");
	var oBorderTop = $ge(id + "_top");
	var oBorderTopRaw = $ge(id + "_top" + edit);
	var oBorderBottom = $ge(id + "_bottom");
	var oBorderBottomRaw = $ge(id + "_bottom" + edit);

	// 顶部 高度处理
	if (oBorderTopRaw) {
		oBorderTopRaw.newHeight = oBorderTopRaw.offsetHeight; // 高度已经固定
		resizeHeight(oBorderTopRaw, oBorderTop, otherHeight);// 指定内部div的高度

		oBorderTopRaw.newWidth = oBorder.newWidth;// 指定宽度
		// oBorderTopRaw.style.width = oBorderTopRaw.newWidth + unit;
		resizeWidth(oBorderTopRaw, oBorderTop, otherWidth); // 指定内部div的宽度
	} else {
		if (oBorderTop) {
			oBorderTop.newHeight = oBorderTop.offsetHeight;
			oBorderTop.newWidth = oBorder.newWidth;
			// oBorderTop.style.width = oBorderTop.newWidth + unit;
		}
	}
	// 顶部高度处理完毕
	// 底部高度处理
	if (oBorderBottomRaw) {
		oBorderBottomRaw.newHeight = oBorderBottomRaw.offsetHeight;
		resizeHeight(oBorderBottomRaw, oBorderBottom, otherHeight);

		oBorderBottomRaw.newWidth = oBorder.newWidth;// 指定宽度
		// oBorderBottomRaw.style.width = oBorderBottomRaw.newWidth + unit;
		resizeWidth(oBorderBottomRaw, oBorderBottom, otherWidth); // 指定内部div的宽度
	} else {
		if (oBorderBottom) {
			oBorderBottom.newHeight = oBorderBottom.offsetHeight;
			oBorderBottom.newWidth = oBorder.newWidth;
		}
	}
	// 底部高度处理 完毕

	// 计算middle的高度
	var oBorderBottomHeight = oBorderBottomRaw
			? oBorderBottomRaw.newHeight
			: (oBorderBottom ? oBorderBottom.newHeight : 0);
	var oBorderTopHeight = oBorderTopRaw
			? oBorderTopRaw.newHeight
			: (oBorderTop ? oBorderTop.newHeight : 0);
	var middleHeightRaw = oBorder.newHeight - oBorderBottomHeight
			- oBorderTopHeight;

	if (oBorderMiddle) {
		oBorderMiddle.newHeight = middleHeightRaw;
		oBorderMiddle.style.height = middleHeightRaw + unit;

		oBorderMiddle.newWidth = oBorder.newWidth;// 指定宽度
		// oBorderMiddle.style.width = oBorderMiddle.newWidth + unit;
	}

	// 处理middle中 左 中 右的宽度
	var oBorderCenter = $ge(id + "_center");
	var oBorderCenterRaw = $ge(id + "_center" + edit);

	var oBorderLeft = $ge(id + "_left");
	var oBorderLeftRaw = $ge(id + "_left" + edit);

	var oBorderRight = $ge(id + "_right");
	var oBorderRightRaw = $ge(id + "_right" + edit);

	// 处理左侧的宽度(包括左侧内部的div的宽度) 和 右侧的宽度（包括右侧内部div的宽度）
	if (oBorderLeftRaw) {
		oBorderLeftRaw.newWidth = oBorderLeftRaw.offsetWidth;
		resizeWidth(oBorderLeftRaw, oBorderLeft, otherWidth);

		oBorderLeftRaw.newHeight = oBorderMiddle.newHeight;
		// oBorderLeftRaw.style.height = oBorderLeftRaw.newHeight + unit;
		resizeHeight(oBorderLeftRaw, oBorderLeft, otherHeight);
	} else {
		if (oBorderLeft) {// 此处宽度已经指定
			oBorderLeft.newWidth = oBorderLeft.offsetWidth;
			oBorderLeft.newHeight = oBorderMiddle.newHeight;
			// oBorderLeft.style.height = oBorderLeft.newHeight + unit;
		}
	}

	if (oBorderRightRaw) {
		oBorderRightRaw.newWidth = oBorderRightRaw.offsetWidth;
		resizeWidth(oBorderRightRaw, oBorderRight, otherWidth);

		oBorderRightRaw.newHeight = oBorderMiddle.newHeight;
		// oBorderRightRaw.style.height = oBorderRightRaw.newHeight + unit;
		resizeHeight(oBorderRightRaw, oBorderRight, otherHeight);
	} else {
		if (oBorderRight) {// 此处宽度已经指定
			oBorderRight.newWidth = oBorderRight.offsetWidth;

			oBorderRight.newHeight = oBorderMiddle.newHeight;
			// oBorderRight.style.height = oBorderRight.newHeight + unit;
		}
	}
	// 处理左侧的宽度和高度(包括左侧内部的div的宽度和高度) 和 右侧的宽度和高度（包括右侧内部div的宽度和高度）完毕

	// 处理中间部分的div的宽度和高度以及内部div的宽度和高度
	var middleWidth = oBorder.newWidth;
	var oBorderLeftRawWidth = oBorderLeftRaw
			? oBorderLeftRaw.newWidth
			: (oBorderLeft ? oBorderLeft.newWidth : 0);
	var oBorderRightRawWidth = oBorderRightRaw
			? oBorderRightRaw.newWidth
			: (oBorderRight ? oBorderRight.newWidth : 0);
	var centerWidthRaw = middleWidth - oBorderLeftRawWidth
			- oBorderRightRawWidth;
	if (oBorderCenterRaw) {
		oBorderCenterRaw.newWidth = centerWidthRaw;
		oBorderCenterRaw.style.width = oBorderCenterRaw.newWidth + unit;
		resizeWidth(oBorderCenterRaw, oBorderCenter, otherWidth);

		oBorderCenterRaw.newHeight = oBorderMiddle.newHeight;
		// oBorderCenterRaw.style.height = oBorderCenterRaw.newHeight + unit;
		resizeHeight(oBorderCenterRaw, oBorderCenter, otherHeight);

	} else {
		oBorderCenter.newWidth = centerWidthRaw;
		oBorderCenter.style.width = oBorderCenter.newWidth + unit;

		oBorderCenter.newHeight = oBorderMiddle.newHeight;
		// oBorderCenter.style.height = oBorderCenter.newHeight + unit;
	}
	// 处理中间部分的div的宽度以及内部div的宽度 - 完毕

	// 如果是边框，则计算innerdiv的高度和宽度
	var oBorderInner = $ge(id + "_inner");
	if (oBorderInner) { // border边框
		oBorderInner.newHeight = oBorderCenter.newHeight;
		oBorderInner.newWidth = oBorderCenter.newWidth;
		oBorderInner.style.height = oBorderInner.newHeight + unit;
		oBorderInner.style.width = oBorderInner.newWidth + unit;
	}
};

/**
 * flowh的onresize方法
 * 
 * @param e
 * @return
 * @private
 */
function flowhResize(e) {

	var oFlow = this;
	var id = oFlow.id;
	var flowmode = isFlowMode(oFlow);
	if(flowmode){
		initEditDivWidth(oFlow);
	}
	else
		initEditDivWidthHeight(oFlow);

	var newWidth = oFlow.newWidth;
	if (newWidth != oFlow.oldWidth) {
		var arrKnownWidthItem = new Array();
		var arrUnknownWidthItem = new Array();

		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(id) != -1) { // 是flowh的子项
				// if (oFlowItem.style.width && oFlowItem.style.width != "") {
				// // 设置了宽度
				if (oFlowItem.getAttribute("haswidth") == "1") { // 设置了宽度
					arrKnownWidthItem.push(oFlowItem);
				} else {
					arrUnknownWidthItem.push(oFlowItem);
				}
			}
		}

		var totalWidth = oFlow.newWidth;
		for (var i = 0; i < arrKnownWidthItem.length; i++) {
			totalWidth -= arrKnownWidthItem[i].offsetWidth;

			// arrKnownWidthItem[i].newHeight = oFlow.newHeight;
			arrKnownWidthItem[i].newWidth = arrKnownWidthItem[i].offsetWidth;
			// arrKnownWidthItem[i].style.height =
			// arrKnownWidthItem[i].newHeight + unit;
			if(flowmode){
				resizeChildrenDivWidth(arrKnownWidthItem[i], otherWidth, unit);
			}
			else{
				arrKnownWidthItem[i].newHeight = arrKnownWidthItem[i].offsetHeight;
				resizeChildrenDiv(arrKnownWidthItem[i], otherHeight, otherWidth, unit);
			}
		}
		var width = totalWidth
				/ (arrUnknownWidthItem.length == 0
						? 1
						: arrUnknownWidthItem.length);
		// width取整数
		width = parseInt(width);

		for (var i = 0, n = arrUnknownWidthItem.length; i < n; i++) {
			if (i == n - 1) {
				width = totalWidth - width * (arrUnknownWidthItem.length - 1);
				if (!IS_IE || IS_IE8) // 处理firefox和IE8宽度显示不正确问题
					width -= 1;
			}
			if (width < 0)
				width = 0;

			// arrUnknownWidthItem[i].newHeight = oFlow.newHeight;
			arrUnknownWidthItem[i].newWidth = width;
			// arrUnknownWidthItem[i].style.height =
			// arrUnknownWidthItem[i].newHeight + unit;
			arrUnknownWidthItem[i].style.width = width + unit;
			
			if(flowmode){
				resizeChildrenDivWidth(arrUnknownWidthItem[i], otherWidth, unit);
			}
			else{
				arrUnknownWidthItem[i].newHeight = arrUnknownWidthItem[i].offsetHeight;
				resizeChildrenDiv(arrUnknownWidthItem[i], otherHeight, otherWidth, unit);
			}
		}
	}
	oFlow.oldWidth = oFlow.offsetWidth;
};


function isFlowMode(obj){
	var flowmode = obj.getAttribute("flowmode");
	if(flowmode == null){
		flowmode = obj.flowmode;
	}
	return flowmode == null ? false : flowmode;
}

/**
 * flowv的onresize方法
 * 
 * @param e
 * @return
 * @private
 */
function flowvResize(e) {
	var oFlow = this;
	var id = oFlow.id;
	if(window.flowMode){
//		for (var i = 0; i < oFlow.childNodes.length; i++) {
//			var oFlowItem = oFlow.childNodes[i];
//			if (!IS_IE) {
//				if (oFlowItem.nodeName == "#text")
//					continue;
//			}
//			oFlowItem.style.minHeight = "30px";
//		}
		return;
	}
	initEditDivWidthHeight(oFlow);

	var newHeight = oFlow.newHeight;
	if (newHeight != oFlow.oldHeight) {
		var arrKnownHeightItem = new Array();
		var arrUnknownHeightItem = new Array();

		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(id) != -1) { // 是flowv的子项
				if (oFlowItem.getAttribute("hasheight") == "1") { // 设置了高度
					arrKnownHeightItem.push(oFlowItem);
				} else {
					arrUnknownHeightItem.push(oFlowItem);
				}
			}
		}

		var totalHeight = newHeight;
		for (var i = 0; i < arrKnownHeightItem.length; i++) {
			totalHeight -= arrKnownHeightItem[i].offsetHeight;

			arrKnownHeightItem[i].newHeight = arrKnownHeightItem[i].offsetHeight;
			arrKnownHeightItem[i].newWidth = arrKnownHeightItem[i].offsetWidth;
			resizeChildrenDiv(arrKnownHeightItem[i], otherHeight, otherWidth,
					unit);
		}
		var height = totalHeight
				/ (arrUnknownHeightItem.length == 0
						? 1
						: arrUnknownHeightItem.length);
		// height取整数
		height = parseInt(height);

		for (var i = 0, n = arrUnknownHeightItem.length; i < n; i++) {
			if (i == n - 1) {
				height = (totalHeight - height
						* (arrUnknownHeightItem.length - 1));
			}
			if (height < 0)
				height = 0;
			arrUnknownHeightItem[i].newHeight = height;
			arrUnknownHeightItem[i].style.height = height + unit;
			arrUnknownHeightItem[i].newWidth = arrUnknownHeightItem[i].offsetWidth;
			resizeChildrenDiv(arrUnknownHeightItem[i], otherHeight, otherWidth,
					unit);
		}
	}
	oFlow.oldHeight = oFlow.offsetHeight;
};

/**
 * 将滚动条移至锚点
 * 
 * @param id
 *            FlowvLayout的id
 * @param anchor
 *            锚点名称
 * @param widgetId
 *            所属Widget的ID
 * @return
 * @private
 */
function scrollToAnchor(id, anchor, widgetId) {
	// 真实ID
	if (id == null || id == "")
		return;
	var divId = "$d_"
			+ (widgetId == null || widgetId == "" ? "" : (widgetId + "_")) + id;
	// FlowvLayout的Div对象
	var oFlow = $ge(divId);
	if (oFlow && anchor != null && anchor != "") {
		// 判断是否出现纵向滚动条
		if (isDivVScroll(oFlow)) {
			var scrollTop = 0;
			for (var i = 0; i < oFlow.childNodes.length; i++) {
				var oFlowItem = oFlow.childNodes[i];
				if (oFlowItem.id && oFlowItem.id.indexOf(divId) != -1) { // 是flowv的子项
					if (oFlowItem.getAttribute("anchor") == anchor) { // 是指定锚点的子项
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
 * 
 * @param id
 *            FlowvLayout的id
 * @param anchor
 *            锚点名称
 * @param widgetId
 *            所属Widget的ID
 * @return
 * @private
 */
function getFlowvPanel(id, anchor, widgetId) {
	// 真实ID
	if (id == null || id == "")
		return null;
	var divId = "$d_"
			+ (widgetId == null || widgetId == "" ? "" : (widgetId + "_")) + id;
	// FlowvLayout的Div对象
	var oFlow = $ge(divId);
	if (oFlow && anchor != null && anchor != "") {
		for (var i = 0; i < oFlow.childNodes.length; i++) {
			var oFlowItem = oFlow.childNodes[i];
			if (oFlowItem.id && oFlowItem.id.indexOf(divId) != -1) { // 是flowv的子项
				if (oFlowItem.getAttribute("anchor") == anchor) { // 是指定锚点的子项
					return oFlowItem;
				}
			}
		}
	}
};

/**
 * grid_layout的onresize方法
 * 
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
	// var rowCount = oGridLayout.getAttribute("rowcount");
	var newHeight = oGridLayout.offsetHeight;
	var newWidth = oGridLayout.offsetWidth;
	if (newHeight != oGridLayout.oldHeight) { // 高度改变
		// 已知行高度DIV集合
		var arrKnownHeightItem = new Array();
		// 未知行高度DIV集合
		var arrUnknownHeightItem = new Array();

		for (var i = 0; i < oGridLayout.childNodes.length; i++) {
			var oGridLayoutItem = oGridLayout.childNodes[i];
			if (oGridLayoutItem.id && oGridLayoutItem.id.indexOf(id) != -1
					&& oGridLayoutItem.getAttribute("hasheight")
					&& oGridLayoutItem.getAttribute("hasheight") != "") { // 是行DIV
				if (oGridLayoutItem.getAttribute("hasheight") == "1") { // 设置了高度
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
			if (i == n - 1) {
				height = (totalHeight - height
						* (arrUnknownHeightItem.length - 1));
			}
			if (height < 0)
				height = 0;
			arrUnknownHeightItem[i].style.height = height + "px";
		}
	}
	if (newWidth != oGridLayout.oldWidth) { // 宽度改变
		// 单元格DIV集合
		// var arrCellItem = new Array();
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
			if (oGridLayoutRowItem.id
					&& oGridLayoutRowItem.id.indexOf(id) != -1
					&& oGridLayoutRowItem.getAttribute("hasheight")
					&& oGridLayoutRowItem.getAttribute("hasheight") != "") { // 是行DIV
				for (var i = 0; i < oGridLayoutRowItem.childNodes.length; i++) {
					var oGridLayoutItem = oGridLayoutRowItem.childNodes[i];
					if (oGridLayoutItem.id
							&& oGridLayoutItem.id.indexOf(id) != -1
							&& oGridLayoutItem.getAttribute("haswidth")
							&& oGridLayoutItem.getAttribute("haswidth") != "") { // 是单元格DIV
						// arrCellItem.push(oGridLayoutItem);
						if (oGridLayoutItem.getAttribute("haswidth") == "1") { // 设置了宽度
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
		var width = totalWidth
				/ (arrUnknownItemNum.length == 0 ? 1 : arrUnknownItemNum.length);
		// width取整数
		width = parseInt(width);

		// 给最后一个无宽度DIV的指定的宽度
		var lastWidth = (totalWidth - width * (arrUnknownItemNum.length - 1));
		lastWidth = lastWidth < 0 ? 0 : lastWidth;
		var specialWidth = (lastWidth - 0.5) < 0 ? 0 : (lastWidth - 0.5);
		for (var i = 0, n = arrUnknownWidthItem.length; i < n; i++) {
			if ((i % arrUnknownItemNum.length) == 1) { // 是每行最后一个无宽度DIV
				if (!IS_IE || IS_IE8) // 处理firefox和IE8宽度显示不正确问题
					arrUnknownWidthItem[i].style.width = specialWidth + "px";
				else
					arrUnknownWidthItem[i].style.width = lastWidth + "px";
			} else {
				arrUnknownWidthItem[i].style.width = (width < 0 ? 0 : width)
						+ "px";
			}
		}
	}
	oGridLayout.oldHeight = oGridLayout.offsetHeight;
	oGridLayout.oldWidth = oGridLayout.offsetWidth;
};

/**
 * cardLayout的onresize方法
 * 
 * @param e
 * @return
 * @private
 */
function cardResize(e) {
	var oCard = this;
	initEditDivWidthHeight(oCard);
	var childDivArray = oCard.childNodes;
	for (var i = 0, n = childDivArray.length; i < n; i++) {
		var childDiv = childDivArray[i];
		if (childDiv.nodeName.toLowerCase() == "div") {
			childDiv.newWidth = oCard.newWidth;
			childDiv.newHeight = oCard.newHeight;
			resizeChildrenDiv(childDiv, otherHeight, otherWidth, unit);
			childDiv.style.width = oCard.newWidth + "px";
			childDiv.style.height = oCard.newHeight + "px";
		}
	}
};
