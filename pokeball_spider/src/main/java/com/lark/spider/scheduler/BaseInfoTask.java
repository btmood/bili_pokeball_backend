package com.lark.spider.scheduler;

import com.lark.spider.core.download.IDownload;
import com.lark.spider.core.download.impl.DownloadImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseInfoTask {

    @Value("${biliUrl.user.onlinePeopleURL}")
    private String onlinePeople;

    @Autowired
    private IDownload iDownload;


    public void currentCount() {
        String content = iDownload.downloadJson(onlinePeople);


    }

}
