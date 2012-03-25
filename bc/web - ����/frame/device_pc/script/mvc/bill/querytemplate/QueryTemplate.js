TreeViewComp.prototype.onBeforeNodeCreate = function(row) {
	if (this.id == "advanceTree") {
		var fieldType = row.getCellValue(9);
		var logicType = row.getCellValue(14);
		if (fieldType == 0 || fieldType == 1 || logicType == 1)
			return false;
	}
	return true;
};

function OnRootNodeCreatedFun(node) {
	node.changeCaption("${ml:trans('query_allcondition')}");
}
function gerChangeNodeFun(node) {
	node.changeCaption("${ml:trans('query_generalquery')}");
}

/**
 * Init
 */
function externalInit() {

	window.widgetId = "${widgetId}";
	window.$c_normalPanel = new QueryTemplatePanel(document
			.getElementById("$d_normalPanel"), "normalPanel", 0, 0, "100%",
			"100%", "relative");
	var widget = pageUI.getWidget(widgetId);
	$c_normalPanel.widget = widget;
	$c_normalPanel.setDataset(widget.getDataset("queryConditionDataset"));
	window.$queryTemplateProcessor = new QueryTemplateProcessor(widget);

	var tab = pageUI.getTab('sqlTab');
	tab.beforeItemInit = function(item) {
	};

	tab.afterItemInit = function(item) {
		// window.$c_advancedPanel = new
		// QueryTemplatePanel(document.getElementById("$d_advancePanel"),
		// "advancePanel", 0, 0, "100%", "100%", "relative");
		// window.$c_advancedPanel.setIfMust(true);
		// $c_advancedPanel.setDataset(pageUI.getWidget(widgetId).getDataset("queryConditionTreeDataset"));

		// TODO ??????????????????????
		var advanceDbClickListener = new TreeNodeListener();
		advanceDbClickListener.ondbclick = function(treeNodeMouseEvent) {
			var node = treeNodeMouseEvent.node;
			templateAdvanceOndbClick(node);
		};
		var advanceTree = pageUI.getWidget(widgetId)
				.getComponent("advanceTree");
		advanceTree.addListener(advanceDbClickListener);

	};

	tab.rightBarSpace.style.width = "90px";
	var toolbar_1 = new ToolBarComp(tab.rightBarSpace, "queryConditionToolbar",
			0, 0, "90", "", "relative", null, true);
	toolbar_1.addButton("imageSave", "", "保存", window.themeGlobalPath
			+ "/html/themes/" + window.themeId
			+ "/images/querytemplate/save.png", "right", true);
	toolbar_1.addButton("imageClear", "", "清空", window.themeGlobalPath
			+ "/html/themes/" + window.themeId
			+ "/images/querytemplate/clear.png", "right", true);
	toolbar_1.addButton("imageReset", "", "重置", window.themeGlobalPath
			+ "/html/themes/" + window.themeId
			+ "/images/querytemplate/reset.png", "right", true);
	// 保存
	var imageSaveMouseListener = new MouseListener();
	imageSaveMouseListener.onclick = function(MouseEvent) {
		doConditionSave.flag = 'Favorites';
		if (!window.$c_conditionSavedialog) {
			window.$c_conditionSavedialog = new InputDialogComp("saveText",
					"输入对话框", 0, 0, null, null, 100, doConditionSave);
			window.$c_conditionSavedialog.addItem("请输入保存名:",
					"conditionSaveText", "string", true, null);
		}
		window.$c_conditionSavedialog.show();
	};
	toolbar_1.getButton("imageSave").addListener(imageSaveMouseListener);
	// 清空
	var imageClearMouseListener = new MouseListener();
	imageClearMouseListener.onclick = function(MouseEvent) {
		var conditionDs = null;
		if (pageUI.getTab('sqlTab').getSelectedIndex() == 0)
			conditionDs = pageUI.getWidget(widgetId).getDataset(
					"queryConditionDataset");
		else
			conditionDs = pageUI.getWidget(widgetId).getDataset(
					"queryConditionTreeDataset");
		var rows = conditionDs.getRows();
		if (rows != null) {
			for ( var i = 0; i < rows.length; i++) {
				// 略过固定
				if (rows[i].getCellValue(9) == 1)
					continue;
				if (rows[i].getCellValue(1) == '$#$')
					continue;
				conditionDs.setValueAt(i, 3, null);
			}
		}
	};
	toolbar_1.getButton("imageClear").addListener(imageClearMouseListener);
	// 重置
	var imageResetMouseListener = new MouseListener();
	imageResetMouseListener.onclick = function(MouseEvent) {
		var conditionDs = null;
		if (pageUI.getTab('sqlTab').getSelectedIndex() == 0) {
			conditionDs = pageUI.getWidget(widgetId).getDataset(
					"queryConditionDataset");
			// 删除已显示内容
			// window.$c_normalPanel.removeAllElement();
		} else {
			conditionDs = pageUI.getWidget(widgetId).getDataset(
					"queryConditionTreeDataset");
			// 删除已显示内容
			// var advanceTree =
			// pageUI.getWidget(widgetId).getComponent("advanceTree");
			// advanceTree.rootNode.clearChildren();
		}
		// 删除原有Dataset内容
		conditionDs.removeRowSet(conditionDs.currentKey);
		// 重新加载Dataset内容
		conditionDs.setCurrentPage(Dataset.MASTER_KEY, 1);
		// conditionDs.undo();
	};
	toolbar_1.getButton("imageReset").addListener(imageResetMouseListener);

	/* 收藏夹 */
	var toolbar_2 = new ToolBarComp(document.getElementById("favoritDiv"),
			"savedConditionToolbar", 0, 0, "100%", "", "relative", null, false);
	toolbar_2.addButton("imageFolder", "", "生成文件夹", window.themeGlobalPath
			+ "/html/themes/" + window.themeId
			+ "/images/querytemplate/folder.png", "right", true);
	toolbar_2.addButton("imageDelete", "", "删除", window.themeGlobalPath
			+ "/html/themes/" + window.themeId
			+ "/images/querytemplate/delete.png", "right", true);
	// 生成文件夹
	var folderMouseListener = new MouseListener();
	folderMouseListener.onclick = function(MouseEvent) {
		// TODO
		doConditionSave.flag = 'Folder';
		if (!window.$c_conditionSavedialog) {
			window.$c_conditionSavedialog = new InputDialogComp("saveText",
					"输入对话框", 0, 0, null, null, 100, doConditionSave);
			window.$c_conditionSavedialog.addItem("请输入保存名:",
					"conditionSaveText", "string", true, null);
		}
		window.$c_conditionSavedialog.show();

		// var dialog = pageUI.getWidget(widgetId).getComponent("saveDialog");
		// dialog.show();
		// doConditionSave.flag = 'Folder';
	};
	toolbar_2.getButton("imageFolder").addListener(folderMouseListener);
	// 删除
	var deleteMouseListener = new MouseListener();
	deleteMouseListener.onclick = function(MouseEvent) {
		deleteFavorit();
	};
	toolbar_2.getButton("imageDelete").addListener(deleteMouseListener);

}

