/**
 * @fileoverview 此文件中包含了客户端Dataset，ComboData，DsRelations等
 * @author dengjt
 * @version 1.0
 */

Dataset.prototype = new ListenerUtil;

Dataset.UPDATE_SAVE = 0;
Dataset.ALL_SAVE = 1;
Dataset.ALL_NOT_SAVE = 2;

// 主表数据的唯一key值常量
Dataset.MASTER_KEY = "MASTER_KEY";

/**
 * Dataset构造函数
 * @class
 * <p>
 * Dataset客户端实体客户端Dataset是服务器端Dataset的一个对应体，一个数据集
 * 结构往往对应数据库中一个表结构或者一个值对(ValueObject)结构。它是整个客户
 * 端MVC的数据事件驱动者，任何对于Dataset的改动将形成具体的Dataset事件，通知对它进行绑定的控件以及相应的页面通用逻辑。
 * </p>
 * <p>
 * 由于Dataset间往往存在主子关系。子Dataset中的各部分数据分别对应主Dataset的一条数据，因此，为了性能考虑和操作
 * 的便利性，对Dataset的实际数据的存储方式，并非采用二维平面结构，而是按照
 * （主数据一行的主--子数据一个块）的Map方式存储的。由于Dataset在某些页面中处于“主”地位，
 * 而在另一些页面中可能处于“子”地位，统一起见，所有Dataset的存储方式都遵从上述规则。
 * 对于主Dataset，它本身的父数据key的值默认使用Dataset.MASTER_KEY，即主Dataset。
 * 只有一个数据块的特殊Dataset。
 * Dataset的具体数据是存在于一系列的content块中的，即DsContent类中。
 * 通过这种结构，可以很方便的进行分页，数据缓加载，整个页面替换等操作。
 * </p>
 * 
 * @see DsContent
 * @constructor
 * @author dengjt
 * @modified 2007-11-19 修改获取行数逻辑，减少内存占用
 */
function Dataset(id, meta, lazyLoad, editable, pageSize) {
	ListenerUtil.call(this, true);
	/* 当前Dataset Id */
	this.id = id;
	// Dataset元数
	this.metadata = meta;
	// 是否缓加
	this.lazyLoad = lazyLoad;
	// masterkey对应的值做key，子数据值做Value。如果为主表，key设置一固定值，简化操
	this.dataMap = new HashMap();
	// 存储请求参数。Dataset本身不负责此Parameter的清理工作，因为类似刷新操作会重复利用里面的参数
	this.reqParameterMap = new HashMap();
	// 存储响应参数。Dataset在每次请求返回时，清空并重新加载此参
	this.resParameterMap = new HashMap();
	// 当前键值，对应主表一行。对于主表此值固
	this.currentKey = "";

	// 用于存储当前处理的dataset的参照装载字段，key:参照主字段ID，value:被参照出来的字段的ID数组
	this.sourceRefMap = null;
	// 此Dataset关联的控观察列表.注意,通知控件绑定并不是独立线程级因此Dataset部分无需考虑同步
	this.compArr = new Array();
	// //重做列表
	this.undoArr = new Array();
	// 静默状态。主要用于发生数据改变而不进行控件通知，在所有处理完后统一通知。避免控件反复渲
	this.silent = false;
	// 记录当前Dataset对控件的可编辑
	this.editable = editable;
	// 记录此Dataset的编辑属性是否被修改过
	this.editableChanged = false;
	// ds操作状
	this.operateStateArray = null;
	this.pageSize = pageSize;
	// 当前据交行行号
	this.focusRowIndex = -1;
	// 随机RowId计数器
	this.randomRowIndex = 0;
};

Dataset.prototype.addField = function(field){
	this.metadata.push(field);
};

Dataset.prototype.destroySelf = function() {
	//TODO 有问题！！！
//	var dataList = this.dataMap.values();
//	if(dataList != null){
//		for(var i = 0; i < dataList.length; i ++)
//			dataList[i].destroySelf();
//	}
//	this.dataMap.clear();
//	this.dataMap = null;
//	this.reqParameterMap.clear();
//	this.resParameterMap.clear();
//	this.compArr.clear();
//	this.compArr = null;
//	clearNodeProperties(this);
};

/**
 * 获得返回参数
 * 
 * @param key 参数 return 参数
 */
Dataset.prototype.getResponseParameter = function(key) {
	return this.resParameterMap.get(key);
};

/**
 * 解析响应参数，并清空旧有相应参数
 * @private 
 */
Dataset.prototype.genResParameter = function(rootNode) {
	this.resParameterMap.clear();
	var nodes = rootNode.selectNodes(EventContextConstant.res_parameters + "/" + EventContextConstant.parameter);
	if (nodes != null) {
		for ( var i = 0; i < nodes.length; i++)
			this.resParameterMap.put(getNodeAttribute(nodes[i], "name"),
					getNodeValue(nodes[i]));
	}
};

/**
 * @private
 */
Dataset.prototype.modifyStruct = function(newmeta) {
	this.metadata = newmeta;
};

/**
 * 清空Dataset内容
 */
Dataset.prototype.clear = function() {
	this.currentKey = "";
	this.dataMap.clear();
};

/**
 * 设置某一页数页码包含在xml，设置此页数据不会影响到当前显示
 * 
 * @param strXML
 */
Dataset.prototype.setData = function(strXML, userObj) {
	if (!strXML)
		return;
	// 将传入string解析成Dom对象
	var dom = createXmlDom(strXML);
	// 因缓加载的数据多嵌套一层，所以rootNode需要多取一
	var rootNode = dom.documentElement;
	// 获取响应参数
	this.genResParameter(rootNode);

	this.randomRowIndex = rootNode.getAttribute("randomRowIndex");
	var currentkey = rootNode.getAttribute("currentkey");
	if (currentkey == EventContextConstant.NULL)
		currentkey = "";
	var rowsetsEle = rootNode.selectSingleNode("rowsets");
	var rowsetList = rowsetsEle.selectNodes("rowset");
	var rowsetKeys = [];
	if (rowsetList != null && rowsetList.length > 0) {
		for ( var i = 0; i < rowsetList.length; i++) {
			var rowsetEle = rowsetList[i];
			var keyValue = rowsetEle.getAttribute("keyvalue");
			if (keyValue == "$NULL")
				keyValue = "";
			var oldKeyValue = rowsetEle.getAttribute("oldkeyvalue");
			rowsetKeys.push(keyValue);
			if (oldKeyValue != null && oldKeyValue != "") {
				this.replaceKeyValue(keyValue, oldKeyValue);
			}
			var rowSet = this.dataMap.get(keyValue);
			// 不存在数据体，则进行创建
			if (rowSet == null) {
				rowSet = new RowSet(rowsetEle, this);
				this.dataMap.put(keyValue, rowSet);
			}
			//否则创建相应数据体下的数
			else {
				rowSet.setData(rowsetEle);
			}
		}
	}
	var nowKeys = this.dataMap.keySet();
	for ( var i = 0; i < nowKeys.length; i++) {
		if (rowsetKeys.indexOf(nowKeys[i]) == -1) {
			this.removeRowSet(nowKeys[i]);
		}
	}

	if (currentkey != this.currentKey) {
		this.setCurrentPage(currentkey, 0, userObj);
	}
	
	var editable = rootNode.getAttribute("editable");
	if ((this.editable + "") != editable) {
		this.setEditable(editable == "true" ? true : false);
	}
	
	var focusIndex = rootNode.getAttribute("focusIndex");
	this.focusRowIndex = focusIndex;
	if (this.focusRowIndex != -1 && !this.silent) {
		var event = new FocusChangeEvent();
		event.focusIndex = this.focusRowIndex;
		this.dispatchEvent(event);
	}
};

/**
 * 设置json形式数据
 */
Dataset.prototype.setJsonData = function(jsonData, keyValue, pageindex, userObj) {
	var rowSet = this.dataMap.get(keyValue);
	// 不存在数据体，则进行创建
	if (rowSet == null) {
		rowSet = new RowSet(null, this);
		this.dataMap.put(keyValue, rowSet);
	}
	//否则创建相应数据体下的数
	rowSet.setJsonData(jsonData, keyValue, pageindex);

	if (keyValue != this.currentKey) {
		this.setCurrentPage(keyValue, 0, userObj);
	}
};

/**
 * 处理metadata相关设置
 */
Dataset.prototype.setMeta = function(jsonData){
	if (jsonData && jsonData.precision){
		var precisions = jsonData.precision;
		for(var field in precisions){
			var precision = precisions[field];
			var index = this.nameToIndex(field);
			if (index == -1) continue;
			var metadata = this.metadata[index];
			//精度不存在，或精度发生变化
			if (metadata.precision == null || metadata.precision != precision){
				metadata.precision = precision;
				var event = new MetaChangeEvent();
				event.colIndex = index;
				event.precision = precision;
				this.dispatchEvent(event);
			}
		}	
	}
};

/**
 * 获得keyValue指定子集的分页数
 * 
 * @param keyValue 可为空，为空则取当前keyValue
 * @return 分页数目
 */
Dataset.prototype.getPageCount = function() {
	var rs = this.getCurrentRowSet();
	if (rs == null)
		return 0;
	return rs.pagecount;
};

/**
 * 获取指定页的当前页码
 * 
 * @param keyValue 可为空，为空则取当前keyValue
 * @return 当前页索
 */
Dataset.prototype.getPageIndex = function() {
	var rs = this.getCurrentRowSet();
	if (rs == null)
		return 0;
	return rs.pageindex;
};

/**
 * 此方法仅用于客户端进行Dataset初始化时调用，非客户端初始化，请不要调用。
 * @private
 */
Dataset.prototype.initialize = function(keyValue) {
	var rowSet = this.getCurrentRowSet();
	var event = new PageChangeEvent();
	event.parentKey = keyValue;
	event.oldParentKey = this.currentKey;
	event.pageIndex = 0;
	event.oldPageIndex = (rowSet == null) ? -1 : rowSet.pageindex;

	if (keyValue != null)
		this.currentKey = keyValue;
	else
		this.currentKey = Dataset.MASTER_KEY;

	var rowSet = new RowSet(null, this);
	rowSet.keyvalue = this.currentKey;
	this.dataMap.put(this.currentKey, rowSet);
	this.dispatchEvent(event);
	this.onAfterPageChange(event);
};

/**
 * 设置页面显示数量
 */
Dataset.prototype.setPageSize = function(pageSize) {
	//当前页面大小
	this.pageSize = pageSize;
	this.dataMap.clear();
	// 设置显示空白
	this.setCurrentPage(-1, -1);
	// //删除前台所有数据缓
	// this.deleteData(null, true);
	// this.currentKey = Dataset.MASTER_KEY;
	// 重新设置页面为当前key对应的第一
	// this.setCurrentPage();
};

/**
 * 删除主表数据对应的子表所有数
 */
