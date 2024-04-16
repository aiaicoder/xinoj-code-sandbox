package com.xin.ojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class mainController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
