package com.conghoan.sinhviencntt.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/admin/dashboard";
    }
}
