package com.yellowpepper.fundstransfers.service;

import java.util.Optional;

import com.yellowpepper.fundstransfers.model.Config;


public interface ConfigService {
	Optional<Config> findByCode(final String code);

	void save(final Config config);
}
