package com.masai.service;


import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Address;
import com.masai.model.Customer;
import com.masai.model.ResetPassword;

public interface CustomerService {
	
    public Customer addCustomer(Customer customer) throws CustomerException;
	
	public Customer updateCustomer(String key, Customer customer) throws CustomerException, LoginException;
	
    public String removeCustomer(String key, String password)throws CustomerException, LoginException;
	
	public Customer viewCustomer(String key)throws CustomerException, LoginException;
	
	public String updateAddress(String key, Address address) throws CustomerException, LoginException;
	
	public String updatepassword(String key, ResetPassword resetPasswordDTO) throws CustomerException, LoginException;
}
