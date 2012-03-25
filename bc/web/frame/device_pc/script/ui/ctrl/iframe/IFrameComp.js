/*******************************************************************************
 * 
 * @fileoverview iframe控件.
 * 
 * @author guoweic
 * @version NC6.0
 * 
 ******************************************************************************/
IFrameComp.prototype = new BaseComponent;
IFrameComp.prototype.componentType = "IFRAME";


/**
 * IFrameComp构造函数
 * @class iframe控件
 * 
 * @param parent 父组件
 * @param name
 * @param src 
 * @param width 
 * @param height 
 * @param border 
 * @param frameBorder 
 * @param scrolling 
 */
function IFrameComp(parent, id, name, src, width, height, border, frameBorder, scrolling) {
	this.base = BaseComponent;
	this.base(id, "", "", width, height);
	this.parentOwner = parent;
	
	this.id = id;
	this.name = name;
	this.src = src;
	this.width = width;
	this.height = height;
	this.border = border;
	this.frameBorder = frameBorder;
	this.scrolling = scrolling;
	this.visible = true;
	
	this.create();
	
};

/**
 * 主体创建函数
 * @private
 */
IFrameComp.prototype.create = function() {
	// 创建整体包容div
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id + "_div";
	this.Div_gen.style.width = "100%";
	this.Div_gen.style.height = "100%";
	this.Div_gen.style.border = "0px";
	
	if (this.parentOwner && this.parentOwner != "")
		this.placeIn(this.parentOwner);
};

/**
 * 二级回调函数
 * @private
 */
IFrameComp.prototype.manageSelf = function() {

	this.frame = $ce("IFRAME");
	this.frame.id = this.id;
	this.frame.name = this.name;
	this.frame.src = this.src;
	this.frame.style.width = this.width ? this.width : "100%";
	this.frame.style.height = this.height ? this.height : "100%";
	this.frame.border = this.border ? this.border : "0px";
	this.frame.frameBorder = this.frameBorder ? this.frameBorder : "0";
	this.frame.scrolling = this.scrolling ? this.scrolling : "auto";
	
//	var oThis = this;
//	this.frame.contentWindow.document.onclick = function(e) {
//		e = EventUtil.getEvent();
//		var win = oThis.frame.contentWindow;
//		try {
//			if (win.parent != win)
//				win.parent.document.onclick(e);
//		}
//		catch(e) {
//		}
//		for ( var i = 0; i < win.clickHolders.length; i++) {
//			if (win.clickHolders[i].outsideClick)
//				win.clickHolders[i].outsideClick(e);
//		}
//		win.clickHolders.trigger = null;
//		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
//		return true;
//	};
	
	this.Div_gen.appendChild(this.frame);
	
};

/**
 * 设值内容地址
 */
IFrameComp.prototype.setSrc = function(src) {
	if (this.src != src) {
		this.src = src;
		this.frame.src = this.src;
	}
};

/**
 * 设值可见性
 */
IFrameComp.prototype.setVisible = function(visible) {
	if (this.visible != visible) {
		this.visible = visible;
		if (visible == true)
			this.Div_gen.style.visibility = "visible";
		else if (visible == false)
			this.Div_gen.style.visibility = "hidden";
	}
};

/**
 * @private
 */
IFrameComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.IFrameContext";
	context.c = "IFrameContext";
	context.id = this.id;
	context.src = this.src;
	context.visible = this.visible;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
IFrameComp.prototype.setContext = function(context) {
	if (context.src != null)
		this.setSrc(context.src);
	if (context.visible != null)
		this.setVisible(context.visible);
};



