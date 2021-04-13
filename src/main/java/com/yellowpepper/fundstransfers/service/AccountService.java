package com.yellowpepper.fundstransfers.service;

import java.util.Optional;

import com.yellowpepper.fundstransfers.dto.AccountDTO;
import com.yellowpepper.fundstransfers.model.Account;


public interface AccountService {
	Optional<Account> findByNumber(final String number);

	void save(final Account account);

	void save(final AccountDTO accountIn);
}
