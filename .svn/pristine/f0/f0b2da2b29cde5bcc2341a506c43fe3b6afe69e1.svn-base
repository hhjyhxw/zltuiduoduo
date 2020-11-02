package com.icloud.front.userauthor;

import com.alibaba.fastjson.util.Base64;
import com.icloud.common.MD5Utils;
import com.icloud.config.global.MyPropertitys;
import com.icloud.modules.wx.entity.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @filename      : AuthorizeInterfaceController.java
 * @description   : 第三方调用接口，用户授权获取 用户信息(旧版备份)
 * @author        : zdh
 * @copyright     : zhumeng.com@crowdweb
 *
 * Modification History:
 * Date             Author       Version
 * --------------------------------------
 */
@Slf4j
@Controller
@RequestMapping(value = "/authorizeInterface")//旧版用户接口
//@RequestMapping(value = "/frontpage/authorizeInterface")
public class AuthorizeInterfaceController {
	
	 @Autowired
	 private MyPropertitys myPropertitys;
	 
	/**
     * http://localhost:9098/authorizeInterface/authorizeInterface!authorize.action
	 * @param state
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/authorizeInterface!authorize.action")
	public String authorize(String state, HttpServletRequest request,
							   HttpServletResponse response){
		log.warn("authorize--->参数state:" + state);
		String thirdRredirectUrl = null;
		try{
			//1、重定向url为空
			if (state==null || "".equals(state)) {
				log.warn("重定向url为空！");
				request.setAttribute("message", "重定向url为空！");
				return "modules/front/error";
			}
			WxUser user = (WxUser)request.getSession().getAttribute("wx_user");
			//2、获取网页授权accessToken和openid
			state = state.replace("-", "=").replace("_", "/");
			thirdRredirectUrl = new String(Base64.decodeFast(state));
			log.warn("getUserInfos decode state:" + thirdRredirectUrl);

			String sign = "openid=" + user.getOpenid() + "&unionid=" + user.getUnionid() + "&key=" + myPropertitys.getUserauthorkey();
			String param = MD5Utils.encode2hex(sign);
			String url = "openid=" + URLEncoder.encode(user.getOpenid(),"utf-8")
					+ "&unionid=" + URLEncoder.encode(user.getUnionid(),"utf-8")
					+ "&sign=" + param+"&nickName="+ URLEncoder.encode(user.getNickname(),"utf-8")
					+"&headPhoto="+ URLEncoder.encode(user.getHeadimgurl(),"utf-8")
					+"&subscribe="+user.getSubscribe();

			if (thirdRredirectUrl.indexOf("?") > 0)
				thirdRredirectUrl = (thirdRredirectUrl + "&" + url);
			else {
				thirdRredirectUrl = (thirdRredirectUrl + "?" + url);
			}
			log.warn("authorize_微信获取openid和unionid成功！" + thirdRredirectUrl);
			response.sendRedirect(thirdRredirectUrl);
			return null;
		}catch (Exception e){
			e.printStackTrace();
			request.setAttribute("message", "授权异常");
		}
		return "modules/front/error";
	}


}
