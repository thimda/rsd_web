/**
 * 根据css属性计算文本宽度
 */
function getTextWidth(text, className, oriCharset) {
	var tmpDiv = $ce("DIV");
//	if (oriCharset != null && oriCharset == true)
		tmpDiv.innerHTML = text;
//	else	
//		tmpDiv.innerHTML = text.changeCharset(true);
	tmpDiv.style.position = 'absolute';
	tmpDiv.className = className;
	tmpDiv.style.top = "0px";
	tmpDiv.style.left = "0px";
	tmpDiv.style.width = 'auto';
	tmpDiv.style.visibility = 'hidden';
	tmpDiv.style.whiteSpace = 'nowrap';
	document.body.appendChild(tmpDiv);
	var width = tmpDiv.offsetWidth;
	document.body.removeChild(tmpDiv);
	return width;
};

/**
 * 根据css属性计算文本高度
 */
function getTextHeight(text, className) {
	var tmpDiv = $ce("div");
	tmpDiv.innerHTML = text;
	tmpDiv.style.position = "absolute";
	tmpDiv.className = className;
	tmpDiv.style.top = "0px";
	tmpDiv.style.left = "0px";
	tmpDiv.style.height = "auto";
	tmpDiv.style.visibility = "hidden";
	tmpDiv.style.whiteSpace = "nowrap";
	document.body.appendChild(tmpDiv);
	var height = tmpDiv.offsetHeight;
	document.body.removeChild(tmpDiv);
	return height;
};

/**
 * 获取css中定义的高度值（用来将JS中定义的常量挪到CSS中定义并获取）
 */
function getCssHeight(className) {
	var tmpDiv = $ce("div");
	tmpDiv.className = className;
	document.body.appendChild(tmpDiv);
	var height = tmpDiv.offsetHeight;
	document.body.removeChild(tmpDiv);
	return height;
};

/**
 * 判断div是否出了水平滚动条 
 */
function isDivScroll(div) {
	// IE和oprea比较scrollHeight和clientHeight  fireFox比较 offsetScroll和clientHeight（guoweic: 该说法不准确）
	// modified by guoweic（IE和firefox都可以用scrollWidth和clientWidth做比较）
	if (div.scrollWidth > div.clientWidth)
		return true;
	else
		return false;
};

/**
 * 判断div是否出竖直滚动条
 */
function isDivVScroll(div) {
	// modified by guoweic（IE和firefox都可以用scrollHeight和clientHeight做比较）
	if (div.scrollHeight > div.clientHeight)
		return true;
	else
		return false;
};

/**
 * 找到第一个滚动偏移
 */
function compFirstScrollTop(oHtml) {
	var offsetTop = 0;
	while (oHtml) {
		offsetTop = oHtml.scrollTop;
		if(offsetTop != 0)
			return offsetTop;
		oHtml = oHtml.parentNode;
	}
	return 0;
};

/**
 * 找到第一个滚动对应的clientHeight
 */
function compFirstScrollClientHeight(oHtml) {
	var offsetTop = 0;
	while (oHtml) {
		offsetTop = oHtml.scrollTop;
		if(offsetTop != 0)
			return oHtml.clientHeight;
		oHtml = oHtml.parentNode;
	}
	return 0;
};

/**
 * 计算html元素相对Top偏移
 * 
 * @param oHtml 度量控件
 * @param targetParent 是目标控件的父容器
 */

function compOffsetTop(oHtml, targetParent) {
	var offsetTop = 0;
	while (oHtml && oHtml != targetParent) {
		offsetTop += oHtml.offsetTop;
		if (oHtml.offsetParent && oHtml.offsetParent.nodeName != 'HTML')
			offsetTop -= oHtml.offsetParent.scrollTop;
		oHtml = oHtml.offsetParent;
	}
	return offsetTop;
};

/**
 * 计算html元素相对Left偏移
 * 
 * @param oHtml 度量控件
 * @param targetParent 是目标控件的父容器
 */
function compOffsetLeft(oHtml, targetParent) {
	var offsetLeft = 0;
	while (oHtml && oHtml != targetParent) {
		offsetLeft += oHtml.offsetLeft;
		if (oHtml.offsetParent)
			offsetLeft -= oHtml.offsetParent.scrollLeft;
		oHtml = oHtml.offsetParent;
	}

	return offsetLeft;
};

/**
 * 将控件放在视线内
 * 
 * @param eleDiv 控件的显示对象(div)
 */
