package com.icloud.modules.zltdd.dao;

import com.icloud.modules.zltdd.entity.ZltddRecommend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 推广用户表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
public interface ZltddRecommendMapper extends BaseMapper<ZltddRecommend> {

	List<ZltddRecommend> queryMixList(Map<String,Object> map);
}
