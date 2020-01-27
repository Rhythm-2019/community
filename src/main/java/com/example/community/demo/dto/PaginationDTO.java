package com.example.community.demo.dto;


import com.example.community.demo.mapper.QuestionMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Data
public class PaginationDTO {
    //继续封装 这个list是给前端显示用的
    private List<QuestionDTO> questionDTOS;
    //是否展示回到首页的按钮
    private boolean showFirstPage;
    //是否显示去往最后一页的按钮
    private boolean showEndPage;
    //是否显示上一页的按钮
    private boolean showPrevious;
    //是否显示下一页的按钮
    private boolean showNext;
    //当前页数
    private Integer page;
    //要显示出来的所有页面
    private List<Integer> pages = new ArrayList<Integer>();
    //一共有多少数据
    private Integer totalCount;
    //页数 前端要用
    private Integer totalPages;

    @Autowired
    private QuestionMapper questionMapper;
    //totalCoun是值一共有多少条问题，page是值当前要查询第几页，size是一页要显示多少条
    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.totalCount = totalCount;
        //计算一下到底有多少页
        Integer totalPages;
        if(totalCount % size == 0) {
            totalPages = totalCount / size;
        }else {
            totalPages = totalCount / size + 1;
        }
        this.totalPages = totalPages;
        if(page <= 0){
            page = 1;
        }
        if(page >totalPages){
            page = totalPages;
        }
        this.page = page;
        //要在pages里面加入当前要显示的页码了
        //如果当前页码是5，则向前显示三个（2 3 4），向后显示三个（6 7 8）
        pages.add(page);
        for(int i = 1; i <= 3; i++){
            if (page - i > 0){
                pages.add(0,page-i);
            }
            if (page + i <= totalPages){
                pages.add(page + i);
            }
        }
        //制定一些规则
        //当为首页时，不显示上一页和回到首页
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }
        //若果是尾页不显示去到尾页和下一页
        if(page == totalPages){
            showNext = false;
        }else {
            showNext = true;
        }
        //列表里有第一页就不显示回到首页的按钮
        if(pages.contains(1)){
            showFirstPage = false;
        }else{
            showFirstPage = true;
        }
        //列表里有最后一页，就不显示去往尾页了
        if(pages.contains(totalPages)){
            showEndPage = false;
        }else{
            showEndPage = true;
        }


    }
}
