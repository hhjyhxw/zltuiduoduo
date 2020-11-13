package com.icloud.modules.message.controller;

import java.util.Arrays;
import java.util.Map;

import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.message.entity.MessageSendrecord;
import com.icloud.modules.message.service.MessageSendrecordService;
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
 * 菜单主连接： modules/message/messagesendrecord.html
 */
@RestController
@RequestMapping("message/messagesendrecord")
public class MessageSendrecordController extends AbstractController{
    @Autowired
    private MessageSendrecordService messageSendrecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("message:messagesendrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = messageSendrecordService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("message:messagesendrecord:info")
    public R info(@PathVariable("id") Long id){
        MessageSendrecord messageSendrecord = (MessageSendrecord)messageSendrecordService.getById(id);

        return R.ok().put("messageSendrecord", messageSendrecord);
    }

    /**
     * 保存
     */
    @SysLog("保存待发送记录")
    @RequestMapping("/save")
    @RequiresPermissions("message:messagesendrecord:save")
    public R save(@RequestBody MessageSendrecord messageSendrecord){
        messageSendrecordService.save(messageSendrecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改待发送记录")
    @RequestMapping("/update")
    @RequiresPermissions("message:messagesendrecord:update")
    public R update(@RequestBody MessageSendrecord messageSendrecord){
        ValidatorUtils.validateEntity(messageSendrecord);
        messageSendrecordService.updateById(messageSendrecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除待发送记录")
    @RequestMapping("/delete")
    @RequiresPermissions("message:messagesendrecord:delete")
    public R delete(@RequestBody Long[] ids){
        messageSendrecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
