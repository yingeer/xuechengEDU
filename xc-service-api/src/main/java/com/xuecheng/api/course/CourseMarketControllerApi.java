package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;

public interface CourseMarketControllerApi {

    @ApiOperation("通过Id查询courseMarket对象")
    public CourseMarket getCourseMarketById(String id);

    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);
}
