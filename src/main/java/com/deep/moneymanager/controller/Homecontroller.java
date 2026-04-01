package com.deep.moneymanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"statues","/health"})
public class Homecontroller {

    @GetMapping
    public String heithcheck(){
        return "Application is runing";
    }
}
