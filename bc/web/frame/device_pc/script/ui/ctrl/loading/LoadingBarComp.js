/**
 * 
 * @fileoverview 加载提示条
 * 
 * @author guoweic
 * @version 6.0
 * 
 */

LoadingBarComp.prototype = new BaseComponent;
LoadingBarComp.prototype.componentType = "LOADING";

//dingrf 111121 改用 Measures.js中的 getZIndex()方法。
LoadingBarComp.ZINDEX = "10000";

/**
 * 加载提示工具条控件构造函数
 * @class 加载提示工具条控件
 * 
 * @param align 横向对齐方式（center, left, right）
 * @param valign 纵向对齐方式（center, top, bottom）
 * @param zIndex 显示层级
 */
function LoadingBarComp(parent, name, left, top, width, height, position, align, valign, zIndex, className, fixed) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.overflow = "hidden";
	this.align = align;
	this.valign = valign;
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.zIndex = getInteger(zIndex, LoadingBarComp.ZINDEX);
	this.zIndex = getZIndex();
	this.innerHTML = null;
	this.visible = false;
	this.className = getString(className, "panel_div_alpha");
	this.fixed = getBoolean(fixed,false);
	this.create();
};

/**
 * @private
 */
LoadingBarComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	if (this.fixed == true){
		this.positionElement();
		this.Div_gen.style.width = this.width.indexOf("px") == -1 ? this.width + "px" : this.width;
		this.Div_gen.style.height = this.height.indexOf("px") == -1 ? this.height + "px" : this.height;
	}
	else{
		this.Div_gen.style.left="0px";
		this.Div_gen.style.top="0px";
		this.Div_gen.style.width = "100%";
		this.Div_gen.style.height = "100%";
	}
	
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.overflow = this.overflow;
	this.Div_gen.className = this.className;
	this.Div_gen.style.visibility = "hidden";
	this.Div_gen.style.zIndex = this.zIndex;
	if (this.parentOwner) {
		if (this.parentOwner.Div_gen) {
			this.parentOwner.Div_gen.appendChild(this.Div_gen);
			if (this.parentOwner.Div_gen.zIndex != null && this.parentOwner.Div_gen.zIndex > this.zIndex)
				this.zIndex = this.parentOwner.Div_gen.zIndex + 1;
		} else {
			this.parentOwner.appendChild(this.Div_gen);
			if (this.parentOwner.zIndex != null && this.parentOwner.zIndex > this.zIndex)
				this.zIndex = this.parentOwner.zIndex + 1;
		}
	} else {
		document.body.appendChild(this.Div_gen);
	}
	this.manageSelf();
};

/**
 * 计算显示位置
 * @private
 */
LoadingBarComp.prototype.positionElement = function() {
	if (this.align != null && this.align != "") {
		var parentWidth = 0;
		if (this.parentOwner == null || this.parentOwner == document.body) {
			parentWidth = document.body.clientWidth;
		} else {
			if (this.parentOwner.Div_gen != null) {
				parentWidth = this.parentOwner.Div_gen.offsetWidth;
			} else {
				parentWidth = this.parentOwner.style.offsetWidth;
			}
		}
		var left = 0;
		var eleWidth = this.width;
		if (this.width.indexOf("%") != -1) {
			left = 0;
		} else if (parentWidth <= parseInt(this.width))
			left = 0;
		else {
			if (this.align == "center") {
				left = (parentWidth - parseInt(this.width)) / 2;
				left = parseInt(left);
			} else if (this.align == "left") {
				left = 0;
			} else if (this.align == "right") {
				left = parentWidth - parseInt(this.width);
			}
				
		}
		this.Div_gen.style.left = left + "px";
	}
	if (this.valign != null && this.valign != "") {
		var parentHeight = 0;
		if (this.parentOwner == null || this.parentOwner == document.body) {
			parentHeight = document.body.clientHeight;
		} else {
			if (this.parentOwner.Div_gen != null) {
				parentHeight = this.parentOwner.Div_gen.offsetHeight;
			} else {
				parentHeight = this.parentOwner.style.offsetHeight;
			}
		}
		var top = 0;
		var eleHeight = this.height;
		if (this.height.indexOf("%") != -1) {
			top = 0;
		} else if (parentHeight <= parseInt(this.height))
			top = 0;
		else {
			if (this.align == "center") {
				top = (parentHeight - parseInt(this.height)) / 2;
				top = parseInt(top);
			} else if (this.align == "top") {
				
			} 
		}
		this.Div_gen.style.top = top + "px";
	}
	if (this.align == null || this.align == "") {
		this.Div_gen.style.left = this.left + "px";
	}
	if (this.valign == null || this.valign == "") {
		this.Div_gen.style.top = this.top + "px";
	}
	
};

