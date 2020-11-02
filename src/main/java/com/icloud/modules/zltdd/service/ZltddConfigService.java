package com.icloud.modules.zltdd.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.zltdd.entity.ZltddConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 活动配置表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Service
@Transactional
public class ZltddConfigService extends BaseServiceImpl<ZltddConfigMapper,ZltddConfig> {

    @Autowired
    private ZltddConfigMapper zltddConfigMapper;

    @Override
    public PageUtils<ZltddConfig> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddConfig> list = zltddConfigMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddConfig> pageInfo = new PageInfo<ZltddConfig>(list);
        PageUtils<ZltddConfig> page = new PageUtils<ZltddConfig>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

