package com.icloud.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.icloud.modules.wx.entity.WxUser;
import lombok.Data;

import java.util.Date;

@Data
public class MyPrizeVo {
    /* id */
    private Long id;

    private String orderNo;
    /* 领取状态（0未领取 1已领取、2已过期） */
    private String status;
    /* 创建时间 */
    private Date createTime;
    /* 领取时间 */
    private Date receiveTime;
    /* 奖品名称 */
    private String prizeName;
    /* 领取过期时间 */
    private Date expireTime;
    /* 获奖方式（0参与活动活动 1下线扫码获得 2绑定成未 顶级推主） */
    private String awardsType;
    /*获奖积分值*/
    private Integer scores;

}
