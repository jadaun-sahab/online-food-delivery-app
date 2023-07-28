package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.exception.RestaurantException;
import com.masai.model.Restaurant;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Integer>{

	public Restaurant findByRestaurantName(String restaurantName);

	public Restaurant findByMobileNumber(String mobileNumer) throws RestaurantException;

}
