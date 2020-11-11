package com.icloud.modules.crowb.dao;

import com.icloud.modules.crowb.entity.CrowbActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:20
 */
public interface CrowbActivityMapper extends BaseMapper<CrowbActivity> {

	List<CrowbActivity> queryMixList(Map<String, Object> map);
}
