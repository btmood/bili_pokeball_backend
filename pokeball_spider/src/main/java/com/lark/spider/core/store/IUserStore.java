package com.lark.spider.core.store;

import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;

import java.util.List;

/**
 * 数据存储
 */
public interface IUserStore {
    /**
     * 存储用户基本信息
     * @param userInfo
     */
    public void storeUserInfo(UserInfo userInfo);

    /**
     * 存储用户数据
     * @param userStat
     */
    public void StoreUserStat(UserStat userStat);
}
