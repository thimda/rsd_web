<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%@ taglib uri="http://www.ufida.com/multilang" prefix="ml" %>
<%@ taglib uri="http://www.ufida.com/lfwtool" prefix="lfwtool" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment"%>
<%@ page import="java.text.SimpleDateFormat"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> NC网上报销</title>
		<meta http-equiv="Page-Enter" Content="revealTrans(duration=5000, transition=23)">
		<lfw:base/>
		<lfw:head/>
		<!-- 以下两个css为框架专门定制  -->
		<link rel='STYLESHEET' type='text/css' href='${NODE_STYLE_PATH}/maintab.css'>
		<link rel='STYLESHEET' type='text/css' href='${NODE_STYLE_PATH}/main.css'>
		<style>
			body{
				padding:0px;
				margin:0px;
				overflow:hidden;
				border:0px;
			}
			.logodiv{
				background:url('${NODE_IMAGE_PATH}/top/nc-logo.png');
				_background:none;
				width:201px;
				height:41px;
				margin-left:36px;
				_filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${ROOT_PATH}/${NODE_IMAGE_PATH}/top/nc-logo.png');
			}
			
			.head_nav{
				text-decoration:none;
				color:#000000;
			}
		</style>
		<lfw:import/>
	</head>
	<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String date = sdf.format(LfwRuntimeEnvironment.getLfwSessionBean().getLoginDate().toDate()); 
	String userName = LfwRuntimeEnvironment.getLfwSessionBean().getUser_name();
	pageContext.setAttribute("date", date);
	pageContext.setAttribute("userName", userName);
	%>
	<body bottommargin="0" topmargin="0" leftmargin="0" rightmargin="0">
		<div id="bodydiv" style="width:100%;height:100%;">
			<lfw:pageModel>
				<lfw:border width="1">
					<lfw:flowvLayout>
						<c:if test="${IS_PORTAL ne '1'}">
							<lfw:flowvPanel height="60px">
								<%--<jsp:include page="main_head.jsp" />--%>
								<table id="webbx_main_head" border="0" cellpadding="0" cellspacing="0" style="width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/top/top-bg1.png') repeat-x;">
									<tr>
										<td align="left" rowspan="2">
											<div style="background:url('${NODE_IMAGE_PATH}/top/top-bg.png') no-repeat;">
												<img class="pngFix" src="${NODE_IMAGE_PATH}/top/ufida-logo.png" id="png_1" style="height:60px;width:140px">
												<img class="pngFix" src="${NODE_IMAGE_PATH}/top/logo-hline.png" id="png_2" style="height:60px;width:1px">
												<img class="pngFix" src="${NODE_IMAGE_PATH}/top/yer-net.png" id="png_3" style="height:60px;width:145px">
											</div>
										</td>
										<td align="left" style="width:580px">
										<lfw:panel id="mainPanel"  scroll="false" paddingTop="2" transparent="true" display="true" visibility="true">
											<lfw:gridLayout id="mainGrid" colcount="3" rowcount="1">
												<lfw:gridPanel height="25px" width="90px">
													<lfw:combo id="grouptext" widget="mainwidget"></lfw:combo>
												</lfw:gridPanel>
												<lfw:gridPanel height="25px" width="250px">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${userName},${ml:transp('lfw','lfw_hello')}&nbsp;&nbsp;<span id="currentDate">${date}</span>&nbsp;&nbsp;
												</lfw:gridPanel>
												<lfw:gridPanel  height="25px">
													<lfw:toolbar id="headerToolBar" widget="mainwidget"></lfw:toolbar>
												</lfw:gridPanel>
											</lfw:gridLayout>
											</lfw:panel>	
											<%--
											<table>
												<td><img class="pngFix" src="${NODE_IMAGE_PATH}/top/logout.png" id="img_lagout" onclick="logout();" style="cursor: hand"></td>
												<td><a href="#" onclick="logout();stopAll(event);" onfocus="this.blur();" class="head_nav">注销</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
												<td><img class="pngFix" src="${NODE_IMAGE_PATH}/top/settings.gif" id="img_lagout" onclick="modifyConfig();" style="cursor: hand"></td>
												<td><a href="#" onclick="modifyConfig();stopAll(event);" onfocus="this.blur();" class="head_nav">设置</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
											--%>
												<%--<td><img class="pngFix" src="${NODE_IMAGE_PATH}/top/help.png" id="img_help" onclick="alert('帮助')" style="cursor: hand"></td>
												<td><a href="#" onclick="window.open('/helpmain.jsp?datasource=design&langcode=simpchn&flag=201103&filename=erm201103.html');stopAll(event);" onfocus="this.blur();" class="head_nav">${ml:transp('lfw','lfw_help')}</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
												--%>
											<%--
											</table>
											--%>
										</td>
									</tr>
									<tr>
										<td align="right">
											<%-- ${corpName}&nbsp;&nbsp;${userName},${ml:transp('lfw','lfw_hello')}&nbsp;&nbsp;${date}
											--%>
											&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
									</tr>
								</table>
							</lfw:flowvPanel>
						</c:if>
						<lfw:flowvPanel>
							<div style="width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/top/bg-big.png') repeat;">
							<div style="width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/top/bg-big-1.png') repeat-x;">
								<lfw:border leftWidth="8" rightWidth="8" bottomWidth="8" color="">
									<lfw:flowhLayout>
										<lfw:flowhPanel width="190px">
											<div id="leftnavtd" style="width:100%;height:100%;">
											<lfw:gridLayout id="leftArea" colcount="3" rowcount="5">
												<%--第1行--%>
												<lfw:gridPanel height="28px" width="6px">
													<div style="width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/normal/bar-left.png') no-repeat;"></div>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<div style="width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/normal/bar-middle.png') repeat-x;">
														<div style="padding-top:8px;color:#FFFFFF;font-weight:bold;">功能区</div>
													</div>
												</lfw:gridPanel>
												<lfw:gridPanel width="6px">
													<div style="width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/normal/bar-right.png') no-repeat;"></div>
												</lfw:gridPanel>
												<%--第2行--%>
												
												<lfw:gridPanel>
													<div style="border-left:2px solid #0568A3;width:100%;height:100%;background-color:#E5EFF4;" align="center"></div>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<div style="width:100%;height:100%;background-color:#E5EFF4;" align="center">
														<div style="width:100%;height:100%;">
															<lfw:flowvLayout autoFill="false">
																<lfw:flowvPanel height="10px">
																</lfw:flowvPanel>
																
																<c:choose>
																	<c:when test="${screenHeight eq '768' or screenHeight eq '800' or screenHeight eq '900'}">
																		<c:set var="imgHeight" value="40"/>
																		<c:set var="imgWidth" value="36"/>
																		<c:set var="listHeight" value="120" />
																		<c:set var="textHeight" value="14" />
																	</c:when>
																	<c:when test="${screenHeight eq '600' or screenHeight eq '720'}">
																		<c:set var="imgHeight" value="30"/>
																		<c:set var="imgWidth" value="30"/>
																		<c:set var="listHeight" value="100" />
																		<c:set var="textHeight" value="12" />
																	</c:when>
																	<c:otherwise>
																		<c:set var="imgHeight" value="50"/>
																		<c:set var="imgWidth" value="48"/>
																		<c:set var="listHeight" value="200" />
																		<c:set var="textHeight" value="22" />
																	</c:otherwise>
																</c:choose>
																<lfw:flowvPanel>
																<c:forEach var="funVO" items="${funcvos}" begin="0" end="100" varStatus="s">
																	<div id="${funVO.pk_shortcut}" style="width:50%;height:50px" align="center"  valign="top">
																		<img class="pngFix" src="${NODE_IMAGE_PATH}/funnodes/node${s.index%5}.png" onclick="openNewPage('${funVO.fun_code}', '${funVO.name}','${funVO.ext1}');" onmouseover="this.style.cursor='pointer';" width="${imgWidth}" height="${imgWidth}"
																		oncontextmenu="onImgContextMenu('${funVO.pk_shortcut}', '${funVO.fun_code}', '${funVO.name}','${funVO.ext1}');"/>
																	</div>
																	<div id="text_${funVO.pk_shortcut}" style="width:100%;height:15px" align="center" valign="top">
																		<a href="#"  style="text-decoration:none;color:#000000;font-weight:bold" onclick="openNewPage('${funVO.fun_code}', '${funVO.name}','${funVO.ext1}');stopAll(event);">${funVO.name}</a>
																	</div>
																</c:forEach>
																<div id="shortCutDiv" style="width:100%;height:100%;overflow-x:hidden;overflow-y:auto;">
																</div>
																</lfw:flowvPanel>
																<lfw:flowvPanel height="10px">
																</lfw:flowvPanel>
															</lfw:flowvLayout>
														</div>
													</div>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<div style="border:0;width:100%;height:100%;background-color:#E5EFF4;" align="center">
														<div style="float:right;height:100%;width:2px;background-color:#0568A3;"></div>
													</div>
												</lfw:gridPanel>
												<%--第3行--%>
												<lfw:gridPanel width="4px" height="25px">
													<lfw:border leftWidth="2" rightWidth="4" bottomWidth="0" rightColor="url('${NODE_IMAGE_PATH}/normal/bg-left.png');border-bottom:#c3d1e4 1px solid;border-top:#c3d1e4 1px solid;" leftColor="#0568A3">
													</lfw:border>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<div onclick="showHideList()" style="padding-top:4px;width:100%;height:100%;background:url('${NODE_IMAGE_PATH}/normal/bg-left.png') repeat-x;border-top:1px solid #C3D1E4;border-bottom:1px solid #C3D1E4;" onmouseover="this.style.background='url(${NODE_IMAGE_PATH}/normal/bg-left-over.png) repeat-x';$ge('$d_border_2_right').style.background = $ge('$d_border_3_left').style.background = this.style.background;" onmouseout="this.style.background='url(${NODE_IMAGE_PATH}/normal/bg-left.png) repeat-x';$ge('$d_border_2_right').style.background = $ge('$d_border_3_left').style.background = this.style.background;">
														<div style="height:100%;float:right">
															<img class="pngFix" src="${NODE_IMAGE_PATH}/normal/b.png" id="funnodeShowImg" onmouseover="changeHideImage(this, 1);" onmouseout="changeHideImage(this, 0);" />&nbsp;&nbsp;
														</div>
														<div style="height:100%;float:right;padding-top:2px">
															<span id="funnodeShowText">隐藏</span>&nbsp;&nbsp;
														</div>
													</div>
												</lfw:gridPanel>
												<lfw:gridPanel width="4px">
													<lfw:border leftWidth="4" rightWidth="2" bottomWidth="0"  leftColor="url('${NODE_IMAGE_PATH}/normal/bg-left.png');border-bottom:#c3d1e4 1px solid;border-top:#c3d1e4 1px solid;" rightColor="#0568A3">
													</lfw:border>
												</lfw:gridPanel>
												<%--第4行--%>
												<lfw:gridPanel height="200px">
													<div style="border-left:2px solid #0568A3;width:100%;height:100%;background-color:#E5EFF4;" align="center"></div>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<div id="funnodeListTr" style="border:0;width:100%;height:100%;overflow:auto;background-color:#E5EFF4;" valign="top">
														<lfw:tree id="funnodetree"/>
													</div>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<div style="border:0;width:100%;height:100%;background-color:#E5EFF4;" align="center">
														<div style="float:right;height:100%;width:2px;background-color:#0568A3;"></div>
													</div>
												</lfw:gridPanel>
												<%--第5行--%>
												<lfw:gridPanel height="2px">
													<lfw:border leftWidth="2" rightWidth="0" bottomWidth="2" color="#0568A3">
														<div style="border:0;width:100%;height:100%;background-color:#E5EFF4;" align="center">
														</div>
													</lfw:border>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<lfw:border leftWidth="0" rightWidth="0" bottomWidth="2" color="#0568A3">
														<div style="border:0;width:100%;height:100%;background-color:#E5EFF4;" align="center">
														</div>
													</lfw:border>
												</lfw:gridPanel>
												<lfw:gridPanel>
													<lfw:border leftWidth="0" rightWidth="2" bottomWidth="2" color="#0568A3">
														<div style="border:0;width:100%;height:100%;background-color:#E5EFF4;" align="center">
														</div>
													</lfw:border>
												</lfw:gridPanel>
												
											</lfw:gridLayout>
											</div>
										</lfw:flowhPanel>
										
										<lfw:flowhPanel width="4px">
											<script>window.leftNavHide = false;            function showHideLeftNav(){             if(window.leftNavHide == false){              $ge('$d_01_1').style.display = "none";              $ge('$d_01_3').style.width = $ge('$d_01_3').offsetWidth + 190 + "px";            window.leftNavHide = true;             }             else{              $ge('$d_01_1').style.display = "block";             $ge('$d_01_3').style.width = $ge('$d_01_3').offsetWidth - 190 + "px";            window.leftNavHide = false;             }            }</script>
											<img class="pngFix" src="${NODE_IMAGE_PATH}/tab/close-left-img.png" onclick="showHideLeftNav();" onmouseover="this.style.cursor='pointer';" style="position:absolute;top:50%"/>
										</lfw:flowhPanel>
										<lfw:flowhPanel>
											<div id="tabDiv" style="width:100%;height:100%;">
											<lfw:tab id="pagetab" className="maintab_div">
			    								<lfw:tabitem id="desktop" text="首页" i18nName="lfw_mainpage" langDir="lfw">
			    									<lfw:border leftWidth="2" rightWidth="2" bottomWidth="2" color="#0568A3">
			    										<div style="width:100%;height:100%;overflow:hidden;">
			    											<iframe name="showFrame" id="showFrame" width="100%" height="100%" border="0" frameborder="0">loading...</iframe>
			    										</div>
			    									</lfw:border>
												</lfw:tabitem>
						    				</lfw:tab>
						    				</div>
										</lfw:flowhPanel>
									</lfw:flowhLayout>
								</lfw:border>
							</div></div>
						</lfw:flowvPanel>
					</lfw:flowvLayout>
				</lfw:border>
				<lfw:contextmenu id="shortcut_menu" widget="navwidget"></lfw:contextmenu>
			</lfw:pageModel>
		</div>
	
		<script>
			<%@ include file="mainutil.jsp"%>
			function externalInit() {
				var date = new Date();
				var year = date.getFullYear();
				var month = date.getMonth() + 1;
				if (parseInt(month)<10) month = "0" + month;
				var day = date.getDate();
				if (parseInt(day)<10) day = "0" + day;
				var formatString = year + "-" + month + "-" + day;
				
				$ge('currentDate').innerHTML = formatString;
			
				var clientCache = '${CLIENT_CACHE}';
				if(clientCache == "1") {
					var proxy = getCacheProxy(window.globalPath + "_" + window.datasourceName);
					if(proxy.isCacheEnabled()){
						if(!proxy.isInitialized()){
							proxy.initCache();
						}
						else{
							<%
								java.util.Map map = (java.util.Map)application.getAttribute("CACHE_VERSION_MAP");
								String objStr = nc.uap.lfw.core.serializer.impl.LfwJsonSerializer.getInstance().toJsObject(map);
							%>
							eval('var versionMap = <%= objStr %>');
							proxy.updateCache(versionMap);
						}
					}
				}
				
				/*
				var tab = getComponent("pageTab");
				if (tab != null) {
					
					tab.closeItem = function(item) {
						for (var i = 0, count = item.divContent.childNodes.length; i < count; i ++) {
							if (item.divContent.childNodes[i].firstChild) {
								var frame = item.divContent.childNodes[i].firstChild;
								frame.src = "";
							}	
						}
					}
				}
				isOtherUserLogin();
				
				showHideList();
				
				changeHideImage($ge('funnodeShowImg'), 0);
				*/
				//判断是否有其他人登陆
				isOtherUserLogin();
			}

			function onBeforeDestroyPage() {
				// 用户已经注销则不继续执行
				if (window.$logoutflag != null && window.$logoutflag == true)
					return false;
			}
		</script>
	</body>
</html>
