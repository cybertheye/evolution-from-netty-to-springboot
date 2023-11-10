package com.attackonarchitect.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;

/**
 * @description:
 */

@RestController
@RequestMapping("/hello")
public class HelloController {


    @RequestMapping("/test")
    public String test(){
        return "hello world!";
    }
}
