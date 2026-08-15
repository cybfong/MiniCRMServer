package com.oji.mini_crm_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/greet")
    public String greet() {
        return "Hello from Mini CRM Server";
    }
}
