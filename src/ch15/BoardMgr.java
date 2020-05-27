package ch15;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.tomcat.util.descriptor.web.MultipartDef;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class BoardMgr {
	private DBConnectionMgr pool;
	private static final String SAVEFOLDER = "D:/Java_JSP_oxygen/DBMS_MySQL_NoticeBoard/WebContent/ch15/fileupload";
	private static final String ENCTYPE = "EUC-KR";
	private static int MAXSIZE = 5*1024*1024;
	
	public BoardMgr() {
		try {
			pool = DBConnectionMgr.getInstance();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 2 */
	// 게시판 List
	public Vector<BoardBean> getBoardList(String keyField, String keyWord, 
			int start, int end){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<BoardBean>vlist = new Vector<BoardBean>();
		
		// // DBConnectionbMgr pool 객체를 통해 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			if(keyWord.equals("null") || keyWord.equals("")) {
				sql = "select * from tblboard order by ref desc, pos limit ?, ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,  start);
				pstmt.setInt(2, end);
			}else {
				sql = "select * from tblboard where " + keyField + " like ?";
				sql += "order by ref desc, pos limit ? , ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,  "%" + keyWord + "%");
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt("num"));
				bean.setName(rs.getString("Name_Attribute"));
				bean.setSubject(rs.getString("Subject_Attribute"));
				bean.setPos(rs.getInt("pos"));
				bean.setRef(rs.getInt("ref"));
				bean.setDepth(rs.getInt("depth"));
				bean.setRegdate(rs.getString("regdate"));
				bean.setCount(rs.getInt("count"));
				vlist.add(bean);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt, rs);
		}
		return vlist;
	}
	
	/* 3 */
	// 총 게시물 수
	public int getTotalCount(String keyField, String keyWord) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int totalCount = 0;
		
		// // 총 게시물 수 또한 DB(MySQL)에 연결  (게시물 개수를 나타내는 num 값 받아오기)
		try {
			conn = pool.getConnection();
			if(keyWord.equals("null") || keyWord.equals("")) {
				sql = "select count(num) from tblboard";
				pstmt = conn.prepareStatement(sql);
			}else {
				sql = "select count(num) from tblboard where " + keyField + " like ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,  "%" + keyWord + "%");
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				totalCount = rs.getInt(1);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt, rs);
		}
		
		return totalCount;
	}
	
	/* i --> 별도 */
	// 게시판 입력
	public void insertBoard(HttpServletRequest req) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		MultipartRequest multi = null;
		int filesize = 0;
		String filename = null;
		
		// // 게시판 입력 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "select max(num) from tblboard";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int ref = 1;
			
			if(rs.next())
				ref = rs.getInt(1) + 1;
			File file = new File(SAVEFOLDER);
			if(!file.exists())
				file.mkdirs();
			
			multi = new MultipartRequest(req, SAVEFOLDER, MAXSIZE, ENCTYPE, 
					new DefaultFileRenamePolicy());
			
			if(multi.getFilesystemName("filename") != null) {
				filename = multi.getFilesystemName("filename");
				filesize = (int)multi.getFile("filename").length();
			}
			String content = multi.getParameter("content");
			if(multi.getParameter("contentType").equalsIgnoreCase("TEXT")) {
				content = UtilMgr.replace(content, "<", "&lt;");
			}
			
			sql = "insert tblboard (Name_Attribute, content, Subject_Attribute, ref, pos, depth, regdate, pass, count, ip, filename, filesize)";
			sql += "values(?, ?, ?, ?, 0, 0, now(), ?, 0, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, multi.getParameter("Name_Attribute"));
			pstmt.setString(2, content);
			pstmt.setString(3,  multi.getParameter("Subject_Attribute"));
			pstmt.setInt(4, ref);
			
			pstmt.setString(5, multi.getParameter("pass"));
			pstmt.setString(6, multi.getParameter("ip"));
			pstmt.setString(7, filename);
			pstmt.setInt(8, filesize);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt, rs);
		}
	}
	
	/* 1 */
	// 게시물  Return
	public BoardBean getBoard(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		BoardBean bean = new BoardBean();
		
		// // 게시물 Return 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "select * from tblboard where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bean.setNum(rs.getInt("num"));
				bean.setName(rs.getString("Name_Attribute"));
				bean.setSubject(rs.getString("Subject_Attribute"));
				bean.setContent(rs.getString("content"));
				
				bean.setPos(rs.getInt("pos"));
				bean.setRef(rs.getInt("ref"));
				bean.setDepth(rs.getInt("depth"));
				bean.setRegdate(rs.getString("regdate"));
				
				bean.setPass(rs.getString("pass"));
				bean.setCount(rs.getInt("count"));
				bean.setFilename(rs.getString("filename"));
				bean.setFilesize(rs.getInt("filesize"));
				bean.setIp(rs.getString("ip"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt, rs);
		}
		return bean;
	}
	
	/* ---------------------------------------------- */
	/* ii */ 
	// 조회수 증가
	public void upCount(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		// // 조회 수 증가 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "update tblboard set count=count+1 where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	// 게시물 삭제
	public void deleteBoard(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		
		// // 게시물 삭제 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "select filename from tblboard where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next() && rs.getString(1) != null) {
				if(!rs.getString(1).equals("")) {
					File file = new File(SAVEFOLDER + "/" + rs.getString(1));
					if(file.exists())
						UtilMgr.delete(SAVEFOLDER + "/" + rs.getString(1));
					
				}
			}
			
			sql = "delete from tblboard where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt, rs);
		}
	}
	
	// 게시물 수정
	public void updateBoard(BoardBean bean) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		// // 게시물 수정 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "update tblboard Set Name_Attribute=?, Subject_Attribute=?, content=? where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getSubject());
			pstmt.setString(3, bean.getContent());
			pstmt.setInt(4, bean.getNum());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	// 게시물 답변
	public void replyBoard(BoardBean bean) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		// // 게시물 답변 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "insert tblboard(Name_Attribute, content, Subject_Attribute, ref, pos, depth, regdate, pass, count, ip)";
			sql += "values(?,?,?,?,?,?,now(),?,0,?)";
			
			int depth = bean.getDepth() + 1;
			int pos = bean.getPos() + 1;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getContent());
			pstmt.setString(3,  bean.getSubject());
			pstmt.setInt(4, bean.getRef());
			pstmt.setInt(5, pos);
			pstmt.setInt(6, depth);
			pstmt.setString(7, bean.getPass());
			pstmt.setString(8, bean.getIp());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	// 답변에 위치 값 증가
	public void replyUpBoard(int ref, int pos) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		// // 답변에 위치 값 증가 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "update tblboard set pos=pos+1 where ref=? and pos > ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, pos);
			pstmt.executeUpdate();		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	// File Download
	public void downLoad(HttpServletRequest req, HttpServletResponse res,
			JspWriter out, PageContext pageContext) {
		try {
			/* File.separtor 는 
			 * Window --> \ 
			 * Unix --> / 
			 * 
			 * 표준 Encoding 방식 ksc5601 로 변환 */
			String filename = req.getParameter("filename");
			File file = new File(UtilMgr.con(SAVEFOLDER + File.separator + filename));
			
			byte b[] = new byte[(int) file.length()];
			res.setHeader("Accept-Ranges", "bytes");
			String strClient = req.getHeader("User-Agent");
			
			if(strClient.indexOf("MSIE6.0") != -1) {
				res.setContentType("application/smnet; charset=euc-kr");
				res.setHeader("Content-Disposition", "filename=" + filename + ";");
			}else {
				res.setContentType("application/smnet); charset=euc-kr");
				res.setHeader("Content-Disposition", "attachment;filename=" + filename + ";");
			}
			out.clear();
			out=pageContext.pushBody();
			
			if(file.isFile()) {
				BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream outs = new BufferedOutputStream(res.getOutputStream());
				
				int read = 0;
				while((read = fin.read(b)) != -1) {
					outs.write(b, 0, read);
				}
				outs.close();
				fin.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	// Paging 및 Block Test 를 위한 게시물 저장 Method
	public void post1000() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		// // Paging 및  Block Test 를 위한 게시물 저장 Method 또한 DB(MySQL)에 연결
		try {
			conn = pool.getConnection();
			sql = "insert tblboard"
					+ "(Name_Attribute, Subject_Attribute, ref, pos, depth, regdate, pass, count, ip, filename, filesize)";
			sql+="values('aaa', 'bbb', 'ccc', 0, 0, 0, now(), '1111', 0, '127.0.0.1', null, 0);";
			pstmt = conn.prepareStatement(sql);
			for(int i=0; i<1000; i++) {
				pstmt.executeUpdate();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	// Main
	public static void main(String[] args) {
		new BoardMgr().post1000();
		System.out.println("SUCCESS");
	}
}
