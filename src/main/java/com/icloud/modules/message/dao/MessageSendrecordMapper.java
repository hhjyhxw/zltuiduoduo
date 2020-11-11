package com.icloud.modules.message.dao;

import com.icloud.modules.message.entity.MessageSendrecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:38
 */
public interface MessageSendrecordMapper extends BaseMapper<MessageSendrecord> {

	List<MessageSendrecord> queryMixList(Map<String, Object> map);
}
