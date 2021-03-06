package com.icloud.modules.zltdd.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.api.util.WxUserInfoUtil;
import com.icloud.basecommon.service.LockComponent;
import com.icloud.common.DateUtil;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.common.SnowflakeUtils;
import com.icloud.config.ServerConfig;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.wx.entity.WxUser;
import com.icloud.modules.wx.service.WxUserService;
import com.icloud.modules.zltdd.entity.ZltddAwards;
import com.icloud.modules.zltdd.entity.ZltddConfig;
import com.icloud.modules.zltdd.entity.ZltddPrize;
import com.icloud.modules.zltdd.entity.ZltddRecommend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddRecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 推广用户表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Slf4j
@Service
@Transactional
public class ZltddRecommendService extends BaseServiceImpl<ZltddRecommendMapper,ZltddRecommend> {

    private static final String RECOMMEND_LOCK = "RECOMMEND_LOCK_";
    @Autowired
    private ZltddRecommendMapper zltddRecommendMapper;
    @Autowired
    private ZltddConfigService zltddConfigService;
    @Autowired
    private ZltddAwardsService zltddAwardsService;
    @Autowired
    private ZltddPrizeService zltddPrizeService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private WxUserInfoUtil wxUserInfoUtil;
    @Autowired
    private LockComponent lockComponent;