function positionElementInView(eleDiv) {
	if (eleDiv.style.pixelLeft < 0)
		eleDiv.style.left = "5px";
	if (eleDiv.style.pixelTop + eleDiv.offsetHeight > document.body.clientHeight)
		eleDiv.style.top = document.body.clientHeight - eleDiv.offsetHeight + "px";
	if (eleDiv.style.pixelTop < 0)
		eleDiv.style.top = "0px";
};

/**
 * 将节点放在父窗口中央
 */
function positionElementToCenter(element, reltop, clientHeight) {
	var parentWidth = 0;
	var parentHeight = 0;
	if (element.parentNode.tagName.toUpperCase() == "BODY") {
		parentWidth = document.body.clientWidth;
		parentHeight = document.body.clientHeight;
	} else {
		parentWidth = element.parentNode.offsetWidth;
		parentHeight = element.parentNode.offsetHeight;
	}
	if (clientHeight != null && clientHeight > 0){
		parentHeight = clientHeight;
	}
	var eleWidth = element.style.width;
	var eleHeight = element.style.height;
	var index = eleWidth.indexOf("%");
	if (index != -1) {
		left = parentWidth * translatePercentToFloat(eleWidth) / 100;
		left = parseInt(left);
	} 
	else if (parentWidth <= parseInt(eleWidth))
		left = 0;
	else {
		left = (parentWidth - parseInt(eleWidth)) / 2;
		left = parseInt(left);
	}
	index = eleHeight.indexOf("%");
	if (index != -1) {
		topb = parentHeight * translatePercentToFloat(eleHeight);
		topb = parseInt(topb);
	} 
	else if (parentHeight <= parseInt(eleHeight))
		topb = 0;
	else {
		topb = (parentHeight - parseInt(eleHeight)) / 2;
		topb = parseInt(topb);
	}
	element.style.position = "absolute";
	//不计算滚动条内隐藏的高度
	if(reltop != null && reltop != 0)
		topb += reltop;
	element.style.top = topb + "px";
	element.style.left = left + "px";
};

/**
 * @private
 */
function positionneSelonEvent(eltApos, e) {
	var droite = document.body.clientWidth - e.clientX;
	var bas = document.body.clientHeight - e.clientY;
	var scrollLeft = document.body.scrollLeft;

	if (droite < eltApos.offsetWidth)
		eltApos.style.left = scrollLeft + e.clientX - eltApos.offsetWidth + "px";
	else
		eltApos.style.left = scrollLeft + e.clientX + "px";

	if (bas < eltApos.offsetHeight)
		eltApos.style.top = scrollLeft + e.clientY - eltApos.offsetHeight + "px";
	else
		eltApos.style.top = scrollLeft + e.clientY + "px";
};

function positionneSelonPosFournie(node, top, left) {
	var visibleWidth = document.body.clientWidth - left;
	var bas = document.body.clientHeight - top;
	var scrollLeft = document.body.scrollLeft;

	if (visibleWidth < node.offsetWidth)
		node.style.left = scrollLeft + left - (node.offsetWidth - visibleWidth) + "px";
	else
		node.style.left = scrollLeft + left + "px";

	if (bas < node.offsetHeight) {
		var realTop = scrollLeft + top - node.offsetHeight;
		if (realTop < 0)
			realTop = 0;
		node.style.top = realTop + "px";
	} else
		node.style.top = scrollLeft + top + "px";
};

/**
 * 获取样式属性
 * @param node
 * @param type
 * @return
 */
function getStyleAttribute(node, type) {
	if (IS_IE) {
		var style = node.currentStyle;
		return style[type];
	} else {
		var style = document.defaultView.getComputedStyle(node, null);
		if (style)
			return style.getPropertyValue(type);
		return null;
	}
};

/**
 * 把百分比转换为符点数
 * 
 * @param percent 百分数形式
 */
function translatePercentToFloat(percent) {
	var index = percent.indexOf("%");
	if (index == -1) {
		log("Measures.js(translatePercentToFloat), The string: " + percent
				+ " is not in percent format!");
		return 1;
	}
	try {
		return parseInt(percent.substring(0, index)) / 100;
	} catch (exception) {
		log("Measures.js(translatePercentToFloat)," + exception.name + ":"
				+ exception.message);
		return 1;
	}
};


//
var STANDARD_ZINDEX = 10000;

/**
 * 获取当前zindex
 */
function getZIndex(){
	return STANDARD_ZINDEX ++; 
};

/**
 * 将宽度转换成适合的形式
 */
function convertWidth(width){
	if(width == null || width == "")
		return width;
	width = width + "";
	if(width.indexOf('%') != -1)
		return width;
	if(width.indexOf('px') != -1)
		return width;
	return width + "px";
}
