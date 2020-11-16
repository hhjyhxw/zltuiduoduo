package com.icloud.front.youka;

import com.icloud.modules.message.service.MessageSendrecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/frontpage/youka")
public class YouKaController {

    @Autowired
    private MessageSendrecordService messageSendrecordService;

    @RequestMapping("/details")
    public String details(String redirect_url, HttpServletResponse response){
//        messageSendrecordService.list()
        return null;
    }
}
