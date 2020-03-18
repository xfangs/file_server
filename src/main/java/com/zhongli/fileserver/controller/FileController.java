package com.zhongli.fileserver.controller;

import com.alibaba.fastjson.JSON;
import com.zhongli.devplatform.vo.Res;
import com.zhongli.fileserver.utils.VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@RestController
@Slf4j
public class FileController {


    @Value("${dev-platform.file-dir}")
    private String baseDir;


    @PostMapping("/upload")
    public Res upload(
            @RequestPart("file") MultipartFile multipartFile
            , String folder
    ) {

        try {
            File folderFile = new File(baseDir + folder);
            if (!folderFile.exists()) {
                boolean mkdir = folderFile.mkdir();
                if (!mkdir) {
                    log.error("创建文件夹失败");
                }
            }
            String fileName = multipartFile.getOriginalFilename();
            //修改文件名称 uuid
            String fileUUIDname = UUID.randomUUID().toString();
            //获取后缀
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = fileUUIDname + "." + prefix;
            String saveFilePath = baseDir + folder + "/" + newFileName;
            IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(saveFilePath));


            return new Res(newFileName);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return Res.error("文件上传失败");
        }


    }


    @PostMapping("/uploadVideo")
    public Res uploadVideo(
            @RequestPart("file") MultipartFile multipartFile
            , String folder
            , @RequestParam(defaultValue = "false") String getDuration
            , @RequestParam(defaultValue = "false") String getVideoCoverImage
            , String coverImageFolder
    ) {

        try {
            File folderFile = new File(baseDir + folder);
            if (!folderFile.exists()) {
                boolean mkdir = folderFile.mkdir();
                if (!mkdir) {
                    log.error("创建文件夹失败");
                }
            }
            String fileName = multipartFile.getOriginalFilename();
            //修改文件名称 uuid
            String fileUUIDname = UUID.randomUUID().toString();
            //获取后缀
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = fileUUIDname + "." + prefix;
            String saveFilePath = baseDir + folder + "/" + newFileName;
            IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(saveFilePath));

            Map<String, Object> fileMap = new HashMap<>();
            fileMap.put("fileName", newFileName);
            File videoFile = null;
            if ("true".equals(getDuration)) {
                videoFile = new File(saveFilePath);
                fileMap.put("duration", VideoUtil.getVideoDuration(videoFile));
            }
            if ("true".equals(getVideoCoverImage)) {

                if (videoFile == null) {
                    videoFile = new File(saveFilePath);
                }

                File coverImageFolderFile = new File(baseDir + coverImageFolder);
                if (!coverImageFolderFile.exists()) {
                    boolean mkdir = coverImageFolderFile.mkdir();
                    if (!mkdir) {
                        log.error("创建文件夹失败");
                    }
                }
                String imgFileName = UUID.randomUUID().toString() + ".jpg";
                String imgPatch = baseDir + coverImageFolder + "/" + imgFileName;
                VideoUtil.getVideoPic(videoFile, imgPatch);
                fileMap.put("coverImage", imgFileName);
            }
            return new Res(fileMap);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return Res.error("文件上传失败");
        }


    }


    @RequestMapping("/getVideoDuration")
    public Res getVideoDuration(String file, String folder) {
        try {
            File videoFile = new File(baseDir + folder + "/" + file);

            return new Res(VideoUtil.getVideoDuration(videoFile));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return Res.error("获取视频时长失败");
        }
    }


    @RequestMapping("/getVideoCoverImages")
    public Res getVideoCoverImages(String files, String folder, String saveFolder) {

        try {
            List<String> coverImages = new ArrayList<>();
            List<String> fileList = JSON.parseArray(files, String.class);
            for (String file : fileList) {
                File videoFile = new File(baseDir + folder + "/" + file);

                String imgFileName = UUID.randomUUID().toString() + ".jpg";
                String imgPatch = baseDir + saveFolder + "/" + imgFileName;
                VideoUtil.getVideoPic(videoFile, imgPatch);
                coverImages.add(imgFileName);
            }
            return new Res(coverImages);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return Res.error("生成视频封面失败");
        }
    }


}
