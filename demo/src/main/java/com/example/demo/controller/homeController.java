package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class homeController {
    
    // localhost:8080/home
    @GetMapping("/home")
    public String mostrarHome() {
        return "home";
    }

    // localhost:8080/
    @GetMapping("/")
    public String redirigirHomeDesdeRaiz() {
        return "redirect:/home";
    }
    
}
