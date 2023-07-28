package com.masai.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.BillException;
import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Bill;
import com.masai.model.CurrentUserSession;
import com.masai.model.Customer;
import com.masai.model.Date;
import com.masai.model.OrderDetails;
import com.masai.repository.BillRepo;
import com.masai.repository.CustomerRepo;
import com.masai.repository.OrderDetailsRepo;
import com.masai.repository.SessionRepo;

@Service
public class BillServiceImpl implements BillService{
	
	@Autowired
	private BillRepo billRepo;
	
	@Autowired
	private SessionRepo sessionRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private OrderDetailsRepo orderDetailsRepo;

	@Override
	public Bill genrateBill(OrderDetails orderDetails) throws BillException {
		Bill bill = new Bill();
		
		bill.setBillDate(LocalDateTime.now());
		bill.setOrderDetails(orderDetails);
		bill.setGrandTotal(orderDetails.getTotalAmount() + bill.getDeliveryCost());
		
		Integer totalItems = 0;
		for(ItemQuantityDTO e : orderDetails.getItems()) {
			totalItems += e.getOrderedQuantity();
		}
		bill.setTotalItems(totalItems);
		
		return billRepo.save(bill);
	}

	@Override
	public Bill viewBill(String key, Integer billId) throws BillException, CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view your bill");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		Bill bill = billRepo.findById(billId).orElseThrow(() -> new BillException("Bill not found"));
		
		if (bill.getOrderDetails().getCustomerId() != customer.getCustomerID()) throw new BillException("Bill not found");
		
		return bill;
	}

	@Override
	public List<Bill> viewBill(String key, Date dateDTO) throws BillException, CustomerException, LoginException {
		LocalDate startDate= dateDTO.getStartDate();
		LocalDate endDate= dateDTO.getEndDate();
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view bill(s)");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		List<OrderDetails> orderDetails = orderDetailsRepo.findByCustomerId(customer.getCustomerID());
		
		if(orderDetails.isEmpty()) throw new BillException("Bill(s) not found");
		
		List<OrderDetails> filteredOrders = new ArrayList<>();
		for(OrderDetails o : orderDetails) {
			LocalDate date = o.getOrderDate().toLocalDate();
			
			if(date.isEqual(endDate) || date.isEqual(startDate) || (date.isAfter(startDate) && date.isBefore(endDate))) {
				filteredOrders.add(o);
			}
		}
		if(filteredOrders.isEmpty()) throw new BillException("Bill(s) not found within these dates");
		
		List<Bill> bills = new ArrayList<>();
		for(OrderDetails o : filteredOrders) {
			bills.add(o.getBill());
		}
		
		return bills;
	}

	@Override
	public List<Bill> viewBills(String key) throws BillException, LoginException, CustomerException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view bill(s)");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		List<OrderDetails> orderDetails = orderDetailsRepo.findByCustomerId(customer.getCustomerID());
		if(orderDetails.isEmpty()) throw new BillException("Bill(s) not found");
		
		List<Bill> bills = new ArrayList<>();
		for(OrderDetails o : orderDetails) {
			bills.add(o.getBill());
		}
		
		return bills;
	}

	@Override
	public Double getTotalCost(String key, Integer billId) throws BillException, CustomerException, LoginException {
		
		CurrentUserSession currentUserSession = sessionRepo.findByUuid(key);
		if(currentUserSession == null) throw new LoginException("Please login to view total cost of your order");
		Customer customer = customerRepo.findById(currentUserSession.getId()).orElseThrow(()-> new CustomerException("Please login as Customer"));
		
		Bill bill = billRepo.findById(billId).orElseThrow(() -> new BillException("Bill not found"));
		if (bill.getOrderDetails().getCustomerId() != customer.getCustomerID()) throw new BillException("Bill not found");
		
		return bill.getGrandTotal();
	}
}
