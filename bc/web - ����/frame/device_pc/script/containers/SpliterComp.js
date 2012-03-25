/*************************************************
 *@fileoverview 分割面板控件,将布局分为div1和div2两部分.
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 *
 **************************************************/

SpliterComp.prototype = new BaseComponent;
SpliterComp.prototype.componentType = "SPLITER";

/**
 * 分割面板构造函数
 * @class 分割面板控件，将布局分为div1和div2两部分。
 */
function SpliterComp(parent, name, top, left, width, height, position, prop, orientation, oneTouch, attrAry, className) {
	this.base = BaseComponent;
	this.base(name,top,left,width,height);
	this.parentOwner = parent;
	// 分割方向
	this.orientation = getString(orientation, 'h');		
	// 是否一键缩进
	this.oneTouch = getBoolean(oneTouch, false);
	this.showDragImg = false;		   
	this.position = getString(position, "absolute");
	this.overflow = "hidden";
	// 点击分隔条时，分隔栏向哪边隐藏。true为左或上。  false为右或下
	this.hideDirection = true;
 	// 边界限定模式。默认是%方式.也可是设定为绝对值方式。boundMode值为"px"
	this.boundMode = "%";
	// 隐藏方向的最小分割比，如果是px模式的话，此处值为对应像素值
	//TODO 像素方式
	this.miniProp = 0;
	this.maxiProp = 1;
	this.spliterWidth = null;
	// 默认是否隐藏拖动bar
	this.hideBar = true;
	// 是否反向设置分割大小
	this.isInverse = false;

	if (attrAry != null) {
		this.boundMode = getString(attrAry.boundMode, "%");		
		this.miniProp = attrAry.miniProp;   
		this.maxiProp = attrAry.maxiProp;		
	   	this.hideDirection = getBoolean(attrAry.hideDirection, true);
	   	if(attrAry.spliterWidth)
	   		this.spliterWidth = attrAry.spliterWidth + "px";
		this.hideBar = getBoolean(attrAry.hideBar, !this.oneTouch);
		
		this.isInverse = getBoolean(attrAry.isInverse, this.isInverse);
	}
	
	if (this.boundMode == "%")
		this.prop = getFloat(prop, 0.5);
	else
		this.prop = getInteger(prop, 100);
	this.redoit = false;
	this.create();
	
	if (!IS_IE) {  // 如果是firefox浏览器，增加window大小改变后事件
		var oSilderComp = this;
		EventUtil.addEventHandler(window, "resize", function(){SpliterComp.spliterCompResize(oSilderComp);});
	}
	
};

/**
 * @private
 */
SpliterComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.className = "spliter_div";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	this.Div_gen.style.position = this.position;	   
//	this.Div_gen.style.overflow = "hidden";
	this.Div_gen.id = this.id;	 
	if (this.parentOwner) {
		this.placeIn(this.parentOwner);
	}
};

/**
 * @private
 */
