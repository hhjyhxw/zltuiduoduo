package com.icloud.modules.wx.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.basecommon.util.lang.StringUtils;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.util.wx.CreateQrcodeUtil;
import com.icloud.modules.wx.dao.WxUserMapper;
import com.icloud.modules.wx.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Mr.Z
 * @email 512162086@qq.com
 * @date 2019-11-01 21:46:51
 */
@Service
@Transactional
public class WxUserService extends BaseServiceImpl<WxUserMapper, WxUser> {
    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private CreateQrcodeUtil createQrcodeUtil;

    public WxUser findByUnionid(String unionid) {
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<WxUser>();
        queryWrapper.eq("unionid",unionid);
        return (WxUser) getOne(queryWrapper);
    }

    public WxUser findByOpenId(String openId) {
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<WxUser>();
        queryWrapper.eq("openid",openId);
        return (WxUser) getOne(queryWrapper);
    }
    public WxUser findByXcxopenid(String xcxopenid) {
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<WxUser>();
        queryWrapper.eq("xcxopenid",xcxopenid);
        return (WxUser) getOne(queryWrapper);
    }

    public WxUser findByTddCode(String tddcode) {
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<WxUser>();
        queryWrapper.eq("tdd_code",tddcode);
        return (WxUser) getOne(queryWrapper);
    }


    @Override
    public PageUtils<WxUser> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<WxUser> list = wxUserMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<WxUser> pageInfo = new PageInfo<WxUser>(list);
        PageUtils<WxUser> page = new PageUtils<WxUser>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }

    public List<WxUser> queryMixList(Map<String,Object> map){
        return wxUserMapper.queryMixList(map);
    }

    /**
     * 顺序获取推广码
     * @return
     */
    public synchronized String getTddNo(){
       return wxUserMapper.getTddNo();
    }

    /**
     * 更新推广名片
     * @param user
     */
    public R updateMyCard(WxUser user) {
        if (StringUtils.isBlank(user.getTddCode())) {
            return R.error("不存在推广码");
        }
        user.setRecommendUrlTime(new Date());
        String cardUrl = null;
        try {
            cardUrl = createQrcodeUtil.createMasterCard(user);
            user.setRecommendUrl(cardUrl);
            wxUserMapper.updateById(user);
            return R.ok().put("url",cardUrl + "?t=" + RandomUtil.randomString(4));
        } catch (IOException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}