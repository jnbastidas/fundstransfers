package com.yellowpepper.fundstransfers.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRANSFER")
public class Transfer implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	private Double amount;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "currency_id")
	private Currency currency;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "origin_account")
	private Account originAccount;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "destination_account")
	private Account destinationAccount;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
}
