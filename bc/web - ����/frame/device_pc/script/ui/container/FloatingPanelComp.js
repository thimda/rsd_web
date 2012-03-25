/**
 * @fileoverview
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.02
 * 
 * 1.修改event对象的获取 . guoweic <b>modified</b>
 * 2.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

/**
 * 浮动面板
 * 
 * @class 浮动面板，可以附着在指定的控件上。根据指定的attachComp获得此控件的实际位置信息。
 * @constructor
 * @param floatPos 浮动位置 左上角:leftTop 右上角:rightTop 左下角:leftBottom 右下角:rightBottom
 * @param vIndent 纵向缩进
 * @param hIndent 横向缩进
 * @param dragEnable 浮动面板是否可以拖动
 */
function FloatingPanelComp(parent, attachComp, name, width, height, floatPos,
		vIndent, hIndent, dragEnable, className) {
	this.attachComp = attachComp;
	this.attachCompHtml = attachComp.getObjHtml();
	if (parent == null || parent == "") {
		this.parentHtml = document.body;
	} else
		this.parentHtml = parent;

	this.name = name;
	this.width = width;
	this.height = height;
	this.floatPos = floatPos;
	this.vIndent = getInteger(vIndent, 0);
	this.hIndent = getInteger(hIndent, 0);
	this.dragEnable = getBoolean(dragEnable, true);
	this.className = className;
	this.create();
};

/**
 * @private
 */
FloatingPanelComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.position = "absolute";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	this.Div_gen.style.border = "solid #99A1B6 1px";
	this.Div_gen.style.overflow = "hidden";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.Div_gen.style.zIndex = "100000";
	this.Div_gen.style.zIndex = getZIndex();
	//this.Div_gen.style.background = "EFF1E6";// "url(" + window.themePath + "/images/floatingpanel/background.gif)";
	this.Div_gen.style.background = "#EFF1E6";
	this.parentHtml.appendChild(this.Div_gen);

	if (this.dragEnable == true) {
		// 左侧的图片
		this.leftImg = new ImageComp(this.Div_gen, "leftImg", window.themePath
				+ "/images/floatingpanel/dragsign.gif", 0, 0, "12",
				this.height, "", window.themePath
						+ "/images/floatingpanel/dragsign.gif", "");
		this.leftImg.Div_gen.parentContainer = this.parentHtml;
		
		// 处理拖放
		this.leftImg.Div_gen.onmousedown = handleMouseDownForFloating;
		if (IS_IE) {
			//this.leftImg.Div_gen.onmousemove = handleMouseMoveForFloating;
			this.leftImg.Div_gen.onmousemove = function(e) {handleMouseMoveForFloating(e, oThis.leftImg.Div_gen);}
		}
		this.leftImg.Div_gen.onmouseup = handleMouseUpForFloating;
		// guoweic: 解决firefox浏览器拖动反应慢造成的问题
		if (!IS_IE) {
			window.onmousemove = function(e) {
				if (window.dragStart_fPanel) {
					handleMouseMoveForFloating(e, oThis.leftImg.Div_gen);
				}
			};
			window.onmouseup = function(e) {
				if (window.dragStart_fPanel)
					handleMouseUpForFloating(e);
			}
		}
		this.leftImg.Div_gen.onmouseover = function() {
			this.style.cursor = "move";
		};
	}

	// 内容区
	this.contentDiv = $ce("div");
	this.contentDiv.style.position = "absolute";
	this.contentDiv.style.left = "12px";
	this.contentDiv.style.top = "0px";
	this.contentDiv.style.height = this.height + "px";
	this.contentDiv.style.width = this.width - 12 + "px";
	this.contentDiv.style.overflow = "hidden";
	this.Div_gen.appendChild(this.contentDiv);

	// 得到附属容器的相对于parent的top、left
	var parentTop = compOffsetTop(this.attachCompHtml, this.parentHtml);
	var parentLeft = compOffsetLeft(this.attachCompHtml, this.parentHtml);

	var attachCompWidth = this.attachCompHtml.offsetWidth;
	var attachCompHeight = this.attachCompHtml.offsetHeight;
	// 浮动在左上角
	if (this.floatPos == "leftTop") {
		this.Div_gen.style.top = parentTop + this.vIndent + "px";
		this.Div_gen.style.left = parentLeft + this.hIndent + "px";
	}
	// 浮动在右上角
	else if (this.floatPos == "rightTop") {
		this.Div_gen.style.top = parentTop + this.vIndent + "px";
		this.Div_gen.style.left = parentLeft + attachCompWidth - this.width - this.hIndent + "px";
	}
	// 浮动在左下角
	else if (this.floatPos == "leftBottom") {
		this.Div_gen.style.top = parentTop + attachCompHeight - this.height - this.vIndent + "px";
		this.Div_gen.style.left = parentLeft + this.hIndent + "px";
	}
	// 浮动在右下角
	else if (this.floatPos == "rightBottom") {
		this.Div_gen.style.top = parentTop + attachCompHeight - this.height - this.vIndent + "px";
		this.Div_gen.style.left = parentLeft + attachCompWidth - this.width - this.hIndent + "px";
	}
};

/**
 * 得到内容面板,可以append其他组件
 */
FloatingPanelComp.prototype.getContentPane = function() {
	return this.contentDiv;
};

/**
 * 隐藏浮动面板
 */
FloatingPanelComp.prototype.hide = function() {
	this.Div_gen.style.display = "none";
};

/**
 * 显示浮动面板
 */
FloatingPanelComp.prototype.show = function() {
	this.Div_gen.style.display = "block";
};

/**
 * 获取浮动面板的显示对象
 */
FloatingPanelComp.prototype.getObjHtml = function() {
	return this.Div_gen;
};

