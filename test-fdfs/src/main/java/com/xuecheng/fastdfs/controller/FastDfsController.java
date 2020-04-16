package com.xuecheng.fastdfs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FastDfsController {

    @GetMapping("/test")
    public String testController() {
        return "fucker";
    }
}
