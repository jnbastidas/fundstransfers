package com.yellowpepper.fundstransfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yellowpepper.fundstransfers.model.Currency;


public interface CurrencyRepository extends JpaRepository <Currency, Long> {
	Currency findFirstByCode(final String code);
}
