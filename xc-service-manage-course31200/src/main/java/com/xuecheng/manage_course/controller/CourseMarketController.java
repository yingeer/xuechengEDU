package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseMarketControllerApi;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coursemarket")
public class CourseMarketController implements CourseMarketControllerApi {

    @Resource
    private CourseMarketService courseMarketService;

    /**
     * 根据id查询courseMarket对象
     * @param courseMarketId
     * @return
     */
    @Override
    @GetMapping("/list/{courseMarketId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseMarketId") String courseMarketId) {
        return courseMarketService.getCourseMarketById(courseMarketId);
    }

    /**
     * 更新courseMarket
     * @param courseMarketId
     * @param postCourseMarket
     * @return
     */
    @Override
    @PutMapping("/update/{courseMarketId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseMarketId") String courseMarketId, @RequestBody CourseMarket postCourseMarket) {
        return courseMarketService.updateCourseMarket(courseMarketId, postCourseMarket);
    }

}
