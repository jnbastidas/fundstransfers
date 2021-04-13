package com.yellowpepper.fundstransfers.service.impl;

import java.util.Map;
import java.util.Optional;

import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowpepper.fundstransfers.dto.CurrencyDTO;
import com.yellowpepper.fundstransfers.model.Currency;
import com.yellowpepper.fundstransfers.repository.CurrencyRepository;
import com.yellowpepper.fundstransfers.service.CurrencyService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

	@Value("${exchangerates.url}")
	private String exchangeratesUrl;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public boolean isSupported(final String currencyCode) {
		if (currencyCode == null || "".equals(currencyCode)) {
			return false;
		}
		return this.currencyRepository.findFirstByCode(currencyCode) != null;
	}

	@Override
	public Optional<Currency> getByCode(final String currencyCode) {
		if (currencyCode == null || "".equals(currencyCode)) {
			throw new IllegalArgumentException("Currency code cannot be null.");
		}
		return Optional.ofNullable(this.currencyRepository.findFirstByCode(currencyCode));
	}

	@Override
	public void save(final Currency currency) {
		if (currency == null) {
			throw new IllegalArgumentException("Currency cannot be null.");
		}

		this.currencyRepository.save(currency);
	}

	@Override
	public void save(final CurrencyDTO currencyIn) throws SystemException {
		if (currencyIn == null) {
			throw new IllegalArgumentException("Currency cannot be null.");
		}

		Currency currency = null;
		final Optional<Currency> optCurrency = this.getByCode(currencyIn.getCode());
		currency = optCurrency.orElseGet(Currency::new);
		currency.setCode(currencyIn.getCode());

		if (!this.getExchangeRates().containsKey(currencyIn.getCode())) {
			throw new IllegalArgumentException("Currency cannot be suppored.");
		}

		this.currencyRepository.save(currency);
	}

	public Map<String, Double> getExchangeRates() throws SystemException {
		try {
			final RestTemplate restTemplate = new RestTemplate();

			final String fullUrl = this.exchangeratesUrl + Currency.CODE_BASE_CURRENCY;
			final ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode root = mapper.readTree(response.getBody());
			final JsonNode rates = root.path("rates");
			return mapper.convertValue(rates, new TypeReference<Map<String, Double>>() {});
		} catch (final JsonProcessingException e) {
			log.error("Error getting exchange rates from http api: {}", e.getMessage(), e);
			throw new SystemException("Error getting exchange rates from http api.");
		}
	}
}
