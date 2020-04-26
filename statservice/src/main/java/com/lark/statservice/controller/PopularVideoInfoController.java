package com.lark.statservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.statservice.pojo.PopularVideoInfo;
import com.lark.statservice.service.PopularVideoInfoService;
import com.lark.usercommon.entity.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-04-25
 */
@RestController
@RequestMapping("/popularVideoInfo")
@CrossOrigin
public class PopularVideoInfoController {

    @Autowired
    private PopularVideoInfoService popularVideoInfoService;

    @GetMapping("/getSuperVideo")
    public AjaxResponse getSuperVideo() {

        QueryWrapper<PopularVideoInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("bili_score");
        wrapper.last("limit 0, 10");
        List<PopularVideoInfo> popularVideoInfoList = popularVideoInfoService.list(wrapper);
        System.out.println(popularVideoInfoList);
        return AjaxResponse.success(popularVideoInfoList);
    }

}

