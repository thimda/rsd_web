/*******************************************************************************
 * 
 * @fileoverview tab页签控件.
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正切换tab页时布局混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 4.修改关闭按钮和左右箭头按钮实现代码（以适应多浏览器）. guoweic <b>modified</b>
 * 
 ******************************************************************************/
TabComp.prototype = new BaseComponent;
TabComp.prototype.componentType = "TAB";

// 记录当前显示的下拉菜单
TabComp.currShowMenu = null;
TabComp.divImgWidth = 60;
TabComp.SEP_HEIGHT = 3;
//页签显示在内容上面
TabComp.TYPE_TOP = "top";
//页签显示在内容下面
TabComp.TYPE_BOTTOM = "bottom";
// guoweic: add start 2009-11-25
var tabCompArray = new Array();
// guoweic: add end

/**
 * 
 * TabComp构造函数
 * 
 * @class tab页签控件
 * 
 * @param parent
 *            父组件
 * @param name
 *            组件名字
 * @param left
 *            组件左部x坐标
 * @param top
 *            组件上部y坐标
 * @param width
 *            总体组件宽度
 * @param height
 *            总体组件高度
 * @param tabItemWidth
 *            tabItem的宽度,-1表示采用文字的宽度,否则超过指定宽度的部分显示"..." （该功能尚未实现）
 * @param attrArr
 *            属性集合（JSON对象）
 * 
 */
function TabComp(parent, name, left, top, width, height, tabItemWidth, attrArr,
		className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	// 每个tabItem的宽度
	this.tabItemWidth = getInteger(tabItemWidth, 80);
	// 是否显示关闭按钮
	this.showCloseIcon = true;
	//tab分为 页签在上，页签在下两种	
	this.tabType = TabComp.TYPE_TOP;
	this.flowmode = true;
	if (attrArr != null){
		this.position = getString(attrArr.position, "absolute");
		this.tabType = 	getString(attrArr.tabType, TabComp.TYPE_TOP);
		this.flowmode = getBoolean(attrArr.flowmode, true);
	}
	// 当前页
	this.currActiveTab = -1;
	// 设置是否一个tab 隐藏
	this.oneTabHide = false;
	this.className = getString(className, "tab_div");

	this.create();

//	if (!IS_IE) { // 如果是firefox浏览器，增加window大小改变后事件
//		var oTabComp = this;
//		EventUtil.addEventHandler(window, "resize", function() {
//			TabComp.tabCompResize(oTabComp);
//		});
//	}
	// TODO guoweic: add start 2009-11-25
	tabCompArray.push(this);
	// guoweic: add end
};

/**
 * Tab组件大小改变后，相应改变内部容器div高度
 * 
 * @private
 */
TabComp.tabCompResize = function(oTabComp) {
	oTabComp.adjustSelf();
	//if (!IS_IE) // 如果不是
	//	oTabComp.adjustSelf();// 在firefox中调用一次，布局会有问题，所以调用两次
	/*// 获取Tab标签高度
	if (window.editMode) { // renxh add
		oTabComp.adjustSelf();
	if (!IS_IE) // 如果不是
		oTabComp.adjustSelf();// 在firefox中调用一次，布局会有问题，所以调用两次
	} else {
		var barHeight = getStyleAttribute(oTabComp.divItemsBar, "height");
		if (oTabComp.isHideTabHead)
			barHeight = 0;
		// 重新设置内部容器div高度
		oTabComp.divContent.style.height = oTabComp.Div_gen.offsetHeight
				- parseInt(barHeight) + 'px';
		// 重新设置页面布局高度
		layoutInitFunc();
	}
	*/
};

/**
 * 调整所有tab组件大小 ManageUI、CardUI、ListUI中调用adjustSpliter()方法时调用
 * 
 * @private
 */
TabComp.allTabCompResize = function() {
	for (var i = 0, n = tabCompArray.length; i < n; i++) {
		TabComp.tabCompResize(tabCompArray[i]);
	}
};

/**
 * 页签控件主体创建函数
 * 
 * @private
 */
TabComp.prototype.create = function() {
	// 创建整体包容div
	this.Div_gen = $ce("DIV");
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * TabComp回掉函数
 * 
 * @private
 */
TabComp.prototype.manageSelf = function() {
	var oThis = this;
	this.Div_gen.id = this.id;
	this.Div_gen.className = this.className;
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.height = this.height;
	this.Div_gen.style.width = this.width;
	// 如果tab宽度是100%则自动调整大小
//	this.Div_gen.onresize = function() {
//		TabComp.tabResize(oThis.id);
//	};
	
	addResizeEvent(this.Div_gen, function() {
		TabComp.tabResize(oThis.id);
	});
	var html = "<table class='bg_center_div' cellspacing='0' cellpadding='0'><tr><td></td><td></td><td></td></tr></table>";
	this.Div_gen.innerHTML = html;

	this.bgCenterDiv = this.Div_gen.firstChild;
	// 创建itemsBar工具条div
	this.divItemsBar = this.bgCenterDiv.rows[0].cells[1];//$ce("DIV");
//	this.Div_gen.appendChild(this.divItemsBar);
	this.divItemsBar.id = "div_items_bar_" + this.id;
	if (this.tabType == TabComp.TYPE_TOP)
		this.divItemsBar.className = "tab_items_bar";
	else
		this.divItemsBar.className = "tab_items_bar_bottom";
		

	/**
	 * 屏蔽空白区域的右键菜单
	 */
	this.divItemsBar.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		stopAll(e);
	};

	
	
	this.rightBarBorder = $ce('DIV');
	this.divItemsBar.appendChild(this.rightBarBorder);
	this.rightBarBorder.className = "right_fixed_space";

	this.leftBarBorder = $ce('DIV');
	this.divItemsBar.appendChild(this.leftBarBorder);
	this.leftBarBorder.className = "left_fixed_space";

	this.leftBarSpace = $ce("DIV");
	this.leftBarSpace.className = "left_space";
	this.divItemsBar.appendChild(this.leftBarSpace);

	// 创建内容部分div
	this.divContent = $ce("DIV");
	this.divContent.id = "div_content_" + this.id;
	this.divContent.className = "tab_content_div";
	
	this.bgLeftDiv = this.bgCenterDiv.rows[0].cells[0];//$ce("DIV");
	
//	this.bgCenterDiv = $ce("DIV");
//	this.bgCenterDiv.className = "bg_center_div";
	
//	var width = 0;
//	if (this.width && this.width.indexOf("%") == -1)
//		width = parseInt(this.width,10);
//	else
//		width = this.Div_gen.offsetWidth;
//	this.bgCenterDiv.style.width = width - 7*2 + "px";
	
//	this.bgCenterDiv.style.width = "100%";
	
	this.bgRightDiv = this.bgCenterDiv.rows[0].cells[2];//$ce("DIV");
	
	if (this.tabType == TabComp.TYPE_TOP){
		this.bgLeftDiv.className = "bg_top_left_div";
		this.bgRightDiv.className = "bg_top_right_div";
//		this.bgCenterDiv.appendChild(this.bgLeftDiv);
//		this.bgCenterDiv.appendChild(this.bgRightDiv);
//		this.Div_gen.appendChild(this.bgCenterDiv);
		
//		this.bgCenterDiv.appendChild(this.divItemsBar);
		this.Div_gen.appendChild(this.divContent);
	}
	else {
		this.bgLeftDiv.className = "bg_bottom_left_div";
		this.bgRightDiv.className = "bg_bottom_right_div";
		this.Div_gen.insertBefore(this.divContent, this.bgCenterDiv);
		
//		this.bgCenterDiv.appendChild(this.bgLeftDiv);
//		this.bgCenterDiv.appendChild(this.bgRightDiv);
//		this.Div_gen.appendChild(this.bgCenterDiv);
		
//		this.bgCenterDiv.appendChild(this.divItemsBar);
	}

	// 存放全部tabItem的数组
	this.tabItems = new Array();
	// 存放可见部分的item数组
	this.visibleItems = new Object;
	// 可见部分的开始位置
	this.visibleItems.begin = 0;
	// 可见部分的结束位置
	this.visibleItems.end = 0;
	// 可见部分的长度
	this.visibleItems.length = 0;
	// 用于判断是否显示左右按钮的标志
	this.showRLArrow = false;
	// 保存当前激活的item
	this.activedItem = null;

	this.rightBarSpace = $ce("DIV");
	this.rightBarSpace.className = "right_space";
	this.divItemsBar.appendChild(this.rightBarSpace);

	// 创建右侧放置关闭按钮的div
	this.divImg = $ce("DIV");
	this.divImg.className = "tab_img_div";
	this.divImg.style.border = "none";
	this.divItemsBar.appendChild(this.divImg);

	var barHeight = getStyleAttribute(this.divItemsBar, "height");
	if (barHeight == "auto")
		barHeight = 0;
	if (this.isHideTabHead)
		barHeight = 0;
	var contentHeight = this.Div_gen.offsetHeight - parseInt(barHeight);
	if(contentHeight > 0  && (this.tabType == TabComp.TYPE_BOTTOM || !this.flowmode)){
		this.divContent.style.height = contentHeight + "px";
	}
};

