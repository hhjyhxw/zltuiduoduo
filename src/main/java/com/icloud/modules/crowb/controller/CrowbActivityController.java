package com.icloud.modules.crowb.controller;

import java.util.Arrays;
import java.util.Map;

import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.common.SnowflakeUtils;
import com.icloud.config.ServerConfig;
import com.icloud.modules.crowb.service.CrowbSendMessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.crowb.entity.CrowbActivity;
import com.icloud.modules.crowb.service.CrowbActivityService;
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
 * @date 2020-11-11 16:06:20
 * 菜单主连接： modules/crowb/crowbactivity.html
 */
@RestController
@RequestMapping("crowb/crowbactivity")
public class CrowbActivityController extends AbstractController{
    @Autowired
    private CrowbActivityService crowbActivityService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private CrowbSendMessageService crowbSendMessageService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("crowb:crowbactivity:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = crowbActivityService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("crowb:crowbactivity:info")
    public R info(@PathVariable("id") Long id){
        CrowbActivity crowbActivity = (CrowbActivity)crowbActivityService.getById(id);

        return R.ok().put("crowbActivity", crowbActivity);
    }

    /**
     * 保存
     */
    @SysLog("保存众筹活动")
    @RequestMapping("/save")
    @RequiresPermissions("crowb:crowbactivity:save")
    public R save(@RequestBody CrowbActivity crowbActivity){
        crowbActivity.setActivityNo("A"+SnowflakeUtils.getOrderNoByWordId(serverConfig.getServerPort()%31L));
        crowbActivityService.save(crowbActivity);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("编辑众筹活动")
    @RequestMapping("/update")
    @RequiresPermissions("crowb:crowbactivity:update")
    public R update(@RequestBody CrowbActivity crowbActivity){
        ValidatorUtils.validateEntity(crowbActivity);
//        crowbActivity.setActivityNo("A"+SnowflakeUtils.getOrderNoByWordId(serverConfig.getServerPort()%31L));
        crowbActivityService.updateById(crowbActivity);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除众筹活动")
    @RequestMapping("/delete")
    @RequiresPermissions("crowb:crowbactivity:delete")
    public R delete(@RequestBody Long[] ids){
        crowbActivityService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     */
    @SysLog("众筹活动成功")
    @RequestMapping("/successActivity")
    @RequiresPermissions("crowb:crowbactivity:update")
    public R successActivity(@RequestBody CrowbActivity crowbActivity){
        CrowbActivity odlcrowbActivity = (CrowbActivity) crowbActivityService.getById(crowbActivity.getId());
        //活动已结束
        if(!"0".equals(odlcrowbActivity.getSendStatus())){
            return R.error("活动已结束");
        }
        //众筹成功发送模板消息
        crowbSendMessageService.sendCrowSuccessMessage(crowbActivity);


        //verifysuccess

        //verifyfair

        //crowsuccess

        //crowfair

        //signsuccess

        //cancelsign
        return R.ok();
    }

    /**
     */
    @SysLog("众筹活动失败")
    @RequestMapping("/faireActivity")
    @RequiresPermissions("crowb:crowbactivity:update")
    public R faireActivity(@RequestBody CrowbActivity crowbActivity){
        CrowbActivity odlcrowbActivity = (CrowbActivity) crowbActivityService.getById(crowbActivity.getId());
        //活动已结束
        if(!"0".equals(crowbActivity.getSendStatus())){
            return R.error("活动已结束");
        }
        //众筹退龙币 并发送模板消息
        crowbSendMessageService.sendCrowFairMessage(crowbActivity);

        return R.ok();
    }



}
