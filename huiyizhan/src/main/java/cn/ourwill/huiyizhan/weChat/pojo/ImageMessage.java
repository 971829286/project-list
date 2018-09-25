package cn.ourwill.huiyizhan.weChat.pojo;

/**
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:响应消息之图片消息
 * @Date: Created in 2018/4/26
 *
 */
public class ImageMessage extends BaseMessage {
    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        Image = image;
    }

}
