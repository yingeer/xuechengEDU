package com.xuecheng.manage_course.controller;

import com.sun.istack.internal.NotNull;
import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Null;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Resource
    private CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {

        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /**
     * 分页查询课程页面
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    @Override
    public QueryResult<CourseInfo> findCourseList(@PathVariable("page") int page, @PathVariable("size") int size, @RequestBody(required = false) CourseListRequest courseListRequest) {
        QueryResult<CourseInfo> queryResult = courseService.findCourseList(page, size, courseListRequest);
        return queryResult;
    }

    /**
     * 根据courseId 获取CourseBase对象
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    @Override
    @GetMapping("/list/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException {
        return courseService.getCourseBaseById(courseId);
    }

    /**
     * 更改id对应的courseBase对象
     * @param courseId
     * @param userCourseBase
     * @return
     */
    @Override
    @PutMapping("/update/{courseId}")
    public ResponseResult updateCourseBase(@PathVariable("courseId") String courseId,
                                           @RequestBody CourseBase userCourseBase) {
        return courseService.updateCourseBase(courseId, userCourseBase);
    }

}
