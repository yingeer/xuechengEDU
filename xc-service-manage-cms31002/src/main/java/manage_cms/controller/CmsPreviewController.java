package manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/cms/page")
public class CmsPreviewController extends BaseController {

    @Resource
    private PageService pageService;

    @GetMapping("/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) {
        String pageHtml = pageService.getPageHtml(pageId);
        if (StringUtils.isNotEmpty(pageHtml)) {
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
