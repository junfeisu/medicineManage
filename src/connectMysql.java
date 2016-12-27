package src;
import java.sql.*;

public class connectMysql {
	private static Statement stmt;
	private static Connection conn;

	public connectMysql() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("成功加载到mysql驱动");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3306/medcine?allowMultiQueries=true";

		try {
			conn = DriverManager.getConnection(url, "root", "sjf978977");
			stmt = conn.createStatement();
			System.out.println("成功连接到数据库");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Statement getStat() {
		return stmt;
	}

	public static Connection getConn() {
		return conn;
	}
}
