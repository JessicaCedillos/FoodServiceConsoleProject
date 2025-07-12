package com.flavorexpress.flavorexpress.shared.ui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.validator.routines.EmailValidator;
import com.flavorexpress.flavorexpress.shared.entities.Customer;
import com.flavorexpress.flavorexpress.shared.exceptions.InvalidInputException;
import com.flavorexpress.flavorexpress.shared.managers.CustomerManager;
import com.flavorexpress.flavorexpress.shared.managers.OrderManager;
import com.flavorexpress.flavorexpress.shared.repository.DBConnection;

public class Console {
	
	private CustomerManager customerManager;
	private OrderManager orderManager;
	private Scanner scanner;
	EmailValidator validateEmail = EmailValidator.getInstance();
	
	public Console(CustomerManager customerManager, 
			OrderManager orderManager, Scanner scanner) {
		
		this.customerManager = customerManager;
		this.orderManager = orderManager;
		this.scanner = scanner;
	}
	
	public void start() {
		
		while(true) {
			welcomeMenu();
			int selection = scanner.nextInt();
			
			switch(selection) {
			
			case 1:
				createOrder();
				break;
			
			case 2:
				getAllOrders();
				break;
				
			case 3:
				sortOrders();
				break;
				
			case 4:
				deleteOrder();
				break;
			
			case 5:
				addCustomer();
				break;
				
			case 6:
				getAllCustomers();
				break;
				
			case 7:
				getCustomerByName();
				break;
				
			case 8:
				updateCustomer();
				break;
				
			case 9:
				sortCustomers();
				break;
				
			case 10:
				deleteById();
				break;

			case 99:
				System.out.println("Have a great day! :)");
				customerManager.shutDownAutoSave();
				
				try {
					DBConnection.getInstance().getConnection().close();
				}
				catch(SQLException e) {
					System.err.println("An error ocurred while closing connection");
				}
				System.exit(0);
				
			default:
				System.out.println("Invalid Choice!");

			}
		}
	}
	
	private void addCustomer() {
		
		try {
			scanner.nextLine();
			System.out.println("Enter your name: ");
			String name = scanner.nextLine();
			
			System.out.println("Enter your email: ");
			String email = scanner.nextLine();
			if(!validateEmail.isValid(email)) {
				throw new InvalidInputException(" Not a valid email entry!");			
			}
			
			System.out.println("Enter your phone number (format: xxx-xxx-xxxx): ");
			String phone = scanner.nextLine();
			
			customerManager.addCustomer(name, email, phone);
			System.out.println("\nCustomer added successfully!");
	
		}
		catch(InvalidInputException e) {
			System.err.println("Invalid entry!" + e.getMessage());
		}	
	}
	
	
	private void createOrder() {
	    LinkedHashMap<String, Float> food = new LinkedHashMap<>();
	    food.put("Hamburger", 5.99f);
	    food.put("Pizza", 3.99f);
	    food.put("HotDog", 2.99f);
	    food.put("Steak & Cheese", 5.99f);
	    food.put("Chicken Sandwich", 6.99f);
	    food.put("Caesar Salad", 4.99f);

	    LinkedHashMap<String, Float> sides = new LinkedHashMap<>();
	    sides.put("French Fries", 1.99f);
	    sides.put("Onion Rings", 2.99f);
	    sides.put("Mashed Potatoes", 3.99f);
	    sides.put("Chicken Nuggets", 1.99f);
	    sides.put("Mac & Cheese", 3.99f);
	    sides.put("Biscuit", 0.99f);

	    LinkedHashMap<String, Float> drinks = new LinkedHashMap<>();
	    drinks.put("Soda", 1.99f);
	    drinks.put("Orange Juice", 1.99f);
	    drinks.put("Iced Tea", 1.99f);
	    drinks.put("Water", 0.99f);
	    drinks.put("Milk", 1.99f);
	    drinks.put("Coffee", 1.99f);

	    try {
	        scanner.nextLine();
	        foodMenu();

	        System.out.println("\nEnter your items (separate by commas): ");
	        String input = scanner.nextLine();
	        String[] choices = input.split(",");
	        
	        float totalPrice = 0.0f;
	        StringBuilder orderItems = new StringBuilder();
	        
	        for (String choiceStr : choices) {
	            int choice = Integer.parseInt(choiceStr.trim());
	            
	            String selectedItem = null;
	            float itemPrice = 0.0f;
	            int currentNum = 1;
	            
	            for (String item : food.keySet()) {
	                if (currentNum == choice) {
	                    selectedItem = item;
	                    itemPrice = food.get(item);
	                    break;
	                }
	                currentNum++;
	            }
	            
	            if (selectedItem == null) {
	                for (String item : sides.keySet()) {
	                    if (currentNum == choice) {
	                        selectedItem = item;
	                        itemPrice = sides.get(item);
	                        break;
	                    }
	                    currentNum++;
	                }
	            }
	            
	            if (selectedItem == null) {
	                for (String item : drinks.keySet()) {
	                    if (currentNum == choice) {
	                        selectedItem = item;
	                        itemPrice = drinks.get(item);
	                        break;
	                    }
	                    currentNum++;
	                }
	            }
	            
	            if (selectedItem != null) {
	                totalPrice += itemPrice;
	                if (orderItems.length() > 0) {
	                    orderItems.append(", ");
	                }
	                orderItems.append(selectedItem);
	            }
	        }
	        
	        System.out.println("\nTotal: $" + String.format("%.2f", totalPrice));
	        System.out.print("Confirm Order? (y/n): ");
	        String confirm = scanner.nextLine();

	        if (confirm.toLowerCase().startsWith("y")) {
	            orderManager.addOrder(orderItems.toString(), choices.length, totalPrice, LocalDateTime.now());
	            System.out.println("\nOrder placed successfully!");
	        } else {
	            System.out.println("\nOrder Cancelled");
	        }

	    } catch (Exception e) {
	        System.err.println("Invalid entry! " + e.getMessage());
	    }
	}
	

