package com.xuecheng.filesystem.service;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 测试FileSystemService
 *
 * @author Ying on 2020/4/18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFileSystemService {

    @Resource
    private FileSystemService fileSystemService;

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    private String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String tracker_servers;

    @Test
    public void UploadCase() {
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            //创建客户端TrackerClient
            TrackerClient tc = new TrackerClient();
            //连接tracker Server
            TrackerServer ts = tc.getConnection();
            if (ts == null) {
                System.out.println("getConnection return null");
            }
            //获取一个storage server
            StorageServer ss = tc.getStoreStorage(ts);
            if (ss == null) {
                System.out.println("getStoreStorage return null");
            }
            //创建一个storage存储客户端
            StorageClient1 sc1 = new StorageClient1(ts, ss);

            String fileId = sc1.upload_file1("D:\\spring1.png", "jpg", null);
            System.out.println(fileId);
        } catch (IOException | MyException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpload() {
        String fileName = "D:\\spring1.png";
        UploadFileResult uploadFileResult = fileSystemService.uploadFile(fileName, "测试用例", "测试用例", "");
        System.out.println(uploadFileResult);
    }
}
