EditableListener.COMPOMENT_TYPE = "component"; // 控件类型
EditableListener.LAYOUT_TYPE = "layout"; // 布局类型（包括panel）
EditableListener.PANEL_TYPE = "panel"; // 布局类型（包括panel）
EditableListener.PAGEUIMETA_TYPE = "pageuimeta"; // 页面级UIMeta
EditableListener.GRID_LAYOUT_TYPE = "grid_layout"; // 布局类型（包括panel）
EditableListener.GRID_PANEL_TYPE = "grid_panel"; // 布局类型（包括panel）

// 三种样式
EditableListener.EDIT_LAYOUT_PANEL = "editable"; // 布局或者panel的类样式
EditableListener.EDIT_LAYOUT_VPANEL = "editable_vPanel"; // 布局或者panel的类样式
EditableListener.EDIT_LAYOUT_HPANEL = "editable_hPanel"; // 布局或者panel的类样式
EditableListener.EDIT_COMPONENT = "edit_component";// 控件的类样式
EditableListener.EDIT_COMPONENT_SELECTED = "edit_component_selected";// 控件选中后的类样式
EditableListener.EDIT_SELECTED = "edit_selected"; // 选中后的类样式

// 表格布局的两种样式
EditableListener.EDIT_GRIDLAYOUT = "edit_gridLayout";// 表格布局的类样式
EditableListener.EDIT_GRIDLAYOUT_SELECTED = "edit_gridLayout_selected";// 表格布局的类样式

EditableListener.prototype = new ListenerUtil;
 
/**
 * divObj div对象，通过document.getElementById 获得 params:
 * 参数对象：包含widgetId，uiid,eleid,type(type为具体的类型，与render中的getSourceType相匹配)
 * objType: 用来区分是布局Layout还是组件component
 */
function EditableListener(divObj, params, objType) {
	if (arguments.length == 0)
		return;
	ListenerUtil.call(this, true);

	this.divObj = divObj;
	if (!this.divObj) {
		return;
	}
	this.params = params;
	this.initObjAttribute();
	this.objType = getString(objType, EditableListener.LAYOUT_TYPE);
	this.divObj.objType = this.objType;
	this.toEditableObj();
	// 屏蔽UIMeta的右键
	// if (this.params.type == "uimeta")
	// return;
	if(this.objType == EditableListener.COMPOMENT_TYPE){
		 this.toDragableObj();
	}
	this.addContextMenu();

};

EditableListener.prototype.toDragableObj = function() {
	var obj = this.divObj;
	EventUtil.addEventHandler(window, 'mousemove', EditableListener.dragMove);
	EventUtil.addEventHandler(window, 'mouseup', EditableListener.dragEnd);
};
EditableListener.dragCancel = function(id){
	var handId = id+"_hand";
	var handler = document.getElementById(handId);
	if(handler){
		handler.style.display = "none";
	}
};

EditableListener.dragStart = function() {
	var e = EventUtil.getEvent();
	//右键，返回
	if(e.button == 2)
		return;
	this.bDragStart = true;
	if(window.dragObj == null){	
		var sourceObj = this;
		var dragObj = sourceObj.cloneNode(true);
		dragObj.style.position = "absolute";
		dragObj.style.height = sourceObj.offsetHeight + "px";
		dragObj.style.width = sourceObj.offsetWidth + "px";
//		dragObj.style.left = sourceObj.offsetLeft + "px";
//		dragObj.style.top = sourceObj.offsetTop + "px";
		dragObj.style.border = "1px solid red";
		document.body.appendChild(dragObj);
		dragObj.style.display="none";
		window.dragObj = dragObj;
		window.currentDropObj = this.eleid;
		var flagStyle = window.dragObj.style;
		flagStyle.left = (e.clientX + 3) + "px";
		flagStyle.top = (e.clientY + 3) + "px";
//		flagStyle.display = "block";
	}
};

