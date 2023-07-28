package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.masai.model.FoodCart;


public interface FoodCartRepo extends JpaRepository<FoodCart, Integer>{

	


}
