package com.icloud.modules.zltdd.controller;

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
import com.icloud.modules.zltdd.entity.ZltddRecommend;
import com.icloud.modules.zltdd.service.ZltddRecommendService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.sys.controller.AbstractController;


/**
 * 推广用户表
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 * 菜单主连接： modules/zltdd/zltddrecommend.html
 */
@RestController
@RequestMapping("zltdd/zltddrecommend")
public class ZltddRecommendController extends AbstractController{
    @Autowired
    private ZltddRecommendService zltddRecommendService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("zltdd:zltddrecommend:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = zltddRecommendService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("zltdd:zltddrecommend:info")
    public R info(@PathVariable("id") Long id){
        ZltddRecommend zltddRecommend = (ZltddRecommend)zltddRecommendService.getById(id);

        return R.ok().put("zltddRecommend", zltddRecommend);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("zltdd:zltddrecommend:save")
    public R save(@RequestBody ZltddRecommend zltddRecommend){
        zltddRecommendService.save(zltddRecommend);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("zltdd:zltddrecommend:update")
    public R update(@RequestBody ZltddRecommend zltddRecommend){
        ValidatorUtils.validateEntity(zltddRecommend);
        zltddRecommendService.updateById(zltddRecommend);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("zltdd:zltddrecommend:delete")
    public R delete(@RequestBody Long[] ids){
        zltddRecommendService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