Dataset.prototype.removeRowSet = function(keyValue) {
	this.dataMap.remove(keyValue);
	// 如果删除的是当前页，则需显示空白
	if (keyValue == this.currentKey) {
		this.currentKey = "";
		// 设置显示空白
		this.setCurrentPage(-1, -1, null, keyValue);
	}

};

/**
 * 清除keyValue页的所有数据
 */
Dataset.prototype.clearData = function(keyValue) {
	if (keyValue == null)
		keyValue = this.currentKey;
	if (keyValue == null)
		return;
	this.dataMap.remove(keyValue);
	this.initialize(keyValue);
};

/**
 * 用新key值替换临时主表键值
 */
Dataset.prototype.replaceKeyValue = function(newKeyValue, oldKeyValue) {
	var dsContent = this.dataMap.get(oldKeyValue);
	if (dsContent) {
		this.dataMap.put(newKeyValue, dsContent);
		this.dataMap.remove(oldKeyValue);
		if (this.currentKey == oldKeyValue)
			this.currentKey = newKeyValue;
	}
};

Dataset.prototype.appendCurrentPage = function(userObj, queryKey){
	this.reqParameterMap.put(IDatasetConstant.QUERY_KEYVALUE,
				queryKey);
	this.onDataLoad(this.currentKey, 0, userObj);
	return;
};

/**
 * 设置当前将通知观察者进行数据重新绑定
 * 
 * @param keyValue
 * @param index 页码 -1表示当前数据没有选中
 * @param userObj 用来反传回调用方的参数。用来解决异步调用无法跟踪发起方的问
 */
Dataset.prototype.setCurrentPage = function(keyValue, index, userObj, currKey, isRefresh, append) {
	var oldCurrentKey = this.currentKey;
	// -1表示设置当前页到空白
	if (keyValue == -1 || keyValue == "") {
		if (currKey != null && currKey != "")
			this.dataMap.remove(currKey);
		this.currentKey = "";
		if (!this.silent) {
			var event = new PageChangeEvent();
			event.oldParentKey = oldCurrentKey;
			this.dispatchEvent(event);
		}
		return;
	}
	//null表示取当前页的上下文
	if (keyValue == null) {
		if (this.currentKey == "" || this.currentKey == "-1")
			keyValue = Dataset.MASTER_KEY;
		else
			keyValue = this.currentKey;
	}
	if (!index)
		index = 0;
	// 强制成整型，防止其它控件逻辑出错
	index = parseInt(index);
	// 没有选中页情
	if (index == -1) {
		var rowSet = this.dataMap.get(keyValue);
		if (rowSet != null)
			rowSet.pageindex = -1;
		if (!this.silent) {
			var event = new PageChangeEvent();
			this.dispatchEvent(event);
		}
	}
	//如果页面没有请求过，发送缺页请求。一般缺页处理函数将处理缺页，并再次调用此方
	if (!this.isDataRequested(keyValue, index) || (isRefresh != null && isRefresh == true) || append) {
		this.reqParameterMap.put(IDatasetConstant.QUERY_PARAM_KEYVALUE,
				keyValue);
		this.reqParameterMap.put(IDatasetConstant.QUERY_PARAM_PAGEINDEX, index);
		this.onDataLoad(keyValue, index, userObj);
		return;
	}
	var rowSet = this.dataMap.get(keyValue);
	if (rowSet != null) {
		//-1代表特殊页面
		if (index > rowSet.pagecount - 1) {
			alert("the page index:" + index + " is not right");
			return;
		}
		if (this.onBeforePageChange(keyValue, index) == false)
			return;
		var event = new PageChangeEvent();
		event.parentKey = keyValue;
		event.oldParentKey = this.currentKey;
		event.pageIndex = index;
		event.oldPageIndex = rowSet.pageindex;
		event.userObject = userObj;
		this.currentKey = keyValue;
		rowSet.pageindex = index;

		if (!this.silent)
			this.dispatchEvent(event);
		if (this.onInternalPageChange) {
			this.onInternalPageChange(event);
		}
		if (!this.silent)
			this.onAfterPageChange(event);
	}
	var indices = rowSet.getSelectedIndices();
	if (indices != null && indices.length > 0) {
		for ( var i = 0; i < indices.length; i++) {
//			this.addRowSelected(indices[i]);
			this.addRowSelectedInternal(indices[i], true, true);
		}
	}
};

/**
 * 设置请求参数Map
 * 
 * @param paramMap
 */
Dataset.prototype.setReqParameterMap = function(paramMap) {
	this.reqParameterMap = paramMap;
};

/**
 * 添加请求参数
 * 
 * @key 参数
 * @value 参数
 */
Dataset.prototype.addReqParameter = function(key, value) {
	this.reqParameterMap.put(key, value);
};

/**
 * 获取当前页的所有行
 */
Dataset.prototype.getRows = function() {
	var rowSet = this.getCurrentRowSet();
	if (rowSet == null)
		return null;
	return rowSet.getRows();
};

/**
 * 获取所有行
 */
Dataset.prototype.getAllRows = function() {
	var rowSets = this.getRowSets();
	var rows = new Array;
	for ( var i = 0; i < rowSets.length; i++) {
		var rowSet = rowSets[i];
		var tempRows = rowSet.getAllRows();
		if (tempRows != null) {
			for ( var j = 0; j < tempRows.length; j++)
				rows.push(tempRows[j]);
		}
	}
	return rows;
};

/**
 * 根据RowId获得行索引号
 */
Dataset.prototype.getRowIndexById = function(id) {
	var rows = this.getAllRows();
	for ( var i = 0; i < rows.length; i++) {
		if (rows[i].rowId == id)
			return i;
	}
	return -1;
};

/**
 * 获取范围内数据行
 * @param startIndex 开始索引
 * @param count 查询数量
 */
Dataset.prototype.getRowsByScale = function(startIndex, count) {
	var rowSet = this.dataMap.get(this.currentKey);
	if (rowSet == null)
		return null;
	return rowSet.getRowsByScale(startIndex, count);
};

/**
 * 获取当前RowSet
 */
Dataset.prototype.getCurrentRowSet = function() {
	return this.getRowSet(this.currentKey);
};

/**
 * 获得参数指定key所对应的RowSet
 * 
 * @param keyValue 父dataset的parent key 所对应的
 * @return dsContent 数据
 */
Dataset.prototype.getRowSet = function(keyValue) {
	if (keyValue == null) {
		return null;
	}
	return this.dataMap.get(keyValue);
};

/**
 * 获取所有RowSet
 */
Dataset.prototype.getRowSets = function() {
	return this.dataMap.values();
};

/**
 * 设置绑定控件，dataset在有变化时将使用适当事件对控件进行通知。要求所有具备绑定dataset功能的控件
 * 必须具备onModelChange(event)的方法实现。
 * 
 * @param comp
 */
Dataset.prototype.bindComponent = function(comp) {
	if (this.lazyLoad == false) {
		if (!this.dsLoaded){
			this.dsLoaded = true;
			this.setCurrentPage(Dataset.MASTER_KEY, 0);
			this.reqParameterMap.remove("openBillId");
		}
	}
	this.compArr.push(comp);
};

/**
 * 解除控件绑定 dataset将去除和此控件的关联
 */
Dataset.prototype.unbindComponent = function(comp) {
	var compindex = new Array();
	for ( var i = 0; i < this.compArr.length; i++) {
		if (this.compArr[i] == comp)
			compindex.push(o);
			//this.compArr[i].splice(i, 1);
	}
	for(var i=0;i<compindex.length;i++){
		this.compArr.splice(compindex[i], 1);
	}
};

/**
 * 通过Dataset集中控制Dataset的可编辑，所有绑定此dataset的控件将被设置成editable的状态。
 * 
 * @param editable{boolean} 是否可编
 */
Dataset.prototype.setEditable = function(editable) {
	this.editableChanged = true;
	this.editable = editable;
	for ( var i = 0; i < this.compArr.length; i++) {
		if (this.compArr[i].setEditable)
			this.compArr[i].setEditable(editable);
	}
};

/**
 * 获取Dataset的可编辑
 */
Dataset.prototype.isEditable = function(editable) {
	return this.editable;
};

/**
 * 获取翻页对象
 */
Dataset.prototype.getPaginations = function() {
	var compArr = new Array;
	for ( var i = 0; i < this.compArr.length; i++) {
		if (this.compArr[i] instanceof PaginationComp)
			compArr.push(this.compArr[i]);
	}
	return compArr;
};

/*
 * 生成用于前后台交互用的record id 规则 0/1 + dsid + random 前台产生的第一位数字是1 后台产生的第一位数字是0
 * @private
 */
Dataset.getRandomRowId = function() {
	var id = "1" + (new Date().getTime());
	var random = Math.random();
	var str = (random * 10000000000).toString();
	id += str.substring(0, 10);
	return id;
};

/**
 * 生成用于前后台交互用的record id 规则 0/1 + random 前台产生的第一位数字是1 后台产生的第一位数字是0
 * @private
Dataset.getRandomRowId = function() {
	var id = "1" + this.id + this.randomRowIndex;
	return id;
};
 */

/**
 * 设置下一行为当前行
 */
Dataset.prototype.nextRow = function() {
	this.changeDisplayRow(0);
};

/**
 * 设置最后一行为当前行
 */
Dataset.prototype.lastRow = function() {
	this.changeDisplayRow(3);
};

/**
 * 设置第一行为当前行
 */
Dataset.prototype.firstRow = function() {
	this.changeDisplayRow(2);
};

/**
 * 设置上一行为当前行
 */
Dataset.prototype.preRow = function() {
	this.changeDisplayRow(1);
};

/**
 * 设置当前供其它方法调用
 * 
 * @private
 * @param type 设置方式
 */
Dataset.prototype.changeDisplayRow = function(type) {
	var dsContent = this.dataMap.get(this.currentKey);
	if (dsContent == null)
		throw new Error("no data");
	var currPage = dsContent.currPage;
	var oldRow = null;
	if (dsContent.selectArr[currPage].selectedIndices == null)
		dsContent.selectArr[currPage].selectedIndices = new Array();
	else if (dsContent.selectArr[currPage].selectedIndices.length > 0)
		oldRow = dsContent
				.getRow(dsContent.selectArr[currPage].selectedIndices[0]);

	// var event = new RowSelectEvent();
	var index = -1;
	// next Row
	if (type == 0) {
		if (oldRow != null)
			index = this.getRowIndex(oldRow);
		if (index == -1)
			index = 0;
		else if (index >= this.getRows().length - 1)
			index = 0;
		else
			index = index + 1;
	}
	//pre Row
	else if (type == 1) {
		if (oldRow != null)
			index = this.getRowIndex(oldRow);
		if (index == -1)
			index = 0;
		else if (index == 0)
			index = this.getRows().length - 1;
		else
			index = index - 1;
	}
	//first Row
	else if (type == 2) {
		index = 0;
	}
	//last Row
	else if (type == 3) {
		index = this.getRows().length - 1;
	}

	this.addRowSelectedInternal(index, false);
};

