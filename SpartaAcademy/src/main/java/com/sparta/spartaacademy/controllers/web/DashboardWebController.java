package com.sparta.spartaacademy.controllers.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    @GetMapping("/dashboard")
    public String redirectToDashboard(Authentication authentication) {

        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TRAINER"));

        if (isTrainer) {
            return "trainer_dashboard";
        } else {
            return "trainee_dashboard";
        }
    }
}
