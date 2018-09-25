package com.souche.niu.util;

import com.alibaba.fastjson.JSON;
import com.souche.niu.bean.FieldRecord;
import com.souche.niu.bean.KvValue;
import com.souche.niu.model.ActivityScreenDto;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description：活动浮窗与KvValue转换的工具类
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public class KvValueToActivityScreenUtils {


    public static KvValue getKvValueByOpenScreen(ActivityScreenDto activityScreenDto) {
        if (activityScreenDto == null) {
            return null;
        }
        KvValue kvValue=new KvValue();
        kvValue.setId(activityScreenDto.getId());
        kvValue.setCreatedAt(activityScreenDto.getCreatedAt());
        kvValue.setUpdatedAt(activityScreenDto.getUpdatedAt());
        kvValue.setGroupId(activityScreenDto.getGroupId());

        List<FieldRecord> value = new ArrayList<>();
        if (StringUtil.isNotEmpty(activityScreenDto.getTitle())) {
            FieldRecord title=new FieldRecord();
            title.setKey("name");
            title.setValue(activityScreenDto.getTitle());
            title.setDesc("标题");
            title.setType("0");
            value.add(title);
        }
        if (StringUtil.isNotEmpty(activityScreenDto.getProtocol())) {
            FieldRecord protocol=new FieldRecord();
            protocol.setKey("protocol");
            protocol.setValue(activityScreenDto.getProtocol());
            protocol.setDesc("跳转协议地址");
            protocol.setType("0");
            value.add(protocol);
        }
        if (StringUtil.isNotEmpty(activityScreenDto.getUrl())) {
            FieldRecord url = new FieldRecord();
            url.setKey("image");
            url.setValue(activityScreenDto.getUrl());
            url.setDesc("图片");
            url.setType("0");
            value.add(url);
        }
        if (StringUtil.isNotEmpty(activityScreenDto.getTargetUser())) {
            FieldRecord target = new FieldRecord();
            target.setKey("target");
            target.setValue(activityScreenDto.getTargetUser());
            target.setDesc("目标用户，用逗号隔开，不填默认所有用户");
            target.setType("0");
            value.add(target);
        }
        if (StringUtil.isNotEmpty(activityScreenDto.getStartTime())) {
            FieldRecord start = new FieldRecord();
            start.setKey("expiry_start");
            start.setValue(activityScreenDto.getStartTime());
            start.setDesc("有效期开始时间 2014-10-01 11:11格式");
            start.setType("0");
            value.add(start);
        }
        if (StringUtil.isNotEmpty(activityScreenDto.getEndTime())) {
            FieldRecord end = new FieldRecord();
            end.setKey("expiry_end");
            end.setValue(activityScreenDto.getEndTime());
            end.setDesc("有限期截止时间 格式同有效期开始时间");
            end.setType("0");
            value.add(end);
        }
        kvValue.setValue(JSON.toJSONString(value));
        return kvValue;
    }

    public static ActivityScreenDto getByKvValue(KvValue kvValue) {
        if (kvValue == null) {
            return null;
        }
        ActivityScreenDto dto = new ActivityScreenDto();
        dto.setId(kvValue.getId());
        dto.setGroupId(kvValue.getGroupId());
        dto.setCreatedAt(kvValue.getCreatedAt());
        List<FieldRecord> list=null;
        if (StringUtil.isNotEmpty(kvValue.getValue())) {
            list = JSON.parseArray(kvValue.getValue(), FieldRecord.class);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            for (FieldRecord record : list) {
                if (record.getKey().equals("name")) {
                    dto.setTitle(record.getValue());
                    continue;
                }
                if (record.getKey().equals("protocol")) {
                    dto.setProtocol(record.getValue());
                    continue;
                }
                if (record.getKey().equals("image")) {
                    dto.setUrl(record.getValue());
                    continue;
                }
                if (record.getKey().equals("target")) {
                    dto.setTargetUser(record.getValue());
                    continue;
                }
                if (record.getKey().equals("expiry_start")) {
                    dto.setStartTime(record.getValue());
                    continue;
                }
                if (record.getKey().equals("expiry_end")) {
                    dto.setEndTime(record.getValue());
                    continue;
                }
            }
        }
        dto.setStatus(KvValueToOpenScreenUtils.findStatus(dto.getStartTime(),dto.getEndTime()));
        return dto;
    }
}
