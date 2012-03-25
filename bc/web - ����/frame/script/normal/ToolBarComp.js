/**
 *
 * @fileoverview ToolBar,可加标题作为Panel的Header，也可加各种按钮等作为工具条
 * 
 * @author guoweic
 * @version NC6.0
 */
ToolBarComp.toolHeight = 25;

ToolBarComp.prototype = new BaseComponent;
ToolBarComp.prototype.componentType = "TOOLBAR";

/**
 * 工具条构造函数
 * @class 工具条控件
 * @constructor
 * 
 * @param transparent: 是否透明，默认为否
 */
function ToolBarComp(parent, name, left, top, width, height, position, className, transparent) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.position = getString(position, "relative");
	this.parentOwner = parent;
	// 保存ToolBar中添加的所有按钮项
	this.buttonItems = new HashMap();
	// 保存ToolBar中添加的所有自定义项
	this.selfDefItems = new HashMap();
	// 保存子项对应的分隔条
	this.itemSepMap = new HashMap();
	this.leftButtonItems = new Array();
	this.rightButtonItems = new Array();
	this.leftSelfDefItems = new Array();
	this.rightSelfDefItems = new Array();
	
	// 所有子项的外层DIV数组，用于在长度超出时进行调整
	this.allItemsDivArray = new Array();
	
	this.userClassName = className;
	var baseClassName = getString(this.userClassName, "toolbar_div");
	this.className = baseClassName;
	this.baseClassName = baseClassName;
	if (transparent == true) {
		this.transparent = true;
		this.className += "_transparent";
	}
	this.create();
};

/**
 * @private
 */
ToolBarComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.position = this.position;
	if (this.width.indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = ToolBarComp.toolHeight + "px";
	this.Div_gen.className = this.className;
	
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * @private
 */
ToolBarComp.prototype.manageSelf = function() {
	this.centerDiv = $ce("DIV");
	this.centerDiv.className = "center_div";
	this.Div_gen.appendChild(this.centerDiv);
	
	this.toRightDiv = $ce("DIV");
	this.toRightDiv.className = this.baseClassName + "_toright_div_hidden";
	this.centerDiv.appendChild(this.toRightDiv);
	
	this.toLeftDiv = $ce("DIV");
	this.toLeftDiv.className = this.baseClassName + "_toleft_div_hidden";
	this.centerDiv.appendChild(this.toLeftDiv);
	
};

/** 
 * 销毁控件
 * @private
 */
ToolBarComp.prototype.destroySelf = function() {
	// 销毁子按钮
	var buttonItems = this.buttonItems.values();
	for (var i = 0, n = buttonItems.length; i < n; i++) {
		var button = buttonItems[i];
		button.destroySelf();
	}
	// 销毁自定义子项
	var selfDefItems = this.selfDefItems.values();
	for (var i = 0, n = selfDefItems.length; i < n; i++) {
		var item = selfDefItems[i];
		if (item.destroySelf)
			item.destroySelf();
	}
	this.destroy();
};

/**
 * 增加标题
 * @param text 标题文字
 * @param color 标题文字颜色
 * @param isBold 标题文字是否为粗体
 * @param srcImg 标题图片链接
 * @param menu 标题图片点击后触发的Menu
 */
ToolBarComp.prototype.setTitle = function(text, color, isBold, refImg_1, refImg_2, menu) {

	this.className = getString(this.userClassName, this.baseClassName + "_title");
	if (this.transparent == true) {
		this.className += "_transparent";
	}
	this.Div_gen.className = this.className;
	
	if (!this.title) {
		this.title = $ce("DIV");
		this.title.style.overflow = "hidden";
		this.title.style.styleFloat = "left";
		this.title.style[ATTRFLOAT] = "left";
		this.title.className = "toolbar_title";
		if (refImg_1) {
			var top = "2";
			if (!IS_IE)
				top = "3";
			this.title.img = new ImageComp(this.title, this.id + "_title_image", refImg_1, "0", top, "18", "18", null, refImg_2 == "" ? null : refImg_2, {position:'relative', boolFloatLeft:true});
			var oThis = this;
			if (menu == null) {
				this.title.img.Div_gen.style.cursor = "default";
				this.title.img.Div_gen.onmouseover = function(e) {
					oThis.title.img.Div_gen.style.cursor = "default";
				};
				this.title.img.Div_gen.onmouseout = function(e) {
					oThis.title.img.Div_gen.style.cursor = "default";
				};
			} else {  // ToolBar的右键菜单在题目的图标上显示
				this.title.style.cursor = "pointer";
				this.title.onclick = function(e) {
					e = EventUtil.getEvent();
					if (menu != null)
						menu.show(e);
					stopAll(e);
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
				};
				this.title.img.onclick = function(e) {
					e = EventUtil.getEvent();
					if (menu != null)
						menu.show(e);
					stopAll(e);
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
				};
				this.title.onmouseover = function(e) {
					oThis.title.img.img1.src = refImg_2;
				};
				this.title.onmouseout = function(e) {
					oThis.title.img.img1.src = refImg_1;
				};
			}
		}
		
		this.title.titleTextDiv = $ce("DIV");
		this.title.titleTextDiv.className = this.className + "_text";
		this.title.titleTextDiv.innerHTML = text;
		this.title.titleTextDiv.style.styleFloat = "left";
		this.title.titleTextDiv.style[ATTRFLOAT] = "left";
		var width = getTextWidth(text, this.className + "_text");
		if (isBold)
			width += text.length * 2;
		this.title.titleTextDiv.style.width = width + "px";
		this.title.titleTextDiv.style.whiteSpace = 'nowrap';
		this.title.titleTextDiv.style.color = color ? color : "black";
		this.title.titleTextDiv.style.fontWeight = isBold == true ? "bold" : "normal";
		
		this.title.appendChild(this.title.titleTextDiv);
		this.centerDiv.appendChild(this.title);
		
		this.title.style.width = width + 25 + "px";
		
	} else {
		if (refImg_1) {
			this.title.img.refImg1 = refImg_1;
			this.title.img.img1.src = refImg_1;
		}
		if (refImg_2 && refImg_2 != "") {
			this.title.img.refImg2 = refImg_2;
			this.title.img.img2.src = refImg_2;
		}
		this.title.titleTextDiv.style.color = color ? color : "black";
		this.title.titleTextDiv.style.fontWeight = isBold == true ? "bold" : "normal";
	}
};

/**
 * 增加分隔条
 * @param align 对齐方式
 */
ToolBarComp.prototype.addSep = function(align, itemId) {
	var sep = $ce("DIV");
	sep.className = "toolitem_seperator";
	var floatAttr = (align && align == "right") ? "right" : "left";
	sep.style.styleFloat = floatAttr;
	sep.style[ATTRFLOAT] = floatAttr;
	if (!IS_IE || IS_IE8)
		sep.style.height = "17px";
	this.centerDiv.appendChild(sep);
	if (itemId != null) {
		sep.id = itemId + "_sep";
		this.itemSepMap.put(itemId, sep);
	}

	this.addItemDiv(sep);
};

/**
 * 增加按钮
 * @param id 按钮ID
 * @param text 文字
 * @param tip 鼠标悬停提示信息
 * @param refImg 图片链接
 * @param align 对齐方式
 * @param withSep 是否有分隔条
 * @param width 按钮宽度
 */
ToolBarComp.prototype.addButton = function(id, text, tip, refImg, align, withSep, width, disabled) {
	var oThis = this;
	
	var floatAttr = (align && align == "right") ? "right" : "left";
	
	if (withSep != false) {
		if (floatAttr == "left" && this.leftButtonItems.length != 0)
			this.addSep("left", id); 
		else if (floatAttr == "right" && this.rightButtonItems.length != 0)
			this.addSep("right", id);
	}
			
	
	if (!id || "" == id)
		id = this.id + "_button_" + this.buttonItems.length;
	
	var className = this.title ? this.baseClassName + "_button_title" : this.baseClassName + "_button";
	if (this.transparent)
		className = this.baseClassName + "_button_transparent";
			
	var realWidth = width;
	if (!realWidth) {
		realWidth = 20;
		if (text && text != "")
			realWidth = getTextWidth(text, "toolbar_div_button_demo", true) + 39;
	}
	
	var button = new ButtonComp(this.centerDiv, id, "0", "2",
			realWidth, "22", text, tip, refImg, "relative", "right",
			false, className);
	
//	button.withSep = withSep == true ? true : false;
	button.align = align;
	
//	if (this.transparent) {
//		button.Div_gen.style.background = "transparent";
//		button.butt.style.background = "transparent";
//	}
		
	button.Div_gen.styleFloat = floatAttr;
	button.Div_gen.style[ATTRFLOAT] = floatAttr;
	
	this.buttonItems.put(id, button);

	if (floatAttr == "left" && this.leftButtonItems.length == 0)
		button.Div_gen.style.marginLeft = "5px";
	else if (floatAttr == "right" && this.rightButtonItems.length == 0)
		button.Div_gen.style.marginRight = "5px";
	
	if (floatAttr == "left")
		this.leftButtonItems.push(button);
	else if (floatAttr == "right")
		this.rightButtonItems.push(button);
	
	// 为按钮增加右键菜单显示事件
	var menuListener = new ContextMenuListener();
	menuListener.beforeShow = function(e) {
		var btn = e.obj;
		// 如果按钮没有右键菜单，则将工具条的右键菜单赋给按钮
		if (btn.getContextMenu() == null) {
			if (oThis.getContextMenu() != null)
				btn.setContextMenu(oThis.getContextMenu());
		}
	};
	button.addListener(menuListener);

	this.addItemDiv(button.Div_gen);
	
	if (disabled == true)
		button.setActive(false);
	
	return button;
	
};

/**
 * 增加自定义子项
 * @param id 子项ID
 * @param align 对齐方式
 * @param width 宽度
 * @param innerHTML 内容代码
 */
ToolBarComp.prototype.addSelfDefItem = function(id, align, width, innerHTML) {

	var floatAttr = (align && align == "right") ? "right" : "left";
	
	if (!id || "" == id)
		id = this.id + "_selfdef_" + this.buttonItems.length;
			
	var selfDef = $ce("DIV");
	selfDef.style.width = width.indexOf("%") == -1 ? getInteger(width) + "px" : width;
	selfDef.style.height = "100%";
	selfDef.style.styleFloat = floatAttr;
	selfDef.style[ATTRFLOAT] = floatAttr;
	this.centerDiv.appendChild(selfDef);
	selfDef.id = id;
	selfDef.innerHTML = innerHTML;
		
	this.selfDefItems.put(id, selfDef);

	if (floatAttr == "left" && this.leftSelfDefItems.length == 0)
		selfDef.style.marginLeft = "5px";
	else if (floatAttr == "right" && this.rightSelfDefItems.length == 0)
		selfDef.style.marginRight = "5px";
	
	if (floatAttr == "left")
		this.leftSelfDefItems.push(selfDef);
	else if (floatAttr == "right")
		this.rightSelfDefItems.push(selfDef);

	this.addItemDiv(selfDef);
	
	return selfDef;
	
};

/**
 * 删除按钮
 */
ToolBarComp.prototype.removeButton = function(id) {
	var btn = this.getButton(id);
	if (btn != null) {
		// 删除分割线
		this.removeSep(btn);
		// 删除按钮
		this.removeItemDiv(btn.Div_gen);
		btn.destroySelf();
		// 从工具条按钮集合中删除
		this.buttonItems.remove(id);
		if (btn.align == "left")
			this.leftButtonItems.removeEle(btn);
		else if (btn.align == "right")
			this.rightButtonItems.removeEle(btn);
	}
};

/**
 * 删除子项对应的分割线
 */
ToolBarComp.prototype.removeSep = function(button) {
	if (this.itemSepMap.containsKey(button.id)) {
		this.removeItemDiv(sep);
		var sep = this.itemSepMap.get(button.id);
		this.centerDiv.removeChild(sep);
		this.itemSepMap.remove(button.id);
	}
};

/**
 * 删除自定义子项
 */
ToolBarComp.prototype.removeSelfDefItem = function(id) {
	var selfDef = this.getSelfDefItem(id);
	if (selfDef != null) {
		// 删除自定义子项
		this.removeItemDiv(selfDef);
		selfDef.destroySelf();
		// 从工具条按钮集合中删除
		this.selfDefItems.remove(id);
		if (selfDef.style[ATTRFLOAT] == "left")
			this.leftSelfDefItems.remove(id);
		else if (selfDef.style[ATTRFLOAT] == "right")
			this.rightSelfDefItems.remove(id);
	}
};

/**
 * 获取所有按钮子项（Map类型）
 */
ToolBarComp.prototype.getButtonItems = function() {
	return this.buttonItems;
};

/**
 * 根据ID获取按钮
 */
ToolBarComp.prototype.getButton = function(id) {
	if (this.buttonItems.containsKey(id))
		return this.buttonItems.get(id);
	return null;
};

/**
 * 获取所有自定义子项（Map类型）
 */
ToolBarComp.prototype.getSelfDefItems = function() {
	return this.selfDefItems;
};

/**
 * 根据ID获取自定义子项
 */
ToolBarComp.prototype.getSelfDefItem = function(id) {
	if (this.selfDefItems.containsKey(id))
		return this.selfDefItems.get(id);
	return null;
};

/**
 * 设置自定义子项内容
 */
ToolBarComp.prototype.setSelfDefItemInnerHTML = function(id, html) {
	if (this.selfDefItems.containsKey(id)) {
		var selfDef = this.selfDefItems.get(id);
		selfDef.innerHTML = html;
	}
};

/**
 * Toolbar本身不显示菜单
 * @private
 */
ToolBarComp.prototype.oncontextmenu = function(e) {
	return;
};

/**
 * 响应快捷键（只响应第一个匹配子项的点击事件）
 * @private
 */
ToolBarComp.prototype.handleHotKey = function(key) {
	// 匹配Button子项快捷键
	var buttonItems = this.buttonItems.values();
	if (buttonItems.length > 0) {
		for (var i = 0, n = buttonItems.length; i < n; i++) {
			var obj = buttonItems[i].handleHotKey(key);
			if (obj != null)
				return obj;
		}
	}
	return null;
};

/******************* 处理子项过多问题 START *******************/

/**
 * 检查工具条中内容总长度
 * @param withArrowDiv 是否把箭头算在内
 * @return true为总长度小于容器长度，false为总长度大于容器长度
 * @private
 */
ToolBarComp.prototype.checkWidth = function(withArrowDiv) {
	var totalWidth = 0;
	var centerDivWidth = this.centerDiv.offsetWidth;
	for (var i = 0, n = this.allItemsDivArray.length; i < n; i++) {
		if (this.allItemsDivArray[i].shown == true) {
			totalWidth += this.allItemsDivArray[i].offsetWidth;
			if (withArrowDiv == true) {
				if (totalWidth >= centerDivWidth - 50)  //TODO 50为预留宽度加两个箭头宽度
					return false;
			} else {
				if (totalWidth >= centerDivWidth - 20)  //TODO 20为预留宽度
					return false;
			}
		}
	}
	return true;
	
};

/**
 * 向子项DIV数组中加入新项
 * @private
 */
ToolBarComp.prototype.addItemDiv = function(div) {
	this.allItemsDivArray.push(div);
	div.shown = true;
	if (this.checkWidth(this.checkArrow()) == true) {
		div.style.display = "block";
	} else {
		div.style.display = "none";
		div.shown = false;
		this.showArrowDiv();
		while (this.checkWidth(this.checkArrow()) == false)
			this.hideNextItemDiv();
	}
};

/**
 * 向子项DIV数组中删除
 * @private
 */
ToolBarComp.prototype.removeItemDiv = function(div) {
	this.allItemsDivArray.removeEle(div);
	if (div.offsetWidth >= 5 && this.allItemsDivArray.length > 0) {  // 不是分隔条
		// 显示下一个
		while (this.checkWidth(this.checkArrow()) == true 
			&& this.allItemsDivArray[this.allItemsDivArray.length - 1].shown != true)
			this.showNextItemDiv();
		if (this.checkWidth(this.checkArrow()) == false) {  // 隐藏多显示的
			this.hideNextItemDiv();
		} else {  // 显示上一个
			while (this.checkWidth(this.checkArrow()) == true 
				&& this.allItemsDivArray[0].shown != true)
				this.showPreviousItemDiv();
			if (this.checkWidth(this.checkArrow()) == false)  // 隐藏多显示的
				this.hidePreviousItemDiv();
		}
	}
	if (this.allItemsDivArray[0] 
		&& this.allItemsDivArray[0].shown == true 
		&& this.allItemsDivArray[this.allItemsDivArray.length - 1].shown == true)  // 闅愯棌绠ご
		this.hideArrowDiv();
};

/**
 * 显示前一个隐藏子项DIV
 * @private
 */
ToolBarComp.prototype.showPreviousItemDiv = function() {
	for (var i = 0, n = this.allItemsDivArray.length; i < n; i++) {
		if (this.allItemsDivArray[i].shown == true) {
			if (i == 0)
				return;
			else {
				this.allItemsDivArray[i-1].shown = true;
				this.allItemsDivArray[i-1].style.display = "block";
				if (this.allItemsDivArray[i-1].offsetWidth < 5)  // 分隔条
					this.showPreviousItemDiv();
				this.activeArrow();
				return;
			}
		}
	}
};

/**
 * 显示后一个隐藏子项DIV
 * @private
 */
ToolBarComp.prototype.showNextItemDiv = function() {
	for (var i = this.allItemsDivArray.length - 1; i >= 0; i--) {
		if (this.allItemsDivArray[i].shown == true) {
			if (i == this.allItemsDivArray.length - 1)
				return;
			else {
				this.allItemsDivArray[i+1].shown = true;
				this.allItemsDivArray[i+1].style.display = "block";
				if (this.allItemsDivArray[i+1].offsetWidth < 5)  // 分隔条
					this.showPreviousItemDiv();
				this.activeArrow();
				return;
			}
		}
	}
};

/**
 * 隐藏前一个隐藏子项DIV
 * @private
 */
ToolBarComp.prototype.hidePreviousItemDiv = function() {
	for (var i = 0, n = this.allItemsDivArray.length; i < n; i++) {
		if (this.allItemsDivArray[i].shown == true) {
			var width = this.allItemsDivArray[i].offsetWidth;
			this.allItemsDivArray[i].shown = false;
			this.allItemsDivArray[i].style.display = "none";
			if (width < 5)  // 分隔条
				this.hidePreviousItemDiv();
			this.activeArrow();
			return;
		}
	}
};

/**
 * 隐藏后一个隐藏子项DIV
 * @private
 */
ToolBarComp.prototype.hideNextItemDiv = function() {
	for (var i = this.allItemsDivArray.length - 1; i >= 0; i--) {
		if (this.allItemsDivArray[i].shown == true) {
			var width = this.allItemsDivArray[i].offsetWidth;
			this.allItemsDivArray[i].shown = false;
			this.allItemsDivArray[i].style.display = "none";
			if (width < 5)  // 分隔条
				this.hidePreviousItemDiv();
			this.activeArrow();
			return;
		}
	}
};

/**
 * 显示到第一个子项
 * @private
 */
ToolBarComp.prototype.showFirstItemDiv = function() {
	while (this.allItemsDivArray[0].shown == false) {
		this.leftMove();
	}
};

/**
 * 显示到最后一个子项
 * @private
 */
ToolBarComp.prototype.showLastItemDiv = function() {
	var length = this.allItemsDivArray.length;
	while (this.allItemsDivArray[length - 1].shown == false) {
		this.rightMove();
	}
};

/**
 * 校验箭头是否已经显示
 * @private
 */
ToolBarComp.prototype.checkArrow = function() {
	if (this.toLeftDiv.className == this.baseClassName + "_toleft_div")
		return true;
	return false;
};

/**
 * 显示左右箭头
 * @private
 */
ToolBarComp.prototype.showArrowDiv = function() {
	if (this.checkArrow() == false) {
		var oThis = this;
		this.toLeftDiv.className = this.baseClassName + "_toleft_div";
		this.toRightDiv.className = this.baseClassName + "_toright_div";
		if (!this.toLeftImg || !this.toRightImg) {
			// 左箭头
			this.toLeftImg = $ce("IMG");
			this.toLeftImg.src = window.themePath + "/images/toolbar/" + "toleft.gif";
			this.toLeftImg.className = this.baseClassName + "_arrow_div";
			this.toLeftImg.onclick = function() {
				oThis.leftMove();
			};
			this.toLeftDiv.appendChild(this.toLeftImg);
			// 右箭头
			this.toRightImg = $ce("IMG");
			this.toRightImg.src = window.themePath + "/images/toolbar/" + "toright.gif";
			this.toRightImg.className = this.baseClassName + "_arrow_div";
			this.toRightImg.onclick = function() {
				oThis.rightMove();
			};
			this.toRightDiv.appendChild(this.toRightImg);
		}
		this.activeArrow();
	}
};

/**
 * 子项向左移动一格
 * @private
 */
ToolBarComp.prototype.leftMove = function() {
	if (this.allItemsDivArray[0].shown == true)
		return;
	// 隐藏下一个
	this.hideNextItemDiv();
	// 显示上一个的数量
	var addCount = 0;
	// 显示上一个
	while (this.checkWidth(this.checkArrow()) == true && this.allItemsDivArray[0].shown != true) {
		this.showPreviousItemDiv();
		addCount++;
	}
	if (addCount > 1)  // 隐藏多显示的
		this.hidePreviousItemDiv();
	// 显示不下则隐藏下一个
	while (this.checkWidth(this.checkArrow()) == false)
		this.hideNextItemDiv();
};

/**
 * 子项向右移动一格
 * @private
 */
ToolBarComp.prototype.rightMove = function() {
	if (this.allItemsDivArray[this.allItemsDivArray.length - 1].shown == true)
		return;
	// 隐藏上一个
	this.hidePreviousItemDiv();
	// 显示下一个的数量
	var addCount = 0;
	// 显示下一个
	while (this.checkWidth(this.checkArrow()) == true && this.allItemsDivArray[this.allItemsDivArray.length - 1].shown != true) {
		this.showNextItemDiv();
		addCount++;
	}
	if (addCount > 1)  // 隐藏多显示的
		this.hideNextItemDiv();
	// 显示不下则隐藏上一个
	while (this.checkWidth(this.checkArrow()) == false)
		this.hidePreviousItemDiv();
};

/**
 * 计算并设置左右箭头的可用性
 * @private
 */
ToolBarComp.prototype.activeArrow = function() {
	if (this.checkArrow() == true && this.allItemsDivArray.length > 1) {
		var length = this.allItemsDivArray.length;
		var firstItemShown = this.allItemsDivArray[0].shown;
		var lastItemShown = this.allItemsDivArray[length - 1].shown;
		this.toLeftImg.style.display = "block";
		this.toRightImg.style.display = "block";
		this.toRightImg.src =  window.themePath + "/images/toolbar/" + "toright.gif";
		this.toLeftImg.src =  window.themePath + "/images/toolbar/" + "toleft.gif";
		if (firstItemShown == false && lastItemShown == true) {
//			this.toLeftImg.style.display = "block";
			this.toRightImg.style.display = "none";
			
//			this.toRightImg.src =  window.themePath + "/images/toolbar/" + "toright1.gif";
		} else if (firstItemShown == true && lastItemShown == false) {
			this.toLeftImg.style.display = "none";
//			this.toRightImg.style.display = "block";
			
//			this.toLeftImg.src =  window.themePath + "/images/toolbar/" + "toleft1.gif";
		} else if (firstItemShown == false && lastItemShown == false) {
//			this.toLeftImg.style.display = "block";
//			this.toRightImg.style.display = "block";
		} else {
			this.toLeftImg.style.display = "none";
			this.toRightImg.style.display = "none";
		}
	}
};

/**
 * 隐藏左右箭头
 * @private
 */
ToolBarComp.prototype.hideArrowDiv = function() {
	this.activeArrow();
	this.toLeftDiv.className = this.baseClassName + "_toleft_div_hidden";
	this.toRightDiv.className = this.baseClassName + "_toright_div_hidden";
};

/******************* 处理子项过多问题 END *******************/

/**
 * 获取对象信息
 * @private
 */
ToolBarComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ToolbarContext";
	context.c = "ToolbarContext";
	context.id = this.id;
	// 获取子项Context
	// Button子项
	var buttonItems = this.buttonItems.values();
	if (buttonItems.length > 0) {
		context.buttonItemContexts = new Array();
		for (var i = 0, n = buttonItems.length; i < n; i++) {
			context.buttonItemContexts.push(buttonItems[i].getContext());
		}
	}
	//TODO
	
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ToolBarComp.prototype.setContext = function(context) {
	// 为子项设置Context
	if (context.buttonItemContexts) {
		// Button子项
		for (var i = 0, n = context.buttonItemContexts.length; i < n; i++) {
			var btnContext = context.buttonItemContexts[i];
			var btn = this.buttonItems.get(context.buttonItemContexts[i].id);
			if (btn)
				btn.setContext(btnContext);
		}
	}
	if (context.delIds != null) {
		for (var i = 0; i < context.delIds.length; i ++) {
			var delId = context.delIds[i];
//			var btn = this.buttonItems.get(delId);
//			var withSep = btn.withSep;
//			if (withSep == true) {
//				this.removeSep(btn);
//			}
//			btn.destroySelf();
			this.removeButton(delId);
		}
	}
	//TODO
};



