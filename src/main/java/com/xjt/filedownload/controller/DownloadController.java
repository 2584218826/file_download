package com.xjt.filedownload.controller;

import com.xjt.filedownload.pojo.DownloadResult;
import com.xjt.filedownload.pojo.FileInfo;
import com.xjt.filedownload.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 15:45
 **/
@RestController
@Slf4j
public class DownloadController {

    @Value("${savePath:C:/test}")
    String path;

    @Value("${fileUrl}")
    String fileUrl;

    @Autowired
    FileUtil fileUtil;

    ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @PostMapping("/download")
    public String download(@RequestBody Map<String, String> map) throws ExecutionException, InterruptedException {
        String downUrl = map.get("downUrl");
        if (!downUrl.contains("http") && !downUrl.contains("https")) {
            return "下载地址不合法";
        }
        String[] split = downUrl.split("/");
        String fileName = split[split.length - 1];
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        if (fileName.contains("?")){
            fileName = fileName.split("\\?")[0];
        }
        fileUtil.down.put(fileName, new DownloadResult("0MB", "正在获取...", "0%"));
        String finalFileName = fileName;
        Future<String> submit = threadPool.submit(() -> {
            if (map.containsKey("downUrl")) {
                try {
                    fileUtil.downLoadFromUrl(downUrl, finalFileName, path);
                } catch (Exception e) {
                    File file = new File(path + File.separator + finalFileName);
                    if (file.exists() && !file.isDirectory()) {
                        file.delete();
                    }
                    e.printStackTrace();
                    return "文件下载异常";
                }finally {
                    fileUtil.down.remove(finalFileName);
                }
            } else {
                return "下载地址为空";
            }
            return "下载成功";
        });
        return submit.get();
    }

    @GetMapping("/getDownResult/{fileName}")
    public DownloadResult getDownResult(@PathVariable String fileName){
        return fileUtil.down.get(fileName);
    }

    @GetMapping("/queryFiles")
    public List<FileInfo> queryFiles(){
        File file = new File(path);
        List<FileInfo> fileInfos = new ArrayList<>();
        if (file.exists() && file.isDirectory()){
            List<File> files = Arrays.asList(fileUtil.orderByDate(path));
            int size = files.size();
            if (size>10){
                files = files.subList(0, 11);
            }
            for (File f : files) {
                String createTime = "";
                try {
                    BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);

                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    createTime = simpleDateFormat.format( new Date( attr.lastModifiedTime().toMillis() ) );
                } catch (IOException e) {
                    log.error("获取文件属性异常");
                }
                String fileName = URLDecoder.decode(f.getName(), StandardCharsets.UTF_8);
                FileInfo fileInfo;
                if (f.isFile() && !fileName.equals(".DS_Store")){
                    fileInfo = new FileInfo(fileName, createTime, fileUrl + File.separator + fileName);
                    fileInfo.setFileSize(fileUtil.getFileSize((int) f.length()));
                    fileInfos.add(fileInfo);
                }
            }
        }
        return fileInfos;
    }

    @GetMapping("/getFileInfo/{fileName}")
    public FileInfo getFileInfo(@PathVariable String fileName){
        FileInfo fileInfo = fileUtil.fileInfo.get(fileName);
        fileUtil.fileInfo.remove(fileName);
        System.out.println(fileInfo);
        return fileInfo;
    }
}
