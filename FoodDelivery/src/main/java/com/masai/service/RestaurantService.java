package com.masai.service;

import java.util.List;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.ResetPasswordDTO;
import com.masai.model.Restaurant;
import com.masai.model.Suggestion;

public interface RestaurantService {

	public Restaurant addRestaurant(Integer verificationId, Restaurant restaurant)throws RestaurantException;
	
	public Restaurant updateRestaurant(String key, Restaurant res)throws RestaurantException, LoginException;
	
	public Restaurant viewRestaurant(Integer restaurantId)throws RestaurantException;
	
	public List<Restaurant> viewNearByRestaurant(String cityName, String pincode)throws RestaurantException;
	
	public List<Restaurant> viewRestaurantByItemName(String itemname, String pincode)throws RestaurantException;
	
	public String restaurantStatus(Integer restaurantId) throws RestaurantException;
	
	public String giveSuggestionAboutItem(String key, Suggestion suggestion, String pincode) throws CustomerException, LoginException, RestaurantException;
	
	public List<Suggestion> viewSuggestions(String key) throws LoginException, RestaurantException;

	public String updatepassword(String key, ResetPasswordDTO resetPasswordDTO) throws RestaurantException, LoginException;
	
}
