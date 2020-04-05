package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestTeachplanMapper {

    @Resource
    private TeachplanMapper teachplanMapper;

    /**
     * 测试selectList能否正常运行
     */
    @Test
    public void selectListTest() {
        TeachplanNode teachplanNode = teachplanMapper.selectList("402885816243d2dd016243f24c030002");
        System.out.println(teachplanNode.toString());
    }
}
