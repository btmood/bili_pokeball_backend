package com.lark.spider.test;

import org.junit.Test;

import java.util.Random;

public class GrammerTest {

    @Test
    public void test01(){
        String s = "https://www.bilibili.com/video/BV1fC4y1s7vv";
        String s1 = s.replaceAll("https://www.bilibili.com/video/", "");
        System.out.println(s1);
    }

    @Test
    public void test02(){
        String s = "//space.bilibili.com/3991980";
        String s1 = s.replaceAll("//space.bilibili.com/", "");
        System.out.println(s1);
    }

    @Test
    public void test03(){
        for (int i = 0; i < 100; i++){
            int max = 500;
            int min = 300;
            Random random = new Random();
            int s = random.nextInt(max)%(max-min+1) + min;
            System.out.println(s);
        }

    }

}
