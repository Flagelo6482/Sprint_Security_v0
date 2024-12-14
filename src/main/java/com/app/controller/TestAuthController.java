package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
//@PreAuthorize("denyAll()")
public class TestAuthController {

    @GetMapping("/hola")
    public String hello(){
        return "holaaaaaaaaa";
    }

    @GetMapping("/get")
//    @PreAuthorize("hasAuthority('READ')")
    public String helloGet(){
        return "hola, con Get";
    }

    @PatchMapping("/patch")
    public String helloPatch(){
        return "hola, con PATCH";
    }
}
