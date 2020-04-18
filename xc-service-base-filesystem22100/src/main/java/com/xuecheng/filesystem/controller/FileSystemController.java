package com.xuecheng.filesystem.controller;

import com.xuecheng.api.filesystem.FileSystemControllerApi;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Resource
    private FileSystemService fileSystemService;

    /**
     * 上传文件
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    @Override
    @PostMapping("/upload")
    public UploadFileResult upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                   @RequestParam(value = "filetag", required = true) String filetag,
                                   @RequestParam(value = "businesskey", required = false) String businesskey,
                                   @RequestParam(value = "metadata", required = false) String metadata) {
        return fileSystemService.uploadFile(multipartFile, filetag, businesskey, metadata);
    }

    /**
     * 下载图片保存图片信息到mysql course_pic表
     * @param courseId
     * @param pic
     * @return
     */
    @PostMapping("/courseid/add")
    @Override
    public ResponseResult addCoursePic(@RequestParam(value = "courseid") String courseId, @RequestParam("pic") String pic) {
        return fileSystemService.saveCoursePic(courseId, pic);
    }

}
