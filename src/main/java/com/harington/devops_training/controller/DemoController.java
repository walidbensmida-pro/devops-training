package com.harington.devops_training.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class DemoController {

    @Value("${db.password:defaultPwd}")
    private String dbPassword;

    @GetMapping("/")
    public String home() {
        return "Application OK";  // Réponse directe 200
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Vault DB Password: " + dbPassword;
    }

    @GetMapping("/private")
    @PreAuthorize("hasRole('ADMIN')")
    public String privateEndpoint() {
        return "Ceci est privé et réservé aux ADMIN !";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String userEndpoint() {
        return "Bienvenue USER ou ADMIN !";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Bienvenue ADMIN !";
    }
}