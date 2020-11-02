package com.icloud.front.userauthor;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.icloud.common.MD5Utils;
import com.icloud.common.util.AccessTokenAndJsapiTicketUtil;
import com.icloud.common.util.wx.CommonUtil;
import com.icloud.common.util.wx.WxConst;
import com.icloud.config.global.MyPropertitys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping({"/subcribeInfo"})
public class SubcribeInfoController
{


    @Autowired
    private AccessTokenAndJsapiTicketUtil AccessTokenAndJsapiTicketUtil;
    @Autowired
    private MyPropertitys myPropertitys;

    /**
     * http://localhost:9098/subcribeInfo/isFollow
     * @param request
     * @param openid
     * @param sign
     * @return
     */
    @RequestMapping({"/isFollow"})
    @ResponseBody
    public Map<String, Object> isFollow(HttpServletRequest request, String openid, String sign) {
        Map jsonMap = new HashMap();
        try {
            String key = myPropertitys.getIsfollowkey();
            jsonMap.put("openid", openid);
            String sign_str = MD5Utils.encode2hex(openid + key).toUpperCase();
            if (!sign_str.equals(sign)) {
                log.warn("isFollowOpenId=[" + openid + "], sign=[" + sign + "], sign_str=[" + sign_str + "]");
                jsonMap.put("message", "签名错误");
                return jsonMap;
            }

            String unionId = "";
            String isSubscribe = "2";
            JSONObject userInfojsonObject = getUserInfo(openid, 0);
            log.warn("用户信息userInfojsonObject====" + userInfojsonObject);
            if ((userInfojsonObject == null) || (!userInfojsonObject.containsKey("openid"))) {
                jsonMap.put("message", "未知，请稍后再试");
                jsonMap.put("isSubscribe", "2");
            } else {
                unionId = userInfojsonObject.get("unionid") != null ? userInfojsonObject.get("unionid").toString() : unionId;
                if (userInfojsonObject.containsKey("subscribe")) {
                    isSubscribe = userInfojsonObject.get("subscribe").toString();
                }
                if ("1".equals(isSubscribe)) {
                    jsonMap.put("message", "已关注");
                    jsonMap.put("isSubscribe", "1");
                } else if ("0".equals(isSubscribe)) {
                    jsonMap.put("message", "未关注");
                    jsonMap.put("isSubscribe", "0");
                } else {
                    jsonMap.put("message", "未知，请稍后再试");
                    jsonMap.put("isSubscribe", "2");
                }
            }
            jsonMap.put("unionid", unionId);
        } catch (Exception e) {
            log.error("异常,e==" + e.getMessage());
            jsonMap.put("message", "未知，请稍后再试");
            jsonMap.put("isSubscribe", "2");
            e.printStackTrace();
        }
        return jsonMap;
    }

    /**
     * http://localhost:9098/subcribeInfo/isFollowtest?openid=okxjsfhksliouqkkujhka
     * 用于本地测试
     * @param request
     * @param openid
     * @param sign
     * @return
     */
    @RequestMapping({"/isFollowtest"})
    @ResponseBody
    public Map<String, Object> isFollowtest(HttpServletRequest request, String openid, String sign) {
        Map jsonMap = new HashMap();
        try {
            String key = myPropertitys.getIsfollowkey();
            jsonMap.put("openid", openid);
            String unionId = "";
            String isSubscribe = "2";
            JSONObject userInfojsonObject = getUserInfo(openid, 0);
            log.warn("用户信息userInfojsonObject====" + userInfojsonObject);
            if ((userInfojsonObject == null) || (!userInfojsonObject.containsKey("openid"))) {
                jsonMap.put("message", "未知，请稍后再试");
                jsonMap.put("isSubscribe", "2");
            } else {
                unionId = userInfojsonObject.get("unionid") != null ? userInfojsonObject.get("unionid").toString() : unionId;
                if (userInfojsonObject.containsKey("subscribe")) {
                    isSubscribe = userInfojsonObject.get("subscribe").toString();
                }
                if ("1".equals(isSubscribe)) {
                    jsonMap.put("message", "已关注");
                    jsonMap.put("isSubscribe", "1");
                } else if ("0".equals(isSubscribe)) {
                    jsonMap.put("message", "未关注");
                    jsonMap.put("isSubscribe", "0");
                } else {
                    jsonMap.put("message", "未知，请稍后再试");
                    jsonMap.put("isSubscribe", "2");
                }
            }
            jsonMap.put("unionid", unionId);
        } catch (Exception e) {
            log.error("异常,e==" + e.getMessage());
            jsonMap.put("message", "未知，请稍后再试");
            jsonMap.put("isSubscribe", "2");
            e.printStackTrace();
        }
        return jsonMap;
    }

    private JSONObject getUserInfo(String openid, int infoCount)
    {
        String access_token = getAccessToken(0);
        if (access_token == null) {
            log.warn("获取access_token失败:" + access_token + ",请稍后再试");
            return null;
        }

        String userInfoUrl = WxConst.USER_INFO_BASE.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        log.warn("userInfoUrl======" + userInfoUrl);
        JSONObject userInfojsonObject = CommonUtil.httpRequest(userInfoUrl, "GET", null);
        infoCount++;
        log.warn("infoCount===" + infoCount);
        if ((userInfojsonObject != null) && (userInfojsonObject.containsKey("errcode")) && ("40001".equals(userInfojsonObject.get("errcode").toString()))) {
            log.warn("获取access_token失败:errcode=" + userInfojsonObject.get("errcode") + ";errmsg=" + userInfojsonObject.get("errmsg"));
            if (infoCount > 5) {
                return null;
            }
            userInfojsonObject = getUserInfo(openid, infoCount);
        }
        return userInfojsonObject;
    }

    private String getAccessToken(int count)
    {
        String access_token = AccessTokenAndJsapiTicketUtil.getAccessToken();
        count++;
        log.warn("count==" + count);
        if (access_token == null) {
            log.warn("获取access_token失败:" + access_token + ",请稍后再试;获取基础access_token为空的次数count==" + count);
            if (count > 3) {
                return null;
            }
            access_token = getAccessToken(count);
        }
        return access_token;
    }



}