package com.adaptavist.example.demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping
    public String defaultRoute() {
        return "You found an application!\n\nIsn't that great!\n";
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

}
