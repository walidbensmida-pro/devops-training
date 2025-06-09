package com.harington.devops_training.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class DemoController {

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/wiki-ai")
    public String wikiAi(Model model) {
        return "wiki-ai";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    @GetMapping("/lab")
    public String lab(Model model) {
        return "lab";
    }
}