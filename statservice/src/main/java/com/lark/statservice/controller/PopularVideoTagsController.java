package com.lark.statservice.controller;


import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.filter.Filter;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import com.lark.statservice.pojo.PopularVideoTags;
import com.lark.statservice.scheduler.GenerateWordsImageTask;
import com.lark.statservice.service.PopularVideoTagsService;
import com.lark.statservice.utils.OSSUploadUtil;
import com.lark.usercommon.entity.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-04-25
 */
@RestController
@RequestMapping("/popularVideoTags")
@CrossOrigin
public class PopularVideoTagsController {

    @Autowired
    private OSSUploadUtil ossUploadUtil;

    @Autowired
    private PopularVideoTagsService popularVideoTagsService;

    @RequestMapping("test")
    public AjaxResponse test01(){
        String filePath = "C:\\Users\\ZHOU\\Desktop\\img\\4.jpg";
        String imgPath = ossUploadUtil.uploadImg(filePath);
        System.out.println(imgPath);
        return AjaxResponse.success(imgPath);
    }

    @RequestMapping("test1")
    public AjaxResponse test1(){
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        //最多展示关键字数量
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        //词语最短长度
        frequencyAnalyzer.setMinWordLength(1);
        //中文解析器
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

        Filter filter = new Filter() {
            @Override
            public boolean test(String s) {
                if (s.matches("^\\d+$")) {
                    return false;
                }
                return true;
            }
        };
        frequencyAnalyzer.setFilter(filter);


        final List<WordFrequency> wordFrequencies = new ArrayList<>();

        LinkedList<String> wordsList = new LinkedList<>();

        List<PopularVideoTags> popularVideoTagsList = popularVideoTagsService.list();
        int i = 0;
        for (PopularVideoTags popularVideoTags : popularVideoTagsList) {
            i++;
            if (i == 50) {
                break;
            }
            String tagString = popularVideoTags.getTags();
            String[] split = tagString.split(",");
            for (String s : split) {
                wordsList.add(s);
            }
        }
        for (String word : wordsList){
            wordFrequencies.add(new WordFrequency(word,new Random().nextInt(wordsList.size())));
        }
        System.out.println("设置词语结束");

        //此处不设置会出现中文乱码
//        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 18);

        //初始化画板
        final Dimension dimension = new Dimension(1500, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        java.awt.Font font = new java.awt.Font("STSong-Light", 5, 100);
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackground(new RectangleBackground(dimension));

        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(20, 100));
        //设置词云显示的三种颜色，越靠前设置表示词频越高的词语的颜色

        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("d://3.png");

        String path = ossUploadUtil.uploadImg("d://3.png");
        System.out.println(path);

        return AjaxResponse.success(path);
    }

    @Autowired
    private GenerateWordsImageTask generateWordsImageTask;
    @RequestMapping("test2")
    public void test2(){
        generateWordsImageTask.makeImg();
    }



}

