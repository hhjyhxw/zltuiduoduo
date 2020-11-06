package com.icloud.api.util;//package com.icloud.front.userauthor;

import com.alibaba.fastjson.JSONObject;
import com.icloud.common.util.AccessTokenAndJsapiTicketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 512162086@qq.com on 2019/3/15 .
 */
@Component
public class WxUserInfoUtil {


    @Autowired
    private AccessTokenAndJsapiTicketUtil accessTokenAndJsapiTicketUtil;

    public final static Logger log = LoggerFactory.getLogger(WxUserInfoUtil.class);

    public  JSONObject  getUserInfo(String openid, int infoCount){
        String access_token = getAccessToken(infoCount);
        if(access_token==null){
            log.warn("获取access_token失败:"+access_token+",请稍后再试");
            return null;
        }
        //根据openid 和 基础access_token 获取用户订阅信息
        JSONObject userInfojsonObject =  WeixinInterfaceUtil.getUserinfoByBaseAccessToken(access_token, openid);
        infoCount++;
        log.warn("infoCount==="+infoCount);
        if(userInfojsonObject!=null && userInfojsonObject.containsKey("errcode") && "40001".equals(userInfojsonObject.getString("errcode"))){
            log.warn("获取access_token失败:errcode="+userInfojsonObject.get("errcode")+";errmsg="+userInfojsonObject.get("errmsg"));
            if(infoCount>5){
                return null;
            }
            userInfojsonObject = getUserInfo(openid,infoCount);
        }
        return userInfojsonObject;
    }

    /**
     * 尝试多次获取基础 access_token
     * @author   : zdh
     * @date     :
     * @version  : 1.0
     * @return   :
     */
    public String getAccessToken(int count){
        String access_token = accessTokenAndJsapiTicketUtil.getAccessToken();
        count++;
        log.warn("count=="+count);
        if(access_token==null){
            log.warn("获取access_token失败:"+access_token+",请稍后再试;获取基础access_token为空的次数count=="+count);
            if(count>3){
                return null;
            }
            access_token = getAccessToken(count);
        }
        return access_token;
    }
}
