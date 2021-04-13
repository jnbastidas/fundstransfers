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
public class AccountDTO implements Serializable {
	@JsonProperty("number")
	private String number;

	@JsonProperty("balance")
	private Double balance;

	@JsonProperty("currency")
	private String currency;
}
