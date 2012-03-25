/**
 * @fileoverview ImagaComp控件.
 * 用图片取代按钮的外观,功能完全等同于按钮.	
 * 
 * @author guoweic
 * @version NC6.0
 * @since NC5.5
 *
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修改图片Div_gen的float属性设置 . guoweic <b>modified</b>
 * 
 */

ImageComp.prototype = new BaseComponent;
ImageComp.prototype.componentType = "IMAGE";	

/**
 * ImageComp控件构造函数
 * @class 图片控件
 * @param refImg1 图片处于未选中效果样式图片的绝对路径
 * @param refImg2 图片处于选中效果的样式图片的绝对路径
 * @param attrObj.inactiveImg 图片处于禁用效果的样式图片的绝对路径
 * 
 */
function ImageComp(parent, name, refImg1, left, top, width, height, alt, refImg2, attrObj) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	
	// 如果没有传入的width或者height则按照实际图片的大小显示
	if (width == '' || width == null || isNaN(parseInt(width)))
		this.width = "auto";		
	if (height == '' || height == null || isNaN(parseInt(height)))
		this.height = "auto";		
	
	this.refImg1 = getString(refImg1, window.themePath + "/images/default/default.gif");
	this.refImg2 = getString(refImg2, this.refImg1);
	this.alt = alt;
	this.parentOwner = parent;
	this.position = "absolute";
	// 标示图片是否处于激活状态	
	this.disabled = false;
	this.boolFloatRight = false;	
	this.boolFloatLeft = false;
	
	this.inactiveImg = this.refImg1;
	if (attrObj != null) {
		if (attrObj.inactiveImg != null) {
			this.inactiveImg = getString(attrObj.inactiveImg, this.refImg1);		
		}
		this.position = getString(attrObj.position, "absolute");
		if (attrObj.boolFloatRight != null && attrObj.boolFloatRight == true) {
			this.boolFloatRight = attrObj.boolFloatRight;		
			this.position = "relative";
		}
		if (attrObj.boolFloatLeft != null && attrObj.boolFloatLeft == true) {
			this.boolFloatLeft = attrObj.boolFloatLeft;
			this.position = "relative";			
		}				
	}
	
	this.create(); 
};

/**
 * ImageComp的主体创建函数
 * @private
 */
ImageComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	
	if (this.boolFloatLeft) {
		// 适应firefox
		this.Div_gen.style[ATTRFLOAT] = "left";
		// 适应IE
		this.Div_gen.style.styleFloat = "left";
	} 
	if (this.boolFloatRight) {
		// 适应firefox
		this.Div_gen.style[ATTRFLOAT] = "right";
		// 适应IE
		this.Div_gen.style.styleFloat = "right";
	}
	
	this.Div_gen.onselectstart = function() { 
		return false; 
	};
	
	if (this.parentOwner)
	 	this.placeIn(this.parentOwner);
};

/**
 * 
 * @private
 */
ImageComp.prototype.manageSelf = function() {
	var oThis = this;
	
	//创建普通状态图片	
	this.img1 = $ce("IMG");
	
	this.img1.src = this.refImg1;
	this.Div_gen.appendChild(this.img1);
	if (this.width == "auto")
		this.Div_gen.style.width = this.img1.width + "px";
	else
		this.Div_gen.style.width = this.width.indexOf("%") == -1 ? this.width + "px" : this.width;
	if (this.height == "auto")
		this.Div_gen.style.height = this.img1.height + "px";
	else{
		if(this.height.indexOf("%") == -1)
			this.Div_gen.style.height = this.height + "px";
		else
			this.Div_gen.style.height = this.height;
	}
	
	// 如果设置了width,height,则按照指定大小显示
	if (this.width != "auto")
		this.img1.style.width = "100%";
   	if (this.height != "auto")
   		this.img1.style.height = "100%";			
	// 设置鼠标移动到图片上的提示信息
	if (this.alt != null) 
		this.Div_gen.title = this.alt;
 	
 	// 如果在装载过程中发生了错误,则调用该处理函数
	this.img1.onerror = function (e) {
	};
	
	if (this.refImg2 != "") {					   
		this.Div_gen.onmouseout = function(e) {											   
		   if (!oThis.disabled) {
			   e = EventUtil.getEvent();
			   oThis.img1.src = oThis.refImg1; 
			   this.style.cursor = "default";
			   oThis.onmouseout(e);
				// 删除事件对象（用于清除依赖关系）
			   clearEventSimply(e);			
		   }
		};				   
	}
  
	this.Div_gen.onmouseover = function(e) {						
		e = EventUtil.getEvent();
				   
		if (!oThis.disabled) {
		   this.style.cursor = "pointer";  
		   // 更换图片为激活状态图片
		   if (oThis.refImg2) {						  
			 oThis.img1.src = oThis.refImg2;
		   }
		} else {
			this.style.cursor = "default";
		}
		oThis.onmouseover(e);
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

	this.Div_gen.oncontextmenu = function(e) {
		oThis.oncontextmenu(e);
	};
};

/**
 * 改变显示图片
 */
ImageComp.prototype.changeImage = function(src1, src2) {
	this.refImg1 = src1;   
	this.img1.src = this.refImg1;
	if (this.refImg2 != '') 
		this.refImg2 = src2;  
};

/**
 * 改变提示信息
 */
ImageComp.prototype.changeAlt = function(alt) {
	this.Div_gen.title = alt;
};

/**
 * 设置此图片控件的激活状态
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
ImageComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {	
		this.mouseoutFunc = this.Div_gen.onmouseout;
		this.mouseoverFunc = this.Div_gen.onmouseover;
		this.clickFunc = this.Div_gen.onclick;
		if (this.Div_gen.ondblclick)
			this.dbclickFunc = this.Div_gen.ondblclick;
		this.contextmenuFunc = this.Div_gen.oncontextmenu;
		this.Div_gen.onmouseout = function(){};
		this.Div_gen.onmouseover = function(){};
		this.Div_gen.ondblclick = function(){};
		this.Div_gen.onclick = function(){};
		this.Div_gen.oncontextmenu = function(){};
		this.disabled = true;
		//变换图片风格为禁用状态
		this.img1.src = this.inactiveImg; 
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		this.Div_gen.onmouseout = this.mouseoutFunc;
		this.Div_gen.onmouseover = this.mouseoverFunc;
		this.Div_gen.onclick = this.clickFunc;
		if(this.dblclickFunc)
			this.Div_gen.ondblclick = this.dblclickFunc;
		this.Div_gen.oncontextmenu = this.contextmenuFunc;
		this.disabled = false;
		this.img1.src = this.refImg1;
	}
};

/**
 * 得到输入框的激活状态
 */
ImageComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 鼠标移出事件
 * @private
 */
ImageComp.prototype.onmouseout = function(e) {
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
ImageComp.prototype.onmouseover = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 单击事件
 * @private
 */
ImageComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 获取对象信息
 * @private
 */
ImageComp.prototype.getContext = function() {
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
ImageComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.image1 != null && context.image1 != this.refImg1) {
		this.refImg1 = context.image1;
		this.img1.src = this.refImg1;
	}
	if (context.image2 != null && context.image2 != this.refImg2) {
		this.refImg2 = context.image2;
	}
	
};