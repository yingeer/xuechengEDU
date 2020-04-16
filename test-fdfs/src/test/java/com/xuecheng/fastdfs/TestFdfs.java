package com.xuecheng.fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFdfs {

    @Test
    public void testUpload() {

        try {
            String trackerServers = "192.168.5.140:22122";
            ClientGlobal.initByTrackers(trackerServers);
//            ClientGlobal.initByProperties("config/fastdfs‐client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);
            //创建客户端
            TrackerClient tc = new TrackerClient();
            //连接tracker Server
            TrackerServer ts = tc.getConnection();
            if (ts == null) {
                System.out.println("getConnection return null");
                return;
            }
            //获取一个storage server
            StorageServer ss = tc.getStoreStorage(ts);
            if (ss == null) {
                System.out.println("getStoreStorage return null");
            }
            //创建一个storage存储客户端
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            NameValuePair[] meta_list = null;  //new NameValuePair[0];
            String item = "D:\\85YK0RRRS47L.jpg";
            String fileid;
            fileid = sc1.upload_file1(item, "jpg", meta_list);
            System.out.println("Upload local file " + item + " ok, fileid=" + fileid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testProperty() throws IOException, MyException {
//        加载 trackerServers 字符串配置：
        String trackerServers = "10.0.11.101:22122,10.0.11.102:22122";
        ClientGlobal.initByTrackers(trackerServers);

        System.out.println(ClientGlobal.configInfo());
    }

    @Test
    public void testQueryFile() throws IOException, MyException {
        String trackerServers = "192.168.5.140:22122";
        ClientGlobal.initByTrackers(trackerServers);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        FileInfo fileInfo = storageClient.query_file_info("group1", "M00/00/00/wKgFjl6Xv0OAF85NABG7On7QrTg138.jpg");
        System.out.println(fileInfo);
    }

    @Test
    public void testDownload() throws IOException, MyException {
        String trackerServers = "192.168.5.140:22122";
        ClientGlobal.initByTrackers(trackerServers);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

        byte[] result = storageClient1.download_file1("group1/M00/00/00/wKgFjl6XtQmAKpRfABG7On7QrTg149.jpg");
        File file = new File("C:\\Users\\Acer\\Desktop\\download\\1.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(result);
        fileOutputStream.close();
    }

}
