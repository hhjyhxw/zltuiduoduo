package com.icloud.modules.message.service;

import com.alibaba.fastjson.JSONObject;
import com.icloud.modules.message.entity.MessageTemplate;

public interface BaseMessageSendService {

    JSONObject sendMessage(MessageTemplate messageTemplate);
}
