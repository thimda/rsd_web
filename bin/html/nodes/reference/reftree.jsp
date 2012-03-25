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
		<%@include file="refscript.jsp" %>
	</head>
	<body>
		<lfw:pageModel>
				<lfw:borderLayout>
					<%--
					<lfw:borderPanel position="top" height="30">
						<lfw:flowhLayout>
							<lfw:flowhPanel>
								<%@include file="header.jsp" %>
							</lfw:flowhPanel>
							
							<c:if test="${showModeBtn eq true}">
								<lfw:flowhPanel width="50" align="right">
								 	<lfw:button id="changeViewModeBtn"/>
								</lfw:flowhPanel>
							</c:if>
						</lfw:flowhLayout>
					</lfw:borderPanel>
					--%>
					<lfw:borderPanel position="center">
						<%--<lfw:cardLayout type="card" cardId="refTreeCard" border="0">
							<lfw:cardPanel cardId="card1">
							--%>
								<lfw:tree id="reftree" />
							<%--</lfw:cardPanel>
							<lfw:cardPanel cardId="card2">
								<c:if test="${showModeBtn eq true}">
									<lfw:grid id="refgrid"></lfw:grid>
								</c:if>
							</lfw:cardPanel>
						</lfw:layout>
							--%>
					</lfw:borderPanel>
					<c:if test="${param.showType != 'type_div'}">
						<lfw:borderPanel position="bottom" height="42px">
							<lfw:flowhLayout>
								<lfw:flowhPanel>
								</lfw:flowhPanel>
								<lfw:flowhPanel width="80px">
									<lfw:button id="okbt"/>
								</lfw:flowhPanel>
								<lfw:flowhPanel width="100px">
									<lfw:button id="cancelbt"/>
								</lfw:flowhPanel>
							</lfw:flowhLayout>
						</lfw:borderPanel>
					</c:if>
				</lfw:borderLayout>
		</lfw:pageModel>
		<script>
		/**
			function selfBeforeReferenceOk() {
				var selLeafOnly = (getParameter("selLeafOnly") != null && getParameter("selLeafOnly") == "true");
				if(selLeafOnly){
					var tree = getComponent("reftree");
					var node = tree.getSelectedNode();
					if(node == null)
						return true;
					return node.isLeaf;
				}
				return true;
			}
			TreeViewComp.prototype.onAfterSelNodeChange = function(node)
			{	
				var selLeafOnly = (getParameter("selLeafOnly") != null && getParameter("selLeafOnly") == "true");
				if(selLeafOnly){
					if(!node.isLeaf)
						getComponent('okbt').setActive(false);
					else
						getComponent('okbt').setActive(true);
				}
			};
*/
		</script>
	</body>
</html>