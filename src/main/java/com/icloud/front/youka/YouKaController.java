package com.icloud.front.youka;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.modules.message.entity.MessageSendrecord;
import com.icloud.modules.message.service.MessageSendrecordService;
import com.icloud.modules.wx.entity.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/frontpage/youka")
public class YouKaController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MessageSendrecordService messageSendrecordService;

    @RequestMapping("/details")
    public String details(HttpServletResponse response,String messageId){
        try {
            WxUser user = (WxUser)request.getSession().getAttribute("wx_user");
            List<MessageSendrecord> list = messageSendrecordService.list(new QueryWrapper<MessageSendrecord>()
                    .eq("message_id",messageId)
                    .eq("openid",user.getOpenid()));
            if(list!=null && list.size()>0){
                request.setAttribute("messageSendrecord",list.get(0));
                return "modules/front/youka/youkadetails";
            }else{
                request.setAttribute("message","没又数据记录");
                return "modules/front/error";
            }
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("message",e.getMessage());
            return "modules/front/error";
        }
    }
}