	private void getAllOrders() {
		if(orderManager.getAllOrders().isEmpty()) {
			System.out.println("There are no orders to display\n");
			return;
		}
		System.out.println("Displaying All Orders");
		orderManager.getAllOrders().forEach(order -> System.out.println(order));
		
	}
	
	private void getAllCustomers() {
		if(customerManager.getAllCustomers().isEmpty()) {
			System.out.println("There are no customers to display\n");
			return;
		}
		System.out.println("Displaying All Customers");
		customerManager.getAllCustomers().forEach(customer 
				-> System.out.println(customer));
	}
	
	private void getCustomerByName() {
		scanner.nextLine();
		System.out.println("Enter the customer's name: ");
		String name = scanner.nextLine();
		
		if(customerManager.getCustomerByName(name) != null) {
			System.out.println("\nRetrieving customer by name:");
			customerManager.getAllCustomers().forEach(Customer::getName);
			System.out.println(customerManager.getCustomerByName(name).toString());
			return;
		}
		else {
			System.out.println("\nThere are no customers with this name!\n");
		}
	}
	
	private void sortCustomers() {	// Revise
		
		System.out.println("Sorted Customers\n");
		Collections.sort(customerManager.getAllCustomers());
		customerManager.getAllCustomers().forEach(System.out::println);
	}
	
	private void sortOrders() {	
		System.out.println("Sorted Orders\n");
		orderManager.getSortedOrdersByTime().forEach(System.out::println);
	}
	
	private void updateCustomer() {	
		displayCustomerIds();
		System.out.println("\nSelect an ID: ");
		
		int choice = scanner.nextInt();
		scanner.nextLine();
		
		try {
			
			Customer existingCustomer = customerManager.getCustomerById(choice);
			System.out.println("Current Customer Information");
			System.out.println(existingCustomer.toString());
			
			System.out.println("Enter new name (Current: ".concat(existingCustomer.getName()).concat("): "));
			String newName = scanner.nextLine();
			if(newName.isBlank()) newName = existingCustomer.getName();
			
			System.out.println("Enter new email (Current: ".concat(existingCustomer.getEmail()).concat("): "));
			String newEmail = scanner.nextLine();
			if(newEmail.isBlank() && validateEmail.isValid(newEmail)) newEmail = existingCustomer.getEmail();
			
			System.out.println("Enter new phone number (Current: ".concat(existingCustomer.getPhoneNumber()).concat("): "));
			String newPhone = scanner.nextLine();
			if(newPhone.isBlank()) newPhone = existingCustomer.getPhoneNumber();
			
			Customer updatedCustomer = new Customer(existingCustomer.getCustomerId());
			updatedCustomer.setName(newName);
			updatedCustomer.setEmail(newEmail);
			updatedCustomer.setPhoneNumber(newPhone);
			
			if(customerManager.updateCustomerInfo(updatedCustomer)) {
				System.out.println("\nCustomer Updated Successfully!");
			}
			else {
				System.out.println("No Changes Made");
			}
		}
		catch(Exception e) {
			System.err.println("Customer was not found or an Invalid Input was entered!");
		}
	}
	
