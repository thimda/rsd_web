/********************************************************
 * @fileoverview  根据DatasetRelation进行Dataset之间的事件分发及处理。
 *                需要在有主子关系的Dataset上处理事件的业务逻辑，可实现此处暴露的方法
 * @version 1.0
 *
 ******************************************************/

/*
 * @class 对外提供的总的静态方法，根据DsRelation进行Dataset之间的事件分发。
 *        目前只实现了对行选中事件的dsRelation处理,即当主表的某条记录被选中
 *        时,该逻辑自动获取所有的子Dataset，并将设置这些子Dataset的当前页为
 *        此时主表选中行相关的子表记录。前台Dataset会判断该页面是否已经被装载，如果没有装载过,会调用通用逻辑进行数据装载操作。
 * 
 * <Strong>注意:</Strong>该方法有一个判断逻辑，可以控制该方法是否执行，该方法在执行之前判断是否存在window级的方法<Strong>onBeforeDispatchEvent2Ds(event,masterDsId)</Strong>
 * 如果存在则函数返回false，则不执行后继逻辑；如果不存在或函数返回true，则继续执行后继逻辑。例：如果希望处理主子关系，则在js文件中书写全局函数
 * function onBeforeDispatchEvent2Ds(event,masterDsId){ //业务逻辑 return true |
 * false; } <Strong>注意:</Strong>
 * 此函数中为模式化暴露了一个内部方法。onInternalBeforeDispatchEvent2Ds(event, masterDsId)。
 * 如果想去掉内部行为，可在js文件中书写全局函数 window.onInternalBeforeDispatchEvent2Ds = null; 或者
 * function onInternalBeforeDispatchEvent2Ds(event, masterDsId){}; 以取消此函数的默认行为
 * 
 * @param event 向子Dataset转发处理的事件对象，目前仅对RowSelectEvent事件进行了处理响应
 * @param masterDsId 主表Dataset的Id
 * 
 */
// 
// function dispatchEvent2Ds(event, masterDsId)
// {
// alert(masterDsId);
// if(event == null || masterDsId == null)
// return;
// // 分发RowSelectEvent事件进行单独处理
// if(RowSelectEvent.prototype.isPrototypeOf(event))
// {
// if(window.onInternalBeforeDispatchEvent2Ds){
// if(window.onInternalBeforeDispatchEvent2Ds(event, masterDsId) == false){
// log("call onInternalBeforeDispatchEvent2DS for ds:" + masterDsId + "
// result:false");
// return;
// }
// }
//		
// if(window.onBeforeDispatchEvent2Ds){
// if(window.onBeforeDispatchEvent2Ds(event, masterDsId) == false)
// {
// log("call onBeforeDispatchEvent2DS for ds:" + masterDsId + " result:false");
// return;
// }
// }
// log("call $processRowSelectEvent for ds:" + masterDsId);
// $processRowSelectEvent(event, masterDsId, true);
// log("finish call $processRowSelectEvent for ds:" + masterDsId);
// }
// else if(RowUnSelectEvent.prototype.isPrototypeOf(event)){
// $processRowSelectEvent(event, masterDsId, false);
// }
// else if(DataChangeEvent.prototype.isPrototypeOf(event))
// {
// if(event.oldValue == event.currentValue){
// return;
// }
// var ds = getDataset(masterDsId);
// /**基本的数学计算式在前台来完成计算*/
// var formular = ds.metadata[event.cellColIndex].clientFormular;
// if(formular != null && formular != ''){
// var tmpKey = ds.metadata[event.cellColIndex].key;
// var a = decodeURIComponent(formular);
// var fArray = eval(a);
// while(fArray.length != 0){//多个公式，则从后向前来运算
// var editFormulars = fArray.pop();
// if(editFormulars == null)
// continue;
// var formulars = editFormulars.formular;//公式中右边的
// for(var k=0; k < formulars.length; k++ ){
// if(formulars[k].value == tmpKey){
// $countFormular(editFormulars, ds, event);
// break;
// }
// }
// }
// }
//		
// /**调用NC编辑公式*/
// formular = ds.metadata[event.cellColIndex].formular;
// if(formular != null && formular != ''){
// var row = event.currentRow;
// var obj = new Object;
// obj.javaClass = 'java.util.HashMap';
// var objMap = new Object;
// obj.map = objMap;
// for(var i = 0,len=ds.metadata.length; i < len; i ++){
// var md = ds.metadata[i];
// var value = row.getCellValue(i);
// //将"."替换成 _$_
// var replaceField = md.field.replace(".", "_$_");
// objMap[replaceField] = value;
// }
// var arr = decodeURIComponent(formular).split(';');
// for(var i=0,len=arr.length; i<len; i++){
// if(arr[i]==null || arr[i] == '')
// continue;
// var result = getFormularService().executeFormular(obj, arr[i]);
// if(!$executeResult(result,ds,event))
// return;
// var resultValue = result.map["formular_value"];
// if(resultValue == '$NULL$')
// resultValue = null;
// ds.setValueAt(event.cellRowIndex, ds.nameToIndex(result.map["formular_key"]),
// resultValue, true);
// }
// }
//		
// /**用户自定义的公式执行*/
// formular = ds.metadata[event.cellColIndex].defEditFormular;
// if(formular != null){
// if(typeof myDefEditInvoke != "undefined"){
// myDefEditInvoke(decodeURIComponent(formular), ds, event);//此方法需用户自己实现
// }
// }
// }
// };

