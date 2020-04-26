package com.lark.spider.core.repository.impl;

import com.lark.spider.core.repository.IVideoRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("videoRepositoryImpl")
@Scope("prototype")
@Data
@Slf4j
public class VideoRepositoryImpl implements IVideoRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${biliUrl.video.videoInfoURL}")
    private String videoInfoURL;

    @Value("${biliUrl.video.videoTagURL}")
    private String videoTagURL;

    private String tempavscore;

    @Override
    public String getRankVideoBaseInfoUrl() {
        String avscore = (String) redisTemplate.opsForSet().pop("bili:video:rank:avscore");
        this.tempavscore = avscore;
        if (StringUtils.isEmpty(avscore)) {
            return null;
        }
        String wholeURL = videoInfoURL + avscore;
        return wholeURL;
    }

    @Override
    public String getRankVideoTagUrl() {
//        String avcore = (String) redisTemplate.opsForSet().pop("bili:video:rank:avscore");
        String avscore = this.tempavscore;
        if (StringUtils.isEmpty(avscore)) {
            return null;
        }
        String[] split = avscore.split(";");
        String wholeURL = videoTagURL + split[0];
        return wholeURL;
    }
}
