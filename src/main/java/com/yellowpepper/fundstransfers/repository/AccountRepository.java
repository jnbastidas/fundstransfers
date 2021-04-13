package com.yellowpepper.fundstransfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yellowpepper.fundstransfers.model.Account;


public interface AccountRepository extends JpaRepository <Account, Long> {
	@Query("SELECT a FROM Account a WHERE a.number = :number")
	Account findByNumber(final String number);
}
