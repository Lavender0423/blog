package com.lavender.blog.web;

import com.lavender.blog.service.BlogService;
import com.lavender.blog.service.TagService;
import com.lavender.blog.service.TypeService;
import com.lavender.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
//    return index page when request root path
    @GetMapping("/")
    public String index(@PageableDefault(size=8,sort={"updateTime"},direction= Sort.Direction.DESC)Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        // can be optimized ————— set the type_size will be return in properties file
        // ----------------------go get six type---------------------
        model.addAttribute("types",typeService.listTypeTop(6));
        // ----------------------go get ten tag---------------------
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listBlogNew(8));

        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size=8,sort={"updateTime"},direction= Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);

        return "search";
    }

    @GetMapping("/Blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));

//        System.out.println("it's work");
        return "Blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        //直接返回模板
        System.out.println("it's work");
        model.addAttribute("newblogs",blogService.listBlogNew(3));
        return "_fragments :: newblogList";
    }
}