/**
 * 获取行索引
 */
Dataset.prototype.getRowIndex = function(row, pageIndex, keyValue) {
	var indices = this.getRowIndices( [ row ], pageIndex, keyValue);
	return (indices == null || indices.length == 0) ? -1 : indices[0];
};

/**
 * 获取查询行索引
 */
Dataset.prototype.getRowIndices = function(rows, pageIndex, keyValue) {
	var rs = null;
	if (keyValue == null)
		rs = this.getCurrentRowSet();
	else
		rs = this.getRowSet(keyValue);
	if (rs == null)
		return null;
	var rd = null;
	if (pageIndex == null || pageIndex == -1) {
		rd = rs.getCurrentRowData();
	} else {
		rd = rs.getRowData(pageIndex);
	}
	if (rd == null)
		return null;

	return rd.getRowIndices(rows);
};

/**
 * 获取当前RowData
 */
Dataset.prototype.getCurrentRowData = function() {
	var rs = this.getCurrentRowSet();
	if (rs == null)
		return null;
	return rs.getCurrentRowData();
};

/**
 * 派发事件,包括控件派发和主子逻辑派发
 * @private
 */
Dataset.prototype.dispatchEvent = function(event) {
	if(this.recordUndoSign){
		if(!(event instanceof DatasetUndoEvent)){
			this.undoArr.push(event);
		}
	}
	for ( var i = 0; i < this.compArr.length; i++) {
		this.compArr[i].onModelChanged(event, this);
	}
	this.widget.dispatchEvent2Ds(event, this.id);
};

/**
 * 设置当前聚焦行
 */
Dataset.prototype.setFocusRowIndex = function(index) {
	if (this.focusRowIndex == index)
		return;
	if (index < 0)
		this.focusRowIndex = -1;
	else
		this.focusRowIndex = index;
};

/**
 * 获取当前聚焦行
 */
Dataset.prototype.getFocusRowIndex = function() {
	return this.focusRowIndex;
};

/**
 * 获取当前聚焦行
 */
Dataset.prototype.getFocusRow = function() {
	return this.getRow(this.focusRowIndex);
};

/**
 * 设置行选中
 */
Dataset.prototype.setRowSelected = function(index) {
	this.addRowSelectedInternal(index, false);
};

/**
 * 增加选中行
 */
Dataset.prototype.addRowSelected = function(index) {
	this.addRowSelectedInternal(index, true);
};

/**
 * 增加选中行
 * 
 * 在设置currentPage时，需要dispath事件
 * 
 * @private
 */
Dataset.prototype.addRowSelectedInternal = function(indices, isAdd, isSetCurrentPage) {
	isSetCurrentPage = getBoolean(isSetCurrentPage, false);
	var index = indices;
	if (indices instanceof Array) {
		index = indices[0];
	}
	var rowSet = this.dataMap.get(this.currentKey);
	if (rowSet == null)
		throw new Error("no data");

	var rowData = rowSet.getCurrentRowData();
	var selIndices = rowData.getSelectedIndices();
	if (!isAdd) {
		if (selIndices != null && selIndices.length > 0) {
			// 获取上一个选中且修改的
			var curSelRow = this.getRow(selIndices[0]);
			if (curSelRow != null && (curSelRow.state == DatasetRow.STATE_UPD || curSelRow.state == DatasetRow.STATE_NEW)) {
				this.checkDatasetRow(curSelRow);
			}
		}
	}
	if (this.onBeforeRowSelect(index) == false)
		return false;

	// 设置之前选中
	var event = new RowSelectEvent();
	var unSelEvent = null;
	//dingrf 之前为非选中状态，变为选中状态
	var newSelected = false;
	if (index == 0)
		newSelected = true;
	event.lastSelectedIndices = selIndices;
	if (indices instanceof Array) {
		event.isMultiSelect = true;
	}

	if (selIndices == null) {
		rowData.selectedIndices = new Array();
		selIndices = rowData.getSelectedIndices();
	} else if (selIndices.length > 0) {
		unSelEvent = new RowUnSelectEvent();
		unSelEvent.currentRowIndex = selIndices[0];
		if (!isAdd || index == -1)
			selIndices.splice(0, selIndices.length);
	}
	if (index != -1) {
		var newRow = this.getRows()[index];
		if (indices instanceof Array) {
			for ( var i = 0; i < indices.length; i++) {
				var id = indices[i];
				var iid = selIndices.indexOf(id);
				if (iid == -1){
					selIndices.push(id);
					newSelected = true;
				}
			}
		} 
		else {
			var iid = -1;
			if (selIndices.length > 0)
				iid = selIndices.indexOf(index);
			if (iid == -1){
				selIndices.push(index);
				newSelected = true;
			}
		}
		
		event.currentRow = newRow;
		event.currentRowIndex = index;
	}
	event.isAdd = isAdd;
	if (!this.silent && (newSelected || isSetCurrentPage)) {
		if (!isAdd)
			this.dispatchEvent(unSelEvent);
		this.dispatchEvent(event);
	}
	if (!this.silent && (newSelected || isSetCurrentPage)) {
		this.onAfterRowSelect(event);
	}
};

/**
 * 对指定cell进行设置错误信息操作
 * @private
 */
Dataset.prototype.setError = function(rowIndex, colIndex, msg) {
	var event = new DataCheckEvent();
	event.cellColIndices = [ colIndex ];
	event.currentRow = this.getRow(rowIndex);
	event.rulesDescribe = [ msg ];
	this.dispatchEvent(event);
};

/**
 * 清除指定cell的错误信息
 * @private
 */
Dataset.prototype.clearError = function(rowIndex, colIndex) {
	var event = new DataCheckEvent();
	event.cellColIndices = [ colIndex ];
	event.currentRow = this.getRow(rowIndex);
	event.rulesDescribe = [ "" ];
	this.dispatchEvent(event);
};

/**
 * 设置指定行的选中状态
 * 
 * @param index{int} 指定行索引
 */
Dataset.prototype.setRowUnSelected = function(index) {
	this.getCurrentRowData().setRowUnSelected(index);
};

/**
 * 获得选中如果分页,则仅返回当前页的 return 选中行
 */
Dataset.prototype.getSelectedRows = function() {
	var rowSet = this.dataMap.get(this.currentKey);
	if (rowSet == null)
		return null;
	return rowSet.getSelectedRows();
};

/**
 * 获得第一个选中行
 * 
 */
Dataset.prototype.getSelectedRow = function() {
	var rows = this.getSelectedRows();
	return rows == null ? null : rows[0];
};

/**
 * 获取所有选中行
 */
Dataset.prototype.getAllSelectedRows = function() {
	var rowArr = new Array;
	var rowSets = this.getRowSets();
	for ( var i = 0; i < rowSets.length; i++) {
		var selRows = rowSets[i].getAllSelectedRows();
		if (selRows != null) {
			for ( var j = 0; j < selRows.length; j++)
				rowArr.push(selRows[j]);
		}
	}
	return rowArr;
};

/**
 * 获得当前页选中行索引的第一个
 */
Dataset.prototype.getSelectedIndex = function() {
	var indices = this.getSelectedIndices();
	if (indices == null || indices.length == 0)
		return -1;
	return indices[0];
};

/**
 * 获取当前选中页的选中索引
 */
Dataset.prototype.getSelectedIndices = function() {
	var rowSet = this.dataMap.get(this.currentKey);
	if (rowSet == null)
		return null;
	var indices = rowSet.getSelectedIndices(rowSet.pageindex);
	if (indices == null)
		return indices;
	return indices;
};

/**
 * 返回更新如果分页,则仅返回当前页的 
 * @return 更新行
 */
Dataset.prototype.getUpdatedRows = function(pageIndex) {
	var dsContent = this.dataMap.get(this.currentKey);
	if (dsContent == null)
		throw new Error("no data");
	var currPage = pageIndex ? pageIndex : dsContent.currPage;
	var records = dsContent.getRows(currPage);
	if (records == null)
		return null;
	var rsArr = new Array();
	for ( var i = 0; i < records.length; i++) {
		if (records[i].state == DatasetRow.STATE_UPD)
			rsArr.push(records[i]);
	}
	return rsArr;
};

/**
 * 返回已删除行，如果分页，则仅返回当前页的删除。
 * 注意:如果已经调用过clearState方法，则删除行将被清空。
 * @return 已删除行
 */
Dataset.prototype.getDeletedRows = function() {
	var dsContent = this.getDsContent();
	if (dsContent == null)
		return null;
	var delArr = dsContent.deletedRows;
	if (delArr == null)
		return null;
	var arr = new Array;
	// 拷贝并修改为del状态
	for ( var i = 0; i < delArr.length; i++) {
		var row = delArr[i].clone();
		row.state = DatasetRow.STATE_DEL;
		row.rowId = delArr[i].rowId;
		arr.push(row);
	}

	return arr;
};

/**
 * 返回新增如果分页，则仅返回当前页的新增行
 * @return 新增行
 */
Dataset.prototype.getNewAddedRows = function(pageIndex) {
	var dsContent = this.dataMap.get(this.currentKey);
	if (dsContent == null)
		throw new Error("no data");
	var currPage = pageIndex ? pageIndex : dsContent.currPage;
	var records = dsContent.getRows(currPage);
	if (records == null)
		return records;
	var rsArr = new Array();
	for ( var i = 0; i < records.length; i++) {
		if (records[i].state == DatasetRow.STATE_NEW)
			rsArr.push(records[i]);
	}
	return rsArr;
};

/**
 * 清除行状恢复普通状，主要用在操作完成，将新增行等转换为普通行
 */
Dataset.prototype.clearState = function() {
	var dsContent = this.dataMap.get(this.currentKey);
	if (dsContent != null) {
		var records = dsContent.getRows(-1);
		if (records != null) {
			for ( var i = 0; i < records.length; i++)
				records[i].state = DatasetRow.STATE_NRM;
		}
		dsContent.deletedRows = null;
	}
	//	this.undoArr.clear();
	var event = new StateClearEvent();
	this.dispatchEvent(event);
};

Dataset.prototype.recordUndo = function() {
	this.clearUndo();
	this.recordUndoSign = true;
};
/**
 * undo操作，一次回退到重做链头部
 *
 */
