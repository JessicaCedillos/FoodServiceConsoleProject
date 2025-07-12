package com.flavorexpress.flavorexpress.shared.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import com.flavorexpress.flavorexpress.shared.exceptions.InvalidInputException;

public class Orders {
	
	private int orderId;
	private String deliveryOption;
	private String orderEntry;
	private int quantity;
	private float price;
	private LocalDateTime orderInitiated;
	public static final float DELIVERY_FEE = 3.99f;
	private Customer customer;
	
	public Orders() {
		this.orderInitiated = LocalDateTime.now();
	}
	
	public Orders(int orderId, LocalDateTime orderInitiated) {
		this.orderId = orderId;
		this.orderInitiated = orderInitiated;
	}
	
	public Orders(String orderEntry, int quantity, float price) {
		this.orderEntry = orderEntry;
		this.quantity = quantity;
		this.price = price;
		orderInitiated = LocalDateTime.now();
	}
	
	public Orders(int orderId, Customer customer, String orderEntry) {
		this.orderId = orderId;
		this.customer = customer;
		this.orderEntry = orderEntry;
	}
	
	public Orders(String deliveryOption, String orderEntry, 
			int quantity, float price, LocalDateTime orderInitiated) {
		
		this.deliveryOption = deliveryOption;
		this.orderEntry = orderEntry;
		this.quantity = quantity;
		this.price = price;
		this.orderInitiated = orderInitiated;
	}
	
	public Orders(String deliveryOption, String orderEntry,
			int quantity, LocalDateTime orderInitiated) {
		
		this.deliveryOption = deliveryOption;
		this.orderEntry = orderEntry;
		this.quantity = quantity;
		this.orderInitiated = orderInitiated;
	}
	
	public void setOrderId(int id) {
		this.orderId = id;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption = deliveryOption;
	}
	
	public String getDeliveryOption() {
		return deliveryOption;
	}
	
	public void setOrderEntry(String order) {
		this.orderEntry = order;
	}
	
	public String getOrderEntry() {
		return orderEntry;
	}
	
	public void setQuantity(int quantity) {
		if(quantity < 0) {
			throw new InvalidInputException("Quantity cannot be a negative value");
		}
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setPrice(float price) {
		if(price < 0) {
			throw new InvalidInputException("Price cannot be a negative value");
		}
		this.price = price;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setTimeCreated(LocalDateTime orderInitiated) {
		this.orderInitiated = orderInitiated;
	}
	
	public LocalDateTime getTimeCreated() {
		return orderInitiated;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	@Override
	public String toString() {
		return "\nOrder ID: " + getOrderId()
				+ "\nOrder Entry: " + getOrderEntry()
				+ "\nQuantity: " + getQuantity()
				+ "\nPrice: " + getPrice()
				+ "\nOrder Created: " + getTimeCreated(); 
	}

	@Override
	public int hashCode() {
		return Objects.hash(deliveryOption, orderEntry, 
				orderId, orderInitiated, price, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orders other = (Orders) obj;
		return Objects.equals(deliveryOption, other.deliveryOption) 
				&& Objects.equals(orderEntry, other.orderEntry)
				&& orderId == other.orderId 
				&& Objects.equals(orderInitiated, other.orderInitiated)
				&& Float.floatToIntBits(price) == Float.floatToIntBits(other.price) 
				&& quantity == other.quantity;
	}
	
}
