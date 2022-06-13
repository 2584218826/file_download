package com.xjt.filedownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileDownloadApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileDownloadApplication.class, args);
    }

}
