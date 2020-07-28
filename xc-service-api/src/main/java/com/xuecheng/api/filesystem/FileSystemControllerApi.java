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

}
