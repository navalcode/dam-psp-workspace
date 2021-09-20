package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public String verControllador (){
        return "index";
    }
}
