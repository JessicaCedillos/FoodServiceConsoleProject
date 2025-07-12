package com.flavorexpress.flavorexpress.shared.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flavorexpress.flavorexpress.shared.entities.Customer;
import com.flavorexpress.flavorexpress.shared.exceptions.CustomerNotFoundException;
import com.flavorexpress.flavorexpress.shared.repository.CustomerRepository;

public class CustomerManager {
	
	private List<Customer> customers;
	private CustomerRepository customerRepository;
	private boolean currentUpdateTracker = false;
	private ExecutorService executorService;

	public CustomerManager(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		try {
			this.customers = this.customerRepository.findAll();
		} 
		catch (SQLException e) {
			System.err.println("An error occured loading the data");
		}
		this.executorService = Executors.newSingleThreadExecutor();
		this.executorService.submit(autoSaveRunnable());
		
	}

	
	public void addCustomer(String name, 
			String email, String phoneNumber) {
		
		Customer customer = new Customer();
		customer.setName(name);
		customer.setEmail(email);
		customer.setPhoneNumber(phoneNumber);
		customer.toString();
		customers.add(customer);
		currentUpdateTracker = true;
	}
	
	public List<Customer> getAllCustomers() {
		return customers;
	}
	
	public Customer getCustomerById(int customerId) {
		return customers.stream().filter(cust -> 
		cust.getCustomerId() == customerId).findFirst().orElseThrow(() -> 
		new CustomerNotFoundException("Customer does not exist"));
	}
	
	public Customer getCustomerByName(String name) {
		for(int i = 0; i < customers.size(); i++) {
			if(customers.get(i).getName().equalsIgnoreCase(name)) {
				return customers.get(i);
			}
		}
		return null;
	}
	
	public List<Customer> getSortedCustomers() { // sorts by name
		return customers.stream()
				.sorted((cust1, cust2) -> cust1.getName()
				.compareTo(cust2.getName())).toList();
		
	}
	
	public boolean updateCustomerInfo(Customer customer) {
		Customer originalInfo = customers.stream()
				.filter(cust1 -> cust1.getCustomerId() == customer.getCustomerId())
				.findAny().orElseThrow(() -> 
				new CustomerNotFoundException("Customer was not found"));
		
		if(originalInfo.equals(customer)) {
			return false;
		}
		
		customers.replaceAll(cust -> 
		cust.getCustomerId() == customer.getCustomerId() ? customer : cust);
		currentUpdateTracker = true;
		return true;
	}
	
	public boolean deleteCustomer(int id) {
		try {
			customerRepository.delete(id);
			return customers.removeIf(cust -> cust.getCustomerId() == id);
		}
		catch(Exception e) {
			System.out.println("An error occured during deletion");
			return false;
		}
	}
	
	public Runnable autoSaveRunnable() {
		Runnable runnable = () -> {
			while(true) {
				if(currentUpdateTracker) {
					customers.forEach(customer -> {
						if(customer.getCustomerId() <= 0) {
							customerRepository.save(customer);
						}
						else if (!customer.getName().isBlank()) {
							try {
								customerRepository.updateCustomer(customer);
							}
							catch(SQLException e) {
								System.err.println("Error occured when updating customer" +
										customer.getCustomerId() + "Error Message:" + e.getMessage());
							}
						}
					});
					currentUpdateTracker = false;
				}
				try {
					Thread.sleep(5000);
				}
				catch(InterruptedException e){
					System.err.println("Thread was not able to execute:" + e.getMessage());
				}
			}
		};
		return runnable;
	}
	
	public void shutDownAutoSave() {
		executorService.shutdown();
	}
	
}
