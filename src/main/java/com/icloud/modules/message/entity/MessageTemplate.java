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
@TableName("t_message_template")
public class MessageTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 消息头部描述 */
       @TableField("first")
       private String first;
   	   	   /* 关键字1 */
       @TableField("keyword1")
       private String keyword1;
   	   	   /* 关键字2 */
       @TableField("keyword2")
       private String keyword2;
   	   	   /* 关键字3 */
       @TableField("keyword3")
       private String keyword3;
   	   	   /* 关键字4 */
       @TableField("keyword4")
       private String keyword4;
   	   	   /* 关键字5 */
       @TableField("keyword5")
       private String keyword5;
   	   	   /* 消息底部描述 */
       @TableField("remark")
       private String remark;
   	   	   /* 微信模板id */
       @TableField("template_id")
       private String templateId;
   	   	   /* 点击模板跳转地址 */
       @TableField("visit_url")
       private String visitUrl;
   	   	   /* 模板标题（用于管理员识别） */
       @TableField("titile")
       private String titile;
   	   	   /* 消息发送处理器（根据业务需求填写） */
       @TableField("deal_zclass")
       private String dealZclass;
   	   	   /* 状态（0停用、1启用） */
       @TableField("status")
       private String status;
   	   	   /* 是否已发送（0未发送 1已发送） */
       @TableField("send_status")
       private String sendStatus;
   	
}
