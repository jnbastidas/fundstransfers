package com.yellowpepper.fundstransfers.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yellowpepper.fundstransfers.dto.AccountDTO;
import com.yellowpepper.fundstransfers.model.Account;
import com.yellowpepper.fundstransfers.model.Currency;
import com.yellowpepper.fundstransfers.repository.AccountRepository;
import com.yellowpepper.fundstransfers.service.AccountService;
import com.yellowpepper.fundstransfers.service.CurrencyService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CurrencyService currencyService;

	@Override
	public Optional<Account> findByNumber(final String number) {
		if (number == null) {
			throw new IllegalArgumentException("Account number cannot be null.");
		}

		return Optional.ofNullable(this.accountRepository.findByNumber(number));
	}

	@Override
	public void save(final Account account) {
		if (account == null) {
			throw new IllegalArgumentException("Account cannot be mull.");
		}

		this.accountRepository.save(account);
	}

	@Override
	@Transactional
	public void save(final AccountDTO accountIn) {
		if (accountIn == null) {
			throw new IllegalArgumentException("Account cannot be mull.");
		}

		if (accountIn.getBalance() != null && accountIn.getBalance() < 0) {
			throw new IllegalArgumentException("Account balance cannot be negative.");
		}

		Account account = null;
		final Optional<Account> optAccount = this.findByNumber(accountIn.getNumber());
		account = optAccount.orElseGet(Account::new);

		account.setNumber(accountIn.getNumber());
		account.setBalance(accountIn.getBalance() == null ? 0.0 : accountIn.getBalance());

	 	final Optional<Currency> optCurrency = this.currencyService.getByCode(accountIn.getCurrency());
		if (optCurrency.isEmpty()) {
			throw new IllegalArgumentException("Currency " + accountIn.getCurrency() + " isn't suported.");
		}
		account.setCurrency(optCurrency.get());

		this.accountRepository.save(account);
	}
}
