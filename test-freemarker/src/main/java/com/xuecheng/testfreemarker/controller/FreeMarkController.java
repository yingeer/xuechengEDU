package com.xuecheng.testfreemarker.controller;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.testfreemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/freemarker")
public class FreeMarkController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/banner")
    public String banner(Map<String, Object> map) {
        String dataUrl = "http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
//        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
//        Map body = forEntity.getBody();
        CmsConfig cmsConfig = restTemplate.getForObject(dataUrl, CmsConfig.class);
        assert cmsConfig != null;
        map.put("model", cmsConfig.getModel());
        map.put("s", 12345);
        return "index_banner";
    }

    @GetMapping("/test1")
    public String test1(Map<String, Object> map) {

        map.put("name", "黑马程序员");

        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());

        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);

        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
//向数据模型放数据
        map.put("stus", stus);
//准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
//向数据模型放数据
        map.put("stu1", stu1);
//向数据模型放数据
        map.put("stuMap", stuMap);
//返回模板文件名称
        return "test1";
// map = {"stu1": stu1, "stu2": stu2, "name": "黑马", "stus": [stu1, stu2], "stuMap": {"stu1": stu1, "stu2": stu2}}
    }
}
