package cn.ourwill.tuwenzb.weixin.pojo;

import lombok.Data;

@Data
public class MessageData {

    private MessageDataList first;

    private MessageDataList keyword1;

    private MessageDataList keyword2;

    private MessageDataList keyword3;

    private MessageDataList keyword4;

    private MessageDataList keyword5;

    private MessageDataList remark;

}
