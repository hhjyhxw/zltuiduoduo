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
@TableName("t_crowb_activity_scorerecord")
public class CrowbActivityScorerecord implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /*  */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;

       @TableField("crow_sign_id")
      private Long crowSignId;
   	   	   /*  */
       @TableField("seq")
       private String seq;
   	   	   /*  */
       @TableField("openid")
       private String openid;
   	   	   /*  */
       @TableField("create_time")
       private Date createTime;
   	
}
