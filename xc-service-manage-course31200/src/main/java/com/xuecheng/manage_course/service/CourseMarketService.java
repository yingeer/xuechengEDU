package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMarketRepository;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * 更新courseMarket
     * @param courseMarketId
     * @param postCourseMarket
     * @return
     */
    public ResponseResult updateCourseMarket(String courseMarketId, CourseMarket postCourseMarket) {
        // 通过courseMarketId查询CourseMarket
        // 没有 新增一个CourseMarket，有则提取
        CourseMarket courseMarket = getCourseMarketById(courseMarketId);
        if (courseMarket == null) {
            // 新增CourseMarket
            courseMarket = this.insertCourseMarket(postCourseMarket);
            if (courseMarket==null)
                return ResponseResult.FAIL();
            return ResponseResult.SUCCESS();
        }
        // 获取传入postCourseMarket的信息
        String charge = postCourseMarket.getCharge();
        String valid = postCourseMarket.getValid();
        String qq = postCourseMarket.getQq();
        Float price = postCourseMarket.getPrice();
        Float price_old = postCourseMarket.getPrice_old();
        Date startTime = postCourseMarket.getStartTime();
        Date endTime = postCourseMarket.getEndTime();

        // 更改原来的courseMarket对象
        courseMarket.setCharge(charge).setValid(valid).setQq(qq).setPrice(price)
                .setPrice_old(price_old).setStartTime(startTime).setEndTime(endTime);
        //保存courseMarket对象
        try {
            courseMarketRepository.save(courseMarket);
        } catch (Exception e) {
            return ResponseResult.FAIL();
        }
        return ResponseResult.SUCCESS();
    }

    /**
     * 插入CourseMarket到数据库
     * @param courseMarket
     * @return
     */
    public CourseMarket insertCourseMarket(CourseMarket courseMarket) {
        // 数据检验
        if (courseMarket.getPrice() == null) {
            courseMarket.setPrice(0.0F);
        }
        if (courseMarket.getCharge() == null) {
            courseMarket.setCharge("");
        }
        // 没传入时间则存储 0000-00-00
        Date dateParse = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateParse = sdf.parse("0000-00-00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (courseMarket.getStartTime() == null) {
            courseMarket.setStartTime(dateParse);
        }
        if (courseMarket.getEndTime() == null) {
            courseMarket.setEndTime(dateParse);
        }

        // 保存到数据库
        CourseMarket courseMarket1 = courseMarketRepository.save(courseMarket);
        return courseMarket1;
    }
}
