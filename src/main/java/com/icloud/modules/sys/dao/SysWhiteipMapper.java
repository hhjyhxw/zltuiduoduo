package com.icloud.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.sys.entity.SysWhiteip;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-26 09:33:26
 */
public interface SysWhiteipMapper extends BaseMapper<SysWhiteip> {

	List<SysWhiteip> queryMixList(Map<String, Object> map);
}
