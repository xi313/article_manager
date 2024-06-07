package com.example.controller;


import com.example.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //把文件内容存储到本地磁盘
        String originalFilename = file.getOriginalFilename();
        //保证文件名字唯一
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File("C:\\Users\\shuyuan\\Desktop\\2024春\\软件工程\\big-event-files\\" + filename));
        return Result.success("url");
    }
}