Dataset.prototype.undo = function(){
	if(this.undoArr != null && this.undoArr.length > 0){
		var undoEvent = new DatasetUndoEvent();
		//设置静默状态
		this.silent = true;
		var event = null;
		log("begin to undo");
		while(this.undoArr.length > 0){
			event = this.undoArr.pop();
			if(RowSelectEvent.prototype.isPrototypeOf(event)){
				this.setRowUnSelected(event.currentRowIndex);
			}
			else if(RowUnSelectEvent.prototype.isPrototypeOf(event)){
				this.setRowSelected(event.currentRowIndex);
			}
			else if(RowInsertEvent.prototype.isPrototypeOf(event)){
				//alert("insert:" + event);
				var indices = new Array();
				for(var i = event.insertedRows.length - 1; i >= 0;  i --)
				{
					indices.push(event.insertedIndex + i);
				}
				this.deleteRows(indices);
			}
			else if(DataChangeEvent.prototype.isPrototypeOf(event)){
				//alert("datachange:" + event);
				this.setValueAt(event.cellRowIndex, event.cellColIndex, event.oldValue);
			}
			
			else if(RowDeleteEvent.prototype.isPrototypeOf(event)){
				for(var i = 0; i < event.deletedIndices.length; i ++){
					this.insertRow(event.deletedIndices[i], event.deletedRows[i]);
				}
			}
			
			else if(PageChangeEvent.prototype.isPrototypeOf(event)){
				//如果change之前没有key，表明之前是初始化状态的。需要设置成-1,表示恢复到初始化。仍使用null会导致仍然使用当前key
				var oldParentKey = event.oldParentKey;
				if(oldParentKey == null)
					oldParentKey = -1;
				this.setCurrentPage(oldParentKey, event.oldPageIndex);
			}
		}
		
		/*var dsContent = this.dataMap.get(this.currentKey);
		if(dsContent != null){
			var records = dsContent.recordArr[dsContent.currPage];
			if(records != null){
				for(var i = 0; i < records.length; i ++)
					records[i].state = DatasetRow.STATE_NRM;
			}
	
			dsContent.deletedRows = null;
		}
		*/
		log("undo complete");
		//恢复静默状态
		this.silent = false;
		//派发Undo事件
	
		this.dispatchEvent(undoEvent);
		this.recordUndoSign = false;
	}
	this.clearState();
};

Dataset.prototype.clearUndo = function()
{
	this.undoArr.clear();
};

/**
 * 判断当前Key值对应的数据是否请求
 */
Dataset.prototype.isDataRequested = function(keyValue, index) {
	var rowSet = this.dataMap.get(keyValue);
	if (index == -1)
		return true;
	if (rowSet == null)
		return false;
	var pageIndex = index ? index : rowSet.getPageIndex();
	if (pageIndex == -1)
		return true;
	return rowSet.getRowData(pageIndex) != null;
};

/**
 * 获得当前页指定行
 * 
 * @param index 行索 return 返回当前页指定行
 */
Dataset.prototype.getRow = function(index) {
	var rows = this.getRows();
	if (rows != null)
		return rows[index];
	return null;
};

/**
* 删除行数据
*/
Dataset.prototype.deleteRow = function(index)
{
	this.deleteRows([index]);
};

/**
* 删除索引指定的多行。
*
*/
Dataset.prototype.deleteRows = function(rowIndices)
{
	var oldRows = this.getRows();
	var delAll = false;
	//排序
	if(rowIndices == -1){
		rowIndices = new Array;
		delAll = true;
		var count = this.getRowCount();
		for(var i = 0; i < count; i ++)
			rowIndices.push(i);
	}
	else{
		rowIndices = rowIndices.sort(defaultIntSort);
	}
	
	var tmpRows = new Array;
	var selIndices = this.getSelectedIndices();
	for(var i = rowIndices.length - 1; i >= 0; i --)
	{
		removeFromArray(selIndices, rowIndices[i]);
		//删除行之后，大于此行的索引都应该减一
		if(selIndices != null){
			for(var j = 0; j < selIndices.length; j ++){
				if(selIndices[j] > rowIndices[i])
					selIndices[j] --;
			}
		}
		var delRow = oldRows[rowIndices[i]];
		if(delRow == null){
			continue;
		}
		oldRows.splice(rowIndices[i], 1);
		var dsContent = this.dataMap.get(this.currentKey);
		//如果是新增行不放入删除行列表中(永久删除)
		if(delRow.state != DatasetRow.STATE_NEW){
			if(dsContent.deletedRows == null)
				dsContent.deletedRows = new Array;
			dsContent.deletedRows.push(delRow);
		}
		tmpRows.push(delRow);		
	}
	
	if(!this.silent)
	{
		var event = new RowDeleteEvent();
		event.deletedRows = tmpRows;
		event.deletedIndices = rowIndices;
		event.deleteAll = delAll;
		this.dispatchEvent(event);
		this.onAfterRowDelete(event);
	}
};

/**
 * @private
 */
Dataset.prototype.getTotalCount = function() {

};

/**
 * 获取行大小
 * @private
 */
Dataset.prototype.getRowSize = function() {
	return this.metadata.length;
};

/**
 * 获取页大小
 * @private
 */
Dataset.prototype.getPageSize = function() {
	return this.pageSize;
};

/**
 * 获取行数
 * @private
 */
Dataset.prototype.getRowCount = function() {
	var rd = this.getCurrentRowData();
	if (rd == null)
		return 0;
	return rd.getRowCount();
};

/**
 * 获得指定页行。
 * 注意，调用此函数时如果当前页数据没有解析过，将先进行解析。
 * 
 * @param index 指定页码 return 页行0表示
 */
Dataset.prototype.getRowCount = function() {
	var rd = this.getCurrentRowData();
	if (rd == null)
		return 0;
	return rd.getRowCount();
};

/**
 * 获得所有行数
 */
Dataset.prototype.getAllRowCount = function() {
	var rs = this.getCurrentRowSet();
	if (rs == null)
		return 0;
	return rs.getAllRowCount();
};

/**
 * 设置row,col所对应元素的值 <b>注意，此方法和DatasetRow的setCellValue的区别在于，此方法会触发DataChangeEvent</b>
 * 
 * @param rowIndex 行索引
 * @param colIndex 列索引
 * @param value 新值
 * @param withTrigger 是否出发参照列取值行为。此参数往往不需要设置，仅在确定需要设置参照触发情况下使用
 */
Dataset.prototype.setValueAt = function(rowIndex, colIndex, value) {
	var rd = this.getCurrentRowData();
	if(rd != null)
		rd.setValueAt(rowIndex, colIndex, value);
};


/**
 * 设置某一列元素的值，此方法触发DataColSingleChangeEvent
 * 
 * @param {} rowIndices 行索引数组
 * @param {} colIndex	列索引
 * @param {} values		值数组
 */
Dataset.prototype.setValuesAt = function(rowIndices, colIndex, values) {
	var rd = this.getCurrentRowData();
	if(rd != null){
		var event = new DataColSingleChangeEvent();
		event.cellColIndex = colIndex;
		event.datasetId = this.id;
		for (var i = 0; i<rowIndices.length; i++){
			var row = this.getRow(rowIndices[i]);
			var oldValue = row.setCellValue(colIndex, values[i]);
			oldValue = oldValue?oldValue:" ";
			if (oldValue != values[i]){
				event.currentRows.push(row);
				event.cellRowIndices.push(rowIndices[i]);
				event.currentValues.push(values[i]);
				event.oldValues.push(oldValue[i]);
			}
		}
		if (!this.silent) {
			this.dispatchEvent(event);
			//  校验cell数据
//			dataset.checkDatasetCell(value, colIndex, row);
			// 对外暴露函数
			this.onAfterDataChange(event);
		}
	}
	
};

/**
 * 设值
 */
Dataset.prototype.setValueAtByKey = function(rowIndex, key, value, withTrigger) {
	var colIndex = this.nameToIndex(key);
	this.setValueAt(rowIndex, colIndex, value, withTrigger);
};

/**
 * 获取row,col所对应元素的
 * 
 * @param rowIndex 行索引
 * @param colIndex 列索引
 * @param value 新值
 */
Dataset.prototype.getValueAt = function(rowIndex, colIndex) {
	var row = this.getRow(rowIndex);
	if (row != null)
		return row == null ? null : row.getCellValue(colIndex);
};

/**
 * 用新的Record更新指定
 * 
 * @param rowIndex 当前页行
 * @param row 行数(Record)
 */
Dataset.prototype.setRow = function(rowIndex, row) {
	//	var count = this.getRowCount();
	// if(rowIndex < 0 || rowIndex >= count)
	// {
	//		
	// }
	alert("now implemented");
};

/**
 * 增加一空行。普通的增行逻辑都应该调用此函数而非addRow(row)。因为此函数会进行默认值处理 新增的行中已经具有前台运算的唯一ID。
 * 
 * @return 新增
 */
Dataset.prototype.addEmptyRow = function() {
	return this.insertEmptyRow(this.getRowCount());
};

/**
 * 插入一空行。普通的增行逻辑都应该调用此函数而非isnertRow(rowIndex, row)。因为此函数会进行默认值处理
 * 新增的行中已经具有前台运算的唯一ID
 * 
 * @return 新增
 */
Dataset.prototype.insertEmptyRow = function(rowIndex) {
	if (rowIndex == null) {
		alert("rowIndex is null in function insertEmptyRow");
		return;
	}
	var row = this.getEmptyRow();
	this.insertRow(rowIndex, row);
	return row;
};

/**
 * 获取一个空行
 */
Dataset.prototype.getEmptyRow = function() {
	this.randomRowIndex++;
	var row = new DatasetRow(null, this.metadata.length);
	var rowCount = this.getRowCount(), lastRow = null;
	for ( var i = 0, count = this.metadata.length; i < count; i++) {
		var defValue = this.metadata[i].dftValue;
		if (defValue != null)
			defValue = decodeURIComponent(defValue);
		row.setCellValue(i, defValue);
		if (this.metadata[i].isLock == true && rowCount > 0) {
			if (lastRow == null)
				lastRow = this.getRow(rowCount - 1);
			row.setCellValue(i, lastRow.getCellValue(i));
		}
	}
	//设置需要触发事
	row.triggerRow = true;
	return row;
};

/**
 * 在当前显示数据页末尾添加一新行 <b>注意，因为插入行有用户控制，不对出入参数进行默认值处理</b>
 * 
 * @param row 行数(Record)
 */
Dataset.prototype.addRow = function(row) {
	return this.insertRow(this.getRowCount(), row);
};

/**
 * 当前页指定位置插入一新行 <b>注意，因为插入行有用户控制，不对出入参数进行默认值处理</b>
 * 
 * @param rowIndex 当前页行
 * @param row 行数(Record)
 */
Dataset.prototype.insertRow = function(rowIndex, row) {
	return this.insertRows(rowIndex, [ row ]);
};

/**
 * 在制定位置插入多行
 */
