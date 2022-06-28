package com.xjt.filedownload.util;

import com.xjt.filedownload.pojo.DownloadResult;
import com.xjt.filedownload.pojo.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: fileDownload
 * @description: 下载文件工具类
 * @author: xujt
 * @date: 2022-06-11 15:48
 **/
@Service
public class FileUtil {

    public Map<String, DownloadResult> down = new HashMap<>();
    public Map<String, FileInfo> fileInfo = new HashMap<>();

    @Value("${fileUrl}")
    String fileUrl;

    public void downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        long l = System.currentTimeMillis();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("Accept-Encoding", "identity");
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        int contentLength = conn.getContentLength();
        System.out.println(contentLength);
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        saveInputStreamToFile(inputStream,fileName,savePath,contentLength);
        Long time = System.currentTimeMillis()-l;
        String finalFileUrl = fileUrl+File.separator+fileName;
        fileInfo.put(fileName, new FileInfo(fileName, getFileSize(contentLength), getTime(time), finalFileUrl));
        inputStream.close();
    }

    public void saveInputStreamToFile(InputStream inputStream,String fileName,String path,Integer fileSize) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        File file = new File(path+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        int available = inputStream.available();
        int downSize = 0;
        while((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            downSize+=buffer.length;
            BigDecimal b = new BigDecimal((double) downSize/fileSize);
            double result = b.doubleValue()*100;
            DecimalFormat df0 = new DecimalFormat("0.00");
            String downRate = df0.format(result>100?100:result)+"%";
            down.put(fileName, new DownloadResult(getFileSize(downSize), getFileSize(fileSize), downRate));
            for (String key : down.keySet()) {
                DownloadResult downloadResult = down.get(key);
                System.out.println("下载进度======="+downloadResult);
            }
        }
        down.remove(fileName);
        fos.close();
    }

    public String getFileSize(Integer bytes){
        if (bytes<1024){
            return bytes+"B";
        }else if (bytes<1048576){
            return String.format("%.2f", (double)bytes/1024)+"KB";
        }else if (bytes<1073741824){
            return String.format("%.2f", (double)bytes/1024/1024)+"MB";
        }else {
            return String.format("%.2f", (double)bytes/1024/1024/1024)+"GB";
        }
    }

    public File[] orderByDate(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return -1;
                else if (diff == 0)
                    return 0;
                else
                    return 1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减,如果 if 中修改为 返回1 同时此处修改为返回 -1  排序就会是递增,
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
        return files;

    }


    public String getTime(Long millis){
        if (millis<1000){
            return millis+"毫秒";
        }else if (millis<60000){
            return millis/1000+"秒";
        }else if (millis<3600000){
            return millis/60000+"分"+millis%60000/1000+"秒";
        }else{
            return millis/3600000+"小时"+millis%3600000/60000+"分"+millis%3600000%60000/1000+"秒";
        }
    }

}
