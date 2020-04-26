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
@TableName("spider_video_popular_tags")
@ApiModel(value="PopularVideoTags对象", description="")
public class PopularVideoTags implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "和视频id相同")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String av;

    private String bv;

    @ApiModelProperty(value = "视频标签")
    private String tags;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;


}
