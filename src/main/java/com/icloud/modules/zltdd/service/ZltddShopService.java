package com.icloud.modules.zltdd.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.DateUtil;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.common.SnowflakeUtils;
import com.icloud.config.ServerConfig;
import com.icloud.modules.wx.entity.WxUser;
import com.icloud.modules.wx.service.WxUserService;
import com.icloud.modules.zltdd.dao.ZltddRecommendMapper;
import com.icloud.modules.zltdd.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddShopMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 顶级资格用户表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:32
 */
@Service
@Transactional
public class ZltddShopService extends BaseServiceImpl<ZltddShopMapper,ZltddShop> {

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
    private ZltddShopMapper zltddShopMapper;

    @Override
    public PageUtils<ZltddShop> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddShop> list = zltddShopMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddShop> pageInfo = new PageInfo<ZltddShop>(list);
        PageUtils<ZltddShop> page = new PageUtils<ZltddShop>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }

    /**
     * 1、创建顶级推客
     * 2、更新 ZltddShop 绑定状态
     * 3、user 绑定状态
     * @param shop
     * @param user
     */
    public void  createTopRecommend(ZltddShop shop, WxUser user) {
        ZltddConfig config = (ZltddConfig) zltddConfigService.getById(shop.getActivityId());
        //1、生成推荐记录
        ZltddRecommend invited = new ZltddRecommend();
        invited.setUserType("1");//天使用户
        invited.setMaxNum(-1);//最大可邀请人数
        invited.setReadyedNum(0);//已邀请人数
        invited.setUserId(user.getId());
        invited.setMyTddCode(user.getTddCode());
        invited.setParentTddCode("000");//上级推广码code
        invited.setParentId(-1L);
        invited.setOpenid(user.getOpenid());
        invited.setActivityId(config.getId());
        zltddRecommendMapper.insert(invited);
        //2、生成奖励记录
        ZltddPrize zltddPrize = (ZltddPrize) zltddPrizeService.getById(config.getPrizeId());
        ZltddAwards zltddAwards = new ZltddAwards();
        zltddAwards.setActivityId(config.getId());
        zltddAwards.setUserId(user.getId());//用户id
        zltddAwards.setAwardsType("3");//绑定成为顶级推主
        zltddAwards.setPrizeId(config.getPrizeId());//奖品id
        zltddAwards.setPrizeName(zltddPrize.getPrizeName());//奖品名称
        zltddAwards.setStatus("0");//未领取
        zltddAwards.setScores(zltddPrize.getScore());//积分值
        zltddAwards.setOrderNo(SnowflakeUtils.getOrderNoByWordId(serverConfig.getServerPort()%31L));
        zltddAwards.setCreateTime(new Date());
        zltddAwards.setExpireTime(DateUtil.getBeforeNDate(zltddAwards.getCreateTime(),30));//获取一个月后的日期
        zltddAwardsService.save(zltddAwards);
        //3、更新shop
        shop.setOpenid(user.getOpenid());
        zltddShopMapper.updateById(shop);
        //4、更新user
        user.setIsbind("1");
        wxUserService.updateById(user);
    }
}

