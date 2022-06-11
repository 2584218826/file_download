package com.xjt.filedownload.controller;

import com.xjt.filedownload.util.FileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 15:45
 **/
@RestController
public class DownloadController {

    @PostMapping("/download")
    public String download(@RequestBody Map<String, String> map){
        if (map.containsKey("downUrl")){
            String downUrl = map.get("downUrl");
            if (!downUrl.contains("http") || !downUrl.contains("https")){
                return "下载地址不合法";
            }
            String path = "C:/test";
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
}
