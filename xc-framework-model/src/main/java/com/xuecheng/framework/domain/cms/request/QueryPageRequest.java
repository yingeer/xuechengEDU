package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接受页面查询的条件 siteId, pageId等
 *
 */
@Data
public class QueryPageRequest {

    @ApiModelProperty("页面Id")
    private String pageId;

    @ApiModelProperty("站点Id")
    private String siteId;

    @ApiModelProperty("页面名称")
    private String pageName;

    @ApiModelProperty("页面别名")
    private String pageAliase;

    @ApiModelProperty("模板Id")
    private String templateId;
    // ...
}
