package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {

    @Resource
    private CategoryService categoryService;

    /**
     * 展示所有的category
     * @return
     */
    @Override
    @GetMapping("/list")
    public CategoryNode findList() {
        return categoryService.findList();
    }

    /**
     * 分页查询category
     * @return
     */
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResult<Category> getCategoryByPage(@PathVariable(value = "page") int page,
                                                   @PathVariable("size") int size) {
        return categoryService.getCategoryByPage(page, size);
    }
}
