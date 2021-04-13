package com.yellowpepper.fundstransfers.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferInputDTO implements Serializable {
	@JsonProperty("amount")
	private Double amount;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("origin_account")
	private String originAccount;

	@JsonProperty("destination_account")
	private String destinationAccount;

	@JsonProperty("description")
	private String description;
}