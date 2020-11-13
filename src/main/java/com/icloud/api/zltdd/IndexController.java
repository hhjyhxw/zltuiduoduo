package com.icloud.api.zltdd;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.icloud.config.threadpool.ThreadPoodExecuteService;
import com.icloud.modules.wx.entity.WxUser;
import com.icloud.modules.wx.service.WxUserService;
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

import java.util.*;

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
    @Autowired
    private WxUserService wxUserService;


    /**
     * 用户信息接口
     * @return
     */
    @ApiOperation(value="用户信息接口", notes="")
    @RequestMapping(value = "/userinfo",method = {RequestMethod.GET})
    @ResponseBody
    public R userinfo(@LoginUser WxUser user) {
        user = (WxUser) wxUserService.getById(user.getId());
       List<ZltddRecommend> list = zltddRecommendService.list(new QueryWrapper<ZltddRecommend>().eq("user_id",user.getId()));
       if(list!=null && list.size()>0){

           user.setIsbind("1");//已经成为推客
           ZltddRecommend recommend = list.get(0);
           if("1".equals(recommend.getIsfirstin())){
               user.setIsfirstin("0");//不是第一次进入
           }else{
               //成为推客后第一次进入
               ZltddRecommend updateRecommend = new ZltddRecommend();
               updateRecommend.setId(recommend.getId());
               updateRecommend.setIsfirstin("1");//系统变成已进入
               zltddRecommendService.updateById(updateRecommend);
               user.setIsfirstin("1");//页面是首次进入
           }
           String parentTddCode = recommend.getParentTddCode();
           WxUser parentUser = wxUserService.findByTddCode(parentTddCode);
           if(parentUser==null){//天使用户，可以无限发展
               user.setParentNick("平台");
               user.setMaxNum(-1);
               user.setIsable(1);
           }else{//普通用户限制发展
               user.setParentNick(parentUser.getNickname());
               user.setMaxNum(recommend.getMaxNum());
               Integer readyedNum = recommend.getReadyedNum()==null?0:recommend.getReadyedNum();
               if(recommend.getMaxNum()<=readyedNum){//发展人数达到最大值，不能再发展
                   user.setIsable(0);
               }else{
                   user.setIsable(1);
               }
           }
           user.setReadyedNum(recommend.getReadyedNum());
           if(StringUtil.checkStr(parentTddCode) && parentTddCode.contains("zltdd_")){
               parentTddCode = parentTddCode.replace("zltdd_","");
           }
           user.setParentTddCode(parentTddCode);
       }else {
           user.setIsbind("0");//还未成为推客
           user.setIsfirstin("0");//
       }
       if(!StringUtil.checkStr(user.getRecommendUrl())){
           wxUserService.updateMyCard(user);
       }
        user.setTddCode(user.getTddCode().replace("zltdd_",""));
       //异步查询用户已发展人数 与实际发展人数是否相等，不等更新
        //异步执行发送任务
//        ThreadPoodExecuteService.getTaskExecutor().execute(cardVerifyCallbackService);
       return R.ok().put("user",user).put("url",myPropertitys.getScorePlatformUrl());
    }

    /**
     * 我的未领取奖励数
     * @return
     */
    @ApiOperation(value="我的未领取奖励数", notes="")
    @RequestMapping(value = "/myUnreceiveAaward",method = {RequestMethod.GET})
    @ResponseBody
    public R myUnreceiveAaward(@LoginUser WxUser user) {
        List<ZltddAwards> list = zltddAwardsService.list(new QueryWrapper<ZltddAwards>().eq("user_id",user.getId()).eq("status","0").gt("expire_time",new Date()));
        int countNum = 0;
        int totalScore = 0;
        if(list!=null && list.size()>0){
            countNum = list.size();
            for (ZltddAwards awards:list){
                totalScore = totalScore+awards.getScores();
            }
        }
        return R.ok().put("countNum",countNum).put("totalScore",totalScore);
    }

    /**
     * 我的名片
     * @return
     */
    @ApiOperation(value="我的名片", notes="")
    @RequestMapping(value = "/myCard",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public R myCard(@LoginUser WxUser user) {
        user = (WxUser) wxUserService.getById(user.getId());
        return R.ok().put("url",user.getRecommendUrl()+"?t="+RandomUtil.randomString(6))
                .put("expiredTime",2592000 - ((System.currentTimeMillis() - user.getRecommendUrlTime().getTime()) / 1000));
    }
    /**
     * 更新推广名片
     * @return
     */
    @ApiOperation(value="更新推广名片", notes="")
    @RequestMapping(value = "/updateMyCard",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public R updateMyCard(@LoginUser WxUser user) {
        return wxUserService.updateMyCard(user);
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
        List<ZltddRecommend> recommendslist = zltddRecommendService.list(new QueryWrapper<ZltddRecommend>().eq("user_id",user.getId()));

        if(recommendslist==null && recommendslist.size()==0){
            return R.ok().put("page",new PageUtils(new ArrayList<>(),0,10,0));
        }
        ZltddRecommend recommend = recommendslist.get(0);
        Map parma = new HashMap();
        parma.put("page",pageNum);
        parma.put("limit",pageSize);
        parma.put("parentId",recommend.getId());
        Query query = new Query(parma);
        PageUtils<ZltddRecommend> page = zltddRecommendService.findByPage(query.getPageNum(),query.getPageSize(), query);
        List<ZltddRecommend> list = (List<ZltddRecommend>) page.getList();
        if(list!=null && list.size()>0){
            list.forEach(p->{
                if(p.getUser()!=null){
                    p.setNickname(p.getUser().getNickname());
                    p.setHeadimgurl(p.getUser().getHeadimgurl());
                    p.setUser(null);
                }
            });
        }
        page.setList(list);
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
        return R.error(105,"不是积分会员").put("url",myPropertitys.getScorePlatformBindUrl());
    }

    /**
     * 领取单个奖品
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
        if("1".equals(prize.getStatus())){
            return R.error("奖品已领取");
        }
        if("2".equals(prize.getStatus()) || new Date().after(prize.getExpireTime())){
            return R.error("奖品已过期");
        }
        if(prize.getUserId().compareTo(user.getId())!=0){
            return R.error("非法请求");
        }
        zltddAwardsService.getAwards(prize,user);
        return R.ok().put("url",myPropertitys.getScorePlatformUrl());
    }

    /**
     * 领取所有奖品
     * @return
     */
    @ApiOperation(value="领取所有奖品", notes="")
    @RequestMapping(value = "/getAwardsAll",method = {RequestMethod.GET})
    @ResponseBody
    public R getAwardsAll(@LoginUser WxUser user) {
        boolean result = zltddAwardsService.getMyAwards(user);
        if(result){
            return R.ok().put("url",myPropertitys.getScorePlatformUrl());
        }else {
            return R.error("领取失败");
        }

    }

    /**
     * 绑定成功天使用户
     * @return
     */
    @ApiOperation(value="绑定成为天使用户", notes="")
    @RequestMapping(value = "/bindRecomend",method = {RequestMethod.POST})
    @ResponseBody
    public R bindRecomend(@LoginUser WxUser user,@RequestBody UserAccount userAccount) {
        ValidatorUtils.validateEntityForFront(userAccount);
       List<ZltddShop> list = zltddShopService.list(new QueryWrapper<ZltddShop>()
//               .eq("contact_phone",userAccount.getContactPhone())
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
