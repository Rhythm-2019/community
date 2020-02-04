package com.example.community.demo.cache;

import com.example.community.demo.dto.TagDTO;

import javax.swing.text.html.HTML;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    public static List<TagDTO> get(){
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();

        //这里必须new三次
        TagDTO front = new TagDTO();
        front.setCategoryName("前端技术");
        front.setTags(Arrays.asList("HTML","CSS","Javascript","Bootstrap","Vue"));
        tagDTOS.add(front);

        TagDTO server = new TagDTO();
        server.setCategoryName("后端技术");
        server.setTags(Arrays.asList("Java","Tomcat","JSP","Spring","Servlet","Spring Boot"));
        tagDTOS.add(server);

        TagDTO machine = new TagDTO();
        machine.setCategoryName("机器学习");
        machine.setTags(Arrays.asList("Python","OpenCV","NLP","机器学习","深度学习"));
        tagDTOS.add(machine);

        return tagDTOS;
    }

    public static String filterIsValid(String tags){
        String[] tagList = tags.split(",");
        //先把他展评
        List<TagDTO> sta = TagCache.get();
        List<String> staList = sta.stream().flatMap(tagDTO -> tagDTO.getTags().stream()).collect(Collectors.toList());
        String inValid = Arrays.stream(tagList).filter(t -> !staList.contains(t)).collect(Collectors.joining(","));
        return inValid;
    }
}
