/**
 *	@fileoverview
 *	组件事件监听类，该类将被具体组件继承
 *	@author guoweic
 *	@version 
 */

/***************************监听器工具*****************************/

/**
 * 监听工具类构造函数
 * @class 监听工具类
 */
function ListenerUtil() {
	if (arguments.length == 0)
		return;
	this.eventMap = new HashMap;
};

/**
 * 为组件增加事件处理方法
 * @param oListener：监听器对象
 */
ListenerUtil.prototype.addListener = function(oListener) {
	var eventMap = this.eventMap;
	if (eventMap.containsKey(oListener.type)) {
		//var arrEvent = eval(eventMap.get(oListener.type));
		var arrEvent = eventMap.get(oListener.type);
		arrEvent.push(oListener);
		eventMap.put(oListener.type, arrEvent);
	} else {
		eventMap.put(oListener.type, [oListener]);
	}
};

/**
 * 为组件移除事件处理方法
 * @param listenerType: 监听器类型
 * @param oListener：监听器对象
 */
ListenerUtil.prototype.removeListener = function(listenerType, oListener) {
	// this 为该类的子类对象
	var eventMap = this.eventMap;
	if (eventMap.containsKey(listenerType)) {
		var arrEvent = eval(eventMap.get(listenerType));
		arrEvent.removeEle(oListener);
		eventMap.put(listenerType, arrEvent);
	}
};

/**
 * 为组件增加事件处理方法
 * @param arrListeners：监听器对象数组
 */
ListenerUtil.prototype.addListeners = function(arrListeners) {
	for (var i = 0, n = arrListeners.length; i < n; i++) {
		this.addListener(arrListeners[i]);
	}
};

/**
 * 为组件移除事件处理方法
 * @param listenerType: 监听器类型
 * @param arrListeners：监听器对象数组
 */
ListenerUtil.prototype.removeListeners = function(listenerType, arrListeners) {
	for (var i = 0, n = arrListeners.length; i < n; i++) {
		this.removeListener(listenerType, arrListeners[i]);
	}
};

/**
 * 为组件移除某事件全部处理方法
 * @param listenerType: 监听器类型
 */
ListenerUtil.prototype.removeAllListeners = function(listenerType) {
	// this 为该类的子类对象
	var eventMap = this.eventMap;
	if (eventMap.get(listenerType)) {
		eventMap.put(listenerType, []);
	}
};

/**
 * 执行事件方法
 * @param eventName：事件名称
 * @param listenerType: 监听器类型
 * @param eventObj: 事件参数对象
 */
ListenerUtil.prototype.doEventFunc = function(eventName, listenerType, eventObj) {
	var map = this.eventMap;
	try {
		ServerProxy.suspend = true;
		if (map.containsKey(listenerType)) {
			var arrListener = map.get(listenerType);
			for (var i = 0; i < arrListener.length; i++) {
				var func = eval("arrListener[i]." + eventName);
				if (func instanceof Function) {
					if (this.beforeCallEvent(eventName, eventObj) == true) {
						var result = func.call(this, eventObj);
						if (result != null)
							return result;
					}
				}
			}
		}
	}
	finally {
		ServerProxy.cleanProxy();
		if (eventObj)
			eventObj.obj = null;
	}
};

/**
 * 执行真正的事件方法之前要执行的内容，只有返回true才继续向下执行（供各组件重写）
 */
ListenerUtil.prototype.beforeCallEvent = function(eventName, eventObj) {
	return true;
};

/***************************监听器*****************************/

/**
 * 鼠标事件监听器
 * @param bIgnoreError: 是否忽略错误，继续执行后面方法
 * @private
 */
