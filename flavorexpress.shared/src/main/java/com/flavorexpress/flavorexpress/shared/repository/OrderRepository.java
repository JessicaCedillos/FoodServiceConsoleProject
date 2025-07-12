package com.flavorexpress.flavorexpress.shared.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.flavorexpress.flavorexpress.shared.entities.Orders;


public class OrderRepository {
	private Connection connection;
	
	public OrderRepository() {
		connection = DBConnection.getInstance().getConnection();
	}
	
	public Orders save(Orders order) {
		String sql = "INSERT INTO orders(order_entry, quantity, price, time_created)" 
				+ "VALUE (?, ?, ?, ?)";
		String[] returnedColumnsStrings = {"id"};
		try (PreparedStatement statement = connection.prepareStatement(sql, returnedColumnsStrings)) {
			statement.setString(1, order.getOrderEntry());
			statement.setInt(2, order.getQuantity());
			statement.setFloat(3, order.getPrice());
			statement.setTimestamp(4, Timestamp.valueOf(order.getTimeCreated()));
			
			int rows = statement.executeUpdate();
			
			if(rows == 0) throw new SQLException("Creating order failed, no rows affected");
			
			try (var generatedKeys = statement.getGeneratedKeys()) {
				if(generatedKeys.next()) {
					int orderId = generatedKeys.getInt(1);
					order.setOrderId(orderId);					
				}
			}
			return order;
		}
		catch(SQLException e) {
			System.err.println("Error has occured while saving" + e.getMessage());
		}
		return null;
	}
	
	public List<Orders> findAll() throws SQLException {
		List<Orders> orders = new ArrayList<Orders>();
		String sql = "SELECT * FROM orders";
		
		try(Statement statement = connection.createStatement(); 
				ResultSet orderSet = statement.executeQuery(sql)) {
			while(orderSet.next()) {
				Orders order = new Orders();
				order.setOrderId(orderSet.getInt(1));
				order.setOrderEntry(orderSet.getString(2));
				order.setQuantity(orderSet.getInt(3));
				order.setPrice(orderSet.getFloat(4));
				order.setTimeCreated(LocalDateTime.now());
				order = null;
			}
			return orders;
		}
	}
	
	public Orders updateOrder(Orders order) throws SQLException {
		String sql = "UPDATE orders SET order_entry=?, quantity=?, price=? WHERE id =?";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, order.getOrderEntry());
			preparedStatement.setInt(2, order.getQuantity());
			preparedStatement.setFloat(3, order.getPrice());
			preparedStatement.setInt(4, order.getOrderId());

			int row = preparedStatement.executeUpdate();
			if(row > 0) {
				System.out.println("Order Updated Successfully");
			}
			else {
				System.err.println("An error occured while updating");
			}
		}
		return order;
		
	}
	
	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM orders WHERE id = ?";
		try(PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
			prepareStatement.setInt(1, id);
			
			int row = prepareStatement.executeUpdate();
			
			if(row > 0) {
				System.out.println("Order Deleted Successfully");
			}
			else {
				System.err.println("An error occured while deleting customer");
			}
		}
	}
	
}
