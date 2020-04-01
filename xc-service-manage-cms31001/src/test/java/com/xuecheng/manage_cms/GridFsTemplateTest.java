package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTemplateTest {

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Resource
    private GridFSBucket gridFSBucket;

    @Test
    public void testGridFs() throws FileNotFoundException {
        File file = new File("D:\\com.xuecheng-edu\\xcEduService\\xc-service-manage-cms31001\\src\\main\\resources\\templates\\index_banner.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "测试用例2", "");
        String fileId = objectId.toString();
        System.out.println(fileId);
    }

    @Test
    public void testGridBucket() throws IOException {
        String fileId = "5e81b400c0e732496453b510";
//      根据id查询文件
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
//      打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
//        创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(fsFile, gridFSDownloadStream);
        String s = IOUtils.toString(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(s);
    }

    @Test
    public void testDeleteFile() {
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5e80a850c0e732577435fd3a")));
    }
}

