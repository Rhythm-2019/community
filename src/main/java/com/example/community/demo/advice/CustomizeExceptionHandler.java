package com.example.community.demo.advice;

import com.alibaba.fastjson.JSON;
import com.example.community.demo.dto.ResultDTO;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response) {
        //这里需要判断一下错误是要返回html还是json
        //返回的类型是请求头规定的
        String contentType = request.getContentType();
        //contentType.indexOf("JSON") != -1 不是对 不写contentType默认html
        if("application/JSON".equals(contentType)){
            //JSON
            HttpStatus status = getStatus(request);
            ResultDTO resultDTO = new ResultDTO();
            if (e instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            }else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SERVER_ERROR);
            }
            try {
                response.setContentType("application/JSON");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.write(JSON.toJSONString(resultDTO));
                printWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }else{
            //HTML
            HttpStatus status = getStatus(request);
            if (e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else{
                log.error("error:{}",e.getMessage());
                model.addAttribute("message","服务器炸了，正在抢修您晚点再来吧！");
            }

            return new ModelAndView("error");
        }

    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
