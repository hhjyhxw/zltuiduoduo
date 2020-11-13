package com.icloud.modules.message.controller;

import java.util.Arrays;
import java.util.Map;

import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.basecommon.service.LockComponent;
import com.icloud.common.SpringContextHolder;
import com.icloud.common.util.StringUtil;
import com.icloud.modules.message.service.BaseMessageSendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.message.entity.MessageTemplate;
import com.icloud.modules.message.service.MessageTemplateService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.sys.controller.AbstractController;


/**
 * 
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:38
 * 菜单主连接： modules/message/messagetemplate.html
 */
@RestController
@RequestMapping("message/messagetemplate")
public class MessageTemplateController extends AbstractController{


    private static final String CONSOLE_SENDMESSAGE_LOCK = "CONSOLE_SENDMESSAGE_LOCK_";
    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private LockComponent lockComponent;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("message:messagetemplate:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = messageTemplateService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("message:messagetemplate:info")
    public R info(@PathVariable("id") Long id){
        MessageTemplate messageTemplate = (MessageTemplate)messageTemplateService.getById(id);

        return R.ok().put("messageTemplate", messageTemplate);
    }

    /**
     * 保存
     */
    @SysLog("保存消息模板记录")
    @RequestMapping("/save")
    @RequiresPermissions("message:messagetemplate:save")
    public R save(@RequestBody MessageTemplate messageTemplate){
        messageTemplateService.save(messageTemplate);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("编辑消息模板")
    @RequestMapping("/update")
    @RequiresPermissions("message:messagetemplate:update")
    public R update(@RequestBody MessageTemplate messageTemplate){
        ValidatorUtils.validateEntity(messageTemplate);
        messageTemplateService.updateById(messageTemplate);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除消息模板")
    @RequestMapping("/delete")
    @RequiresPermissions("message:messagetemplate:delete")
    public R delete(@RequestBody Long[] ids){
        messageTemplateService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("消息发送")
    @RequestMapping("/sendMessage")
    @RequiresPermissions("message:messagetemplate:sendMessage")
    public R sendMessage(@RequestBody MessageTemplate messageTemplate){
        if(messageTemplate.getId()==null){
            return R.error("消息模板不能为空");
        }
        //锁定10分钟，或者等待任务完成
        try {
            if (lockComponent.tryLock(CONSOLE_SENDMESSAGE_LOCK + messageTemplate.getId().toString(), 600)) {
                //有消息处理器，则使用消息处理器，没有 则使用默认的消息处理器
                BaseMessageSendService sendService = null;
                if (StringUtil.checkStr(messageTemplate.getDealZclass())) {
                    sendService = SpringContextHolder.getBean(messageTemplate.getDealZclass());
                } else {
                    sendService = SpringContextHolder.getBean("defaultSendService");
                }
            } else {

            }
        }finally {
            lockComponent.release(CONSOLE_SENDMESSAGE_LOCK + messageTemplate.getId().toString());
        }
        return R.ok();
    }


}
