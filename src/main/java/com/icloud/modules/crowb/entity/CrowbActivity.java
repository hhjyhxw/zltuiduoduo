package com.icloud.modules.crowb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:20
 */
@Data
@TableName("t_crowb_activity")
public class CrowbActivity implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /*  */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 名称 */
       @TableField("title")
       private String title;
        /* 活动编号 */
        @TableField("activity_no")
        private String activityNo;
   	   	   /* 需要积分值 */
       @TableField("score")
       private Integer score;
   	   	   /* 活动人数 */
       @TableField("total")
       private Integer total;
   	   	   /* 已报名人数 */
       @TableField("signed")
       private Integer signed;
   	   	   /* 活动开始时间 */
       @TableField("starttime")
       private Date starttime;
   	   	   /* 活动结束时间 */
       @TableField("endtime")
       private Date endtime;
   	   	   /* 停用标志 0表示停用，1表示启用(默认启用) */
       @TableField("status")
       private String status;
   	   	   /* 消息发送状态0未发送 1表示活动成功已发送 2表示众筹失败发送消息 */
       @TableField("send_status")
       private String sendStatus;
   	   	   /* 活动介绍描述 */
       @TableField("description")
       private String description;
   	   	   /* 活动规则描述 */
       @TableField("rule")
       private String rule;
   	   	   /* 活动入口链接 */
       @TableField("link")
       private String link;
   	   	   /* 报名成功后参与地址 */
       @TableField("attend_address")
       private String attendAddress;
   	   	   /* 报名成功后参与时间 */
       @TableField("attend_time")
       private String attendTime;
   	   	   /* 创建时间 */
       @TableField("create_time")
       private Date createTime;
   	   	   /* 创建时间 */
       @TableField("modify_time")
       private Date modifyTime;
   	   	   /* 是否需要积分支付积分(0不需要 1需要 默认1) */
       @TableField("isneed_score")
       private String isneedScore;
        /* 背景图片 */
        @TableField("bg_img")
        private String bgImg;
        /*活动状态 0未开始 1、进行中 2、成功结束 3 失败结束 4活动结束 状态未更新 5活动已停止*/
        @TableField(exist = false)
        private String activityStatus;
        /*用户报名状态 -1未报名 0已经报名,待审核  1已经报名，并且审核通过  2已经报名，且已取消 3审核失败*/
        @TableField(exist = false)
        private String signState;
   	
}
