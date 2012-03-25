/**
 * @fileoverview 此文件定义了dataset对外暴露事件与事件模型
 * 
 * @author dengjt
 * @version 1.0
 */

/**
 * 行选中之前通知事件
 * 
 * @param 待选中行索引 return boolean值，false表示阻止进一步处理
 * 
 * @private
 * 
 */
Dataset.prototype.onBeforeRowSelect = function(index) {
	var dsIndexEvent = {
		"index" : index
	};
	this.doEventFunc("onBeforeRowSelect", DatasetListener.listenerType,
			dsIndexEvent);
	return true;
};

/**
 * 行选中之后通知事件，传入行选中事件
 * 
 * @private
 */
Dataset.prototype.onAfterRowSelect = function(rowSelectEvent) {
	//	var dsBaseEvent = {
	//			"event" : event
	// };

	this.doEventFunc("onAfterRowSelect", DatasetListener.listenerType,
			rowSelectEvent);
	return true;
};

/**
 * 行反选中之后通知事件，传入行反选中事件
 * 
 * @private
 */
Dataset.prototype.onAfterRowUnSelect = function(rowUnSelectEvent) {
	//	var dsBaseEvent = {
	//			"event" : event
	// };
	this.doEventFunc("onAfterRowUnSelect", DatasetListener.listenerType,
			rowUnSelectEvent);
};

/**
 * 行插入之前通知事件
 * 
 * @param index 插入位置
 * @param rows 待插入行 return boolean值，false表示阻止进一步处理
 * 
 * @private
 */
Dataset.prototype.onBeforeRowInsert = function(index, rows) {
	var dsBeforeRowInsertEvent = {
		"index" : index,
		"rows" : rows
	};
	this.doEventFunc("onBeforeRowInsert", DatasetListener.listenerType,
			dsBeforeRowInsertEvent);
	return true;
};

/**
 * 行选中之后通知事件，传入行选中事件
 * 
 * @private
 */
Dataset.prototype.onAfterRowInsert = function(rowInsertEvent) {
	//	var dsBaseEvent = {
	//			"event" : event
	// };
	this.doEventFunc("onAfterRowInsert", DatasetListener.listenerType,
			rowInsertEvent);
};

/**
 * 数据修改事件
 * 
 * @param rowIndex 行坐标
 * @param colIndex 列坐标
 * @param newValue 新值 return boolean 值，false表示阻止进一步处理
 * @private
 */
Dataset.prototype.onBeforeDataChange = function(rowIndex, colIndex, newValue,
		dataset) {
	var dsBeforeDataChangeEvent = {
		"rowIndex" : rowIndex,
		"colIndex" : colIndex,
		"newValue" : newValue,
		"dataset" : dataset
	};
	this.doEventFunc("onBeforeDataChange", DatasetListener.listenerType,
			dsBeforeDataChangeEvent);
	return true;
};

/**
 * 数据修改之后通知事件.
 * 
 * @param event 数据修改事件
 * @param pageIndex 如果pageIndex与当前页不一致，则表示跨页更新，逻辑中要特别处理。在涉及到分页的Dataset，并且允许跨页操作的
 *                  界面中特别要注意这一项逻辑.此参数可能为空，如果为空，则表示是当前页更新
 * @private
 */
Dataset.prototype.onAfterDataChange = function(dataChangeEvent, pageIndex) {
	//	var dsAfterDataChangeEvent = {
	//			"event" : event,
	//			"pageIndex" : pageIndex
	// };
	dataChangeEvent.pageIndex = pageIndex;
	this.doEventFunc("onAfterDataChange", DatasetListener.listenerType,
			dataChangeEvent);
};

/**
 * 
 * @private
 */
Dataset.prototype.beforeCallEvent = function(eventName, eventObj) {
	if (eventName == "onAfterDataChange") {
		if (this.afterDataChangeAcceptFields == null || this.afterDataChangeAcceptFields == "")
			return true;
		var fields = this.afterDataChangeAcceptFields;
		var colIndex = eventObj.cellColIndex;
		var currentFieldName = this.metadata[colIndex].key;
		for ( var i = 0, n = fields.length; i < n; i++) {
			if (fields[i] == currentFieldName)
				return true;
		}
		return false;
	} else if (eventName == "onBeforeDataChange") {
		if (this.beforeDataChangeAcceptFields == null || this.afterDataChangeAcceptField == "")
			return true;
		var fields = this.beforeDataChangeAcceptFields;
		var colIndex = eventObj.cellColIndex;
		var currentFieldName = this.metadata[colIndex].key;
		for ( var i = 0, n = fields.length; i < n; i++) {
			if (fields[i] == currentFieldName)
				return true;
		}
		return false;
	}
	return true;
};

