package com.xuecheng.manage_cms_client;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PathToServerTest {

    @Resource
    private CmsPageRepository cmsPageRepository;

    @Resource
    private CmsSiteRepository cmsSiteRepository;

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Resource
    private GridFSBucket gridFSBucket;

    @Test
    public void testSave() {
        this.savePageToServerPath("5a795ac7dd573c04508f3a56");
//        CmsPage cmsPage = getCmsPageById("5a795ac7dd573c04508f3a56");
//        System.out.println(cmsPage.toString());
    }

    /**
     * 根据pageId获得htmlId,将页面复制到指定的server path
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        // 获得cmspage对象
        CmsPage cmsPage = this.getCmsPageById(pageId);
        // 获得静态页面id
        String htmlFileId = cmsPage.getHtmlFileId();
        if (htmlFileId == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 获得静态页面输入流
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        CmsSite cmsSite = this.getCmsSiteById(cmsPage.getSiteId());
        // 页面路径
        String pagePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据fileId获得file
     * @param fileId
     * @return
     */
    public InputStream getFileById(String fileId) {
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CmsPage getCmsPageById(String pageId) {
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(pageId);
        CmsPage cmsPage = null;
        if (optionalCmsPage.isPresent()) {
            cmsPage = optionalCmsPage.get();
        }
        return cmsPage;
    }

    /**
     * 根据siteId查询CmsSite
     * @param siteId
     * @return
     */
    public CmsSite getCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
