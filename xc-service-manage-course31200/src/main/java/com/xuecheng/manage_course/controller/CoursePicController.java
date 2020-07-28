package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePicControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CoursePicService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/course")
public class CoursePicController implements CoursePicControllerApi {

    @Resource
    CoursePicService coursePicService;

    /**
     * 下载图片保存图片信息到mysql course_pic表
     * @param courseId
     * @param pic
     * @return
     */
    @PostMapping("/courseid/add")
    @Override
    public ResponseResult addCoursePic(@RequestParam(value = "courseid") String courseId, @RequestParam("pic") String pic) {
        return coursePicService.saveCoursePic(courseId, pic);
    }

    /**
     * 获取course_pic课程图片信息
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return coursePicService.findCoursepic(courseId);
    }

    /**
     * 删除mysql xc_course.course_pic课程图片信息
     * @param courseId
     * @return
     */
    @Override
    @DeleteMapping("/coursepic/delete/{courseId}")
    public ResponseResult deleteCoursePic(@PathVariable("courseId") String courseId) {
        return coursePicService.deleteCoursePic(courseId);
    }
}