/**
 * @private
 */
LoadingBarComp.prototype.manageSelf = function() {
	if (this.innerHTML != null) {  // 用户自定义显示内容
		this.Div_gen.innerHTML = this.innerHTML;
	}
};

/**
 * 设置自定义内容
 */
LoadingBarComp.prototype.setInnerHTML = function(innerHTML) {
	if (innerHTML != null) {
		this.innerHTML = innerHTML;
		this.Div_gen.innerHTML = innerHTML;
	}
};

/**
 * 显示加载提示条
 */
LoadingBarComp.prototype.show = function() {
	if (this.Div_gen) {
		this.Div_gen.style.visibility = "visible";
		this.Div_gen.style.display = "";
		this.visible = true;
	}
};

/**
 * 隐藏加载提示条
 */
LoadingBarComp.prototype.hide = function() {
	if (this.Div_gen) {
		this.Div_gen.style.visibility = "hidden";
		this.Div_gen.style.display = "none";
		this.visible = false;
	}
};

/**
 * 设置页面默认的加载提示条
 * @return
 */
function initDefaultLoadingBar(left, top, width, height, align, valign, zIndex, innerHTML) {
	window.loadingBar = new LoadingBarComp(document.body, "$loadingBar", left, top, width, height, null, align, valign, zIndex);
	window.loadingBar.setInnerHTML(innerHTML);
};

/**
 * 显示页面默认的加载提示条
 */
function showDefaultLoadingBar() {
	if (window.loadingBar == null) {
		var imgSrc = window.themePath + "/ui/ctrl/loading/images/loading.gif";
//		var innerHTML = "<div align='center' style='height:100%;width:100%;'><br><img src='" + imgSrc + "'/> 加载中...</div>";
		var innerHTML = "<table width=100% height=100%><tr><td align='center'><img class='panel_vertical_middle_div' src='" + imgSrc + "'/></td></tr></table>";
//		var imgSrc = window.themePath + "/ui/ctrl/loading/images/loading.swf";
//		var innerHTML = "<div align='center' style='height:100%;width:100%;background-color:#fff;'><br><OBJECT WIDTH='20' HEIGHT='20'><param name='wmode' value='transparent'><PARAM NAME=movie VALUE='" + imgSrc + "'><EMBED src='" + imgSrc + "' WIDTH='20' HEIGHT='20' NAME='2' ALIGN=''></EMBED></OBJECT> 加载中  ... </div>";
		window.loadingBar = new LoadingBarComp(document.body, "$loadingBar", 0, 0, "150", "50", null, "center", "center", 100000);
		window.loadingBar.setInnerHTML(innerHTML);
		//修改背景色 licza
//		window.loadingBar.Div_gen.style.backgroundColor="transparent";
		var bgImgSrc = window.themePath + "/ui/basic/images/transparent.gif";
		window.loadingBar.Div_gen.style.backgroundImage="url("+ bgImgSrc +")";
//		window.loadingBar.Div_gen.style.filter="Alpha(Opacity=50)";
	}
//	if (window.showLoadingBarTimeOutFunc)
//		clearTimeout(window.showLoadingBarTimeOutFunc);
//	// 延迟显示加载条
//	window.showLoadingBarTimeOutFunc = setTimeout("window.loadingBar.show();", 500);
	window.loadingBar.show();
};

/**
 * 隐藏页面默认的加载提示条
 */
function hideDefaultLoadingBar() {
	if (window.showLoadingBarTimeOutFunc)
		clearTimeout(window.showLoadingBarTimeOutFunc);
	if (window.loadingBar != null)
		window.loadingBar.hide();
};

/*
 * 获取对象信息
LoadingBarComp.prototype.getContext = function() {
	var context = new Object;
	context.javaClass = "nc.uap.lfw.core.comp.ctx.LoadingBarContext";
	context.id = this.id;
	context.visible = this.visible;
	return context;
};
 */

/*
 * 设置对象信息
LoadingBarComp.prototype.setContext = function(context) {
	if (context.innerHTML != null)
		this.setInnerHTML(context.innerHTML);
	if (context.visible != this.visible) {
		if (context.visible == true)
			this.show();
		else if (context.visible == false)
			this.hide();
	}
};
 */

