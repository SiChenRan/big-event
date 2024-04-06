package com.edu.cqut.controller;

import com.edu.cqut.pojo.Article;
import com.edu.cqut.pojo.PageBean;
import com.edu.cqut.pojo.Result;
import com.edu.cqut.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        return Result.success();
    }

    @GetMapping("/queryList")
    public Result<PageBean<Article>> queryList(Integer pageNum, Integer pageSize,
                                               @RequestParam(required = false) String categoryId,
                                               @RequestParam(required = false) String state) {
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result<Article> detail(Integer id) {
        return Result.success(articleService.findById(id));
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Article article) {
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id){
        articleService.delete(id);
        return Result.success();
    }

}
