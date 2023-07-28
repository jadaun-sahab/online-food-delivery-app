package com.masai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.ResetPasswordDTO;
import com.masai.model.Restaurant;
import com.masai.model.Suggestion;
import com.masai.service.RestaurantService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/MealsOnWheels")
public class RestaurantController {

	@Autowired
	private RestaurantService resService;
	
	@PostMapping("/restaurants/register/{verficationId}")
	public ResponseEntity<Restaurant> addRestuarants(@PathVariable("verficationId") Integer verificationId,@Valid @RequestBody Restaurant restaurant) throws RestaurantException{
		return new ResponseEntity<>(resService.addRestaurant(verificationId,restaurant),HttpStatus.CREATED);
		
	}
	
	@PutMapping("/restaurants/update_basic_details/{loginkey}")
	public ResponseEntity<Restaurant> updateRestuarants( @PathVariable("loginkey") String  loginkey, @Valid @RequestBody Restaurant restaurant) throws RestaurantException, LoginException{
		return new ResponseEntity<>(resService.updateRestaurant(loginkey,restaurant),HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/restaurants/{resId}")
	public ResponseEntity<Restaurant> viewRestuarant(@PathVariable("resId") Integer resId) throws RestaurantException{
		return new ResponseEntity<>(resService.viewRestaurant(resId),HttpStatus.FOUND);
	
	}
	@GetMapping("/restaurants/location/{city}/{pincode}")
	public ResponseEntity<List<Restaurant>> getRestaurantsByLocation(@PathVariable("city") String cityName,@PathVariable("pincode") String pincode) throws RestaurantException {
		return new ResponseEntity<>(resService.viewNearByRestaurant(cityName,pincode),HttpStatus.FOUND);
	
	}
	
	@GetMapping("/restaurants/{itemName}/{pincode}")
	public ResponseEntity<List<Restaurant>> getRestaurantsByItemName(@PathVariable("itemName") String itemName, @PathVariable("pincode") String pincode) throws RestaurantException {
		return new ResponseEntity<>(resService.viewRestaurantByItemName(itemName,pincode),HttpStatus.FOUND);
	
	}
	
	@GetMapping("/restaurants/status/{resId}")	
	public	ResponseEntity<String> restaurantStatus(@PathVariable("resId") Integer restaurantId) throws RestaurantException {
			return new ResponseEntity<>(resService.restaurantStatus(restaurantId),HttpStatus.FOUND);
			
	}
	
	@PostMapping("/restaurants/customer/suggest_item/{loginkey}/{pincode}")
	public ResponseEntity<String> giveSuggestionsAboutItem(@PathVariable("loginkey") String loginkey,@Valid @RequestBody Suggestion suggestion, @PathVariable("pincode") String pincode) throws CustomerException, LoginException, RestaurantException{
		return new ResponseEntity<>(resService.giveSuggestionAboutItem(loginkey,suggestion,pincode),HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/restaurants/suggestions/{loginkey}")
	public ResponseEntity<List<Suggestion>> viewSuggestions(@PathVariable("loginkey") String loginkey) throws LoginException, RestaurantException{
		return new ResponseEntity<>(resService.viewSuggestions(loginkey),HttpStatus.FOUND);
		
	}
	
	@PutMapping("/restaurants/update_password/{logginKey}")
	public ResponseEntity<String> updateRestaurantPassword(@PathVariable("logginKey") String key, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws LoginException, RestaurantException {
		ResponseEntity<String> restaurantResponseEntity = new ResponseEntity<>(resService.updatepassword(key, resetPasswordDTO), HttpStatus.ACCEPTED);
		return restaurantResponseEntity;
	}
}
