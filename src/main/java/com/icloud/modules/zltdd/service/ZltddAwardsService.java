package com.icloud.modules.zltdd.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.zltdd.entity.ZltddAwards;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddAwardsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 中奖记录表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:32
 */
@Service
@Transactional
public class ZltddAwardsService extends BaseServiceImpl<ZltddAwardsMapper,ZltddAwards> {

    @Autowired
    private ZltddAwardsMapper zltddAwardsMapper;

    @Override
    public PageUtils<ZltddAwards> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddAwards> list = zltddAwardsMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddAwards> pageInfo = new PageInfo<ZltddAwards>(list);
        PageUtils<ZltddAwards> page = new PageUtils<ZltddAwards>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

