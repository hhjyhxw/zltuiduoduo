package com.icloud.modules.zltdd.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import com.icloud.basecommon.model.Query;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.zltdd.entity.ZltddAwards;
import com.icloud.modules.zltdd.service.ZltddAwardsService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.sys.controller.AbstractController;


/**
 * 中奖记录表
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:32
 * 菜单主连接： modules/zltdd/zltddawards.html
 */
@RestController
@RequestMapping("zltdd/zltddawards")
public class ZltddAwardsController extends AbstractController{
    @Autowired
    private ZltddAwardsService zltddAwardsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("zltdd:zltddawards:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = zltddAwardsService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("zltdd:zltddawards:info")
    public R info(@PathVariable("id") Long id){
        ZltddAwards zltddAwards = (ZltddAwards)zltddAwardsService.getById(id);

        return R.ok().put("zltddAwards", zltddAwards);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("zltdd:zltddawards:save")
    public R save(@RequestBody ZltddAwards zltddAwards){

        zltddAwardsService.save(zltddAwards);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("zltdd:zltddawards:update")
    public R update(@RequestBody ZltddAwards zltddAwards){
        ValidatorUtils.validateEntity(zltddAwards);
        zltddAwardsService.updateById(zltddAwards);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("zltdd:zltddawards:delete")
    public R delete(@RequestBody Long[] ids){
        zltddAwardsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
