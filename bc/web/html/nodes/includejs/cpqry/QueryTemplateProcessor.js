/**********************************************************
 *@fileoverview 该js代码主要负责新型查询模版的查询条件构造及调用查询通用查询逻辑进行数据查询。
 *
 *@author lkp
 *@version 1.0
 *@date 20070404
 *
 * modified by dengjt 根据NC UAP查询模板的实际渲染方式做了适当调整
 * modified by dengjt 2008-03-04 重写树拼sql算法
 ************************************************************/
 
/**
   * @class 该类负责查询模版查询条件的构造，并负责调用相关查询逻辑进行数据装载查询。
   *
   *@param queryCondtionDs 构造查询条件的ds对象
   *@param targetDs  需要查询的目标Ds对象，其需要装载的数据对应的keyValue是
   *                 一个专用的keyValue，即IDatasetConstant.QUERY_TEMPLATE_KEYVALUES。
   *@param targetPageId 目标ds所在的页面ID,因为查询模版被查询的Ds所在的页面不是查询模版所在的页面，
   *                    因此需要此属性来使后台获取目标pageMeta对象。
   *
   *
   */
function QueryTemplateProcessor(widget)
{
	this.widget = widget;
	var queryDs = this.widget.getDataset("queryDataset");
	this.targetPageId = queryDs.getResponseParameter(IDatasetConstant.QUERY_TEMPLATE_TARGET_PAGEID);
	this.selfDefOperatorMap = null;
	this.defaultSql = " 1=1 ";
	
	var conditionDs = this.widget.getDataset("queryConditionDataset");
	var treeConditionDs = this.widget.getDataset("queryConditionTreeDataset");
	conditionDs.onAfterDataChange = QueryTemplateProcessor.observerRef;
	treeConditionDs.onAfterDataChange = QueryTemplateProcessor.observerRef;
}
QueryTemplateProcessor.observerRef = function(event) {
	//只要该为空，即设置真实值为空
	if(event.currentValue == null || event.currentValue == "")
		event.currentRow.setCellValue(13, null);
};
 
/**
   *该方法实现对ds的查询
   *
   */
QueryTemplateProcessor.prototype.executeSqlQuery = function()
{
	var sqlPair = this.getQuerySql();
	var sql = sqlPair[0];
	var cond = sqlPair[1];
	if(sql == null || sql == "")
		sql = this.defaultSql;
	var queryParamArray = this.getQueryParamArray();
	log("query condition:" + sql);
	var targetDs = parent.billUI.eventHandler.getMasterDs();
	targetDs.reqParameterMap.clear();
	targetDs.addReqParameter(IDatasetConstant.QUERY_TEMPLATE_CONDITION, sql);
	if(cond != null && cond != "")
		targetDs.addReqParameter(IDatasetConstant.QUERY_TEMPLATE_LOGICCONDITION, cond);
	targetDs.addReqParameter(IDatasetConstant.QUERY_PARAM_VALUES_LIST, queryParamArray);
	targetDs.addReqParameter(IDatasetConstant.FROM_QUERY_TEMPLATE, "true");
	//loadDataset(dataset, keyValue, pageindex, isLoadOtherPage, loadWhichPageId, returnFun, returnArgs, callObj, pageCount, type)
	var args = [targetDs,parent]; 
	//simpleLoadDataset(dataset, method, returnFun, returnArgs)
	var loader = new DatasetLoader(targetDs);
	loader.setReturnFunc($qryReturnFunc);
	loader.setReturnArgs(args);
	loader.setKeyValue(Dataset.MASTER_KEY);
	loader.load(parent);
	//parent.simpleLoadDataset(targetDs, null, $qryReturnFunc, args, Dataset.MASTER_KEY);
};


/**
 * 获取查询的参数的数组
 * @private
 */
QueryTemplateProcessor.prototype.getQueryParamArray = function()
{
	if(pageUI.getTab("sqlTab").getSelectedIndex() == 0)
		return this.getNormalQueryParams();
	else
		return this.getAdvancedQueryParams();
}; 
/**
 * @private
 */
