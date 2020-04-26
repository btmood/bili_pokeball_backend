package com.lark.spider.core.parser;

import com.lark.spider.spiderservice.entity.PopularVideoInfo;
import com.lark.spider.spiderservice.entity.PopularVideoTags;
import org.springframework.stereotype.Service;

public interface IVideoRankParser {

    /**
     * 解析视频
     * @param content
     */
//    void parseVideo(String content);

    /**
     * 解析视频排行榜Html页面
     * @param content
     */
    void parseVideoHtml(String content);

    /**
     * 解析视频url，解析的是json
     * @param content
     */
    void parseVideoJSON(String content, PopularVideoInfo popularVideoInfo);

    /**
     * 解析视频标签
     * @param content
     * @param popularVideoTags
     */
    void parseVideoTagsJSON(String content, PopularVideoTags popularVideoTags);
}
