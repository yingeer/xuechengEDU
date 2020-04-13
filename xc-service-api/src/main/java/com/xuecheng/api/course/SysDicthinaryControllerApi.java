package com.xuecheng.api.course;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.ApiOperation;

public interface SysDicthinaryControllerApi {

    @ApiOperation("数据字典查询接口")
    public SysDictionary getByType(String type);
}
