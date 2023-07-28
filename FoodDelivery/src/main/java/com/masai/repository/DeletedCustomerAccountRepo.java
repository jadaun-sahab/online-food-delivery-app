package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.model.ToBeDeletedCustomerAccount;

public interface DeletedCustomerAccountRepo extends JpaRepository<DeleteCustomerAccount, Integer>{

}
