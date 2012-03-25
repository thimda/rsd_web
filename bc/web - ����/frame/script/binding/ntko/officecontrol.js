var OfficeControl;
if (OfficeControl == undefined) {
	OfficeControl = function (settings) {
		this.initOfficeControl(settings);
		this.load();
	};
}

OfficeControl.prototype.initOfficeControl = function (userSettings) {
	try {
		this.customSettings = {};
		this.settings = {};
		//初始化参数
		this.initSettings(userSettings);
		this.SelectFile = function(filetype){};
		if(userSettings.SelectFile)
			this.SelectFile = userSettings.SelectFile;
		this.LoadBookMarks = function(){};
		if(userSettings.LoadBookMarks)
			this.LoadBookMarks = userSettings.LoadBookMarks;
		this.OnSheetChanged = function(SheetName, row, col){};
		if(userSettings.OnSheetChanged)
			this.OnSheetChanged = userSettings.OnSheetChanged;
			
		this.OnSheetBeforeDoubleClick = function(SheetName, row, col, cancel){};
		if(userSettings.OnSheetBeforeDoubleClick)
			this.OnSheetBeforeDoubleClick = userSettings.OnSheetBeforeDoubleClick;
		
		this.OnSheetSelectionChange = function(SheetName, row, col){};
		if(userSettings.OnSheetSelectionChange)
			this.OnSheetSelectionChange = userSettings.OnSheetSelectionChange;
			
		this.OnSheetFollowHyperlink = function(Sheet, target){};
		if(userSettings.OnSheetFollowHyperlink)
			this.OnSheetFollowHyperlink = userSettings.OnSheetFollowHyperlink;
			
	} catch (ex) {		
		throw ex;
	}
};
//初始化数据
OfficeControl.prototype.initSettings = function (userSettings) {
	this.ensureDefault = function (settingName, defaultValue) {
		var setting = userSettings[settingName];
		if (setting != undefined) {
			if (typeof(setting) === "object" && !(setting instanceof Array)) {
				var clone = {};
				for (var key in setting) {
					if (setting.hasOwnProperty(key)) {
						clone[key] = setting[key];
					}
				}
				this.settings[settingName] = clone;
			} else {
				this.settings[settingName] = setting;
			}
		} else {
			this.settings[settingName] = defaultValue;
		}
	};
	
	//脚本参数
	this.ensureDefault("TargetElement", "");//目标控件
	this.ensureDefault("userName", "");//用户名
	this.ensureDefault("pk", "");//文档key
	this.ensureDefault("url", "");//加载远程路径
	this.ensureDefault("saveurl","");//保存url
	this.ensureDefault("trackRevisions",true);//是否使用痕迹
	this.ensureDefault("autoSave",true);//是否自动保存
	this.ensureDefault("readonly",false);//是否自动保存
	this.ensureDefault("canopen",true);//打开按钮
	this.ensureDefault("occupymsg","");//消息
	this.ensureDefault("showRevisions",false);//显示痕迹	
	//系统参数
	this.ensureDefault("saveTTL", 0);//自动保存
	this.ensureDefault("rootpath","portal");//lfw跟路径
	this.ensureDefault("Caption","双击最大化编辑");//标题
	this.ensureDefault("MakerCaption","用友软件股份有限公司");//公司名称
	this.ensureDefault("CustomMenuCaption","扩展");//自定义菜单标题
	this.ensureDefault("signkey","UFIDA.NC.Uap.Office.SignKey");//自定义菜单标题
	this.ensureDefault("isOpened", false);//文件是否已打开
	this.ensureDefault("width", "100%");//width
	this.ensureDefault("height", "100%");//height
	//office控件参数
	this.ensureDefault("Titlebar", false);//标题栏显示
	this.ensureDefault("Statusbar", true);//状态栏显示
	this.ensureDefault("Menubar", true);//菜单栏显示
	this.ensureDefault("Toolbars", true);//工具栏显示
	this.ensureDefault("FileSave", false);//状态栏显示
	this.ensureDefault("FileSaveAs", false);//状态栏显示
	
	this.ensureDefault("FileNew", false);//新建文件
	this.ensureDefault("FileOpen", true);//打开文件菜单
	this.ensureDefault("FileClose", false);//关闭文件菜单
	
	this.ensureDefault("IsStrictNoCopy", false);//禁用剪切板
	this.ensureDefault("IsNoCopy", false);//禁用office复制
	this.ensureDefault("PrintModel", 2);//打印控制
	this.ensureDefault("IsUseCertificate", false);//证书
	this.ensureDefault("sysid", "");//系统ID
	this.ensureDefault("savePath", "portal/doc");//系统ID
	
	this.ensureDefault("DefaultTemplete", false);//是否使用默认模板
	this.ensureDefault("demo", false);//演示
	delete this.ensureDefault;
};
OfficeControl.prototype.ChangeSetting = function(key,value){
	this.settings[key] = value;
}
//加载控件
OfficeControl.prototype.load = function(){
		//创建对象
		this.loadObject();
		//设置初始化参数
		this.curobj = document.getElementById("ntkoofficectl");
		
		//初始控件化参数
		this.initObject();
		//创建菜单
		this.createMenu();
		//注册事件
		this.addEvent();
		//加载文档
		this.AfterLoad();
}
//加载控件
OfficeControl.prototype.loadObject = function () {
	var targetElement, tempParent;
	targetElement = document.getElementById(this.settings.TargetElement) ;
	if (targetElement == undefined) {
		throw "未能找到主控件: " + this.settings.TargetElement;
	}
	
	var wrapperType = (targetElement.currentStyle && targetElement.currentStyle["display"] || window.getComputedStyle && document.defaultView.getComputedStyle(targetElement, null).getPropertyValue("display")) !== "block" ? "span" : "div";
	
	tempParent = document.createElement(wrapperType);
	tempParent.innerHTML = this.getObjectHTML();	// Using innerHTML is non-standard but the only sensible way to dynamically add Flash in IE (and maybe other browsers)
	targetElement.parentNode.replaceChild(tempParent, targetElement);
};
//获取office 控件的html对象
OfficeControl.prototype.getObjectHTML = function () {
	var htmlstr = "<!-- 用来产生编辑状态的ActiveX控件的JS脚本-->"
		+'<!-- 因为微软的ActiveX新机制，需要一个外部引入的js-->   '
		+'<object id="ntkoofficectl" classid="clsid:A39F1330-3322-4a1d-9BF0-0BA2BB90E970"'
		+'codebase="'+ this.settings.rootpath 
		+'/office/plugin/ntko/OfficeControl.cab#version=5,0,1,1" width="' 
		+ this.settings.width + '" height="' + this.settings.height +'">'
		+'<param name="IsUseUTF8URL" value="-1">   '
		+'<param name="IsUseUTF8Data" value="-1">   '
		+'<param name="BorderStyle" value="1">   '
		+'<param name="BorderColor" value="14402205">   '
		+'<param name="TitlebarColor" value="42768">   '
		+'<param name="TitlebarTextColor" value="0">   '
		+'<param name="MenubarColor" value="14402205">   '
		+'<param name="MenuButtonColor" VALUE="16180947">   '
		+'<param name="MenuBarStyle" value="3">   '
		+'<param name="MenuButtonStyle" value="7">   '
		+'<param name="WebUserName" value="NTKO">   '
		+'<param name="Caption" value="'+ this.settings.Caption+'">'
		+'<param name="Menubar" value="-1">'
		+'<param name="IsNoCopy" value="-1">'
		+'<param name="CustomMenuCaption" value="'+this.settings.CustomMenuCaption+'">'
		+'<param name="MakerCaption" value="'+ this.settings.MakerCaption+'">'
		+'<param name="MakerKey" value="0AD89F6F2214A7E369D4EA6AC028791E54C81FDE">'
		+'<param name="ProductCaption" value="版本:6.1">'
		+'<param name="ProductKey" value="04716DF042F78186FD0F4782CDD0386C2B601A08">'
		+'<param name="WebUserName" value="${userName}">'
		+'<SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>   '
		+'</object>';
	return htmlstr;
};
//office 初始化设置
OfficeControl.prototype.initObject= function (){
    var intervalId=0;
	// 基础设置
	//TANGER_OCX.CreateNew("Word.Document");
	//==== Start ==== Modify by 姚广 2011-5-12
	//修改原因：不需要单独进行文件创建动作
	//TANGER_OCX.CreateNew("Word.Document");
	//==== Start ==== Modify by 姚广 2011-5-12
	
	this.curobj.Titlebar = this.settings.Titlebar;//标题栏
	this.curobj.Statusbar = this.settings.Statusbar;//状态栏
	this.curobj.Menubar = this.settings.Menubar;//菜单
	this.curobj.Toolbars = this.settings.Toolbars;//工具栏
	this.curobj.FileSave = this.settings.FileSave;//保存
	this.curobj.FileSaveAs = this.settings.FileSaveAs;//另存为
	
	this.curobj.FileNew = this.settings.FileNew;//新建文件
	this.curobj.FileOpen = this.settings.FileOpen;//打开文件菜单
	this.curobj.FileClose = this.settings.FileClose;//关闭文件菜单
	
	//ntkoofficectl.EnterRevisionMode = true;
	this.curobj.IsStrictNoCopy = this.settings.IsStrictNoCopy;//禁用剪切板
	this.curobj.IsNoCopy= this.settings.IsNoCopy;//禁用office复制
	

	//最后修改戳
	this.settings.lastModifySpan = 0;
}
//创建菜单
/*痕迹
 * 	显示痕迹
 * 	隐藏痕迹
 * 	接受所有修订
 *  拒绝所有修订
 * 	手写批注
 * 
 * 模板
 * 	套红模板
 * 		头部套红
 * 		底部套红？
 * 	文档模板
 * 		
 * 签章
 * 	手写批注
 * 	手写签名
 * 	本地签章
 * 	服务器签章
 *  UKey签章
 *  标签替换
 * 	
 */