var MouseListener = function(bIgnoreError) {
	this.type = MouseListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
MouseListener.listenerType = "MOUSE_LISTENER";
MouseListener.prototype.onclick = function(mouseEvent) {};
MouseListener.prototype.ondbclick = function(mouseEvent) {};
MouseListener.prototype.onmouseover = function(mouseEvent) {};
MouseListener.prototype.onmouseout = function(mouseEvent) {};

/**
 * 键盘事件监听器
 * @param bIgnoreError: 是否忽略错误，继续执行后面方法
 * @private
 */
var KeyListener = function(bIgnoreError) {
	this.type = KeyListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
KeyListener.listenerType = "KEY_LISTENER";
KeyListener.prototype.onKeyDown = function(keyEvent) {};
KeyListener.prototype.onEnter = function(keyEvent) {};
KeyListener.prototype.onKeyUp = function(keyEvent) {};

/**
 * tab子项事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var TabItemListener = function(bIgnoreError) {
	this.type = TabItemListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
TabItemListener.listenerType = "TAB_ITEM_LISTENER";
TabItemListener.prototype.onclose = function(simpleEvent) {};

/**
 * 对话框事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var DialogListener = function(bIgnoreError) {
	this.type = DialogListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
DialogListener.listenerType = "DIALOG_LISTENER";
DialogListener.prototype.onOk = function(simpleEvent) {};
DialogListener.prototype.onCancel = function(simpleEvent) {};
DialogListener.prototype.beforeShow = function(simpleEvent) {};
DialogListener.prototype.beforeClose = function(simpleEvent) {};
DialogListener.prototype.afterClose = function(simpleEvent) {};
DialogListener.prototype.onclose = function(simpleEvent) {};

/**
 * 右键菜单事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ContextMenuListener = function(bIgnoreError) {
	this.type = ContextMenuListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ContextMenuListener.listenerType = "CONTEXT_MENU_LISTENER";
ContextMenuListener.prototype.beforeShow = function(simpleEvent) {};
ContextMenuListener.prototype.onshow = function(simpleEvent) {};
ContextMenuListener.prototype.beforeClose = function(simpleEvent) {};
ContextMenuListener.prototype.onclose = function(simpleEvent) {};

ContextMenuListener.prototype.onmouseout = function(menuItemEvent) {};

/**
 * 参照事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ReferenceTextListener = function(bIgnoreError) {
	this.type = ReferenceTextListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ReferenceTextListener.listenerType = "REF_TEXT_LISTENER";
ReferenceTextListener.prototype.beforeOpenReference = function(simpleEvent) {};

/**
 * 输入框事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var TextListener = function(bIgnoreError) {
	this.type = TextListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
TextListener.listenerType = "TEXT_LISTENER";
TextListener.prototype.onselect = function(simpleEvent) {};
TextListener.prototype.valueChanged = function(valueChangeEvent) {};


/**
 * 卡片布局事件监听器
 * @param bIgnoreError: 是否忽略错误，继续执行后面方法
 * @private
 */
var CardListener = function(bIgnoreError) {
	this.type = CardListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
CardListener.listenerType = "CARD_LISTENER";
CardListener.prototype.beforePageInit = function(cardEvent) {};
CardListener.prototype.afterPageInit = function(cardEvent) {};
CardListener.prototype.beforePageChange = function(cardEvent) {};

/**
 * Tab布局事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var TabListener = function(bIgnoreError) {
	this.type = TabListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
TabListener.listenerType = "TAB_LISTENER";
TabListener.prototype.beforeItemInit = function(tabItemEvent) {};
TabListener.prototype.afterItemInit = function(tabItemEvent) {};
TabListener.prototype.closeItem = function(tabItemEvent) {};
TabListener.prototype.afterActivedTabItemChange = function(tabItemEvent) {};

TabListener.prototype.beforeActivedTabItemChange = function(tabItemChangeEvent) {};

/**
 * 分割布局事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var SpliterListener = function(bIgnoreError) {
	this.type = SpliterListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
SpliterListener.listenerType = "SPLITER_LISTENER";
SpliterListener.prototype.resizeDiv1 = function(spliterEvent) {};
SpliterListener.prototype.resizeDiv2 = function(spliterEvent) {};

/**
 * Grid事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var GridListener = function(bIgnoreError) {
	this.type = SpliterListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
GridListener.listenerType = "GRID_LISTENER";
GridListener.prototype.onDataOuterDivContextMenu = function(mouseEvent) {};
//GridListener.prototype.processPageCount = function(processPageCountEvent) {};

/**
 * Grid单元格事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var GridCellListener = function(bIgnoreError) {
	this.type = GridCellListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
GridCellListener.listenerType = "GRID_CELL_LISTENER";
GridCellListener.prototype.onCellClick = function(cellEvent) {};
GridCellListener.prototype.cellEdit = function(cellEvent) {};

GridCellListener.prototype.afterEdit = function(afterCellEditEvent) {};

GridCellListener.prototype.beforeEdit = function(beforeCellEditEvent) {};

GridCellListener.prototype.onCellValueChanged = function(cellValueChangedEvent) {};

/**
 * Grid行事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var GridRowListener = function(bIgnoreError) {
	this.type = GridRowListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
GridRowListener.listenerType = "GRID_ROW_LISTENER";
GridRowListener.prototype.beforeRowSelected = function(rowEvent) {};
GridRowListener.prototype.onRowDbClick = function(rowEvent) {};

GridRowListener.prototype.onRowSelected = function(rowSelectedEvent) {};



/**
 * Excel事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ExcelListener = function(bIgnoreError) {
	this.type = SpliterListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ExcelListener.listenerType = "EXCEL_LISTENER";
ExcelListener.prototype.onDataOuterDivContextMenu = function(mouseEvent) {};
//ExcelListener.prototype.processPageCount = function(processPageCountEvent) {};

/**
 * Excel单元格事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ExcelCellListener = function(bIgnoreError) {
	this.type = ExcelCellListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ExcelCellListener.listenerType = "EXCEL_CELL_LISTENER";
ExcelCellListener.prototype.onCellClick = function(cellEvent) {};
ExcelCellListener.prototype.cellEdit = function(cellEvent) {};

ExcelCellListener.prototype.afterEdit = function(afterCellEditEvent) {};

ExcelCellListener.prototype.beforeEdit = function(beforeCellEditEvent) {};

ExcelCellListener.prototype.onCellValueChanged = function(cellValueChangedEvent) {};

/**
 * Excel行事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ExcelRowListener = function(bIgnoreError) {
	this.type = ExcelRowListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ExcelRowListener.listenerType = "EXCEL_ROW_LISTENER";
ExcelRowListener.prototype.beforeRowSelected = function(rowEvent) {};
ExcelRowListener.prototype.onRowDbClick = function(rowEvent) {};

ExcelRowListener.prototype.onRowSelected = function(rowSelectedEvent) {};

/**
 * 树事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var TreeRowListener = function(bIgnoreError) {
	this.type = TreeRowListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
TreeRowListener.listenerType = "TREE_ROW_LISTENER";
TreeRowListener.prototype.beforeNodeCreate = function(treeRowEvent) {};

/**
 * 树节点事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var TreeNodeListener = function(bIgnoreError) {
	this.type = TreeNodeListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
TreeNodeListener.listenerType = "TREE_NODE_LISTENER";
TreeNodeListener.prototype.nodeCreated = function(treeNodeEvent) {};
TreeNodeListener.prototype.rootNodeCreated = function(treeNodeEvent) {};
TreeNodeListener.prototype.afterSelNodeChange = function(treeNodeEvent) {};
TreeNodeListener.prototype.onNodeLoad = function(treeNodeEvent){};
TreeNodeListener.prototype.onChecked = function(treeNodeEvent){};

TreeNodeListener.prototype.beforeSelNodeChange = function(treeNodeChangeEvent){};
TreeNodeListener.prototype.beforeNodeCaptionChange = function(treeNodeCaptionChangeEvent){};
//TreeNodeListener.prototype.beforeNodeCaptionChange = function(treeNodeCaptionChangeEvent) {};

TreeNodeListener.prototype.onclick = function(treeNodeMouseEvent) {};
TreeNodeListener.prototype.ondbclick = function(treeNodeMouseEvent) {};

TreeNodeListener.prototype.onDragStart = function(treeNodeDragEvent) {};
TreeNodeListener.prototype.onDragEnd = function(treeNodeDragEvent) {};

/**
 * 树右键菜单事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var TreeContextMenuListener = function(bIgnoreError) {
	this.type = TreeContextMenuListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
TreeContextMenuListener.listenerType = "TREE_CONTEXT_MENU_LISTENER";
TreeContextMenuListener.prototype.beforeContextMenu = function(treeContextMenuEvent) {};

/**
 * 链接事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var LinkListener = function(bIgnoreError) {
	this.type = LinkListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
LinkListener.listenerType = "LINK_LISTENER";
LinkListener.prototype.onclick = function(linkEvent) {};

/**
 * 列表事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ListListener = function(bIgnoreError) {
	this.type = ListListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ListListener.listenerType = "LIST_LISTENER";
ListListener.prototype.valueChanged = function(listEvent) {};
ListListener.prototype.dbValueChange = function(listEvent) {};

/**
 * 复选框事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var CheckboxListener = function(bIgnoreError) {
	this.type = CheckboxListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
CheckboxListener.listenerType = "CHECKBOX_LISTENER";
CheckboxListener.prototype.onChange = function(checkboxValueChangeEvent) {};

/**
 * 表单事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var AutoformListener = function(bIgnoreError) {
	this.type = AutoformListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
AutoformListener.listenerType = "AUTOFORM_LISTENER";
AutoformListener.prototype.inActive = function(simpleEvent) {};
AutoformListener.prototype.getValue = function(simpleEvent) {};
AutoformListener.prototype.active = function(simpleEvent) {};

AutoformListener.prototype.setValue = function(autoformSetValueEvent) {};


/**
 * Dataset事件监听器
 * @param bIgnoreError: 是否忽略错误，继续执行后面方法
 * @private
 */
var DatasetListener = function(bIgnoreError) {
	this.type = DatasetListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
DatasetListener.listenerType = "WEB_DATASET_LISTENER";
DatasetListener.prototype.onBeforeRowSelect = function(dsIndexEvent) {};
DatasetListener.prototype.onAfterRowSelect = function(rowSelectEvent) {};
DatasetListener.prototype.onAfterRowUnSelect = function(rowUnSelectEvent) {};
DatasetListener.prototype.onBeforeRowInsert = function(dsBeforeRowInsertEvent) {};
DatasetListener.prototype.onAfterRowInsert = function(rowInsertEvent) {};
DatasetListener.prototype.onBeforeDataChange = function(dsBeforeDataChangeEvent) {};
DatasetListener.prototype.onAfterDataChange = function(dataChangeEvent) {};
DatasetListener.prototype.onBeforeRowDelete = function(dsBeforeRowDeleteEvent) {};
DatasetListener.prototype.onAfterRowDelete = function(rowDeleteEvent) {};
//DatasetListener.prototype.onBeforeUndo = function(dsSimpleEvent) {};
DatasetListener.prototype.onBeforePageChange = function(dsBeforePageChangeEvent) {};
DatasetListener.prototype.onAfterPageChange = function(pageChangeEvent) {};
DatasetListener.prototype.onDataLoad = function(dataLoadEvent){};

/**
 * 焦点聚焦事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var FocusListener = function(bIgnoreError){
	this.type = FocusListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
FocusListener.listenerType = "FOCUS_LISTENER";

FocusListener.prototype.onFocus = function(focusEvent) {};
FocusListener.prototype.onBlur = function(focusEvent) {};


/**
 * 菜单中创建子项容器事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var ContainerListener = function(bIgnoreError){
	this.type = ContainerListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
ContainerListener.listenerType = "CONTAINER_LISTENER";

ContainerListener.prototype.onContainerCreate = function(simpleEvent) {};


/**
 * 文件上传组件事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var FileListener = function(bIgnoreError) {
	this.type = FileListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
FileListener.listenerType = "FILE_LISTENER";

FileListener.prototype.onUpload = function(fileUploadEvent) {};


/**
 * 数据校验事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var DataCheckListener = function(bIgnoreError) {
	this.type = DataCheckListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
DataCheckListener.listenerType = "DATA_CHECK_LISTENER";

DataCheckListener.prototype.onSuccess = function(dataCheckEvent) {};
DataCheckListener.prototype.onFailed = function(dataCheckEvent) {};

/**
 * 自定义组件事件监听器
 * @param bIgnoreError
 * @return
 * @private
 */
var SelfDefListener = function(bIgnoreError) {
	this.type = SelfDefListener.listenerType;
	this.ignoreError = getBoolean(bIgnoreError, false);
};
SelfDefListener.listenerType = "SELF_DEF_LISTENER";

SelfDefListener.prototype.onSelfDefEvent = function(selfDefEvent) {};

