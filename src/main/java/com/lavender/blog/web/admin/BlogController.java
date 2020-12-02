package com.lavender.blog.web.admin;

import com.lavender.blog.po.Blog;
import com.lavender.blog.po.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT = "admin/blogs_input";
    private static final String LIST = "admin/blog_management";
    private static final String REDIRECT_LIST = "redirect:/admin/blog_management";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blog_management")
    public String list(@PageableDefault(size=10,sort = {"updateTime"},direction = Sort.Direction.DESC)
                               Pageable pageable, BlogQuery blogQuery, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return LIST;
    }

    @PostMapping("/blog_management/search")
    public String search_list(@PageableDefault(size=10,sort = {"updateTime"},direction = Sort.Direction.DESC)
                               Pageable pageable, BlogQuery blogQuery, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return "admin/blog_management :: blogList";
    }


    @GetMapping("/blog_management/input")
    public String input(Model model){
        saveTypeAndTag(model);
//        System.out.println("it's work!!!");
        model.addAttribute("blog",new Blog());

        return INPUT;
    }


    private void saveTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blog_management/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        saveTypeAndTag(model);
        /*get saved blog by its id*/
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);

        return INPUT;
    }


    @PostMapping("/blog_management")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
//        System.out.println(blog.toString());
        Blog b = blogService.saveBlog(blog);

        if(b==null){
            attributes.addFlashAttribute("message","操作失败");

        }else{
            attributes.addFlashAttribute("message","操作成功");
        }

        return REDIRECT_LIST;
    }

    @GetMapping("/blog_management/{id}/delete")
    public String delete( RedirectAttributes attributes,@PathVariable Long id){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }

}