OfficeControl.prototype.createMenu=function(){
	//文件菜单
	if(this.settings.FileNew){
		//新建
		this.curobj.AddFileMenuItem("新建Word",false,false,0);	
		this.curobj.AddFileMenuItem("新建Excel",false,false,1);
		this.curobj.AddFileMenuItem("新建WPS",false,false,2);
		this.curobj.AddFileMenuItem("新建ET",false,false,3);
		this.curobj.AddFileMenuItem("",true,false,4);
	}
	this.curobj.AddFileMenuItem("保存到服务器",false,false,5);
	this.curobj.AddFileMenuItem("",true,false,6);

	//自定义菜单
	this.curobj.AddCustomMenu2(0," 留痕 ");
	this.curobj.AddCustomMenuItem2(0,0,-1,false,"显示痕迹",false);
	this.curobj.AddCustomMenuItem2(0,1,-1,false,"隐藏痕迹",false);
	if(false){//痕迹的操作通过封装接口实现
		this.curobj.AddCustomMenuItem2(0,2,-1,false,"接受所有修订",false);
		this.curobj.AddCustomMenuItem2(0,3,-1,false,"拒绝所有修订",false);
	}
	this.curobj.AddCustomMenuItem2(0,4,-1,false,"手写批注",false);

	//模板的控件默认操作，通常不使用
	if(this.settings.DefaultTemplete){
		this.curobj.AddCustomMenu2(1," 模板 ");
		this.curobj.AddCustomMenuItem2(1,0,-1,false,"头部套红",false);
		this.curobj.AddCustomMenuItem2(1,1,-1,false,"底部套红",false);
		this.curobj.AddCustomMenuItem2(1,2,-1,false,"-",true);
		this.curobj.AddCustomMenuItem2(1,3,-1,false,"文档模板",false);
		this.curobj.AddCustomMenuItem2(1,4,-1,false,"-",true);
		this.curobj.AddCustomMenuItem2(1,5,-1,false,"标签替换",false);
	}
	this.curobj.AddCustomMenu2(2," 签章 ");
	this.curobj.AddCustomMenuItem2(2,0,-1,false,"手写批注",false);
	this.curobj.AddCustomMenuItem2(2,1,-1,false,"手写签名",false);	
	this.curobj.AddCustomMenuItem2(2,2,-1,false,"本地签章",false);
	if(this.settings.DefaultTemplete){
		this.curobj.AddCustomMenuItem2(2,3,-1,false,"服务器签章",false);
	}
	this.curobj.AddCustomMenuItem2(2,4,-1,false,"UKey签章",false);
	
	if(this.settings.demo){
		this.curobj.AddCustomMenu2(9,"demo");
		this.curobj.AddCustomMenuItem2(9,0,-1,false,"创建标签",false);
		this.curobj.AddCustomMenuItem2(9,1,-1,false,"替换",false);
		this.curobj.AddCustomMenuItem2(9,4,-1,false,"正文替换",false);
		this.curobj.AddCustomMenuItem2(9,2,-1,false,"",true);
		this.curobj.AddCustomMenuItem2(9,3,-1,false,"插入图片",false);
		this.curobj.AddCustomMenuItem2(9,5,-1,false,"获取内容",false);
		this.curobj.AddCustomMenuItem2(9,6,-1,false,"重新打开",false);
	}
}