/**
 * 
function getEntityAttrs(node) {
	if (node.isLeaf || node.childrenTreeNodes.length > 0)
		return;
	var parentRow = node.nodeData;
	var entityName = parentRow.getCellValue(5);
	var entityAttrs = getService("favorite_service").getEntityAttrs(entityName,
			window.level++);
	var dsResult = entityAttrs.map.dsResult;
	var combs = entityAttrs.map.combs;
	var ds = pageUI.getWidget(widgetId).getDataset("queryDataset");
	for ( var i = 0; i < dsResult.length; i++) {
		var row = new DatasetRow();
		for ( var j = 0; j < 7; j++) {
			row.setCellValue(j, dsResult[i][j]);
		}

		// set parent field key
		row.setCellValue(7, parentRow.getCellValue(0));
		// set parentField
		row.setCellValue(9, parentRow.getCellValue(3));
		// set table name
		row.setCellValue(10, dsResult[i][7]);
		ds.addRow(row);
	}

	for ( var i = 0; i < combs.length; i++) {
		var id = combs[i].id;
		if (getComboData(id) != null) {
			continue;
		}
		var comb = new ComboData();
		for ( var j = 0; j < combs[i].allCombItems.length; j++) {
			var item = combs[i].allCombItems[j];
			comb.addItem(new ComboItem(item.i18nName, item.value));
		}
		window["$cb_" + id] = comb;
	}
}
*/

/**
 * 收藏夹保存
 */
