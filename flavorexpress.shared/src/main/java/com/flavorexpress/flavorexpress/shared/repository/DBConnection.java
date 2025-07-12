package com.flavorexpress.flavorexpress.shared.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private static DBConnection instanceConnection;
	private Connection connection;
	
	private DBConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/your-database-name",
					"your-username",
					"your-password"
					);
		}
		catch (Exception e) {
			System.out.println("Connection Failed!" + e.getMessage());
		}
	}
	
	public static DBConnection getInstance() {
		if(instanceConnection == null) {
			instanceConnection = new DBConnection();
		}
		return instanceConnection;
	}
	
	public Connection getConnection() {
		return connection;
	}
}