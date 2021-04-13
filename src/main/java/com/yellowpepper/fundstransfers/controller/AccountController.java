package com.yellowpepper.fundstransfers.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yellowpepper.fundstransfers.dto.AccountDTO;
import com.yellowpepper.fundstransfers.dto.AccountOutputDTO;
import com.yellowpepper.fundstransfers.dto.OutputDTO;
import com.yellowpepper.fundstransfers.model.Account;
import com.yellowpepper.fundstransfers.service.AccountService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@PostMapping(value = "/save")
	public OutputDTO send(@RequestBody final AccountDTO account) {
		final OutputDTO outputDTO = new OutputDTO();
		try {
			this.accountService.save(account);
			outputDTO.setStatus(OutputDTO.STATUS_OK);
			outputDTO.setErrors(Collections.emptySet());
			return outputDTO;
		} catch (final IllegalArgumentException e) {
			final Set<String> errors = new HashSet<>(1);
			errors.add(e.getMessage());

			outputDTO.setStatus(OutputDTO.STATUS_ERROR);
			outputDTO.setErrors(errors);

			return outputDTO;
		}
	}

	@GetMapping(value = "/{number}")
	public AccountOutputDTO get(@PathVariable final String number) {
		final AccountOutputDTO outputDTO = new AccountOutputDTO();
		final Set<String> errors = new HashSet<>(1);
		try {
			final Optional<Account> optAccount = this.accountService.findByNumber(number);
			if (optAccount.isEmpty()) {
				throw new IllegalArgumentException("Account does not exist");
			} else {
				outputDTO.setStatus(OutputDTO.STATUS_OK);
				outputDTO.setErrors(Collections.emptySet());
				outputDTO.setBalance(optAccount.get().getBalance());
			}
		} catch (final IllegalArgumentException e) {
			errors.add(e.getMessage());
			outputDTO.setStatus(OutputDTO.STATUS_ERROR);
			outputDTO.setErrors(errors);
		}
		return outputDTO;
	}
}
