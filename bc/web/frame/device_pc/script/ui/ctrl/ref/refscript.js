// 获取打开此参照的父页面
var trueParent = getTrueParent();
//主数据过滤
function doFilter(value) {
	var ds = pageUI.getWidget("main").getDataset("masterDs");
	if (value != null && value != "") {
		ds.reqParameterMap.put("filterValue", value);
		ds.setCurrentPage("$temp_" + value, 0);
	}
	else {
		var navDs = getRootNavDs();
		if (navDs != null) {  // 树表结构
			if (value == null || value == "") {  //value为空，则根据树中选项进行查询
				if(window.lastTreeSelKey == null)
					window.lastTreeSelKey = -1;
				ds.setCurrentPage(window.lastTreeSelKey, 0);
			}
		}
		else{
			ds.reqParameterMap.put("filterValue", "");
			ds.setCurrentPage(Dataset.MASTER_KEY, 0);
		}
	}
}
//主数据行选择
function selectRow(keyCode) {
	var ds = pageUI.getWidget("main").getDataset("masterDs");
	var index = ds.getSelectedIndex();
	if (null == index)
		index = -1;
	if (keyCode == 40) {  // 下箭头
		if (index < ds.getRowCount() - 1)
			ds.setRowSelected(index + 1);
	} else if (keyCode == 38) {  // 上箭头
		if (index >= 1)
			ds.setRowSelected(index - 1);
	} else if (keyCode == 13) {  // 回车
		if (ds.getAllRowCount() > 0) {
			if (index == -1)
				//ds.setRowSelected(index + 1);
				return false;
			// 选中操作
			var grid = pageUI.getWidget("main").getComponent("refgrid");
			var tree = pageUI.getWidget("main").getComponent("reftree");
			if (grid) {
				var selectedRow = grid.getSelectedRows()[0];
				// 模拟双击事件，调用Listener代码
				grid.onRowDblClick(index + 1, selectedRow);
			} else if (tree) {
				var selectedNode = tree.getSelectedNode();
				// 模拟双击事件，调用Listener代码
				tree.ondbclick(selectedNode);
			}
			return true;
		}
	}
	return false;
}
//获取主数据集
function getMasterDs() {
	var ds = pageUI.getWidget("main").getDataset("masterDs");
	return ds;
}
//从本地数据库加载数据集到目标数据库
function doGetFromCache(sql, size, keyValue, index, ds, userObj) {
	var cache = getCacheProxy(window.globalPath + "_" + window.datasourceName);
	if(cache.isCacheEnabled()){
		try{
			var dataStr = cache.executeQueryByPage(sql, size, index + 1);
			eval("var data = " + dataStr);
			ds.setJsonData(data, keyValue, index,userObj);
			return true;
		}
		catch(error){
			return false;
		}
	}
	return false;
}
//sql替换
function aggSql(sql, value) {
	if(sql == null || sql == "")
		return null;
	if(value != null && value != "")
		value = decodeURIComponent(value);
	sql = sql.replaceAll("REPLACE", value);
	return sql;
}

/**
 * 生成参照关联关系SQL语句
 */
function createRefRelationSql(masterFieldInfos) {
	if (masterFieldInfos != null) {
		var sql = "";
		for (var i = 0, n = masterFieldInfos.length; i < n; i++) {
			var dsId = masterFieldInfos[i].dsId;
			var fieldId = masterFieldInfos[i].fieldId;
			var filterSql = masterFieldInfos[i].filterSql;
			var nullProcess = masterFieldInfos[i].nullProcess;

			var widget = trueParent.window.$refNodeRelations.widget;
			var ds = widget.getDataset(dsId);
			var value = ds.getSelectedRow().getCellValue(ds.nameToIndex(fieldId));
			var tempSql = "";
			if (value == null) {
				if (nullProcess == "null") {
					tempSql = filterSql.substring(0, filterSql.indexOf("=") - 1);
					tempSql += "is null";
				} else if (nullProcess == "empty")
					tempSql = filterSql.replace("?", "''");
				else if (nullProcess == "both") {
					var nullSql = filterSql.substring(0, filterSql.indexOf("=") - 1);
					nullSql += "is null";
					tempSql = "(" + nullSql + " and " + filterSql.replace("?", "''") + ")";
				} else if (nullProcess == "ignore")
					tempSql = "";
			} else {
				tempSql = filterSql.replace("?", "'" + value + "'");
			}
			
			if ("" != tempSql)
				sql = "(" + tempSql + ")";
		}
		return sql;
	}
	return null;
}

