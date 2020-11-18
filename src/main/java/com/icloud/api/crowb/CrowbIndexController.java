package com.icloud.api.crowb;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.LoginUser;
import com.icloud.api.crowb.vo.SignVo;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.crowb.entity.CrowbActivity;
import com.icloud.modules.crowb.entity.CrowbActivitySign;
import com.icloud.modules.crowb.service.CrowbActivityService;
import com.icloud.modules.crowb.service.CrowbActivitySignService;
import com.icloud.modules.wx.entity.WxUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@Api("报名活动首页相关接口")
@RestController
@RequestMapping("/api/crowbindex")
public class CrowbIndexController {

    @Autowired
    private CrowbActivityService crowbActivityService;
    @Autowired
    private CrowbActivitySignService crowbActivitySignService;

    /**
     * 1、判断活动是否存在
     * 2、判断活动是否在在进行中
     * 3、判断用户报名状态
     * @param activityNo
     * @return
     */
    @ApiOperation(value="活动首页数据", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityNo", value = "活动编号", required = false, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/index",method = {RequestMethod.GET})
    public R index(@RequestParam String activityNo,@LoginUser WxUser user){
        List<CrowbActivity> list = crowbActivityService.list(new QueryWrapper<CrowbActivity>().eq("activity_no",activityNo));
        if(list==null || list.size()==0){
            return R.error("活动不存在");
        }
        CrowbActivity activityInfo = list.get(0);
        long nowtime = new Date().getTime();
        if( nowtime< activityInfo.getStarttime().getTime()){
            activityInfo.setActivityStatus("0");//活动未开始
        }
        if( nowtime>activityInfo.getStarttime().getTime()){
            //众筹成功1
            if("1".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("2");//成功结束
                //众筹失败2
            }else if("2".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("3");//失败结束
            }else{
                activityInfo.setActivityStatus("4");//活动结束 状态未更新
            }
        }
        if(nowtime>=activityInfo.getStarttime().getTime() && nowtime<=activityInfo.getStarttime().getTime()){
            //众筹成功1
            if("1".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("2");//成功结束
            //众筹失败2
            }else if("2".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("3");//失败结束
            }else{
                activityInfo.setActivityStatus("1");//活动进行中
            }
        }

        List<CrowbActivitySign> signlist = crowbActivitySignService.list(new QueryWrapper<CrowbActivitySign>()
                .eq("crowb_activity_id",activityInfo.getId())
                .eq("openid",user.getOpenid()));
        CrowbActivitySign activitySign = null;
        if(signlist!=null && signlist.size()>0){
            activitySign = signlist.get(0);
            if(activitySign.getVerifyStatus()==null || "0".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("0");//已经报名,待审核
            }else if("1".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("1");//已经报名，并且审核通过
            }else if("2".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("2");//已经报名，且已取消
            }else if("3".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("3");//已经报名，审核失败
            }
        }else{
            activityInfo.setSignState("-1");//未报名
        }
        return R.ok().put("activityInfo",activityInfo);//活动信息

    }
    /**
     * 报名
     * 1、拍断活动是否存在
     * 2、判断活动是否进行中
     * 3、判断是否报名
     * @return
     */
    @ApiOperation(value="活动报名", notes="")
    @RequestMapping(value = "/sign",method = {RequestMethod.POST})
    public R sign(@RequestBody SignVo signVo, @LoginUser WxUser user) {
        ValidatorUtils.validateEntityForFront(signVo);
        List<CrowbActivity> list = crowbActivityService.list(new QueryWrapper<CrowbActivity>().eq("activity_no",signVo.getActivityNo()));
        if(list==null || list.size()==0){
            return R.error("活动不存在");
        }
        CrowbActivity activityInfo = list.get(0);
        long nowtime = new Date().getTime();
        if( nowtime< activityInfo.getStarttime().getTime()){
            return R.error("活动未开始，不能报名");
        }
        if( nowtime>activityInfo.getEndtime().getTime()){
            return R.error("活动已经结束，不能报名");
        }
        //众筹成功1
        if("1".equals(activityInfo.getSendStatus())){
            return R.error("报名已完成，通道关闭");
            //众筹失败2
        }else if("2".equals(activityInfo.getSendStatus())){
            return R.error("报名已结束，通道关闭");
        }
        //活动总人数
        int readyNum = activityInfo.getSigned()!=null?activityInfo.getSigned():0;
        if(activityInfo.getTotal()<=readyNum){
            return R.error("活动人数已满，不能再次报名");
        }
        List<CrowbActivitySign> signlist = crowbActivitySignService.list(new QueryWrapper<CrowbActivitySign>()
                .eq("crowb_activity_id",activityInfo.getId())
                .eq("openid",user.getOpenid()));
        CrowbActivitySign activitySign = null;
        if(signlist!=null && signlist.size()>0 && !"2".equals(signlist.get(0).getVerifyStatus())){//取消状态可以再次报名
            return R.error("已报名，不能再次报名");
        }

        //创建报名记录,更新报名人数
        CrowbActivitySign crowbActivitySign = new CrowbActivitySign();
        BeanUtils.copyProperties(signVo,crowbActivitySign);
        crowbActivitySign.setCrowbActivityId(activityInfo.getId());
        crowbActivitySign.setActivityName(activityInfo.getTitle());
        crowbActivitySign.setOpenid(user.getOpenid());
        crowbActivitySign.setScore(activityInfo.getScore());
        crowbActivitySignService.addCrowbActivitySign(crowbActivitySign,activityInfo);
        return R.ok().put("activityInfo",activityInfo);//活动信息

    }
    //报名查询
    @ApiOperation(value="报名查询", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityNo", value = "活动编号", required = false, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/querySign",method = {RequestMethod.GET})
    public R querySign(@RequestParam String activityNo,@LoginUser WxUser user){
        List<CrowbActivity> list = crowbActivityService.list(new QueryWrapper<CrowbActivity>().eq("activity_no",activityNo));
        if(list==null || list.size()==0){
            return R.error("活动不存在");
        }
        CrowbActivity activityInfo = list.get(0);
        long nowtime = new Date().getTime();
        if( nowtime< activityInfo.getStarttime().getTime()){
            activityInfo.setActivityStatus("0");//活动未开始
        }
        if( nowtime>activityInfo.getStarttime().getTime()){
            //众筹成功1
            if("1".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("2");//成功结束
                //众筹失败2
            }else if("2".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("3");//失败结束
            }else{
                activityInfo.setActivityStatus("4");//活动结束 状态未更新
            }
        }
        if(nowtime>=activityInfo.getStarttime().getTime() && nowtime<=activityInfo.getStarttime().getTime()){
            //众筹成功1
            if("1".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("2");//成功结束
                //众筹失败2
            }else if("2".equals(activityInfo.getSendStatus())){
                activityInfo.setActivityStatus("3");//失败结束
            }else{
                activityInfo.setActivityStatus("1");//活动进行中
            }
        }

        List<CrowbActivitySign> signlist = crowbActivitySignService.list(new QueryWrapper<CrowbActivitySign>()
                .eq("crowb_activity_id",activityInfo.getId())
                .eq("openid",user.getOpenid()));
        CrowbActivitySign activitySign = null;
        if(signlist!=null && signlist.size()>0){
            activitySign = signlist.get(0);
            if(activitySign.getVerifyStatus()==null || "0".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("0");//已经报名,待审核
            }else if("1".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("1");//已经报名，并且审核通过
            }else if("2".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("2");//已经报名，且已取消
            }else if("3".equals( activitySign.getVerifyStatus())){
                activityInfo.setSignState("3");//已经报名，审核失败
            }
        }else{
            activityInfo.setSignState("-1");//未报名
        }
        return R.ok().put("activityInfo",activityInfo);//活动信息

    }


}
