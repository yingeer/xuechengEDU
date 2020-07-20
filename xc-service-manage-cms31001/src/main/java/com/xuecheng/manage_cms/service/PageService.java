package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.config.RabbitMqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.operator.SymmetricKeyWrapper;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.util.*;

@Service
public class PageService {
    @Resource
    CmsPageRepository cmsPageRepository;

    @Resource
    private CmsTemplateRepository cmsTemplateRepository;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Resource
    private GridFSBucket gridFSBucket;

    @Resource
    private RabbitTemplate rabbitTemplate;

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

        //自定义条件查询 模糊查询
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

        // 分页功能
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
     * 根据Id查询cmsPage
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

    /**
     * 通过查询model和template生成一个html
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {
        Map model = this.getModelById(pageId);
        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String templateContent = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String html = this.generateHtml(templateContent, model);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
//        System.out.println("=================");
//        System.out.println(html);
//        System.out.println("=================");

        return html;
    }

    public String generateHtml(String templateContent, Map model) {
        String html = null;
        try {
            // 生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            // 模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", templateContent);
            configuration.setTemplateLoader(stringTemplateLoader);

            // 获取模板
            Template template = configuration.getTemplate("template");
            html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//            System.out.println(html);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    /**
     * 获得model用于页面静态化
     * @param pageId
     * @return
     */
    public Map getModelById(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataURL = cmsPage.getDataUrl();
        if (dataURL == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(dataURL, Map.class);
        Map body = responseEntity.getBody();
        return body;
    }

    /**
     * 获得template用于页面静态化
     * @param pageId
     * @return
     */
    public String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            String templateFieldId = cmsTemplate.getTemplateFileId();
            //      根据id查询文件
            GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFieldId)));
//      打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
//        创建gridFsResource，用于获取流对象
            GridFsResource gridFsResource = new GridFsResource(fsFile, gridFSDownloadStream);
            try {
                String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF8");
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 执行页面发布
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId) {
        // 执行静态化，获得静态化后的html
        String html = this.getPageHtml(pageId);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        // 保存静态化文件
        CmsPage cmsPage = this.saveHtml(pageId, html);
        // 发送消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存Html至mongodb
     * @param pageId
     * @param PageHtml
     * @return
     */
    private CmsPage saveHtml(String pageId, String PageHtml) {
        // 查询对应page，将htmlFileId的文件删除
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(pageId);
        if (!optionalCmsPage.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optionalCmsPage.get();
        String htmlFileId = cmsPage.getHtmlFileId();
        // 删除htmlFileid对应的File
        if (StringUtils.isNotEmpty(htmlFileId)) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        // 将html静态页面保存至mongodb
        InputStream inputStream = IOUtils.toInputStream(PageHtml);
        ObjectId objectId = gridFsTemplate.store(inputStream, "测试用例2", "");
        String fileId = objectId.toString();
        // 更新原来的cmsPage对象
        cmsPage.setHtmlFileId(fileId);
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    /**
     * 发送消息
     * @param pageId
     */
    private void sendPostPage(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageId", pageId);
        // 发送的消息内容 msg
        String msg = JSON.toJSONString(map);
        // 获取站点id作为routingKey
        String siteId = cmsPage.getSiteId();
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, msg);
    }
}
