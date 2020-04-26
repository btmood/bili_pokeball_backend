package com.lark.spider.core.store.impl;

import com.lark.spider.core.store.IVideoStore;
import com.lark.spider.spiderservice.entity.PopularVideoInfo;
import com.lark.spider.spiderservice.entity.PopularVideoTags;
import com.lark.spider.spiderservice.service.PopularVideoInfoService;
import com.lark.spider.spiderservice.service.PopularVideoTagsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("videoStoreImpl")
@Scope("prototype")
@Data
@Slf4j
public class VideoStoreImpl implements IVideoStore {

    @Autowired
    private PopularVideoInfoService popularVideoInfoService;

    @Autowired
    private PopularVideoTagsService popularVideoTagsService;

    /**
     * 批量存储视频基本信息
     * @param list
     */
    public void saveVideoInfoBatch(List<PopularVideoInfo> list) {
        System.out.println("基本信息"+list);
        popularVideoInfoService.saveBatch(list);
    }

    /**
     * 批量存储视频标签
     * @param list
     */
    public void saveVideoTagsBatch(List<PopularVideoTags> list) {
        System.out.println("标签" + list);
        popularVideoTagsService.saveBatch(list);
    }


}
