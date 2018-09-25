package cn.ourwill.huiyizhan.weChat.pojo;

/**
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:响应消息之视频消息
 * @Date: Created in 2018/4/26
 *
 */
public class VideoMessage extends BaseMessage {
    private Video Video;

    public Video getVideo() {
        return Video;
    }

    public void setVideo(Video video) {
        Video = video;
    }
}
