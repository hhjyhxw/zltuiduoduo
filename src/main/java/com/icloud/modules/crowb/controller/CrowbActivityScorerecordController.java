package com.icloud.modules.crowb.controller;

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
import com.icloud.modules.crowb.entity.CrowbActivityScorerecord;
import com.icloud.modules.crowb.service.CrowbActivityScorerecordService;
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
 * 菜单主连接： modules/crowb/crowbactivityscorerecord.html
 */
@RestController
@RequestMapping("crowb/crowbactivityscorerecord")
public class CrowbActivityScorerecordController extends AbstractController{
    @Autowired
    private CrowbActivityScorerecordService crowbActivityScorerecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("crowb:crowbactivityscorerecord:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = crowbActivityScorerecordService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("crowb:crowbactivityscorerecord:info")
    public R info(@PathVariable("id") Long id){
        CrowbActivityScorerecord crowbActivityScorerecord = (CrowbActivityScorerecord)crowbActivityScorerecordService.getById(id);

        return R.ok().put("crowbActivityScorerecord", crowbActivityScorerecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("crowb:crowbactivityscorerecord:save")
    public R save(@RequestBody CrowbActivityScorerecord crowbActivityScorerecord){
        crowbActivityScorerecordService.save(crowbActivityScorerecord);

        return R.ok();
    }

    /**
     * 修改
     */

    @RequestMapping("/update")
    @RequiresPermissions("crowb:crowbactivityscorerecord:update")
    public R update(@RequestBody CrowbActivityScorerecord crowbActivityScorerecord){
        ValidatorUtils.validateEntity(crowbActivityScorerecord);
        crowbActivityScorerecordService.updateById(crowbActivityScorerecord);
        
        return R.ok();
    }

    /**
     * 删除
     */

    @RequestMapping("/delete")
    @RequiresPermissions("crowb:crowbactivityscorerecord:delete")
    public R delete(@RequestBody Long[] ids){
        crowbActivityScorerecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