function doConditionSave() {
	var saveDs = pageUI.getWidget(widgetId).getDataset("savedQueryCondition");
	// var saveName =
	// pageUI.getWidget(widgetId).getComponent("saveText").getValue();
	var saveName = window.$c_conditionSavedialog.getItem("conditionSaveText")
			.getValue();

	if (saveName == '') {
		//showMessageDialog("${ml:trans('yer_savefoldernotempty')}");
		showMessageDialog("查询方案名不能为空!");
		return;
	}
	var row;
	if (doConditionSave.flag == 'Favorites') {
		var tabIndex = pageUI.getTab('conditionTab').getSelectedIndex();
		if (tabIndex == 0)
			pageUI.getTab('conditionTab').activeTab(1);

		row = getFavoritesData(saveDs);
		if (row == null)
			return;
		var rowArray = $saveFavoritesImpl(row);
		if (rowArray == null)
			return;
		for ( var i = 0; i < rowArray.length; i++)
			saveDs.addRow(rowArray[i]);
	} else if (doConditionSave.flag == 'Folder') {
		row = getFolderData(saveDs);
		if (row == null)
			return;
		var page_fun_code = getParameter("otherPageId");
		var nodeInfo = row.contentToXml() + "," + page_fun_code + ","
				+ "${templateid}";// TODO
		getService("favorite_service").saveQueryCondition(null, nodeInfo);
		saveDs.addRow(row);
	} else if (doConditionSave.flag == 'modify') {
		row = doConditionSave.row;
		row.setCellValue(3, saveName);
		var page_fun_code = getParameter("otherPageId");
		var nodeInfo = row.contentToXml() + "," + page_fun_code + ","
				+ "${templateid}";// TODO
		getService("favorite_service").modifyQueryConditionName(nodeInfo);
		saveDs.setValueAt(saveDs.getRowIndex(row), 3, saveName);
	}
	window.$c_conditionSavedialog.close();
	delete (doConditionSave.flag);
	delete (doConditionSave.row);
}

/**
 * 要保存或修改的Favorites数据
 */
function getFavoritesData(saveDs) {
	// var saveName =
	// pageUI.getWidget(widgetId).getComponent("saveText").getValue();
	var saveName = window.$c_conditionSavedialog.getItem("conditionSaveText")
			.getValue();
	if ($isNameExist())
		return null;
	var row = new DatasetRow();
	var guid = (Math.random() * 1000000).toString().substring(0, 6);
	row.setCellValue(0, guid);
	row.setCellValue(1, guid);
	row.setCellValue(3, saveName);
	row.setCellValue(4, 'Favorites');
	row.setCellValue(5, null);
	row.setCellValue(6, 'N');

	var selectRow = saveDs.getSelectedRow();
	if (selectRow != null
			&& selectRow.getCellValue(saveDs.nameToIndex("kind")) == 'Folder') {
		row.setCellValue(2, selectRow.getCellValue(saveDs.nameToIndex("id")));// have
																				// parent
	} else {
		row.setCellValue(2, null);
	}
	return row;
}

/**
 * 要保存或修改的Folder数据
 */
function getFolderData(saveDs) {
	// var saveName =
	// pageUI.getWidget(widgetId).getComponent("saveText").getValue();
	var saveName = window.$c_conditionSavedialog.getItem("conditionSaveText")
			.getValue();
	if ($isNameExist())
		return null;
	var row = new DatasetRow();
	var guid = (Math.random() * 1000000).toString().substring(0, 6);
	row.setCellValue(0, guid);
	row.setCellValue(1, guid);
	row.setCellValue(3, saveName);
	row.setCellValue(4, 'Folder');
	row.setCellValue(5, null);
	row.setCellValue(6, 'N');

	var selectRow = saveDs.getSelectedRow();
	if (selectRow != null
			&& selectRow.getCellValue(saveDs.nameToIndex("kind")) == 'Folder') {
		row.setCellValue(2, selectRow.getCellValue(saveDs.nameToIndex("id")));// have
																				// parent
	} else {
		row.setCellValue(2, null);
	}
	return row;
}

