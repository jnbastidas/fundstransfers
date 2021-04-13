package com.yellowpepper.fundstransfers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PingPongController {

	@GetMapping(value = "/ping")
	public String ping() {
		return "pong";
	}
}