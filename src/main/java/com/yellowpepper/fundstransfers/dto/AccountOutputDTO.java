package com.yellowpepper.fundstransfers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOutputDTO extends OutputDTO {
	@JsonProperty("account_balance")
	private Double balance;
}
