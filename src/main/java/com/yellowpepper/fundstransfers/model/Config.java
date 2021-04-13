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
@Table(name = "CONFIG")
public class Config implements Serializable {
	public static final String MAX_DAILY_TRANSFERS_CONFIG_CODE = "MAX_DAILY_TRANSFERS";

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String code;

	private String value;
}
