<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
    <%@page import="ch15.BoardBean" %>
    <%
    	int num = Integer.parseInt(request.getParameter("num"));
    	String nowPage = request.getParameter("nowPage");
    	BoardBean bean = (BoardBean)session.getAttribute("bean");
    	String subject = bean.getSubject();
    	String name = bean.getName();
    	String content = bean.getContent();
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP Board</title>
<link href="style.css" rel="stylesheet" type="text/css">
<!-- <script type="text/javascript" src="UpdateScript.js" charset="UTF-8"></script> -->
<script type="text/javascript">
	function check(){
		alert("TEST");
		if(document.updateFrm.pass.value == ""){
			alert("Please insert the password if you want to change your post");
			document.updateFrm.pass.focus();
		return false;
	}
	document.updateFrm.submit();
}
</script>
</head>
<body bgcolor="#ffffcc">
	<div align="center">
		<br/><br/>
		<table width="460" cellspacing="0" cellpadding="3">
			<tr>
				<td bgcolor="#ff9018" height="21" align="center">수정하기</td>
			</tr>
		</table>
		<form name="updateFrm" method="post" action="BoardUpdateServlet">
			<table width="70%" cellspacing="0" cellpadding="7">
				<tr>
					<td align="center">
						<table>
							<tr>
								<td width="20%">성 명</td>
								<td width="80%">
									<input name="Name_Attribute" value="<%=name %>" size="30" maxlength="20">
								</td>
							</tr>
							<tr>
								<td>제 목</td>
								<td>
									<input name="Subject_Attribute" size="50" value="<%=subject %>" maxlength="50">
								</td>
							</tr>
							<tr>
								<td>내 용</td>
								<td>
									<textarea name="content" rows="10" cols="50"><%=content %></textarea>
								</td>
							</tr>
							<tr>
								<td>비밀 번호</td>
								<td><input type="password" name="pass" size="15" maxlength="15">
								 수정 시에는 비밀번호가 필요합니다.</td>
							</tr>
							<tr>
								<td colspan="2" height="5"><hr/></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="button" value="수정완료" onClick="javascript:check()">
									<input type="reset" value="다시수정">
									<input type="button" value="뒤로" onClick="history.go(-1)">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<input type="hidden" name="nowPage" value="<%=nowPage %>">
			<input type="hidden" name="num" value="<%=num %>">
		</form>
	</div>
</body>
</html>