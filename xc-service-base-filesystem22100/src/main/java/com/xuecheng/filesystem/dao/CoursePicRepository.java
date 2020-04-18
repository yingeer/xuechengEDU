package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 映射到course_pic表
 *
 * @author Ying on 2020/4/18
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {

    public long deleteCoursePicByCourseid(String courseId);
}
