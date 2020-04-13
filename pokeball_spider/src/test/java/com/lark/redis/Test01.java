package com.lark.redis;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test01 {

    @Test
    public void test01(){
        String s = "https://api.bilibili.com/x/space/acc/info?mid=928;http://api.bilibili.com/x/relation/stat?vmid=928;https://api.bilibili.com/x/space/upstat?mid=928";
        String s1 = "{1},{2},{3}";
        String[] split = s1.split(",");
        System.out.println(Arrays.toString(split));
        String UrlUserInfo = split[0];
        String urlRelation = split[1];
        String urlUpStat = split[2];
        System.out.println(urlRelation + urlUpStat + UrlUserInfo);
    }

    @Test
    public void test02(){
        String s1 = "{1}";
        String s2 = "{2}";
        String s3 = "{1;2;3}";
        s3 = s3.replaceAll(";", "");
        String all = s1 + ";" + s2 + ";" + s3;

        String[] split = all.split(";");
        System.out.println(Arrays.toString(split));

    }

    @Test
    public void test03(){
        List list01 = new LinkedList<String>();

        System.out.println(list01);

        list01.add("1");
        list01.add("1");
        list01.add("1");
        list01.add("1");
        list01.add("1");

        System.out.println(list01);

        list01.clear();
        System.out.println(list01);
    }

    @Test
    public void test04(){
        BlockingQueue<String> strings = new LinkedBlockingQueue<>();
        List list = new LinkedList();
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");

        System.out.println(strings);
        System.out.println(list);
        System.out.println("=====================");

        strings.drainTo(list);

        System.out.println(strings);
        System.out.println(list);
        System.out.println("=====================");

        list.clear();
        System.out.println(list);
    }
    
    @Test
    public void test05(){
        String s = "http://i1.hdslb.com/bfs/face/45f5854862dc90c394a75dc23df166a8c390d904.jpg";
        int i = s.indexOf("face/");
        i = i + 5;
        String substring = s.substring(i, s.length());
        System.out.println(i);
        System.out.println(substring);
    }

}
