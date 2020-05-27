package ch15;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class BoardUpdateServlet
 */
@WebServlet("/BoardUpdateServlet")
/* @WebServlet("/ch15/boardUpdate") */
public class BoardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		BoardMgr bMgr = new BoardMgr();
		BoardBean bean = (BoardBean) session.getAttribute("bean");
		if(bean == null) {
			
			System.out.println("tblBoard Update Bean null");
			return;
		}
		String nowPage = request.getParameter("nowPage");
		
		BoardBean upBean = new BoardBean();
		upBean.setNum(Integer.parseInt(request.getParameter("num")));
		upBean.setName(request.getParameter("Name_Attribute"));
		upBean.setSubject(request.getParameter("Subject_Attribute"));
		upBean.setContent(request.getParameter("content"));
		upBean.setPass(request.getParameter("pass"));
		upBean.setIp(request.getParameter("ip"));
		
		String upPass = upBean.getPass();
		String inPass = bean.getPass();
		
		if(upPass.equals(inPass)) {
			bMgr.updateBoard(upBean);
			String url = "read.jsp?nowPage=" + nowPage + "&num=" + upBean.getNum();
			response.sendRedirect(url);
		}else {
			out.println("<script>");
			out.println("alert('비밀번호가 틀렸습니다.');");
			out.println("history.back();");
			out.println("</script>");
		}
	}
}
