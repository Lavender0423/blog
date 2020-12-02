package com.lavender.blog.web.admin;

import com.lavender.blog.po.Type;
import com.lavender.blog.service.TypeService;
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
public class TypeController {

    @Autowired
    private TypeService typeService;
    @GetMapping("/types")
    public String types(@PageableDefault(size = 10,sort={"id"},direction = Sort.Direction.DESC/*倒序*/) Pageable pageable,
                        Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String add(Model model){
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/type-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        //表示已经存在同名对象
        if (type1 != null){
            result.rejectValue("name","nameError","请勿添加相同分类！");
        }

        if(result.hasErrors()){
            return "admin/type-input";
        }

        Type t = typeService.saveType(type);
        if(t == null){
            attributes.addFlashAttribute("message","创建失败");
            //如果保存不成功给一个提示
//            throw
        }else{
            attributes.addFlashAttribute("message","创建成功");

            //如果保存成功也给个提示
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        //表示已经存在同名对象
        if (type1 != null){
            result.rejectValue("name","nameError","请勿添加相同分类！");
        }

        if(result.hasErrors()){
            return "admin/type-input";
        }

        Type t = typeService.updateType(id,type);
        if(t == null){
            attributes.addFlashAttribute("message","更新失败");
            //如果保存不成功给一个提示
//            throw
        }else{
            attributes.addFlashAttribute("message","更新成功");

            //如果保存成功也给个提示
        }
        return "redirect:/admin/types";
    }

//    @{/admin/types/{id}/delete(id=${type.id})}
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){


        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }

}
