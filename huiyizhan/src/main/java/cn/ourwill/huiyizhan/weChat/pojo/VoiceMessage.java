package cn.ourwill.huiyizhan.weChat.pojo;

/**
 * @param user
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:响应消息之语音消息
 * @Date: Created in 2018/4/26
 *
 *
 */
public class VoiceMessage extends BaseMessage {
    private Voice Voice;

    public Voice getVoice() {
        return Voice;
    }

    public void setVoice(Voice voice) {
        Voice = voice;
    }
}