QueryTemplateProcessor.prototype.getNormalQueryParams = function()
{
		var rows = this.widget.getDataset("queryConditionDataset").getRows();  
		var params = "";
		if(rows != null){
			for(var i = 0;i < rows.length; i++){
				var condition = rows[i].getCellValue(2) + ",";
				var dataType = rows[i].getCellValue(6) + ",";
			 	var field = rows[i].getCellValue(8) + ",";
			 	var value = null;
		 	    //如果是参照类型的一行数据，则取pk从extend_field中得对应是在LfwQtConditionWrapper的126设置
			    if(rows[i].getCellValue(5) == 'Reference')
					value = rows[i].getCellValue(13);    	
				else
			    	value = rows[i].getCellValue(3);
			    if(value == null || value == "")
					continue;
			 	params += "{" + condition + dataType + field + value + "},";
//			 	list.push(params);
			}
		}
		return params;
};
/**
 * @private
 */
QueryTemplateProcessor.prototype.getAdvancedQueryParams = function()
{
	var advanceTree = this.widget.getComponent("advanceTree");
	var rootNode = advanceTree.rootNode;	
	var pnodes = rootNode.childrenTreeNodes;
	if(pnodes.length == 0)
		return null;
	parentNode = pnodes[0];
	var cond = parentNode.nodeData.getCellValue(3);
	var nodes = parentNode.childrenTreeNodes;
	
};


function $qryReturnFunc(args, isSuccess) {
	if(isSuccess){
		
		if(typeof args[1].afterQueryDataset != "undefined"){
			args[1].afterQueryDataset(args[0]);
		}
		//var ds = args[0];
		//if(ds.getRowCount() > 0 && ds.getSelectedIndex() == -1)
		//	ds.setRowSelected(0);
	}
};

/**
   *通过调用此方法获取构建之后的sql查询条件语句。
   *
   *构建Sql语句的基本算法：(1) 对于非特殊操作符的根记录（即parentId为默认Id并且不是特殊运算符），按and进行处理。
   *                   (2) 遇到特殊运算符记录，入栈；并设置为当前操作运算符（and/or），Sql语句中增加一个"("。
   *                   (3) 在当前操作符之后的所有子节点都按当前操作符处理，直到遇到第一个不是当前操作符子节点的记录。
   *                   (4) 如果遇到一个不是当前操作符的记录，则在操作符栈中出栈查找其父，并记录出栈个数count，sql语句追加count-1个")"，并以父结点操作符作为其运算符，并设置为当前操作节点。
   *                   (5) 如果遇不到记录了，即已经结束，那么在sql语句中追加操作栈当前个数的"）"。
   *                   （6）在处理中遇到的子节点可能是特殊操作符，此时按照(2)进行处理。
   *@param baseOnTreeOrDataset 是通过dataset还是通过树的数据来构建sql语句，当前QueryTemplateController中的拖拽事件是通过
   *                          树来构建的。QueryTemplateProcessor.tree/QueryTemplateProcessor.dataset
   */
QueryTemplateProcessor.prototype.getQuerySql = function()
{ 
	var sql = null;
	var cond = null;
	if(pageUI.getTab('sqlTab').getSelectedIndex() == 0){
		sql = this.getNormalQuerySql();
		cond = this.getLogicCondition(false);
	}
	else{
		sql = this.getAdvancedQuerySql();
		cond = this.getLogicCondition(true);
	}
	//sql = (sql == null || sql == "")? this.defaultSql : sql;
	return [sql, cond];
};

QueryTemplateProcessor.prototype.getAdvancedQuerySql = function() {
	var advanceTree = this.widget.getComponent("advanceTree");
	var rootNode = advanceTree.rootNode;	
	var nodes = rootNode.childrenTreeNodes;
	if(nodes.length == 0)
		return null;
	return this.generateSql(nodes[0]);
};

