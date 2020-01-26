package com.example.community.demo.controller;

import com.example.community.demo.mapper.QuestionMapper;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.QuestionModel;
import com.example.community.demo.model.UserModel;
import org.h2.command.ddl.CreateTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name = "title") String title,
                            @RequestParam(name = "desc") String desc,
                            @RequestParam(name = "tag") String tag,
                            HttpServletRequest request,
                            Model model) {

        model.addAttribute("title",title);
        model.addAttribute("desc",desc);
        model.addAttribute("tag",tag);

        //前后端都要验证一下是否为空
        if (title == null || "".equals(title)) {
            model.addAttribute("error", new String(("标题不能为空")));
            return "publish";
        }
        if (desc == null || "".equals(desc)) {
            model.addAttribute("error", new String(("问题补充不能为空")));
            return "publish";
        }
        if (tag == null || "".equals(tag)) {
            model.addAttribute("error", new String(("标签不能为空")));
            return "publish";
        }

        UserModel user = null;
        //进行登录验证
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    System.out.println(token);
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        break;
                    } else {
                        //有token但是不对的
                        model.addAttribute("error", new String(("您当前的登录状态异常，请重新登录")));
                        return "publish";
                    }
                }
            }
        }
        //根本没有token的情况
        if (user == null) {

            model.addAttribute("error", new String(("请先登录再提问！")));
            return "publish";
        }

        //写入数据库
        QuestionModel questionModel = new QuestionModel();
        questionModel.setTitle(title);
        questionModel.setDescription(desc);
        questionModel.setTag(tag);
        questionModel.setGmtCreate(System.currentTimeMillis());
        questionModel.setGmtModified(questionModel.getGmtCreate());
        questionModel.setCommentCount(0);
        questionModel.setLikeCount(0);
        questionModel.setViewCount(0);
        //需要接受一下当前的用户id，突然想起要进行登录验证
        questionModel.setCreator(user.getId());
        questionMapper.createQuestion(questionModel);

        return "index";
    }


}
