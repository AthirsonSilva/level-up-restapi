package com.api.nextspring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	@GetMapping
	public String hello() {
		return "Hello World. This is a GET request";
	}

	@PostMapping
	public String helloPost() {
		return "Hello World. This is a POST request";
	}
}
