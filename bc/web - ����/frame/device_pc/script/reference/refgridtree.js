
function afterPageInit(){
	parent.setParameter("readDs","masterDs");
	window.onBeforeDispatchEvent2Ds = function(event, masterDsId){
		//如果是配置而来的参照，直接返回，不需要添加参数
		if(!isFromNc())
			return;
//		var treeComp = getComponent('reftree');
//		var treeLevels = treeComp.getAllLevels();
//		if(treeLevels != null){
//			var currentLevel = treeLevels[treeLevels.length-1];//最尾的TreeLevel（最后的TreeLevel和gridDs关联）
//			var dsId = currentLevel.datasetId;
//			if(masterDsId == 'masterDs_tree'){
				var detailDs = getDataset('masterDs');
				detailDs.addReqParameter('refCode', encodeURIComponent(getParameter('userObj')));
				return true;
//			}
//		}
//		return false;
	};
	
	getComponent('referenceLocateTextComp').onblur = function (){
		$locateAction();
	};
	getComponent('referenceLocateTextComp').onenter = function (){
		$locateAction();
	};
};

function externalInit(){
	
};

/*此参照节点是否来自NC模板*/
function isFromNc(){
	var nodeId = getParameter('nodeId');
	var refNodeInfo = parent.getRefNodeInfo(nodeId);
	var a = refNodeInfo.fromNc;
	return a;
};

/**
 * 刷新 动作
 */
function $cm_refreshCommand(){
	var treeComp = getComponent('reftree');
	var topLevel = treeComp.topTreeLevel;
	var ds = getDataset(topLevel.datasetId);
	var flag = isFromNc();
	
	if(flag){
		if(ds.reqParameterMap == null)
			ds.reqParameterMap = new HashMap();
		ds.addReqParameter('refCode', encodeURIComponent(getParameter('userObj')));
		ds.addReqParameter('isRefresh','true');
	}
	var loader = new DatasetLoader(ds);
	loader.load();
	if(flag)
		ds.reqParameterMap.remove('isRefresh');
};

function $locateAction(){
	 var g = getComponent('refgrid');
	 var ds = g.model.dataset ;
//	 var ds = getDataset('masterDs');
	 window.GridDatasetRows = ds.getRows();   
//	 alert(window.GridDatasetRows);
	 var rows = window.GridDatasetRows; 
	 if(rows == null || rows.length == 0)  
	     return ;
	 var textComp = getComponent('referenceLocateTextComp'); 
	 if(textComp.getValue() == null || textComp.getValue().trim() == ''){ 
		 return ; 
	 }
	 var locateValue = textComp.getValue();  
	 var ids = g.getVisibleColumnIds();//默认和ds中field一致
	 
	 var rowIndexArray = new Array();
	 for(var i = 0;i < rows.length; i++) {
	 	for(var k=0; k<ids.length; k++){
	 		var v = rows[i].getCellValue(ds.nameToIndex(ids[k]));
	 		if(v != null && v != ''){
		   	 	if(v.toLowerCase().indexOf(locateValue.toLowerCase()) != -1){  
		   	 		rowIndexArray.push(i);
		   	 		break;
		     	}
		    }
	 	}
	 }  
	 ds.deleteData(null);  
	 ds.initialize(null);  
	 if(rowIndexArray.length != 0){
	 	for(var m=0,len=rowIndexArray.length; m<len; m++)
	 		ds.addRow(rows[rowIndexArray[m]]); 
	 }
	 
	/*
	var textComp = getComponent('referenceLocateTextComp');
	if(textComp.getValue() == null || textComp.getValue().trim() == '')
		return;
	var locateValue = textComp.getValue();

	var ds = getDataset('masterDs');
	if(ds.reqParameterMap == null)
		ds.reqParameterMap = new HashMap();
	ds.addReqParameter('refCode', getParameter('userObj'));
	ds.addReqParameter('isLocate','true');
	ds.addReqParameter('locateValue', locateValue);
	var loader = new DatasetLoader(ds);
	loader.setKeyValue('LOCATE_KEY');
	loader.load();
	ds.reqParameterMap.remove('isLocate');
	*/
//	var g = getComponent('');
//	var cols = g.getVis
//	var rows = ds.getRows();
//	for(var i=0; i<rows.length; i++){
//		var v = rows[i].getCellValue(ds.nameToIndex(''));
//	}
	
};


