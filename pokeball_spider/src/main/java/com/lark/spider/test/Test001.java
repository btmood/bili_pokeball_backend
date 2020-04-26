package com.lark.spider.test;

import com.lark.spider.core.store.IVideoStore;
import com.lark.spider.spiderservice.entity.PopularVideoInfo;
import com.lark.spider.spiderservice.entity.PopularVideoTags;
import com.lark.spider.utils.HttpClientTool;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.LinkedList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test001 {

    @Test
    public void testo1() throws IOException {
        String s = HttpClientTool.doGet("https://api.bilibili.com/x/space/acc/info?mid=1", null);
//        String s = HttpClientTool.doPost("www.bilibili.com/", null);
//        String s = HttpClientTool.doGetSSL("https://www.bilibili.com/", null);
//        String s = HttpClientTool.doGet("https://api.bilibili.com/x/space/acc/info?mid=11357018&jsonp=jsonp", null);
        System.out.println(s);

    }

    @Autowired
    private IVideoStore iVideoStore;

    @Test
    public void test02(){
        LinkedList<PopularVideoInfo> objects = new LinkedList<>();

        PopularVideoInfo popularVideoInfo = new PopularVideoInfo();
        popularVideoInfo.setBiliScore(111111l);
        popularVideoInfo.setCopyright("1");
        objects.add(popularVideoInfo);
        iVideoStore.saveVideoInfoBatch(objects);
    }

}
