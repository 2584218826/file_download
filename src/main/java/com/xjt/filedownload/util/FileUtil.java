package com.xjt.filedownload.util;

import com.xjt.filedownload.pojo.DownloadResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 15:48
 **/
public class FileUtil {

    public static Map<String, DownloadResult> down = new HashMap<>();

    public static void downLoadFromUrl(String urlStr,String fileName,String savePath){
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
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
            down.put(fileName, new DownloadResult(0, inputStream.available(), "0%"));
            saveInputStreamToFile(inputStream,fileName,savePath,contentLength);

            if(inputStream!=null){
                inputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveInputStreamToFile(InputStream inputStream,String fileName,String path,Integer fileSize) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        File file = new File(path+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        int available = inputStream.available();
        int downSize = 0;
        while((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            downSize+=buffer.length;
            BigDecimal b = new BigDecimal((double) downSize/fileSize);
            Double result = b.doubleValue()*100;
            DecimalFormat df0 = new DecimalFormat("0.00");
            String downRate = df0.format(result)+"%";
            down.put(fileName, new DownloadResult(downSize, fileSize, downRate));
            for (String key : down.keySet()) {
                DownloadResult downloadResult = down.get(key);
                System.out.println("下载进度======="+downloadResult);
            }
        }
        down.remove(fileName);
        if(fos!=null){
            fos.close();
        }
    }

}
