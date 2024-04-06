package com.edu.cqut.controller;

import com.edu.cqut.pojo.Category;
import com.edu.cqut.pojo.Result;
import com.edu.cqut.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
        categoryService.add(category);
        return Result.success();
    }

    @GetMapping("/getList")
    public Result<List<Category>> get() {
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    @GetMapping("/detail")
    public Result<Category> getDetail(Integer id) {
        return Result.success(categoryService.findById(id));
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated(Category.Update.class) Category category) {
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id){
        categoryService.deleteById(id);
        return Result.success();
    }
}

