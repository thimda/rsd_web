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
	
		<script type='text/javascript' src='/lfw/frame/script/mvc/bill/querytemplate/QueryTemplatePanel.js'></script>
		<script type='text/javascript' src='/lfw/frame/script/mvc/bill/querytemplate/QueryTemplateController.js'></script>
		<script type='text/javascript' src='/lfw/frame/script/mvc/bill/querytemplate/QueryTemplateProcessor.js'></script>
		<script type='text/javascript' src='/lfw/frame/script/mvc/bill/querytemplate/BetweenPanel.js'></script>
	</head>
	<body bottommargin="0" topmargin="0" leftmargin="0" rightmargin="0" scroll=no onload="pageBodyScript();">
		<lfw:pageModel> 
			<lfw:contextmenu id="queryTemplateContextMenu"/>
			<lfw:contextmenu id="saveContextMenu"/>
			<lfw:borderLayout>
				<lfw:borderPanel position="center">
					<lfw:spliter divideSize="0.25" id="spliter1" orientation="h">
						<lfw:spliterPanelOne>
							<lfw:border width="1">
							<lfw:tab id="conditionTab">
								<lfw:tabitem id="nowCondition" text="查询方案">
									<lfw:tree id="queryTemplateTree" />
								</lfw:tabitem>
								<lfw:tabitem id="savedCondition" text="候选条件">
									<div id="favoritDiv" style="height:24px;background:#F0EFE7"></div>
									<lfw:tree id="savedTree" />
								</lfw:tabitem>	
							</lfw:tab>
							</lfw:border>
						</lfw:spliterPanelOne>
						<lfw:spliterPanelTwo>
							<lfw:tab id="sqlTab">
								<lfw:tabitem id="normalTab" text="普通">
									<div id="$d_normalPanel" style="width:100%;height:100%;overflow-y:scroll;"></div>
								</lfw:tabitem>
								<lfw:tabitem id="advTab" text="高级">
									<lfw:layout type="flowv">
										<lfw:layoutPanel>
											<div id="$d_advancePanel" style="width:100%;"></div>
										</lfw:layoutPanel>
										<lfw:layoutPanel>
											<lfw:tree id="advanceTree" />
										</lfw:layoutPanel>
									</lfw:layout>
								</lfw:tabitem>	
							</lfw:tab>
						</lfw:spliterPanelTwo>
					</lfw:spliter>
				</lfw:borderPanel>
								
				<lfw:borderPanel position="bottom" height="35px">
					<lfw:flowhLayout>
						<lfw:flowhPanel>
						</lfw:flowhPanel>
						<lfw:flowhPanel width="100px">
							<lfw:button id="okBt"/>
						</lfw:flowhPanel>
						<lfw:flowhPanel width="100px">
							<lfw:button id="cancelBt"/>
						</lfw:flowhPanel>
					</lfw:flowhLayout>
				</lfw:borderPanel>
			</lfw:borderLayout>
			<lfw:dialog id="conditionDialog">
				<lfw:borderLayout>
					<lfw:borderPanel position="top" height="20px"/>
					<lfw:borderPanel position="center">
						<lfw:flowhLayout>
							<lfw:flowhPanel>
								<div id="$d_dynCaption" style="width:100%;height:100%;"></div>
							</lfw:flowhPanel>
							<lfw:flowhPanel width="60px">
								<div id="$d_dynCond" style="width:100%;height:100%;"></div>
							</lfw:flowhPanel>
							<lfw:flowhPanel>
								<div id="$d_dynValue" style="width:100%;height:100%;"></div>
							</lfw:flowhPanel>
						</lfw:flowhLayout>
					</lfw:borderPanel>
					<lfw:borderPanel position="bottom" height="30px">
						<lfw:flowhLayout>
							<lfw:flowhPanel>
								<lfw:button id="preBt"/>
							</lfw:flowhPanel>
							<lfw:flowhPanel>
								<lfw:button id="nextBt"/>
							</lfw:flowhPanel>
							<lfw:flowhPanel>
								<lfw:button id="okBt1"/>
							</lfw:flowhPanel>
						</lfw:flowhLayout>
					</lfw:borderPanel>
				</lfw:borderLayout>
			</lfw:dialog>
			
			<lfw:dialog id="saveDialog">
				<lfw:flowvLayout>
					<lfw:flowvPanel height="60px">
						<lfw:flowhLayout>
							<lfw:flowhPanel width="200px">
								<lfw:label id="saveLabel"/>
							</lfw:flowhPanel>
							<lfw:flowhPanel>
								<lfw:textcomp id="saveText"/>
							</lfw:flowhPanel>
						</lfw:flowhLayout>
					</lfw:flowvPanel>
					<lfw:flowvPanel height="60px">
						<lfw:flowhLayout>
							<lfw:flowhPanel width="200px">
								<lfw:button id="saveOkBt"/>
							</lfw:flowhPanel>
							<lfw:flowhPanel>
								<lfw:button id="saveCancelBt"/>
							</lfw:flowhPanel>
						</lfw:flowhLayout>
					</lfw:flowvPanel>
				</lfw:flowvLayout>
			</lfw:dialog>
		</lfw:pageModel>
		<script>
		<%@ include file="querytemplateq.jsp"%>
		</script>
	</body>
</html>