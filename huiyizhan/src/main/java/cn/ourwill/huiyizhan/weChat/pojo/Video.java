package cn.ourwill.huiyizhan.weChat.pojo;

/**
 * @param user
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:
 * @Date: Created in 2018/4/26
 */
public class Video {

    private String MediaId;
    private String Title;
    private String Description;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
