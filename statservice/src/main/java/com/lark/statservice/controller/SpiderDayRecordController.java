package com.lark.statservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.statservice.pojo.SpiderDayRecord;
import com.lark.statservice.service.SpiderDayRecordService;
import com.lark.usercommon.entity.AjaxResponse;
import com.lark.usercommon.exception.CustomException;
import com.lark.usercommon.exception.CustomExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-04-25
 */
@RestController
@RequestMapping("/spiderDayRecord")
@CrossOrigin
public class SpiderDayRecordController {

    @Autowired
    private SpiderDayRecordService spiderDayRecordService;


    /**
     * 返回词云图片的阿里云oss链接
     * @param paramMap 当前时间 e.g. 2020-01-01
     * @return
     */
    @GetMapping("getWordImg/{currentDate}")
    public AjaxResponse getWordImg(@RequestBody Map<String,Object> paramMap) {
        String currentDate = (String) paramMap.get("currentDate");
        if (StringUtils.isEmpty(currentDate)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.REQUESTARGUMENTSERROR));
        }

        QueryWrapper<SpiderDayRecord> wrapper = new QueryWrapper<>();
        wrapper.like("create_time", currentDate);
        SpiderDayRecord spiderDayRecord = spiderDayRecordService.getOne(wrapper);
        String wordsImg = spiderDayRecord.getWordsImg();

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("wordsImg",wordsImg);
        return AjaxResponse.success(resultMap);
    }

}

