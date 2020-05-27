package ch15;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BoardReplyServlet
 */
@WebServlet("/BoardReplyServlet")
public class BoardReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("EUC-KR");
		
		BoardMgr bMgr = new BoardMgr();
		BoardBean reBean = new BoardBean();
		
		reBean.setName(request.getParameter("name"));
		reBean.setSubject(request.getParameter("subject"));
		reBean.setContent(request.getParameter("content"));
		reBean.setRef(Integer.parseInt(request.getParameter("ref")));
		reBean.setPos(Integer.parseInt(request.getParameter("pos")));
		reBean.setDepth(Integer.parseInt(request.getParameter("depth")));
		
		reBean.setPass(request.getParameter("pass"));
		reBean.setIp(request.getParameter("ip"));
		
		/* ref = �亯 ���� �ҼӵǾ� �ִ� �� ���� ��ȣ */
		/* pos = �� �� �ؿ� �亯 ���� ����� ���� ���� */
		/* �ټ��� �亯 �� �߰��� �亯�� �߰��ϱ� ���ؼ� �߰��Ϸ��� �亯 �ۺ��� 
		 * �Ʒ��� �ִ�  �亯�۵��� pos ���� 1�� �������Ѽ� ������ �о���ϴ�. */
		bMgr.replyUpBoard(reBean.getRef(), reBean.getPos());
		bMgr.replyBoard(reBean);
		
		String nowPage = request.getParameter("nowPage");
		response.sendRedirect("list.jsp?nowPage="+nowPage);
	}

}
