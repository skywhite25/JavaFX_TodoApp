package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import util.JDBCUtil;

public class InsertTest {
	
	public static void main(String[] args) {
		JDBCUtil db = new JDBCUtil();
		
		Connection con = db.getConnection();
		if(con == null) {
			System.out.println("���� ����");
			return;
		}
		
		PreparedStatement pstmt = null;
		String sql = " INSERT INTO todo ('memo', 'date') VALUES( ?, ?)";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "�׽�Ʈ");
			pstmt.setDate(2, Date.valueOf(LocalDate.now()));
			int cnt = pstmt.executeUpdate();
			if(cnt == 0) {
				System.out.println("������ ���� ����");
				return;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("������ ���� ����");
			return;
		
		} finally {
			if(pstmt != null) try { pstmt.close(); } catch (Exception e) {}
			if(con != null) try { con.close(); } catch (Exception e) {}
		}
	}
}
