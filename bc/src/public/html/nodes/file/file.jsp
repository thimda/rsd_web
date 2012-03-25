<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw"%>
<%@ taglib	uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="nc.uap.lfw.util.HttpUtil"%>
<%@ page import="nc.uap.cpb.persist.dao.PtBaseDAO"%>
<%
String path = request.getContextPath();
//连接串
String dsName = nc.uap.lfw.core.LfwRuntimeEnvironment.getDatasource();
//基础路径
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//单据类型
String billtype=StringUtils.defaultIfEmpty(request.getParameter("billtype") , "");
//单据pk
String billitem=StringUtils.defaultIfEmpty(request.getParameter("billitem") , "");
if(billitem.equalsIgnoreCase("neednew")){
	billitem = PtBaseDAO.generatePK(dsName); 
}
//分类
String category=StringUtils.defaultIfEmpty(request.getParameter("category") , "");
//是否覆盖
String iscover=StringUtils.defaultIfEmpty(request.getParameter("iscover") , "");
//文件管理器
String filemanager=StringUtils.defaultIfEmpty(request.getParameter("filemanager") , "");
//单据所在片段
String widget=StringUtils.defaultIfEmpty(request.getParameter("widget") , "");
//数据集
String dataset=StringUtils.defaultIfEmpty(request.getParameter("dataset") , "");
//文件名
String filename=StringUtils.defaultIfEmpty(request.getParameter("filename") , "");
//url
String fileurl=StringUtils.defaultIfEmpty(request.getParameter("fileurl") , "");
//回调函数
String method=StringUtils.defaultIfEmpty(request.getParameter("method") , "");
//是否多文件
String multi=StringUtils.defaultIfEmpty(request.getParameter("multi") , "true");
//是否自动保存
String auto=StringUtils.defaultIfEmpty(request.getParameter("auto") , "true");

String parentWidget = StringUtils.defaultIfEmpty(request.getParameter("parentwidget"), "");
String parentDataset = StringUtils.defaultIfEmpty(request.getParameter("parentdataset"), "");
//回调lestener
String uploadListener = StringUtils.defaultIfEmpty(request.getParameter("uploadlistener"), "");
//文件大小限制
String sizeLimit = StringUtils.defaultIfEmpty(request.getParameter("sizeLimit"), "1024*1024*20");
//String sizeLimit = StringUtils.defaultIfEmpty(request.getParameter("sizeLimit"), "1024*1024*1024*20");
//后缀名
String fileExt = StringUtils.defaultIfEmpty(HttpUtil.decodeURL(request.getParameter("fileExt")) , "*.*");
//后缀描述
String fileDesc = StringUtils.defaultIfEmpty(HttpUtil.decodeURL(request.getParameter("fileDesc")) , "");
//文件数量
String queueSizeLimit = StringUtils.defaultIfEmpty(request.getParameter("queueSizeLimit") , "10");
//创建状态,如果是1则不会被视为垃圾文件
String createstatus = StringUtils.defaultIfEmpty(request.getParameter("createstatus") , "1");
//是否立即关闭窗口
String closeDialog = StringUtils.defaultIfEmpty(request.getParameter("closeDialog") , "true");
//是否弹出上传成功
String isalter = StringUtils.defaultIfEmpty(request.getParameter("isalter") , "true");
//浏览器类型
boolean isIos = nc.uap.lfw.core.LfwRuntimeEnvironment.getBrowserInfo().isIos();
//立即上传
String isquick = StringUtils.defaultIfEmpty(request.getParameter("isquick") , "true");
//filePK，如果传入filePK则覆盖原文件
String filepk = StringUtils.defaultIfEmpty(request.getParameter("filepk") , "");

