package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作teachplane表
 */
@Mapper
public interface TeachplanMapper {
    public TeachplanNode selectList(String courseId);

}
