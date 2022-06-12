package com.xjt.filedownload.util;

import com.xjt.filedownload.pojo.DownloadResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: fileDownload
 * @description: 下载文件工具类
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
            System.out.println("添加值");
            saveInputStreamToFile(inputStream,fileName,savePath,contentLength);
            inputStream.close();
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
            String downRate = df0.format(result>100?100:result)+"%";
            down.put(fileName, new DownloadResult(String.format("%.2f", (double)downSize/1024/1024), String.format("%.2f", (double)fileSize/1024/1024), downRate));
            for (String key : down.keySet()) {
                DownloadResult downloadResult = down.get(key);
                System.out.println("下载进度======="+downloadResult);
            }
        }
        down.remove(fileName);
        fos.close();
    }

}
