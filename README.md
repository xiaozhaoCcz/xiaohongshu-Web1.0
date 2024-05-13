# 仿小红书web 1.0

<p align=center>
   仿小红书设计的一个基于微服务架构的前后端分离项目
</p>
<p align="center">

## 项目介绍
此项目**基于微服务架构的前后端分离系统**。**Web** 端使用 **Vue3** +**ts**+**ElementUi** 。后端使用 **SpringBoot** + **Mybatis-plus**进行开发，使用 **ElasticSearch**  作为全文检索服务，使用**webSocket**做聊天和消息推送。

- 所有功能都是自己一个开发完成，1.0版本可能还存在些小bug。
- **推荐项目** 如果想自己做一个仿小红书项目，我这里也提供了免费的前端仿小红书源码 https://gitee.com/xzjsccz/xiaohongshu
- **推荐项目** excel校验工具项目https://gitee.com/xzjsccz/cc-excel-util, 使用反射和设计模式，可以写在简历上绝对是一个加分项目

**数据库文件在`doc/yanhuo-test.sql`**

**这个项目尽量做得简单，所以没有考虑接口的性能，所有功能都是直接对数据库进行操作**

**目前git上面是1.0版本，基本功能都完成，2.0版本花费时间更多，因为之前项目白嫖太多2.0版本决定收费(演示的项目也是2.0版本)，定价**120**元，维护服务器的费用，后续bug也会在2.0版本上修改。**

[qq群] **879599115**，需要2.0版本源码加微信**18572755162**

**2.0版本优势**
- 包括微服务项目和单体项目，支持多服务启动和单服务启动
- 新增注册功能，优化代码bug
- 使用mq+redis优化点赞,收藏,评论功能，提升效率
- 同步记录用户的登陆信息(ip,城市，浏览器类型)
- 使用elasticsearch做聚合查询，按条件搜索对应的数据
- 私聊消息支持发送图片
- 新增minio存储
- other模块有自己学习的一些技术代码
-  ![image text](./doc/img/k.png)
- ![image text](./doc/img/k2.png)

## 重点
- 此项目没有后台管理系统，还有这个项目不适合初学者，别买了又来找我安装环境，帮运行部署，买了以后不支持退款，请考虑好再买！！！
- 需要自己配置七牛云的oss服务或minio服务，不熟悉的先去了解


## 项目地址
http://ccimgvideo.top/

## API接口文档
https://console-docs.apipost.cn/preview/43d05f344c62117e/63d199bf052ccf41

## 运行启动

**前端启动**

下载项目进入`yanhuo-web`中

1.下载依赖
```agsl
yarn install 
```
2.启动项目
```agsl
npm run dev
```
3.启动成功后页面
![image text](./doc/img/front1.png)

![image text](./doc/img/front2.png)


**后端启动**

- 首先需要把`redis`，`elasticsearch`，`mysql` 安装好启动。

- 如果是本地上传，需要修改图片保存地址(目前本地保存有bug)，不然上传图片会失败(默认使用七牛云存储)
  ![image text](./doc/img/admin1.png)
- 如果是使用七牛云存储图片，需要配置七牛云的oss地址(默认使用七牛云存储，因此可以也可以不用修改本地文件地址)
  ![image text](./doc/img/19.png)
- 修改每个项目下的数据库账号密码，如果有es账号密码需要修改 `yanhuo-search`下的配置文件
  ![image text](./doc/img/admin2.png)
  ![image text](./doc/img/admin3.png)
- 启动`yanhuo-auth`,`yanhuo-gateway`,`yanhuo-im`,`yanhuo-platform`,`yanhuo-search`,`yanhuo-util`即可运行项目。

- 启动项目后会发现没有任何数据，这是因为es中还没有同步数据，我们需要启动`xxl-job`（运行xxl-job这里就不展开了，去git上面搜索xxl-job然后下载下来更改数据库后就可以启动了）并配置数据
  - xxl-job添加执行器
    ![image text](./doc/img/admin4.png)
  - 添加定时任务
    ![image text](./doc/img/5.png)
  - 然后运行任务，观察日志运行情况，如果显示成功则数据同步成功
    ![image text](./doc/img/6.png)
### 成功运行

![image text](./doc/img/7.png)

## 联系我
如果有什么问题可以加群讨论，群号：879599115

![image text](./doc/img/8.png)


## 后期维护
这个项目尽量做得简单，所以没有考虑接口的性能，所有功能都是直接对数据库进行操作。后续会对接口慢慢优化。

## 项目特点及功能

- 使用springboot+mybatis_plus+vue3+ts+websocket技术
- 使用gateway做网关过滤，对发送的请求做过滤。（部分请求放行，比如登录请求，首页数据请求）
- 支持本地图片存储，七牛云存储。
- 使用ElasticSearch做内容搜索
- 使用websocket做私信聊天和实时通知
- 使用redis做对象缓存
- 采用elementui完成页面搭建

## 项目目录

- yanhuo-web 前段页面
- yanhuo-auth 认证服务
- yanhuo-common 公共模块,存放一些工具类或公用类
- yanhuo-platform 烟火app主要功能模块
- yanhuo-im 聊天模块
- yanhuo-search 搜索模块
- yanhuo-util  第三方服务模块，邮箱短信，oss对象存储服务
- yanhuo-xo  对象存放模块

## 技术选型

### 后端技术

|      技术       |      版本       |      
|:-------------:|:-------------: 
|  SpringBoot   | 2.3.2.RELEASE |  
|  openfeign	   |       -       |
| MyBatis-Plus  |       -       |          
| Elasticsearch |    7.16.3     |   
|     Redis     |     4.2.2     |
|      JWT      |     0.7.0     |                
|    Lombok     |       -       |
|     Nginx     |    1.12.2     |         
|    Hutool     |       -       |               
|   websocket   | 2.3.2.RELEASE |   

### 前端技术

|   技术    | 版本 |      
|:-------:|:--: 
| nodejs	 | -  |  
| vue3		  | -  |
| axios	  | -  |  
|  其他组件   | -  |  

## 项目截图

|                                 |                                 |
|:-------------------------------:|:-------------------------------:|
| ![image text](./doc/img/7.png)  | ![image text](./doc/img/9.png)  |
| ![image text](./doc/img/10.png) | ![image text](./doc/img/11.png) |
| ![image text](./doc/img/16.png) | ![image text](./doc/img/12.png) |
| ![image text](./doc/img/13.png) | ![image text](./doc/img/14.png) |
| ![image text](./doc/img/15.png) | ![image text](./doc/img/18.png) |
| ![image text](./doc/img/17.png) |  |

## 视频演示
https://www.bilibili.com/video/BV1kN4y1n7gz/?spm_id_from=333.1007.top_right_bar_window_history.content.click