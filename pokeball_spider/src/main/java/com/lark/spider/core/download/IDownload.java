package com.lark.spider.core.download;

import java.util.concurrent.BlockingQueue;

public interface IDownload {

    /**
     * 下载url
     * @param url
     * @return JSON
     */
    public String downloadJson(String url);

    /**
     * 下载url
     * @param blockingQueue 保存url的队列，一个元素由多个url拼接而成
     * @return JSON
     */
    public String downloadJson(BlockingQueue<String> blockingQueue);

    /**
     * 下载html页面
     * @param Url
     * @return
     */
    public String downloadHtml(String Url);

}
