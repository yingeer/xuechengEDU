package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class FileSystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    private String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String tracker_servers;

    @Resource
    private FileSystemRepository fileSystemRepository;

    /**
     * 加载fdfs配置信息
     */
    private void initFdfsConfig() {
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件，暴露到controller
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    public UploadFileResult uploadFile(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        // 上传到fdfs
        String fileId = uploadToFdfs(multipartFile);
        if (fileId == null)
            System.out.println("fileId:\t" + fileId);
        // 创建FileSystem对象
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileType(multipartFile.getContentType());
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        fileSystem.setFilePath(fileId);
        if (StringUtils.isNotEmpty(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);

        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    /**
     * 实际上传到fdfs
     * @param multipartFile
     * @return
     */
    private String uploadToFdfs(MultipartFile multipartFile) {
        try {
            initFdfsConfig();
            //创建客户端TrackerClient
            TrackerClient tc = new TrackerClient();
            //连接tracker Server
            TrackerServer ts = tc.getConnection();
            if (ts == null) {
                System.out.println("getConnection return null");
                return null;
            }
            //获取一个storage server
            StorageServer ss = tc.getStoreStorage(ts);
            if (ss == null) {
                System.out.println("getStoreStorage return null");
            }
            //创建一个storage存储客户端
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            // 文件字节
            byte[] bytes = multipartFile.getBytes();
            // 文件拓展名
            String originalFileName = multipartFile.getOriginalFilename();
            String extName = null;
            if (originalFileName != null) {
                extName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            }
            String fileId = sc1.upload_file1(bytes, extName, null);
            return fileId;
        } catch (IOException | MyException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UploadFileResult uploadFile(String fileName, String filetag,
                                       String businesskey, String metadata) {

        // 上传到fdfs
        String fileId = uploadToFafsByFileName(fileName);
        if (fileId == null)
            System.out.println("fileId:\t" + fileId);
        // 创建FileSystem对象
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFileName(fileName);
//        fileSystem.setFileSize(new MultipartFile(fileName).getSize());
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileSystem.setFileType(extName);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        fileSystem.setFilePath(fileId);
        if (StringUtils.isNotEmpty(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);

        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    private String uploadToFafsByFileName(String fileName) {
        try {
            initFdfsConfig();
            //创建客户端TrackerClient
            TrackerClient tc = new TrackerClient();
            //连接tracker Server
            TrackerServer ts = tc.getConnection();
            if (ts == null) {
                System.out.println("getConnection return null");
                return null;
            }
            //获取一个storage server
            StorageServer ss = tc.getStoreStorage(ts);
            if (ss == null) {
                System.out.println("getStoreStorage return null");
            }
            //创建一个storage存储客户端
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            // 后缀名
            String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            String fileId = sc1.upload_file1(fileName, extName, null);
            return fileId;
        } catch (IOException | MyException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


}
