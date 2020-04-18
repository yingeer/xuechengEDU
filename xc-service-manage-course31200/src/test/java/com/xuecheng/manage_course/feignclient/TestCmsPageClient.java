package com.xuecheng.manage_course.feignclient;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 测试CmsPageClient
 *
 * @author Ying on 2020/4/18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCmsPageClient {

    @Resource
    private CmsPageClient cmsPageClient;

    @Test
    public void findByIdTest() {
        CmsPage cmsPage = cmsPageClient.findById("5a754adf6abb500ad05688d9");
        System.out.println(cmsPage.toString());
    }

}
