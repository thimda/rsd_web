/**
 *
 * 超链接组件
 * @author gd, guoweic
 * @version NC6.0
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 *
 */
LinkComp.prototype = new BaseComponent;
LinkComp.prototype.componentType = "A";

/**
 * 链接控件的构造函数
 * @class 链接控件
 * @param href   链接的目的文件
 * @param hasImg 是否在链接前显示图片
 * @param srcImg 显示在超链接前的图片路径,若hasImg为true且没有提供srcImg则显示默认图片
 * @param target 超文本链接的目标窗口 "_blank", "_self" , "_parent", "_top"
 * @param text   链接中可见部分	
 */
function LinkComp(parent, name, left, top, href, text, hasImg, srcImg, target, position, className) {
	this.base = BaseComponent;
	this.base(name, left, top, "", "");
	this.parentOwner = parent;
	
	this.text = text;
	this.hasImg = getBoolean(hasImg, false);
	if (this.hasImg == true) {
		this.srcImg = getString(srcImg, window.themePath + "/images/a/aimg.gif");
	};
	this.target = getString(target, "_self");
	this.href = getString(href, "#");
	this.position = getString(position, "absolute");
	this.className = getString(this.className, "link_div");
	this.visible = true;
	this.disabled = false;
	this.create(); 
};

/**
 * LinkComp的主体创建函数
 * @private
 */
LinkComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.className = this.className;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = 25 + getTextWidth(this.text) + "px";
	this.Div_gen.style.overflow = "hidden";
	this.Div_gen.style.height = "16px";
	
	if(this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 二级回掉函数
 * @private
 */
LinkComp.prototype.manageSelf = function() {
	var oThis = this;
	//创建图片
	this.aImg = $ce("DIV");
	this.aImg.style.position = "absolute";
	this.aImg.style.left =  "0px";
	this.aImg.style.width = "16px";
	this.aImg.style.height = "16px";
	if (this.hasImg == true){
		this.Div_gen.appendChild(this.aImg);
		this.img = $ce("IMG");
		this.img.src = this.srcImg;
		this.img.style.width = "16px";
		this.img.style.height = "16px";
		this.aImg.appendChild(this.img);
	}
	
	//创建超链接文字
	this.textDiv = $ce("DIV");
	this.textDiv.style.position = "absolute";
	this.textDiv.style.top = "0px";
	if (this.hasImg == true)
		this.textDiv.style.left = "20px";
	else
		this.textDiv.style.left = "0px";
	this.textDiv.style.height = "20px";
	this.a = $ce("a");
	this.a.className = "link_normal";
	this.a.style.left = "20px";
	this.a.innerHTML = this.text;
	if(window.editMode == false){
		this.a.href = this.href;
	}
	
	this.a.onclick = function(e) {
		e = EventUtil.getEvent();
		if (oThis.onclick(this) == false || oThis.href == "#") {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		if (oThis.target == "_blank") {
			window.open(oThis.href);
		}
		else if (oThis.target == "_self")
			window.location.href = oThis.href;
		else if (oThis.target == "_parent")
			window.parent.location.href = oThis.href;
		else if (oThis.target == "_top")
			window.top.location.href = oThis.href;
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.a.onmouseover = function() {
		if (this.disabled == false )  
		oThis.a.className = "link_over";
	};
	this.a.onmouseout = function() {
		if (this.disabled == false  ) 
		oThis.a.className = "link_normal";
	};
	this.textDiv.appendChild(this.a);
	this.Div_gen.appendChild(this.textDiv);
};

/**
 * 设置显示状态
 */
LinkComp.prototype.setVisible = function(visible) {
	if (visible != null && this.visible != visible) {
		if (visible == true)
			this.Div_gen.style.visibility = "";
		else
			this.Div_gen.style.visibility = "hidden";
		this.visible = visible;
	}
};

/**
 * 设置激活状态
 */
LinkComp.prototype.setActive = function(visible) {
	var isActive = getBoolean(visible, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.a.disabled = true;
		this.disabled = true;
		this.a.className = "link_disable";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		this.a.disabled = false;
		this.disabled = false;
		this.a.className = "link_normal";
	}
};


/**
 * 单击事件
 * @private
 */
LinkComp.prototype.onclick = function(link) {
	var linkEvent = {
			"obj" : this,
			"link" : link
		};
	var result = this.doEventFunc("onclick", LinkListener.listenerType, linkEvent);
	if (result == false)
		return false;
	return true;
};

/**
 * 获取对象信息
 * @private
 */
LinkComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.LinkContext";
	context.c = "LinkContext";
	context.id = this.id;
	context.visible = this.visible;
	context.text = this.text;
	context.enabled = !this.disabled;
	return context;
};
/**
 * 设置显示值
 */
LinkComp.prototype.setText = function(text) {
	this.text = text;
	this.a.innerHTML = text;
};

/**
 * 设置对象信息
 * @private
 */
LinkComp.prototype.setContext = function(context) {
	if (context.visible != null)
		this.setVisible(context.visible);
	if (context.text != null)
		this.setText(context.text);
	if (context.enabled == this.disabled)
		this.setActive(context.enabled);
	
};

