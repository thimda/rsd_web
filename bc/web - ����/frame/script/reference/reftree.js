function beforePageInit(){
};
function afterPageInit(){
	getComponent('referenceLocateTextComp').onblur = function (){
		$locateAction();
	};
	getComponent('referenceLocateTextComp').onenter = function (){
		$locateAction();
	};
};

/*此参照节点是否来自NC模板*/
function isFromNc(){
	var nodeId = getParameter('nodeId');
	var refNodeInfo = parent.getRefNodeInfo(nodeId);
	return refNodeInfo.fromNc();
};

/**
 * 刷新 动作
 */
function $cm_refreshCommand(){
//	  var cardLayout = getCardLayout('refTreeCard');
	/*
	  var modeBtn = getComponent('changeViewModeBtn');
	  var curPage = cardLayout.getCurrentPageIndex();
	  if(curPage == 1){
	  	modeBtn.changeText('表');
	  	cardLayout.nextPage();
	  	modeBtn.changeImage('/lfw/frame/themes/ncclassic/images/reference//refgrid.gif');
	  }
	  */
	  var treeComp = getComponent('reftree');
	  var topLevel = treeComp.topTreeLevel;
	  var ds = getDataset(topLevel.datasetId); 
	  var flag = isFromNc();
	  if(flag){
		  if(ds.reqParameterMap == null)
		  	 ds.reqParameterMap = new HashMap();
		  ds.addReqParameter('isRefresh','true');
	  }
	  var loader = new DatasetLoader(ds);
	  loader.load();
	  if(flag)
	  	ds.reqParameterMap.remove('isRefresh');
};

function $locateAction(){
	var treeComp = getComponent('reftree');
	var levels = treeComp.getAllLevels();
	for(var i = 0; i < levels.length; i ++){
		var levelArr = levels[i];
		for(var j = 0; j < levelArr.length; j++){
			var currentDs = getDataset(levelArr[j].datasetId);
	  		var lfs = levelArr[j].labelFields;
	  		if($locateAction1(currentDs, lfs))
	    		break;
		}
	}
};

function $locateAction1(ds, lfs){
	  var textComp = getComponent('referenceLocateTextComp'); 
	  if(textComp.getValue() == null || textComp.getValue().trim() == '') 
		  return ;  
	  var locateValue = textComp.getValue();  
	  var rows = ds.getRows();
	  var rowIndexArray = new Array();
	  for(var i=0,len=ds.getRowCount(); i<len;i++){
	  	for(var k=0; k<lfs.length; k++){
	  		var v = rows[i].getCellValue(ds.nameToIndex(lfs[k]));
	  		if(v != null && v != ''){
		  		if(v.toLowerCase().indexOf(locateValue.toLowerCase()) != -1){
		  			rowIndexArray.push(i);
		  			break;
		  		}
	  		}
	  	}
	  }
	  if(rowIndexArray.length != 0){
	 	for(var m=0,len=rowIndexArray.length; m<len; m++){
	 		ds.setRowSelected(rowIndexArray[m]);
	 	}
	 }else{
	 	return false;
	 }
};

function otherAction(){
	if(typeof selfBeforeReferenceOk != "undefined"){
		if(selfBeforeReferenceOk() == false)
			return;
	}
	var readDsStr = getParameter("readDs");
	var readDs = getDataset(readDsStr);
	
	var returnFunc = getParameter("returnFunc");
	if(returnFunc != null) {
		// window为参照子窗口的运行环境,可以从中获取参照中的各个ds
		var result = parent[returnFunc](readDs, window);
		if(result == false)
			return;
	}
	else{
		var writeDsStr = getParameter("writeDs");
		var readFields = getParameter("readFields").split(",");
		var writeFields = getParameter("writeFields").split(",");
		var multiSel = getParameter("multiSel");
		if(multiSel == null){
			var grid = getComponent("refgrid");
			if(grid != null)
				multiSel = grid.isMultiSelWithBox;
		}
		var rows = null;
		if(multiSel)
			rows = readDs.getAllSelectedRows();
		else{
			var row = readDs.getSelectedRow();
			if(row != null)
				rows = [row];
		}
		
			
		// 读取选中行数据
		//var row = readDs.getSelectedRow();
		if(readDs == null){
			alert("读取的Dataset id不正确:" + readDsStr);
			return;
		}
		//beforeReferenceOk()方法是在ReferenceOk之前调用，返回true则之后不执行，否则继续执行 
		var backValue = false;
		if(typeof beforeReferenceOk != "undefined"){
			if(beforeReferenceOk(rows)){ 
				parent.hideDialog();
				return;
			}
		}
	
		var writeDs = parent.getDataset(writeDsStr);
		if(writeDs == null){
			alert("写入的Dataset id不正确:" + writeDsStr);
			return;
		}
		/*
		if(rows == null)
		{
			alert("请先选择节点!");
			return;
		}
		*/
		for(var i = 0, count = readFields.length; i < count; i ++)
		{
			var rIndex = readDs.nameToIndex(readFields[i]);
			var wIndex = writeDs.nameToIndex(writeFields[i]);
			var targetRow = writeDs.getSelectedRow();
			var targetRowIndex = writeDs.getRowIndex(targetRow);
			var result = "";
			if(rows != null && rows.length > 0)
			{
				for(var j = 0, count1 = rows.length; j < count1; j ++){
					result += rows[j].getCellValue(rIndex);
					if(j != rows.length - 1)
						result += ",";
				}
			}
			else
				result = null;
			writeDs.setValueAt(targetRowIndex, wIndex, result);
		}
		try{
			parent.dispatchRefEvent2RefNode();
		}
		catch(error){
			log("" + error.name + ":" + error.message);
		}
	}
	parent.hideDialog();
};