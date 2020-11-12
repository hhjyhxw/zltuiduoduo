package com.icloud.modules.wx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-04-22 15:27:18
 */
@Data
@TableName("t_wx_user")
public class WxUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /*  */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /* openid */
    @TableField("openid")
    private String openid;
    /* xcxopenid */
    @TableField("xcxopenid")
    private String xcxopenid;
    /* 昵称 */
    @TableField("nickname")
    private String nickname;
    /* 性别 	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 */
    @TableField("sex")
    private Integer sex;
    /* 省 */
    @TableField("province")
    private String province;
    /* 市 */
    @TableField("city")
    private String city;
    /* 县 */
    @TableField("country")
    private String country;
    /* 头像 */
    @TableField("headimgurl")
    private String headimgurl;
    /* 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom） */
    @TableField("privilege")
    private String privilege;
    /* unionid */
    @TableField("unionid")
    private String unionid;
    /* 创建时间 */
    @TableField("create_time")
    private Date createTime;
    /* 更新时间 */
    @TableField("modify_time")
    private Date modifyTime;
    /* 联系电话 */
    @TableField("phone")
    private String phone;

    /* 登录类型 */
    @TableField("login_type")
    private String loginType;
    /* 最近登录时间 */
    @TableField("last_login_time")
    private Date lastLoginTime;
    /* 最近登录ip */
    @TableField("last_login_ip")
    private String lastLoginIp;
    /* 1正常用户 0、黑名单用户 */
    @TableField("status")
    private String status;
    /* 1正常用户 0、黑名单用户 */
    @TableField("subscribe")
    private String subscribe;//是否关注

    /* 推广码(唯一，微信临时场景二维码) */
    @TableField("tdd_code")
    private String tddCode;//推广码
    /**
     * 是否已经是推广者
     * 1已经绑定
     * 0未绑定
     */
    @TableField("isbind")
    private String isbind;
    /*推广名片*/
    @TableField("recommend_url")
    private String recommendUrl;
    /*更新推广名片时间*/
    @TableField("recommend_url_time")
    private Date recommendUrlTime;



    /* 临时存储用户位置经度的值 */
    @TableField(exist = false)
    private String lnt;
    /* 临时存储用户位置纬度的值  */
    @TableField(exist = false)
    private String lat;
    /*   */
    @TableField(exist = false)
    private String token;
    /**
     * 小程序根据code获取的值
     */
    @TableField(exist = false)
    private String sessionKey;


    @TableField(exist = false)
    private String parentTddCode;//推荐人推广码
    @TableField(exist = false)
    private String parentNick;//推荐人昵称
    @TableField(exist = false)
    private Integer maxNum;//最大可发展人数(-1 不限制)
    @TableField(exist = false)
    private Integer isable;//是否还可以发展
    @TableField(exist = false)
    private Integer readyedNum;//已发展人数
    /*是否是首次参与活动 0不是  1是*/
    @TableField(exist = false)
    private String isfirstin;



}
