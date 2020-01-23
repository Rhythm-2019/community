## 社区小项目的记录

###  工具  
[Git](https://git-scm.com/)  
[Maven 仓库](https://mvnrepository.com/)  
[ProcessOn 时序图绘制](https://www.processon.com/)  
[FastJSON]()

### 文档
[Spring serving web content 文档](https://spring.io/guides/gs/serving-web-content/)  
[Github OAuth API 文档](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)    
[Bootstrap 文档](https://v3.bootcss.com/)  
[OKHttp 文档](https://square.github.io/okhttp/)  
[H2 文档](http://www.h2database.com/html/quickstart.html)  
[Spring boot 配置 MyBatis文档](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/#)  
[Spring 默认数据库连接池文档](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)
[FlyWay 文档](https://flywaydb.org/getstarted/firststeps/maven)

### 笔记
注解  
GetMapping Controller Autowired Value Component RequestParam

Git命令  
git statue  
git add .  
git commit -m "something"  
git push

### 脚本
```sql
create table USER
(
	ID INT auto_increment,
	ACCOUNT_ID VARCHAR(100),
	NAME VARCHAR(50),
	TOKEN CHAR(36),
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
    BIO VARCHAR(100),
	constraint USER_PK
		primary key (ID)
);
```
  
###记录步骤  
##### 2020.01.21  
1. 编写hello World并运行，需要引用spring-boot-starter-web
2. Git上传代码到GitHub    
3. 导入bootstrap，编写导航栏    

##### 2020.01.22
1. 注册GitHub OAuth
2. 实现授权登录（前端点击Login携带字段跳转到GitHub，GitHub授权完
毕跳转回redireact_url并返回code，我们需要变现GitHubProvider中的
getaccesstoken携带code等4个字段以json格式模拟发送http请求从而获
得access_token，最后编写getuserinfo方法携带access_token模拟发
送http请求获取用户信息，用户信息是json格式的）
3. 将登录状态存入session，修改前端页面的登录状态

##### 2020.01.23
1. 引入H2，建表
2. 引入Mybatis，编写DAO(UserMapper)和UserModel
3. 登录后用UUID生成自己的token，写入数据库中
4. 将自己的token通过cookie发送回浏览器保持登录状态（避免服务器突然宕机）
5. 在数据库中加入bio字段，并引入FlyWay实现同步
6. 编写前端publish.html页面

###记录问题
* 在写hello world时要用到模本thymeleaf，注意引用正确
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
```
* 在引用MyBatis时，xml文件的写法：  
```xml
    <!--官网版本-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.0</version>
    </dependency>
    <!--修正版本-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
```
* OAuth 里面获取token的URL是：https://api.github.com/user?access_token=xxxxx



