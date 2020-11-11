package com.icloud.modules.crowb.controller;

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
import com.icloud.modules.crowb.entity.CrowbActivitySign;
import com.icloud.modules.crowb.service.CrowbActivitySignService;
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
 * 菜单主连接： modules/crowb/crowbactivitysign.html
 */
@RestController
@RequestMapping("crowb/crowbactivitysign")
public class CrowbActivitySignController extends AbstractController{
    @Autowired
    private CrowbActivitySignService crowbActivitySignService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("crowb:crowbactivitysign:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = crowbActivitySignService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("crowb:crowbactivitysign:info")
    public R info(@PathVariable("id") Long id){
        CrowbActivitySign crowbActivitySign = (CrowbActivitySign)crowbActivitySignService.getById(id);

        return R.ok().put("crowbActivitySign", crowbActivitySign);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("crowb:crowbactivitysign:save")
    public R save(@RequestBody CrowbActivitySign crowbActivitySign){
        crowbActivitySignService.save(crowbActivitySign);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("crowb:crowbactivitysign:update")
    public R update(@RequestBody CrowbActivitySign crowbActivitySign){
        ValidatorUtils.validateEntity(crowbActivitySign);
        crowbActivitySignService.updateById(crowbActivitySign);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("crowb:crowbactivitysign:delete")
    public R delete(@RequestBody Long[] ids){
        crowbActivitySignService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
