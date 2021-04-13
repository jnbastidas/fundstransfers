package com.yellowpepper.fundstransfers.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable {
	public static final String CODE_CURRENCY_USD = "USD";
	public static final String CODE_CURRENCY_CAD = "CAD";
	public static final String CODE_BASE_CURRENCY = CODE_CURRENCY_USD;

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String code;
}