function $isNameExist() {
	var saveDs = pageUI.getWidget(widgetId).getDataset("savedQueryCondition");
	// var saveName =
	// pageUI.getWidget(widgetId).getComponent("saveText").getValue();
	var saveName = window.$c_conditionSavedialog.getItem("conditionSaveText")
			.getValue();
	var nameIndex = saveDs.nameToIndex("display");
	var kindIndex = saveDs.nameToIndex("kind");
	for ( var i = 0, count = saveDs.getRowCount(); i < count; i++) {
		var temRow = saveDs.getRow(i);
		if (temRow.getCellValue(nameIndex) == saveName) {
			if (doConditionSave.flag == temRow.getCellValue(kindIndex)) {
				showMessageDialog("${ml:trans('yer_savefolderduplicate')}");
				return true;
			}
		}
	}
	return false;
}
/**
 * 删除收藏夹
 */
function deleteFavorit() {
	var saveDs = pageUI.getWidget(widgetId).getDataset("savedQueryCondition");
	var selectRow = saveDs.getSelectedRow();
	if (selectRow != null) {
		var node = pageUI.getWidget(widgetId).getComponent("savedTree")
				.getNodeByRowId(selectRow.rowId);
		delNode.row = selectRow;
		if (node.hasChildren())
			ConfirmDialogComp.showDialog("其下有子结点，确定全部删除？", delNode, null, null,
					null);
		else
			ConfirmDialogComp.showDialog("确定删除？", delNode, null, null, null);
	}
}

/**
 * delImpl
 */
delNode = function() {
	var row = delNode.row;
	var saveDs = pageUI.getWidget(widgetId).getDataset("savedQueryCondition");
	// saveDs.deleteRow(saveDs.getRowIndex(row));
	saveDs.removeRow(saveDs.getRowIndex(row));
	var page_fun_code = parent.getParameter("pageId");
	var nodeInfo = row.contentToXml() + "," + page_fun_code + ","
			+ "${templateid}"; // TODO
	getService("favorite_service").deleteQueryCondition(nodeInfo);
	delete (delNode.row);
}


/**
 *收藏夹树的叶子节点双击处理
function templateTreeSaveConditionOndbclick(node)
{
	var rightDs;
	if(node.isLeaf){
		var row = node.nodeData;
		var page_fun_code = parent.getParameter("pageId");
		var nodeInfo = row.contentToXml() + "," + page_fun_code + "," + "${templateid}";//TODO
		var conditionArr = getService("favorite_service").loadQueryCondition(nodeInfo);
		var arr = eval(conditionArr);

		var array = arr[0].split(",");
		var tabIndex = pageUI.getTab('sqlTab').getSelectedIndex();
		
		//TODO
		//if(array[0] == '$AND_START' || array[4] == '$AND_START'){//advance tab
		var conditionValue = arr[0].split("#")[3];
		if (conditionValue.toUpperCase() == "AND" || conditionValue.toUpperCase() == "OR") {  //advance tab
	    	if(tabIndex == 0)
	    		pageUI.getTab('sqlTab').activeTab(1);
		   	 rightDs = pageUI.getWidget(widgetId).getDataset("queryConditionTreeDataset");
		   	 cleanDsData(false);
		}else{//common tab
			if(tabIndex == 1)
		    	pageUI.getTab('sqlTab').activeTab(0);
			rightDs = pageUI.getWidget(widgetId).getDataset("queryConditionDataset");
			cleanDsData(true);
		}
		for(var i=0;i < arr.length;i++){
			var strArr = arr[i].split("#");
		    var addRow = new DatasetRow();
		    addRow.setCellValue(0, strArr[0]);//id
		    addRow.setCellValue(1, strArr[1]);//label
		    addRow.setCellValue(2, strArr[2]);//condition
		    addRow.setCellValue(3, strArr[3]);//condition_value
		    addRow.setCellValue(4, strArr[4]);//parentId
		    addRow.setCellValue(5, strArr[5]);//editorType
		    addRow.setCellValue(6, strArr[6]);//dataType
		    addRow.setCellValue(7, strArr[7]);//editorInfo
		    addRow.setCellValue(8, strArr[8]);//field
		    addRow.setCellValue(9, strArr[9]);//CondType
		    addRow.setCellValue(10, strArr[10]);//editorInfo2
		    addRow.setCellValue(11, strArr[11]);//tableName
		    addRow.setCellValue(12, strArr[12]);
		    addRow.setCellValue(13, strArr[13]);
		    addRow.setCellValue(14, strArr[14]);
		    rightDs.addRow(addRow);
		    rightDs.clearState();
		}
	}
}
 */