Dataset.prototype.insertRows = function(rowIndex, rows) {
	if (this.onBeforeRowInsert(rowIndex, rows) == false)
		return;
	if (rows != null) {
		var oldRows = this.getRows();
		if (oldRows == null) {
			alert("the current data block is not initialized, key is:"
					+ this.currentKey);
			return;
		}
		for ( var i = 0; i < rows.length; i++) {

			rows[i].state = DatasetRow.STATE_NEW;
			oldRows.splice(rowIndex + i, 0, rows[i]);
		}
	}
	var event = new RowInsertEvent();
	event.insertedRows = rows;
	event.insertedIndex = rowIndex;
	if (!this.silent) {
		this.dispatchEvent(event);
		this.onAfterRowInsert(event);
	}

	if (rows != null) {
		for ( var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (row.triggerRow == null)
				continue;
			row.triggerRow = null;
			var rowSize = row.getSize();
			for ( var j = 0; j < rowSize; j++) {
				var cellValue = row.getCellValue(j);
				if (cellValue == null)
					continue;
				var triggerRef = false;
				if (this.metadata[j].sourceRefField != null) {
					if (row.getCellValue(j) != null
							&& row.getCellValue(j - 1) == null)
						triggerRef = true;
				}
				//之所以先设置成空，再设置成当前值，是为了触发一次Datachange事件
				row.setCellValue(j, null);
				this.setValueAt(rowIndex + i, j, cellValue, triggerRef);
			}
		}
	}
	return rowIndex;
};

/**
 * 删除行数据
 */
Dataset.prototype.removeRow = function(index) {
	this.removeRows( [ index ]);
};

/**
 * 删除索引指定的多行
 */
Dataset.prototype.removeRows = function(rowIndices) {
	this.getCurrentRowData().removeRows(rowIndices);
};

/**
 * 获取元数据信息
 */
Dataset.prototype.getMetaData = function() {
	return this.metadata;
};

/**
 * 获取主键字段
 */
Dataset.prototype.getPrimaryKeyField = function() {
	for ( var i = 0; i < this.metadata.length; i++) {
		if (this.metadata[i].isPrimaryKey)
			return this.metadata[i];
	}
};

/**
 * 得到key所在的列的索引
 */
Dataset.prototype.nameToIndex = function(key) {
	for ( var i = 0; i < this.metadata.length; i++) {
		if (this.metadata[i].key == key)
			return i;
	}
	return -1;
};

/**
 * 根据名称获取字段索引
 */
Dataset.prototype.nameToIndices = function(keys) {
	var indicesArr = new Array(keys.length);
	for ( var i = 0; i < keys.length; i++) {
		indicesArr[i] = this.nameToIndex(keys[i]);
		if (indicesArr[i] == -1) {
			alert(keys[i] + "在Dataset中没有正确对");
			return null;
		}
	}
	return indicesArr;
};

/**
 * 获取当前选中行一个key所对应的值
 * 
 * @param key Dataset列键
 * @return 当前选中行所对应的列值。如果允许选中多行，则为第一个选中行的列没有选中行返回null
 */
Dataset.prototype.getValue = function(key) {
	var currRow = this.getSelectedRow();
	if (currRow == null)
		return null;
	return currRow.getCellValue(this.nameToIndex(key));
};

/**
 * 设置当前选中行key所对应的<b>注意</b>：此设置会出发DataChangeEvent
 * 
 * @param key Dataset列键
 * @param value 待设置
 */
Dataset.prototype.setValue = function(key, value) {
	var rowIndex = this.getSelectedIndex();
	if (rowIndex == -1) {
		log("no selected row when call dataset.setValue, key is:" + key);
		return;
	}
	var colIndex = this.nameToIndex(key);
	this.setValueAt(rowIndex, colIndex, value);
};

/**
 * 校验cell数据，校验不合法派发DataCheckEvent
 */
Dataset.prototype.checkDatasetCell = function(value, colIndex, row) {
	var resultStr = checkDatasetCell(this, value, colIndex, row);
	if (resultStr != null && typeof resultStr == "string") {
		// 派发校验事件
		var event = new DataCheckEvent();
		event.cellColIndices = [ colIndex ];
		event.currentRow = row;
		event.rulesDescribe = [ resultStr ];
		if (!this.silent)
			this.dispatchEvent(event);
	}
};

/**
 * 校验行数据，校验不合法派发DataCheckEvent
 */
Dataset.prototype.checkDatasetRow = function(row) {
	var resultArray = checkDatasetRow(this, row);
	if (resultArray != null && resultArray.length > 0) {
		// 解析哪些列校验没有通过
		var cellColIndiceAry = [];
		var rulesAry = [];
		var temp = [];
		for ( var i = 0, count = resultArray.length; i < count; i++) {
			temp = resultArray[i].split(";");
			cellColIndiceAry.push(temp[0]);
			rulesAry.push(temp[1]);
		}

		var event = new DataCheckEvent();
		event.cellColIndices = cellColIndiceAry;
		event.currentRow = row;
		event.rulesDescribe = rulesAry;
		if (!this.silent)
			this.dispatchEvent(event);
	}
};

DatasetRow.STATE_NRM = 0;
DatasetRow.STATE_UPD = 1;
DatasetRow.STATE_NEW = 2;
DatasetRow.STATE_DEL = 3;
DatasetRow.STATE_FALSE_DEL = 4;


DatasetRow.CHANGEED = "ch";

/**
 * Dataset行数据
 * @class Dataset行数据，可来自后台生成的dom元素，也可手工创建出来 <b>手工创建出来的Row已经带有当前Dataset中的唯一ID。</b>
 * @constructor
 * @param ele 对应“数据岛”中的一个element。行数据将从此element中解析获得。如果为空，则表示新建空行。
 * @author dengjt
 */
function DatasetRow(ele, rowCount, dataset) {
	//ele为空一般表示手工NEW出来的行
	if (ele != null) {
		this.domElement = ele;
		this.rowId = this.domElement.getAttribute("id");
		this.dataEle = this.domElement.firstChild;
		for (var i = 1; i < this.domElement.childNodes.length; i++){
			if (this.domElement.childNodes[i].tagName == DatasetRow.CHANGEED){
				this.changedEle = this.domElement.childNodes[i];
				break;
			}
		}
		this.dataset = dataset;
		this.initialize();
		delete this.dataEle;
	} else {
		this.rowId = Dataset.getRandomRowId();
		if (rowCount != null)
			this.dataArr = new Array(rowCount);
		else
			this.dataArr = new Array();
		this.state = DatasetRow.STATE_NEW;
	}
};

/**
 * 获得此行指定位置单元
 * 
 * @param index
 * @return 单元
 */
DatasetRow.prototype.getCellValue = function(index) {
	return this.dataArr[index];
};

/**
 * 获取状态
 */
DatasetRow.prototype.getState = function() {
	return this.state;
};

/**
 * 设置状态
 */
DatasetRow.prototype.setState = function(state) {
	this.state = state;
};

/**
 * 初始化
 */
DatasetRow.prototype.initialize = function() {
	if (IS_IE)
		this.dataArr = this.dataEle.text.split(",");
	else
		this.dataArr = this.dataEle.textContent.split(",");
	var metadata = this.dataset.metadata;
	var length = this.dataArr.length;
	// 解码字符串值。此处是关键性能点，减少方法调用
	for ( var i = 0; i < length; i++) {
		//还原null
		if (this.dataArr[i] == EventContextConstant.NULL) {
			this.dataArr[i] = null;
		} else{
			this.dataArr[i] = decodeURIComponent(this.dataArr[i]);
			//dingrf 把日期时间的long转化为yyyy-MM-dd HH:mi:ss
			if (metadata[i] != null ){
				if (metadata[i].dataType == DataType.UFDATETIME || 
	//				metadata[i].dataType == DataType.UFDATE ||
					metadata[i].dataType == DataType.UFTIME){
						//判断this.dataArr[i]中的日期时间是否为lang类型	
						if (parseInt(this.dataArr[i]) != this.dataArr[i]) continue;	
						var date = new Date();
						date.setTime(this.dataArr[i]);
						this.dataArr[i] = this.dateTimeFormat(date); 
				}
				else if (metadata[i].dataType == DataType.UFDATE ||
					metadata[i].dataType == DataType.UFLITERALDATE ||
					metadata[i].dataType == DataType.UFDATEBEGIN ||
					metadata[i].dataType == DataType.UFDATEEND){
						//判断this.dataArr[i]中的日期时间是否为lang类型	
						if (parseInt(this.dataArr[i]) != this.dataArr[i]) continue;	
						var date = new Date();
						date.setTime(this.dataArr[i]);
						this.dataArr[i] = this.dateFormat(date); 
				}
			}
		}
	}
	this.state = this.dataEle.nodeName;
	if (this.state == "nrm")
		this.state = DatasetRow.STATE_NRM;
	else if (this.state == "upd")
		this.state = DatasetRow.STATE_UPD;
	else if (this.state == "add")
		this.state = DatasetRow.STATE_NEW;
	else if (this.state == "del")
		this.state = DatasetRow.STATE_DEL;
	else if (this.state == "fdel")
		this.state = DatasetRow.STATE_FALSE_DEL;
	if (this.changedEle){
		if (IS_IE)
			this.changedArr = this.changedEle.text.split(",");
		else
			this.changedArr = this.changedEle.textContent.split(",");
	}	
};

/**
 * 把date类型格式化成yyyy-MM-dd HH:Mi:SS
 * @param {date} datetime
 * @return {String}   YYYY-MM-DD HH:Mi:SS
 * private
 */
DatasetRow.prototype.dateTimeFormat = function(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if (parseInt(month)<10) month = "0" + month;
	var day = date.getDate();
	if (parseInt(day)<10) day = "0" + day;
	var hours = date.getHours();
	if (parseInt(hours)<10) hours = "0" + hours;
	var minutes = date.getMinutes();
	if (parseInt(minutes)<10) minutes = "0" + minutes;
	var seconds = date.getSeconds();
	if (parseInt(seconds)<10) seconds = "0" + seconds;
	var formatString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
	return formatString;
};

/**
 * 把date类型格式化成yyyy-MM-dd
 * @param {date} date
 * @return {String}   YYYY-MM-DD
 * private
 */
DatasetRow.prototype.dateFormat = function(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if (parseInt(month)<10) month = "0" + month;
	var day = date.getDate();
	if (parseInt(day)<10) day = "0" + day;
	var formatString = year + "-" + month + "-" + day;
	return formatString;
};

/**
 * 设置单元值 <b>注意：设置此单元值不会出发更新事件。可通过dataset.setValueAt进行修改且出发事件</b>
 * 
 * @param index{int} 指定列索引
 * @param value 新值
 */
DatasetRow.prototype.setCellValue = function(index, value) {
	var oldValue = this.dataArr[index];
	// 确保为字符串
	if (value != null)
		this.dataArr[index] = value + "";
	else
		this.dataArr[index] = null;
	if (this.state != DatasetRow.STATE_NEW)
		this.state = DatasetRow.STATE_UPD;
	return oldValue;
};

/**
 * 获得行cell大小
 * 
 * @return {int} cell
 */
