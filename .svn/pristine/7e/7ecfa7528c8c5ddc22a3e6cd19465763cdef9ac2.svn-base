package com.icloud.modules.zltdd.service;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.basecommon.service.redislock.DistributedLockUtil;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.exceptions.ApiException;
import com.icloud.exceptions.BeanException;
import com.icloud.modules.wx.entity.WxUser;
import com.icloud.modules.zltdd.entity.ZltddAwards;
import com.icloud.thirdinterfaces.score.entity.LongChargeEntity;
import com.icloud.thirdinterfaces.score.service.LongbiServiceImpl;
import com.icloud.thirdinterfaces.score.utils.LongCoinUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddAwardsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 中奖记录表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:32
 */
@Slf4j
@Service
@Transactional
public class ZltddAwardsService extends BaseServiceImpl<ZltddAwardsMapper,ZltddAwards> {

    @Autowired
    private ZltddAwardsMapper zltddAwardsMapper;
    @Autowired
    private DistributedLockUtil distributedLockUtil;
    @Autowired
    private LongbiServiceImpl longbiServiceImpl;
    @Autowired
    private LongCoinUtil longCoinUtil;

    @Override
    public PageUtils<ZltddAwards> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddAwards> list = zltddAwardsMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddAwards> pageInfo = new PageInfo<ZltddAwards>(list);
        PageUtils<ZltddAwards> page = new PageUtils<ZltddAwards>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }

    /**
     * 领取奖品，发放龙币
     * @param awards
     * @param user
     */
    public void getAwards(ZltddAwards awards, WxUser user) {

        LongChargeEntity entity = longCoinUtil.getChargeEntity(user.getOpenid(),awards.getScores().toString(),"2");
            JSONObject result = null;
            try {
                result = longbiServiceImpl.recharge(entity.getRequestParamMap());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApiException("系统开小差了,请稍后再试");
            }
            if(result!=null && "000000".equals(result.getString("returncode"))){
                awards.setSeqNo(entity.getSeq());
                awards.setReceiveTime(new Date());
                awards.setModifyTime(new Date());
                awards.setStatus("1");
                zltddAwardsMapper.updateById(awards);
            }else{
                throw new ApiException(LongbiServiceImpl.getCodeMap().get(result.getString("returncode")));
            }
    }
}

