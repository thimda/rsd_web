<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><%@ page contentType="text/html;charset=UTF-8" %><%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %><%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">	
	<head>		
		<title>UFIDA NC</title>		
		<lfw:base/>		
		<lfw:head/>		
		<lfw:import />	
	</head>	
	<body>
		<script>
			function doPreview(id) {
				var obj = parent.getSessionAttribute("print_content_" + id);
				objPdf.preview(obj);
			}
		</script>
		<lfw:pageModel>
			<OBJECT
			  classid="clsid:F966F133-8B8B-4438-95E1-B21F33D49337"
			  codebase="${LFW_ROOT_PATH}/frame/plugin/UFGears.inf#version=1,0,2,0"
			  width=100%
			  height=100%
			  align=center
			  hspace=0
			  vspace=0
			  name="objPdf">
			</OBJECT>
		</lfw:pageModel>
	</body>
</html>
