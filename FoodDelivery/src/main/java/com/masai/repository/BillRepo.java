package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.Bill;

@Repository
public interface BillRepo extends JpaRepository<Bill, Integer>{

	

}
