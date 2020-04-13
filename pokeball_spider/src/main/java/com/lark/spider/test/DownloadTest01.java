package com.lark.spider.test;

import com.alibaba.fastjson.JSON;
import com.lark.spider.core.download.impl.DownloadImpl;
import com.lark.spider.core.parser.impl.UserParserImpl;
import com.lark.spider.core.repository.impl.UserRepository;
import com.lark.spider.core.store.impl.UserStoreImpl;
import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;
import com.lark.spider.utils.ApplicationContextProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
public class DownloadTest01 {

    /**
     * 测试到下载器
     * @return
     */
    @RequestMapping("/download01")
    public String test01(){
        UserRepository userRepository = ApplicationContextProvider.getBean("userRepository", UserRepository.class);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        userRepository.setUserBlockingQueue(queue);
        Thread userRepositoryThread = new Thread(userRepository);
//        userRepositoryThread.setPriority(10);
        userRepositoryThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DownloadImpl downloadImpl = ApplicationContextProvider.getBean("downloadImpl", DownloadImpl.class);
        downloadImpl.setUrlBlockingQueue(queue);
        BlockingQueue<String> JSONContentQueue = new LinkedBlockingQueue<>();
        //存储爬取内存的队列
        downloadImpl.setJSONContentQueue(JSONContentQueue);
        Thread downloadThread1 = new Thread(downloadImpl);
        Thread downloadThread2 = new Thread(downloadImpl);
        Thread downloadThread3 = new Thread(downloadImpl);
        Thread downloadThread4 = new Thread(downloadImpl);
        Thread downloadThread5 = new Thread(downloadImpl);

        downloadThread1.start();
        downloadThread2.start();
        downloadThread3.start();
        downloadThread4.start();
        downloadThread5.start();

        return "123";
    }

    /**
     * 测试到解析器
     * @return
     */
    @RequestMapping("/parser1")
    public String test02(){
        UserRepository userRepository = ApplicationContextProvider.getBean("userRepository", UserRepository.class);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        userRepository.setUserBlockingQueue(queue);
        Thread userRepositoryThread = new Thread(userRepository);
//        userRepositoryThread.setPriority(10);
        userRepositoryThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DownloadImpl downloadImpl = ApplicationContextProvider.getBean("downloadImpl", DownloadImpl.class);
        downloadImpl.setUrlBlockingQueue(queue);
        BlockingQueue<String> JSONContentQueue = new LinkedBlockingQueue<>();
        //存储爬取内存的队列
        downloadImpl.setJSONContentQueue(JSONContentQueue);
        Thread downloadThread1 = new Thread(downloadImpl);
        Thread downloadThread2 = new Thread(downloadImpl);
        Thread downloadThread3 = new Thread(downloadImpl);
        Thread downloadThread4 = new Thread(downloadImpl);
        Thread downloadThread5 = new Thread(downloadImpl);

        downloadThread1.start();
        downloadThread2.start();
        downloadThread3.start();
        downloadThread4.start();
        downloadThread5.start();


        /*解析器开始*/
        UserParserImpl userParserImpl = ApplicationContextProvider.getBean("userParserImpl", UserParserImpl.class);
        userParserImpl.setJSONContentQueue(JSONContentQueue);
        BlockingQueue<UserStat> userStatQueue = new LinkedBlockingQueue<>();
        BlockingQueue<UserInfo> userInfoQueue = new LinkedBlockingQueue<>();
        userParserImpl.setUserInfoQueue(userInfoQueue);
        userParserImpl.setUserStatQueue(userStatQueue);

        Thread userParserImplThread = new Thread(userParserImpl);
        userParserImplThread.start();

        return "123";
    }

    /**
     * 测试存储器
     * @return
     */
    @RequestMapping("/store1")
    public String test03(){
        UserRepository userRepository = ApplicationContextProvider.getBean("userRepository", UserRepository.class);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        userRepository.setUserBlockingQueue(queue);
        Thread userRepositoryThread = new Thread(userRepository);
//        userRepositoryThread.setPriority(10);
        userRepositoryThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DownloadImpl downloadImpl = ApplicationContextProvider.getBean("downloadImpl", DownloadImpl.class);
        downloadImpl.setUrlBlockingQueue(queue);
        BlockingQueue<String> JSONContentQueue = new LinkedBlockingQueue<>();
        //存储爬取内存的队列
        downloadImpl.setJSONContentQueue(JSONContentQueue);
        Thread downloadThread1 = new Thread(downloadImpl);
        Thread downloadThread2 = new Thread(downloadImpl);
        Thread downloadThread3 = new Thread(downloadImpl);
        Thread downloadThread4 = new Thread(downloadImpl);
        Thread downloadThread5 = new Thread(downloadImpl);

        downloadThread1.start();
        downloadThread2.start();
        downloadThread3.start();
        downloadThread4.start();
        downloadThread5.start();


        /*解析器开始*/
        UserParserImpl userParserImpl = ApplicationContextProvider.getBean("userParserImpl", UserParserImpl.class);
        userParserImpl.setJSONContentQueue(JSONContentQueue);
        BlockingQueue<UserStat> userStatQueue = new LinkedBlockingQueue<>();
        BlockingQueue<UserInfo> userInfoQueue = new LinkedBlockingQueue<>();
        userParserImpl.setUserInfoQueue(userInfoQueue);
        userParserImpl.setUserStatQueue(userStatQueue);

        Thread userParserImplThread = new Thread(userParserImpl);
        userParserImplThread.start();

        UserStoreImpl userStoreImpl = ApplicationContextProvider.getBean("userStoreImpl", UserStoreImpl.class);
        userStoreImpl.setUserInfoQueue(userInfoQueue);
        userStoreImpl.setUserStatQueue(userStatQueue);
        List<UserInfo> userInfoList = new LinkedList<>();
        List<UserStat> userStatList = new LinkedList<>();
        userStoreImpl.setUserInfoList(userInfoList);
        userStoreImpl.setUserStatList(userStatList);
        Thread userStoreImplThread = new Thread(userStoreImpl);
        userStoreImplThread.start();


        return "123";
    }




}
