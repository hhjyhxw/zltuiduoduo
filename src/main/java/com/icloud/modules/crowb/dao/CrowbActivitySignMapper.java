package com.icloud.modules.crowb.dao;

import com.icloud.modules.crowb.entity.CrowbActivitySign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:20
 */
public interface CrowbActivitySignMapper extends BaseMapper<CrowbActivitySign> {

	List<CrowbActivitySign> queryMixList(Map<String, Object> map);
}
