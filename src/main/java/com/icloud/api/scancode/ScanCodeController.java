package com.icloud.api.scancode;

import com.alibaba.fastjson.JSONObject;
import com.icloud.api.util.WxUserInfoUtil;
import com.icloud.basecommon.service.redis.RedisService;
import com.icloud.common.util.wx.XmlMessageUtil;
import com.icloud.config.global.MyPropertitys;
import com.icloud.modules.zltdd.service.ZltddRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.Map;

/**
 * http://zl.haiyunzy.com/zltdd/api/scanCode/pushevent
 */
@Slf4j
@RequestMapping("/api/scanCode")
@Controller
public class ScanCodeController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ZltddRecommendService zltddRecommendService;
    @Autowired
    private MyPropertitys myPropertitys;



    @RequestMapping("/pushevent")
    @ResponseBody
    public String pushevent(HttpServletRequest request){
        try {
            log.info("...扫码事件回调开始...");
            BufferedReader br = request.getReader();
            String inputLine;
            String str = "";

            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();

            str = URLDecoder.decode(str);
            str = str.replaceAll("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "").replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
            log.info("回调消息："+str);
            Map<String,String> msgMap = XmlMessageUtil.parseXmlByString(str);
            String openid = msgMap.get("FromUserName");
            String gh = msgMap.get("ToUserName");
            String EventKey = msgMap.get("EventKey");//场景值
            String event = msgMap.get("Event");
            String createTime = msgMap.get("CreateTime");
            String rediskey = openid+createTime;
            if(redisService.exists(rediskey)){
                log.info("消息"+createTime+"1分钟内已推送");
                return "";
            }else{
                log.info("消息"+createTime+"首次推送");
                redisService.set(rediskey,"1",20L);//20秒
            }

            if(event.equals("subscribe")){
                EventKey = EventKey.replace("qrscene_", "");
            }
            JSONObject result = zltddRecommendService.dealScancodeEevent(openid,EventKey);
            StringBuffer sbf = new StringBuffer();
            sbf.append("<xml>");
            sbf.append("<ToUserName><![CDATA[").append(openid).append("]]></ToUserName>");
            sbf.append("<FromUserName><![CDATA[").append(gh).append("]]></FromUserName>");
            sbf.append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>");
            sbf.append("<MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");

            if("-1".equals(result.getString("code"))){
                sbf.append("对不起，您已有唯一推荐人，推荐人一经确立不可修改。\n" +
                        "欢迎加入 真龙大家庭\n" +
                        "\n" +
                        "扫码可为推荐人累积奖励，您也赶紧推荐好友来参加吧！\n活动详情请点击：<a href='"+myPropertitys.getZltuiduoduo_index_url()+"'>寻找龙的传人主页</a>");
            }else if("1".equals(result.getString("code"))){
                sbf.append("您的好友"+result.getString("nickname")+"推荐您参加“寻找龙的传人”活动，生成自己的推广名片，推荐好友参加活动，丰富好礼等你拿，赶紧参与起来吧！  \n" +
                        "扫码可为推荐人累积奖励，您也赶紧推荐好友来参加吧！\n活动详情请点击：<a href='"+myPropertitys.getZltuiduoduo_index_url()+"'>寻找龙的传人主页</a>");
            }else{
                sbf.append(result.getString("msg"));
            }

            //返回消息
            sbf.append("]]></Content>");
            sbf.append("</xml>");

            return sbf.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
