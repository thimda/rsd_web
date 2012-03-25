<% 
	response.setCharacterEncoding("UTF-8"); 
%>
<%@ page import="nc.uap.lfw.core.common.SessionConstant" %>
<%@ page import="java.lang.Exception" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.net.URLDecoder"%>
<html>
	<head>
		<style>
			body{
				SCROLLBAR-FACE-COLOR: #D8DDBC;
			  	SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
			  	SCROLLBAR-SHADOW-COLOR: #9c9e9c;
			  	SCROLLBAR-3DLIGHT-COLOR: #9c9e9c;
			  	SCROLLBAR-ARROW-COLOR: #6a6a6a;
			  	SCROLLBAR-TRACK-COLOR: #D8DDBC;
			  	SCROLLBAR-DARKSHADOW-COLOR: #b5b2b5;
		  	}
		</style>
	</head>
	<body>
		<%
			String msg = (String)request.getParameter("msg");
			msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
		%>
		<div id="errorDiv" style="top:10px;position:absolute;align:center;border:solid D1D0D1 1px;FILTER:progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#FFFFFF, EndColorStr=#F2F3F3);">
			<div id="errorDetail" style="background-color:#ffffec;top:15px;left:15px;position:absolute;border:solid FC7700 1px">
				<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="0">
					<%--<tr height="20px">
						<td width="10px"></td>
						<td width="70px"></td>
						<td></td>
						<td width="10px">
						</td>
					</tr> --%>
					<tr height="40px">
						<td width="10px">
						</td>
						<td width="70px">
							<img src="img/hint.png"/>
						</td>
						<td align="left">
							<font style="font-size:14px;font-weight:bolder;font-color:#000000;font-family:arial"><%=msg%></font>
						</td>
						<td width="10px">
						</td>
					</tr>
					<%--<tr height="24px">
						<td colspan="3">&nbsp;</td>
						<td width="10px">
						</td>
					</tr> --%>
					<tr>
						<td width="10px">
						</td>
						<td width="70px">
							&nbsp;
						</td>
						<td>
							<div id="expDetail" style="overflow:auto;height:60px;display:none">
								<font style="font-size:12px;font-color:#000000;font-family:arial">
									
								</font>
							</div>
						</td>
						<td width="10px">
						</td>	
					</tr>
					<%--<tr height="35">
						<td width="10px">
						</td>
						<td width="70px">
						</td>
						<td>
						</td>
						<td width="10px">
						</td>
					</tr> --%>
					<tr height="32">
						<td width="10px">
						</td>
						<td width="70px">
						</td>
						<td align="right" valign="top">
							<div style="text-align:center;height:22;width:100px;background:url('img/button.png') repeat-x;border:solid #6192c3 1px" onmouseover="this.style.cursor='pointer'" onmouseout="this.style.cursor='default'" onclick="document.getElementById('expDetail').style.display='block'">
								<font style="font-size:12px;font-color:#000000;font-family:arial">详细信息(D)>></font>
							</div>
						</td>
						<td width="10px">
						</td>
					</tr>
				</table>
			</div>
			<%-- 
			<div id="refreshBtn" style="position:absolute;text-align:center;height:22;width:100px;background:url('img/button.png') repeat-x;border:solid #6192c3 1px" onmouseover="this.style.cursor='pointer'" onmouseout="this.style.cursor='default'" onclick="">
				<font style="font-size:12px;font-color:#000000;font-family:arial">刷新该页</font>
			</div> --%>
		</div>
	</body>
	<script>
		var $loadingDiv = parent.$loadingDiv; 
		if($loadingDiv == null) $loadingDiv = parent.parent.$loadingDiv;
		if($loadingDiv != null){
			$loadingDiv.style.display = "none";
		}
		
		var errorDiv = document.getElementById("errorDiv");
		if(errorDiv != null)
		{
			errorDiv.style.width = document.body.clientWidth - 20;
			errorDiv.style.left = 10;
			errorDiv.style.height = document.body.clientHeight - 40;
		}
		
		var errorDetail = document.getElementById("errorDetail");
		if(errorDetail != null)
		{	
			errorDetail.style.width = errorDiv.offsetWidth - 30;
			errorDetail.style.height = 200;
		}
		
		var refreshBtn = document.getElementById("refreshBtn");
		if(refreshBtn != null)
		{
			refreshBtn.style.top = errorDiv.offsetHeight - 37;
			refreshBtn.style.left = errorDiv.offsetWidth - 115;
		}
	</script>
</html>