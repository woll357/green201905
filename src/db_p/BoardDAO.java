package db_p;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection con;
	PreparedStatement ptmt;
	ResultSet rs;
	String sql;
	
	public BoardDAO() {
		// TODO Auto-generated constructor stub
		try {
			DataSource ds = (DataSource)new InitialContext().lookup("java:comp/env/ggggg");
			con = ds.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	//총 게시글 수 구하기
	public int total() {
		
		int res = 0;
		
		
		try {
			sql = "select count(*) from board";        //총 게시물이 몇개냐
			
			ptmt = con.prepareStatement(sql);		
			
			rs = ptmt.executeQuery();
			
			rs.next() ;
				
			res = rs.getInt(1);
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	
	
	public Object list(int page, int limit) {
		ArrayList<BoardDTO> res = new ArrayList<BoardDTO>();
		
		
		
		try {
			sql = "select * from board order by gid desc , seq limit ? , ?";          //limit 를 이용해서 일부 글만 추출해오는것은 아무 문제가 없음.
			//limit 0, 3  - > 3개만 가져옴
			ptmt = con.prepareStatement(sql);
			
			ptmt.setInt(1, page);
			ptmt.setInt(2, limit);
			
			rs = ptmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setBid(rs.getInt("bid"));
				dto.setGid(rs.getInt("gid"));
				dto.setSeq(rs.getInt("seq"));
				dto.setLevel(rs.getInt("level"));
				dto.setNo(rs.getInt("no"));
				dto.setTitle(rs.getString("title"));
				dto.setPname(rs.getString("pname"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				
				res.add(dto);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			close();
		}
		
		
		return res;
	}
	
	
	public void addCount(BoardDTO dto) {
		
		sql = "update board set no = no + 1 where  bid = ?";
		
		try {
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, dto.bid);
			ptmt.executeUpdate();
			
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	
	
	public BoardDTO detail(BoardDTO dto) {
		BoardDTO res = null;
		
		sql = "select * from board where  bid = ?";
		
		try {
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, dto.bid);
			rs = ptmt.executeQuery();
			
			if(rs.next()) {
				res = new BoardDTO();
				res.setBid(rs.getInt("bid"));
				res.setGid(rs.getInt("gid"));
				res.setSeq(rs.getInt("seq"));
				res.setLevel(rs.getInt("level"));
				res.setNo(rs.getInt("no"));
				res.setTitle(rs.getString("title"));
				res.setPname(rs.getString("pname"));
				res.setRegdate(rs.getTimestamp("regdate"));
				res.setUpfile(rs.getString("upfile"));
				res.setContent(rs.getString("content"));
			}
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		/*
		 * finally {
		 * 
		 * close(); }
		 */          //디테일을 쓰는 애들은 직접 다 close 해줘야함.

		return res;
	}
	
	
	
	public BoardDTO fileDelete(BoardDTO dto) {
		BoardDTO res = null;
		
		sql = "select * from board where  bid = ? and pw = ?";
		
		try {
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, dto.bid);
			ptmt.setString(2, dto.pw);
			rs = ptmt.executeQuery();
			
			if(rs.next()) {
				res = new BoardDTO();
				res.setBid(rs.getInt("bid"));
				res.setGid(rs.getInt("gid"));
				res.setSeq(rs.getInt("seq"));
				res.setLevel(rs.getInt("level"));
				res.setNo(rs.getInt("no"));
				res.setTitle(rs.getString("title"));
				res.setPname(rs.getString("pname"));
				res.setRegdate(rs.getTimestamp("regdate"));
				res.setUpfile(rs.getString("upfile"));
				res.setContent(rs.getString("content"));
				
				
				sql = "update board set upfile = null where bid = ?";  //이 부분은 파일이 삭제 되었을때 파일 삭제된것이 DB에 업데이트해야하기때문에 .
				
				ptmt = con.prepareStatement(sql);
				
				ptmt.setInt(1, dto.getBid());
				
				ptmt.executeUpdate(); 
			}
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			close();
		}

		return res;
	}
	
	
	
	public void write(BoardDTO dto) {
		
		
		try {
			
			sql = "insert into board "
			+ "(seq, level, no, title, pname, pw, content, upfile, regdate ) values "
			+ "(0    ,  0  ,-1,  ? ,  ? ,   ? ,  ? , ? ,  sysdate() )";
			
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, dto.getTitle());
			ptmt.setString(2, dto.getPname());
			ptmt.setString(3, dto.getPw());
			ptmt.setString(4, dto.getContent());
			ptmt.setString(5, dto.getUpfile());
			ptmt.executeUpdate();
			
			sql = "select max(bid) from board";
			
			ptmt = con.prepareStatement(sql);
			
			rs = ptmt.executeQuery();
			
			rs.next();
			
			dto.setBid(rs.getInt(1));
			
			
			sql = "update board set gid = bid where  bid = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, dto.bid);
			ptmt.executeUpdate();

					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			close();
		}

	}
	
	
	
	
	public boolean delete(BoardDTO dto) {
		
		boolean res = false;
		
		try {
			   //만약 파일 삭제를 원하면 여기서 해야함 . 그리고 파일 패스를 해야 파일이 삭제됨
	    	   //DAO에서 직접 파일을 삭제하면 안됨.
	    	   //왜냐하면 리퀘스트를 가져와야하는데 말이되질 않음 .
	    	   //삭제하려면 따로 기능을 만들어주어야함.
	    	   //서비스급으로!
			sql = "delete from board where bid = ? and pw = ? ";
			
			ptmt = con.prepareStatement(sql);
			
			ptmt.setInt(1, dto.getBid());
			ptmt.setString(2, dto.getPw());
			
			res = ptmt.executeUpdate() > 0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			close();
		}
		
		
		return res;

	}
	
	
	public boolean modify(BoardDTO dto) {
		
		boolean res = false;
		
		try {
			
			sql = "update board set title = ?, pname = ?, content = ?, no = no-1 , upfile = ?"
					+ " where bid = ? and pw = ? ";
			
			ptmt = con.prepareStatement(sql);
			
			ptmt.setString(1, dto.getTitle());
			ptmt.setString(2, dto.getPname());
			ptmt.setString(3, dto.getContent());
			ptmt.setString(4, dto.getUpfile());
			ptmt.setInt(5, dto.getBid());
			ptmt.setString(6, dto.getPw());
			
			res = ptmt.executeUpdate() > 0; //익스큐트 없데이트가 1건 이상이여야 하기때문에 0이상이 되야 삭제됨 초기값은  false
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			close();
		}
		
		
		return res;

	}
	
	
	
	
	public void reply(BoardDTO dto) {
		
		
		try {
			
			BoardDTO ori = detail(dto);  //원글
			
			sql = "update board set seq = seq +1 where gid = ? and seq > ?";
			
			ptmt = con.prepareStatement(sql);
			
			ptmt.setInt(1, ori.gid);
			ptmt.setInt(2, ori.seq);
			
			ptmt.executeUpdate();
			
			sql = "insert into board "
			+ "(gid, seq, level, no, title, pname, pw, content, regdate ) values "
			+ "( ?,   ?,    ?  ,-1,   ? ,    ? ,    ? ,  ? ,   sysdate() )";
			
			ptmt = con.prepareStatement(sql);
			
			ptmt.setInt(1, ori.gid);
			ptmt.setInt(2, ori.seq+1);
			ptmt.setInt(3, ori.level+1);
			ptmt.setString(4, dto.getTitle());
			ptmt.setString(5, dto.getPname());
			ptmt.setString(6, dto.getPw());
			ptmt.setString(7, dto.getContent());
			
			ptmt.executeUpdate();
			
			
			sql = "select max(bid) from board";
			
			ptmt = con.prepareStatement(sql);
			
			rs = ptmt.executeQuery();
			
			rs.next();
			
			dto.setBid(rs.getInt(1));
			
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			close();
		}

	}
	
	
	
	
	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {	}
		if(ptmt!=null) try {ptmt.close();} catch (SQLException e) {	}
		if(con!=null) try {con.close();} catch (SQLException e) {	}
	}

}