EditableListener.dragMove = function() {
	if(window.dragObj == null)
		return;
		window.dragObj.style.display = "block";
	var event = EventUtil.getEvent();
	var flagStyle = window.dragObj.style;
	// 这里没有处理IE兼容性 喜海到时候处理下 方法是 pageX + scrollLeft - clientLeft
	flagStyle.left = (event.clientX + 10) + "px";
	flagStyle.top = (event.clientY + 10) + "px";
	flagStyle.display = "block";
	flagStyle.zIndex = 647;
	var trueObj = document.getElementById(window.dragObj.id);
	trueObj.style.display = "none";
	
//	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
//	if ((e.button == 1 || e.button == 0) && window.dragStart) {
////	if (e.button == 1) {
//		var tree = oThis.refTree;
//		if (IS_IE)  // 仅IE中有此方法 
//			window.oDrag.setCapture();
//		oThis.refTree.bDragStart = true;
//		tree.dragDiv.style.display = "block";
//		//TODO
////		tree.dragDiv.style.left = e.x ? e.x : e.pageX - tree.iDiffX + "px";
////		tree.dragDiv.style.top = e.y ? e.y : e.pageY - tree.iDiffY + "px";
//		if (IS_IE) {
//			tree.dragDiv.style.left = e.x + 10 + "px";
//			tree.dragDiv.style.top = e.y + "px";
//		} else {
//			tree.dragDiv.style.left = e.clientX - 185 + "px";
//			tree.dragDiv.style.top = e.clientY - 25 + "px";
//			if (tree.Div_gen.scrollWidth > tree.Div_gen.clientWidth) {  // 出现横向滚动条
//				tree.dragDiv.style.left = e.clientX - 185 + (tree.Div_gen.scrollWidth - tree.Div_gen.clientWidth) + "px";
//			}
//			if (tree.Div_gen.scrollHeight > tree.Div_gen.clientHeight) {  // 出现纵向滚动条
//				tree.dragDiv.style.top = e.clientY - 25 + (tree.Div_gen.scrollHeight - tree.Div_gen.clientHeight) + "px";
//			}
//		}
//	}
};

EditableListener.dragEnd = function(e){
	this.dragStart = false;
	if(window.dragObj){
		var trueObj = document.getElementById(window.dragObj.id);
		trueObj.style.display = "";
		var panel = EditableListener.getSelectPanel(e);
		if(!isChildOfObj(panel, trueObj)){
			var obj = EditableListener.toDragSelected(e);
			 
			obj.currentDropObjType = trueObj.type; // 保存变量
			if(obj.currentDropObjType == "form_element" && trueObj.subeleid){
				obj.currentDropObj = trueObj.subeleid;
			}else{
				obj.currentDropObj = trueObj.eleid; // 保存变量
			}
			obj.currentDropObjType2 = window.currentDropObjType2;
			obj.currentDropDsId = trueObj.widgetid;
			EditableListener.toServer(trueObj, EditableListener.CONTEXTMENUDELETE);
			callParent(obj, "add");
		}
		//选中拖放对象
		callParent(trueObj, "init");
		// 释放页面图片
		var event = {
			type : "release"
		};
		window.dropEventHandler(event);
		window.currentDropObj = null;

		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	
		
		
		//EditableListener.toServer(obj, EditableListener.CONTEXTMENUADD);
	}
	try{
		document.body.removeChild(window.dragObj);
	}catch(e){
			
	}
	window.dragObj = null;
};

EditableListener.prototype.initObjAttribute = function() {
	if (this.divObj && this.params) {
		for(var i in this.params)
			this.divObj[i] = this.params[i];
	}
};

EditableListener.prototype.toEditableObj = function() {
	if (this.divObj) {
		var obj = this.divObj;
		EditableListener.toEditAbleClass(obj);
		EditableListener.removeOnclick(obj)
		EventUtil.addEventHandler(obj, 'click', EditableListener.toEdit);

	}
};

var editingObj;// 正在被编辑的对象，一次只能编辑一个对象

var editingObjP = null;;

/**
 * 
 * @param {}
 *            e
 * @param {}
 *            noParent true表示不进行服务器请求，false表示进行服务器请求
 */

EditableListener.toEdit = function(e, noParent) {
	e = EventUtil.getEvent();
	if (window.currentDropObj != null)
		return;
	var obj = e.target;
	if(obj == EditableListener.lastObj){
		return obj;
	}
	
	while (obj != null) {
		if (obj.objType) {
			if (obj.objType == "tabItem") {
				break;
			} else if(obj.objType == "outLookItem"){
				triggerEditorEvent("showPropertiesView",obj);
				break;
			}else {
				EditableListener.toEditSelected(obj, noParent);
				stopEvent(e);
				break;
			}

		} else {
			if (obj.parentNode) {
				obj = obj.parentNode;
			} else {
				break;
			}
		}

	}
	EditableListener.hideAllContextMenu(e);
	EditableListener.lastObj = obj;
	return obj;
};

