/**
 * @fileoverview Panel鎺т欢
 * 
 * @author dengjt, gd, guoweic
 * @version NC6.0
 * 
 */
PanelComp.prototype = new BaseComponent;
PanelComp.prototype.componentType = "PANEL";

/**
 * PanelComp甯冨眬鏋勯�鍑芥暟
 * 
 * @class Panel甯冨眬
 * @param parent 鐖跺璞� * @param name 鍚嶇О
 * @param left 
 * @param top 
 * @param width 瀹藉害
 * @param height 楂樺害
 * @param position 浣嶇疆
 * @param scroll 鏄惁鏈夋粴鍔ㄦ潯
 * @param transparent: 鑳屾櫙鏄惁閫忔槑
 * @param attrArr: 闄勫姞灞炴�锛�
 * 					1銆乮sRoundRect 鏄惁鏄渾瑙�
 * 					2銆乺adius      鍦嗚鍗婂緞
 * 					3銆亀ithBorder 鏄惁鏈夎竟妗�
 * 					4銆乥orderWidth 杈规瀹藉害
 * 					5銆乥orderColor 杈规棰滆壊
 * 
 */
function PanelComp(parent, name, left, top, width, height, position, scroll,
		className, transparent, background, attrArr) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.scroll = getBoolean(scroll, false);
	this.overflow = this.scroll ? "auto" : "hidden";
	this.className = getString(className, "panel_div");
	this.background = background;
	if (transparent == true) {
		this.transparent = true;
		this.className = "panel_transparent_div";
	}
	if (attrArr != null) {
		this.isRoundRect = attrArr.isRoundRect;
		this.radius = attrArr.radius;
		this.withBorder = attrArr.withBorder;
		this.borderWidth = attrArr.borderWidth;
		this.borderColor = attrArr.borderColor;
		this.withTitle = attrArr.withTitle;
		this.title = attrArr.title;
		this.flowmode = attrArr.flowmode;
	}
	this.create();
};

/**
 * @private
 */
PanelComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = "100%";//this.width;
	if(!this.flowmode)
		this.Div_gen.style.height = "100%";//this.height;
	this.Div_gen.style.position = this.position;
	this.Div_gen.className = this.className;
	if (!this.transparent && this.background != "") {
		this.Div_gen.style.background = this.background;
	}
	
	if(this.withTitle){
		this.titleDiv = $ce("DIV");
		this.titleDiv.style.height = "30px";
		this.titleDiv.style.width = "100%";
		//this.titleDiv.innerHTML = '<div class="panel_transparent_title_img_left_div"></div><div class="panel_transparent_title_left_div">' + this.title + '</div>' + 
		//'<div class="panel_transparent_title_img_right_div"></div><div class="panel_transparent_title_right_div">more</div>';
		
		var titleDiv_img_left = $ce("DIV");
		titleDiv_img_left.className = "panel_transparent_title_img_left_div";
		var titleDiv_title_left = $ce("DIV");
		titleDiv_title_left.className = "panel_transparent_title_left_div";
		titleDiv_title_left.innerHTML = this.title;
		var titleDiv_img_right = $ce("DIV");
		titleDiv_img_right.className = "panel_transparent_title_img_right_div";
		var titleDiv_title_right = $ce("DIV");
		titleDiv_title_right.className = "panel_transparent_title_right_div";
		titleDiv_title_right.innerHTML= "more";
		
		this.titleDiv.appendChild(titleDiv_img_left);
		this.titleDiv.appendChild(titleDiv_title_left);
		this.titleDiv.appendChild(titleDiv_img_right);
		this.titleDiv.appendChild(titleDiv_title_right);
		//this.titleDiv.style.background = "green";
		this.Div_gen.appendChild(this.titleDiv);
		
		this.contentDiv = $ce("DIV");
		this.contentDiv.id = this.id+"_content";
		//this.contentDiv.style.border = "1px solid blue";
		this.contentDiv.style.borderTop = "1px dotted #808080";
		this.contentDiv.style.width = "100%";
		this.Div_gen.appendChild(this.contentDiv);
		if (this.parentOwner){
			var fc = getChildForType(this.parentOwner, "div", 0);
			if(fc){
				this.contentDiv.appendChild(fc);
			}
			this.placeIn(this.parentOwner);
		}
	
		if(this.flowmode){
			if(window.editMode)
				this.contentDiv.style.minHeight = "28px";
			else
				this.contentDiv.style.minHeight = "2px";
		}
		else{
			addResizeEvent(this.Div_gen, function() {
				PanelComp.panelResize(oThis.id);
			});
			var currHeight = this.Div_gen.offsetHeight - 30;
			if(currHeight > 0)
				this.contentDiv.style.height = currHeight + "px";
		}
	}

	else if (this.parentOwner)
		this.placeIn(this.parentOwner);
	
	// 鏈夎竟妗�
	if (this.withBorder && this.borderWidth != null) {
		this.Div_gen.style.width = (this.Div_gen.offsetWidth - 2 * this.borderWidth) + "px";
		this.Div_gen.style.height = (this.Div_gen.offsetHeight - 2 * this.borderWidth) + "px";
		this.Div_gen.style.borderStyle = "solid";
		this.Div_gen.style.borderWidth = this.borderWidth + "px";
		if (this.borderColor != null)
			this.Div_gen.style.borderColor = this.borderColor;
	}
	
	if (this.isRoundRect && this.radius != null) {  // 鍦嗚
		this.round();
	}
	
};

