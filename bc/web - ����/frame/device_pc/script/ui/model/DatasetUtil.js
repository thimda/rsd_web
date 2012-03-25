/**
 * 通用对Dataset进行序列化的操作,方便对于非保存操作而需要对Dataset进行
 * 前后台传递时对Dataset的序列化.
 * @param ds
 * @param rows 若rows不为null则仅序列化rows中的,否则根据ds的ajaxSaveType序列化ds的更新行或者所有行
 * @private
 */
function searializeDataset(ds, type)
{	
	var strArr = new Array;
	//var str = "";
	strArr.push("<dataset id='" + (ds.widget.id + "." + ds.id) + "' currentkey='" + ds.currentKey + "'" + " editable='" + ds.editable + "' randomRowIndex='" + ds.randomRowIndex + "'>"); 
    if(ds.reqParameterMap != null && ds.reqParameterMap.keySet().length > 0)
    {
    	var keyset = ds.reqParameterMap.keySet();
        strArr.push("<" + EventContextConstant.req_parameters + ">");
        for(var j = 0;j < keyset.length; j++)
        {
        	strArr.push("<" + EventContextConstant.parameter + " name='" + keyset[j] + "'>"); 
            strArr.push(convertXml(ds.reqParameterMap.get(keyset[j])));
            strArr.push("</" + EventContextConstant.parameter + ">");

        }
        strArr.push("</" + EventContextConstant.req_parameters + ">");
	}
	
   	strArr.push("<rowsets>");

   	if (type == "ds_current_page" || type == "ds_current_line"){
	   	var rowSets = new Array();
	   	if (ds.getCurrentRowSet() == null)
	   		rowSets = ds.getRowSets();
	   	else
   			rowSets.push(ds.getCurrentRowSet());
   	}
   	else
		var rowSets = ds.getRowSets();
   	if(rowSets != null){
   		for(var j = 0; j < rowSets.length; j ++) {
		   	var rowSet = rowSets[j];
		 	strArr.push("<rowset pagecount=\"");
		 	strArr.push(rowSet.pagecount);
		 	strArr.push("\" pagesize=\"");
		 	strArr.push(rowSet.pagesize);
		 	strArr.push("\" recordcount=\"");
		 	strArr.push(rowSet.allrowcount);
		 	strArr.push("\" pageindex=\"");
			strArr.push(rowSet.pageindex);
			strArr.push("\" keyvalue=\"");
			strArr.push(rowSet.keyvalue);
			strArr.push("\">\n");
			
			var rowDatas = rowSet.getRowDatas();
			if(rowDatas != null){
				for(var i = 0; i < rowDatas.length; i ++){
					var rowData = rowDatas[i];
					if (rowData) {
						strArr.push("<" + EventContextConstant.records + " pageindex=\"" + rowData.pageindex + "\">\n");
						var selStr = "";
						var selIndices = rowData.getSelectedIndices();
						if(selIndices != null && selIndices.length > 0)
							selStr += selIndices.join(",");
						strArr.push("<selected>" + selStr + "</selected>\n");
						var focusRowIndex = ds.getFocusRowIndex();
						if (focusRowIndex != -1)
							strArr.push("<focus>" + focusRowIndex + "</focus>\n");
						strArr.push(serializeRows(ds.currentKey, rowSet, rowData, type, ds));
						strArr.push("</" + EventContextConstant.records + ">");
					}
				}
			}
			
			strArr.push("</rowset>");
   		}
   	}

    strArr.push("</rowsets>");
    strArr.push("</dataset>");
    return strArr.join("");
 };
 
/**
 * 序列化行
 * @param currentKey
 * @param rowSet
 * @param rowData
 * @param type
 * @param ds
 * @return
 * @private
 */
function serializeRows(currentKey, rowSet, rowData, type, ds)
{  
	var strArr = new Array;
	if(type == null)
		type = "ds_current_line";
	if(type == "ds_current_line"){
		var count = rowData.getRowCount();
		var selIndices = rowData.selectedIndices;
		for(var i = 0; i < count; i ++){
			var sel = false;
			if(selIndices != null){
				for(var j = 0; j < selIndices.length; j ++){
					if(i == selIndices[j]){
						strArr.push(rowData.getRow(i).toXml());
						sel = true;
						break;
					}
				}
			}
			
			var focusRowIndex = ds.getFocusRowIndex();
			if (focusRowIndex != -1 && (!sel) && (i == focusRowIndex)){
				strArr.push(rowData.getRow(focusRowIndex).toXml());
				sel = true;
			}
			
			if(!sel){
				strArr.push("<" + EventContextConstant.erecord + " id=\"" + rowData.getRow(i).rowId + "\"/>");
			}
		}
	}
	else if(type == "ds_current_page"){
		if(currentKey != rowSet.keyvalue || rowSet.pageindex != rowData.pageindex){
			var count = rowData.getRowCount();
			for(var i = 0; i < count; i ++){
				strArr.push("<" + EventContextConstant.erecord + "/>");
			}
		}
		else
			$serializeRows(rowData.getRows(), strArr);
	}
	else if(type == "ds_current_key"){
		if(currentKey != rowSet.keyvalue){
			
		}
		else
			$serializeRows(rowData.getRows(), strArr);
	}
	else if(type == "ds_all_line"){
		$serializeRows(rowData.getRows(), strArr);
	}
	return strArr.join("");
}; 
 
 /**
 * 序列化行数据
 *
 * @param 行数据数组
 * @param 用户记录的String串
 * @private
 */
function $serializeRows(rowArr, strArr)
{  
	if(rowArr != null && rowArr.length > 0)
	{
		for(var i = 0, count = rowArr.length; i < count; i++){
        	strArr.push(rowArr[i].toXml(strArr));
        }
	} 
};
 
 