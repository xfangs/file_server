package com.zhongli.fileserver.controller;

import com.zhongli.devplatform.vo.ConfigVO;
import com.zhongli.devplatform.vo.Res;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@RestController
@Slf4j
public class FileController {

    @Autowired
    private CacheManager cacheManager;

    @PostMapping("/upload")
    public Res upload(@RequestParam("file") MultipartFile multipartFile, String folder) {

        try {
            String baseDir = cacheManager.getCache("dev_platform_system_parameter").get("file:baseDir", String::new);
            File folderFile = new File(baseDir + folder);
            if (!folderFile.exists()) {
                folderFile.mkdir();
            }
            String fileName = multipartFile.getOriginalFilename();
            //修改文件名称 uuid
            String fileUUIDname = UUID.randomUUID().toString();
            //获取后缀
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String NewFileName = fileUUIDname + "." + prefix;
            IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(baseDir + folder + "/" + NewFileName));
            return new Res(NewFileName);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return Res.error("文件上传失败");
        }


    }


    @RequestMapping("/getVideoDuration")
    public Res getVideoDuration(String file, String folder) {
        try {
            String baseDir = cacheManager.getCache("dev_platform_system_parameter").get("file:baseDir", String::new);
            File videoFile = new File(baseDir + folder + "/" + file);
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            long duration = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
            return new Res(duration);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return Res.error("获取视频时长失败");
        }
    }


}
