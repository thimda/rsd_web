function makeDragImage(treeNodeDragEvent){
	var node = treeNodeDragEvent.sourceNode;
	var row = node.nodeData;
	var div = $ce('DIV');
	div.style.width = "100px";
	div.style.height = "100px";
	div.style.border = "1px solid red";
	return div;
}
/*
$(document).ready(function(){

alert(document.getElementById("$d_iframe_tmp8").outerHTML);
});

function beginDesign(){
	var editorWindow=document.getElementById("iframe_editor").contentWindow;
	var textAreaWindow=editorWindow.document.frames[0];
	var eidtorDocument=textAreaWindow.FCK.EditorDocument;
	//var str=document.getElementById("iframe_tmp").document.head.innerHTML
		var str=document.getElementById("iframe_tmp").document.body.innerHTML;
	eidtorDocument.body.innerHTML=str;
	//alert(editor.FCK);
	//alert(editor.EditorDocument);
	
}

window.setTimeout("beginDesign()",10000);

*/


function onLoadStruct(id){
	
	var editorIFrame = document.getElementById("iframe_tmp"); 
	editorIFrame.contentWindow.toClickSelected(id);
//	if(document.all){
//		debugger;
//		var editorIFrame = document.getElementById("iframe_tmp"); 
//		alert(editorIFrame.contentWindow.document.getElementById(id).click)
//		editorIFrame.contentWindow.document.getElementById(id).click();
//	} 
//	else{ 
//		var editorIFrame = document.getElementById("iframe_tmp"); 
//		var evt = editorIFrame.contentWindow.document.createEvent("MouseEvents"); 
//       	evt.initEvent("click",true,true); 
//		editorIFrame.contentWindow.document.getElementById(id).dispatchEvent(evt); 
//     }   
}

function deleteFromStruct(id){
	
	var editorIFrame = document.getElementById("iframe_tmp"); 
	editorIFrame.contentWindow.toDeleteSelected(id);
}


function addFromStruct(id){
	var editorIFrame = document.getElementById("iframe_tmp");
	editorIFrame.contentWindow.toAddSelected(id);
}

function callBack(obj, oper){

	var proxy = new ServerProxy(null, null, false);   
	proxy.addParam("listener_class", "nc.uap.lfw.pa.PaPropertyDatasetListener");
			proxy.addParam("divId", obj.id);
			proxy.addParam("uiId", obj.uiid);
			proxy.addParam("eleId", obj.eleid);
			proxy.addParam("widgetId", obj.widgetid);
			proxy.addParam("type", obj.type);
			proxy.addParam("objType", obj.objType);
			proxy.addParam("oper", oper);
			proxy.addParam("subeleid", obj.subeleid);
			proxy.addParam("renderType", obj.renderType); // add by renxh 2011-10-27
			
			
			var sbr = new SubmitRule();  
			//settings中的提交规则
			var pWdRule = new WidgetRule("settings");
				var pFormRule = new FormRule("hintform","all_child");
				pWdRule.addFormRule(pFormRule);
				var dsRule = new DatasetRule("ds_middle","ds_all_line");
				pWdRule.addDsRule("ds_middle", dsRule);
			
			//data中的提交规则
			var dataWdRule = new WidgetRule("data");
				var treeRule = new TreeRule("entitytree", "tree_current_parent_root_tree");
				dataWdRule.addTreeRule(treeRule);
				var structDsRule = new DatasetRule("ds_struct", "ds_all_line");
				dataWdRule.addDsRule("ds_struct", structDsRule)
			
			sbr.addWidgetRule("settings",pWdRule);
			sbr.addWidgetRule("data", dataWdRule);
	 		proxy.setSubmitRule(sbr); 
	 		proxy.execute();
	 		
	triggerEditorEvent("showPropertiesView",obj);
}

function triggerSaveEvent(){

	try{
		var proxy = new ServerProxy(null, null, true);
		proxy.addParam("listener_class", "nc.uap.lfw.pa.PaPropertySaveListener");
		
		var sbr = new SubmitRule();
		var setWdRule = new WidgetRule("settings");
		
		var pFormRule = new FormRule("hintform","all_child");
		setWdRule.addFormRule(pFormRule);
		
		var dsRule = new DatasetRule("ds_middle", "ds_all_line");
		setWdRule.addDsRule("ds_middle", dsRule);
		
		sbr.addWidgetRule("settings", setWdRule);
		proxy.setSubmitRule(sbr);
		proxy.execute();
	}catch(e){
		alert(e);
	}
	
}


function refreshDs(){
	var dataset = pageUI.getWidget("data").getDataset("ctrlds");
	var remaindKey = dataset.currentKey;
	dataset.currentKey = "aaa";
	dataset.currentKey = remaindKey ;
}

function saveForEclipse(){
	//pageUI.getWidget("settings").getMenu("menu_set").getMenu("save").onclick();
	triggerSaveEvent();
}
/**
 * 清除事件状态
 */
function clearEventStatus(){
	try{
		window.status = "stopEvent";
	}catch(e){
		//alert(e);
	}
}

/**
 * 创建一个Eclipse交互事件
 * @param {} type
 * @param {} source
 * @return {}
 */
function triggerEditorEvent(type,source){
	var eventContext = "event:";
	eventContext += type;
	for(i in source){
		eventContext += ",,,";
		eventContext += i;
		eventContext += ":";
		eventContext += source[i];
	}
	window.status = eventContext;
	return true;
}

function setEditorState(){
	try{
		triggerEditorEvent("changeFileStatus" ,{});
	}catch(e){
		//alert(e);
	}
//	document.getElementById("$d_label_editor").click();
//	window.eclipseState = true;
}
document.getEditorState = function(){
	if(window.eclipseState){
		return window.eclipseState;
	}else{
		return false;	
	}
}
document.resetEditorState = function(){
	window.eclipseState = false;
}
function setSaveState(){
	triggerEditorEvent("changeSaveStatus",{})
}
function setSession(sessionId){
	var param = {};
	param.sessionID = sessionId;
	triggerEditorEvent("setSessionId" ,param);
};
setSession(window.JSessionID);