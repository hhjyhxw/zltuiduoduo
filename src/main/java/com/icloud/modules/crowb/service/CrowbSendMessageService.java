package com.icloud.modules.crowb.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.common.util.StringUtil;
import com.icloud.config.global.MyPropertitys;
import com.icloud.modules.crowb.entity.CrowbActivity;
import com.icloud.modules.crowb.entity.CrowbActivitySign;
import com.icloud.modules.message.entity.MessageTemplate;
import com.icloud.modules.message.service.MessageTemplateService;
import com.icloud.modules.message.util.WxMessageUtil;
import com.icloud.modules.message.vo.BaseMessagaeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CrowbSendMessageService {

    @Autowired
    private MyPropertitys myPropertitys;
    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private CrowbActivityService crowbActivityService;
    @Autowired
    private CrowbActivitySignService crowbActivitySignService;
    @Autowired
    private WxMessageUtil wxMessageUtil;

    //报名成功
   public void sendSignSuccessMessage(CrowbActivitySign crowbActivitySign){
       crowbActivitySign = (CrowbActivitySign) crowbActivitySignService.getById(crowbActivitySign.getId());
       List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>()
               .eq("template_code","signsuccess"));
       MessageTemplate  messageTemplate = messageTemplateList.get(0);
       BaseMessagaeVo vo =  new BaseMessagaeVo();
       BeanUtils.copyProperties(messageTemplate,vo);
       vo.setOpenId(crowbActivitySign.getOpenid());
       vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
       String message = vo.getMessageJson();
       wxMessageUtil.sendWeixinMessage(message,1);
    }

    //审核成功
    public void sendVerifySuccessMessage(CrowbActivitySign passSign){
        passSign = (CrowbActivitySign) crowbActivitySignService.getById(passSign.getId());
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>()
                .eq("template_code","verifysuccess"));
        MessageTemplate  messageTemplate = messageTemplateList.get(0);
        if("dev".equals(myPropertitys.getActivein()) || "local".equals(myPropertitys.getActivein())){
            BaseMessagaeVo vo =  new BaseMessagaeVo();
            BeanUtils.copyProperties(messageTemplate,vo);
            vo.setOpenId(passSign.getOpenid());
            vo.setKeyword1(new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
            vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
            String message = vo.getMessageJson();
            wxMessageUtil.sendWeixinMessage(message,1);
        }else if("pro".equals(myPropertitys.getActivein())){
            // 数据开始
            JSONObject jsonObj = new JSONObject();
            JSONObject data = new JSONObject();
            /*0开头语*/
            JSONObject first = new JSONObject();
            first.put("color", "#173177");
            first.put("value", messageTemplate.getFirst());
            /*1获得积分时间*/
            JSONObject time = new JSONObject();
            time.put("value",new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
            time.put("color", "#173177");
            /*2、获得积分*/
            JSONObject Point = new JSONObject();
            Point.put("value",messageTemplate.getKeyword2());
            Point.put("color", "#173177");
            /*3获得原因*/
            JSONObject type = new JSONObject();
            type.put("value", messageTemplate.getKeyword3());
            type.put("color", "#173177");

            JSONObject From = null;
            From = new JSONObject();
            From.put("value", messageTemplate.getKeyword4());
            From.put("color", "#173177");
            /*5备注*/
            JSONObject remark = new JSONObject();
            String remarkStr = messageTemplate.getRemark();
            remark.put("value", remarkStr);
            remark.put("color", "#173177");

            data.put("first", first); //开头语
            data.put("time", time); // 时间
            data.put("Point", Point);// 积分值
            data.put("type", type);// 类型
            data.put("From", From);//获得原因


            data.put("remark", remark);// 备注
            jsonObj.put("url",messageTemplate.getVisitUrl()!=null?messageTemplate.getVisitUrl():"javascript:void(0);");
            jsonObj.put("touser", passSign.getOpenid());
            jsonObj.put("template_id",messageTemplate.getTemplateId());
            jsonObj.put("data", data);
            wxMessageUtil.sendWeixinMessage(jsonObj.toString(),1);

        }

    }

    //审核失败
    public void sendVerifyFailMessage(CrowbActivitySign crowbActivitySign){
        crowbActivitySign = (CrowbActivitySign) crowbActivitySignService.getById(crowbActivitySign.getId());
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>()
                .eq("template_code","verifyfair"));
        MessageTemplate  messageTemplate = messageTemplateList.get(0);
        if("dev".equals(myPropertitys.getActivein()) || "local".equals(myPropertitys.getActivein())){
            BaseMessagaeVo vo =  new BaseMessagaeVo();
            BeanUtils.copyProperties(messageTemplate,vo);
            vo.setOpenId(crowbActivitySign.getOpenid());
            vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
            String message = vo.getMessageJson();
            wxMessageUtil.sendWeixinMessage(message,1);
        }else if("pro".equals(myPropertitys.getActivein())){
            // 数据开始
            JSONObject jsonObj = new JSONObject();
            JSONObject data = new JSONObject();
            /*0开头语*/
            JSONObject first = new JSONObject();
            first.put("color", "#173177");
            first.put("value", messageTemplate.getFirst());
            /*1账号*/
            JSONObject account = new JSONObject();
            account.put("value",new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
            account.put("color", "#173177");
            /*2、时间*/
            JSONObject time = new JSONObject();
            time.put("value",messageTemplate.getKeyword2());
            time.put("color", "#173177");
            /*3内容*/
            JSONObject content = new JSONObject();
            content.put("value", messageTemplate.getKeyword3());
            content.put("color", "#173177");

            /*5备注*/
            JSONObject remark = new JSONObject();
            String remarkStr = messageTemplate.getRemark();
            remark.put("value", remarkStr);
            remark.put("color", "#173177");

            data.put("first", first); //开头语
            data.put("account", account); // 账号
            data.put("time", time); // 时间
            data.put("content", content);// 积分值

            data.put("remark", remark);// 备注
            jsonObj.put("url",messageTemplate.getVisitUrl()!=null?messageTemplate.getVisitUrl():"javascript:void(0);");
            jsonObj.put("touser", crowbActivitySign.getOpenid());
            jsonObj.put("template_id",messageTemplate.getTemplateId());
            jsonObj.put("data", data);
            wxMessageUtil.sendWeixinMessage(jsonObj.toString(),1);

        }
    }

    //取消报名
    public void sendCancelSignMessage(CrowbActivitySign crowbActivitySign){
        crowbActivitySign = (CrowbActivitySign) crowbActivitySignService.getById(crowbActivitySign.getId());
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>()
                .eq("template_code","cancelsign"));
        MessageTemplate  messageTemplate = messageTemplateList.get(0);
        if("dev".equals(myPropertitys.getActivein()) || "local".equals(myPropertitys.getActivein())){
            BaseMessagaeVo vo =  new BaseMessagaeVo();
            BeanUtils.copyProperties(messageTemplate,vo);
            vo.setOpenId(crowbActivitySign.getOpenid());
            vo.setKeyword1(new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
            vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
            String message = vo.getMessageJson();
            wxMessageUtil.sendWeixinMessage(message,1);
        }else if("pro".equals(myPropertitys.getActivein())){
            // 数据开始
            JSONObject jsonObj = new JSONObject();
            JSONObject data = new JSONObject();
            /*0开头语*/
            JSONObject first = new JSONObject();
            first.put("color", "#173177");
            first.put("value", messageTemplate.getFirst());
            /*1获得积分时间*/
            JSONObject time = new JSONObject();
            time.put("value",new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
            time.put("color", "#173177");
            /*2、获得积分*/
            JSONObject Point = new JSONObject();
            Point.put("value",messageTemplate.getKeyword2());
            Point.put("color", "#173177");
            /*3获得原因*/
            JSONObject type = new JSONObject();
            type.put("value", messageTemplate.getKeyword3());
            type.put("color", "#173177");

            JSONObject From = null;
            From = new JSONObject();
            From.put("value", messageTemplate.getKeyword4());
            From.put("color", "#173177");
            /*5备注*/
            JSONObject remark = new JSONObject();
            String remarkStr = messageTemplate.getRemark();
            remark.put("value", remarkStr);
            remark.put("color", "#173177");

            data.put("first", first); //开头语
            data.put("time", time); // 时间
            data.put("Point", Point);// 积分值
            data.put("type", type);// 类型
            data.put("From", From);//获得原因


            data.put("remark", remark);// 备注
            jsonObj.put("url",messageTemplate.getVisitUrl()!=null?messageTemplate.getVisitUrl():"javascript:void(0);");
            jsonObj.put("touser", crowbActivitySign.getOpenid());
            jsonObj.put("template_id",messageTemplate.getTemplateId());
            jsonObj.put("data", data);
            wxMessageUtil.sendWeixinMessage(jsonObj.toString(),1);

        }

    }

    //众筹成功
    public void sendCrowSuccessMessage(CrowbActivity crowbActivity){
        List<CrowbActivitySign> list = crowbActivitySignService.list(new QueryWrapper<CrowbActivitySign>().eq("crowb_activity_id",crowbActivity.getId()) .eq("verify_status","1"));//审核通过的
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>().eq("template_code","crowsuccess"));
        int total = list!=null?list.size():0;
        log.info("total==="+total);
        int successTotal = 0;

        crowbActivity.setSendStatus("1");
        crowbActivity.setModifyTime(new Date());
        crowbActivityService.updateById(crowbActivity);

        if(list!=null && list.size()>0 && messageTemplateList!=null && messageTemplateList.size()>0){
            MessageTemplate  messageTemplate = messageTemplateList.get(0);
            BaseMessagaeVo vo =  new BaseMessagaeVo();
            BeanUtils.copyProperties(messageTemplate,vo);
            for (CrowbActivitySign crowbActivitySign : list){
                vo.setOpenId(crowbActivitySign.getOpenid());
                vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
                String message = vo.getMessageJson();
                JSONObject jsonObject = wxMessageUtil.sendWeixinMessage(message,1);
                if(jsonObject!=null && jsonObject.containsKey("errcode") && "0".equals(jsonObject.getString("errcode"))){
                    successTotal++;
                }
            }
        }

        log.info("sendCrowSuccessMessage_successTotal==="+ successTotal);
        log.info("sendCrowSuccessMessage_fairTotl==="+(total-successTotal));
    }

    //众筹失败
    public void sendCrowFairMessage(CrowbActivity crowbActivity){
        List<CrowbActivitySign> list = crowbActivitySignService.list(new QueryWrapper<CrowbActivitySign>().eq("crowb_activity_id",crowbActivity.getId()).eq("verify_status","1"));//审核通过的
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>().eq("template_code","crowfair"));
        int total = list!=null?list.size():0;
        log.info("total==="+total);
        int successTotal = 0;

        crowbActivity.setSendStatus("2");
        crowbActivity.setModifyTime(new Date());
        crowbActivityService.updateById(crowbActivity);

        if(list!=null && list.size()>0 && messageTemplateList!=null && messageTemplateList.size()>0){
            MessageTemplate  messageTemplate = messageTemplateList.get(0);
//            BaseMessagaeVo vo =  new BaseMessagaeVo();
//            BeanUtils.copyProperties(messageTemplate,vo);
            for (CrowbActivitySign crowbActivitySign : list){
                //退还龙币
                JSONObject result = crowbActivitySignService.crowFair(crowbActivitySign);
                if(result!=null && "000000".equals(result.getString("returncode"))){
//                    vo.setOpenId(crowbActivitySign.getOpenid());
//                    vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
//                    String message = vo.getMessageJson();
//                    JSONObject jsonObject = wxMessageUtil.sendWeixinMessage(message,1);
//                    if(jsonObject!=null && jsonObject.containsKey("errcode") && "0".equals(jsonObject.getString("errcode"))){
//                        successTotal++;
//                    }
                    if("dev".equals(myPropertitys.getActivein()) || "local".equals(myPropertitys.getActivein())){
                        BaseMessagaeVo vo =  new BaseMessagaeVo();
                        BeanUtils.copyProperties(messageTemplate,vo);
                        vo.setOpenId(crowbActivitySign.getOpenid());
                        vo.setKeyword1(new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
                        vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
                        String message = vo.getMessageJson();
                        wxMessageUtil.sendWeixinMessage(message,1);
                    }else if("pro".equals(myPropertitys.getActivein())){
                        // 数据开始
                        JSONObject jsonObj = new JSONObject();
                        JSONObject data = new JSONObject();
                        /*0开头语*/
                        JSONObject first = new JSONObject();
                        first.put("color", "#173177");
                        first.put("value", messageTemplate.getFirst());
                        /*1获得积分时间*/
                        JSONObject time = new JSONObject();
                        time.put("value",new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(new Date()));
                        time.put("color", "#173177");
                        /*2、获得积分*/
                        JSONObject Point = new JSONObject();
                        Point.put("value",messageTemplate.getKeyword2());
                        Point.put("color", "#173177");
                        /*3获得原因*/
                        JSONObject type = new JSONObject();
                        type.put("value", messageTemplate.getKeyword3());
                        type.put("color", "#173177");

                        JSONObject From = null;
                        From = new JSONObject();
                        From.put("value", messageTemplate.getKeyword4());
                        From.put("color", "#173177");
                        /*5备注*/
                        JSONObject remark = new JSONObject();
                        String remarkStr = messageTemplate.getRemark();
                        remark.put("value", remarkStr);
                        remark.put("color", "#173177");

                        data.put("first", first); //开头语
                        data.put("time", time); // 时间
                        data.put("Point", Point);// 积分值
                        data.put("type", type);// 类型
                        data.put("From", From);//获得原因


                        data.put("remark", remark);// 备注
                        jsonObj.put("url",messageTemplate.getVisitUrl()!=null?messageTemplate.getVisitUrl():"javascript:void(0);");
                        jsonObj.put("touser", crowbActivitySign.getOpenid());
                        jsonObj.put("template_id",messageTemplate.getTemplateId());
                        jsonObj.put("data", data);
                        wxMessageUtil.sendWeixinMessage(jsonObj.toString(),1);

                    }
                }
            }
        }

        log.info("sendCrowFairMessage_successTotal==="+ successTotal);
        log.info("sendCrowFairMessage_fairTotl==="+(total-successTotal));
    }
}
