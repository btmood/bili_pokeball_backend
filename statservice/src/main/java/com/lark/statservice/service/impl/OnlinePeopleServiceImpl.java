package com.lark.statservice.service.impl;

import com.lark.statservice.pojo.OnlinePeople;
import com.lark.statservice.mapper.OnlinePeopleMapper;
import com.lark.statservice.service.OnlinePeopleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-04-24
 */
@Service
public class OnlinePeopleServiceImpl extends ServiceImpl<OnlinePeopleMapper, OnlinePeople> implements OnlinePeopleService {

    @Autowired
    private OnlinePeopleMapper onlinePeopleMapper;

    @Override
    public List<OnlinePeople> getOnlinePeopleBatch(String queryTime) {
        System.out.println(queryTime);
        List<OnlinePeople> onlinePeopleList = onlinePeopleMapper.selectOnlinePeopleBatch(queryTime);
        return onlinePeopleList;
    }
}
