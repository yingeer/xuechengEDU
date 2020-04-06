package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Resource
    private TeachplanMapper teachplanMapper;

    @Resource
    private TeachplanRepository teachplanRepository;

    @Resource
    private CourseBaseRepository courseBaseRepository;

    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }

    /**
     * 添加一条teachplan记录
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
//      校验课程id和课程计划名称
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 取出课程id
        String courseId = teachplan.getCourseid();
        // 取出父节点id
        String parentId = teachplan.getParentid();
        if (StringUtils.isEmpty(parentId)) {
            parentId = getTeachplanRoot(courseId);
        }
        if (parentId == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 取出parent teachplan
        Optional<Teachplan> optional = teachplanRepository.findById(parentId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan parentTeachplan = optional.get();
        // 取出parent teachplan 信息
        String parentGrade = parentTeachplan.getGrade();
        // 传入数据teachplan复制一份到teachplanNew
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplanNew);

        teachplanNew.setParentid(parentId);
        teachplanNew.setStatus("0"); // status=0 未发布
        teachplanNew.setCourseid(courseId);
        if (parentGrade.equals("1")) {
            teachplanNew.setGrade("2");
        } else if (parentGrade.equals("2")) {
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 传入teachplan的parentId属性为空时，调用该方法
     * @param courseId
     * @return 作为parentId
     */
    private String getTeachplanRoot(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        // 取出courseId为传入参数，parentId="0"的根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() == 0) {
            //新增一个根结点
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");//1级
            teachplanRoot.setStatus("0");//未发布
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        Teachplan teachplanRoot = teachplanList.get(0);
        return teachplanRoot.getId();
    }

}

