package com.icloud.modules.zltdd.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.zltdd.entity.ZltddRecommend;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddRecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 推广用户表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Service
@Transactional
public class ZltddRecommendService extends BaseServiceImpl<ZltddRecommendMapper,ZltddRecommend> {

    @Autowired
    private ZltddRecommendMapper zltddRecommendMapper;

    @Override
    public PageUtils<ZltddRecommend> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddRecommend> list = zltddRecommendMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddRecommend> pageInfo = new PageInfo<ZltddRecommend>(list);
        PageUtils<ZltddRecommend> page = new PageUtils<ZltddRecommend>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

