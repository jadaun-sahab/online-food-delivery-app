package com.masai.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.CurrentUserSession;
import com.masai.model.Customer;
import com.masai.model.ToBeDeletedCustomerAccount;
import com.masai.model.LoginDTO;
import com.masai.repository.CustomerRepo;
import com.masai.repository.DeletedCustomerAccountRepo;
import com.masai.repository.SessionRepo;

import net.bytebuddy.utility.RandomString;


@Service
public class CustomerLoginServiceImpl implements CustomerLoginService{

	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private DeletedCustomerAccountRepo deletedCustomerAccountRepo;
	
	@Override
	public CurrentUserSession login(LoginDTO dto) throws LoginException, CustomerException {
		
		Customer customer = customerRepo.findByMobileNumber(dto.getMobileNumber());
		
		if(customer == null) throw new CustomerException("Please enter a valid mobile number");
		
		Optional<CurrentUserSession> currentUserSession = sessionRepo.findById(customer.getCustomerID());
		
		if(currentUserSession.isPresent()) throw new LoginException("User already logged in with this mobile number");
		
		List<CurrentUserSession> list = sessionRepo.findAll();
		for(CurrentUserSession c : list) {
			sessionRepo.delete(c);
		}
		
		if(!customer.getPassword().equals(dto.getPassword())) throw new LoginException("Incorrect password");

		String key = RandomString.make(6);

		CurrentUserSession genrateSession = new CurrentUserSession(customer.getCustomerID(), key, LocalDateTime.now());
		sessionRepo.save(genrateSession);
		
		Optional<ToBeDeletedCustomerAccount> account = deletedCustomerAccountRepo.findById(customer.getCustomerID());
		if(account.isPresent()) 
			deletedCustomerAccountRepo.delete(account.get());
		
		return genrateSession;
	}

	@Override
	public String logout(String key) throws LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		
		if(currentUserSession == null) throw new LoginException("Invalid User key");
		
		sessionRepo.delete(currentUserSession);
		
		return "Logged out successfully";
	}

}
