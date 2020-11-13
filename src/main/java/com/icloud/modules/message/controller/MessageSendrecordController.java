package com.icloud.modules.message.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.basecommon.util.codec.Md5Utils;
import com.icloud.basecommon.util.excelutilss.ExcelMoreSheetPoiUtil;
import com.icloud.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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


    /**
     * 导入待发送用户数据
     * 联合主键
     * 1、messageid
     * 2、openid
     */
    @RequestMapping("/importusers")
    @RequiresPermissions("message:messagesendrecord:update")
    public R importusers(@RequestParam String url){//url本地文件
        try {
            if(!StringUtil.checkStr(url)){
                return R.error("文件不能为空");
            }
            if(url.indexOf("xls")<0){
                return R.error("不是合法的文件");
            }
//            String realPath = request.getSession().getServletContext().getRealPath(url);
            log.error(url);
            File dirFile = new File(url);
            log.info("url=============="+url);
            log.info("filename=============="+dirFile.getName());
            List<List<Object>> dataList = ExcelMoreSheetPoiUtil.readExcel(dirFile, 0);
            log.info("excel大小:dataList=============="+ dataList.size());
            MessageSendrecord retail = null;
            Date date = new Date();
            List<MessageSendrecord> sendlist = new ArrayList<>();
            for(int i=0;i<dataList.size();i++){
                List<Object> comlunsList = dataList.get(i);
                if(comlunsList.get(0)==null || comlunsList.get(1)==null){
                    continue;
                }
                String messageId = comlunsList.get(0).toString();
                String openid = comlunsList.get(1).toString();
                //1、消息模板id  2、openid
                int count = messageSendrecordService.count(new QueryWrapper<MessageSendrecord>().eq("message_id",messageId).eq("openid",openid));
                if(count>0){
                    log.info("openid====="+openid+" 用户已存在");
                    continue;
                }
                MessageSendrecord record= new MessageSendrecord();
                record.setCreateTime(date);
                record.setMessageId(Long.valueOf(messageId));
                record.setOpenid(openid);
                record.setStatus("0");
                sendlist.add(record);
            }
            log.info("实际大小=============="+ sendlist.size());
            messageSendrecordService.saveBatch(sendlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok();
    }
}
