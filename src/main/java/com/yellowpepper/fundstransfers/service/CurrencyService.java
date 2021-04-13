package com.yellowpepper.fundstransfers.service;

import java.util.Map;
import java.util.Optional;

import javax.transaction.SystemException;

import com.yellowpepper.fundstransfers.dto.CurrencyDTO;
import com.yellowpepper.fundstransfers.model.Currency;


public interface CurrencyService {
	boolean isSupported(final String currencyCode);

	Optional<Currency> getByCode(final String currencyCode);

	void save(final Currency currency);

	void save(final CurrencyDTO currency) throws SystemException;

	Map<String, Double> getExchangeRates() throws SystemException;
}
