package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseMarket;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;

public interface CourseMarketControllerApi {

    @ApiOperation("通过Id查询courseMarket对象")
    public CourseMarket getCourseMarketById(String id);
}
