package com.lark.spider.start;

import com.lark.spider.core.download.IDownload;
import com.lark.spider.core.parser.IUserParser;
import com.lark.spider.core.store.IUserStore;
import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 爬虫入口类
 */
@Data
@Component
public class ISpider {

    //爬虫下载器
    private IDownload download;

    //爬虫存储器
    private IUserStore store;

    //爬虫解析器
    private IUserParser parser;


    /**
     * 完成网页数据的下载
     * @param url
     * @return
     */
    public String download(String url){
        return this.download.downloadJson(url);
    }

    public void parseUserInfo(String content, UserInfo userInfo){
        this.parser.parseUserInfo(content,userInfo);
    }

    public void parseRelation(String content, UserStat userStat){
        this.parser.parseRelation(content, userStat);
    }

    public void parseUpStat(String content, UserStat userStat){
        this.parser.parseUpStat(content, userStat);
    }

    public void storeUserInfo(UserInfo userInfo){
        this.store.storeUserInfo(userInfo);
    }

    public void StoreUserStat(UserStat userStat){
        this.store.StoreUserStat(userStat);
    }

    public void startCrawler(){

    }
}
