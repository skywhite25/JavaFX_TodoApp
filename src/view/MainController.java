package view;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import domain.Todo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import util.AppUtil;
import util.JDBCUtil;

public class MainController {
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextField txtName;
	
	@FXML
	private ListView<Todo> list;
	
	private ObservableList<Todo> items;
	
	private JDBCUtil db;
	
	@FXML
	private void initialize() {
		items = FXCollections.observableArrayList();
		list.setItems(items);
		db = new JDBCUtil();
		
		Connection con = db.getConnection();
		if(con == null) {
			AppUtil.alert("DB ���� ����", null);
			return;
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		String sql = " SELECT * FROM todo";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String memo = rs.getString("memo");
				LocalDate date = rs.getDate("date").toLocalDate();
				
				Todo todo = new Todo(0, memo, date);
				items.add(todo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppUtil.alert("������ ���� ����", null);
			return;
		}finally {
			if(rs != null) try { rs.close(); } catch ( Exception e ){}
			if(stmt != null) try { stmt.close(); } catch ( Exception e ){}
			if(con != null) try { con.close(); } catch ( Exception e ){}
		}
		
	}
	
	public void addTodo() {
		String name = txtName.getText();
		
		if(name.isEmpty()) {
			AppUtil.alert("������ �̸��� �Է��ϼž� �մϴ�.", null);
			return;
		}
		
		LocalDate date = datePicker.getValue();
		if(date == null) {
			AppUtil.alert("��¥�� �Է��ϼ���", null);
			return;
		}
		Connection con = db.getConnection();
		if(con == null) {
			AppUtil.alert("DB���ῡ �����߽��ϴ�.", null);
			return;
		}
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO todo (`memo`, `date`) VALUES( ?, ?)";
		
		int insertId = 0;
		
		try {
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,  name);
			pstmt.setDate(2, Date.valueOf(date));
			int cnt = pstmt.executeUpdate();
			if(cnt == 0) {
				AppUtil.alert("������ ���� ����", null);
				return;
			}
			ResultSet key = pstmt.getGeneratedKeys();
			if(key.next()) {
				insertId = key.getInt(1); 
			}
		}catch (Exception e) {
			e.printStackTrace();
			AppUtil.alert("������ ���� ����", null);
			return;
		}finally {
			if(pstmt != null) try { pstmt.close(); } catch (Exception e) {}
			if(con != null) try { con.close(); } catch (Exception e) {}
		}
		
		Todo todo = new Todo(insertId, name, date);
		items.add(todo);
	}
	
	public void delTodo() {
		int idx = list.getSelectionModel().getSelectedIndex();
		if(idx >= 0) {
			Todo todo = items.get(idx);
			Connection con = db.getConnection();
			if(con == null) {
				AppUtil.alert("DB���ῡ �����߽��ϴ�.", null);
				return;
			}
			PreparedStatement pstmt = null;
			try {
				pstmt = con.prepareStatement("DELETE FROM todo WHERE id = ?");
				pstmt.setInt(1, todo.getId());
				int cnt = pstmt.executeUpdate();
				
				if(cnt == 0) {
					AppUtil.alert("������ ���� ����", null);
					return;
				}
			}catch (Exception e) {
				AppUtil.alert("������ ���� ����", null);
				return;
			} finally {
				if(pstmt != null) try { pstmt.close(); } catch (Exception e) {}
				if(con != null) try { con.close(); } catch (Exception e) {}
			}
			items.remove(idx);
		}else {
			AppUtil.alert("������ �������� �����ϼ���", "����");
		}
	}
}