package com.icloud.modules.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.icloud.common.util.StringUtil;
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


    public String getMessageJson(String openid) {
        // 数据开始
        JSONObject jsonObj = new JSONObject();
        JSONObject data = new JSONObject();

        /*1开头语*/
        JSONObject first = new JSONObject();
        first.put("color", "#173177");
        first.put("value", StringUtil.checkStr(this.first)?this.first:"xxxxxx！");
        /*2活动主题\活动名称*/
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", StringUtil.checkStr(this.keyword1)?this.keyword1:"XXX活动！");
        keyword1.put("color", "#173177");
        /*3、活动时间*/
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", StringUtil.checkStr(this.keyword2)?this.keyword2:"2014-10-23-2014-10-25");
        keyword2.put("color", "#173177");
        /*4活动主办*/
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value", StringUtil.checkStr(this.keyword3)?this.keyword3:"广西中烟");
        keyword3.put("color", "#173177");

        JSONObject keyword4 = null;
        if(StringUtil.checkStr(this.keyword4)){
            keyword4 = new JSONObject();
            keyword4.put("value", StringUtil.checkStr(this.keyword4)?this.keyword4:"");
            keyword4.put("color", "#173177");
        }
        JSONObject keyword5 = null;
        if(StringUtil.checkStr(this.keyword4)){
            keyword5 = new JSONObject();
            keyword5.put("value", StringUtil.checkStr(this.keyword5)?this.keyword5:"");
            keyword5.put("color", "#173177");
        }

        /*5备注*/
        JSONObject remark = new JSONObject();
        String remarkStr = "如有疑问请于工作日上班时间，咨询真龙微信在线客服或致电客服热线：4008792099。";
        remark.put("value", remarkStr);
        remark.put("color", "#173177");

        data.put("first", first); //开头语
        data.put("keyword1", keyword1); // 活动主题
        data.put("keyword2", keyword2);// 活动时间
        data.put("keyword3", keyword3);// 活动地点
        if(keyword4!=null){
            data.put("keyword4", keyword4);
        }
        if(keyword5!=null){
            data.put("keyword5", keyword5);
        }

        data.put("remark", remark);// 备注
        jsonObj.put("url", this.visitUrl);
        jsonObj.put("touser", openid);
        jsonObj.put("template_id",this.templateId);
        jsonObj.put("data", data);

        return jsonObj.toString();
    }
   	
}
