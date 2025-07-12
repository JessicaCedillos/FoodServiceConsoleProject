package com.flavorexpress.flavorexpress.shared.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.flavorexpress.flavorexpress.shared.entities.Customer;

public class CustomerRepository {

	private Connection connection;
	
	public CustomerRepository() {
		connection = DBConnection.getInstance().getConnection();
	}
	
	public Customer save(Customer customer) {
		String sql = "INSERT INTO customers(customer_name, email, phone_number)" 
				+ "VALUE (?, ?, ?)";
		String[] returnedColumnsStrings = {"id"};
		try (PreparedStatement statement = connection.prepareStatement(sql, returnedColumnsStrings)) {
			statement.setString(1, customer.getName());
			statement.setString(2, customer.getEmail());
			statement.setString(3, customer.getPhoneNumber());
			
			int rows = statement.executeUpdate();
			
			if(rows == 0) throw new SQLException("Creating customer failed, no rows affected");
			
			try (var generatedKeys = statement.getGeneratedKeys()) {
				if(generatedKeys.next()) {
					int customerId = generatedKeys.getInt(1);
					customer.setCustomerId(customerId);					
				}
			}
			return customer;
		}
		catch(SQLException e) {
			System.err.println("Error has occured while saving" + e.getMessage());
		}
		return null;
	}
	
	public List<Customer> findAll() throws SQLException {
		List<Customer> customers = new ArrayList<Customer>();
		String sql = "SELECT * FROM customers";
		
		try(Statement statement = connection.createStatement(); 
				ResultSet customerSet = statement.executeQuery(sql)) {
			while(customerSet.next()) {
				Customer customer = new Customer();
				customer.setCustomerId(customerSet.getInt(1));
				customer.setName(customerSet.getString(2));
				customer.setEmail(customerSet.getString(3));
				customer.setPhoneNumber(customerSet.getString(4));
				customers.add(customer);
				customer = null;
			}
			return customers;
		}
	}
	
	public Customer updateCustomer(Customer customer) throws SQLException {
		String sql = "UPDATE customers SET customer_name=?, email=?, phone_number=? WHERE id =?";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, customer.getName());
			preparedStatement.setString(2, customer.getEmail());
			preparedStatement.setString(3, customer.getPhoneNumber());
			preparedStatement.setInt(4, customer.getCustomerId());

			int row = preparedStatement.executeUpdate();
			if(row > 0) {
				System.out.println("Customer Updated Successfully");
			}
			else {
				System.err.println("An error occured while updating");
			}
		}
		return customer;
		
	}
	
	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM customers WHERE id = ?";
		try(PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
			prepareStatement.setInt(1, id);
			
			int row = prepareStatement.executeUpdate();
			
			if(row > 0) {
				System.out.println("Customer Deleted Successfully");
			}
			else {
				System.err.println("An error occured while deleting customer");
			}
		}
	}
	
	
}
