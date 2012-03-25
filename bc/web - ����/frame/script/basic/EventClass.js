/**
 *	@fileoverview 事件参数对象类型（用于产生提示信息，运行时不必加载此文件）
 *	@author guoweic
 *	@version 
 */
 
/**
 * @private
 */
var simpleEvent = function() {};
simpleEvent.prototype.obj = Object;

/**
 * @private
 */
var mouseEvent = function() {};
mouseEvent.prototype.obj = Object;
mouseEvent.prototype.event = Object;

/**
 * @private
 */
var keyEvent = function() {};
keyEvent.prototype.obj = Object;
keyEvent.prototype.event = Object;

/**
 * @private
 */
var focusEvent = function() {};
focusEvent.prototype.obj = Object;
focusEvent.prototype.event = Object;

/**
 * @private
 */
var cardEvent = function() {};
cardEvent.prototype.obj = Object;
cardEvent.prototype.index = Object;

/**
 * @private
 */
var tabItemEvent = function() {};
tabItemEvent.prototype.obj = Object;
tabItemEvent.prototype.item = Object;

/**
 * @private
 */
var tabItemChangeEvent = function() {};
tabItemChangeEvent.prototype.obj = Object;
tabItemChangeEvent.prototype.activedItem = Object;
tabItemChangeEvent.prototype.targetItem = Object;

/**
 * @private
 */
var spliterEvent = function() {};
spliterEvent.prototype.obj = Object;
spliterEvent.prototype.oldH = Object;
spliterEvent.prototype.oldW = Object;
spliterEvent.prototype.newH = Object;
spliterEvent.prototype.newW = Object;

//var cellEvent = function() {};
//cellEvent.prototype.obj = Object;
//cellEvent.prototype.cell = Object;
//cellEvent.prototype.rowIndex = Object;
//cellEvent.prototype.colIndex = Object;
//
//var afterCellEditEvent = function() {};
//afterCellEditEvent.prototype.obj = Object;
//afterCellEditEvent.prototype.rowIndex = Object;
//afterCellEditEvent.prototype.colIndex = Object;
//afterCellEditEvent.prototype.oldValue = Object;
//afterCellEditEvent.prototype.newValue = Object;
//
//var beforeCellEditEvent = function() {};
//beforeCellEditEvent.prototype.obj = Object;
//beforeCellEditEvent.prototype.rowIndex = Object;
//beforeCellEditEvent.prototype.colIndex = Object;

/**
 * @private
 */
var cellEvent = function() {};
cellEvent.prototype.obj = Object;
cellEvent.prototype.cell = Object;
cellEvent.prototype.rowIndex = Object;
cellEvent.prototype.colIndex = Object;
cellEvent.prototype.oldValue = Object;
cellEvent.prototype.newValue = Object;

/**
 * @private
 */
var rowEvent = function() {};
rowEvent.prototype.obj = Object;
rowEvent.prototype.rowIndex = Object;
rowEvent.prototype.row = Object;

/**
 * @private
 */
var rowSelectedEvent = function() {};
rowSelectedEvent.prototype.obj = Object;
rowSelectedEvent.prototype.rowIndex = Object;

//var processPageCountEvent = function() {};
//processPageCountEvent.prototype.obj = Object;
//processPageCountEvent.prototype.pageInfo = Object;

/**
 * @private
 */
var menuItemEvent = function() {};
menuItemEvent.prototype.obj = Object;
menuItemEvent.prototype.item = Object;

/**
 * @private
 */
var treeRowEvent = function() {};
treeRowEvent.prototype.obj = Object;
treeRowEvent.prototype.row = Object;

/**
 * @private
 */
var treeNodeEvent = function() {};
treeNodeEvent.prototype.obj = Object;
treeNodeEvent.prototype.node = Object;

/**
 * @private
 */