EditableListener.selectParent = function(obj) {
	if (editingObjP != null) {
		EditableListener.toEditAbleClass(editingObjP);
		editingObjP = null;
	}

	if (obj.objType == 'panel') {
		var parent = obj.parentNode;
		while (parent) {
			if (parent.objType) {
				parent.className = 'editable_parent';
				editingObjP = parent;
				break;
			} else {
				parent = parent.parentNode;
			}
		}

	}
}

function isChildOfObj(parent, child){
	if(parent.children.length > 0){
		for(var i = 0; i < parent.children.length; i++){
			if(parent.children[i] == child)
				return true;
			if(isChildOfObj(parent.children[i], child))
				return true;
		}
	}
	return false;
}


EditableListener.getSelectPanel = function(e) {
	e = EventUtil.getEvent();
	var obj = e.target;
	while (true) {
		if (obj.objType && obj.isContainer) {
			 
			//stopEvent(e);
			break;
		} else {
			if (obj.parentNode) {
				obj = obj.parentNode;
			} else {
				break;
			}
		}

	}
	 
	return obj;
};

EditableListener.toDragSelected = function(e) {
	e = EventUtil.getEvent();
	var obj = e.target;
	while (true) {
		if (obj.objType && obj.isContainer) {
			EditableListener.toEditSelected(obj, true);
			stopEvent(e);
			break;
		} else {
			if (obj.parentNode) {
				obj = obj.parentNode;
			} else {
				break;
			}
		}

	}
	EditableListener.hideAllContextMenu(e);
	return obj;
};
// 改变代码 针对布局和panel
/**
 * 
 * @param {}
 *            obj
 * @param {}
 *            noParent true表示只选中，不向服务器发送
 */
EditableListener.toEditSelected = function(obj, noParent) {
	if (obj) {

		if (editingObj) { // 之前编辑的与现在选中的不同
			EditableListener.toEditAbleClass(editingObj);
		}
		EditableListener.selectParent(obj);
		if (obj.objType == EditableListener.GRID_LAYOUT_TYPE) {
			removeClassName(obj, EditableListener.EDIT_GRIDLAYOUT)
			addClassName(obj, EditableListener.EDIT_GRIDLAYOUT_SELECTED);
		} else if (obj.objType == EditableListener.GRID_PANEL_TYPE) { // 表格布局的特殊处理table实现，和div的样式有区别
			removeClassName(obj, 'edit_gridLayout_cell')
			addClassName(obj, EditableListener.EDIT_GRIDLAYOUT_SELECTED);
		} else if (obj.objType == EditableListener.COMPOMENT_TYPE) { // 控件布局的特殊处理table实现，和div的样式有区别
			obj.className = EditableListener.EDIT_COMPONENT_SELECTED;
			if(obj.type != "grid_header" && !(obj.type == "form_element" && obj.uiid == "")){
				var handId = obj.id+"_hand";
				var handler = document.getElementById(handId);
				if(handler){
					handler.style.display = "block";
				}else{
					var handDiv = $ce("DIV");
					handDiv.id = handId;
					var handImg = $ce("IMG");
					handImg.src= window.themePath + "/ui/basic/images/unlock.gif";
					handImg.style.cursor = "move";
					handDiv.appendChild(handImg);
					handImg.id = handId+"_img"; 
					handDiv.style.position = "absolute";
					handDiv.style.left = "0px";
					handDiv.style.top = "0px";
					handDiv.style.cursor = "move";
					handDiv.style.zIndex = 64700;
					EventUtil.addEventHandler(obj, 'mousedown', EditableListener.dragStart);
					obj.appendChild(handDiv);
				}
			}
		} else {
			obj.className = EditableListener.EDIT_SELECTED; // 对于div的统一处理
		}

		editingObj = obj;
		// 调用其父窗口的方法
		if (noParent == true)
			return;
		//EditableListener.toServerProxy(obj, "init");
		callParent(obj, "init");
	}
};

function addClassName(obj, className) {
	if (obj.className) {
		if (obj.className.indexOf(className) >= 0) {
			return;
		} else {
			obj.className = obj.className + ' ' + className;
		}

	} else {
		obj.className = className;
	}

}

function removeClassName(obj, className) {
	if (obj.className) {
		var c = obj.className;
		var array = c.split(' ');
		var a = new Array();
		for (var i = 0; i < array.length; i++) {
			if (array[i] != className) {
				a.push(array[i]);
			}
		}
		obj.className = a.join(' ');
	}
	return;

}
// 将对象置为 编辑态 对布局和layout
/**
 * 将对象设置为编辑状态，即显示黑线
 * 
 * @param {}
 *            obj
 */
