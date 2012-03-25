<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ufida.com/multilang" prefix="ml" %>
<%@ page import="nc.bs.framework.common.InvocationInfoProxy" %>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment" %>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%@ page import="nc.uap.lfw.util.LanguageUtil"%>
<%@ page import="nc.bs.framework.common.UserExit"%>
<%
	String langCode = LanguageUtil.getLangIdByCookie(request);
	InvocationInfoProxy.getInstance().setLangCode(langCode);
	UserExit.getInstance().setLangCode(langCode);
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<lfw:base/>
		<lfw:head/>
		<title>
			NC网上报销
		</title>
		<link href="${NODE_THEME_PATH}/login_button.css" rel="stylesheet" type="text/css" />
		<lfw:import/>
		<%
			 
			if(LfwRuntimeEnvironment.isFromLfw()){
				pageContext.setAttribute("TEMP_PATH", "/webtemp");
			}
			else{
				pageContext.setAttribute("TEMP_PATH", "");
			}
		%>
		<style>
			body{
				background:url(${NODE_IMAGE_PATH}/big-bg.png) no-repeat center center fixed;
				-moz-background-size: cover;
				background-size: 100% 100%;
				filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=${ROOT_PATH}${TEMP_PATH}/${NODE_IMAGE_PATH}/big-bg.png,sizingmethod=scale);
			}
		</style>
	</head>
	<body>
		<lfw:pageModel>			
			<lfw:widget id="main">
			<lfw:flowvLayout id="flowv1">
			<lfw:flowvPanel>
			<lfw:flowhLayout>
			<lfw:flowhPanel>
			</lfw:flowhPanel>
			<lfw:flowhPanel width="430">
			<lfw:flowvLayout id="flowv3">
			<lfw:flowvPanel>
			</lfw:flowvPanel>
			<lfw:flowvPanel height="302">
			<lfw:flowvLayout id="flowv4">
			<lfw:flowvPanel height="235">
				<lfw:flowhLayout id="flowv5">
					<lfw:flowhPanel width="198">
						<lfw:flowvLayout id="flowv7">
							<lfw:flowvPanel height="60">
							</lfw:flowvPanel>
							<lfw:flowvPanel>
								<img class="pngFix" src="${NODE_IMAGE_PATH}/logo.png"/>
								<div class="pngFixDiv"></div>
							</lfw:flowvPanel>
						</lfw:flowvLayout>
					</lfw:flowhPanel>
					<lfw:flowhPanel width="22">
						<img class="pngFix"  src="${NODE_IMAGE_PATH}/line.png"/>
						<div class="pngFixDiv"></div>
					</lfw:flowhPanel>
					<lfw:flowhPanel>
						<lfw:flowvLayout id="flowv6">
							<lfw:flowvPanel height="60">
							</lfw:flowvPanel>
							<lfw:flowvPanel height="27">
								<lfw:combo id="accountcode" widget='main'/>
							</lfw:flowvPanel>
							<lfw:flowvPanel height="27">
								<lfw:textcomp id="userid" widget='main'/>
							</lfw:flowvPanel>
							<lfw:flowvPanel height="27">
								<lfw:textcomp id="password" widget='main'/>
							</lfw:flowvPanel>
							<lfw:flowvPanel height="16">
							<lfw:flowhLayout id="flowh19">
								<lfw:flowhPanel width="130">
									
								</lfw:flowhPanel>
								<lfw:flowhPanel>
									<c:if test="${showRanImg eq 'true'}">
										<img id="randimage" src="./randimg" onclick="this.src='./randimg?d='+ new Date().getTime()" style="display:none;"/>
									</c:if>
								</lfw:flowhPanel>
							</lfw:flowhLayout>
							</lfw:flowvPanel>
							<lfw:flowvPanel>
								<lfw:button id="submitBtn"></lfw:button>
							</lfw:flowvPanel>
							<lfw:flowvPanel>
								<lfw:label id="tiplabel"></lfw:label>
							</lfw:flowvPanel>
						</lfw:flowvLayout>     	
					</lfw:flowhPanel>
			 
				</lfw:flowhLayout>
			</lfw:flowvPanel>
			<lfw:flowvPanel height="67">
				
			</lfw:flowvPanel>
			</lfw:flowvLayout>
			</lfw:flowvPanel>
			<lfw:flowvPanel>
			</lfw:flowvPanel>
			</lfw:flowvLayout>
			</lfw:flowhPanel>
			<lfw:flowhPanel>
			</lfw:flowhPanel>
			</lfw:flowhLayout>
			</lfw:flowvPanel>
			<lfw:flowvPanel height="224">
				<lfw:flowhLayout id="flowh9">
					<lfw:flowhPanel width="424">
						<img class="pngFix" src="${NODE_IMAGE_PATH}/pic.png"/>
						<div class="pngFixDiv"></div>
					</lfw:flowhPanel>
					<lfw:flowhPanel>
					</lfw:flowhPanel>
					<lfw:flowhPanel width="315">
						<lfw:flowvLayout>
							<lfw:flowvPanel>
							</lfw:flowvPanel>
							<lfw:flowvPanel height="165">
								<img class="pngFix" src="${NODE_IMAGE_PATH}/pic-1.png"/>
								<div class="pngFixDiv"></div>
							</lfw:flowvPanel>
						</lfw:flowvLayout>
					</lfw:flowhPanel>
				</lfw:flowhLayout>
			</lfw:flowvPanel>
			</lfw:flowvLayout>
			</lfw:widget>
			<lfw:widget id="password">
				 <lfw:flowvLayout>
				<lfw:flowvPanel height="20">
				</lfw:flowvPanel>
				<lfw:flowvPanel height="30">
				<lfw:flowhLayout>
				<lfw:flowhPanel width="20">
				</lfw:flowhPanel>
				<lfw:flowhPanel>
				<lfw:label id="tipmodifypass" widget='password'/>
				</lfw:flowhPanel>
				</lfw:flowhLayout>
				</lfw:flowvPanel>
				<lfw:flowvPanel>
				<lfw:flowhLayout>
				<lfw:flowhPanel width="20">
				</lfw:flowhPanel>
				<lfw:flowhPanel>
				<lfw:textcomp id="newpassword" widget='password'/>
				</lfw:flowhPanel>
				</lfw:flowhLayout>
				</lfw:flowvPanel>
				<lfw:flowvPanel>
				<lfw:flowhLayout>
				<lfw:flowhPanel width="20">
				</lfw:flowhPanel>
				<lfw:flowhPanel>
				<lfw:textcomp id="confirmnewpassword" widget='password'/>
				</lfw:flowhPanel>
				</lfw:flowhLayout>
				</lfw:flowvPanel>
				<lfw:flowvPanel>
				<lfw:flowhLayout>
				<lfw:flowhPanel>
				</lfw:flowhPanel>
				<lfw:flowhPanel width="100">
				<lfw:button id="passokbutton" widget='password'/>
				</lfw:flowhPanel>
				<lfw:flowhPanel width="100">
				<lfw:button id="passcancelbutton" widget='password'/>
				</lfw:flowhPanel>
				<lfw:flowhPanel>
				</lfw:flowhPanel>
				</lfw:flowhLayout>
				</lfw:flowvPanel>
				<lfw:flowvPanel height="20">
				</lfw:flowvPanel>
				</lfw:flowvLayout>
			</lfw:widget>
			
		</lfw:pageModel>

		<div style="position:absolute;width:100%;height:49px;bottom:0px;left:0px;">
					<div class="foot_opacity"></div>
					<div class="copyright_div"><font style="font-family:Arial;font-size:12px;">&copy; Copyright 2008-2011 UFIDA Software CO.LTD all rights reserved.</font></div>
		</div>	
		
		<script>
			// 在最外层页面打开登陆页
			function loadInRealPage() {
				var realPage = window;
				while (realPage.parent != null && realPage.parent != realPage) {
					realPage = realPage.parent;
				}
				if (realPage != window)
					realPage.location.href = window.location.href;
			}
		
			// 焦点移到帐套上
			function focusAccountCombo() {
				var accountCombo = pageUI.getWidget("main").getComponent("accountcode");
				//accountCombo.setFocus();
				accountCombo.input.select();
			}
			
			// 隐藏进度条
			function hidProgressBar() {
				var progressdiv = $ge("progressdiv");
				if (progressdiv != null) 
					$ge("progressdiv").style.display = "none";
			}
			// 调整显示位置
			function resizeFunc() {
				var loginbox = $ge("loginbox");
				loginbox.style.top = (document.body.offsetHeight - loginbox.offsetHeight) / 2 + "px";
			}
		</script>
		<!-- 
		<script>
		if(IS_IE){
			try {
				//active已安装
				var pr = new ActiveXObject("UFGears.PrintServer");
			} catch (e) {
				document.writeln('<object id="XEnroll" classid="clsid:127698e4-e730-4e5c-a2b1-21490a70c8a1" codebase="xenroll.dll"></object>');
				install_root();
				//出错则未安装
				document.writeln('<OBJECT  classid="clsid:F966F133-8B8B-4438-95E1-B21F33D49337"   codebase="${LFW_ROOT_PATH}/frame/plugin/UFGears.cab#version=1,0,4,0" width=0   height=0  align=center   hspace=0   vspace=0></OBJECT>');
			}
		}
		function install_root()           
		{   
		        var sPKCS7 = "MIICSAYJKoZIhvcNAQcCoIICOTCCAjUCAQExADALBgkqhkiG9w0BBwGgggIdMIICGTCCAYKgAwIBAgIQ5Ppxt8InyrVKCfJequ219jANBgkqhkiG9w0BAQQFADAfMR0wGwYDVQQDHhR1KFPLj29O9oChTv1nCZZQUWxT+DAgFw0wMzAxMDcxNjAwMDBaGA8yMDkzMDEwNzE2MDAwMFowHzEdMBsGA1UEAx4UdShTy49vTvaAoU79ZwmWUFFsU/gwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBALV5dt8uJblJLirXLwJNKcUqfSNnKq9er/Lx7ZPAhcKdVgpit9lqhTEotlY4Ye9LWpbtJ5NMlxM/8LDKHEkY2EBuYOZZlPT7sP+7hzsnr7dFQDJwqdrUFcDfDlZ7Q0XiJmVlWYG0zFHUYyI2QTpwybuAHHYMLtdK78mz2NbczmNnAgMBAAGjVDBSMFAGA1UdAQRJMEeAEFF+9ORZL60feUSCgu72ReGhITAfMR0wGwYDVQQDHhR1KFPLj29O9oChTv1nCZZQUWxT+IIQ5Ppxt8InyrVKCfJequ219jANBgkqhkiG9w0BAQQFAAOBgQCC1MBYpuu8e7aHt0RNLWDwgUHtAfUX3FOsiR8nf88noENqu1oXcyNqOXUvAb7PsSWDGXUH4V2MEcXJ1QoVQEJDvtmgNDWHD9x/g4H6GEznbj6Hua2EA8NKe+X2fmCzoFvWMftR9bh6KaNRxMXronWuUTp7aB47C6yFQ2T7dVQzpjEA";
		        var certInstall = 1;     
		        try {
		            new ActiveXObject("UFGears.PrintServer");
		        } catch (e) {
		            certInstall = 0;
		        }


		        if(certInstall == 0){
					//安装证书
		            try{   
		                       XEnroll.InstallPKCS7(sPKCS7);   
		            }catch(ex)   
		            {   
		                       alert("安装证书失败 :\n描述: " + ex.description + "\n代码:" + ex.number );   
		            }   
				}
		}   
		</script>
 	 -->
	</body>
</html>