OfficeControl.prototype.newFile=function(doctype){
	var ProgID = this.getProgIDbyDoctype(doctype);
	if(ProgID == ""){
		alert("未支持类型");
		return;
	}
	this.curobj.CreateNew(ProgID);
}
/**
 * 1=word；2=Excel.Sheet或者 Excel.Chart ；
 * 3=PowerPoint.Show； 4= Visio.Drawing；
 * 5=MSProject.Project； 6= WPS Doc；
 * 7:Kingsoft Sheet
 */
OfficeControl.prototype.getProgIDbyDoctype= function(doctype){
	var ProgID = "";
	switch(doctype){
		case 1:
			ProgID = "Word.Document";
		break;
		case 2:
			ProgID = "Excel.Sheet";
		break;
		case 6:
			ProgID = "WPS.Document";			
		break;
		case 7:
			ProgID = "ET.WorkBook";			
		break;
	}
	return ProgID;
}

//office控件加载后函数
OfficeControl.prototype.AfterLoad= function (pk){
	var curpk = this.settings.pk;
	if(pk)
		curpk = pk;
	var loadurl = this.settings.url + curpk;
	if(loadurl)
	{
		if(pk)
			this.curobj.OpenFromURL(loadurl, true, false);
		else
			this.curobj.BeginOpenFromURL(loadurl, true, false);
		this.curobj.IsShowToolMenu = true;
		
		var occupymsg= this.settings.occupymsg;
		occupymsg=decodeURIComponent(decodeURIComponent(occupymsg));
		if(occupymsg.length!=0){
			  alert(occupymsg);
		}
	}
}

OfficeControl.prototype.addEvent= function (){
	//菜单事件
	this.curobj.attachEvent("OnCustomMenuCmd2",
		(function(obj){
			return function(menuPos,submenuPos,subsubmenuPos,menuCaption,myMenuID){				
				return customMenuCmd(obj,menuPos,submenuPos,subsubmenuPos,menuCaption,myMenuID);
			};
		})(this));
	//自定义文件菜单事件
	this.curobj.attachEvent("OnCustomFileMenuCmd",
		(function(obj){return function(menuIndex,menuCaption,menuID){return  customFileMenuCmd(obj,menuIndex,menuCaption,menuID);}})(this));
	//打开事件
	this.curobj.attachEvent("OnDocumentOpened",
			(function(obj){return function(filepath,doc){return  docOpened(obj,filepath,doc);}})(this));
			
	this.curobj.attachEvent("OnDocumentClosed",
			(function(obj){return function(){return  docCloseed(obj);}})(this));
}
OfficeControl.prototype.addExcelEvent=function(){
		if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application){
			this.curobj.attachEvent("OnSheetChange",
				(function(obj){return function(SheetName, row, col){return  SheetChanged(obj,SheetName, row, col);}})(this));
			this.curobj.attachEvent("OnSheetBeforeDoubleClick",
				(function(obj){return function(SheetName, row, col,Cancel){return  SheetBeforeDoubleClick(obj,SheetName, row, col,Cancel);}})(this));
			this.curobj.attachEvent("OnSheetSelectionChange",
				(function(obj){return function(SheetName, row, col){return  SheetSelectionChange(obj,SheetName, row, col);}})(this));
			this.curobj.attachEvent("OnSheetFollowHyperlink",
				(function(obj){return function(Sheet, LinkTarget){alert("hello,world"); return  SheetFollowHyperlink(obj,Sheet, LinkTarget);}})(this));
		}
}
/**
 * 自定义文件件菜单
 */
