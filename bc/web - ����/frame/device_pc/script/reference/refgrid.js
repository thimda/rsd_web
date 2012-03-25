

function beforePageInit(){
};
function afterPageInit(){
	 var g = getComponent('refgrid');
	 var ds = g.model.dataset;  
	 var rows = ds.getRows();  
	 window.GridDatasetRows = rows; 
	// 实现Grid参照在用户输入数据时能够打开对话框对数据进行过滤。
	 window.processBlurValue = function(blurField, blurValue){
		 if(ds.reqParameterMap == null){ 
		 	ds.reqParameterMap = new HashMap(); 
		 }
		 ds.addReqParameter('refCode', getParameter('userObj')); 
	
		 //向后台标识模糊匹配
		 ds.addReqParameter('isBlur', 'true');  
		 ds.addReqParameter('blurField', blurField);  
		 ds.addReqParameter('blurValue', blurValue);  
		 
		 var loader = new DatasetLoader(ds); 
		 loader.setKeyValue('BLUR_KEY'); 
		 loader.load(); 
		 ds.reqParameterMap.remove('isBlur'); 
	 };
	getComponent('referenceLocateTextComp').onblur = function (){
		$locateAction();
	};
	getComponent('referenceLocateTextComp').onenter = function (){
		$locateAction();
	};
};
/**
 * 刷新 动作
 */
function $cm_refreshCommand(){
	var g = getComponent('refgrid');
	 var ds = g.model.dataset; 
	var flag = isFromNc();
	if(flag){
		 if(ds.reqParameterMap == null)
			 ds.reqParameterMap = new HashMap();
		 ds.addReqParameter('refCode', getParameter('userObj'));
		 ds.addReqParameter('isRefresh','true');
	}
	
	// 在Grid类型的参照中,刷新完成之后需要将最新的Rows进行记录
	var args = new Array;
    args.push(ds);
	var loader = new DatasetLoader(ds);
    loader.setReturnFunc(refReturnFun);
    loader.setReturnArgs(args);
    loader.load();
	function refReturnFun(args, exception){
		if(exception){
	 		if(flag)
	 			ds.reqParameterMap.remove('isRefresh');
	 		window.GridDatasetRows = ds.getRows(); 
		}
	};
}

/*定位*/
function $locateAction(){
//	 var ds = getDataset('masterDs'); 
	 var g = getComponent('refgrid');
	 var ds = g.model.dataset ;
	 var rows = window.GridDatasetRows; 
	 if(rows == null || rows.length == 0)  
	     return ;  
	 var textComp = getComponent('referenceLocateTextComp'); 
	 if(textComp.getValue() == null || textComp.getValue().trim() == ''){ 
	 	ds.deleteData(null);  
 	    ds.initialize(null); 
	 	for(var i=0;i<rows.length;i++)
	 		ds.addRow(rows[i]);
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
	 	for(var m=0,len=rowIndexArray.length; m<len; m++){
	 		ds.addRow(rows[rowIndexArray[m]]); 
	 	}
	 }
};
/*此参照节点是否来自NC模板*/
function isFromNc(){
	var nodeId = getParameter('nodeId');
	var refNodeInfo = parent.getRefNodeInfo(nodeId);
	return refNodeInfo.fromNc;
}