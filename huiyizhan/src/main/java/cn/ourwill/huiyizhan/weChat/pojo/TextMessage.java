package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:文本消息
 * @Date: Created in 2018/4/26
 */
public class TextMessage extends BaseMessage {

    /**
     *  回复的消息内容
     */
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
