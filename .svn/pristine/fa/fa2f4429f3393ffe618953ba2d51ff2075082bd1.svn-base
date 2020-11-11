package com.icloud.modules.message.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.message.entity.MessageSendrecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.message.dao.MessageSendrecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-11 16:06:38
 */
@Service
@Transactional
public class MessageSendrecordService extends BaseServiceImpl<MessageSendrecordMapper,MessageSendrecord> {

    @Autowired
    private MessageSendrecordMapper messageSendrecordMapper;

    @Override
    public PageUtils<MessageSendrecord> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<MessageSendrecord> list = messageSendrecordMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<MessageSendrecord> pageInfo = new PageInfo<MessageSendrecord>(list);
        PageUtils<MessageSendrecord> page = new PageUtils<MessageSendrecord>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

