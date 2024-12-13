package com.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class TestAuthController {

    @GetMapping("/hola")
    public String hola(){
        return "holo como ta";
    }

    @GetMapping("/holaSeguridad")
    public String holaSeguridad(){
        return "holo como ta, esta protejido";
    }
}