SpliterComp.prototype.manageSelf = function() {
	var oThis = this;
//	this.Div_gen.onresize = function() {
//		oThis.fixProp();
//	};
	
	addResizeEvent(this.Div_gen, function() {
		oThis.fixProp(oThis.prop, oThis.isInverse);
	});
	
	//创建div1
	this.div1 = $ce("DIV");
	this.div1.className= "spliter_div1";
//	this.div1.style.overflow = this.overflow;
	this.Div_gen.appendChild(this.div1);
	this.div1.owner = this;
	this.div1.add = function(obj) {		 
		this.appendChild(obj);
	};
	
	//创建div2
	this.div2 = $ce("DIV");
	this.div2.className = "spliter_div2";
//	this.div2.style.overflow = this.overflow;
	this.Div_gen.appendChild(this.div2);
	this.div2.owner = this;
		
	this.div2.add = function(obj) {
	 	this.appendChild(obj);
	};
	
	this.divBar = $ce("DIV");
	this.divBar.id = this.id + "_bar";
	this.divBar.className = "spliter_bar_" + this.orientation + (this.hideBar ? "_hide" : "");
	this.Div_gen.appendChild(this.divBar);
	
	if (this.orientation == 'h') {
		this.divBar.style.minHeight = "50px";
	}
	//add drag script
	var drag = false;
	var oldX = 0;
	var oldY = 0;
	this.divBar.onmousedown = function(e) {
		e = EventUtil.getEvent();
		drag = true;
		oldX = e.clientX;
		oldY = e.clientY;
		if (this.setCapture)
			this.setCapture();
		else if (window.captureEvents)
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onmouseup = function() {
		if (drag) {
			drag = false;
			if (oThis.divBar.releaseCapture)
				oThis.divBar.releaseCapture();
			else if (window.captureEvents)
				window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
			if (oThis.orientation == 'h') {
				if( oThis.boundMode == '%')
					oThis.fixProp((oThis.divBar.offsetLeft+(oThis.divBar.offsetWidth/2))/oThis.Div_gen.offsetWidth);
				else
					oThis.fixProp(oThis.divBar.offsetLeft);
			} else {
				if (oThis.boundMode == '%')
					oThis.fixProp((oThis.divBar.offsetTop+(oThis.divBar.offsetHeight/2))/oThis.Div_gen.offsetHeight);
				else
					oThis.fixProp(oThis.divBar.offsetTop);
			}
			// 调整所有tab组件大小
			if (TabComp)
				TabComp.allTabCompResize();
		}
		
	};

	this.Div_gen.onmousemove = function(e) {
		e = EventUtil.getEvent();
		if (drag) {
			if (oThis.orientation == 'h') {
				var clientX = e.clientX;
				if (clientX  <= 0 || clientX >= document.body.clientWidth) {
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
					return false;
				}
				var offsetX = clientX - oldX;
				oldX = clientX;
				oThis.divBar.style.left = parseInt(oThis.divBar.offsetLeft) + offsetX + "px";
			} else {
				var clientY = e.clientY;
				if (clientY <= 0 || clientY >= document.body.clientHeight) {
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
					return false;
				}
				var offsetY = clientY - oldY;
				oldY = clientY;
				oThis.divBar.style.top = parseInt(oThis.divBar.offsetTop) + offsetY + "px";
			}
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.divBar.onmouseout = function() {
		if (oThis.oneTouch)
			this.className = "spliter_bar_onetouch";
		else
			this.className = "spliter_bar_" + oThis.orientation + (oThis.hideBar? "_hide" : "");
		this.style.cursor = "default";
	};
	
	this.divBar.onmouseover = function() {
		if (oThis.oneTouch)
			this.className = "spliter_bar_onetouch";
		else
			this.className = "spliter_bar_" + oThis.orientation;
		if(oThis.orientation == 'h')
			this.style.cursor = "e-resize";
		else
			this.style.cursor = "s-resize";
	};
	
	if (this.oneTouch) {
		// 如果可改变大小，创建一拖动条
		this.dragDiv = $ce("DIV");
		this.dragDiv.id = "dragdiv";
		this.dragDiv.className = "spliter_dragdiv";
		this.divBar.appendChild(this.dragDiv);
		
		this.divBar.className = "spliter_bar_onetouch";

		this.dragDiv.onmouseover = function() {
	 		oThis.dragDiv.style.cursor = "pointer";
			oThis.img.src= window.themePath + "/images/spliter/drag_" + oThis.orientation + "_on.gif";  
		};
	 
		this.dragDiv.onmouseout = function() {
			oThis.img.src = window.themePath + "/images/spliter/drag_" + oThis.orientation + "_off.gif";
		};

		if (this.orientation == 'v') {
			this.divBar.style.width = "100%";
			if (this.spliterWidth)	  
				this.divBar.style.height = this.spliterWidth;
			else
				this.divBar.style.height = getStyleAttribute(this.divBar, "height");
			this.dragDiv.style.left = "40%";			
			this.dragDiv.style.width = "20%";			
			this.dragDiv.style.height = "100%";
			this.div1.style.width = "100%";			 
			this.div2.style.width = "100%";			 
		} else {
			this.divBar.style.height  = "100%";
			if (this.spliterWidth)
				this.divBar.style.width = this.spliterWidth;  
			else
				this.divBar.style.width = getStyleAttribute(this.divBar, "width");		  
			this.dragDiv.style.top = "40%";
			this.dragDiv.style.width = "100%";
			this.dragDiv.style.height = "20%";
			this.div1.style.height = "100%";			 
			this.div2.style.height = "100%";  
		}	
	
		// 如果显示图片
	  	this.img = $ce("IMG");
	  	this.img.style.margin = "0 auto";
	  	this.img.src = window.themePath + "/images/spliter/drag_" + this.orientation + "_off.gif";
	  	
	  	this.img.style.display = "block";
	  	
	   	this.dragDiv.appendChild(this.img);
	   	this.dragDiv.onclick = function() {
			if (oThis.hideDirection) {
				if (!oThis.redoit) {
					oThis.div1.style.visibility = "hidden";
					oThis.oldProp = oThis.prop;
					oThis.prop = 0;
					oThis.redoit = true;
				} else {
					oThis.div1.style.visibility = "visible";
					oThis.prop = oThis.oldProp;
					oThis.redoit = false;
				}
			} else {
				if (!oThis.redoit) {
					oThis.div2.style.visibility = "hidden";
					oThis.oldProp = oThis.prop;
					if (oThis.boundMode == '%') {
						oThis.prop = 1;
					} else {
						if (oThis.orientation == 'v') {
							oThis.prop = oThis.Div_gen.offsetHeight - oThis.divBar.offsetHeight;
						} else {
							oThis.prop = oThis.Div_gen.offsetWidth - oThis.divBar.offsetWidth;
						}
					}
					oThis.redoit = true;
				} else {
					oThis.div2.style.visibility = "visible";
					oThis.prop = oThis.oldProp;
					oThis.redoit = false;
				}	
			}
	   		
			oThis.fixProp();
		};
	}
 
	//不显示拖动条
	else {
		this.divBar.appendChild($ce("DIV"));
		if (this.orientation == 'v') {
			this.divBar.style.width = "100%";
			if (this.spliterWidth)		 
				this.divBar.style.height = this.spliterWidth;
			else
				this.divBar.style.height = getStyleAttribute(this.divBar, "height");
			this.div1.style.width = "100%";			 
			this.div2.style.width = "100%";	
		} else {
			this.divBar.style.height  = "100%";	  
			if (this.spliterWidth)
				this.divBar.style.width = this.spliterWidth;
			else
				this.divBar.style.width = getStyleAttribute(this.divBar, "width");
			this.div1.style.height = "100%";			 
			this.div2.style.height = "100%";  
		}
	}
	
	//将分割位置转换为百分比形式
	//this.changeToProp();
	this.fixProp();
};

/**
 * 组件大小改变后，相应改变内部容器div高度
 * @private
 */
SpliterComp.spliterCompResize = function(oSilderComp) {
	oSilderComp.fixProp();
};

/*
	将象素分割转变为百分比模式
*/
//SpliterComp.prototype.changeToProp = function()
//{
//	if(this.boundMode == "%")
//		return;
//	var totalBound = 0;
//	if(this.orientation == "v")
//		totalBound = this.Div_gen.offsetHeight;
//	else
//		totalBound = this.Div_gen.offsetWidth;
//	if(this.prop < 0)
//		this.prop = 0;
//	if(this.prop > totalBound)
//		this.prop = totalBound;
//	this.prop = this.prop/totalBound;
//};

/**
 * @private
 */
SpliterComp.prototype.fixProp = function(prop, isInverse) {
	var propNum = truncFloat(Math.abs(this.prop - prop), 2);
	if (propNum <= 0.01 && this.isInverse == isInverse) {
		if (this.Div_gen.oldWidth == this.Div_gen.offsetWidth && this.Div_gen.oldHeight == this.Div_gen.offsetHeight) {
			return;
		} 
		else {
			this.Div_gen.oldWidth = this.Div_gen.offsetWidth;
			this.Div_gen.oldHeight = this.Div_gen.offsetHeight;
		}
	}
	try {
		if (prop != null && propNum > 0.01) {
			this.prop = prop;
			if (isInverse != null && isInverse == true)
				this.isInverse = true;
			else
				this.isInverse = false;
		}

		var nowProp = this.prop;
		if (this.boundMode == '%') {
			if (this.isInverse)
				nowProp = (1 - nowProp);
		  	if (nowProp < this.miniProp)
		  		nowProp = this.miniProp;
		  	if (nowProp > this.maxiProp)
		  		nowProp = this.maxiProp;
		} else if (this.isInverse) {
			if (this.orientation == 'v')
				nowProp = this.Div_gen.offsetHeight - nowProp;
			else
				nowProp = this.Div_gen.offsetWidth - nowProp;	
		}
		if (this.orientation == 'v') {   
			var limit = Math.max(0, this.Div_gen.offsetHeight - this.divBar.offsetHeight - 1);
			var firstHeight;
			if (this.boundMode == '%')
				firstHeight = nowProp * (this.Div_gen.offsetHeight);
			else
				firstHeight = nowProp;
			this.div1.style.height = Math.floor(Math.min(firstHeight, limit)) + "px";
			this.divBar.style.top = parseFloat(this.div1.style.height) + "px";
			this.divBar.style.left = "0px";
			this.div2.style.top = parseFloat(this.divBar.style.top) + parseFloat(this.divBar.offsetHeight) + "px";
			this.div2.style.left = "0px";
			var secondHeight = this.Div_gen.offsetHeight - parseFloat(this.div1.style.height) - parseFloat(this.divBar.offsetHeight);
			this.div2.style.height = Math.floor(Math.max(0, secondHeight)) + 'px';
		} else {
			var limit = Math.max(0, this.Div_gen.offsetWidth - this.divBar.offsetWidth);
			var firstWidth;
			if (this.boundMode == '%')
				firstWidth = nowProp * (this.Div_gen.offsetWidth);
			else
				firstWidth = nowProp;
			this.div1.style.width = Math.min(firstWidth, limit)+"px";
			this.divBar.style.left = parseFloat(this.div1.style.width) + "px";
			this.div2.style.left = parseFloat(this.divBar.style.left) + parseFloat(this.divBar.offsetWidth) + "px";
			var secondWidth = this.Div_gen.offsetWidth - parseFloat(this.div1.style.width) - parseFloat(this.divBar.offsetWidth);
			this.div2.style.width = Math.max(0, secondWidth) + 'px';
		}
		
		this.onresizeDiv2(this.oldHeightDiv2,this.oldWidthDiv2,this.div2.offsetHeight,this.div2.offsetWidth);
		this.onresizeDiv1(this.oldHeightDiv1,this.oldWidthDiv1,this.div1.offsetHeight,this.div1.offsetWidth);
	} catch(e) {
	}
	
	if (!IS_IE) {
		// 重新设置页面布局高度
		layoutInitFunc();
	}
	
};

/**
 * 获取第一个DIV
 */
SpliterComp.prototype.getDiv1 = function() {
	return this.div1;
};

/**
 * 获取第二个DIV
 */
SpliterComp.prototype.getDiv2 = function() {
	return this.div2;
};

/**
 * 第一个DIV改变大小事件
 * @private
 */
SpliterComp.prototype.onresizeDiv1 = function(oldH,oldW,newH,newW) {
	var spliterEvent = {
			"obj" : this,
			"oldH" : oldH,
			"oldW" : oldW,
			"newH" : newH,
			"newW" : newW
		};
	this.doEventFunc("resizeDiv1", SpliterListener.listenerType, spliterEvent);
};

/**
 * 第二个DIV改变大小事件
 * @private
 */
SpliterComp.prototype.onresizeDiv2 = function(oldH,oldW,newH,newW) {
	var spliterEvent = {
			"obj" : this,
			"oldH" : oldH,
			"oldW" : oldW,
			"newH" : newH,
			"newW" : newW
		};
	this.doEventFunc("resizeDiv2", SpliterListener.listenerType, spliterEvent);

};
