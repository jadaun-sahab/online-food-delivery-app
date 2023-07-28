package com.masai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.BillException;
import com.masai.exception.CustomerException;
import com.masai.exception.LoginException;
import com.masai.model.Bill;
import com.masai.model.Date;
import com.masai.model.DateDTO;
import com.masai.service.BillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/MealsOnWheels")
public class BillController {
	@Autowired
	private BillService billService;
	
	@GetMapping("/bills/customer/view_bill/{loginKey}/{billId}")
	public ResponseEntity<Bill> viewBillHandler(@PathVariable("loginKey") String key, @PathVariable("billId") Integer billId) throws BillException, CustomerException, LoginException{
		Bill bill= billService.viewBill(key, billId);
		return new ResponseEntity<Bill>(bill, HttpStatus.ACCEPTED);
	}
	
	
	@PostMapping("/bills/customer/view_bills_date_filtered/{loginKey}")
	public ResponseEntity<List<Bill>> viewBillBetweenDateHandler(@PathVariable("loginKey") String key, @Valid @RequestBody Date dateDTO) throws BillException, CustomerException, LoginException{
		List<Bill> bills= billService.viewBill(key, dateDTO);
		return new ResponseEntity<List<Bill>>(bills, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/bills/customer/view_all_bills/{loginKey}")
	public ResponseEntity<List<Bill>> viewAllBillsHandler(@PathVariable("loginKey") String key) throws BillException, LoginException, CustomerException{
		List<Bill> bills= billService.viewBills(key);
		return new ResponseEntity<List<Bill>>(bills, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/bills/customer/get_total_cost_of_bill/{loginKey}/{billId}")
	public ResponseEntity<Double> getTotalCostOfBillHandler(@PathVariable("loginKey") String key, @PathVariable("billId") Integer billId) throws BillException, CustomerException, LoginException{
		Double totalCost= billService.getTotalCost(key, billId);
		return new ResponseEntity<Double>(totalCost, HttpStatus.ACCEPTED);
	}
}
