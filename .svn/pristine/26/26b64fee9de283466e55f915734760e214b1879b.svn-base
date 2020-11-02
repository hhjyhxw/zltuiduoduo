package com.icloud.modules.zltdd.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.zltdd.entity.ZltddPrize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddPrizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 奖品表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Service
@Transactional
public class ZltddPrizeService extends BaseServiceImpl<ZltddPrizeMapper,ZltddPrize> {

    @Autowired
    private ZltddPrizeMapper zltddPrizeMapper;

    @Override
    public PageUtils<ZltddPrize> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddPrize> list = zltddPrizeMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddPrize> pageInfo = new PageInfo<ZltddPrize>(list);
        PageUtils<ZltddPrize> page = new PageUtils<ZltddPrize>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