/**
 * 从前台缓存中获取参照数据内容
 */
function getFromCache(userObj) {
	var size = 20;
	var index = 0;
	var ds = getMasterDs();
	var indexStr = ds.reqParameterMap.get(IDatasetConstant.QUERY_PARAM_PAGEINDEX);
	var keyValue = ds.reqParameterMap.get(IDatasetConstant.QUERY_PARAM_KEYVALUE);
	if (indexStr != null && indexStr != "")
		index = parseInt(indexStr);
	var filterValue = ds.reqParameterMap.get("filterValue");
	var sql = null;
	if (filterValue != null) {
		sql = aggSql(getSessionAttribute("ref_filter_sql"), filterValue);
	}
	else{
		sql = getSessionAttribute("ref_query_sql");
		var joinSql = ds.reqParameterMap.get("joinSql");
		if (joinSql != null)
			sql += joinSql;
	}
	if (sql == null || sql == "")
		return false;
	
	// 拼接参照关联关系SQL语句
	var refRelationSql = "";
	if (trueParent.window.$refNodeRelations != null) {
		var refNodeId = getParameter("nodeId");
		var widget = trueParent.window.$refNodeRelations.widget;
		var refNode = widget.getRefNode(refNodeId);
		if(refNode == null)
			return false;
		var refNodeRelation = trueParent.window.$refNodeRelations.getRelationBySlaveRefNode(refNodeId, refNode.writeDs);
	
		if (refNodeRelation != null) {
			refRelationSql = createRefRelationSql(refNodeRelation.masterFieldInfos);
			if (refRelationSql != null) {
				if (sql.indexOf("where ") != -1) {
					sql = sql.replace("where ", "where " + refRelationSql + " and ");
				} else {
					sql += "where " + refRelationSql;
				}
			}
		}
	}
	var ordersql = getSessionAttribute("ref_query_sql_order");
	if(ordersql != null && ordersql != "")
		sql += ordersql;		
	// 为Dataset增加参数，供后台查询数据的方法调用
	ds.addReqParameter("relationWhereSql", refRelationSql);
	return doGetFromCache(sql, size, keyValue, index, ds,userObj);
	
}
/*
*加载一级树数据
*/
function getRootNavFromCache(eventdata){
	var userObj = null;
	if(eventdata != null && eventdata.userObj != null)
		userObj = eventdata.userObj;
	var ds = getRootNavDs();
	var index = 0;
	var keyValue = ds.reqParameterMap.get(IDatasetConstant.QUERY_PARAM_KEYVALUE);
	var filterValue = ds.reqParameterMap.get("filterValue");
	var sql = null;
	if (filterValue != null) {
		sql = aggSql(getSessionAttribute("ref_query_sql_root"), filterValue);
	}
	else {
		sql = getSessionAttribute("ref_query_sql_root");
		var joinSql = ds.reqParameterMap.get("joinSql");
		if (joinSql != null)
			sql += joinSql;
	}
	if (sql == null || sql == "")
		return false;

	return doGetFromCache(sql, -1, keyValue, index, ds,userObj);
}


/**
 * 加载二级树数据
 */

function getLeafNavFromCache(eventdata) {
	var userObj = null;
	if(eventdata != null && eventdata.userObj != null)
		userObj = eventdata.userObj;
		
	var ds = getLeafNavDs();
	var index = 0;
	var keyValue = ds.reqParameterMap.get(IDatasetConstant.QUERY_PARAM_KEYVALUE);
	var filterValue = ds.reqParameterMap.get("filterValue");
	var sql = getSessionAttribute("ref_query_sql_dir");
	var joinsql = getSessionAttribute("ref_query_sql_root_join");
	if (joinsql)
	    sql = sql + joinsql;
	if (keyValue != null) {
	    sql = aggSql(sql, keyValue);
	}
	if (sql == null || sql == "")
		return false;

	// 拼接参照关联关系SQL语句
	var refRelationSql = "";
	if (trueParent.window.$refNodeRelations != null) {
		var refNodeId = getParameter("nodeId");
		var widget = trueParent.window.$refNodeRelations.widget;
		var refNode = widget.getRefNode(refNodeId);
		if(refNode == null)
			return false;
		var refNodeRelation = trueParent.window.$refNodeRelations.getRelationBySlaveRefNode(refNodeId, refNode.writeDs);
	
		if (refNodeRelation != null) {
			refRelationSql = createRefRelationSql(refNodeRelation.masterFieldInfos);
			if (refRelationSql != null) {
				if (sql.indexOf("where ") != -1) {
					sql = sql.replace("where ", "where " + refRelationSql + " and ");
				} else {
					sql += "where " + refRelationSql;
				}
			}
		}
	}
	var ordersql = getSessionAttribute("ref_query_sql_dir_order");
	if(ordersql != null && ordersql != "")
		sql += ordersql;
	// 为Dataset增加参数，供后台查询数据的方法调用
	ds.addReqParameter("relationWhereSql", refRelationSql);
	hasloaddata =doGetFromCache(sql, -1, keyValue, index, ds,userObj); 
	return hasloaddata;
};
/**
* 树的一级行选中后的方法
*/
function treeRootDsOnAfterRowSelectFromCache() {
	
}
/**
 * 树表参照，树的二级行选中后执行方法（从缓存中获取表数据）
 */
