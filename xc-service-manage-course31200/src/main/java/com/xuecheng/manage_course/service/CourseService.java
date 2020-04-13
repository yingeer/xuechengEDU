package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.crypto.spec.OAEPParameterSpec;
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

    @Resource
    private CourseMapper courseMapper;

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

    /**
     * 实现分页查询course
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseInfoPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> result = courseInfoPage.getResult();
        QueryResult<CourseInfo> queryResult = new QueryResult<CourseInfo>();
        queryResult.setList(result);
        queryResult.setTotal(result.size());
        return queryResult;
    }

    /**
     * 根据courseId 获取CourseBase对象
     * @param courseId
     * @return
     */
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseId);
        return optionalCourseBase.get();
    }

    /**
     * 更改id对应的courseBase对象
     * @param courseId
     * @param userCourseBase
     * @return
     */
    public ResponseResult updateCourseBase(String courseId, CourseBase userCourseBase) {
        // 获取原来的courseBase
        CourseBase courseBase = null;
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseId);
        if (!optionalCourseBase.isPresent()) {
            return new ResponseResult(CommonCode.FAIL);
        }
        courseBase = optionalCourseBase.get();

        // 用userCourseBase里面的数据更新原来的courseBase
        String name = userCourseBase.getName();
        String users = userCourseBase.getUsers();
        String mt = userCourseBase.getMt();
        String grade = userCourseBase.getGrade();
        String studyModel = userCourseBase.getStudymodel();
        String teachMode = userCourseBase.getTeachmode();
        String description = userCourseBase.getDescription();
        courseBase.setName(name).setUsers(users).setMt(mt).setGrade(grade)
                .setStudymodel(studyModel).setTeachmode(teachMode).setDescription(description);

        // 保存courseBase
        CourseBase courseBase1 = courseBaseRepository.save(courseBase);

        // 返回成功ResponseResult
        return new ResponseResult(CommonCode.SUCCESS);
    }

}

