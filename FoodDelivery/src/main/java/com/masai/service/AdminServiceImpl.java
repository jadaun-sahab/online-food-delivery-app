package com.masai.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Customer;
import com.masai.model.DeleteCustomerAccount;
import com.masai.model.Login;
import com.masai.model.LoginDTO;
import com.masai.model.ToBeDeletedCustomerAccount;
import com.masai.repository.CustomerRepo;
import com.masai.repository.DeletedCustomerAccountRepo;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private DeletedCustomerAccountRepo deletedCustomerAccountRepo;
	
	@Override
	public String deleteAccounts(Login loginDTO) throws LoginException {
		
		if(!loginDTO.getMobileNumber().equals("9999988888") || !loginDTO.getPassword().equals("9999888888")) {
			throw new LoginException("Invalid login details");
		}
		
		Integer noOfAccountsDeleted = 0;
		
		List<DeleteCustomerAccount> deletedCustomerAccounts = deletedCustomerAccountRepo.findAll();
		
		for(DeleteCustomerAccount e : deletedCustomerAccounts) {
			if(LocalDateTime.now().isAfter(e.getDeletionSheduledAt().plusMinutes(2))) {
				Customer customer = customerRepo.findById(e.getCustomerId()).get();
				customerRepo.delete(customer);
				deletedCustomerAccountRepo.delete(e);
				noOfAccountsDeleted++;
			}
		}
		
		return noOfAccountsDeleted + " Accounts deleted successfully";
	}

	@Override
	public List<Customer> showToBeDeletedAccounts(Login loginDTO) throws CustomerException, LoginException {
		
		if(!loginDTO.getMobileNumber().equals("9999988888") || !loginDTO.getPassword().equals("9999888888")) {
			throw new LoginException("Invalid login details");
		}

		List<Customer> customers = new ArrayList<>();

		List<DeleteCustomerAccount> deletedCustomerAccounts = deletedCustomerAccountRepo.findAll();
		
		for(DeleteCustomerAccount e : deletedCustomerAccounts) {
			if(LocalDateTime.now().isAfter(e.getDeletionSheduledAt().plusMinutes(2))) {

				Customer customer = customerRepo.findById(e.getCustomerId()).get();
				System.out.println(customer.getFirstName());
				customers.add(customer);
			}
		}
		
		if(customers.isEmpty()) throw new CustomerException("No customers found to be deleted");
		
		return customers;
		
	}

}
