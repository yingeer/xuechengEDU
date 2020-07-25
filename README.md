# xuechengEDU——基于springboot&springcloud的在线教育平台（后端）

# 1.  写在前面

因为要参加秋招，但是发现自己好像没有拿的出手的项目😥于是就照着某个培训班的一个项目做了起来。

**项目基本情况：**

1. 在线教育平台是一个很大的话题，本人目前只实现了个别业务功能

2. 项目涉及到的技术很多，基于springcloud Finchley.SR1+springboot2.0.1.RELEASE+spring data jpa+mybatis+rabbitmq+fastdfs+mongodb……

   基于springcloud和springboot，spring cloud 用的是netflix F版（没有用alibaba那套），springboot用的是2.0.1.RELEASE，其他技术版本不做过多介绍。

3. 该项目使用前后端分离，本人只做了后端，前端原本使用的是vue技术栈。所以跑起来只能在浏览器上返回一些json数据😵

4. 项目搭建过程中用到了1台Windows+2台Ubuntu，也只能在本地跑起来😓上线不现实，**如果你发现了本人的这个项目并且想模仿学习**，**我觉得还是算了吧**，毕竟这只是一个练手、找工作的项目，没太大的参考意义。



# 2.  项目基本结构

![基本项目结构](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/%E5%9F%BA%E6%9C%AC%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84.png)

上面这张图就是该项目的基本结构

项目第一步就是要搭建整体框架，注意：

1. 顶级目录的pom文件<parent>指向spring boot，并且这里我没有像参考一样，专门建一个parent工程来做父工程（后期子模块引用的时候会出很多bug）

   ```xml
   <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.0.1.RELEASE</version>
       </parent>
   ```

