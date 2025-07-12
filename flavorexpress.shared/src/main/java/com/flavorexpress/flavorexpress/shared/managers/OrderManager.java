package com.flavorexpress.flavorexpress.shared.managers;
import com.flavorexpress.flavorexpress.shared.entities.Customer;
import com.flavorexpress.flavorexpress.shared.entities.Orders;
import com.flavorexpress.flavorexpress.shared.exceptions.OrderNotFoundException;
import com.flavorexpress.flavorexpress.shared.repository.OrderRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class OrderManager {
	
	private List<Orders> orders;
	private OrderRepository orderRepository;
	private boolean currentUpdateTracker = false;
	private ExecutorService executorService;

	public OrderManager(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		try {
			this.orders = this.orderRepository.findAll();
		} 
		catch (SQLException e) {
			System.err.println("An error occured loading the data");
		}
		this.executorService = Executors.newSingleThreadExecutor();
		this.executorService.submit(autoSaveRunnable());
		
		
	}
	
	public void addOrder(String orderEntry, float price, int quantity) {
		Orders order = new Orders();
		order.setOrderEntry(orderEntry);
		order.setPrice(price);
		order.setQuantity(quantity);
		orders.add(order);
		currentUpdateTracker = true;
		
	}
	
	public void addOrder(String orderEntry, 
			int quantity, float price, LocalDateTime orderInitiated) {	// this one
		
		Orders order = new Orders();
		order.setOrderEntry(orderEntry);
		order.setQuantity(quantity);
		order.setPrice(price);
		order.setTimeCreated(orderInitiated);
		orders.add(order);
		currentUpdateTracker = true;
	}
	
	public List<Orders> getAllOrders() {
		return orders;
	}
	
	public Orders getOrdersById(int id) {
		return orders.stream()
			.filter(ord -> ord.getOrderId() == id)
			.findFirst().orElseThrow(
			() -> new OrderNotFoundException("Order not found!"));
	}
	
	public List<Orders> getOrdersByCustomerName(String name) { 			// Review
		ArrayList<Orders> customerOrders = new ArrayList<Orders>();
		
		for(int i = 0; i < orders.size(); i++) {
			Customer customer = orders.get(i).getCustomer();
			if(customer != null && customer.getName().equals(name)) {
				customerOrders.add(orders.get(i));
			}
		}
		return customerOrders;
	}
	
	public List<Orders> getSortedOrdersByTime() {
		return orders.stream()
				.sorted(Comparator.comparing(Orders::getTimeCreated)
				.reversed()).collect(Collectors.toList());
	}
	
	public boolean updateOrderInfo(Orders order) {
		Orders originalOrder = orders.stream()
			.filter(order1 -> order1.getOrderId() == order.getOrderId())
			.findAny().orElseThrow(
			() -> new OrderNotFoundException("Order was not found"));
		
		if(originalOrder.equals(order)) {
			return false;
		}
		
		orders.replaceAll(order1 -> 
		order1.getOrderId() == order.getOrderId() ? order : order1);
		currentUpdateTracker = true;
		return true;
	}
	
	public boolean cancelOrder(int id) {
		try {
			orderRepository.delete(id);
			return orders.removeIf(order1 -> order1.getOrderId() == id);
		}
		catch(Exception e) {
			System.out.println("An error occured during cancellation");
			return false;
		}
	}
	
	public Runnable autoSaveRunnable() {
		Runnable runnable = () -> {
			while(true) {
				if(currentUpdateTracker) {
					orders.forEach(order -> {
						if(order.getOrderId() <= 0) {
							orderRepository.save(order);
						}
						else if (!order.getOrderEntry().isBlank()) {
							try {
								orderRepository.updateOrder(order);
							}
							catch(SQLException e) {
								System.err.println("Error occured when updating order" +
										order.getOrderId() + "Error Message:" + e.getMessage());
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
		System.out.println("Auto save thread shutting down");
	}
	

}
