package com.yellowpepper.fundstransfers.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yellowpepper.fundstransfers.dto.CurrencyDTO;
import com.yellowpepper.fundstransfers.dto.OutputDTO;
import com.yellowpepper.fundstransfers.service.CurrencyService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/currency")
public class CurrencyController {
	@Autowired
	private CurrencyService currencyService;

	@PostMapping(value = "/save")
	public OutputDTO send(@RequestBody final CurrencyDTO currency) {
		final OutputDTO outputDTO = new OutputDTO();
		try {
			this.currencyService.save(currency);
			outputDTO.setStatus(OutputDTO.STATUS_OK);
			outputDTO.setErrors(Collections.emptySet());
			return outputDTO;
		} catch (final SystemException | IllegalArgumentException e) {
			final Set<String> errors = new HashSet<>(1);
			errors.add(e.getMessage());

			outputDTO.setStatus(OutputDTO.STATUS_ERROR);
			outputDTO.setErrors(errors);

			return outputDTO;
		}
	}
}
