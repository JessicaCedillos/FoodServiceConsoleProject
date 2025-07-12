package com.flavorexpress.flavorexpress.shared;

import java.util.Scanner;

import com.flavorexpress.flavorexpress.shared.managers.CustomerManager;
import com.flavorexpress.flavorexpress.shared.managers.OrderManager;
import com.flavorexpress.flavorexpress.shared.repository.CustomerRepository;
import com.flavorexpress.flavorexpress.shared.repository.OrderRepository;
import com.flavorexpress.flavorexpress.shared.ui.Console;

public class App 
{
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        CustomerRepository customerRepository = new CustomerRepository();
    	CustomerManager customerManager = new CustomerManager(customerRepository);
    	OrderRepository orderRepository = new OrderRepository();
    	OrderManager orderManager = new OrderManager(orderRepository);
    	Console console = new Console(customerManager, orderManager, scanner);
    	console.start();
        
    }
}