function customFileMenuCmd(ofctl,menuIndex,menuCaption,menuID){
	if(ofctl.settings.readonly)
	{
		return;
	}
	switch(menuID)
		{
			case 0:
				ofctl.newFile(1);
			break;
			case 1:
				ofctl.newFile(2);
			break;
			case 2:
				ofctl.newFile(6);
			break;
			case 3:
				ofctl.newFile(7);
			break;
			case 5:
				ofctl.saveFileToURL();
			break;
		}
}

//菜单事件
function customMenuCmd(ofctl,menuPos,submenuPos,subsubmenuPos,menuCaption,myMenuID){
	if(ofctl.settings.readonly)
	{
		return;
	}
	try
	{
		switch(menuPos)
		{
			case 0://留痕
				switch(submenuPos)
				{
					case 0://显示痕迹
					ofctl.showRevisions(true);
					break;
					case 1://隐藏痕迹
					ofctl.showRevisions(false);
					break;
					case 2://接受全部修订
					ofctl.acceptAllRevision();
					break;
					case 3://拒绝所有修订
					ofctl.rejectAllRevisions();
					break;
					case 4://手写批注
					ofctl.addHandSign();
					break;
				}
			break;
			case 1://套红
				switch(submenuPos)
				{
					case 0://头部套红
						if(!ofctl.settings.isOpened){return;}
						if(ofctl.curobj.doctype!=1 && ofctl.curobj.doctype!=6){
							alert("不能在该类型文档中使用头部套红.");
							return;
						}
						if(ofctl.SelectFile){
							ofctl.SelectFile("redhead",ofctl.curobj.doctype);
						}
					break;
					case 1://底部套红
						if(!ofctl.settings.isOpened){return;}
						if(ofctl.curobj.doctype!=1 && ofctl.curobj.doctype!=6){
							alert("不能在该类型文档中使用头部套红.");
							return;
						}
						if(ofctl.SelectFile){
							ofctl.SelectFile("redhead",ofctl.curobj.doctype);//暂时不区分头部、底部模板							
							//ofctl.SelectFile("redend",ofctl.curobj.doctype);
						}
					//ofctl.insertRedEndFromUrl();
					break;
					case 2://分隔符
					break;
					case 3://文档模板
						if(!ofctl.settings.isOpened){return;}
						if(ofctl.SelectFile){
							ofctl.SelectFile("templete",ofctl.curobj.doctype);
						}
					break;
					case 5://标签替换
					ofctl.replaseBookMark();
					break;
				}
			break;
			case 2://签章
				switch(submenuPos)
				{
					case 0://手写批注
						ofctl.addHandSecSignUnprotect();
					break;
					case 1://手写签名
						ofctl.addHandSecSign();
					break;
					case 2://本地签章
						ofctl.addLocalSign();
					break;
					case 3://服务器签章
						if(!ofctl.settings.isOpened){return;}
						if(ofctl.curobj.doctype==1||ofctl.curobj.doctype==2){
							if(ofctl.SelectFile){
								ofctl.SelectFile("sign","");
							}else{
								alert("不能在该类型文档中使用签章");			
							}
						}
					break;
					case 4://UKey签章
						alert("需根据UKey类型调用接口");
					break;
				}
			break;
			case 9://demo
				switch(submenuPos)
				{
					case 0://创建标签
						ofctl.addBookMark('bookmasktest','this is a bookmark create test')
					break;
					case 1://替换标签
						var bookmarks = new  HashMap();
						bookmarks.put('bookmasktest','this is a bookmark replease test');
						ofctl.replaseBookMark(bookmarks);
					break;
					case 4://正文替换
						var markname = "zhengwen";
						var url = "/portal/pt/doc/file/down?id=0001AA1000000000480R";
						ofctl.replaseBookMarkToFile(markname,url);
						break;
					case 5://读取内容
						var text = ofctl.getContent(0,20);
						alert(text);
						break;
					case 6://重新load正文
						ofctl.AfterLoad();
						break;
					case 3://插入图片
						var url="D:\\ref_on.gif";
						url = "http://20.1.85.63/lfw/frame/themes/rigo/images/calendar/close_off.gif";
						//var url="";
						
						var range = ofctl.GetActiveRange();						
						var top = 0;
						var left = 0;
						var width = 15;
						var height = 15;
						if(range != null){
							top = range.Top;
							left = range.Left + range.Width -15;
							height = range.Height;
							if(left < 0)
								left = 0;
						}
						var shape = null;
						var shapes = ofctl.GetActiveShapes();
						if(null != shapes.Count){
							for(var i=0;i<shapes.Count;i++){
								if(shapes.Item(i).Name = "refIndexpic"){
									shape = shapes.Item(i);
									break;
								}
							}
						}
						if(shape == null){
							shape = ofctl.AddPicByApplication(url,true,true,left,top,15,height);
							shape.Name = "refIndexpic";
						}
						else{
							shape.Left = left;
							shape.Top = top;
						}
					break;
				}
			break;
			default:
				alert("未处理菜单项:" + menuCaption);
			break;
		}
	} catch(ex){
		alert(ex.message);
	}
}