/**
 *clean ds data
function cleanDsData(flag)
{
	var rightDadaset;
	if(flag){//common
		rightDadaset = pageUI.getWidget(widgetId).getDataset("queryConditionDataset");
		var count = rightDadaset.getRowCount();
		if(count != 0){
			for(var k=0; k < count; k++){
				rightDadaset.removeRow(0);
			}
		}
	}
	else{//advance
		 rightDadaset = pageUI.getWidget(widgetId).getDataset("queryConditionTreeDataset");
		 var count = rightDadaset.getRowCount();
		 if(count != 0){
			for(var k=0; k < count; k++){
				//TODO ????????????????
				//var tmpRow = rightDadaset.getRow(k);
				//if(tmpRow.getCellValue(0) == '$AND_START'){
				//	rightDadaset.removeRow(k);
				//	break;
				//}
				rightDadaset.removeRow(0);
			}
		 }
	}
}
 */

/**
 * 设为默认查询条件
 */
function setDefaultCondition(item) {
	var node = item.parentOwner.triggerObj;
	var row = node.nodeData;
	if (row.getCellValue(4) != 'Favorites')
		return;
	row.setCellValue(6, 'Y');
	$saveFavoritesImpl(row);

	// other row set not default
	var saveDs = pageUI.getWidget(widgetId).getDataset("savedQueryCondition");
	for ( var i = 0; i < saveDs.getRowCount(); i++) {
		var temRow = saveDs.getRow(i);
		if ($isTrue(row.getCellValue(6)) && row.getCellValue(4) == 'Favorites') {
			row.setCellValue(6, 'N');
			$saveFavoritesImpl(row);
		}
	}
}

/**
 * string to boolean
 */

function $isTrue(val) {
	if (val != null && val.length > 0
			&& (val == "true" || val.charAt(0) == 'Y' || val.charAt(0) == 'y'))
		return true;
	else
		return false;
}

/**
 * modify name
 */
function modifySavedConditionName(item) {
	var node = item.parentOwner.triggerObj;
	var row = node.nodeData;
	var dialog = pageUI.getWidget(widgetId).getComponent("saveDialog");
	dialog.show();
	doConditionSave.flag = 'modify';
	doConditionSave.row = row;
}
/**
 * 具体的保存方法
 * 
 * @param 节点信息row
 * @return 节点对应的条件row数组
 */
function $saveFavoritesImpl(row) {
	var saveDs = pageUI.getWidget(widgetId).getDataset("savedQueryCondition");
	var conditionDs = null;
	if (pageUI.getTab('sqlTab').getSelectedIndex() == 0)
		conditionDs = pageUI.getWidget(widgetId).getDataset(
				"queryConditionDataset");
	else
		conditionDs = pageUI.getWidget(widgetId).getDataset(
				"queryConditionTreeDataset");
	var rows = conditionDs.getRows();
	var returnArrayRow;
	if (rows != null && rows.length > 0) {
		var condArr = new Array;
		for ( var i = 0; i < rows.length; i++) {
			var content = rows[i].contentToXml();
			condArr.push(content);
		}
		var page_fun_code = getParameter("otherPageId");
		var nodeInfo = row.contentToXml() + "," + page_fun_code + ","
				+ "${templateid}";// TODO
		getService("favorite_service").saveQueryCondition(condArr, nodeInfo);
		returnArrayRow = new Array();
		returnArrayRow.push(row);
	} else {
		showMessageDialog("条件不能为空!");
		return;
	}
	return returnArrayRow;
}

function toDeleteTreeCondition(item) {
	if (item != null) {
		var node = item.parentOwner.triggerObj;
		$templateAdanceTreeRowDelete(node.nodeData);
	}
}

function $templateAdanceTreeRowDelete(row) {
	var ds = pageUI.getWidget(widgetId).getDataset("queryConditionTreeDataset");
	var count = ds.getRowCount();
	// ds.deleteRow(ds.getRowIndex(row));
	if (count != 0) {
		for ( var k = 0; k < count; k++) {
			var tmpRow = ds.getRow(k);
			if (tmpRow.getCellValue(0) == '$AND_START') {
				ds.deleteRow(k);
				break;
			}
		}
	}
}