PanelComp.prototype.setTitle = function(title){
	this.titleDiv.innerHTML = title;
};

/**
 * 璁剧疆涓哄渾瑙�
 */
PanelComp.prototype.round = function() {
	var settings = {
    	tl: { radius: this.radius },
    	tr: { radius: this.radius },
    	bl: { radius: this.radius },
    	br: { radius: this.radius },
    	antiAlias: true
	};
	return curvyCorners(settings, this.Div_gen.id);
};

/**
 * 璁剧疆Padding灞炴�
 */
PanelComp.prototype.setPadding = function(left, top, right, bottom) {
	this.Div_gen.style.paddingLeft = left ? left + "px" : "0" + "px";
	this.Div_gen.style.paddingTop = top ? top + "px" : "0" + "px";
	this.Div_gen.style.paddingRight = right ? right + "px" : "0" + "px";
	this.Div_gen.style.paddingBottom = bottom ? bottom + "px" : "0" + "px";
};

/**
 * 鑾峰緱鍐呭div
 * @return {}
 */
PanelComp.prototype.getContentDiv = function(){
	if(this.withTitle){
		return this.contentDiv;
	}else{
		return this.Div_gen;
	}
}
/**
 * 璁剧疆鍐呭
 */
PanelComp.prototype.setContent = function(obj) {
	if(this.withTitle){
		this.contentDiv.appendChild(obj);
	}else{
		this.Div_gen.appendChild(obj);
	}
	
};
/**
 * 鍒犻櫎鍐呭
 * @param {} obj
 */
PanelComp.prototype.removeContent = function(obj) {
	if(this.withTitle){
		this.contentDiv.removeChild(obj);
	}else{
		this.Div_gen.removeChild(obj);
	}
	
};

/**
 * 璁剧疆鏄惁鏈夋粴鍔ㄦ潯
 * 
 * @param scroll 鏄惁鏈夋粴鍔ㄦ潯
 */
PanelComp.prototype.setScroll = function(scroll) {
	this.scroll = getBoolean(scroll, false);
	this.overflow = this.scroll ? "auto" : "hidden";
	this.Div_gen.style.overflow = this.overflow;
};

/**
 * 璁剧疆鏄惁娓叉煋
 */
PanelComp.prototype.setDisplay = function(display) {
	this.parentOwner.style.display = display == true ? "block" : "none";
	this.display = display;
};

/**
 * 璁剧疆鍙鎬�
 */
PanelComp.prototype.setVisible = function(visible) {
	this.parentOwner.style.visibility = visible == true ? "visible" : "hidden";
	this.visible = visible;
};

/**
 * @private
 */
PanelComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.PanelContext";
	context.c = "PanelContext";
	context.id = this.id;
	context.display = this.display;
	context.visible = this.visible;
	return context;
};

/**
 * @private
 */
PanelComp.prototype.setContext = function(context) {
	if (context.display != null && context.display != this.display)
		this.setDisplay(context.display);
	if (context.visible != null && context.visible != this.visible)
		this.setVisible(context.visible);
	
};

PanelComp.panelResize = function(id){
	var panel = window.objects[id];
	PanelComp.prototype.adjustSelf.call(panel);
};

PanelComp.prototype.adjustSelf = function(){
	if(this.titleDiv.style.display == "none"){
		this.contentDiv.style.height = this.Div_gen.offsetHeight + 'px';
	}
	else{
		var barHeight = 30;  
		if(this.Div_gen.offsetHeight != null && this.Div_gen.offsetHeight > parseInt(barHeight))
			this.contentDiv.style.height = (this.Div_gen.offsetHeight - parseInt(barHeight))+ 'px';
	}
};

