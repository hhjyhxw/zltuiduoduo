package com.icloud.api.zltdd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.AuthIgnore;
import com.icloud.annotation.LoginUser;
import com.icloud.api.vo.MyPrizeVo;
import com.icloud.api.vo.UserAccount;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.beanutils.ColaBeanUtils;
import com.icloud.common.util.StringUtil;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.config.global.MyPropertitys;
import com.icloud.modules.wx.entity.WxUser;
import com.icloud.modules.zltdd.entity.ZltddAwards;
import com.icloud.modules.zltdd.entity.ZltddRecommend;
import com.icloud.modules.zltdd.entity.ZltddShop;
import com.icloud.modules.zltdd.service.ZltddAwardsService;
import com.icloud.modules.zltdd.service.ZltddRecommendService;
import com.icloud.modules.zltdd.service.ZltddShopService;
import com.icloud.thirdinterfaces.score.entity.LongQueryEntity;
import com.icloud.thirdinterfaces.score.service.LongbiServiceImpl;
import com.icloud.thirdinterfaces.score.utils.LongCoinUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api("首页相关接口")
@RestController
@RequestMapping("/api/index")
public class IndexController {

    @Autowired
    private ZltddRecommendService zltddRecommendService;
    @Autowired
    private ZltddShopService zltddShopService;
    @Autowired
    private ZltddAwardsService zltddAwardsService;
    @Autowired
    private LongbiServiceImpl longbiServiceImpl;
    @Autowired
    private LongCoinUtil longCoinUtil;
    @Autowired
    private MyPropertitys myPropertitys;


    /**
     * 用户信息接口
     * @return
     */
    @ApiOperation(value="用户信息接口", notes="")
    @RequestMapping(value = "/userinfo",method = {RequestMethod.GET})
    @ResponseBody
    public R userinfo(@LoginUser WxUser user) {
       List<ZltddRecommend> list = zltddRecommendService.list(new QueryWrapper<ZltddRecommend>().eq("user_id",user.getId()));
       if(list!=null && list.size()>0){
           user.setIsbind("1");//已经成为推客
       }else {
           user.setIsbind("0");//还未成为推客
       }
       return R.ok().put("user",user);
    }

    /**
     * 我的名片
     * @return
     */
    @ApiOperation(value="我的名片", notes="")
    @RequestMapping(value = "/myCard",method = {RequestMethod.GET})
    @ResponseBody
    public R myCard(@LoginUser WxUser user) {

        return R.ok().put("user",user);
    }

    /**
     * 我的团队
     * @return
     */
    @ApiOperation(value="我的团队", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少记录", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/myTeam",method = {RequestMethod.GET})
    @ResponseBody
    public R myTeam(@LoginUser WxUser user,String pageNum,String pageSize) {
        Map parma = new HashMap();
        parma.put("page",pageNum);
        parma.put("limit",pageSize);
        parma.put("userId",user.getId());
        Query query = new Query(parma);
        PageUtils<ZltddRecommend> page = zltddRecommendService.findByPage(query.getPageNum(),query.getPageSize(), query);
        return R.ok().put("page",page);
    }

    /**
     * 我的中奖记录
     * @return
     */
    @ApiOperation(value="我的中奖记录", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少记录", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/myAarwordsList",method = {RequestMethod.GET})
    @ResponseBody
    public R myAarwordsList(@LoginUser WxUser user,String pageNum,String pageSize) {
        Map parma = new HashMap();
        parma.put("page",pageNum);
        parma.put("limit",pageSize);
        parma.put("userId",user.getId());
        Query query = new Query(parma);
        PageUtils<ZltddAwards> page = zltddAwardsService.findByPage(query.getPageNum(),query.getPageSize(), query);
        List<ZltddAwards> list = (List<ZltddAwards>) page.getList();
        if(list!=null && list.size()>0){
            List<MyPrizeVo> newlist = ColaBeanUtils.copyListProperties(list , MyPrizeVo::new, (articleEntity, articleVo) -> {
                // 回调处理
            });
            page.setList(newlist);
        }
        return R.ok().put("page",page);

    }

    /**
     * 查询是否是会员
     * @return
     */
    @ApiOperation(value="查询是否是会员", notes="")
    @RequestMapping(value = "/checkMember",method = {RequestMethod.GET})
    @ResponseBody
    public R checkMember(@LoginUser WxUser user) {
        LongQueryEntity queryEntity = longCoinUtil.getQueryEntity(user.getOpenid(),"2");
        JSONObject result = longbiServiceImpl.queryLongbi(queryEntity.getRequestParamMap());
        log.info("checkMember_querylongbi=="+ result);
        if(result!=null && result.containsKey("returncode") && "000000".equals(result.getString("returncode"))){
            return R.ok();
        }
        return R.error(105,"不是积分会员").put("url",myPropertitys.getScorePlatformUrl());
    }

    /**
     * 领取奖品
     * @return
     */
    @ApiOperation(value="领取奖品", notes="")
    @RequestMapping(value = "/getAwards",method = {RequestMethod.GET})
    @ResponseBody
    public R getAwards(@LoginUser WxUser user,@RequestParam Long id) {
        Object awaard = zltddAwardsService.getById(id);
        if(awaard==null){
            return R.error("奖品不存在");
        }
        ZltddAwards prize = (ZltddAwards) awaard;
        if(prize.getUserId().compareTo(user.getId())!=0){
            return R.error("非法请求");
        }
        zltddAwardsService.getAwards(prize,user);
        return R.ok();
    }
    /**
     * 绑定成功天使用户
     * @return
     */
    @ApiOperation(value="绑定成功天使用户", notes="")
    @RequestMapping(value = "/bindRecomend",method = {RequestMethod.GET})
    @ResponseBody
    public R bindRecomend(@LoginUser WxUser user,@RequestBody UserAccount userAccount) {
        ValidatorUtils.validateEntityForFront(userAccount);
       List<ZltddShop> list = zltddShopService.list(new QueryWrapper<ZltddShop>()
               .eq("contact_phone",userAccount.getContactPhone())
               .eq("licenese",userAccount.getLicenese()));
       if(list==null || list.size()==0){
           return R.error(102,"账号不存在");
       }
        ZltddShop shop = list.get(0);
       if(StringUtil.checkStr(shop.getOpenid())){
           return R.error(103,"账号已被绑定");
       }
        zltddShopService.createTopRecommend(shop,user);
       return R.ok();
    }
}