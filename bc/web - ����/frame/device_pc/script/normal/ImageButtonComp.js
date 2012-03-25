/**
 * @fileoverview ImageButtonComp控件.
 * 将给定的图片平铺显示于按钮之上.
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.修改event对象的获取 . guoweic <b>modified</b>
 * 2.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 3.修正原有缺陷 . guoweic <b>modified</b>
 *
 */
ImageButtonComp.prototype = new ImageComp;
ImageButtonComp.prototype.componentType = "IMAGEBUTTON";	

/**
 * 图片按钮构造函数
 * @class ImageButtonComp控件
 * @constructor 
 * @param refImg1 按钮普通状态下的图片路径
 * @param refImg2 按钮激活状态下的图片路径
 * @param text	  显示在按钮上的文字
 * @param alt	  鼠标悬停时的提示文字
 */
function ImageButtonComp(parent, name, left, top, width, height, text, alt, refImg1, refImg2, position) {
	this.refImg1 = getString(refImg1, window.themePath + "/images/button/button.gif"); 
	this.refImg2 = getString(refImg2, window.themePath + "/images/button/button_on.gif");
 	this.caption = text;
	var attrObj = {'position':getString(position,'absolute')};
	
	this.base = ImageComp;
	this.base(parent, name, this.refImg1, left, top, width, height, alt, this.refImg2, attrObj);
 	this.alt = alt;
};

/**
 * ImageButtonComp控件的回调函数
 * @private
 */
ImageButtonComp.prototype.manageSelf = function() {
	var oThis = this;
	this.Div_gen.title = this.alt;
	if (this.width.toString().indexOf("%") == -1 && this.width.toString().indexOf("px") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1 && this.height.toString().indexOf("px") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	this.Div_gen.style.background = "#FFFFFF url('" + this.refImg1 + "') no-repeat center center";
	
	this.buttonText = $ce("DIV");
	this.buttonText.className = "button_text";
	if (this.height.toString().indexOf("%") == -1 && this.height.toString().indexOf("px") == -1) {
		this.buttonText.style.height = this.height + "px";
		this.buttonText.style.lineHeight = this.height + "px";
	} else {
		this.buttonText.style.height = this.height;
		this.buttonText.style.lineHeight = this.height;
	}
	// guoweic: modify end
	
	this.buttonText.appendChild(document.createTextNode(this.caption));
	this.Div_gen.appendChild(this.buttonText);
	
	// 双击不提供任何处理,为了屏蔽调用ImageComp的ondblclick,所以做了这样的处理
	this.Div_gen.ondblclick = function(e) {	
	};
	
	this.Div_gen.onmouseover = function(e) {
		e = EventUtil.getEvent();
		if (oThis.disabled) {
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}	
		oThis.Div_gen.style.background = "#FFFFFF url('" + oThis.refImg2 + "') no-repeat center center";
		this.style.cursor = "pointer";
		oThis.onmouseover(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onmouseout = function(e) {
		e = EventUtil.getEvent();
		if (oThis.disabled) {
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}	
		oThis.Div_gen.style.background = "#FFFFFF url('" + oThis.refImg1 + "') no-repeat center center";
		oThis.onmouseout(e);
		stopEvent(e);  
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	// 图片点击事件处理   
	this.Div_gen.onclick = function(e) {
		e = EventUtil.getEvent();
		if (!oThis.disabled) {
			oThis.onclick(e);						  
			stopEvent(e);
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
};

/**
 * 设置ImageButtonComp控件的激活状态
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
ImageButtonComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (isActive == false) {
  		this.buttonText.className = "button_inactivetext";
  		this.Div_gen.style.background = "#FFFFFF url('" + this.refImg1 + "') no-repeat center center";
  		this.disabled = true;
	}
	// 控件处于禁用状态变为激活状态
	else if (isActive == true) {
		this.buttonText.className = "button_text";
		this.Div_gen.style.background = "#FFFFFF url('" + this.refImg1 + "') no-repeat center center";
		this.disabled = false;
	}
};

/**
 * 得到输入框的激活状态
 */
ImageButtonComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 改变按钮上的文字
 */
ImageButtonComp.prototype.changeTitle =function(title) {
	this.buttonText.replaceChild(document.createTextNode(title), this.buttonText.firstChild);   
};

/**
 * 获取对象信息
 * @private
 */
ImageButtonComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ImageContext";
	context.c = "ImageContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ImageButtonComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	//TODO
	
};