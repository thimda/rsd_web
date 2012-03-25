<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://www.ufida.com/multilang" prefix="ml" %>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%
	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//String date = sdf.format(LfwRuntimeEnvironment.getLoginDate().toDate()); 
	//String corpName = LfwRuntimeEnvironment.getCorp() == null ? "" : LfwRuntimeEnvironment.getCorp().getUnitname();
	//String userName = LfwRuntimeEnvironment.getUserVO().getUserName();
	//pageContext.setAttribute("date", date);
	//pageContext.setAttribute("corpName", corpName);
	//pageContext.setAttribute("userName", userName);
 %>
<table id="webbx_main_head" border="0" cellpadding="0" cellspacing="0" style="width:100%;height:100%;background:#94C6F9">
	<tr>
		<td align="left" rowspan="2" width="698px" style="background:url('${NODE_IMAGE_PATH}/top/banner.gif') no-repeat">
			<div >
				<img src="${NODE_IMAGE_PATH}/top/nc-logo.png" id="png_1" style="height:50px;width:185px">
			</div>	
		</td>
		<td align="right" width="35%">
			<lfw:toolbar id="headerToolBar" widget="mainwidget">
			</lfw:toolbar>
			<%--
			<table>
				<td><img src="${NODE_IMAGE_PATH}/top/logout.png" id="img_lagout" onclick="logout();" style="cursor: hand"></td>
				<td><a href="#" onclick="logout();stopAll(event);" onfocus="this.blur();" class="head_nav">注销</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><img src="${NODE_IMAGE_PATH}/top/settings.gif" id="img_lagout" onclick="modifyConfig();" style="cursor: hand"></td>
				<td><a href="#" onclick="modifyConfig();stopAll(event);" onfocus="this.blur();" class="head_nav">设置</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			--%>
				<%--<td><img src="${NODE_IMAGE_PATH}/top/help.png" id="img_help" onclick="alert('帮助')" style="cursor: hand"></td>
				<td><a href="#" onclick="window.open('/helpmain.jsp?datasource=design&langcode=simpchn&flag=201103&filename=erm201103.html');stopAll(event);" onfocus="this.blur();" class="head_nav">${ml:transp('lfw','lfw_help')}</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				--%>
			<%--
			</table>
			--%>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right">
			<%-- ${corpName}&nbsp;&nbsp;${userName},${ml:transp('lfw','lfw_hello')}&nbsp;&nbsp;${date}
			--%>
			&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>