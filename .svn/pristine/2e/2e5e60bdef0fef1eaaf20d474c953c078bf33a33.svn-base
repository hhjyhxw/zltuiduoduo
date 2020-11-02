package com.icloud.modules.sys.controller;

import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.config.interceptor.ThirdInterfaceInterceptor;
import com.icloud.modules.sys.entity.SysWhiteip;
import com.icloud.modules.sys.service.SysWhiteipService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-26 09:33:26
 * 菜单主连接： modules/sys/syswhiteip.html
 */
@RestController
@RequestMapping("sys/syswhiteip")
public class SysWhiteipController extends AbstractController {
    @Autowired
    private SysWhiteipService sysWhiteipService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:syswhiteip:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = sysWhiteipService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:syswhiteip:info")
    public R info(@PathVariable("id") Integer id){
        SysWhiteip sysWhiteip = (SysWhiteip)sysWhiteipService.getById(id);

        return R.ok().put("sysWhiteip", sysWhiteip);
    }

    /**
     * 保存
     */
    @SysLog("添加ip白名单")
    @RequestMapping("/save")
    @RequiresPermissions("sys:syswhiteip:save")
    public R save(@RequestBody SysWhiteip sysWhiteip){
        ValidatorUtils.validateEntity(sysWhiteip);
        sysWhiteip.setCreateTime(new Date());
        sysWhiteipService.save(sysWhiteip);
        //更新缓存
        ThirdInterfaceInterceptor.loaLORreLoadIpList();
        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改白名单ip")
    @RequestMapping("/update")
    @RequiresPermissions("sys:syswhiteip:update")
    public R update(@RequestBody SysWhiteip sysWhiteip){
        ValidatorUtils.validateEntity(sysWhiteip);
        sysWhiteip.setModifyTime(new Date());
        sysWhiteipService.updateById(sysWhiteip);
        //更新缓存
        ThirdInterfaceInterceptor.loaLORreLoadIpList();
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除白名单ip")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:syswhiteip:delete")
    public R delete(@RequestBody Integer[] ids){
        sysWhiteipService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
