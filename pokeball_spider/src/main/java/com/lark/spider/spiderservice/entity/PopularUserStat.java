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
@TableName("spider_user_popular_stat")
public class PopularUserStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid", type = IdType.INPUT)
    private String uid;

    /**
     * 粉丝数
     */
    private String fansNumber;

    /**
     * 播放数
     */
    private String playNumber;

    /**
     * 被点赞数
     */
    private String praiseNumber;

    /**
     * 关注别人数
     */
    private String concernNumber;

    /**
     * 用户排名（旧）
     */
    private String oldRanking;

    /**
     * 用户排名（新）
     */
    private String newRanking;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;


}
