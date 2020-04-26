package com.lark.spider.core.download.impl;

import com.lark.spider.core.download.ICommonDownload;
import com.lark.spider.core.download.IDownload;
import com.lark.spider.utils.WebTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
@Component("commonDownloadImpl")
@Scope("prototype")
@Data
@Slf4j
public class CommonDownloadImpl implements ICommonDownload {


    @Override
    public String downloadGet(String url) {
        String content = WebTools.doGet(url, null);
        return content;
    }
}
