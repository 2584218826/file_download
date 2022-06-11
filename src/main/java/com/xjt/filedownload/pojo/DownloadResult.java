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
public class DownloadResult {
    /**
     * 已下载进度
     */
    Integer downloaded;
    /**
     * 文件大小
     */
    Integer fileSize;
    /**
     * 下载百分比
     */
    String downRate;
}
