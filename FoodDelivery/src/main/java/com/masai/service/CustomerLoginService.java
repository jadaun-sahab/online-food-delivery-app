package com.masai.service;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.CurrentUserSession;
import com.masai.model.LoginDTO;

public interface CustomerLoginService {

	public CurrentUserSession login(LoginDTO dto) throws LoginException, CustomerException;
	
	public String logout(String key) throws LoginException;
	
}
