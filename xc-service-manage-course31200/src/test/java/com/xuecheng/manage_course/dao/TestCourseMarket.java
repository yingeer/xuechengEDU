package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCourseMarket {

    @Resource
    private CourseMarketService courseMarketService;

    @Test
    public void insertCourseMarketTest() {
        CourseMarket courseMarket = new CourseMarket().setPrice(1F);

        CourseMarket courseMarket1 = courseMarketService.insertCourseMarket(courseMarket);
        System.out.println(courseMarket1.toString());
    }

    @Test
    public void updateCourseMarketTest() {
        CourseMarket courseMarket = new CourseMarket().setPrice(9.0F).setCharge("203001");
        ResponseResult responseResult = courseMarketService.updateCourseMarket("4028e58161bd3b380161bd3bcd2f0004", courseMarket);
        System.out.println(responseResult);
    }

    @Test
    public void test1() {

        System.out.println(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
    }


}
