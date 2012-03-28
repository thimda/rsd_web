
CanvasComp.prototype = new BaseComponent;
CanvasComp.prototype.componentType = "CANVAS";

/**
 * 
 */
function CanvasComp(parent, name, left, top, width, height, position, attr, className) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "fullcanvas");
	this.title = attr.title;
	if(this.title == null)
		this.title = "快捷栏";
	this.create();
};

/**
 * @private
 */
CanvasComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = "100%";//this.width;
	this.Div_gen.style.minHeight = "20px";
	this.generateDiv(this.className);
	if (this.parentOwner){
		var fc = getChildForType(this.parentOwner, "div", 0);
		if(fc){
			this.contentDiv.appendChild(fc);
		}
		else{
			if(this.title != null && this.title != ""){
				this.contentDiv.innerHTML = this.title;
				this.contentDiv.className = "canvas_title title_font";
			}
		}
		this.placeIn(this.parentOwner);
	}
	
};

CanvasComp.prototype.generateDiv = function(className){
	//var type = getCssHeight(className + "_TYPE");
	var contentTd = null;
	if(className == "nonecanvas"){
		var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='centercontent'></td></tr></table>";
		this.Div_gen.innerHTML = html;
		contentTd = this.Div_gen.firstChild.rows[0].cells[0];
	}
	//leftcanvas
	else if(className == "leftcanvas"){
		var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='leftborder'></td><td class='centercontent'></td></tr></table>";
		this.Div_gen.innerHTML = html;
		contentTd = this.Div_gen.firstChild.rows[0].cells[1];
	}
	
	else if(className == "rightcanvas"){
		var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='centercontent'></td><td class='rightborder'></td></tr></table>";
		this.Div_gen.innerHTML = html;
		contentTd = this.Div_gen.firstChild.rows[0].cells[0];
	}
	
	else if(className == "fullcanvas"){
		var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='leftborder'></td><td class='centercontent'></td><td class='rightborder'></td></tr></table>";
		this.Div_gen.innerHTML = html;
		contentTd = this.Div_gen.firstChild.rows[0].cells[1];
	}
	
	else if(className == "centercanvas"){
		var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='centercontent'></td></tr></table>";
		this.Div_gen.innerHTML = html;
		contentTd = this.Div_gen.firstChild.rows[0].cells[0];
	}
	
	this.contentDiv = $ce("DIV");
//	this.contentDiv.style.height = "100%";
	contentTd.appendChild(this.contentDiv);
};


CanvasComp.prototype.getContentDiv = function(){
	return this.contentDiv;
	
};

CanvasComp.prototype.setContent = function(obj) {
	this.contentDiv.appendChild(obj);
};


CanvasComp.prototype.removeContent = function(obj) {
	this.contentDiv.removeChild(obj);
};

CanvasComp.prototype.changeClass = function(className){
	var child = this.contentDiv.firstChild;
	this.generateDiv(className);
};


/**
 * @private
 */
CanvasComp.prototype.getContext = function() {
	var context = new Object;
	context.c = "CanvasContext";
	context.id = this.id;
	return context;
};

/**
 * @private
 */
CanvasComp.prototype.setContext = function(context) {
	if (context.display != null && context.display != this.display)
		this.setDisplay(context.display);
	if (context.visible != null && context.visible != this.visible)
		this.setVisible(context.visible);
};