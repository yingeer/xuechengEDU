package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.QueryResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

public interface CategoryControllerApi {

    @ApiOperation("查询分类")
    public CategoryNode findList();

    @ApiOperation("分页查询category")
    public QueryResult<Category> getCategoryByPage(int page, int size);
}
