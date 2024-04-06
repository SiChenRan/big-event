package com.edu.cqut.service;

import com.edu.cqut.pojo.Article;
import com.edu.cqut.pojo.PageBean;

public interface ArticleService {
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, String categoryId, String state);

    Article findById(Integer id);

    void update(Article article);

    void delete(Integer id);
}
