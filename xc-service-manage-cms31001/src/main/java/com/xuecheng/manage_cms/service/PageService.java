package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class PageService {
    @Resource
    CmsPageRepository cmsPageRepository;

    /**
     * 页面列表分页查询
     *
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //自定义条件查询
        //定义条件匹配器

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //设置条件值（站点id）
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义条件对象Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //分页参数
        if(page <=0) {
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult<CmsPage> queryResult = new QueryResult<CmsPage>();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     *  添加页面功能
     * @param cmsPage
     * @return
     */
    public CmsPageResult addAPage(CmsPage cmsPage) {
        // 调用dao层存储CmsPage

        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        if (cmsPage1 == null) {
            cmsPage.setPageId(null);
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 根据Id查询页面
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult editPage(String id, CmsPage cmsPage) {
        CmsPage toBeEditedPage = null;
        try {
            toBeEditedPage = this.getById(id);
        } catch (Exception ex) {
        }
        if (cmsPage != null && toBeEditedPage != null) {
                toBeEditedPage.setPageId(cmsPage.getPageId());
                toBeEditedPage.setPageAliase(cmsPage.getPageAliase());
                toBeEditedPage.setSiteId(cmsPage.getSiteId());
                toBeEditedPage.setTemplateId(cmsPage.getTemplateId());
                toBeEditedPage.setPageName(cmsPage.getPageName());
                toBeEditedPage.setPageCreateTime(cmsPage.getPageCreateTime());
                toBeEditedPage.setPageWebPath(cmsPage.getPageWebPath());
                toBeEditedPage.setPageType(cmsPage.getPageType());
                toBeEditedPage.setDataUrl(cmsPage.getDataUrl());
                toBeEditedPage.setHtmlFileId(cmsPage.getHtmlFileId());

            CmsPage save = cmsPageRepository.save(toBeEditedPage);
//            cmsPageRepository.delete(toBeEditedPage);
            return new CmsPageResult(CommonCode.SUCCESS, save);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 删除page
     * @param id
     * @return
     */
    public ResponseResult deletePage(String id) {
        CmsPage one = this.getById(id);
        if (one != null) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 通过pageName&pageType查询页面
     * @param pageName
     * @param pageType
     * @return
     */
    public List<CmsPage> queryByPageNameAndPageType(String pageName, String pageType) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageName",
                ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageType(pageType);
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        List<CmsPage> cmsPageList = cmsPageRepository.findAll(example);
        return cmsPageList;
    }
}

