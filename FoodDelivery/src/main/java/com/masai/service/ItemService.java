package com.masai.service;

import java.util.List;
import java.util.Map;

import com.masai.exception.CustomerException;
import com.masai.exception.ItemException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.Item;

public interface ItemService {

	public Item addItem(String key,Item item)throws ItemException, LoginException, RestaurantException;
	
	public Item updateItem(String key,Item item) throws ItemException, LoginException, RestaurantException;
	
	public List<Item> viewAllItemsByRestaurant(Integer restaurantId) throws ItemException ,RestaurantException;
	
	public String setItemNotAvailable(String key, Integer itemId) throws ItemException, RestaurantException, LoginException;
	
	public Item viewItem(String itemName, Integer restaurantId) throws ItemException, RestaurantException;
	
	public Map<String, Item> viewItemsOnMyAddress(String key, String itemName) throws ItemException, RestaurantException, LoginException, CustomerException;
	
	
}
