package com.icloud.modules.zltdd.dao;

import com.icloud.modules.zltdd.entity.ZltddAwards;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 中奖记录表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:32
 */
public interface ZltddAwardsMapper extends BaseMapper<ZltddAwards> {

	List<ZltddAwards> queryMixList(Map<String,Object> map);
}
