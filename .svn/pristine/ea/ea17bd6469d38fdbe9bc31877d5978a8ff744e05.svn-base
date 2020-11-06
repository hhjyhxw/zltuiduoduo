package com.icloud.config.global;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@ConfigurationProperties(prefix="mypros")//加载自定义属性
@Configuration
public class MyPropertitys {

    //
    private String cdnPath;//
    //项目路径
    private String service_domain;
    //文件上传目录前缀
    private String uploadpath;
    //判断是在本地调试还是发布服务器
    private String activein;
    //
    private Session session;
    //第三方用户授权接口签名keys
    private String userauthorkey;
    ///获取用户是否关注信息 签名key
    private String isfollowkey;
    //new tuiduoduo 首页地址
    private String zltuiduoduo_index_url;
    //微信公众号参数配置
    private Wx wx;
    //龙币接口相关参数
    private LongCoin longcoin;
    //积分平台单点登录地址
    private String scorePlatformUrl;



    /**
     * spring redis session 相关参数
     */

    @Data
    public static class Session{
        private int timeout;//#spring-session中session过期时间 单位：秒
        private String namespace;//#spring-session中redis命名空间
        private String parentDomainName;// #父域名
        private String cookieName;//#cookie名字

    }

    /**
     * 微信相关参数
     */
    @Data
    public static class Wx {
        private String appid;
        private String appsecret;
        private String getUserInfo;//第三方登陆接口
        private String infokey;//登陆接口签名Key
        private String jssdk_key;//获取jssdk对象key

        private String hostnumber;//gh号 用于区分不同公众号的id
        private String imcchost;//登陆接口签名Key
        private String imcchostport;//登陆接口签名Key
        private String hosttel;//登陆接口签名Key
        private String host;
        private String qrcodePath;//二维码存放地址
    }


    /**
     * 龙币接口相关
     */
    @Data
    public static class LongCoin{
        private String sid;//商户号
        private String key;//数据签名key
        private String rechargetype;//充值类型
        private String consumetype;//消费类型
        private String queryUrl;//查询url
        private String rechargeUrl;//充值url
        private String consumeUrl;//消费url
        private String sid_signup;//新会员注册充值 商户号
        private String key_signup;//新会员注册充值 签名key
        private String chareAmount;//给新用户充值的龙币值
    }
}
