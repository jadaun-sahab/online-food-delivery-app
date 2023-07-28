package com.masai.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
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
public class FoodCart {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "cartGenerator")
	@SequenceGenerator(name="cartGenerator", sequenceName = "cartGen", allocationSize = 1, initialValue = 100001)
	private Integer cartId;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "foodCart")
	private Customer customer;
	
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<Integer, Integer> items = new HashMap<>();

}
