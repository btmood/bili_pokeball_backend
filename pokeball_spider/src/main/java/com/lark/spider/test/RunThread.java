package com.lark.spider.test;

import com.lark.spider.core.repository.impl.UserRepository;
import com.lark.spider.utils.ApplicationContextProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
public class RunThread {



    @RequestMapping("/thread01")
    public String test01(){
        ThreadTest01 mTask01 = ApplicationContextProvider.getBean("mTask", ThreadTest01.class);
        ThreadTest01 mTask02 = ApplicationContextProvider.getBean("mTask", ThreadTest01.class);
        ThreadTest01 mTask03 = ApplicationContextProvider.getBean("mTask", ThreadTest01.class);
        ThreadTest01 mTask04 = ApplicationContextProvider.getBean("mTask", ThreadTest01.class);
        ThreadTest01 mTask05 = ApplicationContextProvider.getBean("mTask", ThreadTest01.class);
        ThreadTest01 mTask06 = ApplicationContextProvider.getBean("mTask", ThreadTest01.class);


        Thread thread01 = new Thread(mTask01);
        Thread thread02 = new Thread(mTask02);
        Thread thread03 = new Thread(mTask03);
        Thread thread04 = new Thread(mTask04);
        Thread thread05 = new Thread(mTask05);
        Thread thread06 = new Thread(mTask06);

            thread01.start();
            thread02.start();
            thread03.start();
            thread04.start();
            thread05.start();
            thread06.start();

        return "123";
    }

    @RequestMapping("/thread02")
    public String test02(){
        BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
        UserRepository userRepository1 = ApplicationContextProvider.getBean("userRepository", UserRepository.class);

        userRepository1.setUserBlockingQueue(urlQueue);

        Thread thread1 = new Thread(userRepository1);

        thread1.start();
        System.out.println("队列的长度是：" + urlQueue.size());

        return "123";
    }

}
