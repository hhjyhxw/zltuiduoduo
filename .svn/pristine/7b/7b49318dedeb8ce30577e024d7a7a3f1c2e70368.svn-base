package com.icloud.modules.crowb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.crowb.entity.CrowbActivitySign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.crowb.dao.CrowbActivitySignMapper;
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
public class CrowbActivitySignService extends BaseServiceImpl<CrowbActivitySignMapper,CrowbActivitySign> {

    @Autowired
    private CrowbActivitySignMapper crowbActivitySignMapper;

    @Override
    public PageUtils<CrowbActivitySign> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<CrowbActivitySign> list = crowbActivitySignMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<CrowbActivitySign> pageInfo = new PageInfo<CrowbActivitySign>(list);
        PageUtils<CrowbActivitySign> page = new PageUtils<CrowbActivitySign>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

