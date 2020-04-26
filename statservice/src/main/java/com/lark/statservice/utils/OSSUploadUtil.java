package com.lark.statservice.utils;

import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云oss图片上传
 */
@Component
@Slf4j
public class OSSUploadUtil {

    @Value("${aliyun.oss.file.endpoint}")
    String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    String accessKeyId;
    @Value("${aliyun.oss.file.keysecret}")
    String accessKeySecret;
    @Value("${aliyun.oss.file.bucketname}")
    String yourBucketName;

    public String uploadImg(String filePath) {
        File file = new File(filePath);
        //2.获取上传文件名称
        String filename = file.getName();
        //2.1给文件名加上一个uuid，让文件名不重重复
        String uuid = UUID.randomUUID().toString();
        filename = uuid + filename;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("oss图片上传找不到本地图片路径");
        }
        //2.2进一步做优化，如果用户量大的话，每天都建一个文件夹，用于存放当天的文件
        //获取当前日期,用工具类转换成这种格式2019/4/13
        String filepath = new DateTime().toString("yyyy/MM/dd");
        //拼成如下形式：2019/4/23/asdadad.txt
        filename = filepath + "/" + filename;

        //添加一个host，作为不同文件夹的名称，用来对上传的文件进行分类
        String hostName = "hotWords";
        filename = hostName + "/" + filename;

        //3.把上传的文件存储到阿里云oss里面
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        /*
         * 第一个参数：BucketName
         * 第二个参数：文件名
         * 第三个参数：文件输入流
         * */
        ossClient.putObject(yourBucketName, filename, in);

        // 关闭OSSClient。
        ossClient.shutdown();
        String imgurl = "https://" + yourBucketName + "." + endpoint + "/" + filename;

        return imgurl;

    }



}
