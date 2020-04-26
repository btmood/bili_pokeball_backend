package com.lark.statservice.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="PopularVideoInfo对象", description="")
public class PopularVideoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "和视频id相同")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String av;

    private String bv;

    @ApiModelProperty(value = "播放数")
    private Long view;

    @ApiModelProperty(value = "弹幕数")
    private Long danmaku;

    @ApiModelProperty(value = "回复数")
    private Long reply;

    @ApiModelProperty(value = "收藏数")
    private Long favorite;

    @ApiModelProperty(value = "硬币数")
    private Long coin;

    @ApiModelProperty(value = "分享数")
    private Long share;

    @ApiModelProperty(value = "点赞数")
    private Long likeNum;

    @ApiModelProperty(value = "是否是原创：1是，0不是")
    private String copyright;

    @ApiModelProperty(value = "b站综合得分")
    private Long biliScore;

    @ApiModelProperty(value = "视频排名（旧）")
    private Integer oldRanking;

    @ApiModelProperty(value = "视频排名（新）")
    private Integer newRanking;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;


}
