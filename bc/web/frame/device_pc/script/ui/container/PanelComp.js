/**
 * 
 * @version NC6.0
 * 
 */
PanelComp.prototype = new BaseComponent;
PanelComp.prototype.componentType = "PANEL";

/**
 * 
 */
function PanelComp(parent, name, left, top, width, height, position, title, attrArr, className) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "panel_div");
	this.topPadding = 20;
	this.bottomPadding = 15;
//	if (transparent == true) {
//		this.transparent = true;
//	}
//	this.className = "panel_transparent_div";
	this.title = title;
	if (attrArr != null) {
		this.topPadding = getInteger(attrArr.topPadding, this.topPadding);
		this.bottomPadding = getInteger(attrArr.bottomPadding, this.bottomPadding);
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
	else
		this.Div_gen.style.minHeight = "20px";
	this.Div_gen.style.position = this.position;
	this.Div_gen.className = this.className;

	this.titleDiv = $ce("DIV");
	this.titleDiv.style.height = "16px";
	this.titleDiv.style.width = "100%";
	
	this.titleDiv_img_left = $ce("DIV");
	this.titleDiv_img_left.className = "img_left_div_col";
	this.titleDiv_img_left.expand = true;
	this.titleDiv_img_left.onclick = function() {
		if(this.expand){
			this.className = "img_left_div_expand";
			this.expand = false;
			oThis.contentDiv.style.display = "none";
		}
		else{
			this.className = "img_left_div_col";
			this.expand = true;
			oThis.contentDiv.style.display = "";
		}
	};
	
	var titleDiv_title_left = $ce("DIV");
	titleDiv_title_left.className = "panel_transparent_title_left_div";
	titleDiv_title_left.innerHTML = this.title;

	
	this.titleDiv.appendChild(this.titleDiv_img_left);
	this.titleDiv.appendChild(titleDiv_title_left);

	this.Div_gen.appendChild(this.titleDiv);
	
	this.hr = $ce("HR");
	this.hr.className = "panel_hr";
	this.Div_gen.appendChild(this.hr);
	
	this.contentDiv = $ce("DIV");
	this.contentDiv.id = this.id+"_content";

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

	if(this.topPadding != null)
		this.Div_gen.style.marginTop = this.topPadding + "px";
	if(this.bottomPadding != null)
		this.Div_gen.style.marginBottom = this.bottomPadding + "px";
};

PanelComp.prototype.setTitle = function(title){
	this.titleDiv.innerHTML = title;
};

PanelComp.prototype.getContentDiv = function(){
	return this.contentDiv;
};
PanelComp.prototype.setContent = function(obj) {
	this.contentDiv.appendChild(obj);
};
PanelComp.prototype.removeContent = function(obj) {
	this.contentDiv.removeChild(obj);
};

/**
 */
PanelComp.prototype.setDisplay = function(display) {
	this.parentOwner.style.display = display == true ? "block" : "none";
	this.display = display;
};

/**
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
//		this.contentDiv.style.height = "100%";
		var barHeight = 30;  
		if(this.Div_gen.offsetHeight != null && this.Div_gen.offsetHeight > parseInt(barHeight)){
			var height = this.Div_gen.offsetHeight - parseInt(barHeight);
			if (IS_IE7 || IS_IE8) //避免死循环，多减5个像素
				height -= 5;
			if (height > 0)
				this.contentDiv.style.height = height + 'px';
		}
	}
};

