package com.chatapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/v1/test")
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok("require authenticate");
    }

    @GetMapping("/v2/test")
    public ResponseEntity<String> test2() {
        return ResponseEntity.ok("without authenticate");
    }
}
