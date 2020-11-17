package com.icloud.modules.crowb.service;


import com.icloud.modules.message.service.MessageTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CrowbSendMessageService {

    @Autowired
    private MessageTemplateService messageTemplateService;

    //报名成功
   public void sendSignSuccessMessage(){

    }

    //审核成功
    public void sendVerifySuccessMessage(){

    }

    //审核失败
    public void sendVerifyFailMessage(){

    }

    //取消报名
    public void sendCancelSignMessage(){

    }

    //众筹成功
    public void sendCrowSuccessMessage(){

    }
    //众筹失败
    public void sendCrowFairMessage(){

    }
}