var treeNodeChangeEvent = function() {};
treeNodeChangeEvent.prototype.obj = Object;
treeNodeChangeEvent.prototype.newNode = Object;
treeNodeChangeEvent.prototype.oldNode = Object;

/**
 * @private
 */
var treeNodeCaptionChangeEvent = function() {};
treeNodeCaptionChangeEvent.prototype.obj = Object;
treeNodeCaptionChangeEvent.prototype.labelFields = Object;
treeNodeCaptionChangeEvent.prototype.labelDelims = Object;
treeNodeCaptionChangeEvent.prototype.node = Object;

/**
 * @private
 */
var treeNodeMouseEvent = function() {};
treeNodeMouseEvent.prototype.obj = Object;
treeNodeMouseEvent.prototype.node = Object;
treeNodeMouseEvent.prototype.isClickSameNode = Object;

/**
 * @private
 */
var treeNodeDragEvent = function() {};
treeNodeDragEvent.prototype.obj = Object;
treeNodeDragEvent.prototype.sourceNode = Object;
treeNodeDragEvent.prototype.targetNode = Object;

/**
 * @private
 */
var treeContextMenuEvent = function() {};
treeContextMenuEvent.prototype.obj = Object;
treeContextMenuEvent.prototype.node = Object;
treeContextMenuEvent.prototype.ds = Object;
treeContextMenuEvent.prototype.level = Object;

//var dateEvent = function() {};
//dateEvent.prototype.obj = Object;
//dateEvent.prototype.date = Object;

//var comboChangeEvent = function() {};
//comboChangeEvent.prototype.obj = Object;
//comboChangeEvent.prototype.newItem = Object;
//comboChangeEvent.prototype.lastItem = Object;

/**
 * @private
 */
var linkEvent = function() {};
linkEvent.prototype.obj = Object;
linkEvent.prototype.link = Object;

/**
 * @private
 */
var checkboxValueChangeEvent = function() {};
checkboxValueChangeEvent.prototype.obj = Object;
checkboxValueChangeEvent.prototype.newValue = Object;
checkboxValueChangeEvent.prototype.oldValue = Object;

/**
 * @private
 */
var listEvent = function() {};
listEvent.prototype.obj = Object;
listEvent.prototype.item = Object;

/**
 * @private
 */
var autoformSetValueEvent = function() {};
autoformSetValueEvent.prototype.obj = Object;
autoformSetValueEvent.prototype.value = Object;

/**
 * @private
 */
var valueChangeEvent = function() {};
valueChangeEvent.prototype.obj = Object;
valueChangeEvent.prototype.newValue = Object;
valueChangeEvent.prototype.oldValue = Object;

/***************************Dataset事件对象*****************************/

/**
 * @private
 */
var dsIndexEvent = function() {};
dsIndexEvent.prototype.index = Object;

/**
 * @private
 */
var dsBeforeRowInsertEvent = function() {};
dsBeforeRowInsertEvent.prototype.index = Object;
dsBeforeRowInsertEvent.prototype.rows = Object;

/**
 * @private
 */
var dsBeforeDataChangeEvent = function() {};
dsBeforeDataChangeEvent.prototype.rowIndex = Object;
dsBeforeDataChangeEvent.prototype.colIndex = Object;
dsBeforeDataChangeEvent.prototype.newValue = Object;

/**
 * @private
 */
var dsBeforeRowDeleteEvent = function() {};
dsBeforeRowDeleteEvent.prototype.indices = Object;

/**
 * @private
 */
var dsBeforePageChangeEvent = function() {};
dsBeforePageChangeEvent.prototype.key = Object;
dsBeforePageChangeEvent.prototype.index = Object;

/**
 * @private
 */
var afterCellEditEvent = function() {};
afterCellEditEvent.prototype.obj = Object;
afterCellEditEvent.prototype.rowIndex = Object;
afterCellEditEvent.prototype.colIndex = Object;
afterCellEditEvent.prototype.oldValue = Object;
afterCellEditEvent.prototype.newValue = Object;

