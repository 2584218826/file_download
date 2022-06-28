package com.xjt.filedownload.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 17:44
 **/
@Data
@AllArgsConstructor
public class FileInfo {
    /**
     * 文件名称
     */
    String fileName;
    /**
     * 文件大小
     */
    String fileSize;
    /**
     * 下载用时
     */
    String downTime;

    /**
     * 下载地址
     */
    String fileUrl;

    String createTime;

    public FileInfo(String fileName,String createTime,String fileUrl){
        this.fileName = fileName;
        this.createTime = createTime;
        this.fileUrl = fileUrl;
    }

    public FileInfo(String fileName, String fileSize, String downTime, String fileUrl) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.downTime = downTime;
        this.fileUrl = fileUrl;
    }

    public FileInfo(){

    }
}