    @Override
    public PageUtils<ZltddRecommend> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddRecommend> list = zltddRecommendMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddRecommend> pageInfo = new PageInfo<ZltddRecommend>(list);
        PageUtils<ZltddRecommend> page = new PageUtils<ZltddRecommend>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }

    public List<ZltddRecommend> queryMixList( Map<String, Object> query) {
        return zltddRecommendMapper.queryMixList(MapEntryUtils.clearNullValue(query));
    }

    /**
     *
     * 扫码场景分析：
     * 1、用户扫自己的码 false
     * 2、用户已扫码 再次扫码 false
     * 3、
     * 扫码事件处理
     * 1、根据场景值id（推主的二维码唯一参数） 查询是否存在推主
     * 2、
     * @param openid
     * @param eventKey
     * @return
     */
    public JSONObject dealScancodeEevent(String openid, String eventKey) {
        JSONObject result = new JSONObject();
        //1、根据场景值id（推主的二维码唯一参数） 查询是否存在推主
        List<ZltddRecommend> list = zltddRecommendMapper.selectList(new QueryWrapper<ZltddRecommend>().eq("my_tdd_code",eventKey));
        if (list==null || 0 == list.size()) {
            log.info("无效二维码_eventKey:"+eventKey+" 推主不存在");
            result.put("code", "-2");
            result.put("msg", "无效二维码");
            return result;//无效邀请
        }
        int size = 0;
        //2、判断活动是否结束
        for (ZltddRecommend recommend : list) {
            Long activityId = recommend.getActivityId();
            ZltddConfig config = (ZltddConfig) zltddConfigService.getById(activityId);
            if("0".equals(config.getStatus()) || config.getEndTime().compareTo(new Date())==-1){//结束时间比当前时间小
                log.info(config.getActivityName()+"结束活动_"+openid);
                result.put("code", "-3");
                result.put("msg", "活动已结束");
                continue;
            }
            //3、判断是否是普通推主 以及已达到最大限制人数
            if("0".equals(recommend.getUserType())){
                if(recommend.getMaxNum()!=null && recommend.getReadyedNum()!=null && recommend.getMaxNum().compareTo(recommend.getReadyedNum())<=0){
                    log.info(openid+"推广已达上限");
                    result.put("code", "-3");
                    result.put("msg", "你扫的二维码已达发展上限");
                    continue;
                }
            }
            //4、判断是否已经是推主
            List<ZltddRecommend> invitedlist = zltddRecommendMapper.selectList(new QueryWrapper<ZltddRecommend>().eq("openid",openid).eq("activity_id",activityId));
            if (invitedlist!=null && invitedlist.size()>0) {
                log.info("openid:"+openid+" 用户已被"+invitedlist.get(0).getParentTddCode()+"邀请");
                result.put("code", "-1");
                result.put("msg", "已存在唯一推荐人");
                return result;//无效邀请
            }
            //5、判断是否是用户是否存在或者扫自己的推荐码
            WxUser user = wxUserService.findByOpenId(openid);
            if(user!=null){
                if (recommend.getUserId().compareTo(user.getId()) == 0) {
                    log.info("openid:"+openid+"不能自己邀请自己");
                    result.put("code", "-1");
                    result.put("msg", "已存在唯一推荐人");
                    continue;
                }
            }else{
                //生成微信用户记录
                user = new WxUser();
                try{
                    JSONObject json = wxUserInfoUtil.getUserInfo(openid,2);
                    if(json!=null && json.containsKey("nickname")){
                        user.setNickname(json.getString("nickname"));
                    }
                    if(json!=null && json.containsKey("unionid")){
                        user.setUnionid(json.getString("unionid"));
                    }
                    if(json!=null && json.containsKey("headimgurl")){
                        user.setHeadimgurl(json.getString("headimgurl"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                user.setCreateTime(new Date());
                user.setOpenid(openid);
                user.setTddCode("zltdd_"+wxUserService.getTddNo());
                wxUserService.save(user);
            }
            //7、更新推主 推广人数
            try {
                if (lockComponent.tryLock(RECOMMEND_LOCK + recommend.getOpenid(), 2)) {
                    recommend = zltddRecommendMapper.selectById(recommend.getId());
                    recommend.setReadyedNum(recommend.getReadyedNum() != null ? recommend.getReadyedNum() + 1 : 1);
                    recommend.setModifyTime(new Date());
                    updateUserReadyedNum(recommend);
                } else {
                    log.info("openid:" + openid + "更新用户总数失败");
                    result.put("code", "3");
                    result.put("msg", "系统繁忙，请稍后再试");
                    continue;
                }
            }finally {
                lockComponent.release(RECOMMEND_LOCK + recommend.getOpenid());
            }
            //6、生成推荐记录
            ZltddRecommend invited = new ZltddRecommend();
            invited.setUserType("0");//普通用户
            invited.setMaxNum(config.getMaxNum());//最大可邀请人数
            invited.setReadyedNum(0);//已邀请人数
            invited.setUserId(user.getId());
            invited.setMyTddCode(user.getTddCode());
            invited.setParentTddCode(recommend.getMyTddCode());//上级推广码code
            invited.setParentId(recommend.getId());
            invited.setOpenid(user.getOpenid());
            invited.setActivityId(activityId);
            invited.setCreateTime(new Date());
            zltddRecommendMapper.insert(invited);

            //8、生成推主与被邀请者奖励记录
            //被邀请者奖励
            ZltddPrize zltddPrize = (ZltddPrize) zltddPrizeService.getById(config.getPrizeId());
            ZltddAwards zltddAwards = new ZltddAwards();
            zltddAwards.setActivityId(activityId);
            zltddAwards.setUserId(user.getId());//用户id
            zltddAwards.setAwardsType("1");//参与扫描活动
            zltddAwards.setPrizeId(config.getPrizeId());//奖品id
            zltddAwards.setPrizeName(zltddPrize.getPrizeName());//奖品名称
            zltddAwards.setStatus("0");//未领取
            zltddAwards.setScores(zltddPrize.getScore());//积分值
            zltddAwards.setOrderNo(SnowflakeUtils.getOrderNoByWordId(serverConfig.getServerPort()%31L));
            zltddAwards.setCreateTime(new Date());
            zltddAwards.setExpireTime(DateUtil.getBeforeNDate(zltddAwards.getCreateTime(),30));//获取一个月后的日期
            zltddAwardsService.save(zltddAwards);
            //推主奖励
            zltddAwards = new ZltddAwards();
            zltddAwards.setActivityId(activityId);
            zltddAwards.setUserId(recommend.getUserId());//用户id
            zltddAwards.setAwardsType("2");//下线扫码
            zltddAwards.setPrizeId(config.getPrizeId());//奖品id
            zltddAwards.setPrizeName(zltddPrize.getPrizeName());//奖品名称
            zltddAwards.setStatus("0");//未领取
            zltddAwards.setScores(zltddPrize.getScore());//积分值
            zltddAwards.setOrderNo(SnowflakeUtils.getOrderNoByWordId(serverConfig.getServerPort()%31L));
            zltddAwards.setCreateTime(new Date());
            zltddAwards.setExpireTime(DateUtil.getBeforeNDate(zltddAwards.getCreateTime(),30));//获取一个月后的日期
            zltddAwardsService.save(zltddAwards);
            //
            user.setIsbind("1");//成为推广者
            wxUserService.updateById(user);
            result.put("code", "1");
            WxUser recommendWxUser = (WxUser) wxUserService.getById(recommend.getUserId());
            result.put("nickname",recommendWxUser.getNickname());
            result.put("msg", "扫码成功");
        }
        return result;
    }

    public void updateUserReadyedNum(ZltddRecommend recommend){
        zltddRecommendMapper.updateById(recommend);
    }
}

