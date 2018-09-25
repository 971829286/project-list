package cn.ourwill.huiyizhan.entity;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import static cn.ourwill.huiyizhan.entity.Config.bucketDomain;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-12 10:37
 **/
@Data
public class Image {
    private Integer id;
    private String  pic;
    private Integer type;

    private String picUrl;

    public String getPicUrl() {
        if (StringUtils.isNotEmpty(this.pic) && this.pic.indexOf("http") < 0) {
            return bucketDomain + this.pic;
        }
        return this.pic;
    }
}
