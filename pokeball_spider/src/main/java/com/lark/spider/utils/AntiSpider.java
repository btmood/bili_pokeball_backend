package com.lark.spider.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntiSpider {

    public static String getOneUserAgent() {
        ArrayList<String> userAgentList = new ArrayList<>();
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-US) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27");
        userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27");
        userAgentList.add("Mozilla/5.0 (Android; Mobile; rv:14.0) Gecko/14.0 Firefox/14.0");
        userAgentList.add("Opera/9.80 (Windows NT 6.1; WOW64; U; en) Presto/2.10.229 Version/11.62");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        userAgentList.add("Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.9.168 Version/11.50");
        userAgentList.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3)");
        userAgentList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; Tablet PC 2.0; .NET4.0E)");
        userAgentList.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB7.0)");
        userAgentList.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; ) AppleWebKit/534.12 (KHTML, like Gecko) Maxthon/3.0 Safari/534.12");
        userAgentList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)");
        userAgentList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        Random random = new Random();
        int n = random.nextInt(userAgentList.size());
        String userAgent = userAgentList.get(n);
        return userAgent;
    }

}
