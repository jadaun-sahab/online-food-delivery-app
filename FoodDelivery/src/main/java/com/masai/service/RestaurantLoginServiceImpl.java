package com.masai.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.CurrentUserSession;
import com.masai.model.LoginDTO;
import com.masai.model.Restaurant;
import com.masai.repository.RestaurantRepo;
import com.masai.repository.SessionRepo;

import net.bytebuddy.utility.RandomString;

@Service
public class RestaurantLoginServiceImpl implements RestaurantLoginService{

	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private RestaurantRepo restaurantRepo;
	
	@Override
	public CurrentUserSession login(LoginDTO dto) throws LoginException, RestaurantException {
		
		Restaurant restaurant = restaurantRepo.findByMobileNumber(dto.getMobileNumber());
		
		if(restaurant==null) throw new RestaurantException("Please enter a valid mobile number");
		
		Optional<CurrentUserSession> currentUserSession = sessionRepo.findById(restaurant.getRestaurantId());
		
		if(currentUserSession.isPresent()) throw new LoginException("User already logged in with this mobile number");
		
		List<CurrentUserSession> list = sessionRepo.findAll();
		for(CurrentUserSession c : list) {
			sessionRepo.delete(c);
		}
		
		if(!restaurant.getPassword().equals(dto.getPassword())) throw new LoginException("Incorrect password");

		String key = RandomString.make(6);
		
		CurrentUserSession genrateSession = new CurrentUserSession(restaurant.getRestaurantId(), key, LocalDateTime.now());
		
		sessionRepo.save(genrateSession);
		
		return genrateSession;
	}

	@Override
	public String logout(String key) throws LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		
		if(currentUserSession==null) throw new LoginException("Invalid User key");
		
		sessionRepo.delete(currentUserSession);
		
		return "Logged out successfully";
	}

}
