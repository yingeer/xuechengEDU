package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_course.dao.CategoryMapper;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 查询所有category
     *
     * @return
     */
    public CategoryNode findList() {

        return categoryMapper.getCategoryNode();
    }

    /**
     * 分页查询category
     *
     * @return
     */
    public QueryResult<Category> getCategoryByPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Category> categoryPage = categoryMapper.getCategoryByPage();
        List<Category> result = categoryPage.getResult();
        QueryResult<Category> queryResult = new QueryResult<>();
        queryResult.setList(result);
        queryResult.setTotal(result.size());
        return queryResult;
    }
}
