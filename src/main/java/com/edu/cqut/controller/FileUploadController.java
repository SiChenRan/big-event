package com.edu.cqut.controller;

import com.edu.cqut.pojo.Result;
import com.edu.cqut.utils.AliOSSUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin

public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {

        String originalName = file.getOriginalFilename();
        String filename = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));

//        file.transferTo(new File("C:/Users/m1323/Desktop/files/" + filename));
        String url = AliOSSUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }

}
