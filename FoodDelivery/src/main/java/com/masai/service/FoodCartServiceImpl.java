package com.masai.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.CustomerException;
import com.masai.exception.FoodCartException;
import com.masai.exception.ItemException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.CurrentUserSession;
import com.masai.model.Customer;
import com.masai.model.FoodCart;
import com.masai.model.Item;
import com.masai.model.ItemQuantityDTO;
import com.masai.model.Restaurant;
import com.masai.repository.CustomerRepo;
import com.masai.repository.FoodCartRepo;
import com.masai.repository.ItemRepo;
import com.masai.repository.RestaurantRepo;
import com.masai.repository.SessionRepo;

@Service
public class FoodCartServiceImpl implements FoodCartService{
	@Autowired
	private FoodCartRepo cartRepo;
	
	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private RestaurantRepo restaurantRepo;
	
	@Autowired
	private ItemRepo itemRepo;
	
	@Override
	public FoodCart addItemToCart(String key, String itemName, Integer restaurantId) throws FoodCartException, LoginException, ItemException, RestaurantException, CustomerException{
		
		Restaurant restaurant = restaurantRepo.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant not found"));
		
		List<Item> items = restaurant.getItems();
		
		Item item = null;
		
		for(Item i: items) {
			if(i.getItemName().equalsIgnoreCase(itemName)) {
				item = i;
				break;
			}
		}
		if(item == null || item.getQuantity()<=0) throw new ItemException("Item not found in this restaurant or item is out of stock");
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to add item in your cart");
		Customer customer= customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		FoodCart foodCart= customer.getFoodCart();
		
		Map<Integer, Integer> itemsMap = foodCart.getItems();
		
		Integer itemId = item.getItemId();
		
		
			
			if(itemsMap.containsKey(itemId)) {
				itemsMap.put(itemId, itemsMap.get(itemId)+1);
			}
			else {
				itemsMap.put(itemId, 1);
			}
		
		if(customer.getAddress() == null) throw new CustomerException("Please add address first");
		
		if(!restaurant.getAddress().getPincode().equals(customer.getAddress().getPincode())) {
			throw new CustomerException("This item is not deliverable in your area");
		}
		
		foodCart.setCustomer(customer);
		customer.setFoodCart(foodCart);
		
		return cartRepo.save(foodCart);
	}

	@Override
	public FoodCart increaseQuantity(String key, String itemName, 	int quantity) throws FoodCartException, LoginException, ItemException, CustomerException{
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to place your order");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		FoodCart foodCart = customer.getFoodCart();
		Map<Integer, Integer> itemsMap = foodCart.getItems();
		Item item= null;
		
		for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
			Item itemInCart = itemRepo.findById(entry.getKey()).get();
			if(itemInCart.getItemName().equalsIgnoreCase(itemName)) {
				item = itemInCart;
				break;
			}
		}
		
		if(item == null) throw new FoodCartException("Item is not available in the cart, please add the item first");
		
		Integer itemId = item.getItemId();
		
		if(item.getQuantity() >= (quantity + itemsMap.get(itemId)))  itemsMap.put(itemId, itemsMap.get(itemId) + quantity);
		else throw new ItemException("Insufficient item quantity");
		
		return cartRepo.save(foodCart);
	}

	@Override
	public FoodCart reduceQuantity(String key, String itemName, int quantity) throws FoodCartException, LoginException, ItemException, CustomerException{

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to reduce quantity in your cart");
		Customer customer= customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		FoodCart foodCart = customer.getFoodCart();
		
		Map<Integer, Integer> itemsMap = foodCart.getItems();
		Item item = null;
		for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
			Item itemInCart = itemRepo.findById(entry.getKey()).get();
			if(itemInCart.getItemName().equalsIgnoreCase(itemName)) {
				item = itemInCart;
				break;
			}
		}
		if(item==null) throw new FoodCartException("Item not found in your cart");
		
		Integer itemId = item.getItemId();
		
		if(item.getQuantity() == 0) {
			itemsMap.remove(itemId);
			cartRepo.save(foodCart);
			throw new ItemException(itemName+ " is already out of stock and removed from your cart");
		}
		
		if(quantity < itemsMap.get(itemId)) itemsMap.put(itemId, itemsMap.get(itemId) - quantity);
		else {
			itemsMap.remove(itemId);
			cartRepo.save(foodCart);
			throw new ItemException(item.getItemName()+ " is removed from the cart");
		}
		
		return cartRepo.save(foodCart);
	}

	@Override
	public FoodCart removeItem(String key, String itemName) throws FoodCartException, CustomerException, LoginException{

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to remove item from your cart");
		Customer customer= customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		FoodCart foodCart= customer.getFoodCart();
		
		Map<Integer, Integer> itemsMap = foodCart.getItems();
		Item item = null;
		for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
			Item itemInCart = itemRepo.findById(entry.getKey()).get();
			if(itemInCart.getItemName().equalsIgnoreCase(itemName)) {
				item = itemInCart;
				break;
			}
		}
		if(item == null) throw new FoodCartException(itemName + " not found in your cart");
		
		Integer itemId = item.getItemId();
		
		itemsMap.remove(itemId);
		
		return cartRepo.save(foodCart);
	}
	
	@Override
	public FoodCart clearCart(String key) throws FoodCartException, CustomerException, LoginException {

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to clear your cart");
		Customer customer= customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		FoodCart foodCart = customer.getFoodCart();
		
		Map<Integer, Integer> itemsMap = foodCart.getItems();
		
		if(itemsMap.size()==0) throw new FoodCartException("Cart is already empty");
		
		itemsMap.clear();
		
		return cartRepo.save(foodCart);
	}

	@Override
	public List<ItemQuantityDTO> viewCart(String key) throws LoginException, CustomerException, FoodCartException {

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view your cart items");
		Customer customer= customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		if(customer.getFoodCart().getItems().isEmpty()) throw new FoodCartException("Item(s) not found in your cart");
		
		Map<Integer, Integer> itemsMap = customer.getFoodCart().getItems();
		
		List<ItemQuantityDTO> items = new ArrayList<>();
		
		for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
			Item item = itemRepo.findById(entry.getKey()).get();
			ItemQuantityDTO dto = new ItemQuantityDTO(item.getItemId(), item.getItemName(), entry.getValue(), item.getCategory().getCategoryName(), item.getCost());
			items.add(dto);
		}
		
		return items;
	}

}
