package com.lark.spider.spiderservice.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("spider_video_popular_base_info")
public class PopularVideoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String av;

    private String bv;

    /**
     * 播放数
     */
    private Long view;

    /**
     * 弹幕数
     */
    private Long danmaku;

    /**
     * 回复数
     */
    private Long reply;

    /**
     * 收藏数
     */
    private Long favorite;

    /**
     * 硬币数
     */
    private Long coin;

    /**
     * 分享数
     */
    private Long share;

    /**
     * 点赞数
     */
    private Long likeNum;

    /**
     * 是否是原创：1是，0不是
     */
    private String copyright;

    /**
     * b站综合得分
     */
    private Long biliScore;

    /**
     * 视频排名（旧）
     */
    private Integer oldRanking;

    /**
     * 视频排名（新）
     */
    private Integer newRanking;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;


}
