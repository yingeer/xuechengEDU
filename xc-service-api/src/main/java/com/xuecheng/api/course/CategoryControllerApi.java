package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.ApiOperation;

public interface CategoryControllerApi {

    @ApiOperation("查询分类")
    public CategoryNode findList();
}
