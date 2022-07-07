package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
	private Connection connection;
	private static ConnectionUtils connectionUtils;

	public Connection getConnection() {
		return connection;
	}

	private ConnectionUtils() throws SQLException {
		String url = System.getenv("DB_URL");
		String dbName = System.getenv("DB_PROJECT0");
		String dbUser = System.getenv("DB_USER");
		String dbPass = System.getenv("DB_PASS");
		try {
			Class.forName("org.postgresql.Driver");
			this.connection=DriverManager.getConnection(url+dbName, dbUser, dbPass);
		} catch (ClassNotFoundException  e) {

			e.printStackTrace();
		}
	
	}
	
	public static ConnectionUtils getInstance() throws SQLException {
		if(connectionUtils == null) {
			connectionUtils=new ConnectionUtils();
		}else if (connectionUtils.getConnection().isClosed()) {
			connectionUtils = new ConnectionUtils();
		}
		
		return connectionUtils;
	}
	
	

}        