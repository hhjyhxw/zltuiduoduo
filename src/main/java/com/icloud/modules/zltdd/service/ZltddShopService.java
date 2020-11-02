package com.icloud.modules.zltdd.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.zltdd.entity.ZltddShop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.zltdd.dao.ZltddShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 顶级资格用户表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:32
 */
@Service
@Transactional
public class ZltddShopService extends BaseServiceImpl<ZltddShopMapper,ZltddShop> {

    @Autowired
    private ZltddShopMapper zltddShopMapper;

    @Override
    public PageUtils<ZltddShop> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ZltddShop> list = zltddShopMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ZltddShop> pageInfo = new PageInfo<ZltddShop>(list);
        PageUtils<ZltddShop> page = new PageUtils<ZltddShop>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

