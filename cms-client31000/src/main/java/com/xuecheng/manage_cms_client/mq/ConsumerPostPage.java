package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Resource
    private CmsPageRepository cmsPageRepository;

    @Resource
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        // 解析消息msg
        Map map = JSON.parseObject(msg, Map.class);
        // 获取pageId
        String pageId = (String) map.get("pageId");
        // 判断是否有该页面存在
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(pageId);
        if (!optionalCmsPage.isPresent()) {
            LOGGER.error("从消息队列里获得的pageId无法找到对应page:{}", msg.toString());
        }
        pageService.savePageToServerPath(pageId);
    }
}
