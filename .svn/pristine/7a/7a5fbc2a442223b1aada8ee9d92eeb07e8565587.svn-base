package com.icloud.modules.message.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.message.entity.MessageTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.message.dao.MessageTemplateMapper;
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
public class MessageTemplateService extends BaseServiceImpl<MessageTemplateMapper,MessageTemplate> {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public PageUtils<MessageTemplate> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<MessageTemplate> list = messageTemplateMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<MessageTemplate> pageInfo = new PageInfo<MessageTemplate>(list);
        PageUtils<MessageTemplate> page = new PageUtils<MessageTemplate>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

