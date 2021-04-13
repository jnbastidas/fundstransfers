package com.yellowpepper.fundstransfers.dto;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputDTO implements Serializable {
	public static String STATUS_OK = "OK";
	public static String STATUS_ERROR = "ERROR";

	@JsonProperty("status")
	private String status;

	@JsonProperty("errors")
	private Set<String> errors;
}
