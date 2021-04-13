package com.yellowpepper.fundstransfers.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yellowpepper.fundstransfers.model.Account;
import com.yellowpepper.fundstransfers.model.Transfer;


public interface TransferRepository extends JpaRepository <Transfer, Long> {

	@Query("SELECT COUNT(t) FROM Transfer t WHERE t.originAccount = :originAccount AND t.date BETWEEN :from AND :to")
	Long totalAllByOriginAccountAndDateFromTo(
			final Account originAccount,
			final Date from,
			final Date to
	);
}
