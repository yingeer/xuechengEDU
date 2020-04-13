package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import jdk.nashorn.internal.runtime.regexp.joni.CodeRangeBuffer;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class CourseMarketService {

    @Resource
    private CourseMarketRepository courseMarketRepository;

    /**
     * 根据id查询courseMarket对象
     * @param courseMarketId
     * @return
     */
    public CourseMarket getCourseMarketById(String courseMarketId) {
        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(courseMarketId);
        if (optionalCourseMarket.isPresent()) {
            return optionalCourseMarket.get();
        }
        return null;
    }
}