QueryTemplateProcessor.prototype.generateSql = function(parentNode) {
	var cond = parentNode.nodeData.getCellValue(3);
	var nodes = parentNode.childrenTreeNodes;
	var sql = null;
	//有子节点，表示必定为组合条件 AND/OR
	if(nodes != null && nodes.length > 0){
		var tmpSqls = new Array();
		var filteredSqls = new Array();
		for(var i = 0; i < nodes.length; i ++){
			var row = nodes[i].nodeData;
			tmpSqls[i] = this.processRowToSql(row);
		}
		//确定有几个sql块，多余一个，需要加AND/OR 和括号
		for(var i = 0; i < nodes.length; i ++){
			if(tmpSqls[i] != null && tmpSqls[i] != ""){
				filteredSqls.push(tmpSqls[i]);
			}
		}
		if(filteredSqls.length != 0){
			if(filteredSqls.length == 1)
				sql = filteredSqls[0];
			else{
				sql = "(";
				for(var i = 0; i < filteredSqls.length; i ++){
					sql += filteredSqls[i];
					if(i != filteredSqls.length - 1)
						sql += " " + cond + " ";
				}
				sql += ")";
			}
		}
		delete tmpSqls;
	}
	else{
		var row = parentNode.nodeData;
		sql = this.processRowToSql(row);
	}
	return sql;	
};

 
QueryTemplateProcessor.prototype.getNormalQuerySql = function() {
	var rows = this.widget.getDataset("queryConditionDataset").getRows();  
	var sql = "";
	if(rows != null){
		var validIndex = -1;
		for(var i = 0; i < rows.length; i ++){
			if(rows[i].getCellValue(14) == '1')
				continue;
			var rowSql = this.processRowToSql(rows[i]);
			if(rowSql == null)
				continue;
			validIndex ++;
			if(validIndex == 0) 
				sql += (" " + rowSql + " ");   
			else{
				sql += (" AND " + rowSql + " ");
			}
		}
	}
	return sql;
};

QueryTemplateProcessor.prototype.getLogicCondition = function(isTree) {
	if(isTree)
		ds = this.widget.getDataset("queryConditionTreeDataset");
	else
		ds = this.widget.getDataset("queryConditionDataset");
	var rows = ds.getRows();
	var cond = "";
	if(rows != null){
		for(var i = 0; i < rows.length; i ++){
			var row = rows[i];
			if(row.getCellValue(14) != '1')
				continue;
			var field = row.getCellValue(8);
			//var condition = row.getCellValue(2);
    		var value = null;
    
    		//如果是参照类型的一行数据，则取pk从extend_field中得对应是在LfwQtConditionWrapper的126设置
    		if(row.getCellValue(5) == 'Reference')
				value = row.getCellValue(13);    	
			else
    			value = row.getCellValue(3);
    		if(value == null || value == "")
    			continue;
    		if(cond != "")
    			cond += "&";
    		cond += (field + "=" + value);
		}
	}
	return cond;
};
/**
  *该方法对一行数据进行分析，生成sql片段。
  *@private
  */
