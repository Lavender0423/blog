package com.lavender.blog.web.admin;

import com.lavender.blog.po.Tag;
import com.lavender.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;
    @GetMapping("/tags")
    public String types(@PageableDefault(size = 10,sort={"id"},direction = Sort.Direction.DESC/*倒序*/) Pageable pageable,
                        Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String add(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tag-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tag-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        //表示已经存在同名对象
        if (tag1 != null){
            result.rejectValue("name","nameError","请勿添加相同标签！");
        }

        if(result.hasErrors()){
            return "admin/tag-input";
        }

        Tag t = tagService.saveTag(tag);
        if(t == null){
            attributes.addFlashAttribute("message","创建失败");
            //如果保存不成功给一个提示
//            throw
        }else{
            attributes.addFlashAttribute("message","创建成功");

            //如果保存成功也给个提示
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        //表示已经存在同名对象
        if (tag1 != null){
            result.rejectValue("name","nameError","请勿添加相同分类！");
        }

        if(result.hasErrors()){
            return "admin/tag-input";
        }

        Tag t = tagService.updateTag(id,tag);
        if(t == null){
            attributes.addFlashAttribute("message","更新失败");
            //如果保存不成功给一个提示
//            throw
        }else{
            attributes.addFlashAttribute("message","更新成功");

            //如果保存成功也给个提示
        }
        return "redirect:/admin/tags";
    }

//    @{/admin/tags/{id}/delete(id=${tag.id})}
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){


        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }

}
