package com.masai.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bill {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "billGenerator")
	@SequenceGenerator(name= "billGenerator", sequenceName = "billGen", allocationSize = 1, initialValue = 10001)
	private Integer billId;
	
	private LocalDateTime billDate;
	
	@OneToOne(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
	private OrderDetails orderDetails;
	
	private Integer totalItems;
	
	private final Integer deliveryCost = 50;
	
	private Double grandTotal;
}
