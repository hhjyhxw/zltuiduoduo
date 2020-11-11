package com.icloud.modules.crowb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.crowb.entity.CrowbActivityScorerecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.crowb.dao.CrowbActivityScorerecordMapper;
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
public class CrowbActivityScorerecordService extends BaseServiceImpl<CrowbActivityScorerecordMapper,CrowbActivityScorerecord> {

    @Autowired
    private CrowbActivityScorerecordMapper crowbActivityScorerecordMapper;

    @Override
    public PageUtils<CrowbActivityScorerecord> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<CrowbActivityScorerecord> list = crowbActivityScorerecordMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<CrowbActivityScorerecord> pageInfo = new PageInfo<CrowbActivityScorerecord>(list);
        PageUtils<CrowbActivityScorerecord> page = new PageUtils<CrowbActivityScorerecord>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

