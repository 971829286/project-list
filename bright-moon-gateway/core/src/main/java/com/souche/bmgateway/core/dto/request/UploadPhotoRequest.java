package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 图片上传必要参数
 *
 * @author chenwj
 * @since 2018/8/21
 */
@Getter
@Setter
@ToString(exclude = "picture")
public class UploadPhotoRequest extends CommonRequest {

    /**
     * 图片类型
     */
    private String photoType;

    /**
     * 外部交易号
     */
    private String outTradeNo;

    /**
     * 图片
     */
    private String picture;

    /**
     * 文件名
     */
    private String pictureName;

}
