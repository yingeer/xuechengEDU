package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 定义页面查询的接口 controller
 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    // 页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    // 新增页面
    @ApiOperation("添加页面功能")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过Id查询页面")
    public CmsPage findById(String id);

    @ApiOperation("修改页面")
    public CmsPageResult editPage(String id, CmsPage cmsPage);

    @ApiOperation("通过pageName&pageType查询页面")
    public List<CmsPage> queryByPageNameAndPageType(String pageName, String pageType);

    @ApiOperation("删除页面")
    public ResponseResult deletePage(String id);

    @ApiOperation("发布页面")
    public ResponseResult post(String pageId);
}