//打开文件事件
function docOpened(ofctl,filepath,doc){
	if(ofctl.curobj)
	{
	   try
	   {
	   		ofctl.settings.isOpened = true;
			doc = ofctl.curobj.ActiveDocument;
			//痕迹用户名
			doc.Application.UserName = ofctl.settings.userName;
			ofctl.showRevisions(ofctl.settings.showRevisions);
			if(ofctl.settings.trackRevisions){
				ofctl.trackRevisions(true);
				ofctl.curobj.IsShowToolMenu = false;
	/*			
				ofctl.curobj.ActiveDocument.CommandBars("Reviewing").Enabled = false;
				ofctl.curobj.ActiveDocument.CommandBars("Track Changes").Enabled = false;
	
				ofctl.curobj.ShowCommandBar("Reviewing",true);
				doc.Application.CommandBars("Reviewing").Controls(5).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(6).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(7).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(8).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(9).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(10).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(11).Enabled = false;
				doc.Application.CommandBars("Reviewing").Controls(13).Enabled = false;
				doc.CommandBars("Reviewing").Controls(5).Enabled = false;
				doc.CommandBars("Reviewing").Controls(6).Enabled = false;
				doc.CommandBars("Reviewing").Controls(7).Enabled = false;
				doc.CommandBars("Reviewing").Controls(8).Enabled = false;
				doc.CommandBars("Reviewing").Controls(9).Enabled = false;
				doc.CommandBars("Reviewing").Controls(10).Enabled = false;
				doc.CommandBars("Reviewing").Controls(11).Enabled = false;
				doc.CommandBars("Reviewing").Controls(13).Enabled = false;
				
				doc.Application.CommandBars("Track Changes").Controls(7).Enabled = false;
				doc.Application.CommandBars("Track Changes").Controls(8).Enabled = false;
				doc.Application.CommandBars("Track Changes").Controls(9).Enabled = false;
				doc.Application.CommandBars("Track Changes").Controls(10).Enabled = false;
				doc.Application.CommandBars("Track Changes").Controls(12).Enabled = false;
				*/
		 		//setInterval(checkTrackRevisions, 4000);
			}else{
				ofctl.trackRevisions(true);
			}
			ofctl.setReadOnly(ofctl.settings.readonly);
			//文件自动保存
			if(ofctl.settings.autoSave && ofctl.settings.saveTTL > 0){
				intervalId = setInterval(function(){AutoSaveFileToURL(ofctl)}, ofctl.settings.saveTTL);
			}
			if(ofctl.curobj.doctype==2){
				ofctl.addExcelEvent();
			}
		}
	   catch(err){
	       //alert("错误：" + err.number + ":" + err.description);
	   		alert(err.message)
	   }
	   finally{}
	}
}
/**
 * 关闭事件
 * @param {} ofctl
 */
function docCloseed(ofctl){
	ofctl.settings.isOpened = false;
}
function SheetChanged(ofctl,SheetName, row, col){
	ofctl.OnSheetChanged(SheetName, row, col);
}
function SheetSelectionChange(ofctl,SheetName, row, col){
	ofctl.OnSheetSelectionChange(SheetName, row, col);
}

function SheetBeforeDoubleClick(ofctl,SheetName, row, col, cancel){
	ofctl.OnSheetBeforeDoubleClick(SheetName, row, col, cancel);
}
function SheetFollowHyperlink(ofctl,Sheet, LinkTarget){
	ofctl.OnSheetFollowHyperlink(Sheet, LinkTarget);
}
//痕迹隐藏
OfficeControl.prototype.showRevisions=function(isshow){
	if(this.settings.isOpened)
	{
		if(this.curobj.doctype !=1 && this.curobj.doctype!=6)//word,wps
		{
			//alert("该功能不支持当前文档类型");
			return;
		}
		this.curobj.ActiveDocument.ShowRevisions = isshow;
	}
}

//使用痕迹
OfficeControl.prototype.trackRevisions=function(isuse){
	if(this.settings.isOpened)
	{
		if(this.curobj.doctype !=1 && this.curobj.doctype!=6)//word,wps
		{
			//alert("该功能不支持当前文档类型");
			return;
		}
		this.curobj.ActiveDocument.TrackRevisions = isuse;
	}
}


//接受所有修订
OfficeControl.prototype.acceptAllRevision=function(){
	if(this.settings.isOpened)
	{
		if(this.curobj.doctype !=1 && this.curobj.doctype!=6)//word,wps
		{
			alert("该功能不支持当前文档类型");
			return;
		}
		this.curobj.ActiveDocument.AcceptAllRevisions();
	}
}

//拒绝所有修订
OfficeControl.prototype.rejectAllRevisions =function(){
	if(this.settings.isOpened)
	{
		if(this.curobj.doctype !=1 && this.curobj.doctype!=6)//word,wps
		{
			alert("该功能不支持当前文档类型");
			return;
		}
		this.curobj.ActiveDocument.RejectAllRevisions ();
	}
}

