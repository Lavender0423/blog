package com.lavender.blog.web;

import com.lavender.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AchiveShowController {

    @Autowired
    private BlogService blogService;


    @GetMapping("/archives")
    public String arhives(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlog());
//        System.out.println(blogService.archiveBlog().keySet());
        model.addAttribute("blogCount",blogService.countBlog());
        return "archives";
    }
}