function treeLeafDsOnAfterRowSelectFromCache(eventdata) {
	var userObj = null;
	if(eventdata != null && eventdata.userObj != null)
		userObj = eventdata.userObj;
		
	var widget = pageUI.getWidget("main");
	var treeDs = getLeafNavDs();
	var selecteRow = treeDs.getSelectedRow();
	if(selecteRow == null){
		return true;
	}
	var dsRels = widget.getDsRelations();
	if (dsRels != null)
	{
		var masterRels = dsRels.getRelationsByMasterDataset(treeDs.id);
		for (var i = 0; i < masterRels.length; i++) {
			var dr = masterRels[i];
			var masterKey = dr.masterKeyField;
			var keyValue = selecteRow.getCellValue(treeDs.nameToIndex(masterKey));
			
			if (keyValue == null)
				keyValue = selecteRow.rowId;

			var ds = getMasterDs();
			window.lastTreeSelKey = keyValue;
			ds.reqParameterMap.clear();
			var joinSql = getSessionAttribute("ref_query_sql_dir_join");
			if(joinSql != null){
				joinSql = joinSql.replace("REPLACE", keyValue);
				ds.reqParameterMap.put("joinSql", joinSql);
			}
			ds.setCurrentPage(keyValue, 0);
			
			return getFromCache(userObj);
		}
	}
	return false;
}
/**
*二级dataset
*/
function getLeafNavDs() {
	ds = pageUI.getWidget("main").getDataset("masterDs_tree");
	if(ds == null)//说明左边没有二级 树，直接加载一级树
		ds = getRootNavDs();
	return ds;
}
/**
*一级dataset
*/
function getRootNavDs() {
	var ds = pageUI.getWidget("main").getDataset("masterDs_tree_1");	
	return ds;
}


/**
 * 通过前台缓存处理OK事件（行双击事件）
 */
function refOkFromCache() {
	if(window.clientMode){
		var widgetId = getParameter("widgetId");
		var refNodeId = getParameter("nodeId");
		var rfnode = trueParent.pageUI.getWidget(widgetId).getRefNode(refNodeId);
	
		var currentDs = getMasterDs();
		var currRow = currentDs.getSelectedRow();
		var readFields = rfnode.readFields;
		var writeFields = rfnode.writeFields;
		if (rfnode.writeDs == null || "" == rfnode.writeDs) {  // 未绑定dataset
			var owner = getParameter("owner");
			var refComp = trueParent.pageUI.getWidget(widgetId).getComponent(owner);
			if (currRow != null) {
				// 要求readField第一个值必须为PK ！！！
				var value = currRow.getCellValue(currentDs.nameToIndex(readFields[0]));
				var showValue = currRow.getCellValue(currentDs.nameToIndex(readFields[1]));
				refComp.showValue = showValue;
				refComp.setValue(value);
				refComp.setShowValue(showValue);
			}
		} 
		else {  // 绑定了dataset
			var wds = trueParent.pageUI.getWidget(widgetId).getDataset(rfnode.writeDs);
			
			var row = wds.getSelectedRow();
			for (var i = 0; i < writeFields.length; i++) {
				var value = null;
				if (currRow != null) {
					var index = currentDs.nameToIndex(readFields[i]);
					if (index == -1) {
						showErrorDialog("没有找到列:" + readFields[i]);
						return false;
					}
					value = currRow.getCellValue(index);
				}
				wds.setValue(writeFields[i], value);
			}
		}
		refClose();
		return true;
	}
	return false;
}

/**
 * 关闭参照DIV或参照对话框
 */
function refClose() {
	//trueParent.document.onclick();
	if (trueParent != null)
		trueParent.hideDialog();
}


