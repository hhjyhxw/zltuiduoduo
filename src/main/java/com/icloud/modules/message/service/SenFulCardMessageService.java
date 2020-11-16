package com.icloud.modules.message.service;

import com.alibaba.fastjson.JSONObject;
import com.icloud.modules.message.entity.MessageTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenFulCardMessageService implements BaseMessageSendService{

    @Override
    public JSONObject sendMessage(MessageTemplate messageTemplate) {
        return null;
    }
}
