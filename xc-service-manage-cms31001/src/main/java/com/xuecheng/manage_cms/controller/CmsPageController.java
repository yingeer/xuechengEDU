package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Resource
    PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size,
                                        QueryPageRequest queryPageRequest) {
        return pageService.findList(page, size, queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.addAPage(cmsPage);
    }

    @Override
    @GetMapping("/list/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.getById(id);
    }

    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult editPage(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.editPage(id, cmsPage);
    }

    @DeleteMapping("/delete/id")
    @Override
    public ResponseResult deletePage(String id) {
        ResponseResult responseResult = pageService.deletePage(id);
        return responseResult;
    }

    @Override
    @GetMapping("/postpage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.postPage(pageId);
    }


    @GetMapping("/{pageName}/{pageType}")
    @ResponseBody
    public List<CmsPage> queryByPageNameAndPageType(@PathVariable("pageName") String pageName,
                                                    @PathVariable("pageType") String pageType) {
        return pageService.queryByPageNameAndPageType(pageName, pageType);
    }
}

