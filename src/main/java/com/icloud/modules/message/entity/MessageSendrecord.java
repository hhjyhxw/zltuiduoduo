package com.icloud.modules.message.entity;

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
 * @date 2020-11-11 16:06:38
 */
@Data
@TableName("t_message_sendrecord")
public class MessageSendrecord implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /*  */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 本地消息模板id(活动id) */
       @TableField("message_id")
       private Long messageId;
   	   	   /* 用户openid */
       @TableField("openid")
       private String openid;
   	   	   /* 发送状态(0未发送 1已发送 2发送失败) */
       @TableField("status")
       private String status;
   	   	   /* 发送结果描述 */
       @TableField("msg")
       private String msg;
   	   	   /* 发送时间 */
       @TableField("create_time")
       private Date createTime;
   	   	   /* 更新时间 */
       @TableField("modify_time")
       private Date modifyTime;
   	
}
