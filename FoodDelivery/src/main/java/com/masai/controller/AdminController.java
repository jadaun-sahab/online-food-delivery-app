package com.masai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Customer;
import com.masai.model.LoginDTO;
import com.masai.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Foodapps")
public class AdminController {
	
	@Autowired
	private AdminService adminService;

	@DeleteMapping("/admin/customers/delete")
	public ResponseEntity<String> deleteCustomersHandler(@Valid @RequestBody LoginDTO loginDTO) throws LoginException{
		String result = adminService.deleteAccounts(loginDTO);
		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/admin/view_customers_to_be_deleted")
	public ResponseEntity<List<Customer>> showToBeDeletedAccountsHandler(@Valid @RequestBody LoginDTO loginDTO) throws CustomerException, LoginException{
		List<Customer> customers = adminService.showToBeDeletedAccounts(loginDTO);
		return new ResponseEntity<>(customers, HttpStatus.FOUND);
		
	}
	
}
