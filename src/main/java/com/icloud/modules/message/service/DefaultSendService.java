package com.icloud.modules.message.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.modules.message.entity.MessageSendrecord;
import com.icloud.modules.message.entity.MessageTemplate;
import com.icloud.modules.message.util.WxMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultSendService implements BaseMessageSendService {

    @Autowired
    private DefaultBaseSendService defaultBaseSendService;

    @Override
    public JSONObject sendMessage(MessageTemplate messageTemplate) {
        return defaultBaseSendService.sendMessage(messageTemplate);
    }
}
