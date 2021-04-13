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
public class TransferOutputDTO extends OutputDTO {
	@JsonProperty("tax_collected")
	private Double taxCollected;

	@JsonProperty("CAD")
	private Double cad;
}
