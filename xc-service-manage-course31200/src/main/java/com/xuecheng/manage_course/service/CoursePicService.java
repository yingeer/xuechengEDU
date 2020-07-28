package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class CoursePicService {

    @Resource
    CoursePicRepository coursePicRepository;

    /**
     * 下载图片保存图片信息到mysql course_pic表
     * @param courseId
     * @param pic
     * @return
     */
    public ResponseResult saveCoursePic(String courseId, String pic) {
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (optionalCoursePic.isPresent()) {
            coursePic= optionalCoursePic.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取course_pic课程图片信息
     * @param courseId
     * @return
     */
    public CoursePic findCoursepic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (optional.isPresent()) {
            coursePic = optional.get();
        }
        return coursePic;
    }

    /**
     * 删除course_pic课程图片信息
     * @param courseId
     * @return
     */
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        long resultCode = coursePicRepository.deleteCoursePicByCourseid(courseId);
        if (resultCode > 0) {
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }
}
