package com.example.community.demo.controller;

import com.example.community.demo.cache.TagCache;
import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.mapper.QuestionMapper;
import com.example.community.demo.model.Question;
import com.example.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("categorys", TagCache.get());

        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name = "title",required = false) String title,
                            @RequestParam(name = "desc",required = false) String desc,
                            @RequestParam(name = "tag",required = false) String tag,
                            @RequestParam(name = "id",required = false) Integer id,
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
        String tagInValid = TagCache.filterIsValid(tag);
        if (!"".equals(tagInValid) || tagInValid != null){
            model.addAttribute("error", new String(("不合法的标签：" + tagInValid)));
            return "publish";
        }

        GitHubUserDTO gitHubUserDTO = (GitHubUserDTO) request.getSession().getAttribute("userInfo");

        //根本没有token的情况
        if (gitHubUserDTO == null) {

            model.addAttribute("error", new String(("请先登录再提问！")));
            return "publish";
        }

        //写入数据库
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(desc);
        question.setTag(tag);
        question.setId(id);
        //需要接受一下当前的用户id，突然想起要进行登录验证
        question.setCreator(gitHubUserDTO.getId());
        //这里要判断一下是更新还是创建
        questionService.createOrUpdate(question);

        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                        Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("id",id);
        model.addAttribute("title",questionDTO.getTitle());
        model.addAttribute("desc",questionDTO.getDescription());
        model.addAttribute("tag",questionDTO.getTag());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }


}
