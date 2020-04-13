package com.lark.spider.core.repository;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 用户模块对于redis的操作
 */
public interface IUserRepository {

    /**
     * 获取用户相关所有api
     * @return
     */
    void getBiliUserIdNow(BlockingQueue<String> blockingQueue);

}
