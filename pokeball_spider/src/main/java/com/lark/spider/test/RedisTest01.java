package com.lark.spider.test;

import com.lark.spider.core.repository.impl.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest01 {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test01(){
//        redisTemplate.opsForValue().set("count", 1);
//        String count = (String) redisTemplate.opsForValue().get("count").toString();

//        System.out.println("查到的count是：" + count);
        redisTemplate.opsForValue().set("spider.state", 1);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test02(){
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        while (true){
            userRepository.getBiliUserIdNow(queue);
            System.out.println(queue);
            System.out.println(queue.size());
            System.out.println("==================================================");
        }

    }

}
