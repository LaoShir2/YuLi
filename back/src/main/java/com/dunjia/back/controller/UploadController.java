package com.dunjia.back.controller;

import com.aliyuncs.exceptions.ClientException;
import com.dunjia.back.pojo.Result;
import com.dunjia.back.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    AliOSSUtils aliOSSUtils = new AliOSSUtils();

    public UploadController() throws ClientException {
    }

    // 上传图片到云端
    @PostMapping("/upload")
    public Result upload(@RequestParam("image") MultipartFile image) throws IOException {
        log.info("上传文件, 文件名：{}", image.getOriginalFilename());

        // 上传到阿里云OSS
//        String url = aliOSSUtils.upload(image);
        // 上传到本地
        String url = aliOSSUtils.localUpload(image);
        log.info("上传成功，url：{}", url);

        return Result.success(url);
    }

}
