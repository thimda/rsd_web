MenuBarGroup.prototype.componentType = "MENUBARGROUP";

/**
 * MenuBarGroup构造函数
 * @class 菜单组控件
 */
function MenuBarGroup(id) {
	this.id = id;
	this.menubars = new HashMap();
};

/**
 * 添加子项
 * @param state 状态
 * @param menubar 该状态下显示的menubar
 */
MenuBarGroup.prototype.addItem = function(state, menubar) {
	menubar.Div_gen.parentNode.style.position = "absolute";
	menubar.hideV();
	if (!IS_IE6)
		menubar.Div_gen.parentNode.style.visibility = "hidden";
	this.menubars.put(state, menubar);
};

/**
 * 移除某状态对应的menubar
 */
MenuBarGroup.prototype.removeItem = function(state) {
	this.menubars.remove(state);
};

/**
 * 设置菜单组的当前状态
 */
MenuBarGroup.prototype.setState = function(state) {
	var bar = this.menubars.get(state);
	if (this.nowMenubar != null) {
		this.nowMenubar.hideV();
		if (!IS_IE6)
			this.nowMenubar.Div_gen.parentNode.style.visibility = "hidden";
	}

	if (bar != null) {
		if (!IS_IE6)
			bar.Div_gen.parentNode.style.visibility = "";
		bar.showV();
		this.nowMenubar = bar;
	}
};
