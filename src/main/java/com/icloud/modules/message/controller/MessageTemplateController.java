package com.icloud.modules.message.controller;

import java.util.Arrays;
import java.util.Map;
import com.icloud.basecommon.model.Query;
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
    @Autowired
    private MessageTemplateService messageTemplateService;

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
    @RequestMapping("/save")
    @RequiresPermissions("message:messagetemplate:save")
    public R save(@RequestBody MessageTemplate messageTemplate){
        messageTemplateService.save(messageTemplate);

        return R.ok();
    }

    /**
     * 修改
     */
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
    @RequestMapping("/delete")
    @RequiresPermissions("message:messagetemplate:delete")
    public R delete(@RequestBody Long[] ids){
        messageTemplateService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
