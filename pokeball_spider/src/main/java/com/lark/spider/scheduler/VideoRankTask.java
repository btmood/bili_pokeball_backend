package com.lark.spider.scheduler;

import com.lark.spider.core.download.ICommonDownload;
import com.lark.spider.core.download.IDownload;
import com.lark.spider.core.parser.IVideoRankParser;
import com.lark.spider.core.repository.IVideoRepository;
import com.lark.spider.core.store.IVideoStore;
import com.lark.spider.spiderservice.entity.PopularVideoInfo;
import com.lark.spider.spiderservice.entity.PopularVideoTags;
import com.lark.spider.utils.AntiSpider;
import com.lark.spider.utils.WebTools;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

@Component("videoRankTask")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class VideoRankTask {

    /**
     * bilibili排行榜url
     */
    @Value("${biliUrl.html.videoRankURL}")
    private String videoRankURL;

    /**
     * 下载器
     */
    @Autowired
    @Qualifier("downloadImpl")
    private IDownload idownload;

    /**
     * 排行榜解析器
     */
    @Autowired
    @Qualifier("videoRankParserImpl")
    private IVideoRankParser iVideoRankParser;

    /**
     * url生成器
     */
    @Autowired
    private IVideoRepository iVideoRepository;

    /**
     * 下载器
     */
    @Autowired
    private ICommonDownload iCommonDownload;

    /**
     * 存储器
     */
    @Autowired
    private IVideoStore iVideoStore;


    @Test
    public void videoRank() throws InterruptedException {
        //下载html
        String videoRankHtml = WebTools.doGet(videoRankURL, null);
        //获取html中所有url，添加到redis队列中
        iVideoRankParser.parseVideoHtml(videoRankHtml);
        //获取排行榜视频信息，保存到对象里
        LinkedList<PopularVideoInfo> popularVideoInfoList = new LinkedList<>();
        LinkedList<PopularVideoTags> popularVideoTagsList = new LinkedList<>();
        while (true) {
            /*基础视频信息开始*/
            PopularVideoInfo popularVideoInfo = new PopularVideoInfo();
            //获取url，由于url拼接了视频得分，所以需要切割一下
            String rankVideoBaseInfoUrl = iVideoRepository.getRankVideoBaseInfoUrl();
            //返回null表示已经爬完了
            if (StringUtils.isEmpty(rankVideoBaseInfoUrl)) {
                log.info("获取的url为空");
                break;
            }
            String[] split = rankVideoBaseInfoUrl.split(";");
            popularVideoInfo.setBiliScore(Long.parseLong(split[1]));
            rankVideoBaseInfoUrl = split[0];
            //下载视频信息json
            String content = iCommonDownload.downloadGet(rankVideoBaseInfoUrl);
            //解析视频信息json
            iVideoRankParser.parseVideoJSON(content,popularVideoInfo);
            //把对象添加到列表中
            popularVideoInfoList.add(popularVideoInfo);
            //线程休眠一会儿
            Thread.sleep(AntiSpider.getRandomNum());
            /*基础视频信息结束*/
//            ==================================
//            ==================================
//            ==================================
            /*视频tag获取开始*/
            PopularVideoTags popularVideoTags = new PopularVideoTags();
            //获取url
            String rankVideoTagUrl = iVideoRepository.getRankVideoTagUrl();
            //下载视频标签json
            String tagContent = iCommonDownload.downloadGet(rankVideoTagUrl);
            //解析视频标签json
            iVideoRankParser.parseVideoTagsJSON(tagContent,popularVideoTags);
            popularVideoTags.setAv(popularVideoInfo.getAv());
            popularVideoTags.setBv(popularVideoInfo.getBv());
            popularVideoTagsList.add(popularVideoTags);
            Thread.sleep(AntiSpider.getRandomNum());
            /*视频tag结束*/
        }
        System.out.println(popularVideoInfoList);
        System.out.println(popularVideoInfoList.size());
        System.out.println("=====================================");
        System.out.println(popularVideoTagsList);
        System.out.println(popularVideoTagsList.size());

        //批量存储到mysql
        iVideoStore.saveVideoTagsBatch(popularVideoTagsList);
        iVideoStore.saveVideoInfoBatch(popularVideoInfoList);


    }

}
