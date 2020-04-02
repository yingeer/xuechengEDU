package com.xuecheng.manage_cms_client.controller;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ManageCmsClientController {

    @Resource
    private PageService pageService;

    @GetMapping("/mpp")
    public String get() {
        CmsPage cmsPage = pageService.getCmsPageById("5a795ac7dd573c04508f3a56");
        return cmsPage.toString();
    }


}
