package com.lark.statservice.service;

import com.lark.statservice.pojo.OnlinePeople;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-04-24
 */
public interface OnlinePeopleService extends IService<OnlinePeople> {

    /**
     * 按照queryTime查询出这个时间以及这个时间点前面的时间，20条
     * @param queryTime
     * @return
     */
    List<OnlinePeople> getOnlinePeopleBatch(String queryTime);
}
