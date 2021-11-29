package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
	
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("드라이버 없음");
			return null;
		}
		
		String url = "jdbc:mysql://localhost:3306/todoapp";
		String id = "root";
		String pw = "a135719";
		
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("연결 실패");
		}
		return con;
		
	}
}
