package com.xjt.filedownload.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: fileDownload
 * @description:
 * @author: xujt
 * @date: 2022-06-11 15:36
 **/
@Controller
public class ViewController {


    @GetMapping("/")
    public String index(){
        return "index";
    }
}