/**
 * @private
 */
function $countFormular(array, ds, event) {
	var formular = array.formular;
	var tmp = new Array();
	var mathMap = new HashMap();
	mathMap.put("min", "Math.min");
	mathMap.put("max", "Math.max");
	while (formular.length != 0) {
		var val = formular.shift();
		if (val.type != 'ASTFunNode')
			tmp.push(val);
		else {
			var two = $convert(tmp.pop(), ds, event);
			var one = $convert(tmp.pop(), ds, event);
			if (one == null || two == null)
				return;
			try {
				//防止出现以0开头的数据
				if (two != null && two != "" && two.indexOf(".") != -1)
					two = parseFloat(two);
				else if (two != null && two != "")
					two = parseInt(two);
				if (one != null && one != "" && one.indexOf(".") != -1)
					one = parseFloat(one);
				else if (one != null && one != "")
					one = parseInt(one);
			} catch (error) {
			}
			var result = null;
			if (mathMap.containsKey(val.value)) {
				result = eval(mathMap.get(val.value) + "(" + one + "," + two
						+ ")");
			} else
				result = eval(one + val.value + "(" + two + ")");
			var v = new Object;
			v.type = 'ASTConstant';
			v.value = result;
			tmp.push(v);
		}
	}
	var cellKey = array.key;
	var result = tmp.pop().value;
	ds.setValueAt(event.cellRowIndex, ds.nameToIndex(cellKey), result, true);
};

/**
 * @private
 */
function $convert(tmp, ds, event) {
	if (tmp.type == 'ASTConstant')
		return tmp.value;
	else if (tmp.type == 'ASTVarNode') {
		var row = event.currentRow;
		var result = row.getCellValue(ds.nameToIndex(tmp.value));
		if (result == null || result == '')
			result = null;
		return result;
	}
};

/**
 * 编辑公式中的验证
 * 
 * @param result
 * @param ds
 * @param event
 * @return
 * @private
 */
function $executeResult(result, ds, event) {
	if (result == null)
		return false;
	var fv = result.map["formular_value"];
	if (fv == null || fv == '' || fv == '$NULL$')
		return false;

	var fk = result.map["formular_key"];
	var flag = false;
	switch (fk) {
	case "$Confirm":
		var params = [ ds, event ];
		$ok4ConfirmDlg.param = params;
		$cancen4ConfirmDlg.param = params;
		flag = true;
		showConfirmDialog(fv, $ok4ConfirmDlg, $cancen4ConfirmDlg);
		break;
	case "$Error":
		var params = [ ds, event ];
		$ok4ErrorDlg.param = params;
		flag = true;
		showWarningDialog(fv, $ok4ErrorDlg);
		break;
	case "$Message":
		flag = true;
		showMessageDialog(fv);
		break;
	default:
		break;
	}
	return flag;
};