EditableListener.toEditAbleClass = function(obj) {
	if (obj && obj.objType) {
		if (obj.objType == EditableListener.COMPOMENT_TYPE) { // 控件类型
			obj.className = EditableListener.EDIT_COMPONENT;
			EditableListener.dragCancel(obj.id);
		} else if (obj.objType == EditableListener.GRID_LAYOUT_TYPE) { // 表格布局
			removeClassName(obj, EditableListener.EDIT_GRIDLAYOUT_SELECTED);
			addClassName(obj, EditableListener.EDIT_GRIDLAYOUT);
		} else if (obj.objType == EditableListener.GRID_PANEL_TYPE) { // 表格的cell
			removeClassName(obj, EditableListener.EDIT_GRIDLAYOUT_SELECTED);
			addClassName(obj, 'edit_gridLayout_cell');
		} else if (obj.objType == EditableListener.PANEL_TYPE) { // panel
			if (obj.type == 'flowhpanel') {
				obj.className = EditableListener.EDIT_LAYOUT_HPANEL;
			} else if (obj.type == 'flowvpanel') {
				obj.className = EditableListener.EDIT_LAYOUT_VPANEL;
			} else {
				obj.className = EditableListener.EDIT_LAYOUT_PANEL;
			}
		} else { // layout
			obj.className = EditableListener.EDIT_LAYOUT_PANEL;
		}
	}

};

// 删除该节点和子节点的onclick事件,当遇到是可编辑的div时，则停止，当没有子节点时停止
EditableListener.removeOnclick = function(obj) {
	// return;
	if (obj) {
		try {
			if (!obj.objType) {
				if (obj.onclick) {
					obj.onclick = '';
				}
				if (obj.click) {
					obj.click = '';
				}
			}

			if (obj.childNodes) {

				for (var i = 0; i < obj.childNodes.length; i++) {
					var child = obj.childNodes[i];
					if (child.objType) {
						return
					}
					EditableListener.removeOnclick(child)
				}
			}
		} catch (error) {
			Logger.error(error.name + ":" + error.message);
		}
	}

};
// 添加右键菜单
EditableListener.prototype.addContextMenu = function() {
	if (this.divObj) {
		var obj = this.divObj;
		EventUtil.addEventHandler(obj, 'contextmenu',
				EditableListener.contextMenuRender);
	}
};
// ------------------------------------------------------------------------------------------------
// 表格布局的监听

// ------------------------------------------------------------------------------------------------
// 定义两个右键菜单，一个为布局使用，一个为控件使用
EditableListener.allContextMenus = new Array();
EditableListener.compContextMenu = null; // 控件右键菜单
EditableListener.layoutContextMenu = null; // 布局右键菜单
EditableListener.panelContextMenu == null; // panel右键菜单
/**
 * 操作类型常量，对应右键菜单的项
 * 
 * @type {String}
 */
EditableListener.CONTEXTMENUADD = "add";
EditableListener.CONTEXTMENUDELETE = "delete";
EditableListener.CONTEXTMENUSETTING = "setting";
EditableListener.UPDATEID = "updateid";

EditableListener.CONTEXTMENUNEXTPAGE = "nextPage";
EditableListener.CONTEXTMENULASTPAGE = "lastPage";
/**
 * 右键菜单某一项被点击时，触发此事件
 * 
 * @param {}
 *            contextMenu
 * @param {}
 *            name
 * @param {}
 *            caption
 * @param {}
 *            operationType
 * @return {}
 */
EditableListener.contextMenuOnClick = function(contextMenu, name, caption,
		operationType) {
	var menuItem = contextMenu.addMenu(name, caption);
	var menuItemListener = new MouseListener();
	menuItemListener.onclick = function() {
		if (contextMenu.triggerObj) {
			EditableListener.toServer(contextMenu.triggerObj,
					operationType)

		} else {
			alert("错误的方法调用，为指定调用对象！");
		}
	}
	menuItem.addListener(menuItemListener);
	return menuItem;
};

/**
 * 隐藏所有的右键菜单，左键单击时隐藏
 */
EditableListener.hideAllContextMenu = function(e) {
	for (var i = 0; i < EditableListener.allContextMenus.length; i++) {
		var contextMenu = EditableListener.allContextMenus[i];
		if (contextMenu && contextMenu.hide) {
			contextMenu.hide();
		}
	}
};

/**
 * 为不同的类型添加不同的右键事件
 * 
 * @param {}
 *            e
 */