	private void deleteById() {
		displayCustomerIds();
		System.out.println("Select an ID: ");
		
		int choice = scanner.nextInt();
		scanner.nextLine();
		
		if(customerManager.deleteCustomer(choice)) {
			System.out.println("Customer Deleted Successfully");
		}
		else {
			System.err.println("Invalid Option!");
		}
	}
	
	private void deleteOrder() {
		displayOrderIds();
		System.out.println("Select an ID: ");
		
		int choice = scanner.nextInt();
		scanner.nextLine();
		
		if(orderManager.cancelOrder(choice)) {
			System.out.println("Order has been Cancelled");
		}
		else {
			System.err.println("Invalid Option!");
		}
	}
	
	private void displayCustomerIds() {
		System.out.println("Current Customer IDs\n");
		customerManager.getAllCustomers().forEach(cust -> {
		System.out.printf("ID: %d, Name: %s\n", cust.getCustomerId(), cust.getName());
		});
	}
	
	private void displayOrderIds() {
		System.out.println("Current Orders\n");
		orderManager.getAllOrders().forEach(order1 -> {
			System.out.printf("ID: %d, Order: %s\n", 
				order1.getOrderId(), order1.getOrderEntry());
		});
	}
	
	
	private void welcomeMenu() {
	    System.out.println("""
	                 ________                          ______                              
	                / ____/ /___ __   ______  _____   / ____/  ______  ________  __________
	               / /_  / / __ `/ | / / __ \\/ ___/  / __/ | |/_/ __ \\/ ___/ _ \\/ ___/ ___/
	              / __/ / / /_/ /| |/ / /_/ / /     / /____>  </ /_/ / /  /  __(__  |__  ) 
	             /_/   /_/\\__,_/ |___/\\____/_/     /_____/_/|_/ ,___/_/   \\___/____/____/  
	       		                               	             /_/                                                             
	                     
	                               Welcome To The Tastiest Shop In Town!   
	                
	                  ___________________________________________________________
	                 |                              |                            | 
	                 |        Order Options         |      Customer Settings     |
	                 |______________________________|____________________________|
	                 |                              |                            |
	                 |                              |   5. Add A Customer        |
	                 |      1. Create Order         |   6. Get All Customers     |
	                 |      2. Get All Orders       |   7. Get Customer By Name  |
	                 |      3. Sort Orders          |   8. Update a Customer     |
	                 |      4. Cancel Order         |   9. Sort Customers        |
	                 |                              |   10. Delete Customer      |
	                 |                              |                            |
	                 |-----------------------------------------------------------|
	                 |                         99. Exit                          | 
	                 |___________________________________________________________|

         
                                      Select An Option To Start:
	            """);
	}
	
	
	private void foodMenu() {
	    System.out.println("""
	            +-----------------------------------------------------------------------------------+
	            |                                         MENU                                      |
	            +-----------------------------------------------------------------------------------+
	            |           Food             |           Sides            |         Drinks          |
	            +-----------------------------------------------------------------------------------+
	            | 1. Hamburger - $5.99       | 7. French Fries - $1.99    | 13. Soda - $1.99        |
	            | 2. Pizza - $3.99           | 8. Onion Rings - $2.99     | 14. Orange Juice - $1.99|
	            | 3. HotDog - $2.99          | 9. Mashed Potatoes - $3.99 | 15. Iced Tea - $1.99    |
	            | 4. Steak & Cheese - $5.99  | 10. Chicken Nuggets - $1.99| 16. Water - $0.99       |
	            | 5. Chicken Sandwich - $6.99| 11. Mac & Cheese - $3.99   | 17. Milk - $1.99        |
	            | 6. Caesar Salad - $4.99    | 12. Biscuit - $0.99        | 18. Coffee - $1.99      |
	            +-----------------------------------------------------------------------------------+
	            """);
	}

	
	

}
