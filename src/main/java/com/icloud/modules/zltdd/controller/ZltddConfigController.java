package com.icloud.modules.zltdd.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.modules.zltdd.entity.ZltddPrize;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.zltdd.entity.ZltddConfig;
import com.icloud.modules.zltdd.service.ZltddConfigService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.sys.controller.AbstractController;


/**
 * 活动配置表
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 * 菜单主连接： modules/zltdd/zltddconfig.html
 */
@RestController
@RequestMapping("zltdd/zltddconfig")
public class ZltddConfigController extends AbstractController{
    @Autowired
    private ZltddConfigService zltddConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("zltdd:zltddconfig:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = zltddConfigService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }

    @RequestMapping("/configlist")
    public R list(){
        List<ZltddConfig> list = zltddConfigService.list(new QueryWrapper<ZltddConfig>().eq("status","1"));
        return R.ok().put("list", list);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("zltdd:zltddconfig:info")
    public R info(@PathVariable("id") Long id){
        ZltddConfig zltddConfig = (ZltddConfig)zltddConfigService.getById(id);

        return R.ok().put("zltddConfig", zltddConfig);
    }

    /**
     * 保存
     */
    @SysLog("添加活动")
    @RequestMapping("/save")
    @RequiresPermissions("zltdd:zltddconfig:save")
    public R save(@RequestBody ZltddConfig zltddConfig){
        ValidatorUtils.validateEntity(zltddConfig);
        zltddConfig.setCreateTime(new Date());
        zltddConfig.setCreateMan(getUser().getUsername());
        zltddConfigService.save(zltddConfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改活动")
    @RequestMapping("/update")
    @RequiresPermissions("zltdd:zltddconfig:update")
    public R update(@RequestBody ZltddConfig zltddConfig){
        ValidatorUtils.validateEntity(zltddConfig);
        zltddConfig.setModifyTime(new Date());
        zltddConfig.setMofityMan(getUser().getUsername());
        zltddConfigService.updateById(zltddConfig);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除活动")
    @RequestMapping("/delete")
    @RequiresPermissions("zltdd:zltddconfig:delete")
    public R delete(@RequestBody Long[] ids){
        zltddConfigService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