/**
 * 行删除之前通知事件
 * 
 * @param indices 待删除行索引。已经从小到大排序 return boolean 值，false表示阻止进一步处理
 * @private
 */
Dataset.prototype.onBeforeRowDelete = function(indices) {
	var dsBeforeRowDeleteEvent = {
		"indices" : indices
	};
	this.doEventFunc("onBeforeRowDelete", DatasetListener.listenerType,
			dsBeforeRowDeleteEvent);
	return true;
};

/**
 * 行删除之后通知事件，传入行删除事件
 * 
 * @private
 */
Dataset.prototype.onAfterRowDelete = function(rowDeleteEvent) {
	this.doEventFunc("onAfterRowDelete", DatasetListener.listenerType,
			rowDeleteEvent);
};

// /**
// * 恢复之前通知事件
// * return boolean 值，false表示阻止进一步处理
// */
// Dataset.prototype.onBeforeUndo = function()
// {
// var dsSimpleEvent = {
//			
// };
// this.doEventFunc("onBeforeUndo", DatasetListener.listenerType,
// dsSimpleEvent);
// return true;
// };
//

/**
 * 恢复之后通知事件，传入Undo事件
 *
 */
Dataset.prototype.onAfterUndo = function(datasetUndoEvent){
 	this.doEventFunc("onAfterUndo", DatasetListener.listenerType, datasetUndoEvent);
};

/**
 * 显示页修改之前通知事件, 传入页面修改事件 return boolean 值，false表示阻止进一步处理
 * 
 * @private
 */
Dataset.prototype.onBeforePageChange = function(key, index) {
	var dsBeforePageChangeEvent = {
		"key" : key,
		"index" : index
	};
	this.doEventFunc("onBeforePageChange", DatasetListener.listenerType,
			dsBeforePageChangeEvent);
	return true;
};

/**
 * 显示页修改之后通知事件，传入页面修改事件
 * @private
 */
Dataset.prototype.onAfterPageChange = function(pageChangeEvent) {
	//	var dsBaseEvent = {
	//			"event" : event
	// };
	this.doEventFunc("onAfterPageChange", DatasetListener.listenerType,
			pageChangeEvent);
};

/**
 * 
 * @private
 */
Dataset.prototype.onDataLoad = function(keyValue, index, userObj) {
	var dataLoadEvent = {
		"keyValue" : keyValue,
		"pageIndex" : index,
		"userObj" : userObj
	};
	this.doEventFunc("onDataLoad", DatasetListener.listenerType, dataLoadEvent);
};

/**
 * @class Dataset行选择事件,此事件包含1.原来选中数组，2.新选中数组，3.当前选中行, 4.当前选中行标
 * @author dengjt
 * @private
 */
function RowSelectEvent() {
	/** 当前选中行*/
	this.currentRow = null;
	/** 当前选中行索引 */
	this.currentRowIndex = -1;
	/** 是否追加选中 */
	this.isAdd = false;
	/** 之前的选中序列 */
	this.lastSelectedIndices = null;
	/** 本次选择是否多选 */
	this.isMultiSelect = false;
}

/**
 * 
 * @private
 */
RowSelectEvent.prototype.toString = function() {
	return "row selected event:the current row is：" + this.currentRow
			+ ",the current index is:" + this.currentRowIndex;
};

/**
 * @class 整页数据更改事件
 * @author dengjt
 * @private
 */
function PageChangeEvent() {
	/** 页数据更改父键的值*/
	this.parentKey = null;
	/** 页数据更改前父键的值 */
	this.oldParentKey = null;
	/** 页数据更改后的页码 */
	this.pageIndex = null;
	/** 页数据更改前的页码 */
	this.oldPageIndex = null;
	/** 用户扩展字段 */
	this.userObject = null;
}

/**
 * 
 * @private
 */
PageChangeEvent.prototype.toString = function() {
	return "page change event:the current parentKey：" + this.parentKey
			+ ",the current page index:" + this.pageIndex
			+ ",the old parentKey:" + this.oldParentKey
			+ ",the old page index:" + this.oldPageIndex;
};

/**
 * @class 行插入事件。
 * @author dengjt
 * @private
 */
function RowInsertEvent() {
	/** 待插入行数组*/
	this.insertedRows = null;
	/** 插入行索引 */
	this.insertedIndex = -1;
}

/**
 * 
 * @private
 */
RowInsertEvent.prototype.toString = function() {
	return "row insert event:the current row is：" + this.insertedRows
			+ ",the current row index is:" + this.insertedIndex;
};

