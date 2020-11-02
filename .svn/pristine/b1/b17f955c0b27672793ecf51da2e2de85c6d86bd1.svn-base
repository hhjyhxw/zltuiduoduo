package com.icloud.config.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.basecommon.service.redis.RedisService;
import com.icloud.common.IpUtil;
import com.icloud.common.SpringContextHolder;
import com.icloud.common.util.StringUtil;
import com.icloud.modules.sys.entity.SysWhiteip;
import com.icloud.modules.sys.service.SysWhiteipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 第三方ip拦截器
 */
public class ThirdInterfaceInterceptor implements HandlerInterceptor{


	public final static Logger log = LoggerFactory.getLogger(ThirdInterfaceInterceptor.class);
	public static Map<String,String> ipMap = new HashMap<String,String>();
	private  static SysWhiteipService beanWhiteipService = SpringContextHolder.getBean(SysWhiteipService.class);
	private  static RedisService redisService = SpringContextHolder.getBean(RedisService.class);

	static {
		loaLORreLoadIpList();
	}

	@Override
	public void afterCompletion(HttpServletRequest reqeust, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest reqeust, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest reqeust, HttpServletResponse response, Object arg2) throws Exception {
		String key = IpUtil.getIpAddr(reqeust);
		log.info("请求参数:ip==="+key+";uri==="+reqeust.getRequestURI());
		Object obectiplist = redisService.get("iplist");//方便修改ip 获取添加新白名单后，清空缓存，重新读取
		log.info("缓存中放行ip总数==="+obectiplist);
		if(obectiplist==null){
			loaLORreLoadIpList();
			redisService.set("iplist",ipMap.size(),300L);
		}
		if(StringUtil.checkStr(key)){
			if(StringUtil.checkObj(ipMap.get(key))){
				return true;
			}
		}
		log.info("不合法的ip==="+key);
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		JSONObject result = new JSONObject();
		result.put("code",4);
		result.put("message","ip鉴权失败");
		writer.print(result);
		writer.close();
		response.flushBuffer();
		return false;
	}


	public  static  void loaLORreLoadIpList(){
		SysWhiteip parm = new SysWhiteip();
		parm.setStatus("1");
		List<SysWhiteip> iplist = beanWhiteipService.list(new QueryWrapper<SysWhiteip>().eq("status","1"));
		ipMap.clear();//清空
		for (SysWhiteip ip:iplist){
			ipMap.put(ip.getIpname(),ip.getIpname());
		}
	}
}