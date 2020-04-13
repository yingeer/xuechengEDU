package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseMarketControllerApi;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
