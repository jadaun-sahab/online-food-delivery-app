package com.masai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.CurrentUserSession;
import com.masai.model.LoginDTO;
import com.masai.service.CustomerLoginService;
import com.masai.service.RestaurantLoginService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/MealsOnWheels")
public class LoginController {

	@Autowired
	private CustomerLoginService customerLoginService;
	
	@Autowired
	private RestaurantLoginService restaurantLoginService;
	
	@PostMapping("/login/customer/login")
	public ResponseEntity<CurrentUserSession> customerLoginHandler(@RequestBody LoginDTO dto) throws LoginException, CustomerException{
		
		CurrentUserSession currentUserSession = customerLoginService.login(dto);
		
		return new ResponseEntity<>(currentUserSession, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/login/restaurant/login")
	public ResponseEntity<CurrentUserSession> restaurantLoginHandler(@RequestBody LoginDTO dto) throws LoginException, RestaurantException{
		
		CurrentUserSession currentUserSession = restaurantLoginService.login(dto);
		
		return new ResponseEntity<>(currentUserSession, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/logout/user/{key}")
	public ResponseEntity<String> customerLogoutHandler(@PathVariable("key") String key) throws LoginException{
		
		String result = customerLoginService.logout(key);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
