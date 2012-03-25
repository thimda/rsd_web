<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="nc.uap.lfw.ncadapter.login.NcSessionBean"%>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment"%>
<html xmlns="http://www.w3.org/1999/xhtml">	
<head>		
<title>UFIDA NC</title>		
<lfw:base/>		
<lfw:head/>		
<lfw:import />	
<% 
	NcSessionBean ses = (NcSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
	String userName = ses.getUser_name();
%>
<script>
	var userName = "<%=userName%>";
</script>
</head>	
<body>		
<lfw:pageModel>			
<lfw:borderLayout>
<lfw:borderPanel position="center">
<lfw:flowvLayout>
<lfw:flowvPanel>
<lfw:panel id="null" className="panel_medicine"  scroll="false" transparent="false" display="true" visibility="true">
<lfw:flowvLayout>
<lfw:flowvPanel height="60">
<lfw:widget id="main">
<lfw:borderLayout>
<lfw:borderPanel position="center">
<lfw:flowvLayout id="null" widget="null">
<lfw:flowvPanel height="30">
<lfw:toolbar id="head" widget='main'/>
</lfw:flowvPanel>
<lfw:flowvPanel>
<lfw:flowhLayout>
<lfw:flowhPanel>
<lfw:toolbar id="shortcut_toolbar" widget='main'/>
</lfw:flowhPanel>
<lfw:flowhPanel width="110">
<lfw:toolbar id="btn_toolbar" widget='main'/>
</lfw:flowhPanel>
</lfw:flowhLayout>
</lfw:flowvPanel>
</lfw:flowvLayout>
</lfw:borderPanel>
</lfw:borderLayout>
</lfw:widget>
</lfw:flowvPanel>
<lfw:flowvPanel>
<lfw:tab id="pagetab" className="main_tab_div">
<lfw:tabitem id="item1" text="首页面">
	<iframe name="showFrame" id="showFrame" width="100%" height="100%" border="0" frameborder="0" src="${ROOT_PATH}/core/uimeta.um?pageId=quickdesk" style="background:#FFFFFF;"></iframe>
</lfw:tabitem>
<lfw:tabspace width="13"></lfw:tabspace>
</lfw:tab>
</lfw:flowvPanel>
</lfw:flowvLayout>
</lfw:panel>
</lfw:flowvPanel>
</lfw:flowvLayout>
</lfw:borderPanel>
</lfw:borderLayout>
</lfw:pageModel>
</body>
</html>