EditableListener.contextMenuRender = function(e) {
	e = EventUtil.getEvent();
	if (window.currentDropObj != null)
		return;
	var obj = EditableListener.toEdit(e, false);// 先选中
	if (obj.objType == EditableListener.COMPOMENT_TYPE) {
		EditableListener.compContextMenuRender(e, obj);
	} else if (obj.objType == EditableListener.PANEL_TYPE
			|| obj.objType == EditableListener.GRID_PANEL_TYPE) {
		if (obj.type == 'uimeta') {
			EditableListener.uimetaContextMenuRender(e, obj);
		} else {
			EditableListener.panelContextMenuRender(e, obj);
		}

	} else {
		if (obj.type == "cardlayout") {
			EditableListener.cardContextMenuRender(e, obj);
		} else {
			EditableListener.layoutContextMenuRender(e, obj);
		}
	}

	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 布局右键菜单触发事件
 */

EditableListener.layoutContextMenuRender = function(e, obj) {
	// 布局的右键菜单
	if (EditableListener.layoutContextMenu == null) {
		EditableListener.layoutContextMenu = new ContextMenuComp(
				"layoutContextMenu", 0, 0, false);
		EditableListener.layoutContextMenu.deleteMenu = EditableListener
				.contextMenuOnClick(EditableListener.layoutContextMenu,
						"delete", "删除", EditableListener.CONTEXTMENUDELETE);
		// EditableListener.layoutContextMenu.settingMenu = EditableListener
		// .contextMenuOnClick(EditableListener.layoutContextMenu,
		// "setting", "设置", EditableListener.CONTEXTMENUSETTING);
		EditableListener.layoutContextMenu.addMenu = EditableListener
				.contextMenuOnClick(EditableListener.layoutContextMenu, "add",
						"增加", EditableListener.CONTEXTMENUADD);
		EditableListener.allContextMenus
				.push(EditableListener.layoutContextMenu);
	}
	EditableListener.layoutContextMenu.triggerObj = obj;
	EditableListener.layoutContextMenu.show(e);

};

/**
 * 控件右键菜单触发事件
 */

EditableListener.compContextMenuRender = function(e, obj) {
	// 控件的右键菜单
	if (EditableListener.compContextMenu == null) {
		EditableListener.compContextMenu = new ContextMenuComp(
				"compContextMenu", 0, 0, false);
		EditableListener.compContextMenu.deleteMenu = EditableListener
				.contextMenuOnClick(EditableListener.compContextMenu, "delete",
						"删除", EditableListener.CONTEXTMENUDELETE);
		EditableListener.allContextMenus.push(EditableListener.compContextMenu);
	}
	EditableListener.compContextMenu.triggerObj = obj;
	EditableListener.compContextMenu.show(e);
};

EditableListener.panelContextMenuRender = function(e, obj) {
	// 控件的右键菜单
	if (EditableListener.panelContextMenu == null) {
		EditableListener.panelContextMenu = new ContextMenuComp(
				"panelContextMenu", 0, 0, false);
		EditableListener.panelContextMenu.deleteMenu = EditableListener
				.contextMenuOnClick(EditableListener.panelContextMenu,
						"delete", "删除", EditableListener.CONTEXTMENUDELETE);
		
		EditableListener.panelContextMenu.addMenu = EditableListener
				.contextMenuOnClick(EditableListener.panelContextMenu, "add",
						"插入", EditableListener.CONTEXTMENUADD);	
		
		EditableListener.allContextMenus
				.push(EditableListener.panelContextMenu);
	}
	obj.currentDropObjType2 = "isPanel";
	EditableListener.panelContextMenu.triggerObj = obj;
	EditableListener.panelContextMenu.show(e);
};

EditableListener.uimetaContextMenuRender = function(e, obj) {
	// 控件的右键菜单
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 卡片布局的右键，需要添加上一页 下一页
 * 
 * @type {}
 */
EditableListener.cardContextMenu = null;
EditableListener.cardContextMenuRender = function(e, obj) {
	// 控件的右键菜单
	if (EditableListener.cardContextMenu == null) {
		EditableListener.cardContextMenu = new ContextMenuComp(
				"cardContextMenu", 0, 0, false);
		// EditableListener.cardContextMenu.lastPageMenu = EditableListener
		// .contextMenuOnClick(EditableListener.cardContextMenu,
		// "firstPage", "首页", "firstPage");
		// EditableListener.cardContextMenu.lastPageMenu = EditableListener
		// .contextMenuOnClick(EditableListener.cardContextMenu,
		// "prePage", "上一页", "prePage");
		// EditableListener.cardContextMenu.nextPageMenu = EditableListener
		// .contextMenuOnClick(EditableListener.cardContextMenu,
		// "nextPage","下一页", "nextPage");
		// EditableListener.cardContextMenu.nextPageMenu = EditableListener
		// .contextMenuOnClick(EditableListener.cardContextMenu,
		// "lastPage","末页", "lastPage");
		EditableListener.cardContextMenu.deleteMenu = EditableListener
				.contextMenuOnClick(EditableListener.cardContextMenu, "delete",
						"删除", EditableListener.CONTEXTMENUDELETE);
		EditableListener.cardContextMenu.settingMenu = EditableListener
				.contextMenuOnClick(EditableListener.cardContextMenu,
						"setting", "设置", EditableListener.CONTEXTMENUSETTING);
		EditableListener.cardContextMenu.addMenu = EditableListener
				.contextMenuOnClick(EditableListener.cardContextMenu, "add",
						"增加", EditableListener.CONTEXTMENUADD);
		EditableListener.allContextMenus.push(EditableListener.cardContextMenu);
	}
	EditableListener.cardContextMenu.triggerObj = obj;
	EditableListener.cardContextMenu.show(e);
};

/**
 * ------------------------------------------------------------------
 * 接收拖拽事件，消息通知，参数为类型，整个页面的布局和容器接收onmourse事件，前提，没有子元素的容器才可以，
 * 
 * 
 * ------------------------------------------------------------------
 */

EditableListener.toDragSelected = function(e) {
	e = EventUtil.getEvent()
	var obj = e.target;
	while (true) {
		if (obj.objType && obj.isContainer) {
			EditableListener.toEditSelected(obj, true);
			stopEvent(e);
			break;
		} else {
			if (obj.parentNode) {
				obj = obj.parentNode;
			} else {
				break;
			}
		}

	}
	EditableListener.hideAllContextMenu(e);
	return obj;
};

function DragListener(obj) {
	obj.isContainer = true;
	this.obj = obj;
	this.create();
}

DragListener.prototype.create = function() {
	var obj = this.obj;
	EventUtil.addEventHandler(obj, 'click', DragListener.onclick);
	EventUtil.addEventHandler(obj, 'mouseover', DragListener.onmouseover);
	EventUtil.addEventHandler(obj, 'contextmenu', DragListener.oncontextmenu);
}

DragListener.onclick = function(e) {
	if(window.dragObj != null)
		return;
	if (window.currentDropObj != null) {
		var obj = EditableListener.toDragSelected(e);
		obj.currentDropObj = window.currentDropObj; // 保存变量
		obj.currentDropObjType = window.currentDropObjType; // 保存变量
		obj.currentDropObjType2 = window.currentDropObjType2;
		obj.currentDropDsId = window.currentDropDsId;

		// 释放页面图片
		var event = {
			type : "release"
		};
		window.dropEventHandler(event);
		EditableListener.toServer(obj, "add");
		//EditableListener.toServerProxy(obj, "add");
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	}
}

DragListener.onmouseover = function(e) {
	if (window.currentDropObj != null) {
		EditableListener.toDragSelected(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	}
}

DragListener.oncontextmenu = function(e) {
	if (window.currentDropObj != null) {
		e = EventUtil.getEvent()
		var event = {
			type : "release"
		};
		window.dropEventHandler(event);
		// stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		// clearEventSimply(e);
		return false;
	}

}

// --------------------------------------------------------
// 页面操作脚本

window.execDynamicScript2RemoveLayout = function(id, type, params) {
	if (!id)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent;
	if (obj.objType) {
		parent = $ge(obj.id + "_raw");
		if (parent) {
			obj = parent;
			parent = obj.parentNode;
		}
	} else {
		parent = obj.parentNode;
	}
	if (parent) {
		parent.removeChild(obj);
		return true
	}
	return false;
};

window.execDynamicScript2RemovePanel = function(id, type, params) {
	if (!id)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent;
	if (obj.objType) {
		parent = $ge(obj.id + "_raw");
		if (parent) {
			obj = parent;
			parent = obj.parentNode;
		}
	} else {
		parent = obj.parentNode;
	}
	if (parent) {
		parent.removeChild(obj);
		window.layoutInitFunc();
		return true
	}
	return false;
};

window.execDynamicScript2RemoveComponent = function(id, widgetId, compId,
		params) {
	if (!id || !widgetId || !compId)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent = obj.parentNode;
	if (parent) {
		pageUI.getWidget(widgetId).removeComponent(compId);
		parent.removeChild(obj);
		return true
	}
	return false;
};

/**
 * 删除formElement
 * 
 * @param {}
 *            id
 * @param {}
 *            widgetId
 * @param {}
 *            formId
 * @param {}
 *            feId
 * @return {Boolean}
 */
window.execDynamicScript2RemoveFormElement2 = function(id, widgetId, formId,
		feId) {
	if (!id || !widgetId || !formId)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent = obj.parentNode;
	if (parent) {
		pageUI.getWidget(widgetId).getComponent(formId).removeElementById(feId);
		parent.removeChild(obj);
		return true
	}
	return false;
};

window.execDynamicScript2RemoveGridColumn = function(widgetId, gridId, keyName,
		params) {
	var comp = pageUI.getWidget(widgetId).getComponent(gridId);
	var header = comp.removeHeader(keyName);
	comp.paintData();
};

window.execDynamicScript2RemoveFormElement = function(widgetId, formId,
		keyName, params) {
	try {
		var comp = pageUI.getWidget(widgetId).getComponent(formId);
		comp.removeElementById(keyName);
		comp.pLayout.paint(true);
	} catch (e) {
		alert(e);
	}

};

/**
 * 控件拖放事件处理
 * 
 * @param event
 */
window.dropEventHandler = function(event) {
	if (event.type == "start") {
		window.currentDropObj = event.componentId;
		window.currentDropObjType = event.compType;
		window.currentDropObjType2 = event.compType2;
		window.currentDropDsId = event.text;
		var dropFlagPanel = this.getDropFlagPanel();
		dropFlagPanel.innerHTML = event.text;
	}
	if (event.type == "release") {
		window.getDropFlagPanel().style.display = "none";
		window.currentDropObj = null;
	}
};
/**
 * 拖放时鼠标事件，使拖放辅助信息板跟随鼠标
 * 
 * @param {}
 *            event
 */
function dropPanelMouseEventHandler(event) {
	if (window.currentDropObj == null)
		return;
		
	if(window.dragObj != null)
		return;
	var dropFlagPanel = window.getDropFlagPanel();
	var flagStyle = dropFlagPanel.style;
	// 这里没有处理IE兼容性 喜海到时候处理下 方法是 pageX + scrollLeft - clientLeft
	flagStyle.left = (event.clientX + 10) + "px";
	flagStyle.top = (event.clientY + 10) + "px";
	flagStyle.display = "block";
	flagStyle.zIndex = 2147483647;
}

/**
 * 获得拖放的面板，不存在则创建
 */
window.getDropFlagPanel = function() {
	var dropFlagPanel = $ge("dropFlagPanel");
	if (typeof(dropFlagPanel) == "undefined" || dropFlagPanel == null) {
		var dropFlagPanel = $ce("div");
		dropFlagPanel.id = "dropFlagPanel";
		var flagStyle = dropFlagPanel.style;
		flagStyle.position = "absolute";
		flagStyle.width = "250px";
		flagStyle.height = "60px";
		flagStyle.borderColor = "#0992C1";
		flagStyle.borderStyle = "solid";
		flagStyle.borderWidth = "1px";
		flagStyle.display = "none";
		document.body.appendChild(dropFlagPanel);
		EventUtil.addEventHandler(document, "mousemove",
				dropPanelMouseEventHandler);
	}
	return dropFlagPanel;
}

/**
 * 更新属性值，触发后台事件
 */
window.updateProperty = function(obj) {
	if(obj.updateid){ 
		//删除原始对象
		var dvId = "$d_" + obj.viewId + "_" + obj.oldValue;
		var trueObj = document.getElementById(dvId);
		EditableListener.toServer(trueObj, EditableListener.CONTEXTMENUDELETE); 
		//通知服务器修改对象
		EditableListener.toServer(obj, EditableListener.UPDATEID); 
		//选中对象
		//EditableListener.toServerProxy(trueObj, "init"); 
		
	}
	else{
		EditableListener.toServer(obj, "update");
	}
}

/**
 * 
 * 先调用wupeng1的listener进行数据持久化 然后调用renxh的listener,进行页面的响应
 * 
 * @param {}
 *            obj
 * @param {}
 *            oper
 */

EditableListener.toServerProxy = function(obj, oper) {
	if (oper == EditableListener.CONTEXTMENUDELETE) {
		if (!confirm("确定要删除吗?")) {
			return;
		}
	}

	if (oper == EditableListener.CONTEXTMENUADD) {
		window.isRenderDone = false;
		var div = $ge(obj.id);
		var children = div.childNodes;
		EditableListener.toServer(obj, oper);
		callParent(obj, oper);
	}
}

function callParent(obj, oper) {
	var parent = window.parent;
	if (parent && parent.callBack) {
		parent.callBack(obj, oper);
	}
}

/**
 * 
 * 进行uimeta操作响应的listener，同时会更新页面的脚本
 */
EditableListener.toServer = function(obj, oper) {
	if(oper != "delete" && oper != "add" && oper != "update" && oper != "updateid")
		return;
	var proxy = new ServerProxy(null, null, false);
	proxy.addParam("listener_class",
			"nc.uap.lfw.ra.listener.RaDynamicScriptListener");
	
	proxy.addParam("oper", oper);
	
	proxy.addParam("uiid", obj.uiid);
	proxy.addParam("eleid", obj.eleid);
	proxy.addParam("widgetid", obj.widgetid);
	proxy.addParam("type", obj.type);
	proxy.addParam("objtype", obj.objtype);
	proxy.addParam("rendertype", obj.rendertype);
	proxy.addParam("subeleid", obj.subeleid);
	proxy.addParam("subuiid", obj.subuiid);
	
	proxy.addParam("divid", obj.id);
	proxy.addParam("rowindex", obj.rowindex);
	proxy.addParam("colindex", obj.colindex);
	
	proxy.addParam("currentDropObj", obj.currentDropObj);
	proxy.addParam("currentDropObjType", obj.currentDropObjType);
	proxy.addParam("currentDropObjType2", obj.currentDropObjType2);
	proxy.addParam("currentDropDsId", obj.currentDropDsId);

	// 更新属性传递的参数
	proxy.addParam("compid", obj.compid); // 前台组件ID
	proxy.addParam("prtid", obj.prtid); //父节点ID
	proxy.addParam("viewid", obj.viewid);
	proxy.addParam("comptype", obj.type); // 前台组件类型
	proxy.addParam("attr", obj.attr); // 前台属性名
	proxy.addParam("attrtype", obj.attrtype); // 属性类型
	proxy.addParam("oldvalue", obj.oldvalue); // 修改前的值
	proxy.addParam("newvalue", obj.newvalue); // 修改后的值
	
	// 设置提交父数据集的提交规则
	var rule = new SubmitRule();
	var parent_rule = new SubmitRule();
	var wdr_parentWidget = new WidgetRule('data');
	var dsr_parentTree = new TreeRule('structtree',
			'tree_current_parent_root_tree');
	var dsr_parentDataset = new DatasetRule('ds_struct', 'ds_all_line');
	wdr_parentWidget.addTreeRule('structtree', dsr_parentTree);
	wdr_parentWidget.addDsRule('ds_struct', dsr_parentDataset);
	parent_rule.addWidgetRule('data', wdr_parentWidget);
	rule.setParentSubmitRule(parent_rule);
	proxy.setSubmitRule(rule);

	proxy.execute();
}

window.toClickSelected = function(id) {
	var obj = document.getElementById(id);
	if (document.all) {
		obj.click();
	} else {
		try{
			var evt = document.createEvent("MouseEvents");
			evt.initEvent("click", true, true);
			document.getElementById(id).dispatchEvent(evt);
		}catch(error){
			Logger.error(error.name + ":" + error.message);
		}
	}
}

window.toDeleteSelected = function(id) {
	var obj = document.getElementById(id);
	EditableListener.toServer(obj, "delete");
}

window.toAddSelected = function(obj) {
	var trueObj = document.getElementById(obj.id);
	trueObj.currentDropObjType2 = obj.currentDropObjType2;
	EditableListener.toServerProxy(trueObj, "add");
}
/**
 * 修改控件ID通知
 */
function triggerChangerId(oldId,newId){
	var obj = {};
	obj.oldId = oldId;
	obj.newId = newId;
	triggerEditorEvent("triggerChangerId",obj);
}

/**
 * 创建一个Eclipse交互事件
 * @param {} type
 * @param {} source
 * @return {}
 */
function triggerEditorEvent(type,source){
	var eventContext = "event:";
	eventContext += type;
	for(i in source){
		eventContext += ",,,";
		eventContext += i;
		eventContext += ":";
		eventContext += source[i];
	}
	window.status = eventContext;
	return true;
}
