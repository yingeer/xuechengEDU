package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.MessageUtils;

/**
 * 用于实现分布式文件服务
 *
 * @author Ying on 2020/4/17
 */
@Api(value = "分布式文件系统管理接口", description = "提供文件的上传，浏览，下载")
public interface FileSystemControllerApi {

    /**
     * 上传文件
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    @ApiOperation("上传文件")
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata);

    /**
     * 下载图片保存图片信息到mysql course_pic表
     * @param courseId
     * @param pic
     * @return
     */
    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId, String pic);

    /**
     * 获取课程基础信息
     *
     * @param courseId
     * @return
     */
    @ApiOperation("获取课程基础信息")
    public CoursePic findCoursePic(String courseId);

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);
}