/**
 * 执行验证公式
 * @param ds 待验证的ds
 * @param fieldArr 需要验证的ds属性，为null则遍历ds
 * @return 如果不通过，返回true
 */
function executeValidateFormular(ds) {
	var meta = ds.metadata;
	// 构造验证公式 
	var vfListStr = "{javaClass:'java.util.ArrayList',list:[";
	var vfArr = new Array();
	var flag1 = false;
	for ( var i = 0, len = meta.length; i < len; i++) {
		var f = meta[i].validateFormular;
		if (f != null && f != "") {
			vfArr.push(f);
			flag1 = true;
		}
	}
	if (!flag1)
		return false;
	for ( var i = 0, len = vfArr.length; i < len; i++) {
		vfListStr += "'" + vfArr[i] + "'";
		if (i != len - 1)
			vfListStr += ",";
	}

	vfListStr += "]}";
	// 构造值 
	var rows = ds.getRows();
	if (rows == null || ds.getRowCount() == 0)
		return false;
	var listStr = "{javaClass:'java.util.ArrayList',list:[";
	for ( var k = 0, len = rows.length; k < len; k++) {

		var row = rows[k];
		var obj = new Object;
		obj.javaClass = 'java.util.HashMap';
		var objMap = new Object;
		obj.map = objMap;

		var objMap = "{javaClass:'java.util.HashMap',map:{";
		var arr = new Array();
		for ( var i = 0; i < ds.metadata.length; i++) {
			var md = ds.metadata[i];
			var value = row.getCellValue(i);
			// 将"."替换成 _$_
			var replaceField = md.field.replace(".", "_$_");
			objMap[replaceField] = value;
			if (value != null) {
				var dataType = md.dataType;
				if (dataType == DataType.INTEGER
						|| dataType == DataType.UFDOUBLE
						|| dataType == DataType.INT
						|| dataType == DataType.DOUBLE
						|| dataType == DataType.dOUBLE
						|| dataType == DataType.Decimal)
					arr.push(replaceField + ":" + value);
				else if (dataType == DataType.BOOLEAN
						|| dataType == DataType.bOOLEAN) {
					arr.push(replaceField + ":" + value);
				} else
					arr.push(replaceField + ":'" + value + "'");
			} else
				arr.push(replaceField + ":null");
		}

		objMap += arr.join(",");
		listStr += objMap + "}}";
		if (k != len - 1)
			listStr += ",";
	}
	listStr += "]}";
	var result = getFormularService().executeVatelitionFormular(vfListStr,
			listStr);
	return $executeResult(result, ds, event);
};

/**
 * @private
 */
function $ok4ConfirmDlg() {
};

/**
 * @private
 */
function $cancen4ConfirmDlg() {
	var params = $cancen4ConfirmDlg.param;
	var ds = params[0];
	var event = params[1];
	ds.setValueAt(event.cellRowIndex, event.cellColIndex, 0);
	delete ($cancen4ConfirmDlg.param);
};

/**
 * @private
 */
function $ok4ErrorDlg() {
	var params = $ok4ErrorDlg.param;
	var ds = params[0];
	var event = params[1];
	ds.setValueAt(event.cellRowIndex, event.cellColIndex, 0);
	delete ($ok4ErrorDlg.param);
};

