package com.masai.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.CustomerException;
import com.masai.exception.ItemException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.Item;
import com.masai.service.ItemService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/MealsOnWheels")
public class ItemController {

	@Autowired
	private ItemService iItemService;

	@PostMapping("/items/restaurant/{logginKey}")
	public ResponseEntity<Item> addItemsHandler(@PathVariable("logginKey") String key,
			@Valid @RequestBody Item item) throws ItemException, LoginException, RestaurantException
	{
		Item addeditem = iItemService.addItem(key, item);

		return new ResponseEntity<>(addeditem, HttpStatus.ACCEPTED);
	}

	
	@PutMapping("/items/restaurant/update_item/{logginKey}")
	public ResponseEntity<Item> updateItemsHandler(@PathVariable("logginKey") String key,
			@Valid @RequestBody Item item) throws ItemException, LoginException, RestaurantException
	{
		Item updateditem = iItemService.updateItem(key, item);

		return new ResponseEntity<>(updateditem, HttpStatus.OK);
	}
	
	@GetMapping("/items/{restaurantId}")
	public ResponseEntity<List<Item>> viewAllItemsByRestaurantHandler(@PathVariable("restaurantId")Integer restaurantId) throws ItemException, RestaurantException
	{
		List<Item> items = iItemService.viewAllItemsByRestaurant(restaurantId);

		return new ResponseEntity<>(items, HttpStatus.FOUND);
	}
	
	@DeleteMapping("/items/restaurant/set_not_available/{logginKey}/{itemId}")
	public ResponseEntity<String> setItemNotAvailableHandler(@PathVariable("logginKey") String logginKey, @PathVariable("itemId") Integer itemId) throws ItemException, RestaurantException, LoginException{
		
		String result = iItemService.setItemNotAvailable(logginKey, itemId);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	@GetMapping("/items/get_item/{itemName}/{restaurantId}")
	public ResponseEntity<Item> viewItemHandler(@PathVariable("itemName") String itemName,@PathVariable("restaurantId") Integer restaurantId) throws ItemException, RestaurantException{
		
		Item  item = iItemService.viewItem(itemName, restaurantId);
		
		return new ResponseEntity<>(item, HttpStatus.FOUND);
	}
	
	@GetMapping("/items/customer/search_item_from_nearby_restaurants/{loginKey}/{itemName}")
	public ResponseEntity<Map<String, Item>> viewItemsOnMyAddressHandler(@PathVariable("loginKey") String loginKey, @PathVariable("itemName") String itemName) throws ItemException, RestaurantException, LoginException, CustomerException

	{
		Map<String, Item> items= iItemService.viewItemsOnMyAddress(loginKey, itemName);

		return new ResponseEntity<>(items, HttpStatus.FOUND);
	}

}