DatasetRow.prototype.getSize = function() {
	return this.dataArr.length;
};

/**
 * 行复制方 <b>注意，由此clone的新行，将具有新的rowId。行状态为New。即，仅仅是数据保持和源行相/b>
 * 
 * @return 克隆后的
 * 
 * @private
 */
DatasetRow.prototype.clone = function() {
	var row = new DatasetRow();
	for ( var i = 0; i < this.dataArr.length; i++)
		row.setCellValue(i, this.dataArr[i]);
	return row;
};

/**
 * 将行数据打包成xml<record></record>部分
 * 
 * @see webframe设计文档的交互协议篇
 * @return {String} xml
 * @private
 */
DatasetRow.prototype.toXml = function() {
	var prefix, postfix;
	if (this.state == DatasetRow.STATE_NEW) {
		prefix = "<add>";
		postfix = "</add>";
	} 
	else if (this.state == DatasetRow.STATE_DEL) {
		prefix = "<del>";
		postfix = "</del>";
	} 
	else if (this.state == DatasetRow.STATE_UPD) {
		prefix = "<upd>";
		postfix = "</upd>";
	}
	else if (this.state == DatasetRow.STATE_FALSE_DEL){
		prefix = "<fdel>";
		postfix = "</fdel>";
	}
	else {
		prefix = "<nrm>";
		postfix = "</nrm>";
	}
	var str = "<" + EventContextConstant.record + " id='" + this.rowId + "'>" + prefix + "";
	str += this.contentToXml();
	str += postfix + "</" + EventContextConstant.record + ">";
	return str;
};

/**
 * 单独对内容进行序列化处理,如果传入strArr，则将内容直接加入array，否则返回
 * @private
 */
DatasetRow.prototype.contentToXml = function(strArr) {
	var arr = null;
	if(strArr != null)
		arr = strArr;
	else
		arr = new Array;
	var length = this.dataArr.length;
	for ( var i = 0; i < length; i++) {
		var temp;
		if (this.dataArr[i] != null) {
			temp = this.dataArr[i];
			//dingrf 处理日期时间
			if (this.dataset != null && this.dataset.metadata[i] != null && (this.dataset.metadata[i].dataType == DataType.UFDATETIME || 
				this.dataset.metadata[i].dataType == DataType.UFDATE ||
				this.dataset.metadata[i].dataType == DataType.UFLITERALDATE ||
				this.dataset.metadata[i].dataType == DataType.UFDATEBEGIN ||
				this.dataset.metadata[i].dataType == DataType.UFDATEEND ||
				this.dataset.metadata[i].dataType == DataType.UFTIME)){
				temp = this.dateToUTCString(temp);
			}
			arr.push(encodeURIComponent(temp));
		} else {
			temp = EventContextConstant.NULL;
			arr.push(temp);
		}
		if (i != length - 1)
			arr.push(",");
	}
	return strArr ? null : arr.join("");
};

/**
 * 取得date对应的协调世界时
 * @param {String} date
 * private
 */
DatasetRow.prototype.dateToUTCString = function(date) {
	if (date.indexOf("-") > -1)
		date = date.replace(/\-/g,"/");
	var utcString = Date.parse(date);
	if (isNaN(utcString)) return ""; 
	return utcString; 	
};

/**
 * 重写toString，便于调试。使用时可直接alert(row)来完成调用。
 * 
 * @return {String} 行的完整信息
 * @private
 */
DatasetRow.prototype.toString = function() {
	return "RowId is:" + this.rowId + " and state is:" + this.state
			+ " and data is:" + this.dataArr;
};

// DatasetRow.maskPhase = function(str){
// if(str == null || str == "")
// return str;
// str = str.replaceAll("&", "&amp;");
// str = str.replaceAll("<", "&lt;");
// str = str.replaceAll(">", "&gt;");
// return str;
// };

/**
 * 根据制定列进行合计。只合计指定页。
 */
Dataset.prototype.totalSum = function(cols, keyValue, index, precision,
		rowFilter) {
	var resultArr = new Array(cols.length);
	var rows = this.getRows(index, keyValue);
	if (rows != null && rows.length > 0) {
		var indicesArr = this.nameToIndices(cols);
		for ( var i = 0; i < rows.length; i++) {
			for ( var j = 0; j < indicesArr.length; j++) {
				if (resultArr[j] == null)
					resultArr[j] = 0;
				var v = null;
				if (rowFilter) {
					if (rowFilter(rows[i])) {
						v = rows[i].getCellValue(indicesArr[j]);
					}
				} else
					v = rows[i].getCellValue(indicesArr[j]);
				if (v != null && v != "") {
					var dataType = this.metadata[indicesArr[j]].dataType;
					if (dataType == DataType.INTEGER
							|| dataType == DataType.INT)
						resultArr[j] += parseInt(v);
					else
						resultArr[j] += parseFloat(v);
				}
			}
		}
		if (precision != null) {
			precision = parseInt(precision);
			for ( var i = 0; i < resultArr.length; i++) {
				resultArr[i] = resultArr[i].toFixed(precision);
			}
		}
	}
	return resultArr;
};

/**
 * Dataset空行
 * @class Dataset空行
 * @param rowId
 * @return
 */
function EmptyDsRow(rowId) {
	this.rowId = rowId;
	this.empty = true;
};

/**
 * @private 转换Node列表到DatasetRow列表。如果传入数据为空，返回空数组而不是null
 * @param node列表
 * @param dataset 数据集
 * @return DatasetRow数组
 * @private
 */
function maskNodesToRecords(nodeList, dataset) {
	if (nodeList == null || nodeList.length == 0)
		return new Array;
	var records = new Array(nodeList.length);
	for ( var i = 0; i < nodeList.length; i++) {
		var node = nodeList[i];
		if (node.nodeName == EventContextConstant.record)
			records[i] = new DatasetRow(node, null, dataset);
		else {
			var rowId = node.getAttribute("id");
			records[i] = new EmptyDsRow(rowId);
		}
	}
	return records;
};

/**
 * @private
 */
function defaultRowSorter(row1, row2) {
	var keys = this.tempKeys;
	var orders = this.tempOrders;
	var ds = this.tempDs;
	return 0;
};

/**
 * @private
 */
function defaultIntSort(value1, value2) {
	var intV1 = parseInt(value1);
	var intV2 = parseInt(value2);
	if (intV1 < intV2)
		return -1;
	else if (intV1 == intV2)
		return 0;
	return 1;
};

/**
 * RowSet构造函数
 * @class
 * <p>
 * DsContent是Dataset内容块的实际载体。它的结构如下：<br>
 * 每个DsContent的currPage代表了当前数据块的显示页码。<br>
 * pageCount代表当前数据块数据量。<br> recordArr是按页存储的数据数组。<br>
 * deletedRows是当前数据块中被删除的行，因为在后面可能需要再访问，所以单独使用一个数组进行临时存储。<br>
 * selectedIndices是当前选中行索引。注意，它只记录当前页中的选中行。<br>
 * DsContent中的domArr的来源需看Dataset的设计，Dataset的具体数据是来自于网页中的“数据岛”，即如下格式的网页数据：<br>
 * &lt;xml id="bd_invcl"&gt;<br>
 * &lt;data&gt;<br>
 * &lt;res-parameters&gt;<br>
 * &lt;parameter name="pagecount"&gt;-1&lt;/parameter&gt;<br>
 * &lt;/res-parameters&gt;<br>
 * &lt;records pageindex="0"&gt;<br>
 * &lt;record id="0bd_invcl309031006"&gt;<br>
 * &lt;nrm&gt;1051AA1000000000000Q,1051,mk,mk,1,,,&lt;/nrm&gt;&lt;/record&gt;<br>
 * &lt;record id="0bd_invcl1874957385"&gt;<br>
 * &lt;nrm&gt;1006AA1000000000CXI8,1006,%E8%B4%B9%E7%94%A8,09,1,,,&lt;/nrm&gt;&lt;/record&gt;<br>
 * &lt;/data&gt;<br>
 * &lt;/xml&gt;<br>
 * Dataset在初始化的时候，并没有进行相应的解析，而只是进行了数据岛的引用，将各个岛的xml的根节点存在相应的块中，这就是domArr。这样做，避免了一次性解析所有数据带来的性能问题。domArr只在解析的时候使用一次。（数据岛是IE中独有的，webframe中通过一些代码是它能够在firefox等浏览器中得到同样的支持）
 * 
 * @constructor 数据体内
 * @author dengjt
 * @param rootNode dom根节
 * @param pageCount 分页数目
 * @param currPage 当前
 */
function RowSet(rootNode, dataset) {
	this.pagecount = 1;
	this.pageindex = 0;
	this.allrowcount = 0;
	this.pagesize = -1;
	this.keyvalue = null;
	this.dataset = dataset;

	// 将当前页Root节点放在按页索引的数组中
	// this.domArr = new Array();
	// 放置指定页的记录数组
	this.rowDatas = new Array();

	if (rootNode != null)
		this.setData(rootNode);
	else {
		var rd = new RowData(null, this);
		this.rowDatas[0] = rd;
	}
};

/**
 * 获取页数
 */
RowSet.prototype.getPageCount = function() {
	return this.pagecount;
};

/**
 * 获取页索引
 */
RowSet.prototype.getPageIndex = function() {
	return this.pageindex;
};

/**
 * 获取所有行数
 */
RowSet.prototype.getAllRowCount = function() {
	return this.allrowcount;
};

/**
 * 设置数据
 */
RowSet.prototype.setData = function(ele) {
	var pageindex = ele.getAttribute("pageindex");
	var pagecount = ele.getAttribute("pagecount");
	var pagesize = ele.getAttribute("pagesize");
	var recordcount = ele.getAttribute("recordcount");
	this.pageindex = parseInt(pageindex);
	this.pagecount = parseInt(pagecount);
	this.allrowcount = parseInt(recordcount);
	this.pagesize = parseInt(pagesize);
	this.keyvalue = ele.getAttribute("keyvalue");
	var recordsList = ele.selectNodes(EventContextConstant.records);
	if (recordsList.length > 0) {
		for ( var i = 0; i < recordsList.length; i++) {
			var records = recordsList[i];
			var pIndex = records.getAttribute("pageindex");
			var rowData = this.rowDatas[parseInt(pIndex)];
			if (rowData != null) {
				rowData.setData(records);
			} else {
				rowData = new RowData(records, this);
				this.rowDatas[parseInt(pIndex)] = rowData;
				rowData.setData(records, true);
			}
			if (this.allrowcount == 0)
				this.allrowcount = rowData.getRowCount();
		}
	}
};

/**
 * 设置JSON形式数据
 */
