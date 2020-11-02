package com.icloud.modules.zltdd.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广用户表
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Data
@TableName("t_zltdd_recommend")
public class ZltddRecommend implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 用户昵称 */
       @TableField("nickname")
       private String nickname;
   	   	   /* openid */
       @TableField("openid")
       private String openid;
   	   	   /* 头像 */
       @TableField("headimgurl")
       private String headimgurl;
   	   	   /* 活动id */
       @TableField("activity_id")
       private Long activityId;
   	   	   /* 父类id */
       @TableField("parent_id")
       private Long parentId;
   	   	   /* 用户类型 */
       @TableField("user_type")
       private String userType;
   	   	   /* 0为无无限发展 */
       @TableField("max_num")
       private Integer maxNum;
   	   	   /* 已发展人数 */
       @TableField("readyed_num")
       private Integer readyedNum;
   	   	   /* 场景码(推广参数) */
       @TableField("myscene_id")
       private String mysceneId;
   	   	   /* 父类场景码（父类推广参数） */
       @TableField("parent_scene_id")
       private String parentSceneId;
   	
}
