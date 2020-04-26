package com.lark.spider.core.parser.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lark.spider.core.parser.IVideoRankParser;
import com.lark.spider.spiderservice.entity.PopularVideoInfo;
import com.lark.spider.spiderservice.entity.PopularVideoTags;
import com.lark.spider.utils.BiliBV2AV;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Data
@Slf4j
@Component("videoRankParserImpl")
public class VideoRankParserImpl implements IVideoRankParser {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Override
//    public void parseVideo(String content) {
//        //解析html
//        parseVideoHtml(content);
//    }

    /**
     * 使用xpath解析排行榜的html
     * @param content
     */
    @Override
    public void parseVideoHtml(String content) {
        if (StringUtils.isEmpty(content)) {
            log.error("下载器下载下来的内容为空");
            return;
        }
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode tagNode = htmlCleaner.clean(content);
        try {
            Object[] objects = tagNode.evaluateXPath("//div[@class='rank-list-wrap']//li[@class='rank-item']");
            for (Object o : objects) {
                TagNode n = (TagNode) o;
                //视频url
                Object[] objects1 = n.evaluateXPath("/div/div[@class'info']/a/@href");
                String videoUrl = objects1[0].toString();
                String bv = videoUrl.replaceAll("https://www.bilibili.com/video/", "");
                String av = BiliBV2AV.b2v(bv);
                av = av.replaceAll("av", "");
//                System.out.println("bv:" + bv);
//                System.out.println("av:" + av);
//                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");
                //用户url
                Object[] objects2 = n.evaluateXPath("/div/div[@class'info']/div[@class='detail']/a/@href");
                String userSpaceUrl = objects2[0].toString();
                String userUid = userSpaceUrl.replaceAll("//space.bilibili.com/", "");
//                System.out.println("userUid:" + userUid);
                //视频得分
                Object[] objects3 = n.evaluateXPath("/div/div[@class'info']/div[@class='pts']/div/text()");
                String biliScore = objects3[0].toString();
//                System.out.println("biliScore:" + biliScore);
//                System.out.println("==================");

                //将视频id和视频得分存到redis队列中
                redisTemplate.opsForSet().add("bili:video:rank:avscore", av + ";" + biliScore);
                //将uid存到redis队列中
                redisTemplate.opsForSet().add("bili:user:rank:uid",userUid);
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析视频url，解析的是json
     * {"code":0,"message":"0","ttl":1,"data":{"aid":98464354,"bvid":"BV1FE411c7co","view":275898,"danmaku":1606,"reply":1458,"favorite":20509,"coin":31590,"share":2286,"like":29242,"now_rank":0,"his_rank":0,"no_reprint":1,"copyright":1,"argue_msg":"","evaluation":""}}
     * @param content
     */
    @Override
    public void parseVideoJSON(String content, PopularVideoInfo popularVideoInfo) {
        if (StringUtils.isEmpty(content)) {
            log.error("下载器下载下来的内容为空");
            return;
        }
        JSONObject rootJSON = JSON.parseObject(content);
        String data = rootJSON.getString("data");
        JSONObject dataJSON = JSON.parseObject(data);
        String aid = dataJSON.getString("aid");
        String bvid = dataJSON.getString("bvid");
        String view = dataJSON.getString("view");
        String danmaku = dataJSON.getString("danmaku");
        String reply = dataJSON.getString("reply");
        String favorite = dataJSON.getString("favorite");
        String coin = dataJSON.getString("coin");
        String share = dataJSON.getString("share");
        String like = dataJSON.getString("like");
        String copyright = dataJSON.getString("copyright");
        popularVideoInfo.setAv(aid)
                .setBv(bvid)
                .setView(Long.parseLong(view))
                .setDanmaku(Long.parseLong(danmaku))
                .setReply(Long.parseLong(reply))
                .setFavorite(Long.parseLong(favorite))
                .setCoin(Long.parseLong(coin))
                .setShare(Long.parseLong(share))
                .setLikeNum(Long.parseLong(like))
                .setCopyright(copyright);
    }

    /**
     * 解析视频的标签
     * @param content
     * @param popularVideoTags
     */
    public void parseVideoTagsJSON(String content, PopularVideoTags popularVideoTags) {
        if (StringUtils.isEmpty(content)) {
            log.error("下载器下载下来的内容为空");
            return;
        }
        JSONObject rootJSON = JSON.parseObject(content);
        JSONArray dataArr = rootJSON.getJSONArray("data");
        String tags = "";
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject dataBean = (JSONObject) dataArr.get(i);
            String tagName = dataBean.getString("tag_name");
            tagName = tagName.replaceAll(",","");
            tags = tags + tagName + ",";
        }
        tags = tags.substring(0, tags.length()-1);
        popularVideoTags.setTags(tags);
    }


}
