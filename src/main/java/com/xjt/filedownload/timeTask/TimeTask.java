package com.xjt.filedownload.timeTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-13 09:34
 **/
@Service
@Slf4j
public class TimeTask {

    @Value("${savePath:C:/test}")
    String path;

    /**
     * 定时删除文件
     * 每周一，两点删除所有下载文件
     */
    @Scheduled(cron = "0 0 2 ? * 2")
    public void timingDeleteFile(){
        File file = new File(path);
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
            log.info("文件已清空...");
        }
    }
}
