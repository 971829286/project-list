package com.souche.niu.util;

import com.alibaba.fastjson.JSON;
import com.souche.niu.bean.FieldRecord;
import com.souche.niu.bean.KvValue;
import com.souche.niu.model.OpenScreenDto;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public class KvValueToOpenScreenUtils {

    private static final Logger logger = LoggerFactory.getLogger(KvValueToOpenScreenUtils.class);

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static KvValue getKvValueByOpenScreen(OpenScreenDto openScreenDto) {
        if (openScreenDto == null) {
            return null;
        }
        KvValue kvValue=new KvValue();
        kvValue.setId(openScreenDto.getId());
        kvValue.setCreatedAt(openScreenDto.getCreatedAt());
        kvValue.setUpdatedAt(openScreenDto.getUpdatedAt());
        kvValue.setGroupId(openScreenDto.getGroupId());

        List<FieldRecord> value = new ArrayList<>();
        if (StringUtil.isNotEmpty(openScreenDto.getTitle())) {
            FieldRecord title=new FieldRecord();
            title.setKey("name");
            title.setValue(openScreenDto.getTitle());
            title.setDesc("标题");
            title.setType("0");
            value.add(title);
        }
        if (StringUtil.isNotEmpty(openScreenDto.getProtocol())) {
            FieldRecord protocol=new FieldRecord();
            protocol.setKey("protocol");
            protocol.setValue(openScreenDto.getProtocol());
            protocol.setDesc("跳转协议地址");
            protocol.setType("0");
            value.add(protocol);
        }
        if (StringUtil.isNotEmpty(openScreenDto.getUrl())) {
            FieldRecord url = new FieldRecord();
            url.setKey("image");
            url.setValue(openScreenDto.getUrl());
            url.setDesc("图片");
            url.setType("0");
            value.add(url);
        }
        if (StringUtil.isNotEmpty(openScreenDto.getTargetUser())) {
            FieldRecord target = new FieldRecord();
            target.setKey("target");
            target.setValue(openScreenDto.getTargetUser());
            target.setDesc("目标用户，用逗号隔开，不填默认所有用户");
            target.setType("0");
            value.add(target);
        }
        if (StringUtil.isNotEmpty(openScreenDto.getStartTime())) {
            FieldRecord start = new FieldRecord();
            start.setKey("expiry_start");
            start.setValue(openScreenDto.getStartTime());
            start.setDesc("有效期开始时间 2014-10-01 11:11格式");
            start.setType("0");
            value.add(start);
        }
        if (StringUtil.isNotEmpty(openScreenDto.getEndTime())) {
            FieldRecord end = new FieldRecord();
            end.setKey("expiry_end");
            end.setValue(openScreenDto.getEndTime());
            end.setDesc("有限期截止时间 格式同有效期开始时间");
            end.setType("0");
            value.add(end);
        }
        kvValue.setValue(JSON.toJSONString(value));
        return kvValue;
    }

    public static OpenScreenDto getByKvValue(KvValue kvValue) {
        if (kvValue == null) {
            return null;
        }
        OpenScreenDto dto = new OpenScreenDto();
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
        dto.setStatus(findStatus(dto.getStartTime(),dto.getEndTime()));
        return dto;
    }

    public static String findStatus(String startTime, String endTime) {
        if (StringUtil.isEmpty(startTime)) {
            return "1";
        }
        if (StringUtil.isEmpty(endTime)) {
            return "1";
        }
        Date now = new Date();
        try {
            Date start = df.parse(startTime);
            Date end = df.parse(endTime);
            if (now.getTime() < start.getTime()) {
                return "3";//未生效
            }
            if (now.getTime() >= start.getTime() && now.getTime() <= end.getTime()) {
                return "2";//生效
            }
            if (now.getTime() > end.getTime()) {
                return "4";//已过期
            }
            return "1";
        } catch (Exception e) {
            logger.info("计算有效期状态失败 {}",e.toString());
            return "1";
        }
    }

    public static void main(String[] args) throws Exception{
        String json="[{\"key\":\"name\",\"value\":\"车易拍\",\"desc\":\"标题\",\"type\":\"0\"},\n" +
                " {\"key\":\"expiry_start\",\"value\":\"2018-08-14 00:00:00\",\"desc\":\"有效期开始 2014-01-01 00:00:00格式\",\"type\":\"0\"},\n" +
                " {\"key\":\"expiry_end\",\"value\":\"2018-08-24 00:00:00\",\"desc\":\"有效期截止 同上\",\"type\":\"0\"},\n" +
                " {\"key\":\"protocol\",\"value\":\"http://m.cheyipai.com/static/cheniu/active/index.html\",\"desc\":\"链接地址\",\"type\":\"0\"},\n" +
                " {\"key\":\"target\",\"value\":\"13107716356\",\"desc\":\"目标用户，用逗号隔开，不填默认所有用户\",\"type\":\"0\"},\n" +
                " {\"key\":\"image\",\"value\":\"http://img.souche.com/d8f515cd6e59e6ad0aa53c3c5f6bd759.png\",\"desc\":\"图片\",\"type\":\"1\"}]";
        List<FieldRecord> list = JSON.parseArray(json, FieldRecord.class);
        System.out.println(list.size());

        String end = "2018-10-20 16:26";
        Date now=new Date();
        System.out.println("now:"+now.getTime());
        System.out.println("end:"+df.parse(end).getTime());
    }

}