/**
 * @class 数据校验事件
 * @author gd
 * @private
 */
function DataCheckEvent() {
	/*违反规则的行*/
	this.currentRow = null;
	/* 违反规则的列索引数组 */
	this.cellColIndices = null;
	/* 违反的规则描述数组 */
	this.rulesDescribe = null;
};

/**
 * 
 * @private
 */
DataCheckEvent.prototype.toString = function() {
	return "the current row is:" + this.currentRow;
};

/**
 * @class 数据修改事件
 * @author dengjt
 * @private
 */
function DataChangeEvent() {
	/** 当前行 */
	this.currentRow = null;
	/** 修改行索引 */
	this.cellRowIndex = -1;
	/** 修改列索引 */
	this.cellColIndex = -1;
	/** 修改后值 */
	this.currentValue = null;
	/** 修改前值 */
	this.oldValue = null;
	
	// guoweic: add
	this.pageIndex = -1;
	// dingrf: add 不是列批量Change
	this.isColSingle = false;
};

/**
 * 
 * @private
 */
DataChangeEvent.prototype.toString = function() {
	return "data change event:the current row is：" + this.currentRow
			+ ",the current row index is:" + this.cellRowIndex
			+ ",the current column is:" + this.cellColIndex
			+ ",the current value:" + this.currentValue + ",the old value:"
			+ this.oldValue;
};

/**
 * @class 数据假删除事件
 * @author dengjt
 * @private
 */
function DataFalseDelEvent() {
	/** 假删除行索引 */
	this.delRowIndex = -1;
	this.delRow = null;
};

/**
 * 
 * @private
 */
DataFalseDelEvent.prototype.toString = function() {
};

/**
 * @class 数据某列批量修改事件
 * @author dingrf
 * @private
 */
function DataColSingleChangeEvent() {
	/** 所有修改行 */
	this.currentRows = new Array();
	/** 修改行索引 */
	this.cellRowIndices = new Array();
	/** 修改列索引 */
	this.cellColIndex = -1;
	/** 修改后值 */
	this.currentValues = new Array();
	/** 修改前值 */
	this.oldValues = new Array();
	/** 批量修标识 */
	this.isColSingle = true;
};

/**
 * 
 * @private
 */
DataColSingleChangeEvent.prototype.toString = function() {
	return "data col single change event:the rows are：" + this.cellRowIndices
			+ ",the current column is:" + this.cellColIndex
			+ ",the current values:" + this.currentValues + ",the old values:"
			+ this.oldValues;
};

 /**
 * @class
 * 数据操作撤销事件
 * @author dengjt
 */
function DatasetUndoEvent(){
}
DatasetUndoEvent.prototype.toString = function(){
	return "Dataset Undo Event";
};

/**
 * @class 行删除事件
 * @author dengjt
 * @private
 */
function RowDeleteEvent() {
	/**删除行索引*/
	this.deletedIndices = null;
	/** 删除行数据 */
	this.deletedRows = null;
	/** 是否全删 */
	this.deleteAll = false;
}

/**
 * 
 * @private
 */
RowDeleteEvent.prototype.toString = function() {
	return "row delete event:the deleted row index:" + this.deletedIndices
			+ ",the deleted row is:" + this.deletedRows;
};

/**
 * @class 行反选事件
 * @author dengjt
 * @private
 */
function RowUnSelectEvent() {
	// 当前反选行索引
	this.currentRowIndex = -1;
	this.unSelectedIndices = null;
}

/**
 * 
 * @private
 */
RowUnSelectEvent.prototype.toString = function() {
	return "row unselected event:the current unselected index:"
			+ this.currentRowIndex;
};

/**
 * 
 * @private
 */
function StateClearEvent() {
}

/**
 * 
 * @private
 */
StateClearEvent.prototype.toString = function() {
	return "state clear event";
};

/**
 * @class pagemeta改变事件
 * @author dingrf
 * @private
 */
function MetaChangeEvent() {
	/**改变列索引*/
	this.colIndex = null;
	/** 精度 */
	this.precision = null;
}

/**
 * 
 * @private
 */
MetaChangeEvent.prototype.toString = function() {
	return "metaChange event:the changed colIndex:" + this.colIndex;
};

/**
 * @class pagemeta改变事件
 * @private
 */
function FocusChangeEvent() {
	/**改变列索引*/
	this.focusIndex = null;
}

/**
 * 
 * @private
 */
FocusChangeEvent.prototype.toString = function() {
	return "focusChange event:the changed focusIndex:" + this.focusIndex;
};
