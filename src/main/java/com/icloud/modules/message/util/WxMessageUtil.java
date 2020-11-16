package com.icloud.modules.message.util;

import com.alibaba.fastjson.JSONObject;
import com.icloud.api.util.WxUserInfoUtil;
import com.icloud.common.util.wx.CommonUtil;
import com.icloud.common.util.wx.WxConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 512162086@qq.com on 2019/4/17 .
 */
@Slf4j
@Component
public class WxMessageUtil {

    @Autowired
    private WxUserInfoUtil wxUserInfoUtil;

    public  JSONObject sendWeixinMessage(String newJson, int infoCount){
        JSONObject jsonObject = null;
        String access_token = wxUserInfoUtil.getAccessToken(2);
        if(access_token==null){
            log.warn("获取access_token失败:"+access_token+",请稍后再试");
            jsonObject = new JSONObject();
            jsonObject.put("errmsg","access_token为空");
            return jsonObject;
        }
        String urls = WxConst.SEND_TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", access_token);
        // 发送请求获取状态
        log.warn("body:" + newJson + ",url:"+ urls);
        //根据openid 和 基础access_token 获取用户订阅信息
        //JSONObject jsonResult = CommonUtil.httpRequest(PropertyConstants.getPropertiesKey("wxsendTemplateMsgUrl") + access_token, "GET",null); //发送目标消息

        try {
            jsonObject = CommonUtil.httpRequest(urls, "POST", newJson);
            infoCount++;
            log.warn("infoCount===" + infoCount);
            if(jsonObject!=null && jsonObject.containsKey("errcode") && "40001".equals(jsonObject.getString("errcode"))) {
                log.warn("获取access_token失败:errcode=" + jsonObject.get("errcode") + ";errmsg=" + jsonObject.get("errmsg"));
                if (infoCount > 3)
                {
                    jsonObject = new JSONObject();
                    jsonObject.put("errmsg","access_token失败");
                    return jsonObject;
                }
                jsonObject = sendWeixinMessage(newJson, infoCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = new JSONObject();
            jsonObject.put("errmsg",e.getMessage());
            return jsonObject;
        }
        if(jsonObject!=null && jsonObject.containsKey("errcode") && "0".equals(jsonObject.getString("errcode"))){
            log.warn("infoCount===" + infoCount+"  消息发送成功");
        }else {
            log.warn("infoCount===" + infoCount+"  消息发送失败");
        }
        return jsonObject;
    }
}
