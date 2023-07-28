package com.masai.service;

import java.util.List;

import com.masai.exception.BillException;
import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Bill;
import com.masai.model.Date;
import com.masai.model.OrderDetails;



public interface BillService {
	
	public Bill genrateBill(OrderDetails orderDetails) throws BillException;
	
	public Bill viewBill(String key, Integer billId) throws BillException, CustomerException, LoginException;
	
	public List<Bill> viewBill(String key, Date dateDTO) throws BillException, CustomerException, LoginException;
	
	public List<Bill> viewBills(String key) throws BillException, LoginException, CustomerException;
	
	public Double getTotalCost(String key, Integer billId) throws BillException, CustomerException, LoginException;
}
