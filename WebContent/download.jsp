<%@ page language="java" contentType="text/html; application; charset=EUC-KR"
    pageEncoding="UTF-8"%>
    <jsp:useBean id="bMgr" class="ch15.BoardMgr"/>
    <% 
    	bMgr.downLoad(request, response, out, pageContext);
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Download</title>
</head>
<body>

</body>
</html>