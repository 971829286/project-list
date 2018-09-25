package cn.ourwill.willcenter.weixin.pojo;

import lombok.Data;

@Data
public class MessageData {

    private MessageDataList first;

    private MessageDataList keyword1;

    private MessageDataList keyword2;

    private MessageDataList keyword3;

    private MessageDataList remark;

}