/**
 * 
 * 初始化右键菜单
 * 
 * @private
 * 
 */

TabComp.prototype.initContextMenu = function() {
	if (!this.contextMenu) {
		var menuName = this.id + "_menu";
		this.contextMenu = new ContextMenuComp(menuName, 0, 0, false);
		// 关闭当前页签
		var closeCurrentCaption = "关闭当前页签"; // TODO 多语
		var menuCloseCurrent = this.contextMenu.addMenu(menuName
				+ "_close_current", closeCurrentCaption, null, null, false);
		// 增加点击事件
		var closeCurrentMouseListener = new MouseListener();
		closeCurrentMouseListener.onclick = function(e) {
			var menuItem = e.obj;
			var currentItem = menuItem.parentOwner.triggerObj;
			currentItem.parentTab.removeItemTab(currentItem.parentTab
					.getItemIndex(currentItem));
		};
		menuCloseCurrent.addListener(closeCurrentMouseListener);
		// 关闭其它页签
		var closeOthersCaption = "关闭其它页签"; // TODO 多语
		var menuCloseOthers = this.contextMenu.addMenu(menuName
				+ "_close_others", closeOthersCaption, null, null, false);
		// 增加点击事件
		var closeOthersMouseListener = new MouseListener();
		closeOthersMouseListener.onclick = function(e) {
			var menuItem = e.obj;
			var currentItem = menuItem.parentOwner.triggerObj;
			var tab = currentItem.parentTab;
			for (var i = tab.tabItems.length - 1; i > 0; i--) {
				var tabItem = tab.tabItems[i];
				if (tabItem.showCloseIcon && tabItem != currentItem) {
					tab.removeItemTab(tab.getItemIndex(tabItem));
				}
			}
		};
		menuCloseOthers.addListener(closeOthersMouseListener);
		// 关闭所有页签
		var closeAllCaption = "关闭所有页签"; // TODO 多语
		var menuCloseAll = this.contextMenu.addMenu(menuName + "_close_all",
				closeAllCaption, null, null, false);
		// 增加点击事件
		var closeAllMouseListener = new MouseListener();
		closeAllMouseListener.onclick = function(e) {
			var menuItem = e.obj;
			var currentItem = menuItem.parentOwner.triggerObj;
			var tab = currentItem.parentTab;
			for (var i = tab.tabItems.length - 1; i > 0; i--) {
				var tabItem = tab.tabItems[i];
				if (tabItem.showCloseIcon) {
					tab.removeItemTab(tab.getItemIndex(tabItem));
				}
			}
		};
		menuCloseAll.addListener(closeAllMouseListener);
	}
};

/**
 * 改变大小
 * 
 * @private
 */
TabComp.tabResize = function(tabId) {
	var tab = window.objects[tabId];
	if (tab == null)
		return;
	var outerDiv = tab.Div_gen;

	try {
		// 如果此时tab大小和上次不发生变化,则不会真正的重新调整grid大小
		var cond1 = (tab.outerDivWidth != null && tab.outerDivWidth == outerDiv.offsetWidth);
		var cond2 = (tab.outerDivHeight != null && tab.outerDivHeight == outerDiv.offsetHeight);
		var cond3 = (tab.lastTabSelectedIndex != null && tab.lastTabSelectedIndex == tab
				.getSelectedIndex());
		if (cond1 && cond2 && cond3)
			return;
		tab.outerDivWidth = outerDiv.offsetWidth;
		tab.outerDivHeight = outerDiv.offsetHeight;
		tab.lastTabSelectedIndex = tab.getSelectedIndex();
		tab.adjustSelf();
		
//		tab.bgCenterDiv.style.width = outerDiv.offsetWidth - 7*2 + "px";//7*2左右边框图片宽度
	} catch (error) {
		log("tab resize error");
	}
};

/**
 * 重置各子项的样式
 * 
 * @private
 */
TabComp.prototype.resetItemStyle = function() {
	var currentIndex = this.getSelectedIndex();
	if (currentIndex == null)
		currentIndex = 0;
	for (var i = 0, n = this.tabItems.length; i < n; i++) {
		var item = this.tabItems[i];
		// 设置中间位置样式
		if (currentIndex == i) {
			item.isActive = true;
			item.borderCenter.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_centerborder_on" : "tab_item_centerborder_on_bottom";
		} else {
			item.isActive = false;
			item.borderCenter.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_centerborder_off" : "tab_item_centerborder_off_bottom";
		}
		// 设置第一个页签的左边位置样式
		if (i == 0) {
			if (currentIndex == i) { // 当前为选中项
				item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_on_0" : "tab_item_leftborder_on_0_bottom";
				if (i != n - 1) {
					item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_on_1" : "tab_item_rightborder_on_1_bottom";
				}
			} else { // 当前为未选中项
				item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_0" : "tab_item_leftborder_off_0_bottom";
				if (currentIndex == i + 1) { // 右边为选中项
					item.borderRight.className = this.tabType == TabComp.TYPE_TOP ?  "tab_item_rightborder_off_1" : "tab_item_rightborder_off_1_bottom";
				} else { // 右边为未选中项
					item.borderRight.className = this.tabType == TabComp.TYPE_TOP ?  "tab_item_rightborder_off_2" : "tab_item_rightborder_off_2_bottom";
				}
			}
		}
		// 设置最后一个页签的右边位置样式
		if (i == n - 1) {
			if (currentIndex == i) { // 当前为选中项
				item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_on_0" : "tab_item_rightborder_on_0_bottom";
				if (i != 0) {
					item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_on_1" : "tab_item_leftborder_on_1_bottom";
				}
			} else { // 当前为未选中项
				item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_off_0" : "tab_item_rightborder_off_0_bottom";
				if (currentIndex == i - 1) { // 左边为选中项
					item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_1" : "tab_item_leftborder_off_1_bottom";
				} else { // 左边为未选中项
					item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_2" : "tab_item_leftborder_off_2_bottom";
				}
			}
		}
		// 设置其他位置页签的左右样式
		if (i != 0 && i != n - 1) {
			if (currentIndex == i) { // 选中项
				item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_on_1" : "tab_item_leftborder_on_1_bottom";
				item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_on_1" : "tab_item_rightborder_on_1_bottom";
			} else { // 未选中项
				if (currentIndex == i - 1) { // 左边为选中项
					item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_1" : "tab_item_leftborder_off_1_bottom";
					item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_off_2" : "tab_item_rightborder_off_2_bottom";
				} else if (currentIndex == i + 1) { // 右边为选中项
					item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_2" : "tab_item_leftborder_off_2_bottom";
					item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_off_1" : "tab_item_rightborder_off_1_bottom";
				} else { // 左右都为未选中项
					item.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_2" : "tab_item_leftborder_off_2_bottom";
					item.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_off_2" : "tab_item_rightborder_off_2_bottom";
				}
			}
		}
		// 设置删除按钮样式
		if (item.closeImgDiv) {
			if (currentIndex == i){
				item.closeImgDiv.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon_on" : "tab_item_closeicon_on_bottom";
			}
			else{
				item.closeImgDiv.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon" : "tab_item_closeicon_bottom";
			}
			if(item.borderCenter.children && item.borderCenter.children.length > 0){
				item.borderCenter.children[item.borderCenter.children.length-1].style.marginRight = "13px";
			}
		}
	}
};

