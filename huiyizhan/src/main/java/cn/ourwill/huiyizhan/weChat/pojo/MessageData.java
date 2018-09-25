package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;
/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:34
 * @Version1.0
 */
@Data
public class MessageData {

    private MessageDataList first;

    private MessageDataList keyword1;

    private MessageDataList keyword2;

    private MessageDataList keyword3;

    private MessageDataList remark;

}
