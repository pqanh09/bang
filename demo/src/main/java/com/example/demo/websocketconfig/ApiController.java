package com.example.demo.websocketconfig;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	@GetMapping("/api")
    public String hello() {
            return "Hello Spring Boot!";
    }
}
