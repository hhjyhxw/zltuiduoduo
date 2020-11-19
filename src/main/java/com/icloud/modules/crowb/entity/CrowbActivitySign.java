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
@TableName("t_crowb_activity_sign")
public class CrowbActivitySign implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /*  */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 活动id */
       @TableField("crowb_activity_id")
       private Long crowbActivityId;
   	   	   /* 姓名 */
       @TableField("name")
       private String name;
   	   	   /* 手机 */
       @TableField("phone")
       private String phone;
   	   	   /* 报名积分 */
       @TableField("score")
       private Integer score;
   	   	   /* 报名时间 */
       @TableField("create_time")
       private Date createTime;
   	   	   /* openid */
       @TableField("openid")
       private String openid;
   	   	   /* 昵称 */
       @TableField("nickname")
       private String nickname;
   	   	   /* 是否带同伴，停用标志 0不带，1带同伴(默认不带) */
       @TableField("take_partner")
       private String takePartner;
   	   	   /* 会员手机 */
       @TableField("memerber_phone")
       private String memerberPhone;
   	   	   /* 是否有赞助意向，停用标志 0否，1是 */
       @TableField("suportIntention")
       private String suportintention;
   	   	   /* 活动名称 */
       @TableField("activity_name")
       private String activityName;
   	   	   /* 单位名称 */
       @TableField("unit_name")
       private String unitName;
   	   	   /* 职务名称 */
       @TableField("office_name")
       private String officeName;
   	   	   /* 携带同伴人数 */
       @TableField("take_num")
       private Integer takeNum;
   	   	   /* //0已报名、待审核，1已报名，并且审核通过，2取消，3审核失败 */
       @TableField("verify_status")
       private String verifyStatus;
   	   	   /* 修改时间 */
       @TableField("modify_time")
       private Date modifyTime;
   	
}
