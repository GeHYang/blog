package com.yang.blog.upload.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yang.blog.model.CodeConstant;
import com.yang.blog.model.Result;
import com.yang.blog.model.StatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author: Yang
 * @create: 2023-04-24
 * @Description:
 */
@RestController
@CrossOrigin(value = "*", maxAge = 3600)
public class UploadController {

    @Value("${base.staticPath}")
    private String staticPath;
    private String absPath = System.getProperties().getProperty("user.dir") + "/base/src/main/resources/static/image/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file){

        if(file.isEmpty()){
            return Result.ok(false, CodeConstant.ERROR, "上传失败");
        }
        String filename = file.getOriginalFilename();
        // 文件后缀名
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 生成随机文件名
        long id = IdWorker.getId();
        // 拼接文件名
        filename = id + suffix;

        File newFile = new File(absPath + filename);
        try {
            file.transferTo(newFile);
            return Result.ok(true, CodeConstant.ERROR, "上传成功", staticPath + "/image/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(false, CodeConstant.ERROR, "上传失败");
    }


    @DeleteMapping("/{fileName}")
    public Result deleteFile(@PathVariable("fileName") String fileName){
        File file = new File(absPath + fileName);
        if(file.exists()){
            file.delete();
        }
        return Result.ok(StatusEnum.REQUEST_SUCCESS);
    }
}