/**
 * @private
 */
var beforeCellEditEvent = function() {};
beforeCellEditEvent.prototype.obj = Object;
beforeCellEditEvent.prototype.rowIndex = Object;
beforeCellEditEvent.prototype.colIndex = Object;

/**
 * @private
 */
var cellValueChangedEvent = function() {};
cellValueChangedEvent.prototype.obj = Object;
cellValueChangedEvent.prototype.rowIndex = Object;
cellValueChangedEvent.prototype.colIndex = Object;
cellValueChangedEvent.prototype.oldValue = Object;
cellValueChangedEvent.prototype.newValue = Object;

/***************************Dataset原有事件对象*****************************/

/**
 * @private
 */
var rowSelectEvent = function() {};
rowSelectEvent.prototype.currentRow = Object;
rowSelectEvent.prototype.currentRowIndex = Object;
rowSelectEvent.prototype.isAdd = Object;
rowSelectEvent.prototype.lastSelectedIndices = Object;
rowSelectEvent.prototype.isMultiSelect = Object;
rowSelectEvent.prototype.toString = function() {};

/**
 * @private
 */
var pageChangeEvent = function() {};
pageChangeEvent.prototype.parentKey = Object;
pageChangeEvent.prototype.oldParentKey = Object;
pageChangeEvent.prototype.pageIndex = Object;
pageChangeEvent.prototype.oldPageIndex = Object;
pageChangeEvent.prototype.userObject = Object;
pageChangeEvent.prototype.toString = function() {};

/**
 * @private
 */
var rowInsertEvent = function() {};
rowInsertEvent.prototype.insertedRows = Object;
rowInsertEvent.prototype.insertedIndex = Object;
rowInsertEvent.prototype.toString = function() {};

/**
 * @private
 */
var dataCheckEvent = function() {};
dataCheckEvent.prototype.currentRow = Object;
dataCheckEvent.prototype.cellColIndices = Object;
dataCheckEvent.prototype.rulesDescribe = Object;
dataCheckEvent.prototype.toString = function() {};

/**
 * @private
 */
var dataChangeEvent = function() {};
dataChangeEvent.prototype.currentRow = Object;
dataChangeEvent.prototype.cellRowIndex = Object;
dataChangeEvent.prototype.cellColIndex = Object;
dataChangeEvent.prototype.currentValue = Object;
dataChangeEvent.prototype.oldValue = Object;
dataChangeEvent.prototype.pageIndex = Object;
dataChangeEvent.prototype.toString = function() {};

/**
 * @private
 */
var rowDeleteEvent = function() {};
rowDeleteEvent.prototype.deletedIndices = Object;
rowDeleteEvent.prototype.deletedRows = Object;
rowDeleteEvent.prototype.deleteAll = Object;
rowDeleteEvent.prototype.toString = function() {};

/**
 * @private
 */
var rowUnSelectEvent = function() {};
rowUnSelectEvent.prototype.currentRowIndex = Object;
rowUnSelectEvent.prototype.unSelectedIndices = Object;
rowUnSelectEvent.prototype.toString = function() {};

/**
 * @private
 */
var dataLoadEvent = function() {};
dataLoadEvent.prototype.keyValue = Object;
dataLoadEvent.prototype.pageIndex = Object;
dataLoadEvent.prototype.userObj = Object;

/**
 * @private
 */
var fileUploadEvent = function() {};
dataLoadEvent.prototype.obj = Object;
dataLoadEvent.prototype.files = Object;

/**
 * @private
 */
var stateClearEvent = function() {};
stateClearEvent.prototype.toString = function() {};

/**
 * @private
 */
var selfDefEvent = function() {};
selfDefEvent.prototype.obj = Object;
selfDefEvent.prototype.triggerId = Object;
selfDefEvent.prototype.event = Object;



