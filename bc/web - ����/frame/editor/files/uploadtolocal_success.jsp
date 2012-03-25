<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<title>
				文件上传
			</title>
	</head>
	<body topmargin="10" leftmargin="0">		
		<c:if test="${not empty exception}">
			文件上传失败
		</c:if>
		<c:if test="${empty exception}">
			文件上传成功
			<script>
				<c:forEach var="fileName" items="${filenames}" varStatus="s">
					window.parent.UploadSaved('${fileName}', '${fileKeys[s.index]}');
				</c:forEach>
			</script>
		</c:if>
	</body>
</html>