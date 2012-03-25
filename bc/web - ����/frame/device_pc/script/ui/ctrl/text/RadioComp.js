/**
 * @fileoverview Radio控件.  
 * 图形化的单选按钮.Radio按钮元素通常在一个选项组中使用,
 * 其中的选项互斥,具有相同的名字.	
 *
 * @author  gd, guoweic
 * @version NC6.0
 * @since   NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 4.将文字和radio绑定 . guoweic <b>modified</b>
 *	
 */
RadioComp.prototype = new TextComp;
RadioComp.prototype.componentType = "RADIO";

/**
 * 单选控件构造函数
 * @class 单选控件
 * @param value 一个可读可写的字符串,声明了一段文本,如果在提交表单时该单选按钮处于被选中的状态,那么这段文本就会被传递给服务器.此属性
 *				不是声明当前该单选按钮是否被选中了.
 * @param group group相同的为一组,此组中只能选中一个radio
 */
function RadioComp(parent, name, left, top, group, value, text, checked, position, className) {	
	this.group = group;
	this.text = getString(text, "");
	this.value = getString(value, "");
	this.checked = getBoolean(checked, false);		
	this.tabIndex = -1;
	this.base = TextComp;
	this.base(parent, name, left, top, "", "RA", position, null, className);
};

/**
 * RadioComp控件的主体创建函数
 * @private
 */
RadioComp.prototype.create = function() {
	var oThis=this;	
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";   
	var htmlContent = '<input style="float:left;" type="radio" id="' + this.id + '" name="' + this.group + '" value="'+ this.value +'" />';
	this.Div_gen.innerHTML = htmlContent;

	this.input = this.Div_gen.firstChild;
	this.input.defaultChecked = this.checked;	
	this.input.checked = this.checked;
	
	this.label = new LabelComp(this, this.id + "_label", 20, 2, this.text, "absolute", "label_normal");
	this.label.Div_gen.onmousedown = function(e) {
		e = EventUtil.getEvent();

		//TODO 不禁用的情况下才可以通过Div_gen的点击改变radio的选中状态
		if (true) {
			$ge(oThis.id).click();
		}
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.Div_gen.style.width = this.label.getTextWidth() + 20 + "px";
	
	if (this.tabIndex != -1)
		this.input.tabIndex = this.tabIndex;
	 
	this.input.onclick = function(e) {
		e = EventUtil.getEvent();
		oThis.input.checked = !oThis.checked;
		oThis.checked = !oThis.checked;
		oThis.onclick(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		if (oThis.click(oThis.input.checked) == false)
			return false;
		else	
			return true;
	};
 
	if (this.parentOwner) {	
		this.placeIn(this.parentOwner);
	}
	
};

/**
 * @private
 */
RadioComp.prototype.manageSelf = function() {
	
};

/**
 * 返回value属性的值
 */
RadioComp.prototype.getValue = function() {
	return this.value;
};

/**
 * 返回显示内容
 */
RadioComp.prototype.getText = function() {
	return this.text;
};

/**
 * 获取选中状态
 */
RadioComp.prototype.getChecked = function() {
	return this.input.checked;
};

/**
 * 设置选中状态
 */
RadioComp.prototype.setChecked = function(checked) {
	if (checked == null)
		this.input.checked = false;
	else
		this.input.checked = checked;	  
};

/**
 * 单击执行方法
 * @private
 */
RadioComp.prototype.click = function(isChecked) {
	return true;
};

/**
 * 设置只读状态
 */
RadioComp.prototype.setReadOnly = function(readOnly) {
	if (this.readOnly==readOnly)
		return;
	if (this.disabled){
		this.setActive(true);
	}
	// 控件处于只读状态变为非只读状态
	if (this.readOnly == true && readOnly == false) {
		this.input.disabled = false;
		this.readOnly = false;
		this.input.className = "checkbox_box";
		this.Div_gen.className = "checkbox_box";
	}
	// 控件处于只读状态变为只读状态
	else if (this.readOnly == false && readOnly == true) {
		this.input.disabled = true;
		this.readOnly = true;
		this.input.className = "checkbox_box inactive_bgcolor";
		this.Div_gen.className = "checkbox_box inactive_bgcolor";
	}
};

/**
 * 单击事件
 * @private
 */
RadioComp.prototype.onclick = function(e) {
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
RadioComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.RadioContext";
	context.c = "RadioContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.checked = this.checked;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
RadioComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.checked != null)
		this.setChecked(context.checked);
};