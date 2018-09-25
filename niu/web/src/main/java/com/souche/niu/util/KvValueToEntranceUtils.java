package com.souche.niu.util;

import com.alibaba.fastjson.JSON;
import com.souche.niu.bean.FieldRecord;
import com.souche.niu.bean.KvValue;
import com.souche.niu.model.EntranceDto;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/19
 **/
public class KvValueToEntranceUtils {

    public static KvValue getKvValueByEntrance(EntranceDto dto) {
        if (dto == null) {
            return null;
        }
        KvValue kvValue=new KvValue();
        kvValue.setId(dto.getId());
        kvValue.setCreatedAt(dto.getCreatedAt());
        kvValue.setUpdatedAt(dto.getUpdatedAt());
        kvValue.setGroupId(dto.getGroupId());

        List<FieldRecord> value = new ArrayList<>();
        if (StringUtil.isNotEmpty(dto.getProtocol())) {
            FieldRecord title=new FieldRecord();
            title.setKey("allutilprotocol");
            title.setValue(dto.getProtocol());
            title.setDesc("所有工具页面 打开协议");
            title.setType("0");
            value.add(title);
        }
        if (StringUtil.isNotEmpty(dto.getTitle())) {
            FieldRecord protocol=new FieldRecord();
            protocol.setKey("allutilname");
            protocol.setValue(dto.getTitle());
            protocol.setDesc("所有工具页面 链接名称");
            protocol.setType("0");
            value.add(protocol);
        }
        kvValue.setValue(JSON.toJSONString(value));
        return kvValue;
    }

    public static EntranceDto getByKvValue(KvValue kvValue) {
        if (kvValue == null) {
            return null;
        }
        EntranceDto dto = new EntranceDto();
        dto.setId(kvValue.getId());
        dto.setGroupId(kvValue.getGroupId());
        dto.setCreatedAt(kvValue.getCreatedAt());
        dto.setUpdatedAt(kvValue.getUpdatedAt());
        List<FieldRecord> list=null;
        if (StringUtil.isNotEmpty(kvValue.getValue())) {
            list = JSON.parseArray(kvValue.getValue(), FieldRecord.class);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            for (FieldRecord record : list) {
                if (record.getKey().equals("allutilname")) {
                    dto.setTitle(record.getValue());
                    continue;
                }
                if (record.getKey().equals("allutilprotocol")) {
                    dto.setProtocol(record.getValue());
                    continue;
                }
            }
        }
        return dto;
    }
}
