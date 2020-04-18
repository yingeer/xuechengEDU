package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    // 根据页面名称查询
    public CmsPage findByPageName(String pageName);

    // 根据页面名称和类型查询
    public CmsPage findByPageNameAndPageType(String pageName, String pageType);

    // 根据站点和页面类型查询记录数
    public int countBySiteIdAndPageType(String siteId, String pageType);

    // 根据站点和页面类型分页查询
    public Page<CmsPage> findBySiteIdAndPageType(String siteId, String pageType, Pageable pageable);

    // 根据站点Id、页面名称、PageWebPath查询
    public CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId, String pageName, String pageWebPath);


}

