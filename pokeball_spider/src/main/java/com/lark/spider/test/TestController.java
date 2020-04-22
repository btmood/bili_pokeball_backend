package com.lark.spider.test;

import com.lark.spider.utils.HttpClientTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/test")
@RestController
public class TestController {

    @Value("${biliUrl.user.baseUserInfoURL}")
    private String baseUserInfoURL;

    /**
     * 测试get请求
     * @return
     */
    @RequestMapping("testGet")
    public Map<String,Object> testGet() throws InterruptedException {
        Map<String, Object> resultMap = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            String wholeUrl = baseUserInfoURL + i;
            String response = HttpClientTool.doGet(wholeUrl, null);
            resultMap.put(String.valueOf(i), response);
            Thread.sleep(200);
        }
        return resultMap;
    }

    /**
     * 测试https的ssl请求，忽略ssl验证
     * @return
     */
    @RequestMapping("testGetSSL")
    public Map<String,Object> testGetSSL() throws InterruptedException {
        Map<String, Object> resultMap = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            String wholeUrl = baseUserInfoURL + i;
            String response = HttpClientTool.doGetSSL(wholeUrl, null);
            resultMap.put(String.valueOf(i), response);
            Thread.sleep(200);
        }
        return resultMap;
    }


}