QueryTemplateProcessor.prototype.processRowToSql = function(row)
{
	// <Field id="query_condition_id" field="id" dataType="String"
		// label="标识" nullAble="false"/>
		// <Field id="query_condition_label" field="label" dataType="String"
		// label="条件字段" nullAble="false"/>
		// <Field id="query_condition_condition" field="condition"
		// dataType="String" label="条件类型" nullAble="false"/>
		// <Field id="query_condition_value" field="value" dataType="String"
		// label="条件对应值" nullAble="false"/>
		// <Field id="query_condition_parentId" field="parentId"
		// dataType="String" label="父节点标识" nullAble="false"/>
		// <Field id="query_condition_editorType" field="editorType"
		// dataType="String" label="该条件对应的编辑器类型"/>
		// <Field id="query_condition_dataType" field="dataType"
		// dataType="String" label="该条件对应的值的数据类型"/>
		// <Field id="query_condition_editorInfo" field="editorInfo"
		// dataType="String" label="编辑器器类型的附加信息"/>
		// <Field id="query_condition_field" field="field" datatType="String"
		// label="对应的field字段"/>
		// <Field id="query_condition_type" field="fieldType" datatType="String"
		// label="字段类型(必须0，固定1，默认2，可选3)"/>
		//<Field id="query_condition_editorInfo2" field="editorInfo2"  dataType="String" label="编辑器类型的附加信息2"/>
		//<Field id="parent_field" field="parentField"  dataType="String" label="parentField"/>
		//<Field id="tableName" field="tableName"  dataType="String" label="tableName"/>
		//<Field id="query_extend_field" field="query_extend_field"  dataType="String" label="query_extend_field"/><!--  扩展字段--
	
 	var field = row.getCellValue(8);
    var dataType = row.getCellValue(6);
    var condition = row.getCellValue(2);
    var value = null;
    
    //如果是参照类型的一行数据，则取pk从extend_field中得对应是在LfwQtConditionWrapper的126设置
    if(row.getCellValue(5) == 'Reference'){
		value = row.getCellValue(13); 
	}
	else
    	value = row.getCellValue(3);
    
    if(value == null || value == "")
    	return null;
	var isStringType = this.isStringType(dataType);
    var sqlSpatch = "";
	if(condition == ">" || condition == "<" || condition == "<=" || condition == ">=" || condition == "<>")
	{
        sqlSpatch += (field + " " + condition + " ");
        if(isStringType)
           sqlSpatch += ("'" +  value + "' ");
        else
           sqlSpatch +=  (value + " ");   
	}
	else if(condition == "between")
	{
		var values = value.split(",");
		var smallValue = values[0];
		var bigValue = values[1];
		if((bigValue == null || bigValue == '') && (smallValue == null || smallValue == ""))
			return null;
		if(bigValue == null || bigValue == '')
		{
			sqlSpatch += (field + " >= ");
       		if(isStringType)
           		sqlSpatch += ("'" +  smallValue + "' ");
        	else
           		sqlSpatch += (smallValue + " ");   
		}
		else if(smallValue == null || smallValue == ''){
			sqlSpatch += (field + " <= ");
       		if(isStringType)
           		sqlSpatch += ("'" +  bigValue + "' ");
        	else
           		sqlSpatch += (bigValue + " ");   
		}
		else{
			if(isStringType)
				sqlSpatch += (field + " >= '" + smallValue + "' and " + field + " <= '" + bigValue + "' ");
			else
				sqlSpatch += (field + " >= " + smallValue + " and " + field + " <= " + bigValue + "' ");
		}
	}
	else if(condition == "=")
	{
		var values = value.split(",");
        if(values.length == 1)
        {
			sqlSpatch += (field + " = ");
			if(isStringType)
				sqlSpatch += ("'" + values[0] + "' ");
			else
    			sqlSpatch += (values[0] + " ");
        }
        else
        {
           sqlSpatch += (field + " in (");
           for(var i = 0;i < values.length; i++)
           {
              if(isStringType)
                sqlSpatch += ("'" + values[i] + "'");
              else
                sqlSpatch += values[i];
              if(i != values.length -1 )
                sqlSpatch += ",";
              else
                sqlSpatch += ")";
           }
        }
     }
     else if(condition == "like")
     {
        sqlSpatch += (field + " like '%");
        sqlSpatch += (value + "%'"); 
     }
        
    
    if(row.getCellValue(11) != null && row.getCellValue(11) != ""){
    	var parentField = row.getCellValue(11);
    	var tableName = row.getCellValue(12);
    	sqlSpatch = parentField + " in (select " + parentField + " from " + tableName + " where " + sqlSpatch + ")";
    } 
    if(sqlSpatch == "")
    	alert(row);
    return sqlSpatch;
 }
 
 
// /**
//  * 设置该查询模版所用到的操作符解析函数。
//  *
//  * @hashMap 用于存储 operator-->fun的映射关系
//  *  对于fun可以是function也可以是function 的string。要求该function接受的参数依次为：key,value,dataType
//  *  需要返回值为运用该operator生成的sql片段。
//  * 
//  * @private
//  */
// QueryTemplateProcessor.prototype.setSelfDefOperator = function(hashMap)
// {
//    this.selfDefOperatorMap = hashMap;
// }
// 
// /**
//   *运用自定义的操作符来处理生成Sql.通过这种方式可以覆盖普通操作符的传统意义。
//   * 
//   * @private
//   */
// QueryTemplateProcessor.prototype.processSelfDefOperator = function(row)
// {
//     if(this.selfDefOperatorMap == null)
//       return null;
//     var operator = row.getCellValue(2);
//     if(!this.selfDefOperatorMap.containsKey(operator))
//        return null;
//     var fun = this.selfDefOperatorMap.get(operator);
//     
//     var key = row.getCellValue(8);
//     var value = row.getCellValue(3);
//     var dataType = row.getCellValue(6);
//     
//     if(fun == null)
//       return null;
//     if(typeof fun == "function")
//       return fun(key, value, dataType);
//     else if(typeof fun == "string")
//     {
//       var operateFun = eval(fun);
//       if(operateFun != null)
//         return operateFun(key, value, dataType);
//     }else
//       return null;
// }
 
 /**
   *查询完毕后,将ConditionDs的数据清除掉，同时需要联动查询模版页面清理页面显示。
   * 
   * @private
   */
 QueryTemplateProcessor.prototype.clearConditionDs = function()
 {  
    var rows = this.queryConditionDs.getRows();
    if(rows != null)
    {
      var indices = new Array;
      for(var i = 0;i < rows.length; i++)
         indices.push(this.queryConditionDs.getRowIndex(rows[i]));
      this.queryConditionDs.deleteRows(indices);
    }
 }
 
 
 //-------------------------------------------------------------------------------------------------------------------
 
 /**
  * @private
  */
