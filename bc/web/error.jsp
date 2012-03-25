<%@ page contentType="text/html; charset=gb2312" language="java" import="java.io.*"  isErrorPage="true"%>   
<html>   
<head>   
<title>页面出错啦！</title>   
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />   
</head>   
<body>   
	

<div align="center">
抱歉，您请求的页面出错啦！
  　</div>
<div align="center">
  <font style="BACKGROUND-COLOR: #fffffd" color="#0000ff" size="4"></font></div>
<%            
         if("true".equals(System.getProperty("showexception"))) {       
     	String exception_info = (String) request.getAttribute("javax.servlet.error.message");   
       Exception theException = (Exception) request.getAttribute("javax.servlet.error.exception_type");         
               
      out.println("<br><b>exception_type:</b> " +  theException);   
      out.println("<br><b>Exception:</b>" + exception_info);   
      }
%>

<table>   
  <tbody>   
    <tr>   
      <td>   
     
      <hr>      </td>   
    </tr>   
    <tr>   
      <td></td>   
    </tr>   
    <tr>   
      <td>   
     </TD>   
    </TR>   
      <TD>   
      <HR>      </TD>   
    </TR>   
  </TBODY>   
</TABLE>   

</body>   
</html>