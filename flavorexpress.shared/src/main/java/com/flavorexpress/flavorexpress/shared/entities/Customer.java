package com.flavorexpress.flavorexpress.shared.entities;

import java.util.Objects;
import org.apache.commons.validator.routines.EmailValidator;


import com.flavorexpress.flavorexpress.shared.exceptions.InvalidInputException;

public class Customer implements Comparable<Customer>{
	
	private int customerId;
	private String name;
	private String address;
	private String email;
	private String phoneNumber;
	EmailValidator validateEmail = EmailValidator.getInstance();
	
	public Customer() {
		
	}
	
	public Customer(int customerId) {
		this.customerId = customerId;
	}
	
//	public Customer(String name, String address, String email, String phoneNumber) {
//		
//		if(name.isBlank() || name.isEmpty() || name == null) {
//			throw new InvalidInputException("Name has to be filled out");
//		}
//		
//		if(address.isBlank() || address.isEmpty() || address == null) {
//			throw new InvalidInputException("Address had to be filled out");
//		}
//		
//		if(email.isBlank() || email.isEmpty() || email == null) {
//			throw new InvalidInputException("Email has to be filled out");
//		}
//		
//		if(phoneNumber.isBlank() || phoneNumber.isEmpty() || phoneNumber == null) {
//			throw new InvalidInputException("Phone number has to be filled out");
//		}
//		
//		this.name = name;
//		this.address = address;
//		this.email = email;
//		this.phoneNumber = phoneNumber;
//	}
	
	public Customer(String name, String email, String phoneNumber) {
		
		if(name.isBlank() || name.isEmpty() || name == null) {
			throw new InvalidInputException("Name has to be filled out");
		}
		
		if(email.isBlank() || email.isEmpty() || email == null) {
			throw new InvalidInputException("Email has to be filled out");
		}
		
		if(phoneNumber.isBlank() || phoneNumber.isEmpty() || phoneNumber == null) {
			throw new InvalidInputException("phone number has to be filled out");
		}
		
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
//	public void setAddress(String address) {
//		if(!address.matches("^(\\d{1,}) [a-zA-Z0-9\\s]+(,\\s)?[a-zA-Z]"
//			+ "+(,\\s)?[A-Z]{2}\\s[0-9]{5,6}$")) {
//			
//			throw new InvalidInputException("Invalid Address Entry!");
//		}
//		this.address = address;
//	}
//	
//	public String getAddress() {
//		return address;
//	}
	
	public void setEmail(String email) {
		if(!validateEmail.isValid(email)) {
			throw new InvalidInputException("Invalid Email Entry!");
		}
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		if(!phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
			throw new InvalidInputException("Wrong Number Format Used!");
		}
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	@Override
	public String toString() {
		return "\nCustomer ID: " + getCustomerId() 
				+ "\nName: " + getName()  
				+ "\nEmail: " + getEmail()
				+ "\nPhone Number: " + getPhoneNumber() + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, customerId, email, name, phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(address, other.address) && customerId == other.customerId
				&& Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}

	@Override
	public int compareTo(Customer o) {
		return this.name.compareToIgnoreCase(o.name);
	}
	

}