//读写属性
OfficeControl.prototype.setReadOnly =function(isreadonly){
  if (isreadonly) this.curobj.IsShowToolMenu = false;
  with(this.curobj.ActiveDocument)
  { 
  	if (this.curobj.DocType == 1){//word
		if ( (ProtectionType != -1) &&  !isreadonly)
		{
			Unprotect();
		}
		if ( (ProtectionType == -1) &&  isreadonly)
		{
			Protect(2,true,"");
		}
	}
	else if(this.curobj.DocType == 2){//excel
		for(var i=1;i<=Application.Sheets.Count;i++)
		{
			if(isreadonly)
			{
				Application.Sheets(i).Protect("",true,true,true);
			}
			else
			{
				Application.Sheets(i).Unprotect("");
			}
		}
		if(isreadonly)
		{
			Application.ActiveWorkbook.Protect("",true);					
		}
		else
		{
			Application.ActiveWorkbook.Unprotect("");
		}
	}//excel
   }
}
//自动保存
function AutoSaveFileToURL(ofctl){
	if(!ofctl.settings.isOpened)
		return;
	if(!ofctl.curobj.ActiveDocument.saved){		
		try{
			ofctl.saveFileToURL();
			//saved
		} catch(err){
  			//alert("错误：" + err.number + ":" + err.description);
		}
	}
}
//保存到服务器
OfficeControl.prototype.saveFileToURL = function (pk){
	var saveURL = this.settings.saveurl;
	var savepk = this.settings.pk;
	if(pk)
		savepk = pk
	var msg = this.curobj.SaveToURL(saveURL, savepk,"",savepk,false);
	//根据控件的状态码，返回文件保存状态的提示信息
	if (this.curobj.StatusCode != 0 ) {
		alert(msg);
	}
}

//手写批注_图片
OfficeControl.prototype.addHandSign=function(){
	if(this.settings.isOpened)
	{
		if(this.curobj.doctype !=1 && this.curobj.doctype!=2)//word,excel
		{
			alert("该功能不支持当前文档类型");
			return;
		}
		this.curobj.DoHandSign2(this.settings.username,//手写签名用户名称
									"",//signkey,DoCheckSign(检查印章函数)需要的验证密钥。
									0,//left
									0,//top
									1,//relative,设定签名位置的参照对象.0：表示按照屏幕位置插入，此时，Left,Top属性不起作用。1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落（为兼容以前版本默认方式）
									100);
	}
}
/**
 * 手写批注,签名方式，但不保护
 */
