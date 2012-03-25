<%@ page import="java.util.Map"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="nc.uap.lfw.core.cache.LfwCacheManager"%>
<body>
	<table style="width:100%" border="1">
		<thead>
			<tr>
				<td colspan="2">Session Scope</td>
			</tr>
			<tr>
				<td>Object Key</td><td>Object Type</td>
			</tr>
		</thead>
		<tbody>
			<%
				Enumeration em = request.getSession().getAttributeNames();
				while(em.hasMoreElements()){
					String key = (String)em.nextElement();
					Object value = request.getSession().getAttribute(key);
				
			%>
			<tr>
				<td><%= key %></td><td><%= value.getClass().toString() %></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
</body>