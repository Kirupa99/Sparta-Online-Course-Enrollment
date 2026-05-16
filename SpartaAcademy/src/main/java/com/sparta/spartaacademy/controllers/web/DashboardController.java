package com.sparta.spartaacademy.controllers.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String redirectToDashboard(Model model, Authentication authentication)
    {
        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TRAINER"));
        String email =authentication.getName();
        String user = (email.split("@")[0]);
        String name = user.substring(0,1).toUpperCase() + user.substring(1);
        model.addAttribute("name",name);
        if (isTrainer)
        {
            return "trainer_dashboard";
        } else {
            return "trainee_dashboard";
        }
    }
}
