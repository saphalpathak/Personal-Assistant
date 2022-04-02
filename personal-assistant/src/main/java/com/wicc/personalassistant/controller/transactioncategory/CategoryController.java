package com.wicc.personalassistant.controller.transactioncategory;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transactioncategory.CategoryDto;
import com.wicc.personalassistant.service.transactioncategory.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/transaction-category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/home")
    public String getHome(Model model){
        List<CategoryDto> all = categoryService.findAll();
        model.addAttribute("data",all);
        return "transactioncategory/category_landing";
    }

    @GetMapping("/add")
    public String addCategory(Model model){
        model.addAttribute("categoryDto",new CategoryDto());
        return "transactioncategory/add_category";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("message","Category creation failed!!");
            model.addAttribute("categoryDto",categoryDto);
            return "transactioncategory/add_category";
        }
        ResponseDto save = categoryService.save(categoryDto);
        redirectAttributes.addFlashAttribute("message",save.getMessage());
        return "redirect:/transaction-category/add";
    }

    //delete category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Task deleted");
        return "redirect:/transaction-category/home";
    }

}
