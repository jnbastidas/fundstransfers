package com.yellowpepper.fundstransfers.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yellowpepper.fundstransfers.model.Config;
import com.yellowpepper.fundstransfers.repository.ConfigRepository;
import com.yellowpepper.fundstransfers.service.ConfigService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	private ConfigRepository configRepository;

	@Override
	public Optional<Config> findByCode(final String code) {
		if (code == null) {
			throw new IllegalArgumentException("Config code cannot be null.");
		}

		return Optional.ofNullable(configRepository.findByCode(code));
	}

	@Override
	public void save(final Config config) {
		if (config == null) {
			throw new IllegalArgumentException("Config cannot be null.");
		}

		this.configRepository.save(config);
	}
}
