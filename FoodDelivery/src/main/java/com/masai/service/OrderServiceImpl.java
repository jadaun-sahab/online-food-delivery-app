package com.masai.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.BillException;
import com.masai.exception.CustomerException;
import com.masai.exception.FoodCartException;
import com.masai.exception.ItemException;
import com.masai.exception.LoginException;
import com.masai.exception.OrderDetailsException;
import com.masai.exception.RestaurantException;
import com.masai.model.Bill;
import com.masai.model.CurrentUserSession;
import com.masai.model.Customer;
import com.masai.model.FoodCart;
import com.masai.model.Item;
import com.masai.model.ItemQuantityDTO;
import com.masai.model.OrderDetails;
import com.masai.model.Restaurant;
import com.masai.model.Status;
import com.masai.repository.CustomerRepo;
import com.masai.repository.FoodCartRepo;
import com.masai.repository.ItemRepo;
import com.masai.repository.OrderDetailsRepo;
import com.masai.repository.RestaurantRepo;
import com.masai.repository.SessionRepo;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDetailsRepo orderDetailsRepo;
	
	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private ItemRepo itemRepo;
	
	@Autowired
	private RestaurantRepo restaurantRepo;
	
	@Autowired
	private FoodCartRepo foodCartRepo;
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Override
	public List<OrderDetails> placeOrder(String key, String paymentType) throws OrderDetailsException, LoginException, CustomerException, FoodCartException, ItemException, BillException, RestaurantException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to place your order");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		FoodCart foodCart = customer.getFoodCart();

		Map<Integer, Integer> itemsMap = foodCart.getItems();
		if(itemsMap.isEmpty()) throw new FoodCartException("Item(s) not found in your cart");
		
		for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
			
			Item itemInCart = itemRepo.findById(entry.getKey()).get();
			
			if(itemInCart.getQuantity() < entry.getValue()) {
				throw new ItemException("Insufficient item quantity in the restaurant");				
			}
			
			if(restaurantService.restaurantStatus(itemInCart.getRestaurant().getRestaurantId()).equals("Closed")) {
				throw new RestaurantException(itemInCart.getRestaurant().getRestaurantName() + " is closed");
			}
			
			if(!itemInCart.getRestaurant().getAddress().getPincode().equals(customer.getAddress().getPincode())) {
				throw new CustomerException("This item is not deliverable in your area");
			}
		}
		
		Map<Integer, OrderDetails> restaurantOrderMap = new HashMap<>();
		
		for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
			
			Item item = itemRepo.findById(entry.getKey()).get();
			
			Integer restaurantId = item.getRestaurant().getRestaurantId();
			
			
			
			if(restaurantOrderMap.containsKey(restaurantId)) {
				
				ItemQuantityDTO dto = new ItemQuantityDTO(item.getItemId(), item.getItemName(), entry.getValue(), item.getCategory().getCategoryName(), item.getCost());
				
				OrderDetails orderDetails = restaurantOrderMap.get(restaurantId);
				orderDetails.setTotalAmount(orderDetails.getTotalAmount() + item.getCost() * entry.getValue());
				orderDetails.getItems().add(dto);
				
				restaurantOrderMap.put(restaurantId, orderDetails);
				
			}else {
				
				ItemQuantityDTO dto = new ItemQuantityDTO(item.getItemId(), item.getItemName(), entry.getValue(), item.getCategory().getCategoryName(), item.getCost());
				
				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setOrderDate(LocalDateTime.now());
				orderDetails.setCustomerId(customer.getCustomerID());
				orderDetails.setRestaurantId(restaurantId);
				orderDetails.setPaymentStatus(Status.valueOf(paymentType));
				orderDetails.setTotalAmount(item.getCost() * entry.getValue());
				orderDetails.getItems().add(dto);
				
				restaurantOrderMap.put(restaurantId, orderDetails);
			}
			
			item.setQuantity(item.getQuantity()-entry.getValue());
			itemRepo.save(item);
		}
		
		List<OrderDetails> orderDetailsList = new ArrayList<>();
		
		for(Map.Entry<Integer, OrderDetails> restaurantOrder : restaurantOrderMap.entrySet()) {
			
			OrderDetails orderDetails = restaurantOrder.getValue();
			
			Bill bill =  billService.genrateBill(orderDetails);
			
			orderDetails.setBill(bill);
			
			orderDetails = orderDetailsRepo.save(orderDetails);
			
			orderDetailsList.add(orderDetails);
		}
		
		foodCart.setItems(new HashMap<Integer, Integer>());
		
		foodCartRepo.save(foodCart);
		
		return orderDetailsList;
		
	}
	
	@Override
	public String cancelOrder(String key, Integer orderId) throws OrderDetailsException, LoginException, CustomerException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to cancel your order");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		OrderDetails orderDetails = orderDetailsRepo.findById(orderId).orElseThrow(()-> new OrderDetailsException("Please pass valid order Id"));
		
		if(orderDetails.getCustomerId() != customer.getCustomerID()) throw new CustomerException("Invalid order Id: "+ orderId);
		
		LocalDateTime deliverTime = orderDetails.getOrderDate().plusMinutes(20);
		
		if(LocalDateTime.now().isAfter(deliverTime.minusMinutes(10))) {
			throw new OrderDetailsException("Order can not be cancelled, time limit exceeded for cancellation");
		}
		
		List<ItemQuantityDTO> itemsDto = orderDetails.getItems();

		String result = "Order cancelled successfully";
		
		if(orderDetails.getPaymentStatus().toString().equals("PAYMENT_SUCCESS")) {
			result += " and your payment transferred back to your account.";
		}
		
		orderDetailsRepo.delete(orderDetails);
		
		for(ItemQuantityDTO i : itemsDto) {
			
			Item item = itemRepo.findById(i.getItemId()).get();
			item.setQuantity(item.getQuantity() + i.getOrderedQuantity());
			itemRepo.save(item);
		}
		
		return result;
		
	}

	@Override
	public OrderDetails viewOrderByIdByCustomer(String key, Integer orderId) throws OrderDetailsException, CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view your order");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		OrderDetails orderDetails = orderDetailsRepo.findById(orderId).orElseThrow(()-> new OrderDetailsException("Invalid order Id: "+ orderId));
		
		if(orderDetails.getCustomerId() != customer.getCustomerID()) throw new OrderDetailsException("Invalid order Id: "+ orderId);
		
		return orderDetails;
		
	}
	
	
	
	@Override
	public OrderDetails viewOrderByIdByRestaurant(String key, Integer orderId) throws OrderDetailsException, RestaurantException, LoginException{

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view order details");
		Restaurant restaurant = restaurantRepo.findById(currentUserSession.getId()).orElseThrow(()-> new RestaurantException("Please login as Restaurant"));
		
		OrderDetails orderDetails=orderDetailsRepo.findById(orderId).orElseThrow(() -> new OrderDetailsException("Invalid order Id: "+ orderId));
		
		if(orderDetails.getRestaurantId().equals(restaurant.getRestaurantId())) {
			return orderDetails;
		}
		
		throw new OrderDetailsException("Invalid order Id: "+ orderId);
		
	}
	
	@Override
	public List<OrderDetails> viewAllOrdersByRestaurant(String key) throws OrderDetailsException, LoginException , RestaurantException{
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view order(s) details");
		Restaurant restaurant = restaurantRepo.findById(currentUserSession.getId()).orElseThrow(()-> new RestaurantException("Please login as Restaurant"));
		
		List<OrderDetails> orders =  orderDetailsRepo.findByRestaurantId(restaurant.getRestaurantId());
		
		if(orders.isEmpty()) throw new OrderDetailsException("Orders not found");
		
		return orders;
	}

	@Override
	public List<OrderDetails> viewAllOrdersByCustomer(String key) throws OrderDetailsException, CustomerException, LoginException {

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view your order(s) details");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		List<OrderDetails> orders = orderDetailsRepo.findByCustomerId(customer.getCustomerID());
		
		if(orders.isEmpty()) throw new OrderDetailsException("Order(s) not found");
		
		return orders;
		
	}
	
	@Override
	public List<OrderDetails> viewAllOrdersByRestaurantByCustomerId(String key, Integer customerId) throws OrderDetailsException, LoginException , RestaurantException, CustomerException{

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view order(s) details");
		Restaurant restaurant = restaurantRepo.findById(currentUserSession.getId()).orElseThrow(()-> new RestaurantException("Please login as Restaurant"));
		
		List<OrderDetails> orders= orderDetailsRepo.findByRestaurantIdAndCustomerId(restaurant.getRestaurantId(), customerId);
		
		if(orders.isEmpty()) throw new OrderDetailsException("Orders not found");
		
		return orders;
		
	}
}