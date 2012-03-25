/**
 * 
 * Frame容器。此容器封装了一个窗口的动作。比如最大化，最小话，关闭，打印等。
 * 
 * @author dengjt, guoweic
 * @version NC6.0
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 
 */
FrameComp.prototype = new BaseComponent;
FrameComp.prototype.componentType = "FRAME";

/**
 * FrameComp构造函数
 * @class frame布局
 */
function FrameComp(parent, name, left, top, width, height, title, position,
		dragAndDrop, autoResizeParent, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);

	this.parentOwner = parent;
	this.title = getString(title, "");
	this.position = getString(position, "absolute");

	this.autoResizeParent = false;
	this.autoResizeParent = getBoolean(autoResizeParent, false);
	this.dragAndDrop = getBoolean(dragAndDrop, false);
	this.className = getString(className, "frame_div");
	this.create();
};

/**
 * FrameComp 主体构建函数
 * @private
 */
FrameComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.left = this.left + "px";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	this.Div_gen.style.position = this.position;
	this.Div_gen.id = this.id;
	this.Div_gen.className = this.className;
	if (this.parentOwner) {
		this.placeIn(this.parentOwner);
	}
};

/**
 * 此控件的二级回掉函数。
 * @private
 */
FrameComp.prototype.manageSelf = function() {
	this.titleBar = $ce("DIV");
	this.titleBar.style.position = "absolute";

	this.divContent = $ce("DIV");
	this.divContent.className = "frame_content";
	this.Div_gen.appendChild(this.divContent);
	
	if (!IS_IE)
		this.divContent.style.border = "solid gray 1px";
	this.divContent.style.height = this.Div_gen.offsetHeight - this.divContent.offsetTop - 2 + "px";
	
	// 增加外部拖动标志
	this.divContent.moveSign = "move";

	this.titleBar.className = "frame_title_bar";
	this.Div_gen.appendChild(this.titleBar);

	this.divTitle = $ce("DIV");
	this.divTitle.appendChild(document.createTextNode(this.title));
	this.titleBar.appendChild(this.divTitle);

};

/**
 * 得到内容部分的高度
 * @private
 */
FrameComp.prototype.getContentHeight = function() {
	return parseInt(this.divContent.style.height);
};

/**
 * 覆盖父类的此方法
 * @private
 */
FrameComp.prototype.add = function(obj) {
	this.divContent.appendChild(obj);
	if (obj.owner)
		this.allChildObjects.push(obj.owner);
};

/**
 * 更改标题
 */
FrameComp.prototype.changeTitle = function(newTitle) {
	this.title = getString(newTitle, "");
	if (this.divTitle != null) {
		var text = document.createTextNode(this.title);
		this.divTitle.replaceChild(text, this.divTitle.firstChild);
	}
};

/**
 * 更改名称
 */
FrameComp.prototype.changeName = function(name) {
	if (name == null || name == "")
		return;
	this.id = name;
	this.Div_gen.id = name;
};

/**
 * 重载父类的方法
 * @private
 */
FrameComp.prototype.adjustSelf = function() {

};