//
// /**
// * 处理RowSelectEvent事件
// * @private
// */
// function $processRowSelectEvent(rowSelectEvent, masterDsId, isSel)
// {
// var dsRelations =
// getDatasetRelations().getRelationsByMasterDataset(masterDsId);
// // 如果没有主子关系则退出
// if(dsRelations == null || dsRelations.length == 0){
// log("no dsRelations for masterDs:" + masterDsId);
// return;
// }
// for(var j = 0,count = dsRelations.length; j < count; j++)
// {
// var dsRelation = dsRelations[j];
// log("process dsRelation=" + dsRelation.id);
// if(dsRelation != null)
// {
// var detailDsId = dsRelation.detailDataset;
// var masterKeyField = dsRelation.masterKeyField;
// var detailForeignkey = dsRelation.detailForeignkey;
// // 主DataSet对象
// var masterDataset = getDataset(masterDsId);
// // 子DataSet对象
// var detailDataset = getDataset(detailDsId);
// // 没有子ds不能继续下面的逻辑
// if(detailDataset == null)
// continue;
//		  	
// if(isSel){
// var keyValue = null;
// // 获得主表主键值数组
// if(rowSelectEvent.currentRow != null){
// keyValue =
// rowSelectEvent.currentRow.getCellValue(masterDataset.nameToIndex(masterKeyField));
// if(keyValue == null || keyValue == ""){
// detailDataset.initialize(rowSelectEvent.currentRow.rowId);
// continue;
// }
// }
// else
// keyValue = -1;
// detailDataset.addReqParameter(IDatasetConstant.QUERY_PARAM_KEYS ,
// detailForeignkey);
// detailDataset.addReqParameter(IDatasetConstant.QUERY_PARAM_VALUES ,
// keyValue);
// // 暴露该方法,用户可在子表中设入一些数据带到后台
// if(window.beforeDetailDsLoad)
// window.beforeDetailDsLoad(detailDataset);
// // 设置子表当前需要显示的记录页Key值
// detailDataset.setCurrentPage(keyValue, 0);
// }
// else{
// detailDataset.setCurrentPage(-1, 0);
// }
// }
// }
// };

/**
 * 
 * function dispatchRefEvent2RefNode() { var relations =
 * window.$refNodeRelations.getRelationsByMasterFieldId(window.$nowRefNodeId);
 * if(relations != null){ for(var i = 0; i < relations.length; i ++){ var
 * relation = relations[i]; $processRefNodeRelation(relation); } } };
 * 
 * function $processRefNodeRelation(relation) { var masterRefNodeId =
 * relation.masterRefNode; var masterRefNode = getRefNodeInfo(masterRefNodeId);
 * var detailRefNode = getRefNodeInfo(relation.detailRefNode); if(detailRefNode ==
 * null){ return; } var masterDs = getDataset(masterRefNode.writeDs); var
 * masterRow = masterDs.getSelectedRow(); var masterValue =
 * masterRow.getCellValue(masterDs.nameToIndex(masterRefNode.writeFields[0]));
 * 
 * var detailDsId = relation.targetDs; var detailDs = null; if(detailDsId !=
 * null) detailDs = getDataset(detailDsId); else detailDs = masterDs; var
 * detailKey = detailRefNode.writeFields[0]; var selectedIndex =
 * detailDs.getSelectedIndex(); var colIndex = detailDs.nameToIndex(detailKey);
 * var oldValue = detailDs.getValueAt(selectedIndex, colIndex);
 * if(relation.clearDetail) detailDs.setValueAt(selectedIndex, colIndex, null,
 * true);
 * 
 * var filterSql = relation.filterSql; filterSql = filterSql.replaceStr("?",
 * masterValue); detailRefNode.setFilterSql(filterSql); //TODO ????????? // var
 * slaveRelations =
 * getRefNodeRelations().getRelationsByMasterFieldId(detailRefNode.id); var
 * slaveRelations =
 * window.$refNodeRelations.getRelationsBySlaveRefNode(detailRefNode.id);
 * if(slaveRelations != null){ for(var i = 0; i < slaveRelations.length; i ++){
 * var slaveRelation = slaveRelations[i];
 * $processRefNodeRelation(slaveRelation); } } }
 */
