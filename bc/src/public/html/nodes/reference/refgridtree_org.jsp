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
			<lfw:flowvLayout>
				<lfw:flowvPanel height="30">
					<lfw:textcomp id="refcomp_org" widget='main' width="250"/>
				</lfw:flowvPanel>
				<lfw:flowvPanel>	
					<lfw:borderLayout>
						<%--
						<c:if test="${param.showType != 'type_div'}">
							<lfw:borderPanel position="top" height="30">
								 <%@include file="header.jsp" %>
							</lfw:borderPanel>
						</c:if>
						--%>
						<lfw:borderPanel position="center">
		                	<lfw:spliter divideSize="0.40" id="spliter2" orientation="h">
								<lfw:spliterPanelOne>
									<lfw:border leftWidth="1">
										<lfw:tree id="reftree"/>
									</lfw:border>
								</lfw:spliterPanelOne>
								<lfw:spliterPanelTwo>
									<lfw:border rightWidth="1">
							        	<lfw:grid id="refgrid"/>
									</lfw:border>
							  	</lfw:spliterPanelTwo>
							</lfw:spliter>
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
				</lfw:flowvPanel>	
			</lfw:flowvLayout>
		</lfw:pageModel>
	</body>
</html>