/**
 * 设定浮动面板位于给定控件的某个位置,该方法会自动获取comp的left和top值,将浮动面板 放在该控件的上,下,左,右
 * 
 * @param objHtml 控件的显示对象(div)
 * @param posi 取值为top,bottom,left,right
 */
FloatingPanelComp.prototype.setPositionByComp = function(objHtml, posi) {
	if (objHtml == null)
		return;
	if (posi == "bottom") {
		this.Div_gen.style.left = compOffsetLeft(objHtml) + "px";
		this.Div_gen.style.top = compOffsetTop(objHtml) + objHtml.offsetHeight + "px";
	} else if (posi == "top") {
		this.Div_gen.style.left = compOffsetLeft(objHtml) + "px";
		this.Div_gen.style.top = compOffsetTop(objHtml)
				- this.Div_gen.offsetHeight + "px";
	} else if (posi == "right") {
		this.Div_gen.style.left = compOffsetLeft(objHtml)
				+ this.Div_gen.offsetWidth + "px";
		this.Div_gen.style.top = compOffsetTop(objHtml) + "px";
	} else if (posi == "left") {
		this.Div_gen.style.left = compOffsetLeft(objHtml)
				- this.Div_gen.offsetWidth + "px";
		this.Div_gen.style.top = compOffsetTop(objHtml) + "px";
	}
};

/**
 * 鼠标点下时执行方法
 * @param e
 * @return
 * @private
 */
function handleMouseDownForFloating(e) {
	e = EventUtil.getEvent();

	// guoweic: modify start 2009-11-11
	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	//if (e.button == 1) {
	if (e.button == 1 || e.button == 0) {
		/* 处理拖拽初始化事件 */
		window.dragSource = getTarget(e);
		// this.parentNode.style.cursor = "move";
		window.iDiffX_panel = e.clientX - compOffsetLeft(this, document.body);
		window.iDiffY_panel = e.clientY - compOffsetTop(this, document.body);
	}
	
	// guoweic: add start 2009-11-11
	window.dragStart_fPanel = true;
	if (IS_IE)
		window.dragSource.parentNode.setCapture();
	else
		window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
	// guoweic: add end
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 鼠标移动时执行方法
 * @author guoweic
 * @private
 */
function handleMouseMoveForFloating(e, oThis) {
	e = EventUtil.getEvent();
	
	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if ((e.button == 1 || e.button == 0) && window.dragStart_fPanel) {
		if (window.dragSource.parentNode == null) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		
		var currLeft = e.clientX - window.iDiffX_panel
				- compOffsetLeft(oThis.parentContainer, document.body);
		var currTop = e.clientY - window.iDiffY_panel
				- compOffsetTop(oThis.parentContainer, document.body);
		
		oThis.parentNode.style.left = currLeft + "px";
		oThis.parentNode.style.top = currTop + "px";
		var containerLeft = compOffsetLeft(oThis.parentContainer, document.body);
		var containerTop = compOffsetTop(oThis.parentContainer, document.body);
		var maxLeft = oThis.parentContainer.offsetWidth
				- oThis.parentNode.offsetWidth;
		var maxTop = oThis.parentContainer.offsetHeight
				- oThis.parentNode.offsetHeight;
		// floatingPanel的移动范围不能超过其parent
		if (currLeft < 0)
			oThis.parentNode.style.left = "0px";
		else if (currLeft > maxLeft)
			oThis.parentNode.style.left = maxLeft + "px";
		if (currTop < 0)
			oThis.parentNode.style.top = "0px";
		else if (currTop > maxTop)
			oThis.parentNode.style.top = maxTop + "px";
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 该方法已被改写（guoweic）
function handleMouseMoveForFloating(e) {
	if (!e)
		e = window.event;
	
	if (e.button == 1) {
		if (window.dragSource.parentNode == null)
			return;
		
		window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
		// this.parentNode.style.cursor = "move";
		// document.body.style.cursor = "move";
		window.bDragStart_panel = true;
		
		var currLeft = e.clientX - window.iDiffX_panel
				- compOffsetLeft(this.parentContainer, document.body);
		var currTop = e.clientY - window.iDiffY_panel
				- compOffsetTop(this.parentContainer, document.body);
		alert(this.parentNode);
		this.parentNode.style.left = currLeft;
		this.parentNode.style.top = currTop;
		var containerLeft = compOffsetLeft(this.parentContainer, document.body);
		var containerTop = compOffsetTop(this.parentContainer, document.body);
		var maxLeft = this.parentContainer.offsetWidth
				- this.parentNode.offsetWidth;
		var maxTop = this.parentContainer.offsetHeight
				- this.parentNode.offsetHeight;
		// floatingPanel的移动范围不能超过其parent
		if (currLeft < 0)
			this.parentNode.style.left = 0;
		else if (currLeft > maxLeft)
			this.parentNode.style.left = maxLeft;
		if (currTop < 0)
			this.parentNode.style.top = 0;
		else if (currTop > maxTop)
			this.parentNode.style.top = maxTop;
	}
};
 */

/**
 * 鼠标抬起时执行方法
 * @param e
 * @return
 * @private
 */
function handleMouseUpForFloating(e) {
	// guoweic: modify start 2009-11-11
	/**
	if (!e)
		e = window.event;
	
	if (window.bDragStart_panel != null && window.bDragStart_panel == true) {
		window.dragSource.parentNode.releaseCapture();
		// document.body.style.cursor = "move";
		window.bDragStart_panel = false;
	}
	*/
	// guoweic: modify end
	// guoweic: add start 2009-11-11
	if (window.dragStart_fPanel) {
		if (IS_IE)
			window.dragSource.parentNode.releaseCapture();
		else
			window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
		window.dragStart_fPanel = false;
	}
	// guoweic: add end
};