2. **技术选型一定要明确，版本一定要明确**，spring cloud 与 spring boot的兼容问题，不然后面会配哭的😭[【版本对照】](https://blog.csdn.net/weixin_42548604/article/details/92805523)

3. 这里本人非常欣赏的是模块的划分，基础模块区分业务模块，基础模块细分，业务模块细分

这步虽然简单，但是非常重要，是保证接下来开发有序进行的基本保障，如果中间的某个技术选型出了问题，后面很可能要反反复复倒过去改（别问我是怎么知道的😥）

# 3.  业务功能1—— CMS服务

## 3.1 需求分析

本项目对CMS系统的定位是对该网站的各个**子站点的页面**进行管理，可以先将设计好的页面做存储，等待要更换页面时可以即时更新。

## 3.2 大致开发过程描述

### 3.2.1 基本配置

1. 把前端的门户工程拷贝到一个地方

![门户工程目录](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/门户工程目录.png)

2. 配置nignx 

   这里要用到nignx的SSI技术（服务端嵌入），ssi包含类似于jsp页面中的incluce指令，ssi是在web服务端将include指定 的页面包含在网页中，渲染html网页响应给客户端 。

   <!‐‐#include virtual="/../....html"‐‐>

   **这样就可以把门户页面拆分多个可更换的部分，每个部分都可以换成存储中的页面，实现对子站点页面的管理。**

   nignx前期配置：

   ```
   # www.xuecheng.com
   #cms页面预览  
       server {
           listen       80;
           server_name  www.xuecheng.com;  # 在hosts中添加    127.0.0.1 www.xuecheng.com
           #charset koi8-r;
           #access_log  logs/host.access.log  main;

    		ssi on;
      		ssi_silent_errors on;
       	location / {
               alias   D:/xuecheng-edu/xcEduUI01/xc-ui-pc-static-portal/;
               index  index.html index.htm;
       	}
       
       	……
      }
   ```

   启动nginx查看效果

   ![门户效果图](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/门户效果图.png)

   注意，这里有各种图，反复强调，正是这些页面要经常换，所以才要进行CMS管理，否则等到要临时更换时再开发就来不及了😑

   ​

3. MongoDB数据库配置

   第一次用MongoDB，找份基础教程学习一下，客户端推荐用dbKoda

   创建xc_cms数据库，把json数据文件导入，一次导入的数据文件有点多，需要慢慢理解

   ![mongodb数据库展示](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/mongdb数据库展示.png)

   建议：给数据库添加用户

   ---

   ​

   ​

###  3.2.2 页面查询接口定义

1. 定义模型

   具体需求：
   1、分页查询CmsPage 集合下的数据
   2、根据站点Id、模板Id、页面别名查询页面信息
   3、接口基于Http Get请求，响应Json数据

   三个主要模型：

   CmsSite：站点模型 （有多个不同的子站点）
   CmsTemplate：页面模板 （涉及到模板引擎渲染）
   CmsPage：页面信息 （保留用于渲染的页面信息，可理解为页面元数据 包括_id，siteId，pageName，pagePhysicalPath，templateId，htmlFileId 等属性）

   ​

   ![cms模型](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/cms模型.png)

模型都是统一在model模块中定义的，直接在业务模块中添加一个model依赖就行，方便管理。



2. 定义接口

   专门定义请求&响应的数据结构 QueryPageRequest 和 CmsPageResult

   在api模块下定义

   ![api模块](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/api模块.png)

   定义查询接口 findList

   ```java
   public interface CmsPageControllerApi {
       
       public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
   ……
   }
   ```

   ​

### 3.2.3 实现cms_page查询功能（完整流程）

​	 	先创建xc-service-manage-cms31001模块

1. 写pom

   ```
   父模块xcEduService，另外四个基础模块得有

   spring-boot-starter-web  核心启动器
   spring-boot-starter-actuator 监控和管理
   spring-boot-starter-freemarker 模板引擎freemarker支持
   spring-boot-starter-data-mongodb  mongodb支持
   spring-boot-starter-test 测试
   spring-boot-devtools 热启动
   ……
   ```

   只要在父模块的xcEduService定义好，后期要什么就添加什么

2. 改yaml

一开始先别着急添那么多功能，先把基本的功能跑起来

```xml
server:
  port: 31001
spring:
  application:
    name: xc-service-manage-cms
  data:
    mongodb:
      uri:  mongodb://ying:newman123@127.0.0.1:27017
      database: xc_cms
```



3. 主启动类

   ```java

   @SpringBootApplication
   //@EnableDiscoveryClient
   @EntityScan("com.xuecheng.framework.domain.cms")
   @ComponentScan(basePackages = {"com.xuecheng.api"}) // 扫描bean
   @ComponentScan(basePackages={"com.xuecheng.manage_cms"})
   @ComponentScan(basePackages = {"com.xuecheng.framework"})
   public class ManageCmsApplication {
       public static void main(String[] args) {
           SpringApplication.run(ManageCmsApplication.class);
       }

   }

   ```

   常规操作，不过要注意，扫包的时候@ComponentScan一旦指定，就不会默认地再扫一遍主启动类当前的包（这里是com.xuecheng.manage_cms），所以要扫的包全都要手动添加进去

   ​

   看到项目跑起来是件幸福的事☺

   ![跑起来啦](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/项目跑起来啦.png)

   ​



4. controller 

   贴一下代码，常规操作，调用一下service层

   ```java
   @RestController
   @RequestMapping("/cms/page")
   public class CmsPageController implements CmsPageControllerApi {

       @Resource
       PageService pageService;

       @Override
       @GetMapping("/list/{page}/{size}")
       public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size,
                                           QueryPageRequest queryPageRequest) {
           return pageService.findList(page, size, queryPageRequest);
       }
   ```

   ​

5. dao

   ```java
   @Component
   public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
   }

   ```

   CmsPageRepository接口直接继承Mongodb的那套接口，上面的findAll方法直接调用service 

   这里用到了分页 ，大致流程如下：

   把传进来的参数构造一个CmsPage对象，定义条件匹配器ExampleMatcher（这里对pageAliase属性用了模糊查询），定义条件对象Example， 定义Pageable，调用dao层findAll方法，得到Page<CmsPage>对象

   ​

   ```java
   @Service
   public class PageService {
       
       @Resource
       CmsPageRepository cmsPageRepository;

       /**
        * 页面列表分页查询
        *
        * @param page             当前页码
        * @param size             页面显示个数
        * @param queryPageRequest 查询条件
        * @return                 页面列表
        */
       public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

           if (queryPageRequest == null) {
               queryPageRequest = new QueryPageRequest();
           }
          
           //分页参数
           if(page <=0) {
               page = 1;
           }
           page = page -1;
           if(size<=0){
               size = 10;
           }
           Pageable pageable = PageRequest.of(page,size);
           Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
           QueryResult<CmsPage> queryResult = new QueryResult<CmsPage>();
           queryResult.setList(all.getContent());//数据列表
           queryResult.setTotal(all.getTotalElements());//数据总记录数
           return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
       }

   ```

   ​

   ​

7. 测试

   7.1 看看controller层能否正常返回数据  http://localhost:31001/cms/page/list/1/10

   ![返回数据成功](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/返回数据成功.png)

前端不行，只能做到这步了，返回个json就……👀，再用用spring boot自带的test来测试一下

  		7.2 编写测试类

注意，test的包结构要与主启动类所在包结构一致，resources里面可以不加文件，一旦加了（如application.yaml），那测试开启后就会读取 test/resources/*.yaml，建议不加，如果改了原配置，那么test/下也要改，麻烦

![test包结构](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/test包结构注意.png)

再贴一下代码

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void testFindPage() {
        int page = 0;//从0开始
        int size = 10;//每页记录数
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

}
```

结果：

![测试成功](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/首次测试成功.png)

当然，MongoRepository提供了很多现成的方法，可以测试

*补充：*

1. 测试也可以在swagger或postman上进行，这两款都是常用的接口测试工具。
2. service层添加模糊查询功能，以pageAliase为查询条件，在findList方法上添加

```java
ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //设置条件值（站点id）
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义条件对象Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

```

还是来看一下前端好了，虽然不是自己写的，但还是花了不少时间把他运行调整起来

webpack打包后，http://localhost:11000

![cms前端](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/cms前端.png)

用一下搜索功能，看看是不是模糊查询，没有问题

![模糊查询](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/cms前端模糊查询.png)



以上实现了一个完整的查询功能开发

------

### 3.2.4 实现cms_page其他功能

#### 新增页面

在cms_page集中上创建页面名称、站点Id、页面webpath为唯一索引

1. api接口 `public CmsPageResult add(CmsPage cmsPage);`

2. DAO

   ```java
   public interface CmsPageRepository extends MongoRepository {
   //根据页面名称、站点id、页面访问路径查询
   CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
   }
   ```

3.  Service

    `public CmsPageResult add(CmsPage cmsPage)`

4. Controller

   `@PostMapping("/add")`
   `public CmsPageResult add(@RequestBody CmsPage cmsPage)` 

5. 测试

6. 前端…………

#### 修改页面

1. Api 

   `public CmsPageResult editPage(String id, CmsPage cmsPage);`

2. Dao 使用 Spring Data提供的save方法完成删除操作 。

3.  Service `public CmsPageResult update(String id,CmsPage cmsPage)`

4. Controller 

   `@PutMapping("/edit/{id}")`
   `public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage)`

5. 测试

#### 删除页面

1. Api 

   `public ResponseResult deletePage(String id);`

2. Dao 使用 Spring Data提供的deleteById方法完成删除操作 。

3.  Service `public ResponseResult delete(String id)`

4. Controller 

   `@DeleteMapping("/del/{id}")` 
   `public ResponseResult delete(@PathVariable("id") String id)` 

5. 测试


------



### 3.2.5 页面静态化

上面的查询是针对mongoDB中CmsPage集合的增删改查，是对页面元数据进行操作，如果要得到完整的html，还要有页面模板（这里用freemarker）,然后将新的页面存储到mongdb自带的GridFS中。



![页面静态化](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/页面静态化.png)



#### 页面静态化流程

代码 `com.xuecheng.manage_cms.service.PageService.getPageHtml`

流程

![静态化流程](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/页面静态化流程.png)

1. 一开始从cms_page中获得dataUrl
2. 通过restTemplate工具发送dataUrl请求，cms_manage服务继续解析请求，返回一个CmsConfig对象，从model属性获得轮播图真正url
3. 从mongoDB的GridFS中获得页面的模板
4. 模板渲染生成html

![cms_config模型](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/cmsconfig一个模型.png)



#### 数据模型

```java
@Data
@ToString
@Document(collection = "cms_config")
public class CmsConfig {
@Id
private String id;//主键
private String name;//数据模型的名称
private List model;//数据模型项目
}
```

```java
@Data
@ToString
public class CmsConfigModel {
private String key;//主键
private String name;//项目名称
private String url;//项目url
private Map mapValue;//项目复杂值
private String value;//项目简单值
}
```

##### Cms_config查询功能

API

```java
public interface CmsConfigControllerApi {
@ApiOperation("根据id查询CMS配置信息")
public CmsConfig getmodel(String id);
}
```

Dao

```java
public interface CmsConfigRepository extends MongoRepository {
}
```

Service  `public CmsConfig getConfigById(String id)`

Controller  ……

#### 模板管理

![模板管理](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/模板管理.png)



附：[GridFS基本操作参考](https://juejin.im/post/5e85762d6fb9a03c93055f3c)



------

### 3.2.6 页面预览

#### 流程

页面在发布前增加页面预览的步骤，方便用户检查页面内容是否正确。页面预览的流程如下：

![页面预览](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/页面预览流程.png)

1. 用户进入cms前端，点击“页面预览”在浏览器请求cms页面预览链接。
2. cms根据页面id查询DataUrl并远程请求DataUrl获取数据模型。
3. cms根据页面id查询页面模板内容
4. cms执行页面静态化。
5. cms将静态化内容响应给浏览器。
6. 在浏览器展示页面内容，实现页面预览的功能。

参考代码  com\xuecheng\manage_cms\controller\CmsPreviewController.java

#### 测试

通过nginx代理进行页面预览

```
server {
        listen       80;
        server_name  www.xuecheng.com
```

添加

```
location /cms/preview/ {
	proxy_pass http://cms_server_pool/cms/preview/;
}

upstream cms_server_pool{
	server 127.0.0.1:31001 weight=10;
}
```



效果

http://www.xuecheng.com/cms/page/preview/5a795ac7dd573c04508f3a56

返回写好的轮播图

![页面预览效果](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/页面预览效果.png)



### 3.2.7 页面发布

#### 业务流程

![页面发布业务](https://github.com/yingeer/xuechengEDU/blob/master/assets/img/cms/%E9%A1%B5%E9%9D%A2%E5%8F%91%E5%B8%83%E6%B5%81%E7%A8%8B.png)



1. 前端请求cms执行页面发布。
2. cms执行静态化程序生成html文件。
3. cms将html文件存储到GridFS中。
4. cms向MQ发送页面发布消息
5. MQ将页面发布消息通知给Cms Client
6. Cms Client从GridFS中下载html文件
7. Cms Client将html保存到所在服务器指定目录

![项目代码 MQ Consumer](https://github.com/yingeer/xuechengEDU/tree/master/cms-client31000)
![项目代码 MQ Producer](https://github.com/yingeer/xuechengEDU/blob/master/xc-service-manage-cms31001/src/main/java/com/xuecheng/manage_cms/controller/CmsPageController.java#L63)



# 业务功能2——课程管理服务

[课程管理服务.md](https://github.com/yingeer/xuechengEDU/blob/master/assets/doc/%E8%AF%BE%E7%A8%8B%E7%AE%A1%E7%90%86%E6%9C%8D%E5%8A%A1.md)

# 总结

1. 虽然技术用的多，但大都感觉都是蜻蜓点水，只是停留在了会配置环境，会基本使用的阶段，没有深入某个技术。

2. 项目感觉是为了用技术而用技术，一上来就用各种中间件。实际上的开发，应该是先实现基本功能后，在生产过程中遇到流量超载，才慢慢引入这些中间件。一个系统不可能一次就建成终极形态，技术的引入应该是适应业务需求的变化。

3. 

4. 
