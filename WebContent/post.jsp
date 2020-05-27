<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP Board</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor = "#ffffcc">
	<div align="center">
		<br/><br/>
		<table width="600" cellpadding="3">
			<tr>
				<td bgcolor="84F399" height="25" align="center">글쓰기</td>
			</tr>
		</table>
	</div>
	<br/>
	<form name="postFrm" method="post" action="BoardPostServlet" enctype="multipart/form-data">
		<table width="600" cellpadding="3" align="center">
			<tr>
				<td align="center">
					<table align="center">
						<tr>
							<td width="10%">성 명</td>
							<td width="90%">
							<input name="Name_Attribute" size="10" maxlength="8"></td>
						</tr>
						<tr>
							<td>제 목</td>
							<td>
							<input name="Subject_Attribute" size="50" maxlength="30"></td>
						</tr>
						<tr>
							<td>내 용</td>
							<td><textarea name="content" rows="10" cols="50"></textarea></td>
						</tr>
						<tr>
							<td>비밀 번호</td>
							<td><textarea type="password" name="pass" size="15" maxlength="15"></textarea>
						</tr>
						<tr>
							<td>파일 찾기</td>
							<td><input type="file" name="filename" size="50" maxlength="50"></td>
						</tr>
						<tr>
							<td>내용 타입</td>
							<td>HTML<input type="radio" name="contentType" value="HTTP">&nbsp; &nbsp; &nbsp;
							TEXT<input type="radio" name="contentType" value="TEXT" checked>
							</td>
						</tr>
						<tr>
							<td colspan="2"><hr/></td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="submit" value="등록">
								<input type="reset" value="다시 쓰기">
								<input type="button" value="리스트" onClick="location.href='list.jsp'">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<input type="hidden" name="ip" value="<%=request.getRemoteAddr()%>">
	</form>
</body>
</html>