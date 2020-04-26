package com.lark.spider.core.store;

import com.lark.spider.spiderservice.entity.PopularVideoInfo;
import com.lark.spider.spiderservice.entity.PopularVideoTags;

import java.util.List;

public interface IVideoStore {

    /**
     * 批量存储视频基本信息
     * @param list
     */
    void saveVideoInfoBatch(List<PopularVideoInfo> list);

    /**
     * 批量存储视频标签
     * @param list
     */
    void saveVideoTagsBatch(List<PopularVideoTags> list);
}
