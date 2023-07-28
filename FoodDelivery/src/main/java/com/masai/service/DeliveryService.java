package com.masai.service;

import com.masai.exception.CustomerException;
import com.masai.exception.DeliveryException;
import com.masai.exception.LoginException;
import com.masai.exception.OrderDetailsException;

public interface DeliveryService {

	public String getOrderDetails(String key, Integer orderId) throws DeliveryException, LoginException, OrderDetailsException, CustomerException;
	
}
