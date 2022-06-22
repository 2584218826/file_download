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
}
