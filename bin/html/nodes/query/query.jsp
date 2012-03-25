<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>UFIDA NC</title>
		<lfw:base/>
		<lfw:head/>
		<lfw:import/>
	
		<script type='text/javascript' src='${LFW_ROOT_PATH}/frame/script/mvc/bill/querytemplate/QueryTemplatePanel.js'></script>
		<script type='text/javascript' src='${LFW_ROOT_PATH}/frame/script/mvc/bill/querytemplate/QueryTemplateProcessor.js'></script>
		<script type='text/javascript' src='${LFW_ROOT_PATH}/frame/script/mvc/bill/querytemplate/BetweenPanel.js'></script>
	</head>
	<body>
		<lfw:pageModel> 
			<lfw:borderLayout>
				<lfw:borderPanel position="center">
					<lfw:spliter divideSize="0.25" id="spliter1" orientation="h">
						<lfw:spliterPanelOne>
							<lfw:border width="1">
							<lfw:tab id="conditionTab">
								<lfw:tabitem id="nowCondition" text="候选条件" i18nName="query_candidatecondition" langDir="lfw">
									<lfw:tree id="queryTemplateTree" />
								</lfw:tabitem>
								<%--
								<lfw:tabitem id="savedCondition" text="查询方案" i18nName ="query_scheme" langDir="lfw">
									<div id="favoritDiv" style="height:24px;background:#F0EFE7"></div>
									<lfw:tree id="savedTree" />
								</lfw:tabitem>
								--%> 
							</lfw:tab>
							</lfw:border>
						</lfw:spliterPanelOne>
						<lfw:spliterPanelTwo>
							<lfw:tab id="sqlTab">
								<%--
								<lfw:tabspace id="rightspace" position="right" width="75px">
									<lfw:toolbar id="queryConditionToolbar"></lfw:toolbar>
								</lfw:tabspace>
								--%>
								<lfw:tabitem id="normalTab" text="普通" i18nName ="query_general" langDir="lfw">
									<div id="$d_normalPanel" style="width:100%;height:100%;overflow-y:auto;"></div>
								</lfw:tabitem>
								<%--
								<lfw:tabitem id="advTab" text="高级">
									<lfw:flowvLayout>
										<lfw:flowvPanel height="0px">
											<div id="$d_advancePanel" style="width:100%;"></div>
										</lfw:flowvPanel>
										<lfw:flowvPanel>
											<lfw:tree id="advanceTree" />
										</lfw:flowvPanel>
									</lfw:flowvLayout>
								</lfw:tabitem>	
								--%>
							</lfw:tab>
						</lfw:spliterPanelTwo>
					</lfw:spliter>
				</lfw:borderPanel>
								
				<lfw:borderPanel position="bottom" height="32px">
					<lfw:flowhLayout>
						<lfw:flowhPanel>
						</lfw:flowhPanel>
						<lfw:flowhPanel width="80px">
							<lfw:button id="okBt"/>
						</lfw:flowhPanel>
						<lfw:flowhPanel width="100px">
							<lfw:button id="cancelBt"/>
						</lfw:flowhPanel>
					</lfw:flowhLayout>
				</lfw:borderPanel>
			</lfw:borderLayout>
			
		</lfw:pageModel>
		<script>
		<%@ include file="querytemplateq.jsp"%>
		</script>
	</body>
</html>