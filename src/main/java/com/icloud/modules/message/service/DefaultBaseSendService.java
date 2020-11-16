package com.icloud.modules.message.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.common.R;
import com.icloud.common.util.StringUtil;
import com.icloud.modules.message.entity.MessageSendrecord;
import com.icloud.modules.message.entity.MessageTemplate;
import com.icloud.modules.message.util.WxMessageUtil;
import com.icloud.modules.message.vo.BaseMessagaeVo;
import com.icloud.modules.wx.entity.WxUser;
import com.icloud.modules.wx.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DefaultBaseSendService {

    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private MessageSendrecordService messageSendrecordService;
    @Autowired
    private WxMessageUtil wxMessageUtil;
    @Autowired
    private WxUserService wxUserService;

    public JSONObject sendMessage(MessageTemplate messageTemplate) {
        JSONObject json = new JSONObject();
        json.put("code", "0000");
        json.put("message", "发送完成");
        messageTemplate = (MessageTemplate) messageTemplateService.getById(messageTemplate.getId());
        List<MessageSendrecord> list = messageSendrecordService.list(new QueryWrapper<MessageSendrecord>()
                .eq("message_id",messageTemplate.getId())
                .eq("status","0"));
        int total = list!=null?list.size():0;
        log.info("total==="+total);
        json.put("total",total);
        int successTotal = 0;
        BaseMessagaeVo vo =  new BaseMessagaeVo();
        BeanUtils.copyProperties(messageTemplate,vo);
        if(list!=null || list.size()>0){
            for (MessageSendrecord record:list){
                if(StringUtil.checkStr(record.getOpenid())){
                    WxUser user = wxUserService.findByOpenId(record.getOpenid());
                    vo.setOpenId(record.getOpenid());
                    vo.setKeyword1(user!=null?user.getNickname():"xxx");
                    vo.setKeyword2("卡号:"+record.getCardId()+";"+"卡密:"+record.getCardCode());
                    String message = vo.getMessageJson();
                    JSONObject jsonObject = wxMessageUtil.sendWeixinMessage(message,1);
                    if(jsonObject!=null && jsonObject.containsKey("errcode") && "0".equals(jsonObject.getString("errcode"))){
                        record.setStatus("1");
                        record.setModifyTime(new Date());
                        messageSendrecordService.updateById(record);
                        successTotal++;
                    }else {
                        record.setStatus("2");
                        record.setModifyTime(new Date());
                        record.setMsg(jsonObject!=null?jsonObject.getString("errmsg"):"null");
                        messageSendrecordService.updateById(record);
                    }
                }
            }
        }
        log.info("successTotal==="+ successTotal);
        json.put("successTotal",successTotal);
        json.put("fairTotl",total-successTotal);
        return json;
    }
}
