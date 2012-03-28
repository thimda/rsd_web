/**
 * @fileoverview 按钮控件
 * 
 * @auther gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 .  guoweic <b>added</b> 
 * 2.修改event对象的获取 .  guoweic <b>modified</b> 
 * 3.修正在firefox浏览器中Div_gen宽度显示问题 .  guoweic <b>modified</b> 
 *  
 */

ButtonComp.prototype = new BaseComponent;
ButtonComp.prototype.componentType = "BUTTON";

// 按钮大小默认值
// 高度
ButtonComp.BUTTON_HEIGHT = 23;
// 左边宽度
ButtonComp.BUTTON_LEFT_WIDTH = 4;
// 右边宽度
ButtonComp.BUTTON_RIGHT_WIDTH = 4;
// 按钮默认宽度
ButtonComp.WIDTH = 90;

/**
 * 按钮控件的构造函数
 * @class 按钮控件类。用户可以指定图片显示于按钮之上，可以指定文字和图片的相对位置，不传入图片路径则只显示文字。
 * @param{String} align 文字和图片的相对位置.相对位置均为文字相对图片的位置,有center,left,right,top,bottom
 *                5种相对位置
 * @param{String} text 显示于按钮上的文字
 * @param{String} tip 鼠标悬停于按钮之上时的提示文字
 * @param{String} refImg 按钮上要显示图片的绝对路径
 * @param{Boolean} disabled 初始是否为禁用状态
 */
function ButtonComp(parent, name, left, top, width, height, text, tip, refImg,
		position, align, disabled, className) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.className = getString(className, "button_div");
	this.initStaticConstant();
	// 高度只能在CSS中设置
	this.height = ButtonComp.BUTTON_HEIGHT;
	this.base(name, left, top, width, height);
	
	this.align = align;
	//this.width = getInteger(width, ButtonComp.WIDTH);

	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.tip = getString(tip, "");
	this.text = getString(text, "");
	this.disabled = getBoolean(disabled, false);

	// 得到按钮上要显示图片的绝对路径
	this.refImg = getString(refImg, "");
	
	this.create();
};

/**
 * 初始化静态常量
 * @private
 */
ButtonComp.prototype.initStaticConstant = function() {
	  
};

/**
 * 按钮的主体创建函数
 * @private
 */
ButtonComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = convertWidth(this.width);
	this.Div_gen.style.height = convertWidth(this.height);
	this.Div_gen.className = this.className;

	
	this.btnLeftDiv = $ce("div");
	this.btnLeftDiv.className = "btn_left_off";
	this.btnCenterDiv = $ce("div");
	this.btnCenterDiv.className = "btn_center";
	this.btnRightDiv = $ce("div");
	this.btnRightDiv.className = "btn_right_off";