QueryTemplateProcessor.prototype.fetchIndex = function(parentId, rows)
{
	if(rows == null || rows.length == 0)
		return -1;
	for(var i = 0;i < rows.length; i++)
	{
		if(rows[i].getCellValue(0) == parentId)
			return i;
	}
	return -1;
 }

 
 /**
  * @private
  * 判断是否是需加 ‘ 的字符串类型
  */
QueryTemplateProcessor.prototype.isStringType = function(dataType)
{
	if(dataType == DataType.STRING || dataType == DataType.BOOLEAN || dataType == DataType.bOOLEAN
         || dataType == DataType.UFBOOLEAN || dataType == DataType.CHAR || dataType == DataType.CHARACTER
         || dataType == DataType.UFDATE || dataType == DataType.UFDATETIME || dataType == DataType.UFTIME)
		return true;
	else
		return false;
};
 
/**
  *该方法实现对查询模版中一条数据的删除操作.
  *@row 被删除的记录信息
  */
function templateRowDelete(row)
{
	if(row == null)
       return ;
    var ds = pageUI.getWidget("main").getDataset("queryConditionDataset");
    if(ds == null)
       return ;
    var parentId = row.getCellValue(4);
    ds.deleteRow(ds.getRowIndex(row));
    if(parentId == null)
       return ;
     
    /*
     *下面主要的目的是：如果节点被删除后，其原有父节点仅剩下一个子节点了，则需要
     *将该剩余子节点需要向上合并到其某个子节点不为1个的祖先节点上。
     */
    var rows = ds.getRows();
    var oldChildren = $fetchChildren(parentId, rows);
    var parentRow = $fetchRowById(parentId, rows);
    if(oldChildren.length  == 1 && parentRow != null && parentRow.getCellValue(4) != null)
    {    
       var needProcessRow = oldChildren[0];
       var needDeleteRows = new Array;
       var parentRow = $fetchRowById(parentId, rows); 
       needDeleteRows.push(parentRow);
       var ppid = parentRow.getCellValue(4);
       var needAddRow = null;
       while(ppid != null && $fetchChildren(ppid, rows).length == 1)
       {
         var tempRow = $fetchRowById(ppid, rows);
         ppid = tempRow.getCellValue(4);
         if(ppid == null)
           needAddRow = tempRow;
         needDeleteRows.push(tempRow);
       }
       if(needAddRow == null)
         needAddRow = $fetchRowById(ppid, rows);
       
       ds.setValueAt(ds.getRowIndex(needProcessRow), 4, needAddRow.getCellValue(0));
       
       for(var i = 0;i < needDeleteRows.length; i++)
         ds.deleteRow(ds.getRowIndex(needDeleteRows[i]));
    }
}

/**
  *在查询模版ds查找某个记录的所有子记录数组。
  */
function $fetchChildren(parentId, rows)
{
   var children = new Array;
   if(parentId == null || rows == null || rows.length == 0)
      return children;
   for(var i = 0;i < rows.length; i++)
   {
       if(parentId == rows[i].getCellValue(4))
         children.push(rows[i]);
   }
    
   return children;
}

function $fetchRowById(id, rows)
{
   if(id == null || rows == null || rows.length == 0)
     return null;
   for(var i = 0;i < rows.length; i++)
     if(rows[i].getCellValue(0) == id)
       return rows[i];
   return null;
}
 
 
 