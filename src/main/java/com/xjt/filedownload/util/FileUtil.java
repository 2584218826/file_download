package com.xjt.filedownload.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 15:48
 **/
public class FileUtil {

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
            saveInputStreamToFile(inputStream,savePath+File.separator+fileName);
            //文件保存位置
            File saveDir = new File(savePath);
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveInputStreamToFile(InputStream inputStream,String path) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        while((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        if(fos!=null){
            fos.close();
        }
    }

}
