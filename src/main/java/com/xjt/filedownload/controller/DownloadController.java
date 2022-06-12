package com.xjt.filedownload.controller;

import com.xjt.filedownload.pojo.DownloadResult;
import com.xjt.filedownload.util.FileUtil;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 15:45
 **/
@RestController
public class DownloadController {

    ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @PostMapping("/download")
    public String download(@RequestBody Map<String, String> map){
        if (map.containsKey("downUrl")){
            String downUrl = map.get("downUrl");
            if (!downUrl.contains("http") || !downUrl.contains("https")){
                return "下载地址不合法";
            }
            String path = "/Users/xujiangtao/test";
            String[] split = downUrl.split("/");
            String fileName = split[split.length-1];
            try {
                FileUtil.downLoadFromUrl(downUrl,fileName,path);
            }catch (Exception e){
                File file = new File(path + File.separator + fileName);
                if (file.exists()){
                    file.delete();
                }
                e.printStackTrace();
                return "文件下载异常";
            }
        }else {
            return "下载地址为空";
        }
        return "下载成功";
    }

    @GetMapping("/getDownResult/{fileName}")
    public DownloadResult getDownResult(@PathVariable String fileName){
        return FileUtil.down.get(fileName);
    }
}
