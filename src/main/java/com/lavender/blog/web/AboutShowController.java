package com.lavender.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {
    @GetMapping("/about_me")
    public String about_me(){
        return "about_me";
    }
}
