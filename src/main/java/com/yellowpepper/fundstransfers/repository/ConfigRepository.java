package com.yellowpepper.fundstransfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yellowpepper.fundstransfers.model.Config;


public interface ConfigRepository extends JpaRepository <Config, Long> {
	Config findByCode(final String code);
}
