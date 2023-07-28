package com.masai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Address;
import com.masai.model.Customer;
import com.masai.model.ResetPasswordDTO;
import com.masai.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/MealsOnWheels")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customers/create_account")
	public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer)throws CustomerException{
		ResponseEntity<Customer> customerResponseEntity = new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.CREATED);
		return customerResponseEntity;
	}
	
	@PutMapping("/customers/update_basic_details)/{logginKey}")
	public ResponseEntity<Customer> updateCustomerDetails(@PathVariable("logginKey") String key,@Valid @RequestBody Customer customer)throws CustomerException, LoginException{
		ResponseEntity<Customer> customerResponseEntity = new ResponseEntity<>(customerService.updateCustomer(key,customer), HttpStatus.ACCEPTED);
		return customerResponseEntity;
	}
	
	@DeleteMapping("/customers/delete_account/{logginKey}/{password}")
	public ResponseEntity<String> deleteCustomerByid(@PathVariable("logginKey") String key, @PathVariable("password") String password)throws CustomerException, LoginException{
		ResponseEntity<String> customerResponseEntity = new ResponseEntity<>(customerService.removeCustomer(key,password), HttpStatus.ACCEPTED);
		return customerResponseEntity;
	}
	
	@GetMapping("/customers/view_profile/{logginKey}")
	public ResponseEntity<Customer> findCustomer(@PathVariable("logginKey") String key)throws CustomerException, LoginException{
		ResponseEntity<Customer> customerResponseEntity = new ResponseEntity<>(customerService.viewCustomer(key), HttpStatus.FOUND);
		return customerResponseEntity;
	}
	
	@PutMapping("/customers/update_address/{logginKey}")
	public ResponseEntity<String> updateCustomerAddress(@PathVariable("logginKey") String key,@Valid @RequestBody Address address) throws CustomerException, LoginException {
		ResponseEntity<String> customerResponseEntity = new ResponseEntity<>(customerService.updateAddress(key,address), HttpStatus.ACCEPTED);
		return customerResponseEntity;
	}
	
	@PutMapping("/customers/update_password/{logginKey}")
	public ResponseEntity<String> updateCustomerPassword(@PathVariable("logginKey") String key, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws CustomerException, LoginException {
		ResponseEntity<String> customerResponseEntity = new ResponseEntity<>(customerService.updatepassword(key, resetPasswordDTO), HttpStatus.ACCEPTED);
		return customerResponseEntity;
	}
}
