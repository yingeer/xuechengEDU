package com.xuecheng.testfreemarker;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.models.auth.In;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
public class FreeMarkerTest {

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        Configuration configuration=new Configuration();
        //设置模板路径
        String classpath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
        //设置字符集
        configuration.setDefaultEncoding("utf8");
        //加载模板
        Template template = configuration.getTemplate("test1.ftl");
        //数据模型
        Map<String,Object> map = new HashMap<>();
        map.put("name","黑马程序员");
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //静态化内容
        System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        //输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/test1.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
    }

    @Test
    public void GenerateHtmlString() throws IOException, TemplateException {

        Configuration configuration = new Configuration(Configuration.getVersion());
        String templateString="" +
                "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                " 名称：${name}\n" +
                " </body>\n" +
                "</html>";

        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateString);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate("template", "utf8");

        Map<String,Object> map = new HashMap<>();
        map.put("name","黑马程序员");

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/test1.html"));
        IOUtils.copy(inputStream, fileOutputStream);
    }

    @Test
    public void testGridFs() throws FileNotFoundException {
        File file = new File("d:/index_banner.html");
        FileInputStream fileInputStream = new FileInputStream(file);

        ObjectId objectId = gridFsTemplate.store(fileInputStream, "轮播图测试文件01", "");
        String fileId = objectId.toString();
        System.out.println(fileId);
    }
}
