package com.masai.service;

import com.masai.exception.LoginException;
import com.masai.exception.RestaurantException;
import com.masai.model.CurrentUserSession;
import com.masai.model.LoginDTO;

public interface RestaurantLoginService {

	public CurrentUserSession login(LoginDTO dto) throws LoginException, RestaurantException;
	
	public String logout(String key) throws LoginException;
	
}
