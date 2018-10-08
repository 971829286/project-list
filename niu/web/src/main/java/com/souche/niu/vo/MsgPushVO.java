package com.souche.niu.vo;

/**
 * @program: niu
 * @ClassName: MsgPushVO
 * @description: 消息推送字段封装对象
 * @author: malin
 * @create: 2018-09-26 21:52
 **/

public class MsgPushVO {

    private String msgPushUser;

    private String msgPushPass;

    private String msgPushUrl;

    private String msgPushSound;

    private String msgPushChannel;

    private String msgPushType;

    private String msgPushMode;

    private String msgPushCardType;

    private String msgPushProtocol;

    public MsgPushVO() {
    }

    public MsgPushVO(String msgPushUser, String msgPushPass, String msgPushUrl, String msgPushSound,
                     String msgPushChannel, String msgPushType, String msgPushMode, String msgPushCardType,
                     String msgPushProtocol) {
        this.msgPushUser = msgPushUser;
        this.msgPushPass = msgPushPass;
        this.msgPushUrl = msgPushUrl;
        this.msgPushSound = msgPushSound;
        this.msgPushChannel = msgPushChannel;
        this.msgPushType = msgPushType;
        this.msgPushMode = msgPushMode;
        this.msgPushCardType = msgPushCardType;
        this.msgPushProtocol = msgPushProtocol;
    }

    public String getMsgPushUser() {
        return msgPushUser;
    }

    public void setMsgPushUser(String msgPushUser) {
        this.msgPushUser = msgPushUser;
    }

    public String getMsgPushPass() {
        return msgPushPass;
    }

    public void setMsgPushPass(String msgPushPass) {
        this.msgPushPass = msgPushPass;
    }

    public String getMsgPushUrl() {
        return msgPushUrl;
    }

    public void setMsgPushUrl(String msgPushUrl) {
        this.msgPushUrl = msgPushUrl;
    }

    public String getMsgPushSound() {
        return msgPushSound;
    }

    public void setMsgPushSound(String msgPushSound) {
        this.msgPushSound = msgPushSound;
    }

    public String getMsgPushChannel() {
        return msgPushChannel;
    }

    public void setMsgPushChannel(String msgPushChannel) {
        this.msgPushChannel = msgPushChannel;
    }

    public String getMsgPushType() {
        return msgPushType;
    }

    public void setMsgPushType(String msgPushType) {
        this.msgPushType = msgPushType;
    }

    public String getMsgPushMode() {
        return msgPushMode;
    }

    public void setMsgPushMode(String msgPushMode) {
        this.msgPushMode = msgPushMode;
    }

    public String getMsgPushCardType() {
        return msgPushCardType;
    }

    public void setMsgPushCardType(String msgPushCardType) {
        this.msgPushCardType = msgPushCardType;
    }

    public String getMsgPushProtocol() {
        return msgPushProtocol;
    }

    public void setMsgPushProtocol(String msgPushProtocol) {
        this.msgPushProtocol = msgPushProtocol;
    }

    @Override
    public String toString() {
        return "MsgPushVO{" +
                "msgPushUser='" + msgPushUser + '\'' +
                ", msgPushPass='" + msgPushPass + '\'' +
                ", msgPushUrl='" + msgPushUrl + '\'' +
                ", msgPushSound='" + msgPushSound + '\'' +
                ", msgPushChannel='" + msgPushChannel + '\'' +
                ", msgPushType='" + msgPushType + '\'' +
                ", msgPushMode='" + msgPushMode + '\'' +
                ", msgPushCardType='" + msgPushCardType + '\'' +
                ", msgPushProtocol='" + msgPushProtocol + '\'' +
                '}';
    }
}
