/**
 * @author
 * @version
 * 2018年8月15日 下午4:41:31
 */
package com.icloud.api.common;

import com.icloud.common.R;
import com.icloud.common.util.AccessTokenAndJsapiTicketUtil;
import com.icloud.common.util.wx.WxConst;
import com.icloud.common.util.wx.model.JsSDK;
import com.icloud.config.global.MyPropertitys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api("分享签名参数")
@RestController
@RequestMapping("/api/jsSdkConfig")
public class JsSdkConfigController {


	@Autowired
	private MyPropertitys myPropertitys;
	@Autowired
	private AccessTokenAndJsapiTicketUtil accessTokenAndJsapiTicketUtil;

	@ApiOperation(value="用户信息接口", notes="")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "url", value = "页面地址", required = true, paramType = "query", dataType = "String")
	})
	@RequestMapping(value = "/getJsSdkConfig",method = {RequestMethod.GET})
	@ResponseBody
	public R getJsSdkConfig(@RequestParam String url){
		try{
			JsSDK config = new JsSDK(myPropertitys.getWx().getAppid(), myPropertitys.getWx().getAppsecret(),url,accessTokenAndJsapiTicketUtil);
			return R.ok().put("config",config);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
	
	

}
