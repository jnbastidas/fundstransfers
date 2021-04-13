package com.yellowpepper.fundstransfers.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ACCOUNT")
public class Account implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String number;

	private Double balance;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "currency_id")
	private Currency currency;
}
