package com.masai.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.CurrentUserSession;
import com.masai.model.Customer;
import com.masai.model.Item;
import com.masai.model.ResetPasswordDTO;
import com.masai.model.Restaurant;
import com.masai.model.Suggestion;
import com.masai.repository.CustomerRepo;
import com.masai.repository.RestaurantRepo;
import com.masai.repository.SessionRepo;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepo restaurantRepo;

	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private CustomerRepo customerRepo;

	@Override
	public Restaurant addRestaurant(Integer verificationId, Restaurant restaurant) throws RestaurantException {

		List<CurrentUserSession> list = sessionRepo.findAll();
		for(CurrentUserSession c : list) {
			sessionRepo.delete(c);
		}
		
		if(verificationId != 8080) throw new RestaurantException("Enter vaild verification id");
		
		Restaurant restaurantExist = restaurantRepo.findByMobileNumber(restaurant.getMobileNumber());
		if(restaurantExist != null) throw new RestaurantException("Mobile number already registered");
		
		List<Restaurant> restaurants = restaurantRepo.findAll();
		
		for(Restaurant r : restaurants) {
			if(r.getAddress().getPincode().equals(restaurant.getAddress().getPincode()) && r.getRestaurantName().equals(restaurant.getRestaurantName())) {
				throw new RestaurantException("Restaurant  with this name is already present in your area");
			}
		}
		
		return restaurantRepo.save(restaurant);
		
	}

	@Override
	public Restaurant updateRestaurant(String key, Restaurant updatedRestaurant) throws RestaurantException, LoginException {

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to update your restaurant details");
		Restaurant restaurant = restaurantRepo.findById(currentUserSession.getId()).orElseThrow(()-> new RestaurantException("Please login as Restaurant"));
		
		List<Restaurant> restaurants = restaurantRepo.findAll();
		
		for(Restaurant r : restaurants) {
			if(r.equals(restaurant)) continue;
			if(r.getAddress().getPincode().equals(updatedRestaurant.getAddress().getPincode()) && r.getRestaurantName().equalsIgnoreCase(updatedRestaurant.getRestaurantName())) {
				throw new RestaurantException("Can't change restaurant name, restaurant with this name is already present in your area");
			}
		}
		
		if(updatedRestaurant.getRestaurantName() != null)
			restaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
		if(updatedRestaurant.getManagerName() != null)
			restaurant.setManagerName(updatedRestaurant.getManagerName());
		if(updatedRestaurant.getOpenTime() != null)
			restaurant.setOpenTime(updatedRestaurant.getOpenTime());
		if(updatedRestaurant.getCloseTime() != null)
			restaurant.setCloseTime(updatedRestaurant.getCloseTime());
		if(updatedRestaurant.getEmail() != null)
			restaurant.setEmail(updatedRestaurant.getEmail());
		restaurant.setMobileNumber(updatedRestaurant.getMobileNumber());
		restaurant.setAddress(updatedRestaurant.getAddress());
		return restaurantRepo.save(restaurant);
	}

	@Override
	public Restaurant viewRestaurant(Integer restaurantId) throws RestaurantException {
		Restaurant restaurant = restaurantRepo.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant not found with the id: " + restaurantId));
		
		return restaurant;
		
	}

	@Override
	public String restaurantStatus(Integer restaurantId) throws RestaurantException {
		Restaurant restaurant = restaurantRepo.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant not found with the id: " + restaurantId));
		
		if(LocalTime.now().isAfter(restaurant.getCloseTime()) || LocalTime.now().isBefore(restaurant.getOpenTime())) return "Closed";
		
		return "Open";
		
	}
	
	@Override
	public List<Restaurant> viewNearByRestaurant(String cityName, String pincode) throws RestaurantException {
		List<Restaurant> nearByRestaurants = new ArrayList<>();
		
		List<Restaurant> restaurants = restaurantRepo.findAll();
		
		for(Restaurant r : restaurants) {
			if(r.getAddress().getCity().equalsIgnoreCase(cityName) && r.getAddress().getPincode().equals(pincode)) {
				nearByRestaurants.add(r);
			}
		}
		
		if(nearByRestaurants.isEmpty()) throw new RestaurantException("Restaurant(s) not found in your area");
		
		return nearByRestaurants;
		
	}

	@Override
	public List<Restaurant> viewRestaurantByItemName(String itemname, String pincode) throws RestaurantException {
		
		List<Restaurant> nearByRestaurants = new ArrayList<>();
		List<Restaurant> restaurants = restaurantRepo.findAll();
		
		for(Restaurant r : restaurants) {
			if(r.getAddress().getPincode().equals(pincode)) {
				nearByRestaurants.add(r);
			}
		}
		if(nearByRestaurants.isEmpty()) throw new RestaurantException("Restaurant(s) not found in your area with "+ itemname);
		
		List<Restaurant> filteredRestaurants = new ArrayList<>();
		for(Restaurant r : nearByRestaurants) {
			List<Item> items = r.getItems();
			for(Item i : items) {
				if(i.getItemName().equalsIgnoreCase(itemname) && i.getQuantity() > 0) {
					filteredRestaurants.add(r);
				}
			}
		}
		
		if(filteredRestaurants.isEmpty()) throw new RestaurantException("Restaurant(s) not found in your area currently serving " + itemname + ". You can give suggestion to add your dish.");
		
		return filteredRestaurants;
		
	}

	@Override
	public String giveSuggestionAboutItem(String key, Suggestion suggestion, String pincode) throws CustomerException, LoginException, RestaurantException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to suggest your dish");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		List<Restaurant> nearByRestaurants = new ArrayList<>();
		List<Restaurant> restaurants = restaurantRepo.findAll();
		
		for(Restaurant r : restaurants) {
			if(r.getAddress().getPincode().equals(pincode)) {
				nearByRestaurants.add(r);
			}
		}
		if(nearByRestaurants.isEmpty()) throw new RestaurantException("Restaurant(s) not found in your area");
		
		for(Restaurant r : nearByRestaurants) {
			List<Item> items = r.getItems();
			for(Item i : items) {
				if(i.getItemName().equalsIgnoreCase(suggestion.getItemName()) && i.getQuantity() > 0) {
					throw new RestaurantException(suggestion.getItemName() + " is already present in your area");
				}
			}
		}
		
		for(Restaurant r : nearByRestaurants) {
			r.getSuggestions().add(suggestion);
			restaurantRepo.save(r);
		}
		
		return "Thankyou " + customer.getFirstName() + ", for the suggestion of " + suggestion.getItemName();
		
	}

	@Override
	public List<Suggestion> viewSuggestions(String key) throws LoginException, RestaurantException {

		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login get suggestions");
		Restaurant restaurant = restaurantRepo.findById(currentUserSession.getId()).orElseThrow(()-> new RestaurantException("Please login as Restaurant"));
		
		List<Suggestion> suggestions = restaurant.getSuggestions();
		if(suggestions.isEmpty()) throw new RestaurantException("Suggestion(s) not found");
		return suggestions;
		
	}

	@Override
	public String updatepassword(String key, ResetPasswordDTO resetPasswordDTO) throws RestaurantException, LoginException {
		
		String currentPassword = resetPasswordDTO.getCurrentPassword();
		
		String newPassword = resetPasswordDTO.getNewPassword();
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to change password");
		Restaurant restaurant = restaurantRepo.findById(currentUserSession.getId()).orElseThrow(()-> new RestaurantException("Please login as Restaurant"));
		
		if(!restaurant.getPassword().equals(currentPassword)) throw new RestaurantException("Invaild current password");
		
		restaurant.setPassword(newPassword);
		restaurantRepo.save(restaurant);
		
		return "Password updated sucssesfully";
		
	}
	
}