//	if (IS_IE6)
//		this.btnCenterDiv.style.width = this.width - ButtonComp.BUTTON_LEFT_WIDTH - ButtonComp.BUTTON_RIGHT_WIDTH + 10 + "px"; 
//	else
	
	this.Div_gen.appendChild(this.btnLeftDiv);
	this.Div_gen.appendChild(this.btnCenterDiv);
	this.Div_gen.appendChild(this.btnRightDiv);
	
	this.butt = $ce("button");
	this.butt.style.width = "100%";
	this.butt.style.height = this.Div_gen.style.height;
	this.butt.className = "btn_off";
	//type在某些浏览器中默认为submit,在ie下，在input中敲回车会触发button的click事件。 
	if (this.butt.type == "submit")
		this.butt.setAttribute("type","button");
	
	this.btnCenterDiv.appendChild(this.butt);
	this.Div_gen.title = this.tip;

	// 初始化禁用按钮
	if (this.disabled) {
		this.butt.disabled = true;
		this.btnLeftDiv.className = "btn_left_disabled";
		this.btnRightDiv.className = "btn_right_disabled";
		this.butt.className = "btn_disabled";
	}

	this.Div_gen.onmouseover = function(e) {
		if (oThis.disabled)
			return;
		e = EventUtil.getEvent();
		oThis.btnLeftDiv.className = "btn_left_on";
		oThis.btnRightDiv.className = "btn_right_on";
		oThis.butt.className = "btn_on";
		oThis.onmouseover(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.Div_gen.onmouseout = function(e) {
		if (oThis.disabled)
			return;
		e = EventUtil.getEvent();
		oThis.btnLeftDiv.className = "btn_left_off";
		oThis.btnRightDiv.className = "btn_right_off";
		oThis.butt.className = "btn_off";
		oThis.onmouseout(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onmousedown = function(e){
		if(oThis.disabled){
			return;
		}
		e = EventUtil.getEvent();
		oThis.btnLeftDiv.className = "btn_left_down";
		oThis.btnRightDiv.className = "btn_right_down";
		oThis.butt.className = "btn_down";
		oThis.onmousedown(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onmouseup = function(e){
		if(oThis.disabled){
			return;
		}
		e = EventUtil.getEvent();
		oThis.btnLeftDiv.className = "btn_left_on";
		oThis.btnRightDiv.className = "btn_right_on";
		oThis.butt.className = "btn_on";
		oThis.onmouseup(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onfocus = function(e){
		if(oThis.disabled){
			return;
		}
		e = EventUtil.getEvent();
		oThis.btnLeftDiv.className = "btn_left_focus";
		oThis.btnRightDiv.className = "btn_right_focus";
		oThis.butt.className = "btn_focus";
		oThis.onfocus(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onblur = function(e){
		if(oThis.disabled){
			return;
		}
		e = EventUtil.getEvent();
		oThis.btnLeftDiv.className = "btn_left_off";
		oThis.btnRightDiv.className = "btn_right_off";
		oThis.butt.className = "btn_off";
		oThis.onblur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.Div_gen.onclick = function(e) {
		if (oThis.disabled)
			return;
		e = EventUtil.getEvent();
		e.triggerObj = oThis;
		// 先调用一下document的onclick处理方法,然后执行按钮自己的点击处理方法
		document.onclick();
		oThis.onclick(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
//		clearEvent(e);
		e.triggerObj = null;
		clearEventSimply(e);
	};

	if (this.parentOwner)
		this.placeIn(this.parentOwner);
	
	addResizeEvent(this.Div_gen, function() {
		ButtonComp.btnResize(oThis.id);
	});

	if (this.Div_gen.offsetWidth > 0){
		this.btnCenterDiv.style.width = this.Div_gen.offsetWidth - ButtonComp.BUTTON_LEFT_WIDTH - ButtonComp.BUTTON_RIGHT_WIDTH + "px";  // 20为左右DIV的宽度	
	}
	
};

/**
 * 按钮控件的二级回调函数
 * @private
 */
ButtonComp.prototype.manageSelf = function() {
	this.textNode = document.createTextNode(this.text);

	this.textNodeDiv = $ce("DIV");
	this.textNodeDiv.style.marginBottom = "3px";
	this.textNodeDiv.appendChild(this.textNode);

	if (this.refImg != "") {
		this.imgNode = $ce("img");
		this.imgNode.style.marginRight = "5px";
		var brNode = $ce("br");
		this.imgNode.src = this.refImg;
		// 如果文字没有指定则只显示图片
		if (this.text == null || this.text == "") {
			this.butt.appendChild(this.imgNode);
		} else {
			this.imgNode.align = "absmiddle";

			// 文字位于图片之左
			if (this.align == "left") {
				this.butt.appendChild(this.textNodeDiv);
				this.butt.appendChild(this.imgNode);
			}

			// 文字位于图片之右
			if (this.align == "right") {
				this.butt.appendChild(this.imgNode);
				this.butt.appendChild(this.textNodeDiv);
			}

			// 文字位于图片之上
			if (this.align == "top") {
				this.butt.appendChild(this.textNodeDiv);
				this.butt.appendChild(brNode);
				this.butt.appendChild(this.imgNode);
			}

			// 文字位于图片之下
			if (this.align == "bottom") {
				this.butt.appendChild(this.imgNode);
				this.butt.appendChild(brNode);
				this.butt.appendChild(this.textNodeDiv);
			}
		}
	} else {
		this.butt.appendChild(this.textNodeDiv);
	}
};

ButtonComp.btnResize = function(id){
	var btn = window.objects[id];
	if (btn == null)
		return;
	btn.adjustSelf();
};

ButtonComp.prototype.adjustSelf = function() {
	if (this.Div_gen.offsetWidth > 0){
		this.btnCenterDiv.style.width = this.Div_gen.offsetWidth - ButtonComp.BUTTON_LEFT_WIDTH - ButtonComp.BUTTON_RIGHT_WIDTH + "px";  // 20为左右DIV的宽度
	}
};

/**
 * 改变该button显示的图片
 * 
 * @param imgPath 新图片的路径
 */
ButtonComp.prototype.changeImage = function(imgPath) {
	if (this.refImg != null && this.refImg != "") {
		this.refImg = imgPath;
		this.imgNode.src = imgPath;
	}
};

/**
 * 改变按钮上显示的文字
 * 
 * @param text 按钮上要显示的文字
 */
ButtonComp.prototype.changeText = function(text) {
	var newNode = document.createTextNode(text);
	this.textNodeDiv.replaceChild(newNode, this.textNode);
	this.textNode = newNode;
	this.text = text;
};

/**
 * 设置此按钮控件的激活状态.
 * 
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
ButtonComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.butt.disabled = true;
		this.disabled = true;
		this.butt.className = "btn_disabled";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		this.butt.disabled = false;
		this.disabled = false;
		this.butt.className = "btn_off";
	}
};

/**
 * 得到按钮的激活状态.
 */
ButtonComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 鼠标移出事件
 * @private
 */
ButtonComp.prototype.onmouseout = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseout", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标移入事件
 * @private
 */
ButtonComp.prototype.onmouseover = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标按下事件
 * @private
 */
ButtonComp.prototype.onmousedown = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmousedown", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标弹起事件
 * @private
 */
ButtonComp.prototype.onmouseup = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseup", MouseListener.listenerType, mouseEvent);
};

/**
 * 单击事件
 * @private
 */
ButtonComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 响应快捷键
 * @private
 */
BaseComponent.prototype.handleHotKey = function(key) {
	if (this.isActive() == false)
		return null;
	if (this.hotKey != null) {
		if (key == this.hotKey && this.onclick) {
			this.onclick(null);
			return this;
		}
	}
	return null;
};

/**
 * 获取对象信息
 * @private
 */
ButtonComp.prototype.getContext = function() {
	var context = new Object;
//	var context = TextComp.prototype.getContext.call(this);
	context.c = "ButtonContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.visible = this.visible; 
	context.text = this.text;
	return context;
};

ButtonComp.prototype.setVisible = function(visible) {
	if (visible != null && this.visible != visible) {
		if (visible == true)
			this.Div_gen.style.visibility = "";
		else
			this.Div_gen.style.visibility = "hidden";
		this.visible = visible;
	}
};
/**
 * 设置对象信息
 * @private
 */
ButtonComp.prototype.setContext = function(context) {
	if (context.enabled == this.disabled)
		this.setActive(context.enabled);
	if (context.visible != null && context.visible != this.visible)
		this.setVisible(context.visible);
	if (context.text != null && context.text != this.text)
		this.changeText(context.text);
	if (context.height != null && (context.height + "px") != this.height)
		this.Div_gen.style.height = context.height + "px";
	if (context.width != null && (context.width + "px") != this.width){
		this.Div_gen.style.width = context.width + "px";
		this.btnCenterDiv.style.width = context.width - ButtonComp.BUTTON_LEFT_WIDTH - ButtonComp.BUTTON_RIGHT_WIDTH + "px"; 
	}
	if (context.refImg != null && context.refImg != this.width)
		this.changeImage(context.refImg);
};
