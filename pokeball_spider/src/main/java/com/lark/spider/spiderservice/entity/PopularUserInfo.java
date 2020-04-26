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
@TableName("spider_user_popular_base_info")
public class PopularUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的uid
     */
    @TableId(value = "uid", type = IdType.INPUT)
    private String uid;

    /**
     * 用户等级
     */
    private String level;

    /**
     * 用户性别0：男，1：女
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户签名
     */
    private String signature;

    /**
     * 用户是否为大会员0：不是，1：是
     */
    private String superVip;

    /**
     * 用户昵称
     */
    private String nickname;

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