RowSet.prototype.setJsonData = function(jsonData, keyValue, pageindex) {
	if (pageindex == null)
		this.pageindex = 0;
	else
		this.pageindex = pageindex;
	var allrowcount = jsonData.total;
	var size = jsonData.size;
	var pagecount = 1;
	if(size != -1){
		if (allrowcount != 0 && allrowcount % size == 0)
			pagecount = Math.floor(allrowcount / size);
		else
			pagecount = Math.floor((allrowcount / size) + 1);
	}

	this.pagecount = pagecount;
	this.allrowcount = allrowcount;
	this.pagesize = size;
	this.keyvalue = keyValue;
	var rowData = this.rowDatas[parseInt(pageindex)];
	if (rowData == null) {
		rowData = new RowData(null, this);
		this.rowDatas[parseInt(pageindex)] = rowData;
	}
	rowData.setJsonData(jsonData, pageindex);
};

/**
 * 获取当前RowData
 */
RowSet.prototype.getCurrentRowData = function() {
	return this.getRowData(this.pageindex);
};

/**
 * 获取RowData
 */
RowSet.prototype.getRowData = function(pageindex) {
	return this.rowDatas[pageindex];
};

/**
 * 获取所有RowData
 */
RowSet.prototype.getRowDatas = function() {
	return this.rowDatas;
};

/**
 * 获取选中行索引
 */
RowSet.prototype.getSelectedIndex = function() {
	return this.getCurrentRowData().getSelectedIndex();
};

/**
 * 获取所有选中行索引
 */
RowSet.prototype.getSelectedIndices = function() {
	return this.getCurrentRowData().getSelectedIndices();
};
/**
 * 获取当前选中页中的选中行
 */
RowSet.prototype.getSelectedRows = function() {
	return this.getCurrentRowData().getSelectedRows();
};

/**
 * 获取当前选中页中的选中行
 */
RowSet.prototype.getSelectedRow = function() {
	var rows = this.getSelectedRows();
	if (rows == null)
		return null;
	return rows[0];
};

/**
 * 获取所有页中的选中行
 */
RowSet.prototype.getAllSelectedRows = function() {

};

/**
 * 获得指定页对应的指定行
 * 
 * @param index 行索
 * @param pageIndex 页索可以为空
 */
RowSet.prototype.getRow = function(index) {
	if (this.rowDatas[this.pageindex] == null)
		return null;
	return this.rowDatas[this.pageindex].getRows()[index];
};

/**
 * 获取指定页的数据
 */
RowSet.prototype.getRows = function() {
	if (this.rowDatas[this.pageindex] == null)
		return null;
	return this.rowDatas[this.pageindex].getRows();
};

/**
 * 获取所有行
 */
RowSet.prototype.getAllRows = function() {
	var rowArr = new Array;
	if (this.rowDatas == null)
		return rowArr;
	for ( var i = 0; i < this.rowDatas.length; i++) {
		var rows = this.rowDatas[i].getRows();
		if (rows != null) {
			for ( var j = 0; j < rows.length; j++) {
				rowArr.push(rows[j]);
			}
		}
	}
	return rowArr;
};

/**
 * 获取指定范围内的行
 * @param startIndex 开始索引
 * @param count 查询数量
 */
RowSet.prototype.getRowsByScale = function(startIndex, count) {
	if (this.rowDatas[this.pageindex] == null)
		return null;
	return this.rowDatas[this.pageindex].getRowsByScale();
};

/**
 * 获取指定页的数据
 * 
 * @param pageIndex,如果1,则表示获取当前所有行
 */
RowSet.prototype.getRowCount = function() {
	if (this.rowDatas[this.pageindex] == null)
		return null;
	return this.rowDatas[this.pageindex].getRowCount();
};

/**
 * RowData构造函数
 * @class RowData类
 * @param ele
 * @param rowSet
 * @return
 */
function RowData(ele, rowSet) {
	if (ele != null)
		this.pageindex = parseInt(ele.getAttribute("pageindex"));
	else
		this.pageindex = 0;
	this.rows = new Array;
	this.deleteRows = null;
	this.selectedIndices = null;
	this.rowSet = rowSet;
};

/**
 * 设置JSON形式数据
 */
RowData.prototype.setJsonData = function(jsonData, pageindex) {
	this.pageindex = pageindex;
	var ds = this.rowSet.dataset;
	var keyIndexObj = new Object;
	for ( var j = 0; j < jsonData.meta.length; j++) {
		var jsonKey = jsonData.meta[j].toLowerCase();
		for ( var i = 0; i < ds.metadata.length; i++) {
			if (jsonKey == ds.metadata[i].key) {
				keyIndexObj[jsonData.meta[j]] = i;
				break;
			}
		}
	}

	delete this.rows;
	this.rows = [];

	for ( var i = 0; i < jsonData.data.length; i++) {
		var row = new DatasetRow(null, ds.metadata.length);
		row.rowData = this;

		var jsonRow = jsonData.data[i];
		for ( var j = 0; j < jsonData.meta.length; j++) {
			row.dataArr[keyIndexObj[jsonData.meta[j]]] = jsonRow[j];
		}
		this.rows[i] = row;
	}

	if (this.rowSet.keyvalue == ds.currentKey) {
		var pevent = new PageChangeEvent();
		pevent.pageIndex = this.pageindex;
		pevent.parentKey = ds.currentKey;
		ds.dispatchEvent(pevent);
	}
};

/**
 * 设置数据
 */
RowData.prototype.setData = function(recordsNode, isInit) {
	var isCurrent = (this.rowSet.dataset.currentKey == this.rowSet.keyvalue)
			&& (this.rowSet.pageindex == this.pageindex);
	var selChanged = false;
	var selectNode = recordsNode.selectSingleNode("selected");
	var records = maskNodesToRecords(recordsNode.selectNodes(EventContextConstant.record + "|" + EventContextConstant.erecord), this.rowSet.dataset);
	if (selectNode != null) {
		var indicesStr = getNodeValue(selectNode);
		var indices = null;
		if (indicesStr != null) {
			indices = indicesStr.split(",");
			for ( var i = 0; i < indices.length; i++) {
				indices[i] = parseInt(indices[i]);
			}
		}
		if (isCurrent) {
			if (indices != null && this.selectedIndices == null)
				selChanged = true;
			else if (this.selectedIndices != null && indices == null)
				selChanged = true;
			else if (this.selectedIndices.length != indices.length)
				selChanged = true;
			else if (records.length != this.rows.length) {
				selChanged = true;
			} else {
				for ( var i = 0; i < indices.length; i++) {
					//||indices[i]==0
					if (indices[i] != this.selectedIndices[i]) {
						selChanged = true;
						break;
					}
				}
			}
			if (selChanged) {
				if (this.selectedIndices != null) {
					for ( var i = this.selectedIndices.length - 1; i >= 0; i--) {
						this.setRowUnSelected(this.selectedIndices[i]);
					}
				}
			}
		}
	}

	var changed = recordsNode.getAttribute("changed");
	if (changed == "true" || isInit) {
		delete this.rows;
		delete this.deleteRows;
		delete this.selectedIndices;

		// var records = maskNodesToRecords(recordsNode.selectNodes(EventContextConstant.record));
		this.rows = records;
		for ( var i = 0; i < this.rows.length; i++)
			this.rows[i].rowData = this;

		if (isCurrent) {
			var pevent = new PageChangeEvent();
			pevent.pageIndex = this.pageindex;
			pevent.parentKey = this.rowSet.dataset.currentKey;
			this.rowSet.dataset.dispatchEvent(pevent);
			// 换页之后，重新触发选中
			selChanged = true;
		}
	} else {
		var oldRows = this.getRows();
		var nindex = 0;
		for ( var i = 0; i < records.length; i++) {
			var find = false;
			for ( var j = nindex; j < oldRows.length; j++) {
				if (oldRows[j].rowId == records[i].rowId) {
					this.$updateRow(j, oldRows[j], records[i]);
					nindex++;
					find = true;
					break;
				}
			}
			if (!find) {
				this.insertRow(i, records[i]);
			} 
			//else
			//	records[i].state = DatasetRow.STATE_NRM;
		}
		var deleteNodes = recordsNode.selectNodes(EventContextConstant.drecord);
		if (deleteNodes != null) {
			for ( var i = 0; i < deleteNodes.length; i++) {
				var delNode = deleteNodes[i];
				var id = getNodeAttribute(delNode, "id");
				var index = this.getRowIndexById(id);
				this.removeRow(index);
			}
		}
	}

	if (isCurrent) {
		if (indices != null && selChanged) {
			for ( var i = 0; i < indices.length; i++) {
				this.rowSet.dataset.addRowSelected(indices[i]);
			}
		}
	} else {
		this.selectedIndices = indices;
	}
	//find record
	// if find ,update
	// else insert
	// delete
	// select
};

/**
 * 增加行
 */
RowData.prototype.addRow = function(row) {
	this.insertRow(this.getRowCount(), row);
};

/**
 * 插入行
 */
RowData.prototype.insertRow = function(index, row) {
	this.insertRows(index, [ row ]);
};

/**
 * 设值
 */
RowData.prototype.setValueAt = function(rowIndex, colIndex, value) {
	var row = this.getRow(rowIndex);
	var oldValue = row.setCellValue(colIndex, value);
	if (value == oldValue)
		return;
	var dataset = this.rowSet.dataset;
	// if(row != null && colIndex >=0 && colIndex < row.getSize())
	// {
	// //对外暴露函数
	// if(this.onBeforeDataChange(rowIndex, colIndex, value, dataset) == false)
	// return;
	// var oldValue = row.setCellValue(colIndex, value);
	var event = new DataChangeEvent();
	event.currentRow = row;
	event.cellRowIndex = rowIndex;
	event.cellColIndex = colIndex;
	event.currentValue = value;
	event.oldValue = oldValue;
	event.datasetId = dataset.id;
	if (!dataset.silent) {
		dataset.dispatchEvent(event);
		// 处理参照编辑公式
		// if(withTrigger == null || withTrigger)
		// this.processRefEditFormula(rowIndex, colIndex, value, withTrigger);
		
		//  校验cell数据
		dataset.checkDatasetCell(value, colIndex, row);
		
		// 对外暴露函数
		dataset.onAfterDataChange(event);
	}

	// }
	// else
	// {
	// log("the row assigned is empty in func setValueAt,rowIndex=" + rowIndex);
	// }
};

/**
 * 插入多行
 */
