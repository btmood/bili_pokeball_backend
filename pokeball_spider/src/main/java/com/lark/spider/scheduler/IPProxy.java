package com.lark.spider.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Component
public class IPProxy {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    int i = 0;

    @Scheduled(cron = "*/1 * * * * ?")
    public void test1(){
//        System.out.println("插入");
        redisTemplate.opsForSet().add("test", i++);
        System.out.println("插入" + Thread.currentThread().getName());
    }

    @Scheduled(cron = "*/6 * * * * ?")
    public void test2(){
        Set<Object> test = redisTemplate.opsForSet().members("test");
        System.out.println(test);
        System.out.println("获取" + Thread.currentThread().getName());
    }

}
