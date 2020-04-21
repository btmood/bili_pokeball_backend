package com.lark.spider.core.parser.impl;

import com.alibaba.fastjson.JSONObject;
import com.lark.spider.core.parser.IUserParser;
import com.lark.spider.spiderservice.controller.UserInfoController;
import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Component("userParserImpl")
@Scope("prototype")
@Data
@Slf4j
public class UserParserImpl implements IUserParser,Runnable {

    /**
     * 存储爬取下来的用户信息的队列
     */
    private BlockingQueue<String> JSONContentQueue;

    /**
     * 存储UserInfo对象
     * 用于批量插入
     */
    private BlockingQueue<UserInfo> userInfoQueue;

    /**
     * 存储UserStat对象
     * 用于批量插入
     */
    private BlockingQueue<UserStat> userStatQueue;



    @Override
    public void parseUserInfo(String content, UserInfo userInfo) throws Exception {
        JSONObject root = new JSONObject().parseObject(content);

        String code = root.getString("code");
        //返回的不是0说明解析有问题
        if (!code.equals(0)) {
            log.error("解析失败，返回的状态码错误{}",code);
            throw new Exception("解析失败");
        }
        String data = root.getString("data");

        JSONObject dataJson = new JSONObject().parseObject(data);

        String uid = dataJson.getString("mid");
        String name = dataJson.getString("name");
        String sex = dataJson.getString("sex");
        String avatar = dataJson.getString("face");
        String sign = dataJson.getString("sign");
        String level = dataJson.getString("level");
        String vip = dataJson.getString("vip");

        JSONObject vipJson = new JSONObject().parseObject(vip);
        String type = vipJson.getString("type");
        String status = vipJson.getString("status");

        userInfo.setUid(uid)
                .setLevel(level)
                .setSex(sex)
                .setAvatar(avatar)
                .setSignature(sign)
                .setSuperVip(type)
                .setNickname(name);

//        System.out.println(uid);
//        System.out.println(name);
//        System.out.println(sex);
//        System.out.println(avatar);
//        System.out.println(sign);
//        System.out.println(level);
//        System.out.println(type);
//        System.out.println(status);


    }

    @Override
    public void parseRelation(String content, UserStat userStat) throws Exception {
        JSONObject root = new JSONObject().parseObject(content);

        String code = root.getString("code");
        //返回的不是0说明解析有问题
        if (!code.equals(0)) {
            log.error("解析失败，返回的状态码错误{}",code);
            throw new Exception("解析失败");
        }

        String data = root.getString("data");

        JSONObject dataJson = new JSONObject().parseObject(data);

        String following = dataJson.getString("following");
        String follower = dataJson.getString("follower");

        userStat.setConcernNumber(following);
        userStat.setFansNumber(follower);
    }

    @Override
    public void parseUpStat(String content, UserStat userStat) throws Exception {
        JSONObject root = new JSONObject().parseObject(content);

        String code = root.getString("code");
        //返回的不是0说明解析有问题
        if (!code.equals(0)) {
            log.error("解析失败，返回的状态码错误{}",code);
            throw new Exception("解析失败");
        }

        String data = root.getString("data");

        JSONObject dataJson = new JSONObject().parseObject(data);

        String likes = dataJson.getString("likes");
        String archive = dataJson.getString("archive");

        JSONObject archiveJSON = dataJson.parseObject(archive);

        String view = archiveJSON.getString("view");

        userStat.setPlayNumber(view);
        userStat.setPraiseNumber(likes);

    }

    @Override
    public void run() {
        while (true) {
            while (userInfoQueue.size() >= 800) {
                log.error("userInfoList队列中数据超过800条，暂停解析");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (JSONContentQueue.size() <= 5) {
                log.error("JSONContentQueue数据少于5条，暂停解析，等待下载器下载内容......");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String JSONContent = JSONContentQueue.poll();

            String[] JSONContentArray = JSONContent.split(";");

            //注意顺序
            String upStatJSON = JSONContentArray[0];
            String relationJSON = JSONContentArray[1];
            String userInfoJSON = JSONContentArray[2];

            UserStat userStat = new UserStat();
            UserInfo userInfo = new UserInfo();

            try {
                //有些用户被封了，所以不存在
                parseUserInfo(userInfoJSON, userInfo);
                String uid = userInfo.getUid();
                userStat.setUid(uid);
                parseRelation(relationJSON, userStat);
                parseUpStat(upStatJSON, userStat);
            } catch (Exception e) {
                //TODO
                log.error("解析失败，错误信息：{0}；返回的JSON内容为：{1}",2,JSONContent);
                continue;
            }

            log.debug("UserParseImpl爬取到的UserStat对象" + userStat);
            log.debug("UserParseImpl爬取到的UserInfo对象" + userInfo);

            userStatQueue.add(userStat);
            userInfoQueue.add(userInfo);

//            System.out.println("解析器解析到的数据userStatQueue" + userStatQueue.size() + userStatQueue);
//            System.out.println("解析器解析到的数据userInfoQueue" + userInfoQueue.size() + userInfoQueue);
            log.info("当前等待保存的数据有" + userInfoQueue.size() + "条");
//            System.out.println("当前等待保存的数据有" + userInfoQueue.size());
//            System.out.println("==============");
        }
    }
}
