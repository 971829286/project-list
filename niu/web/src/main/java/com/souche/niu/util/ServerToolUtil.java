package com.souche.niu.util;

import com.alibaba.fastjson.JSON;
import com.souche.niu.bean.FieldRecord;
import com.souche.niu.bean.KvValue;
import com.souche.niu.model.ServerToolDto;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XuJinNiu
 * @since 2018-09-14
 */
public class ServerToolUtil {
    private static final Logger logger = LoggerFactory.getLogger(KvValueToOpenScreenUtils.class);

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static KvValue getKvValueByServerTool(ServerToolDto serverToolDto){
        if(serverToolDto == null) {
             return null;
        }
        KvValue kvValue = new KvValue();
        kvValue.setId(serverToolDto.getId());
        kvValue.setCreatedAt(serverToolDto.getCreatedAt());
        kvValue.setUpdatedAt(serverToolDto.getUpdatedAt());
        kvValue.setGroupId(serverToolDto.getGroupId());
        List<FieldRecord> value = new ArrayList<>();
        if(StringUtil.isNotEmpty(serverToolDto.getTitle())){
            FieldRecord title=new FieldRecord();
            title.setKey("title");
            title.setValue(serverToolDto.getTitle());
            title.setDesc("标题");
            title.setType("0");
            value.add(title);
        }
        if(StringUtil.isNotEmpty(serverToolDto.getProtocol())){
            FieldRecord protocol=new FieldRecord();
            protocol.setKey("protocol");
            protocol.setValue(serverToolDto.getProtocol());
            protocol.setDesc("要打开的协议地址");
            protocol.setType("0");
            value.add(protocol);
        }
        if(StringUtil.isNotEmpty(serverToolDto.getUrl2X())){
            FieldRecord url2X = new FieldRecord();
            url2X.setKey("image_2x");
            url2X.setValue(serverToolDto.getUrl2X());
            url2X.setDesc("图标 2倍精细度");
            url2X.setType("0");
            value.add(url2X);
        }
        if(StringUtil.isNotEmpty(serverToolDto.getUrl3X())){
            FieldRecord url3X = new FieldRecord();
            url3X.setKey("image_3x");
            url3X.setValue(serverToolDto.getUrl3X());
            url3X.setDesc("图标 3倍精细度");
            url3X.setType("0");
            value.add(url3X);
        }

        if(serverToolDto.getFirstShow() != null){
            FieldRecord firstShow = new FieldRecord();
            firstShow.setKey("firstShow");
            firstShow.setValue(serverToolDto.getFirstShow().toString());
            firstShow.setDesc("是否在首页显示");
            firstShow.setType("0");
            value.add(firstShow);
        }
        if(StringUtil.isNotEmpty(serverToolDto.getOrderNum())){
            FieldRecord orderNum = new FieldRecord();
            orderNum.setKey("orderNum");
            orderNum.setValue(serverToolDto.getOrderNum());
            orderNum.setDesc("排序值 1-1000 数字越大越靠前");
            orderNum.setType("0");
            value.add(orderNum);
        }

        if(StringUtil.isNotEmpty(serverToolDto.getClickPoint())){
            FieldRecord orderNum = new FieldRecord();
            orderNum.setKey("eventKey");
            orderNum.setValue(serverToolDto.getClickPoint());
            orderNum.setDesc("点击事件打点key");
            orderNum.setType("0");
            value.add(orderNum);
        }
        kvValue.setValue(JsonUtils.toJson(value));
        return kvValue;
    }


    public static ServerToolDto getByKvValue(KvValue kvValue) {
        if (kvValue == null) {
            return null;
        }
        ServerToolDto dto = new ServerToolDto();
        dto.setId(kvValue.getId());
        dto.setGroupId(kvValue.getGroupId());
        dto.setCreatedAt(kvValue.getCreatedAt());
        List<FieldRecord> list=null;
        if (StringUtil.isNotEmpty(kvValue.getValue())) {
            list = JSON.parseArray(kvValue.getValue(), FieldRecord.class);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            for (FieldRecord record : list) {
                if (record.getKey().equals("title")) {
                    dto.setTitle(record.getValue());
                }else if (record.getKey().equals("protocol")) {
                    dto.setProtocol(record.getValue());
                }else if (record.getKey().equals("image_2x")) {
                    dto.setUrl2X(record.getValue());
                }else if (record.getKey().equals("image_3x")) {
                    dto.setUrl3X(record.getValue());
                }else if (record.getKey().equals("eventKey")) {
                    dto.setClickPoint(record.getValue());
                }else if (record.getKey().equals("firstShow")) {
                    dto.setFirstShow(Boolean.getBoolean(record.getValue()));
                }else if (record.getKey().equals("orderNum")) {
                    dto.setOrderNum(record.getValue());
                }
            }
        }
        return dto;
    }
    public static void main(String[] args) {
        String s1 = "200";
        String s2 = "21";
        System.out.println(s1.compareTo(s2));
    }
}
