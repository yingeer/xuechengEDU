package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

public interface CoursePicControllerApi {

    /**
     * 保存图片信息到mysql course_pic表
     * @param courseId
     * @param pic
     * @return
     */
    @ApiOperation("保存图片信息到course_pic")
    public ResponseResult addCoursePic(String courseId, String pic);

    /**
     * 获取course_pic课程图片信息
     *
     * @param courseId
     * @return
     */
    @ApiOperation("获取course_pic课程图片信息")
    public CoursePic findCoursePic(String courseId);

    /**
     * 删除course_pic课程图片信息
     * @param courseId
     * @return
     */
    @ApiOperation("删除course_pic课程图片")
    public ResponseResult deleteCoursePic(String courseId);
}
