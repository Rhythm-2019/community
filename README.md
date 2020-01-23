##社区小项目的记录

##工具
[Git](https://git-scm.com/)  
[Maven 仓库](https://mvnrepository.com/)  
[ProcessOn 时序图绘制](https://www.processon.com/)  

##文档
[Spring serving web content 文档](https://spring.io/guides/gs/serving-web-content/)  
[Github OAuth API 文档](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)    
[Bootstrap 文档](https://v3.bootcss.com/)  
[OKHttp 文档](https://square.github.io/okhttp/)  
[H2 文档](http://www.h2database.com/html/quickstart.html)  
[MyBatis文档](https://mybatis.org/mybatis-3/getting-started.html)
##笔记
注解  
GetMapping Controller Autowired Value Component RequestParam

Git命令  
git statue  
git add .  
git commit -m "something"  
git push

记录步骤
1. 编写hello World并运行
2. Git上传代码到GitHub  
3. 导入bootstrap，编写导航栏
4. 注册GitHub OAuth
5. 实现授权登录（前端点击Login携带字段跳转到GitHub，GitHub授权完
毕跳转回redireact_url并返回code，我们需要变现GitHubProvider中的
getaccesstoken携带code模拟发送http请求从而获得access_token，最
后编写getuserinfo方法携带access_token模拟发送http请求获取用户信
息）
6. 将登录状态存入session，修改前端页面的登录状态
