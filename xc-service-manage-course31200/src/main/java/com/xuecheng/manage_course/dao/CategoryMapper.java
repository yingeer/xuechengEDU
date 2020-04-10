package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    /**
     * 查询所有的category
     * @return 返回categoryNode类型
     */
    public CategoryNode getCategoryNode();
}
