package com.lark.spider.core.repository.impl;

import com.lark.spider.core.repository.IUserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component("userRepository")
@Scope("prototype")
@Data
@Slf4j
public class UserRepository implements Runnable {

    private BlockingQueue userBlockingQueue;

    String baseUserInfo = "https://api.bilibili.com/x/space/acc/info?mid=";
    String baseRelation = "http://api.bilibili.com/x/relation/stat?vmid=";
    String baseUpStat = "https://api.bilibili.com/x/space/upstat?mid=";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 产生用户信息相关api
     * @param blockingQueue 用于保存api的队列
     */
    public void getBiliUserIdNow(BlockingQueue<String> blockingQueue) {

        this.userBlockingQueue = blockingQueue;

        long startTime = System.currentTimeMillis();

        //从redis获取用户信息前首先判断一下爬虫的状态
        //如果是1说明爬虫是启动状态，就跳出死循环，爬虫如果处于睡眠状态，每隔10s检测一次爬虫状态
        while (true) {
            int state = (int) redisTemplate.opsForValue().get("spider.state");
            if (state == 1) {
                System.out.println("检测爬虫状态");
                break;
            }
            System.out.println("爬虫是关闭状态，重新检测中。。。");
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        //获取当前爬取的用户id
        Integer biliUserIdNowTemp = (Integer) redisTemplate.opsForValue().get("bili.user.id.now");
        Long biliUserIdNow = biliUserIdNowTemp.longValue();
        //拼接出完整的url
        String userInfo = baseUserInfo + biliUserIdNow;
        String relation = baseRelation + biliUserIdNow;
        String upStat = baseUpStat + biliUserIdNow;
        //将user的id加1
        redisTemplate.opsForValue().increment("bili.user.id.now", 1l);
        //将这几个api使用字符串拼接起来，避免单个api使用set存储再套一个集合了
        String allUrl = userInfo + ";" + relation + ";" + upStat;
        blockingQueue.add(allUrl);
        //检测一下队列里的数据有没有超过60，超过60就让线程休眠
        while (true) {
            if (blockingQueue.size() <= 60) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (blockingQueue.size() == 60){

            long endTime = System.currentTimeMillis();
            System.out.println("花费的时间是：" + (endTime - startTime) + "s");
        }
    }


    private int i = 1;

    /**
     * 本地生成用户id，加了同步锁，防止多线程出问题
     * @return
     */
    private synchronized int getBiliUserIdNowAuto(){
        return i++;
    }

    /**
     * 从redis获取当前用户id
     * 但是效率太低了
     * 所以直接本地生成了
     * @return
     */
    private Long getBiliUserIdNowByRedis(){
//        Integer biliUserIdNow = (Integer) redisTemplate.opsForValue().get("bili.user.id.now");
        Long biliUserIdNow = redisTemplate.opsForValue().increment("bili.user.id.now", 1l);
        return biliUserIdNow;
    }

    @Override
    public void run() {
        while (true) {
//            System.out.println("url仓库的" + Thread.currentThread().getName() + "正在执行");

            //从redis获取用户信息前首先判断一下爬虫的状态
            //如果是1说明爬虫是启动状态，就跳出死循环，爬虫如果处于睡眠状态，每隔10s检测一次爬虫状态
            while (true) {
                int state = (int) redisTemplate.opsForValue().get("spider.state");
                if (state == 1) {
                    break;
                }
                log.info("爬虫处于关闭状态，正在重新获取爬虫状态......");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //获取当前爬取的用户id
            Long biliUserIdNow = getBiliUserIdNowByRedis();
            //拼接出完整的url
            String userInfo = baseUserInfo + biliUserIdNow;
            String relation = baseRelation + biliUserIdNow;
            String upStat = baseUpStat + biliUserIdNow;
            //将user的id加1
            //将这几个api使用字符串拼接起来，避免单个api使用set存储再套一个集合了
            String allUrl = userInfo + ";" + relation + ";" + upStat;
            this.userBlockingQueue.add(allUrl);
            //检测一下队列里的数据有没有超过60，超过60就让线程休眠
            while (true) {
                if (this.userBlockingQueue.size() <= 60) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("=====" + userBlockingQueue.size());
//            System.out.println("=====" + userBlockingQueue);

        }

    }

}
