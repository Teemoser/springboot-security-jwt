package com.quick.jwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppController {

	@RequestMapping("/test")
	public String test() {
		return "hello world!";
	}
}
