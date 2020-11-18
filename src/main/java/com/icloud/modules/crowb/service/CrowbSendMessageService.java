package com.icloud.modules.crowb.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CrowbSendMessageService {

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
        BaseMessagaeVo vo =  new BaseMessagaeVo();
        BeanUtils.copyProperties(messageTemplate,vo);
        vo.setOpenId(passSign.getOpenid());
        vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
        String message = vo.getMessageJson();
        wxMessageUtil.sendWeixinMessage(message,1);
    }

    //审核失败
    public void sendVerifyFailMessage(CrowbActivitySign crowbActivitySign){
        crowbActivitySign = (CrowbActivitySign) crowbActivitySignService.getById(crowbActivitySign.getId());
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>()
                .eq("template_code","verifyfair"));
        MessageTemplate  messageTemplate = messageTemplateList.get(0);
        BaseMessagaeVo vo =  new BaseMessagaeVo();
        BeanUtils.copyProperties(messageTemplate,vo);
        vo.setOpenId(crowbActivitySign.getOpenid());
        vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
        String message = vo.getMessageJson();
        wxMessageUtil.sendWeixinMessage(message,1);
    }

    //取消报名
    public void sendCancelSignMessage(CrowbActivitySign crowbActivitySign){
        List<MessageTemplate> messageTemplateList = messageTemplateService.list(new QueryWrapper<MessageTemplate>()
                .eq("template_code","cancelsign"));
        MessageTemplate  messageTemplate = messageTemplateList.get(0);
        BaseMessagaeVo vo =  new BaseMessagaeVo();
        BeanUtils.copyProperties(messageTemplate,vo);
        vo.setOpenId(crowbActivitySign.getOpenid());
        vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
        String message = vo.getMessageJson();
        wxMessageUtil.sendWeixinMessage(message,1);
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
            BaseMessagaeVo vo =  new BaseMessagaeVo();
            BeanUtils.copyProperties(messageTemplate,vo);
            for (CrowbActivitySign crowbActivitySign : list){
                //退还龙币
                JSONObject result = crowbActivitySignService.crowFair(crowbActivitySign);
                if(result!=null && "000000".equals(result.getString("returncode"))){
                    vo.setOpenId(crowbActivitySign.getOpenid());
                    vo.setVisitUrl(vo.getVisitUrl()!=null?vo.getVisitUrl():"javascript:void(0);");
                    String message = vo.getMessageJson();
                    JSONObject jsonObject = wxMessageUtil.sendWeixinMessage(message,1);
                    if(jsonObject!=null && jsonObject.containsKey("errcode") && "0".equals(jsonObject.getString("errcode"))){
                        successTotal++;
                    }
                }
            }
        }

        log.info("sendCrowFairMessage_successTotal==="+ successTotal);
        log.info("sendCrowFairMessage_fairTotl==="+(total-successTotal));
    }
}
