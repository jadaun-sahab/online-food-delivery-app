package com.masai.service;

import java.util.List;

import com.masai.exception.BillException;
import com.masai.exception.CustomerException;
import com.masai.exception.FoodCartException;
import com.masai.exception.ItemException;
import com.masai.exception.LoginException;
import com.masai.exception.OrderDetailsException;
import com.masai.exception.RestaurantException;
import com.masai.model.OrderDetails;


public interface OrderService {
	public List<OrderDetails> placeOrder(String uuId, String paymentType) throws OrderDetailsException, LoginException, CustomerException, FoodCartException,ItemException, BillException, RestaurantException ;
	
	
	public String cancelOrder(String key, Integer orderId) throws OrderDetailsException, LoginException, CustomerException;
	
	public OrderDetails viewOrderByIdByCustomer(String key, Integer orderId) throws OrderDetailsException, CustomerException, LoginException;
	
	public OrderDetails viewOrderByIdByRestaurant(String key, Integer orderId) throws OrderDetailsException, LoginException, RestaurantException;
	
	public List<OrderDetails> viewAllOrdersByRestaurant(String key) throws OrderDetailsException, LoginException , RestaurantException;
	
	public List<OrderDetails> viewAllOrdersByCustomer(String key) throws OrderDetailsException, CustomerException, LoginException;
	
	public List<OrderDetails> viewAllOrdersByRestaurantByCustomerId(String key, Integer customerId) throws OrderDetailsException, LoginException , RestaurantException, CustomerException;
}
