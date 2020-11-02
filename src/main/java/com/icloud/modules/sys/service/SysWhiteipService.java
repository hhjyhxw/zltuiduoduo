package com.icloud.modules.sys.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.sys.dao.SysWhiteipMapper;
import com.icloud.modules.sys.entity.SysWhiteip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-26 09:33:26
 */
@Service
@Transactional
public class SysWhiteipService extends BaseServiceImpl<SysWhiteipMapper, SysWhiteip> {

    @Autowired
    private SysWhiteipMapper sysWhiteipMapper;

    @Override
    public PageUtils<SysWhiteip> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<SysWhiteip> list = sysWhiteipMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<SysWhiteip> pageInfo = new PageInfo<SysWhiteip>(list);
        PageUtils<SysWhiteip> page = new PageUtils<SysWhiteip>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

