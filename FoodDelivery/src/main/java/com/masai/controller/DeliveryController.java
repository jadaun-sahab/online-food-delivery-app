package com.masai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.CustomerException;
import com.masai.exception.DeliveryException;
import com.masai.exception.LoginException;
import com.masai.exception.OrderDetailsException;
import com.masai.service.DeliveryService;

@RestController
@RequestMapping("/MealsOnWheels")
public class DeliveryController {

	@Autowired
	private DeliveryService deliveryService;
	
	@GetMapping("/delivery/customer/{key}/{orderId}")
	public ResponseEntity<String> orderDeliveryHandler(@PathVariable("key") String key, @PathVariable("orderId") Integer orderId) throws DeliveryException, LoginException, OrderDetailsException, CustomerException{
		
		String result = deliveryService.getOrderDetails(key, orderId);
		
		return new ResponseEntity<>(result, HttpStatus.FOUND);
	}
}
