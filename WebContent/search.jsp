<%@ page import="java.util.*" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.io.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<%
//String[] list = (String[])session.getAttribute("result");
//String m=(String)session.getAttribute("Memory");
String r=(String)session.getAttribute("result");
out.println("<p>"+r+"</p>");
%>
</body>
</html>