OfficeControl.prototype.addHandSecSignUnprotect=function(){
if(this.settings.isOpened){
		if(this.curobj.doctype==1||this.curobj.doctype==2) //word,excel
		{
			try
			{
				this.curobj.AddSecHandSign(
				this.settings.uername,//用户名
				0,0,1,
				0,//打印模式
				false,//不使用证书
				false,//不锁定印章
				false,//检测文档变化
				false,//显示印章设定对话框选项
				""
				);}
			catch(error){
				if(error.message != "")
					alert(error.message);
			}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}
}

//手写签名
OfficeControl.prototype.addHandSecSign=function()
{
	if(this.settings.isOpened)
	{
		if(this.curobj.doctype==1||this.curobj.doctype==2) //word,excel
		{
			try
			{
				this.curobj.AddSecHandSign(
				this.settings.uername,//用户名
				0,0,1,
				this.settings.PrintModel,//打印模式
				false,//不使用证书
				false,//不锁定印章
				true,//检测文档变化
				false//显示印章设定对话框选项
				);}
			catch(error){
				if(error.message != "")
					alert(error.message);
			}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}
}
//头部套红
OfficeControl.prototype.insertRedHeadFromUrl=function(temfile){	
	if(temfile)
	{
		try{
			this.trackRevisions(false);//暂时退出痕迹保留状态
			var doc = this.curobj.ActiveDocument;
			var curSel = doc.Application.Selection;
			curSel.HomeKey(6); //跳转到文档开头
			//插入模板头部
			this.curobj.AddTemplateFromURL(temfile.url);
		}
		catch(err)
		{
			alert("错误：" + err.number + ":" + err.description);
		};
		this.trackRevisions(this.settings.trackRevisions)			
	}
}
//底部套红
OfficeControl.prototype.insertRedEndFromUrl=function(temfile){
	if(temfile)
	{
		try{
			this.trackRevisions(false);//暂时退出痕迹保留状态
			var doc = this.curobj.ActiveDocument;
			var curSel = doc.Application.Selection;				
			curSel.EndKey(6); //跳转到文档尾部
			//插入模板头部
			this.curobj.AddTemplateFromURL(temfile.url);
		}
		catch(err)
		{
			alert("错误：" + err.number + ":" + err.description);
		};
		this.trackRevisions(this.settings.trackRevisions)			
	}
}
//替换模板
OfficeControl.prototype.insertTempleteFromUrl=function(temfile){
	if(temfile)
	{
		try{
			this.trackRevisions(false);//暂时退出痕迹保留状态
			var ProgID = this.getProgIDbyDoctype(this.curobj.doctype);
			this.curobj.OpenFromURL(temfile.url,false,ProgID);
		}
		catch(err)
		{
			alert("错误：" + err.number + ":" + err.description);
		};
		this.trackRevisions(this.settings.trackRevisions)			
	}
	
}
//加载标签
OfficeControl.prototype.innerLoadBookMark=function(){
	if(this.LoadBookMarks){
		return this.LoadBookMarks();		
	}
	return null;
}
/**
 * 替换标签
 * @param {} bookmarks
 */
OfficeControl.prototype.replaseBookMark=function(bookmarks){
	if(this.curobj == null ||this.curobj.ActiveDocument == null )
		return;
	var doc = this.curobj.ActiveDocument;
	if(doc.BookMarks.Count < 1)
		return;	
	var curbookmarks;
	if(bookmarks){
		curbookmarks = bookmarks;
	}
	else{
		curbookmarks = this.innerLoadBookMark();
	}
	if(curbookmarks){
		var keysets = curbookmarks.keySet();
		for(var i=0;i<=curbookmarks.size();i++){
			var markname = keysets[i];
			if(this.curobj.ActiveDocument.BookMarks.Exists(markname)){
				this.curobj.SetBookmarkValue(markname, curbookmarks.get(markname));
			}
		}
		/*
		for(var i=1;i<=doc.BookMarks.Count;i++){
				var docmark = doc.BookMarks(i);
				var sysmark = curbookmarks.get(docmark.name);
				if(sysmark)
					this.curobj.SetBookmarkValue(docmark.name,sysmark.value);
					//docmark.Range.Text = sysmark.value;
			}
			*/
	}	
}
/**
 * 将标签替换未文件
 * @param {} markname 标签名称 
 * @param {} fileurl 文件地址
 */
OfficeControl.prototype.replaseBookMarkToFile=function(markname,fileurl){
	/**
	 * 1.定位标签
	 * 2.选择标签
	 * 3.插入文件
	 */
	if(this.curobj == null ||this.curobj.ActiveDocument == null )
		return;
	var doc = this.curobj.ActiveDocument;
	if(doc){
		if(doc.BookMarks.Exists(markname)){
			var cursel = this.curobj.ActiveDocument.Application.Selection;
			doc.BookMarks(markname).Select();
			cursel.Delete();
			this.curobj.AddTemplateFromURL(fileurl);
		}
		else{
			alert("标签:" + markname + "不存在!");
		}
	}
}
//远程签章
OfficeControl.prototype.addRemoteSign=function(temfile){	
		if(temfile)
		{
			try{
				this.trackRevisions(false);//暂时退出痕迹保留状态
			    this.curobj.AddSecSignFromURL(this.settings.userName,
					temfile.url,//URL
					50,//left
					50,//top
					1,//相对位置
					this.settings.PrintModel,//打印模式
					this.settings.IsUseCertificate,//是否使用证书
					false,//锁定印章
					true,//检查文档改变
					false,//IsShowUI
					"",//密码
					true,//签章允许批注
					true//签章用在上方
					)

			}
			catch(err)
			{
				alert("错误：" + err.number + ":" + err.description);
			};
			this.trackRevisions(this.settings.trackRevisions)			
		}
}
//本地文件签章
OfficeControl.prototype.addLocalSign=function(){
	var doc = this.curobj.ActiveDocument;
	if(doc == null)
		return;
	if(this.curobj.doctype==1||this.curobj.doctype==2)
		{
			try{
				this.curobj.AddSecSignFromLocal(
					this.settings.userName,
					"",//URL
					true,//选择文件
					50,//left
					50,//top
					1,//相对位置
					this.settings.PrintModel,//打印模式
					this.settings.IsUseCertificate,//是否使用证书
					false,//锁定印章
					true,//检查文档改变
					false,//IsShowUI
					"",//密码
					true,//签章允许批注
					true//签章用在上方
				)
			}
			catch(error){}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
}
/**
 * Ukey签章
 */
OfficeControl.prototype.addSignFromEkey = function(){
	var doc = this.curobj.ActiveDocument;
	if(doc == null)
		return;
	if(this.curobj.doctype==1||this.curobj.doctype==2)
		{	
			this.curobj.AddSecSignFromEkey(
				this.settings.userName,
				50,
				50,
				1,//相对位置
				this.settings.PrintModel,//打印模式
				this.settings.IsUseCertificate,//是否使用证书
				false,//是否锁定印章
				true,//检测文件变化
				true,//显示Ukey选择对话框
				"",//密码
				-1,//不自动指定ekey索引
				true,//签章允许批注
				true//签章用在上方
			)			
		}
		else{
			throw ("EKey签章不支持当前文档类型");			
		}
} 
/**
 * 创建书签
 */
OfficeControl.prototype.addBookMark = function(name,text){
	if(!name){
		alert( "书签名称不可为空");
		return;
	}
	if(this.curobj.doctype!=1 && ofctl.curobj.doctype!=6){
		alert(  "当前文档类型不支持书签操作");
		return;
	}
	if(this.curobj.ActiveDocument.BookMarks.Exists(name)){
			alert("已存在名称为：\""+BookMarkName+"\"的书签！");
			return;
	}
	var doc = this.curobj.ActiveDocument;
	var rg =doc.range(doc.Application.Selection.Range.Start,doc.Application.Selection.Range.End);
	if(text)
		rg.Text = text;
	doc.Bookmarks.Add(name, rg); 
	
}
/*
 * 获取正文内容
 */
OfficeControl.prototype.getContent =function(start,end) {
	if(this.curobj.doctype!=1 && ofctl.curobj.doctype!=6){
		alert(  "当前文档类型不支持书签操作");
		return;
	}	
	
	var range = this.curobj.ActiveDocument.range(0);
	if(range.end < end){
		if(range.end < start){
			alert("当前文档长度小于起始点,不可读取内容");
		}
		else{
			end = range.end;
		}
	}
	return this.curobj.ActiveDocument.range(start,end).Text;
}
/**
 * 
 */
OfficeControl.prototype.Exit = function(){
	
}
/**
 * 启用事件
 */
OfficeControl.prototype.enableEvents=function(){
	if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application)
		if(!this.curobj.ActiveDocument.Application.EnableEvents)
			this.curobj.ActiveDocument.Application.EnableEvents = true;
}
/**
 * 禁用事件
 */
OfficeControl.prototype.unableEvents=function(){
	if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application)
		if(this.curobj.ActiveDocument.Application.EnableEvents)
			this.curobj.ActiveDocument.Application.EnableEvents = false;
}
/**
 * 获取sheet 集合
 * @return {}
 */
OfficeControl.prototype.GetSheets = function(){
	var ret = null;
	if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application)
		ret = this.curobj.ActiveDocument.Application.Sheets;
	return ret;
}
OfficeControl.prototype.GetActiveSheet=function(){
	if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application)
		return this.curobj.ActiveDocument.Application.ActiveSheet;
	else
		return null;
}
OfficeControl.prototype.GetActiveShapes=function(){
	var activesheet = this.GetActiveSheet();
	if(activesheet != null)
		return activesheet.Shapes;
	else
		return null;
}
OfficeControl.prototype.GetActiveRange=function(){	
	if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application)
		return this.curobj.ActiveDocument.Application.ActiveCell;
	else
		return null;
}
OfficeControl.prototype.GetRange=function(sheet,row,col){	
	if(sheet != null)
		return sheet.Cells(row,col);
	return null;
}
OfficeControl.prototype.GetRangeValue=function(SheetName, RangeName){
	return this.curobj.GetRangeValue(SheetName, RangeName);	
}
OfficeControl.prototype.SetRangeValue=function(SheetName, RangeName,varValue){
	this.curobj.SetRangeValue(SheetName, RangeName,varValue);
}
OfficeControl.prototype.GetRangeFormula=function(SheetName, RangeName){
	return this.curobj.GetRangeFormula(SheetName, RangeName);
}
OfficeControl.prototype.SetRangeFormula=function(SheetName, RangeName,varValue){
	return this.curobj.SetRangeFormula(SheetName, RangeName,varValue);
}
/*
OfficeControl.prototype.SetRangeLocked=function(SheetName, RangeName,isLocked){
	return this.curobj.SetRangeLocked(SheetName, RangeName,isLocked);
}
*/
OfficeControl.prototype.SetRangeLocked=function(range,islock){
	try{
	range.Locked = islock;
	}
	catch(ex){
	}
}
OfficeControl.prototype.EnableSheetDoubleClick=function(){
	this.curobj.CancelSheetDoubleClick = true;
}
OfficeControl.prototype.UnableSheetDoubleClick=function(){
	this.curobj.CancelSheetDoubleClick = false;
}

