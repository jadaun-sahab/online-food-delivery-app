package com.masai.service;

import java.util.List;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Customer;
<<<<<<< HEAD


public interface AdminService {

	public String deleteAccounts(LoginDTOO loginDTO) throws LoginException;
	
	public List<Customer> showToBeDeletedAccounts(LoginDTOO loginDTO) throws CustomerException, LoginException;
=======
import com.masai.model.Login;

public interface AdminService {

	public String deleteAccounts(Login loginDTO) throws LoginException;
	
	public List<Customer> showToBeDeletedAccounts(Login loginDTO) throws CustomerException, LoginException;
>>>>>>> a6a47b2a8fdad92e7f208a2e3ceb69790afec34b
}
