package com.citibank.hackthon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class MainController {

    @GetMapping("/getUser")
    public String getUser(@PathVariable int userId){
        System.out.println("userIddddd"+ userId);
        return null;
    }

}
