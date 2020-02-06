package com.example.community.demo.controller;


import com.example.community.demo.dto.FileUploadDTO;
import com.example.community.demo.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileUploadController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @ResponseBody
    @RequestMapping(value = "/file/upload")
    public FileUploadDTO fileUpload(HttpServletRequest request) {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");

        try {
            String url = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileUploadDTO fileUploadDTO = new FileUploadDTO();
            fileUploadDTO.setSuccess(1);
            fileUploadDTO.setMessage("Success");
            fileUploadDTO.setUrl(url);
            return fileUploadDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }

         return null;
    }
}
