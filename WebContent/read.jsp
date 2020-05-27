<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="ch15.BoardBean" %>
    <jsp:useBean id="bMgr" class="ch15.BoardMgr"/>
    <%
    	request.setCharacterEncoding("UTF-8");
    	int num = Integer.parseInt(request.getParameter("num"));
    	String nowPage = request.getParameter("nowPage");
    	
    	String keyField = request.getParameter("keyField");
    	String keyWord = request.getParameter("keyWord");
    	bMgr.upCount(num);
    	BoardBean bean = bMgr.getBoard(num);
    	
    	String name = bean.getName();
    	String subject = bean.getSubject();
    	String regdate = bean.getRegdate();
    	String content = bean.getContent();
    	String filename = bean.getFilename();
    	
    	int filesize = bean.getFilesize();
    	String ip = bean.getIp();
    	int count = bean.getCount();
    	session.setAttribute("bean", bean);
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSPBoard</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="ListScript.js" charset="UTF-8"></script>
</head>
<body bgcolor="#ffffcc">
	<br/><br/>
	<table align="center" width="70%" border="0" cellspacing="3" cellpadding="0">
		<tr>
			<td bgcolor="#9ca2ee" height="25" align="center">글 읽기</td>
		</tr>
		<tr>
			<td colspan="2">
				<table border="0" cellpadding="3" cellspacing="0" width="100%">
					<tr>
						<td align="center" bgcolor="#dddddd" width="10%">이 름</td>
						<td bgcolor="#ffffe8"><%=name %></td>
						<td align="center" bgcolor="#dddddd" width="10%">등록 날짜</td>
						<td bgcolor="#ffffe8"><%=regdate %></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#dddddd">제 목</td>
						<td bgcolor="#ffffe8" colspan="3"><%=subject %></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#dddddd">첨부 파일</td>
						<td bgcolor="#ffffe8" colspan="3">
							"Test";
							<%if(filename != null && filename == ""){ %>
								<a href="javascript:down('<%=filename %>')"><%=filename %></a>
								&nbsp; &nbsp; <font color="blue">(<%=filesize %>KBytes)</font>
							<%}else{ %> 등록된 파일이 없습니다. <%} %>
						</td>
					</tr>
					<tr>
						<td colspan="4"><br/><pre><%=content %></pre><br/></td>
					</tr>
					<tr>
						<td colspan="4" align="right">
							<%=ip %>로부터 글을 남기셨습니다. / 조회수 <%=count %>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<hr/>
				[ <a href="javascript:list()">리스트</a> |
				<a href="update.jsp?nowPage=<%=nowPage %>&num=<%=num %>">수 정</a> |
				<a href="reply.jsp?nowPage=<%=nowPage %>">답 변</a> |
				<a href="delete.jsp?nowPage=<%=nowPage %>&num=<%=num %>">삭 제</a> ]<br/>
			</td>
		</tr>
	</table>
	
	<form name="downFrm" action="download.jsp" method="post">
		<input type="hidden" name="filename">
	</form>
	<form name="listFrm" action="list.jsp" method="post">
		<input type="hidden" name="newPage" value="<%=nowPage%>">
		<%if(!(keyWord==null || keyWord.equals(""))){ %>
			<input type="hidden" name="keyField" value="<%=keyField %>">
			<input type="hidden" name="keyWord"_ value="<%=keyWord %>">
		<%} %>
	</form>
</body>
</html>