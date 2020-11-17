package com.icloud.modules.crowb.service;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.basecommon.service.redislock.DistributedLockUtil;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.crowb.entity.CrowbActivityScorerecord;
import com.icloud.modules.crowb.entity.CrowbActivitySign;
import com.icloud.thirdinterfaces.score.entity.LongChargeEntity;
import com.icloud.thirdinterfaces.score.entity.LongConsumeEntity;
import com.icloud.thirdinterfaces.score.service.LongbiServiceImpl;
import com.icloud.thirdinterfaces.score.utils.LongCoinUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.crowb.dao.CrowbActivitySignMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:20
 */
@Service
@Transactional
public class CrowbActivitySignService extends BaseServiceImpl<CrowbActivitySignMapper,CrowbActivitySign> {

    @Autowired
    private CrowbActivitySignMapper crowbActivitySignMapper;
    @Autowired
    private DistributedLockUtil distributedLockUtil;
    @Autowired
    private LongbiServiceImpl longbiServiceImpl;
    @Autowired
    private LongCoinUtil longCoinUtil;
    @Autowired
    private CrowbActivityScorerecordService crowbActivityScorerecordService;
    @Override
    public PageUtils<CrowbActivitySign> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<CrowbActivitySign> list = crowbActivitySignMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<CrowbActivitySign> pageInfo = new PageInfo<CrowbActivitySign>(list);
        PageUtils<CrowbActivitySign> page = new PageUtils<CrowbActivitySign>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }


    /**
     * 审核通过
     * 消耗龙币
    * @param passSign
     */
    public void passSign(CrowbActivitySign passSign) {
        CrowbActivitySign crowbActivitySign = crowbActivitySignMapper.selectById(passSign.getId());
        LongConsumeEntity entity = longCoinUtil.getComsueEntity(crowbActivitySign.getOpenid(),crowbActivitySign.getScore().toString(),"3");
        JSONObject result = null;
        try {
            result = longbiServiceImpl.consume(entity.getRequestParamMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("系统开小差了,请稍后再试");
        }
        if(result!=null && "000000".equals(result.getString("returncode"))){
            CrowbActivityScorerecord crowbActivityScorerecord = new CrowbActivityScorerecord();
            crowbActivityScorerecord.setId(passSign.getId());
            crowbActivityScorerecord.setCreateTime(new Date());
            crowbActivityScorerecord.setOpenid(crowbActivitySign.getOpenid());
            crowbActivityScorerecord.setSeq(entity.getSeq());
            //保存龙币消耗记录
            crowbActivityScorerecordService.save(crowbActivityScorerecord);
            //更新审核通过状态
            passSign.setVerifyStatus("1");
            passSign.setModifyTime(new Date());
            crowbActivitySignMapper.updateById(passSign);
        }else{
            throw new ApiException(LongbiServiceImpl.getCodeMap().get(result.getString("returncode")));
        }
    }

    /**
     * 取消审核
     * //1、回退龙币
     * @param cancelSign
     */
    public void cancelSign(CrowbActivitySign cancelSign) {
        CrowbActivitySign crowbActivitySign = crowbActivitySignMapper.selectById(cancelSign.getId());
        LongChargeEntity entity = longCoinUtil.getChargeEntity(crowbActivitySign.getOpenid(),crowbActivitySign.getScore().toString(),"3");
        JSONObject result = null;
        try {
            result = longbiServiceImpl.recharge(entity.getRequestParamMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("系统开小差了,请稍后再试");
        }
        if(result!=null && "000000".equals(result.getString("returncode"))){
            CrowbActivityScorerecord crowbActivityScorerecord = new CrowbActivityScorerecord();
            crowbActivityScorerecord.setId(cancelSign.getId());
            crowbActivityScorerecord.setCreateTime(new Date());
            crowbActivityScorerecord.setOpenid(crowbActivitySign.getOpenid());
            crowbActivityScorerecord.setSeq(entity.getSeq());
            //保存龙币消耗记录
            crowbActivityScorerecordService.save(crowbActivityScorerecord);
            //更新审核通过状态
            cancelSign.setVerifyStatus("2");
            cancelSign.setModifyTime(new Date());
            crowbActivitySignMapper.updateById(cancelSign);
        }else{
            throw new ApiException(LongbiServiceImpl.getCodeMap().get(result.getString("returncode")));
        }
    }
}

