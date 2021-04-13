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

import com.yellowpepper.fundstransfers.dto.OutputDTO;
import com.yellowpepper.fundstransfers.dto.TransferInputDTO;
import com.yellowpepper.fundstransfers.dto.TransferOutputDTO;
import com.yellowpepper.fundstransfers.service.TransferService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private TransferService transferService;

	@PostMapping(value = "/send")
	public TransferOutputDTO send(@RequestBody final TransferInputDTO transferInput) {
		try {
			final TransferOutputDTO outputDTO = this.transferService.send(transferInput);
			outputDTO.setStatus(OutputDTO.STATUS_OK);
			outputDTO.setErrors(Collections.emptySet());
			return  outputDTO;
		} catch (final SystemException | IllegalArgumentException e) {
			final Set<String> errors = new HashSet<>(1);
			errors.add(e.getMessage());

			final TransferOutputDTO outputDTO = new TransferOutputDTO();
			outputDTO.setStatus(OutputDTO.STATUS_ERROR);
			outputDTO.setErrors(errors);
			outputDTO.setTaxCollected(0.0);

			return outputDTO;
		}
	}
}
