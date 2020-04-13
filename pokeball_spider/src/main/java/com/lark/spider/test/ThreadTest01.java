package com.lark.spider.test;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.management.monitor.Monitor;

@Component("mTask")
@Scope("prototype")
public class ThreadTest01 implements Runnable {

    @Override
    public void run() {
        while (true) {

            System.out.println(Thread.currentThread().getName() + "运行中");
        }
    }
}
