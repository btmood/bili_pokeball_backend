package com.lark.spider.core.download.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lark.spider.core.download.IDownload;
import com.lark.spider.spiderservice.entity.UserStat;
import com.lark.spider.utils.HttpClientTool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.BlockingQueue;

@Component("downloadImpl")
@Scope("prototype")
@Data
@Slf4j
public class DownloadImpl implements IDownload,Runnable {

    private String url;

    private BlockingQueue<String> urlBlockingQueue;

    private BlockingQueue<String> JSONContentQueue;

    public DownloadImpl() {

    }

//    public DownloadImpl(String url) {
//        this.url = url;
//    }


    /**
     * 下载url,
     * @param url
     * @return JSON
     */
    @Override
    public String downloadJson(String url) {
        String result = HttpClientTool.doGet(url, null);
        System.out.println(result);
        return result;
    }

    /**
     * 下载url
     * @param blockingQueue 保存url的队列，一个元素由多个url拼接而成
     * @return 返回的是三个url拼接成的content
     */
    @Override
    public String downloadJson(BlockingQueue<String> blockingQueue) {
//        if (blockingQueue.size() <= 2 ){
//            return null;
//        }
        String url = blockingQueue.poll();
        if (url == null){
            return null;
        }
        String[] split = url.split(";");
        String UrlUserInfo = split[0];
        String urlRelation = split[1];
        String urlUpStat = split[2];

        String relation = HttpClientTool.doGet(urlRelation, null);
        Boolean fansMoreThan10000 = this.isFansMoreThan10000(relation);
        //如果用户粉丝数小于10w，那么直接返回空
        if (fansMoreThan10000 == false) {
            return null;
        }
        String userInfo = HttpClientTool.doGet(UrlUserInfo, null);
        //防止userinfo里的用户签名包含特殊字符
        userInfo = userInfo.replaceAll(";", "");
        String upStat = HttpClientTool.doGet(urlUpStat, null);

        //把用户相关的所有content拼接在一起返回
        String allJson = upStat + ";" + relation + ";" + userInfo;

        JSONContentQueue.add(allJson);

        return allJson;
    }

    /**
     * 下载html页面
     * @param url
     * @return
     */
    @Override
    public String downloadHtml(String url) {
        System.out.println("发起请求");
        String result = HttpClientTool.doGet(url, null);
        return result;
    }

    @Override
    public void run() {
        while (true) {
            log.info("下载器的" + Thread.currentThread().getName() + "正在执行");
            while (urlBlockingQueue.size() <= 5) {
                log.error("urlBlockingQueue队列中url少于5个，等待url填充中......");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (JSONContentQueue.size() >= 500) {
                log.info("JSONContentQueue队列中json数量大于500，停止继续下载，等待解析器解析中......");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //弹出一个url
            String url = urlBlockingQueue.poll();
            if (StringUtils.isEmpty(url)) {
                log.error("从redis获取的下载url为空");
                continue;
            }
            String[] split = url.split(";");
            String UrlUserInfo = split[0];
            String urlRelation = split[1];
            String urlUpStat = split[2];

            //relation包含当前用户粉丝数
            //策略：粉丝数小于10w的就放弃，后面的url就不请求了
            String relation = HttpClientTool.doGet(urlRelation, null);
            if (StringUtils.isEmpty(relation)) {
                log.error("relation下载失败{}",relation);
                continue;
            }
            Boolean fansMoreThan10000 = this.isFansMoreThan10000(relation);
            //如果用户粉丝数小于10w，那么直接返回空
            if (fansMoreThan10000 == false) {
                log.debug("当前用户粉丝数小于10w，取消爬取相关信息");
                continue;
            }
            String userInfo = HttpClientTool.doGet(UrlUserInfo, null);
            if (StringUtils.isEmpty(userInfo)){
                log.error("userInfo下载失败{}",userInfo);
                continue;
            }
            //防止userinfo里的用户签名包含特殊字符
            userInfo = userInfo.replaceAll(";", "");
            String upStat = HttpClientTool.doGet(urlUpStat, null);
            if (StringUtils.isEmpty(userInfo)){
                log.error("upStat下载失败{}",upStat);
                continue;
            }

            //把爬取下来的所有json，拼接在一起返回
            String allJson = upStat + ";" + relation + ";" + userInfo;

            JSONContentQueue.add(allJson);
//            log.debug(String.valueOf(JSONContentQueue));
            log.debug("当前队列中剩余待解析用户数量" + JSONContentQueue.size());
//            System.out.println(JSONContentQueue);
//            System.out.println("当前队列中剩余待解析用户数量" + JSONContentQueue.size());
        }
    }

    /**
     * 判断当前用户的粉丝数是不是大于10w
     * @param content
     * @return
     */
    private Boolean isFansMoreThan10000(String content){
        JSONObject root = new JSONObject().parseObject(content);

        String data = root.getString("data");

        JSONObject dataJson = new JSONObject().parseObject(data);

        String follower = dataJson.getString("follower");

        if (Integer.parseInt(follower) >= 10000){
            return true;
        }
        return false;
    }
}
