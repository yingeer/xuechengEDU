package com.xuecheng.manage_course.dao;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestAddTeachPlan {

    @Resource
    private CourseService courseService;

    @Test
    public void addTeachplanTest() {
        Teachplan teachplan = new Teachplan();
        teachplan.setCourseid("297e7c7c62b888f00162b8a7dec20011");
//        teachplan.setParentid("1");
        teachplan.setPname("3号测试用例");
        ResponseResult responseResult = null;

        responseResult = courseService.addTeachplan(teachplan);

        if (responseResult != null)
            System.out.println(responseResult.toString());
        else
            System.out.println("responseResult为空");
    }
}
