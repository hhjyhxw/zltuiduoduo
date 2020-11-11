package com.icloud.modules.crowb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.crowb.entity.CrowbActivity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.crowb.dao.CrowbActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CrowbActivityService extends BaseServiceImpl<CrowbActivityMapper,CrowbActivity> {

    @Autowired
    private CrowbActivityMapper crowbActivityMapper;

    @Override
    public PageUtils<CrowbActivity> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<CrowbActivity> list = crowbActivityMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<CrowbActivity> pageInfo = new PageInfo<CrowbActivity>(list);
        PageUtils<CrowbActivity> page = new PageUtils<CrowbActivity>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

