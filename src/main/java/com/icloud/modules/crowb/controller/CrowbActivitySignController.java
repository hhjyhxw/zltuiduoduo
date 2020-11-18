package com.icloud.modules.crowb.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.modules.crowb.service.CrowbSendMessageService;
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
    @Autowired
    private CrowbSendMessageService crowbSendMessageService;

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
    @SysLog("保存报名记录")
    @RequestMapping("/save")
    @RequiresPermissions("crowb:crowbactivitysign:save")
    public R save(@RequestBody CrowbActivitySign crowbActivitySign){
        crowbActivitySignService.save(crowbActivitySign);
        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("更新报名记录")
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
    @SysLog("删除报名记录")
    @RequestMapping("/delete")
    @RequiresPermissions("crowb:crowbactivitysign:delete")
    public R delete(@RequestBody Long[] ids){
        crowbActivitySignService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 报名审核通过或者失败
     */
    @SysLog("报名审核通过")
    @RequestMapping("/signPass")
    @RequiresPermissions("crowb:crowbactivitysign:update")
    public R signPass(@RequestBody CrowbActivitySign crowbActivitySign){
        if(crowbActivitySign.getId()!=null){
            CrowbActivitySign passSign = new CrowbActivitySign();
            passSign.setId(crowbActivitySign.getId());
            passSign.setModifyTime(new Date());
            passSign.setVerifyStatus("1");
            crowbActivitySignService.passSign(passSign);
            //发送通知消息
            crowbSendMessageService.sendVerifySuccessMessage(passSign);
            return R.ok();
        }
        return R.error("id不能为空");
    }

    /**
     * 报名审核通过或者失败
     */
    @SysLog("报名审核失败")
    @RequestMapping("/signFair")
    @RequiresPermissions("crowb:crowbactivitysign:update")
    public R signFair(@RequestBody CrowbActivitySign crowbActivitySign){
        if(crowbActivitySign.getId()!=null){
            CrowbActivitySign passSign = new CrowbActivitySign();
            passSign.setId(crowbActivitySign.getId());
            passSign.setModifyTime(new Date());
            passSign.setVerifyStatus("3");
            crowbActivitySignService.updateById(passSign);
            //发送通知消息
            crowbSendMessageService.sendVerifyFailMessage(passSign);
            return R.ok();
        }
        return R.error("id不能为空");
    }


    /**
     * 后台取消报名
     */
    @SysLog("后台取消报名")
    @RequestMapping("/cancelSign")
    @RequiresPermissions("crowb:crowbactivitysign:update")
    public R cancelSign(@RequestBody CrowbActivitySign crowbActivitySign){
        if(crowbActivitySign.getId()!=null){
            CrowbActivitySign passSign = new CrowbActivitySign();
            passSign.setId(crowbActivitySign.getId());
            passSign.setModifyTime(new Date());
            passSign.setVerifyStatus("2");
            //退龙币
            crowbActivitySignService.cancelSign(passSign);
            //发送通知消息
            crowbSendMessageService.sendCancelSignMessage(passSign);
            return R.ok();
        }
        return R.error("id不能为空");
    }
}
