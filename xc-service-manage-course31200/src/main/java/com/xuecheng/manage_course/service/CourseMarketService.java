package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMarketRepository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.CoderResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
            postCourseMarket.setId(courseMarketId);
            courseMarket = this.insertCourseMarket(postCourseMarket);
            if (courseMarket==null)
                return ResponseResult.FAIL();
            return ResponseResult.SUCCESS();
        }
        // 数据检验
        CourseMarket courseMarketChecked = this.checkInfo(postCourseMarket);
        // 获取经过检验处理courseMarketChecked的信息
        String charge = courseMarketChecked.getCharge();
        String valid = courseMarketChecked.getValid();
        String qq = courseMarketChecked.getQq();
        Float price = courseMarketChecked.getPrice();
        Float price_old = courseMarketChecked.getPrice_old();
        Date startTime = courseMarketChecked.getStartTime();
        Date endTime = courseMarketChecked.getEndTime();

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
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        courseMarket.setId(id);
        // 数据检验
        CourseMarket courseMarketChecked = this.checkInfo(courseMarket);
        // 保存到数据库
        CourseMarket courseMarket1 = courseMarketRepository.save(courseMarketChecked);
        return courseMarket1;
    }

    /**
     * 检验传入CourseMarket的数据，把为null的项进行处理
     * @param courseMarket
     * @return
     */
    private CourseMarket checkInfo(CourseMarket courseMarket) {

        if (courseMarket.getPrice_old() == null) {
            courseMarket.setPrice_old(0.0F);
        }
        if (courseMarket.getPrice() == null) {
            courseMarket.setPrice(0.0F);
        }
        if (courseMarket.getQq() == null) {
            courseMarket.setQq("");
        }
        if (courseMarket.getValid() == null) {
            courseMarket.setValid("");
        }
        if (courseMarket.getCharge() == null) {
            courseMarket.setCharge("");
        }
        // 没传入时间则存储 0000-00-00
        Date dateParse = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateParse = sdf.parse("0000-00-00 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (courseMarket.getStartTime() == null) {
            courseMarket.setStartTime(dateParse);
        }
        if (courseMarket.getEndTime() == null) {
            courseMarket.setEndTime(dateParse);
        }
        return courseMarket;
    }

    /**
     * updateCourseMarket功能相同
     * @param id
     * @param courseMarket
     * @return
     */
    public ResponseResult quickUpdate(String id, CourseMarket courseMarket) {
        courseMarket.setId(id);
        try {
            courseMarketRepository.saveAndFlush(checkInfo(courseMarket));
        } catch (Exception e) {
            return ResponseResult.FAIL();
        }
        return ResponseResult.SUCCESS();
    }
}