RowData.prototype.insertRows = function(index, rows) {
	var oldRows = this.getRows();
	if (index > oldRows.length)
		index = oldRows.length;

	var dataset = this.rowSet.dataset;
	if (dataset.onBeforeRowInsert(index, rows) == false)
		return;
	if (rows != null) {
		if (oldRows == null) {
			alert("the current data block is not initialized, key is:"
					+ this.currentKey);
			return;
		}
		for ( var i = 0; i < rows.length; i++) {
			rows[i].state = DatasetRow.STATE_NEW;
			oldRows.splice(index + i, 0, rows[i]);
		}
	}
	var event = new RowInsertEvent();
	event.insertedRows = rows;
	event.insertedIndex = index;
	if (!dataset.silent) {
		dataset.dispatchEvent(event);
		dataset.onAfterRowInsert(event);
	}

	//	// 默认选中
	// if(selected == null || selected == true){
	// this.setRowSelected(rowIndex);
	// }
	// if(rows != null){
	// for(var i = 0; i < rows.length; i ++){
	// var row = rows[i];
	// if(row.triggerRow == null)
	// continue;
	// row.triggerRow = null;
	// var rowSize = row.getSize();
	// for(var j = 0; j < rowSize; j ++){
	// var cellValue = row.getCellValue(j);
	// if(cellValue == null)
	// continue;
	// var triggerRef = false;
	// if(this.metadata[j].sourceRefField != null){
	// if(row.getCellValue(j) != null && row.getCellValue(j - 1) == null)
	// triggerRef = true;
	// }
	// //之所以先设置成空，再设置成当前值，是为了触发一次Datachange事件
	// row.setCellValue(j, null);
	// this.setValueAt(rowIndex + i, j, cellValue, triggerRef);
	// }
	// }
	// }
	// return rowIndex;

};

/**
 * 删除行
 */
RowData.prototype.removeRow = function(index) {
	this.removeRows( [ index ]);
};

/**
 * 删除索引指定的多行
 * 
 */
RowData.prototype.removeRows = function(rowIndices) {
	var oldRows = this.getRows();
	var delAll = false;
	// 排序
	if (rowIndices == -1) {
		rowIndices = new Array;
		delAll = true;
		var count = this.getRowCount();
		for ( var i = 0; i < count; i++)
			rowIndices.push(i);
	} else {
		rowIndices = rowIndices.sort(defaultIntSort);
	}

	// 执行删除前的方法(阻止进一步执行需要抛出异
	this.rowSet.dataset.onBeforeRowDelete(rowIndices);

	var tmpRows = new Array;
	var selIndices = this.getSelectedIndices();
	for ( var i = rowIndices.length - 1; i >= 0; i--) {
		removeFromArray(selIndices, rowIndices[i]);
		// 删除行之后，大于此行的索引都应该减一
		if (selIndices != null) {
			for ( var j = 0; j < selIndices.length; j++) {
				if (selIndices[j] > rowIndices[i])
					selIndices[j]--;
			}
		}
		var delRow = oldRows[rowIndices[i]];
		if (delRow == null) {
			continue;
		}
		oldRows.splice(rowIndices[i], 1);
		// 如果是新增行不放入删除行列表永久删除)
		if (delRow.state != DatasetRow.STATE_NEW) {
			if (this.deletedRows == null)
				this.deletedRows = new Array;
			this.deletedRows.push(delRow);
		}
		tmpRows.push(delRow);
	}

	if (!this.rowSet.dataset.silent) {
		var event = new RowDeleteEvent();
		event.deletedRows = tmpRows;
		event.deletedIndices = rowIndices;
		event.deleteAll = delAll;
		this.rowSet.dataset.dispatchEvent(event);
		this.rowSet.dataset.onAfterRowDelete(event);
	}
};

/**
 * 更新行
 * @private
 */
RowData.prototype.$updateRow = function(index, oldRow, newRow) {
	if (newRow.empty)
		return;
	var ds = this.rowSet.dataset;
	var mds = ds.metadata;
	var oldState = oldRow.state;
	if (newRow.changedArr != null && newRow.changedArr.length > 0){
		for (var i = 0; i < newRow.changedArr.length; i++){
			var changedIndex = newRow.changedArr[i];
			var newValue = newRow.getCellValue(changedIndex);
			if(newValue == null)
				newValue = "";
			var oldValue = oldRow.getCellValue(changedIndex);
			if(oldValue == null)
				oldValue = "";
			if (newValue != oldValue) {
				this.setValueAt(index, changedIndex, newValue);
			}
		}
	}
	else{
		for (var i = 0; i < mds.length; i++) {
			var newValue = newRow.getCellValue(i);
			if(newValue == null)
				newValue = "";
			var oldValue = oldRow.getCellValue(i);
			if(oldValue == null)
				oldValue = "";
			if (newValue != oldValue) {
				this.setValueAt(index, i, newValue);
			}
		}
	}
	//如果oldRow的state有发生变化，则不再赋值
	if (oldRow.state == oldState)
		oldRow.state = newRow.state;
	//对假删除行打标记
	if  (newRow.state == DatasetRow.STATE_FALSE_DEL){
		var event = new DataFalseDelEvent();
		event.delRowIndex = index;
		event.delRow = newRow;
		ds.dispatchEvent(event);
		
	}	
		
	// //如果是被动编辑字段，并且值为空，则不进行值设
	// if(newValue == null && this.metadata[currIndex].refField != null)
	// continue;
	// //当前页，走正常更新逻辑
	// if(isCurrent)
	// this.setValueAt(i, currIndex, newValue);
	// else{
	// var event = new DataChangeEvent();
	// event.currentRow = existRows[i];
	// event.cellRowIndex = i;
	// event.cellColIndex = currIndex;
	// event.currentValue = rows[j].getCellValue(currIndex);
	// event.oldValue = existRows[i].getCellValue(currIndex);
	// existRows[i].setCellValue(currIndex, rows[j].getCellValue(currIndex));
	// this.onAfterDataChange(event, pageIndex);
	// }
	// //设置已更新标
	// rows[j].updated = true;
	// matchCount ++;
};

/**
 * 获取行索引
 */
RowData.prototype.getRowIndices = function(rows) {
	var indices = new Array;
	for ( var i = 0; i < rows.length; i++) {
		indices.push(this.getRowIndex(rows[i]));
	}
	return indices;
};

/**
 * 获取行索引
 */
RowData.prototype.getRowIndex = function(row) {
	var rows = this.getRows();
	for ( var i = 0; i < rows.length; i++) {
		if (rows[i].rowId == row.rowId)
			return i;
	}
	return -1;
};

/**
 * 根据ID获取行索引
 */
RowData.prototype.getRowIndexById = function(id) {
	var rows = this.getRows();
	for ( var i = 0; i < rows.length; i++) {
		if (rows[i].rowId == id)
			return i;
	}
	return -1;
};

/**
 * 获取所有行
 */
RowData.prototype.getRows = function() {
	return this.rows;
};

/**
 * 获取指定行
 */
RowData.prototype.getRow = function(index) {
	return this.rows[index];
};

/**
 * 获取指定范围内的行
 * @param startIndex 开始索引
 * @param count 查询数量
 */
RowData.prototype.getRowsByScale = function(start, count) {
	var rows = this.getRows();
	if (rows == null)
		return null;
	if (start >= rows.length)
		return null;
	var rowArr = new Array;
	for ( var i = start; i < count && i < rows.length; i++) {
		rowArr.push(rows[i]);
	}
	return rowArr;
};

/**
 * 行反选 （废弃）
 */
RowData.prototype.setRowUnSelected_ = function(index) {
	var selIndices = this.getSelectedIndices();
	if (selIndices == null)
		return;
	for ( var i = 0; i < selIndices.length; i++) {
		if (selIndices[i] == index) {
			selIndices.splice(i, 1);
			var event = new RowUnSelectEvent();
			event.currentRowIndex = index;
			if (!this.rowSet.dataset.silent) {
				this.rowSet.dataset.dispatchEvent(event);
				this.rowSet.dataset.onAfterRowUnSelect(event);
			}
			//多选时，如果存在已选中行，派发选中事件
			if (selIndices.length > 0){
				var event = new RowSelectEvent();	
				event.isMultiSelect = true;
				var index = selIndices[0];
				event.currentRow = this.getRows()[index];
				event.currentRowIndex = index;
				if (!this.rowSet.dataset.silent) {
					this.rowSet.dataset.dispatchEvent(event);
//					this.rowSet.dataset.onAfterRowSelect(event);
				}
			}
		}
	}
};

/**
 * 行反选 
 */
RowData.prototype.setRowUnSelected = function(indices) {
	var selIndices = this.getSelectedIndices();
	if (selIndices == null)
		return;
	var newUnSelected = false;	
	if (indices instanceof Array){
		var event = new RowUnSelectEvent();
		for ( var i = 0; i < indices.length; i++) {
			var index = selIndices.indexOf(indices[i]);
			if (index != -1) {
				selIndices.splice(index, 1);
				newUnSelected = true;
				var event = new RowUnSelectEvent();
				event.currentRowIndex = i;
				if (!this.rowSet.dataset.silent) {
					this.rowSet.dataset.dispatchEvent(event);
				}
			}	
		}
		if (!this.rowSet.dataset.silent) {
			this.rowSet.dataset.onAfterRowUnSelect(event);
		}
	}
	else{
		var index = selIndices.indexOf(indices);
		if (index != -1) {
			newUnSelected = true;
			selIndices.splice(index, 1);
			var event = new RowUnSelectEvent();
			event.currentRowIndex = indices;
			if (!this.rowSet.dataset.silent) {
				this.rowSet.dataset.dispatchEvent(event);
				this.rowSet.dataset.onAfterRowUnSelect(event);
			}
		}	
	}
	//多选时，如果存在已选中行，派发选中事件
	if (selIndices.length > 0 && newUnSelected){
		var event = new RowSelectEvent();	
		event.isMultiSelect = true;
		var index = selIndices[selIndices.length - 1];
		if (this.rowSet.dataset.onBeforeRowSelect(index) == false)
			return false;
		event.currentRow = this.getRows()[index];
		event.currentRowIndex = index;
		if (!this.rowSet.dataset.silent) {
			this.rowSet.dataset.dispatchEvent(event);
			this.rowSet.dataset.onAfterRowSelect(event);
		}
	}
	
};

/**
 * 获取行数
 */
RowData.prototype.getRowCount = function() {
	var rows = this.getRows();
	if (rows == null)
		return 0;
	return rows.length;
};

/**
 * 获取所有选中行索引
 */
RowData.prototype.getSelectedIndices = function() {
	return this.selectedIndices;
};

/**
 * 获取选中行索引
 */
RowData.prototype.getSelectedIndex = function() {
	if (this.selectedIndices == null || this.selectedIndices.length == 0)
		return -1;
	return this.selectedIndices[0];
};

/**
 * 获取选中行
 */
RowData.prototype.getSelectedRow = function() {
	var rows = this.getSelectedRows();
	if (rows == null)
		return null;
	return rows[0];
};

/**
 * 获取所有选中行
 */
RowData.prototype.getSelectedRows = function() {
	var indices = this.getSelectedIndices();
	if (indices == null || indices.length == 0) {
		return null;
	}
	var rows = this.getRows();
	var selRows = new Array;
	for ( var i = 0; i < indices.length; i++)
		selRows.push(rows[indices[i]]);
	return selRows;
};