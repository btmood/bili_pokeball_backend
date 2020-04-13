package com.lark.spider.test;

import com.lark.spider.utils.HttpClientTool;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

public class Test001 {

    @Test
    public void testo1() throws IOException {
        String s = HttpClientTool.doGet("https://www.bilibili.com/video/av94418092", null);
//        String s = HttpClientTool.doPost("www.bilibili.com/", null);
//        String s = HttpClientTool.doGetSSL("https://www.bilibili.com/", null);
//        String s = HttpClientTool.doGet("https://api.bilibili.com/x/space/acc/info?mid=11357018&jsonp=jsonp", null);
        System.out.println(s);

    }

}
