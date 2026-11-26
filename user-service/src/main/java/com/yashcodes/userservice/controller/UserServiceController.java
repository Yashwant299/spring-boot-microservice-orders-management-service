package com.yashcodes.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceController {

    @GetMapping("/api/users/health")
    public String Healthcheck(){
        return "Application is up and running";
    }
}
