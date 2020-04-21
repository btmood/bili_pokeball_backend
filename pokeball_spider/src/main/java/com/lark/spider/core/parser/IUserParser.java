package com.lark.spider.core.parser;


import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;

public interface IUserParser {
    /**
     * 解析用户基本信息
     * @param content
     */
    public void parseUserInfo(String content, UserInfo userInfo) throws Exception;

    /**
     * 解析粉丝数、关注别人数
     * @param content
     */
    public void parseRelation(String content, UserStat userStat) throws Exception;

    /**
     * 解析总播放量、被点赞数
     * @param content
     */
    public void parseUpStat(String content, UserStat userStat) throws Exception;
}
