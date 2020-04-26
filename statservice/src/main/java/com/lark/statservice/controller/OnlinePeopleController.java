package com.lark.statservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.statservice.pojo.OnlinePeople;
import com.lark.statservice.service.OnlinePeopleService;
import com.lark.usercommon.entity.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-04-24
 */
@RestController
@RequestMapping("/onlinePeople")
@CrossOrigin
@Slf4j
public class OnlinePeopleController {

    @Autowired
    private OnlinePeopleService onlinePeopleService;

    @GetMapping("test")
    public AjaxResponse test(){
        OnlinePeople onlinePeople = new OnlinePeople();
        onlinePeople.setWebOnline(123l);
        onlinePeople.setPlayOnline(345l);
        onlinePeopleService.save(onlinePeople);
        return AjaxResponse.success("成功了！！！");
    }

    /**
     * 返回今天当前hour的在线人数
     * @param hour
     * @return
     */
    @GetMapping("getOnlinePeople/{hour}")
    public AjaxResponse getOnlinePeople(@PathVariable("hour") String hour) {
        String currentNYR = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        String queryTime = currentNYR + " " + hour;
        QueryWrapper<OnlinePeople> wrapper = new QueryWrapper<>();
        wrapper.like("create_time", queryTime);
        List<OnlinePeople> list = onlinePeopleService.list(wrapper);
        System.out.println(list);
        return AjaxResponse.success(list);
    }

    /**
     * 返回当前小时前一小时相关在线人数，20条
     * @param hour 当前小时的前一小时
     * @return
     */
    @GetMapping("getOnlinePeopleBatch/{hour}")
    public AjaxResponse getOnlinePeopleBatch(@PathVariable("hour") String hour) {
        String currentNYR = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        String queryTime = currentNYR + " " + hour;
        List<OnlinePeople> onlinePeopleList = onlinePeopleService.getOnlinePeopleBatch(queryTime);
        System.out.println(onlinePeopleList);
        return AjaxResponse.success(onlinePeopleList);
    }


}