/**
 * 处理点击左右移动按扭后的函数
 * 
 * @param direction
 *            用数字表示移动的方向(1:向右移动,-1:向左移动)
 * @private
 */
TabComp.prototype.MoveTab = function(direction) {
	if (this.tabItems == null || this.tabItems.length == 0)
		return;

	// 点击向右的按钮
	if (direction > 0) {
		if (this.tabItems[this.visibleItems.end + 1]) {
			// 存放下一个要显示出来的item(右边的)
			this.nextDisplayItem = this.tabItems[this.visibleItems.end + 1];
			// 存放当前显示的最左边的item
			this.borderItem = this.tabItems[this.visibleItems.begin];
			// 存放当前所有显示item的开始位置
			var mayActivePos = this.visibleItems.begin;

			if (!this.tempLength)
				this.tempLength = this.visibleItems.begin + 1;

			// 使两个指示指针向指定方向移动,卡住新范围内的可见items
			this.visibleItems.begin += direction;

			// 处理下一个将要显示的item
			if (this.nextDisplayItem.trueVisible == true)
				this.nextDisplayItem.showTitle();
			// 如果要隐藏的item是ActiveTab,则改变ActiveTab的位置
			if (this.currActiveTab == mayActivePos) {
				// 调用用户的函数,处理用户的事件
				// var tempPosi = this.currActiveTab + 1; //得到当前激活组件的位置的前一个位置
				// index, triggerItem, currItem, targetItem
				// this.beforeActivedTabItemChange(null,
				// this.tabItems[this.currActiveTab], this.tabItems[tempPosi]);
				this.activeTab(mayActivePos + direction,
						this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChangeForInternal(this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChange(this.tabItems[this.currActiveTab]);
			}
			this.manageImages();
			this.changeVisibleItems();
		} else {
			if (this.tempLength < this.tabItems.length - 1) {
				this.tempLength = this.tempLength + direction;
				// 调用用户的函数,处理用户的事件
				// this.beforeActivedTabItemChange(null,
				// this.tabItems[this.currActiveTab],
				// this.tabItems[this.tempLength]);
				this.activeTab(this.tempLength,
						this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChangeForInternal(this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChange(this.tabItems[this.currActiveTab]);
			} else {
				// 调用用户的函数,处理用户的事件
				// this.beforeActivedTabItemChange(null,
				// this.tabItems[this.tabItems.length - 1],
				// this.tabItems[this.tabItems.length - 1]);
				// this.activeTab(this.tabItems.length - 1,
				// this.tabItems[this.tabItems.length - 1]);
				// this.afterActivedTabItemChangeForInternal(this.tabItems[this.tabItems.length
				// - 1]);
				// this.afterActivedTabItemChange(this.tabItems[this.tabItems.length
				// - 1]);
			}
		}
	}
	// 点击向左的的按钮
	else {
		if (this.tabItems[this.visibleItems.begin - 1]) {
			// 存放下一个要显示出来的item(左边的)
			this.nextDisplayItem = this.tabItems[this.visibleItems.begin - 1];
			// 存放当前显示的最右边的item
			this.borderItem = this.tabItems[this.visibleItems.end];
			// 存放当前所有显示item的开始位置
			var mayActivePos = this.visibleItems.end;
			if (!this.tempLength)
				this.tempLength = this.visibleItems.end - 1;

			// 使两个指示指针向指定方向移动,卡住新范围内的可见items
			this.visibleItems.begin += direction;
			this.visibleItems.end += direction;

			// 处理下一个将要显示的item
			if (this.nextDisplayItem.trueVisible != false)
				this.nextDisplayItem.showTitle();

			// 如果要隐藏的item是ActiveTab,则改变ActiveTab的位置
			if (this.currActiveTab == mayActivePos) {
				// 得到当前激活组件的位置的前一个位置
				// var tempPosi = this.currActiveTab - 1;
				// 调用用户的函数,处理用户的事件
				// this.beforeActivedTabItemChange(null,
				// this.tabItems[this.currActiveTab], this.tabItems[tempPosi]);
				this.activeTab(mayActivePos + direction,
						this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChangeForInternal(this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChange(this.tabItems[this.currActiveTab]);
			}
			this.manageImages();
			this.changeVisibleItems();
		} else {
			if (this.tempLength > 0) {
				this.tempLength = this.tempLength + direction;
				// 调用用户的函数,处理用户的事件
				// this.beforeActivedTabItemChange(null,
				// this.tabItems[this.currActiveTab]
				// ,this.tabItems[this.tempLength]);
				// if(this.tabItems[this.tempLength].trueVisble != false)
				// this.tabItems[this.tempLength].showTitle();
				this.activeTab(this.tempLength,
						this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChangeForInternal(this.tabItems[this.currActiveTab]);
				// this.afterActivedTabItemChange(this.tabItems[this.currActiveTab]);
			} else {
				// 调用用户的函数,处理用户的事件
				// this.beforeActivedTabItemChange(null, this.tabItems[0],
				// this.tabItems[0]);
				this.activeTab(0, this.tabItems[0]);
				// this.afterActivedTabItemChangeForInternal(this.tabItems[0]);
				// this.afterActivedTabItemChange(this.tabItems[0]);
			}
		}
	}
};

/**
 * 根据情况设置图片的显示状态(现改为始终显示左右箭头)
 * 
 * @private
 */
TabComp.prototype.manageImages = function() {

	this.divImg.style.width = TabComp.divImgWidth + "px";

		// this.previous.show();
		// this.next.show();

		// 向左向右的按钮始终处于激活状态
		// this.previous.active();
		// this.next.active();
};

/**
 * 改变显示区的items
 * 
 * @private
 */
TabComp.prototype.changeVisibleItems = function(millTime) {
	if (!millTime)
		defaultMillTime = 100;
	else
		defaultMillTime = millTime / 10;
	if (parseInt(this.borderItem.tabItemDiv.style.width) > 20) {
		this.borderItem.changeWidth(-20);
		this.nextDisplayItem.changeWidth(+20);
		this.time = window.setTimeout("window.objects['" + this.id
				+ "'].changeVisibleItems(" + defaultMillTime + ");",
				defaultMillTime);
	} else
		this.borderItem.hide();
};

/**
 * 跟据id激活组件
 * 
 * @param {} id
 */
TabComp.prototype.activeTabByName = function(name){
	var item = this.getItemByName(name);
	if (item == null) return;
	var index = this.getItemIndex(item);
	if (index == -1) return;
	this.activeTab(index);
};


/**
 * 指定某位置激活组件
 */
TabComp.prototype.activeTab = function(index) {
	index = parseInt(index);
	if (this.currActiveTab == index)
		return;
	if (index > this.tabItems.length - 1 || index < 0)
		return;
	else {
		
		if (this.tabItems[index].trueVisible == false)
			this.showItem(this.tabItems[index].name);

		// 保存当前激活的tab索引
		this.currActiveTab = index;
		// 使当前tab可见
		this.letBeginToEndVisible();
		var tempActivedItem = this.tabItems[index];
		this.activedItem = tempActivedItem;
		tempActivedItem.showContent();
		this.adjustSelf();
		// 激活的还是自己只需设置一下display属性
		// else
		// tempActivedItem.divContent.style.visibility = "";
		// this.divContent.removeChild(tempActivedItem.divContent);
		// if(this.divContent.firstChild != null)
		// this.divContent.insertBefore(tempActivedItem.divContent,
		// this.divContent.firstChild);
		// else
		// this.divContent.appendChild(tempActivedItem.divContent);
		this.tempLength = index;

		this.afterActivedTabItemChangeForInternal(this.tabItems[index]);
		this.afterActivedTabItemChange(this.tabItems[index]);

		// 重置各子项的样式
		this.resetItemStyle();

		// guoweic: added start 2009-10-21
		// 调用布局初始化方法，以调整tabItem中包含的其他布局
		layoutInitFunc();
		// guoweic: added end
	}
};

/**
 * 使指定位置的组件激活
 * 
 * @param index
 *            当前要激活的item的位置
 * @param activedItem
 *            当前激活的item
 */
TabComp.prototype.activeTab = function(index, activedItem) {
	index = parseInt(index);
	if (this.currActiveTab == index)
		return;
	if (index > this.tabItems.length - 1 || index < 0)
		return;
	else {
		if (this.tabItems[index].trueVisible == false)
			this.showItem(this.tabItems[index].name);


		if (this.beforeActivedTabItemChange(activedItem, this.tabItems[index]) == false)
			return;
		// 保存当前激活的tab索引
		this.currActiveTab = index;
		// 使当前tab可见
		this.letBeginToEndVisible();
		var tempActivedItem = this.tabItems[index];
		if (this.activedItem != tempActivedItem) {
			if (this.activedItem != null) {
				var oldActiveItem = this.activedItem;
				oldActiveItem.hideContent();
			}
			this.activedItem = tempActivedItem;
			tempActivedItem.showContent();
			this.adjustSelf();
		}
		// 激活的还是自己只需设置一下display属性
		else
			tempActivedItem.divContent.style.visibility = "";
		// this.divContent.removeChild(tempActivedItem.divContent);
		// if(this.divContent.firstChild != null)
		// this.divContent.insertBefore(tempActivedItem.divContent,
		// this.divContent.firstChild);
		// else
		// this.divContent.appendChild(tempActivedItem.divContent);
		this.tempLength = index;

		this.afterActivedTabItemChangeForInternal(this.tabItems[index]);
		this.afterActivedTabItemChange(this.tabItems[index]);

		// 重置各子项的样式
		this.resetItemStyle();

		// guoweic: added start 2009-10-21
		// 调用布局初始化方法，以调整tabItem中包含的其他布局
		layoutInitFunc();
		// guoweic: added end
	}
};

/**
 * 由item项的名字得到item项的索引
 * 
 * @return -1表示没有找到
 */
TabComp.prototype.getItemIndexByName = function(name) {
	for (var i = 0, count = this.tabItems.length; i < count; i++) {
		if (this.tabItems[i].name == name)
			return i;
	}
	return -1;
};

/**
 * 得到item的索引值(索引值从0开始)
 */
TabComp.prototype.getItemIndex = function(item) {
	return this.tabItems.indexOf(item);
};

/**
 * 获取第一个非隐藏页签
 */
TabComp.prototype.getFirstVisibleItemIndex = function() {
	for (var i = 0, count = this.tabItems.length; i < count; i++) {
		if (this.tabItems[i].trueVisible == true)
			return i;
	}
	return -1;
};

/**
 * 根据name获取item
 */
TabComp.prototype.getItemByName = function(name) {
	for (var i = 0, count = this.tabItems.length; i < count; i++) {
		if (this.tabItems[i].name == name)
			return this.tabItems[i];
	}
	return null;
};

/**
 * 显示item
 */
TabComp.prototype.showItem = function(name) {
	if (name == null)
		return;
	var item = this.getItemByName(name);
	if (item != null) {
		item.trueVisible = true;
		// 如果显示的tabItem不是激活的item,那么只是显示出tabItemDiv，不显示内容
		if (this.getSelectedItem() != null
				&& this.getSelectedItem().name == name)
			item.show();
		else
			item.tabItemDiv.style.display = "block";
	}
};

/**
 * 隐藏item
 */
TabComp.prototype.hideItem = function(name) {
	if (name == null)
		return;
	var item = this.getItemByName(name);
	if (item != null) {
		item.trueVisible = false;
		item.hide();
		//找到第一个不隐藏的item,显示出来
		for (var i = 0; i < this.tabItems.length ; i++){
			if (this.tabItems[i].trueVisible == true){
				this.activeTab(i);
				break;
			}
		}
	}
};

/**
 * 得到当前的激活项
 */
TabComp.prototype.getSelectedItem = function() {
	if (this.currActiveTab == -1)
		return null;
	return this.tabItems[this.currActiveTab];
};

/**
 * 得到当前激活项的索引值
 */
TabComp.prototype.getSelectedIndex = function() {
	return this.currActiveTab;
};

/**
 * 创建指定item子项
 */
TabComp.prototype.createItem = function(name, title, showCloseIcon,
		isFirstItem, disabled, pageState) {
	// TODO:pageState不应放这里
	var oThis = this;
	var sep = null;
	if (this.tabItems == null || this.tabItems.length == 0)
		isFirstItem = true;
	if (!isFirstItem) {
		sep = $ce("DIV");
		sep.className = "tab_seperator_ver";
		this.divItemsBar.appendChild(sep);
	}

	// tabItem是否采用自动宽度
	var autoWidth = true;
	if (this.tabItemWidth > 0)
		autoWidth = false;

	// 创建item
	var item = new TabItem(name, title, showCloseIcon, autoWidth, {
		'disabled' : disabled,
		'pageState' : pageState,
		'tabType' : this.tabType
	});
	// 将sep绑定到每个item数据对象上
	if (!isFirstItem)
		item.sep = sep;
	item.parentTab = this;
	item.create();

	var end = this.tabItems.push(item);
	this.divContent.appendChild(item.getObjHtml());
	this.divItemsBar.appendChild(item.tabItemDiv);

	this.letBeginToEndVisible();
	// 当item过多时显示左右移动按钮于右侧
	if (this.visibleItems.length < this.tabItems.length)
		this.showRLArrow = true;
	if (this.showRLArrow) {
		if (this.next == null && this.previous == null) {
			// 创建"箭头向右"的图标
			attrObj = {
				position : 'relative',
				boolFloatRight : true,
				pRefInactive : window.themePath + "/ui/container/tab/images/toright.gif"
			};
			this.next = new ImageComp(this.divImg, "ToRight", window.themePath
					+ "/ui/container/tab/images/toright.gif", 0, 0, "24", "22",
					trans("ml_toright"), window.themePath
							+ "/ui/container/tab/images/toright.gif", attrObj);
			var barHeight = parseInt(this.divItemsBar.offsetHeight), imageHeight = parseInt(this.next.Div_gen.offsetHeight);

			this.next.Div_gen.style.top = 0 + "px";

			this.next.onclick = function(e) {
				oThis.MoveTab(1);
			};

			// 创建"箭头向左"的图标
			attrObj = {
				position : 'relative',
				boolFloatRight : true,
				pRefInactive : window.themePath + "/ui/container/tab/images/toleft.gif"
			};
			this.previous = new ImageComp(this.divImg, "ToLeft",
					window.themePath + "/ui/container/tab/images/toleft.gif", 0, 0, "24",
					"22", trans("ml_toleft"), window.themePath
							+ "/ui/container/tab/images/toleft.gif", attrObj);

			this.previous.Div_gen.style.top = 0 + "px";

			this.previous.onclick = function(e) {
				oThis.MoveTab(-1);
			};
			this.divImg.style.display = "block";
		}
	}
	// item的click事件
	item.click = function(e) {
		oThis.tempLength = oThis.tabItems.indexOf(item);
		if (item.onclick(e) == false)
			return;

		// 调用用户的函数,处理用户的事件
		// if(oThis.beforeActivedTabItemChange(e.triggerItem,
		// oThis.tabItems[oThis.currActiveTab],
		// oThis.tabItems[oThis.tempLength]) == false)
		// return;
		oThis.activeTab(oThis.tempLength, oThis.tabItems[oThis.currActiveTab]);
		// TODO:
		if (item.pageState != null && item.pageState != "")
			pageUI.setPageState(item.pageState);
		// oThis.afterActivedTabItemChangeForInternal(oThis.tabItems[oThis.currActiveTab]);
		// oThis.afterActivedTabItemChange(oThis.tabItems[oThis.currActiveTab]);
		// 继续将事件传到itemsBar中
		oThis.onclick(e);
	};

	item.show();

	// 重置各子项的样式
	this.resetItemStyle();
	if (this.oneTabHide == true && this.isHideTabHead == true && this.visibleItems.length > 1)
		this.showTabHead();		
	return item;
};

/**
 * 使处于begin和end间的items可见
 * 
 * @private
 */
TabComp.prototype.letBeginToEndVisible = function() {
	this.manageVisibleItems();

	// 先隐藏掉所有的items
	for (var i = 0, count = this.tabItems.length; i < count; i++)
		this.tabItems[i].hide();
	// 使处于begin和end间的item可见,重新设定begin to end的items的弹出菜单的left值
	for (var i = this.visibleItems.begin; i <= this.visibleItems.end; i++) {
		if (i >= this.tabItems.length)
			break;
		var item = this.tabItems[i];
		if (item.trueVisible == true)
			item.showTitle();
	}
	this.manageImages();
};

/**
 * 调整可见区域的begin和end,使得当前激活的tab处于可见区
 * 
 * @private
 */
TabComp.prototype.manageVisibleItems = function() {
	var itemsRealLength = 0;
	// 创建了左右箭头按钮时的items可见区长度
	if (this.showRLArrow)
		itemsRealLength = this.divItemsBar.offsetWidth - TabComp.divImgWidth
				- this.leftBarSpace.offsetWidth
				- this.rightBarSpace.offsetWidth
				- this.leftBarBorder.offsetWidth
				- this.rightBarBorder.offsetWidth - 100;
	else
		itemsRealLength = this.divItemsBar.offsetWidth
				- this.leftBarSpace.offsetWidth
				- this.rightBarSpace.offsetWidth
				- this.leftBarBorder.offsetWidth
				- this.rightBarBorder.offsetWidth;

	// 调整可见区的begin和end,使currActiveTab处于可见区
	var needCal = true;
	while (needCal) {
		var visibleLength = 0;
		itemsRealLength -= 30;
		var lastBorderWidth = 0;
		for (var i = this.visibleItems.begin; i < this.tabItems.length; i++) {
			var itemWidth = this.tabItems[i].tabItemDiv.scrollWidth;
			// 隐藏值根据前一个推算
			if (itemWidth == 0) {
				if (lastBorderWidth == 0) {
					if (this.tabItems[i - 1]) {
						lastBorderWidth = this.tabItems[i - 1].tabItemDiv.offsetWidth
								- parseInt(this.tabItems[i - 1].divTitle.style.width);
						itemWidth = parseInt(this.tabItems[i].divTitle.style.width)
								+ lastBorderWidth;
					}
				}
			}
			if (itemWidth == 0) {
				this.tabItems[i].showTitle();
				itemWidth = this.tabItems[i].tabItemDiv.scrollWidth;
				this.tabItems[i].hide();
			}
			itemsRealLength -= itemWidth;
			if (itemsRealLength >= 0) {
				visibleLength++;
			} else {
				break;
			}
		}

		if (this.currActiveTab > this.visibleItems.begin + visibleLength) {
			this.visibleItems.begin = this.currActiveTab - visibleLength + 1;
			needCal = (itemsRealLength > 0);
		} else {
			this.visibleItems.length = visibleLength;
			needCal = false;
		}
	}
	if (itemsRealLength > 0) {
		for (var i = this.visibleItems.begin - 1; i > 0; i--) {
			var itemWidth = this.tabItems[i].tabItemDiv.scrollWidth;
			// 隐藏值根据前一个推算
			if (itemWidth == 0) {
				if (lastBorderWidth == 0) {
					if (this.tabItems[i - 1]) {
						lastBorderWidth = this.tabItems[i - 1].tabItemDiv.offsetWidth
								- parseInt(this.tabItems[i - 1].divTitle.style.width);
						itemWidth = parseInt(this.tabItems[i].divTitle.style.width)
								+ lastBorderWidth;
					}
				}
			}
			if (itemWidth <= 0) {
				this.tabItems[i].showTitle();
				itemWidth = this.tabItems[i].tabItemDiv.scrollWidth;
				this.tabItems[i].hide();
			}
			itemsRealLength -= itemWidth;
			if (itemsRealLength >= 0) {
				visibleLength++;
				this.visibleItems.begin = i;
			} else {
				break;
			}
		}
	}

	this.visibleItems.length = visibleLength;
	this.visibleItems.end = (this.visibleItems.begin + this.visibleItems.length
			- 1 < this.tabItems.length) ? this.visibleItems.begin
			+ this.visibleItems.length - 1 : this.visibleItems.length - 1;
	this.showRLArrow = this.visibleItems.length < this.tabItems.length;

};

/**
 * 从tab中移除掉指定的item
 */
TabComp.prototype.removeItemTab = function(index) {
	if (this.tabItems.length == 0)
		return;

	var item = this.tabItems[index];
	// 在处理之前留给用户执行自己的代码段的机会,用户可以重载tab的closeItem方法来实现自己的功能
	if (item.close() == false)
		return;

	if (this.tabItems.length >= 2) {
		// 删除tab处于可见区尾,且是激活tab
		var targetItem = null;
		if (index == this.visibleItems.end && index == this.currActiveTab) {
			// this.beforeActivedTabItemChange(null, item,
			// this.tabItems[index-1]);
			targetItem = this.tabItems[index - 1];
			this.visibleItems.end--;
		}
		// 删除的tab在可见区中间或开头,且是激活tab
		else if (index != this.visibleItems.end && index == this.currActiveTab) {
			// this.beforeActivedTabItemChange(null, item,
			// this.tabItems[index+1]);
			targetItem = this.tabItems[index + 1];
		}

		// 删除此tab边上的竖条
		if (item.sep)
			item.sep.parentNode.removeChild(item.sep);

		// 删除掉此tab
		this.divItemsBar.removeChild(item.tabItemDiv);
		item.divContent.innerHTML = "";
		this.divContent.removeChild(item.divContent);
		this.tabItems.splice(index, 1);
		this.letBeginToEndVisible();

		if (this.tabItems[index])
			this.activeTab(index, targetItem);
		else
			this.activeTab(index - 1, targetItem);

		// this.afterActivedTabItemChangeForInternal(item);
		// this.afterActivedTabItemChange(item);
	}
	// 只剩一个tab,且是激活tab
	else if (this.tabItems.length == 1 && index == this.currActiveTab) {
		this.beforeActivedTabItemChange(item, null);
		this.divItemsBar.removeChild(item.tabItemDiv);
		item.divContent.innerHTML = "";
		this.divContent.removeChild(item.divContent);
		this.tabItems.splice(index, 1);
	} else if (this.tabItems.length < 1)
		return;

		// if(index != this.currActiveTab)
		// {
		// this.divItemsBar.removeChild(item.tabItemDiv);
		// this.divContent.removeChild(item.divContent);
		// this.tabItems.splice(index, 1);
		// }

};

/**
 * 调整大小
 * 
 * @private
 */
TabComp.prototype.adjustSelf = function() {
	var selectedItem = this.getSelectedItem();
	if (!selectedItem) {
		var barHeight = getStyleAttribute(this.divItemsBar, "height");
		if (this.isHideTabHead)
			barHeight = 0;
		var height = this.Div_gen.offsetHeight - parseInt(barHeight);
//		if (height > 0)
//			this.divContent.style.height = height + 'px';

		return;
	}
	var oThis = this;
	this.letBeginToEndVisible();
	if (selectedItem.trueVisible != false)
		this.getSelectedItem().show();
	// 如果显示区长度小于所有items长度,则显示左右按钮
	if (this.visibleItems.length < this.tabItems.length) {
		// 创建"箭头向右"的图标
		if (this.next == null && this.previous == null) {
			attrObj = {
				position : 'relative',
				boolFloatRight : true,
				pRefInactive : window.themePath + "/ui/container/tab/images/toright.gif"
			};
			this.next = new ImageComp(this.divImg, "ToRight", window.themePath
					+ "/ui/container/tab/images/toright.gif", -8, 0, "", "", "To Right",
					window.themePath + "/ui/container/tab/images/toright.gif", attrObj);
			this.next.onclick = function(e) {
				oThis.MoveTab(1);
			};

			// 创建"箭头向左"的图标
			attrObj = {
				position : 'relative',
				boolFloatRight : true,
				pRefInactive : window.themePath + "/ui/container/tab/images/toleft.gif"
			};
			this.previous = new ImageComp(this.divImg, "ToLeft",
					window.themePath + "/ui/container/tab/images/toleft.gif", -17, 0, "",
					"", "To Left",
					window.themePath + "/ui/container/tab/images/toleft.gif", attrObj);
			this.previous.onclick = function(e) {
				oThis.MoveTab(-1);
			};
		}
		this.divImg.style.display = "block";
	} else {
		this.divImg.style.display = "none";
	}

	var barHeight = this.divItemsBar.offsetHeight;
	if (this.isHideTabHead)
		barHeight = 0;
	var contentHeight = (this.Div_gen.offsetHeight - parseInt(barHeight));
	if(contentHeight > 20 && (this.tabType == TabComp.TYPE_BOTTOM || !this.flowmode)){
		this.divContent.style.height = contentHeight + "px";
	}
	// 此处是为单表体显示时grid能够正常显示的处理
	this.divContent.offsetHeight;
	if (window.editMode)
		layoutInitFunc();
};

/**
 * 隐藏tab头
 */
TabComp.prototype.hideTabHead = function() {
	this.isHideTabHead = true;
	//this.divItemsBar.style.display = "none";
	this.bgCenterDiv.style.display = "none";
	this.adjustSelf();
};

/**
 * 显示tab头
 */
TabComp.prototype.showTabHead = function() {
	this.isHideTabHead = false;
	this.divItemsBar.style.display = "block";
	this.adjustSelf();
};

TabComp.prototype.getTabItems = function() {
	return this.tabItems;
};

/**
 * 供内部使用的方法
 * 
 * @private
 */
TabComp.prototype.afterActivedTabItemChangeForInternal = function(targetItem) {
	var tmpFunc = window['$' + targetItem.parentTab.id + '_' + targetItem.id
			+ '_init'];
	if (tmpFunc) {
		this.beforeItemInit(targetItem);
		tmpFunc();
		window['$' + targetItem.parentTab.id + '_' + targetItem.id + '_init'] = null;
		this.afterItemInit(targetItem);
	}
};

/**
 * 点击事件
 * 
 * @private
 */
TabComp.prototype.onclick = function(e) {
	var mouseEvent = {
		"obj" : this,
		"event" : e
	};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 子项初始化前事件
 * 
 * @private
 */
TabComp.prototype.beforeItemInit = function(targetItem) {
	var tabItemEvent = {
		"obj" : this,
		"item" : targetItem
	};
	this.doEventFunc("beforeItemInit", TabListener.listenerType, tabItemEvent);
};

/**
 * 子项初始化后事件
 * 
 * @private
 */
TabComp.prototype.afterItemInit = function(targetItem) {
	var tabItemEvent = {
		"obj" : this,
		"item" : targetItem
	};
	this.doEventFunc("afterItemInit", TabListener.listenerType, tabItemEvent);
};

/**
 * 在关闭item之前会调用该方法,返回false将阻止关闭页签
 * 
 * @private
 */
TabComp.prototype.closeItem = function(item) {
	var tabItemEvent = {
		"obj" : this,
		"item" : item
	};
	return this
			.doEventFunc("closeItem", TabListener.listenerType, tabItemEvent);
};

/**
 * 当激活item位置变化时会掉用这个函数,用户可以重载此函数处理自己事情
 * 
 * @param activedItem
 *            当前激活的item
 * @param targetItem
 *            要被激活的item
 * @private
 */
TabComp.prototype.beforeActivedTabItemChange = function(activedItem, targetItem) {
	var tabItemChangeEvent = {
		"obj" : this,
		"activedItem" : activedItem,
		"targetItem" : targetItem
	};
	this.doEventFunc("beforeActivedTabItemChange", TabListener.listenerType,
			tabItemChangeEvent);
};

/**
 * 当激活item位置变化后会掉用这个函数,用户可以重载此函数处理自己事情
 * 
 * @private
 */
TabComp.prototype.afterActivedTabItemChange = function(activedItem) {
	var tabItemEvent = {
		"obj" : this,
		"item" : activedItem
	};
	this.doEventFunc("afterActivedTabItemChange", TabListener.listenerType,
			tabItemEvent);
};

TabComp.prototype.setOnTabHide = function(hide){
	if (hide == null) return;
	this.oneTabHide = hide;
	// 显示的tabitem数
	var showItemCount = 0;
	
	for (var i = 0; i < this.tabItems.length; i++){
		if (this.tabItems[i].trueVisible == true)
			showItemCount++;
	}
	
	if (showItemCount == 1 && this.oneTabHide
			&& (this.isHideTabHead == null || this.isHideTabHead == false))
		this.hideTabHead();
	else if (showItemCount > 1 && this.isHideTabHead)
		this.showTabHead();

};


/*******************************************************************************
 * 
 * 创建tabItem
 * 
 ******************************************************************************/
TabItem.prototype = new BaseComponent;
TabItem.prototype.componentType = "TABITEM";

/**
 * TabItem构造函数
 * 
 * @class Tab子项
 * 
 * Tab子项构造函数
 * @param{boolean} showCloseIcon 是否显示关闭按钮,默认不显示
 * @param{boolean} autoWidth 是否自适应宽度,自适应宽度将按照title的长度进行显示
 */
function TabItem(name, title, showCloseIcon, autoWidth, attrObj) {
	this.base = BaseComponent;
	this.base(name, "", "", "", "");
	this.name = name;
	this.showCloseIcon = getBoolean(showCloseIcon, false);
	this.tabType = TabComp.TYPE_TOP;
	this.autoWidth = getBoolean(autoWidth, false);
	var oThis = this;
	this.title = getString(title, "");
	this.parentTab = null;
	this.trueVisible = true;

	this.disabled = false;
	this.isActive = false;
	if (attrObj != null) {
		this.disabled = getBoolean(attrObj.disabled, false);
		this.pageState = getString(attrObj.pageState, "");
		this.tabType = attrObj.tabType;
	}
};

/**
 * 真正创建tabitem
 * 
 * @private
 */
TabItem.prototype.create = function() {
	var oThis = this;

	// 创建每个tab的总div
	this.tabItemDiv = $ce("TABLE");
	this.tabItemDiv.cellSpacing = '0px';
	this.tabItemDiv.cellPadding = '0px';
	this.tabItemDiv.style.position = "relative";
	this.tabItemDiv.title = this.title;
	this.tabItemDiv.id = "div_tabitem_" + this.name;
	this.tabItemDiv.objType = "tabItem"; // 解决编辑态下 页签切换的标识
	// 得到文字的宽度
//	var textWidth = getTextWidth(this.title, this.parentTab.className + "_text_width") + 2;
	var className = this.tabType == TabComp.TYPE_TOP ? "tab_item_div" : "tab_item_div_bottom";
	this.tabItemDiv.className = className;
	
	var row = this.tabItemDiv.insertRow(-1);
	// 创建每个tab的左边界显示效果
	this.borderLeft = row.insertCell(-1); 
	this.borderLeft.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_leftborder_off_2" : "tab_item_leftborder_off_2_bottom";
	//this.tabItemDiv.appendChild(this.borderLeft);
	//var leftWidth = getStyleAttribute(this.borderLeft, "width");

	// 创建每个tab中部显示效果
	this.borderCenter = row.insertCell(-1);
	this.borderCenter.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_centerborder_off" : "tab_item_centerborder_off_bottom";
//	this.tabItemDiv.appendChild(this.borderCenter);
//	var paddingLeft = getStyleAttribute(this.borderCenter, "paddingLeft");
//	var paddingRight = getStyleAttribute(this.borderCenter, "paddingRight");
//	var centerWidth = textWidth + parseInt(paddingLeft)
//			+ parseInt(paddingRight);

	// 创建每个tab的右边界显示效果
	this.borderRight = row.insertCell(-1);//$ce("DIV");
//	this.tabItemDiv.appendChild(this.borderRight);
	this.borderRight.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_rightborder_off_2" : "tab_item_rightborder_off_2_bottom";
//	var rightWidth = getStyleAttribute(this.borderRight, "width");

	// 创建中部显示标题名字的div
	this.divTitle = $ce("DIV");
	this.divTitle.style.minWidth = "40px";
	this.divTitle.appendChild(document.createTextNode(this.title));
//	this.divTitle.style.width = textWidth + "px";
	//if (this.tabType != TabComp.TYPE_TOP){
		//var topDiv = $ce("DIV");
//		topDiv.style.width = textWidth + "px";
		//topDiv.style.height = "10px";
		//this.borderCenter.appendChild(topDiv);
	//}
	this.borderCenter.appendChild(this.divTitle);

	// 创建内容面板
	this.divContent = $ce("DIV");
	if (IS_IE)
		this.divContent.style.width = "100%";
	this.divContent.style.height = "100%";
	this.divContent.owner = this;

	// 创建item关闭按钮
	if (this.showCloseIcon && this.closeImgDiv == null) {
		this.closeImgDiv = $ce("div");
		this.tabItemDiv.appendChild(this.closeImgDiv);
		if (this.isActive)
			this.closeImgDiv.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon_on" : "tab_item_closeicon_on_bottom";
		else
			this.closeImgDiv.className = this.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon" : "tab_item_closeicon_bottom";

		// 关闭该页签逻辑
		this.closeImgDiv.onclick = function(e) {
			e = EventUtil.getEvent();
			oThis.parentTab.hideItem(oThis.name);
//			oThis.parentTab.removeItemTab(oThis.parentTab.getItemIndex(oThis));
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
		this.closeImgDiv.onmouseover = function(e) {
			this.className = oThis.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon_over" : "tab_item_closeicon_over_bottom";
		};
		this.closeImgDiv.onmouseout = function(e) {
			if (oThis.isActive)
				this.className = oThis.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon_on" : "tab_item_closeicon_on_bottom";
			else
				this.className = oThis.tabType == TabComp.TYPE_TOP ? "tab_item_closeicon" : "tab_item_closeicon_bottom";
		};
	}

	this.tabItemDiv.onmouseover = function(e) {
		e = EventUtil.getEvent();
		// if (oThis.parentTab.activedItem == oThis) {
		// oThis.borderLeft.className = "tab_item_leftborder_on";
		// oThis.borderRight.className = "tab_item_rightborder_on";
		// oThis.borderCenter.className = "tab_item_centerborder_on";
		// }
		this.style.cursor = "pointer";

		// // 创建item关闭按钮
		// if (oThis.showCloseIcon && oThis.closeImgDiv == null) {
		// oThis.closeImgDiv = $ce("div");
		// this.appendChild(oThis.closeImgDiv);
		// oThis.closeImgDiv.className = "tab_item_closeicon";
		//
		// // 关闭该页签逻辑
		// oThis.closeImgDiv.onclick = function(e) {
		// e = EventUtil.getEvent();
		// oThis.parentTab.removeItemTab(oThis.parentTab
		// .getItemIndex(oThis));
		// stopAll(e);
		// // 删除事件对象（用于清除依赖关系）
		// clearEventSimply(e);
		// };
		// }
		// if (oThis.closeImgDiv) {
		// oThis.closeImgDiv.style.left = this.offsetWidth - 13;
		// oThis.closeImgDiv.style.display = "block";
		// }
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// this.tabItemDiv.onmouseout = function(e) {
	// e = EventUtil.getEvent();
	// if (oThis.parentTab.activedItem == oThis) {
	// oThis.borderLeft.className = "tab_item_leftborder_off";
	// oThis.borderRight.className = "tab_item_rightborder_off";
	// oThis.borderCenter.className = "tab_item_centerborder_off";
	// }
	//
	// // 隐藏关闭按钮
	// // if (oThis.closeImgDiv)
	// // oThis.closeImgDiv.style.display = "none";
	// stopEvent(e);
	// // 删除事件对象（用于清除依赖关系）
	// clearEventSimply(e);
	// };

	// 鼠标点击tabitem的事件处理
	this.tabItemDiv.onclick = function(e) {
		e = EventUtil.getEvent();
		e.triggerItem = oThis;
		oThis.click(e);
		// 删除事件对象（用于清除依赖关系）
		// clearEvent(e);
		e.triggerItem = null;
		clearEventSimply(e);
	};

	// 可关闭页签增加右键菜单
	if (this.showCloseIcon) {
		this.tabItemDiv.oncontextmenu = function(e) {
			e = EventUtil.getEvent();
			// 获取右键菜单对象
			if (!oThis.parentTab.contextMenu) {
				// 初始化右键菜单
				oThis.parentTab.initContextMenu();
			}
			oThis.parentTab.contextMenu.triggerObj = oThis;
			oThis.parentTab.contextMenu.show(e);
			// 删除事件对象（用于清除依赖关系）
			clearEvent(e);
		};
	}

	if (this.disabled) {
		// 将与下拉按钮的各种动作事件保存
		this.divButtonClickFunc = this.tabItemDiv.onclick;
		this.divButtonMouseOutFuc = this.tabItemDiv.onmouseout;
		this.divButtonMouseOverFuc = this.tabItemDiv.onmouseover;
		this.tabItemDiv.onclick = function() {
		};
		this.tabItemDiv.onmouseout = function() {
		};
		this.tabItemDiv.onmouseover = function() {
		};
	}

	this.hideContent();

};

/**
 * 隐藏包含内容
 */
TabItem.prototype.hideContent = function() {
	if (IS_IE) {
		this.divContent.style.visibility = "hidden";
		this.divContent.style.top = "10000px";
		this.divContent.style.position = "absolute";
	} else {
		this.divContent.style.display = "none";
	}
};

/**
 * 隐藏包含内容
 */
TabItem.prototype.showContent = function() {
	if (IS_IE) {
		this.divContent.style.position = "relative";
		this.divContent.style.top = "0px";
		this.divContent.style.visibility = "";
	} else {
		this.divContent.style.display = "block";
	}
};

/**
 * 改变item的title
 * 
 * @param title
 */
TabItem.prototype.changeTitle = function(title) {
	this.title = getString(title, this.title);
	this.tabItemDiv.title = this.title;
	// 得到文字的宽度
	var textWidth = getTextWidth(this.title, this.parentTab.className + "_text_width") + 2;

	this.divTitle.style.width = textWidth;
	// 改变关闭按钮的位置
	if (this.divTitle.closeImgDiv)
		this.divTitle.closeImgDiv.style.left = textWidth - 15;

	var newTitle = document.createTextNode(this.title);
	this.divTitle.replaceChild(newTitle, this.divTitle.firstChild);
};

/**
 * 添加分行符
 */
TabItem.prototype.addSeparator = function() {
	this.menuDiv.appendChild($ce("hr"));
};

/**
 * 使指定item项不可见,包括此item的title和content
 */
TabItem.prototype.hide = function() {
	this.tabItemDiv.style.display = "none";
	if (IS_IE)
		this.divContent.style.visibility = "hidden";
	else
		this.divContent.style.display = "none";
	if (this.sep)
		this.sep.style.display = "none";
	// 隐藏激活的控件,则将激活控件设为null
	// if(this.parentTab.activedItem == this)
	// this.parentTab.activedItem = null;
	this.visible = false;
};

/**
 * 显示指定的item项
 */
TabItem.prototype.show = function() {
	this.tabItemDiv.style.display = "block";
	if (IS_IE)
		this.divContent.style.visibility = "";
	else
		this.divContent.style.display = "block";
	if (this.sep)
		this.sep.style.display = "block";
	this.visible = true;
};

/**
 * 使指定的tabItem显示标题名字
 */
TabItem.prototype.showTitle = function() {
	this.tabItemDiv.style.display = "block";
	if (this.sep)
		this.sep.style.display = "block";
	this.visible = true;
};


/**
 * 改变itemDiv的宽度
 * 
 * @private
 */
TabItem.prototype.changeWidth = function(delta) {
	if (delta == 0)
		this.tabItemDiv.style.width = "0px";
	else
		this.tabItemDiv.style.width = Math.max(0,
				parseInt(this.tabItemDiv.offsetWidth) + delta)
				+ "px";
};

/**
 * itemtab设置宽度
 */
TabItem.prototype.setWidth = function(width) {
	// 总tabdiv设置宽度
	this.tabItemDiv.style.width = Math.max(0, width) + "px";
};

/**
 * TabItem是否激活 TabItem.prototype.setActive = function(isActive) { var isActive =
 * getBoolean(isActive, false); // 控件处于激活状态变为非激活状态 if (this.disabled == false &&
 * isActive == false) { // 将与下拉按钮的各种动作事件保存 this.divButtonClickFunc =
 * this.tabItemDiv.onclick; this.divButtonMouseOutFuc =
 * this.tabItemDiv.onmouseout; this.divButtonMouseOverFuc =
 * this.tabItemDiv.onmouseover; this.tabItemDiv.onclick = function() { };
 * this.tabItemDiv.onmouseout = function() { }; this.tabItemDiv.onmouseover =
 * function() { }; // this.borderCenter.className += "
 * tab_item_centerboder_disable"; } // 控件处于禁用状态变为激活状态 else if (this.disabled ==
 * true && isActive == true) { this.tabItemDiv.onclick =
 * this.divButtonClickFunc; this.tabItemDiv.onmouseout =
 * this.divButtonMouseOutFuc; this.tabItemDiv.onmouseover =
 * this.divButtonMouseOverFuc; // this.borderCenter.className =
 * "tab_item_centerborder_off"; } this.isActive = isActive; };
 */

/**
 * 在内容面板上添加需要的其它组件
 */
TabItem.prototype.add = function(obj) {
	this.divContent.appendChild(obj);
};

/**
 * 得到内容面板的显示对象
 */
TabItem.prototype.getObjHtml = function() {
	return this.divContent;
};

/**
 * 会调用到tab的closeItem方法,方便在tab控件统一监听item的关闭事件
 */
TabItem.prototype.close = function() {
	return this.parentTab.closeItem(this);
};

/**
 * 响应具体的弹出菜单item点击事件
 * 
 * @private
 */
TabItem.prototype.onclick = function(e) {
	var mouseEvent = {
		"obj" : this,
		"event" : e
	};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 若用户返回false,则不删除此itemtab.否则删除
 * 
 * @private
 */
TabItem.prototype.onclose = function() {
	var simpleEvent = {
		"obj" : this
	};
	this.doEventFunc("onclose", TabItemListener.listenerType, simpleEvent);
};


/**
 * 获取对象信息
 * 
 * @private
 */
TabComp.prototype.getContext = function() {
	var context = new Object;
	// context.javaClass = "nc.uap.lfw.core.comp.ctx.TabContext";
	context.c = "TabContext";
	var items = this.tabItems;
	var itemIds = [], itemTexts = [];
	var showIcons = [];
	var enables = [];
	var visibles = [];
	for (var i = 0; i < items.length; i++) {
		itemIds.push(items[i].name);
		itemTexts.push(items[i].title);
		showIcons.push(items[i].showCloseIcon);
		enables.push(!items[i].disabled);
		visibles.push(items[i].trueVisible);
	}
	context.tabItemTexts = itemTexts;
	context.tabItemIds = itemIds;
	context.tabItemEnables = enables;
	context.showCloseIcons = showIcons;
	context.tabItemVisibles = visibles;
	context.id = this.id;
	context.currentIndex = this.getSelectedIndex();
	context.oneTabHide = this.oneTabHide;
	return context;
};

/**
 * 设置对象信息
 * 
 * @private
 */
TabComp.prototype.setContext = function(context) {
	// 设置是否一个tab时隐藏
	this.oneTabHide = context.oneTabHide;
	// 显示的tabitem数
	var showItemCount = 0;
	for (var i = 0; i < context.tabItemVisibles.length; i++) {
		// 处理TabItem显示隐藏
		if (this.tabItems[i].trueVisible != context.tabItemVisibles[i]) {
			if (context.tabItemVisibles[i] == true)
				this.showItem(this.tabItems[i].name);
			else
				this.hideItem(this.tabItems[i].name);
		}
		// 统计显示的tabItem数
		if (context.tabItemVisibles[i])
			showItemCount++;
	}
	if (showItemCount == 1 && this.oneTabHide
			&& (this.isHideTabHead == null || this.isHideTabHead == false))
		this.hideTabHead();
	else if (showItemCount > 1 && this.isHideTabHead)
		this.showTabHead();

	if (context.currentIndex != null
			&& context.currentIndex != this.getSelectedIndex())
		this.activeTab(context.currentIndex, this.tabItems[this
				.getSelectedIndex()]);

};
