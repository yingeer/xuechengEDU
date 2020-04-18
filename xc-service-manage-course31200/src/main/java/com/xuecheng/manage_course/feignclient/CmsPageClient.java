package com.xuecheng.manage_course.feignclient;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * openFeign远程调用cms_manage服务
 *
 * @author Ying on 2020/4/18
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS")
public interface CmsPageClient {

    @GetMapping("/cms/page/list/{id}")
    public CmsPage findById(@PathVariable("id") String id);
}
