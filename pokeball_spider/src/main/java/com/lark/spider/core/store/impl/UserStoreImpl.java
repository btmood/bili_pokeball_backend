package com.lark.spider.core.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;
import com.lark.spider.spiderservice.service.UserInfoService;
import com.lark.spider.core.store.IUserStore;
import com.lark.spider.spiderservice.service.UserStatService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component("userStoreImpl")
@Scope("prototype")
@Data
@Slf4j
public class UserStoreImpl implements IUserStore,Runnable {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserStatService userStatService;

    /**
     * 存储要批量插入的UserStat对象
     */
    private BlockingQueue<UserStat> userStatQueue;

    /**
     * 存储要批量插入的UserInfo对象
     */
    private BlockingQueue<UserInfo> userInfoQueue;

    /**
     * 用于批量插入
     */
    private List<UserStat> userStatList;

    /**
     * 用于批量插入
     */
    private List<UserInfo> userInfoList;


    @Override
    public void storeUserInfo(UserInfo userInfo) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userInfo.getUid());
        UserInfo originalUserInfo = userInfoService.getOne(wrapper);
        if (originalUserInfo == null){
            userInfoService.save(userInfo);
        }else{
            userInfoService.updateById(userInfo);
        }
    }

    @Override
    public void StoreUserStat(UserStat userStat) {
        QueryWrapper<UserStat> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userStat.getUid());
        UserStat originalUserStat = userStatService.getOne(wrapper);
        if (originalUserStat == null){
            userStatService.save(userStat);
        }else{
            userStatService.updateById(userStat);
        }
    }

    @Override
    public void run() {
        while (true) {
            while (userInfoQueue.size() < 50) {
                log.info("userInfoQueue队列包含对象小于400，等待解析器解析中......");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //把队列中的所有数据全部传到list中
            int i = userStatQueue.drainTo(userStatList);
            userInfoQueue.drainTo(userInfoList);
            log.info("本次将要插入数据库" + i + "条数据");

            //批量插入
            boolean b = userStatService.saveBatch(userStatList);
            userInfoService.saveBatch(userInfoList);
            log.info("插入数据成功");

            if (!b) {
                //TODO
                log.info("插入数据失败");
            }

            //清除list里的数据
            userStatList.clear();
            userInfoList.clear();
        }
    }
}
