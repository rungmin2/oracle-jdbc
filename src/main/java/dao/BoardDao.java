package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import vo.Board;

public class BoardDao {
	// 검색 추가
	public ArrayList<Board> selectBoardListByPage(Connection conn, int beginRow, int endRow) throws Exception {
		ArrayList<Board> list = new ArrayList<Board>();
		String sql = "SELECT board_no boardNo, board_title boardTitle, createdate"
				+ " FROM (SELECT rownum rnum, board_no, board_title, createdate"
				+ "			FROM (SELECT board_no, board_title, createdate"
				+ "					FROM board ORDER BY board_no DESC))"
				+ " WHERE rnum BETWEEN ? AND ?"; // WHERE rnum >=? AND rnum <=?;
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, beginRow);
		stmt.setInt(2, endRow);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Board b = new Board();
			b.setBoardNo(rs.getInt("boardNo"));
			b.setBoardTitle(rs.getString("boardTitle"));
			b.setCreatedate(rs.getString("createdate"));
			list.add(b);
		}
		return list;
	}
	// 게시글 추가
	public int insertBoard(Connection conn, Board board) throws Exception {
		int row = 0;
		/*
	 	insert into board (
			board_no, board_title, board_content, member_id, updatedate, createdate
		) values (
			board_seq.nextval, ? , ?, ?, sysdate, sysdate
		)
		*/
		String sql = "INSERT INTO board(board_no, board_title, board_content, member_id, updatedate, createdate) VALUES(board_seq.nextval, ?, ?, ?, sysdate, sysdate)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, board.getBoardTitle());
		stmt.setString(2, board.getBoardContent());
		stmt.setString(3, board.getMemberId());
		row = stmt.executeUpdate();
		return row;
	}
	// BoardOne
	public Board boardOne(Connection conn, int boardNo) throws Exception {
		Board b = null;
		String sql = "SELECT member_id memberId, board_title boardTitle, board_content boardContent FROM board WHERE board_no =?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, boardNo);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			b = new Board();
			b.setMemberId(rs.getString("memberId"));
			b.setBoardTitle(rs.getString("boardTitle"));
			b.setBoardContent(rs.getString("boardContent"));
		}
		return b;
	}
	// 게시글 수정
	public ArrayList<Board> updateBoard(Connection conn, int boardNo) throws Exception {
		ArrayList<Board> list = new ArrayList<Board>();
		Board b = null;
		String sql = "SELECT member_id memberId, board_title boardTitle, board_content boardContent FROM board WHERE board_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, boardNo);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			b =  new Board();
			b.setMemberId(rs.getString("memberId"));
			b.setBoardTitle(rs.getString("boardTitle"));
			b.setBoardContent(rs.getString("boardContent"));
			list.add(b);
		}
		return list;
	}
}