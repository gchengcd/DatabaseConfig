package com.glf.uiforglf;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/*
 * 数据库连接，关闭操作
 */
public final class JdbcUtils {
	/*private static String url = "jdbc:mysql://localhost/buptlab?useUnicode=true&characterEncoding=utf-8";//?useUnicode=true&characterEncoding=gbk
	private static String user = "root";
	private static String password = "07363001";*/
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	private JdbcUtils() {
	}
	public static Connection getConnection() throws SQLException {
		Properties mysqlProperties = new Properties();
		InputStream inputStream;
		try {
			inputStream = new BufferedInputStream(new FileInputStream("dbConfig.properties"));
			mysqlProperties.load(inputStream);
			String drivers = mysqlProperties.getProperty("jdbc.drivers");
			String url = mysqlProperties.getProperty("jdbc.url");
			String username = mysqlProperties.getProperty("jdbc.username");
			String password = mysqlProperties.getProperty("jdbc.password");
			//Class.forName(drivers);
			return DriverManager.getConnection(url, username, password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*public static void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
	}*/
}
