package com.masai.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Address;
import com.masai.model.CurrentUserSession;
import com.masai.model.Customer;
import com.masai.model.ResetPassword;
import com.masai.model.ResetPasswordDTO;
import com.masai.model.ToBeDeletedCustomerAccount;
import com.masai.repository.CustomerRepo;
import com.masai.repository.DeletedCustomerAccountRepo;
import com.masai.repository.SessionRepo;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private CustomerLoginService customerLoginService;
	
	@Autowired
	private DeletedCustomerAccountRepo deletedCustomerAccountRepo;

	@Override
	public Customer addCustomer(Customer customer) throws CustomerException {
		
		List<CurrentUserSession> list = sessionRepo.findAll();
		for(CurrentUserSession c : list) {
			sessionRepo.delete(c);
		}
		
		Customer customerExist = customerRepo.findByMobileNumber(customer.getMobileNumber());
		if(customerExist != null) throw new CustomerException("Customer already registered with this mobile number");
		
		return customerRepo.save(customer);
	}

	@Override
	public Customer updateCustomer(String key, Customer customer) throws CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to update your details");
		Customer existingCustomer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		if(customer.getAddress() != null)
			existingCustomer.setAddress(customer.getAddress());
		if(customer.getAge() != null)
			existingCustomer.setAge(customer.getAge());
		if(customer.getGender() != null)
			existingCustomer.setGender(customer.getGender());
		if(customer.getLastName() != null)
			existingCustomer.setLastName(customer.getLastName());
		if(customer.getFirstName() != null)
			existingCustomer.setFirstName(customer.getFirstName());
		if(customer.getEmail() != null)
			existingCustomer.setEmail(customer.getEmail());
		existingCustomer.setMobileNumber(customer.getMobileNumber());
		
		return customerRepo.save(existingCustomer);

	}

	@Override
	public String removeCustomer(String key, String password) throws CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to remove your account");
		Customer existingCustomer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		if (!existingCustomer.getPassword().equals(password)) throw new CustomerException("Enter valid password");
		
		ToBeDeletedCustomerAccount account = new ToBeDeletedCustomerAccount(existingCustomer.getCustomerID(), LocalDateTime.now());
		deletedCustomerAccountRepo.save(account);
		
		customerLoginService.logout(key);
		
		return "Logged out successfully, your account will be deleted after 24 hours, you can login again within 24 hours to avoid it";
		
	}

	@Override
	public Customer viewCustomer(String key) throws CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view your details");
		Customer existingCustomer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		return existingCustomer;
	
	}

	@Override
	public String updateAddress(String key, Address address) throws CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to update your address");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));

		customer.setAddress(address);
		customerRepo.save(customer);
		
		return "Address updated sucssesfully";
	}



	@Override
	public String updatepassword(String key, ResetPassword resetPasswordDTO) throws CustomerException, LoginException {
		String currentPassword= resetPasswordDTO.getCurrentPassword();
		
		String newPassword= resetPasswordDTO.getNewPassword();
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to update your password");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		if(!customer.getPassword().equals(currentPassword)) throw new CustomerException("Enter vaild current password");
		
		customer.setPassword(newPassword);
		
		customerRepo.save(customer);
		
		return "Password updated sucssesfully";
	}

}