if(!filepk.equals("")){
	createstatus = "1";
	queueSizeLimit = "1";
}
//extendclass,扩展类，继承自nc.uap.lfw.file.IFileUploadExtender
String extendclass = StringUtils.defaultIfEmpty(request.getParameter("extendclass") , "");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
      <head>
       
		<lfw:base />
		<lfw:head />
		<lfw:import />
        <title>Uploadify</title>
        <link href="html/nodes/file/css/default.css" rel="stylesheet" type="text/css" />
        <link href="html/nodes/file/css/uploadify.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="html/nodes/file/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="html/nodes/file/js/swfobject.js"></script>
        <script type="text/javascript" src="html/nodes/file/js/jquery.uploadify.v2.1.4.js"></script>
		
        <script type="text/javascript">
          var needAlert = <%= isalter %>
          var needclose = <%= closeDialog %>
          function uploadComplete(returnData){
          var req = returnData.split("||");
      		if(req.length == 1){
					alert(req[0]);
					return;
				}
				if('<%=widget%>'!=''&&'<%=dataset%>'!=''){
					var ds =parent.pageUI.getWidget('<%=widget%>').getDataset('<%=dataset%>');
					var row = ds.getSelectedRow();
					if('<%=filename%>'!=''){
					ds.setValue("<%=filename%>",req[1]);
					}
					if('<%=fileurl%>'!=''){
					ds.setValue("<%=fileurl%>", "<%=basePath%>pt/file/down?id="+req[0]);
					}
				}
				<% if(!method.equals("")){ %>
				//获得真正的parent
				var trueParent;
				var pWin = window.dialogArguments;
				if( parent.<%=method%>){
					trueParent=parent;
				}else if(window.opener && window.opener.<%=method%>){
					trueParent=window.opener;
				}else if(parent.getTrueParent() && parent.getTrueParent().<%=method%>){
					trueParent=parent.getTrueParent();
				}else if(pWin.<%=method%>){
					trueParent=pWin;
				}
				//主键,文件名,文件大小,文件类型,上传时间,上传人PK,上传人姓名，单据主键
				<%
					out.print("trueParent."+method+"(req[0],req[1],req[2],req[3],req[4],req[5],req[6],'"+ billitem  +"');");
				}%>
				<% if(!uploadListener.equals("")){ %>
					needAlert = false;
					var proxy = new ServerProxy(null, null, false); //true表示异步调用
					proxy.addParam("listener_class", "nc.uap.lfw.file.listener.UploadCallBackServerListener"); 
	    			proxy.addParam("upload_listener_class", "<%=uploadListener%>"); //指定后台调用类
					proxy.addParam("pk_file", req[0]);
					proxy.addParam("fileName", req[1]);
					proxy.addParam("size", req[2]);
					proxy.addParam("fileType", req[3]);
					proxy.addParam("createtime", req[4]);
					proxy.addParam("pk_user", req[5]);
					proxy.addParam("user_name", req[6]);					
					proxy.addParam("billitem",'<%=billitem%>');
					
	    			var rule = new SubmitRule();
	    			var prule = new SubmitRule();
	    			var wdr_parentWidget = new WidgetRule('<%=parentWidget%>');
	    			var dsr_parentDataset = new DatasetRule('<%=parentDataset%>','ds_all_line');
	    			wdr_parentWidget.addDsRule('<%=parentDataset%>', dsr_parentDataset);
	    			prule.addWidgetRule('<%=parentWidget%>', wdr_parentWidget);
					rule.setParentSubmitRule(prule);
	    			proxy.setSubmitRule(rule);
	    			proxy.execute();
				<%
					}
				%>
				if(needAlert)
					alert(req[1]+"上传成功!");
				
          }
          
          </script>

    </head>
    <body>
    	<% if(!isIos){ %>
        <script type="text/javascript">
		  $(document).ready(function() {
		  	var needclose = <%= closeDialog %>
		  	var isquick = <%= isquick %>
		  	
            $("#uploadify").uploadify({
                'uploader'       : 'html/nodes/file/uploadify.swf',
                'script'         : '<%=basePath%>pt/file/upload;jsessionid=<%= session.getId() %>',
				'scriptData'	 :{ billtype :'<%=billtype%>',billitem:'<%=billitem%>',category:'<%=category%>',iscover:'<%=iscover%>',filemanager:'<%=filemanager%>',filepk:'<%=filepk%>',extendclass:'<%=extendclass%>'},
                'cancelImg'      : 'html/nodes/file/images/cancel.png',
                'folder'         : 'uploads',
                'queueID'        : 'fileQueue',
                'auto'           : <%=auto%>,
                'multi'          : <%=multi%>,
				'queueSizeLimit' : <%=queueSizeLimit%>,
				'fileExt' : '<%=fileExt%>',
				'fileDesc' : '<%=fileDesc%>',
				'sizeLimit'		 : <%=sizeLimit%>,
                'simUploadLimit' : 1,
                'buttonText'     : 'Select File',
                'buttonImg'     : 'html/nodes/file/images/uInputBg.gif',
                'hideButton' : true,
				'onComplete'	: function(){
						uploadComplete(arguments[3]);						
					},
				'onAllComplete' : function(){
					if(needclose || isquick)
						parent.hideDialog();
				}
            });
            /*
            if("<%=isquick%>" == "true"){
            	jQuery('#uploadify').uploadifyUpload();
            }*/
        });
       </script>
       <style>
		#uploadItem	{ vertical-align:top;}
        #uploadItem td a{ display:inline-block; width:86px; height:25px; border:0; background:url(html/nodes/file/images/aInputBg.gif); color:#FFF; font-weight:bold; text-align:center;text-decoration:none; line-height:25px; vertical-align:middle; font-size:12px;}
       </style>
       <div id="fileQueue"></div>
       <table>
        <tr id="uploadItem">
        	<td><input type="file" name="uploadify" id="uploadify" /></td>
        	<% if(!"true".equals(isquick)){ %>
            <td><% if(!"true".equals(auto)){ %><a href="javascript:jQuery('#uploadify').uploadifyUpload()">开始上传</a><% }%></td>
            <% } %>
        </tr>
       </table>
       <% }else{ %>
       <form method="POST" enctype="multipart/form-data" target="filesubmiter" action="<%=basePath%>pt/file/upload?sys_datasource=<%= dsName %>">
       		<input type="file" name="file"/ > 
       		<input type="hidden" name="billtype" value="<%=billtype%>">
       		<input type="hidden" name="billitem" value="<%=billitem%>">
       		<input type="hidden" name="category" value="<%=category%>">
       		<input type="hidden" name="iscover" value="<%=iscover%>">
       		<input type="hidden" name="filemanager" value="<%=filemanager%>">
       		<input type="hidden" name="filepk" value="<%=filepk%>">
       		<input type="hidden" name="extendclass" value="<%=extendclass%>">
       		<input type="submit" value="提交" name="sbt1"/> 
       </form>
      	 <iframe name="filesubmiter" vspace="0" height="0" width="0" hspace="0" scrolling="no"> </iframe>
       <% } %>
    </body>
	<lfw:pageModel>
	</lfw:pageModel>
</html>
