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
[Lombok 文档](https://projectlombok.org/)  
[Thymeleaf 官方文档](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)  
[MyBatis Generator 文档](http://mybatis.org/generator/)
### 笔记
* 注解  
GetMapping Controller Autowired Value Component RequestParam  Configuration


* Git命令  
git statue  
git add .  
git commit -m "something"  
git push

* 运行mybatis generator  
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
####  IDEA 快捷键  
ctrl + alt + v  快速创建变量  
ctrl + d  复制代码  
ctrl + alt + n 搜索文件  
shift + f6 快速命名文件或变量  
alt + insert 生成代码  
ctrl + alt + o  快速去除多余的引入  
shift + enter  快速到下一行首  
ctrl + P 参数提示   
ctrl + E 打开最近文件窗口  
ctrl + X  删除代码  
alt + enter 修正代码  
ctrl + shift + L  格式化
F2  来到错误的地方  
ctrl + shift + up/down  代码向上移动或向下移动
ctrl + alt + left 回退
ctrl + =

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
    AVATAR_URL VARCHAR(256),
	constraint USER_PK
		primary key (ID)
);
<<<<<<< HEAD

create table question
(
	id int auto_increment,
	title varchar(50),
	description text,
	gmt_create bigint,
	gmt_modified bigint,
	creator int,
	comment_count int default 0,
	view_count int default 0,
	like_count int default 0,
	tag varchar(256),
	constraint question_pk
		primary key (id)
);

```

### 记录步骤  
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
4. 把常用的url地址放到xml里面
##### 2020.01.23
1. 引入H2，建表
2. 引入Mybatis，编写DAO(UserMapper)和UserModel
3. 登录后用UUID生成自己的token，写入数据库中
4. 将自己的token通过cookie发送回浏览器保持登录状态（避免服务器突然宕机）
5. 在数据库中加入bio字段，并引入FlyWay实现同步
6. 编写前端publish.html页面，这里css不知道为什么不能更新
##### 2020.01.24 除夕
1. 解决css不能更新的问题，顺便弄了热部署
2. 建立question表，编写QuestionModel和QuestionMapper
3. 发现css和html刷新是可以更新的，但是class不行
4. 接受post请求，编写controller处理一下
5. 为了偷懒不用写getter和setter，引入lombok，在dto中加入@Data注解，
虽然能运行但是还是会报错，所以要在setting里面的plugin安装lombok插件
6. 在user表中加入头像url,修复当我在登录前吧cookie请了是登录不了的bug
##### 2020.01.25 初一
1. 想歇，编写首页展示列表的html
2. 完成展示问题的业务，这里注意需要查询两个表，所以需要用到service层,
这里需要处理一下mybatis的驼峰问题
3. 修正了publish时用户填写不全需要回写的情况
##### 2020.01.26 初二  
1. 实现一下分页功能，发现是需要对整个页面的信息重新封装，把QuestionDTO封装到PaginationDTO里
2. 这个视频中的DTO其实等同于我理解的VO，这里的model等同于我理解的DTO
##### 2020.01.27 初三
1. 还在搞昨天的，把导航栏抽离出来；
2. 编写我的提问页面
3. 抽取登录验证
的代码到拦截器里面，拦截器的写法是先写MyConfig继承WebMVCConfigurer接口，编写addinterceptors的函数，
对registry添加注册器，传递一个封装了自己代码的HandleInterceptor的类，并加上拦截地址。
##### 2020.01.28  
1. 编写详情页面
2. 登录有bug，每次授权登录都要创建，不是很好，所以改成每次登录先校验一下accountId，数据库有就修改没有就
创建
3. 完成退出登录
4. 用mybatis generator生成代码：先写generatorConfig，然后运行命令，在run和xml里配置好，最后修改自己的代码
5. 修正自己的代码的过程中会发现一些问题，分页需要引用插件org.mybatis.generator.plugins.RowBoundsPlugin
##### 2020.01.29  
1. 处理异常，编写error页面，处理一下异常
##### 2020.01.30
1. 继续处理异常，先编写error.html，发生异常时spring boot的BasicErrorController会自动路由到error.html页面上。我们需要编写在自己
的service里面找到会发生异常的地方，主动抛出异常，异常的类型可以自定义，再写advice对抛出的异常进行拦截，将异常信息
传给error.html。但是有一些服务器或者客户端异常我们捕获不了，可以编写controller获取状态码，对状态码进行判断。
2. 实现阅读数增加的功能，自定义了一个mapper
##### 2020.01.31
1. 实现回复功能，建表.
2. 编写服务端的接口，用postman模拟请求，注意要设置请求头为application/JSON的格式，模拟的时候发现我如果少了参数服务器会报错，所以
要判断一下参数是不是为空，这些逻辑要抽离到service层，通过判断值是否为空，抛出异常，并对错误码和错误信息进行了封装。
3. 编写添加评论的接口，通过postman进行测试，加入事务@transactional
4. 修改question.html并加入js，通过ajax调用写好的接口添加评论
##### 2020.02.01
1. 实现展示回复的功能，这里尝试使用了java8的新特性，也可以用首页的方式

##### 2020.02.02
1. 实现二级回复功能的前端逻辑，对接口重构
2. 修复了一下bug
3. 完成question页面的标签的显示，相关问题的显示

##### 2020.02.03
1. 完成标签的添加

##### 2020.02.04 
1. 完成消息提示


### 记录问题  
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
* 修改css后重启服务器有时候还是无效，要把文件删了重新创建才行
* 关于热部署需要进行两部操作，第一步在Edit Configuration里面选择update classes 
and resource 然后ctrl alt shift / 选择registry 勾选compiler.automake.allow.when.app.running
* FlyWay常常会出错，可以用repair指令修复
* 关于css不更新的问题，需要在浏览器访问http://localhost:8080/css/community.css才会更新
* mybatis驼峰自动识别：mybatis.configuration.map-underscore-to-camel-case=true
* fastJSON可以自动识别驼峰
* 写mapper的时候不能重名
* 为什么要用redirect:/跳转，是因为直接return页面的地址是不变的，用redirect才回变
* 官方的分页插件
```xml
<plugin xmlns="org.mybatis.generator.plugins.RowBoundsPlugin"></plugin>>
```
* bootstrap里面英文文本不自动换行，可以加上这个样式word-break: break-all;