/**
 * 创建sheet
 * @param {} Before 前sheet名称、索引
 * @param {} After 后sheet名称、索引
 * @param {} Count 数量
 * @param {} Type 类型
 */
OfficeControl.prototype.CreateSheet=function(Before,After,Count,Type){	
	if(this.curobj.ActiveDocument && this.curobj.ActiveDocument.Application)
		this.curobj.AddSheet(Before,After,Count,Type);
	else{
		this.newFile(2);
	}
}

/**
 * 通过路径添加图片
 * @param {} url
 */
OfficeControl.prototype.AddPicFromURL=function(url){
	this.curobj.AddPicFromURL(url);
}
/**
 * 添加文件到本地
 * @param {} filename 文件名
 * @param {} PromptSelect 提示选择文件
 * @param {} IsFloat 是否浮动
 * @param {} left 
 * @param {} top
 * @param {} relative 取值1-4。1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落（为兼容以前版本默认方式）；
 * @param {} scale 表示图片是置于文字的上方还是下方。0：下方；1：上方；
 * @param {} zorder
 */
OfficeControl.prototype.AddPicFromLocal=function(filename,PromptSelect,IsFloat,left,top,relative,scale,zorder){
	if(!filename)
		filename = "";
	if(PromptSelect == null || PromptSelect == undefined)
		PromptSelect = true;
	if(IsFloat == null || IsFloat == undefined)
		IsFloat = true;
	if(left == null || left == undefined)
		left = 0;
	if(top == null || top == undefined)
		top = 0;
	if(relative == null || relative == undefined)
		relative = 1;
	if(scale == null || scale == undefined)
		scale = 100;
	
	this.curobj.AddPicFromLocal(filename,PromptSelect,IsFloat,left,top,relative,scale,zorder);

}
/**
 * 
 * @param {} filename 文件名，必徐
 * @param {} linktofile 是否连接到文件	MsoTriState 可为以下 MsoTriState 常量之一。 
					msoCTrue 
					msoFalse  使图片成为其源文件的独立副本。 
					msoTriStateMixed 
					msoTriStateToggle 
					msoTrue 建立图片与其源文件之间的链接。
 * @param {} savewithdocment MsoTriState
 * @param {} left
 * @param {} top
 * @param {} width
 * @param {} height
 */
OfficeControl.prototype.AddPicByApplication = function(filename,linktofile,savewithdocment,left,top,width,height){
	var activesheet = this.GetActiveSheet();
	if(activesheet != null){
		return activesheet.Shapes.AddPicture(filename,linktofile,savewithdocment,left,top,width,height)
	}
	return null;
}
